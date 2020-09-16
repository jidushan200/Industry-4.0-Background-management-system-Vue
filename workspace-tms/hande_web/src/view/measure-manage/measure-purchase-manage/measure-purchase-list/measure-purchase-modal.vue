<template>
  <!--审核信息弹出modal-->
  <Modal
    v-model="measurePurchaseModalshow "
    :title="isModify?'申购单详情':'量具申购单'"
    width="980"
    draggable
  >
    <div style="height:320px;overflow-y:auto; overflow-x:hidden">
      <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="90" inline>
        <Row>
          <Col span="8">
            <FormItem label="物料名称" prop="measureNumber" v-if="isModify?false:true">
              <Select
                style="width:200px"
                v-model="formValidate.measureNumber"
                ref="baseId"
                placeholder="请选择物料名称"
                @on-change="searchMeasure()"
                clearable
                filterable
              >
                <Icon type="ios-search" slot="suffix" />
                <Option
                  v-for="item in baseList"
                  :value="item.measureNumber"
                  :key="item.measureNumber"
                >{{ item.measureName }}</Option>
              </Select>
            </FormItem>
          </Col>
          <Col span="8">
            <FormItem label="物料名称" prop="measureName" v-if="isModify?true:false">
              <Input style="width:200px" v-model="measureName" readonly></Input>
            </FormItem>
          </Col>
          <Col span="8">
            <FormItem label="物料编码" prop="measureCode">
              <Input style="width:200px" v-model="measureCode" readonly></Input>
            </FormItem>
          </Col>
          <Col span="8">
            <FormItem label="型号规格" prop="model">
              <Input style="width:200px" v-model="model" readonly />
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span="8">
            <FormItem label="TMS库存" prop="inventoryQty">
              <Input style="width:200px" v-model="inventoryQty" readonly />
            </FormItem>
          </Col>
          <Col span="8">
            <FormItem label="ERP库存" prop="erpQty">
              <Input style="width:200px" v-model="erpQty" readonly />
            </FormItem>
          </Col>
          <Col span="8">
            <FormItem label="ERP待检数量" prop="noCheckQty">
              <Input style="width:200px" v-model="noCheckQty" readonly />
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span="8">
            <FormItem label="申请部门" prop="applyDepartment">
              <Input style="width:200px" v-model="applyDepartment" readonly />
            </FormItem>
          </Col>
          <Col span="8">
            <FormItem label="申请人" prop="applicant">
              <Input style="width:200px" v-model="applicant" readonly />
            </FormItem>
          </Col>
          <Col span="8">
            <FormItem label="申购原因" prop="purchaseReasion">
              <Select
                style="width:200px"
                v-model="formValidate.purchaseReasion"
                ref="purchaseResion"
                placeholder="请选择采购原因"
                :disabled="isAudit"
              >
                <Option
                  v-for="item in purchaseReasion"
                  :value="item.value"
                  :key="item.value"
                >{{ item.name }}</Option>
              </Select>
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span="8">
            <FormItem label="需求时间" prop="demandTime">
              <DatePicker
                type="date"
                style="width: 200px"
                v-model="formValidate.demandTime"
                :options="dateOption"
                placeholder="选择需求时间"
                :readonly="isAudit"
              ></DatePicker>
            </FormItem>
          </Col>
          <Col span="8">
            <FormItem label="申购数量" prop="applyQty">
              <InputNumber
                style="width:200px"
                :max="20"
                :min="1"
                v-model="formValidate.applyQty"
                :readonly="isAudit"
              ></InputNumber>
              <Icon type="md-arrow-round-forward" slot="suffix" />
            </FormItem>
          </Col>
          <Col span="8" v-if="isModify">
            <FormItem label="申购时间" prop="applyTime">
              <DatePicker
                type="datetime"
                style="width: 200px"
                v-model="formValidate.applyTime"
                readonly
              ></DatePicker>
            </FormItem>
          </Col>
        </Row>
        <Row
          style="border-top:1px solid #e3e3e3;padding-top: 40px;margin-top: 20px;"
          v-if="isModify"
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
          <Col span="8" v-if="radioAudit" class="ivu-form">
            <FormItem label="审核意见" prop="agreeFlag">
              <RadioGroup v-model="agreeFlag">
                <Radio :label="agreeStatus">{{agreeText}}</Radio>
                <Radio :label="disagreeStatus">{{disagreeText}}</Radio>
              </RadioGroup>
            </FormItem>
          </Col>
          <Col span="8">
            <FormItem label="审核人" prop="auditStaffInfo" v-if="radioAudit">
              <Input style="width:160px" v-model="auditStaffInfo" readonly />
            </FormItem>
          </Col>
          <Col span="8">
            <FormItem label="价格" prop="purchasePrice" v-if="isPurchase">
              <Input
                style="width:160px"
                v-model="formValidate.purchasePrice"
                @on-change="changePrice()"
                :readonly="!isPrice"
              />
            </FormItem>
          </Col>
          <Col span="24" v-if="radioAudit">
            <FormItem label="意见描述" prop="auditRemark">
              <Input
                v-model="auditRemark"
                type="textarea"
                :rows="2"
                placeholder="意见描述"
                style="width:830px;"
              />
            </FormItem>
          </Col>
        </Row>
      </Form>
    </div>
    <Table
      :columns="logtableColumns"
      height="200"
      :data="logList"
      border
      stripe
      highlight-row
      v-if="isModify"
    ></Table>

    <div slot="footer">
      <Button
        @click="handleSave('formValidate')"
        v-if="btnSave && purchaseAuth"
        type="primary"
        :disabled="isDisabled"
      >保存</Button>
      <Button
        @click="handleSubmit('formValidate')"
        v-if="btnSubmit && purchaseAuth"
        type="warning"
        :disabled="isDisabled"
      >提交</Button>
      <Button @click="handleAudit()" type="primary" v-if="btnAudit">{{auditText}}</Button>
      <Button @click="handleReset('formValidate')" style="margin-left: 8px">关闭</Button>
    </div>
  </Modal>
