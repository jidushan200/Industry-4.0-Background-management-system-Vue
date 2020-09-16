<template>
  <div>
    <Card>
      <Row>
        <Form ref="formInline" :model="searchCondition" inline :label-width="85">
          <Col span="18">
            <FormItem prop="composeNumber" label="刀条组合条码">
              <Input type="text" v-model="searchCondition.fullNumber" placeholder="请输入刀条组合条码"></Input>
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
              <Button
                type="warning"
                style="margin-right: 5px;"
                v-if="applyAuth"
                @click="addApply()"
              >
                <Icon type="md-add"></Icon>报废申请
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

    <Modal v-model="toolApplyModalshow" :title="isModify?'报废审核':'报废申请'" width="1060" draggable>
      <Form
        ref="formValidate"
        :model="formValidate"
        :rules="ruleValidate"
        :label-width="100"
        inline
      >
        <Row>
          <Col span="6">
            <FormItem label="刀条组合条码" prop="composeNumber">
              <Input
                style="width:160px"
                v-model="formValidate.composeNumber"
                @keyup.enter.native="searchBlade()"
                :readonly="isAudit"
              >
                <Icon type="ios-search" slot="suffix" />
              </Input>
            </FormItem>
          </Col>
          <Col span="6">
            <FormItem label="刀条组合名称" prop="headName">
              <Input style="width:160px" v-model="headName" readonly />
            </FormItem>
          </Col>
          <Col span="6">
            <FormItem label="已加工数量" prop="productionQty">
              <Input style="width:160px" v-model="productionQty" readonly />
            </FormItem>
          </Col>
          <Col span="6">
            <FormItem label="报废原因" prop="scrapResion">
              <Select
                style="width:160px"
                v-model="formValidate.scrapResion"
                ref="scrapType"
                placeholder="请选择报废原因"
                :disabled="isAudit"
              >
                <Option
                  v-for="item in scrapType"
                  :value="item.value"
                  :key="item.value"
                >{{ item.name }}</Option>
              </Select>
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Table
            ref="myTable"
            border
            :columns="columns"
            :data="detailList"
            style="margin-bottom:5px;"
          ></Table>
        </Row>
        <Row>
          <Col span="6">
            <FormItem label="申请部门" prop="applyDepartment">
              <Input style="width:160px" v-model="applyDepartment" readonly />
            </FormItem>
          </Col>
          <Col span="6">
            <FormItem label="申请人" prop="applierCode">
              <Input style="width:160px" v-model="staffInfo" readonly></Input>
            </FormItem>
          </Col>

          <Col span="12">
            <FormItem label="原因说明" prop="scrapRemark">
              <Input
                style="width:420px"
                v-model="formValidate.scrapRemark"
                placeholder="输入原因说明"
                :readonly="isAudit"
              />
            </FormItem>
          </Col>
        </Row>

        <Row
          style="border-top:1px solid #e3e3e3;padding-top: 40px;margin-top: 20px;"
          v-if="isModify"
        >
          <Col span="24" class="ivu-form">
            <div class="ivu-form-item">
              <label class="ivu-form-item-label" style="width: 100px;text-align: right;">审核进度</label>
              <div class="ivu-form-item-content" style="margin-left: 100px; ">
                <Steps :current="currentStep">
                  <Step title="提交" content="分厂填写刀具报废申请并提交"></Step>
                  <Step title="审核" content="工艺部审核"></Step>
                  <Step title="执行" content="待库房执行报废"></Step>
                </Steps>
              </div>
            </div>
          </Col>
          <Col span="8" v-if="radioAudit" class="ivu-form">
            <FormItem label="审核意见" prop="agreeFlag">
              <RadioGroup v-model="agreeFlag">
                <Radio :label="agreeStatus">同意</Radio>
                <Radio :label="disagreeStatus">不同意</Radio>
              </RadioGroup>
            </FormItem>
          </Col>
          <Col span="8" v-if="radioAudit">
            <FormItem label="审核部门" prop="auditDepartment">
              <Input style="width:170px" v-model="auditDepartment" readonly />
            </FormItem>
          </Col>
          <Col span="8" v-if="radioAudit">
            <FormItem label="审核人" prop="auditStaffInfo">
              <Input style="width:170px" v-model="auditStaffInfo" readonly />
            </FormItem>
          </Col>
          <Col span="24" v-if="radioAudit">
            <FormItem label="意见描述" prop="auditRemark">
              <Input mv-model="auditRemark" placeholder="意见描述" style="width:930px;" />
            </FormItem>
          </Col>
        </Row>
      </Form>
      <div slot="footer">
        <Button
          @click="handleSave('formValidate')"
          v-if="btnSave && applyAuth"
          type="primary"
          :disabled="isDisabled"
        >保存</Button>
        <Button
          @click="handleSubmit('formValidate')"
          v-if="btnSubmit && applyAuth"
          type="warning"
          :disabled="isDisabled"
        >提交</Button>
        <Button @click="handleAudit()" type="primary" v-if="btnAudit" :disabled="isDisabled">提交</Button>
        <Button @click="handleReset('formValidate')" style="margin-left: 8px">关闭</Button>
      </div>
    </Modal>

    <Modal
      v-model="deleteModalShow"
      width="250"
      title="删除报告"
      @on-ok="deleteModalSureBtn"
      @on-cancel="deleteModalCancelBtn"
    >
      <p>确定删除所选报告？</p>
    </Modal>
  </div>
