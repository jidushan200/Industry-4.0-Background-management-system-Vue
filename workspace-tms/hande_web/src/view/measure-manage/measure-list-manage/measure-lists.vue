<style>
.ivu-table .demo-table-info-row td {
  background-color: #2db7f5;
  color: #fff;
}

.ivu-table .demo-table-error-row td {
  background-color: #ff8c00 !important;
  /* #ff6600
  #ffA500 */
  color: #fff;
}

.ivu-table td.demo-table-info-column {
  background-color: #2db7f5;
  color: #fff;
}

.ivu-table .demo-table-info-cell-name {
  background-color: #2db7f5;
  color: #fff;
}

.ivu-table .demo-table-info-cell-age {
  background-color: #ff6600;
  color: #fff;
}

.ivu-table .demo-table-info-cell-address {
  background-color: #187;
  color: #fff;
}
</style>
<template>
  <div>
    <Card>
      <Row>
        <Form ref="formInline" :model="searchCondition" :label-width="85" inline>
          <Col span="20">
            <FormItem prop="fullNumber" label="量具条码">
              <Input
                type="text"
                ref="fullNumberFocus"
                v-model="searchCondition.fullNumber"
                placeholder="请输入量具条码"
              ></Input>
            </FormItem>
            <FormItem prop="factoryMetrology" label="本厂计量编码">
              <Input type="text" v-model="searchCondition.factoryMetrology" placeholder="请输入本厂计量编码"></Input>
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
            <FormItem prop="staffCode" label="使用人">
              <Select
                style="width:158px"
                clearable
                filterable
                v-model="searchCondition.staffCode"
                ref="staff"
              >
                <Option
                  v-for="item in staffList"
                  :value="item.staffCode"
                  :key="item.pkId"
                >{{ item.staffCode }}-{{ item.staffName }}</Option>
              </Select>
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
              <Button type="primary" style="margin-left: -60px" @click="doSearch('formInline')">搜索</Button>
            </FormItem>
            <FormItem style="margin-left: -65px">
              <Button type="dashed" @click="doReset('formInline')">重置</Button>
            </FormItem>
          </Col>
          <Col span="4">
            <FormItem style="width:100%;text-align:right">
              <Upload
                :show-upload-list="false"
                :action="uploadAction"
                :on-success="handleSuccess"
                :on-error="handleError"
                style="display: inline-block;margin-right: 5px;"
              >
                <Button icon="ios-cloud-upload-outline">导入</Button>
              </Upload>
              <Button type="warning" @click="exportData()">
                <Icon type="ios-download-outline"></Icon>导出
              </Button>
            </FormItem>
          </Col>
        </Form>
      </Row>

      <Table
        ref="tablesMain"
        :row-class-name="rowClassName"
        :data="tableData.rows"
        :columns="tableColumns"
        stripe
        border
        @on-sort-change="changeSort"
      ></Table>

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
      :title="isBatchDetele?'批量量具报废':'量具报废'"
      @on-ok="deleteModalSureBtn"
      @on-cancel="deleteModalCancelBtn"
    >
      <p>是否确定报废该量具？</p>
    </Modal>
    <measure-output ref="measureOutput" @search="toPage"></measure-output>
    <measure-check-cycle ref="measureCheckCycle" @search="toPage"></measure-check-cycle>
    <measure-return ref="measureReturn" @search="toPage"></measure-return>
  </div>
</template>

<script>
import axios from "@/libs/api.request";
import { getToken, getBaseUrl } from "@/libs/util";
import { dateFormat, objCopy, hasOneOf } from "@/libs/tools.js";
import measureCheckCycle from "./measure-check-cycle.vue";
import measureOutput from "./measure-output.vue";
import measureReturn from "./measure-return.vue";
import supplier_manage_form_config from "../../base-manage/supplier/supplier-manage-form-config.js";

