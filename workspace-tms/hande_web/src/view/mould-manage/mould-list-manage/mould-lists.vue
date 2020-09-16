<style>
.ivu-table .demo-table-info-row td {
  background-color: #2db7f5;
  color: #fff;
}

.ivu-table .demo-table-warning-row td {
  background-color: #ff8c00 !important;
  /* #ff6600
  #ffA500 */
  color: #fff;
}

.ivu-table .demo-table-error-row td {
  background-color: #eb5353 !important;
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
        <Form ref="formInline" :model="searchCondition" :label-width="65" inline>
          <Col span="18">
            <FormItem prop="fullNumber" label="模具条码">
              <Input type="text" v-model="searchCondition.fullNumber" placeholder="请输入模具条码"></Input>
            </FormItem>
            <FormItem prop="mouldNumber" label="模具编码">
              <Input type="text" v-model="searchCondition.mouldNumber" placeholder="请输入模具编码"></Input>
            </FormItem>
            <FormItem label="图号名称" prop="mouldMap">
              <Select
                style="width:158px"
                v-model="searchCondition.mouldMap"
                ref="mouldMap"
                placeholder="请选择图号名称"
                clearable
                filterable
              >
                <Icon type="ios-search" slot="suffix" />
                <Option
                  v-for="item in baseList"
                  :value="item.mouldMap"
                  :key="item.mouldNumber"
                >{{ item.mouldMap }}-{{ item.mouldName }}</Option>
              </Select>
            </FormItem>
            <FormItem label="状态" prop="mouldStatus">
              <Select
                style="width:120px"
                v-model="searchCondition.mouldStatus"
                ref="mouldStatus"
                placeholder="请选择状态"
              >
                <Icon type="ios-search" slot="suffix" />
                <Option
                  v-for="item in statusList"
                  :value="item.value"
                  :key="item.value"
                >{{ item.name }}</Option>
              </Select>
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
              <Button type="primary" style="margin-left: -60px" @click="doSearch('formInline')">搜索</Button>
            </FormItem>
            <FormItem style="margin-left: -65px">
              <Button type="dashed" @click="doReset('formInline')">重置</Button>
            </FormItem>
          </Col>
          <Col span="6">
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
              <Button type="success" style="margin-right: 5px" @click="addMould">
                <Icon type="md-add"></Icon>新模具
              </Button>
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
      :title="isBatchDetele?'批量模具报废':'模具报废'"
      @on-ok="deleteModalSureBtn"
      @on-cancel="deleteModalCancelBtn"
    >
      <p>是否确定报废该模具？</p>
    </Modal>
    <mould-add ref="mouldAdd" @search="doSearch"></mould-add>
    <mould-output ref="mouldOutput" @search="doSearch"></mould-output>
    <mould-return ref="mouldReturn" @search="doSearch"></mould-return>
  </div>
</template>

<script>
import axios from "@/libs/api.request";
import { getToken, getBaseUrl } from "@/libs/util";
import { dateFormat, objCopy, hasOneOf } from "@/libs/tools.js";
import statusList from "./mould-status-list.js";
import mouldAdd from "./mould-add.vue";
import mouldOutput from "./mould-output.vue";
import mouldReturn from "./mould-return.vue";

export default {
  components: {
    mouldAdd,
    mouldOutput,
    mouldReturn
  },
  data() {
    return {
      uploadAction:
        getBaseUrl() + "/mould/mould-import.json?token=" + getToken(),
      isBatchDetele: false,
      deleteModalShow: false, //控制删除modal提示 显示
      multiselectRowData: [], //复选列数据
      baseList: [],
      statusList: statusList,
      searchCondition: {
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
          title: "模具编码",
          key: "mouldNumber",
          minWidth: 160,
          fixed: "left"
        },
        {
          title: "模具名称",
          key: "mouldName",
          minWidth: 280,
          fixed: "left"
        },
        {
          title: "模具图号",
          key: "mouldMap",
          minWidth: 140,
          fixed: "left"
        },
        {
          title: "模具条码",
          key: "mouldBarcode",
          minWidth: 260,
          fixed: "left"
        },
        {
          title: "库位",
          key: "warehouse",
          minWidth: 100
        },

        {
          title: "模具坯编码",
          key: "embryoCode",
          minWidth: 140
        },
        {
          title: "模具坯名称",
          key: "embryoName",
          minWidth: 320
        },
        {
          title: "库管员",
          key: "keeper",
          minWidth: 100
        },
        {
          title: "模具数量",
          key: "mouldAmount",
          minWidth: 100
        },
        {
          title: "让步状态",
          key: "isCompromise",
          minWidth: 100,
          render: function(h, params) {
            let statusStr = "";
            switch (params.row.isCompromise) {
              case 1:
                statusStr = "让步使用";
                break;
              default:
                statusStr = "正常使用";
                break;
            }
            return h("div", statusStr);
          }
        },
        {
          title: "模具状态",
          key: "mouldStatus",
          minWidth: 100,
          render: function(h, params) {
            let statusStr = "";
            switch (params.row.mouldStatus) {
              case 0:
                statusStr = "待入库";
                break;
              case 1:
                statusStr = "在库";
                break;
              case 2:
                statusStr = "使用";
                break;
              case 3:
                statusStr = "待修磨";
                break;
              case 4:
                statusStr = "待检";
                break;
              case 5:
                statusStr = "待返库";
                break;
              case 6:
                statusStr = "不合格待处理";
                break;
              case 7:
                statusStr = "报废";
                break;
            }
            return h("div", statusStr);
          }
        },
        {
          title: "领用部门",
          key: "useDepartmentName",
          minWidth: 150
        },
        {
          title: "领用班组",
          key: "useTeamName",
          minWidth: 120
        },
        {
          title: "领用人",
          key: "userName",
          minWidth: 100
        },
        {
          title: "入库时间",
          key: "warehouseTime",
          minWidth: 120,
          render: function(h, params) {
            if (!!params.row.warehouseTime) {
              return h(
                "div",
                dateFormat(new Date(params.row.warehouseTime), "yyyy-MM-dd")
              );
            }
          }
        },
        {
          title: "领用时间",
          key: "receiveTime",
          minWidth: 120,
          render: function(h, params) {
            if (!!params.row.receiveTime) {
              return h(
                "div",
                dateFormat(new Date(params.row.receiveTime), "yyyy-MM-dd")
              );
            }
          }
        },
        {
          title: "最后更新时间",
          key: "updateTime",
          sortable: "custom",
          minWidth: 160,
          render: function(h, params) {
            return h(
              "div",
              dateFormat(new Date(params.row.updateTime), "yyyy-MM-dd hh:mm")
            );
          }
        },
        {
          title: "已加工次数",
          key: "processTimes",
          width: 120
        },
        {
          title: "已加工数量",
          key: "processAmount",
          width: 120
        },
        {
          title: "最大生产寿命",
          key: "lifeMax",
          width: 120
        },
        {
          title: "热处理批次号",
          key: "heatNumber",
          width: 240
        },
        {
          title: "表面处理批次号",
          key: "surfaceNumber",
          width: 220
        },
        {
          title: "已修磨次数",
          key: "repairTimes",
          width: 100
        },
        {
          title: "修磨人",
          key: "grinder",
          width: 100
        },
        {
          title: "修磨时间",
          key: "repairTime",
          width: 120,
          render: function(h, params) {
            if (!!params.row.repairTime) {
              return h(
                "div",
                dateFormat(new Date(params.row.repairTime), "yyyy-MM-dd")
              );
            }
          }
        },
        {
          title: "出库备注",
          key: "outRemark",
          minWidth: 160
        },
        {
          title: "返库备注",
          key: "returnRemark",
          minWidth: 160
        },
        {
          title: "操作",
          key: "action",
          minWidth: 200,
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
                    disabled: params.row.mouldStatus !== 1,
                    size: "small"
                  },
                  style: {
                    display:
                      params.row.mouldStatus === 5 ? "none" : "inline-block",
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
                    type: "success",
                    size: "small"
                  },
                  style: {
                    marginRight: "5px",
                    display:
                      params.row.mouldStatus !== 5 ? "none" : "inline-block"
                  },
                  on: {
                    click: () => {
                      this.returnMould(params.row);
                    }
                  }
                },
                "返库"
              ),
              h(
                "Button",
                {
                  props: {
                    type: "error",
                    size: "small"
                  },
                  style: {
                    marginRight: "5px",
                    display: params.row.isScrip == 1 ? "inline-block" : "none"
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
    self.getBaseListData();
    self.getListData();
  },
  methods: {
    getBaseListData() {
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/mould/mould-base-list",
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
        mouldBarcode: self.searchCondition.fullNumber,
        mouldNumber: self.searchCondition.mouldNumber,
        mouldMap: self.searchCondition.mouldMap,
        page: self.searchCondition.page,
        rows: self.searchCondition.rows,
        beginDate: self.searchCondition.beginDate,
        endDate: self.searchCondition.endDate,
        sort: self.searchCondition.sort,
        mouldStatus: self.searchCondition.mouldStatus,
        isList: 1
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/mould/mould-page-list",
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
    deleteRowDate(pkId, mouldBarcode) {
      let para = {
        pkId: pkId,
        mouldBarcode: mouldBarcode
      };
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/mould/mould-delete-by-id",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          self.deleteModalShow = false;
          if (res.data.code === 200) {
            self.$Message.success("模具已成功报废!");
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
          this.deleteRowData.mouldBarcode
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
    handleSuccess(res) {
      const self = this;
      if (res.code == 200) {
        self.getListData();
        self.$Message.success("文件导入成功!");
      } else {
        self.$Message.error(res.info);
      }
    },
    handleError() {
      const self = this;
      self.$Message.success("文件导入失败!");
    },
    addMould() {
      const self = this;
      self.$refs.mouldAdd.showModal();
    },
    outbound(row) {
      const self = this;
      self.$refs.mouldOutput.showModal(row);
    },
    returnMould(row) {
      const self = this;
      self.$refs.mouldReturn.showModal(row);
    },
    remove(row) {
      const self = this;
      if (row.isScrip === 1) {
        self.deleteModalShow = true;
        self.deleteRowData = row;
      } else {
        self.$Message.error("该模具尚未通过锻造研究所报废判定!");
      }
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
    rowClassName(row, index) {
      let lifeMax = row.lifeMax;
      if (lifeMax > 0) {
        let processAmount = row.processAmount;
        let remindLife = lifeMax * 0.9;
        if (remindLife <= processAmount) {
          return "demo-table-warning-row";
        }
      }
      let returnResion = row.returnResion;
      if (!!returnResion && returnResion == 2) {
        return "demo-table-error-row";
      }
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
      let fullNumber = self.searchCondition.fullNumber;
      let beginDate = self.searchCondition.beginDate;
      let endDate = self.searchCondition.endDate;
      let mouldStatus = self.searchCondition.mouldStatus;
      let para = "";
      if (!!fullNumber) {
        para = "&mouldBarcode=" + fullNumber;
      }
      if (!!mouldStatus) {
        para = "&mouldStatus=" + mouldStatus;
      }
      if (!!beginDate) {
        para = para + "&beginDate=" + beginDate;
      }
      if (!!endDate) {
        para = para + "&endDate=" + endDate;
      }
      window.location.href =
        getBaseUrl() + "/mould/mould-list-export?token=" + getToken() + para;
    },
    print(pkId) {
      window.open(
        "http://10.36.10.11:8080/decision/view/report?viewlet=mould.cpt&pkId=" +
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
