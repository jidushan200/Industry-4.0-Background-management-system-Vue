<template>
  <!--质检项弹出modal-->
  <Modal v-model="warehouseModalshow" class="modal-class" title="新夹具入库" width="910" draggable>
    <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="90" inline>
      <Row>
        <Col span="8">
          <FormItem label="物料条码" prop="fixtureBarcode">
            <Input v-model="fixtureBarcode" readonly style="width:200px;" />
          </FormItem>
        </Col>
        <Col span="8">
          <FormItem label="物料名称" prop="fixtureName">
            <Input v-model="fixtureName" readonly style="width:200px;" />
          </FormItem>
        </Col>

        <Col span="8">
          <FormItem label="物料图号" prop="fixtureMap">
            <Input v-model="fixtureMap" readonly style="width:200px;" />
          </FormItem>
        </Col>
      </Row>
      <Row>
        <Col span="8">
          <FormItem prop="departmentId" label="所属部门">
            <Select
              style="width:200px"
              v-model="formValidate.departmentId"
              @on-change="departmentSelectChange"
              ref="department"
            >
              <Option
                v-for="item in departmentList"
                :value="item.pkId"
                :key="item.pkId"
              >{{ item.departmentName }}</Option>
            </Select>
          </FormItem>
        </Col>
        <Col span="8">
          <FormItem label="库管员" prop="staffInfo">
            <Input v-model="staffInfo" readonly style="width:200px;" />
          </FormItem>
        </Col>
        <Col span="8">
          <FormItem label="库位" prop="storeHouse">
            <Input v-model="formValidate.storeHouse" style="width:200px;" />
          </FormItem>
        </Col>
      </Row>
      <Table
        :columns="detailColumns"
        height="360"
        :data="detailList"
        border
        stripe
        highlight-row
        v-if="detailList.length>0"
      ></Table>
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
      warehouseModalshow: false,
      fixtureNumber: "",
      fixtureName: "",
      fixtureMap: "",
      fixtureBarcode: "",
      supplierName: "",
      departmentList: [],
      departmentName: "",
      staffInfo: "",
      detailList: [],
      detailColumns: [
        {
          title: "配件图号",
          key: "fixtureMap",
          minWidth: 150
        },
        {
          title: "配件条码",
          key: "fixtureBarcode",
          minWidth: 120
        },
        {
          title: "配件名称",
          key: "fixtureName",
          minWidth: 270
        }
      ],
      formValidate: warehouse_form_config.formValidate, // form表单字段
      ruleValidate: warehouse_form_config.ruleValidate // form表单验证规则
    };
  },
  created() {
    const self = this;
    self.getCurrentStaff();
    self.getDepartmentListData();
  },
  init() {
    const self = this;
  },
  methods: {
    showModal(row) {
      const self = this;
      self.warehouseModalshow = true;
      self.fixtureNumber = row.fixtureNumber;
      self.fixtureName = row.fixtureName;
      self.fixtureMap = row.fixtureMap;
      self.supplierName = row.supplierName;
      self.receiptId = row.pkId;
      self.formValidate = {};
      self.$refs["formValidate"].resetFields();
      self.fixtureBarcode = row.fixtureBarcode;
      if (row.fixtureType == 2) {
        return;
      }
      let para = {
        pkId: self.receiptId
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/fixture/purchase-receipt-get-by-id",
          isAuth: true,
          method: "post",
          params: para
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            self.detailList = data.data.detailList;
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },

    getDepartmentListData() {
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/base/staff-department-list",
          isAuth: true,
          method: "post"
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            self.departmentList = data.data;
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },

    departmentSelectChange(item) {
      const self = this;
      let departmentList = self.departmentList;
      if (item !== undefined) {
        for (const department of departmentList) {
          if (department.pkId === item) {
            self.departmentName = department.departmentName;
          }
        }
      }
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
            receiptId: self.receiptId,
            storeHouse: self.formValidate.storeHouse,
            departmentId: self.formValidate.departmentId,
            departmentName: self.departmentName,
            staffCode: self.staffCode,
            staffName: self.staffName
          };
          axios
            .request({
              headers: {
                "Content-Type": "application/json; charset=UTF-8"
              },
              url: "/fixture/fixture-add.json",
              method: "post",
              isAuth: true,
              params: para
            })
            .then(function(res) {
              self.warehouseModalshow = false;
              if (res.data.code === 200) {
                self.$Message.success("夹具已入库!");                
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
      self.warehouseModalshow = false;
    }
  },
  mounted() {}
};
</script>