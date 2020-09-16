<style lang="less">
.fsize {
  font-size: 18px !important;
}
</style>
<template>
  <div>
    <Card>
      <Row>
        <Form ref="formInline" :model="searchCondition" :label-width="90" inline>
          <Col span="20">
            <FormItem prop="composeNumber" label="刀条组合条码">
              <Input type="text" v-model="searchCondition.composeNumber" placeholder="请输入刀条组合条码"></Input>
            </FormItem>
            <!--<FormItem prop="toolNumber" label="刀条编码">
              <Input type="text" v-model="searchCondition.toolNumber" placeholder="请输入刀条编码"></Input>
            </FormItem>
            <FormItem>
              <Button type="primary" style="margin-left: -60px;" @click="doSearch('formInline')">搜索</Button>
            </FormItem>-->
            <FormItem style="margin-left: -65px;">
              <Button style type="dashed" @click="doReset('formInline')">重置</Button>
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
    <Modal
      v-model="toolRepairDetailModalshow"
      title="新刀条刃磨信息录入"
      width="560"
      draggable
      style="font-size:20px"
    >
      <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="90" inline>
        <row>
          <Col span="12">
            <FormItem label="物料编码" prop="toolNumber">
              <Input style="width:170px" v-model="toolNumber" />
            </FormItem>
          </Col>
          <Col span="12">
            <FormItem label="物料名称" prop="toolNumber">
              <Input style="width:170px" v-model="toolName" />
            </FormItem>
          </Col>
        </row>
        <row>
          <Col span="12">
            <FormItem label="物料数量" prop="toolQty">
              <Input style="width:170px" v-model="toolQty" />
            </FormItem>
          </Col>
          <Col span="12">
            <FormItem label="本次刃磨量" prop="repairMeasure">
              <InputNumber
                :max="99"
                :min="0.01"
                :step="0.01"
                style="width:170px"
                v-model="formValidate.repairMeasure"
                placeholder="输入本次刃磨量"
              />
            </FormItem>
          </Col>
        </row>
        <row>
          <Col>
            <FormItem label="备注" prop="remark">
              <Input style="width:430px" v-model="formValidate.remark" placeholder="输入备注" />
            </FormItem>
          </Col>
        </row>
      </Form>
      <div slot="footer">
        <Button @click="handleSubmit('formValidate')" type="primary" :disabled="isDisabled">保存</Button>
        <Button @click="handleReset('formValidate')" style="margin-left: 8px">取消</Button>
      </div>
    </Modal>

    <Modal
      v-model="composeRepairModalshow"
      title="刀条组合刃磨信息录入"
      width="660"
      draggable
      style="font-size:20px"
    >
      <Form
        ref="formValidate"
        :model="formValidate"
        :rules="ruleValidate"
        :label-width="100"
        inline
      >
        <row>
          <Col span="12">
            <FormItem label="刀条组合条码" prop="composeNumber">
              <Input style="width:200px" v-model="composeNumber" />
            </FormItem>
          </Col>
          <Col span="12">
            <FormItem label="刀条组合名称" prop="headName">
              <Input style="width:200px" v-model="headName" />
            </FormItem>
          </Col>
        </row>
        <Row>
          <Table
            ref="myTable"
            border
            :columns="columns"
            :data="repairList"
            height="180"
            style="margin-bottom:5px;"
          ></Table>
        </Row>

        <!-- <row>
          <Col>
            <FormItem label="备注" prop="remark">
              <Input style="width:530px" v-model="formValidate.remark" placeholder="输入备注" />
            </FormItem>
          </Col>
        </row>-->
      </Form>
      <div slot="footer">
        <Button
          @click="composehandleSubmit('formValidate')"
          type="primary"
          :disabled="isDisabled"
        >保存</Button>
        <Button @click="composehandleReset('formValidate')" style="margin-left: 8px">取消</Button>
      </div>
    </Modal>
  </div>
</template>