export default {
  components: {
    measureOutput,
    measureCheckCycle,
    measureReturn
  },
  computed: {
    access() {
      return this.$store.state.user.userData.data.access;
    },
    //库房
    warehouseAuth() {
      return hasOneOf(["0104040301"], this.access);
    },
    //质量部
    checkAuth() {
      return hasOneOf(["0104040302"], this.access);
    }
  },
  data() {
    return {
      uploadAction:
        getBaseUrl() + "/measure/measure-import.json?token=" + getToken(),
      newSupplierId: "",
      staffInfo: "",
      fullNumber: "",
      toolMap: "",
      toolName: "",
      page: 1,
      warehouseNumber: "",
      measureStatus: "",
      departmentList: [],
      staffList: [],
      statusList: [],
      commonOut: true,
      coatOut: false,
      isBatchDetele: false,
      deleteModalShow: false, //控制删除modal提示 显示
      multiselectRowData: [], //复选列数据
      toolWarehouseModalshow: false, //控制入库明细modal显示
      toolReturnModalshow: false, //控制返库明细modal显示
      isReturn: false, //是否是入库明细操作
      measureCheckCycle: 10,
      searchCondition: {
        factoryMetrology: "",
        departmentId: "",
        staffCode: "",
        fullNumber: "",
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
          title: "量具编码",
          key: "measureNumber",
          width: 140,
          fixed: "left"
        },
        {
          title: "量具名称",
          key: "measureName",
          width: 220,
          fixed: "left"
        },
        {
          title: "量具图号",
          key: "model",
          width: 120
        },
        {
          title: "量具条码",
          key: "measureBarcode",
          width: 240
        },
        {
          title: "库位",
          key: "warehouse",
          width: 150
        },
        {
          title: "入库时间",
          key: "storageTime",
          sortable: true,
          width: 120,
          render: function(h, params) {
            return h(
              "div",
              dateFormat(new Date(params.row.storageTime), "yyyy-MM-dd")
            );
          }
        },
        {
          title: "检定时间",
          key: "checkTime",
          sortable: "custom",
          width: 120,
          render: function(h, params) {
            if (!!params.row.checkTime) {
              return h(
                "div",
                dateFormat(new Date(params.row.checkTime), "yyyy-MM-dd")
              );
            }
          }
        },
        {
          title: "下次检定时间",
          key: "nextCheckTime",
          width: 130,
          sortable: true,
          render: function(h, params) {
            if (!!params.row.nextCheckTime) {
              return h(
                "div",
                dateFormat(new Date(params.row.nextCheckTime), "yyyy-MM-dd")
              );
            }
          }
        },
        {
          title: "领用部门",
          key: "useDepartmentName",
          width: 150
        },
        {
          title: "领用班组",
          key: "useTeamName",
          width: 120
        },
        {
          title: "领用人",
          key: "userName",
          width: 100
        },

        {
          title: "封存状态",
          key: "sealMark",
          width: 120,
          render: function(h, params) {
            let statusStr = "";
            switch (params.row.sealMark) {
              case 0:
                statusStr = "启用";
                break;
              case 1:
                statusStr = "封存";
                break;
            }
            return h("div", statusStr);
          }
        },
        {
          title: "物料状态",
          key: "measureStatus",
          width: 120,
          render: function(h, params) {
            let statusStr = "";
            switch (params.row.measureStatus) {
              case 1:
                statusStr = "待入库";
                break;
              case 2:
                statusStr = "在库";
                break;
              case 3:
                statusStr = "完善定检周期";
                break;
              case 4:
                statusStr = "使用";
                break;
              case 5:
                statusStr = "返库待检";
                break;
              case 6:
                statusStr = "封存";
                break;
              case 7:
                statusStr = "丢失";
                break;
              case 8:
                statusStr = "报废";
                break;
            }
            return h("div", statusStr);
          }
        },
        {
          title: "封存时间",
          key: "sealTime",
          width: 150,
          render: function(h, params) {
            if (!!params.row.sealTime) {
              return h(
                "div",
                dateFormat(new Date(params.row.sealTime), "yyyy-MM-dd HH:mm:ss")
              );
            }
          }
        },
        {
          title: "启用时间",
          key: "enableTime",
          width: 150,
          render: function(h, params) {
            if (!!params.row.enableTime) {
              return h(
                "div",
                dateFormat(
                  new Date(params.row.enableTime),
                  "yyyy-MM-dd HH:mm:ss"
                )
              );
            }
          }
        },
        {
          title: "生产厂家",
          key: "supplierName",
          width: 240
        },
        {
          title: "厂家编码",
          key: "manufacturingNumber",
          width: 220
        },
        {
          title: "本厂计量编号",
          key: "factoryMetrology",
          width: 220
        },
        {
          title: "修磨次数",
          key: "repairTimes",
          width: 100
        },
        {
          title: "操作",
          key: "action",
          width: 290,
          align: "center",
          fixed: "right",
          render: (h, params) => {
            const self = this;
            return h("div", [
              h(
                "Button",
                {
                  props: {
                    type: "info",
                    size: "small"
                  },
                  style: {
                    marginRight: "5px"
                  },
                  on: {
                    click: () => {
                      this.print(params.row.pkId);
                    }
                  }
                },
                "打印"
              ),
              h(
                "Button",
                {
                  props: {
                    type: "primary",
                    disabled:
                      params.row.measureStatus !== 2 || !self.warehouseAuth,
                    size: "small"
                  },
                  style: {
                    display:
                      params.row.measureStatus === 3 ||
                      params.row.measureStatus === 4 // params.row.measureStatus === 5
                        ? "none"
                        : "inline-block",
                    marginRight: "5px"
                  },
                  on: {
                    click: () => {
                      this.outbound(params.row);
                    }
                  }
                },
                "出库"
              ),
              h(
                "Button",
                {
                  props: {
                    type: "info",
                    disabled: !self.checkAuth,
                    size: "small"
                  },
                  style: {
                    marginRight: "5px",
                    display:
                      params.row.measureStatus !== 3 ? "none" : "inline-block"
                  },
                  on: {
                    click: () => {
                      this.checkCycle(params.row);
                    }
                  }
                },
                "检定"
              ),
              h(
                "Button",
                {
                  props: {
                    type: "success",
                    disabled: !self.warehouseAuth,
                    size: "small"
                  },
                  style: {
                    marginRight: "5px",
                    display:
                      params.row.measureStatus !== 4 ? "none" : "inline-block"
                  },
                  on: {
                    click: () => {
                      this.returnMeasure(params.row);
                    }
                  }
                },
                "返库"
              ),
              h(
                "i-switch",
                {
                  //数据库1是已处理，0是未处理
                  props: {
                    size: "large",
                    disabled:
                      params.row.measureStatus !== 2 &&
                      params.row.measureStatus !== 6,
                    value: params.row.sealMark !== 1 //控制开关的打开或关闭状态，官网文档属性是value
                  },
                  style: {
                    marginRight: "5px",
                    display: !self.warehouseAuth ? "none" : "inline-block",
                    tintColor: "red"
                  },
                  on: {
                    "on-change": value => {
                      //触发事件是on-change,用双引号括起来，
                      //参数value是回调值，并没有使用到
                      this.switch(params.row, value); //params.index是拿到table的行序列，可以取到对应的表格值
                    }
                  }
                },
                [
                  h(
                    "span",
                    {
                      slot: "open"
                    },
                    "启用"
                  ),
                  h(
                    "span",
                    {
                      slot: "close"
                    },
                    "封存"
                  )
                ]
              ),
              h(
                "Button",
                {
                  props: {
                    type: "warning",
                    disabled: !self.warehouseAuth,
                    size: "small"
                  },
                  style: {
                    marginRight: "5px",
                    display:
                      params.row.measureStatus === 7 ? "none" : "inline-block"
                  },
                  on: {
                    click: () => {
                      this.lostMeasure(params.row);
                    }
                  }
                },
                "丢失"
              ),
              h(
                "Button",
                {
                  props: {
                    type: "success",
                    disabled: !self.warehouseAuth,
                    size: "small"
                  },
                  style: {
                    marginRight: "5px",
                    display:
                      params.row.measureStatus !== 7 && self.checkAuth
                        ? "none"
                        : "inline-block"
                  },
                  on: {
                    click: () => {
                      this.findMeasure(params.row);
                    }
                  }
                },
                "找回"
              ),
              h(
                "Button",
                {
                  props: {
                    type: "error",
                    disabled: params.row.isScrip !== 1,
                    size: "small"
                  },
                  style: {
                    marginRight: "5px",
                    display: !self.warehouseAuth ? "none" : "inline-block"
                  },
                  on: {
                    click: () => {
                      this.remove(params.row);
                    }
                  }
                },
                "报废"
              )
            ]);
          }
        }
      ]
    };
  },
  created() {
    const self = this;
    self.getSysParamData();
    self.getDepartmentListData();
    self.getStaffListData();
    self.getListData();
    setTimeout(function() {
      self.$refs["fullNumberFocus"].focus();
    }, 200);
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
    getStaffListData() {
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/base/staff-list",
          isAuth: true,
          method: "post"
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            self.staffList = data.data;
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },

    getSysParamData() {
      const self = this;
      let para = {
        paramKey: "measureCheckCycle"
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/sys/get-sysparam-by-key",
          isAuth: true,
          params: para,
          method: "post"
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            self.measureCheckCycle = parseInt(data.data.paramValue);
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
        factoryMetrology: self.searchCondition.factoryMetrology,
        measureBarcode: self.searchCondition.fullNumber,
         departmentId: self.searchCondition.departmentId,
        staffCode: self.searchCondition.staffCode,
        staffName: self.searchCondition.staffName,
        page: self.searchCondition.page,
        rows: self.searchCondition.rows,
        beginDate: self.searchCondition.beginDate,
        endDate: self.searchCondition.endDate,
        sort: self.searchCondition.sort,
        isList: 1
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/measure/measure-page-list",
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
    deleteRowDate(pkId, measureBarcode) {
      let para = {
        pkId: pkId,
        measureBarcode: measureBarcode
      };
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/measure//measure-delete-by-id",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          self.deleteModalShow = false;
          if (res.data.code === 200) {
            self.$Message.success("量具已成功报废!");
            self.getListData();
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },
    deleteModalSureBtn() {
      if (this.isBatchDetele) {
        let ids = [];
        for (const item of this.multiselectRowData) {
          ids.push(item.pkId);
        }
      } else {
        this.deleteRowDate(
          this.deleteRowData.pkId,
          this.deleteRowData.measureBarcode
        );
      }
    },
    //删除modal 取消方法
    deleteModalCancelBtn() {
      if (this.isBatchDetele) {
        this.multiselectRowData = [];
      } else {
        this.deleteRowData = {};
      }
    },
    outbound(row) {
      const self = this;
      self.$refs.measureOutput.showModal(row);
    },
    checkCycle(row) {
      const self = this;
      self.$refs.measureCheckCycle.showModal(row);
    },
    returnMeasure(row) {
      const self = this;
      self.$refs.measureReturn.showModal(row);
    },
    lostMeasure(row) {
      const self = this;
      let para = {
        pkId: row.pkId,
        measureBarcode: row.measureBarcode,
        measureStatus: 7
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/measure/measure-lost-find",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          self.deleteModalShow = false;
          if (res.data.code === 200) {
            self.$Message.success("量具状态已变更为丢失!");
            self.getListData();
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },
    findMeasure(row) {
      const self = this;
      let para = {
        pkId: row.pkId,
        measureBarcode: row.measureBarcode,
        measureStatus: 2
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/measure//measure-lost-find",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          self.deleteModalShow = false;
          if (res.data.code === 200) {
            self.$Message.success("量具已成功找回!");
            self.getListData();
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },
    remove(row) {
      const self = this;
      if (row.isScrip === 1) {
        self.deleteModalShow = true;
        self.deleteRowData = row;
      } else {
        self.$Message.error("该量具尚未通过质量部报废判定!");
      }
    },
    toCheck(row) {
      const self = this;
      let para = {
        pkId: row.pkId
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/measure/measure-to-check",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          if (res.data.code === 200) {
            self.returnDetailModalshow = false;
            self.$Message.success("量具送检成功!");
            self.getListData();
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
    doSearch(name) {
      const self = this;
      self.page = 1;
      self.changePage(1);
    },
    toPage() {
      const self = this;
      self.$set(self.searchCondition, "page", self.page);
      self.getListData();
    },
    doReset(name) {
      const self = this;
      self.$refs[name].resetFields();
      self.getListData();
    },
    changePage(cuurentPage) {
      const self = this;
      self.page = cuurentPage;
      self.$set(self.searchCondition, "page", cuurentPage);
      self.getListData();
    },
    handlePageSize(value) {
      const self = this;
      self.$set(self.searchCondition, "rows", value);
      self.getListData();
    },
    changeSort() {
      const self = this;
      if (self.sort === "DESC") {
        self.sort = "ASC";
      } else {
        self.sort = "DESC";
      }
      self.searchCondition.sort = self.sort;
      self.changePage(1);
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
      let measureBarcode = self.searchCondition.fullNumber;
      let departmentId = self.searchCondition.departmentId;
      let factoryMetrology = self.searchCondition.factoryMetrology;
      let staffCode = self.searchCondition.staffCode;
      let beginDate = self.searchCondition.beginDate;
      let endDate = self.searchCondition.endDate;
      let para = "";
      if (!!measureBarcode) {
        para = "&measureBarcode=" + measureBarcode;
      }
      if (!!departmentId) {
        para = "&departmentId=" + departmentId;
      }
      if (!!factoryMetrology) {
        para = "&factoryMetrology=" + factoryMetrology;
      }
      if (!!staffCode) {
        para = "&staffCode=" + staffCode;
      }
      if (!!beginDate) {
        para = para + "&beginDate=" + beginDate;
      }
      if (!!endDate) {
        para = para + "&endDate=" + endDate;
      }
      window.location.href =
        getBaseUrl() + "/measure/measure-export?token=" + getToken() + para;
    },
    switch(row, value) {
      const self = this;
      if (value === true) {
        self.measureStatus = 2;
        self.$Message.info("当前订单状态修改为 启用");
      } else {
        self.measureStatus = 6;
        self.$Message.info("当前订单状态修改为 封存");
      }
      let para = {
        pkId: row.pkId,
        measureBarcode: row.measureBarcode,
        measureStatus: self.measureStatus
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/measure/measure-seal-by-id",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          if (res.data.code === 200) {
            self.getListData();
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
    handleSuccess(res) {
      const self = this;
      if (res.code === 200) {
        self.getListData();
        self.$Message.success("文件导入成功!");
      } else if (res.code === 500) {
        self.$Notice.error({
          title: "错误提示",
          desc: res.info
        });
      }
    },
    handleError() {
      const self = this;
      self.$Message.success("文件导入失败!");
    },
    rowClassName(row, index) {
      const self = this;
      let dateDiff = Math.floor(
        (row.nextCheckTime - new Date()) / (24 * 3600 * 1000)
      );
      if (dateDiff <= self.measureCheckCycle) {
        return "demo-table-error-row";
      } else if (index === 3) {
        return "";
      }
      // return '';
    },
    print(pkId) {
      window.open(
        "http://10.36.10.11:8080/decision/view/report?viewlet=measure.cpt&pkId=" +
          pkId,
        "newwindow",
        "height=360,width=600,top=0,left=0,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no"
      );
    }
  }
};
</script>

<style>
</style>
