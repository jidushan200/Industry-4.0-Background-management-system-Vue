<template>
  <div>
    <Card>
      <Row>
        <Form ref="formInline" :model="searchCondition" :label-width="65" inline>
          <row>
            <FormItem prop="fixtureNumber" label="夹具编码">
              <Input type="text" v-model="searchCondition.fixtureNumber" placeholder="请输入夹具编码" />
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
              <Button type="primary" style="margin-left: -60px" @click="doSearch('formInline')">搜索</Button>
            </FormItem>
            <FormItem style="margin-left: -65px">
              <Button style type="dashed" @click="doReset('formInline')">重置</Button>
            </FormItem>
          </row>
        </Form>
      </Row>

      <Table
        ref="tablesMain"
        :data="tableData.rows"
        :columns="tableColumns"
        border
        stripe
        highlight-row
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
            show-sizer
          ></Page>
        </div>
      </div>
    </Card>
    <fixture-warehouse-modal ref="fixtureWarehouseModal" @search="getListData"></fixture-warehouse-modal>
  </div>
</template>

<script>
import axios from "@/libs/api.request";
import { dateFormat, objCopy } from "@/libs/tools.js";
import fixtureWarehouseModal from "./fixture-warehouse-modal.vue";
export default {
  components: {
    fixtureWarehouseModal
  },
  data() {
    return {
      searchCondition: {
        fixtureNumber: "",
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
          title: "夹具条码",
          key: "fixtureBarcode",
          minWidth: 180
        },
        {
          title: "夹具编码",
          key: "fixtureNumber",
          minWidth: 160
        },
        {
          title: "夹具名称",
          key: "fixtureName",
          minWidth: 220
        },
        {
          title: "夹具图号",
          key: "fixtureMap",
          minWidth: 180
        },
        {
          title: "夹具类型",
          key: "fixtureType",
          minWidth: 100,
          render: function(h, params) {
            let temp = "";
            switch (params.row.fixtureType) {
              case 1:
                temp = "组合";
                break;
              case 2:
                temp = "配件";
                break;
            }
            return h("div", temp);
          }
        },
        {
          title: "来源",
          key: "procurementType",
          minWidth: 100,
          render: function(h, params) {
            let temp = "";
            switch (params.row.procurementType) {
              case 1:
                temp = "自制";
                break;
              case 2:
                temp = "外购";
                break;
            }
            return h("div", temp);
          }
        },
        {
          title: "供应商",
          key: "supplierName",
          minWidth: 180
        },
        {
          title: "检验时间",
          key: "updateTime",
          minWidth: 150,
          render: function(h, params) {
            return h(
              "div",
              dateFormat(new Date(params.row.updateTime), "yyyy-MM-dd HH:mm")
            );
          }
        },
        {
          title: "操作",
          key: "action",
          minWidth: 120,
          align: "center",
          fixed: "right",
          render: (h, params) => {
            return h("div", [
              h(
                "Button",
                {
                  props: {
                    type: "primary",
                    size: "small"
                  },
                  style: {
                    marginRight: "5px"
                  },
                  on: {
                    click: () => {
                      this.addFixture(params.row);
                    }
                  }
                },
                "入库"
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
        fixtureNumber: self.searchCondition.fixtureNumber,
        checkResult: 1,
        checkStatus: 1,
        handleResult: 0,
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
          url: "/fixture/purchase-receipt-page-list",
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
    handlePageSize(value) {
      const self = this;
      self.$set(self.searchCondition, "rows", value);
      self.getListData();
    },
    doSearch(name) {
      const self = this;
      self.searchCondition.page = 1;
      self.changePage(1);
    },
    doReset(name) {
      const self = this;
      self.$refs[name].resetFields();
      self.getListData();
    },
    addFixture(row) {
      const self = this;
      self.$refs.fixtureWarehouseModal.showModal(row);
    }
  }
};
</script>
<style>
</style>
