<template>
  <Modal v-model="maintainModalshow" class="modal-class" title="夹具保养" width="680" draggable>
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
        <Col span="24">
          <FormItem label="保养项目" prop="maintainItem">
            <Select
              v-model="formValidate.maintainItem"
              multiple
              placeholder="选择保养项目"
              style="width:530px;"
              @on-change="getMaintainItem"
            >
              <Option
                v-for="item in maintainItems"
                :value="item.name"
                :key="item.value"
              >{{item.name}}</Option>
            </Select>
          </FormItem>
        </Col>
      </Row>
      <Row>
        <Col span="12">
          <FormItem label="使用状态" prop="useStatus">
            <Select v-model="formValidate.useStatus" placeholder="选择使用状态" style="width:200px;">
              <Option
                v-for="item in useStatusList"
                :value="item.value"
                :key="item.value"
              >{{item.name}}</Option>
            </Select>
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
            <Input v-model="formValidate.remark" style="width:530px;" />
          </FormItem>
        </Col>
      </Row>
    </Form>
    <div slot="footer">
      <Button @click="handleSubmit(1)" type="primary" :disabled="isDisable">保存</Button>
      <!--<Button @click="handleSubmit(1)" type="warning">提交</Button>-->
      <Button @click="closeModal()" style="margin-left: 8px">关闭</Button>
    </div>
  </Modal>
</template>
<script>
import { dateFormat, objCopy } from "@/libs/tools.js";
import { getToken, getBaseUrl } from "@/libs/util";
import axios from "@/libs/api.request";
import qs from "qs";
import site_form_config from "./site-form-config.js";
import useStatusList from "./use-status";
import maintainItems from "./maintain-items";
export default {
  data() {
    return {
      isDisable: false,
      maintainModalshow: false,
      fixtureName: "",
      fixtureMap: "",
      fixtureBarcode: "",
      staffInfo: "",
      useStatusList: useStatusList,
      maintainItems: maintainItems,
      formValidate: site_form_config.formValidate, // form表单字段
      ruleValidate: site_form_config.ruleValidate // form表单验证规则
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
      self.maintainModalshow = true;
      self.fixtureBarcode = row.fixtureBarcode;
      self.fixtureName = row.fixtureName;
      self.fixtureMap = row.fixtureMap;
      self.pkId = row.pkId;
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
    getMaintainItem(data) {
      // console.log("data", data);
    },
    //保存
    handleSubmit(maintainStatus) {
      this.isDisabled = true;
      const self = this;
      self.$refs["formValidate"].validate(valid => {
        if (valid) {
          let para = {
            pkId: self.pkId,
            fixtureBarcode: self.fixtureBarcode,
            fixtureName: self.fixtureName,
            maintainItem: self.formValidate.maintainItem.join(","),
            useStatus: self.formValidate.useStatus,
            remark: self.formValidate.remark,
            maintainStatus: maintainStatus
          };
          self.isDisable = true;
          axios
            .request({
              headers: {
                "Content-Type": "application/json; charset=UTF-8"
              },
              url: "/fixture/maintain-add.json",
              method: "post",
              isAuth: true,
              params: para
            })
            .then(function(res) {
              self.maintainModalshow = false;
              if (res.data.code === 200) {
                self.$Message.success("保养信息已保存!");
                self.isDisabled = false;
                self.$emit("search");
              } else {
                self.$Notice.error({
                  title: "错误提示",
                  desc: res.data.info
                });
                self.isDisabled = false;
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
      self.maintainModalshow = false;
    }
  },
  mounted() {}
};
</script>