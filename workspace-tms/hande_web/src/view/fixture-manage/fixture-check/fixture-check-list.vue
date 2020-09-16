<template>
  <div>
    <Card>
      <Row>
        <Form ref="formInline" :model="searchCondition" :label-width="65" inline>
          <row>
            <Col span="16">
              <FormItem prop="fixtureNumber" label="夹具编码">
                <Input type="text" v-model="searchCondition.fixtureNumber" placeholder="请输入夹具编码" />
              </FormItem>
              <FormItem label="夹具名称" prop="fixtureName">
                <Input type="text" v-model="searchCondition.fixtureName" placeholder="请输入夹具名称"></Input>
              </FormItem>
              <FormItem prop="fixtureMap" label="夹具图号">
                <Input type="text" v-model="searchCondition.fixtureMap" placeholder="请输入夹具图号" />
              </FormItem>
              <FormItem prop="checkType" label="检验类型">
                <Select
                  style="width:158px"
                  v-model="searchCondition.checkType"
                  ref="checkType"
                  placeholder="请选择检验类型"
                >
                  <Option
                    v-for="item in checkTypeList"
                    :value="item.value"
                    :key="item.value"
                  >{{ item.name }}</Option>
                </Select>
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
    <Modal
      v-model="setRepairModalShow"
      width="250"
      title="送修磨"
      @on-ok="setRepairModalSureBtn"
      @on-cancel="setRepairModalCancelBtn"
    >
      <p>确定送修所选夹具？</p>
    </Modal>
    <Modal v-model="goBackModalShow" width="250" title="质检返回" @on-ok="goBackModalSureBtn">
      <p>确定质检完成并返回？</p>
    </Modal>
    <scrip-apply-modal ref="scripApplyModal" @search="getListData"></scrip-apply-modal>
    <new-check-modal ref="newCheckModal" @search="getListData"></new-check-modal>
    <repair-check-modal ref="repairCheckModal" @search="getListData"></repair-check-modal>
  </div>
</template>

<script>
import axios from "@/libs/api.request";
import { dateFormat, objCopy } from "@/libs/tools.js";
import { getToken, getBaseUrl } from "@/libs/util";
import checkTypeList from "./check-type.js";
import newCheckModal from "./new-check-modal.vue";
import repairCheckModal from "./repair-check-modal.vue";
import scripApplyModal from "./scrip-apply-modal.vue";
export default {
  components: {
    newCheckModal,
    repairCheckModal,
    scripApplyModal
  },
  data() {
    return {
      deleteModalShow: false, //控制删除modal提示 显示
      setRepairModalShow: false,
      goBackModalShow: false,

      deleteRowData: {}, //行删除行数据
      checkTypeList: checkTypeList,
      searchCondition: {
        fixtureNumber: "",
        fixtureMap: "",
        fixtureName: "",
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
          minWidth: 140
        },

        {
          title: "夹具条码",
          key: "fixtureBarcode",
          minWidth: 160
        },
        {
          title: "夹具名称",
          key: "fixtureName",
          minWidth: 300
        },
        {
          title: "检验类型",
          key: "checkType",
          minWidth: 110,
          render: function(h, params) {
            let temp = "";
            switch (params.row.checkType) {
              case 4:
                temp = "新夹具检验";
                break;
              case 5:
                temp = "修磨检验";
                break;
              case 6:
                temp = "夹具点检";
                break;
              default:
                break;
            }
            return h("div", temp);
          }
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
                      this.fixtureCheckDetail(params.row);
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
                    size: "small"
                  },
                  style: {
                    marginRight: "5px",
                    display:
                      params.row.checkType == 5 &&
                      params.row.handleResult == 0 &&
                      params.row.checkResult == 1
                        ? "inline-block"
                        : "none"
                  },
                  on: {
                    click: () => {
                      this.goBack(params.row.pkId, params.row.fixtureBarcode);
                    }
                  }
                },
                "返库"
              ),
              h(
                "Button",
                {
                  props: {
                    type: "info",
                    size: "small"
                  },
                  style: {
                    marginRight: "5px",
                    display:
                      params.row.checkType == 5 &&
                      params.row.handleResult == 0 &&
                      params.row.checkResult == 2
                        ? "inline-block"
                        : "none"
                  },
                  on: {
                    click: () => {
                      this.setRepair(
                        params.row.pkId,
                        params.row.fixtureBarcode
                      );
                    }
                  }
                },
                "重修"
              ),
              h(
                "Button",
                {
                  props: {
                    type: "warning",
                    size: "small"
                  },
                  style: {
                    marginRight: "5px",
                    display:
                      params.row.checkType == 5 &&
                      params.row.handleResult == 0 &&
                      params.row.checkResult == 2
                        ? "inline-block"
                        : "none"
                  },
                  on: {
                    click: () => {
                      this.scripApply(params.row);
                    }
                  }
                },
                "申请报废"
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
        fixtureNumber: self.searchCondition.fixtureNumber,
        fixtureName: self.searchCondition.fixtureName,
        fixtureMap: self.searchCondition.fixtureMap,
        checkType: self.searchCondition.checkType,
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

    fixtureCheckDetail(row) {
      const self = this;
      if (row.checkType == 4) {
        self.$refs.newCheckModal.showModal(null, row.pkId, 2);
      } else if (row.checkType == 5) {
        self.$refs.repairCheckModal.showModal(null, row.pkId, 2);
      }
    },
    deleteFixtureCheck(pkId, checkType) {
      const self = this;
      self.pkId = pkId;
      self.checkType = checkType;
      self.deleteModalShow = true;
    },

    scripApply(row) {
      const self = this;
      self.$refs.scripApplyModal.showModal(row);
    },

    setRepair(pkId, fixtureBarcode) {
      const self = this;
      self.checkId = pkId;
      self.fixtureBarcode = fixtureBarcode;
      self.setRepairModalShow = true;
    },
    setRepairModalSureBtn() {
      const self = this;
      let para = {
        //pkId: self.pkId,
        checkId: self.checkId,
        fixtureBarcode: self.fixtureBarcode
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/fixture/set-repair",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          self.setRepairModalShow = false;
          if (res.data.code === 200) {
            self.$Message.success("夹具送修磨成功!");
            self.getListData();
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },
    setRepairModalCancelBtn() {},
    //质检完成返库
    goBack(pkId, fixtureBarcode) {
      const self = this;
      self.pkId = pkId;
      self.fixtureBarcode = fixtureBarcode;
      self.goBackModalShow = true;
    },
    goBackModalSureBtn() {
      const self = this;
      let para = {
        checkId: self.pkId,
        fixtureBarcode: self.fixtureBarcode
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/fixture/go-back",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          self.goBackModalShow = false;
          if (res.data.code === 200) {
            self.$Message.success("夹具返库成功!");
            self.getListData();
          }
        })
        .catch(function(err) {
          console.log(err);
        });
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
      if (!!self.searchCondition.beginDate) {
        para = para + "&beginDate=" + self.searchCondition.beginDate;
      }
      if (!!self.searchCondition.endDate) {
        para = para + "&endDate=" + self.searchCondition.endDate;
      }
      if (!!self.searchCondition.checkType) {
        para = para + "&checkType=" + self.searchCondition.checkType;
      }
      if (!!self.searchCondition.fixtureNumber) {
        para = para + "&fixtureNumber=" + self.searchCondition.fixtureNumber;
      }
      window.location.href =
        getBaseUrl() + "/fixture/fixture-check-export?" + para;
    }
  }
};
</script>
<style>
</style>
