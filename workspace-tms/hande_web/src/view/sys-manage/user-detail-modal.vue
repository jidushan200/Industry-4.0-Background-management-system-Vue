<style lang="less">
//引入css文件
@import "./set-role-auth-modal.less";
</style>
<template>
  <!--用户信息弹出modal 用于修改和新增用户-->
  <Modal
    v-model="userDetailModalshow"
    :title="isModify?'修改用户':'新增用户'"
    width="660"
    draggable
    class="set-role-auth-modal"
  >
    <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="80" inline>
      <FormItem label="登陆名" prop="loginName">
        <Input
          :disabled="isModify"
          v-model="formValidate.loginName"
          placeholder="输入职工工号"
          @on-blur="searchStaff()"
        />
      </FormItem>
      <FormItem label="登陆密码" prop="loginPwd" v-if="!isModify">
        <Input
          :disabled="isModify"
          type="password"
          v-model="formValidate.loginPwd"
          placeholder="输入登陆密码"
        />
      </FormItem>
      <FormItem label="姓名" prop="realName">
        <Input v-model="realName" placeholder="职工姓名" />
      </FormItem>
      <FormItem label="部门" prop="departmentName">
        <Input v-model="departmentName" placeholder="部门" readonly />
      </FormItem>
      <FormItem label="角色" prop="roleId">
        <Select
          v-model="formValidate.roleId"
          placeholder="选择一个角色"
          ref="role"
          @on-change="roleListSelectChange"
          style="width:160px;"
        >
          <Option v-for="item in roleList" :value="item.pkId" :key="item.pkId">{{item.roleName }}</Option>
        </Select>
      </FormItem>
    </Form>
    <div class="ivu-form-item">
      <label class="ivu-form-item-label" style="margin-left:45px;width: 100px;text-align: right;">权限</label>
      <Tree
        ref="treeelement"
        :data="treeNode"
        show-checkbox
        @on-check-change="onCheckChange"
        style="margin-left:80px"
      ></Tree>
      <Spin size="large" fix v-if="spinShow"></Spin>
    </div>
    <div slot="footer">
      <Button @click="handleSubmit('formValidate')" type="primary">保存</Button>
      <Button @click="handleReset('formValidate')" style="margin-left: 8px">取消</Button>
    </div>
  </Modal>
</template>

<script>
import { getTrees, dateFormat, objCopy } from '@/libs/tools.js'
import axios from '@/libs/api.request'
import { getRoleList, addUser, updateUser } from '@/api/user_manage.js'
import user_manage_form_config from './user-manage-form-config.js'

