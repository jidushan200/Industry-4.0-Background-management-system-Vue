<template>
  <div>
    <Card>
      <Row>
        <Form ref="formInline" :model="searchCondition" :label-width="85" inline>
          <Col span="16">
            <FormItem prop="composeNumber" label="刀盘组合条码">
              <Input
                type="text"
                v-model="searchCondition.composeNumber"
                style="width:170px;"
                placeholder="请输入刀盘组合条码"
              ></Input>
            </FormItem>
            <!--
            <FormItem prop="toolNumber" label="刀条编码">
              <Input
                type="text"
                v-model="searchCondition.toolNumber"
                style="width:170px;"
                placeholder="请输入刀条编码"
              ></Input>
            </FormItem>-->
            <FormItem prop="dateInterval" label="时间区间">
              <DatePicker
                type="daterange"
                split-panels
                placeholder="请选择时间区间"
                placement="bottom-end"
                v-model="searchCondition.dateInterval"
                style="width:180px;"
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
    <tool-oper-modal ref="toolOperModal"></tool-oper-modal>
  </div>
</template>

<script>
import axios from '@/libs/api.request'
import { dateFormat, objCopy } from '@/libs/tools.js'
import { getToken, getBaseUrl } from '@/libs/util'
import toolOperModal from './tool-oper-modal'
export default {
  components: {
    toolOperModal
  },
  data () {
    return {
      searchCondition: {
        dateInterval: [],
        composeNumber: '',
        headNumber: '',
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
          key: 'composeNumber',
          minWidth: 180,
          render: (h, params) => {
            return h('div', [
              h(
                'a',
                {
                  on: {
                    click: () => {
                      this.showOperList(params.row.composeNumber)
                    }
                  }
                },
                params.row.composeNumber
              )
            ])
          }
        },
        {
          title: '刀条组合编码',
          align: 'center',
          key: 'headNumber',
          minWidth: 120
        },
        {
          title: '刀条组合名称',
          align: 'center',
          key: 'headName',
          minWidth: 220
        },
        {
          title: '刀条',
          align: 'center',
          children: [
            {
              title: '物料编码',
              align: 'center',
              minWidth: 150,
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
              title: '物料名称',
              align: 'center',
              minWidth: 260,
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
              title: '物料图号',
              align: 'center',
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
              title: '物料数量',
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
          title: '刀条状态',
          key: 'toolStatus',
          minWidth: 100,
          render: function (h, params) {
            let temp = ''
            switch (params.row.toolStatus) {
              case 1:
                temp = '生产中'
                break
              case 2:
                temp = '待刃磨'
                break
              case 3:
                temp = '待刃磨质检'
                break
              case 4:
                temp = '待涂层'
                break
              case 5:
                temp = '待涂层质检'
                break
              case 6:
                temp = '待安装'
                break
              case 7:
                temp = '待报废'
                break
              case 8:
                temp = '已报废'
                break
              default:
                break
            }
            return h('div', temp)
          }
        },
        {
          title: '已加工数量',
          key: 'productionQty',
          width: 100
        },
        {
          title: '组合时间',
          key: 'createTime',
          minWidth: 130,
          render: function (h, params) {
            return h(
              'div',
              dateFormat(new Date(params.row.createTime), 'yyyy-MM-dd HH:mm:ss')
            )
          }
        },
        {
          title: '加工记录',
          align: 'center',
          children: [
            {
              title: '加工开始时间',
              key: 'processList',
              align: 'center',
              width: 120,
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
                      undefined != this.tableData.rows[params.index].processList
                        ? this.tableData.rows[params.index].processList.map(
                          item => {
                            if (item.endTime != null) {
                              return h(
                                'li',
                                {},
                                dateFormat(
                                  new Date(item.beginTime),
                                  'yyyy-MM-dd hh:mm'
                                )
                              )
                            } else {
                              return h('li', {}, '暂无日期数据')
                            }
                          }
                        )
                        : ''
                    )
                  ]
                )
              }
            },
            {
              title: '加工结束时间',
              key: 'processList',
              align: 'center',
              width: 120,
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
                      undefined != this.tableData.rows[params.index].processList
                        ? this.tableData.rows[params.index].processList.map(
                          item => {
                            if (item.endTime != null) {
                              return h(
                                'li',
                                {},
                                dateFormat(
                                  new Date(item.endTime),
                                  'yyyy-MM-dd hh:mm'
                                )
                              )
                            } else {
                              return h('li', {}, '暂无日期数据')
                            }
                          }
                        )
                        : ''
                    )
                  ]
                )
              }
            },
            {
              title: '设备标签',
              key: 'processList',
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
                      undefined != this.tableData.rows[params.index].processList
                        ? this.tableData.rows[params.index].processList.map(
                          item => {
                            return h('li', {}, item.tagNumber)
                          }
                        )
                        : ''
                    )
                  ]
                )
              }
            },
            {
              title: '设备名称',
              key: 'processList',
              align: 'center',
              minWidth: 150,
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
                      undefined != this.tableData.rows[params.index].processList
                        ? this.tableData.rows[params.index].processList.map(
                          item => {
                            return h('li', {}, item.equipmentName)
                          }
                        )
                        : ''
                    )
                  ]
                )
              }
            },
            {
              title: '制件编号',
              key: 'processList',
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
                      undefined != this.tableData.rows[params.index].processList
                        ? this.tableData.rows[params.index].processList.map(
                          item => {
                            return h('li', {}, item.partCode)
                          }
                        )
                        : ''
                    )
                  ]
                )
              }
            },
            {
              title: '制件名称',
              key: 'processList',
              align: 'center',
              minWidth: 250,
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
                      undefined != this.tableData.rows[params.index].processList
                        ? this.tableData.rows[params.index].processList.map(
                          item => {
                            return h('li', {}, item.partName)
                          }
                        )
                        : ''
                    )
                  ]
                )
              }
            },
            {
              title: '加工数量',
              key: 'processList',
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
                      undefined != this.tableData.rows[params.index].processList
                        ? this.tableData.rows[params.index].processList.map(
                          item => {
                            return h('li', {}, item.processQty)
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
          title: '刃磨记录',
          align: 'center',
          children: [
            {
              title: '刃磨时间',
              key: 'repairList',
              align: 'center',
              width: 120,
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
                      undefined != this.tableData.rows[params.index].repairList
                        ? this.tableData.rows[params.index].repairList.map(
                          item => {
                            return h(
                              'li',
                              {},
                              dateFormat(
                                new Date(item.executTime),
                                'yyyy-MM-dd hh:mm'
                              )
                            )
                          }
                        )
                        : ''
                    )
                  ]
                )
              }
            },
            {
              title: '物料编号',
              key: 'repairList',
              align: 'center',
              width: 120,
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
                      undefined != this.tableData.rows[params.index].repairList
                        ? this.tableData.rows[params.index].repairList.map(
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
              title: '刃磨量',
              key: 'repairList',
              align: 'center',
              width: 120,
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
                      undefined != this.tableData.rows[params.index].repairList
                        ? this.tableData.rows[params.index].repairList.map(
                          item => {
                            return h('li', {}, item.repairMeasure)
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
          title: '涂层记录',
          align: 'center',
          key: 'processAmount',
          children: [
            {
              title: '物料编号',
              key: 'coatList',
              align: 'center',
              width: 120,
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
                      undefined != this.tableData.rows[params.index].coatList
                        ? this.tableData.rows[params.index].coatList.map(
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
              title: '涂层时间',
              key: 'coatList',
              align: 'center',
              width: 120,
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
                      undefined != this.tableData.rows[params.index].coatList
                        ? this.tableData.rows[params.index].coatList.map(
                          item => {
                            return h(
                              'li',
                              {},
                              dateFormat(
                                new Date(item.coatTime),
                                'yyyy-MM-dd hh:mm'
                              )
                            )
                          }
                        )
                        : ''
                    )
                  ]
                )
              }
            },
            {
              title: '涂层厂家',
              key: 'coatList',
              align: 'center',
              width: 200,
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
                      undefined != this.tableData.rows[params.index].coatList
                        ? this.tableData.rows[params.index].coatList.map(
                          item => {
                            return h('li', {}, item.coatSupplier)
                          }
                        )
                        : ''
                    )
                  ]
                )
              }
            }
          ]
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
          url: '/tool/blade-compose-life-page-list',
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
    showOperList (fullNumber) {
      const self = this
      self.$refs.toolOperModal.showModal(fullNumber)
    },
    exportData () {
      const self = this
      let para = 'token=' + getToken()
      let composeNumber = self.searchCondition.composeNumber
      let toolNumber = self.searchCondition.toolNumber
      let beginDate = self.searchCondition.beginDate
      let endDate = self.searchCondition.endDate
      if (composeNumber) {
        para = para + '&composeNumber=' + composeNumber
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
      window.location.href = getBaseUrl() + '/tool/blade-export?' + para
    }
  }
}
</script>

<style>
.subCol {
  width: 100%;
  height: 100%;
}

.subCol > ul {
  width: 100%;
  display: flex;
  height: 100%;
  flex-direction: column;
}

.subCol > ul > li {
  margin: 0 -18px;
  /* list-style: none;*/
  text-align: center;
  padding: 9px;
  border-bottom: 1px solid #ccc;
  overflow-x: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
  flex: 1;
}
.subCol > ul > li:last-child {
  border-bottom: none;
}

th .ivu-table-cell{
  margin: 0 auto;
  text-align: center;
  width: 100%;
}

td .ivu-table-cell {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
