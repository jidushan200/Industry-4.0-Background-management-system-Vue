<template>
  <div>
    <Card>
      <Row>
        <Form ref="formInline" :model="searchCondition" :label-width="65" inline>
          <Col span="18">
            <FormItem label="供应商" prop="supplierId">
              <Select
                style="width:200px"
                v-model="searchCondition.supplierId"
                ref="supplierId"
                clearable
                filterable
              >
                <Option
                  v-for="item in supplierList"
                  :value="item.pkId"
                  :key="item.pkId"
                >{{item.supplierName }}</Option>
              </Select>
            </FormItem>
            <FormItem prop="dateInterval" label="涂回时间">
              <DatePicker
                type="daterange"
                split-panels
                placeholder="请选择时间区间"
                placement="bottom-end"
                style="width:180px;"
                v-model="searchCondition.dateInterval"
              ></DatePicker>
            </FormItem>
            <FormItem>
              <Button type="primary" style="margin-left: -60px" @click="doSearch('formInline')">搜索</Button>
            </FormItem>
            <FormItem style="margin-left: -65px">
              <Button type="dashed" @click="doReset('formInline')">重置</Button>
            </FormItem>
          </Col>

          <Col span="6">
            <FormItem style="width:100%;text-align:right">
              <Button type="warning" @click="exportData()">
                <Icon type="ios-download-outline"></Icon>导出
              </Button>
            </FormItem>
          </Col>
        </Form>
      </Row>

      <Table
        ref="tablesMain"
        :data="tableData.rows"
        :columns="tableColumns"
        stripe
        border
        @on-selection-change="selectionChange"
      ></Table>
      <div style="margin: 10px;overflow: hidden">
        <div style="float: right;">
          <Page
            show-total
            :total="tableData.total"
            :page-size="searchCondition.rows"
            :current="searchCondition.page"
            @on-change="changePage"
            @on-page-size-change="handlePageSize"
            :page-size-opts="[10,20,50,100]"
            show-sizer
          ></Page>
        </div>
      </div>
    </Card>
    <tool-oper-modal ref="toolOperModal"></tool-oper-modal>
  </div>
</template>

<script>
import axios from "@/libs/api.request";
import { getToken, getBaseUrl } from "@/libs/util";
import { dateFormat, objCopy } from "@/libs/tools.js";
import toolOperModal from "./tool-oper-modal.vue";
export default {
  components: { toolOperModal },
  data() {
    return {
      searchCondition: {
        settlementStatus: null,
        supplierId: null,
        dateInterval: [],
        page: 1,
        rows: 10
      },
      supplierList: [],
      tableData: {
        total: 0, // 总页数
        rows: [] // 每页条数
      },
      tableColumns: [
        {
          title: "供应商名称",
          key: "coatSupplier",
          minWidth: 220
        },
        {
          title: "物料编码",
          key: "toolNumber",
          minWidth: 120
        },
        {
          title: "物料名称",
          key: "toolName",
          minWidth: 220
        },
        {
          title: "物料图号",
          key: "toolMap",
          minWidth: 160
        },
        {
          title: "物料条码",
          key: "fullNumber",
          minWidth: 260,
          render: (h, params) => {
            return h("div", [
              h(
                "a",
                {
                  on: {
                    click: () => {
                      this.showOperList(
                        params.row.fullNumber,
                        params.row.coatTime
                      );
                    }
                  }
                },
                params.row.fullNumber
              )
            ]);
          }
        },
        {
          title: "涂回时间",
          key: "coatTime",
          width: 150,
          render: function(h, params) {
            return h(
              "div",
              dateFormat(new Date(params.row.coatTime), "yyyy-MM-dd HH:mm:ss")
            );
          }
        },
        {
          title: "加工数量",
          key: "processQty",
          width: 100
        },
        {
          title: "理论加工数量",
          key: "theoreticalQty",
          minWidth: 120
        },
        {
          title: "加工完成度",
          key: "completionDegree",
          minWidth: 120,
          render: function(h, params) {
            if (!!params.row.completionDegree) {
              return h(
                "div",
                (params.row.completionDegree * 100).toFixed(0) + "%"
              );
            }
          }
        }
      ]
    };
  },
  created() {
    const self = this;
    self.getSupplierList();
    self.getListData();
  },
  methods: {
    getSupplierList() {
      const self = this;
      let para = {
        isCoat: 1
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/base/supplier-list",
          method: "post",
          isAuth: true,
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
        supplierId: self.searchCondition.supplierId,
        settlementStatus: self.searchCondition.settlementStatus,
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
          url: "/tool/coat-statistics-page-list",
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
    selectionChange(selection) {
      const self = this;
      self.multiselectRowData = selection;
    },
    showOperList(fullNumber, coatTime) {
      const self = this;
      debugger;
      self.$refs.toolOperModal.showModal(fullNumber, coatTime);
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

      let supplierId = self.searchCondition.supplierId;
      let beginDate = self.searchCondition.beginDate;
      let endDate = self.searchCondition.endDate;
      let para = "";
      if (!!supplierId) {
        para = "&supplierId=" + supplierId;
      }
      if (!!beginDate) {
        para = para + "&beginDate=" + beginDate;
      }
      if (!!endDate) {
        para = para + "&endDate=" + endDate;
      }
      window.location.href =
        getBaseUrl() +
        "/tool/coat-statistics-export?token=" +
        getToken() +
        para;
    }
  }
};
</script>


