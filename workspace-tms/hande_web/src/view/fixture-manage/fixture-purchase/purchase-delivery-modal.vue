
<template>
  <!--申请单信息弹出modal-->
  <Modal
    v-model="purchaseDeliveryModalshow"
    class="modal-class"
    title="采购收货"
    width="1160"
    draggable
  >
    <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="95" inline>
      <Row>
        <Col span="6">
          <FormItem label="夹具编码" prop="fixtureName">
            <Input style="width:180px" v-model="fixtureNumber" readonly></Input>
          </FormItem>
        </Col>
        <Col span="6">
          <FormItem label="夹具名称" prop="fixtureName">
            <Input style="width:180px" v-model="fixtureName" readonly></Input>
          </FormItem>
        </Col>
        <Col span="6">
          <FormItem label="夹具图号" prop="fixtureMap">
            <Input style="width:180px" v-model="fixtureMap" readonly></Input>
          </FormItem>
        </Col>
        <Col span="6">
          <FormItem label="TMS库存" prop="inventoryQty">
            <Input style="width:180px" v-model="inventoryQty" readonly />
          </FormItem>
        </Col>
      </Row>
      <Row>
        <Col span="6">
          <FormItem label="ERP库存" prop="erpQty">
            <Input style="width:180px" v-model="erpQty" readonly />
          </FormItem>
        </Col>
        <Col span="6">
          <FormItem label="ERP待检数量" prop="noCheckQty">
            <Input style="width:180px" v-model="noCheckQty" readonly />
          </FormItem>
        </Col>
        <Col span="6">
          <FormItem label="申购类型" prop="purchaseType">
            <Select
              style="width:180px"
              v-model="purchaseType"
              ref="purchaseType"
              placeholder="请选择采购类型"
            >
              <Option
                v-for="item in purchaseTypeList"
                :value="item.value"
                :key="item.value"
              >{{ item.name }}</Option>
            </Select>
          </FormItem>
        </Col>
        <Col span="6">
          <FormItem label="申请部门" prop="departmentName">
            <Input style="width:180px" v-model="departmentName" readonly />
          </FormItem>
        </Col>
      </Row>
      <Row>
        <Col span="6">
          <FormItem label="申请人" prop="applierName">
            <Input style="width:180px" v-model="applierName" readonly />
          </FormItem>
        </Col>
        <Col span="6">
          <FormItem label="申购原因" prop="purchaseResion">
            <Select
              style="width:180px"
              v-model="purchaseResion"
              ref="purchaseResion"
              placeholder="请选择采购原因"
            >
              <Option
                v-for="item in purchaseResionList"
                :value="item.value"
                :key="item.value"
              >{{ item.name }}</Option>
            </Select>
          </FormItem>
        </Col>
        <Col span="6">
          <FormItem label="需求时间" prop="demandTime">
            <DatePicker
              type="date"
              style="width: 180px"
              :options="dateOption"
              v-model="demandTime"
              readonly
            ></DatePicker>
          </FormItem>
        </Col>
        <Col span="6">
          <FormItem label="申购数量" prop="purchaseQty">
            <InputNumber style="width:180px" v-model="purchaseQty" :min="1" :max="20" readonly></InputNumber>
            <Icon type="md-arrow-round-forward" slot="suffix" />
          </FormItem>
        </Col>
      </Row>
      <Row>
        <Col span="6">
          <FormItem label="申请时间" prop="applyTime">
            <DatePicker type="datetime" style="width: 180px" v-model="applyTime" readonly></DatePicker>
          </FormItem>
        </Col>
        <Col span="6" class="ivu-form" v-if="amount">
          <FormItem label="金额合计" prop="amount">
            <Input style="width:180px" v-model="amount" readonly />
          </FormItem>
        </Col>
        <Col span="12">
          <FormItem label="申购备注" prop="remark">
            <Input v-model="remark" style="width:460px;" readonly />
          </FormItem>
        </Col>
        <Col span="12">
          <FormItem label="可用顺序号" prop="availableNumber">
            <Input v-model="availableNumber" style="width:460px;" readonly />
          </FormItem>
        </Col>
        <Col span="6">
          <FormItem label="已到货数量" prop="arrivedQty">
            <Input v-model="arrivedQty" readonly />
          </FormItem>
        </Col>
      </Row>

      <Table
        :columns="detailColumns"
        height="160"
        :data="detailList"
        border
        stripe
        highlight-row
        v-if="detailList.length>0"
      ></Table>
      <Divider>
        <Button @click="addRow">添加项</Button>
      </Divider>
      <Table border :columns="columns" :data="data" height="160"></Table>
    </Form>
    <div slot="footer">
      <Button @click="saveReceipt()" type="primary" :disabled="isDisable">提交</Button>
      <Button @click="handleReset()" style="margin-left: 8px">关闭</Button>
    </div>
  </Modal>
