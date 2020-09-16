<template>
  <div>

    <Card>
      <Row>
        <Form ref="formInline" :model="searchCondition" :label-width="65" inline>
          <Col span="16">
          <FormItem label="终端编码" prop="terminalCode">
            <Input type="text" v-model="searchCondition.terminalCode" placeholder="请输入终端编码">
            </Input>
          </FormItem>
          <FormItem>
            <Button type="primary" style="margin-left: -60px;" @click="doSearch('formInline')">搜索</Button>
          </FormItem>
          <FormItem style="margin-left: -65px;">
            <Button type="dashed" @click="doReset('formInline')">重置</Button>
          </FormItem>
          </Col>
          <Col span="8">
          <FormItem style="width: 100%;text-align: right;">
            <!-- <Upload :show-upload-list="false" action="" :on-success="handleSuccess" :on-error="handleError" style="display: inline-block;margin-right: 5px;">
                <Button icon="ios-cloud-upload-outline">批量导入</Button>
              </Upload> -->
            <Button type="success" style="margin-right: 5px;" @click="addTerminal">
              <Icon type="md-add"></Icon>添加
            </Button>
            <Button type="warning" @click="exportData()">
              <Icon type="ios-download-outline"></Icon>导出
            </Button>
          </FormItem>
          </Col>
        </Form>
      </Row>

      <Table ref="tablesMain" show-total :data="tableData.rows" :columns="tableColumns" stripe border></Table>

      <div style="margin: 10px;overflow: hidden">
        <div style="float: right;">
          <Page show-total :total="tableData.total" :page-size="searchCondition.rows" :current="searchCondition.page" @on-change="changePage" @on-page-size-change="handlePageSize" show-sizer></Page>
        </div>
      </div>
    </Card>

    <Modal v-model="terminalDetailModalshow" :title="isModify?'修改终端':'新增终端'" width="700" draggable>
      <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="80" inline>
        <FormItem label="终端编码" prop="terminalCode">
          <Input v-model="formValidate.terminalCode" placeholder="输入终端编码" style="width:240px" />
        </FormItem>
        <FormItem label="IP地址" prop="ip">
          <Input v-model="formValidate.ip" placeholder="输入IP地址" style="width:240px" />
        </FormItem>
        <FormItem label="所属部门" prop="departmentId">
          <Select style="width:240px" v-model="formValidate.departmentId" ref="department" @on-change="departmentListSelectChange">
            <Option v-for="item in departmentList" :value="item.pkId" :key="item.pkId">{{ item.departmentName }}</Option>
          </Select>
        </FormItem>
        <FormItem label="负责人" prop="managerId">
          <Select style="width:240px" v-model="formValidate.managerId" ref="manager" @on-change="staffListSelectChange">
            <Option v-for="item in staffList" :value="item.pkId" :key="item.pkId">{{ item.staffName }}</Option>
          </Select>
        </FormItem>
      </Form>
      <div slot="footer">
        <Button @click="handleSubmit('formValidate')" type="primary" :disabled="isDisabled">保存</Button>
        <Button @click="handleReset('formValidate')" style="margin-left: 8px">取消</Button>
      </div>
    </Modal>

    <Modal v-model="deleteModalShow" width="250" :title="isBatchDetele?'批量删除':'删除'" @on-ok="deleteModalSureBtn" @on-cancel="deleteModalCancelBtn">
      <p>确定删除所选项？</p>
    </Modal>
  </div>
</template>

<script>
import axios from "@/libs/api.request";
import terminal_manage_form_config from "./terminal-manage-form-config.js";
import { dateFormat, objCopy } from "@/libs/tools.js";
import { getToken, getBaseUrl } from "@/libs/util";

