<template>
  <div>
    <Card>
      <Row>
        <Form ref="formInline" :model="searchCondition" inline :label-width="65">
          <Col span="24">
            <FormItem label="图号名称" prop="toolNumber">
              <Select
                style="width:158px"
                v-model="searchCondition.toolNumber"
                ref="toolName"
                placeholder="请选择图号名称"
                clearable
                filterable
              >
                <Icon type="ios-search" slot="suffix" />
                <Option
                  v-for="item in baseList"
                  :value="item.toolNumber"
                  :key="item.toolNumber"
                >{{ item.toolName }}</Option>
              </Select>
            </FormItem>
            <FormItem prop="purchaseType" label="申购类型">
              <Select
                style="width:158px"
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
  </div>
</template>

<script>
import axios from "@/libs/api.request";
import { dateFormat, objCopy } from "@/libs/tools.js";
import purchaseSelect from "./purchaseSelect-type.js";
export default {
  data() {
    return {
      baseList:[],
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
          minWidth: 150
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
          minWidth: 160
        },
        {
          title: "申请人",
          key: "applierName",
          minWidth: 120
        },
        {
          title: "申购时间",
          key: "needTime",
          sortable: true,
          minWidth: 160,
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
          align: "center",
          minWidth: 100
        },
        {
          title: "需求时间",
          key: "applyTime",
          sortable: true,
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
          minWidth: 150,
          key: "purchaseResion",
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
          minWidth: 150,
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
          title: "审核人",
          key: "auditorName",
          align: "center",
          minWidth: 120
        },
        {
          title: "审核结果",
          key: "auditResult",
          align: "center",
          minWidth: 200
        }
      ]
    };
  },
  created() {
    const self = this;
    self.getListData();
    self.getBaseListData();
  },
  methods: {
     getBaseListData() {
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/tool-base-list",
          isAuth: true,
          method: "post"
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200 && data.data !== null) {
            self.baseList = data.data;
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
        toolNumber: self.searchCondition.toolNumber,
        purchaseType: self.searchCondition.purchaseType,
        page: self.searchCondition.page,
        rows: self.searchCondition.rows,
        beginDate: self.searchCondition.beginDate,
        endDate: self.searchCondition.endDate,
        auditorId: self.$store.state.user.userData.data.userId
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/audited-apply-page-list.json",
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
    }
  }
};
</script>

<style>
</style>
