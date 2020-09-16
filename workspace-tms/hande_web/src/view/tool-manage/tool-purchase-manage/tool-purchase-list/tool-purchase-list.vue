<template>
  <div>
    <Card>
      <Row>
        <Form ref="formInline" :model="searchCondition" inline :label-width="65">
          <Col span="20">
            <FormItem prop="toolNumber" label="物料编码">
              <Input type="text" v-model="searchCondition.toolNumber" placeholder="请输入物料编码"></Input>
            </FormItem>
            <FormItem prop="purchaseType" label="申购类型">
              <Select
                style="width:158px"
                v-model="searchCondition.purchaseType"
                ref="purchaseType"
                placeholder="请选择采购类型"
              >
                <Option
                  v-for="item in purchaseSelect"
                  :value="item.value"
                  :key="item.value"
                >{{ item.name }}</Option>
              </Select>
            </FormItem>
            <FormItem label="申请部门" prop="departmentId" v-if="departmentAuth">
              <Select style="width:158px" v-model="searchCondition.departmentId" ref="department">
                <Option
                  v-for="item in departmentList"
                  :value="item.pkId"
                  :key="item.pkId"
                >{{ item.departmentName }}</Option>
              </Select>
            </FormItem>
            <FormItem label="审核状态" prop="applyStatus" v-if="roleId===1">
              <Select style="width:158px" v-model="searchCondition.applyStatus" ref="applyStatus">
                <Option
                  v-for="item in statusList"
                  :value="item.value"
                  :key="item.value"
                >{{ item.name }}</Option>
              </Select>
            </FormItem>
            <FormItem prop="dateInterval" label="时间区间">
              <DatePicker
                type="daterange"
                placeholder="请选择时间区间"
                v-model="searchCondition.dateInterval"
                style="width:180px;"
              ></DatePicker>
            </FormItem>
            <FormItem>
              <Button type="primary" style="margin-left: -60px;" @click="doSearch('formInline')">搜索</Button>
            </FormItem>
            <FormItem style="margin-left: -65px;">
              <Button style type="dashed" @click="doReset('formInline')">重置</Button>
            </FormItem>
          </Col>
          <Col span="4">
            <FormItem style="width:100%;text-align:right">
              <Button
                type="success"
                style="margin-right: 5px;"
                @click="addPurchase()"
                v-if="purchaseAuth"
              >
                <Icon type="md-add"></Icon>刀具申购
              </Button>
              <Button type="warning" @click="exportData()" v-if="roleId===1">
                <Icon type="ios-download-outline"></Icon>导出
              </Button>
            </FormItem>
          </Col>
        </Form>
      </Row>
      <Table ref="tablesMain" :data="tableData.rows" :columns="tableColumns" stripe border></Table>

      <div style="margin: 10px;overflow: hidden">
        <div style="float: right;">
          <Page
            show-total
            :total="tableData.total"
            :page-size="searchCondition.rows"
            :current="searchCondition.page"
            @on-change="changePage"
            @on-page-size-change="handlePageSize"
            show-sizer
          ></Page>
        </div>
      </div>
    </Card>

    <Modal
      v-model="toolPurchaseModalshow"
      @on-visible-change="channgeFocus"
      :title="isModify?'申购单详情':'刀具申购单'"
      width="1100"
      draggable
    >
      <div style="height:480px;overflow-y:auto; overflow-x:hidden">
        <Form
          ref="formValidate"
          :model="formValidate"
          :rules="ruleValidate"
          :label-width="100"
          inline
        >
          <Row>
            <Col span="6">
              <FormItem label="物料编码" prop="toolNumber" v-if="isModify?false:true">
                <Input
                  style="width:170px"
                  v-model="toolNumber"
                  @on-blur="searchTool()"
                  placeholder="输入物料编码"
                  :readonly="isAudit"
                  ref="refocus"
                  @on-change="validateToolNumber()"
                >
                  <Icon type="ios-search" slot="suffix" />
                </Input>
              </FormItem>
            </Col>
            <Col span="6">
              <FormItem label="物料编码" prop="toolNumber" v-if="isModify?true:false">
                <Input style="width:170px" v-model="toolNumber"></Input>
              </FormItem>
            </Col>
            <Col span="6">
              <FormItem label="图号名称" prop="baseId" v-if="isModify?false:true">
                <Select
                  style="width:170px"
                  v-model="formValidate.baseId"
                  ref="baseId"
                  placeholder="请选择图号名称"
                  @on-change="searchToolByBase()"
                  clearable
                  filterable
                >
                  <Icon type="ios-search" slot="suffix" />
                  <Option
                    v-for="item in baseList"
                    :value="item.pkId"
                    :key="item.pkId"
                  >{{ item.toolName }}</Option>
                </Select>
              </FormItem>
            </Col>
            <Col span="6">
              <FormItem label="物料名称" prop="toolName" v-if="isModify?true:false">
                <Input style="width:170px" v-model="toolName" readonly></Input>
              </FormItem>
            </Col>
            <Col span="6">
              <FormItem label="物料图号" prop="toolMap">
                <Input style="width:170px" v-model="toolMap" readonly></Input>
              </FormItem>
            </Col>
            <Col span="6">
              <FormItem label="TMS库存" prop="keepQty">
                <Input style="width:170px" v-model="keepQty" readonly />
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span="6">
              <FormItem label="ERP库存" prop="erpQty">
                <Input style="width:170px" v-model="erpQty" readonly />
              </FormItem>
            </Col>
            <Col span="6">
              <FormItem label="ERP待检数量" prop="noCheckQty">
                <Input style="width:170px" v-model="noCheckQty" readonly />
              </FormItem>
            </Col>
            <Col span="6">
              <FormItem label="申购类型" prop="purchaseType">
                <Select
                  style="width:170px"
                  v-model="formValidate.purchaseType"
                  ref="purchaseType"
                  placeholder="请选择采购类型"
                  :disabled="isAudit"
                >
                  <Option
                    v-for="item in purchaseType"
                    :value="item.value"
                    :key="item.value"
                  >{{ item.name }}</Option>
                </Select>
              </FormItem>
            </Col>
            <Col span="6">
              <FormItem label="申请部门" prop="applyDepartment">
                <Input style="width:170px" v-model="applyDepartment" readonly />
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span="6">
              <FormItem label="申请人" prop="applicant">
                <Input style="width:170px" v-model="applicant" readonly />
              </FormItem>
            </Col>
            <Col span="6">
              <FormItem label="申购原因" prop="purchaseResion">
                <Select
                  style="width:170px"
                  v-model="formValidate.purchaseResion"
                  ref="purchaseResion"
                  placeholder="请选择采购原因"
                  :disabled="isAudit"
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
              <FormItem label="需求时间" prop="applyTime">
                <DatePicker
                  type="date"
                  style="width: 170px"
                  :options="dateOption"
                  v-model="formValidate.applyTime"
                  placeholder="选择需求时间"
                  :readonly="isAudit"
                ></DatePicker>
              </FormItem>
            </Col>
            <Col span="6">
              <FormItem label="申购数量" prop="needQty">
                <InputNumber
                  style="width:170px"
                  v-model="formValidate.needQty"
                  :min="1"
                  :max="200"
                  placeholder="输入需求数量"
                  :readonly="isAudit"
                ></InputNumber>
                <Icon type="md-arrow-round-forward" slot="suffix" />
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span="6" v-if="isModify">
              <FormItem label="申购时间" prop="needTime">
                <DatePicker
                  type="datetime"
                  style="width: 170px"
                  v-model="formValidate.needTime"
                  readonly
                ></DatePicker>
              </FormItem>
            </Col>
            <Col span="6">
              <FormItem label="刀具制件" prop="partId" v-if="!isAudit">
                <Select
                  style="width:170px"
                  v-model="formValidate.partId"
                  @on-change="partListSelectChange"
                  ref="partList"
                  placeholder="请选择刀具制件"
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
            <Col span="6">
              <FormItem label="刀具制件" prop="partName" v-if="isAudit">
                <Input v-model="formValidate.partName" readonly />
              </FormItem>
            </Col>
            <Col span="12">
              <FormItem label="可用序号" prop="useSeq" v-if="seqModel">
                <Input style="width:435px" v-model="useSeq" readonly />
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span="24">
              <FormItem label="申购备注" prop="remark">
                <Input style="width:970px" v-model="formValidate.remark" :readonly="isAudit" />
              </FormItem>
            </Col>
          </Row>
          <Row style="border-top:1px solid #e3e3e3;padding-top: 20px;" v-if="isModify">
            <Col span="24" class="ivu-form">
              <div class="ivu-form-item">
                <label class="ivu-form-item-label" style="width: 100px;text-align: right;">审核进度</label>
                <div class="ivu-form-item-content" style="margin-left: 100px; ">
                  <Steps :current="currentStep">
                    <Step title="提交" content="分厂填写刀具申购报告并提交"></Step>
                    <Step title="审核" content="分厂领导审核"></Step>
                    <Step title="审核" content="工艺部审核"></Step>
                    <Step title="审核" content="主管领导审核"></Step>
                    <Step title="执行" content="待采购部接收"></Step>
                  </Steps>
                </div>
              </div>
            </Col>
            <Col span="8" v-if="radioAudit" class="ivu-form">
              <FormItem label="审核意见" prop="agreeFlag">
                <RadioGroup v-model="agreeFlag">
                  <Radio :label="agreeStatus">同意</Radio>
                  <Radio :label="disagreeStatus">不同意</Radio>
                </RadioGroup>
              </FormItem>
            </Col>
            <Col span="8">
              <FormItem label="审核人" prop="auditStaffInfo" v-if="radioAudit">
                <Input style="width:160px" v-model="auditStaffInfo" readonly />
              </FormItem>
            </Col>
            <Col span="24" v-if="radioAudit">
              <FormItem label="意见描述" prop="auditRemark">
                <Input
                  v-model="auditRemark"
                  type="textarea"
                  :rows="2"
                  placeholder="意见描述"
                  style="width:970px;"
                />
              </FormItem>
            </Col>
          </Row>
        </Form>
        <Table
          :columns="logtableColumns"
          height="160"
          :data="logList"
          border
          stripe
          highlight-row
          v-if="!!logList&&logList.length>0"
        ></Table>
      </div>
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

    <Modal
      v-model="deleteModalShow"
      width="250"
      title="删除报告"
      @on-ok="deleteModalSureBtn"
      @on-cancel="deleteModalCancelBtn"
    >
      <p>确定删除所选报告？</p>
    </Modal>

    <!-- <audit-log-model ref="auditLogModel"></audit-log-model> -->
  </div>
