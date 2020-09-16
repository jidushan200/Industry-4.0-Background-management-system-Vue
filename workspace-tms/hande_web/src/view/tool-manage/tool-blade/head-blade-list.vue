<template>
  <div>
    <Card>
      <Row>
        <Form ref="formInline" :model="searchCondition" :label-width="85" inline>
          <Col span="16">
            <FormItem label="刀条组合条码" prop="composeNumber">
              <Input type="text" v-model="searchCondition.composeNumber" placeholder="请输入刀条组合条码"></Input>
            </FormItem>
            <FormItem label="刀条组合码" prop="headNumber">
              <Input type="text" v-model="searchCondition.headNumber" placeholder="请输入刀条组合码"></Input>
            </FormItem>

            <FormItem>
              <Button type="primary" style="margin-left: -60px" @click="doSearch('formInline')">搜索</Button>
            </FormItem>
            <FormItem style="margin-left: -65px">
              <Button type="dashed" @click="doReset('formInline')">重置</Button>
            </FormItem>
          </Col>
          <Col span="8">
            <FormItem style="width: 100%;text-align: right;">
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

    <Modal v-model="outboundModalshow" title="送刃磨" width="700" draggable>
      <Form
        ref="formValidateOut"
        :model="formValidate"
        :rules="ruleValidate"
        :label-width="100"
        inline
      >
        <Row>
          <Col span="12">
            <FormItem label="刀条组合条码" prop="composeNumber">
              <Input style="width:200px" v-model="composeNumber" readonly />
            </FormItem>
          </Col>
          <Col span="12">
            <FormItem label="刀条组合名称" prop="headName">
              <Input style="width:200px" v-model="headName" readonly />
            </FormItem>
          </Col>
        </Row>

        <Row>
          <Table
            ref="myTable"
            border
            :columns="columns"
            :data="composeDetailList"
            height="160"
            style="margin-bottom:5px;"
          ></Table>
        </Row>
        <Row>
          <Col span="12">
            <FormItem label="说明" prop="remark">
              <Input style="width:530px" v-model="formValidate.remark" placeholder="输入说明" />
            </FormItem>
          </Col>
        </Row>
      </Form>
      <div slot="footer">
        <Button
          @click="handleOutboundSubmit('formValidateOut')"
          type="primary"
          :disabled="isDisabled"
        >提交</Button>
        <Button @click="handleOutboundReset('formValidateOut')" style="margin-left: 8px">取消</Button>
      </div>
    </Modal>

    <Modal v-model="supplementModalshow" title="补充刀条" width="700" draggable>
      <Form
        ref="formValidateOut"
        :model="formValidate"
        :rules="ruleValidate"
        :label-width="100"
        inline
      >
        <Row>
          <Col span="12">
            <FormItem label="刀条组合码" prop="composeNumber">
              <Input style="width:200px" v-model="composeNumber" readonly />
            </FormItem>
          </Col>
          <Col span="12">
            <FormItem label="刀条组合名称" prop="toolMap">
              <Input style="width:200px" v-model="headNumber" readonly />
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span="12">
            <FormItem label="刀条编号" prop="toolNumber">
              <Input style="width:200px" v-model="toolNumber" readonly />
            </FormItem>
          </Col>
          <Col span="12">
            <FormItem label="刀条名称" prop="toolName">
              <Input style="width:200px" v-model="toolName" readonly />
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span="12">
            <FormItem label="库存数量" prop="inventoryQty">
              <InputNumber style="width:200px" v-model="inventoryQty" readonly />
            </FormItem>
          </Col>
          <Col span="12">
            <FormItem label="补充数量" prop="toolQty">
              <InputNumber style="width:200px" v-model="formValidate.toolQty" />
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col>
            <FormItem label="领料人" prop="staffCode">
              <Input
                style="width:200px"
                v-model="formValidate.staffCode"
                placeholder="输入职工号"
                @on-blur="searchStaff()"
              >
                <Icon type="ios-search" slot="suffix" />
              </Input>&nbsp;
              <Input style="width:300px" v-model="staffInfo" readonly></Input>
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span="12">
            <FormItem label="说明" prop="remark">
              <Input style="width:530px" v-model="formValidate.remark" placeholder="输入说明" />
            </FormItem>
          </Col>
        </Row>
      </Form>
      <div slot="footer">
        <Button
          @click="handleSupplementSubmit('formValidateOut')"
          type="primary"
          :disabled="isDisabled2"
        >确定</Button>
        <Button @click="handleSupplementReset('formValidateOut')" style="margin-left: 8px">取消</Button>
      </div>
    </Modal>

    <Modal v-model="installModalshow" title="安装刀盘" width="700" draggable>
      <Form
        ref="formValidate"
        :model="formValidate"
        :rules="ruleValidate"
        :label-width="100"
        inline
      >
        <Row>
          <Col span="12">
            <FormItem label="刀条组合条码" prop="composeNumber">
              <Input style="width:200px" v-model="composeNumber" readonly />
            </FormItem>
          </Col>
          <Col span="12">
            <FormItem label="刀条组合名称" prop="headName">
              <Input style="width:200px" v-model="headName" readonly />
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Table
            ref="myTable"
            border
            :columns="columns"
            :data="composeDetailList"
            height="160"
            style="margin-bottom:5px;"
          ></Table>
        </Row>
        <row>
          <Col span="12">
            <FormItem label="上次刀盘编号" prop="lastPlateNumber">
              <Input style="width:200px" v-model="lastPlateNumber" readonly />
            </FormItem>
          </Col>
          <Col span="12">
            <FormItem label="刀盘" prop="plateNumber">
              <Select
                style="width:200px"
                v-model="formValidate.plateNumber"
                ref="plateNumber"
                placeholder="请选择刀盘"
                clearable
                filterable
              >
                <Option
                  v-for="item in plateList"
                  :value="item.plateNumber"
                  :key="item.pkId"
                >{{ item.plateNumber }}</Option>
              </Select>
            </FormItem>
          </Col>
        </row>
      </Form>
      <div slot="footer">
        <Button
          @click="handleInstallSubmit('formValidate')"
          type="primary"
          :disabled="isDisabled2"
        >确定</Button>
        <Button @click="handleInstallReset('formValidate')" style="margin-left: 8px">取消</Button>
      </div>
    </Modal>

    <Modal v-model="scrapModalShow" width="250" title="刀条报废" @on-ok="scrapSureBtn">
      <p>确定报废所选刀条？</p>
    </Modal>
  </div>
