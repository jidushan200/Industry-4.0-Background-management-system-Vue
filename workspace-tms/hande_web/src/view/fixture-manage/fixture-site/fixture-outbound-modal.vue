<template>
  <!--出库modal-->
  <Modal v-model="outboundModalshow" class="modal-class" title="夹具领用出库" width="700" draggable>
    <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="100" inline>
      <Row>
        <Col span="12">
          <FormItem label="物料编码" prop="fixtureNumber">
            <Input v-model="fixtureNumber" readonly style="width:200px;" />
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
          <FormItem label="物料条码" prop="fixtureBarcode">
            <Input v-model="fixtureBarcode" readonly style="width:200px;" />
          </FormItem>
        </Col>
      </Row>
      <Row>
        <Col span="12">
          <FormItem label="设备名称" prop="equipmentId">
            <Select
              style="width:200px"
              v-model="formValidate.equipmentId"
              ref="equipmentId"
              @on-change="equipmentListSelectChange"
              clearable
              filterable
            >
              <Option
                v-for="item in equipmentList"
                :value="item.pkId"
                :key="item.pkId"
              >{{ item.equipmentCode }}-{{ item.equipmentName }}</Option>
            </Select>
          </FormItem>
        </Col>
        <Col span="12">
          <FormItem label="领用人" prop="staffCode">
            <Select
              style="width:200px"
              v-model="formValidate.staffCode"
              ref="equipmentId"
              @on-change="staffSelectChange"
              clearable
              filterable
            >
              <Option
                v-for="item in staffList"
                :value="item.staffCode"
                :key="item.pkId"
              >{{ item.staffCode }}-{{ item.staffName }}</Option>
            </Select>
          </FormItem>
        </Col>
      </Row>
      <row>
        <FormItem label="备注" prop="remark">
          <Input style="width:560px" v-model="formValidate.remark" placeholder="备注" />
        </FormItem>
      </row>
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
      outboundModalshow: false,
      fixtureNumber: "",
      fixtureName: "",
      fixtureMap: "",
      fixtureBarcode: "",
      staffList: [],
      equipmentList: [],
      formValidate: warehouse_form_config.formValidate, // form表单字段
      ruleValidate: warehouse_form_config.ruleValidate // form表单验证规则
    };
  },
  created() {
    const self = this;
    //self.getCurrentStaff();
    self.getEquipmentListData();
  },
  methods: {
    showModal(row) {
      const self = this;
      //self.formValidate.returnReason = "";
      self.outboundModalshow = true;
      self.fixtureBarcode = row.fixtureBarcode;
      self.fixtureNumber = row.fixtureNumber;
      self.fixtureName = row.fixtureName;
      self.fixtureMap = row.fixtureMap;
      self.pkId = row.pkId;
      self.formValidate = {};
      self.searchStaff(row.departmentId);
    },
    searchStaff(departmentId) {
      const self = this;
      let para = {
        departmentId: departmentId
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/base/staff-list",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            self.staffList = data.data;
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

    getEquipmentListData() {
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/base/get-equipment-list",
          isAuth: true,
          method: "post",
          params: []
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            self.equipmentList = data.data;
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },
    equipmentListSelectChange(item) {
      const self = this;
      let equipmentList = self.equipmentList;
      if (item !== undefined) {
        for (const equipment of equipmentList) {
          if (equipment.pkId === item) {
            self.formValidate.equipmentCode = equipment.equipmentCode;
            self.formValidate.equipmentName = equipment.equipmentName;
          }
        }
      }
    },

    staffSelectChange(item) {
      const self = this;
      let staffList = self.staffList;
      if (item !== undefined) {
        for (const staff of staffList) {
          if (staff.staffCode == item) {
            self.formValidate.staffCode = staff.staffCode;
            self.formValidate.staffName = staff.staffName;
          }
        }
      }
    },
    //保存
    handleSubmit() {
      const self = this;
      self.$refs["formValidate"].validate(valid => {
        if (valid) {
          self.isDisable = true;
          let para = {
            pkId: self.pkId,
            fixtureBarcode: self.fixtureBarcode,
            staffCode: self.formValidate.staffCode,
            staffName: self.formValidate.staffName,
            equipmentId: self.formValidate.equipmentId,
            equipmentName: self.formValidate.equipmentName,
            remark: self.formValidate.remark
          };
          axios
            .request({
              headers: {
                "Content-Type": "application/json; charset=UTF-8"
              },
              url: "/fixture/fixture-use.json",
              method: "post",
              isAuth: true,
              params: para
            })
            .then(function(res) {
              self.outboundModalshow = false;
              if (res.data.code === 200) {
                self.$Message.success("夹具已出库!");
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
      self.outboundModalshow = false;
    }
  },
  mounted() {}
};
</script>