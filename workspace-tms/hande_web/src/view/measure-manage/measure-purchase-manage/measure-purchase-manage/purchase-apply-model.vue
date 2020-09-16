<template>
  <!--申请单信息弹出modal-->
  <Modal v-model="purchaseApplyModalshow" title="采购申请单" width="1100" draggable>
    <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="100">
      <Row>
        <Col span="6">
        <FormItem label="物料编码" prop="measureNumber">
          <Input style="width:170px" v-model="formValidate.measureNumber" readonly />
        </FormItem>
        </Col>
        <Col span="6">
        <FormItem label="型号规格" prop="model">
          <Input style="width:170px" v-model="formValidate.model" readonly />
        </FormItem>
        </Col>
        <Col span="6">
        <FormItem label="物料名称" prop="measureName">
          <Input style="width:170px" v-model="formValidate.measureName" readonly />
        </FormItem>
        </Col>
        <Col span="6">
        <FormItem label="TMS库存" prop="inventoryQty">
          <Input style="width:170px" v-model="formValidate.inventoryQty" readonly />
        </FormItem>
        </Col>
      </Row>
      <Row>
        <Col span="6">
        <FormItem label="ERP库存" prop="erpQty">
          <Input style="width:170px" v-model="formValidate.erpQty" readonly />
        </FormItem>
        </Col>
        <Col span="6">
        <FormItem label="申购原因" prop="purchaseReasion">
          <Select style="width:170px" v-model="formValidate.purchaseReasion" ref="purchaseResion" placeholder="请选择采购原因" disabled>
            <Option v-for="item in purchaseResionType" :value="item.value" :key="item.value">{{ item.name }}</Option>
          </Select>
        </FormItem>
        </Col>
        <Col span="6">
        <FormItem label="申请部门" prop="applyDepartment">
          <Input v-model="departmentName" style="width:170px" readonly />
        </FormItem>
        </Col>
        <Col span="6">
        <FormItem label="申请人" prop="applicant">
          <Input v-model="applierName" style="width:170px" readonly />
        </FormItem>
        </Col>
      </Row>
      <Row>

        <Col span="6">
        <FormItem label="需求时间" prop="demandTime">
          <DatePicker type="date" style="width: 170px" v-model="formValidate.demandTime" placeholder readonly></DatePicker>
        </FormItem>
        </Col>
        <Col span="6">
        <FormItem label="申购数量" prop="applyQty">
          <Input style="width:170px" v-model="formValidate.applyQty" placeholder="输入需求数量" readonly />
        </FormItem>
        </Col>
        <Col span="6">
        <FormItem label="申购时间" prop="applyTime">
          <DatePicker type="datetime" style="width: 170px" v-model="formValidate.applyTime" readonly></DatePicker>
        </FormItem>
        </Col>
        <Col span="6">
        <FormItem label="已到货数量" prop="arrivaledQty" v-if="btnReturn? false:true">
          <Input style="width:170px" v-model="formValidate.arrivaledQty" readonly> </Input>
        </FormItem>
        </Col>
      </Row>
      <Row>
        <Col span="12">
        <FormItem label="申购备注" prop="remark">
          <Input type="textarea" :rows="1" style="width:437px" v-model="formValidate.remark" readonly />
        </FormItem>
        </Col>
        <Col span="12">
        <FormItem label="可用序号" prop="availableNumber">
          <Input style="width:437px" v-model="formValidate.availableNumber" readonly />
        </FormItem>
        </Col>
      </Row>
      <Row>
      </Row>
      <Row style="border-top:1px solid #e3e3e3;padding-top: 20px;" v-if="btnReturn">
        <Col span="6">
        <FormItem label="审核人" prop="auditStaffInfo">
          <Input style="width:160px" v-model="auditStaffInfo" readonly />
        </FormItem>
        </Col>
        <Col span="18">
        <FormItem label="意见描述" prop="auditRemark">
          <Input v-model="auditRemark" type="textarea" :rows="1" placeholder="意见描述" style="width:702px;" />
        </FormItem>
        </Col>
      </Row>
    </Form>
    <Table :columns="tableColumns" :data="logList" height="300" border stripe highlight-row></Table>
    <div slot="footer">
      <Button @click="handleReceive()" v-if="btnReturn" type="success" :disabled="isDisabled">接收</Button>
      <Button @click="handleReturn()" v-if="btnReturn" type="error" :disabled="isDisabled">驳回</Button>
      <Button @click="handleReset()" style="margin-left: 8px">关闭</Button>
    </div>
  </Modal>
</template>
<script>
import { dateFormat, objCopy } from "@/libs/tools.js";
import axios from "@/libs/api.request";
import { getToken, getBaseUrl } from "@/libs/util";
import apply_form_config from "./apply-form-config.js";
import purchaseResionType from "../measure-purchase-list/purchaseResion-type";
export default {
  data() {
    return {
      isDisabled: false,
      btnReturn: false,
      auditStaffInfo: "",
      auditRemark: "",
      departmentName: "",
      applierName: "",
      logList: [],
      purchaseApplyModalshow: false,
      purchaseResionType: purchaseResionType,
      tableColumns: [
        {
          title: "审核次序",
          key: "auditSeq",
          width: 90
        },
        {
          title: "审核部门",
          key: "auditDepartmentName",
          width: 120
        },
        {
          title: "审核领导",
          key: "auditorName",
          width: 90
        },
        {
          title: "审核结果",
          key: "auditResult",
          minwidth: 180
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
  created() {},
  methods: {
    init() {
      const self = this;
    },
    showModal(pkId, isReturn) {
      const self = this;
      let para = {
        pkId: pkId
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/measure/measure-purchase-apply-get-by-id.json",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            self.formValidate = objCopy(data.data, {});
            self.formValidate.demandTime = dateFormat(
              new Date(self.formValidate.demandTime),
              "yyyy-MM-dd HH:mm:ss"
            );
            self.formValidate.applyTime = dateFormat(
              new Date(self.formValidate.applyTime),
              "yyyy-MM-dd HH:mm:ss"
            );
            self.applierName = data.data.applierName;
            self.departmentName = data.data.applyDepartmentName;
            self.logList = data.data.auditList;
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
      if (isReturn) {
        self.btnReturn = true;
        self.searchAuditStaff();
      }
      self.auditRemark = "";
      self.purchaseApplyModalshow = true;
    },
    handleReturn() {
      const self = this;
      if (!!!self.auditRemark) {
        self.$Notice.error({
          title: "错误提示",
          desc: "审核意见不完整，请补充后驳回"
        });
      } else {
        this.isDisabled = true;
        let para = {
          pkId: self.formValidate.pkId,
          measureNumber:self.formValidate.measureNumber,
          applyStatus: 0,
          auditRemark: self.auditRemark,
          auitDepartmentId: self.auitDepartmentId,
          auitDepartmentName: self.auitDepartmentName
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
            self.purchaseApplyModalshow = false;
            if (res.data.code === 200) {
              self.$Message.success("报告审核成功!");
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
    handleReceive() {
      this.isDisabled = true;
      const self = this;
      let para = {
        pkId: self.formValidate.pkId,
        applyStatus: 7
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
          const data = res.data;
          if (data.code === 200) {
            self.purchaseApplyModalshow = false;
            self.$Message.success("采购任务已接收!");
            self.isDisabled = false;
            self.$emit("search");
          } else {
            self.isDisabled = false;
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
              self.auitDepartmentId = data.data.departmentId;
              self.auitDepartmentName = data.data.departmentName;
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
    handleReset() {
      const self = this;
      self.purchaseApplyModalshow = false;
    }
  }
};
</script>