</template>
<script>
import { dateFormat, objCopy } from "@/libs/tools.js";
import { getToken, getBaseUrl } from "@/libs/util";
import axios from "@/libs/api.request";
import qs from "qs";
import purchase_delivery_config from "./purchase-delivery-config.js";
import purchaseTypeList from "../purchase-type.js";
import purchaseResionList from "../purchase-resion.js";
export default {
  data() {
    return {
      isDisable: false,
      fixtureNumber: "",
      fixtureMap: "",
      fixtureName: "",
      erpQty: 0,
      noCheckQty: 0,
      inventoryQty: 0,
      purchaseQty: 0,
      departmentName: "",
      applierName: "",
      demandTime: null,
      applyTime: null,
      purchaseDeliveryModalshow: false,
      purchaseTypeList: purchaseTypeList,
      purchaseResionList: purchaseResionList,
      purchaseType: null,
      purchaseResion: null,
      availableNumber: "",
      arrivedQty: 0,
      remark: "",
      dateOption: {
        disabledDate(date) {
          return date && date.valueOf() < Date.now() - 86400000;
        }
      },
      detailList: [],
      detailColumns: [
        {
          title: "配件编号",
          key: "fixtureNumber",
          minWidth: 120
        },
        {
          title: "配件名称",
          key: "fixtureName",
          minWidth: 270
        },
        {
          title: "配件图号",
          key: "fixtureMap",
          minWidth: 150
        },
        {
          title: "标准数量",
          key: "baseQty",
          width: 90
        },
        {
          title: "配件数量",
          key: "fixtureQty",
          width: 90
        },
        {
          title: "顺序号",
          key: "availableNumber",
          minWidth: 320
        }
      ],
      amount: null,
      data: [],
      seqArray: [],
      supplierList: [],
      columns: [
        {
          title: "顺序号",
          key: "fixtureBarcode",
          width: 180,
          render: (h, params) => {
            return h("Input", {
              props: {
                value: this.data[params.index].fixtureBarcode,
                readonly: params.row.checkStatus != 0
              },
              on: {
                input: val => {
                  this.data[params.index].fixtureBarcode = val;
                },
                "on-blur": event => {
                  const val = this.data[params.index].fixtureBarcode;
                  const item = this.seqArray.find(item => item == val);
                  if (item == undefined) {
                    this.$Notice.error({
                      title: "顺序号输入错误!"
                    });
                    this.data[params.index].fixtureBarcode = "";
                  } else {
                    let i = 0;
                    for (let it of this.data) {
                      if (
                        !!it.fixtureBarcode &&
                        item == it.fixtureBarcode &&
                        i != params.index
                      ) {
                        this.$Message.error("与第" + (i + 1) + "行顺序号重复!");
                        this.data[params.index].fixtureBarcode = "";
                        return;
                      }
                      i++;
                    }
                  }
                }
              }
            });
          }
        },
        {
          title: "供应商",
          key: "supplierId",
          width: 360,
          render: (h, params) => {
            return h(
              "Select",
              {
                props: {
                  clearable: true,
                  filterable: true,
                  value: this.data[params.index].supplierId
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
          title: "送货人",
          key: "deliverer",
          width: 180,
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
          width: 200,
          render: (h, params) => {
            return h("DatePicker", {
              props: {
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
          title: "质检状态",
          key: "checkStatus",
          width: 140,
          render: function(h, params) {
            let statusStr = "";
            if (params.row.checkStatus == 0) {
              statusStr = "待质检";
            } else {
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
                      this.delRow(params.index, params.row);
                    }
                  }
                },
                "删除"
              )
            ]);
          }
        }
      ],
      formValidate: purchase_delivery_config.formValidate, //form表单字段
      ruleValidate: purchase_delivery_config.ruleValidate //form表单验证规则
    };
  },
  created() {
    const self = this;
  },
  methods: {
    showModal(pkId) {
      const self = this;
      (self.data = []), self.getPurchaseApplyByPkId(pkId);
      self.purchaseDeliveryModalshow = true;
      self.getSupplierListData();
    },
    handleReset() {
      const self = this;
      self.purchaseDeliveryModalshow = false;
    },
    getPurchaseApplyByPkId(pkId) {
      const self = this;
      let para = {
        pkId: pkId
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/fixture/receipt-get-by-apply-id",
          isAuth: true,
          params: para,
          method: "post"
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200 && data.data !== null) {
            const detail = data.data;
            self.pkId = detail.pkId;
            self.demandTime = dateFormat(
              new Date(detail.demandTime),
              "yyyy-MM-dd"
            );
            self.applyTime = dateFormat(
              new Date(detail.applyTime),
              "yyyy-MM-dd HH:mm:ss"
            );
            self.fixtureNumber = detail.fixtureNumber;
            self.fixtureName = detail.fixtureName;
            self.fixtureMap = detail.fixtureMap;
            self.departmentName = detail.applyDepartmentName;
            self.applierName = detail.applierName;
            self.erpQty = detail.erpQty;
            self.inventoryQty = detail.inventoryQty;
            self.noCheckQty = detail.noCheckQty;
            if (!!detail.receiptList) {
              self.data = detail.receiptList;
              for (let item of self.data) {
                item.deliveryTime = dateFormat(new Date(item.deliveryTime), "yyyy-MM-dd HH:mm:ss");
              }
            }
            self.amount = detail.amount;
            self.purchaseType = detail.purchaseType;
            self.purchaseResion = detail.purchaseResion;
            self.availableNumber = detail.availableNumber;
            self.purchaseQty = detail.purchaseQty;
            self.arrivedQty = detail.arrivedQty;
            if (detail.fixtureType == 1) {
              self.detailList = detail.detailList;
            }
            self.seqArray = self.availableNumber.split(";");
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },
    delRow(idx, row) {
      const self = this;
      if (!!!row.pkId) {
        self.data.splice(idx, 1);
        return;
      }
      let para = {
        receiptId: row.pkId,
        fixtureBarcode: row.fixtureBarcode
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/fixture/purchase-receipt-delete",
          isAuth: true,
          params: para,
          method: "post"
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            self.data.splice(idx, 1);
            self.arrivedQty = self.arrivedQty - 1;
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },
    addRow() {
      const self = this;
      this.data.push({
        fixtureBarcode: "",
        supplierId: null,
        deliverer: "",
        deliveryTime: dateFormat(new Date(), "yyyy-MM-dd HH:mm"),
        checkStatus: 0
      });
    },
    getSupplierListData() {
      const self = this;
      let para = {
        isFixture: 1
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
      for (let item of self.data) {
        if (!!!item.fixtureBarcode) {
          self.$Message.error("第" + i + "行顺序号不能空!");
          return;
        }
        i++;
      }
      let para = {
        applyId: self.pkId,
        receiptList: JSON.stringify(self.data)
      };
      self.isDisable = true;
      axios
        .request({
          headers: {
            "Content-Type": "application/x-www-form-urlencoded"
          },
          url: "/fixture/purchase-receipt-save.json",
          method: "post",
          isAuth: true,
          data: qs.stringify(para)
        })
        .then(function(res) {
          self.purchaseDeliveryModalshow = false;
          if (res.data.code === 200) {
            self.$Message.success("采购到货已确认!");
            self.$emit("search");
          } else {
            self.$Notice.error({
              title: "错误提示",
              desc: res.data.info
            });
          }
          self.isDisable = false;
        })
        .catch(function(err) {
          console.log(err);
        });
    },

    downFile(pkId) {
      window.location.href =
        getBaseUrl() +
        "/tool/appendix-download?pkId=" +
        pkId +
        "&token=" +
        getToken();
    }
  }
};
</script>
