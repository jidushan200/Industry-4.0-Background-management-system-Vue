<template>
  <!--审核信息弹出modal-->
  <Modal v-model="outboundDetailModalshow" title="模具出库" width="600" draggable>
    <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="100" inline>
      <FormItem label="模具编码" prop="mouldNumber">
        <Input style="width:170px" v-model="formValidate.mouldNumber" readonly></Input>
      </FormItem>
      <FormItem label="模具名称" prop="mouldName">
        <Input style="width:170px" v-model="formValidate.mouldName" readonly></Input>
      </FormItem>
      <FormItem label="模具图号" prop="mouldMap">
        <Input style="width:170px" v-model="formValidate.mouldMap" readonly />
      </FormItem>
      <FormItem label="模具条码" prop="mouldBarcode">
        <Input style="width:170px" v-model="formValidate.mouldBarcode" readonly />
      </FormItem>
      <FormItem label="库位" prop="warehouse">
        <Input style="width:170px" v-model="formValidate.warehouse" readonly />
      </FormItem>
      <FormItem label="出库类型" prop="outType">
        <Select style="width: 170px" v-model="formValidate.outType" ref="outType">
          <Option v-for="item in outType" :value="item.value" :key="item.value">{{ item.name }}</Option>
        </Select>
      </FormItem>
      <FormItem label="领用人职工号" prop="staffCode">
        <Input
          style="width:170px"
          v-model="formValidate.staffCode"
          placeholder="输入领用人职工号"
          @on-blur="searchStaff()"
        >
          <Icon type="ios-search" slot="suffix" />
        </Input>&nbsp;
        <Input style="width:270px" v-model="staffInfo" readonly></Input>
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
import outType from "./out-type.js";
import output_form_config from "./output-form-config.js";
export default {
  data() {
    return {
      isDisabled: false,
      staffInfo: "",
      outboundDetailModalshow: false,
      outType: outType,
      formValidate: output_form_config.formValidate, //form表单字段
      ruleValidate: output_form_config.ruleValidate //form表单验证规则
    };
  },
  created() {},
  methods: {
    showModal(row) {
      const self = this;
      self.outboundDetailModalshow = true;
      self.formValidate = objCopy(row, {});
      self.staffInfo = "";
    },
    searchStaff() {
      const self = this;
      if (!!!self.formValidate.staffCode) {
        return;
      }
      let para = {
        staffCode: self.formValidate.staffCode
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/base/staff-get-by-code.json",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            if (data.data !== null && data.data !== undefined) {
              self.formValidate.departmentId = data.data.departmentId;
              let departmentName = data.data.departmentName;
              self.formValidate.departmentName = data.data.departmentName;

              self.formValidate.receiveClass = data.data.teamName;
              self.formValidate.receiveClassId = data.data.teamId;

              self.formValidate.receiverId = data.data.pkId;
              self.formValidate.receiver = data.data.staffName;
              self.staffInfo =
                departmentName + "-" + self.formValidate.receiver;
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
          this.isDisabled = true;
          let para = {
            pkId: self.formValidate.pkId,
            mouldNumber: self.formValidate.mouldNumber,
            mouldMap: self.formValidate.mouldMap,
            mouldName: self.formValidate.mouldName,
            warehouse: self.formValidate.warehouse,
            outType: self.formValidate.outType,
            mouldBarcode: self.formValidate.mouldBarcode,
            departmentId: self.formValidate.departmentId,
            departmentName: self.formValidate.departmentName,
            receiverId: self.formValidate.receiverId,
            receiver: self.formValidate.receiver,
            receiveClassId: self.formValidate.receiveClassId,
            receiveClass: self.formValidate.receiveClass,
            remark: self.formValidate.remark
          };
          axios
            .request({
              headers: {
                "Content-Type": "application/json; charset=UTF-8"
              },
              url: "/mould/mould-outbound",
              method: "post",
              isAuth: true,
              params: para
            })
            .then(function(res) {
              if (res.data.code === 200) {
                self.outboundDetailModalshow = false;
                self.$Message.success("模具出库成功!");
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
      self.outboundDetailModalshow = false;
    }
  }
};
</script>
