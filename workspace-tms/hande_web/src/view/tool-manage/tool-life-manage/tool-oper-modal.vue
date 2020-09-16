<template>
  <Modal v-model="toolOperModalshow" class="modal-class" title="刀具操作日志" width="1030" draggable>
    <Row>
      <Form ref="formInline" :model="searchCondition" :label-width="65" inline>
        <Col span="16">
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
            <Button type="primary" style="margin-left:-60px" @click="doSearch('formInline')">搜索</Button>
          </FormItem>
          <FormItem style="margin-left:-65px">
            <Button type="dashed" @click="doReset('formInline')">重置</Button>
          </FormItem>
        </Col>
        <Col span="8">
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
    <div slot="footer">
      <Button @click="closeModal()" style="margin-left: 8px">关闭</Button>
    </div>
  </Modal>
</template>
<script>
import { dateFormat, objCopy } from "@/libs/tools.js";
import { getToken, getBaseUrl } from "@/libs/util";
import axios from "@/libs/api.request";
import qs from "qs";
export default {
  data() {
    return {
      toolOperModalshow: false,
      searchCondition: {
        dateInterval: [],
        fullNumber: "",
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
          fixed: "left",
          key: "fullNumber",
          width: 240
        },
        {
          title: "物料名称",
          key: "toolName",
          width: 220
        },
        {
          title: "物料编码",
          key: "toolNumber",
          width: 120
        },
        {
          title: "物料图号",
          key: "toolMap",
          width: 120
        },
        {
          title: "操作类型",
          key: "operType",
          width: 120,
          render: function(h, params) {
            let statusStr = "";
            switch (params.row.operType) {
              case 0:
                statusStr = "新刀质检";
                break;
              case 1:
                statusStr = "新刀入库";
                break;
              case 2:
                statusStr = "领用出库";
                break;
              case 3:
                statusStr = "生产返库";
                break;
              case 4:
                statusStr = "刃磨出库";
                break;
              case 5:
                statusStr = "刃磨";
                break;
              case 6:
                statusStr = "刃磨检验";
                break;
              case 7:
                statusStr = "涂层出库";
                break;
              case 8:
                statusStr = "涂层检验";
                break;
              case 9:
                statusStr = "刀盘安装";
                break;
              case 10:
                statusStr = "刀具报废申请";
                break;
              case 11:
                statusStr = "报废审核";
                break;
              case 12:
                statusStr = "执行报废";
                break;
            }
            return h("div", statusStr);
          }
        },
        {
          title: "操作时间",
          key: "createTime",
          minWidth: 150,
          render: function(h, params) {
            return h(
              "div",
              dateFormat(new Date(params.row.createTime), "yyyy-MM-dd HH:mm:ss")
            );
          }
        },
        {
          title: "操作人",
          key: "createUserName",
          width: 100
        },
        {
          title: "操作内容",
          key: "operateInfo",
          width: 160
        },
        {
          title: "备注",
          key: "operateRemark",
          width: 160
        }
      ]
    };
  },
  created() {
    const self = this;
  },
  methods: {
    showModal(fullNumber) {
      const self = this;
      self.toolOperModalshow = true;
      self.searchCondition.fullNumber = fullNumber;
      self.getListData();
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
        fullNumber: self.searchCondition.fullNumber,
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
          url: "/tool/tool-oper-page-list",
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
    //关闭弹窗
    closeModal() {
      const self = this;
      self.toolOperModalshow = false;
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
      if (!!beginDate) {
        para = para + "&beginDate=" + beginDate;
      }
      if (!!endDate) {
        para = para + "&endDate=" + endDate;
      }
      window.location.href =
        getBaseUrl() + "/tool/tool-oper-export?token=" + getToken() + para;
    }
  },
  mounted() {}
};
</script>