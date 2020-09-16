
<template>
  <Modal :transfer="false" v-model="statisticsModalshow" title="涂层结算" width="1200" draggable>
    <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="90" inline>
      <Table
        ref="myTable"
        border
        :columns="columns"
        :data="data"
        height="500"
        style="margin-bottom:5px"
      ></Table>
      <Row>
        <Col span="6">
          <FormItem label="选择行数" prop="settlementTotal">
            <Input style="width:170px" v-model="recodeTotal" readonly></Input>
          </FormItem>
        </Col>
        <Col span="6">
          <FormItem label="结算数量" prop="settlementTotal">
            <Input style="width:170px" v-model="settlementTotal" readonly></Input>
          </FormItem>
        </Col>
        <Col span="6">
          <FormItem label="结算总价" prop="settlementAmount">
            <Input style="width:170px" v-model="settlementAmount" readonly></Input>
          </FormItem>
        </Col>
        <Col span="6">
          <FormItem label="支付总价" prop="payAmount">
            <Input style="width:170px" v-model="payAmount" readonly></Input>
          </FormItem>
        </Col>
      </Row>
    </Form>
    <div slot="footer">
      <Button @click="handleSave()" type="primary" :disabled="isDisable">保存</Button>
      <Button @click="closeModal()" style="margin-left: 8px">关闭</Button>
    </div>

    <Modal :transfer="false" v-model="toolOperModalshow" title="刀具操作日志" width="1030" draggable>
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
      <div slot="footer">
        <Button @click="closeOperModal()" style="margin-left: 8px">关闭</Button>
      </div>
    </Modal>
  </Modal>
</template>
<script>
import { dateFormat, objCopy } from '@/libs/tools.js'
import axios from '@/libs/api.request'
import qs from 'qs'
import { getToken, getBaseUrl } from '@/libs/util'
import coat_statistics_form_config from './coat-statistics-form-config.js'

