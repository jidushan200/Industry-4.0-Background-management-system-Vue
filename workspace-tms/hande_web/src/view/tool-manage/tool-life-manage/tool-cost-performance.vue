<template>
  <div>
    <Card>
      <Row>
        <Form ref="formInline" :model="searchCondition" :label-width="65" inline>
          <Col span="16">
            <FormItem prop="toolNumber" label="物料编码">
              <Input type="text" v-model="searchCondition.toolNumber" placeholder="请输入物料编码"></Input>
            </FormItem>
            <FormItem prop="fullNumber" label="物料条码">
              <Input type="text" v-model="searchCondition.fullNumber" placeholder="请输入物料条码"></Input>
            </FormItem>
            <FormItem>
              <Button type="primary" style="margin-left:-60px" @click="doSearch('formInline')">搜索</Button>
            </FormItem>
            <FormItem style="margin-left:-65px">
              <Button type="dashed" @click="doReset('formInline')">重置</Button>
            </FormItem>
          </Col>
          <Col span="8">
            <FormItem style="width:100%;text-align:right">
              <Button type="warning" @click="exportData()">
                <Icon type="ios-download-outline"></Icon>导出
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
  </div>
</template>

<script>
import axios from "@/libs/api.request";
import knife_lists from "../tool-list-manage/knife-lists-form-config.js";
import { dateFormat, objCopy } from "@/libs/tools.js";
import { getToken, getBaseUrl } from "@/libs/util";
import { CLIENT_RENEG_LIMIT } from "tls";

