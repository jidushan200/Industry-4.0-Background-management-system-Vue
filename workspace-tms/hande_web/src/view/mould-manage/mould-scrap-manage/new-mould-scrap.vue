<template>
  <div>
    <Card>
      <Row>
        <Form ref="formInline" :model="searchCondition" :label-width="65" inline>
          <Col span="20">
            <FormItem prop="mouldBarcode" label="模具条码">
              <Input type="text" v-model="searchCondition.mouldBarcode" placeholder="请输入模具条码"></Input>
            </FormItem>
            <FormItem prop="mouldNumber" label="模具编码">
              <Input type="text" v-model="searchCondition.mouldNumber" placeholder="请输入模具编码"></Input>
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

export default {
  data() {
    return {
      searchCondition: {
        mouldNumber: "",
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
          minWidth: 160
        },
        {
          title: "模具名称",
          key: "mouldName",
          minWidth: 320
        },
        {
          title: "模具名称",
          key: "mouldSeq",
          minWidth: 220
        },
        {
          title: "模具状态",
          key: "mouldStatus",
          width: 120,
          render: function(h, params) {
            let statusStr = "";
            switch (params.row.mouldStatus) {
              case 0:
                statusStr = "新模具待检";
                break;
              case 1:
                statusStr = "合格";
                break;
              case 2:
                statusStr = "不合格待处理";
                break;
              case 3:
                statusStr = "报废";
                break;
            }
            return h("div", statusStr);
          }
        },
        {
          title: "模具图号",
          key: "mouldMap",
          minWidth: 120
        },
        {
          title: "报废处理人",
          key: "scripHandler",
          minWidth: 160
        },
        {
          title: "报废处理时间",
          key: "scripHandlerTime",
          minWidth: 160,
          sortable: true,
          render: function(h, params) {
            if (
              params.row.scripHandlerTime !== null &&
              params.row.scripHandlerTime !== undefined
            ) {
              return h(
                "div",
                dateFormat(
                  new Date(params.row.scripHandlerTime),
                  "yyyy-MM-dd HH:mm:ss"
                )
              );
            }
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
        mouldNumber: self.searchCondition.mouldNumber,
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
          url: "/mould/new-mould-scrip-page-list",
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
