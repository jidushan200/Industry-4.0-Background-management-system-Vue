<template>
  <!--审核信息弹出modal-->
  <Modal v-model="returnDetailModalshow" title="模具返库" width="620" draggable>
    <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="75" inline>
      <FormItem label="模具条码" prop="mouldBarcode">
        <Input style="width:200px" v-model="formValidate.mouldBarcode" readonly></Input>
      </FormItem>
      <FormItem label="模具名称" prop="mouldName">
        <Input style="width:200px" v-model="formValidate.mouldName" readonly></Input>
      </FormItem>
      <FormItem label="模具编码" prop="mouldNumber">
        <Input style="width:200px" v-model="formValidate.mouldNumber" readonly></Input>
      </FormItem>
      <FormItem label="模具图号" prop="mouldMap">
        <Input style="width:200px" v-model="formValidate.mouldMap" readonly />
      </FormItem>
      <FormItem label="库位" prop="warehouse">
        <Input style="width:200px" v-model="formValidate.warehouse" placeholder="输入库位" />
      </FormItem>
      <FormItem label="库管员" prop="staffInfo">
        <Input style="width:200px" v-model="staffInfo" readonly />
      </FormItem>
      <FormItem label="说明" prop="remark">
        <Input style="width:480px" v-model="formValidate.remark" placeholder="输入说明" />
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
      const self = this;
      this.$refs["formValidate"].validate(valid => {
        if (valid) {
          self.isDisabled = true;
          let para = {
            pkId: self.formValidate.pkId,
            mouldBarcode: self.formValidate.mouldBarcode,
            warehouse: self.formValidate.warehouse,
            remark: self.formValidate.remark
          };
          axios
            .request({
              headers: {
                "Content-Type": "application/json; charset=UTF-8"
              },
              url: "/mould/mould-return",
              method: "post",
              isAuth: true,
              params: para
            })
            .then(function(res) {
              if (res.data.code === 200) {
                self.returnDetailModalshow = false;
                self.$Message.success("模具返库成功!");
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
      self.returnDetailModalshow = false;
    }
  }
};
</script>