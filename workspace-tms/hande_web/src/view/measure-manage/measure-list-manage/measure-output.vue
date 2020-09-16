<template>
  <!--审核信息弹出modal-->
  <Modal v-model="outboundDetailModalshow" title="量具出库" width="600" draggable>
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
        <Input style="width:170px" v-model="formValidate.warehouse" readonly />
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
      self.formValidate = objCopy(row, {});
      self.searchKeeper();
      self.staffInfo = "";
      self.outboundDetailModalshow = true;
      if (!!row.userCode) {
        self.formValidate.staffCode = row.userCode;
        self.searchStaff();
      }
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
              self.formValidate.departmentName = data.data.departmentName;
              self.formValidate.teamId = data.data.teamId;
              self.formValidate.teamName = data.data.teamName;
              self.formValidate.staffCode = data.data.staffCode;
              self.formValidate.staffName = data.data.staffName;
              self.staffInfo =
                self.formValidate.departmentName +
                "-" +
                data.data.teamName +
                "-" +
                self.formValidate.staffName;
            } else {
              self.$Message.error("无法找到与职工号对应的职工");
              self.formValidate.staffCode = "";
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
      self.$refs["formValidate"].validate(valid => {
        if (valid) {
          let para = {
            pkId: self.formValidate.pkId,
            measureBarcode: self.formValidate.measureBarcode,
            measureNumber: self.formValidate.measureNumber,
            model: self.formValidate.model,
            measureName: self.formValidate.measureName,
            warehouse: self.formValidate.warehouse,
            outType: 1,
            receiveResion: "生产使用",
            departmentId: self.formValidate.departmentId,
            departmentName: self.formValidate.departmentName,
            teamId: self.formValidate.teamId,
            teamName: self.formValidate.teamName,
            staffCode: self.formValidate.staffCode,
            staffName: self.formValidate.staffName,
            remark: self.formValidate.remark
          };
          self.isDisabled = true;
          axios
            .request({
              headers: {
                "Content-Type": "application/json; charset=UTF-8"
              },
              url: "/measure/measure-outbound",
              method: "post",
              isAuth: true,
              params: para
            })
            .then(function(res) {
              if (res.data.code === 200) {
                self.outboundDetailModalshow = false;
                self.$Message.success("量具出库成功!");
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
    handleReset() {
      const self = this;
      self.outboundDetailModalshow = false;
    }
  }
};
</script>