export default {
  data() {
    return {
      searchCondition: {
        dateInterval: [],
        fullNumber: "",
        toolNumber: "",
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
          fixed: "left",
          key: "fullNumber",
          width: 240
        },
        {
          title: "物料名称",
          fixed: "left",
          key: "toolName",
          width: 220
        },
        {
          title: "物料编码",
          fixed: "left",
          key: "toolNumber",
          width: 120
        },
        {
          title: "物料图号",
          key: "toolMap",
          width: 120
        },

        {
          title: "物料类型",
          key: "toolType",
          width: 120,
          render: function(h, params) {
            let statusStr = "";
            switch (params.row.toolType) {
              case 2:
                statusStr = "拉刀";
                break;
              case 3:
                statusStr = "剃齿刀";
                break;
              case 4:
                statusStr = "刀头";
                break;
              case 5:
                statusStr = "刀条";
                break;
              default:
                statusStr = "滚刀";
                break;
            }
            return h("div", statusStr);
          }
        },
        {
          title: "物料状态",
          key: "toolState",
          width: 120,
          render: function(h, params) {
            let statusStr = "";
            switch (params.row.toolState) {
              case 2:
                statusStr = "使用中";
                break;
              case 3:
                statusStr = "待刃磨";
                break;
              case 4:
                statusStr = "刃磨完成";
                break;
              case 5:
                statusStr = "待涂层";
                break;
              case 6:
                statusStr = "涂层完成";
                break;
              case 7:
                statusStr = "待质检";
                break;
              case 8:
                statusStr = "已完成计数";
                break;
              case 9:
                statusStr = "质检完成";
                break;
              default:
                statusStr = "闲置";
                break;
            }
            return h("div", statusStr);
          }
        },

        {
          title: "入库编码",
          key: "warehouseCode",
          width: 180
        },
        {
          title: "单价（元）",
          key: "price",
          sortable: true,
          width: 120
        },
        {
          title: "已加工数量",
          key: "processAmount",
          width: 120
        },
        {
          title: "加工记录",
          align: "center",
          children: [
            {
              title: "加工开始时间",
              key: "processList",
              align: "center",
              width: 120,
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
                      undefined != this.tableData.rows[params.index].processList
                        ? this.tableData.rows[params.index].processList.map(
                            item => {
                              if (item.endTime != null) {
                                return h(
                                  "li",
                                  {},
                                  dateFormat(
                                    new Date(item.beginTime),
                                    "yyyy-MM-dd"
                                  )
                                );
                              } else {
                                return h("li", {}, "暂无日期数据");
                              }
                            }
                          )
                        : ""
                    )
                  ]
                );
              }
            },
            {
              title: "加工结束时间",
              key: "processList",
              align: "center",
              width: 120,
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
                      undefined != this.tableData.rows[params.index].processList
                        ? this.tableData.rows[params.index].processList.map(
                            item => {
                              if (item.endTime != null) {
                                return h(
                                  "li",
                                  {},
                                  dateFormat(
                                    new Date(item.endTime),
                                    "yyyy-MM-dd"
                                  )
                                );
                              } else {
                                return h("li", {}, "暂无日期数据");
                              }
                            }
                          )
                        : ""
                    )
                  ]
                );
              }
            },
            {
              title: "加工数量",
              key: "processList",
              align: "center",
              width: 120,
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
                      undefined != this.tableData.rows[params.index].processList
                        ? this.tableData.rows[params.index].processList.map(
                            item => {
                              return h("li", {}, item.processAmount);
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
          title: "刃磨记录",
          align: "center",
          children: [
            {
              title: "刃磨时间",
              key: "repairList",
              align: "center",
              width: 120,
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
                      undefined != this.tableData.rows[params.index].repairList
                        ? this.tableData.rows[params.index].repairList.map(
                            item => {
                              return h(
                                "li",
                                {},
                                dateFormat(
                                  new Date(item.executTime),
                                  "yyyy-MM-dd"
                                )
                              );
                            }
                          )
                        : ""
                    )
                  ]
                );
              }
            },
            {
              title: "刃磨量",
              key: "repairList",
              align: "center",
              width: 120,
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
                      undefined != this.tableData.rows[params.index].repairList
                        ? this.tableData.rows[params.index].repairList.map(
                            item => {
                              return h("li", {}, item.repairMeasure);
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
          title: "涂层记录",
          align: "center",
          key: "processAmount",
          children: [
            {
              title: "涂层时间",
              key: "coatList",
              align: "center",
              width: 120,
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
                      undefined != this.tableData.rows[params.index].coatList
                        ? this.tableData.rows[params.index].coatList.map(
                            item => {
                              return h(
                                "li",
                                {},
                                dateFormat(
                                  new Date(item.coatTime),
                                  "yyyy-MM-dd"
                                )
                              );
                            }
                          )
                        : ""
                    )
                  ]
                );
              }
            },
            {
              title: "涂层厂家",
              key: "coatList",
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
                      undefined != this.tableData.rows[params.index].coatList
                        ? this.tableData.rows[params.index].coatList.map(
                            item => {
                              return h("li", {}, item.coatSupplier);
                            }
                          )
                        : ""
                    )
                  ]
                );
              }
            }
          ]
        }
      ],
      formValidate: knife_lists.formValidate, //user form表单字段
      ruleValidate: knife_lists.ruleValidate //user form表单验证规则
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
        fullNumber: self.searchCondition.fullNumber,
        toolNumber: self.searchCondition.toolNumber,
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
          url: "/tool/tool-life-page-list",
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
    doSearch(name) {
      const self = this;
      self.changePage(1);
    },
    doReset(name) {
      const self = this;
      self.$refs[name].resetFields();
      self.getListData();
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
    exportData() {
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

      let fullNumber = self.searchCondition.fullNumber;
      let toolNumber = self.searchCondition.toolNumber;
      let beginDate = self.searchCondition.beginDate;
      let endDate = self.searchCondition.endDate;
      let para = "";
      if (!!fullNumber) {
        para = "&fullNumber=" + fullNumber;
      }
      if (!!toolNumber) {
        para = para + "&toolNumber=" + toolNumber;
      }
      if (!!beginDate) {
        para = para + "&beginDate=" + beginDate;
      }
      if (!!endDate) {
        para = para + "&endDate=" + endDate;
      }
      window.location.href =
        getBaseUrl() + "/tool/tool-export?token=" + getToken()+para;
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
