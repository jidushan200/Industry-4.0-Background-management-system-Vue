<template>
  <div>
    <Card>
      <Row>
        <Form ref="formInline" :model="searchCondition" :label-width="65" inline>
          <Col span="20">
            <FormItem prop="toolNumber" label="物料编码">
              <Input type="text" v-model="searchCondition.toolNumber" placeholder="请输入物料编码" />
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
              <Button type="primary" style="margin-left: -60px" @click="search('formInline')">搜索</Button>
            </FormItem>
            <FormItem style="margin-left: -65px">
              <Button type="dashed" @click="doReset('formInline')">重置</Button>
            </FormItem>
          </Col>         
        </Form>
      </Row>
      <Table :data="tableData.rows" :columns="tableColumns" border stripe highlight-row></Table>
      <div style="margin: 10px;overflow: hidden">
        <div style="float: right;">
          <Page
            show-total
            :total="tableData.total"
            :page-size="searchCondition.rows"
            :current="searchCondition.page"
            show-sizer
            @on-change="changePage"
            @on-page-size-change="handlePageSize"
          />
        </div>
      </div>
    </Card>
    <Modal v-model="deleteModalShow" width="250" title="删除报告" @on-ok="deleteReportSureBtn">
      <p>确定删除所选报告？</p>
    </Modal>
    <new-unqualified-model ref="newUnqualifiedModel" @search="getListData"></new-unqualified-model>
    <unqualified-log-model ref="unqualifiedLogModel"></unqualified-log-model>
  </div>
</template>
<script>
import axios from "@/libs/api.request";
import { dateFormat, objCopy, hasOneOf } from "@/libs/tools.js";
import newUnqualifiedModel from "./new-unqualified-model.vue";
import unqualifiedLogModel from "./unqualified-log-model.vue";
let self = this;
export default {
  name: "page",
  computed: {
    access() {
      return this.$store.state.user.userData.data.access;
    },
    //保存、提交、删除（质检部）
    qualityAuth() {
      return hasOneOf(["0103060301"], this.access);
    },
    //审核(采购部)
    purchaseAuth() {
      return hasOneOf(["0103060302"], this.access);
    },
    //审核(工艺部)
    technologyAuth() {
      return hasOneOf(["0103060304"], this.access);
    },
    //审核(质检部领导)
    qualityLeaderAuth() {
      return hasOneOf(["0103060305"], this.access);
    }
  },
  components: {
    newUnqualifiedModel,
    unqualifiedLogModel
  },
  data() {
    return {
      deleteModalShow: false, //控制删除modal提示显示
      statusArray: [],
      searchCondition: {
        dateInterval: [],
        page: 1,
        rows: 10,
        reportType: 1
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
          title: "顺序号",
          key: "toolSeq",
          minWidth: 150
        },
        {
          title: "物料名称",
          key: "toolName",
          minWidth: 220
        },
         {
          title: "物料图号",
          key: "toolMap",
          minWidth: 160
        },
        {
          title: "质检人",
          key: "reporterName",
          minWidth: 120
        },
        {
          title: "质检时间",
          align: "center",
          key: "reportTime",
          width: 150,
          render: function(h, params) {
            return h(
              "div",
              dateFormat(new Date(params.row.reportTime), "yyyy-MM-dd HH:mm:ss")
            );
          }
        },
        {
          title: "报告描述",
          key: "reportDesc",
          minWidth: 180
        },
        {
          title: "当前状态",
          key: "newAuditStatus",
          align: "center",
          width: 200,
          render: function(h, params) {
            let temp = "";
            switch (params.row.newAuditStatus) {
              case 0:
                temp = "保存待提交";
                break;
              case 1:
                temp = "待采购部审核";
                break;
              case -1:
                temp = "采购部退回";
                break;
              case -2:
                temp = "采购部退回";
                break;
              case -3:
                temp = "待采购部退回";
                break;
              case 2:
                temp = "让步使用,待工艺部审批";
                break;
              case 3:
                temp = "让步使用,待质检部领导审批";
                break;
              case 4:
                temp = "让步使用,待入库";
                break;
              case 5:
                temp = "让步使用,已入库";
                break;
              default:
                break;
            }
            return h("div", temp);
          }
        },
        {
          title: "操作",
          key: "action",
          align: "center",
          width: 200,
          fixed: "right",
          render: (h, params) => {
            return h("div", [
              h(
                "Button",
                {
                  props: {
                    type: "primary",
                    size: "small"
                  },
                  style: {
                    marginRight: "5px"
                  },
                  on: {
                    click: () => {
                      this.show(params.row.pkId);
                    }
                  }
                },
                "详情"
              ),
              h(
                "Button",
                {
                  props: {
                    type: "success",
                    disabled:
                      params.row.newAuditStatus == 0 ||
                      params.row.newAuditStatus == 1,
                    size: "small"
                  },
                  style: {
                    marginRight: "5px"
                  },
                  on: {
                    click: () => {
                      this.showAuditLog(params.row.pkId);
                    }
                  }
                },
                "审核记录"
              ),
              h(
                "Button",
                {
                  props: {
                    type: "error",
                    size: "small",
                    disabled: params.row.newAuditStatus != 0
                  },
                  style: {
                    display: this.qualityAuth ? "inline-block" : "none"
                  },
                  on: {
                    click: () => {
                      this.deleteReport(params.index);
                    }
                  }
                },
                "删除"
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
      if (!self.qualityAuth) {
        if (self.purchaseAuth) {
          self.statusArray.push(1);
          self.statusArray.push(-3);
        }
        if (self.technologyAuth) {
          self.statusArray.push(2);
        }
        if (self.qualityLeaderAuth) {
          self.statusArray.push(3);
        }
      }
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
        reportType: self.searchCondition.reportType,
        toolNumber: self.searchCondition.toolNumber,
        page: self.searchCondition.page,
        rows: self.searchCondition.rows,
        beginDate: self.searchCondition.beginDate,
        endDate: self.searchCondition.endDate,
        statusArray: self.statusArray.toString()
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/unqualified-report-pagelist.json",
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
    search(name) {
      const self = this;
      self.changePage(1);
    },
    doReset(name) {
      const self = this;
      self.$refs[name].resetFields();
      self.getListData();
    },
    show(pkId) {
      const self = this;
      self.$refs.newUnqualifiedModel.showModal(pkId);
    },
    showAuditLog(pkId) {
      const self = this;
      self.$refs.unqualifiedLogModel.showModal(pkId);
    },
    deleteReport(index) {
      const self = this;
      self.reportRowData = self.tableData.rows[index];
      self.deleteModalShow = true;
    },
    deleteReportSureBtn() {
      const self = this;
      let para = {
        pkId: this.reportRowData.pkId,
        fullNumber: this.reportRowData.fullNumber
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/unqualified-report-delete-by-id.json",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          if (res.data.code === 200) {
            self.$Message.success("删除成功!");
            self.getListData();
          } else {
            self.$Notice.error({
              title: "错误提示",
              desc: res.data.info
            });
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },
    //删除modal 取消方法
    deleteReportCancelBtn() {
      this.reportRowData = {};
    }
  }
};
</script>

<style>
</style>
