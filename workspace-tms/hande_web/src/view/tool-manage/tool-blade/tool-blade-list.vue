<template>
  <div>
    <Card>
      <Row>
        <Form ref="formInline" :model="searchCondition" :label-width="65" inline>
          <Col span="16">
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
            <FormItem>
              <Button type="primary" style="margin-left: -60px" @click="doSearch('formInline')">搜索</Button>
            </FormItem>
            <FormItem style="margin-left: -65px">
              <Button type="dashed" @click="doReset('formInline')">重置</Button>
            </FormItem>
          </Col>
          <Col span="8">
            <FormItem style="width: 100%;text-align: right;">
              <Button type="success" @click="complexOutbound()">组合出库</Button>
              <Upload
                :show-upload-list="false"
                :action="uploadAction"
                :on-success="handleSuccess"
                :on-error="handleError"
                style="display: inline-block;margin-left: 5px;margin-right: 5px;"
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
    <Modal v-model="outboundModalshow" title="刀具领用" width="700" draggable>
      <Form
        ref="formValidate"
        :model="formValidate"
        :rules="ruleValidate"
        :label-width="100"
        inline
      >
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
            <FormItem label="刀条图号" prop="toolMap">
              <Input style="width:200px" v-model="toolMap" readonly />
            </FormItem>
          </Col>
          <Col span="12">
            <FormItem label="刀条组合" prop="headId">
              <Select
                style="width:200px"
                v-model="formValidate.headId"
                ref="headId"
                placeholder="请选择刀条组合"
                @on-change="headSelectChange"
              >
                <Option
                  v-for="item in headBladeList"
                  :value="item.pkId"
                  :key="item.pkId"
                >{{ item.headName }}</Option>
              </Select>
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span="12">
            <FormItem label="标准数量" prop="bladeQty">
              <Input style="width:200px" v-model="bladeQty" readonly />
            </FormItem>
          </Col>
          <Col span="12">
            <FormItem label="领用数量" prop="useQty">
              <InputNumber style="width:200px" v-model="useQty" readonly />
            </FormItem>
          </Col>
        </Row>
        <Row>
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
          <Col span="12">
            <FormItem label="刀条组合条码" prop="composeNumber">
              <Input style="width:200px" v-model="composeNumber" readonly />
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col>
            <FormItem label="领用人职工号" prop="staffCode">
              <Input
                style="width:200px"
                v-model="formValidate.staffCode"
                placeholder="输入职工号"
                @on-blur="searchStaff()"
              >
                <Icon type="ios-search" slot="suffix" />
              </Input>&nbsp;
              <Input style="width:330px" v-model="staffInfo" readonly></Input>
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span="12">
            <FormItem label="说明" prop="remark">
              <Input style="width:540px" v-model="formValidate.remark" placeholder="输入说明" />
            </FormItem>
          </Col>
        </Row>
      </Form>
      <div slot="footer">
        <Button
          @click="handleOutboundSubmit('formValidate')"
          type="primary"
          :disabled="isDisabled"
        >出库</Button>
        <Button @click="handleOutboundReset('formValidate')" style="margin-left: 8px">取消</Button>
      </div>
    </Modal>

    <tool-complex-outbound-modal ref="toolComplexOutboundModal" @search="getListData"></tool-complex-outbound-modal>
  </div>
</template>

