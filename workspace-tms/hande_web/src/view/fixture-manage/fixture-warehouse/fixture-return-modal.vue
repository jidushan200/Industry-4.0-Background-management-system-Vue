<template>
  <!--质检项弹出modal-->
  <Modal v-model="returnModalshow" class="modal-class" title="夹具退货" width="700" draggable>
    <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="100" inline>
      <Row>
        <Col span="12">
        <FormItem label="物料条码" prop="fixtureNumber">
          <Input v-model="fixtureBarcode" readonly style="width:200px;" />
        </FormItem>
        </Col>
        <Col span="12">
        <FormItem label="物料名称" prop="fixtureName">
          <Input v-model="fixtureName" readonly style="width:200px;" />
        </FormItem>
        </Col>
      </Row>
      <Row>
        <Col span="12">
        <FormItem label="物料图号" prop="fixtureMap">
          <Input v-model="fixtureMap" readonly style="width:200px;" />
        </FormItem>
        </Col>
        <Col span="12">
        <FormItem label="库管员" prop="staffInfo">
          <Input v-model="staffInfo" readonly style="width:200px;" />
        </FormItem>
        </Col>
      </Row>
      <Row>
        <Col>
        <FormItem label="退货原因" prop="returnReason">
          <Input v-model="formValidate.returnReason" style="width:536px;" />
        </FormItem>
        </Col>
      </Row>
    </Form>
    <div slot="footer">
      <Button @click="handleSubmit()" type="primary" :disabled="isDisable">提交</Button>
      <Button @click="closeModal()" style="margin-left: 8px">关闭</Button>
    </div>
  </Modal>
</template>
<script>
import { dateFormat, objCopy } from "@/libs/tools.js";
import { getToken, getBaseUrl } from "@/libs/util";
import axios from "@/libs/api.request";
import qs from "qs";
import warehouse_form_config from "./warehouse-form-config.js";
export default {
  data() {
    return {
      isDisable: false,
      returnModalshow: false,
      fixtureNumber: "",
      fixtureName: "",
      fixtureMap: "",
      fixtureBarcode: "",
      staffInfo: "",
      formValidate: warehouse_form_config.formValidate, // form表单字段
      ruleValidate: warehouse_form_config.ruleValidate // form表单验证规则
    };
  },
  created() {
    const self = this;
    self.getCurrentStaff();
  },
  methods: {
    showModal(row) {
      const self = this;
      //self.formValidate.returnReason = "";
      self.returnModalshow = true;
      self.fixtureName = row.fixtureName;
      self.fixtureMap = row.fixtureMap;
      self.pkId = row.pkId;
      self.fixtureBarcode = row.fixtureBarcode;
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
    handleSubmit() {      
      const self = this;
      self.$refs["formValidate"].validate(valid => {
        if (valid) {
          self.isDisable = true;
          let para = {
            receiptId:self.pkId,
            fixtureBarcode:self.fixtureBarcode,
            returnReason: self.formValidate.returnReason
          };
          axios
            .request({
              headers: {
                "Content-Type": "application/json; charset=UTF-8"
              },
              url: "/fixture/fixture-refund",
              method: "post",
              isAuth: true,
              params: para
            })
            .then(function(res) {             
              if (res.data.code === 200) {
                self.returnModalshow = false;
                self.$Message.success("夹具已退货!");
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
      self.returnModalshow = false;
    }
  },
  mounted() {}
};
</script>