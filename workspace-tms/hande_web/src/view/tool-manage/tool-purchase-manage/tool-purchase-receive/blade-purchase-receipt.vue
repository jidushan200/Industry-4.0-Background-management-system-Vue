<template>
  <Modal v-model="toolPurchaseReceipt" title="采购收货" width="1100" draggable>
    <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="100" inline>
      <Row>
        <Col span="6">
          <FormItem label="物料编码" prop="toolNumber">
            <Input style="width:170px" v-model="toolNumber" readonly></Input>
          </FormItem>
        </Col>
        <Col span="6">
          <FormItem label="物料图号" prop="toolMap">
            <Input style="width:170px" v-model="toolMap" readonly />
          </FormItem>
        </Col>
        <Col span="6">
          <FormItem label="物料名称" prop="toolName">
            <Input style="width:170px" v-model="toolName" readonly></Input>
          </FormItem>
        </Col>
        <Col span="6">
          <FormItem label="申请部门" prop="departmentName">
            <Input style="width:170px" v-model="departmentName" readonly />
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
          <FormItem label="需求时间" prop="applyTime">
            <DatePicker type="date" style="width: 170px" v-model="applyTime" readonly></DatePicker>
          </FormItem>
        </Col>
        <Col span="6">
          <FormItem label="申请时间" prop="needTime">
            <DatePicker type="datetime" style="width: 170px" v-model="needTime" readonly></DatePicker>
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
          <FormItem label="申购数量" prop="needQty">
            <Input style="width:170px" v-model="needQty" readonly></Input>
          </FormItem>
        </Col>
        <Col span="6">
          <FormItem label="已到货数量" prop="arrivaledQty">
            <Input style="width:170px" v-model="arrivaledQty" readonly></Input>
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
import receipt_form_config from "./tool-purchase-receipt-form-config.js";
export default {
  name: "toolPurchaseReceipt",
  data() {
    return {
      isDisabled: false,
      toolPurchaseReceipt: false,
      toolNumber: "",
      toolName: "",
      toolMap: "",
      keepQty: null,
      erpQty: null,
      departmentName: "",
      applierName: "",
      needTime: null,
      applyTime: null,
      needQty: null,
      arrivaledQty: null,
      data: [],
      supplierList: [],
      columns: [
        {
          title: "供应商",
          key: "supplierId",
          width: 280,
          render: (h, params) => {
            return h(
              "Select",
              {
                props: {
                  clearable: true,
                  filterable: true,
                  value: this.data[params.index].supplierId,
                  disabled: params.row.checkStatus != 0
                },
                on: {
                  "on-change": event => {
                    this.data[params.index].supplierId = event;
                  }
                }
              },
              this.supplierList.map(item => {
                return h("Option", {
                  props: {
                    value: item.pkId,
                    label: item.supplierName
                  }
                });
              })
            );
          }
        },
        {
          title: "到货数量",
          key: "toolQty",
          width: 200,
          render: (h, params) => {
            return h("Input", {
              props: {
                value: this.data[params.index].toolQty,
                readonly: params.row.checkStatus != 0
              },
              on: {
                input: val => {
                  this.data[params.index].toolQty = val;
                }
              }
            });
          }
        },
        {
          title: "送货人",
          key: "deliverer",
          width: 160,
          render: (h, params) => {
            return h("Input", {
              props: {
                value: this.data[params.index].deliverer,
                readonly: params.row.checkStatus != 0
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
          width: 200,
          render: (h, params) => {
            return h("DatePicker", {
              props: {
                value: dateFormat(
                  new Date(this.data[params.index].deliveryTime),
                  "yyyy-MM-dd"
                ),
                type: "date",
                readonly: params.row.checkStatus != 0
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
          title: "质检状态",
          key: "checkStatus",
          width: 120,
          render: function(h, params) {
            let statusStr = "";
            if (params.row.checkStatus == 0) {
              statusStr = "待质检";
            } else if (params.row.checkStatus == 1) {
              statusStr = "质检中";
            } else if (params.row.checkStatus == 2) {
              statusStr = "质检完成";
            }
            return h("div", statusStr);
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
      (self.data = []), (self.toolPurchaseReceipt = true);
      self.formValidate = objCopy(row, {});
      self.pkId = row.pkId;
      self.toolNumber = row.toolNumber;
      self.toolName = row.toolName;
      self.toolMap = row.toolMap;
      self.keepQty = row.keepQty;
      self.erpQty = row.erpQty;
      self.departmentName = row.departmentName;
      self.applierName = row.applierName;
      self.needQty = row.needQty;
      self.arrivaledQty = row.arrivaledQty;
      self.formValidate.arrivalQty = 0;
      self.needTime = dateFormat(new Date(row.needTime), "yyyy-MM-dd HH:mm:ss");
      self.applyTime = new Date(row.applyTime);
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
            url: "/tool/get-purchase-apply-receipt-list.json",
            method: "post",
            isAuth: true,
            params: para
          })
          .then(function(res) {
            if (res.data.code === 200) {
              self.data = res.data.data;
              for (let item of self.data) {
                item.deliveryTime = dateFormat(new Date(item.deliveryTime), "yyyy-MM-dd HH:mm:ss");
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
      }
      self.addRow();
      self.getSupplierListData();
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
        toolQty: null,
        deliveryTime: dateFormat(new Date(), "yyyy-MM-dd HH:mm:ss"),
        checkStatus: 0
      });
      //self.formValidate.arrivalQty = self.formValidate.arrivalQty + 1;
    },
    getSupplierListData() {
      const self = this;
      let para = {
        isNewTool: 1
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/base/supplier-list",
          isAuth: true,
          method: "post",
          params: para
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            self.supplierList = data.data;
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },

    saveReceipt() {
      const self = this;
      if (!!!self.data || self.data.length < 1) {
        self.$Message.error("收货数量不能空!");
        return;
      }
      let i = 1;
      let qtyTotal = 0;
      for (let item of self.data) {
        if (!!!item.supplierId) {
          self.$Message.error("第" + i + "行供应商不能空!");
          return;
        }
        if (!!!item.toolQty) {
          self.$Message.error("第" + i + "行到货数量不能空!");
          return;
        }
        qtyTotal = qtyTotal + parseInt(item.toolQty);
        i++;
      }
      if (qtyTotal > self.needQty) {
        self.$Message.error("到货数量不能大于采购数量!");
        return;
      }
      this.isDisabled = true;
      let para = {
        pkId: self.pkId,
        toolNumber: self.toolNumber,
        receiptList: JSON.stringify(self.data)
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/x-www-form-urlencoded"
          },
          url: "/tool/purchase-apply-receipt.json",
          method: "post",
          isAuth: true,
          data: qs.stringify(para)
        })
        .then(function(res) {
          self.toolPurchaseReceipt = false;
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
      this.toolPurchaseReceipt = false;
      this.$refs[name].resetFields();
    }
  }
};
</script>

<style>
</style>
