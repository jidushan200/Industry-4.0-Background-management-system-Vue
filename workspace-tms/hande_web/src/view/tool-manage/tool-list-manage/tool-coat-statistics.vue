<template>
  <div>
    <Card>
      <Row>
        <Form ref="formInline" :model="searchCondition" :label-width="65" inline>
          <Col span="18">
            <FormItem label="结算状态" prop="settlementStatus">
              <Select
                style="width:120px"
                v-model="searchCondition.settlementStatus"
                ref="settlementStatus"
                clearable
                filterable
              >
                <Option
                  v-for="item in settlementStatusArray"
                  :value="item.value"
                  :key="item.value"
                >{{item.name }}</Option>
              </Select>
            </FormItem>
            <FormItem label="供应商" prop="supplierId">
              <Select
                style="width:170px"
                v-model="searchCondition.supplierId"
                ref="supplierId"
                clearable
                filterable
              >
                <Option
                  v-for="item in supplierList"
                  :value="item.pkId"
                  :key="item.pkId"
                >{{item.supplierName }}</Option>
              </Select>
            </FormItem>
            <FormItem prop="dateInterval" label="涂回时间">
              <DatePicker
                type="daterange"
                split-panels
                placeholder="请选择时间区间"
                placement="bottom-end"
                v-model="searchCondition.dateInterval"
                style="width:180px"
              ></DatePicker>
            </FormItem>
            <FormItem prop="timeInterval" label="结算时间">
              <DatePicker
                type="daterange"
                split-panels
                placeholder="请选择时间区间"
                placement="bottom-end"
                v-model="searchCondition.timeInterval"
                style="width:180px"
              ></DatePicker>
            </FormItem>
            <FormItem prop="degreeMin" label="完成度">
              <InputNumber
                style="width:70px;"
                :max="100"
                v-model="searchCondition.degreeMin"
                :formatter="value => `${value}%`"
                :parser="value => value.replace('%', '')"
                empty
              ></InputNumber>
            </FormItem>
            <FormItem prop="degreeMax" label="-">
              <InputNumber
                style="width:70px;"
                :max="200"
                :min=0
                v-model="searchCondition.degreeMax"
                :formatter="value => `${value}%`"
                :parser="value => value.replace('%', '')"
              ></InputNumber>
            </FormItem>
            <FormItem>
              <Button type="primary" style="margin-left: -60px" @click="doSearch('formInline')">搜索</Button>
            </FormItem>
            <FormItem style="margin-left: -65px">
              <Button type="dashed" @click="doReset('formInline')">重置</Button>
            </FormItem>
            <FormItem style="margin-left: -65px">
              <Button type="success" style="margin-right: 5px;" @click="coatStatistics">
                <Icon type="md-calculator"></Icon>结算
              </Button>
            </FormItem>
          </Col>
          <Col span="6">
            <FormItem style="width:100%;text-align:right">
              <Button type="warning" @click="exportData()">
                <Icon type="ios-download-outline"></Icon>导出
              </Button>
            </FormItem>
          </Col>
        </Form>
      </Row>

      <Table
        ref="tablesMain"
        :data="tableData.rows"
        :columns="tableColumns"
        stripe
        border
        @on-selection-change="selectionChange"
      ></Table>
      <div style="margin: 10px;overflow: hidden">
        <Col span="12">
          <label for>结算数量：</label>
          <Input style="width:120px" v-model="recodeTotal" readonly></Input>&nbsp;&nbsp;
          <label for>结算总价：</label>
          <Input style="width:120px" v-model="settlementAmount" readonly></Input>
        </Col>
        <div style="float: right;">
          <Page
            show-total
            :total="tableData.total"
            :page-size="searchCondition.rows"
            :current="searchCondition.page"
            @on-change="changePage"
            @on-page-size-change="handlePageSize"
            :page-size-opts="[10,20,50,100]"
            show-sizer
          ></Page>
        </div>
      </div>
    </Card>

    <Modal v-model="settlementModalshow" title="复核结算价格" width="800" draggable>
      <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="80" inline>
        <Row>
          <row>
            <Col span="8">
              <FormItem label="物料条码" prop="fullNumber">
                <Input style="width:170px" v-model="formValidate.fullNumber" readonly />
              </FormItem>
            </Col>
            <Col span="8">
              <FormItem label="物料名称" prop="toolName">
                <Input style="width:170px" v-model="formValidate.toolName" readonly />
              </FormItem>
            </Col>
            <Col span="8">
              <FormItem label="物料图号" prop="toolMap">
                <Input style="width:170px" v-model="formValidate.toolMap" readonly />
              </FormItem>
            </Col>
          </row>
          <row>
            <Col span="8">
              <FormItem label="供应商名称" prop="coatSupplier">
                <Input style="width:170px" v-model="formValidate.coatSupplier" readonly />
              </FormItem>
            </Col>
            <Col span="8">
              <FormItem label="涂回时间" prop="coatTime">
                <Input style="width:170px" v-model="formValidate.coatTime" readonly />
              </FormItem>
            </Col>
            <Col span="8">
              <FormItem label="加工数量" prop="processQty">
                <Input style="width:170px" v-model="formValidate.processQty" readonly />
              </FormItem>
            </Col>
          </row>
          <row>
            <Col span="8">
              <FormItem label="加工数量" prop="theoreticalQty">
                <Input style="width:170px" v-model="formValidate.theoreticalQty" readonly />
              </FormItem>
            </Col>
            <Col span="8">
              <FormItem label="加工完成度" prop="completionDegree">
                <Input style="width:170px" v-model="formValidate.completionDegree" readonly />
              </FormItem>
            </Col>
            <Col span="8">
              <FormItem label="理论价格" prop="coatPrice">
                <Input style="width:170px" v-model="formValidate.coatPrice" />
              </FormItem>
            </Col>
          </row>
          <row>
            <Col span="8">
              <FormItem label="结算价格" prop="settlementPrice">
                <InputNumber
                  style="width:170px"
                  v-model="formValidate.settlementPrice"
                  @on-blur="statistics()"
                />
              </FormItem>
            </Col>
            <Col span="8">
              <FormItem label="结算数量" prop="settlementQty">
                <InputNumber
                  style="width:170px"
                  v-model="formValidate.settlementQty"
                  @on-blur="statistics()"
                />
              </FormItem>
            </Col>
            <Col span="8">
              <FormItem label="结算总价" prop="processQty">
                <Input style="width:170px" v-model="formValidate.settlementAmount" readonly />
              </FormItem>
            </Col>
          </row>
          <row>
            <Col span="8">
              <FormItem label="支付价格" prop="payPrice">
                <Input style="width:170px" v-model="formValidate.payPrice" />
              </FormItem>
            </Col>
            <Col span="8">
              <FormItem label="支付总价" prop="payAmount">
                <Input style="width:170px" v-model="formValidate.payAmount" />
              </FormItem>
            </Col>
          </row>
        </Row>
      </Form>
      <div slot="footer">
        <Button @click="handleSubmit('formValidate')" type="primary">保存</Button>
        <Button @click="handleReset('formValidate')" style="margin-left: 8px">取消</Button>
      </div>
    </Modal>
    <coat-statistics-modal ref="coatStatisticsModal" @search="getListData"></coat-statistics-modal>
  </div>
