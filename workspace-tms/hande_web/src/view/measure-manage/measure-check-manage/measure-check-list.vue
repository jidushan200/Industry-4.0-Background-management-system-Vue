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
            <FormItem label="量具名称" prop="measureNumber">
              <Select
                style="width:158px"
                v-model="searchCondition.measureNumber"
                ref="measureNumber"
                placeholder="请选择量具名称"
                clearable
                filterable
              >
                <Icon type="ios-search" slot="suffix" />
                <Option
                  v-for="item in baseList"
                  :value="item.measureNumber"
                  :key="item.measureNumber"
                >{{ item.measureName }}</Option>
              </Select>
            </FormItem>
            <FormItem label="使用部门" prop="departmentId">
              <Select style="width:158px" v-model="searchCondition.departmentId" ref="department">
                <Option
                  v-for="item in departmentList"
                  :value="item.pkId"
                  :key="item.pkId"
                >{{ item.departmentName }}</Option>
              </Select>
            </FormItem>
            <FormItem>
              <Button type="primary" style="margin-left: -60px;" @click="doSearch('formInline')">搜索</Button>
            </FormItem>
            <FormItem style="margin-left: -65px;">
              <Button style type="dashed" @click="doReset('formInline')">重置</Button>
            </FormItem>
          </Col>
          <Col span="4">
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
  </div>
</template>

<script>
import axios from "@/libs/api.request";
import { dateFormat, objCopy } from "@/libs/tools.js";
import { getToken, getBaseUrl } from "@/libs/util";
export default {
  data() {
    return {
      baseList: [],
      isDisabled: false,
      measureMake: "",
      deleteRowData: {},
      isScrip: false,
      departmentList: [],
      deleteModalShow: false,
      deleteContent: "",
      searchCondition: {
        measureName: "",
        measureNumber: "",
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
          key: "measureNumber"
        },
        {
          title: "顺序号",
          minWidth: 120,
          key: "measureSeq"
        },
        {
          title: "物料名称",
          minWidth: 240,
          key: "measureName"
        },
        {
          title: "物料图号",
          minWidth: 150,
          key: "model"
        },
        {
          title: "本厂计量编码",
          minWidth: 150,
          key: "factoryMetrology"
        },
        {
          title: "领用部门",
          minWidth: 150,
          key: "useDepartmentName"
        },
        {
          title: "领用班组",
          minWidth: 100,
          key: "useTeamName"
        },
        {
          title: "送检时间",
          minWidth: 120,
          key: "deliveryTime",
          render: function(h, params) {
            if (!!params.row.deliveryTime) {
              return h(
                "div",
                dateFormat(new Date(params.row.deliveryTime), "yyyy-MM-dd")
              );
            }
          }
        },
        {
          title: "供应商名称",
          minWidth: 250,
          key: "supplierName"
        },

        {
          title: "质检类型",
          minWidth: 120,
          key: "checkType",
          render: function(h, params) {
            let statusStr = "";
            switch (params.row.checkType) {
              case 1:
                statusStr = "新量具质检";
                break;
              case 2:
                statusStr = "周期定检";
                break;
            }
            return h("div", statusStr);
          }
        },
        {
          title: "质检结果",
          minWidth: 100,
          key: "checkResult",
          render: function(h, params) {
            let statusStr = "";
            switch (params.row.checkResult) {
              case 1:
                statusStr = "合格";
                break;
              case 2:
                statusStr = "不合格";
                break;
            }
            return h("div", statusStr);
          }
        }
      ]
    };
  },
  created() {
    const self = this;
    self.getListData();
    self.getBaseListData();
    self.getDepartmentListData();
  },
  methods: {
    getDepartmentListData() {
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/base/staff-department-list",
          isAuth: true,
          method: "post"
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            self.departmentList = data.data;
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },
    getBaseListData() {
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/measure/measure-base-list",
          isAuth: true,
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
        page: self.searchCondition.page,
        rows: self.searchCondition.rows,
        departmentId: self.searchCondition.departmentId,
        measureNumber: self.searchCondition.measureNumber
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/measure/check-recode-page-list",
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
      let departmentId = self.searchCondition.departmentId;
      let measureNumber = self.searchCondition.measureNumber;
      let para = "";
      if (!!departmentId) {
        para = "&departmentId=" + departmentId;
      }
      if (!!measureNumber) {
        para = "&measureNumber=" + measureNumber;
      }
      window.location.href =
        getBaseUrl() + "/measure/measure-check-export?token=" + getToken() + para;
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
