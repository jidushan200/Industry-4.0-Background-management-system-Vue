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
    <Modal v-model="returnModalShow" width="250" title="退货确认" @on-ok="returnModalSureBtn">
      <p>是否确定退回量具？</p>
    </Modal>
  </div>
</template>

<script>
import axios from "@/libs/api.request";
import { dateFormat, objCopy } from "@/libs/tools.js";
import apply_form_config from "./apply-form-config.js";
export default {
  data() {
    return {
      returnModalShow: false, //控制明细modal显示
      isModify: false, //是否是修改明细操作
      searchCondition: {
        measureNumber: "",
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
          minWidth: 180,
          key: "measureName"
        },
        {
          title: "物料图号",
          minWidth: 150,
          key: "model"
        },
        {
          title: "送货时间",
          minWidth: 150,
          key: "deliveryTime",
          render: function(h, params) {
            return h(
              "div",
              dateFormat(new Date(params.row.deliveryTime), "yyyy-MM-dd HH:mm")
            );
          }
        },
        {
          title: "供应商名称",
          minWidth: 250,
          key: "supplierName"
        },
        {
          title: "申请部门",
          minWidth: 160,
          key: "applyDepartmentName"
        },
        {
          title: "申请人",
          minWidth: 100,
          key: "applierName"
        },
        {
          title: "不合格说明",
          minWidth: 250,
          key: "remark"
        },
        {
          title: "操作",
          key: "action",
          minWidth: 120,
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
                      this.returnPurchase(params.row.pkId);
                    }
                  }
                },
                "退货"
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
        measureNumber: self.searchCondition.measureNumber,
        page: self.searchCondition.page,
        rows: self.searchCondition.rows,
        beginDate: self.searchCondition.beginDate,
        endDate: self.searchCondition.endDate,
        checkResult: 2,
        checkType: 1
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/measure/new-measure-check-page-list.json",
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
    selectReport(pkId) {
      const self = this;
      self.$refs.purchaseApplyModel.showModal(pkId);
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
    returnPurchase(pkId) {
      const self = this;
      self.pkId = pkId;
      self.returnModalShow = true;
    },
    returnModalSureBtn() {
      const self = this;
      let para = {
        pkId: self.pkId
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/measure/return-purchase.json",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            self.$Message.success("退货已确认!");
            self.getListData();
            self.returnModalShow = false;
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