</template>

<script>
import axios from "@/libs/api.request";
import { getToken, getBaseUrl } from "@/libs/util";
import { dateFormat, objCopy } from "@/libs/tools.js";
import tool_blade_form_config from "./tool-blade-form-config.js";
export default {
  components: {},
  data() {
    return {
      isDisabled: false,
      isDisabled2: false,
      outboundModalshow: false,
      installModalshow: false,
      supplementModalshow: false,
      scrapModalShow: false,
      composeNumber: "",
      headNumber: "",
      headName: "",
      plateNumber: "",
      uploadAction:
        getBaseUrl() + "/tool/blade-import.json?token=" + getToken(),
      toolQty: 0,
      lastPlateNumber: "",
      toolNumber: "",
      toolName: "",
      receiver: "",
      staffInfo: "",
      inventoryQty: 0,
      goBackId: null,
      plateList: [],
      searchCondition: {
        toolNumber: "",
        page: 1,
        rows: 10
      },
      composeDetailList: [],
      columns: [
        {
          title: "刀条编码",
          key: "toolNumber",
          width: "120px"
        },
        {
          title: "刀条名称",
          key: "toolName",
          width: "260px"
        },
        {
          title: "刀条图号",
          key: "toolMap",
          width: "180px"
        },
        {
          title: "刀条数量",
          key: "toolQty",
          width: "100px"
        }
      ],

      tableData: {
        total: 0, // 总页数
        rows: [] // 每页条数
      },
      tableColumns: [
        {
          title: "刀条组合条码",
          key: "composeNumber",
          minWidth: 160,
          fixed: "left"
        },
        {
          title: "刀条组合码",
          key: "headNumber",
          minWidth: 130,
          fixed: "left"
        },
        {
          title: "刀条组合名称",
          key: "headName",
          minWidth: 200,
          fixed: "left"
        },
        {
          title: "刀盘编码",
          key: "plateNumber",
          minWidth: 100
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
              title: "刀条数量",
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
                              return h("li", {}, item.toolQty);
                            }
                          )
                        : ""
                    )
                  ]
                );
              }
            },
            {
              title: "补充数量",
              key: "detailList",
              align: "center",
              minWidth: 100,
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
                              return h("li", {}, item.supplementQty);
                            }
                          )
                        : "&nbsp;"
                    )
                  ]
                );
              }
            },
            {
              title: "操作",
              key: "detailList",
              align: "center",
              minWidth: 100,
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
                              return h("li", {}, [
                                h(
                                  "a",
                                  {
                                    props: {
                                      type: "primary",
                                      size: "small"
                                    },
                                    style: {
                                      fontWeight: 800,
                                      marginRight: "5px",
                                      display:
                                        params.row.toolStatus === 7
                                          ? "none"
                                          : "inline-block"
                                    },
                                    on: {
                                      click: () => {
                                        this.supplement(
                                          this.tableData.rows[params.index]
                                            .composeNumber,
                                          this.tableData.rows[params.index]
                                            .headNumber,
                                          item.toolNumber,
                                          item.toolName
                                        );
                                      }
                                    }
                                  },
                                  "补充"
                                )
                              ]);
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
          title: "制件",
          align: "center",
          children: [
            {
              title: "制件编码",
              key: "partList",
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
                      undefined != this.tableData.rows[params.index].partList
                        ? this.tableData.rows[params.index].partList.map(
                            item => {
                              return h("li", {}, item.partCode);
                            }
                          )
                        : ""
                    )
                  ]
                );
              }
            },
            {
              title: "制件名称",
              key: "partList",
              align: "center",
              minWidth: 260,
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
                      undefined != this.tableData.rows[params.index].partList
                        ? this.tableData.rows[params.index].partList.map(
                            item => {
                              return h("li", {}, item.partName);
                            }
                          )
                        : ""
                    )
                  ]
                );
              }
            },
            {
              title: "制件数量",
              key: "partList",
              align: "center",
              minWidth: 100,
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
                      undefined != this.tableData.rows[params.index].partList
                        ? this.tableData.rows[params.index].partList.map(
                            item => {
                              return h("li", {}, item.partQty);
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
          title: "组合时间",
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
          title: "刀条状态",
          key: "toolStatus",
          minWidth: 120,
          render: function(h, params) {
            let temp = "";
            switch (params.row.toolStatus) {
              case 1:
                temp = "生产中";
                break;
              case 2:
                temp = "待刃磨";
                break;
              case 3:
                temp = "待刃磨质检";
                break;
              case 4:
                temp = "待涂层";
                break;
              case 5:
                temp = "待涂层质检";
                break;
              case 6:
                temp = "待安装";
                break;
              case 7:
                temp = "待报废";
                break;
              case 8:
                temp = "已报废";
                break;
              default:
                break;
            }
            return h("div", temp);
          }
        },
        {
          title: "已生产数量",
          key: "productionQty",
          minWidth: 100
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
                      this.print(params.row.composeNumber);
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
                    size: "small"
                  },
                  style: {
                    marginRight: "5px",
                    display:
                      params.row.toolStatus === 1 ? "inline-block" : "none"
                  },
                  on: {
                    click: () => {
                      this.outbound(params.row, 2);
                    }
                  }
                },
                "送刃磨"
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
                      params.row.toolStatus === 6 ? "inline-block" : "none"
                  },
                  on: {
                    click: () => {
                      this.install(params.row);
                    }
                  }
                },
                "安装"
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
                    display:
                      params.row.toolStatus === 7 ? "inline-block" : "none"
                  },
                  on: {
                    click: () => {
                      this.scrap(params.row.composeNumber);
                    }
                  }
                },
                "报废"
              )
            ]);
          }
        }
      ],
      formValidate: tool_blade_form_config.formValidate, // form表单字段
      ruleValidate: tool_blade_form_config.ruleValidate // form表单验证规则
    };
  },
  created() {
    const self = this;
    self.getListData();
  },
  methods: {
    getListData() {
      const self = this;
      let para = {
        headNumber: self.searchCondition.headNumber,
        composeNumber: self.searchCondition.composeNumber,
        page: self.searchCondition.page,
        rows: self.searchCondition.rows
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/blade-compose-page-list",
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

    getPlateList() {
      const self = this;
      let para = {
        useStatus: 2
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/plate-list",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            self.plateList = data.data;
          }
        })
        .catch(function(err) {
          console.log(err);
        });
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
              self.departmentId = data.data.departmentId;
              self.departmentName = data.data.departmentName;
              self.teamId = data.data.teamId;
              self.teamName = data.data.teamName;
              self.receiver = data.data.staffName;
              self.staffInfo =
                data.data.departmentName + "-" + data.data.staffName;
            } else {
              self.$Message.error("无法找到与职工号对应的职工");
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

    outbound(row, outType) {
      const self = this;
      self.formValidate = {};
      self.$refs["formValidateOut"].resetFields();
      self.outboundModalshow = true;
      self.headName = row.headName;
      self.plateNumber = row.plateNumber;
      self.composeNumber = row.composeNumber;
      self.formValidate.composeNumber = row.composeNumber;
      self.composeDetailList = row.detailList;
    },
    //涂层返库安装
    install(row) {
      const self = this;
      self.getPlateList();
      self.toolName = row.toolName;
      self.pkId = row.pkId;
      self.formValidate = {};
      self.$refs["formValidate"].resetFields();
      self.composeNumber = row.composeNumber;
      self.formValidate.composeNumber = row.composeNumber;
      self.headName = row.headName;
      self.lastPlateNumber = row.lastPlateNumber;
      self.composeDetailList = row.detailList;
      self.installModalshow = true;
    },

    handleInstallSubmit(name) {
      const self = this;
      self.$refs[name].validate(valid => {
        if (valid) {
          self.isDisabled2 = true;
          let para = {
            pkId: self.pkId,
            composeNumber: self.composeNumber,
            plateNumber: self.formValidate.plateNumber
          };
          axios
            .request({
              headers: {
                "Content-Type": "application/json; charset=UTF-8"
              },
              url: "/tool/blade-compose-install",
              method: "post",
              isAuth: true,
              params: para
            })
            .then(function(res) {
              self.installModalshow = false;
              if (res.data.code === 200) {
                self.$Message.success("刀条安装成功!");
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

    handleInstallReset(name) {
      const self = this;
      self.installModalshow = false;
      self.$refs[name].resetFields();
    },

    handleOutboundSubmit(name) {
      this.isDisabled = true;
      const self = this;
      let para = {
        pkId: self.pkId,
        composeNumber: self.composeNumber,
        headNumber: self.headNumber,
        remark: self.formValidate.remark
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/blade-set-repair",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          self.outboundModalshow = false;
          if (res.data.code === 200) {
            self.$Message.success("刀条送刃磨成功!");
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

    handleOutboundReset(name) {
      const self = this;
      self.outboundModalshow = false;
      self.$refs[name].resetFields();
    },
    //补充
    supplement(composeNumber, headNumber, toolNumber, toolName) {
      const self = this;
      debugger;
      self.supplementModalshow = true;
      self.formValidate = {};
      self.$refs["formValidateOut"].resetFields();
      self.toolNumber = toolNumber;
      self.toolName = toolName;
      self.headNumber = headNumber;
      self.formValidate.toolNumber = toolNumber;
      self.formValidate.composeNumber = composeNumber;
      self.composeNumber = composeNumber;
      //查询库存数量
      let para = {
        toolNumber: toolNumber
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/get-blade-inventory-qty",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            self.inventoryQty = data.data;
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },

    handleSupplementSubmit(name) {
      const self = this;
      self.$refs[name].validate(valid => {
        if (valid) {
          if (self.formValidate.toolQty > self.inventoryQty) {
            self.$Message.error("补充数量不能大于库存数量");
            return false;
          }
          self.isDisabled2 = true;
          let para = {
            pkId: self.pkId,
            composeNumber: self.composeNumber,
            toolNumber: self.toolNumber,
            partCode: self.partCode,
            headNumber: self.headNumber,
            departmentId: self.departmentId,
            departmentName: self.departmentName,
            teamId: self.teamId,
            teamName: self.teamName,
            receiver: self.receiver,
            outType: 4,
            outQty: self.formValidate.toolQty,
            remark: self.formValidate.remark
          };
          axios
            .request({
              headers: {
                "Content-Type": "application/json; charset=UTF-8"
              },
              url: "/tool/blade-outbound",
              method: "post",
              isAuth: true,
              params: para
            })
            .then(function(res) {
              self.supplementModalshow = false;
              if (res.data.code === 200) {
                self.$Message.success("刀具补充成功!");
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
    handleSupplementReset(name) {
      const self = this;
      self.supplementModalshow = false;
      self.$refs[name].resetFields();
    },

    exportData() {
      const self = this;
      let toolNumber = self.searchCondition.toolNumber;
      let para = "token=" + getToken();
      if (!!toolNumber) {
        para = para + "&toolNumber=" + toolNumber;
      }
      window.location.href =
        getBaseUrl() + "/tool/blade-compose-export?" + para;
    },

    print(composeNumber) {
      window.open(
        "http://10.36.10.11:8080/decision/view/report?viewlet=tool_compose.cpt&composeNumber=" +
          composeNumber,
        "newwindow",
        "height=360,width=600,top=0,left=0,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no"
      );
    },
    //报废
    scrap(composeNumber) {
      const self = this;
      self.scrapModalShow = true;
      self.composeNumber = composeNumber;
    },
    scrapSureBtn() {
      const self = this;
      let para = {
        composeNumber: self.composeNumber
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/blade-scrap",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            self.$Message.success("刀具报废成功!");
            self.getListData();
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
