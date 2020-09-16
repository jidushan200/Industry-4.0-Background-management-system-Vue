<template>
  <Modal v-model="toolHeadModalshow" class="modal-class" title="新刀入库" width="930" draggable>
    <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="100" inline>
      <Row>
        <Col span="8">
        <FormItem label="物料编码" prop="toolNumber">
          <Input style="width:200px" v-model="formValidate.toolNumber" @on-blur="searchTool()" placeholder="输入物料编码">
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
        <FormItem label="顺序号" prop="warehouseSeq">
          <Input style="width:200px" v-model="formValidate.warehouseSeq" placeholder="输入流水号" @on-change="searchFullNumber()" @on-blur="searchSupplier()"></Input>
        </FormItem>
        </Col>
        <Col span="8">
        <FormItem label="刀具条码" prop="fullNumber">
          <Input v-model="fullNumber" readonly style="width:200px;" />
        </FormItem>
        </Col>
        <Col span="8">
        <FormItem label="供应商" prop="supplierName">
          <Input v-model="supplierName" readonly style="width:200px;" />
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
        <Col>
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
import tool_head_form_config from "./tool-head-form-config.js";
export default {
  data() {
    return {
      isDisabled: false,
      toolHeadModalshow: false,
      toolNumber: "",
      toolName: "",
      toolMap: "",
      fullNumber: "",
      staffInfo: "",
      supplierName: "",
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
      formValidate: tool_head_form_config.formValidate, // form表单字段
      ruleValidate: tool_head_form_config.ruleValidate // form表单验证规则
    };
  },
  created() {
    const self = this;
    self.getCurrentStaff();
  },
  init() {
    const self = this;
  },
  methods: {
    showModal(row) {
      const self = this;
      self.toolHeadModalshow = true;
      self.formValidate = {};
      self.$refs["formValidate"].resetFields();
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
                  self.warehouseNumber = data.data.toolNumber;
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
    searchFullNumber() {
      const self = this;
      if (!!!self.warehouseNumber) {
        return;
      } else {
        let warehouseNumber = self.warehouseNumber;
        let warehouseSeq = self.formValidate.warehouseSeq;
        if (warehouseSeq !== null && warehouseSeq !== undefined) {
          self.formValidate.warehouseCode =
            warehouseNumber + "-" + warehouseSeq;
        } else {
          self.$Notice.error({
            title: "错误提示",
            desc: "无法找到物料序号，请补充完整入库编码"
          });
        }
        let toolMap = self.toolMap;
        if (toolMap !== null && toolMap !== undefined) {
          self.fullNumber = self.formValidate.warehouseCode + "/" + toolMap;
          self.formValidate.fullNumber =
            self.formValidate.warehouseCode + "/" + toolMap;
        } else {
          self.$Notice.error({
            title: "错误提示",
            desc: "无法找到物料图号，生成物料条码失败"
          });
        }
      }
    },
    searchSupplier() {
      const self = this;
      if (!!!self.formValidate.toolNumber) {
        return;
      }
      if (!!!self.formValidate.warehouseSeq) {
        return;
      } else {
        let para = {
          toolNumber: self.formValidate.toolNumber,
          toolSeq: self.formValidate.warehouseSeq
        };
        axios
          .request({
            headers: {
              "Content-Type": "application/json; charset=UTF-8"
            },
            url: "/tool/tool-check-get-by-seq.json",
            method: "post",
            isAuth: true,
            params: para
          })
          .then(function(res) {
            const data = res.data;
            if (data.code === 200) {
              if (data.data === null || data.data === undefined) {
                self.$Notice.error({
                  title: "错误提示",
                  desc:
                    "无法找到物料编码为：" +
                    self.formValidate.toolNumber +
                    "的新刀质检信息"
                });
              } else {
                self.supplierId = data.data.supplierId;
                self.supplierName = data.data.supplierName;
              }
            } else {
              self.$Notice.error({
                title: "错误提示",
                desc:
                  "无法找到入库流水号为：" +
                  self.formValidate.warehouseSeq +
                  "的新刀质检信息"
              });
            }
          })
          .catch(function(err) {
            console.log(err);
          });
      }
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
      const self = this;
      self.$refs["formValidate"].validate(valid => {
        if (valid) {
          this.isDisabled = true;
          let para = {
            toolNumber: self.formValidate.toolNumber,
            toolName: self.toolName,
            toolMap: self.toolMap,
            fullNumber: self.fullNumber,
            warehouseCode: self.formValidate.warehouseCode,
            supplierId: self.supplierId,
            supplierName: self.supplierName,
            departmentId: self.departmentId,
            departmentName: self.departmentName,
            warehouse: self.formValidate.warehouse,
            typeId: 3,
            warehouseSeq: self.formValidate.warehouseSeq,
            remark: self.formValidate.remark
          };
          axios
            .request({
              headers: {
                "Content-Type": "application/json; charset=UTF-8"
              },
              url: "/tool/tool-warehouse.json",
              method: "post",
              isAuth: true,
              params: para
            })
            .then(function(res) {
              self.toolHeadModalshow = false;
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
      self.toolHeadModalshow = false;
    }
  },
  mounted() {}
};
</script>