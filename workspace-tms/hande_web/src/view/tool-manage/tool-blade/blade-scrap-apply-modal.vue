<template>
  <Modal v-model="bladeScrapModalshow" class="modal-class" title="刀条报废" width="930" draggable>
    <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="100" inline>
      <Row>
        <Col span="8">
        <FormItem label="刀盘组合码" prop="composeNumber">
          <Input style="width:200px" v-model="formValidate.composeNumber" @keyup.enter.native="searchTool()" placeholder="输入刀盘组合码">
          <Icon type="ios-search" slot="suffix" />
          </Input>
        </FormItem>
        </Col>
        <Col span="8">
        <FormItem label="刀盘编码" prop="headNumber">
          <Input v-model="headNumber" readonly style="width:200px;" />
        </FormItem>
        </Col>
        <Col span="8">
        <FormItem label="刀条编码" prop="toolNumber">
          <Input v-model="toolNumber" readonly style="width:200px;" />
        </FormItem>
        </Col>
      </Row>
      <Row>
        <Col span="8">
        <FormItem label="申请部门" prop="applyDepartment">
          <Input style="width:170px" v-model="applyDepartment" readonly />
        </FormItem>
        </Col>
        <Col span="8">
        <FormItem label="申请人" prop="applierCode">
          <Input style="width:170px" v-model="staffInfo" readonly></Input>
        </FormItem>
        </Col>
        <Col span="8">
        <FormItem label="已加工数量" prop="processQty">
          <Input style="width:170px" v-model="processQty" readonly />
        </FormItem>
        </Col>
      </Row>
      <Row>
        <Col span="8">
        <FormItem label="刀条名称" prop="toolName">
          <Input v-model="toolName" readonly style="width:200px;" />
        </FormItem>
        </Col>
        <Col span="8">
        <FormItem label="制件名称" prop="partName">
          <Input v-model="partName" readonly style="width:200px;" />
        </FormItem>
        </Col>
        <Col span="8">
        <FormItem label="刀具数量" prop="toolQty">
          <InputNumber style="width:200px" :max="100" :min="1" :step="1" v-model="formValidate.toolQty" placeholder="输入刀具数量" />
        </FormItem>
        </Col>
      </Row>
      <Row>
        <Col span="8">
        <FormItem label="报废原因" prop="scripResion">
          <Select style="width:170px" v-model="formValidate.scripResion" ref="scrapType" placeholder="请选择报废原因" :disabled="isAudit">
            <Option v-for="item in scrapType" :value="item.value" :key="item.value">{{ item.name }}</Option>
          </Select>
        </FormItem>
        </Col>
        <Col span="16">
        <FormItem label="原因说明" prop="scripRemark">
          <Input type="textarea" :rows="1" style="width:460px" v-model="formValidate.scripRemark" placeholder="输入原因说明" :readonly="isAudit" />
        </FormItem>
        </Col>
      </Row>
    </Form>
    <div slot="footer">
      <Button @click="handleSave('formValidate')" v-if="btnSave && applyAuth" type="primary" :disabled="isDisabled">保存</Button>
      <Button @click="handleSubmit('formValidate')" v-if="btnSubmit && applyAuth" type="warning" :disabled="isDisabled">提交</Button>
      <Button @click="handleAudit()" type="primary" v-if="btnAudit" :disabled="isDisabled">提交</Button>
      <Button @click="handleReset('formValidate')" style="margin-left: 8px">关闭</Button>
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
      bladeScrapModalshow: false,
      headNumber: "",
      toolNumber: "",
      toolName: "",
      toolMap: "",
      staffInfo: "",
      processQty: 0,
      scrapType: [
        {
          name: "正常报废",
          value: 1
        },
        {
          name: "异常报废",
          value: 2
        }
      ],
      formValidate: tool_blade_form_config.formValidate, // form表单字段
      ruleValidate: tool_blade_form_config.ruleValidate // form表单验证规则
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
      self.toolBladeModalshow = true;
      self.formValidate = {};
      self.$refs["formValidate"].resetFields();
      self.formValidate.toolQty = 1;
    },
    searchTool() {
      const self = this;
      if (!!!self.formValidate.composeNumber) {
        return;
      } else {
        let para = {
          composeNumber: self.formValidate.composeNumber
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
                    "无法找到刀条编码为：" +
                    self.formValidate.toolNumber +
                    "相关的刀条信息"
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
                    desc: "请维护刀条图号后再进行入库操作"
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
      self.bladeScrapModalshow = false;
    }
  },
  mounted() {}
};
</script>