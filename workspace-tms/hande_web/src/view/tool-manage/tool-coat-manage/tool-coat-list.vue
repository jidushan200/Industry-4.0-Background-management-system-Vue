<template>
  <div>
    <Card>
      <Row>
        <Form ref="formInline" :model="searchCondition" :label-width="80" inline>
          <Col span="20">
            <FormItem label="涂层供应商" prop="supplierId">
              <Select
                style="width:170px"
                v-model="searchCondition.supplierId"
                ref="supplier"
                @on-change="supplierListSelectChange"
              >
                <Option
                  v-for="item in supplierList"
                  :value="item.pkId"
                  :key="item.pkId"
                >{{ item.supplierName }}</Option>
              </Select>
            </FormItem>
            <FormItem label="涂层出库人" prop="outboundUserName">
              <Input
                style="width:100px"
                v-model="searchCondition.outboundUserName"
                placeholder="涂层出库人"
              />
            </FormItem>
            <FormItem prop="dateInterval" label="时间区间">
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
              <Button type="primary" style="margin-left: -75px;" @click="doSearch('formInline')">搜索</Button>
            </FormItem>
            <FormItem style="margin-left: -95px;">
              <Button style type="dashed" @click="doReset('formInline')">重置</Button>
            </FormItem>
          </Col>
          <Col span="4">
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
import tool_coat_manage_form_config from "./tool-coat-manage-form-config.js";
import { dateFormat, objCopy } from "@/libs/tools.js";
import { getToken, getBaseUrl } from "@/libs/util";
import toolType from "./tool-type.js";
import toolSelect from "./tool-select.js";
export default {
  data() {
    return {
      toolType: toolType,
      toolSelect: toolSelect,
      supplierList: [],
      searchCondition: {
        supplierId: "",
        outboundUserName:"",
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
          title: "物料条码",
          key: "fullNumber",
          minWidth: 240
        },
        {
          title: "物料名称",
          key: "toolName",
          minWidth: 220
        },
        {
          title: "物料编码",
          key: "toolNumber",
          minWidth: 180
        },
        {
          title: "数量",
          key: "toolQty",
          minWidth: 100
        },
        {
          title: "涂层出库人",
          key: "outboundUserName",
          minWidth: 100
        },
        {
          title: "涂层出库时间",
          key: "outboundTime",
          minWidth: 160,
          render: function(h, params) {
            if (!!params.row.outboundTime) {
              return h(
                "div",
                dateFormat(
                  new Date(params.row.outboundTime),
                  "yyyy-MM-dd HH:mm:ss"
                )
              );
            }
          }
        },
        {
          title: "供应商",
          key: "coatSupplier",
          minWidth: 200
        },
        {
          title: "涂层时间",
          key: "coatTime",
          minWidth: 160,
          render: function(h, params) {
            return h(
              "div",
              dateFormat(new Date(params.row.coatTime), "yyyy-MM-dd HH:mm:ss")
            );
          }
        },
        {
          title: "价格",
          key: "coatPrice",
          minWidth: 120
        }
      ],
      formValidate: tool_coat_manage_form_config.formValidate, //user form表单字段
      ruleValidate: tool_coat_manage_form_config.ruleValidate //user form表单验证规则
    };
  },
  created() {
    const self = this;
    self.getListData();
    self.getSupplierListData();
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
        supplierId: self.searchCondition.supplierId,
        coatSupplier: self.searchCondition.coatSupplier,
        page: self.searchCondition.page,
        rows: self.searchCondition.rows,
        outboundUserName:self.searchCondition.outboundUserName,
        beginDate: self.searchCondition.beginDate,
        endDate: self.searchCondition.endDate
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/tool-coat-page-list",
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
    getSupplierListData() {
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
          isAuth: true,
          method: "post",
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
    supplierListSelectChange(item) {
      let supplierList = this.supplierList;
      if (item !== undefined) {
        for (const supplier of supplierList) {
          if (supplier.pkId === item) {
            this.searchCondition.coatSupplier = supplier.supplierName;
          }
        }
      }
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

      let para = "token=" + getToken();
      if (!!self.searchCondition.supplierId) {
        para = para + "&supplierId=" + self.searchCondition.supplierId;
      }
      if(!!self.searchCondition.outboundUserName){
        para = para + "&outboundUserName=" + self.searchCondition.outboundUserName;
      }
      if (!!self.searchCondition.beginDate) {
        para = para + "&beginDate=" + self.searchCondition.beginDate;
      }
      if (!!self.searchCondition.endDate) {
        para = para + "&endDate=" + self.searchCondition.endDate;
      }
      window.location.href = getBaseUrl() + "/tool/tool-coat-export?" + para;
    }
  }
};
</script>

<style>
</style>
