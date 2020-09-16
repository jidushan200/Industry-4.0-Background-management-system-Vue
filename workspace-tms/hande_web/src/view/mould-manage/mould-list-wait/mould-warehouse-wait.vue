<template>
  <div>
    <Card>
      <Row>
        <Form ref="formInline" :model="searchCondition" :label-width="65" inline>
          <Col span="18">
            <FormItem prop="measureNumber" label="模具条码">
              <Input type="text" v-model="searchCondition.mouldBarcode" placeholder="请输入模具条码"></Input>
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
              <Button type="dashed" @click="doReset('formInline')">重置</Button>
            </FormItem>
          </Col>
        </Form>
      </Row>

      <Table ref="tablesMain" :data="tableData.rows" :columns="tableColumns" stripe border></Table>

      <div style="margin: 10px;overflow: hidden">
        <div style="float: right;">
          <Page show-total
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
    <mould-warehouse-model ref="mouldWarehouseModel" @search="doSearch"></mould-warehouse-model>
  </div>
</template>

<script>
import axios from "@/libs/api.request";
import { getToken, getBaseUrl } from "@/libs/util";
import { dateFormat, objCopy } from "@/libs/tools.js";
import mouldWarehouseModel from "./mould-warehouse-model.vue";

export default {
  components: {
    mouldWarehouseModel
  },
  data() {
    return {
      searchCondition: {
        toolNumber: "",
        warehouseCode: "",
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
          minWidth: 150
        },
        {
          title: "模具条码",
          key: "mouldBarcode",
          minWidth: 300
        },
        {
          title: "模具坯编码",
          key: "embryoCode",
          minWidth: 140
        },
        {
          title: "模具坯名称",
          key: "embryoName",
          minWidth: 240
        },
        {
          title: "热处理批次号",
          key: "heatNumber",
          minWidth: 150
        },
        {
          title: "表面处理批次号",
          key: "surfaceNumber",
          minWidth: 150
        },
        {
          title: "模具状态",
          key: "mouldStatus",
          width: 100,
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
                statusStr = "在用";
                break;
            }
            return h("div", statusStr);
          }
        },
        {
          title: "操作",
          key: "action",
          width: 120,
          align: "center",
          fixed: "right",
          render: (h, params) => {
            /**
             * render写法 添加按钮
             */
            return h("div", [
              h(
                "Button",
                {
                  props: {
                    type: "primary",
                    size: "small"
                  },
                  on: {
                    click: () => {
                      this.warehouse(params.row);
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
        mouldBarcode: self.searchCondition.mouldBarcode,
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
          url: "/mould/mould-wait-page-list",
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
    warehouse(row) {
      const self = this;
      self.$refs.mouldWarehouseModel.showModal(row);
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
      window.location.href =
        getBaseUrl() + "/base/staff-export?token=" + getToken();
    }
  }
};
</script>

<style>
</style>
