<template>
  <div>
    <Card>
      <Row>
        <Form ref="formInline" :model="searchCondition" :label-width="65" inline>
          <Col span="20">
            <FormItem prop="toolNumber" label="物料编码">
              <Input type="text" v-model="searchCondition.toolNumber" placeholder="请输入物料编码"></Input>
            </FormItem>
            <FormItem prop="toolMap" label="物料图号">
              <Input type="text" v-model="searchCondition.toolMap" placeholder="请输入物料图号"></Input>
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
              <Button type="primary" style="margin-left: -60px;" @click="doSearch('formInline')">搜索</Button>
            </FormItem>
            <FormItem style="margin-left: -65px;">
              <Button style type="dashed" @click="doReset('formInline')">重置</Button>
            </FormItem>
          </Col>
          <!-- <Col span="4"> 
            <FormItem style="width:100%;text-align:right">
              <Button type="warning" @click="exportData()"><Icon type="ios-download-outline"></Icon>导出</Button>
            </FormItem>
          </Col>-->
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
import toolType from "./tool-type.js";
import tool_scrap_manage_form_config from "./tool-scrap-manage-form-config.js";
import { dateFormat, objCopy } from "@/libs/tools.js";

export default {
  data() {
    return {
      toolType: toolType,
      searchCondition: {
        toolMap: "",
        toolNumber: "",
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
          minWidth: 250
        },
        {
          title: "物料编码",
          key: "toolNumber",
          minWidth: 160
        },
        {
          title: "物料名称",
          key: "toolName",
          minWidth: 220
        },
        {
          title: "已生产数量",
          key: "processAmount",
          width: 100
        },        
        {
          title: "刀具图号",
          key: "toolMap",
          minWidth: 120
        },
        {
          title: "报废申请人",
          key: "scripApplicant",
          minWidth: 100
        },
        {
          title: "报废申请时间",
          key: "scripApplicantTime",
          minWidth: 160,
          render: function(h, params) {
            return h(
              "div",
              dateFormat(
                new Date(params.row.scripApplicantTime),
                "yyyy-MM-dd HH:mm:ss"
              )
            );
          }
        },
        {
          title: "报废审核人",
          key: "scripAuditor",
          minWidth: 100
        },
        {
          title: "报废审核时间",
          key: "scripAuditTime",
          minWidth: 160,
          render: function(h, params) {
            return h(
              "div",
              dateFormat(
                new Date(params.row.scripAuditTime),
                "yyyy-MM-dd HH:mm:ss"
              )
            );
          }
        },
        {
          title: "报废处理人",
          key: "scripHandler",
          minWidth: 100
        },
        {
          title: "报废处理时间",
          key: "scripHandlerTime",
          minWidth: 160,
          render: function(h, params) {
            return h(
              "div",
              dateFormat(
                new Date(params.row.scripHandlerTime),
                "yyyy-MM-dd HH:mm:ss"
              )
            );
          }
        }
        // {
        //   title: "单价（元）",
        //   key: "price",
        //   sortable: true
        // },
        // {
        //   title: "理论加工数量",
        //   key: "processTotal"
        // }
      ],
      formValidate: tool_scrap_manage_form_config.formValidate, //user form表单字段
      ruleValidate: tool_scrap_manage_form_config.ruleValidate //user form表单验证规则
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
        toolMap: self.searchCondition.toolMap,
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
          url: "/tool/tool-scrip-get-by-delMark",
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
      self.changePage(1);
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
