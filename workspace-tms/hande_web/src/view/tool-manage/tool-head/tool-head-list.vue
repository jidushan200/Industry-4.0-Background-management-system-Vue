<template>
  <div>
    <Card>
      <Row>
        <Form ref="formInline" :model="searchCondition" :label-width="65" inline>
          <Col span="18">
            <FormItem label="图号名称" prop="toolNumber">
              <Select
                style="width:158px"
                v-model="searchCondition.toolNumber"
                ref="toolName"
                placeholder="请选择图号名称"
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
            <FormItem prop="fullNumber" label="物料条码">
              <Input type="text" v-model="searchCondition.fullNumber" placeholder="请输入刀具码"></Input>
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

    <Modal v-model="toolReturnModalshow" title="刀具返库" width="600" draggable>
      <Form
        ref="formValidateReturn"
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
        <Button
          @click="handleReturnSubmit('formValidateReturn')"
          :disabled="isDisabled"
          type="primary"
        >保存</Button>
        <Button @click="handleReturnReset('formValidateReturn')" style="margin-left: 8px">取消</Button>
      </div>
    </Modal>

    <Modal v-model="outboundDetailModalshow" title="刀头领用出库" width="600" draggable>
      <Form
        ref="formValidateOut"
        :model="formValidate"
        :rules="ruleValidate"
        :label-width="100"
        inline
      >
        <FormItem label="物料编码" prop="toolNumber">
          <Input style="width:170px" v-model="formValidate.toolNumber" readonly></Input>
        </FormItem>
        <FormItem label="刀具名称" prop="toolName">
          <Input style="width:170px" v-model="formValidate.toolName" readonly></Input>
        </FormItem>
        <FormItem label="物料图号" prop="toolMap">
          <Input style="width:170px" v-model="formValidate.toolMap" readonly />
        </FormItem>
        <FormItem label="库位" prop="warehouse">
          <Input style="width:170px" v-model="formValidate.warehouse" readonly />
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
        <FormItem label="领用原因" prop="receiveResion" style="display:none">
          <Input style="width:170px" v-model="formValidate.receiveResion" placeholder="输入领用原因" />
        </FormItem>
        <FormItem label="说明" prop="remark">
          <Input style="width:450px" v-model="formValidate.remark" placeholder="输入说明" />
        </FormItem>
      </Form>
      <div slot="footer">
        <Button
          @click="handleOutboundSubmit('formValidateOut')"
          :disabled="isDisabled2"
          type="primary"
        >出库</Button>
        <Button @click="handleOutboundReset('formValidateOut')" style="margin-left: 8px">取消</Button>
      </div>
    </Modal>

    <tool-head-model ref="toolHeadModel" @search="getListData"></tool-head-model>
    <Modal
      v-model="deleteModalShow"
      width="250"
      title="刀具报废"
      @on-ok="deleteModalSureBtn"
      @on-cancel="deleteModalCancelBtn"
    >
      <p>是否确定报废该刀具？</p>
    </Modal>
  </div>
</template>

<script>
import axios from "@/libs/api.request";
import { getToken, getBaseUrl } from "@/libs/util";
import { dateFormat, objCopy } from "@/libs/tools.js";
import toolHeadModel from "./tool-head-modal.vue";

