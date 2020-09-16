<template>
  <!--申请单信息弹出modal-->
  <Modal v-model="purchaseApplyModalshow" title="采购申请单" width="1100" draggable>
    <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="100">
      <Row>
        <Col span="6">
          <FormItem label="物料编码" prop="measureNumber">
            <Input style="width:170px" v-model="formValidate.measureNumber" readonly/>
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
              <Select
                style="width:170px"
                v-model="formValidate.purchaseReasion"
                ref="purchaseResion"
                placeholder="请选择采购原因"
                disabled
              >
                <Option
                  v-for="item in purchaseResionType"
                  :value="item.value"
                  :key="item.value"
                >{{ item.name }}</Option>
              </Select>
            </FormItem>
          </Col>
        <Col span="6">
          <FormItem label="申请部门" prop="applyDepartment">
            <Input v-model="departmentName"  style="width:170px" readonly />
          </FormItem>
        </Col>
        <Col span="6">
          <FormItem label="申请人" prop="applicant">
            <Input v-model="applierName"  style="width:170px" readonly />
          </FormItem>
        </Col>
      </Row>
      <Row>
        <Col span="6">
          <FormItem label="需求时间" prop="applyTime">
            <DatePicker
              type="date"
              style="width: 170px"
              v-model="formValidate.applyTime"
              placeholder
              readonly
            ></DatePicker>
          </FormItem>
        </Col>
        <Col span="6">
          <FormItem label="申购数量" prop="applyQty">
            <Input style="width:170px" v-model="formValidate.applyQty" placeholder="输入需求数量" readonly/>
          </FormItem>
        </Col>
        <Col span="6">
            <FormItem label="申购时间" prop="applyTime">
              <DatePicker
                type="datetime"
                style="width: 170px"
                v-model="formValidate.applyTime"
                readonly
              ></DatePicker>
            </FormItem>
          </Col>
        <Col span="6">
          <FormItem label="已到货数量" prop="arrivaledQty">
            <Input style="width:170px" v-model="formValidate.arrivaledQty" readonly> </Input>
          </FormItem>
        </Col>
      </Row>
      <Row>
        <Col span="12">
          <FormItem label="申购备注" prop="remark">
            <Input
              type="textarea"
               :rows="1"
              style="width:437px"
              v-model="formValidate.remark"
              readonly
            />
          </FormItem>
        </Col>
        <Col span="12">
          <FormItem label="可用序号" prop="availableNumber">
            <Input style="width:437px" v-model="formValidate.availableNumber" readonly />
          </FormItem>
        </Col>
      </Row>
    </Form>
    <Table :columns="tableColumns" :data="logList" height="300" border stripe highlight-row></Table>
    <div slot="footer">
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
            self.formValidate.applyTime = new Date(self.formValidate.applyTime);
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
      self.purchaseApplyModalshow = true;
    },
    handleReset() {
      const self = this;
      self.purchaseApplyModalshow = false;
    }
  }
};
</script>
