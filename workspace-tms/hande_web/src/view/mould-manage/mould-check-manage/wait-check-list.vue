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
          <Col span="18">
            <FormItem prop="mouldBarcode" label="模具条码">
              <Input type="text" v-model="searchCondition.mouldBarcode" placeholder="请输入模具条码"></Input>
            </FormItem>
            <FormItem prop="mouldNumber" label="模具编码">
              <Input type="text" v-model="searchCondition.mouldNumber" placeholder="请输入模具编码"></Input>
            </FormItem>
            <FormItem prop="mouldName" label="模具名称">
              <Input type="text" v-model="searchCondition.mouldName" placeholder="请输入模具名称"></Input>
            </FormItem>
            <FormItem prop="dateInterval" label="时间区间">
              <DatePicker
                type="daterange"
                split-panels
                placeholder="请选择时间区间"
                placement="bottom-end"
                v-model="searchCondition.dateInterval"
              ></DatePicker>
            </FormItem>
            <FormItem>
              <Button type="primary" style="margin-left: -60px;" @click="doSearch('formInline')">搜索</Button>
            </FormItem>
            <FormItem style="margin-left: -65px;">
              <Button style type="dashed" @click="doReset('formInline')">重置</Button>
            </FormItem>
          </Col>
          <Col span="6">
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
    <Modal
      v-model="deleteModalShow"
      width="750"
      :title="isScrip?'模具报废':'模具退回'"
      @on-ok="deleteModalSureBtn"
      @on-cancel="deleteModalCancelBtn"
    >
      <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="120">
        <Row>
          <Col span="16">
            <FormItem prop="remark" label="不合格原因">
              <Input type="text" v-model="formValidate.remark" placeholder="请输入不合格原因"></Input>
            </FormItem>
          </Col>
          <Col span="8">
            <FormItem prop="measureMake" label="处理措施">
              <Input type="text" v-model="measureMake" readonly></Input>
            </FormItem>
          </Col>
        </Row>
      </Form>
    </Modal>
    <check-quality-model ref="checkQualityModel" @search="doSearch"></check-quality-model>
  </div>
</template>

<script>
import axios from "@/libs/api.request";
import { dateFormat, objCopy } from "@/libs/tools.js";
import checkQualityModel from "./check-quality-model.vue";
import apply_form_config from "./apply-form-config.js";
import { getToken, getBaseUrl } from "@/libs/util";
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
      searchCondition: {
        mouldName: "",
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
          title: "物料编码",
          minWidth: 150,
          fixed: "left",
          key: "mouldNumber"
        },
        {
          title: "物料序号",
          width: 100,
          key: "mouldSeq"
        },
        {
          title: "物料名称",
          minWidth: 180,
          key: "mouldName"
        },
        {
          title: "物料图号",
          minWidth: 120,
          key: "mouldMap"
        },
        {
          title: "热处理批次号",
          key: "heatNumber",
          minWidth: 120
        },
        {
          title: "表面处理批次号",
          key: "surfaceNumber",
          minWidth: 120
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
          title: "操作",
          key: "action",
          width: 100,
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
                      this.doCheck(params.row);
                    }
                  }
                },
                "质检"
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
        mouldBarcode: self.searchCondition.mouldBarcode,
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
          url: "/mould/mould-check-page-list",
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
      let beginDate = self.searchCondition.beginDate;
      let endDate = self.searchCondition.endDate;
      let mouldBarcode = self.searchCondition.mouldBarcode;
      let mouldNumber = self.searchCondition.mouldNumber;
      let mouldName = self.searchCondition.mouldName;

      if (!!mouldBarcode) {
        para = para + "&mouldBarcode=" + mouldBarcode;
      }
      if (!!mouldNumber) {
        para = para + "&mouldNumber=" + mouldNumber;
      }
      if (!!mouldName) {
        para = para + "&mouldName=" + mouldName;
      }
      if (!!beginDate) {
        para = para + "&beginDate=" + beginDate;
      }
      if (!!endDate) {
        para = para + "&endDate=" + endDate;
      }
      window.location.href = getBaseUrl() + "/mould/mould-check-export?" + para;
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
    doCheck(row) {
      const self = this;
      self.$refs.checkQualityModel.showModal(row);
    },
    deleteModalSureBtn() {
      this.checkUnqualified(this.deleteRowData);
    },
    //删除modal 取消方法
    deleteModalCancelBtn() {
      this.deleteRowData = {};
    }
  }
};
</script>

<style>
</style>
