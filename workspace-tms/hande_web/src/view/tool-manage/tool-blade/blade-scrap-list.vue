<template>
  <div>
    <Card>
      <Row>
        <Form ref="formInline" :model="searchCondition" :label-width="100" inline>
          <Col span="18">
            <FormItem prop="composeNumber" label="刀条组合条码">
              <Input type="text" v-model="searchCondition.composeNumber" placeholder="请输入刀条组合条码"></Input>
            </FormItem>
            <FormItem>
              <Button type="primary" style="margin-left: -60px" @click="doSearch('formInline')">搜索</Button>
            </FormItem>
            <FormItem style="margin-left: -65px">
              <Button type="dashed" @click="doReset('formInline')">重置</Button>
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
      baseList: [],
      searchCondition: {
        toolNumber: "",
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
          key: "composeNumber",
          minWidth: 180,
          fixed: "left"
        },
        {
          title: "刀条组合名称",
          key: "headName",
          minWidth: 150,
          fixed: "left"
        },
        {
          title: "刀条",
          align: "center",
          children: [
            {
              title: "刀条编码",
              key: "detailList",
              align: "center",
              minWidth: 120,
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
                      undefined != this.tableData.rows[params.index].scrapList
                        ? this.tableData.rows[params.index].scrapList.map(
                            item => {
                              return h("li", {}, item.toolNumber);
                            }
                          )
                        : ""
                    )
                  ]
                );
              }
            },
            {
              title: "刀条名称",
              key: "detailList",
              align: "center",
              minWidth: 200,
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
                      undefined != this.tableData.rows[params.index].scrapList
                        ? this.tableData.rows[params.index].scrapList.map(
                            item => {
                              return h("li", {}, item.toolName);
                            }
                          )
                        : ""
                    )
                  ]
                );
              }
            },
            {
              title: "报废数量",
              key: "scrapList",
              align: "center",
              width: 100,
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
                      undefined != this.tableData.rows[params.index].scrapList
                        ? this.tableData.rows[params.index].scrapList.map(
                            item => {
                              return h("li", {}, item.scrapQty);
                            }
                          )
                        : ""
                    )
                  ]
                );
              }
            }
          ]
        },
        {
          title: "组合时间",
          key: "createTime",
          minWidth: 160,
          render: function(h, params) {
            return h(
              "div",
              dateFormat(new Date(params.row.createTime), "yyyy-MM-dd HH:mm:ss")
            );
          }
        },
        {
          title: "报废时间",
          key: "createTime",
          minWidth: 160,
          render: function(h, params) {
            return h(
              "div",
              dateFormat(new Date(params.row.updateTime), "yyyy-MM-dd HH:mm:ss")
            );
          }
        }
      ]
    };
  },
  created() {
    const self = this;
    self.getListData();
    self.getBaseListData();
  },
  methods: {
    getBaseListData() {
      const self = this;
      let para = {
        typeId: 4
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/tool-base-list",
          isAuth: true,
          params: para,
          method: "post"
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200 && data.data !== null) {
            self.baseList = data.data;
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },
    getListData() {
      const self = this;
      let para = {
        toolNumber: self.searchCondition.toolNumber,
        page: self.searchCondition.page,
        rows: self.searchCondition.rows
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/blade-scrap-page-list",
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
    }
  }
};
</script>

<style>
</style>
