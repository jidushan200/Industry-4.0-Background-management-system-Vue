<template>
  <!--审核信息弹出modal-->
  <Modal v-model="checkCycleDetailModalshow" title="量具完善检定周期" width="600" draggable>
    <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="100" inline>
      <FormItem label="物料编码" prop="measureNumber">
        <Input style="width:170px" v-model="formValidate.measureNumber" readonly></Input>
      </FormItem>
      <FormItem label="物料名称" prop="measureName">
        <Input style="width:170px" v-model="formValidate.measureName" readonly></Input>
      </FormItem>
      <FormItem label="物料图号" prop="model">
        <Input style="width:170px" v-model="formValidate.model" readonly />
      </FormItem>
      <FormItem label="领用部门" prop="useDepartmentName">
        <Input style="width:170px" v-model="formValidate.useDepartmentName" readonly />
      </FormItem>
      <FormItem label="领用班组" prop="useTeamName">
        <Input style="width:170px" v-model="formValidate.useTeamName" readonly />
      </FormItem>
      <FormItem label="领用人" prop="userName">
        <Input style="width:170px" v-model="formValidate.userName" readonly />
      </FormItem>
      <FormItem label="厂家计量编号" prop="manufacturingNumber">
        <Input
          style="width:170px"
          v-model="formValidate.manufacturingNumber"
          placeholder="请输入厂家编号"
        />
      </FormItem>
      <FormItem label="本厂计量编号" prop="factoryMetrology">
        <Input style="width:170px" v-model="formValidate.factoryMetrology" placeholder="请输入本厂计量编号" />
      </FormItem>
      <FormItem label="定检周期(日)" prop="checkCycle">
        <InputNumber :max="365" :min="1" style="width:170px" v-model="formValidate.checkCycle" />
      </FormItem>
    </Form>
    <div slot="footer">
      <Button @click="handleSubmit('formValidate')" type="primary" :disabled="isDisabled">提交</Button>
      <Button @click="handleReset('formValidate')" style="margin-left: 8px">取消</Button>
    </div>
  </Modal>
</template>
<script>
import { dateFormat, objCopy } from "@/libs/tools.js";
import axios from "@/libs/api.request";
import check_cycle_form_config from "./check-cycle-form-config.js";
export default {
  data() {
    return {
      isDisabled: false,
      checkCycleDetailModalshow: false,
      formValidate: check_cycle_form_config.formValidate, //form表单字段
      ruleValidate: check_cycle_form_config.ruleValidate //form表单验证规则
    };
  },
  created() {},
  methods: {
    showModal(row) {
      const self = this;
      self.formValidate = objCopy(row, {});
      if (self.formValidate.checkCycle === null) {
        self.formValidate.checkCycle = 1;
      }
      self.checkCycleDetailModalshow = true;
    },
    handleSubmit() {
      const self = this;
      self.$refs["formValidate"].validate(valid => {
        if (valid) {
          this.isDisabled = true;
          let para = {
            pkId: self.formValidate.pkId,
            measureBarcode: self.formValidate.measureBarcode,
            checkCycle: self.formValidate.checkCycle,
            factoryMetrology: self.formValidate.factoryMetrology,
            manufacturingNumber: self.formValidate.manufacturingNumber
          };
          axios
            .request({
              headers: {
                "Content-Type": "application/json; charset=UTF-8"
              },
              url: "/measure/measure-check-cycle",
              method: "post",
              isAuth: true,
              params: para
            })
            .then(function(res) {
              if (res.data.code === 200) {
                self.checkCycleDetailModalshow = false;
                self.$Message.success("量具检定周期已完善完善!");
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
    handleReset() {
      const self = this;
      self.checkCycleDetailModalshow = false;
    }
  }
};
</script>