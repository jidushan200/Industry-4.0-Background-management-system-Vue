<template>
  <!--申请单信息弹出modal-->
  <Modal v-model="purchaseApplyModalshow" title="采购申请单" width="1100" draggable>
    <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="100">
      <Row>
        <Col span="6">
        <FormItem label="物料编码" prop="toolNumber">
          <Input style="width:170px" v-model="formValidate.toolNumber" readonly />
        </FormItem>
        </Col>
        <Col span="6">
        <FormItem label="物料图号" prop="toolMap">
          <Input style="width:170px" v-model="formValidate.toolMap" readonly />
        </FormItem>
        </Col>
        <Col span="6">
        <FormItem label="物料名称" prop="toolName">
          <Input style="width:170px" v-model="formValidate.toolName" readonly />
        </FormItem>
        </Col>
        <Col span="6">
        <FormItem label="TMS库存" prop="keepQty">
          <Input style="width:170px" v-model="formValidate.keepQty" readonly />
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
        <FormItem label="申购类型" prop="purchaseType">
          <Select style="width:170px" v-model="formValidate.purchaseType" ref="purchaseType" placeholder="请选择采购类型" readonly>
            <Option v-for="item in purchaseType" :value="item.value" :key="item.value">{{ item.name }}</Option>
          </Select>
        </FormItem>
        </Col>
        <Col span="6">
        <FormItem label="申购原因" prop="purchaseResion">
          <Select style="width:170px" v-model="formValidate.purchaseResion" ref="purchaseResion" placeholder="请选择采购原因" readonly>
            <Option v-for="item in purchaseResion" :value="item.value" :key="item.value">{{ item.name }}</Option>
          </Select>
        </FormItem>
        </Col>
        <Col span="6">
        <FormItem label="申请部门" prop="applyDepartment">
          <Input v-model="departmentName" style="width:170px" readonly />
        </FormItem>
        </Col>
      </Row>
      <Row>
        <Col span="6">
        <FormItem label="申请人" prop="applicant">
          <Input v-model="applierName" style="width:170px" readonly />
        </FormItem>
        </Col>
        <Col span="6">
        <FormItem label="需求时间" prop="applyTime">
          <DatePicker type="date" style="width: 170px" v-model="formValidate.applyTime" placeholder readonly></DatePicker>
        </FormItem>
        </Col>
        <Col span="6">
        <FormItem label="申购数量" prop="needQty">
          <InputNumber style="width:170px" v-model="formValidate.needQty" placeholder="申购数量" readonly></InputNumber>
          <Icon type="md-arrow-round-forward" slot="suffix" />
        </FormItem>
        </Col>
        <Col span="6">
        <FormItem label="已到货数量" prop="arrivaledQty" v-if="btnReturn? false:true">
          <Input style="width:170px" v-model="formValidate.arrivaledQty" readonly> </Input>
        </FormItem>
        </Col>
      </Row>
      <Row>
        <Col span="24" v-if="!!formValidate.availableNumber">
        <FormItem label="可用序号" prop="availableNumber">
          <Input style="width:967px" v-model="formValidate.availableNumber" readonly />
        </FormItem>
        </Col>
      </Row>
      <Row>
        <Col span="24">
        <FormItem label="申购备注" prop="remark">
          <Input type="textarea" :rows="1" style="width:967px" v-model="formValidate.remark" placeholder="" readonly />
        </FormItem>
        </Col>
      </Row>
      <Row style="border-top:1px solid #e3e3e3;padding-top: 20px;" v-if="btnReturn">
        <Col span="6">
        <FormItem label="审核人" prop="auditStaffInfo">
          <Input style="width:160px" v-model="auditStaffInfo" readonly />
        </FormItem>
        </Col>
        <Col span="18">
        <FormItem label="意见描述" prop="auditRemark">
          <Input v-model="auditRemark" type="textarea" :rows="1" placeholder="意见描述" style="width:700px;" />
        </FormItem>
        </Col>
      </Row>
    </Form>
    <Table :columns="tableColumns" :data="logList" height="180" border stripe highlight-row></Table>
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
import purchaseType from "../tool-purchase-list/purchaseType-type.js";
import purchaseResion from "../tool-purchase-list/purchaseResion-type";
export default {
  data() {
    return {
      isDisabled: false,
      btnReturn: false,
      auditRemark: "",
      auitDepartmentId: "",
      auitDepartmentName: "",
      auditStaffInfo: "",
      departmentName: "",
      applierName: "",
      logList: [],
      purchaseApplyModalshow: false,
      toReceiveModalShow: false, //控制明细modal显示
      purchaseResion: purchaseResion,
      purchaseType: purchaseType,
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
      self.formValidate.pkId = pkId;
      let para = {
        pkId: pkId
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/tool-purchase-apply-get-by-id.json",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            self.formValidate = objCopy(data.data, {});
            self.formValidate.applyTime = new Date(self.formValidate.applyTime);
            self.applierName = data.data.applierName;
            self.departmentName = data.data.departmentName;
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
        self.searchAuditStaff(self.$store.state.user.userData.data.userId);
      }
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
          toolNumber: self.formValidate.toolNumber,
          applyStatus: -4,
          auditRemark: self.auditRemark,
          auitDepartmentId: self.auitDepartmentId,
          auitDepartmentName: self.auitDepartmentName,
          toolNumber: self.formValidate.toolNumber
        };
        debugger;
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
    },
    handleReceive() {
      this.isDisabled = true;
      const self = this;
      let para = {
        pkId: self.formValidate.pkId,
        toolNumber: self.formValidate.toolNumber,
        applyStatus: 5
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
    }
  }
};
</script>
