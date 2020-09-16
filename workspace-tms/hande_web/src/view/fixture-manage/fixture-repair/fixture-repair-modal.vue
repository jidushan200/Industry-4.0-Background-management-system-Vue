<template>
  <Modal v-model="repairModalshow" class="modal-class" title="夹具修磨" width="700" draggable>
    <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="100" inline>
      <Row>
        <Col span="12">
        <FormItem label="夹具条码" prop="fixtureBarcode">
          <Input v-model="fixtureBarcode" readonly style="width:200px;" />
        </FormItem>
        </Col>
        <Col span="12">
        <FormItem label="夹具名称" prop="fixtureName">
          <Input v-model="fixtureName" readonly style="width:200px;" />
        </FormItem>
        </Col>
      </Row>
      <Row>
        <Col span="12">
        <FormItem label="修磨量" prop="repairMeasure">
          <InputNumber :min="0.01" :step="0.01" style="width:200px" v-model="formValidate.repairMeasure" placeholder="输入本次刃磨量" />
        </FormItem>
        </Col>
        <Col span="12">
        <FormItem label="记录人" prop="staffInfo">
          <Input v-model="staffInfo" readonly style="width:200px;" />
        </FormItem>
        </Col>
      </Row>
      <Row>
        <Col span="12">
        <FormItem label="备注" prop="remark">
          <Input v-model="formValidate.remark" style="width:560px;" />
        </FormItem>
        </Col>
      </Row>
    </Form>
    <div slot="footer">
      <!--  <Button @click="handleSubmit(0)" type="primary">保存</Button>-->
      <Button @click="handleSubmit(1)" type="primary" :disabled="isDisable">提交</Button>
      <Button @click="closeModal()" style="margin-left: 8px">关闭</Button>
    </div>
  </Modal>
</template>
<script>
import { dateFormat, objCopy } from "@/libs/tools.js";
import { getToken, getBaseUrl } from "@/libs/util";
import axios from "@/libs/api.request";
import qs from "qs";
import repair_form_config from "./repair-form-config.js";

export default {
  data() {
    return {
      isDisable: false,
      repairModalshow: false,
      fixtureName: "",
      fixtureBarcode: "",
      staffInfo: "",
      formValidate: repair_form_config.formValidate, // form表单字段
      ruleValidate: repair_form_config.ruleValidate // form表单验证规则
    };
  },
  created() {
    const self = this;
    self.getCurrentStaff();
  },
  methods: {
    showModal(row) {
      const self = this;
      self.repairModalshow = true;
      self.fixtureBarcode = row.fixtureBarcode;
      self.fixtureName = row.fixtureName;
      self.fixtureId = row.pkId;
      self.formValidate = {};
      self.$refs["formValidate"].resetFields();
      self.formValidate.repairMeasure=0.01;
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
          }
        });
    },

    //保存
    handleSubmit(repairStatus) {
      const self = this;
      self.$refs["formValidate"].validate(valid => {
        if (valid) {
          let para = {
            fixtureId: self.fixtureId,
            fixtureBarcode: self.fixtureBarcode,
            fixtureName: self.fixtureName,
            repairMeasure: self.formValidate.repairMeasure,
            remark: self.formValidate.remark,
            repairStatus: repairStatus
          };
          self.isDisable = true;
          axios
            .request({
              headers: {
                "Content-Type": "application/json; charset=UTF-8"
              },
              url: "/fixture/repair-add.json",
              method: "post",
              isAuth: true,
              params: para
            })
            .then(function(res) {
              self.repairModalshow = false;
              if (res.data.code === 200) {
                self.$Message.success("修磨信息已保存!");
                self.$emit("search");
              } else {
                self.$Notice.error({
                  title: "错误提示",
                  desc: res.data.info
                });
              }
              self.isDisable = false;
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
      self.repairModalshow = false;
    }
  },
  mounted() {}
};
</script>