<script>
import axios from '@/libs/api.request'
import tool_repair_manage_form_config from './tool-repair-form-config.js'
import { dateFormat, objCopy } from '@/libs/tools.js'
import qs from 'qs'
export default {
  data () {
    const self = this
    return {
      isDisabled: false,
      toolRepairDetailModalshow: false, // 控制明细modal显示
      composeRepairModalshow: false,
      composeNumber: '',
      headName: '',
      toolNumber: '',
      toolName: '',
      toolQty: null,
      repairMeasure: null,
      repairList: [],
      searchCondition: {
        toolNumber: '',
        page: 1,
        rows: 10
      },
      tableData: {
        total: 0, // 总页数
        rows: [] // 每页条数
      },
      tableColumns: [
        {
          title: '刀条组合条码',
          fixed: 'left',
          minWidth: 160,
          key: 'composeNumber'
        },
        {
          title: '刀条组合名称',
          fixed: 'left',
          minWidth: 220,
          key: 'headName'
        },

        {
          title: '刀条',
          align: 'center',
          children: [
            {
              title: '刀条编码',
              key: 'detailList',
              align: 'center',
              minWidth: 120,
              render: (h, params) => {
                return h(
                  'div',
                  {
                    attrs: {
                      class: 'subCol'
                    }
                  },
                  [
                    h(
                      'ul',
                      undefined != this.tableData.rows[params.index].detailList
                        ? this.tableData.rows[params.index].detailList.map(
                          item => {
                            return h('li', {}, item.toolNumber)
                          }
                        )
                        : ''
                    )
                  ]
                )
              }
            },
            {
              title: '刀条名称',
              key: 'detailList',
              align: 'center',
              minWidth: 200,
              render: (h, params) => {
                return h(
                  'div',
                  {
                    attrs: {
                      class: 'subCol'
                    }
                  },
                  [
                    h(
                      'ul',
                      undefined != this.tableData.rows[params.index].detailList
                        ? this.tableData.rows[params.index].detailList.map(
                          item => {
                            return h('li', {}, item.toolName)
                          }
                        )
                        : ''
                    )
                  ]
                )
              }
            },
            {
              title: '刀条图号',
              key: 'detailList',
              align: 'center',
              minWidth: 200,
              render: (h, params) => {
                return h(
                  'div',
                  {
                    attrs: {
                      class: 'subCol'
                    }
                  },
                  [
                    h(
                      'ul',
                      undefined != this.tableData.rows[params.index].detailList
                        ? this.tableData.rows[params.index].detailList.map(
                          item => {
                            return h('li', {}, item.toolMap)
                          }
                        )
                        : ''
                    )
                  ]
                )
              }
            },
            {
              title: '刀条数量',
              key: 'detailList',
              align: 'center',
              minWidth: 100,
              render: (h, params) => {
                return h(
                  'div',
                  {
                    attrs: {
                      class: 'subCol'
                    }
                  },
                  [
                    h(
                      'ul',
                      undefined != this.tableData.rows[params.index].detailList
                        ? this.tableData.rows[params.index].detailList.map(
                          item => {
                            return h('li', {}, item.toolQty)
                          }
                        )
                        : ''
                    )
                  ]
                )
              }
            }
          ]
        },
        {
          title: '送磨时间',
          minWidth: 150,
          key: 'createTime',
          render: function (h, params) {
            return h(
              'div',
              dateFormat(new Date(params.row.createTime), 'yyyy-MM-dd HH:mm')
            )
          }
        },
        {
          title: '操作',
          key: 'action',
          width: 160,
          align: 'center',
          fixed: 'right',
          render: (h, params) => {
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
                      this.addToolRepair(params.row)
                    }
                  }
                },
                '刃磨'
              )
            ])
          }
        }
      ],
      columns: [
        {
          title: '刀条编码',
          key: 'toolNumber',
          width: '150px'
        },
        {
          title: '刀条名称',
          key: 'toolName',
          width: '200px'
        },
        {
          title: '刀条图号',
          key: 'toolMap',
          width: '120px'
        },
        {
          title: '数量',
          key: 'toolQty',
          width: '100px'
        },
        {
          title: '刃磨量',
          key: 'repairMeasure',
          width: '100px',
          render: (h, params) => {
            return h('Input', {
              props: {
                value: this.repairList[params.index].repairMeasure
              },
              on: {
                input: val => {
                  this.repairList[params.index].repairMeasure = val
                }
              }
            })
          }
        },
        {
          title: '备注',
          key: 'remark',
          width: '220px',
          render: (h, params) => {
            return h('Input', {
              props: {
                value: this.repairList[params.index].remark
              },
              style: {},
              on: {
                input: val => {
                  this.repairList[params.index].remark = val
                }
              }
            })
          }
        }
      ],

      formValidate: tool_repair_manage_form_config.formValidate, // user form表单字段
      ruleValidate: tool_repair_manage_form_config.ruleValidate // user form表单验证规则
    }
  },
  created () {
    const self = this
    self.getListData()
    // self.getStaffListData();
  },
  methods: {
    getListData () {
      const self = this
      let para = {
        toolNumber: self.searchCondition.toolNumber,
        composeNumber: self.searchCondition.composeNumber,
        handleResult: 0,
        page: self.searchCondition.page,
        rows: self.searchCondition.rows
      }
      axios
        .request({
          headers: {
            'Content-Type': 'application/json; charset=UTF-8'
          },
          url: '/tool/blade-wait-repair-page-list',
          method: 'post',
          isAuth: true,
          params: para
        })
        .then(function (res) {
          const data = res.data
          if (data.code === 200) {
            self.tableData = data
          }
        })
        .catch(function (err) {
          console.log(err)
        })
    },
    changePage (cuurentPage) {
      const self = this
      self.$set(self.searchCondition, 'page', cuurentPage)
      self.getListData()
    },
    handlePageSize (value) {
      const self = this
      self.$set(self.searchCondition, 'rows', value)
      self.getListData()
    },
    doSearch (name) {
      const self = this
      self.changePage(1)
    },
    doReset (name) {
      const self = this
      self.$refs[name].resetFields()
      self.getListData()
    },
    addToolRepair (row) {
      const self = this
      debugger
      self.repairList = []
      self.pkId = row.pkId
      if (row.composeNumber) {
        self.composeNumber = row.composeNumber
        self.headName = row.headName
        self.repairList = row.detailList
        self.composeRepairModalshow = true
      } else {
        self.toolRepairDetailModalshow = true
        self.toolNumber = row.toolNumber
        self.toolName = row.toolName
        self.toolQty = row.toolQty
      }
    },

    handleSubmit (name) {
      const self = this
      self.$refs[name].validate(valid => {
        if (valid) {
          this.isDisabled = true
          let para = {
            waitId: self.pkId,
            toolNumber: self.toolNumber,
            toolQty: self.toolQty,
            repairMeasure: self.formValidate.repairMeasure,
            remark: self.formValidate.remark
          }
          axios
            .request({
              headers: {
                'Content-Type': 'application/json; charset=UTF-8'
              },
              url: '/tool/blade-repair',
              method: 'post',
              isAuth: true,
              params: para
            })
            .then(function (res) {
              self.toolRepairDetailModalshow = false
              if (res.data.code === 200) {
                self.$Message.success('新刀条刃磨信息已记录!')
                self.isDisabled = false
                self.getListData()
              } else {
                self.$Notice.error({
                  title: '错误提示',
                  desc: res.data.info
                })
                self.isDisabled = false
              }
            })
            .catch(function (err) {
              console.log(err)
            })
        } else {
          self.$Message.error('请输入正确信息!')
        }
      })
    },
    handleReset (name) {
      this.toolRepairDetailModalshow = false
      this.$refs[name].resetFields()
    },
    composehandleSubmit (name) {
      const self = this
      self.$refs[name].validate(valid => {
        if (valid) {
          this.isDisabled = true
          let para = {
            waitId: self.pkId,
            fullNumber: self.composeNumber,
            detailJson: JSON.stringify(self.repairList)
          }
          axios
            .request({
              headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
              },
              url: '/tool/compose-blade-repair.json',
              method: 'post',
              isAuth: true,
              data: qs.stringify(para)
            })
            .then(function (res) {
              self.composeRepairModalshow = false
              if (res.data.code === 200) {
                self.$Message.success('刀条组合刃磨信息已记录!')
                self.isDisabled = false
                self.getListData()
              } else {
                self.$Notice.error({
                  title: '错误提示',
                  desc: res.data.info
                })
                self.isDisabled = false
              }
            })
            .catch(function (err) {
              console.log(err)
            })
        } else {
          self.$Message.error('请输入正确信息!')
        }
      })
    },
    composehandleReset (name) {
      this.composeRepairModalshow = false
      this.$refs[name].resetFields()
    }
  }
}
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
