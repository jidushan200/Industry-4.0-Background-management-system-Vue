<template>
  <div>
    <Card>
      <Row>
        <Form ref="formInline" :model="searchCondition" inline :label-width="65">
          <Col span="20">
            <FormItem prop="measureNumber" label="量具编码">
              <Input type="text" v-model="searchCondition.measureNumber" placeholder="请输入量具编码" @keyup.enter.native="doSearch()"></Input>
            </FormItem>
            <FormItem label="申请部门" prop="departmentId" v-if="departmentAuth">
              <Select style="width:158px" v-model="searchCondition.departmentId" ref="department">
                <Option
                  v-for="item in departmentList"
                  :value="item.pkId"
                  :key="item.pkId"
                >{{ item.departmentName }}</Option>
              </Select>
            </FormItem>
            <FormItem prop="dateInterval" label="时间区间">
              <DatePicker
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
          <Col span="4">
            <FormItem style="width:100%;text-align:right">
              <Button
                type="success"
                style="margin-right: 5px;"
                @click="addPurchase()"
                v-if="purchaseAuth"
              >
                <Icon type="md-add"></Icon>量具申购
              </Button>
              <!-- <Button type="warning" @click="exportData()">
                <Icon type="ios-download-outline"></Icon>导出
              </Button>-->
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
      v-model="deleteModalShow"
      width="250"
      title="删除报告"
      @on-ok="deleteModalSureBtn"
      @on-cancel="deleteModalCancelBtn"
    >
      <p>确定删除所选报告？</p>
    </Modal>
    <measure-purchase-modal ref="measurePurchaseModal" @search="doSearch"></measure-purchase-modal>
    <audit-log-model ref="auditLogModel"></audit-log-model>
  </div>
</template>