</template>

<script>
import axios from '@/libs/api.request'
import { getToken, getBaseUrl } from '@/libs/util'
import { dateFormat, objCopy } from '@/libs/tools.js'
import coatStatisticsModal from './coat-statistics-modal.vue'
import coat_statistics_form_config from './coat-statistics-form-config.js'
export default {
  components: { coatStatisticsModal },
  data () {
    return {
      searchCondition: {
        settlementStatus: null,
        supplierId: null,
        degreeMin: null,
        degreeMax: null,
        dateInterval: [],
        timeInterval: [],
        page: 1,
        rows: 10
      },
      recodeTotal: 0,
      settlementAmount: 0,
      settlementModalshow: false,
      supplierList: [],
      multiselectRowData: [], // 复选列数据
      tableData: {
        total: 0, // 总页数
        rows: [] // 每页条数
      },
      settlementStatusArray: [
        {
          name: '未结算',
          value: 0
        },
        {
          name: '已结算',
          value: 1
        }
      ],
      tableColumns: [
        {
          // 多选框
          type: 'selection',
          width: 60,
          align: 'center',
          fixed: 'left'
        },
        {
          title: '结算价格',
          key: 'settlementPrice',
          minWidth: 100
        },
        {
          title: '结算总价',
          key: 'settlementAmount',
          minWidth: 100
        },
        {
          title: '理论价格',
          key: 'coatPrice',
          minWidth: 100
        },
        {
          title: '支付价格',
          key: 'payPrice',
          minWidth: 100
        },
        {
          title: '支付总价',
          key: 'payAmount',
          minWidth: 100
        },
        {
          title: '结算数量',
          key: 'settlementQty',
          minWidth: 100
        },
        {
          title: '结算人',
          key: 'settlementName',
          minWidth: 100
        },
        {
          title: '结算时间',
          key: 'settlementTime',
          width: 130,
          render: function (h, params) {
            if (params.row.settlementTime != null) {
              return h(
                'div',
                dateFormat(
                  new Date(params.row.settlementTime),
                  'yyyy-MM-dd HH:mm'
                )
              )
            }
          }
        },
        {
          title: '供应商名称',
          key: 'coatSupplier',
          minWidth: 220
        },
        {
          title: '物料编码',
          key: 'toolNumber',
          sortable: 'custom',
          minWidth: 120
        },
        {
          title: '物料名称',
          key: 'toolName',
          minWidth: 220
        },
        {
          title: '物料图号',
          key: 'toolMap',
          minWidth: 160
        },
        {
          title: '物料条码',
          key: 'fullNumber',
          minWidth: 260
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
              return h(
                'div',
                Math.floor(params.row.completionDegree * 100) + '%'
              )
            }
          }
        },
        {
          title: '操作',
          key: 'action',
          width: 130,
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
                    marginRight: '5px',
                    display:
                      params.row.settlementStatus === 1
                        ? 'inline-block'
                        : 'none'
                  },
                  on: {
                    click: () => {
                      this.updateSettlement(params.row)
                    }
                  }
                },
                '修改'
              )
            ])
          }
        }
      ],
      formValidate: coat_statistics_form_config.formValidate, // user form表单字段
      ruleValidate: coat_statistics_form_config.ruleValidate // user form表单验证规则
    }
  },
  created () {
    const self = this
    self.getSupplierList()
    self.getListData()
  },
  methods: {
    getSupplierList () {
      const self = this
      let para = {
        isCoat: 1
      }
      axios
        .request({
          headers: {
            'Content-Type': 'application/json; charset=UTF-8'
          },
          url: '/base/supplier-list',
          method: 'post',
          isAuth: true,
          params: para
        })
        .then(function (res) {
          const data = res.data
          if (data.code === 200) {
            self.supplierList = data.data
          }
        })
        .catch(function (err) {
          console.log(err)
        })
    },
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
      if (self.searchCondition.timeInterval) {
        let beginTime = self.searchCondition.timeInterval[0]
        let endTime = self.searchCondition.timeInterval[1]
        if (beginTime) {
          self.searchCondition.beginTime = dateFormat(
            new Date(beginTime),
            'yyyy-MM-dd'
          )
        } else {
          self.searchCondition.beginTime = null
        }
        if (endTime) {
          self.searchCondition.endTime = dateFormat(
            new Date(endTime),
            'yyyy-MM-dd'
          )
        } else {
          self.searchCondition.endTime = null
        }
      } else {
        self.searchCondition.beginTime = null
        self.searchCondition.endTime = null
      }

      let para = {
        supplierId: self.searchCondition.supplierId,
        settlementStatus: self.searchCondition.settlementStatus,
        page: self.searchCondition.page,
        rows: self.searchCondition.rows,
        beginDate: self.searchCondition.beginDate,
        endDate: self.searchCondition.endDate,
        beginTime: self.searchCondition.beginTime,
        endTime: self.searchCondition.endTime,
        degreeMin: self.searchCondition.degreeMin,
        degreeMax: self.searchCondition.degreeMax,
        sort: self.searchCondition.sort
      }
      axios
        .request({
          headers: {
            'Content-Type': 'application/json; charset=UTF-8'
          },
          url: '/tool/coat-statistics-page-list',
          method: 'post',
          isAuth: true,
          params: para
        })
        .then(function (res) {
          const data = res.data
          if (data.code === 200) {
            self.recodeTotal = 0
            self.settlementAmount = 0
            for (let item of data.rows) {
              if (item.settlementStatus == 1) {
                item._disabled = true
                self.recodeTotal++
                self.settlementAmount =
                  self.settlementAmount + item.settlementAmount
              }
            }
            self.tableData = data
            self.multiselectRowData = []
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
    selectionChange (selection) {
      const self = this
      self.multiselectRowData = selection
    },
    coatStatistics () {
      const self = this
      if (self.multiselectRowData.length < 1) {
        self.$Message.error('请选择要结算的数据!')
        return
      }
      self.$refs.coatStatisticsModal.showModal(self.multiselectRowData)
    },

    updateSettlement (row) {
      const self = this
      self.settlementModalshow = true
      self.formValidate = objCopy(row, {})
      self.formValidate.coatTime = dateFormat(
        new Date(row.coatTime),
        'yyyy-MM-dd HH:mm:ss'
      )
    },

    statistics () {
      const self = this
      const settlementPrice = self.formValidate.settlementPrice
      if (!settlementPrice) {
        return
      }
      const settlementQty = self.formValidate.settlementQty
      if (!settlementQty) {
        return
      }
      const completionDegree = self.formValidate.completionDegree
      self.formValidate.settlementAmount = (
        settlementPrice * settlementQty
      ).toFixed(2)
      self.formValidate.payPrice = (settlementPrice * completionDegree).toFixed(
        2
      )
      self.formValidate.payAmount = (
        settlementPrice *
        settlementQty *
        completionDegree
      ).toFixed(2)
    },

    handleSubmit (name) {
      const self = this
      self.$refs[name].validate(valid => {
        if (valid) {
          let para = {
            pkId: self.formValidate.pkId,
            settlementPrice: self.formValidate.settlementPrice,
            settlementQty: self.formValidate.settlementQty
          }
          axios
            .request({
              headers: {
                'Content-Type': 'application/json; charset=UTF-8'
              },
              url: '/tool/tool-coat-settlement-update',
              method: 'post',
              isAuth: true,
              params: para
            })
            .then(function (res) {
              const data = res.data
              if (data.code === 200) {
                self.settlementModalshow = false
                self.$Message.success('涂层结算已修改!')
                self.getListData()
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
      const self = this
      self.$refs['formValidate'].resetFields()
      self.settlementModalshow = false
    },

    changeSort () {
      const self = this
      if (self.sort === 'DESC') {
        self.sort = 'ASC'
      } else {
        self.sort = 'DESC'
      }
      self.searchCondition.sort = self.sort
      self.changePage(1)
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

      if (self.searchCondition.timeInterval) {
        let beginTime = self.searchCondition.timeInterval[0]
        let endTime = self.searchCondition.timeInterval[1]
        if (beginTime) {
          self.searchCondition.beginTime = dateFormat(
            new Date(beginTime),
            'yyyy-MM-dd'
          )
        } else {
          self.searchCondition.beginTime = null
        }
        if (endTime) {
          self.searchCondition.endTime = dateFormat(
            new Date(endTime),
            'yyyy-MM-dd'
          )
        } else {
          self.searchCondition.endTime = null
        }
      } else {
        self.searchCondition.beginTime = null
        self.searchCondition.endTime = null
      }

      let supplierId = self.searchCondition.supplierId
      let beginDate = self.searchCondition.beginDate
      let endDate = self.searchCondition.endDate
      let beginTime = self.searchCondition.beginTime
      let endTime = self.searchCondition.endTime
      let settlementStatus = self.searchCondition.settlementStatus
      let degreeMin = self.searchCondition.degreeMin
      let degreeMax = self.searchCondition.degreeMax
      let para = ''
      if (supplierId) {
        para = '&supplierId=' + supplierId
      }
      if (beginDate) {
        para = para + '&beginDate=' + beginDate
      }
      if (endDate) {
        para = para + '&endDate=' + endDate
      }
      if (beginTime) {
        para = para + '&beginTime=' + beginTime
      }
      if (endTime) {
        para = para + '&endTime=' + endTime
      }
      if (settlementStatus != null) {
        para = para + '&settlementStatus=' + settlementStatus
      }
      if (degreeMin != null) {
        para = para + '&degreeMin=' + degreeMin
      }
      if (degreeMax != null) {
        para = para + '&degreeMax=' + degreeMax
      }
      debugger
      window.location.href =
        getBaseUrl() +
        '/tool/coat-settlement-export?token=' +
        getToken() +
        para
    }
  }
}
</script>
