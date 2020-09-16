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
</style>
<template>
  <div>
    <Card>
      <Row>
        <Form ref="formInline" :model="searchCondition" :label-width="65" inline>
          <Col span="18">
            <FormItem prop="fullNumber" label="物料条码">
              <Input
                type="text"
                style="width:158px"
                v-model="searchCondition.fullNumber"
                placeholder="请输入物料条码"
              ></Input>
            </FormItem>
            <FormItem prop="number" label="物料编码">
              <Input
                type="text"
                style="width:158px"
                v-model="searchCondition.number"
                placeholder="请输入物料编码"
              ></Input>
            </FormItem>
            <FormItem label="图号名称" prop="toolNumber">
              <Select
                style="width:158px"
                v-model="searchCondition.toolNumber"
                ref="toolName"
                placeholder="请选择编号名称"
                clearable
                filterable
              >
                <Icon type="ios-search" slot="suffix" />
                <Option
                  v-for="item in baseList"
                  :value="item.toolNumber"
                  :key="item.toolNumber"
                >{{ item.toolName }}</Option>
              </Select>
            </FormItem>
            <FormItem label="物料类型" prop="typeId">
              <Select
                style="width:158px"
                v-model="searchCondition.typeId"
                ref="toolType"
                placeholder="请选择物料类型"
              >
                <Option
                  v-for="item in typeList"
                  :value="item.pkId"
                  :key="item.typeNumber"
                >{{ item.typeName }}</Option>
              </Select>
            </FormItem>
            <FormItem prop="dateInterval" label="时间区间">
              <DatePicker
                type="daterange"
                split-panels
                placeholder="请选择时间区间"
                placement="bottom-end"
                style="width:180px;"
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

    <Modal v-model="toolReturnModalshow" title="刀具返库" width="600" draggable>
      <Form
        ref="formValidate"
        :model="formValidate"
        :rules="ruleValidate"
        :label-width="100"
        inline
      >
        <FormItem label="物料条码" prop="fullNumber">
          <Input style="width:170px" v-model="formValidate.fullNumber" readonly />
        </FormItem>
        <FormItem label="物料编码" prop="toolNumber">
          <Input style="width:170px" v-model="formValidate.toolNumber" readonly />
        </FormItem>
        <FormItem label="物料图号" prop="toolMap">
          <Input style="width:170px" v-model="formValidate.toolMap" readonly />
        </FormItem>
        <FormItem label="物料名称" prop="toolName">
          <Input style="width:170px" v-model="formValidate.toolName" readonly />
        </FormItem>
        <FormItem label="库位" prop="warehouse">
          <Input style="width:170px" v-model="formValidate.warehouse" placeholder="输入库位" />
        </FormItem>
        <FormItem label="送货人" prop="deliever">
          <Input style="width:170px" v-model="formValidate.deliever" placeholder="输入送货人" />
        </FormItem>
        <FormItem label="说明" prop="remark">
          <Input style="width:450px" v-model="formValidate.remark" placeholder="输入说明" />
        </FormItem>
      </Form>
      <div slot="footer">
        <Button @click="handleReturnSubmit('formValidate')" :disabled="isDisabled" type="primary">保存</Button>
        <Button @click="handleReturnReset('formValidate')" style="margin-left: 8px">取消</Button>
      </div>
    </Modal>

    <Modal v-model="outboundDetailModalshow" title="刀具出库" width="600" draggable>
      <Form
        ref="formValidate"
        :model="formValidate"
        :rules="ruleValidate"
        :label-width="100"
        inline
      >
        <FormItem label="物料条码" prop="warehouseCode">
          <Input style="width:170px" v-model="formValidate.fullNumber" readonly></Input>
        </FormItem>
        <FormItem label="物料名称" prop="toolName">
          <Input style="width:170px" v-model="formValidate.toolName" readonly></Input>
        </FormItem>
        <FormItem label="物料编码" prop="toolNumber">
          <Input style="width:170px" v-model="formValidate.toolNumber" readonly></Input>
        </FormItem>
        <FormItem label="物料图号" prop="toolMap">
          <Input style="width:170px" v-model="formValidate.toolMap" readonly />
        </FormItem>
        <FormItem label="库位" prop="warehouse">
          <Input style="width:170px" v-model="formValidate.warehouse" readonly />
        </FormItem>
        <FormItem label="出库类型" prop="outType">
          <Select
            style="width: 170px"
            v-model="formValidate.outType"
            ref="outType"
            @on-change="outTypeChange"
          >
            <Option v-for="item in outType" :value="item.value" :key="item.value">{{ item.name }}</Option>
          </Select>
        </FormItem>
        <FormItem label="领用人职工号" prop="staffCode" v-if="commonOut">
          <Input
            style="width:170px"
            v-model="formValidate.staffCode"
            placeholder="输入领用人职工号"
            @on-blur="searchStaff()"
          >
            <Icon type="ios-search" slot="suffix" />
          </Input>&nbsp;
          <Input style="width:270px" v-model="staffInfo" readonly></Input>
        </FormItem>
        <FormItem label="涂层供应商" prop="coatSupplierId" v-if="coatOut">
          <Select
            style="width:170px"
            v-model="formValidate.coatSupplierId"
            ref="supplier"
            @on-change="supplierListSelectChange"
          >
            <Option
              v-for="item in supplierList"
              :value="item.pkId"
              :key="item.pkId"
            >{{ item.supplierName }}</Option>
          </Select>
        </FormItem>
        <FormItem label="领用人" prop="receiver" v-if="coatOut">
          <Input style="width:170px" v-model="formValidate.staffName" placeholder="输入领用人"></Input>
        </FormItem>
        <FormItem label="说明" prop="remark">
          <Input style="width:450px" v-model="formValidate.remark" placeholder="输入说明" />
        </FormItem>
      </Form>
      <div slot="footer">
        <Button
          @click="handleOutboundSubmit('formValidate')"
          type="primary"
          :disabled="isDisabled2"
        >出库</Button>
        <Button @click="handleOutboundReset('formValidate')" style="margin-left: 8px">取消</Button>
      </div>
    </Modal>
    <Modal
      v-model="outboundTypeModalShow"
      width="250"
      title="出库确认"
      :z-index="10000"
      @on-ok="modalSureBtn"
      @on-cancel="modalCancelBtn"
    >
      <p>
        当前出库类型：
        <b>{{outTypeName}}</b>
      </p>
      <p>
        该刀具的领用人：
        <b>{{staffInfo}}</b>
      </p>
      <p>点击确认之后将无法修改!</p>
    </Modal>

    <Modal
      v-model="deleteModalShow"
      width="250"
      :title="isBatchDetele?'批量刀具报废':'刀具报废'"
      @on-ok="deleteModalSureBtn"
      @on-cancel="deleteModalCancelBtn"
    >
      <p>是否确定报废该刀具？</p>
    </Modal>
  </div>
