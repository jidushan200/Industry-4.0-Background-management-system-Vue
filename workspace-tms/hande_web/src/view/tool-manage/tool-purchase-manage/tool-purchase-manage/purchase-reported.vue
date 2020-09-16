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

    <purchase-apply-model ref="purchaseApplyModel" @search="doSearch"></purchase-apply-model>
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
          minWidth: 120
        },
        {
          title: "物料名称",
          key: "toolName",
          minWidth: 250
        },
        {
          title: "物料图号",
          key: "toolMap",
          minWidth: 120
        },
        {
          title: "物料类型",
          key: "typeName",
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
              dateFormat(new Date(params.row.needTime), "yyyy-MM-dd HH:mm:ss")
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
          title: "采购原因",
          key: "purchaseResion",
          minWidth: 120,
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
          minWidth: 120,
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
          title: "操作",
          key: "action",
          minWidth: 120,
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
        applyStatus: 4
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/tool-purchase-apply-page-list.json",
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
      self.changePage(1);
    },
    doReset(name) {
      const self = this;
      self.$refs[name].resetFields();
      self.getListData();
    },
    selectReport(pkId) {
      const self = this;
      self.$refs.purchaseApplyModel.showModal(pkId, true);
    },

   
  }
};
</script>

<style>
</style>
