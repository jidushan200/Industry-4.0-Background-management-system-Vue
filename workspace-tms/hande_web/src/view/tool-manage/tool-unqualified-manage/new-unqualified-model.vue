<template>
  <!--报告信息弹出modal-->
  <Modal v-model="reportDetailModalshow" :title="isModify?'详情报告':'新增报告'" width="1024" draggable>
    <div style="height:600px;overflow-y:auto; overflow-x:hidden">
    <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="80">
      <Row>
        <Col span="6">
        <FormItem label="物料编码" prop="toolNumber">
          <Input v-model="formValidate.toolNumber"></Input>
        </FormItem>
        </Col>
        <Col span="6">
        <FormItem label="物料名称" prop="toolName">
          <Input v-model="toolName" readonly />
        </FormItem>
        </Col>
        <Col span="6">
        <FormItem label="物料图号" prop="toolMap">
          <Input v-model="toolMap" readonly />
        </FormItem>
        </Col>
        <Col span="6">
        <FormItem label="顺序号" prop="toolSeq">
          <Input v-model="toolSeq" readonly />
        </FormItem>
        </Col>
      </Row>
      <Row>
        <Col span="6">
        <FormItem label="供应商名称" prop="supplierName">
          <Input v-model="supplierName" readonly style="width:150px;" />
        </FormItem>
        </Col>
        <Col span="6">
        <FormItem label="送货时间" prop="deliveryTime">
          <DatePicker type="datetime" v-model="deliveryTime" placeholder="选择送货时间" style="width:150px;"></DatePicker>
        </FormItem>
        </Col>
        <Col span="6">
        <FormItem label="质检结果" prop="checkResult">
          <RadioGroup v-model="checkResult">
            <Radio :label="1">合格</Radio>
            <Radio :label="2">不合格</Radio>
          </RadioGroup>
        </FormItem>
        </Col>
        <Col span="6">
        <FormItem label="质检时间" prop="checkTime">
          <DatePicker type="datetime" v-model="checkTime" placeholder="选择质检时间" style="width:150px;"></DatePicker>
        </FormItem>
        </Col>
      </Row>
      <Row>
        <Col span="24">
        <FormItem label="报告描述" prop="reportDesc">
          <Input v-model="formValidate.reportDesc" placeholder="" style="width:100%;" />
        </FormItem>
        </Col>
      </Row>
      <Row>
        <Col span="24" class="ivu-form">
        <div class="ivu-form-item">
          <label class="ivu-form-item-label" style="width: 100px;text-align: right;">质检明细</label>
          <div class="ivu-form-item-content" style="margin-left: 100px; ">
            <Table :columns="tableColumns" :data="checkItemList" border stripe highlight-row height="250"></Table>
          </div>
        </div>
        </Col>
        <Col span="24" class="ivu-form" v-show="appendixList.length>0">
        <div class="ivu-form-item">
          <label class="ivu-form-item-label" style="width: 100px;text-align: right;">相关附件</label>
          <div class="ivu-form-item-content" style="margin-left: 100px; ">
            <Button type="dashed" style="margin-right: 10px;" v-for="item in appendixList" :key="item.pkId" @click="downFile(item.pkId)">{{item.sourceName}}</Button>
          </div>
        </div>
        </Col>
      </Row>
      <Row style="border-top:1px solid #e3e3e3;padding-top: 40px;margin-top: 20px;" v-show="auditStatus!=0">
        <Col span="24" class="ivu-form">
        <div class="ivu-form-item">
          <label class="ivu-form-item-label" style="width: 100px;text-align: right;">审核进度</label>
          <div>
            <img :src="step" style="width:80%;height:80%" />
          </div>
        </div>
        </Col>
        <Col span="6" class="ivu-form" v-if="radioVisible">
        <FormItem label="审核意见" prop="nextStatus">
          <RadioGroup v-model="nextStatus">
            <Radio :label="useRadio">让步使用</Radio>
            <Radio :label="gobackRadio">退回</Radio>
          </RadioGroup>
        </FormItem>
        </Col>
        <Col span="6" class="ivu-form" v-if="auditor && btnAudit">
        <FormItem label="审核人" prop="auditor">
          <Input v-model="auditor" readonly style="width:300px;" />
        </FormItem>
        </Col>
        <Col span="24" class="ivu-form" v-if="btnAudit">
        <FormItem label="意见描述" prop="remark">
          <Input v-model="remark" type="textarea" :rows="2" placeholder="意见描述" style="width:100%;" />
        </FormItem>
        </Col>
      </Row>
    </Form>
    </div>
    <div slot="footer">
      <Button @click="handleSave('formValidate')" v-if="btnSave && qualityAuth" type="primary" :disabled="isDisabled">保存</Button>
      <Button @click="handleSubmit('formValidate')" v-if="btnSubmit && qualityAuth" type="warning" :disabled="isDisabled">提交</Button>
      <Button @click="handleAudit()" v-if="btnAudit" type="warning">{{auditText}}</Button>
      <Button @click="handleReset('formValidate')" style="margin-left: 8px">关闭</Button>
    </div>
  </Modal>