</template>

<script>
import axios from "@/libs/api.request";
import tool_form_config from "./knife-lists-form-config.js";
import { getToken, getBaseUrl } from "@/libs/util";
import { dateFormat, objCopy } from "@/libs/tools.js";
import toolStatus from "./tool-status.js";
import outType from "./out-type.js";

export default {
  data() {
    return {
      outTypeName: "",
      isDisabled: false,
      isDisabled2: false,
      uploadAction: getBaseUrl() + "/tool/tool-import.json?token=" + getToken(),
      staffInfo: "",
      fullNumber: "",
      toolMap: "",
      toolName: "",
      commonOut: true,
      coatOut: false,
      outboundTypeModalShow: false,
      deleteModalShow: false, //控制删除modal提示 显示
      deleteRowData: {}, //行删除行数据
      isBatchDetele: false, //批量删除标志
      multiselectRowData: [], //复选列数据
      toolReturnModalshow: false, //控制返库明细modal显示
      outboundDetailModalshow: false,
      isReturn: false, //是否是入库明细操作
      outType: outType,
      toolStatus: toolStatus,
      baseList: [],
      newSupplierList: [],
      toolPartList: [],
      supplierList: [],
      typeList: [],
      typeId: null,
      searchCondition: {
        fullNumber: "",
        toolNumber: "",
        number: "",
        typeId: null,
        dateInterval: [],
        page: 1,
        rows: 10
      },
      tableData: {
        total: 0, // 总页数
        rows: [] // 每页条数
      },
      toolTableColumns: [
        {
          title: "制件编码",
          key: "partCode"
        },
        {
          title: "制件名称",
          key: "partName"
        }
      ],
      tableColumns: [
        {
          title: "物料条码",
          key: "fullNumber",
          minWidth: 250,
          fixed: "left"
        },       
        {
          title: "物料图号",
          key: "toolMap",
          minWidth: 150,
          fixed: "left"
        },
        {
          title: "物料名称",
          key: "toolName",
          minWidth: 160,
          fixed: "left"
        },
        {
          title: "物料编码",
          key: "toolNumber",
          minWidth: 150
        },
        /*{
          title: "入库编码",
          key: "warehouseCode",
          width: 180
        },*/
        {
          title: "物料类型",
          key: "typeName",
          minWidth: 100
        },
        {
          title: "库位",
          key: "warehouse",
          minWidth: 100
        },
        {
          title: "库管员",
          key: "keeper",
          minWidth: 100
        },
        {
          title: "供应商",
          key: "supplierName",
          minWidth: 200
        },
        {
          title: "物料状态",
          key: "toolState",
          minWidth: 120,
          render: function(h, params) {
            let statusStr = "";
            switch (params.row.toolState) {
              case 1:
                statusStr = "在库";
                break;
              case 2:
                statusStr = "在用";
                break;
              case 3:
                statusStr = "待刃磨";
                break;
              case 4:
                statusStr = "刃磨待送检";
                break;
              case 5:
                statusStr = "已送涂";
                break;
              case 6:
                statusStr = "涂层";
                break;
              case 7:
                statusStr = "刃磨待检";
                break;
              case 8:
                statusStr = "交回在库";
                break;
              case 9:
                statusStr = "质检完成";
                break;
              case 10:
                statusStr = "已报废";
                break;
            }
            return h("div", statusStr);
          }
        },
       /*       
        {
          title: "物料数量",
          key: "toolAmount",
          minWidth: 100
        },*/
        {
          title: "已加工次数",
          key: "processTimes",
          minWidth: 100
        },
        {
          title: "单次加工标准",
          key: "processEach",
          minWidth: 110
        },
        {
          title: "本次加工数量",
          key: "processCur",
          minWidth: 110
        },
        {
          title: "已加工数量",
          key: "processAmount",
          minWidth: 110
        },
        {
          title: "理论加工数量",
          key: "processTotal",
          minWidth: 110
        },
        {
          title: "已涂层次数",
          key: "coatTimes",
          minWidth: 100
        },
        {
          title: "已刃磨次数",
          key: "repairTimes",
          minWidth: 100
        },
        {
          title: "最大刃磨量",
          key: "grindingMax",
          minWidth: 100
        },
        {
          title: "累计刃磨量",
          minWidth: 100,
          key: "repairAmount"
        },
        {
          title: "操作人",
          key: "updateUserName",
          minWidth: 100
        },
        {
          title: "操作时间",
          key: "updateTime",
          minWidth: 150,
          render: function(h, params) {
            return h(
              "div",
              dateFormat(new Date(params.row.updateTime), "yyyy-MM-dd HH:mm:ss")
            );
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
                      params.row.toolState !== 1 && params.row.toolState !== 8,
                    size: "small"
                  },
                  style: {
                    marginRight: "5px",
                    display:
                      params.row.toolState === 9 ? "none" : "inline-block"
                  },
                  on: {
                    click: () => {
                      this.outbound(params.index);
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
                      params.row.toolState !== 9 ? "none" : "inline-block"
                  },
                  on: {
                    click: () => {
                      this.returnTool(params.index);
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
                    display: params.row.isScrip == 1 ? "inline-block" : "none"
                  },
                  on: {
                    click: () => {
                      this.remove(params.index);
                    }
                  }
                },
                "报废"
              )
            ]);
          }
        }
      ],
      formValidate: tool_form_config.formValidate, //warehouse form表单字段
      ruleValidate: tool_form_config.ruleValidate //warehouse form表单验证规则
    };
  },
  created() {
    const self = this;
    self.getListData();
    self.getCoatSupplierListData();
    self.getBaseListData();
    self.getTypeListData();
  },
  methods: {
    getBaseListData() {
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/tool-base-list",
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
    getTypeListData() {
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/tool-type-list",
          method: "post",
          isAuth: true
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            self.typeList = data.data;
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
        fullNumber: self.searchCondition.fullNumber,
        toolNumber: self.searchCondition.toolNumber,
        number: self.searchCondition.number,
        typeId: self.searchCondition.typeId,
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
          url: "/tool/tool-page-list",
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

    getCoatSupplierListData() {
      const self = this;
      let para = {
        isCoat: 1
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/base/supplier-list",
          isAuth: true,
          method: "post",
          params: para
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            self.supplierList = data.data;
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },

    toolReturn() {
      this.isDisabled = true;
      let para = {
        pkId: this.formValidate.pkId,
        toolNumber: this.formValidate.toolNumber,
        toolName: this.formValidate.toolName,
        fullNumber: this.formValidate.fullNumber,
        inType: 2,
        warehouse: this.formValidate.warehouse,
        deliever: this.formValidate.deliever,
        remark: this.formValidate.remark
      };
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/tool-return",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          self.toolReturnModalshow = false;
          if (res.data.code === 200) {
            self.$Message.success("刀具返库成功!");
            self.isDisabled = false;
            self.getListData();
          } else {
            self.$Notice.error({
              title: "错误提示",
              desc: res.data.info
            });
            self.isDisabled = false;
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },

    toolOutbound() {
      const self = this;
      if (!!!this.formValidate.staffName) {
        self.$Message.error("请检查领用人信息!");
        return;
      }
      self.isDisabled2 = true;
      let para = {
        toolId: self.formValidate.pkId,
        toolNumber: self.formValidate.toolNumber,
        fullNumber: self.formValidate.fullNumber,
        toolName: self.formValidate.toolName,
        toolMap: self.formValidate.toolMap,
        warehouse: self.formValidate.warehouse,
        outType: self.formValidate.outType,
        outTypeName: self.outTypeName,
        departmentId: self.formValidate.departmentId,
        departmentName: self.formValidate.departmentName,
        teamId: self.formValidate.teamId,
        teamName: self.formValidate.teamName,
        staffCode: self.formValidate.staffCode,
        staffName: self.formValidate.staffName,
        typeId: self.formValidate.typeId,
        supplierId: self.formValidate.coatSupplierId,
        supplierName: self.formValidate.coatSupplierName,
        remark: self.formValidate.remark
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/tool-outbound",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          self.outboundDetailModalshow = false;
          if (res.data.code === 200) {
            self.$Message.success("刀具出库成功!");
            self.isDisabled2 = false;
            self.getListData();
          } else {
            self.$Notice.error({
              title: "错误提示",
              desc: res.data.info
            });
            self.isDisabled2 = false;
          }
        });
    },

    searchStaff() {
      const self = this;
      if (!!!self.formValidate.staffCode) {
        return;
      }
      let para = {
        staffCode: self.formValidate.staffCode
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/base/staff-get-by-code.json",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            if (data.data !== null && data.data !== undefined) {
              self.formValidate.departmentId = data.data.departmentId;
              self.formValidate.departmentName = data.data.departmentName;
              self.formValidate.teamId = data.data.teamId;
              self.formValidate.teamName = data.data.teamName;
              self.formValidate.staffName = data.data.staffName;
              self.staffInfo =
                data.data.departmentName + "-" + self.formValidate.staffName;
            } else {
              self.$Message.error("无法找到与职工号对应的职工");
              self.formValidate.staffCode = "";
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

    returnTool(index) {
      const self = this;
      self.formValidate = objCopy(self.tableData.rows[index], {});
      self.staffInfo = "";
      self.toolReturnModalshow = true;
    },
    remove(index) {
      const self = this;
      self.deleteRowData = self.tableData.rows[index];
      let para = {
        fullNumber: self.deleteRowData.fullNumber
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/apply-get-by-full.json",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          if (res.data.code === 200) {
            if (res.data.data !== null && res.data.data !== undefined) {
              if (res.data.data.applyStatus === 2) {
                self.deleteModalShow = true;
                self.isBatchDetele = false;
                self.resiontext = res.data.data.scripResion;
              } else {
                self.$Message.error("该刀具尚未通过报废审核!");
              }
            } else {
              self.$Message.error("该刀具尚未通过报废审核!");
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
    supplierListSelectChange(item) {
      let supplierList = this.supplierList;
      if (item !== undefined) {
        for (const supplier of supplierList) {
          if (supplier.pkId === item) {
            this.formValidate.coatSupplierName = supplier.supplierName;
          }
        }
      }
    },

    deleteRowDate(pkId, fullNumber) {
      let para = {
        pkId: pkId,
        fullNumber: fullNumber
      };
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/tool-delete-by-id",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          self.deleteModalShow = false;
          if (res.data.code === 200) {
            self.$Message.success("刀具已成功报废!");
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
          this.deleteRowData.fullNumber
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
    outTypeChange(item) {
      const self = this;
      if (item === 3) {
        self.formValidate.staffCode = "";
        self.staffInfo = "";
        self.formValidate.departmentId = "";
        self.formValidate.departmentName = "";
        self.formValidate.staffName = "";
        self.commonOut = false;
        self.coatOut = true;
      } else {
        //this.$refs['formValidate'].resetFields();
        self.formValidate.staffName = "";
        self.coatOut = false;
        self.commonOut = true;
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
    outbound(index) {
      const self = this;
      self.outboundDetailModalshow = true;
      self.staffInfo = "";
      self.isReturn = true;
      self.formValidate = {};
      self.formValidate = objCopy(self.tableData.rows[index], {});
      self.formValidate.typeId = self.tableData.rows[index].typeId;
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
    handleSuccess(res) {
      if (res.code == 200) {
        const self = this;
        self.getListData();
        self.$Message.success("文件导入成功!");
      } else {
        self.$Message.error("文件导入失败!");
      }
    },
    handleError() {
      const self = this;
      self.getListData();
      self.$Message.success("文件导入失败!");
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
      let para = "isHead=2&token=" + getToken();
      if (!!self.searchCondition.toolNumber) {
        para = para + "&toolNumber=" + self.searchCondition.toolNumber;
      }
      if (!!self.searchCondition.number) {
        para = para + "&number=" + self.searchCondition.number;
      }
      if (!!self.searchCondition.typeId) {
        para = para + "&typeId=" + self.searchCondition.typeId;
      }
      window.location.href = getBaseUrl() + "/tool/tool-list-export?" + para;
    },

    rowClassName(row, index) {
      // return "demo-table-warning-row";
      if (row.processEach > row.processCur) {
        return "demo-table-error-row";
      }
      if (row.repairAmount === undefined) {
        row.repairAmount = 0;
      }
      if (row.repairCordon !== undefined) {
        console.log(row.repairAmount+'  '+ row.repairCordon +'  '+(row.repairAmount >= row.repairCordon));
        if (row.repairAmount >= row.repairCordon) {
          return 'demo-table-warning-row';
        }
      }
    },

    //form 校验方法
    handleReturnSubmit(name) {
      const self = this;
      if (!!self.formValidate.warehouse) {
        self.toolReturn();
      } else {
        self.$Message.error("库位不能为空!");
      }
    },
    handleReturnReset(name) {
      this.toolReturnModalshow = false;
      this.$refs[name].resetFields();
    },
    //form 校验方法
    modalSureBtn() {
      const self = this;
      self.toolOutbound();
    },
    //删除modal 取消方法
    modalCancelBtn() {
      const self = this;
      self.outboundTypeModalShow = false;
      self.staffInfo = "";
      self.searchStaff();
    },
    handleOutboundSubmit(name) {
      const self = this;
      if (self.formValidate.outType == 1) {
        self.outTypeName = "领用出库";
      } else if (self.formValidate.outType == 2) {
        self.outTypeName = "刃磨出库";
      } else if (self.formValidate.outType == 3) {
        self.outTypeName = "涂层出库";
      }
      // self.receiveInfo = self.formValidate.staffName;
      self.$refs[name].validate(valid => {
        if (valid) {
          self.outboundTypeModalShow = true;
        } else {
          self.$Message.error("请输入正确信息!");
        }
      });
    },
    handleOutboundReset(name) {
      const self = this;
      self.outboundDetailModalshow = false;
      self.$refs[name].resetFields();
    },
    print(pkId) {
      window.open(
        "http://10.36.10.11:8080/decision/view/report?viewlet=tool.cpt&pkId=" +
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
