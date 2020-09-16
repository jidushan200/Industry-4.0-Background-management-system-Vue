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
            <FormItem label="夹具图号" prop="fixtureBarcode">
              <Input type="text" v-model="searchCondition.fixtureMap" placeholder="请输入夹具图号"></Input>
            </FormItem>
            <FormItem>
              <Button type="primary" style="margin-left: -60px;" @click="doSearch('formInline')">搜索</Button>
            </FormItem>
            <FormItem style="margin-left: -65px;">
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
    <fixture-repair-modal ref="fixtureRepairModal" @search="getListData"></fixture-repair-modal>
  </div>
</template>

<script>
import axios from "@/libs/api.request";
import { dateFormat, objCopy } from "@/libs/tools.js";
import { getToken, getBaseUrl } from "@/libs/util";
import fixtureRepairModal from "./fixture-repair-modal.vue";
export default {
  components: {
    fixtureRepairModal
  },
  data() {
    return {
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
          minWidth: 180
        },
        {
          title: "夹具图号",
          key: "fixtureMap",
          minWidth: 180
        },
        {
          title: "夹具名称",
          key: "fixtureName",
          minWidth: 220
        },
        {
          title: "修磨量",
          key: "repairMeasure",
          minWidth: 100
        },
        {
          title: "备注",
          key: "remark",
          minWidth: 220
        },
        {
          title: "修磨人",
          key: "createUserName",
          minWidth: 120
        },
        {
          title: "修磨时间",
          key: "createTime",
          minWidth: 160,
          render: function(h, params) {
            return h(
              "div",
              dateFormat(new Date(params.row.createTime), "yyyy-MM-dd HH:mm:ss")
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
          url: "/fixture/repair-page-list",
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
    repair(row) {
      const self = this;
      self.$refs.fixtureRepairModal.showModal(row);
    },
    exportData() {
      const self = this;
      let fixtureBarcode = self.searchCondition.fixtureBarcode;
      let fixtureName = self.searchCondition.fixtureName;
      let fixtureMap = self.searchCondition.fixtureMap;
      let para = "token=" + getToken();
      if (!!fixtureBarcode) {
        para = para + "&fixtureBarcode=" + fixtureBarcode;
      }
      if (!!fixtureName) {
        para = para + "&fixtureName=" + fixtureName;
      }
      if (!!fixtureMap) {
        para = para + "&fixtureMap=" + fixtureMap;
      }
      window.location.href =
        getBaseUrl() + "/fixture/fixture-repair-export?" + para;
    }
  }
};
</script>

<style>
</style>