import tool_head_form_config from "./tool-head-form-config.js";
export default {
  components: {
    toolHeadModel
  },
  data() {
    return {
      isDisabled: false,
      isDisabled2: false,
      outboundDetailModalshow: false,
      toolReturnModalshow: false,
      staffInfo: "",
      fullNumber: "",
      toolMap: "",
      toolName: "",
      warehouse: "",
      repairTime: null,
      departmentId: null,
      departmentName: "",
      commonOut: true,
      deleteModalShow: false, //控制删除modal提示 显示
      uploadAction: getBaseUrl() + "/tool/tool-import.json?token=" + getToken(),
      baseList: [],
      searchCondition: {
        fullNumber: "",
        toolNumber: "",
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
          key: "toolNumber",
          width: 150,
          fixed: "left"
        },
        {
          title: "入库编码",
          key: "warehouseCode",
          width: 180,
          fixed: "left"
        },
        {
          title: "物料名称",
          key: "toolName",
          width: 220,
          fixed: "left"
        },
        {
          title: "物料图号",
          key: "toolMap",
          width: 150
        },
        {
          title: "供应商",
          key: "supplierName",
          width: 250
        },
        {
          title: "物料状态",
          key: "toolState",
          width: 120,
          render: function(h, params) {
            let statusStr = "";
            switch (params.row.toolState) {
              case 1:
                statusStr = "在库";
                break;
              case 2:
                statusStr = "使用中";
                break;
              case 3:
                statusStr = "待刃磨";
                break;
              case 4:
                statusStr = "刃磨";
                break;
              case 5:
                statusStr = "已送涂";
                break;
              case 7:
                statusStr = "刃磨待检";
                break;
              case 8:
                statusStr = "在库";
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
        {
          title: "物料条码",
          key: "fullNumber",
          width: 250
        },
        {
          title: "物料数量",
          key: "toolAmount",
          width: 120
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
          title: "理论加工数量",
          key: "processTotal",
          width: 120
        },

        {
          title: "已刃磨次数",
          key: "repairTimes",
          width: 120
        },
        {
          title: "最大刃磨量",
          key: "grindingMax",
          width: 120
        },
        {
          title: "累计刃磨量",
          width: 120,
          key: "repairAmount"
        },
        {
          title: "操作人",
          key: "updateUserName",
          width: 120
        },
        {
          title: "操作时间",
          key: "updateTime",
          width: 150,
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
                      !(params.row.toolState == 1 || params.row.toolState == 8),
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
                    display: params.row.toolState == 9 ? "inline-block" : "none"
                  },
                  on: {
                    click: () => {
                      this.back(params.row);
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
      formValidate: tool_head_form_config.formValidate, // form表单字段
      ruleValidate: tool_head_form_config.ruleValidate // form表单验证规则
    };
  },
  created() {
    const self = this;
    self.getListData();
    self.getBaseListData();
    self.getCoatSupplierListData();
  },
  methods: {
    getBaseListData() {
      const self = this;
      let para = {
        typeId: 3
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/tool-base-list",
          isAuth: true,
          params: para,
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
        isHead: 1,
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
              self.formValidate.needDepartmentId = data.data.departmentId;
              self.formValidate.needDepartmentName = data.data.departmentName;
              self.formValidate.teamId = data.data.teamId;
              self.formValidate.teamName = data.data.teamName;
              self.formValidate.staffName = data.data.staffName;
              self.staffInfo =
                data.data.departmentName + "-" + data.data.staffName;
            } else {
              self.$Message.error("无法找到与职工号对应的职工");
              self.formValidate.staffCode='';
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
    back(row) {
      const self = this;
      self.formValidate = objCopy(row, {});
      self.staffInfo = "";
      self.toolReturnModalshow = true;
      self.$refs["formValidateReturn"].resetFields();
      self.formValidate.warehouse = "";
      self.formValidate.deliever = "";
      self.formValidate.remark = "";
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
    handleReturnSubmit(name) {
      this.$refs[name].validate(valid => {
        if (valid) {
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
        } else {
          this.$Message.error("请输入正确信息!");
        }
      });
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

    deleteModalSureBtn() {
      this.deleteRowDate(
        this.deleteRowData.pkId,
        this.deleteRowData.fullNumber
      );
    },
    //删除modal 取消方法
    deleteModalCancelBtn() {
      this.deleteRowData = {};
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

    handleReturnReset(name) {
      this.toolReturnModalshow = false;
      this.$refs[name].resetFields();
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
    addToolHead() {
      const self = this;
      self.$refs.toolHeadModel.showModal();
    },
    outbound(index) {
      this.outboundDetailModalshow = true;
      this.staffInfo = "";
      this.isReturn = true;
      this.formValidate = objCopy(this.tableData.rows[index], {});
      this.formValidate.typeId=this.tableData.rows[index].typeId;
    },
    handleOutboundReset(name) {
      const self = this;
      self.outboundDetailModalshow = false;
      self.$refs[name].resetFields();
    },
    //form 校验方法
    handleOutboundSubmit(name) {
      const self = this;
      this.$refs[name].validate(valid => {
        if (valid) {
          this.isDisabled2 = true;
          let para = {
            toolId: self.formValidate.pkId,
            toolNumber: self.formValidate.toolNumber,
            fullNumber: self.formValidate.fullNumber,
            toolName: self.formValidate.toolName,
            toolMap: self.formValidate.toolMap,
            typeId:self.formValidate.typeId,
            warehouse: self.formValidate.warehouse,
            outType: 2,
            departmentId: self.formValidate.needDepartmentId,
            departmentName: self.formValidate.needDepartmentName,
            teamId: self.formValidate.teamId,
            teamName: self.formValidate.teamName,
            staffCode: self.formValidate.staffCode,
            staffName:self.formValidate.staffName,
            toolAmount: self.formValidate.toolAmount,
            supplierId: self.formValidate.coatSupplierId,
            supplierName: self.formValidate.coatSupplierName,
            receiveResion: self.formValidate.receiveResion,
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
            })
            .catch(function(err) {
              console.log(err);
            });
        } else {
          this.$Message.error("请输入正确信息!");
        }
      });
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
      self.$Message.error("文件导入失败!");
    },
    exportData() {
      const self = this;
      window.location.href =
        getBaseUrl() + "/tool/tool-list-export?isHead=1&token=" + getToken();
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
