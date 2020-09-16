<template>
  <div>
    <Card>
      <Row>
        <Form ref="formInline" :model="searchCondition" :label-width="65" inline>
          <Col span="16">
            <FormItem label="夹具条码" prop="fixtureBarcode">
              <Input type="text" v-model="searchCondition.fixtureBarcode" placeholder="请输入夹具条码"></Input>
            </FormItem>
            <FormItem label="夹具名称" prop="fixtureName">
              <Input type="text" v-model="searchCondition.fixtureName" placeholder="请输入夹具名称"></Input>
            </FormItem>
            <FormItem label="夹具图号" prop="fixtureMap">
              <Input type="text" v-model="searchCondition.fixtureMap" placeholder="请输入夹具图号"></Input>
            </FormItem>
            <FormItem>
              <Button type="primary" style="margin-left: -60px;" @click="doSearch('formInline')">搜索</Button>
            </FormItem>
            <FormItem style="margin-left: -65px;">
              <Button style type="dashed" @click="doReset('formInline')">重置</Button>
            </FormItem>
            <Button type="warning" @click="exportData()">
              <Icon type="ios-download-outline"></Icon>导出
            </Button>
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
    <fixture-maintain-modal ref="fixtureMaintainModal" @search="getListData"></fixture-maintain-modal>
  </div>
</template>

<script>
import axios from "@/libs/api.request";
import { dateFormat, objCopy } from "@/libs/tools.js";
import { getToken, getBaseUrl } from "@/libs/util";
import fixtureMaintainModal from "./fixture-maintain-modal.vue";
export default {
  components: { fixtureMaintainModal },
  data() {
    return {
      supplierList: [],
      searchCondition: {
        fixtureBarcode: "",
        fixtureName: "",
        fixtureMap: "",
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
          minWidth: 200
        },
        {
          title: "夹具名称",
          key: "fixtureName",
          minWidth: 220
        },
        {
          title: "夹具图号",
          key: "fixtureMap",
          minWidth: 160
        },
        {
          title: "保养项目",
          key: "maintainItem",
          minWidth: 300
        },
        {
          title: "使用状态",
          key: "useStatus",
          minWidth: 120,
          render: function(h, params) {
            let temp = "";
            switch (params.row.useStatus) {
              case 1:
                temp = "正常使用";
                break;
              case 2:
                temp = "修磨";
                break;
              case 3:
                temp = "建议报废";
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
          minWidth: 120
        },
        {
          title: "记录人",
          key: "createUserName",
          minWidth: 120
        },
        {
          title: "记录时间",
          key: "updateTime",
          sortable: true,
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
  },
  methods: {
    getListData() {
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/fixture/maintain-page-list",
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
    outbound(row) {
      const self = this;
      self.$refs.fixtureMaintainModal.showModal(row);
    },
    exportData() {
      const self = this;
      let fixtureBarcode = self.searchCondition.fixtureBarcode;
      let para = "?token=" + getToken();
      if (!!fixtureBarcode) {
        para = para + "&fixtureBarcode=" + fixtureBarcode;
      }
      let fixtureNumber = self.searchCondition.fixtureNumber;    
      if (!!fixtureNumber) {
        para = para + "&fixtureNumber=" + fixtureNumber;
      }
      let fixtureName = self.searchCondition.fixtureName;    
      if (!!fixtureName) {
        para = para + "&fixtureName=" + fixtureName;
      }
      window.location.href = getBaseUrl() + "/fixture/maintain-export" + para;
    }
  }
};
</script>

<style>
</style>
