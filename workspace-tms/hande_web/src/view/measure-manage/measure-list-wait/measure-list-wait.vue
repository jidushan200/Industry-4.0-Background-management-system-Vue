<template>
  <div>
    <Card>
      <Row>
        <Form ref="formInline" :model="searchCondition" :label-width="65" inline>
          <Col span="18">
            <FormItem prop="measureNumber" label="量具编码">
              <Input type="text" v-model="searchCondition.measureNumber" placeholder="请输入量具编码"></Input>
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
    <measure-warehouse-model ref="measureWarehouseModel" @search="doSearch"></measure-warehouse-model>
  </div>
</template>

<script>
import axios from "@/libs/api.request";
import { getToken, getBaseUrl } from "@/libs/util";
import { dateFormat, objCopy } from "@/libs/tools.js";
import measureWarehouseModel from "./measure-warehouse-model.vue";

export default {
  components: {
    measureWarehouseModel
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
          title: "物料编码",
          key: "measureNumber",
          width: 150,
          fixed: "left"
        },
        {
          title: "物料名称",
          key: "measureName",
          width: 220,
          fixed: "left"
        },
        {
          title: "物料图号",
          key: "model",
          width: 150
        },
        {
          title: "物料条码",
          key: "measureBarcode",
          width: 240
        },
        {
          title: "生产厂家",
          key: "supplierName",
          width: 200
        },
        {
          title: "厂家编码",
          key: "manufacturingNumber",
          width: 220
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
                statusStr = "在用";
                break;
              case 4:
                statusStr = "封存";
                break;
              case 5:
                statusStr = "报废";
                break;
              case 6:
                statusStr = "丢失";
                break;
            }
            return h("div", statusStr);
          }
        },
        {
          title: "本厂计量编号",
          key: "factoryMetrology",
          width: 220
        },
        {
          title: "质检时间",
          key: "checkTime",
          width: 150,
          render: function(h, params) {
            return h(
              "div",
              dateFormat(new Date(params.row.updateTime), "yyyy-MM-dd")
            );
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
        measureNumber: self.searchCondition.measureNumber,
        page: self.searchCondition.page,
        rows: self.searchCondition.rows,
        beginDate: self.searchCondition.beginDate,
        endDate: self.searchCondition.endDate,
        measureStatus: 1
      };

      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/measure/measure-page-list",
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
          debugger;
          self.$Notice.error({
            title: "错误提示",
            desc: res.data.info
          });
        });
    },
    warehouse(row) {
      const self = this;
      self.$refs.measureWarehouseModel.showModal(row);
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
    }
  }
};
</script>

<style>
</style>
