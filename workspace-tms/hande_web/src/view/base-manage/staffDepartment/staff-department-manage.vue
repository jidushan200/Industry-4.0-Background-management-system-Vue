<template>
  <div>
    <Card>
      <Row>
        <Form ref="formInline" :model="searchCondition" :label-width="65" inline>
          <Col span="16">
          <FormItem label="部门编码" prop="departmentCode">
            <Input type="text" v-model="searchCondition.departmentCode" placeholder="请输入部门编码"></Input>
          </FormItem>
          <FormItem label="部门名称" prop="departmentName">
            <Input type="text" v-model="searchCondition.departmentName" placeholder="请输入部门名称"></Input>
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
            <Upload :show-upload-list="false" :action="uploadAction" :on-success="handleSuccess" :on-error="handleError" style="display: inline-block;margin-right: 5px;">
              <Button icon="ios-cloud-upload-outline">导入</Button>
            </Upload>
            <Button type="success" style="margin-right: 5px;" @click="addDepart">
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

    <Modal v-model="staffDepartmentDetailModalshow" :title="isModify?'修改部门':'新增部门'" width="400" draggable>
      <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="80" inline>
        <row>
          <FormItem label="部门编码" prop="departmentCode">
            <Input style="width:200px" v-model="formValidate.departmentCode" placeholder="输入部门编码" />
          </FormItem>
        </row>
        <row>
          <FormItem label="部门名称" prop="departmentName">
            <Input style="width:200px" v-model="formValidate.departmentName" placeholder="输入部门名称" />
          </FormItem>
        </row>
      </Form>
      <div slot="footer">
        <Button @click="handleSubmit('formValidate')" type="primary" :disabled="isDisabled">保存</Button>
        <Button @click="handleReset('formValidate')" style="margin-left: 8px">取消</Button>
      </div>
    </Modal>
  </div>
</template>

<script>
import axios from "@/libs/api.request";
import staff_department_manage_form_config from "./staff-department-manage-form-config.js";
import { dateFormat, objCopy } from "@/libs/tools.js";
import { getToken, getBaseUrl, util } from "@/libs/util";
export default {
  data() {
    return {
      isDisabled: false,
      deleteModalShow: false, //控制删除modal提示 显示
      deleteRowData: {}, //行删除行数据
      isBatchDetele: false, //批量删除标志
      multiselectRowData: [], //复选列数据
      staffDepartmentDetailModalshow: false, //控制明细modal显示
      isModify: false, //是否是修改明细操作
      uploadAction:
        getBaseUrl() + "/base/staff-department-import.json?token=" + getToken(),
      departmentList: [],
      searchCondition: {
        departmentName: "",
        departmentCode: "",
        page: 1,
        rows: 10
      },
      tableData: {
        total: 0, // 总页数
        rows: [] // 每页条数
      },
      tableColumns: [
        /*{
          title: "部门编码",
          key: "departmentCode",
          minWidth: 200
        },*/
        {
          title: "部门名称",
          key: "departmentName",
          minWidth: 200
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
          width: 120,
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
              )
            ]);
          }
        }
      ],
      formValidate: staff_department_manage_form_config.formValidate, //user form表单字段
      ruleValidate: staff_department_manage_form_config.ruleValidate //user form表单验证规则
    };
  },
  created() {
    const self = this;
    self.getListData();
  },
  methods: {
    getListData() {
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/base/staff-department-page-list",
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
    addNewStaffDepartment() {
      this.isDisabled = true;
      let para = {
        departmentCode: this.formValidate.departmentCode,
        departmentName: this.formValidate.departmentName
      };
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/base/staff-department-add",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          self.staffDepartmentDetailModalshow = false;
          if (res.data.code === 200) {
            self.$Message.success("部门新建成功!");
            self.getListData();
            self.isDisabled = false;
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
    editStaffDepartment() {
      this.isDisabled = true;
      let para = {
        pkId: this.formValidate.pkId,
        departmentCode: this.formValidate.departmentCode,
        departmentName: this.formValidate.departmentName
      };
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/base/staff-department-update",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          self.staffDepartmentDetailModalshow = false;
          if (res.data.code === 200) {
            self.$Message.success("部门信息修改成功!");
            self.getListData();
            self.isDisabled = false;
          }
        })
        .catch(function(err) {
          console.log(err);
          self.isDisabled = false;
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
      self.changePage(1);
    },
    doReset(name) {
      const self = this;
      self.$refs[name].resetFields();
      self.changePage(1);
    },
    show(index) {
      this.staffDepartmentDetailModalshow = true;
      this.isModify = true;
      this.formValidate = objCopy(this.tableData.rows[index], {});
    },
    addDepart() {
      this.staffDepartmentDetailModalshow = true;
      this.isModify = false;
      this.formValidate = {
        gender: 1
      };
      this.$refs["formValidate"].resetFields();
    },

    exportData() {
      const self = this;
      window.location.href =
        getBaseUrl() + "/base/staff-department-export?token=" + getToken();
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
            this.editStaffDepartment();
          } else {
            this.addNewStaffDepartment();
          }
        } else {
          this.$Message.error("请输入正确信息!");
        }
      });
    },
    handleReset(name) {
      this.staffDepartmentDetailModalshow = false;
      this.$refs[name].resetFields();
    }
  }
};
</script>

<style>
</style>
