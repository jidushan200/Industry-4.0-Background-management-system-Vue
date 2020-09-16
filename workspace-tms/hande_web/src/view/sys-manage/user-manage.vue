<template>
  <div>
    <Card>
      <Row>
        <Form ref="formInline" :model="searchCondition" :label-width="70" inline>
          <FormItem prop="loginName" label="用户名">
            <Input type="text" v-model="searchCondition.loginName" placeholder="请输入用户名" />
          </FormItem>
          <FormItem prop="realName" label="真实姓名">
            <Input type="text" v-model="searchCondition.realName" placeholder="请输入真实姓名" />
          </FormItem>
          <FormItem prop="department" label="所属部门">
            <Select style="width:158px" v-model="searchCondition.departmentId" ref="department">
              <Option
                v-for="item in departmentList"
                :value="item.pkId"
                :key="item.pkId"
              >{{ item.departmentName }}</Option>
            </Select>
          </FormItem>
          <FormItem>
            <Button type="primary" @click="refreshList">搜索</Button>&nbsp;
            <Button type="dashed" @click="doReset('formInline')">重置</Button>&nbsp;
            <Button type="success" @click="addUser">
              <Icon type="md-add"></Icon>添加
            </Button>&nbsp;
            <Button type="warning" @click="exportData()">
              <Icon type="ios-download-outline"></Icon>导出
            </Button>
          </FormItem>
        </Form>
      </Row>
      <Table
        :data="tableData.rows"
        :columns="tableColumns"
        border
        stripe
        highlight-row
        ref="userList"
      ></Table>
      <div style="margin: 10px;overflow: hidden">
        <div style="float: right;">
          <Page
            show-total
            :total="tableData.total"
            :page-size="searchCondition.rows"
            :current="searchCondition.page"
            show-sizer
            @on-change="changePage"
            @on-page-size-change="handlePageSize"
          ></Page>
        </div>
      </div>
    </Card>
    <!--删除弹出modal-->
    <Modal
      v-model="deleteModalShow"
      width="250"
      :title="isBatchDetele?'批量删除用户':'删除用户'"
      @on-ok="deleteUserSureBtn"
      @on-cancel="deleteUserCancelBtn"
    >
      <p>确定删除所选用户？</p>
    </Modal>

    <!--重置密码modal-->
    <Modal
      v-model="resetPasswordModalShow"
      width="250"
      title="重置密码"
      @on-ok="resetPasswordSureBtn"
      @on-cancel="resetPasswordCancelBtn"
    >
      <p>确定重置密码？</p>
    </Modal>
    <user-detail-modal ref="userDetailModal" @refreshList="loadTableData"></user-detail-modal>
  </div>