</template>

<script>
import axios from "@/libs/api.request";
import blade_scrap_manage_form_config from "./blase-scrap-manage-form-config.js";
import { dateFormat, objCopy, hasOneOf } from "@/libs/tools.js";
import qs from "qs";
export default {
  name: "page",
  computed: {
    access() {
      return this.$store.state.user.userData.data.access;
    },
    //保存、提交、删除（质检部）
    applyAuth() {
      return hasOneOf(["0103040601"], this.access);
    },
    //审核(工艺部)
    technologyAuth() {
      return hasOneOf(["0103040602"], this.access);
    }
  },
  components: {},
  data() {
    return {
      isDisabled: false,
      productionQty: "",
      auditDepartment: "",
      applyDepartment: "",
      isAudit: false,
      auditStaffInfo: "",
      staffInfo: "",
      headName: "",
      isModify: false,
      scrapType: [
        {
          name: "正常报废",
          value: 1
        },
        {
          name: "异常报废",
          value: 2
        }
      ],
      radioAudit: false,
      btnSave: true, //保存按钮可见
      btnSubmit: true, //提交按钮可见
      btnAudit: false, //审核按钮可见
      currentStep: 0, //当前步骤
      auditRemark: "", //审核备注
      agreeFlag: 2,
      agreeStatus: 2,
      disagreeStatus: -1,
      applyStatusList: [],
      deleteModalShow: false, //控制删除modal提示 显示
      deleteRowData: {}, //行删除行数据
      toolApplyModalshow: false, //控制明细modal显示
      searchCondition: {
        fullNumber: "",
        warehouseCode: "",
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
          title: "刀条组合条码",
          fixed: "left",
          key: "composeNumber",
          minWidth: 150
        },
        {
          title: "刀条组合名称",
          fixed: "left",
          key: "headName",
          minWidth: 220
        },
        {
          title: "刀条",
          align: "center",
          children: [
            {
              title: "刀条编码",
              key: "detailList",
              align: "center",
              minWidth: 120,
              render: (h, params) => {
                return h(
                  "div",
                  {
                    attrs: {
                      class: "subCol"
                    }
                  },
                  [
                    h(
                      "ul",
                      undefined != this.tableData.rows[params.index].detailList
                        ? this.tableData.rows[params.index].detailList.map(
                            item => {
                              return h("li", {}, item.toolNumber);
                            }
                          )
                        : ""
                    )
                  ]
                );
              }
            },
            {
              title: "刀条名称",
              key: "detailList",
              align: "center",
              minWidth: 200,
              render: (h, params) => {
                return h(
                  "div",
                  {
                    attrs: {
                      class: "subCol"
                    }
                  },
                  [
                    h(
                      "ul",
                      undefined != this.tableData.rows[params.index].detailList
                        ? this.tableData.rows[params.index].detailList.map(
                            item => {
                              return h("li", {}, item.toolName);
                            }
                          )
                        : ""
                    )
                  ]
                );
              }
            },
            {
              title: "报废数量",
              key: "detailList",
              align: "center",
              width: 100,
              render: (h, params) => {
                return h(
                  "div",
                  {
                    attrs: {
                      class: "subCol"
                    }
                  },
                  [
                    h(
                      "ul",
                      undefined != this.tableData.rows[params.index].detailList
                        ? this.tableData.rows[params.index].detailList.map(
                            item => {
                              return h("li", {}, item.scrapQty);
                            }
                          )
                        : ""
                    )
                  ]
                );
              }
            }
          ]
        },
        {
          title: "申请部门",
          key: "departmentName",
          minWidth: 150
        },
        {
          title: "申请人",
          key: "applierName",
          minWidth: 120
        },
        {
          title: "申请时间",
          key: "createTime",
          minWidth: 150,
          render: function(h, params) {
            return h(
              "div",
              dateFormat(new Date(params.row.createTime), "yyyy-MM-dd HH:mm:ss")
            );
          }
        },
        {
          title: "报废原因",
          width: 120,
          key: "scrapResion",
          render: function(h, params) {
            let statusStr = "";
            switch (params.row.scrapResion) {
              case 1:
                statusStr = "正常报废";
                break;
              case 2:
                statusStr = "异常报废";
                break;
            }
            return h("div", statusStr);
          }
        },
        {
          title: "申请状态",
          key: "applyStatus",
          width: 180,
          render: function(h, params) {
            let statusStr = "";
            switch (params.row.applyStatus) {
              case 0:
                statusStr = "报废申请待提交";
                break;
              case 1:
                statusStr = "待工艺部审核";
                break;
              case -1:
                statusStr = "工艺部审核未通过，已驳回";
                break;
              case 2:
                statusStr = "待库房执行报废";
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
                      this.show(params.row.pkId,params.row.headName);
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
                    display: this.applyAuth ? "inline-block" : "none"
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
      ],
      detailList: [],
      columns: [
        {
          title: "刀条编码",
          key: "toolNumber",
          minWidth: 120
        },
        {
          title: "刀条名称",
          key: "toolName",
          minWidth: 320
        },
        {
          title: "刀条图号",
          key: "toolMap",
          minWidth: 120
        },
        {
          title: "报废数量",
          key: "scrapQty",
          minWidth: 120,
          render: (h, params) => {
            return h("Input", {
              props: {
                value: this.detailList[params.index].scrapQty
              },
              on: {
                input: val => {
                  this.detailList[params.index].scrapQty = val;
                }
              }
            });
          }
        }
      ],
      formValidate: blade_scrap_manage_form_config.formValidate, //user form表单字段
      ruleValidate: blade_scrap_manage_form_config.ruleValidate //user form表单验证规则
    };
  },
  created() {
    const self = this;
    self.getListData();
  },
  methods: {
    getListData() {
      const self = this;
      if (self.technologyAuth) {
        self.applyStatusList.push(1);
      }
      if (self.applyAuth) {
        self.formValidate.applierId =
          self.$store.state.user.userData.data.userId;
        self.applyStatusList = [];
      }
      let para = {
        applyStatusList: self.applyStatusList.toString(),
        toolNumber: self.searchCondition.toolNumber,
        composeNumber: self.searchCondition.composeNumber,
        page: self.searchCondition.page,
        rows: self.searchCondition.rows
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/blade-scrap-apply-page-list.json",
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

    addNewApply() {
      this.isDisabled = true;
      const self = this;
      let para = {
        composeNumber: self.formValidate.composeNumber,
        detailJson: JSON.stringify(self.detailList),
        departmentId: self.formValidate.departmentId,
        departmentName: self.formValidate.departmentName,
        scrapResion: self.formValidate.scrapResion,
        applyStatus: self.formValidate.applyStatus,
        scrapRemark: self.formValidate.scrapRemark
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/x-www-form-urlencoded"
          },
          url: "/tool/blade-scrap-apply-add",
          method: "post",
          isAuth: true,
          data: qs.stringify(para)
        })
        .then(function(res) {
          self.toolApplyModalshow = false;
          if (res.data.code === 200) {
            self.$Message.success("报废申请已保存!");
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

    editApply() {
      this.isDisabled = true;
      const self = this;
      let para = {
        pkId: self.formValidate.pkId,
        detailJson: JSON.stringify(self.detailList),
        scrapResion: self.formValidate.scrapResion,
        applyStatus: self.formValidate.applyStatus,
        scrapRemark: self.formValidate.scrapRemark
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/x-www-form-urlencoded"
          },
          url: "/tool/blade-scrap-apply-update",
          method: "post",
          isAuth: true,
          data: qs.stringify(para)
        })
        .then(function(res) {
          self.toolApplyModalshow = false;
          if (res.data.code === 200) {
            self.$Message.success("报废申请内容已变更!");
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
    deleteRowDate(pkId) {
      let para = {
        pkId: pkId
      };
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/blade-scrap-apply-delete-by-id",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          self.deleteModalShow = false;
          if (res.data.code === 200) {
            self.$Message.success("报废申请删除成功!");
            self.getListData();
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },

    searchCurrentStaff() {
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/base/staff-get-current.json",
          method: "post",
          isAuth: true
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            if (self.isModify == false) {
              self.applyDepartment = data.data.departmentName;
              self.staffInfo = data.data.staffName;
              self.formValidate.departmentId = data.data.departmentId;
              self.formValidate.departmentName = data.data.departmentName;
            } else {
              self.auditDepartment = data.data.departmentName;
              self.formValidate.auitDepartmentId = data.data.departmentId;
              self.auditStaffInfo = data.data.staffName;
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
      this.formValidate = {};
      self.changePage(1);
    },
    doReset(name) {
      const self = this;
      self.$refs[name].resetFields();
      self.getListData();
    },

    show(pkId,headName) {
      const self = this;
      self.toolApplyModalshow = true;
      self.isModify = true;
      self.formValidate.pkId = pkId;
      let para = {
        pkId: pkId
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/blade-scrap-apply-get-by-id.json",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            self.formValidate = objCopy(data.data, {});
            self.headName = headName;
            self.detailList = data.data.detailList;
            self.productionQty = data.data.productionQty;
            self.applyDepartment = data.data.departmentName;
            self.staffInfo = data.data.applierName;
            const auditStatus = self.formValidate.applyStatus;
            if (auditStatus == 0 || auditStatus == -1) {
              if (self.applyAuth) {
                self.btnSave = true;
                self.btnSubmit = true;
              } else {
                self.btnSave = false;
                self.btnSubmit = false;
              }

              self.currentStep = 0;
              self.btnAudit = false;
              self.radioAudit = false;
            } else if (auditStatus == 1 || auditStatus == -2) {
              if (self.technologyAuth) {
                self.isAudit = true;
                self.radioAudit = true;
                self.btnAudit = true;
              } else {
                self.isAudit = false;
                self.radioAudit = false;
                self.btnAudit = false;
              }
              self.auditStaffInfo = "";
              self.currentStep = 1;
              self.btnSave = false;
              self.btnSubmit = false;
              self.agreeFlag = 2;
              self.agreeStatus = 2;
              self.disagreeStatus = -1;
              self.searchCurrentStaff();
            } else if (auditStatus == 2) {
              self.currentStep = 2;
              self.isAudit = true;
              self.btnSave = false;
              self.btnSubmit = false;
              self.btnAudit = false;
              self.radioAudit = false;
            }
          } else {
            self.$Notice.error({
              title: "错误提示",
              desc: res.data.info
            });
          }
        });
    },
    remove(index) {
      this.deleteRowData = this.tableData.rows[index];
      this.deleteModalShow = true;
    },
    addApply() {
      const self = this;
      self.isModify = false;
      self.isAudit = false;
      self.btnSave = true;
      self.btnSubmit = true;
      self.btnAudit = false;
      self.radioAudit = false;
      self.toolApplyModalshow = true;
      self.detailList = [];
      self.staffInfo = "";
      self.formValidate = {};
      self.searchCurrentStaff();
      self.$refs["formValidate"].resetFields();
    },
    deleteModalSureBtn() {
      this.deleteRowDate(this.deleteRowData.pkId);
    },
    //删除modal 取消方法
    deleteModalCancelBtn() {
      if (this.isBatchDetele) {
        this.multiselectRowData = [];
      } else {
        this.deleteRowData = {};
      }
    },
    handleSave(name) {
      const self = this;
      self.formValidate.applyStatus = 0;
      self.$refs[name].validate(valid => {
        if (valid) {
          if (self.isModify) {
            self.editApply();
          } else {
            self.addNewApply();
          }
        } else {
          self.$Message.error("请输入正确信息!");
        }
      });
    },
    searchBlade() {
      const self = this;
      if (!!!self.formValidate.composeNumber) {
        return;
      } else {
        let para = {
          composeNumber: self.formValidate.composeNumber
        };
        axios
          .request({
            headers: {
              "Content-Type": "application/json; charset=UTF-8"
            },
            url: "/tool/scrap-get-by-compose-number.json",
            method: "post",
            isAuth: true,
            params: para
          })
          .then(function(res) {
            const data = res.data;
            if (data.code === 200) {
              self.headName = data.data.headName;
              self.productionQty = data.data.productionQty;
              self.detailList = data.data.detailList;
            } else {
              self.$Notice.error({
                title: "错误提示",
                desc: res.data.info
              });
            }
          });
      }
    },
    handleSubmit(name) {
      const self = this;
      self.formValidate.applyStatus = 1;
      self.$refs[name].validate(valid => {
        if (valid) {
          if (self.isModify) {
            self.editApply();
          } else {
            self.addNewApply();
          }
        } else {
          self.$Message.error("请输入正确信息!");
        }
      });
    },
    handleAudit() {
      this.isDisabled = true;
      const self = this;
      let para = {
        pkId: self.formValidate.pkId,
        composeNumber: self.formValidate.composeNumber,
        applyStatus: self.agreeFlag,
        auditRemark: self.auditRemark,
        departmentId: self.formValidate.auitDepartmentId,
        departmentName: self.auitDepartmentName
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/blade-scrap-apply-audit.json",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          self.toolApplyModalshow = false;
          if (res.data.code === 200) {
            self.$Message.success("报告审核成功!");
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
    handleReset(name) {
      this.toolApplyModalshow = false;
      this.$refs[name].resetFields();
    }
  }
};
</script>

<style>
.subCol > ul > li {
  margin: 0 -18px;
  /* list-style: none;*/
  text-align: center;
  padding: 9px;
  border-bottom: 1px solid #ccc;
  overflow-x: hidden;
}
.subCol > ul > li:last-child {
  border-bottom: none;
}
</style>
