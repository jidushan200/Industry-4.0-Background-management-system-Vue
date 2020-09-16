<template>
  <div>
    <Card>
      <Row>
        <Form ref="formInline" :model="searchCondition" :label-width="65" inline>
          <Col span="20">
            <FormItem prop="mouldBarcode" label="模具条码">
              <Input type="text" v-model="searchCondition.mouldBarcode" placeholder="请输入模具条码"></Input>
            </FormItem>
            <FormItem prop="mouldNumber" label="模具编码">
              <Input type="text" v-model="searchCondition.mouldNumber" placeholder="请输入模具编码"></Input>
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
    <check-quality-model ref="checkQualityModel" @search="doSearch"></check-quality-model>
  </div>
</template>

<script>
import axios from "@/libs/api.request";
import { dateFormat, objCopy } from "@/libs/tools.js";
import checkQualityModel from "./check-quality-model.vue";

export default {
  components: {
    checkQualityModel
  },
  data() {
    return {
      searchCondition: {
        mouldNumber: "",
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
          title: "模具编码",
          key: "mouldNumber",
          minWidth: 130
        },
        {
          title: "顺序号",
          key: "mouldSeq",
          minWidth: 100
        },
        {
          title: "模具图号",
          key: "mouldMap",
          minWidth: 140
        },
        {
          title: "模具名称",
          key: "mouldName",
          minWidth: 200
        },
        {
          title: "供应商",
          key: "supplierName",
          minWidth: 200
        },
        {
          title: "质检类型",
          key: "checkType",
          width: 100,
          render: function(h, params) {
            let statusStr = "";
            switch (params.row.checkType) {
              case 1:
                statusStr = "新模具质检";
                break;
              case 2:
                statusStr = "修磨质检";
                break;
            }
            return h("div", statusStr);
          }
        },
        {
          title: "质检时间",
          key: "checkTime",
          width: 160,
          render: function(h, params) {
            return h(
              "div",
              dateFormat(new Date(params.row.checkTime), "yyyy-MM-dd HH:mm:ss")
            );
          }
        },
        {
          title: "质检人",
          key: "checker",
          width: 100
        },
        {
          title: "操作",
          key: "action",
          width: 100,
          align: "center",
          fixed: "right",
          render: (h, params) => {
            /**
             * render写法 添加按钮
             */
            const self = this;
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
                      this.show(params.row);
                    }
                  }
                },
                "质检详情"
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
        mouldNumber: self.searchCondition.mouldNumber,
        mouldBarcode:self.searchCondition.mouldBarcode,
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
          url: "/mould/mould-checked-page-list",
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
    show(row) {
      const self = this;
      self.$refs.checkQualityModel.showReportModal(row);
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
    }
  }
};
</script>

<style>
</style>
