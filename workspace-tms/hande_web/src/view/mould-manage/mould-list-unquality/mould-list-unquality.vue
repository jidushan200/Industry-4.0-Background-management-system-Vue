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
            <FormItem prop="mouldName" label="模具名称">
              <Input type="text" v-model="searchCondition.mouldName" placeholder="请输入模具名称"></Input>
            </FormItem>
            <FormItem prop="mouldNumber" label="模具编码">
              <Input type="text" v-model="searchCondition.mouldNumber" placeholder="请输入模具编码"></Input>
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
import apply_form_config from "./apply-form-config.js";
import checkQualityModel from "./check-quality-model.vue";

export default {
  components: {
    checkQualityModel
  },
  data() {
    return {
      measureMake: "",
      deleteRowData: {},
      isScrip: false,
      deleteModalShow: false,
      deleteContent: "",
      applierCode: "",
      departmentId: "",
      departmentName: "",
      searchCondition: {
        mouldName: "",
        mouldNumber: "",
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
          minWidth: 140,
          key: "mouldNumber"
        },
        {
          title: "模具名称",
          minWidth: 300,
          key: "mouldName"
        },
        {
          title: "模具序号",
          minWidth: 100,
          key: "mouldSeq"
        },
        {
          title: "模具图号",
          minWidth: 150,
          key: "mouldMap"
        },
        {
          title: "自检报告",
          key: "selfCheckReport",
          minWidth: 100,
          render: function(h, params) {
            let statusStr = "";
            switch (params.row.selfCheckReport) {
              case 1:
                statusStr = "有";
                break;
              case 2:
                statusStr = "无";
                break;
            }
            return h("div", statusStr);
          }
        },
        {
          title: "质检类型",
          key: "checkType",
          minWidth: 100,
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
          title: "质检人",
          minWidth: 100,
          key: "checker"
        },
        {
          title: "质检时间",
          minWidth: 160,
          key: "checkTime",
          render: function(h, params) {
            if (!!params.row.checkTime) {
              return h(
                "div",
                dateFormat(
                  new Date(params.row.checkTime),
                  "yyyy-MM-dd HH:mm:ss"
                )
              );
            }
          }
        },
        {
          title: "操作",
          key: "action",
          minWidth: 100,
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
                      this.checkReport(params.row);
                    }
                  }
                },
                "质检记录"
              )
            ]);
          }
        }
      ],
      formValidate: apply_form_config.formValidate, //form表单字段
      ruleValidate: apply_form_config.ruleValidate //form表单验证规则
    };
  },
  created() {
    const self = this;
    self.getListData();
    self.searchStaff();
  },
  methods: {
    searchStaff() {
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/base/staff-get-current.json",
          method: "post",
          isAuth: true
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            if (data.data !== null && data.data !== undefined) {
              self.applierCode = data.data.staffCode;
              self.departmentId = data.data.departmentId;
              self.departmentName = data.data.departmentName;
            } else {
              self.$Message.error("无法找到与职工号对应的职工");
            }
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
        mouldName: self.searchCondition.mouldName,
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
          url: "/mould/check-unquality-page-list",
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
    checkReport(row) {
      const self = this;
      self.$refs.checkQualityModel.showModal(row);
    }
  }
};
</script>

<style>
</style>
