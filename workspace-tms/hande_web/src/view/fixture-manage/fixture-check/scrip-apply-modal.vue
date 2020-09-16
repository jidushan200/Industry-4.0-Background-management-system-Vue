<template>
  <Modal v-model="scripModalshow" class="modal-class" title="夹具报废申请" width="700" draggable>
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
        <FormItem label="报废原因" prop="scripResion">
          <Select style="width:170px" v-model="formValidate.scripResion" ref="scripType" placeholder="请选择报废原因">
            <Option v-for="item in scripType" :value="item.value" :key="item.value">{{ item.name }}</Option>
          </Select>
        </FormItem>
        </Col>
        <Col span="12">
        <FormItem label="申请人" prop="staffInfo">
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
      <Button @click="handleSubmit(1)" type="warning" :disabled="isDisable">提交</Button>
      <Button @click="closeModal()" style="margin-left: 8px">关闭</Button>
    </div>
  </Modal>
</template>
<script>
import { dateFormat, objCopy } from "@/libs/tools.js";
import { getToken, getBaseUrl } from "@/libs/util";
import axios from "@/libs/api.request";
import qs from "qs";
import check_form_config from "./check-form-config.js";
export default {
  data() {
    return {
      isDisabled: false,
      scripModalshow: false,
      isDisable: false,
      fixtureName: "",
      fixtureMap: "",
      fixtureBarcode: "",
      staffInfo: "",
      scripType: [
        {
          name: "正常报废",
          value: 1
        },
        {
          name: "异常报废",
          value: 2
        }
      ],
      formValidate: check_form_config.formValidate, // form表单字段
      ruleValidate: check_form_config.ruleValidate // form表单验证规则
    };
  },
  created() {
    const self = this;
    self.getCurrentStaff();
  },
  methods: {
    showModal(row) {
      const self = this;
      this.formValidate = {};
      this.$refs["formValidate"].resetFields();
      self.scripModalshow = true;
      self.fixtureBarcode = row.fixtureBarcode;
      self.fixtureName = row.fixtureName;
      self.fixtureId = row.fixtureId;
      self.fixtureNumber = row.fixtureNumber;
      self.checkId = row.pkId;
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
            self.departmentId = data.data.departmentId;
            self.departmentName = data.data.departmentName;
            self.staffInfo =
              data.data.departmentName + "-" + data.data.staffName;
          }
        });
    },

    //保存
    handleSubmit(status) {
      this.isDisabled = true;
      const self = this;
      self.isDisable = true;
      self.$refs["formValidate"].validate(valid => {
        if (valid) {
          let para = {
            checkId: self.checkId,
            fixtureId: self.fixtureId,
            fullNumber: self.fixtureBarcode,
            fixtureNumber: self.fixtureNumber,
            fixtureName: self.fixtureName,
            scripResion: self.formValidate.scripResion,
            departmentId: self.departmentId,
            departmentName: self.departmentName,
            scripRemark: self.formValidate.remark,
            applyStatus: status
          };
          axios
            .request({
              headers: {
                "Content-Type": "application/json; charset=UTF-8"
              },
              url: "/fixture/fixture-scrip-apply-add",
              method: "post",
              isAuth: true,
              params: para
            })
            .then(function(res) {
              self.scripModalshow = false;
              if (res.data.code === 200) {
                self.$Message.success("申请信息已保存!");
                self.isDisabled = false;
                self.$emit("search");
              } else {
                self.$Notice.error({
                  title: "错误提示",
                  desc: res.data.info
                });                
              }
              self.isDisable=false;
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
      self.scripModalshow = false;
    }
  },
  mounted() {}
};
</script>