</template>
<script>
import { dateFormat, objCopy, hasOneOf } from "@/libs/tools.js";
import measure_purchase_manage_form_config from "./measure-purchase.js";
import purchaseReasion from "./purchaseResion-type.js";
import axios from "@/libs/api.request";
export default {
  computed: {
    access() {
      return this.$store.state.user.userData.data.access;
    },
    //保存、提交、删除（生产部）
    purchaseAuth() {
      return hasOneOf(["01040101"], this.access);
    },
    //审核(分厂)
    productAuth() {
      return hasOneOf(["01040102"], this.access);
    },
    //审核(圆柱分厂)
    workshopAuth() {
      return hasOneOf(["01040103"], this.access);
    },
    //审核(采购部补充价格)
    purchasePriceAuth() {
      return hasOneOf(["01040104"], this.access);
    },
    //审核(主管领导)
    leaderAuth() {
      return hasOneOf(["01040105"], this.access);
    }
  },
  data() {
    return {
      isDisabled: false,
      stepsValue: [
        { title: "提交", content: "分厂填写量具申购报告并提交", key: 1 },
        { title: "审核", content: "分厂领导审核", key: 2 },
        { title: "审核", content: "圆柱分厂自制判断", key: 3 },
        { title: "审核", content: "采购部确认外购价格", key: 4 },
        { title: "审核", content: "主管领导审核", key: 5 },
        { title: "执行", content: "待采购部接收", key: 6 }
      ], // 默认的steps数组，动态生成这个数组
      isPurchase: false,
      isPrice: false,
      measureCode: "",
      measureName: "",
      model: "",
      inventoryQty: "",
      applyDepartment: "",
      applicant: "",
      erpQty: "",
      noCheckQty: "",
      auditStaffInfo: "",
      auditText: "提交",
      agreeText: "同意",
      disagreeText: "不同意",
      currentStep: 0,
      agreeFlag: 2,
      agreeStatus: 2,
      disagreeStatus: -1,
      baseList: [],
      logList: [],
      // departmentList: [],
      // teamList: [],
      // staffList: [],
      isAudit: false,
      isModify: false,
      btnSave: true,
      btnSubmit: true,
      btnAudit: false,
      measurePurchaseModalshow: false,
      purchaseReasion: purchaseReasion,
      logtableColumns: [
        {
          title: "审核次序",
          key: "auditSeq",
          width: 100
        },
        {
          title: "审核部门",
          key: "auditDepartmentName",
          width: 160
        },
        {
          title: "审核领导",
          key: "auditorName",
          width: 90
        },
        {
          title: "审核结果",
          key: "auditResult",
          width: 160
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
      dateOption: {
        disabledDate(date) {
          return date && date.valueOf() < Date.now() - 86400000;
        }
      },
      formValidate: measure_purchase_manage_form_config.formValidate, //user form表单字段
      ruleValidate: measure_purchase_manage_form_config.ruleValidate //user form表单验证规则
    };
  },
  created() {
    const self = this;
    // self.getDepartmentListData();
    self.getBaseListData();
  },
  methods: {
    getBaseListData() {
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/measure/measure-base-list",
          isAuth: true,
          method: "post"
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200 && data.data !== null) {
            self.baseList = data.data;
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },

    addModal() {
      const self = this;
      self.measurePurchaseModalshow = true;
      self.logList = [];
      self.isModify = false;
      self.isAudit = false;
      self.btnSave = true;
      self.btnSubmit = true;
      self.btnAudit = false;
      self.radioAudit = false;
      self.radioAudit = false;
      self.measureNumber = "";
      self.measureName = "";
      self.model = "";
      self.inventoryQty = "";
      self.erpQty = "";
      self.noCheckQty = "";
      self.formValidate = {};
      self.formValidate.applyQty = 1;
      self.searchStaff(this.$store.state.user.userData.data.userId);
    },
    show(index) {
      const self = this;
      self.stepsValue = [
        { title: "提交", content: "分厂填写量具申购报告并提交", key: 1 },
        { title: "审核", content: "分厂领导审核", key: 2 },
        { title: "审核", content: "圆柱分厂自制判断", key: 3 },
        { title: "审核", content: "采购部确认外购价格", key: 4 },
        { title: "审核", content: "主管领导审核", key: 5 },
        { title: "执行", content: "待采购部接收", key: 6 }
      ]; // 默认的steps数组，动态生成这个数组
      self.measurePurchaseModalshow = true;
      self.isModify = true;
      self.agreeText = "同意";
      self.disagreeText = "不同意";
      self.auditStaffInfo = "";
      self.formValidate = objCopy(index, {});
      self.searchStaff(self.formValidate.applierId);
      self.loadLogList(self.formValidate.pkId);
      self.model = self.formValidate.model;
      self.measureName = self.formValidate.measureName;
      self.measureCode = self.formValidate.measureNumber;
      self.inventoryQty = self.formValidate.inventoryQty;
      self.erpQty = self.formValidate.erpQty;
      self.noCheckQty = self.formValidate.noCheckQty;
      self.formValidate.demandTime = dateFormat(
        new Date(self.formValidate.demandTime),
        "yyyy-MM-dd HH:mm:ss"
      );
      self.formValidate.applyTime = dateFormat(
        new Date(self.formValidate.applyTime),
        "yyyy-MM-dd HH:mm:ss"
      );
      self.auditRemark = "";
      const auditStatus = self.formValidate.applyStatus;
      if (auditStatus == 0 || auditStatus == -1) {
        if (this.purchaseAuth) {
          self.btnSave = true;
          self.btnSubmit = true;
        } else {
          self.btnSave = false;
          self.btnSubmit = false;
        }
        self.currentStep = 0;
        self.isPurchase = false;
        self.isPrice = false;
        self.btnAudit = false;
        self.radioAudit = false;
        self.stepsValue = [
          { title: "提交", content: "分厂填写量具申购报告并提交", key: 1 },
          { title: "审核", content: "分厂领导审核", key: 2 },
          { title: "审核", content: "圆柱分厂自制判断", key: 3 },
          { title: "审核", content: "采购部确认外购价格", key: 4 },
          { title: "审核", content: "主管领导审核", key: 5 },
          { title: "执行", content: "待采购部接收", key: 6 }
        ];
      } else if (auditStatus == 1 || auditStatus == -2) {
        self.btnSave = false;
        self.btnSubmit = false;
        if (this.productAuth) {
          self.isAudit = true;
          self.btnAudit = true;
          self.radioAudit = true;
        } else {
          self.isAudit = false;
          self.btnAudit = false;
          self.radioAudit = false;
        }
        self.searchAuditStaff();
        self.currentStep = 1;
        self.isPurchase = false;
        self.isPrice = false;
        self.agreeFlag = 2;
        self.agreeStatus = 2;
        self.disagreeStatus = -1;
        self.stepsValue = [
          { title: "提交", content: "分厂填写量具申购报告并提交", key: 1 },
          { title: "审核", content: "分厂领导审核", key: 2 },
          { title: "审核", content: "圆柱分厂自制判断", key: 3 },
          { title: "审核", content: "采购部确认外购价格", key: 4 },
          { title: "审核", content: "主管领导审核", key: 5 },
          { title: "执行", content: "待采购部接收", key: 6 }
        ];
      } else if (auditStatus == 2 || auditStatus == -4) {
        if (this.workshopAuth) {
          self.isAudit = true;
          self.btnAudit = true;
          self.radioAudit = true;
        } else {
          self.isAudit = false;
          self.btnAudit = false;
          self.radioAudit = false;
        }
        self.agreeText = "外购";
        self.disagreeText = "自制";
        self.searchAuditStaff();
        self.currentStep = 2;
        self.btnSave = false;
        self.btnSubmit = false;
        self.isPurchase = false;
        self.isPrice = false;
        self.agreeFlag = 4;
        self.agreeStatus = 4;
        self.disagreeStatus = 3;
        self.stepsValue = [
          { title: "提交", content: "分厂填写量具申购报告并提交", key: 1 },
          { title: "审核", content: "分厂领导审核", key: 2 },
          { title: "审核", content: "圆柱分厂自制判断", key: 3 },
          { title: "审核", content: "采购部确认外购价格", key: 4 },
          { title: "审核", content: "主管领导审核", key: 5 },
          { title: "执行", content: "待采购部接收", key: 6 }
        ];
      } else if (auditStatus == 3) {
        self.currentStep = 3;
        self.isAudit = true;
        self.btnAudit = false;
        self.radioAudit = false;
        self.isPurchase = false;
        self.isPrice = false;
        self.btnSave = false;
        self.btnSubmit = false;
        self.stepsValue = [
          { title: "提交", content: "分厂填写量具申购报告并提交", key: 1 },
          { title: "审核", content: "分厂领导审核", key: 2 },
          { title: "审核", content: "圆柱分厂自制判断", key: 3 },
          { title: "执行", content: "圆柱分厂判断自制", key: 4 }
        ];
      } else if (auditStatus == 4 || auditStatus == -5) {
        if (this.purchasePriceAuth) {
          self.isAudit = true;
          self.btnAudit = true;
          self.radioAudit = true;
        } else {
          self.isAudit = false;
          self.btnAudit = false;
          self.radioAudit = false;
        }
        self.isPurchase = true;
        self.isPrice = true;
        self.agreeText = "同意";
        self.disagreeText = "不同意";
        self.searchAuditStaff();
        self.currentStep = 3;
        self.btnSave = false;
        self.btnSubmit = false;
        self.agreeFlag = 5;
        self.agreeStatus = 5;
        self.disagreeStatus = -4;
        self.stepsValue = [
          { title: "提交", content: "分厂填写量具申购报告并提交", key: 1 },
          { title: "审核", content: "分厂领导审核", key: 2 },
          { title: "审核", content: "圆柱分厂自制判断", key: 3 },
          { title: "审核", content: "采购部完善采购价格", key: 4 },
          { title: "审核", content: "主管领导审核", key: 5 },
          { title: "执行", content: "待采购部接收", key: 6 }
        ];
      } else if (auditStatus == 5 || auditStatus == -6) {
        if (this.leaderAuth) {
          self.isAudit = true;
          self.btnAudit = true;
          self.radioAudit = true;
        } else {
          self.isAudit = false;
          self.btnAudit = false;
          self.radioAudit = false;
        }
        self.isPurchase = true;
        self.isPrice = false;
        self.agreeText = "同意";
        self.disagreeText = "不同意";
        self.searchAuditStaff();
        self.currentStep = 4;
        self.btnSave = false;
        self.btnSubmit = false;
        self.agreeFlag = 6;
        self.agreeStatus = 6;
        self.disagreeStatus = -5;
        self.stepsValue = [
          { title: "提交", content: "分厂填写量具申购报告并提交", key: 1 },
          { title: "审核", content: "分厂领导审核", key: 2 },
          { title: "审核", content: "圆柱分厂自制判断", key: 3 },
          { title: "审核", content: "采购部完善采购价格", key: 4 },
          { title: "审核", content: "主管领导审核", key: 5 },
          { title: "执行", content: "待采购部接收", key: 6 }
        ];
      } else {
        //self.searchSeq();
        self.isPurchase = false;
        self.isPrice = false;
        if (self.formValidate.purchasePrice > 2000) {
          self.currentStep = 5;
          self.stepsValue = [
            { title: "提交", content: "分厂填写量具申购报告并提交", key: 1 },
            { title: "审核", content: "分厂领导审核", key: 2 },
            { title: "审核", content: "圆柱分厂自制判断", key: 3 },
            { title: "审核", content: "采购部完善采购价格", key: 4 },
            { title: "审核", content: "主管领导审核", key: 5 },
            { title: "执行", content: "待采购部接收", key: 6 }
          ];
        } else {
          self.currentStep = 4;
          self.stepsValue = [
            { title: "提交", content: "分厂填写量具申购报告并提交", key: 1 },
            { title: "审核", content: "分厂领导审核", key: 2 },
            { title: "审核", content: "圆柱分厂自制判断", key: 3 },
            { title: "审核", content: "采购部完善采购价格", key: 4 },
            { title: "执行", content: "待采购部接收", key: 5 }
          ];
        }
        self.isAudit = true;
        self.btnSave = false;
        self.btnSubmit = false;
        self.btnAudit = false;
        self.radioAudit = false;
      }
    },
    addNewPurchase() {
      const self = this;
      self.formValidate.demandTime = dateFormat(
        new Date(self.formValidate.demandTime),
        "yyyy-MM-dd"
      );
      if (!!!self.model) {
        self.$Notice.error({
          title: "错误提示",
          desc: "请完善物料图号信息"
        });
      } else {
        this.isDisabled = true;
        let para = {
          measureNumber: self.formValidate.measureNumber,
          model: self.model,
          measureName: self.measureName,
          applyQty: self.formValidate.applyQty,
          demandTime: self.formValidate.demandTime,
          purchaseReasion: self.formValidate.purchaseReasion,
          inventoryQty: self.inventoryQty,
          erpQty: self.erpQty,
          noCheckQty: self.noCheckQty,
          applyDepartmentId: self.formValidate.applyDepartmentId,
          applyDepartmentName: self.formValidate.applyDepartmentName,
          applyStatus: self.formValidate.applyStatus,
          remark: self.formValidate.remark
        };

        axios
          .request({
            headers: {
              "Content-Type": "application/json; charset=UTF-8"
            },
            url: "/measure/measure-purchase-apply-add",
            method: "post",
            isAuth: true,
            params: para
          })
          .then(function(res) {
            self.measurePurchaseModalshow = false;
            if (res.data.code === 200) {
              self.$Message.success("申购报告信息已记录!");
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
    },
    editPurchase() {
      this.isDisabled = true;
      const self = this;
      self.formValidate.demandTime = dateFormat(
        new Date(self.formValidate.demandTime),
        "yyyy-MM-dd"
      );
      let para = {
        pkId: self.formValidate.pkId,
        measureNumber: self.formValidate.measureNumber,
        model: self.model,
        measureName: self.measureName,
        applyQty: self.formValidate.applyQty,
        demandTime: self.formValidate.demandTime,
        purchaseReasion: self.formValidate.purchaseReasion,
        inventoryQty: self.inventoryQty,
        erpQty: self.erpQty,
        noCheckQty: self.noCheckQty,
        applyDepartmentId: self.formValidate.applyDepartmentId,
        applyDepartmentName: self.formValidate.applyDepartmentName,
        applyStatus: self.formValidate.applyStatus,
        remark: self.formValidate.remark
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/measure/measure-purchase-apply-update",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          self.measurePurchaseModalshow = false;
          if (res.data.code === 200) {
            self.$Message.success("申购报告内容已变更!");
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
    },
    searchStaff(pkId) {
      const self = this;
      let para = {
        userPkId: pkId
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/base/staff-get-by-user-id.json",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            if (data.data !== null && data.data !== undefined) {
              self.formValidate.applyDepartmentId = data.data.departmentId;
              self.formValidate.applyDepartmentName = data.data.departmentName;
              self.applyDepartment = data.data.departmentName;
              self.applicant = data.data.staffCode + "-" + data.data.staffName;
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
    searchAuditStaff() {
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
              self.staffCode = data.data.staffCode;
              self.formValidate.auitDepartmentId = data.data.departmentId;
              self.formValidate.auitDepartmentName = data.data.departmentName;
              self.auditStaffInfo =
                data.data.departmentName + "-" + data.data.staffName;
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
    handleSave(name) {
      const self = this;
      self.formValidate.applyStatus = 0;
      self.$refs[name].validate(valid => {
        if (valid) {
          if (self.isModify) {
            self.editPurchase();
          } else {
            self.addNewPurchase();
          }
        } else {
          self.$Message.error("请输入正确信息!");
        }
      });
    },
    handleSubmit(name) {
      const self = this;
      self.formValidate.applyStatus = 1;
      self.$refs[name].validate(valid => {
        if (valid) {
          if (self.isModify) {
            self.editPurchase();
          } else {
            self.addNewPurchase();
          }
        } else {
          self.$Message.error("请输入正确信息!");
        }
      });
    },
    handleAudit() {
      const self = this;
      if (
        self.agreeFlag == 5 &&
        !!!self.formValidate.purchasePrice &&
        (self.formValidate.applyStatus === 4 ||
          self.formValidate.applyStatus === -5)
      ) {
        self.$Notice.error({
          title: "错误提示",
          desc: "价格不能为空，请补充后继续操作"
        });
      } else {
        let para = {
          pkId: self.formValidate.pkId,
          measureNumber: self.formValidate.measureNumber,
          applyStatus: self.agreeFlag,
          auditRemark: self.auditRemark,
          auitDepartmentId: self.formValidate.auitDepartmentId,
          auitDepartmentName: self.formValidate.auitDepartmentName,
          purchasePrice: self.formValidate.purchasePrice
        };
        axios
          .request({
            headers: {
              "Content-Type": "application/json; charset=UTF-8"
            },
            url: "/measure/measure-purchase-report-audit.json",
            method: "post",
            isAuth: true,
            params: para
          })
          .then(function(res) {
            self.measurePurchaseModalshow = false;
            if (res.data.code === 200) {
              self.$Message.success("报告审核成功!");
              self.$emit("search");
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
      }
    },
    // departmentListSelectChange(item) {
    //   let departmentList = this.departmentList;
    //   if (item !== undefined) {
    //     for (const department of departmentList) {
    //       if (department.pkId === item) {
    //         this.formValidate.useDepartmentName = department.departmentName;
    //         this.getTeamListData(item);
    //       }
    //     }
    //   }
    // },
    // teamListSelectChange(item) {
    //   let teamList = this.teamList;
    //   if (item !== undefined) {
    //     for (const team of teamList) {
    //       if (team.pkId === item) {
    //         this.formValidate.useTeamName = team.teamName;
    //         this.getStaffListData(item);
    //       }
    //     }
    //   }
    // },
    // staffListSelectChange(item) {
    //   let staffList = this.staffList;
    //   if (item !== undefined) {
    //     for (const staff of staffList) {
    //       if (staff.pkId === item) {
    //         this.formValidate.userName = staff.staffName;
    //       }
    //     }
    //   }
    // },
    searchMeasure() {
      const self = this;
      if (!!!self.formValidate.measureNumber) {
        return;
      } else {
        let para = {
          measureNumber: self.formValidate.measureNumber
        };
        axios
          .request({
            headers: {
              "Content-Type": "application/json; charset=UTF-8"
            },
            url: "/measure/purchase-get-by-measure-number.json",
            method: "post",
            isAuth: true,
            params: para
          })
          .then(function(res) {
            //self.reportDetailModalshow = false;
            const data = res.data;
            if (data.code === 200 && data.data.toolMap !== null) {
              self.measureName = data.data.measureName;
              self.measureCode = data.data.measureNumber;
              self.model = data.data.model;
              self.formValidate.model = data.data.model;
              self.inventoryQty = data.data.measureAmount;
              if (
                data.data.erpAmount !== undefined &&
                data.data.erpAmount !== null
              ) {
                self.erpQty = data.data.erpAmount;
              } else {
                self.erpQty = 0;
              }
              if (
                data.data.noCheckQty !== undefined &&
                data.data.noCheckQty !== null
              ) {
                self.noCheckQty = data.data.noCheckQty;
              } else {
                self.noCheckQty = 0;
              }
            } else {
              self.$Notice.error({
                title: "错误提示",
                desc: "无法找到该物料的相关信息"
              });
            }
          })
          .catch(function(err) {
            console.log(err);
          });
      }
    },
    loadLogList(reportId) {
      const self = this;
      let para = {
        applyId: reportId
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/apply-audit-get-list.json",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            self.logList = data.data;
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
    changePrice() {
      const self = this;
      if (self.formValidate.purchasePrice > 2000) {
        self.agreeFlag = 5;
        self.agreeStatus = 5;
      } else {
        self.agreeFlag = 6;
        self.agreeStatus = 6;
      }
    },
    handleReset(name) {
      this.measurePurchaseModalshow = false;
      this.$refs[name].resetFields();
    },
    closeModal() {
      const self = this;
      self.logModalshow = false;
    }
  }
};
</script>