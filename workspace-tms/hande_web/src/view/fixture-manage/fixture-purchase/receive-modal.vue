<style lang="less">
@import url("./purchase-apply-modal.less");
</style>
<template>
  <!--申请单信息弹出modal-->
  <Modal v-model="receiveModalshow" class="modal-class" title="申购单详情" width="1160" draggable>
    <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="95" inline>
      <Row>
        <Col span="6">
          <FormItem label="夹具编码" prop="fixtureName">
            <Input style="width:180px" v-model="fixtureNumber" readonly></Input>
          </FormItem>
        </Col>
        <Col span="6">
          <FormItem label="夹具名称" prop="fixtureName">
            <Input style="width:180px" v-model="fixtureName" readonly></Input>
          </FormItem>
        </Col>
        <Col span="6">
          <FormItem label="夹具图号" prop="fixtureMap">
            <Input style="width:180px" v-model="fixtureMap" readonly></Input>
          </FormItem>
        </Col>
        <Col span="6">
          <FormItem label="制件名称" prop="partName">
            <Input style="width:180px" v-model="partName" readonly></Input>
          </FormItem>
        </Col>
        <Col span="6">
          <FormItem label="TMS库存" prop="inventoryQty">
            <Input style="width:180px" v-model="inventoryQty" readonly />
          </FormItem>
        </Col>
        <Col span="6">
          <FormItem label="ERP库存" prop="erpQty">
            <Input style="width:180px" v-model="erpQty" readonly />
          </FormItem>
        </Col>
        <Col span="6">
          <FormItem label="ERP待检数量" prop="noCheckQty">
            <Input style="width:180px" v-model="noCheckQty" readonly />
          </FormItem>
        </Col>
        <Col span="6">
          <FormItem label="申购类型" prop="purchaseType">
            <Select style="width:180px" v-model="formValidate.purchaseType" ref="purchaseType">
              <Option
                v-for="item in purchaseType"
                :value="item.value"
                :key="item.value"
              >{{ item.name }}</Option>
            </Select>
          </FormItem>
        </Col>
        <Col span="6">
          <FormItem label="申请部门" prop="departmentName">
            <Input style="width:180px" v-model="departmentName" readonly />
          </FormItem>
        </Col>
        <Col span="6">
          <FormItem label="申请人" prop="applierName">
            <Input style="width:180px" v-model="applierName" readonly />
          </FormItem>
        </Col>
        <Col span="6">
          <FormItem label="申购原因" prop="purchaseResion">
            <Select style="width:180px" v-model="formValidate.purchaseResion" ref="purchaseResion">
              <Option
                v-for="item in purchaseResion"
                :value="item.value"
                :key="item.value"
              >{{ item.name }}</Option>
            </Select>
          </FormItem>
        </Col>
        <Col span="6">
          <FormItem label="需求时间" prop="demandTime">
            <DatePicker type="date" style="width: 180px" v-model="demandTime"></DatePicker>
          </FormItem>
        </Col>
        <Col span="6">
          <FormItem label="申购数量" prop="purchaseQty">
            <InputNumber style="width:180px" v-model="purchaseQty" :min="1" :max="20"></InputNumber>
            <Icon type="md-arrow-round-forward" slot="suffix" />
          </FormItem>
        </Col>
        <Col span="6">
          <FormItem label="申请时间" prop="applyTime">
            <DatePicker type="datetime" style="width: 180px" v-model="applyTime" readonly></DatePicker>
          </FormItem>
        </Col>
        <Col span="12">
          <FormItem label="可用顺序号" prop="availableNumber">
            <Input v-model="availableNumber" style="width:465px;" />
          </FormItem>
        </Col>
      </Row>
      <Row>
        <Col span="6" class="ivu-form" v-if="amount">
          <FormItem label="金额合计" prop="amount">
            <Input style="width:180px" v-model="amount" readonly />
          </FormItem>
        </Col>
        <Col span="18">
          <FormItem label="申购备注" prop="remark">
            <Input v-model="remark" style="width:740px;" />
          </FormItem>
        </Col>
      </Row>
      <Row>
        <Col span="6" v-if="auditAuth">
          <FormItem label="接收人" prop="auditorInfo">
            <Input style="width:180px" v-model="auditorInfo" readonly />
          </FormItem>
        </Col>
        <Col span="18" v-if="auditAuth">
          <FormItem label="意见描述" prop="auditRemark">
            <Input v-model="auditRemark" style="width:740px;" />
          </FormItem>
        </Col>
      </Row>
    </Form>
    <Table
      :columns="detailColumns"
      height="200"
      :data="detailList"
      border
      stripe
      highlight-row
      v-if="detailList.length>0"
    ></Table>
    <br />
    <Table
      :columns="logtableColumns"
      height="200"
      :data="logList"
      border
      stripe
      highlight-row
      v-if="logList.length>0"
    ></Table>
    <div slot="footer">
      <Button @click="handleAudit(1)" type="primary" v-if="auditAuth" :disabled="isDisabled">接收</Button>
      <Button @click="handleAudit(2)" type="error" v-if="auditAuth" :disabled="isDisabled">驳回</Button>
      <Button @click="handleReset()" style="margin-left: 8px">关闭</Button>
    </div>
  </Modal>
