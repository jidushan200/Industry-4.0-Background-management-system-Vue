<template>
  <div>
    <Card>
      <Row>
        <Form ref="formInline" :model="searchCondition" :label-width="65" inline>
          <row>
            <Col span="16">
              <FormItem label="物料类型" prop="typeId">
                <Select
                  style="width:120px"
                  v-model="searchCondition.typeId"
                  ref="typeId"
                  placeholder="请选择物料类型"
                >
                  <Option
                    v-for="item in typeList"
                    :value="item.pkId"
                    :key="item.typeNumber"
                  >{{ item.typeName }}</Option>
                </Select>
              </FormItem>
              <FormItem prop="fullNumber" label="物料条码">
                <Input type="text" v-model="searchCondition.fullNumber" placeholder="请输入物料条码"></Input>
              </FormItem>
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
                <Button type="primary" style="margin-left: -60px" @click="doSearch('formInline')">搜索</Button>
              </FormItem>
              <FormItem style="margin-left: -65px">
                <Button style type="dashed" @click="doReset('formInline')">重置</Button>
              </FormItem>
            </Col>
            <Col span="8">
              <FormItem style="width: 100%;text-align: right;">
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
    <new-check-modal ref="newCheckModal" @search="getListData"></new-check-modal>
  </div>
</template>

<script>
import axios from "@/libs/api.request";
import { getToken, getBaseUrl } from "@/libs/util";
import { dateFormat, objCopy } from "@/libs/tools.js";
import newCheckModal from "./new-check-modal.vue";
export default {
  components: {
    newCheckModal
  },
  data() {
    return {
      deleteModalShow: false, //控制删除modal提示 显示
      deleteRowData: {}, //行删除行数据
      typeList: [],
      searchCondition: {
        toolNumber: "",
        fullNumber: "",
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
          minWidth: 220
        },
        {
          title: "物料编码",
          key: "toolNumber",
          minWidth: 160
        },
        {
          title: "顺序号",
          key: "toolSeq",
          minWidth: 100
        },
        {
          title: "物料名称",
          key: "toolName",
          minWidth: 260
        },
        {
          title: "物料图号",
          key: "toolMap",
          minWidth: 160
        },
        {
          title: "物料类型",
          key: "typeName",
          minWidth: 100
        },
        {
          title: "物料数量",
          key: "toolQty",
          minWidth: 100
        },
        {
          title: "质检人",
          key: "checker",
          minWidth: 100
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
          minWidth: 160,
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
                      this.toolCheckDetail(
                        params.row.pkId,
                        params.row.checkType,
                        params.row.typeId
                      );
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
                    size: "small"
                  },
                  style: {
                    marginRight: "5px",
                    display:
                      params.row.checkStatus == 0 ? "inline-block" : "none"
                  },
                  on: {
                    click: () => {
                      this.deleteToolCheck(params.row.pkId);
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
    self.getTypeListData();
    self.getListData();
  },
  methods: {
    getTypeListData() {
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/tool-type-list",
          method: "post",
          isAuth: true
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            self.typeList = data.data;
          }
        })
        .catch(function(err) {
          console.log(err);
        });
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
        typeId: self.searchCondition.typeId,
        toolNumber: self.searchCondition.toolNumber,
        fullNumber: self.searchCondition.fullNumber,
        checkType: 1,
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
          url: "/tool/tool-check-page-list",
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
      let toolNumber = self.searchCondition.toolNumber;
      let beginDate = self.searchCondition.beginDate;
      let endDate = self.searchCondition.endDate;
      let para = "token=" + getToken() + "&checkType=1";
      if (!!toolNumber) {
        para = para + "&toolNumber=" + toolNumber;
      }
      if (!!beginDate) {
        para = para + "&beginDate=" + beginDate;
      }
      if (!!endDate) {
        para = para + "&endDate=" + endDate;
      }
      window.location.href = getBaseUrl() + "/tool/check-export?" + para;
    },

    toolCheckDetail(pkId, checkType, typeId) {
      const self = this;
      self.$refs.newCheckModal.showModal(null, pkId, 2);
    },
    deleteToolCheck(pkId) {
      const self = this;
      self.pkId = pkId;
      self.deleteModalShow = true;
    },
    deleteSureBtn() {
      const self = this;
      let para = {
        pkId: this.pkId
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/tool-check-delete-by-id.json",
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
