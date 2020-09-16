<template>
  <Modal v-model="checkQualityModel" title="量具质检" width="550" draggable>
    <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="90">
      <Row>
        <Col span="12">
          <FormItem label="物料编码" prop="measureNumber">
            <Input style="width:170px" v-model="formValidate.measureNumber" readonly />
          </FormItem>
        </Col>
        <Col span="12">
          <FormItem label="物料名称" prop="measureName">
            <Input style="width:170px" v-model="formValidate.measureName" readonly />
          </FormItem>
        </Col>
      </Row>
      <Row>
        <Col span="12">
          <FormItem label="型号规格" prop="model">
            <Input style="width:170px" v-model="formValidate.model" readonly />
          </FormItem>
        </Col>
        <Col span="12">
          <FormItem label="是否修磨" prop="isRepair" v-if="!isNew">
            <RadioGroup style="width: 170px" v-model="isRepair">
              <Radio :label="0">否</Radio>
              <Radio :label="1">是</Radio>
            </RadioGroup>
          </FormItem>
        </Col>
      </Row>
      <Row>
        <Col span="12">
          <FormItem label="合格确认" prop="quality">
            <RadioGroup
              style="width: 170px"
              v-model="formValidate.quality"
              @on-change="radioChange"
            >
              <Radio :label="0">不合格</Radio>
              <Radio :label="1">合格</Radio>
            </RadioGroup>
          </FormItem>
        </Col>
      </Row>
    </Form>
    <div slot="footer">
      <Button
        @click="handleSubmit()"
        type="primary"
        style="margin-left: 8px"
        v-if="isQuality"
        :disabled="isDisabled"
      >提交</Button>
      <Button @click="handleReset()" style="margin-left: 8px">关闭</Button>
    </div>
  </Modal>
</template>
<script>
import { dateFormat, objCopy } from "@/libs/tools.js";
import axios from "@/libs/api.request";
import check_form_config from "./check-form-config.js";

export default {
  data() {
    return {
      quality: "",
      isQuality: true,
      isDisabled: false,
      isRepair: null,
      isNew: true,
      checkQualityModel: false,
      formValidate: check_form_config.formValidate, //form表单字段
      ruleValidate: check_form_config.ruleValidate //form表单验证规则
    };
  },
  created() {},
  methods: {
    init() {
      const self = this;
    },
    showModal(index) {
      const self = this;
      self.isRepair = null;
      self.formValidate = {};
      self.formValidate = objCopy(index, {});
      if (index.checkType === 1) {
        self.isNew = true;
      } else if (index.checkType === 2) {
        self.isNew = false;
        self.isRepair = 0;
      }
      self.checkQualityModel = true;
      self.isQuality = true;
    },
    handleSubmit() {
      const self = this;
      self.$refs["formValidate"].validate(valid => {
        if (valid) {
          let para = {
            pkId: self.formValidate.pkId,
            measureNumber: self.formValidate.measureNumber,
            checkType: self.formValidate.checkType,
            isRepair: self.isRepair
          };
          self.isDisabled = true;
          axios
            .request({
              headers: {
                "Content-Type": "application/json; charset=UTF-8"
              },
              url: "/measure/measure-check-qualified.json",
              method: "post",
              isAuth: true,
              params: para
            })
            .then(function(res) {
              const data = res.data;
              if (data.code === 200) {
                self.checkQualityModel = false;
                self.$Message.success("量具定检合格!");
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
        }
      });
    },
    radioChange() {
      const self = this;
      if (self.formValidate.quality == 0) {
        self.$Message.info("不合格产品请关闭窗口后点击不合格按钮");
        self.isQuality = false;
      } else {
        self.isQuality = true;
      }
    },
    handleReset() {
      const self = this;
      self.checkQualityModel = false;
    }
  }
};
</script>
