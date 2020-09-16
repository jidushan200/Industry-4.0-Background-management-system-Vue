<style lang="less">
.fsize {
  font-size: 18px !important;
}
</style>
<template>
  <div>
    <Card>
      <Row>
        <Form ref="formInline" :model="searchCondition" :label-width="65" inline>
          <Col span="20">
            <FormItem prop="toolNumber" label="物料编码">
              <Input type="text" v-model="searchCondition.toolNumber" placeholder="请输入物料编码"></Input>
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
      v-model="toCheckModalShow"
      width="300"
      title="刀条送检确认"
      @on-ok="toCheckModalSureBtn"
      @on-cancel="toCheckModalCancelBtn"
    >
      <p>确定刀具已完成刃磨，并送质检？</p>
    </Modal>
  </div>
</template>

<script>
import axios from "@/libs/api.request";
import { dateFormat, objCopy } from "@/libs/tools.js";

export default {
  data() {
    return {
      toCheckModalShow: false,
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
          title: "刀盘组合码",
          fixed: "left",
          minWidth: 160,
          key: "composeNumber"
        },
        {
          title: "物料编码",
          fixed: "left",
          minWidth: 150,
          key: "toolNumber"
        },
        {
          title: "物料名称",
          fixed: "left",
          minWidth: 240,
          key: "toolName"
        },
        {
          title: "物料图号",
          minWidth: 200,
          key: "toolMap"
        },
        {
          title: "数量",
          minWidth: 100,
          key: "toolQty"
        },
        {
          title: "是否新刀",
          minWidth: 100,
          key: "isNew",
          render: function(h, params) {
            let statusStr = "";
            switch (params.row.isNew) {
              case 1:
                statusStr = "是";
                break;
              case 0:
                statusStr = "否";
                break;
            }
            return h("div", statusStr);
          }
        },
        {
          title: "送磨时间",
          minWidth: 160,
          key: "createTime",
          render: function(h, params) {
            return h(
              "div",
              dateFormat(new Date(params.row.createTime), "yyyy-MM-dd HH:mm:ss")
            );
          }
        },
        {
          title: "刃磨时间",
          minWidth: 160,
          key: "updateTime",
          render: function(h, params) {
            return h(
              "div",
              dateFormat(new Date(params.row.updateTime), "yyyy-MM-dd HH:mm:ss")
            );
          }
        },
        {
          title: "操作",
          key: "action",
          width: 160,
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
    //self.getStaffListData();
  },
  methods: {
    getListData() {
      const self = this;
      let para = {
        toolNumber: self.searchCondition.toolNumber,
        handleResult: 1,
        page: self.searchCondition.page,
        rows: self.searchCondition.rows
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/blade-wait-repair-page-list",
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
      self.changePage(1);
    },
    doReset(name) {
      const self = this;
      self.$refs[name].resetFields();
      self.getListData();
    },
    setCheck(pkId) {
      const self = this;
      self.pkId = pkId;
      self.toCheckModalShow = true;
    },
    toCheckModalSureBtn() {
      const self = this;
      let para = {
        pkId: self.pkId
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/blade-set-check",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          self.toCheckModalShow = false;
          if (res.data.code === 200) {
            self.$Message.success("刀条已送检!");
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
    toCheckModalCancelBtn() {
      this.toCheckModalShow = false;
    }
  }
};
</script>

<style>
</style>