export default {
  data () {
    return {
      isDisable: false,
      statisticsModalshow: false,
      toolOperModalshow: false,
      recodeTotal: 0,
      settlementTotal: 0,
      settlementAmount: 0,
      payAmount: 0,
      data: [],
      columns: [
        {
          title: '结算单价',
          key: 'settlementPrice',
          width: 100,
          fixed: 'left',
          render: (h, params) => {
            return h('Input', {
              props: {
                value: this.data[params.index].coatPrice,
                required: true
              },
              rules: {
                required: true
              },
              on: {
                input: val => {
                  if (val <= 0) {
                    this.$Message.error('结算单价必须为正数!')
                    val = ''
                  }
                  this.data[params.index].settlementPrice = val
                  this.rowStatistics(params.index)
                  this.rowStatisticsNew(params.index)
                }
              }
            })
          }
        },
        {
          title: '结算数量',
          key: 'settlementQty',
          width: 100,
          fixed: 'left'
        },
        {
          title: '结算总价',
          key: 'settlementAmount',
          width: 120,
          render: (h, params) => {
            return h(
              'div',
              {},
              this.data[params.index].coatPrice *
                this.data[params.index].settlementQty
            )
          }
        },
        {
          title: '理论单价',
          key: 'coatPrice',
          width: 100
        },
        {
          title: '支付单价',
          key: 'payPrice',
          width: 100,
          render: (h, params) => {
            return h('div', {}, this.data[params.index].coatPrice)
          }
        },
        {
          title: '支付总价',
          key: 'payAmount',
          width: 100,
          render: (h, params) => {
            return h(
              'div',
              {},
              this.data[params.index].coatPrice *
                this.data[params.index].settlementQty
            )
          }
        },
        {
          title: '加工数量',
          key: 'processQty',
          width: 100
        },
        {
          title: '理论加工数量',
          key: 'theoreticalQty',
          minWidth: 120
        },
        {
          title: '加工完成度',
          key: 'completionDegree',
          minWidth: 120,
          render: function (h, params) {
            if (params.row.completionDegree) {
              return h('div', params.row.completionDegree * 100 + '%')
            }
          }
        },
        {
          title: '涂层厂家',
          key: 'coatSupplier',
          minWidth: 240
        },
        {
          title: '物料编码',
          key: 'toolNumber',
          sortable: true,
          width: 120
        },
        {
          title: '物料条码',
          key: 'fullNumber',
          minWidth: 240,
          render: (h, params) => {
            return h('div', [
              h(
                'a',
                {
                  on: {
                    click: () => {
                      this.showOperList(
                        params.row.fullNumber,
                        params.row.coatTime
                      )
                    }
                  }
                },
                params.row.fullNumber
              )
            ])
          }
        },
        {
          title: '物料名称',
          key: 'toolName',
          minWidth: 240
        },
        {
          title: '物料图号',
          key: 'toolMap',
          width: 160
        },
        {
          title: '涂回时间',
          key: 'coatTime',
          width: 150,
          render: function (h, params) {
            return h(
              'div',
              dateFormat(new Date(params.row.coatTime), 'yyyy-MM-dd HH:mm')
            )
          }
        },
        {
          title: '操作',
          key: 'action',
          align: 'center',
          width: 90,
          fixed: 'right',
          render: (h, params) => {
            return h('div', [
              h(
                'Button',
                {
                  props: {
                    type: 'error',
                    size: 'small'
                  },
                  style: {
                    marginRight: '5px'
                  },
                  on: {
                    click: () => {
                      this.delRow(params.index)
                    }
                  }
                },
                '删除'
              )
            ])
          }
        }
      ],

      searchCondition: {
        dateInterval: [],
        fullNumber: '',
        page: 1,
        rows: 10
      },
      tableData: {
        total: 0, // 总页数
        rows: [] // 每页条数
      },
      tableColumns: [
        {
          title: '物料条码',
          fixed: 'left',
          key: 'fullNumber',
          width: 240
        },
        {
          title: '物料名称',
          key: 'toolName',
          width: 220
        },
        {
          title: '物料编码',
          key: 'toolNumber',
          width: 120
        },
        {
          title: '物料图号',
          key: 'toolMap',
          width: 120
        },
        {
          title: '操作类型',
          key: 'operType',
          width: 120,
          render: function (h, params) {
            let statusStr = ''
            switch (params.row.operType) {
              case 0:
                statusStr = '新刀质检'
                break
              case 1:
                statusStr = '新刀入库'
                break
              case 2:
                statusStr = '领用出库'
                break
              case 3:
                statusStr = '生产返库'
                break
              case 4:
                statusStr = '刃磨出库'
                break
              case 5:
                statusStr = '刃磨'
                break
              case 6:
                statusStr = '刃磨检验'
                break
              case 7:
                statusStr = '涂层出库'
                break
              case 8:
                statusStr = '涂层检验'
                break
              case 9:
                statusStr = '刀盘安装'
                break
              case 10:
                statusStr = '刀具报废申请'
                break
              case 11:
                statusStr = '报废审核'
                break
              case 12:
                statusStr = '执行报废'
                break
            }
            return h('div', statusStr)
          }
        },
        {
          title: '操作时间',
          key: 'createTime',
          minWidth: 150,
          render: function (h, params) {
            return h(
              'div',
              dateFormat(new Date(params.row.createTime), 'yyyy-MM-dd HH:mm:ss')
            )
          }
        },
        {
          title: '操作人',
          key: 'createUserName',
          width: 100
        },
        {
          title: '操作内容',
          key: 'operateInfo',
          width: 160
        },
        {
          title: '备注',
          key: 'operateRemark',
          width: 160
        }
      ],

      formValidate: coat_statistics_form_config.formValidate, // form表单字段
      ruleValidate: coat_statistics_form_config.ruleValidate // form表单验证规则
    }
  },
  created () {},
  methods: {
    init () {},
    showModal (selectRowData) {
      const self = this
      console.log(selectRowData)

      self.statisticsModalshow = true
      self.data = selectRowData
      self.recodeTotal = selectRowData.length
      self.settlementTotal = 0
      self.settlementAmount = 0
      self.payAmount = 0
    },
    showOperModal (fullNumber, beginDate) {
      const self = this
      self.toolOperModalshow = true
      self.searchCondition.fullNumber = fullNumber
      self.searchCondition.beginDate = beginDate
      self.getListData(fullNumber, beginDate)
    },
    getListData (fullNumber, beginDate) {
      const self = this
      let para = {
        fullNumber: fullNumber,
        page: self.searchCondition.page,
        rows: self.searchCondition.rows,
        beginDate: beginDate
      }
      axios
        .request({
          headers: {
            'Content-Type': 'application/json; charset=UTF-8'
          },
          url: '/tool/tool-oper-page-list',
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
    // 关闭弹窗
    closeOperModal () {
      const self = this
      self.toolOperModalshow = false
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
    delRow (idx) {
      const self = this
      self.data.splice(idx, 1)
      self.sumStatistics()
    },
    // 保存
    handleSave () {
      const self = this
      let i = 1
      for (let item of self.data) {
        if (!item.settlementPrice) {
          self.$Message.error('第' + i + '行结算单价不能空!')
          return
        }
        if (!item.settlementQty) {
          self.$Message.error('第' + i + '行结算数量不能空!')
          return
        }
        item.coatTime = null
        i++
      }
      self.isDisabled = true
      let para = {
        settlementList: JSON.stringify(self.data)
      }
      axios
        .request({
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
          },
          url: '/tool/coat-settlement.json',
          method: 'post',
          isAuth: true,
          data: qs.stringify(para)
        })
        .then(function (res) {
          self.statisticsModalshow = false
          if (res.data.code === 200) {
            self.$Message.success('所选涂层已结算!')
            self.isDisabled = false
            self.$emit('search')
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
    },

    rowStatistics (index) {
      const self = this
      let row = self.data[index]
      const settlementPrice = row.settlementPrice
      const settlementQty = row.settlementQty
      const completionDegree = row.completionDegree
      row.settlementAmount = (settlementPrice * settlementQty).toFixed(2)
      row.payPrice = (settlementPrice * completionDegree).toFixed(2)
      row.payAmount = (
        settlementPrice *
        settlementQty *
        completionDegree
      ).toFixed(2)
      self.data.splice(index, 1, row)
      self.sumStatistics()
    },

    rowStatisticsNew (index) {
      this.data[index].coatPrice = this.data[index].settlementPrice
    },

    sumStatistics () {
      const self = this
      let settlementAmount = 0
      let payAmount = 0
      let i = 0
      for (let item of self.data) {
        if (item.settlementAmount) {
          settlementAmount += parseFloat(item.settlementAmount)
          payAmount += parseFloat(item.payAmount)
          i++
        }
      }
      self.settlementAmount = settlementAmount
      self.payAmount = payAmount
      self.settlementTotal = i
    },

    showOperList (fullNumber, beginDate) {
      const self = this
      self.showOperModal(fullNumber, beginDate)
    },

    // 关闭弹窗
    closeModal () {
      const self = this
      self.statisticsModalshow = false
    }
  },
  mounted () {}
}
</script>
<style>
</style>
