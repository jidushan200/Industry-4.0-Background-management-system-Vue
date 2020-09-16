<style lang="less">
@import url("./purchase-apply-modal.less");
</style>
<template>
  <!--申请单信息弹出modal-->
  <Modal
    v-model="purchaseApplyModalshow"
    class="modal-class"
    :title="isModify?'申购单详情':'夹具申购单'"
    width="1160"
    draggable
  >
  <div style="height:480px;overflow-y:auto; overflow-x:hidden">
    <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="95" inline>
      <Row>
        <Col span="6">
          <FormItem label="夹具编码" prop="fixtureNumber" v-if="isModify?false:true">
            <Input
              style="width:180px"
              v-model="fixtureNumber"
              @on-blur="searchFixtureByNumber()"
              placeholder="输入夹具编码"
              ref="refocus"
            >
              <Icon type="ios-search" slot="suffix" />
            </Input>
          </FormItem>
          <FormItem label="夹具编码" prop="fixtureName" v-if="isModify?true:false">
            <Input style="width:180px" v-model="fixtureNumber" readonly></Input>
          </FormItem>
        </Col>
        <Col span="6">
          <FormItem label="图号名称" prop="fixtureBaseId" v-if="isModify?false:true">
            <Select
              style="width:180px"
              v-model="formValidate.fixtureBaseId"
              ref="baseId"
              placeholder="请选择图号名称"
              @on-change="searchFixtureBase()"
              clearable
              filterable
            >
              <Icon type="ios-search" slot="suffix" />
              <Option
                v-for="item in fixtureBaseList"
                :value="item.pkId"
                :key="item.pkId"
              >{{ item.fixtureName }}</Option>
            </Select>
          </FormItem>
        </Col>
        <Col span="6">
          <FormItem label="夹具名称" prop="fixtureName" v-if="isModify ? true:false">
            <Input style="width:180px" v-model="fixtureName" readonly></Input>
          </FormItem>
        </Col>
        <Col span="6">
          <FormItem label="夹具图号" prop="fixtureMap">
            <Input style="width:180px" v-model="fixtureMap" readonly></Input>
          </FormItem>
        </Col>
        <Col span="6">
          <FormItem label="制件" prop="partId">
            <Select
              style="width:170px"
              v-model="formValidate.partId"
              ref="part"
              @on-change="partListSelectChange"
              clearable
              filterable
            >
              <Option
                v-for="item in partList"
                :value="item.pkId"
                :key="item.pkId"
              >{{ item.partCode }}-{{ item.partName }}</Option>
            </Select>
          </FormItem>
        </Col>
      </Row>
      <Row>
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
            <Select
              style="width:180px"
              v-model="formValidate.purchaseType"
              ref="purchaseType"
              placeholder="请选择采购类型"
            >
              <Option
                v-for="item in purchaseType"
                :value="item.value"
                :key="item.value"
              >{{ item.name }}</Option>
            </Select>
          </FormItem>
        </Col>
      </Row>
      <Row>
        <Col span="6">
          <FormItem label="申购原因" prop="purchaseResion">
            <Select
              style="width:180px"
              v-model="formValidate.purchaseResion"
              ref="purchaseResion"
              placeholder="请选择采购原因"
            >
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
            <DatePicker
              type="date"
              style="width: 180px"
              :options="dateOption"
              v-model="formValidate.demandTime"
              placeholder="选择需求时间"
            ></DatePicker>
          </FormItem>
        </Col>
        <Col span="6">
          <FormItem label="申购数量" prop="purchaseQty">
            <InputNumber
              style="width:180px"
              v-model="formValidate.purchaseQty"
              :min="1"
              :max="20"
              placeholder="输入需求数量"
              @on-blur="summaryQty()"
              @on-change="summaryQty()"
            ></InputNumber>
            <Icon type="md-arrow-round-forward" slot="suffix" />
          </FormItem>
        </Col>
        <Col span="6" v-if="isModify">
          <FormItem label="申请时间" prop="applyTime">
            <DatePicker
              type="datetime"
              style="width: 180px"
              v-model="formValidate.applyTime"
              readonly
            ></DatePicker>
          </FormItem>
        </Col>
        <Col span="6" class="ivu-form" v-if="amount && applyStatus!=5">
          <FormItem label="金额合计" prop="amount">
            <Input style="width:180px" v-model="amount" readonly />
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
        <Col span="12">
          <FormItem label="可用顺序号" prop="availableNumber" v-if="!!availableNumber">
            <Input style="width:460px" v-model="availableNumber" readonly />
          </FormItem>
        </Col>
        <Col span="12">
          <FormItem label="申购备注" prop="remark">
            <Input v-model="formValidate.remark" style="width:460px;" />
          </FormItem>
        </Col>
      </Row>
      <Row
        style="border-top:1px solid #e3e3e3;padding-top: 20px;margin-top: 20px;"
        v-show="applyStatus > 0 && applyStatus < 10"
      >
        <Col span="24" class="ivu-form">
          <div class="ivu-form-item">
            <label class="ivu-form-item-label" style="width: 100px;text-align: right;">审核进度</label>
            <div class="ivu-form-item-content" style="margin-left: 100px; ">
              <Steps :current="currentStep">
                <Step
                  v-for="(item,key) in stepsValue"
                  :key="item.key"
                  :title="item.title"
                  :content="item.content"
                ></Step>
              </Steps>
            </div>
          </div>
        </Col>
        <Col span="6" class="ivu-form" v-show="auditAuth">
          <FormItem label="审核意见" prop="nextStatus">
            <RadioGroup v-model="nextStatus">
              <Radio :label="agree">{{agreeText}}</Radio>
              <Radio :label="disagree">{{disagreeText}}</Radio>
            </RadioGroup>
          </FormItem>
        </Col>
        <Col span="6" class="ivu-form" v-if="(applyStatus==5)">
          <FormItem label="金额合计" prop="amount">
            <Input style="width:180px" v-model="amount" />
          </FormItem>
        </Col>
        <Col span="6" v-if="auditAuth">
          <FormItem label="审核人" prop="auditorInfo">
            <Input style="width:160px" v-model="auditorInfo" readonly />
          </FormItem>
        </Col>

        <Col span="24" v-if="auditAuth">
          <FormItem label="意见描述" prop="auditRemark">
            <Input v-model="auditRemark" placeholder="意见描述" style="width:1020px;" />
          </FormItem>
        </Col>
      </Row>
    </Form>
    <Table
      :columns="childColumns"
      height="120"
      :data="childList"
      border
      stripe
      highlight-row
      v-if="childList.length>0"
    ></Table>
    <br />
    <Table
      :columns="logtableColumns"
      height="120"
      :data="logList"
      border
      stripe
      highlight-row
      v-if="logList.length>0"
    ></Table>
    </div>
    <div slot="footer">
      <Button
        @click="handleSave(0)"
        type="primary"
        v-if="applyStatus<=0 && factoryAuth"
        :disabled="isDisable"
      >保存</Button>
      <Button
        @click="handleSave(1)"
        type="warning"
        v-if="applyStatus<=0 && factoryAuth"
        :disabled="isDisable"
      >提交</Button>
      <Button @click="handleAudit()" type="primary" v-if="auditAuth" :disabled="isDisable">提交</Button>
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
    //保存、提交、删除(分厂)
    factoryAuth() {
      return hasOneOf(["01050101"], this.access);
    },
    //审核（分厂领导）
    factoryLeaderAuth() {
      return hasOneOf(["01050102"], this.access);
    },
    //审核(工艺部)
    technologyAuth() {
      return hasOneOf(["01050103"], this.access);
    },
    //审核(圆柱分厂 )
    equipFactoryAuth() {
      return hasOneOf(["01050104"], this.access);
    },
    //审核(采购)
    purchaseAuth() {
      return hasOneOf(["01050105"], this.access);
    },
    //审核（主管领导-新品夹具）
    leaderResearchAuth() {
      return hasOneOf(["0105010601"], this.access);
    },
    //审核(主管领导审核-常用夹具)
    leaderFactoryAuth() {
      return hasOneOf(["0105010602"], this.access);
    }
  },
  data() {
    return {
      isDisable: false,
      isModify: false,
      auditAuth: false,
      applyStatus: 0,
      fixtureNumber: "",
      fixtureMap: "",
      fixtureBaseId: null,
      fixtureName: "",
      fixtureType: 2,
      availableNumber: "",
      erpQty: 0,
      noCheckQty: 0,
      inventoryQty: 0,
      departmentName: "",
      applierName: "",
      fixtureBaseList: [],
      partList: [],
      purchaseApplyModalshow: false,
      purchaseResion: purchaseResion,
      purchaseType: purchaseType,
      dateOption: {
        disabledDate(date) {
          return date && date.valueOf() < Date.now() - 86400000;
        }
      },
      auditStatus: false,
      agree: 2,
      disagree: -1,
      agreeText: "同意",
      disagreeText: "不同意",
      currentStep: 0,
      stepArray: [
        { title: "提交", content: "分厂提交夹具申购单", key: 0 },
        { title: "审核", content: "分厂领导审核", key: 1 },
        { title: "审核", content: "工艺部审核", key: 2 },
        { title: "审核", content: "圆柱分厂审核", key: 3 },
        { title: "审核", content: "采购部确认外购总价", key: 4 },
        { title: "审核", content: "主管领导审核", key: 5 },
        { title: "执行", content: "待采购部接收", key: 6 }
      ],
      stepResearchArray: [
        { title: "提交", content: "分厂提交夹具申购单", key: 0 },
        { title: "审核", content: "分厂领导审核", key: 1 },
        { title: "审核", content: "圆柱分厂审核", key: 2 },
        { title: "审核", content: "采购部确认外购总价", key: 3 },
        { title: "审核", content: "主管领导审核", key: 4 },
        { title: "执行", content: "待采购部接收", key: 5 }
      ],
      stepSelfArray: [
        { title: "提交", content: "分厂提交夹具申购单", key: 0 },
        { title: "审核", content: "分厂领导审核", key: 1 },
        { title: "审核", content: "工艺部审核", key: 2 },
        { title: "审核", content: "圆柱分厂审核", key: 3 },
        { title: "审核", content: "圆柱分厂接收", key: 4 }
      ],
      stepResearchSelfArray: [
        { title: "提交", content: "分厂提交夹具申购单", key: 0 },
        { title: "审核", content: "分厂领导审核", key: 1 },
        { title: "审核", content: "圆柱分厂审核", key: 2 },
        { title: "审核", content: "圆柱分厂接收", key: 3 }
      ],
      stepsValue: [],
      nextStatus: null, //审核结果
      auditorInfo: "",
      auditRemark: "", //审核意见
      amount: null,
      logList: [],
      childList: [],
      childColumns: [
        {
          title: "配件编号",
          key: "fixtureNumber",
          minWidth: 220
        },
        {
          title: "配件名称",
          key: "fixtureName",
          minWidth: 300
        },
        {
          title: "配件图号",
          key: "fixtureMap",
          minWidth: 160
        },
        {
          title: "标准数量",
          key: "baseQty",
          width: 100
        },
        {
          title: "采购数量",
          key: "fixtureQty",
          width: 100
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
    self.getPartListData();
  },
  methods: {
    init() {
      const self = this;
      self.fixtureNumber = "";
      self.fixtureName = "";
      self.fixtureMap = "";
      self.availableNumber = "";
      self.formValidate = {};
      self.formValidate.purchaseQty = 1;
      self.amount = null;
      self.logList = [];
      self.childList = [];
    },
    showModal(pkId, applyStatus, purchaseType) {
      const self = this;
      self.auditAuth = false;
      self.stepsValue = [];

      //新增
      if (!!!pkId) {
        self.applyStatus = applyStatus;
        self.isModify = false;
        self.auditAuth = false;
        self.init();
        self.getBaseList();
        self.getCurrentStaff();
      } else {
        self.isModify = true;
        self.auditRemark = "";
        self.applyStatus = applyStatus;
        if (self.applyStatus) {
          self.getCurrentStaff();
        }
        self.nextStatus = null;
        if (self.applyStatus == 1) {
          if (purchaseType == 1) {
            self.stepsValue = self.stepResearchArray;
          } else {
            self.stepsValue = self.stepArray;
          }
          self.currentStep = 0;
          self.agree = 2;
          self.disagree = -1;
          self.agreeText = "同意";
          self.disagreeText = "不同意";
          if (self.factoryLeaderAuth) {
            self.auditAuth = true;
          }
          self.nextStatus = 2;
          self.getCurrentStaff();
        } else if (self.applyStatus == 2) {
          self.stepsValue = self.stepArray;
          self.currentStep = 1;
          self.agree = 3;
          self.disagree = -2;
          self.agreeText = "同意";
          self.disagreeText = "不同意";
          self.getCurrentStaff();
          if (self.technologyAuth) {
            self.auditAuth = true;
          }
          self.nextStatus = 3;
        } else if (self.applyStatus == 3) {
          if (purchaseType == 1) {
            self.stepsValue = self.stepResearchArray;
            self.currentStep = 1;
          } else {
            self.stepsValue = self.stepArray;
            self.currentStep = 2;
          }
          self.agree = 4;
          self.disagree = 5;
          self.agreeText = "自制";
          self.disagreeText = "外购";
          if (self.equipFactoryAuth) {
            self.auditAuth = true;
          }
          self.nextStatus = 4;
        } else if (self.applyStatus == 4) {
          if (purchaseType == 1) {
            self.stepsValue = self.stepResearchSelfArray;
            self.currentStep = 2;
          } else {
            self.stepsValue = self.stepSelfArray;
            self.currentStep = 3;
          }
        } else if (self.applyStatus == 5) {
          if (purchaseType == 1) {
            self.stepsValue = self.stepResearchArray;
            self.currentStep = 2;
          } else {
            self.stepsValue = self.stepArray;
            self.currentStep = 3;
          }
          self.agree = 6;
          self.disagree = -2;
          self.agreeText = "同意";
          self.disagreeText = "不同意";
          if (self.purchaseAuth) {
            self.auditAuth = true;
          }
          self.nextStatus = 6; //6或者7 金额小于2000 状态改为7 否则改为6
        } else if (self.applyStatus == 6) {
          if (purchaseType == 1) {
            self.stepsValue = self.stepResearchArray;
            self.currentStep = 3;
            if (self.leaderResearchAuth) {
              self.auditAuth = true;
            }
          } else {
            self.stepsValue = self.stepArray;
            self.currentStep = 4;
            if (self.leaderFactoryAuth) {
              self.auditAuth = true;
            }
          }
          self.agree = 7;
          self.disagree = 5; //主管驳回到采购部
          self.agreeText = "同意";
          self.disagreeText = "不同意";
          self.nextStatus = 7;
        } else if (self.applyStatus == 7) {
          if (purchaseType == 1) {
            self.stepsValue = self.stepResearchArray;
            self.currentStep = 4;
          } else {
            self.stepsValue = self.stepArray;
            self.currentStep = 5;
          }
          self.auditAuth = false;
        } else if (self.applyStatus == 8) {
          if (purchaseType == 1) {
            self.stepsValue = self.stepResearchArray;
            self.currentStep = 5;
          } else {
            self.stepsValue = self.stepArray;
            self.currentStep = 6;
          }
          self.auditAuth = false;
        } else if (self.applyStatus == 9) {
          if (purchaseType == 1) {
            self.stepsValue = self.stepResearchArray;
            self.currentStep = 4;
          } else {
            self.stepsValue = self.stepArray;
            self.currentStep = 5;
          }
        }
        self.childList = [];
        self.getPurchaseApplyByPkId(pkId);
      }
      self.purchaseApplyModalshow = true;
    },
    handleReset() {
      const self = this;
      self.purchaseApplyModalshow = false;
    },
    validateFixtureNumber() {
      const self = this;
      self.formValidate.fixtureNumber = self.fixtureNumber;
    },

    summaryQty() {
      const self = this;
      debugger;
      if (self.fixtureType == 2 || !!!self.formValidate.purchaseQty) {
        return;
      }
      const purchaseQty = self.formValidate.purchaseQty;
      let arr = [];
      for (const item of self.childList) {
        item.fixtureQty = item.baseQty * purchaseQty;
        arr.push(item);
      }
      self.childList = arr;
    },

    partListSelectChange(item) {
      let partList = this.partList;
      if (item !== undefined) {
        for (const part of partList) {
          if (part.pkId === item) {
            this.formValidate.partName = part.partName;
            this.formValidate.partCode = part.partCode;
          }
        }
      }
    },

    getPartListData() {
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/base/part-list.json",
          isAuth: true,
          method: "post"
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            self.partList = data.data;
          }
        })
        .catch(function(err) {
          console.log(err);
        });
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
            self.formValidate.demandTime = dateFormat(
              new Date(self.formValidate.demandTime),
              "yyyy-MM-dd"
            );
            self.formValidate.applyTime = dateFormat(
              new Date(self.formValidate.applyTime),
              "yyyy-MM-dd HH:mm:ss"
            );
            self.fixtureNumber = detail.fixtureNumber;
            self.fixtureName = detail.fixtureName;
            self.fixtureMap = detail.fixtureMap;
            self.departmentName = detail.applyDepartmentName;
            self.applierName = detail.applierName;
            self.erpQty = detail.erpQty;
            self.inventoryQty = detail.inventoryQty;
            self.noCheckQty = detail.noCheckQty;
            self.amount = detail.amount;
            self.availableNumber = detail.availableNumber;
            self.logList = detail.auditList;
            if (detail.fixtureType == 1 && !!detail.detailList) {
              self.childList = detail.detailList;
            }
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },
    getBaseList() {
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/fixture/get-base-list",
          isAuth: true,
          method: "post"
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200 && data.data !== null) {
            self.fixtureBaseList = data.data;
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
            if (self.applyStatus == 0) {
              self.departmentName = data.data.departmentName;
              self.departmentId = data.data.departmentId;
              self.applierName = data.data.staffName;
            } else if (self.applyStatus > 0) {
              self.auditorInfo =
                data.data.departmentName + "-" + data.data.staffName;
            }
          }
        });
    },

    searchFixtureByNumber() {
      const self = this;
      self.childList = [];
      if (!!!self.fixtureNumber) {
        return;
      } else {
        self.formValidate.fixtureNumber = self.fixtureNumber;
        let para = {
          fixtureNumber: self.fixtureNumber
        };
        axios
          .request({
            headers: {
              "Content-Type": "application/json; charset=UTF-8"
            },
            url: "/fixture/purchase-get-by-fixture-number.json",
            method: "post",
            isAuth: true,
            params: para
          })
          .then(function(res) {
            const data = res.data;
            if (data.code === 200 && data.data.fixtureMap !== null) {
              const basefixture = data.data;
              self.formValidate.fixtureBaseId = basefixture.pkId;
              self.fixtureMap = basefixture.fixtureMap;
              self.fixtureNumber = basefixture.fixtureNumber;
              self.fixtureName = basefixture.fixtureName;
              self.formValidate.fixtureNumber = basefixture.fixtureNumber;
              self.inventoryQty = basefixture.inventoryQty;
              self.erpQty = basefixture.erpQty;
              self.noCheckQty = basefixture.noCheckQty;
              self.fixtureType = basefixture.fixtureType;
              if (basefixture.fixtureType == 1 && !!basefixture.childList) {
                self.childList = basefixture.childList;
              }
            } else {
              self.$Notice.error({
                title: "错误提示",
                desc: "物料不存在"
              });
            }
          })
          .catch(function(err) {
            console.log(err);
          });
      }
    },

    searchFixtureBase() {
      const self = this;
      self.childList = [];
      if (!!!self.formValidate.fixtureBaseId) {
        return;
      } else {
        let para = {
          pkId: self.formValidate.fixtureBaseId,
          postType: 1
        };
        axios
          .request({
            headers: {
              "Content-Type": "application/json; charset=UTF-8"
            },
            url: "/fixture/fixture-base-get-by-id.json",
            method: "post",
            isAuth: true,
            params: para
          })
          .then(function(res) {
            const data = res.data;
            if (data.code === 200) {
              const basefixture = data.data;
              self.fixtureMap = basefixture.fixtureMap;
              self.fixtureNumber = basefixture.fixtureNumber;
              self.formValidate.fixtureNumber = basefixture.fixtureNumber;
              self.fixtureName = basefixture.fixtureName;
              self.formValidate.fixtureNumber = basefixture.fixtureNumber;
              self.inventoryQty = basefixture.inventoryQty;
              self.erpQty = basefixture.erpQty;
              self.fixtureType = basefixture.fixtureType;
              self.noCheckQty = basefixture.noCheckQty;
              if (basefixture.fixtureType == 1 && !!basefixture.childList) {
                self.childList = basefixture.childList;
              }
            } else {
              self.fixtureMap = "";
              (self.fixtureNumber = ""), (self.erpQty = null);
              self.noCheckQty = null;
              self.$Message.error("请先完善相关物料图号信息");
            }
          })
          .catch(function(err) {
            console.log(err);
          });
      }
    },

    //审核
    handleAudit() {
      this.isDisabled = true;
      const self = this;
      if (!!!self.nextStatus) {
        self.$Message.error("请选择审核意见");
        return;
      }
      let para = {
        pkId: self.formValidate.pkId,
        auditStatus: self.nextStatus,
        remark: self.auditRemark
      };
      if (self.applyStatus == 5) {
        if (!!!self.amount) {
          self.$Message.error("请填写总价");
          return;
        }
        para.amount = self.amount;
      }
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
          self.purchaseApplyModalshow = false;
          if (res.data.code === 200) {
            self.$Message.success("申请单审核成功!");
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
    },

    handleSave(applyStatus) {
      this.isDisabled = true;
      const self = this;
      self.summaryQty()
      self.formValidate.applyStatus = applyStatus;
      self.$refs["formValidate"].validate(valid => {
        if (valid) {
          self.isDisable = true;
          self.formValidate.demandTime = dateFormat(
            new Date(self.formValidate.demandTime),
            "yyyy-MM-dd"
          );
          let para = {
            fixtureNumber: self.formValidate.fixtureNumber,
            fixtureMap: self.fixtureMap,
            fixtureName: self.fixtureName,
            fixtureType: self.fixtureType,
            partCode: self.formValidate.partCode,
            partName: self.formValidate.partName,
            inventoryQty: self.inventoryQty,
            erpQty: self.erpQty,
            noCheckQty: self.noCheckQty,
            demandTime: self.formValidate.demandTime,
            purchaseQty: self.formValidate.purchaseQty,
            applyDepartmentId: self.departmentId,
            applyDepartmentName: self.departmentName,
            purchaseType: self.formValidate.purchaseType,
            purchaseResion: self.formValidate.purchaseResion,
            applyStatus: self.formValidate.applyStatus,
            remark: self.formValidate.remark,
            details: JSON.stringify(self.childList)
          };
          let url = "";
          if (self.isModify) {
            para.pkId = self.formValidate.pkId;
            url = "/fixture/purchase-apply-update";
          } else {
            url = "/fixture/purchase-apply-add";
          }
          axios
            .request({
              headers: {
                "Content-Type": "application/x-www-form-urlencoded"
              },
              url: url,
              method: "post",
              isAuth: true,
              data: qs.stringify(para)
            })
            .then(function(res) {
              self.purchaseApplyModalshow = false;
              if (res.data.code === 200) {
                self.$Message.success("采购申请保存成功!");
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
            });
        } else {
          self.$Message.error("请输入正确信息!");
        }
      });
    },
    downFile(pkId) {
      window.location.href =
        getBaseUrl() +
        "/tool/appendix-download?pkId=" +
        pkId +
        "&token=" +
        getToken();
    }
  }
};
</script>