<script>
import axios from "@/libs/api.request";
import { dateFormat, objCopy, hasOneOf } from "@/libs/tools.js";
import measurePurchaseModal from "./measure-purchase-modal.vue";
import auditLogModel from "./audit-log-model.vue";
import { setTimeout } from "timers";
export default {
  name: "page",
  computed: {
    access() {
      return this.$store.state.user.userData.data.access;
    },
    //保存、提交、删除（生产部）
    purchaseAuth() {
      return hasOneOf(["01040101"], this.access);
    },
    //审核(分厂)
    productAuth() {
      return hasOneOf(["01040102"], this.access);
    },
    //审核(装备车间)
    workshopAuth() {
      return hasOneOf(["01040103"], this.access);
    },
    //审核(采购部补充价格)
    purchasePriceAuth() {
      return hasOneOf(["01040104"], this.access);
    },
    //审核(主管领导)
    leaderAuth() {
      return hasOneOf(["01040105"], this.access);
    },
    //审核(主管领导)
    newMeasureAuth() {
      return hasOneOf(["0104010501"], this.access);
    },
    //审核(主管领导)
    commonMeasureAuth() {
      return hasOneOf(["0104010502"], this.access);
    }
  },
  components: {
    auditLogModel,
    measurePurchaseModal
  },
  data() {
    return {
      isNew: "",
      departmentAuth: true,
      applierId: "",
      isAudit: false,
      seqModel: false,
      radioAudit: false,
      btnSave: true, //保存按钮可见
      btnSubmit: true, //提交按钮可见
      auditText: "提交",
      btnAudit: false, //审核按钮可见
      currentStep: 0, //当前步骤
      auditRemark: "", //审核备注
      applyStatusList: [],
      deleteModalShow: false, //控制删除modal提示 显示
      deleteRowData: {}, //行删除行数据
      isBatchDetele: false, //批量删除标志
      multiselectRowData: [], //复选列数据
      toolPurchaseModalshow: false, //控制明细modal显示
      isModify: false, //是否是修改明细操作
      departmentList: [],
      searchCondition: {
        departmentId: "",
        measureNumber: "",
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
          minWidth: 180
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
                statusStr = "采购部驳回";
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
                      this.show(params.index);
                    }
                  }
                },
                "详情"
              ),
              h(
                "Button",
                {
                  props: {
                    type: "error",
                    disabled: params.row.applyStatus != 0,
                    size: "small"
                  },
                  style: {
                    marginRight: "5px",
                    display: this.purchaseAuth ? "inline-block" : "none"
                  },
                  on: {
                    click: () => {
                      this.remove(params.index);
                    }
                  }
                },
                "删除"
              )
            ]);
          }
        }
      ]
    };
  },
  created() {
    const self = this;
    if (!self.productAuth || !self.newMeasureAuth || !self.commonMeasureAuth) {
      self.departmentAuth = true;
      self.getDepartmentListData();
    } else {
      self.departmentAuth = false;
    }
    self.getListData();
  },
  methods: {
    // channgeFocus(val) {
    //   const self = this;
    //   setTimeout(function() {
    //     if (val) {
    //       self.$refs["refocus"].focus();
    //     }
    //   }, 200);
    // },
    getListData() {
      const self = this;
      if (self.leaderAuth) {
        self.applyStatusList.push(5);
        self.applyStatusList.push(-5);
        if (self.newMeasureAuth && !self.commonMeasureAuth) {
          self.isNew = 1;
        }
        if (self.commonMeasureAuth && !self.newMeasureAuth) {
          self.isNew = 2;
        }
      }
      if (self.purchasePriceAuth) {
        self.applyStatusList.push(4);
        self.applyStatusList.push(-4);
      }
      if (self.workshopAuth) {
        self.applyStatusList.push(2);
        self.applyStatusList.push(3);
      }
      if (self.productAuth) {
        if (
          !self.purchaseAuth &&
          !self.workshopAuth &&
          !self.purchasePriceAuth &&
          !self.leaderAuth
        ) {
          self.searchCondition.departmentId =
            self.$store.state.user.userData.data.departmentId;
        }
        self.applyStatusList.push(1);
      }
      if (self.purchaseAuth) {
        if (
          !!!self.leaderAuth &&
          !!!self.purchasePriceAuth &&
          !!!self.workshopAuth &&
          !!!self.productAuth
        ) {
          self.applierId = self.$store.state.user.userData.data.userId;
        }
        self.applyStatusList = [];
      }
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
        isNew: self.isNew,
        applierId: self.applierId,
        applyStatusList: self.applyStatusList.toString(),
        departmentId: self.searchCondition.departmentId,
        measureNumber: self.searchCondition.measureNumber,
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
          url: "/measure/measure-purchase-apply-page-list.json",
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
    deleteRowDate(pkId, measureNumber) {
      let para = {
        pkId: pkId,
        measureNumber: measureNumber
      };
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/measure/measure-purchase-apply-delete-by-id",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          self.deleteModalShow = false;
          if (res.data.code === 200) {
            self.$Message.success("申购报告删除成功!");
            self.getListData();
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
    search() {
      const self = this;
      self.getListData();
    },
    doSearch(name) {
      const self = this;
      this.formValidate = {};
      self.changePage(1);
    },
    doReset(name) {
      const self = this;
      self.$refs[name].resetFields();
      self.getListData();
    },
    show(index) {
      const self = this;
      self.$refs.measurePurchaseModal.show(self.tableData.rows[index]);
    },
    remove(index) {
      this.deleteRowData = this.tableData.rows[index];
      this.deleteModalShow = true;
    },
    addPurchase() {
      const self = this;
      self.$refs.measurePurchaseModal.addModal();
    },
    deleteModalSureBtn() {
      this.deleteRowDate(
        this.deleteRowData.pkId,
        this.deleteRowData.measureNumber
      );
    },
    //删除modal 取消方法
    deleteModalCancelBtn() {
      if (this.isBatchDetele) {
        this.multiselectRowData = [];
      } else {
        this.deleteRowData = {};
      }
    }
  }
};
</script>

<style>
</style>
