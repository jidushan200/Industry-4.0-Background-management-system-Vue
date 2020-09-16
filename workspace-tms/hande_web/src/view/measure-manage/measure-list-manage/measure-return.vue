<template>
  <!--审核信息弹出modal-->
  <Modal v-model="returnDetailModalshow" title="量具返库" width="600" draggable>
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
      <FormItem label="库位" prop="warehouse">
        <Input style="width:170px" v-model="formValidate.warehouse" placeholder="输入库位" />
      </FormItem>
      <FormItem label="库管员" prop="staffInfo">
        <Input v-model="staffInfo" readonly />
      </FormItem>
      <FormItem label="说明" prop="remark">
        <Input style="width:450px" v-model="formValidate.remark" placeholder="输入说明" />
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
import return_form_config from "./return-form-config.js";
export default {
  data() {
    return {
      isDisabled: false,
      staffInfo: "",
      returnDetailModalshow: false,
      formValidate: return_form_config.formValidate, //form表单字段
      ruleValidate: return_form_config.ruleValidate //form表单验证规则
    };
  },
  created() {},
  methods: {
    showModal(row) {
      const self = this;
      self.formValidate = objCopy(row, {});
      self.returnDetailModalshow = true;
      self.searchKeeper();
    },
    searchKeeper() {
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/base/staff-get-current.json",
          method: "post",
          isAuth: true
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            if (data.data !== null && data.data !== undefined) {
              self.formValidate.keeperId = data.data.pkId;
              self.formValidate.keeper = data.data.staffName;
              self.staffInfo = data.data.staffCode + "-" + data.data.staffName;
            } else {
              self.$Message.error("无法找到与职工号对应的职工");
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
    },
    handleSubmit() {
      this.isDisabled = true;
      const self = this;
      let para = {
        pkId: self.formValidate.pkId,
        measureBarcode:self.formValidate.measureBarcode,
        warehouse: self.formValidate.warehouse,
        keeperId: self.formValidate.keeperId,
        keeper: self.formValidate.keeper,
        remark: self.formValidate.remark
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/measure/measure-return",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          if (res.data.code === 200) {
            self.returnDetailModalshow = false;
            self.$Message.success("量具返库成功!");
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
    },
    handleReset() {
      const self = this;
      self.returnDetailModalshow = false;
    }
  }
};
</script>