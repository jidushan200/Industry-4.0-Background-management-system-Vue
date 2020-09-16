<template>
  <div>
    <Card>
      <Row>
        <Form ref="formInline" :model="searchCondition" inline :label-width="65">
          <Col span="20">
            <FormItem prop="fixtureMap" label="夹具图号">
              <Input type="text" v-model="searchCondition.fixtureMap" placeholder="请输入夹具图号"></Input>
            </FormItem>
            <FormItem prop="purchaseType" label="申购类型">
              <Select
                style="width:158px"
                v-model="searchCondition.purchaseType"
                ref="purchaseType"
                placeholder="请选择申购类型"
              >
                <Option
                  v-for="item in purchaseType"
                  :value="item.value"
                  :key="item.value"
                >{{ item.name }}</Option>
              </Select>
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
              <Button type="primary" style="margin-left: -60px;" @click="search()">搜索</Button>
            </FormItem>
            <FormItem style="margin-left: -65px;">
              <Button style type="dashed" @click="doReset('formInline')">重置</Button>
            </FormItem>
          </Col>
          <Col span="4" v-if="factoryAuth">
            <FormItem style="width:100%;text-align:right">
              <Button type="success" style="margin-right: 5px;" @click="addPurchase()">
                <Icon type="md-add"></Icon>夹具申购
              </Button>
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
      width="250"
      title="删除"
      @on-ok="deleteModalSureBtn"
      @on-cancel="deleteModalCancelBtn"
    >
      <p>确定删除所选记录？</p>
    </Modal>
    <purchase-apply-modal ref="purchaseApplyModal" @search="getListData"></purchase-apply-modal>
  </div>
