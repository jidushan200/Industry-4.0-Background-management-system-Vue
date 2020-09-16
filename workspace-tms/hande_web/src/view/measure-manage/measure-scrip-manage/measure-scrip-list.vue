<template>
  <div>
    <Card>
      <Row>
        <Form ref="formInline" :model="searchCondition" :label-width="65" inline>
          <Col span="20">
            <FormItem prop="measureNumber" label="量具编码">
              <Input type="text" v-model="searchCondition.measureNumber" placeholder="请输入量具编码"></Input>
            </FormItem>
            <!-- <FormItem prop="toolName" label="刀具名称">
              <Input type="text" v-model="searchCondition.toolName" placeholder="请输入刀具名称"></Input>
            </FormItem> -->
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
              <Button type="warning" @click="exportData()"><Icon type="ios-download-outline"></Icon>导出</Button>
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
import { getToken, getBaseUrl } from "@/libs/util";
import { dateFormat, objCopy } from "@/libs/tools.js";

export default {
  data() {
    return {
      searchCondition: {
        measureNumber: "",
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
          width: 120,
          fixed: "left"
        },
        {
          title: "物料名称",
          key: "measureName",
          width: 180,
          fixed: "left"
        },
        {
          title: "物料图号",
          key: "model",
          width: 120
        },
        {
          title: "物料条码",
          key: "measureBarcode",
          width: 240
        },
        {
          title: "库位",
          key: "warehouse",
          width: 120
        },
        {
          title: "领用部门",
          key: "useDepartmentName",
          width: 150
        },
        {
          title: "领用班组",
          key: "useTeamName",
          width: 120
        },
        {
          title: "领用人",
          key: "userName",
          width: 100
        },
        {
          title: "修理次数",
          key: "repairTimes",
          width: 100
        },
        {
          title: "封存状态",
          key: "sealMark",
          width: 120,
          render: function(h, params) {
            let statusStr = "";
            switch (params.row.sealMark) {
              case 0:
                statusStr = "启用";
                break;
              case 1:
                statusStr = "封存";
                break;
            }
            return h("div", statusStr);
          }
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
                statusStr = "完善定检周期";
                break;
              case 4:
                statusStr = "使用";
                break;
              case 5:
                statusStr = "返库待检";
                break;
              case 6:
                statusStr = "封存";
                break;
              case 7:
                statusStr = "丢失";
                break;
              case 8:
                statusStr = "报废";
                break;
            }
            return h("div", statusStr);
          }
        },
        {
          title: "封存时间",
          key: "sealTime",
          width: 150,
          render: function(h, params) {
            if (!!params.row.sealTime) {
              return h(
                "div",
                dateFormat(new Date(params.row.sealTime), "yyyy-MM-dd HH:mm:ss")
              );
            }
          }
        },
        {
          title: "启用时间",
          key: "enableTime",
          width: 150,
          render: function(h, params) {
            if (!!params.row.enableTime) {
              return h(
                "div",
                dateFormat(
                  new Date(params.row.enableTime),
                  "yyyy-MM-dd HH:mm:ss"
                )
              );
            }
          }
        },
        {
          title: "检定时间",
          key: "checkTime",
          width: 120,
          render: function(h, params) {
            if (!!params.row.checkTime) {
              return h(
                "div",
                dateFormat(new Date(params.row.checkTime), "yyyy-MM-dd")
              );
            }
          }
        },
        {
          title: "下次检定时间",
          key: "nextCheckTime",
          width: 120,
          render: function(h, params) {
            if (!!params.row.nextCheckTime) {
              return h(
                "div",
                dateFormat(new Date(params.row.nextCheckTime), "yyyy-MM-dd")
              );
            }
          }
        },
        {
          title: "生产厂家",
          key: "supplierName",
          width: 240
        },
        {
          title: "厂家编码",
          key: "manufacturingNumber",
          width: 220
        },

        {
          title: "本厂计量编号",
          key: "factoryMetrology",
          width: 220
        },
        {
          title: "质检时间",
          key: "checkTime",
          width: 120,
          render: function(h, params) {
            return h(
              "div",
              dateFormat(new Date(params.row.updateTime), "yyyy-MM-dd")
            );
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
        endDate: self.searchCondition.endDate
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/measure/measure-scrip-page-list",
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
      let measureNumber = self.searchCondition.measureNumber;
      let beginDate = self.searchCondition.beginDate;
      let endDate = self.searchCondition.endDate;
      let para = "";
      if (!!measureNumber) {
        para = "&measureNumber=" + measureNumber;
      }
      if (!!beginDate) {
        para = para + "&beginDate=" + beginDate;
      }
      if (!!endDate) {
        para = para + "&endDate=" + endDate;
      }
      window.location.href =
        getBaseUrl() +
        "/measure/measure-scrip-export?token=" +
        getToken() +
        para;
    }
  }
};
</script>

<style>
</style>