</template>
<script>
import { dateFormat, objCopy, hasOneOf } from "@/libs/tools.js";
import { getToken, getBaseUrl } from "@/libs/util";
import axios from "@/libs/api.request";
import qs from "qs";
import apply_form_config from "./apply-form-config.js";
import purchaseType from "../purchase-type.js";
import purchaseResion from "../purchase-resion.js";
export default {
  name: "page",
  computed: {
    access() {
      return this.$store.state.user.userData.data.access;
    },
    //审核(采购)
    purchaseAuth() {
      return hasOneOf(["010502"], this.access);
    },
    //审核(装备分厂)
    equipFactoryAuth() {
      return hasOneOf(["010503"], this.access);
    }
  },
  data() {
    return {
      isDisabled: false,
      applyStatus: 0,
      fixtureNumber: "",
      fixtureMap: "",
      fixtureName: "",
      partName: "",
      erpQty: 0,
      noCheckQty: 0,
      inventoryQty: 0,
      purchaseQty: 0,
      departmentName: "",
      applierName: "",
      demandTime: null,
      applyTime: null,
      availableNumber: "",
      remark: "",
      receiveModalshow: false,
      purchaseResion: purchaseResion,
      purchaseType: purchaseType,
      auditorInfo: "",
      auditRemark: "", //审核意见
      amount: null,
      logList: [],
      auditAuth: false,
      detailList: [],
      detailColumns: [
        {
          title: "配件编号",
          key: "fixtureNumber",
          minWidth: 120
        },
        {
          title: "配件名称",
          key: "fixtureName",
          minWidth: 270
        },
        {
          title: "配件图号",
          key: "fixtureMap",
          minWidth: 150
        },
        {
          title: "标准数量",
          key: "baseQty",
          width: 90
        },
        {
          title: "采购数量",
          key: "fixtureQty",
          width: 90
        },
        {
          title: "顺序号",
          key: "availableNumber",
          minWidth: 320
        }
      ],
      logtableColumns: [
        {
          title: "序号",
          key: "_index",
          width: 70,
          render: function(h, params) {
            return h("div", params.row._index + 1);
          }
        },
        {
          title: "审核部门",
          key: "auditDepartmentName",
          width: 120
        },
        {
          title: "审核人",
          key: "auditorName",
          width: 90
        },
        {
          title: "审核结果",
          key: "auditResult",
          width: 180
        },
        {
          title: "审核时间",
          key: "createTime",
          width: 150,
          render: function(h, params) {
            return h(
              "div",
              dateFormat(new Date(params.row.createTime), "yyyy-MM-dd HH:mm:ss")
            );
          }
        },
        {
          title: "备注",
          key: "remark",
          minWidth: 150
        }
      ],
      formValidate: apply_form_config.formValidate, //form表单字段
      ruleValidate: apply_form_config.ruleValidate //form表单验证规则
    };
  },
  created() {
    const self = this;
  },
  methods: {
    init() {
      const self = this;
      self.fixtureNumber = "";
      self.fixtureName = "";
      self.fixtureMap = "";
      self.formValidate = {};
    },
    showModal(pkId, applyStatus) {
      const self = this;
      self.applyStatus = applyStatus;
      self.auditRemark = "";
      self.getCurrentStaff();
      self.auditAuth = false;
      if (self.applyStatus == 4) {
        if (self.equipFactoryAuth) {
          self.auditAuth = true;
        }
      } else if (self.applyStatus == 7) {
        if (self.purchaseAuth) {
          self.auditAuth = true;
        }
      }
      self.getPurchaseApplyByPkId(pkId);
      self.receiveModalshow = true;
    },
    handleReset() {
      const self = this;
      self.receiveModalshow = false;
    },
    getPurchaseApplyByPkId(pkId) {
      const self = this;
      let para = {
        pkId: pkId
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/fixture/purchase-apply-get-by-id",
          isAuth: true,
          params: para,
          method: "post"
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200 && data.data !== null) {
            const detail = data.data;
            self.formValidate.pkId = detail.pkId;
            self.formValidate = objCopy(data.data, {});
            self.demandTime = dateFormat(
              new Date(detail.demandTime),
              "yyyy-MM-dd"
            );
            self.applyTime = dateFormat(
              new Date(detail.applyTime),
              "yyyy-MM-dd HH:mm:ss"
            );
            self.fixtureNumber = detail.fixtureNumber;
            self.fixtureName = detail.fixtureName;
            self.fixtureMap = detail.fixtureMap;
            self.partName = detail.partName;
            self.departmentName = detail.applyDepartmentName;
            self.applierName = detail.applierName;
            self.erpQty = detail.erpQty;
            self.inventoryQty = detail.inventoryQty;
            self.noCheckQty = detail.noCheckQty;
            self.amount = detail.amount;
            self.logList = detail.auditList;
            self.purchaseQty = detail.purchaseQty;
            self.availableNumber = detail.availableNumber;
            if (detail.fixtureType == 1) {
              self.detailList = detail.detailList;
            }
          }
        })
        .catch(function(err) {
          console.log(err);
        });
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
            self.auditorInfo =
              data.data.departmentName + "-" + data.data.staffName;
          }
        });
    },

    //审核
    handleAudit(result) {
      this.isDisabled = true;
      const self = this;
      let nextStatus;
      if (self.applyStatus == 7) {
        if (result == 1) {
          nextStatus = 8;
        } else {
          nextStatus = -9;
        }
      } else if (self.applyStatus == 4) {
        if (result == 1) {
          nextStatus = 9;
        } else {
          nextStatus = 3;
        }
      }
      let para = {
        pkId: self.formValidate.pkId,
        auditStatus: nextStatus,
        remark: self.auditRemark
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/fixture/purchase-apply-audit.json",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          self.receiveModalshow = false;
          if (res.data.code === 200) {
            self.$Message.success("申请单接收成功!");
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
  }
};
</script>