<script>
import axios from "@/libs/api.request";
import { getToken, getBaseUrl } from "@/libs/util";
import { dateFormat, objCopy } from "@/libs/tools.js";
import toolComplexOutboundModal from "./tool-complex-outbound-modal.vue";
import tool_blade_form_config from "./tool-blade-form-config.js";
export default {
  components: { toolComplexOutboundModal },
  data() {
    return {
      isDisabled: false,
      outboundModalshow: false,
      staffInfo: "",
      composeNumber: "",
      toolNumber: "",
      toolMap: "",
      toolName: "",
      warehouse: "",
      partCode: "",
      bladeQty: 1,
      useQty: 0,
      repairTime: null,
      departmentId: null,
      departmentName: "",
      receiver: "",
      uploadAction:
        getBaseUrl() + "/tool/blade-import.json?token=" + getToken(),
      baseList: [],
      plateList: [],
      headBladeList: [],
      //partList: [],
      searchCondition: {
        toolNumber: "",
        page: 1,
        rows: 10
      },
      tableData: {
        total: 0, // 总页数
        rows: [] // 每页条数
      },
      tableColumns: [
        {
          title: "刀条编码",
          key: "toolNumber",
          minWidth: 150,
          fixed: "left"
        },
        {
          title: "刀条名称",
          key: "toolName",
          minWidth: 320
        },
        {
          title: "刀条图号",
          key: "toolMap",
          minWidth: 180
        },
        {
          title: "库存数量",
          key: "inventoryQty",
          minWidth: 120
        },
        {
          title: "在用数量",
          key: "useQty",
          minWidth: 120
        },
        {
          title: "报废数量",
          key: "scrapQty",
          minWidth: 120
        },
        {
          title: "操作",
          key: "action",
          width: 160,
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
                      this.outbound(params.row, 1);
                    }
                  }
                },
                "出库"
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
    self.getBaseListData();
  },
  methods: {
    getBaseListData() {
      const self = this;
      let para = {
        typeId: 4
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
      let para = {
        toolNumber: self.searchCondition.toolNumber,
        page: self.searchCondition.page,
        rows: self.searchCondition.rows
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/blade-page-list",
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

    complexOutbound() {
      const self = this;
      self.$refs.toolComplexOutboundModal.showModal();
    },
    outbound(row) {
      const self = this;
      self.formValidate = {};
      self.partList = [];
      self.$refs["formValidate"].resetFields();
      self.outboundModalshow = true;
      (self.pkId = row.pkId), (self.toolName = row.toolName);
      self.toolMap = row.toolMap;
      self.toolNumber = row.toolNumber;
      self.formValidate.toolNumber = row.toolNumber;
      self.inventoryQty = row.inventoryQty;
      self.getHeadBladeList(row.toolNumber);
      self.getPlateList();
    },
    handleOutboundReset(name) {
      const self = this;
      self.outboundModalshow = false;
      self.$refs[name].resetFields();
    },

    getPartList(headNumber) {
      const self = this;
      let para = {
        headNumber: headNumber
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/head-part-list",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            self.partList = data.data;
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
    handleSuccess() {
      const self = this;
      self.getListData();
      self.$Message.success("文件导入成功!");
    },
    handleError() {
      const self = this;
      self.getListData();
      self.$Message.success("文件导入失败!");
    },
    exportData() {
      const self = this;
      window.location.href =
        getBaseUrl() + "/tool/tool-blade-export?token=" + getToken();
    },
    getHeadBladeList(toolNumber) {
      const self = this;
      let para = {
        toolNumber: toolNumber
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/head-blade-list",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            self.headBladeList = data.data;
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },

    headSelectChange(item) {
      const self = this;
      let headBladeList = self.headBladeList;
      if (item !== undefined) {
        for (const headBlade of headBladeList) {
          if (headBlade.pkId === item) {
            self.bladeQty = headBlade.toolQty;
            if (self.bladeQty > self.inventoryQty) {
              self.$Message.error("库存不足!");
              return;
            }
            self.useQty = self.bladeQty;
            self.formValidate.useQty = self.bladeQty;
            self.formValidate.partCode = [];
            self.partCode = "";
            self.headNumber = headBlade.headNumber;
            // self.getPartList(headBlade.headNumber);
            self.createComposeNumber(headBlade.headNumber);
          }
        }
      }
    },

    createComposeNumber(headNumber) {
      const self = this;
      self.composeNumber = "";
      self.formValidate.composeNumber = "";
      let para = {
        headNumber: headNumber
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/create-compose-number",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            self.composeNumber = data.data;
            self.formValidate.composeNumber = data.data;
          } else {
            self.$Message.error(data.info);
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },

    //form 校验方法
    handleOutboundSubmit(name) {
      const self = this;
      this.$refs[name].validate(valid => {
        if (valid) {
          /*if (!!!self.partCode || self.partCode.length < 1) {
            self.$Message.error("制件不能为空!");
            return;
          }*/
          let para = {
            pkId: self.pkId,
            toolNumber: self.toolNumber,
            // partCode: self.partCode.join("&"),
            headNumber: self.headNumber,
            composeNumber: self.composeNumber,
            plateNumber: self.formValidate.plateNumber,
            departmentId: self.departmentId,
            departmentName: self.departmentName,
            teamId: self.teamId,
            teamName: self.teamName,
            receiver: self.receiver,
            outType: 1,
            outQty: self.useQty,
            remark: self.formValidate.remark
          };
          self.isDisabled = true;
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
              self.outboundModalshow = false;
              if (res.data.code === 200) {
                self.$Message.success("刀具出库成功!");
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
    }
  }
};
</script>

<style>
</style>
