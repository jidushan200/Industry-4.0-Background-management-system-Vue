<template>
  <Modal v-model="toolBladeModalshow" class="modal-class" title="新刀入库" width="930" draggable>
    <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="100" inline>
      <Row>
        <Col span="8">
        <FormItem label="物料编码" prop="toolNumber">
          <Input style="width:200px" v-model="formValidate.toolNumber" @keyup.enter.native="searchTool()" placeholder="输入物料编码">
          <Icon type="ios-search" slot="suffix" />
          </Input>
        </FormItem>
        </Col>
        <Col span="8">
        <FormItem label="物料名称" prop="toolName">
          <Input v-model="toolName" readonly style="width:200px;" />
        </FormItem>
        </Col>
        <Col span="8">
        <FormItem label="物料图号" prop="toolMap">
          <Input v-model="toolMap" readonly style="width:200px;" />
        </FormItem>
        </Col>
      </Row>
      <Row>
        <Col span="8">
        <FormItem label="刀具数量" prop="toolQty">
          <InputNumber style="width:200px" :max="100" :min="1" :step="1" v-model="formValidate.toolQty" placeholder="输入刀具数量" />
        </FormItem>
        </Col>
        <Col span="8">
        <FormItem label="供应商" prop="supplierId">
          <Select style="width:200px" v-model="formValidate.supplierId" ref="supplierId" @on-change="supplierSelectChange" clearable filterable>
            <Option v-for="item in supplierList" :value="item.pkId" :key="item.pkId">{{ item.supplierName }}</Option>
          </Select>
        </FormItem>
        </Col>
        <Col span="8">
        <FormItem label="送货人" prop="deliever">
          <Input v-model="formValidate.deliever" style="width:200px;" />
        </FormItem>
        </Col>
      </Row>
      <Row>
        <Col span="8">
        <FormItem label="库管员" prop="staffInfo">
          <Input v-model="staffInfo" readonly style="width:200px;" />
        </FormItem>
        </Col>
        <Col span="8">
        <FormItem label="库位" prop="warehouse">
          <Input v-model="formValidate.warehouse" style="width:200px;" />
        </FormItem>
        </Col>
      </Row>
      <Row>
        <Col span="24">
        <FormItem label="刀具说明" prop="staffInfo">
          <Input v-model="formValidate.remark" style="width:790px;" />
        </FormItem>
        </Col>
      </Row>
    </Form>
    <Table :columns="toolTableColumns" :data="toolPartList" border stripe highlight-row></Table>
    <div slot="footer">
      <Button @click="handleSubmit()" type="primary" :disabled="isDisabled">提交</Button>
      <Button @click="closeModal()" style="margin-left: 8px">关闭</Button>
    </div>
  </Modal>
</template>
<script>
import { dateFormat, objCopy } from "@/libs/tools.js";
import { getToken, getBaseUrl } from "@/libs/util";
import axios from "@/libs/api.request";
import qs from "qs";
import tool_blade_form_config from "./tool-blade-form-config.js";
export default {
  data() {
    return {
      isDisabled: false,
      toolBladeModalshow: false,
      toolNumber: "",
      toolName: "",
      toolMap: "",
      staffInfo: "",
      supplierList: [],
      toolPartList: [],
      toolTableColumns: [
        {
          title: "制件编码",
          key: "partCode"
        },
        {
          title: "制件名称",
          key: "partName"
        }
      ],
      formValidate: tool_blade_form_config.formValidate, // form表单字段
      ruleValidate: tool_blade_form_config.ruleValidate // form表单验证规则
    };
  },
  created() {
    const self = this;
    self.getCurrentStaff();
    self.getSupplierList();
  },
  init() {
    const self = this;
  },
  methods: {
    showModal(row) {
      const self = this;
      self.toolBladeModalshow = true;
      self.formValidate = {};
      self.$refs["formValidate"].resetFields();
      self.formValidate.toolQty = 1;
    },
    searchTool() {
      const self = this;
      if (!!!self.formValidate.toolNumber) {
        return;
      } else {
        let para = {
          toolNumber: self.formValidate.toolNumber
        };
        axios
          .request({
            headers: {
              "Content-Type": "application/json; charset=UTF-8"
            },
            url: "/tool/tool-get-by-tool-number.json",
            method: "post",
            isAuth: true,
            params: para
          })
          .then(function(res) {
            //self.reportDetailModalshow = false;
            const data = res.data;
            if (data.code === 200) {
              if (data.data === null || data.data === undefined) {
                self.$Notice.error({
                  title: "错误提示",
                  desc:
                    "无法找到物料编码为：" +
                    self.formValidate.toolNumber +
                    "相关的物料信息"
                });
              } else {
                if (!!data.data.toolMap) {
                  self.toolMap = data.data.toolMap;
                  self.toolName = data.data.toolName;
                  self.toolPartList = data.data.toolPartList;
                } else {
                  self.formValidate.toolNumber = "";
                  self.$Notice.error({
                    title: "错误提示",
                    desc: "请维护物料图号后再进行入库操作"
                  });
                }
              }
            } else {
              self.$Notice.error({
                title: "错误提示",
                desc: res.data.info
              });
            }
          })
          .catch(function(err) {
            console.log(err);
          });
      }
    },

    supplierSelectChange(item) {
      let supplierList = this.supplierList;
      if (item !== undefined) {
        for (const supplier of supplierList) {
          if (supplier.pkId === item) {
            //  this.formValidate.supplierCode = supplier.supplierCode;
            this.formValidate.supplierName = supplier.supplierName;
          }
        }
      }
    },

    getSupplierList() {
      const self = this;
      let para = {
        isNewTool: 1
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/base/supplier-list.json",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            self.supplierList = data.data;
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },
    getCurrentStaff() {
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/base/staff-get-current",
          isAuth: true,
          method: "post"
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200 && data.data !== null) {
            self.staffInfo =
              data.data.departmentName + "-" + data.data.staffName;
            self.departmentId = data.data.departmentId;
            self.departmentName = data.data.departmentName;
          }
        });
    },

    //保存
    handleSubmit() {
      this.isDisabled = true;
      const self = this;
      self.$refs["formValidate"].validate(valid => {
        if (valid) {
          let para = {
            toolNumber: self.formValidate.toolNumber,
            toolName: self.toolName,
            toolMap: self.toolMap,
            toolQty: self.formValidate.toolQty,
            supplierId: self.formValidate.supplierId,
            supplierName: self.formValidate.supplierName,
            deliever: self.formValidate.deliever,
            departmentId: self.departmentId,
            departmentName: self.departmentName,
            warehouse: self.formValidate.warehouse,
            warehouseType: 1,
            remark: self.formValidate.remark
          };
          axios
            .request({
              headers: {
                "Content-Type": "application/json; charset=UTF-8"
              },
              url: "/tool/blade-warehouse.json",
              method: "post",
              isAuth: true,
              params: para
            })
            .then(function(res) {
              self.toolBladeModalshow = false;
              if (res.data.code === 200) {
                self.$Message.success("刀头已入库!");
                self.isDisabled = false;
                self.$emit("search");
              } else {
                self.$Notice.error({
                  title: "错误提示",
                  desc: res.data.info
                });
                self.isDisabled = false;
              }
            })
            .catch(function(err) {
              console.log(err);
            });
        } else {
          self.$Message.error("请输入正确信息!");
        }
      });
    },

    //关闭弹窗
    closeModal() {
      const self = this;
      self.toolBladeModalshow = false;
    }
  },
  mounted() {}
};
</script>