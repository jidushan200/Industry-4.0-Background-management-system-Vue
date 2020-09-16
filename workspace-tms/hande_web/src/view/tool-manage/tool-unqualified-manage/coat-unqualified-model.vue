<template>
  <!--报告信息弹出modal-->
  <Modal v-model="reportDetailModalshow" :title="isModify?'详情报告':'新增报告'" width="1024" draggable>
    <div style="height:600px;overflow-y:auto; overflow-x:hidden">
    <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="80">
      <Row>
        <Col span="8">
        <FormItem label="物料条码" prop="fullNumber">
          <Input v-model="formValidate.fullNumber"></Input>
        </FormItem>
        </Col>
        <Col span="8">
        <FormItem label="物料名称" prop="toolName">
          <Input v-model="toolName" readonly />
        </FormItem>
        </Col>
        <Col span="8">
        <FormItem label="物料图号" prop="toolMap">
          <Input v-model="toolMap" readonly />
        </FormItem>
        </Col>
      </Row>
      <Row>
        <Col span="6">
        <FormItem label="涂层厂家" prop="supplierName">
          <Input v-model="supplierName" />
        </FormItem>
        </Col>
        <Col span="6">
        <FormItem label="送检时间" prop="deliveryTime">
          <DatePicker type="datetime" v-model="deliveryTime" readonly></DatePicker>
        </FormItem>
        </Col>
        <Col span="6">
        <FormItem label="质检结果" prop="checkResult">
          <RadioGroup v-model="checkResult" readonly>
            <Radio :label="1">合格</Radio>
            <Radio :label="2">不合格</Radio>
          </RadioGroup>
        </FormItem>
        </Col>
        <Col span="6">
        <FormItem label="质检时间" prop="checkTime">
          <DatePicker type="datetime" v-model="checkTime" readonly></DatePicker>
        </FormItem>
        </Col>
      </Row>
      <Row>
        <Col span="24">
        <FormItem label="报告描述" prop="reportDesc">
          <Input v-model="formValidate.reportDesc" style="width:100%;" />
        </FormItem>
        </Col>
      </Row>
      <Row>
        <Col span="24" class="ivu-form">
        <div class="ivu-form-item">
          <label class="ivu-form-item-label" style="width: 100px;text-align: right;">质检明细</label>
          <div class="ivu-form-item-content" style="margin-left: 100px; ">
            <Table :columns="tableColumns" :data="checkItemList" border stripe highlight-row height="200"></Table>
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
      <Row style="border-top:1px solid #e3e3e3;padding-top: 10px;margin-top: -5px;margin-bottom: -5px;" v-show="auditStatus!=0">
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
        <Col span="24" v-if="btnAudit" class="ivu-form">
        <FormItem label="意见描述" prop="remark">
          <Input v-model="remark" type="textarea" :rows="1" placeholder="意见描述" style="width:100%;" />
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
import coat1 from "./step/coat1.png";
import coat2 from "./step/coat2.png";
import coat3 from "./step/coat3.png";
import coat4 from "./step/coat4.png";
import coat5 from "./step/coat5.png";
import coat_1 from "./step/coat-1.png";
import coat_2 from "./step/coat-2.png";
import coat_3 from "./step/coat-3.png";

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
    //审核（生产部）
    productionAuth() {
      return hasOneOf(["0103060303"], this.access);
    },
    //审核(工艺部)
    technologyAuth() {
      return hasOneOf(["0103060304"], this.access);
    },
    //审核(质检部领导)
    qualityLeaderAuth() {
      return hasOneOf(["0103060305"], this.access);
    },
    //入库(库房)
    warehouseAuth() {
      return hasOneOf(["0103060306"], this.access);
    }
  },
  data() {
    return {
      isDisabled: false,
      reportDetailModalshow: false,
      coat1,
      coat2,
      coat3,
      coat4,
      coat5,
      coat_1,
      coat_2,
      coat_3,
      step: "",
      isModify: false, //是否是修改明细操作
      toolName: "",
      toolMap: "",
      warehouseCode: "",
      deliveryTime: null,
      supplierName: "",
      checker: "",
      auditor: "",
      checkResult: "",
      checkTime: "",
      btnSave: false, //保存按钮可见
      btnSubmit: false, //提交按钮可见
      auditText: "审核",
      btnAudit: false, //审核按钮可见
      radioVisible: false,
      useRadio: 2,
      gobackRadio: -2,
      checkItemList: [],
      appendixList: [],
      auditStatus: -2, //当前审核状态
      nextStatus: -2, //下一审核状态
      remark: "", //审核备注
      tableColumns: [
        {
          title: "质检项",
          key: "checkItem",
          width: 220
        },
        {
          title: "质检标准",
          key: "itemStandard",
          width: 120
        },
        {
          title: "上偏值",
          key: "upDeviation",
          width: 120
        },
        {
          title: "下偏值",
          key: "downDeviation",
          width: 120
        },
        {
          title: "实测值",
          key: "measuredValue",
          width: 120
        },
        {
          title: "单位",
          key: "unit",
          width: 80
        },
        {
          title: "结果",
          key: "checkResult",
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
      self.warehouseCode = "";
      self.checker = "";
      self.auditor = "";
      self.checkResult = "";
      self.checkTime = "";
      self.btnSave = false;
      self.btnSubmit = false;
      self.btnAudit = false;
      self.radioVisible = false;
      self.remark = "";
      self.nextStatus = 0;
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
              self.formValidate.fullNumber = report.toolCheck.fullNumber;
              self.formValidate.reportDesc = report.reportDesc;
              self.auditor = report.auditor;
              self.toolName = report.toolCheck.toolName;
              self.toolMap = report.toolCheck.toolMap;
              self.supplierName = report.toolCheck.supplierName;
              self.warehouseCode = report.toolCheck.warehouseCode;
              self.checker = report.toolCheck.checker;
              self.checkResult = report.toolCheck.checkResult;
              self.deliveryTime = new Date(report.toolCheck.deliveryTime);
              self.checkTime = new Date(report.toolCheck.checkTime);
              self.auditStatus = report.coatAuditStatus;
              self.checkItemList = report.toolCheck.resultList;
              self.appendixList = report.toolCheck.appendixList;
              self.pkId = pkId;
              if (self.auditStatus != 0) {
                self.btnSave = false;
                self.btnSubmit = false;
                //self.btnAudit = true;
              } else {
                self.btnSave = true;
                self.btnSubmit = true;
              }
              if (self.auditStatus == 1) {
                //生产部审核
                self.step = self.coat1;
                if (self.productionAuth) {
                  self.radioVisible = true;
                  self.useRadio = 2;
                  self.gobackRadio = -2;
                  self.btnAudit = true;
                } else {
                  self.btnAudit = false;
                }
              } else if (self.auditStatus == -1) {
                self.step = self.coat_1;
                self.btnAudit = false;
                self.radioVisible = false;
              } else if (self.auditStatus == -2) {
                self.step = self.coat_2;
                self.btnAudit = false;
                self.radioVisible = false;
              } else if (self.auditStatus == 2) {
                // self.auditText ="审核";
                self.step = self.coat2;
                //工艺部审核
                if (self.technologyAuth) {
                  self.useRadio = 3;
                  self.gobackRadio = -3;
                  self.radioVisible = true;
                  self.btnAudit = true;
                } else {
                  self.btnAudit = false;
                }
              } else if (self.auditStatus == -3) {
                //生产部退回
                if (self.productionAuth) {
                  self.step = self.coat_3;
                  self.radioVisible = false;
                  self.nextStatus = -1;
                  self.auditText = "退回";
                  self.btnAudit = true;
                } else {
                  self.btnAudit = false;
                }
              } else if (self.auditStatus == 3) {
                //质检部领导审批
                self.step = self.coat3;
                if (self.qualityLeaderAuth) {
                  self.radioVisible = false;
                  self.nextStatus = 4;
                  self.auditText = "同意";
                  self.btnAudit = true;
                } else {
                  self.btnAudit = false;
                }
              } else if (self.auditStatus == 4) {
                self.step = self.coat4;
                if (self.warehouseAuth) {
                  self.radioVisible = false;
                  self.nextStatus = 5;
                  self.auditText = "入库";
                  self.btnAudit = true;
                } else {
                  self.btnAudit = false;
                }
              } else if (self.auditStatus == 5) {
                self.step = self.coat5;
                self.radioVisible = false;
                self.btnAudit = false;
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
      const self = this;
      this.isDisabled = true;
      let para = {
        reportNumber: self.formValidate.reportNumber,
        toolId: self.formValidate.toolId,
        toolNumber: self.formValidate.toolNumber,
        fullNumber: self.formValidate.fullNumber,
        toolName: self.formValidate.toolName,
        reportDesc: self.formValidate.reportDesc,
        reportStatus: self.formValidate.status,
        reportType: 3
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
        fullNumber: self.formValidate.fullNumber,
        reportDesc: self.formValidate.reportDesc,
        reportStatus: self.formValidate.status,
        reportType: 3
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
