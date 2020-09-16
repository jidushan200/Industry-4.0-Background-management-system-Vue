<template>
  <div>
    <Card>
      <Row>
        <Form ref="formInline" :model="searchCondition" :label-width="80" inline>
          <FormItem prop="roleName" label="角色名称">
            <Input type="text" v-model="searchCondition.roleName" placeholder="角色名称" />
          </FormItem>
          <FormItem>
            <Button type="primary" @click="doSearch('formInline')">搜索</Button>&nbsp;
            <Button type="dashed" @click="doReset('formInline')">重置</Button>&nbsp;
            <Button type="success" @click="addRole">
              <Icon type="md-add"></Icon>添加
            </Button>
          </FormItem>
        </Form>
      </Row>

      <Table :data="tableData.rows" :columns="tableColumns" border stripe highlight-row></Table>
      <div style="margin: 10px;overflow: hidden">
        <div style="float: right;">
          <Page
            show-total
            :total="tableData.total"
            :page-size="searchCondition.rows"
            :current="searchCondition.page"
            @on-change="changePage"
          ></Page>
        </div>
      </div>
    </Card>
    <Modal
      v-model="deleteModalShow"
      width="250"
      :title="isBatchDetele?'批量删除角色':'删除角色'"
      @on-ok="deleteRoleSureBtn"
    >
      <p>确定删除所选角色？</p>
    </Modal>
    <Modal v-model="roleDetailModalshow" :title="isModify?'修改角色':'新增角色'" width="320" draggable>
      <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="80">
        <FormItem label="角色名称" prop="roleName">
          <Input v-model="formValidate.roleName" placeholder="输入角色名称" />
        </FormItem>
        <FormItem label="角色描述" prop="roleDesc">
          <Input v-model="formValidate.remark" placeholder="输入角色描述" />
        </FormItem>
      </Form>
      <div slot="footer">
        <Button type="primary" @click="handleSubmit('formValidate')" :disabled="isDisabled">保存</Button>
        <Button @click="handleReset('formValidate')" style="margin-left: 8px">取消</Button>
      </div>
    </Modal>
    <set-role-auth-modal ref="setRoleAuthModal"></set-role-auth-modal>
  </div>
</template>

<script>
import axios from "@/libs/api.request";
import setRoleAuthModal from "./set-role-auth-modal.vue";
import role_manage_form_config from "./role-manage-form-config.js";
import { dateFormat, objCopy, hasOneOf } from "@/libs/tools.js";
let self = this;
export default {
  components: {
    setRoleAuthModal
  },
  data() {
    return {
      isDisabled: false,
      roleDetailModalshow: false, //控制明细modal显示
      isModify: false, //是否是修改明细操作
      deleteModalShow: false, //控制删除modal提示 显示
      isBatchDetele: false, //是否是批量删除操作
      searchCondition: {
        roleName: "",
        page: 1,
        rows: 10
      },
      tableData: {
        total: 0, // 总页数
        rows: [] // 每页条数
      },
      tableColumns: [
        {
          title: "角色名称",
          key: "roleName",
          minWidth: 200
        },
        {
          title: "角色描述",
          key: "remark",
          minWidth: 300
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
          align: "center",
          minWidth: 200,
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
                    type: "success",
                    size: "small"
                  },
                  style: {
                    marginRight: "5px"
                  },
                  on: {
                    click: () => {
                      this.setRoleAuth(params.index);
                    }
                  }
                },
                "权限"
              ),
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
                "修改"
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
                      this.deleteRole(params.index);
                    }
                  }
                },
                "删除"
              )
            ]);
          }
        }
      ],
      formValidate: role_manage_form_config.formValidate, //user form表单字段
      ruleValidate: role_manage_form_config.ruleValidate //user form表单验证规则
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
          url: "/role/sys-role-page-list.json",
          method: "post",
          isAuth: true,
          params: self.searchCondition
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            self.tableData = data;
            // roleName: this.searchKeys.roleName;
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
    doSearch(name) {
      const self = this;
      self.changePage(1);
    },
    doReset(name) {
      const self = this;
      self.$refs[name].resetFields();
      self.getListData();
    },
    //权限列表
    setRoleAuth(index) {
      const self = this;
      self.$refs.setRoleAuthModal.showModal(self.tableData.rows[index]);
    },
    //新增角色操作
    addRole() {
      const self = this;
      this.roleDetailModalshow = true;
      this.isModify = false;
      this.formValidate = {};
      this.$refs["formValidate"].resetFields();
    },
    //新增角色提交方法
    addRoleSubmit() {
      this.isDisabled = true;
      const self = this;
      let para = {
        roleName: this.formValidate.roleName,
        remark: this.formValidate.remark
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/role/sys-role-add.json",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          self.roleDetailModalshow = false;
          if (res.data.code === 200) {
            self.$Message.success("角色新建成功!");
            self.isDisabled = false;
            self.getListData();
          } else {
            self.$Message.error(res.data.info);
            self.isDisabled = false;
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },
    show(index) {
      this.roleDetailModalshow = true;
      this.isModify = true;
      this.formValidate = objCopy(this.tableData.rows[index], {});
    },
    //表格内行删除操作
    deleteRole(index) {
      this.roleDeleteRowData = this.tableData.rows[index];
      this.deleteModalShow = true;
      this.isBatchDetele = false;
    },
    //删除modal 确认方法
    deleteRoleSureBtn() {
      const self = this;
      let para = {
        pkId: this.roleDeleteRowData.pkId,
        roleName: this.roleDeleteRowData.roleName
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/role/sys-role-delete.json",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          if (res.data.code === 200) {
            self.$Message.success("删除成功!");
            self.getListData();
          } else {
            self.$Message.error(res.data.info);
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },
    //删除modal 取消方法
    deleteRoleCancelBtn() {
      this.roleDeleteRowData = {};
    },
    editRoleSubmit() {
      this.isDisabled = true;
      const self = this;
      let para = {
        pkId: this.formValidate.pkId,
        roleName: this.formValidate.roleName,
        remark: this.formValidate.remark
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/role/sys-role-update.json",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          const data = res.data;
          self.roleDetailModalshow = false;
          if (res.data.code === 200) {
            self.$Message.success("角色修改成功!");
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
    //form 校验方法
    handleSubmit(name) {
      this.$refs[name].validate(valid => {
        if (valid) {
          if (this.isModify) {
            this.editRoleSubmit();
          } else {
            this.addRoleSubmit();
          }
        } else {
          this.$Message.error("请输入正确信息!");
        }
      });
    },
    handleReset(name) {
      this.roleDetailModalshow = false;
      this.$refs[name].resetFields();
    }
  }
};
</script>

<style>
</style>
