<template>
  <div>
    <Card>
      <Row>
        <Form ref="formInline" :model="searchCondition" inline :label-width="65">
          <Col span="18">
            <FormItem prop="fullNumber" label="物料条码">
              <Input type="text" v-model="searchCondition.fullNumber" placeholder="请输入物料条码"></Input>
            </FormItem>
            <FormItem prop="toolNumber" label="物料编码">
              <Input type="text" v-model="searchCondition.toolNumber" placeholder="请输入物料编码"></Input>
            </FormItem>
            <FormItem prop="warehouseCode" label="入库编码">
              <Input type="text" v-model="searchCondition.warehouseCode" placeholder="请输入入库编码"></Input>
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
          <Col span="6">
            <FormItem style="width:100%;text-align:right">
              <Button
                type="success"
                style="margin-right: 5px;"
                v-if="applyAuth"
                @click="addApply()"
              >
                <Icon type="md-add"></Icon>报废申请
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

    <Modal v-model="toolApplyModalshow" :title="isModify?'报废审批单':'报废申请单'" width="900" draggable>
      <Form
        ref="formValidate"
        :model="formValidate"
        :rules="ruleValidate"
        :label-width="100"
        inline
      >
        <Row>
          <Col span="16">
            <FormItem label="物料条码" prop="fullNumber">
              <Input
                style="width:460px"
                v-model="formValidate.fullNumber"
                @keyup.enter.native="searchTool()"
                :readonly="isAudit"
              >
                <Icon type="ios-search" slot="suffix" />
              </Input>
            </FormItem>
          </Col>
          <Col span="8">
            <FormItem label="物料编码" prop="toolNumber">
              <Input style="width:170px" v-model="toolNumber" readonly />
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span="8">
            <FormItem label="入库编码" prop="warehouseCode">
              <Input style="width:170px" v-model="warehouseCode" readonly />
            </FormItem>
          </Col>
          <Col span="8">
            <FormItem label="物料图号" prop="toolMap">
              <Input style="width:170px" v-model="toolMap" readonly />
            </FormItem>
          </Col>
          <Col span="8">
            <FormItem label="物料名称" prop="toolName">
              <Input style="width:170px" v-model="toolName" readonly></Input>
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span="8">
            <FormItem label="申请部门" prop="applyDepartment">
              <Input style="width:170px" v-model="applyDepartment" readonly />
            </FormItem>
          </Col>
          <Col span="8">
            <FormItem label="申请人" prop="applierCode">
              <Input style="width:170px" v-model="staffInfo" readonly></Input>
            </FormItem>
          </Col>
          <Col span="8">
            <FormItem label="已加工数量" prop="processAmount">
              <Input style="width:170px" v-model="processAmount" readonly />
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span="8">
            <FormItem label="报废原因" prop="scripResion">
              <Select
                style="width:170px"
                v-model="formValidate.scripResion"
                ref="scrapType"
                placeholder="请选择报废原因"
                :disabled="isAudit"
              >
                <Option
                  v-for="item in scrapType"
                  :value="item.value"
                  :key="item.value"
                >{{ item.name }}</Option>
              </Select>
            </FormItem>
          </Col>
          <Col span="16">
            <FormItem label="原因说明" prop="scripRemark">
              <Input
                type="textarea"
                :rows="1"
                style="width:460px"
                v-model="formValidate.scripRemark"
                placeholder="输入原因说明"
                :readonly="isAudit"
              />
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
                  <Step title="提交" content="分厂填写刀具报废申请并提交"></Step>
                  <Step title="审核" content="工艺部审核"></Step>
                  <Step title="执行" content="待库房执行报废"></Step>
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
          <Col span="8" v-if="radioAudit">
            <FormItem label="审核部门" prop="auditDepartment">
              <Input style="width:170px" v-model="auditDepartment" readonly />
            </FormItem>
          </Col>
          <Col span="8" v-if="radioAudit">
            <FormItem label="审核人" prop="auditStaffInfo">
              <Input style="width:170px" v-model="auditStaffInfo" readonly />
            </FormItem>
          </Col>
          <Col span="24" v-if="radioAudit">
            <FormItem label="意见描述" prop="auditRemark">
              <Input
                mv-model="auditRemark"
                type="textarea"
                :rows="2"
                placeholder="意见描述"
                style="width:750px;"
              />
            </FormItem>
          </Col>
        </Row>
      </Form>
      <div slot="footer">
        <Button
          @click="handleSave('formValidate')"
          v-if="btnSave && applyAuth"
          type="primary"
          :disabled="isDisabled"
        >保存</Button>
        <Button
          @click="handleSubmit('formValidate')"
          v-if="btnSubmit && applyAuth"
          type="warning"
          :disabled="isDisabled"
        >提交</Button>
        <Button @click="handleAudit()" type="primary" v-if="btnAudit" :disabled="isDisabled">提交</Button>
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

    <audit-log-model ref="auditLogModel"></audit-log-model>
  </div>
