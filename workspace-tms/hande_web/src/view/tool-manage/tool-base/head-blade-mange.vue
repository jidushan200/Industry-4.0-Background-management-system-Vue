<template>
  <div>
    <Card>
      <Row>
        <Form ref="formInline" :model="searchCondition" :label-width="85" inline>
          <Col span="16">
            <FormItem label="刀条组合编码" prop="headNumber">
              <Input style="width:170px" v-model="searchCondition.headNumber" />
            </FormItem>
            <FormItem label="刀条编码" prop="toolNumber">
              <Input style="width:170px" v-model="searchCondition.toolNumber" />
            </FormItem>
            <FormItem>
              <Button type="primary" style="margin-left: -60px" @click="doSearch('formInline')">搜索</Button>
            </FormItem>
            <FormItem style="margin-left: -65px">
              <Button style type="dashed" @click="doReset ('formInline')">重置</Button>
            </FormItem>
          </Col>
          <Col span="8">
            <FormItem style="width:100%;text-align:right">
              <Upload
                :show-upload-list="false"
                :action="uploadAction"
                :on-success="handleSuccess"
                :on-error="handleError"
                style="display: inline-block;margin-right:5px"
              >
                <Button icon="ios-cloud-upload-outline">导入</Button>
              </Upload>
              <Button type="success" style="margin-right: 5px" @click="addHeadBladeShow">
                <Icon type="md-add"></Icon>添加
              </Button>
              <Button type="warning" @click="exportData()">
                <Icon type="ios-download-outline"></Icon>导出
              </Button>
            </FormItem>
          </Col>
        </Form>
      </Row>

      <Table ref="tablesMain" :data="tableData.rows" :columns="tableColumns" stripe border></Table>

      <!-- 分页器 -->
      <div style="margin: 10px;overflow: hidden">
        <div style="float: right">
          <Page
            :total="tableData.total"
            show-total
            :page-size="searchCondition.rows"
            :current="searchCondition.page"
            @on-change="changePage"
            @on-page-size-change="handlePageSize"
            show-sizer
          ></Page>
        </div>
      </div>
    </Card>

    <!-- 弹出框——编辑/添加 -->
    <Modal
      v-model="headbladeDetailModalshow"
      :title="isModify?'修改刀条组合':'新增刀条组合'"
      width="600"
      draggable
    >
      <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="80" inline>
        <Row>
          <FormItem label="刀条组合" prop="headNumber">
            <Select
              style="width:170px"
              v-model="formValidate.headNumber"
              ref="headNumber"
              placeholder="请选择刀条组合"
              clearable
              filterable
            >
              <Icon type="ios-search" slot="suffix" />
              <Option
                v-for="item in headList"
                :value="item.headNumber"
                :key="item.headNumber"
              >{{ item.headNumber }}</Option>
            </Select>
          </FormItem>
          <FormItem label="刀条编码" prop="toolNumber">
            <Select
              style="width:170px"
              v-model="formValidate.toolNumber"
              ref="toolNumber"
              placeholder="请选择图号名称"
              clearable
              filterable
              @on-change="toolSelectChange"
            >
              <Icon type="ios-search" slot="suffix" />
              <Option
                v-for="item in baseList"
                :value="item.toolNumber"
                :key="item.toolNumber"
              >{{ item.toolNumber }}-{{ item.toolMap }}</Option>
            </Select>
          </FormItem>
        </Row>
        <Row>
          <FormItem label="刀条名称" prop="toolName">
            <Input v-model="toolName" readonly />
          </FormItem>
          <FormItem label="刀条数量" prop="toolQty">
            <InputNumber
              style="width:170px"
              :max="99999"
              :min="1"
              :step="1"
              v-model="formValidate.toolQty"
              placeholder="输入刀条数量"
            />
          </FormItem>
        </Row>
        <Row>
          <FormItem label="最大刃磨量" prop="grindingMax">
            <InputNumber
              style="width:170px"
              :max="999"
              :min="1"
              :step="1"
              v-model="formValidate.grindingMax"
              placeholder="最大刃磨量"
            />
          </FormItem>
          <FormItem label="刃磨警戒线" prop="grindingCordon">
            <InputNumber
              style="width:170px"
              :max="999"
              :min="1"
              :step="1"
              v-model="formValidate.grindingCordon"
              placeholder="刃磨警戒线"
            />
          </FormItem>
        </Row>
        <Row>
          <FormItem label="每次换刀加工数" prop="eachProcessQty">
            <InputNumber
              style="width:170px"
              :max="999"
              :min="1"
              :step="1"
              v-model="formValidate.eachProcessQty"
              placeholder="加工次数"
            />
          </FormItem>
          <FormItem label="加工次数" prop="processTimes">
            <InputNumber
              style="width:170px"
              :max="999"
              :min="1"
              :step="1"
              v-model="formValidate.processTimes"
              placeholder="加工次数"
            />
          </FormItem>
        </Row>
      </Form>
      <div slot="footer">
        <Button @click="handleSubmit ('formValidate')" type="primary" :disabled="isDisabled">保存</Button>
        <Button @click="handleReset ('formValidate')" style="margin-left: 8px">取消</Button>
      </div>
    </Modal>

    <Modal
      v-model="deleteModalShow"
      width="250"
      title="删除"
      @on-ok="deleteModalSureBtn"
      @on-cancel="deleteModalCancelBtn"
    >
      <p>确定删除所选项？</p>
    </Modal>
  </div>
