<template>
  <div>
    <Card>
      <Row>
        <Form ref="formInline" :model="searchCondition" :label-width="65" inline>
          <row>
            <Col span="16">
              <FormItem prop="fullNumber" label="组合条码">
                <Input type="text" v-model="searchCondition.fullNumber" placeholder="请输入组合条码" />
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
    <coat-compose-modal :tableData="tableData" ref="coatComposeModal" @search="getListData"></coat-compose-modal>
  </div>
</template>

<script>
import axios from "@/libs/api.request";
import { dateFormat, objCopy } from "@/libs/tools.js";
import { getToken, getBaseUrl } from "@/libs/util";
import coatComposeModal from "./coat-compose-modal.vue";

export default {
  components: {
    coatComposeModal
  },
  data() {
    return {
      deleteModalShow: false, // 控制删除modal提示 显示
      deleteRowData: {}, // 行删除行数据
      searchCondition: {
        fullNumber: "",
        dateInterval: [],
        page: 1,
        rows: 10,
        detailList: ""
      },
      tableData: {
        total: 0, // 总页数
        rows: [] // 每页条数
      },
      typeList: [],
      tableColumns: [
        {
          title: "组合条码",
          key: "fullNumber",
          minWidth: 160
        },
        {
          title: "组合名称",
          key: "toolName",
          minWidth: 220
        },
        {
          title: "物料编码",
          minWidth: 160,
          render: (h, params) => {
            return h(
              "div",
              {
                attrs: {
                  class: "subCol"
                }
              },
              [
                h(
                  "ul",
                  undefined != this.tableData.rows[params.index].detailList
                    ? this.tableData.rows[params.index].detailList.map(item => {
                        return h("li", {}, item.toolNumber);
                      })
                    : ""
                )
              ]
            );
          }
        },
        {
          title: "物料名称",
          minWidth: 260,
          render: (h, params) => {
            return h(
              "div",
              {
                attrs: {
                  class: "subCol"
                }
              },
              [
                h(
                  "ul",
                  undefined != this.tableData.rows[params.index].detailList
                    ? this.tableData.rows[params.index].detailList.map(item => {
                        return h("li", {}, item.toolName);
                      })
                    : ""
                )
              ]
            );
          }
        },
        {
          title: "物料数量",
          minWidth: 100,
          render: (h, params) => {
            return h(
              "div",
              {
                attrs: {
                  class: "subCol"
                }
              },
              [
                h(
                  "ul",
                  undefined != this.tableData.rows[params.index].detailList
                    ? this.tableData.rows[params.index].detailList.map(item => {
                        return h("li", {}, item.coatQty);
                      })
                    : ""
                )
              ]
            );
          }
        },
        {
          title: "送涂时间",
          key: "setCheckTime",
          minWidth: 140,
          render: function(h, params) {
            return h(
              "div",
              dateFormat(new Date(params.row.setCheckTime), "yyyy-MM-dd HH:mm")
            );
          }
        },
        {
          title: "供应商",
          key: "supplierName",
          minWidth: 180
        },
        {
          title: "检验",
          key: "action",
          minWidth: 120,
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
                      this.toolCheck(params.row);
                    }
                  }
                },
                "检验"
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
      if (self.searchCondition.dateInterval) {
        let beginDate = self.searchCondition.dateInterval[0];
        let endDate = self.searchCondition.dateInterval[1];
        if (beginDate) {
          self.searchCondition.beginDate = dateFormat(
            new Date(beginDate),
            "yyyy-MM-dd"
          );
        } else {
          self.searchCondition.beginDate = null;
        }
        if (endDate) {
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
        checkType: 8,
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
          url: "/tool/compose-wait-check-page-list",
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

    toolCheck(row) {
      const self = this;
      self.$refs.coatComposeModal.showModal(row, null, row.toolName);
    }
  }
};
</script>

<style>
.subCol > ul > li {
  margin: 0 -18px;
  /* list-style: none;*/
  text-align: left;
  padding: 9px;
  padding-left: 18px;
  border-bottom: 1px solid #ccc;
  overflow-x: hidden;
}

.subCol > ul > li:last-child {
  border-bottom: none;
}
</style>