</template>

<script>
import axios from "@/libs/api.request";
import tool_scrap_manage_form_config from "./tool-scrap-manage-form-config.js";
import { dateFormat, objCopy, hasOneOf } from "@/libs/tools.js";
import auditLogModel from "./audit-log-model.vue";
import scrapType from "./scrap-type.js";
export default {
  name: "page",
  computed: {
    access() {
      return this.$store.state.user.userData.data.access;
    },
    //保存、提交、删除（质检部）
    applyAuth() {
      return hasOneOf(["0103030701"], this.access);
    },
    //审核(工艺部)
    technologyAuth() {
      return hasOneOf(["0103030702"], this.access);
    }
  },
  components: {
    auditLogModel
  },
  data() {
    return {
      isDisabled: false,
      processAmount: "",
      auditDepartment: "",
      applyDepartment: "",
      isAudit: false,
      auditStaffInfo: "",
      staffInfo: "",
      toolNumber: "",
      warehouseCode: "",
      toolMap: "",
      toolName: "",
      scrapType: scrapType,
      radioAudit: false,
      btnSave: true, //保存按钮可见
      btnSubmit: true, //提交按钮可见
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
      toolApplyModalshow: false, //控制明细modal显示
      isModify: false, //是否是修改明细操作
      searchCondition: {
        fullNumber: "",
        warehouseCode: "",
        toolNumber: "",
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
          title: "物料条码",
          key: "fullNumber",
          fixed: "left",
          width: 220
        },
        {
          title: "物料名称",
          fixed: "left",
          key: "toolName",
          width: 250
        },
        {
          title: "物料图号",
          key: "toolMap",
          width: 150
        },
        {
          title: "物料编码",
          key: "toolNumber",
          width: 120
        },
        {
          title: "已生产数量",
          key: "processAmount",
          width: 100
        },
        {
          title: "申请部门",
          key: "departmentName",
          width: 150
        },
        {
          title: "申请人",
          key: "applierName",
          width: 120
        },
        {
          title: "申请时间",
          key: "applyTime",
          width: 150,
          render: function(h, params) {
            return h(
              "div",
              dateFormat(new Date(params.row.applyTime), "yyyy-MM-dd HH:mm:ss")
            );
          }
        },
        {
          title: "报废原因",
          width: 120,
          key: "scripResion",
          render: function(h, params) {
            let statusStr = "";
            switch (params.row.scripResion) {
              case 1:
                statusStr = "正常报废";
                break;
              case 2:
                statusStr = "异常报废";
                break;
            }
            return h("div", statusStr);
          }
        },
        {
          title: "申请状态",
          key: "applyStatus",
          width: 180,
          render: function(h, params) {
            let statusStr = "";
            switch (params.row.applyStatus) {
              case 0:
                statusStr = "报废申请待提交";
                break;
              case 1:
                statusStr = "待工艺部审核";
                break;
              case -1:
                statusStr = "工艺部审核未通过，已驳回";
                break;
              case 2:
                statusStr = "待库房执行报废";
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
                    type: "info",

                    size: "small"
                  },
                  style: {
                    marginRight: "5px"
                  },
                  on: {
                    click: () => {
                      this.showAuditLog(params.row.pkId);
                    }
                  }
                },
                "审核记录"
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
                    display: this.applyAuth ? "inline-block" : "none"
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
      formValidate: tool_scrap_manage_form_config.formValidate, //user form表单字段
      ruleValidate: tool_scrap_manage_form_config.ruleValidate //user form表单验证规则
    };
  },
  created() {
    const self = this;
    self.getListData();
  },
  methods: {
    getListData() {
      const self = this;
      if (self.technologyAuth) {
        self.applyStatusList.push(1);
      }
      if (self.applyAuth) {
        self.formValidate.applierId =
          self.$store.state.user.userData.data.userId;
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
        applyStatusList: self.applyStatusList.toString(),
        applierId: self.formValidate.applierId,
        toolNumber: self.searchCondition.toolNumber,
        fullNumber: self.searchCondition.fullNumber,
        toolNumber: self.searchCondition.toolNumber,
        warehouseCode: self.searchCondition.warehouseCode,
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
          url: "/tool/tool-scrip-apply-page-list.json",
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
              self.formValidate.auiterId = data.data.pkId;
              self.formValidate.auiterName = data.data.staffName;
              self.formValidate.auitDepartmentId = data.data.departmentId;
              self.formValidate.auitDepartmentName = data.data.departmentName;
              self.auditDepartment = data.data.departmentName;
              self.auditStaffInfo =
                data.data.staffCode + "-" + data.data.staffName;
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
    addNewApply() {
      this.isDisabled = true;
      const self = this;
      let para = {
        toolNumber: self.toolNumber,
        toolMap: self.toolMap,
        toolName: self.toolName,
        fullNumber: self.formValidate.fullNumber,
        warehouseCode: self.warehouseCode,
        applierCode: self.formValidate.applierCode,
        departmentId: self.formValidate.departmentId,
        departmentName: self.formValidate.departmentName,
        scripResion: self.formValidate.scripResion,
        applyStatus: self.formValidate.applyStatus,
        scripRemark: self.formValidate.scripRemark
      };

      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/tool-scrip-apply-add",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          self.toolApplyModalshow = false;
          if (res.data.code === 200) {
            self.$Message.success("报废报告信息已记录!");
            self.isDisabled = false;
            self.getListData();
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
    editApply() {
      this.isDisabled = true;
      const self = this;
      let para = {
        pkId: self.formValidate.pkId,
        toolNumber: self.toolNumber,
        toolMap: self.toolMap,
        toolName: self.toolName,
        fullNumber: self.formValidate.formValidate,
        warehouseCode: self.warehouseCode,
        applierCode: self.formValidate.applierCode,
        departmentId: self.formValidate.departmentId,
        departmentName: self.formValidate.departmentName,
        scripResion: self.formValidate.scripResion,
        applyStatus: self.formValidate.applyStatus,
        scripRemark: self.formValidate.scripRemark
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/tool-scrip-apply-update",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          self.toolApplyModalshow = false;
          if (res.data.code === 200) {
            self.$Message.success("报废申请内容已变更!");
            self.isDisabled = false;
            self.getListData();
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
    deleteRowDate(pkId, fullNumber) {
      let para = {
        pkId: pkId,
        fullNumber: fullNumber
      };
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/tool-scrip-apply-delete-by-id",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          self.deleteModalShow = false;
          if (res.data.code === 200) {
            self.$Message.success("报废申请删除成功!");
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
    show(index) {
      const self = this;
      self.toolApplyModalshow = true;
      self.isModify = true;
      self.formValidate = objCopy(self.tableData.rows[index], {});
      self.toolNumber = self.formValidate.toolNumber;
      self.toolMap = self.formValidate.toolMap;
      self.toolName = self.formValidate.toolName;
      self.warehouseCode = self.formValidate.warehouseCode;
      self.processAmount = self.formValidate.processAmount;
      self.disagreeRemark = "";
      const auditStatus = self.formValidate.applyStatus;
      if (auditStatus == 0 || auditStatus == -1) {
        if (this.applyAuth) {
          self.btnSave = true;
          self.btnSubmit = true;
        } else {
          self.btnSave = false;
          self.btnSubmit = false;
        }
        let departId = self.formValidate.departmentId;
        self.currentStep = 0;
        self.btnAudit = false;
        self.radioAudit = false;
      } else if (auditStatus == 1 || auditStatus == -2) {
        if (self.technologyAuth) {
          self.isAudit = true;
          self.radioAudit = true;
          self.btnAudit = true;
        } else {
          self.isAudit = false;
          self.radioAudit = false;
          self.btnAudit = false;
        }
        self.auditStaffInfo = "";
        self.searchStaff(self.formValidate.applierId);
        self.searchAuditStaff(self.$store.state.user.userData.data.userId);
        self.currentStep = 1;
        self.btnSave = false;
        self.btnSubmit = false;
        self.agreeFlag = 2;
        self.agreeStatus = 2;
        self.disagreeStatus = -1;
      } else if (auditStatus == 2) {
        self.searchStaff(self.formValidate.applierId);
        self.currentStep = 2;
        self.isAudit = true;
        self.btnSave = false;
        self.btnSubmit = false;
        self.btnAudit = false;
        self.radioAudit = false;
      }
    },
    remove(index) {
      this.deleteRowData = this.tableData.rows[index];
      this.deleteModalShow = true;
    },
    showAuditLog(pkId) {
      const self = this;
      //self.isModify = false;

      self.$refs.auditLogModel.showModal(pkId);
    },
    addApply() {
      this.isModify = false;
      const self = this;
      self.isAudit = false;
      self.btnSave = true;
      self.btnSubmit = true;
      self.btnAudit = false;
      self.radioAudit = false;
      self.toolApplyModalshow = true;
      self.toolMap = "";
      self.toolName = "";
      self.warehouseCode = "";
      self.toolNumber = "";
      self.staffInfo = "";
      self.formValidate = {};
      self.formValidate.needQty = 1;
      self.searchStaff(self.$store.state.user.userData.data.userId);
      self.$refs["formValidate"].resetFields();
    },
    deleteModalSureBtn() {
      this.deleteRowDate(
        this.deleteRowData.pkId,
        this.deleteRowData.fullNumber
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
          if (self.isModify) {
            self.editApply();
          } else {
            self.addNewApply();
          }
        } else {
          self.$Message.error("请输入正确信息!");
        }
      });
    },
    searchTool() {
      const self = this;
      if (!!!self.formValidate.fullNumber) {
        return;
      } else {
        let paras = {
          fullNumber: self.formValidate.fullNumber
        };
        axios
          .request({
            headers: {
              "Content-Type": "application/json; charset=UTF-8"
            },
            url: "/tool/scrip-fullNumber-validate.json",
            method: "post",
            isAuth: true,
            params: paras
          })
          .then(function(result) {
            if (result.data) {
              let para = {
                fullNumber: self.formValidate.fullNumber
              };
              axios
                .request({
                  headers: {
                    "Content-Type": "application/json; charset=UTF-8"
                  },
                  url: "/tool/tool-get-by-full-number.json",
                  method: "post",
                  isAuth: true,
                  params: para
                })
                .then(function(res) {
                  const data = res.data;
                  if (data.code === 200) {
                    self.toolMap = data.data.toolMap;
                    self.warehouseCode = data.data.warehouseCode;
                    self.toolNumber = data.data.toolNumber;
                    self.toolName = data.data.toolName;
                    self.processAmount = data.data.processAmount;
                  } else {
                    self.$Notice.error({
                      title: "错误提示",
                      desc: res.data.info
                    });
                  }
                });
            } else {
              self.btnSave = false;
              self.btnSubmit = false;
              self.$Notice.error({
                title: "错误提示",
                desc: "该刀具已提交报废审核申请，请重新确认！"
              });
            }
          })
          .catch(function(err) {
            console.log(err);
          });
      }
    },
    handleSubmit(name) {
      const self = this;
      self.formValidate.applyStatus = 1;
      self.$refs[name].validate(valid => {
        if (valid) {
          if (self.isModify) {
            self.editApply();
          } else {
            self.addNewApply();
          }
        } else {
          self.$Message.error("请输入正确信息!");
        }
      });
    },
    handleAudit() {
      this.isDisabled = true;
      const self = this;
      let para = {
        pkId: self.formValidate.pkId,
        fullNumber: self.formValidate.fullNumber,
        applyStatus: self.agreeFlag,
        disagreeRemark: self.disagreeRemark,
        auiterId: self.formValidate.auiterId,
        auiterName: self.formValidate.auiterName,
        auitDepartmentId: self.formValidate.auitDepartmentId,
        auitDepartmentName: self.formValidate.auitDepartmentName
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/scrip-report-audit.json",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          self.toolApplyModalshow = false;
          if (res.data.code === 200) {
            self.$Message.success("报告审核成功!");
            self.isDisabled = false;
            self.getListData();
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
    handleReset(name) {
      this.toolApplyModalshow = false;
      this.$refs[name].resetFields();
    }
  }
};
</script>

<style>
</style>