export default {
  data () {
    return {
      userDetailModalshow: false,
      isModify: false, // 是否是修改明细操作
      roleList: [], // 角色列表
      userAuthList: [], // 用户权限列表
      departmentName: '',
      realName: '',
      formValidate: user_manage_form_config.formValidate, // user form表单字段
      ruleValidate: user_manage_form_config.ruleValidate, // user form表单验证规则
      spinShow: false,
      defaultProps: {
        children: 'children',
        label: 'authorName'
      },
      nodeLists: [], // 原始的权限树
      treeNode: [],
      selectAuthkeyIds: [],
      selectFullAuthkeyIds: []
    }
  },
  created () {},
  methods: {
    init () {},
    showModal (userItem) {
      const self = this
      if (userItem) {
        self.isModify = true
        self.formValidate = objCopy(userItem, {})
        self.realName = userItem.realName
        self.departmentName = userItem.departmentName
        // self.loadUserAuthList(self.formValidate.pkId);
        self.userAuthList = userItem.authCode
          ? userItem.authCode.split(',')
          : []
        self.loadTreeData(self.formValidate.roleId)
      } else {
        self.isModify = false
        self.formValidate = {}
        self.treeNode = []
        self.userAuthList = []
        self.$refs['formValidate'].resetFields()
      }
      self.userDetailModalshow = true
      self.loadRoleList()
    },
    searchStaff () {
      const self = this
      if (!self.formValidate.loginName) {
        return
      }
      let para = {
        staffCode: self.formValidate.loginName
      }
      axios
        .request({
          headers: {
            'Content-Type': 'application/json; charset=UTF-8'
          },
          url: '/base/staff-get-by-code.json',
          method: 'post',
          isAuth: true,
          params: para
        })
        .then(function (res) {
          const data = res.data
          if (data.code === 200) {
            if (data.data !== null && data.data !== undefined) {
              self.departmentName = data.data.departmentName
              self.realName = data.data.staffName
              self.formValidate.realName = self.realName
            } else {
              self.$Message.error('职工不存在')
            }
          } else {
            self.$Notice.error({
              title: '错误提示',
              desc: res.data.info
            })
          }
        })
        .catch(function (err) {
          console.log(err)
        })
    },
    loadRoleList () {
      const self = this
      getRoleList()
        .then(res => {
          if (res.data.code === 200) {
            self.roleList = res.data.data
          } else {
            this.$Notice.error({
              title: '错误提示',
              desc: res.data.info
            })
            return []
          }
        })
        .catch(err => {
          console.log(err)
        })
    },
    // 获取用户的权限
    loadUserAuthList (userId) {
      const self = this
      let para = {
        userId: userId
      }
      axios
        .request({
          headers: {
            'Content-Type': 'application/json; charset=UTF-8'
          },
          url: '/auth/sys-auth-list-by-user-id.json',
          method: 'post',
          isAuth: true,
          params: para
        })
        .then(function (res) {
          if (res.data.code === 200) {
            self.userAuthList = res.data.data
          } else {
            self.$Notice.error({
              title: '错误提示',
              desc: res.data.info
            })
          }
        })
        .catch(function (err) {
          console.log(err)
        })
    },
    roleListSelectChange (item) {
      const self = this
      self.loadTreeData(item)
      let roleList = self.roleList
      if (item !== undefined) {
        for (const role of roleList) {
          if (role.pkId === item) {
            self.formValidate.roleId = role.pkId
          }
        }
      }
    },
    // form 校验方法
    addUserSubmit () {
      const self = this
      self.selectAuthkeyIds = []
      self.selectFullAuthkeyIds = []
      let checkedKeys = self.$refs.treeelement.getCheckedNodes()
      let fullcheckedKeys = self.$refs.treeelement.getCheckedAndIndeterminateNodes()
      self.selectAuthkeyIds = [...checkedKeys]
      self.selectFullAuthkeyIds = [...fullcheckedKeys]
      let para = {
        loginName: self.formValidate.loginName,
        loginPwd: self.formValidate.loginPwd,
        realName: self.realName,
        roleId: self.formValidate.roleId,
        fullAuthCode: self.selectFullAuthkeyIds
          .map(function (item) {
            return item.authorCode
          })
          .join(','),
        authCodes: self.selectAuthkeyIds
          .map(function (item) {
            return item.authorCode
          })
          .join(',')
      }
      axios
        .request({
          headers: {
            'Content-Type': 'application/json; charset=UTF-8'
          },
          url: '/user/user-account-add.json',
          method: 'post',
          isAuth: true,
          params: para
        })
        .then(function (res) {
          self.userDetailModalshow = false
          if (res.data.code === 200) {
            self.$Message.success('用户新建成功!')
            self.$emit('refreshList')
          } else {
            self.$Notice.error({
              title: '错误提示',
              desc: res.data.info
            })
          }
        })
        .catch(function (err) {
          console.log(err)
        })
    },
    editUserSubmit () {
      const self = this
      self.selectAuthkeyIds = []
      self.selectFullAuthkeyIds = []
      let checkedKeys = self.$refs.treeelement.getCheckedNodes()
      let fullcheckedKeys = self.$refs.treeelement.getCheckedAndIndeterminateNodes()
      self.selectAuthkeyIds = [...checkedKeys]
      self.selectFullAuthkeyIds = [...fullcheckedKeys]
      let para = {
        pkId: self.formValidate.pkId,
        loginName: self.formValidate.loginName,
        loginPwd: self.formValidate.loginPwd,
        realName: self.realName,
        roleId: self.formValidate.roleId,
        fullAuthCode: self.selectFullAuthkeyIds
          .map(function (item) {
            return item.authorCode
          })
          .join(','),
        authCodes: self.selectAuthkeyIds
          .map(function (item) {
            return item.authorCode
          })
          .join(',')
      }
      axios
        .request({
          headers: {
            'Content-Type': 'application/json; charset=UTF-8'
          },
          url: '/user/user-account-update.json',
          method: 'post',
          isAuth: true,
          params: para
        })
        .then(function (res) {
          self.userDetailModalshow = false
          if (res.data.code === 200) {
            self.$Message.success('用户修改成功!')
            self.$emit('refreshList')
          } else {
            self.$Notice.error({
              title: '错误提示',
              desc: res.data.info
            })
          }
        })
        .catch(function (err) {
          console.log(err)
        })
    },
    handleSubmit (name) {
      const self = this
      self.$refs[name].validate(valid => {
        if (valid) {
          if (self.isModify) {
            self.editUserSubmit()
          } else {
            self.addUserSubmit()
          }
        } else {
          self.$Message.error('请输入正确信息!')
        }
      })
    },
    handleReset (name) {
      const self = this
      self.userDetailModalshow = false
    },

    loadTreeData (roleId) {
      const self = this
      let para = {
        roleId: roleId
      }
      axios
        .request({
          headers: {
            'Content-Type': 'application/json; charset=UTF-8'
          },
          url: '/auth/sys-auth-list-by-role-id.json',
          method: 'post',
          isAuth: true,
          params: para
        })
        .then(function (res) {
          const data = res.data
          if (data.code === 200) {
            if (data.data) {
              self.nodeLists = [data.data]
              self.activeNodes(self.userAuthList)
            } else {
              self.treeNode = []
            }
            // self.treeNode= []
            // self.activeNodes(self.userAuthList);
          }
        })
        .catch(function (err) {
          console.log(err)
        })
    },
    onCheckChange (checkedArr, currentValue) {
      // 树节点复选框变化
      // console.log("树节点复选框变化");
      // console.log(checkedArr);
      // console.log(currentValue);
    },
    activeNodes (checkedIdArr) {
      const self = this
      // console.log("########");
      // console.log(self.nodeLists);
      // console.log(checkedIdArr);
      // console.log("########");
      let nodes = [].concat(self.nodeLists)
      for (let i = 0; i < nodes.length; i++) var level1 = nodes[i]
      if (
        checkedIdArr.some(function (item) {
          return item == level1['authorCode']
        })
      ) {
        level1['checked'] = true
      }
      // 第二级
      if (
        typeof level1['children'] !== 'undefined' &&
        Array.isArray(level1['children'])
      ) {
        for (var j = 0; j < level1['children'].length; j++) {
          var level2 = level1['children'][j]
          if (
            checkedIdArr.some(function (item) {
              return item == level2['authorCode']
            })
          ) {
            level2['checked'] = true
          }
          // 判断是否有第三级
          if (
            typeof level2['children'] !== 'undefined' &&
            Array.isArray(level2['children'])
          ) {
            for (var m = 0; m < level2['children'].length; m++) {
              var level3 = level2['children'][m]
              if (
                checkedIdArr.some(function (item) {
                  return item == level3['authorCode']
                })
              ) {
                level3['checked'] = true
              }
              // 判断是否有第四级
              if (
                typeof level3['children'] !== 'undefined' &&
                Array.isArray(level3['children'])
              ) {
                for (var n = 0; n < level3['children'].length; n++) {
                  var level4 = level3['children'][n]
                  if (
                    checkedIdArr.some(function (item) {
                      return item == level4['authorCode']
                    })
                  ) {
                    level4['checked'] = true
                  }
                  // 判断是否有第五级
                  if (
                    typeof level4['children'] !== 'undefined' &&
                    Array.isArray(level4['children'])
                  ) {
                    for (var l = 0; l < level4['children'].length; l++) {
                      var level5 = level4['children'][l]
                      if (
                        checkedIdArr.some(function (item) {
                          return item == level5['authorCode']
                        })
                      ) {
                        level5['checked'] = true
                      }
                    }
                  }
                  // 判断是否有第五级
                }
              }
              // 判断是否有第四级
            }
          }
          // 判断是否有第三级
        }
      }
      // 判断是否有第二级
      // self.treeNode = nodes;
      var a = JSON.stringify(nodes)
      self.treeNode = JSON.parse(a)
    }
  }
}
</script>