</template>
<script>
import axios from "@/libs/api.request";
import { dateFormat, objCopy, hasOneOf } from "@/libs/tools.js";
import purchaseType from "../purchase-type.js";
import { getToken, getBaseUrl } from "@/libs/util";
import purchaseResion from "../purchase-resion.js";
import purchaseApplyModal from "./purchase-apply-modal.vue";
export default {
  name: "page",
  computed: {
    access() {
      return this.$store.state.user.userData.data.access;
    },
    //保存、提交、删除(分厂)
    factoryAuth() {
      return hasOneOf(["01050101"], this.access);
    },
    //审核（分厂领导）
    factoryLeaderAuth() {
      return hasOneOf(["01050102"], this.access);
    },
    //工艺部
    technologyAuth() {
      return hasOneOf(["01050103"], this.access);
    },
    //圆柱分厂
    cylindricalAuth() {
      return hasOneOf(["01050104"], this.access);
    },
    //采购部
    purchasingAuth() {
      return hasOneOf(["01050105"], this.access);
    },
    //审核（主管领导-新品夹具）
    leaderAuthResearch() {
      return hasOneOf(["0105010601"], this.access);
    },
    //审核（主管领导-常用夹具）
    leaderAuthFactory() {
      return hasOneOf(["0105010602"], this.access);
    }
  },

  components: {
    purchaseApplyModal
  },
  data() {
    return {
      departmentAuth: true,
      departmentList: [],
      deleteModalShow: false, //控制删除modal提示 显示
      pkId: null,
      isModify: false, //是否是修改明细操作
      purchaseType: purchaseType,
      purchaseResion: purchaseResion,
      departmentId: null,
      searchCondition: {
        departmentId: null,
        fixtureNumber: "",
        purchaseType: null,
        applyStatusArray: [],
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
          title: "夹具编码",
          key: "fixtureNumber",
          minWidth: 150
        },
        {
          title: "夹具名称",
          key: "fixtureName",
          minWidth: 300
        },
        {
          title: "制件名称",
          key: "partName",
          minWidth: 160
        },
        {
          title: "申请部门",
          key: "applyDepartmentName",
          minWidth: 140
        },
        {
          title: "申请人",
          key: "applierName",
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
          title: "申购数量",
          key: "purchaseQty",
          align: "center",
          minWidth: 100
        },
        {
          title: "申请时间",
          key: "applyTime",
          sortable: true,
          minWidth: 160,
          render: function(h, params) {
            return h(
              "div",
              dateFormat(new Date(params.row.applyTime), "yyyy-MM-dd HH:mm:ss")
            );
          }
        },
        {
          title: "申购原因",
          minWidth: 120,
          key: "purchaseResion",
          render: function(h, params) {
            let statusStr = "";
            switch (params.row.purchaseResion) {
              case 1:
                statusStr = "产量提升";
                break;
              case 2:
                statusStr = "夹具报废";
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
          title: "申购类型",
          key: "purchaseType",
          minWidth: 120,
          render: function(h, params) {
            let statusStr = "";
            switch (params.row.purchaseType) {
              case 1:
                statusStr = "新品开发";
                break;
              case 2:
                statusStr = "常用夹具";
                break;
            }
            return h("div", statusStr);
          }
        },
        {
          title: "申请状态",
          key: "applyStatus",
          fixed: "right",
          minWidth: 180,
          render: function(h, params) {
            let statusStr = "";
            switch (params.row.applyStatus) {
              case 0:
                statusStr = "暂存";
                break;
              case 1:
                statusStr = "待分厂领导审核";
                break;
              case -1:
                statusStr = "分厂领导未通过，已驳回";
                break;
              case 2:
                statusStr = "待工艺部审核";
                break;
              case -2:
                statusStr = "工艺部未通过,已驳回";
                break;
              case 3:
                statusStr = "待圆柱分厂审核";
                break;
              case -3:
                statusStr = "采购部驳回";
                break;
              case 4:
                statusStr = "待圆柱分厂接收";
                break;
              case 5:
                statusStr = "待采购询价";
                break;
              case 6:
                statusStr = "待主管领导审核";
                break;
              case 7:
                statusStr = "待采购接收";
                break;
              case 8:
                statusStr = "采购已接收";
                break;
              case 9:
                statusStr = "圆柱分厂已接收";
                break;
              case 10:
                statusStr = "已完成";
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
                      this.showDetail(
                        params.row.pkId,
                        params.row.applyStatus,
                        params.row.purchaseType
                      );
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
                    display: this.factoryAuth ? "inline-block" : "none"
                  },
                  on: {
                    click: () => {
                      this.delete(params.row.pkId);
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
    const roleId = self.$store.state.user.userData.data.roleId;
    if (roleId != 1) {
      if (!!self.factoryAuth) {
        //分厂员工
      } else if (!!self.factoryLeaderAuth) {
        //分厂领导
        self.searchCondition.applyStatusArray = [1, 2];
      } else if (!!slef.technologyAuth) {
        //工艺部
        self.getDepartmentListData();
        self.departmentAuth = true;
        self.searchCondition.applyStatusArray = [2, 3];
      } else if (!!self.cylindricalAuth) {
        //圆柱分厂
        self.getDepartmentListData();
        self.departmentAuth = true;
        self.searchCondition.applyStatusArray = [3, 4];
      } else if (!!self.purchasingAuth) {
        //采购部
        self.getDepartmentListData();
        self.departmentAuth = true;
        self.searchCondition.applyStatusArray = [5, 6];
      } else if (!!self.leaderAuthFactory) {
        //常用夹具
        self.getDepartmentListData();
        self.departmentAuth = true;
        self.searchCondition.purchaseType = 2;
        self.searchCondition.applyStatusArray = [6, 7];
      } else if (!!self.leaderAuthResearch) {
        //新品夹具
        self.getDepartmentListData();
        self.departmentAuth = true;
        self.searchCondition.applyStatusArray = [6, 7];
      }
    } else {
      //系统管理员
      self.getDepartmentListData();
      self.departmentAuth = true;
    }
    self.getListData();
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
        departmentId: self.searchCondition.departmentId,
        fixtureMap: self.searchCondition.fixtureMap,
        purchaseType: self.searchCondition.purchaseType,
        applyStatusList: self.searchCondition.applyStatusArray.toString(),
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
          url: "/fixture/purchase-apply-page-list.json",
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
    search(name) {
      const self = this;
      self.changePage(1);
    },
    doReset(name) {
      const self = this;
      self.$refs[name].resetFields();
      self.getListData();
    },
    delete(pkId) {
      this.pkId = pkId;
      this.deleteModalShow = true;
    },

    deleteModalSureBtn() {
      const self = this;
      self.deleteRowDate(self.pkId);
    },
    deleteRowDate(pkId) {
      const self = this;
      let para = {
        pkId: pkId
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/fixture/purchase-apply-delete-by-id",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          self.deleteModalShow = false;
          if (res.data.code === 200) {
            self.$Message.success("夹具申购单删除成功!");
            self.getListData();
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },
    //删除modal 取消方法
    deleteModalCancelBtn() {
      const self = this;
      self.pkId = null;
    },
    addPurchase() {
      const self = this;
      self.$refs.purchaseApplyModal.showModal(null, 0, null);
    },
    showDetail(pkId, applyStatus, purchaseType) {
      const self = this;
      self.$refs.purchaseApplyModal.showModal(pkId, applyStatus, purchaseType);
    },
    exportData() {
      const self = this;
      let para = "token=" + getToken();
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
      if (!!self.searchCondition.fixtureMap) {
        para = para + "&fixtureMap=" + self.searchCondition.fixtureMap;
      }
      if (!!self.searchCondition.departmentId) {
        para = para + "&departmentId=" + self.searchCondition.departmentId;
      }
      if (!!self.searchCondition.purchaseType) {
        para = para + "&purchaseType=" + self.searchCondition.purchaseType;
      }
      window.location.href =
        getBaseUrl() + "/fixture/purchase-apply-export?" + para;
    }
  }
};
</script>