</template>

<script>
import axios from "@/libs/api.request";
import { getToken, getBaseUrl } from "@/libs/util";
import tool_purchase_manage_form_config from "./tool-purchase.js";
import { dateFormat, objCopy, hasOneOf } from "@/libs/tools.js";
import purchaseType from "./purchaseType-type.js";
import purchaseSelect from "./purchaseSelect-type.js";
import purchaseResion from "./purchaseResion-type.js";
import statusList from "./tool-apply-status-list.js";
// import auditLogModel from "./audit-log-model.vue";
import { setTimeout } from "timers";
export default {
  name: "page",
  computed: {
    access() {
      return this.$store.state.user.userData.data.access;
    },
    //保存、提交、删除（生产部）
    purchaseAuth() {
      return hasOneOf(["01030101"], this.access);
    },
    //审核(生产部)
    productAuth() {
      return hasOneOf(["01030102"], this.access);
    },
    //审核(工艺部)
    technologyAuth() {
      return hasOneOf(["01030103"], this.access);
    },
    //审核(主管领导)
    leaderAuth() {
      return hasOneOf(["01030104"], this.access);
    },
    //审核(主管领导)
    newToolAuth() {
      return hasOneOf(["0103010401"], this.access);
    },
    //审核(主管领导)
    commonToolAuth() {
      return hasOneOf(["0103010402"], this.access);
    }
  },
  // components: {
  //   auditLogModel
  // },
  data() {
    return {
      roleId: 0,
      departmentAuth: true,
      isDisabled: false,
      auditStaffInfo: "",
      applicant: "",
      applyDepartment: "",
      toolNumber: "",
      toolMap: "",
      toolName: "",
      toolAmount: "",
      erpQty: "",
      noCheckQty: "",
      keepQty: "",
      useSeq: "",
      partList: [],
      logList: [],
      auitdepartmentList: [],
      departmentList: [],
      statusList: statusList,
      staffList: [],
      auiterList: [],
      baseList: [],
      isAudit: false,
      seqModel: false,
      radioAudit: false,
      btnSave: true, //保存按钮可见
      btnSubmit: true, //提交按钮可见
      auditText: "提交",
      btnAudit: false, //审核按钮可见
      currentStep: 0, //当前步骤
      auditRemark: "", //审核备注
      agreeFlag: 2,
      agreeStatus: 2,
      disagreeStatus: -1,
      applyStatusList: [],
      deleteModalShow: false, //控制删除modal提示 显示
      deleteRowData: {}, //行删除行数据
      isBatchDetele: false, //批量删除标志
      multiselectRowData: [], //复选列数据
      toolPurchaseModalshow: false, //控制明细modal显示
      isModify: false, //是否是修改明细操作
      purchaseResion: purchaseResion,
      purchaseType: purchaseType,
      purchaseSelect: purchaseSelect,
      searchCondition: {
        departmentId: "",
        toolNumber: "",
        purchaseType: "",
        dateInterval: [],
        page: 1,
        rows: 10
      },
      tableData: {
        total: 0, // 总页数
        rows: [] // 每页条数
      },
      tableColumns: [
        {
          title: "物料编码",
          key: "toolNumber",
          minWidth: 150
        },
        {
          title: "物料名称",
          key: "toolName",
          minWidth: 300
        },
        {
          title: "物料图号",
          key: "toolMap",
          minWidth: 160
        },
        {
          title: "物料类型",
          key: "typeName",
          minWidth: 100
        },
        {
          title: "制件",
          key: "partName",
          minWidth: 270
        },
        {
          title: "申请部门",
          key: "departmentName",
          minWidth: 140
        },
        {
          title: "申请人",
          key: "applierName",
          minWidth: 100
        },
        {
          title: "申购时间",
          key: "needTime",
          sortable: true,
          minWidth: 160,
          render: function(h, params) {
            return h(
              "div",
              dateFormat(new Date(params.row.needTime), "yyyy-MM-dd HH:mm:ss")
            );
          }
        },
        {
          title: "需求数量",
          key: "needQty",
          align: "center",
          minWidth: 100
        },
        {
          title: "需求时间",
          key: "applyTime",
          sortable: true,
          minWidth: 120,
          render: function(h, params) {
            return h(
              "div",
              dateFormat(new Date(params.row.applyTime), "yyyy-MM-dd")
            );
          }
        },
        {
          title: "采购原因",
          minWidth: 100,
          key: "purchaseResion",
          render: function(h, params) {
            let statusStr = "";
            switch (params.row.purchaseResion) {
              case 1:
                statusStr = "产量提升";
                break;
              case 2:
                statusStr = "刀具报废";
                break;
              case 3:
                statusStr = "产品开发";
                break;
              case 4:
                statusStr = "其他";
                break;
            }
            return h("div", statusStr);
          }
        },
        {
          title: "采购类型",
          key: "purchaseType",
          minWidth: 100,
          render: function(h, params) {
            let statusStr = "";
            switch (params.row.purchaseType) {
              case 1:
                statusStr = "新品开发";
                break;
              case 2:
                statusStr = "常用刀具";
                break;
            }
            return h("div", statusStr);
          }
        },
        {
          title: "申请状态",
          key: "applyStatus",
          minWidth: 180,
          render: function(h, params) {
            let statusStr = "";
            switch (params.row.applyStatus) {
              case 1:
                statusStr = "待分厂领导审核";
                break;
              case -1:
                statusStr = "分厂领导未通过，已驳回";
                break;
              case 2:
                statusStr = "待工艺部审核";
                break;
              case -2:
                statusStr = "工艺部未通过,已驳回";
                break;
              case 3:
                statusStr = "待主管领导审核";
                break;
              case -3:
                statusStr = "主管领导审核未通过,已驳回";
                break;
              case 4:
                statusStr = "待采购部接收";
                break;
              case -4:
                statusStr = "采购部驳回";
                break;
              case 5:
                statusStr = "采购收货中";
                break;
            }
            return h("div", statusStr);
          }
        },
        {
          title: "操作",
          key: "action",
          width: 200,
          align: "center",
          fixed: "right",
          render: (h, params) => {
            /**
             * render写法 添加按钮
             */
            return h("div", [
              h(
                "Button",
                {
                  props: {
                    type: "primary",
                    size: "small"
                  },
                  style: {
                    marginRight: "5px"
                  },
                  on: {
                    click: () => {
                      this.show(params.index);
                    }
                  }
                },
                "详情"
              ),
              h(
                "Button",
                {
                  props: {
                    type: "error",
                    disabled: params.row.applyStatus != 0,
                    size: "small"
                  },
                  style: {
                    marginRight: "5px",
                    display: this.purchaseAuth ? "inline-block" : "none"
                  },
                  on: {
                    click: () => {
                      this.remove(params.index);
                    }
                  }
                },
                "删除"
              )
            ]);
          }
        }
      ],
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
      formValidate: tool_purchase_manage_form_config.formValidate, //user form表单字段
      ruleValidate: tool_purchase_manage_form_config.ruleValidate //user form表单验证规则
    };
  },
  created() {
    const self = this;
    self.roleId = self.$store.state.user.userData.data.roleId;
    if (!self.productAuth || !self.newToolAuth || !self.commonToolAuth) {
      self.departmentAuth = true;
      self.getDepartmentListData();
    } else {
      self.departmentAuth = false;
    }
    self.getListData();
    self.getBaseListData();
  },
  methods: {
    channgeFocus(val) {
      const self = this;
      if (!self.isModify) {
        setTimeout(function() {
          if (val) {
            self.$refs["refocus"].focus();
          }
        }, 200);
      }
    },
    validateToolNumber() {
      const self = this;
      self.formValidate.toolNumber = this.toolNumber;
    },
    validateToolMap() {
      this.formValidate.toolMap = this.toolMap;
    },
    getListData() {
      const self = this;
      if (self.leaderAuth) {
        self.applyStatusList.push(3);
        self.applyStatusList.push(-4);
        if (self.newToolAuth && !self.commonToolAuth) {
          self.searchCondition.purchaseType = 1;
        }
        if (self.commonToolAuth && !self.newToolAuth) {
          self.searchCondition.purchaseType = 2;
        }
      }
      if (self.technologyAuth) {
        self.applyStatusList.push(2);
        self.applyStatusList.push(-3);
      }
      if (self.productAuth) {
        if (!self.purchaseAuth && !self.technologyAuth && !self.leaderAuth) {
          self.searchCondition.departmentId =
            self.$store.state.user.userData.data.departmentId;
        }
        self.applyStatusList.push(1);
        self.applyStatusList.push(-2);
      }
      if (self.purchaseAuth) {
        self.formValidate.applierId = null;
        if (!self.productAuth && !self.technologyAuth && !self.leaderAuth) {
          self.formValidate.applierId =
            self.$store.state.user.userData.data.userId;
        }
        self.applyStatusList = [];
      }
      if (!!self.searchCondition.dateInterval) {
        let beginDate = self.searchCondition.dateInterval[0];
        let endDate = self.searchCondition.dateInterval[1];
        if (!!beginDate) {
          self.searchCondition.beginDate = dateFormat(
            new Date(beginDate),
            "yyyy-MM-dd"
          );
        } else {
          self.searchCondition.beginDate = null;
        }
        if (!!endDate) {
          self.searchCondition.endDate = dateFormat(
            new Date(endDate),
            "yyyy-MM-dd"
          );
        } else {
          self.searchCondition.endDate = null;
        }
      } else {
        self.searchCondition.beginDate = null;
        self.searchCondition.endDate = null;
      }
      let para = {
        applierId: self.formValidate.applierId,
        applyStatus:self.searchCondition.applyStatus,
        applyStatusList: self.applyStatusList.toString(),
        departmentId: self.searchCondition.departmentId,
        toolNumber: self.searchCondition.toolNumber,
        purchaseType: self.searchCondition.purchaseType,
        page: self.searchCondition.page,
        rows: self.searchCondition.rows,
        beginDate: self.searchCondition.beginDate,
        endDate: self.searchCondition.endDate
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/tool-purchase-apply-page-list.json",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            self.tableData = data;
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },
    getBaseListData() {
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/tool-base-list",
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
    getStaffListData(departmentId) {
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
          isAuth: true,
          method: "post",
          params: para
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            self.staffList = data.data;
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },
    getStaffByDepartment(departmentId) {
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
          isAuth: true,
          method: "post",
          params: para
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            self.auiterList = data.data;
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },
    addNewPurchase() {
      const self = this;
      let addFlag = true;
      if (
        self.formValidate.purchaseType === 1 &&
        self.formValidate.partName != undefined
      ) {
        addFlag = true;
      } else if (
        self.formValidate.purchaseType === 1 &&
        self.formValidate.partName == undefined
      ) {
        addFlag = false;
      } else {
        addFlag = true;
      }
      if (!addFlag) {
        self.$Message.error("制件不能为空");
      } else {
        self.formValidate.applyTime = dateFormat(
          new Date(self.formValidate.applyTime),
          "yyyy-MM-dd"
        );
        this.isDisabled = true;
        let para = {
          toolMap: self.toolMap,
          toolName: self.formValidate.toolName,
          keepQty: self.keepQty,
          erpQty: self.erpQty,
          noCheckQty: self.noCheckQty,
          applyTime: self.formValidate.applyTime,
          departmentId: self.formValidate.departmentId,
          departmentName: self.formValidate.departmentName,
          toolNumber: self.formValidate.toolNumber,
          purchaseType: self.formValidate.purchaseType,
          needQty: self.formValidate.needQty,
          purchaseResion: self.formValidate.purchaseResion,
          applyStatus: self.formValidate.applyStatus,
          remark: self.formValidate.remark,
          partId: self.formValidate.partId,
          partName: self.formValidate.partName
        };
        axios
          .request({
            headers: {
              "Content-Type": "application/json; charset=UTF-8"
            },
            url: "/tool/tool-purchase-apply-add",
            method: "post",
            isAuth: true,
            params: para
          })
          .then(function(res) {
            self.toolPurchaseModalshow = false;
            if (res.data.code === 200) {
              self.$Message.success("申购报告信息已记录!");
              self.isDisabled = false;
              self.getListData();
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
      }
    },
    editPurchase() {
      const self = this;
      self.formValidate.applyTime = dateFormat(
        new Date(self.formValidate.applyTime),
        "yyyy-MM-dd"
      );
      this.isDisabled = true;
      let para = {
        pkId: self.formValidate.pkId,
        toolMap: self.toolMap,
        toolName: self.formValidate.toolName,
        keepQty: self.keepQty,
        erpQty: self.erpQty,
        noCheckQty: self.noCheckQty,
        applyTime: self.formValidate.applyTime,
        departmentId: self.formValidate.departmentId,
        departmentName: self.formValidate.departmentName,
        toolNumber: self.formValidate.toolNumber,
        purchaseType: self.formValidate.purchaseType,
        needQty: self.formValidate.needQty,
        purchaseResion: self.formValidate.purchaseResion,
        applyStatus: self.formValidate.applyStatus,
        remark: self.formValidate.remark,
        partId: self.formValidate.partId,
        partName: self.formValidate.partName
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/tool-purchase-apply-update",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          self.toolPurchaseModalshow = false;
          if (res.data.code === 200) {
            self.$Message.success("申购报告内容已变更!");
            self.isDisabled = false;
            self.getListData();
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
    deleteRowDate(pkId, toolNumber) {
      let para = {
        pkId: pkId,
        toolNumber: toolNumber
      };
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/tool-purchase-apply-delete-by-id",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          self.deleteModalShow = false;
          if (res.data.code === 200) {
            self.$Message.success("申购报告删除成功!");
            self.getListData();
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
              self.formValidate.departmentId = data.data.departmentId;
              self.formValidate.departmentName = data.data.departmentName;
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
    searchAuditStaff(pkId) {
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
    searchToolByBase() {
      const self = this;
      if (!!!self.formValidate.baseId) {
        return;
      } else {
        let para = {
          pkId: self.formValidate.baseId
        };
        axios
          .request({
            headers: {
              "Content-Type": "application/json; charset=UTF-8"
            },
            url: "/tool/tool-base-get-by-id.json",
            method: "post",
            isAuth: true,
            params: para
          })
          .then(function(res) {
            const data = res.data;
            if (data.code === 200 && data.data.toolMap !== undefined) {
              self.formValidate.toolName = data.data.toolName;
              self.toolMap = data.data.toolMap;
              self.toolNumber = data.data.toolNumber;
              self.formValidate.toolNumber = data.data.toolNumber;
              self.formValidate.toolMap = data.data.toolMap;
              self.keepQty = data.data.toolAmount;
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
              self.toolMap = "";
              self.toolNumber = "";
              self.formValidate.toolNumber = "";
              self.formValidate.toolName = "";
              self.keepQty = null;
              self.erpQty = null;
              self.noCheckQty = null;
              self.$Notice.error({
                title: "错误提示",
                desc: "请先完善相关物料图号信息"
              });
            }
          })
          .catch(function(err) {
            console.log(err);
          });
      }
    },
    searchTool() {
      const self = this;
      if (!!!self.toolNumber) {
        return;
      } else {
        self.formValidate.toolNumber = self.toolNumber;
        let para = {
          toolNumber: self.toolNumber
        };
        axios
          .request({
            headers: {
              "Content-Type": "application/json; charset=UTF-8"
            },
            url: "/tool/purchase-get-by-tool-number.json",
            method: "post",
            isAuth: true,
            params: para
          })
          .then(function(res) {
            //self.reportDetailModalshow = false;
            const data = res.data;
            if (data.code === 200 && data.data.toolMap !== null) {
              self.toolName = data.data.toolName;
              self.toolMap = data.data.toolMap;
              self.formValidate.baseId = data.data.pkId;
              self.formValidate.toolName = data.data.toolName;
              self.formValidate.toolMap = data.data.toolMap;
              self.keepQty = data.data.toolAmount;
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
    getPartList() {
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/base/part-list.json",
          method: "post",
          isAuth: true
        })
        .then(function(res) {
          //self.reportDetailModalshow = false;
          const data = res.data;
          if (data.code === 200 && data.data.toolMap !== null) {
            self.partList = data.data;
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
    },
    partListSelectChange(item) {
      const self = this;
      let partList = self.partList;
      if (item !== undefined) {
        for (const part of partList) {
          if (part.pkId === item) {
            self.formValidate.partName = part.partName;
          }
        }
      }
    },
    changePage(cuurentPage) {
      const self = this;
      self.$set(self.searchCondition, "page", cuurentPage);
      self.getListData();
    },
    handlePageSize(value) {
      const self = this;
      self.$set(self.searchCondition, "rows", value);
      self.getListData();
    },
    doSearch(name) {
      const self = this;
      this.formValidate = {};
      self.changePage(1);
    },
    doReset(name) {
      const self = this;
      self.$refs[name].resetFields();
      self.getListData();
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
    show(index) {
      const self = this;
      self.staffCode = "";
      self.staffInfo = "";
      self.auditStaffInfo = "";
      self.toolPurchaseModalshow = true;
      self.isModify = true;
      self.formValidate = objCopy(self.tableData.rows[index], {});
      self.searchStaff(self.formValidate.applierId);
      self.toolNumber = self.formValidate.toolNumber;
      self.searchTool();
      self.loadLogList(self.formValidate.pkId);
      self.toolMap = self.formValidate.toolMap;
      self.toolName = self.formValidate.toolName;
      self.keepQty = self.formValidate.keepQty;
      self.erpQty = self.formValidate.erpQty;
      self.noCheckQty = self.formValidate.noCheckQty;
      self.formValidate.needTime = dateFormat(
        new Date(self.formValidate.needTime),
        "yyyy-MM-dd HH:mm:ss"
      );
      self.formValidate.applyTime = new Date(self.formValidate.applyTime);
      self.auditRemark = "";

      self.btnAudit = false;
      self.radioAudit = false;

      let selectDepartmentId = self.formValidate.departmentId;
      const auditStatus = self.formValidate.applyStatus;
      if (auditStatus == 0 || auditStatus == -1) {
        if (self.purchaseAuth) {
          self.btnSave = true;
          self.btnSubmit = true;
        } else {
          self.btnSave = false;
          self.btnSubmit = false;
        }
        self.currentStep = 0;
        self.btnAudit = false;
        self.radioAudit = false;
        self.seqModel = false;
        self.getPartList();
      } else if (auditStatus == 1 || auditStatus == -2) {
        if (self.productAuth) {
          self.isAudit = true;
          self.btnAudit = true;
          self.radioAudit = true;
        } else {
          self.isAudit = false;
          self.btnAudit = false;
          self.radioAudit = false;
        }
        self.searchAuditStaff(self.$store.state.user.userData.data.userId);
        self.currentStep = 1;
        self.btnSave = false;
        self.btnSubmit = false;
        self.seqModel = false;
        self.agreeFlag = 2;
        self.agreeStatus = 2;
        self.disagreeStatus = -1;
      } else if (auditStatus == 2 || auditStatus == -3) {
        if (this.technologyAuth) {
          self.isAudit = true;
          self.btnAudit = true;
          self.radioAudit = true;
        } else {
          self.isAudit = false;
          self.btnAudit = false;
          self.radioAudit = false;
        }
        self.searchAuditStaff(self.$store.state.user.userData.data.userId);
        self.currentStep = 2;
        self.btnSave = false;
        self.btnSubmit = false;
        self.seqModel = false;
        self.agreeFlag = 3;
        self.agreeStatus = 3;
        self.disagreeStatus = -2;
      } else if (auditStatus == 3 || auditStatus == -4) {
        if (self.leaderAuth) {
          self.isAudit = true;
          self.btnAudit = true;
          self.radioAudit = true;
        } else {
          self.isAudit = false;
          self.btnAudit = false;
          self.radioAudit = false;
        }
        self.searchAuditStaff(self.$store.state.user.userData.data.userId);
        self.currentStep = 3;
        self.btnSave = false;
        self.btnSubmit = false;
        self.seqModel = false;
        self.agreeFlag = 4;
        self.agreeStatus = 4;
        self.disagreeStatus = -3;
      } else {
        //self.searchSeq();
        self.useSeq = self.formValidate.availableNumber;
        self.currentStep = 4;
        self.isAudit = true;
        self.btnSave = false;
        self.btnSubmit = false;
        self.btnAudit = false;
        self.radioAudit = false;
        if (!!self.useSeq) {
          self.seqModel = true;
        }
      }
    },
    remove(index) {
      this.deleteRowData = this.tableData.rows[index];
      this.deleteModalShow = true;
    },

    addPurchase() {
      this.isModify = false;
      const self = this;
      self.searchStaff(this.$store.state.user.userData.data.userId);
      self.isAudit = false;
      self.seqModel = false;
      self.btnSave = true;
      self.btnSubmit = true;
      self.btnAudit = false;
      self.radioAudit = false;
      self.radioAudit = false;
      self.toolPurchaseModalshow = true;
      self.toolNumber = "";
      self.toolMap = "";
      self.toolName = "";
      self.keepQty = "";
      self.erpQty = "";
      self.noCheckQty = "";
      self.formValidate = {};
      self.formValidate.needQty = 1;
      self.logList = [];
      self.$refs["formValidate"].resetFields();
      self.getPartList();
    },
    deleteModalSureBtn() {
      this.deleteRowDate(
        this.deleteRowData.pkId,
        this.deleteRowData.toolNumber
      );
    },
    //删除modal 取消方法
    deleteModalCancelBtn() {
      if (this.isBatchDetele) {
        this.multiselectRowData = [];
      } else {
        this.deleteRowData = {};
      }
    },
    handleSave(name) {
      const self = this;
      self.formValidate.applyStatus = 0;
      self.$refs[name].validate(valid => {
        if (valid) {
          self.isDisable = true;
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
      self.isDisable = true;
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
      let para = {
        pkId: self.formValidate.pkId,
        applyStatus: self.agreeFlag,
        auditRemark: self.auditRemark,
        auitDepartmentId: self.formValidate.auitDepartmentId,
        auitDepartmentName: self.formValidate.auitDepartmentName,
        toolNumber: self.toolNumber
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/purchase-report-audit.json",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          self.toolPurchaseModalshow = false;
          if (res.data.code === 200) {
            self.$Message.success("报告审核成功!");
            self.getListData();
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
    },

    handleReset(name) {
      this.toolPurchaseModalshow = false;
      this.$refs[name].resetFields();
    },
    exportData() {
      const self = this;
      if (!!self.searchCondition.dateInterval) {
        let beginDate = self.searchCondition.dateInterval[0];
        let endDate = self.searchCondition.dateInterval[1];
        if (!!beginDate) {
          self.searchCondition.beginDate = dateFormat(
            new Date(beginDate),
            "yyyy-MM-dd"
          );
        } else {
          self.searchCondition.beginDate = null;
        }
        if (!!endDate) {
          self.searchCondition.endDate = dateFormat(
            new Date(endDate),
            "yyyy-MM-dd"
          );
        } else {
          self.searchCondition.endDate = null;
        }
      } else {
        self.searchCondition.beginDate = null;
        self.searchCondition.endDate = null;
      }

      let toolNumber = self.searchCondition.toolNumber;
      let purchaseType = self.searchCondition.purchaseType;
      let beginDate = self.searchCondition.beginDate;
      let endDate = self.searchCondition.endDate;

      let para = "token=" + getToken();
      if (!!toolNumber) {
        para += "&toolNumber=" + toolNumber;
      }
      if (!!purchaseType) {
        para += "&purchaseType=" + purchaseType;
      }
      if (!!beginDate) {
        para += "&beginDate=" + beginDate;
      }
      if (!!endDate) {
        para += "&endDate=" + endDate;
      }
      window.location.href = getBaseUrl() + "/tool/purchase-export?" + para;
    }
  }
};
</script>

<style>
</style>