export default {
  data() {
    return {
      isDisabled: false,
      deleteModalShow: false, //控制删除modal提示 显示
      deleteRowData: {}, //行删除行数据
      isBatchDetele: false, //批量删除标志
      multiselectRowData: [], //复选列数据
      terminalDetailModalshow: false, //控制明细modal显示
      isModify: false, //是否是修改明细操作
      departmentList: [],
      staffList: [],
      searchCondition: {
        terminalCode: "",
        page: 1,
        rows: 10
      },
      tableData: {
        total: 0, // 总页数
        rows: [] // 每页条数
      },
      tableColumns: [
        {
          title: "终端编码",
          key: "terminalCode",
          sortable: true,
          minWidth: 200
        },
        {
          title: "所属部门",
          key: "departmentName",
          minWidth: 200
        },
        {
          title: "负责人",
          key: "managerName",
          minWidth: 200
        },
        {
          title: "IP地址",
          key: "ip",
          minWidth: 200
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
      formValidate: terminal_manage_form_config.formValidate, //user form表单字段
      ruleValidate: terminal_manage_form_config.ruleValidate //user form表单验证规则
    };
  },
  created() {
    const self = this;
    self.getListData();
    self.getDepartmentListData();
    self.getStaffListData();
  },
  methods: {
    getListData() {
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/base/terminal-page-list",
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
    addNewTerminal() {
      this.isDisabled = true;
      let para = {
        terminalCode: this.formValidate.terminalCode,
        departmentId: this.formValidate.departmentId,
        departmentName: this.formValidate.departmentName,
        managerId: this.formValidate.managerId,
        managerName: this.formValidate.managerName,
        ip: this.formValidate.ip
      };
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/base/terminal-add",
          isAuth: true,
          method: "post",
          params: para
        })
        .then(function(res) {
          self.terminalDetailModalshow = false;
          if (res.data.code === 200) {
            self.$Message.success("终端新建成功!");
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
    editTerminal() {
      this.isDisabled = true;
      let para = {
        pkId: this.formValidate.pkId,
        terminalCode: this.formValidate.terminalCode,
        departmentId: this.formValidate.departmentId,
        departmentName: this.formValidate.departmentName,
        managerId: this.formValidate.managerId,
        managerName: this.formValidate.managerName,
        ip: this.formValidate.ip
      };
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/base/terminal-update",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          self.terminalDetailModalshow = false;
          if (res.data.code === 200) {
            self.$Message.success("终端信息修改成功!");
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
          url: "/base/terminal-delete-by-id",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          self.deleteModalShow = false;
          if (res.data.code === 200) {
            self.$Message.success("终端信息删除成功!");
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
        this.deleteRowDate(this.deleteRowData.pkId);
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
    show(index) {
      this.terminalDetailModalshow = true;
      this.isModify = true;
      this.formValidate = objCopy(this.tableData.rows[index], {});
    },
    remove(index) {
      this.deleteRowData = this.tableData.rows[index];
      this.deleteModalShow = true;
      this.isBatchDetele = false;
    },
    addTerminal() {
      this.terminalDetailModalshow = true;
      this.isModify = false;
      this.formValidate = {
        terminalType: 0
      };
      this.$refs["formValidate"].resetFields();
    },
    departmentListSelectChange(item) {
      let departmentList = this.departmentList;
      if (item !== undefined) {
        for (const department of departmentList) {
          if (department.pkId === item) {
            this.formValidate.departmentName = department.departmentName;
          }
        }
      }
    },
    staffListSelectChange(item) {
      let staffList = this.staffList;
      if (item !== undefined) {
        for (const staff of staffList) {
          if (staff.pkId === item) {
            this.formValidate.managerName = staff.staffName;
          }
        }
      }
    },
    exportData() {
      const self = this;
      window.location.href =
        getBaseUrl() + "/base/terminal-export?token=" + getToken();
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
    //form 校验方法
    handleSubmit(name) {
      this.$refs[name].validate(valid => {
        if (valid) {
          if (this.isModify) {
            this.editTerminal();
          } else {
            this.addNewTerminal();
          }
        } else {
          this.$Message.error("请输入正确信息!");
        }
      });
    },
    handleReset(name) {
      this.terminalDetailModalshow = false;
      this.$refs[name].resetFields();
    }
  }
};
</script>

<style>
</style>
