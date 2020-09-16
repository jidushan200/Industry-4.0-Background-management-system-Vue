<template>
  <div>
    <Card>
      <Row>
        <Form ref="formInline" :model="searchCondition" :label-width="65" inline>
          <row>
            <Col span="16">
              <FormItem prop="fixtureName" label="夹具名称">
                <Input type="text" v-model="searchCondition.fixtureName" placeholder="请输入夹具名称" />
              </FormItem>
              <FormItem prop="fixtureMap" label="夹具图号">
                <Input type="text" v-model="searchCondition.fixtureMap" placeholder="请输入夹具图号" />
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
                <Button type="primary" style="margin-left: -60px" @click="doSearch('formInline')">搜索</Button>
              </FormItem>
              <FormItem style="margin-left: -65px">
                <Button style type="dashed" @click="doReset('formInline')">重置</Button>
              </FormItem>
            </Col>
            <Col span="8">
              <FormItem style="width:100%;text-align:right">
                <Button type="warning" @click="exportData()">
                  <Icon type="ios-download-outline"></Icon>导出
                </Button>
              </FormItem>
            </Col>
          </row>
        </Form>
      </Row>

      <Table
        ref="tablesMain"
        :data="tableData.rows"
        :columns="tableColumns"
        border
        stripe
        highlight-row
      ></Table>
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
    <Modal v-model="deleteModalShow" width="250" title="删除质检信息" @on-ok="deleteSureBtn">
      <p>确定删除所选质检信息？</p>
    </Modal>
    <spot-check-modal ref="spotCheckModal" @search="getListData"></spot-check-modal>
  </div>
</template>

<script>
import axios from "@/libs/api.request";
import { getToken, getBaseUrl } from "@/libs/util";
import { dateFormat, objCopy } from "@/libs/tools.js";
import spotCheckModal from "./spot-check-modal.vue";
export default {
  components: { spotCheckModal },
  data() {
    return {
      deleteModalShow: false, //控制删除modal提示 显示
      deleteRowData: {}, //行删除行数据
      searchCondition: {
        fixtureName: "",
        fixtureMap: "",
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
          title: "夹具编码",
          key: "fixtureNumber",
          minWidth: 160
        },
        {
          title: "夹具条码",
          key: "fixtureBarcode",
          minWidth: 260
        },
        {
          title: "夹具图号",
          key: "fixtureMap",
          minWidth: 160
        },
        {
          title: "夹具名称",
          key: "fixtureName",
          minWidth: 220
        },
        {
          title: "质检人",
          key: "checker",
          minWidth: 160
        },
        {
          title: "检验结果",
          key: "checkResult",
          minWidth: 100,
          render: function(h, params) {
            let temp = "";
            switch (params.row.checkResult) {
              case 1:
                temp = "合格";
                break;
              case 2:
                temp = "不合格";
                break;
              default:
                break;
            }
            return h("div", temp);
          }
        },
        {
          title: "备注",
          key: "remark",
          minWidth: 150
        },
        {
          title: "检验时间",
          key: "checkTime",
          minWidth: 150,
          render: function(h, params) {
            return h(
              "div",
              dateFormat(new Date(params.row.checkTime), "yyyy-MM-dd HH:mm:ss")
            );
          }
        },
        {
          title: "单据状态",
          key: "checkStatus",
          minWidth: 100,
          render: function(h, params) {
            let temp = "";
            switch (params.row.checkStatus) {
              case 0:
                temp = "暂存";
                break;
              case 1:
                temp = "提交";
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
          minWidth: 200,
          align: "center",
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
                      this.fixtureCheckDetail(params.row.pkId);
                    }
                  }
                },
                "详情"
              ),
              h(
                "Button",
                {
                  props: {
                    type: "error",
                    size: "small",
                    disabled: params.row.checkStatus != 0
                  },
                  on: {
                    click: () => {
                      this.deleteFixtureCheck(
                        params.row.pkId,
                        params.row.checkType
                      );
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
        fixtureName: self.searchCondition.fixtureName,
        checkType: 6,
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
          url: "/fixture/fixture-check-page-list",
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
      self.searchCondition.page = 1;
      self.changePage(1);
    },
    doReset(name) {
      const self = this;
      self.$refs[name].resetFields();
      self.getListData();
    },

    fixtureCheckDetail(pkId) {
      const self = this;
      self.$refs.spotCheckModal.showModal(null, pkId, 2);
    },
    deleteFixtureCheck(pkId, checkType) {
      const self = this;
      self.pkId = pkId;
      self.checkType = checkType;
      self.deleteModalShow = true;
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
      let para = "token=" + getToken() + "&checkType=6";
      if (!!self.searchCondition.beginDate) {
        para = para + "&beginDate=" + self.searchCondition.beginDate;
      }
      if (!!self.searchCondition.endDate) {
        para = para + "&endDate=" + self.searchCondition.endDate;
      }

      if (!!self.searchCondition.fixtureName) {
        para = para + "&fixtureName=" + self.searchCondition.fixtureName;
      }
      if (!!self.searchCondition.fixtureMap) {
        para = para + "&fixtureMap=" + self.searchCondition.fixtureMap;
      }
      window.location.href =
        getBaseUrl() + "/fixture/fixture-check-export?" + para;
    },
    deleteSureBtn() {
      const self = this;
      let para = {
        pkId: self.pkId,
        checkType: self.checkType
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/fixture/fixture-check-delete-by-id.json",
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
    }
  }
};
</script>
<style>
</style>
