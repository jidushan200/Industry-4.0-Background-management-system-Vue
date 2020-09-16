<template>
  <div>
    <Card>
      <Row>
        <Form ref="formInline" :model="searchCondition" :label-width="85" inline>
          <Col span="16">
            <FormItem prop="composeNumber" label="刀条组合条码">
              <Input type="text" v-model="searchCondition.composeNumber" placeholder="请输入刀条组合条码"></Input>
            </FormItem>
            <FormItem prop="dateInterval" label="归还时间">
              <DatePicker
                type="daterange"
                split-panels
                placeholder="请选择时间区间"
                placement="bottom-end"
                style="width:180px;"
                v-model="searchCondition.dateInterval"
              ></DatePicker>
            </FormItem>
            <FormItem>
              <Button type="primary" style="margin-left:-60px" @click="doSearch('formInline')">搜索</Button>
            </FormItem>
            <FormItem style="margin-left:-65px">
              <Button type="dashed" @click="doReset('formInline')">重置</Button>
            </FormItem>
          </Col>
          <Col span="8">
            <FormItem style="width:100%;text-align:right">
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
  </div>
</template>

<script>
import axios from '@/libs/api.request'
import { dateFormat, objCopy } from '@/libs/tools.js'
import { getToken, getBaseUrl } from '@/libs/util'

export default {
  data () {
    return {
      searchCondition: {
        dateInterval: [],
        composeNumber: '',
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
          key: 'composeNumber',
          minWidth: 180
        },
        {
          title: '刀条组合名称',
          fixed: 'left',
          key: 'headName',
          minWidth: 220
        },
        {
          title: '物料编码',
          fixed: 'left',
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
                    ? this.tableData.rows[params.index].detailList.map(item => {
                      return h('li', {}, item.toolNumber)
                    })
                    : ''
                )
              ]
            )
          }
        },
        {
          title: '物料名称',
          fixed: 'left',
          minWidth: 220,
          render: (h, params) => {
            console.log(params)
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
                    ? this.tableData.rows[params.index].detailList.map(item => {
                      return h('li', {}, item.toolName)
                    })
                    : ''
                )
              ]
            )
          }
        },
        {
          title: '物料图号',
          minWidth: 160,
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
                    ? this.tableData.rows[params.index].detailList.map(item => {
                      return h('li', {}, item.toolMap)
                    })
                    : ''
                )
              ]
            )
          }
        },
        {
          title: '使用部门',
          key: 'departmentName',
          minWidth: 120
        },
        {
          title: '领用时间',
          key: 'beginTime',
          minWidth: 150,
          render: function (h, params) {
            return h(
              'div',
              dateFormat(new Date(params.row.beginTime), 'yyyy-MM-dd HH:mm')
            )
          }
        },
        {
          title: '归还时间',
          key: 'endTime',
          minWidth: 150,
          render: function (h, params) {
            return h(
              'div',
              dateFormat(new Date(params.row.endTime), 'yyyy-MM-dd HH:mm')
            )
          }
        },
        {
          title: '制件编码',
          key: 'partCode',
          minWidth: 140
        },
        {
          title: '制件名称',
          key: 'partName',
          minWidth: 280
        },
        {
          title: '设备标签',
          key: 'tagNumber',
          minWidth: 120
        },
        {
          title: '设备名称',
          key: 'equipmentName',
          minWidth: 150
        },
        {
          title: '理论加工数量',
          key: 'theoreticalQty',
          width: 120
        },
        {
          title: '加工数量',
          key: 'processQty',
          width: 100
        }
      ]
    }
  },
  created () {
    const self = this
    self.getListData()
  },
  methods: {
    getListData () {
      const self = this
      if (self.searchCondition.dateInterval) {
        let beginDate = self.searchCondition.dateInterval[0]
        let endDate = self.searchCondition.dateInterval[1]
        if (beginDate) {
          self.searchCondition.beginDate = dateFormat(
            new Date(beginDate),
            'yyyy-MM-dd'
          )
        } else {
          self.searchCondition.beginDate = null
        }
        if (endDate) {
          self.searchCondition.endDate = dateFormat(
            new Date(endDate),
            'yyyy-MM-dd'
          )
        } else {
          self.searchCondition.endDate = null
        }
      } else {
        self.searchCondition.beginDate = null
        self.searchCondition.endDate = null
      }
      let para = {
        composeNumber: self.searchCondition.composeNumber,
        toolNumber: self.searchCondition.toolNumber,
        page: self.searchCondition.page,
        rows: self.searchCondition.rows,
        beginDate: self.searchCondition.beginDate,
        endDate: self.searchCondition.endDate
      }
      axios
        .request({
          headers: {
            'Content-Type': 'application/json; charset=UTF-8'
          },
          url: '/tool/blade-process-page-list',
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
    exportData () {
      const self = this
      if (self.searchCondition.dateInterval) {
        let beginDate = self.searchCondition.dateInterval[0]
        let endDate = self.searchCondition.dateInterval[1]
        if (beginDate) {
          self.searchCondition.beginDate = dateFormat(
            new Date(beginDate),
            'yyyy-MM-dd'
          )
        } else {
          self.searchCondition.beginDate = null
        }
        if (endDate) {
          self.searchCondition.endDate = dateFormat(
            new Date(endDate),
            'yyyy-MM-dd'
          )
        } else {
          self.searchCondition.endDate = null
        }
      } else {
        self.searchCondition.beginDate = null
        self.searchCondition.endDate = null
      }

      let composeNumber = self.searchCondition.composeNumber
      let toolNumber = self.searchCondition.toolNumber
      let beginDate = self.searchCondition.beginDate
      let endDate = self.searchCondition.endDate
      let para = ''
      if (composeNumber) {
        para = '&composeNumber=' + composeNumber
      }
      if (toolNumber) {
        para = para + '&toolNumber=' + toolNumber
      }
      if (beginDate) {
        para = para + '&beginDate=' + beginDate
      }
      if (endDate) {
        para = para + '&endDate=' + endDate
      }
      window.location.href =
        getBaseUrl() + '/tool/blade-process-export?token=' + getToken() + para
    }
  }
}
</script>

<style>
.subCol > ul > li {
  margin: 0 -18px;
  list-style: none;
  text-align: center;
  padding: 9px;
  border-bottom: 1px solid #ccc;
  overflow-x: hidden;
}
.subCol > ul > li:last-child {
  border-bottom: none;
}
</style>