</template>
<script>
import { dateFormat, objCopy, hasOneOf } from "@/libs/tools.js";
import axios from "@/libs/api.request";
import { getToken, getBaseUrl } from "@/libs/util";
import unqualified_form_config from "./unqualified-form-config.js";
import new1 from "./step/new1.png";
import new2 from "./step/new2.png";
import new3 from "./step/new3.png";
import new4 from "./step/new4.png";
import new5 from "./step/new5.png";
import new_1 from "./step/new-1.png";
import new_2 from "./step/new-2.png";
import new_3 from "./step/new-3.png";
export default {
  name: "page",
  computed: {
    access() {
      return this.$store.state.user.userData.data.access;
    },
    //保存、提交、删除（质检部）
    qualityAuth() {
      return hasOneOf(["0103060301"], this.access);
    },
    //审核(采购部)
    purchaseAuth() {
      return hasOneOf(["0103060302"], this.access);
    },
    //审核(工艺部)
    technologyAuth() {
      return hasOneOf(["0103060304"], this.access);
    },
    //审核(质检部领导)
    qualityLeaderAuth() {
      return hasOneOf(["0103060305"], this.access);
    }
  },
  data() {
    return {
      isDisabled: false,
      reportDetailModalshow: false,
      new1,
      new2,
      new3,
      new4,
      new5,
      new_1,
      new_2,
      new_3,
      step: "",
      isModify: false, //是否是修改明细操作
      toolName: "",
      toolMap: "",
      toolSeq: "",
      auditor: "",
      warehouseCode: "",
      deliveryTime: null,
      supplierName: "",
      checker: "",
      checkResult: "",
      checkTime: "",
      btnSave: true, //保存按钮可见
      btnSubmit: true, //提交按钮可见
      auditText: "审核",
      btnAudit: false, //审核按钮可见
      radioVisible: false,
      useRadio: 2,
      gobackRadio: -2,
      checkItemList: [],
      appendixList: [],
      currentStep: 0, //当前步骤
      auditStatus: -2, //当前审核状态
      nextStatus: -2, //下一审核状态
      remark: "", //审核备注
      step2content: "工艺部查询检验报告判断重修/报废",
      step3title: "报废",
      step3content: "库房报废",
      tableColumns: [
        {
          title: "质检项",
          key: "checkItem"
        },
        {
          title: "质检标准",
          key: "itemStandard",
          winWidth: 120
        },
        {
          title: "上偏差值",
          key: "upDeviation",
          width: 100
        },
        {
          title: "下偏差值",
          key: "downDeviation",
          width: 100
        },
        {
          title: "实测值",
          key: "measuredValue",
          width: 100
        },
        {
          title: "单位",
          key: "unit",
          width: 100
        },
        {
          title: "结果",
          key: "checkResult",
          width: 100,
          render: function(h, params) {
            let temp = "";
            switch (params.row.checkResult) {
              case 1:
                temp = "合格";
                break;
              case 2:
                temp = "不合格";
                break;
              default:
                break;
            }
            return h("div", temp);
          }
        }
      ],
      formValidate: unqualified_form_config.formValidate, //form表单字段
      ruleValidate: unqualified_form_config.ruleValidate //form表单验证规则
    };
  },
  created() {},
  methods: {
    init() {
      const self = this;
      self.isModify = false;
      self.formValidate = {};
      self.$refs["formValidate"].resetFields();
      self.checkItemList = [];
      self.appendixList = [];
      self.toolName = "";
      self.checker = "";
      (self.auditor = ""), (self.checkResult = "合格");
      self.checkTime = "";
      self.btnSave = true;
      self.btnSubmit = true;
      self.btnAudit = false;
      self.radioVisible = false;
      self.nextStatus = null;
      self.remark = "";
    },
    showModal(pkId) {
      const self = this;
      self.init();
      if (pkId) {
        self.isModify = true;
        let para = {
          pkId: pkId
        };
        axios
          .request({
            headers: {
              "Content-Type": "application/json; charset=UTF-8"
            },
            url: "/tool/unqualified-report-get-by-id.json",
            method: "post",
            isAuth: true,
            params: para
          })
          .then(function(res) {
            if (res.data.code === 200) {
              const report = res.data.data;
              self.formValidate.toolNumber = report.toolCheck.toolNumber;
              self.formValidate.reportDesc = report.reportDesc;
              self.auditor = report.auditor;
              self.toolName = report.toolCheck.toolName;
              self.toolMap = report.toolCheck.toolMap;
              self.toolSeq = report.toolCheck.toolSeq;
              self.supplierName = report.toolCheck.supplierName;
              self.checker = report.toolCheck.checker;
              self.checkResult = report.toolCheck.checkResult;
              self.deliveryTime = new Date(report.toolCheck.deliveryTime);
              self.checkTime = new Date(report.toolCheck.checkTime);
              self.auditStatus = report.newAuditStatus;
              self.checkItemList = report.toolCheck.resultList;
              self.appendixList = report.toolCheck.appendixList;
              self.pkId = pkId;
              if (self.auditStatus != 0) {
                self.btnSave = false;
                self.btnSubmit = false;
                //self.btnAudit = true;
              }
              if (self.auditStatus == 1) {
                self.step = self.new1;
                //采购部审核权限
                if (self.purchaseAuth) {
                  self.radioVisible = true;
                  self.useRadio = 2;
                  self.gobackRadio = -2;
                  self.btnAudit = true;
                } else {
                  self.btnAudit = false;
                }
              } else if (self.auditStatus == -1) {
                self.btnAudit = false;
                self.radioVisible = false;
                self.step = self.new_1;
              } else if (self.auditStatus == -2) {
                self.btnAudit = false;
                self.radioVisible = false;
                self.step = self.new_2;
              } else if (self.auditStatus == 2) {
                self.step = self.new2;
                //工艺部审核权限
                if (self.technologyAuth) {
                  self.radioVisible = true;
                  self.useRadio = 3;
                  self.gobackRadio = -3;
                  self.btnAudit = true;
                } else {
                  self.btnAudit = false;
                }
              } else if (self.auditStatus == -3) {
                self.step = self.new_3;
                //采购部退回
                if (self.purchaseAuth) {
                  self.radioVisible = false;
                  self.nextStatus = -1;
                  self.auditText = "退回";
                  self.btnAudit = true;
                } else {
                  self.btnAudit = false;
                }
              } else if (self.auditStatus == 3) {
                self.step = self.new3;
                //质检部领导审批
                if (self.qualityLeaderAuth) {
                  self.radioVisible = false;
                  self.nextStatus = 4;
                  self.auditText = "同意";
                  self.btnAudit = true;
                } else {
                  self.btnAudit = false;
                }
              } else if (self.auditStatus == 4) {
                self.radioVisible = false;
                self.nextStatus = 5;
                self.auditText = "入库";
                self.step = self.new4;
              } else if (self.auditStatus == 5) {
                self.radioVisible = false;
                self.btnAudit = false;
                self.step = self.new5;
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
      } else {
        self.auditStatus = 0;
      }
      self.reportDetailModalshow = true;
    },

    downFile(pkId) {
      window.location.href =
        getBaseUrl() +
        "/tool/appendix-download?pkId=" +
        pkId +
        "&token=" +
        getToken();
    },
    handleSave(name) {
      const self = this;
      self.formValidate.status = 0;
      self.$refs[name].validate(valid => {
        if (valid) {
          if (self.isModify) {
            self.editReportSubmit();
          } else {
            self.addReportSubmit();
          }
        } else {
          self.$Message.error("请输入正确信息!");
        }
      });
    },
    handleSubmit(name) {
      const self = this;
      self.formValidate.status = 1;
      self.$refs[name].validate(valid => {
        if (valid) {
          if (self.isModify) {
            self.editReportSubmit();
          } else {
            self.addReportSubmit();
          }
        } else {
          self.$Message.error("请输入正确信息!");
        }
      });
    },
    addReportSubmit() {
      this.isDisabled = true;
      const self = this;
      let para = {
        toolId: self.formValidate.toolId,
        toolNumber: self.formValidate.toolNumber,
        toolName: self.formValidate.toolName,
        reportDesc: self.formValidate.reportDesc,
        reportStatus: self.formValidate.status,
        reportType: 1
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/unqualified-report-add.json",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          self.reportDetailModalshow = false;
          if (res.data.code === 200) {
            self.$Message.success("报告保存成功!");
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
    editReportSubmit() {
      this.isDisabled = true;
      const self = this;
      let para = {
        pkId: self.pkId,
        reportDesc: self.formValidate.reportDesc,
        reportStatus: self.formValidate.status,
        reportType: 1
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/unqualified-report-update.json",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          self.reportDetailModalshow = false;
          if (res.data.code === 200) {
            self.$Message.success("报告保存成功!");
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

    handleAudit() {
      const self = this;
      if (!!!self.nextStatus) {
        self.$Message.error("请选择审核意见");
        return;
      }
      let para = {
        pkId: self.pkId,
        fullNumber: self.formValidate.fullNumber,
        auditStatus: self.nextStatus,
        remark: self.remark
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/unqualified-report-audit.json",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          self.reportDetailModalshow = false;
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
    },
    handleReset(name) {
      const self = this;
      self.reportDetailModalshow = false;
    }
  }
};
</script>
