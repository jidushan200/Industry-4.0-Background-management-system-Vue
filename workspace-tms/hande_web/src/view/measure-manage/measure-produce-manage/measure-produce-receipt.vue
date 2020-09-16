<template>
  <Modal v-model="measureProduceReceipt" title="采购收货" width="1100" draggable>
    <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="100" inline>
      <Row>
        <Col span="6">
        <FormItem label="物料编码" prop="measureNumber">
          <Input style="width:170px" v-model="formValidate.measureNumber" readonly></Input>
        </FormItem>
        </Col>
        <Col span="6">
        <FormItem label="物料图号" prop="model">
          <Input style="width:170px" v-model="formValidate.model" readonly />
        </FormItem>
        </Col>
        <Col span="6">
        <FormItem label="物料名称" prop="measureName">
          <Input style="width:170px" v-model="formValidate.measureName" readonly></Input>
        </FormItem>
        </Col>
        <Col span="6">
        <FormItem label="申请部门" prop="applyDepartmentName">
          <Input style="width:170px" v-model="formValidate.applyDepartmentName" readonly />
        </FormItem>
        </Col>
      </Row>
      <Row>
        <Col span="6">
        <FormItem label="申请人" prop="applierName">
          <Input style="width:170px" v-model="applierName" readonly />
        </FormItem>
        </Col>
        <Col span="6">
        <FormItem label="申请时间" prop="applyTime">
          <DatePicker type="datetime" style="width: 170px" v-model="formValidate.applyTime" readonly></DatePicker>
        </FormItem>
        </Col>
        <Col span="6">
        <FormItem label="TMS库存" prop="inventoryQty">
          <Input style="width:170px" v-model="formValidate.inventoryQty" readonly />
        </FormItem>
        </Col>
        <Col span="6">
        <FormItem label="ERP库存" prop="erpQty">
          <Input style="width:170px" v-model="formValidate.erpQty" readonly />
        </FormItem>
        </Col>
      </Row>
      <Row>
        <Col span="6">
        <FormItem label="申购数量" prop="applyQty">
          <Input style="width:170px" v-model="formValidate.applyQty" readonly></Input>
        </FormItem>
        </Col>
        <Col span="6">
        <FormItem label="已到货数量" prop="arrivaledQty">
          <Input style="width:170px" v-model="formValidate.arrivaledQty" readonly></Input>
        </FormItem>
        </Col>
        <!--
        <Col span="6">
          <FormItem label="本次到货数" prop="arrivalQty">
            <InputNumber
              :min="1"
              style="width:170px"
              v-model="formValidate.arrivalQty"
              placeholder="输入到货数量"
            ></InputNumber>
          </FormItem>
        </Col>
        -->
      </Row>
      <Row>
        <Col span="18">
        <FormItem label="可用序号" prop="availableNumber">
          <Input style="width:704px" v-model="availableNumber" readonly />
        </FormItem>
        </Col>
      </Row>
      <!--
        <Row>
          <Col span="24">
            <FormItem label="申购备注" prop="remark">
              <Input type="textarea" style="width:750px" v-model="formValidate.remark" readonly />
            </FormItem>
          </Col>
        </Row>
      -->
      <Divider>
        <Button @click="addRow">添加项</Button>
      </Divider>
      <Table border :columns="columns" :data="data" height="400"></Table>
    </Form>

    <div slot="footer">
      <Button @click="saveReceipt()" type="primary" :disabled="isDisabled">提交</Button>
      <Button @click="handleReset('formValidate')" style="margin-left: 8px">关闭</Button>
    </div>
  </Modal>
</template>

