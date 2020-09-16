<template>
  <div>
    <Card>
      <Row>
        <Form ref="formInline" :model="searchCondition" :label-width="65" inline>
          <Col span="18">
            <FormItem label="刀具条码" prop="fullNumber">
              <Input style="width:170px" v-model="searchCondition.fullNumber" placeholder="请输入刀具条码"/>
            </FormItem>
            <FormItem label="刀具编码" prop="toolNumber">
              <Input style="width:170px" v-model="searchCondition.toolNumber" placeholder="请输入刀具编码"/>
            </FormItem>
            <FormItem label="刀具名称" prop="toolNumber">
              <Input style="width:170px" v-model="searchCondition.toolName" placeholder="请输入刀具名称"/>
            </FormItem>
            <FormItem prop="dateInterval" label="时间区间">
              <DatePicker
                type="daterange"
                split-panels
                placeholder="请选择时间区间"
                placement="bottom-end"
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
import { getToken, getBaseUrl } from "@/libs/util";
import { dateFormat, objCopy } from "@/libs/tools.js";
export default {
  components: {},
  data() {
    return {
      searchCondition: {
        fullNumber: "",
        toolNumber: "",
        toolName: "",
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
          minWidth: 260,
          fixed: "left"
        },
        {
          title: "物料编码",
          key: "toolNumber",
          minWidth: 120,
          fixed: "left"
        },
        {
          title: "物料名称",
          key: "toolName",
          minWidth: 180
        },
        {
          title: "物料图号",
          key: "toolMap",
          minWidth: 120
        },
        {
          title: "入库日期",
          key: "createTime",
          width: 120,
          render: function(h, params) {
            return h(
              "div",
              dateFormat(new Date(params.row.createTime), "yyyy-MM-dd")
            );
          }
        },
        {
          title: "制件编码",
          key: "partCode",
          minWidth: 130
        },
        {
          title: "制件名称",
          key: "partName",
          minWidth: 200
        },
        {
          title: "加工总数",
          key: "processAmount",
          minWidth: 120
        },
        {
          title: "涂层次数",
          key: "coatTimes",
          minWidth: 120
        },
        {
          title: "涂层总价",
          key: "coatPriceAmount",
          minWidth: 120
        },
        {
          title: "单件消耗",
          key: "coatStatistics",
          minWidth: 120,
          fixed: "right"
        },
        {
          title: "刀具状态",
          key: "toolState",
          width: 120,
          render: function(h, params) {
            let statusStr = "";
            switch (params.row.toolState) {
              case 1:
                statusStr = "在库";
                break;
              case 2:
                statusStr = "使用";
                break;
              case 3:
                statusStr = "待刃磨";
                break;
              case 4:
                statusStr = "刃磨";
                break;
              case 5:
                statusStr = "已送涂";
                break;
              case 6:
                statusStr = "涂层";
                break;
              case 7:
                statusStr = "刃磨待检";
                break;
              case 8:
                statusStr = "交回返库";
                break;
              case 9:
                statusStr = "质检完成";
                break;
              case 10:
                statusStr = "已报废";
                break;
            }
            return h("div", statusStr);
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
        fullNumber: self.searchCondition.fullNumber,
        toolNumber: self.searchCondition.toolNumber,
        toolName: self.searchCondition.toolName,
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
          url: "/tool/tool-coat-statistics-page-list",
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

      let fullNumber = self.searchCondition.fullNumber;
      let toolNumber = self.searchCondition.toolNumber;
      let beginDate = self.searchCondition.beginDate;
      let endDate = self.searchCondition.endDate;
      let para = "";
      if (!!fullNumber) {
        para = "&fullNumber=" + fullNumber;
      }
      if (!!toolNumber) {
        para = para + "&toolNumber=" + toolNumber;
      }
      if (!!beginDate) {
        para = para + "&beginDate=" + beginDate;
      }
      if (!!endDate) {
        para = para + "&endDate=" + endDate;
      }

      window.location.href =
        getBaseUrl() + "/tool/tool-coat-statistics-export?token=" + getToken() + para;
    }
  }
};
</script>


