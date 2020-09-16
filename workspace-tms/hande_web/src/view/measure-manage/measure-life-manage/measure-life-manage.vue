<template>
  <div>
    <Card>
      <Row>
        <Form ref="formInline" :model="searchCondition" :label-width="85" inline>
          <Col span="16">
            <FormItem prop="fullNumber" label="量具条码">
              <Input type="text" v-model="searchCondition.fullNumber" placeholder="请输入量具条码"></Input>
            </FormItem>
            <FormItem prop="measureNumber" label="量具编码">
              <Input type="text" v-model="searchCondition.measureNumber" placeholder="请输入量具编码"></Input>
            </FormItem>
            <FormItem prop="staffCode" label="使用人">
              <Select
                style="width:158px"
                clearable
                filterable
                v-model="searchCondition.staffCode"
                ref="staff"
              >
                <Option
                  v-for="item in staffList"
                  :value="item.staffCode"
                  :key="item.pkId"
                >{{ item.staffCode }}-{{ item.staffName }}</Option>
              </Select>
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
        page: 1,
        rows: 10
      },
      staffList: [],
      tableData: {
        total: 0, // 总页数
        rows: [] // 每页条数
      },
      tableColumns: [
        {
          title: "物料编码",
          key: "measureNumber",
          width: 120,
          fixed: "left"
        },
        {
          title: "物料名称",
          key: "measureName",
          width: 180,
          fixed: "left"
        },
        {
          title: "物料图号",
          key: "model",
          width: 120
        },
        {
          title: "领用人",
          key: "userName",
          width: 100
        },
        {
          title: "库位",
          key: "warehouse",
          width: 120
        },
        {
          title: "封存状态",
          key: "sealMark",
          width: 120,
          render: function(h, params) {
            let statusStr = "";
            switch (params.row.sealMark) {
              case 0:
                statusStr = "启用";
                break;
              case 1:
                statusStr = "封存";
                break;
            }
            return h("div", statusStr);
          }
        },
        {
          title: "物料状态",
          key: "measureStatus",
          width: 120,
          render: function(h, params) {
            let statusStr = "";
            switch (params.row.measureStatus) {
              case 1:
                statusStr = "待入库";
                break;
              case 2:
                statusStr = "在库";
                break;
              case 3:
                statusStr = "完善定检周期";
                break;
              case 4:
                statusStr = "使用";
                break;
              case 5:
                statusStr = "返库待检";
                break;
              case 6:
                statusStr = "封存";
                break;
              case 7:
                statusStr = "丢失";
                break;
              case 8:
                statusStr = "报废";
                break;
            }
            return h("div", statusStr);
          }
        },
        {
          title: "物料条码",
          key: "measureBarcode",
          width: 240
        },
        {
          title: "生产厂家",
          key: "supplierName",
          width: 240
        },
        {
          title: "厂家编码",
          key: "manufacturingNumber",
          width: 220
        },

        {
          title: "本厂计量编号",
          key: "factoryMetrology",
          width: 220
        },
        {
          title: "定检记录",
          align: "center",
          children: [
            {
              title: "定检时间",
              key: "checkList",
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
                      undefined != this.tableData.rows[params.index].checkList
                        ? this.tableData.rows[params.index].checkList.map(
                            item => {
                              if (item.checkTime != null) {
                                return h(
                                  "li",
                                  {},
                                  dateFormat(
                                    new Date(item.checkTime),
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
              title: "定检类型",
              key: "checkList",
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
                      undefined != this.tableData.rows[params.index].checkList
                        ? this.tableData.rows[params.index].checkList.map(
                            item => {
                              let statusStr = "";
                              if (item.checkType != null) {
                                switch (item.checkType) {
                                  case 1:
                                    statusStr = "新量具定检";
                                    break;
                                  case 2:
                                    statusStr = "周期定检";
                                    break;
                                }
                                return h("li", {}, statusStr);
                              } else {
                                return h("li", {}, "暂无质检类型");
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
              title: "定检人",
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
                                    "yyyy-MM-dd"
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
                      undefined !=
                        this.tableData.rows[params.index].outboundList
                        ? this.tableData.rows[params.index].outboundList.map(
                            item => {
                              if (item.createTime != null) {
                                return h(
                                  "li",
                                  {},
                                  dateFormat(
                                    new Date(item.createTime),
                                    "yyyy-MM-dd"
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
              title: "领用部门",
              key: "outboundList",
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
                      undefined !=
                        this.tableData.rows[params.index].outboundList
                        ? this.tableData.rows[params.index].outboundList.map(
                            item => {
                              return h("li", {}, item.departmentName);
                            }
                          )
                        : ""
                    )
                  ]
                );
              }
            },
            {
              title: "领用班组",
              key: "outboundList",
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
                      undefined !=
                        this.tableData.rows[params.index].outboundList
                        ? this.tableData.rows[params.index].outboundList.map(
                            item => {
                              return h(
                                "li",
                                {},
                                !!item.receiveClass ? item.receiveClass : "  "
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
              title: "领用人",
              key: "outboundList",
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
                      undefined !=
                        this.tableData.rows[params.index].outboundList
                        ? this.tableData.rows[params.index].outboundList.map(
                            item => {
                              return h("li", {}, item.receiver);
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
        }
      ]
    };
  },
  created() {
    const self = this;
    self.getListData();
    self.getStaffListData();
  },
  methods: {
    getStaffListData() {
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/base/staff-list",
          isAuth: true,
          method: "post"
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            self.staffList = data.data;
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },
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
        measureNumber: self.searchCondition.measureNumber,
        staffCode: self.searchCondition.staffCode,
        //staffName:self.searchCondition.staffName,
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
          url: "/measure/measure-life-page-list",
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
      let measureNumber = self.searchCondition.measureNumber;
      let beginDate = self.searchCondition.beginDate;
      let endDate = self.searchCondition.endDate;
      let para = "";
      if (!!fullNumber) {
        para = "&fullNumber=" + fullNumber;
      }
      if (!!measureNumber) {
        para = "&measureNumber=" + measureNumber;
      }
      if (!!beginDate) {
        para = para + "&beginDate=" + beginDate;
      }
      if (!!endDate) {
        para = para + "&endDate=" + endDate;
      }
      window.location.href =
        getBaseUrl() +
        "/measure/measure-life-export?token=" +
        getToken() +
        para;
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
