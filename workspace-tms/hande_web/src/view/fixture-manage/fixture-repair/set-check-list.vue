<template>
  <div>
    <Card>
      <Row>
        <Form ref="formInline" :model="searchCondition" :label-width="65" inline>
          <Col span="16">
            <FormItem label="夹具条码" prop="fixtureBarcode">
              <Input type="text" v-model="searchCondition.fixtureBarcode" placeholder="请输入夹具条码"></Input>
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
    <Modal
      v-model="setCheckModalShow"
      width="250"
      title="送修磨"
      @on-ok="setCheckModalSureBtn"
      @on-cancel="setCheckModalCancelBtn"
    >
      <p>确定送检所选夹具？</p>
    </Modal>
  </div>
</template>

<script>
import axios from "@/libs/api.request";
import { dateFormat, objCopy } from "@/libs/tools.js";
import { getToken, getBaseUrl } from "@/libs/util";
export default {
  components: {
  },
  data() {
    return {
      setCheckModalShow:false,
      searchCondition: {
        fixtureStatus: 3,
        fixtureBarcode: "",
        page: 1,
        rows: 10
      },
      tableData: {
        total: 0, // 总页数
        rows: [] // 每页条数
      },
      tableColumns: [
        {
          title: "夹具条码",
          key: "fixtureBarcode",
          minWidth: 180
        },
        {
          title: "夹具名称",
          key: "fixtureName",
          minWidth: 220
        },
        {
          title: "部门",
          key: "departmentName",
          minWidth: 220
        },
        {
          title: "班组",
          key: "teamName",
          minWidth: 220
        },
        {
          title: "送修磨时间",
          key: "updateTime",
          minWidth: 160,
          render: function(h, params) {
            return h(
              "div",
              dateFormat(
                new Date(params.row.updateTime),
                "yyyy-MM-dd HH:mm:ss"
              )
            );
          }
        },
        {
          title: "操作",
          key: "action",
          width: 200,
          align: "center",
          fixed: "right",
          render: (h, params) => {
            /**
             * render写法 添加按钮
             */
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
                      this.setCheck(params.row.pkId);
                    }
                  }
                },
                "送检"
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
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/fixture/use-page-list",
          method: "post",
          isAuth: true,
          params: self.searchCondition
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
      self.changePage(1);
    },
    doReset(name) {
      const self = this;
      self.$refs[name].resetFields();
      self.changePage(1);
    },
     setCheck(pkId) {
      const self = this;
      self.setCheckId = pkId;
      self.setCheckModalShow = true;
    },
    setCheckModalCancelBtn(){

    },
    setCheckModalSureBtn() {
      const self = this;
      let para = {
        pkId: self.setCheckId,
        fixtureStatus: 4,
        oper: "setCheck"
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/fixture/fixture-oper",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          self.setCheckModalShow = false;
          if (res.data.code === 200) {
            self.$Message.success("夹具送检成功!");
            self.getListData();
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
