<template>
  <div>
    <Card>
      <Row>
        <Form ref="formInline" :model="searchCondition" :label-width="65" inline>
          <Col span="16">
            <FormItem prop="fullNumber" label="模具条码">
              <Input type="text" v-model="searchCondition.fullNumber" placeholder="请输入模具条码"></Input>
            </FormItem>
            <FormItem prop="mouldNumber" label="模具编码">
              <Input type="text" v-model="searchCondition.mouldNumber" placeholder="请输入模具编码"></Input>
            </FormItem>
            <FormItem prop="mouldName" label="模具名称">
              <Input type="text" v-model="searchCondition.mouldName" placeholder="请输入模具名称"></Input>
            </FormItem>
            <FormItem prop="dateInterval" label="时间区间">
              <DatePicker
                type="daterange"
                split-panels
                placeholder="请选择时间区间"
                placement="bottom-end"
                v-model="searchCondition.dateInterval"
                style="width:180px;"
              ></DatePicker>
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
import { dateFormat, objCopy, hasOneOf } from "@/libs/tools.js";
import { getToken, getBaseUrl } from "@/libs/util";
import { CLIENT_RENEG_LIMIT } from "tls";

export default {
  data() {
    return {
      searchCondition: {
        dateInterval: [],
        fullNumber: "",
        mouldNumber: "",
        mouldName:"",
        page: 1,
        rows: 10
      },
      tableData: {
        total: 0, // 总页数
        rows: [] // 每页条数
      },
      tableColumns: [
        {
          title: "模具编码",
          key: "mouldNumber",
          minWidth: 150,
          fixed: "left"
        },
        {
          title: "模具名称",
          key: "mouldName",
          minWidth: 220,
          fixed: "left"
        },
        {
          title: "模具图号",
          key: "mouldMap",
          minWidth: 120
        },
        {
          title: "模具条码",
          key: "mouldBarcode",
          minWidth: 240
        },
        {
          title: "库位",
          key: "warehouse",
          minWidth: 100
        },
        {
          title: "库管员",
          key: "keeper",
          minWidth: 100
        },
        {
          title: "模具数量",
          key: "mouldAmount",
          minWidth: 100
        },
        {
          title: "模具状态",
          key: "mouldStatus",
          minWidth: 120,
          render: function(h, params) {
            let statusStr = "";
            switch (params.row.mouldStatus) {
              case 0:
                statusStr = "待入库";
                break;
              case 1:
                statusStr = "在库";
                break;
              case 2:
                statusStr = "使用";
                break;
              case 3:
                statusStr = "待修磨";
                break;
              case 4:
                statusStr = "待检";
                break;
              case 5:
                statusStr = "待返库";
                break;
              case 6:
                statusStr = "不合格待处理";
                break;
              case 7:
                statusStr = "报废";
                break;
            }
            return h("div", statusStr);
          }
        },
        {
          title: "已加工次数",
          key: "processTimes",
          minWidth: 120
        },
        {
          title: "已加工数量",
          key: "processAmount",
          minWidth: 120
        },
        {
          title: "最大生产寿命",
          key: "lifeMax",
          minWidth: 120
        },
        {
          title: "热处理批次号",
          key: "heatNumber",
          minWidth: 120
        },
        {
          title: "表面处理批次号",
          key: "surfaceNumber",
          minWidth: 120
        },
        {
          title: "已修磨次数",
          key: "repairTimes",
          minWidth: 120
        },
        {
          title: "质检记录",
          align: "center",
          children: [
            {
              title: "质检时间",
              key: "checkList",
              align: "center",
              minWidth: 150,
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
                      undefined != this.tableData.rows[params.index].checkList
                        ? this.tableData.rows[params.index].checkList.map(
                            item => {
                              if (item.checkTime != null) {
                                return h(
                                  "li",
                                  {},
                                  dateFormat(
                                    new Date(item.checkTime),
                                    "yyyy-MM-dd HH:mm"
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
              title: "质检人",
              key: "checkList",
              align: "center",
              minWidth: 100,
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
                      undefined != this.tableData.rows[params.index].checkList
                        ? this.tableData.rows[params.index].checkList.map(
                            item => {
                              return h("li", {}, item.checker);
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
          title: "入库记录",
          align: "center",
          children: [
            {
              title: "入库时间",
              key: "warehouseList",
              align: "center",
              minWidth: 150,
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
                      undefined !=
                        this.tableData.rows[params.index].warehouseList
                        ? this.tableData.rows[params.index].warehouseList.map(
                            item => {
                              if (item.warehosingTime != null) {
                                return h(
                                  "li",
                                  {},
                                  dateFormat(
                                    new Date(item.warehosingTime),
                                    "yyyy-MM-dd HH:mm"
                                  )
                                );
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
              title: "库管员",
              key: "warehouseList",
              align: "center",
              minWidth: 100,
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
                      undefined !=
                        this.tableData.rows[params.index].warehouseList
                        ? this.tableData.rows[params.index].warehouseList.map(
                            item => {
                              return h("li", {}, item.keeper);
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
          title: "出库记录",
          align: "center",
          key: "outboundList",
          children: [
            {
              title: "出库时间",
              key: "outboundList",
              align: "center",
              minWidth: 150,
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
                      undefined !=
                        this.tableData.rows[params.index].outboundList
                        ? this.tableData.rows[params.index].outboundList.map(
                            item => {
                              if (item.receiveTime != null) {
                                return h(
                                  "li",
                                  {},
                                  dateFormat(
                                    new Date(item.receiveTime),
                                    "yyyy-MM-dd HH:mm"
                                  )
                                );
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
              title: "领用人",
              key: "outboundList",
              align: "center",
              minWidth: 160,
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
                      undefined !=
                        this.tableData.rows[params.index].outboundList
                        ? this.tableData.rows[params.index].outboundList.map(
                            item => {
                              return h(
                                "li",
                                {},
                                item.useDepartmentName +
                                  "-" +
                                  item.useTeamName +
                                  "-" +
                                  item.userName
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
              title: "库管员",
              key: "outboundList",
              align: "center",
              minWidth: 100,
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
                      undefined !=
                        this.tableData.rows[params.index].outboundList
                        ? this.tableData.rows[params.index].outboundList.map(
                            item => {
                              return h("li", {}, item.keeper);
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
          title: "制件记录",
          align: "center",
          key: "processList",
          children: [
            {
              title: "交回时间",
              key: "processList",
              align: "center",
              minWidth: 120,
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
                                    "yyyy-MM-dd HH:mm"
                                  )
                                );
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
              title: "制件编号",
              key: "processList",
              align: "center",
              minWidth: 130,
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
                              return h("li", {}, item.partCode);
                            }
                          )
                        : ""
                    )
                  ]
                );
              }
            },
            {
              title: "制件名称",
              key: "processList",
              align: "center",
              minWidth: 320,
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
                              return h("li", {}, item.partName);
                            }
                          )
                        : ""
                    )
                  ]
                );
              }
            },
            {
              title: "制件数量",
              key: "processList",
              align: "center",
              minWidth: 100,
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
        mouldBarcode: self.searchCondition.fullNumber,
        mouldNumber: self.searchCondition.mouldNumber,
        mouldName: self.searchCondition.mouldName,
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
          url: "/mould/mould-life-page-list",
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
      let mouldNumber = self.searchCondition.mouldNumber;
      let beginDate = self.searchCondition.beginDate;
      let endDate = self.searchCondition.endDate;
      let para = "";
      if (!!fullNumber) {
        para = "&mouldBarcode=" + fullNumber;
      }
      if (!!mouldNumber) {
        para = "&mouldNumber=" + mouldNumber;
      }
      if (!!beginDate) {
        para = para + "&beginDate=" + beginDate;
      }
      if (!!endDate) {
        para = para + "&endDate=" + endDate;
      }
      window.location.href =
        getBaseUrl() + "/mould/mould-life-export?token=" + getToken() + para;
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
