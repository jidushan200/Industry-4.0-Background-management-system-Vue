<template>
  <!--申请单信息弹出modal-->
  <Modal v-model="mouldWarehouseModel" title="模具入库" width="650" draggable>
    <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="80">
      <Row>
        <Col span="12">
          <FormItem label="模具编码" prop="mouldNumber">
            <Input style="width:200px" v-model="formValidate.mouldNumber" readonly />
          </FormItem>
        </Col>
        <Col span="12">
          <FormItem label="模具图号" prop="mouldMap">
            <Input style="width:200px" v-model="formValidate.mouldMap" readonly />
          </FormItem>
        </Col>
      </Row>
      <Row>
        <Col span="12">
          <FormItem label="模具名称" prop="mouldName">
            <Input style="width:200px" v-model="formValidate.mouldName" readonly />
          </FormItem>
        </Col>

        <Col span="12">
          <FormItem label="模具条码" prop="mouldBarcode">
            <Input style="width:200px" v-model="formValidate.mouldBarcode" readonly />
          </FormItem>
        </Col>
      </Row>
      <Row>
        <Col span="12">
          <FormItem label="库管员" prop="staffInfo">
            <Input style="width:200px" v-model="staffInfo" readonly />
          </FormItem>
        </Col>
        <Col span="12">
          <FormItem label="库位" prop="warehouse">
            <Input style="width:200px" v-model="formValidate.warehouse" />
          </FormItem>
        </Col>
      </Row>
    </Form>
    <div slot="footer">
      <Button
        @click="handleSubmit()"
        type="primary"
        style="margin-left: 8px"
        :disabled="isDisabled"
      >提交</Button>
      <Button @click="handleReset()" style="margin-left: 8px">关闭</Button>
    </div>
  </Modal>
</template>
<script>
import { dateFormat, objCopy } from "@/libs/tools.js";
import axios from "@/libs/api.request";
import warehouse_form_config from "./warehouse-form-config.js";

export default {
  data() {
    return {
      isDisabled: false,
      staffInfo: "",
      isNew: true,
      mouldWarehouseModel: false,
      formValidate: warehouse_form_config.formValidate, //form表单字段
      ruleValidate: warehouse_form_config.ruleValidate //form表单验证规则
    };
  },
  created() {},
  methods: {
    init() {
      const self = this;
    },
    searchStaff() {
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
    showModal(index) {
      const self = this;
      self.isNew = false;
      self.searchStaff();
      self.formValidate = objCopy(index, {});
      self.mouldWarehouseModel = true;
      if (self.formValidate.checkType === 1) {
        self.isNew = true;
      } else {
        self.isNew = false;
      }
      self.formValidate.isRepair = 0;
    },
    handleSubmit() {
      const self = this;
      this.$refs["formValidate"].validate(valid => {
        if (valid) {
          let para = {
            mouldBarcode: self.formValidate.mouldBarcode,
            pkId: self.formValidate.pkId,
            warehouse: self.formValidate.warehouse
          };
          self.isDisabled = true;
          axios
            .request({
              headers: {
                "Content-Type": "application/json; charset=UTF-8"
              },
              url: "/mould/mould-warehouse",
              method: "post",
              isAuth: true,
              params: para
            })
            .then(function(res) {
              if (res.data.code === 200) {
                self.mouldWarehouseModel = false;
                self.$Message.success("模具入库成功!");
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
      self.mouldWarehouseModel = false;
    }
  }
};
</script>