</template>
<script>
import {
  getUserPageList,
  removeUserById,
  resetPasswordById
} from '@/api/user_manage.js'
import axios from '@/libs/api.request'
import { dateFormat, objCopy } from '@/libs/tools.js'
import userDetailModal from './user-detail-modal.vue'
import { getToken, getBaseUrl, util } from '@/libs/util'
export default {
  components: {
    userDetailModal
  },
  data () {
    return {
      isBatchDetele: false, // 是否是批量删除操作
      deleteModalShow: false, // 控制删除modal提示 显示
      resetPasswordModalShow: false, // 控制删除modal提示 显示
      departmentList: [],
      searchCondition: {
        realName: '',
        loginName: '',
        departmentId: null,
        page: 1,
        rows: 10
      },

      idPara: {
        pkId: 0
      },
      tableData: {
        total: 0, // 总页数
        rows: [] // 每页条数
      },
      tableColumns: [
        {
          title: '用户名',
          align: 'center',
          key: 'loginName',
          minWidth: 250
        },
        {
          title: '真实姓名',
          align: 'center',
          key: 'realName',
          minWidth: 150
        },
        {
          title: '部门',
          align: 'center',
          key: 'departmentName',
          minWidth: 250
        },
        {
          title: '用户角色',
          align: 'center',
          key: 'roleName',
          minWidth: 250
        },
        {
          title: '创建人',
          key: 'createUserName',
          minWidth: 100
        },
        {
          title: '创建时间',
          key: 'createTime',
          minWidth: 140,
          render: function (h, params) {
            return h(
              'div',
              dateFormat(new Date(params.row.createTime), 'yyyy-MM-dd HH:mm')
            )
          }
        },
        {
          title: '最后修改人',
          key: 'updateUserName',
          minWidth: 100
        },
        {
          title: '最后修改时间',
          key: 'updateTime',
          minWidth: 140,
          render: function (h, params) {
            return h(
              'div',
              dateFormat(new Date(params.row.updateTime), 'yyyy-MM-dd HH:mm')
            )
          }
        },
        {
          title: '操作',
          key: 'action',
          align: 'center',
          minWidth: 220,
          fixed: 'right',
          render: (h, params) => {
            /**
             * render写法 添加按钮
             */
            return h('div', [
              h(
                'Button',
                {
                  props: {
                    type: 'primary',
                    size: 'small'
                  },
                  style: {
                    marginRight: '5px'
                  },
                  on: {
                    click: () => {
                      this.editUser(params.index)
                    }
                  }
                },
                '修改'
              ),
              h(
                'Button',
                {
                  props: {
                    type: 'warning',
                    size: 'small'
                  },
                  style: {
                    marginRight: '5px'
                  },
                  on: {
                    click: () => {
                      this.resetPassword(params.index)
                    }
                  }
                },
                '重置密码'
              ),
              h(
                'Button',
                {
                  props: {
                    type: 'error',
                    size: 'small'
                  },
                  on: {
                    click: () => {
                      this.deleteUser(params.index)
                    }
                  }
                },
                '删除'
              )
            ])
          }
        }
      ] // table列信息
    }
  },
  created () {
    const self = this
    self.getDepartmentListData()
    self.loadTableData()
  },
  methods: {
    // 删除modal 确认方法
    getDepartmentListData () {
      const self = this
      axios
        .request({
          headers: {
            'Content-Type': 'application/json; charset=UTF-8'
          },
          url: '/base/staff-department-list',
          isAuth: true,
          method: 'post'
        })
        .then(function (res) {
          const data = res.data
          if (data.code === 200) {
            self.departmentList = data.data
          }
        })
        .catch(function (err) {
          console.log(err)
        })
    },
    deleteUserSureBtn () {
      const self = this
      self.idPara.pkId = self.userDeleteRowData.pkId
      self.idPara.loginName = self.userDeleteRowData.loginName
      removeUserById(self.idPara)
        .then(res => {
          if (res.data.code === 200) {
            self.$Message.success('删除成功!')
            self.loadTableData()
          } else {
            self.$Notice.error({
              title: '错误提示',
              desc: res.data.info
            })
          }
        })
        .catch(err => {
          console.log(err)
        })
    },
    // 删除modal 取消方法
    deleteUserCancelBtn () {
      this.userDeleteRowData = {}
    },
    // 获取数据列表方法
    loadTableData () {
      const self = this
      getUserPageList(self.searchCondition)
        .then(res => {
          if (res.data.code === 200) {
            self.tableData = res.data
            // loginName: self.searchKeys.loginName;
          } else {
            self.$Notice.error({
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
    refreshList (searchData) {
      const self = this
      self.searchKeys = searchData
      self.changePage(1)
    },
    doReset (name) {
      const self = this
      self.$refs[name].resetFields()
      self.loadTableData()
    },
    handlePageSize (value) {
      const self = this
      self.$set(self.searchCondition, 'rows', value)
      self.loadTableData()
    },
    changePage (cuurentPage) {
      const self = this
      self.$set(self.searchCondition, 'page', cuurentPage)
      self.loadTableData()
    },
    // 表格内行删除操作
    deleteUser (index) {
      const self = this
      self.userDeleteRowData = self.tableData.rows[index]
      self.deleteModalShow = true
      self.isBatchDetele = false
    },
    addUser () {
      const self = this
      self.isModify = false
      self.$refs.userDetailModal.showModal()
    },
    editUser (index) {
      const self = this
      self.isModify = true
      self.$refs.userDetailModal.showModal(this.tableData.rows[index])
    },
    resetPassword (index) {
      const self = this
      self.userDeleteRowData = self.tableData.rows[index]
      self.resetPasswordModalShow = true
    },
    exportData () {
      const self = this
      window.location.href =
        getBaseUrl() + '/user/user-complete-export?token=' + getToken()
    },
    resetPasswordSureBtn () {
      const self = this
      let para = {
        pkId: self.userDeleteRowData.pkId,
        loginName: self.userDeleteRowData.loginName
      }
      resetPasswordById(para)
        .then(res => {
          if (res.data.code === 200) {
            self.$Message.success('密码重置成功!')
          } else {
            self.$Notice.error({
              title: '错误提示',
              desc: res.data.info
            })
          }
        })
        .catch(err => {
          console.log(err)
        })
    },
    resetPasswordCancelBtn () {
      this.userDeleteRowData = {}
    }
  }
}
</script>