</template>

<script>
import axios from "@/libs/api.request";
import head_blade_manage_form_config from "./head-blade-manage-form-config.js";
import { dateFormat, objCopy } from "@/libs/tools.js";
import { getToken, getBaseUrl } from "@/libs/util";
export default {
  data() {
    return {
      isDisabled: false,
      headbladeDetailModalshow: false,
      baseList: [],
      headList: [],
      toolName: "",
      deleteModalShow: false, // 控制删除modal提示显示
      deleteRowData: {}, // 行删除行数据
      isModify: false, // 是否是修改明细操作
      uploadAction:
        getBaseUrl() + "/tool/head-blade-import.json?token=" + getToken(),
      searchCondition: {
        headNumber: "",
        toolNumber: "",
        page: 1,
        rows: 10
      },
      tableData: {
        total: 0, //  总页数
        rows: [] //  每页条数
      },
      tableColumns: [
        {
          title: "刀条组合编码",
          key: "headNumber",
          minWidth: 160
        },
        {
          title: "刀条组合名称",
          key: "headName",
          minWidth: 200
        },
        {
          title: "刀条编码",
          key: "toolNumber",
          minWidth: 160
        },
        {
          title: "刀条图号",
          key: "toolMap",
          minWidth: 160
        },
        {
          title: "刀条名称",
          key: "toolName",
          minWidth: 260
        },
        {
          title: "刀条数量",
          key: "toolQty",
          minWidth: 100
        },
        {
          title: "每次换刀加工数",
          key: "eachProcessQty",
          minWidth: 160
        },
        {
          title: "加工次数",
          key: "processTimes",
          minWidth: 100
        },
        {
          title: "最大刃磨量",
          key: "grindingMax",
          minWidth: 100
        },
        {
          title: "预警刃磨量",
          key: "grindingCordon",
          minWidth: 100
        },

        {
          title: "创建人",
          key: "createUserName",
          minWidth: 100
        },
        {
          title: "创建时间",
          key: "createTime",
          minWidth: 140,
          render: function(h, params) {
            return h(
              "div",
              dateFormat(new Date(params.row.createTime), "yyyy-MM-dd HH:mm")
            );
          }
        },
        {
          title: "最后修改人",
          key: "updateUserName",
          minWidth: 100
        },
        {
          title: "最后修改时间",
          key: "updateTime",
          minWidth: 140,
          render: function(h, params) {
            return h(
              "div",
              dateFormat(new Date(params.row.updateTime), "yyyy-MM-dd HH:mm")
            );
          }
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
                      this.show(params.row);
                    }
                  }
                },
                "编辑"
              ),
              h(
                "Button",
                {
                  props: {
                    type: "error",
                    size: "small"
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
      formValidate: head_blade_manage_form_config.formValidate, // user form表单字段
      ruleValidate: head_blade_manage_form_config.ruleValidate // user form表单验证规则
    };
  },
  created() {
    const self = this;
    self.getListData();
    self.getBaseListData();
    self.getHeadListData();
  },
  methods: {
    getHeadListData() {
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json charset=UTF-8"
          },
          url: "/tool/head-list",
          isAuth: true,
          method: "post"
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200 && data.data !== null) {
            self.headList = data.data;
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },
    getBaseListData() {
      const self = this;
      let para = {
        typeId: 4
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json charset=UTF-8"
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
      axios
        .request({
          headers: {
            "Content-Type": "application/json charset=UTF-8"
          },
          url: "/tool/head-blade-page-list",
          method: "post",
          isAuth: true,
          params: self.searchCondition
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

    addHeadBladeShow() {
      const self = this;
      self.isModify = false;
      self.formValidate = {};
      self.formValidate.toolQty = 1;
      self.toolName = "";
      self.headbladeDetailModalshow = true;
    },
    show(row) {
      const self = this;
      self.isModify = true;
      self.formValidate = objCopy(row, {});
      self.formValidate.pkId = row.pkId;
      self.headbladeDetailModalshow = true;
      self.formValidate.headNumber = row.headNumber;
      self.formValidate.toolNumber = row.toolNumber;
      self.toolName = row.toolName;
      self.formValidate.toolQty = row.toolQty;
    },

    toolSelectChange(item) {
      const self = this;
      let baseList = self.baseList;
      if (item !== undefined) {
        for (const base of baseList) {
          if (base.toolNumber === item) {
            self.toolName = base.toolName;
            self.formValidate.toolName = base.toolName;
            self.formValidate.toolNumber = base.toolNumber;
            return;
          }
        }
      }
    },

    addHeadBlade() {
      this.isDisabled = true;
      const self = this;
      let para = {
        headNumber: self.formValidate.headNumber,
        toolNumber: self.formValidate.toolNumber,
        toolName: self.formValidate.toolName,
        toolQty: self.formValidate.toolQty,
        eachProcessQty: self.formValidate.eachProcessQty,
        processTimes: self.formValidate.processTimes,
        grindingMax: self.formValidate.grindingMax,
        grindingCordon: self.formValidate.grindingCordon
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json charset=UTF-8"
          },
          url: "/tool/head-blade-add",
          isAuth: true,
          method: "post",
          params: para
        })
        .then(function(res) {
          if (res.data.code === 200) {
            self.headbladeDetailModalshow = false;
            self.$Message.success("刀条组合刀条保存成功!");
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
    editHeadBlade() {
      this.isDisabled = true;
      const self = this;
      let para = {
        pkId: self.formValidate.pkId,
        headNumber: self.formValidate.headNumber,
        toolNumber: self.formValidate.toolNumber,
        toolName: self.toolName,
        toolQty: self.formValidate.toolQty,
        eachProcessQty: self.formValidate.eachProcessQty,
        processTimes: self.formValidate.processTimes,
        grindingMax: self.formValidate.grindingMax,
        grindingCordon: self.formValidate.grindingCordon
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json charset=UTF-8"
          },
          url: "/tool/head-blade-update",
          method: "post",
          isAuth: true,
          params: para
        })

        .then(function(res) {
          if (res.data.code === 200) {
            self.headbladeDetailModalshow = false;
            self.$Message.success("刀条组合刀条保存成功!");
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
            "Content-Type": "application/json charset=UTF-8"
          },
          url: "/tool/head-blade-delete-by-id",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          self.deleteModalShow = false;
          if (res.data.code === 200) {
            self.$Message.success("刀条组合删除成功!");
            self.getListData();
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },
    deleteModalSureBtn() {
      this.deleteRowDate(this.deleteRowData.pkId);
    },
    // 删除modal取消方法
    deleteModalCancelBtn() {
      this.deleteRowData = {};
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
      self.changePage(1);
    },
    doReset(name) {
      const self = this;
      self.$refs[name].resetFields();
      self.changePage(1);
    },

    remove(index) {
      this.deleteRowData = this.tableData.rows[index];
      this.deleteModalShow = true;
    },

    exportData() {
      const self = this;
      window.location.href =
        getBaseUrl() + "/tool/head-blade-export?token=" + getToken();
    },
    handleSuccess() {
      const self = this;
      self.getListData();
      self.$Message.success("文件导入成功!");
    },
    handleError() {
      const self = this;
      self.$Message.success("文件导入失败!");
    },
    // form 校验方法
    handleSubmit(name) {
      this.$refs[name].validate(valid => {
        if (valid) {
          if (this.isModify) {
            this.editHeadBlade();
          } else {
            this.addHeadBlade();
          }
        } else {
          this.$Message.error("请输入正确信息!");
        }
      });
    },
    handleReset(name) {
      this.headbladeDetailModalshow = false;
      this.$refs[name].resetFields();
    }
  }
};
</script>

<style></style>
