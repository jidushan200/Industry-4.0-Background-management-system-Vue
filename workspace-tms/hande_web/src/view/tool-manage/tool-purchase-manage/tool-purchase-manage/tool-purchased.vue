<template>
  <div>
    <Card>
      <Row>
        <Form ref="formInline" :model="searchCondition" inline :label-width="65">
          <Col span="24">
            <FormItem prop="toolNumber" label="物料编码">
              <Input type="text" v-model="searchCondition.toolNumber" placeholder="请输入物料编码"></Input>
            </FormItem>
            <FormItem prop="purchaseType" label="申购类型">
              <Select
                style="width:120px"
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
            <FormItem prop="dateInterval" label="时间区间">
              <DatePicker
                split-panels
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
    <purchase-apply-model ref="purchaseApplyModel"></purchase-apply-model>
  </div>
</template>

<script>
import axios from "@/libs/api.request";
import { dateFormat, objCopy } from "@/libs/tools.js";
import purchaseSelect from "./purchaseSelect-type.js";
import purchaseApplyModel from "./purchase-apply-model.vue";
export default {
  components: {
    purchaseApplyModel
  },
  data() {
    return {
      purchaseSelect: purchaseSelect,
      toReceiveModalShow: false, //控制明细modal显示
      isModify: false, //是否是修改明细操作
      searchCondition: {
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
          minWidth: 160
        },
        {
          title: "物料名称",
          key: "toolName",
          minWidth: 200
        },
        {
          title: "物料图号",
          key: "toolMap",
          minWidth: 120
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
          title: "申请时间",
          key: "needTime",
          minWidth: 180,
          render: function(h, params) {
            return h(
              "div",
              dateFormat(new Date(params.row.needTime), "yyyy-MM-dd HH:mm")
            );
          }
        },
        {
          title: "需求数量",
          key: "needQty",
          minWidth: 100
        },
        {
          title: "需求时间",
          key: "applyTime",
          minWidth: 120,
          render: function(h, params) {
            return h(
              "div",
              dateFormat(new Date(params.row.applyTime), "yyyy-MM-dd")
            );
          }
        },
        {
          title: "已到货数量",
          key: "arrivaledQty",
          minWidth: 100
        },
        {
          title: "完成时间",
          key: "finishTime",
          minWidth: 150,
          render: function(h, params) {
            return h(
              "div",
              dateFormat(new Date(params.row.finishTime), "yyyy-MM-dd HH:mm")
            );
          }
        },
        {
          title: "采购原因",
          key: "purchaseResion",
          minWidth: 100,
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
          title: "明细",
          align: "center",
          children: [
            {
              title: "顺序号",
              key: "receipList",
              align: "center",
              width: 90,
              render: (h, params) => {
                return h(
                  "div",
                  {
                    attrs: {
                      class: "subCol"
                    }
                  },
                  [
                    h(
                      "ul",
                      undefined != this.tableData.rows[params.index].receipList
                        ? this.tableData.rows[params.index].receipList.map(
                            item => {
                              return h("li", {}, item.sequenceNumber);
                            }
                          )
                        : ""
                    )
                  ]
                );
              }
            },
            {
              title: "供应商",
              key: "receipList",
              align: "center",
              width: 200,
              render: (h, params) => {
                return h(
                  "div",
                  {
                    attrs: {
                      class: "subCol"
                    }
                  },
                  [
                    h(
                      "ul",
                      undefined != this.tableData.rows[params.index].receipList
                        ? this.tableData.rows[params.index].receipList.map(
                            item => {
                              return h("li", {}, item.supplierName);
                            }
                          )
                        : ""
                    )
                  ]
                );
              }
            },
            {
              title: "收货人",
              key: "receipList",
              align: "center",
              width: 100,
              render: (h, params) => {
                return h(
                  "div",
                  {
                    attrs: {
                      class: "subCol"
                    }
                  },
                  [
                    h(
                      "ul",
                      undefined != this.tableData.rows[params.index].receipList
                        ? this.tableData.rows[params.index].receipList.map(
                            item => {
                              return h("li", {}, item.createUserName);
                            }
                          )
                        : ""
                    )
                  ]
                );
              }
            },
            {
              title: "收货时间",
              key: "receipList",
              align: "center",
              width: 150,
              render: (h, params) => {
                return h(
                  "div",
                  {
                    attrs: {
                      class: "subCol"
                    }
                  },
                  [
                    h(
                      "ul",
                      undefined != this.tableData.rows[params.index].receipList
                        ? this.tableData.rows[params.index].receipList.map(
                            item => {
                              return h(
                                "li",
                                {},
                                dateFormat(
                                  new Date(item.createTime),
                                  "yyyy-MM-dd HH:mm"
                                )
                              );
                            }
                          )
                        : ""
                    )
                  ]
                );
              }
            }
          ]
        },

        {
          title: "操作",
          key: "action",
          minWidth: 200,
          align: "center",
          fixed: "right",
          render: (h, params) => {
            return h("div", [
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
                      this.selectReport(params.row.pkId);
                    }
                  }
                },
                "查看采购单"
              ),
              h(
                "Button",
                {
                  props: {
                    type: "success",
                    size: "small"
                  },
                  style: {
                    marginRight: "5px"
                  },
                  on: {
                    click: () => {
                      this.doPrint(params.row.pkId);
                    }
                  }
                },
                "打印"
              )
            ]);
          }
        }
      ]
    };
  },
  created() {
    const self = this;
    self.getListData();
  },
  methods: {
    getListData() {
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
      let para = {
        toolNumber: self.searchCondition.toolNumber,
        purchaseType: self.searchCondition.purchaseType,
        page: self.searchCondition.page,
        rows: self.searchCondition.rows,
        beginDate: self.searchCondition.beginDate,
        endDate: self.searchCondition.endDate,
        applyStatus: 6
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/purchase-receipt-page-list.json",
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
    changePage(cuurentPage) {
      const self = this;
      self.$set(self.searchCondition, "page", cuurentPage);
      self.getListData();
    },
    selectReport(pkId) {
      const self = this;
      self.$refs.purchaseApplyModel.showModal(pkId, false);
    },
    handlePageSize(value) {
      const self = this;
      self.$set(self.searchCondition, "rows", value);
      self.getListData();
    },
    doSearch(name) {
      const self = this;
      self.changePage(1);
    },
    doReset(name) {
      const self = this;
      self.$refs[name].resetFields();
      self.getListData();
    },
    doPrint(pkId) {
      window.open(
        "http://10.36.10.11:8080/decision/view/report?viewlet=tool_purchase.cpt&pkId=" +
          pkId,
        "newwindow",
        "height=760,width=800,top=0,left=0,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no"
      );
    }
  }
};
</script>

<style>
.subCol > ul > li {
  margin: 0 -18px;
  list-style: none;
  text-align: center;
  padding: 9px;
  border-bottom: 1px solid #ccc;
  overflow-x: hidden;
}
.subCol > ul > li:last-child {
  border-bottom: none;
}
</style>