<script>
import axios from "@/libs/api.request";
import qs from "qs";
import { dateFormat, objCopy } from "@/libs/tools.js";
import receipt_form_config from "./measure-produce-receipt-form-config.js";
export default {
  name: "measureProduceReceipt",
  data() {
    return {
      isDisabled: false,
      measureProduceReceipt: false,
      applierName: "",
      availableNumber: "",
      seqArray: [],
      data: [],
      supplierList: [],
      columns: [
        {
          title: "顺序号",
          key: "sequenceNumber",
          minwidth: 200,
          render: (h, params) => {
            return h("Input", {
              props: {
                value: this.data[params.index].sequenceNumber
              },
              on: {
                input: val => {
                  this.data[params.index].sequenceNumber = val;
                },
                "on-blur": event => {
                  const val = this.data[params.index].sequenceNumber;
                  const item = this.seqArray.find(item => item == val);
                  if (item == undefined) {
                    this.$Notice.error({
                      title: "顺序号输入错误!"
                    });
                    this.data[params.index].sequenceNumber = "";
                  }
                }
              }
            });
          }
        },
        {
          title: "送货人",
          key: "deliverer",
          minwidth: 160,
          render: (h, params) => {
            return h("Input", {
              props: {
                value: this.data[params.index].deliverer
              },
              on: {
                input: val => {
                  this.data[params.index].deliverer = val;
                }
              }
            });
          }
        },
        {
          title: "送货时间",
          key: "deliveryTime",
          minWidth: 200,
          render: (h, params) => {
            return h("DatePicker", {
              props: {
                readonly: true,
                value: dateFormat(
                  new Date(this.data[params.index].deliveryTime),
                  "yyyy-MM-dd HH:mm:ss"
                ),
                type: "datetime"
              },
              on: {
                input: val => {
                  this.data[params.index].deliveryTime = val;
                }
              }
            });
          }
        },
        {
          title: "操作",
          key: "action",
          width: 115,
          render: (h, params) => {
            /**
             * render写法 添加按钮
             */
            return h("div", [
              h(
                "Button",
                {
                  props: {
                    type: "error",
                    size: "small",
                    disabled: params.row.checkStatus != 0
                  },
                  style: {
                    marginRight: "5px"
                  },
                  on: {
                    click: () => {
                      this.delRow(params.index);
                    }
                  }
                },
                "删除"
              )
            ]);
          }
        }
      ],
      formValidate: receipt_form_config.formValidate, //form表单字段
      ruleValidate: receipt_form_config.ruleValidate //form表单验证规则
    };
  },
  methods: {
    show(row) {
      const self = this;
      self.data = [];
      self.measureProduceReceipt = true;
      self.formValidate = objCopy(row, {});
      self.pkId = row.pkId;
      self.applierName = row.applierName;
      self.arrivaledQty = row.arrivaledQty;
      self.formValidate.arrivalQty = 0;
      self.availableNumber = row.availableNumber;
      let array = self.availableNumber.split("  ");
      for (let item of array) {
        if (!!item) {
          self.seqArray.push(item.split("-")[1]);
        }
      }
      self.formValidate.applyTime = dateFormat(
        new Date(row.applyTime),
        "yyyy-MM-dd HH:mm:ss"
      );
      if (self.arrivaledQty == null) {
        self.arrivaledQty = 0;
      }

      if (self.arrivaledQty > 0) {
        let para = {
          pkId: row.pkId
        };
        axios
          .request({
            headers: {
              "Content-Type": "application/json; charset=UTF-8"
            },
            url: "/measure/get-purchase-apply-receipt-list.json",
            method: "post",
            isAuth: true,
            params: para
          })
          .then(function(res) {
            if (res.data.code === 200) {
              self.data = res.data.data;
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
      self.addRow();
    },
    /** 删除行 */
    delRow(idx) {
      this.data.splice(idx, 1);
    },
    addRow() {
      const self = this;
      this.data.push({
        sequenceNumber: "",
        supplierId: null,
        deliverer: "",
        deliveryTime: dateFormat(new Date(), "yyyy-MM-dd HH:mm:ss"),
        checkStatus: 0
      });
      //self.formValidate.arrivalQty = self.formValidate.arrivalQty + 1;
    },
    saveReceipt() {
      const self = this;
      if (!!!self.data || self.data.length < 1) {
        self.$Message.error("收货数量不能空!");
        return;
      }
      let i = 1;
      for (let item of self.data) {
        if (!!!item.sequenceNumber) {
          self.$Message.error("第" + i + "行顺序号不能空!");
          return;
        }
        i++;
      }
       this.isDisabled = true;
      let para = {
        pkId: self.pkId,
        measureNumber: self.formValidate.measureNumber,
        receiptList: JSON.stringify(self.data)
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/x-www-form-urlencoded"
          },
          url: "/measure/purchase-apply-receipt.json",
          method: "post",
          isAuth: true,
          data: qs.stringify(para)
        })
        .then(function(res) {
          self.measureProduceReceipt = false;
          if (res.data.code === 200) {
            self.$Message.success("采购到货已确认!");
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

    handleReset(name) {
      this.measureProduceReceipt = false;
      this.$refs[name].resetFields();
    }
  }
};
</script>

<style>
</style>
