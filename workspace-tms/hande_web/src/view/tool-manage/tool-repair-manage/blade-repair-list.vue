<style lang="less">
.fsize {
  font-size: 18px !important;
}
</style>
<template>
  <div>
    <Card>
      <Row>
        <Form ref="formInline" :model="searchCondition" :label-width="90" inline>
          <Col span="20">
            <FormItem prop="composeNumber" label="刀条组合条码">
              <Input
                type="text"
                style="width:140px;"
                v-model="searchCondition.composeNumber"
                placeholder="请输入刀条组合条码"
              ></Input>
            </FormItem>
            <FormItem prop="toolNumber" label="物料编码">
              <Input
                type="text"
                style="width:140px;"
                v-model="searchCondition.toolNumber"
                placeholder="请输入物料编码"
              ></Input>
            </FormItem>
            <FormItem prop="executor" label="刃磨人">
              <Input
                type="text"
                style="width:100px;"
                v-model="searchCondition.executor"
                placeholder="请输入刃磨人"
              ></Input>
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
              <Button type="primary" style="margin-left: -60px;" @click="doSearch('formInline')">搜索</Button>
            </FormItem>
            <FormItem style="margin-left: -65px;">
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
import { dateFormat, objCopy } from "@/libs/tools.js";
import { getToken, getBaseUrl } from "@/libs/util";
export default {
  data() {
    return {
      searchCondition: {
        composeNumber: "",
        toolNumber: "",
        executor: "",
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
          title: "刀条组合条码",
          fixed: "left",
          minWidth: 150,
          key: "fullNumber"
        },
        {
          title: "物料编码",
          fixed: "left",
          minWidth: 160,
          key: "toolNumber"
        },
        {
          title: "物料名称",
          fixed: "left",
          minWidth: 240,
          key: "toolName"
        },
        {
          title: "物料图号",
          minWidth: 200,
          key: "toolMap"
        },
        {
          title: "数量",
          minWidth: 100,
          key: "toolQty"
        },
        {
          title: "刃磨量",
          minWidth: 100,
          key: "repairMeasure"
        },
        {
          title: "刃磨人",
          minWidth: 100,
          key: "executor"
        },
        {
          title: "刃磨时间",
          minWidth: 160,
          key: "executTime",
          render: function(h, params) {
            return h(
              "div",
              dateFormat(new Date(params.row.executTime), "yyyy-MM-dd HH:mm:ss")
            );
          }
        },
        {
          title: "备注",
          minWidth: 160,
          key: "remark"
        }
      ]
    };
  },
  created() {
    const self = this;
    self.getListData();
    //self.getStaffListData();
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
        fullNumber: self.searchCondition.composeNumber,
        executor: self.searchCondition.executor,
        typeId: 4,
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
          url: "/tool/tool-repair-page-list",
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
      let para = "token=" + getToken() + "&typeId=4";
      if (!!self.searchCondition.composeNumber) {
        para = para + "&fullNumber=" + self.searchCondition.composeNumber;
      }
      if (!!self.searchCondition.toolNumber) {
        para = para + "&toolNumber=" + self.searchCondition.toolNumber;
      }
      if (!!self.searchCondition.executor) {
        para = para + "&executor=" + self.searchCondition.executor;
      }
      if (!!self.searchCondition.beginDate) {
        para = para + "&beginDate=" + self.searchCondition.beginDate;
      }
      if (!!self.searchCondition.endDate) {
        para = para + "&endDate=" + self.searchCondition.endDate;
      }
      window.location.href = getBaseUrl() + "/tool/tool-repair-export?" + para;
    }
  }
};
</script>

<style>
</style>
