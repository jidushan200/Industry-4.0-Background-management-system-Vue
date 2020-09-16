<template>
  <div>
    <Card>
      <Row>
        <Form ref="formInline" :model="searchCondition" inline :label-width="65">
          <Col span="24">
            <FormItem prop="measureNumber" label="量具编码">
              <Input type="text" v-model="searchCondition.measureNumber" placeholder="请输入量具编码"></Input>
            </FormItem>
            <FormItem prop="dateInterval" label="时间区间">
              <DatePicker
                split-panels
                type="daterange"
                placeholder="请选择时间区间"
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
export default {
  data() {
    return {
      searchCondition: {
        toolNumber: "",
        purchaseType: "",
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
          key: "measureNumber",
          minWidth: 150
        },
        {
          title: "物料名称",
          key: "measureName",
          minWidth: 150
        },
        {
          title: "型号规格",
          key: "model",
          minWidth: 150
        },
        {
          title: "申请部门",
          key: "applyDepartmentName",
          minWidth: 150
        },
        {
          title: "申请人",
          key: "applierName",
          minWidth: 120
        },
        {
          title: "申购时间",
          key: "applyTime",
          sortable: true,
          minWidth: 200,
          render: function(h, params) {
            return h(
              "div",
              dateFormat(new Date(params.row.applyTime), "yyyy-MM-dd HH:mm:ss")
            );
          }
        },
        {
          title: "申购数量",
          key: "applyQty",
          minWidth: 120
        },
        {
          title: "需求时间",
          key: "demandTime",
          sortable: true,
          minWidth: 120,
          render: function(h, params) {
            return h(
              "div",
              dateFormat(new Date(params.row.demandTime), "yyyy-MM-dd")
            );
          }
        },
        {
          title: "采购原因",
          minWidth: 120,
          key: "purchaseReasion",
          render: function(h, params) {
            let statusStr = "";
            switch (params.row.purchaseReasion) {
              case 1:
                statusStr = "产量提升";
                break;
              case 2:
                statusStr = "量具报废";
                break;
              case 3:
                statusStr = "产品开发";
                break;
              case 4:
                statusStr = "其他";
                break;
            }
            return h("div", statusStr);
          }
        },
        {
          title: "申请状态",
          key: "applyStatus",
          minWidth: 200,
          render: function(h, params) {
            let statusStr = "";
            switch (params.row.applyStatus) {
              case 0:
                statusStr = "待提交";
                break;
              case 1:
                statusStr = "待分厂领导审核";
                break;
              case -1:
                statusStr = "分厂领导未通过，已驳回";
                break;
              case 2:
                statusStr = "待装备车间判断";
                break;
              case 3:
                statusStr = "装备车间自制";
                break;
              case 4:
                statusStr = "待采购部补充外购价格";
                break;
              case -4:
                statusStr = "采购部驳回装备车间";
                break;
              case 5:
                statusStr = "价格偏高，待主管领导审核";
                break;
              case -5:
                statusStr = "主管领导驳回";
                break;
              case 6:
                statusStr = "待采购部接收";
                break;
              case 7:
                statusStr = "采购部已接收";
                break;
              case 8:
                statusStr = "采购部到货确认，采购完成";
                break;
            }
            return h("div", statusStr);
          }
        },
        {
          title: "审核人",
          key: "auditorName",
          align: "center",
          minWidth: 120
        },
        {
          title: "审核结果",
          key: "auditResult",
          align: "center",
          minWidth: 200
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
        measureNumber: self.searchCondition.measureNumber,
        page: self.searchCondition.page,
        rows: self.searchCondition.rows,
        beginDate: self.searchCondition.beginDate,
        endDate: self.searchCondition.endDate,
        auditorId: self.$store.state.user.userData.data.userId
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/measure/audited-apply-page-list.json",
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
    }
  }
};
</script>

<style>
</style>
