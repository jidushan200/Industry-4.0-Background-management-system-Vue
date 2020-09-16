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
            <FormItem prop="toolNumber" label="刀条编码">
              <Input type="text" v-model="searchCondition.toolNumber" placeholder="请输入刀条编码"></Input>
            </FormItem>
            <FormItem>
              <Button type="primary" style="margin-left: -60px;" @click="doSearch('formInline')">搜索</Button>
            </FormItem>
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

    <Modal v-model="outboundModalshow" title="新刀条涂层出库" :width="580" draggable>
      <Form ref="formValidateNew" :model="formValidate" :rules="ruleValidate" :label-width="80" inline>
        <Row>
          <Col span="12">
            <FormItem label="刀条编号" prop="toolNumber">
              <Input style="width:180px" v-model="toolNumber" readonly />
            </FormItem>
          </Col>
          <Col span="12">
            <FormItem label="刀条名称" prop="toolName">
              <Input style="width:180px" v-model="toolName" readonly />
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span="12">
            <FormItem label="刀条图号" prop="toolMap">
              <Input style="width:180px" v-model="toolMap" readonly />
            </FormItem>
          </Col>

          <Col span="12">
            <FormItem label="出库数量" prop="toolQty">
              <InputNumber style="width:180px" v-model="toolQty" />
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span="12">
            <FormItem label="供应商" prop="supplierId">
              <Select
                style="width:180px"
                v-model="formValidate.supplierId"
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
          </Col>
          <Col span="12">
            <FormItem label="领料人" prop="receiver">
              <Input style="width:180px" v-model="receiver" />
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span="24">
            <FormItem label="说明" prop="remark">
              <Input style="width:460px" v-model="formValidate.remark" placeholder="输入说明" />
            </FormItem>
          </Col>
        </Row>
      </Form>
      <div slot="footer">
        <Button @click="handleSubmit('formValidateNew')" type="primary" :disabled="isDisabled">出库</Button>
        <Button @click="handleReset('formValidateNew')" style="margin-left: 8px">取消</Button>
      </div>
    </Modal>

    <Modal v-model="composeOutboundModalshow" title="组合涂层出库" :width="width" draggable>
      <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="90" inline>
        <Row>
          <Col span="8">
            <FormItem label="组合条码" prop="composeNumber">
              <Input style="width:200px" v-model="composeNumber" readonly />
            </FormItem>
          </Col>
          <Col span="8">
            <FormItem label="组合名称" prop="headName">
              <Input style="width:200px" v-model="headName" readonly />
            </FormItem>
          </Col>
          <Col span="8">
            <FormItem label="换刀加工数" prop="eachProcessQty">
              <Input style="width:200px" v-model="eachProcessQty" readonly />
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Table
            :data="detailist"
            :columns="detailColumns"
            style="margin-bottom:15px;"
            highlight-row
            stripe
            border
          ></Table>
        </Row>
        <Row>
          <Col span="8">
            <FormItem label="设备名称" prop="equipmentId">
              <Select
                style="width:200px"
                v-model="formValidate.equipmentId"
                ref="equipmentId"
                @on-change="equipmentListSelectChange"
                clearable
                filterable
              >
                <Option
                  v-for="item in equipmentList"
                  :value="item.pkId"
                  :key="item.pkId"
                >{{ item.tagNumber }}-{{ item.equipmentName }}</Option>
              </Select>
            </FormItem>
          </Col>
          <Col span="16">
            <FormItem label="工人职工号" prop="staffCode">
              <Input
                style="width:200px"
                v-model="formValidate.staffCode"
                placeholder="输入工人职工号"
                @on-blur="searchStaff()"
              >
                <Icon type="ios-search" slot="suffix" />
              </Input>&nbsp;
              <Input style="width:290px" v-model="staffInfo" readonly></Input>
            </FormItem>
          </Col>
        </Row>

        <row>
          <Table
            ref="myTable"
            border
            :columns="columns"
            :data="partList"
            style="margin-bottom:15px;"
            highlight-row
          ></Table>
        </row>
        <Row>
          <Col span="8">
            <FormItem label="供应商" prop="supplierId">
              <Select
                style="width:200px"
                v-model="formValidate.supplierId"
                ref="supplierId"
                @on-change="supplierSelectChange"
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
          </Col>
          <Col span="16">
            <FormItem label="说明" prop="remark">
              <Input style="width:530px" v-model="formValidate.remark" placeholder="输入说明" />
            </FormItem>
          </Col>
        </Row>
      </Form>
      <div slot="footer">
        <Button @click="composeSubmit('formValidate')" type="primary" :disabled="isDisabled">出库</Button>
        <Button @click="composeReset('formValidate')" style="margin-left: 8px">取消</Button>
      </div>
    </Modal>
  </div>
</template>

<script>
import axios from '@/libs/api.request'
import tool_coat_manage_form_config from './tool-coat-form-config.js'
import { dateFormat, objCopy } from '@/libs/tools.js'
import qs from 'qs'
export default {
  data () {
    return {
      isDisabled: false,
      outboundModalshow: false, // 控制明细modal显示
      composeOutboundModalshow: false,
      composeNumber: '',
      headName: '',
      toolMap: '',
      toolNumber: '',
      toolName: '',
      partName: '',
      toolQty: 0,
      receiver: '',
      newBlade: '',
      partList: [],
      equipmentList: [],
      eachProcessQty: 0,
      processQty: 0,
      width: 960,
      staffInfo: '',
      departmentName: '',
      departmentId: null,
      supplierList: [],
      supplierName: '',
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
          minWidth: 200,
          key: 'headName'
        },
        {
          title: '物料编码',
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
          title: '物料数量',
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
                    ? this.tableData.rows[params.index].detailList.map(item => {
                      return h('li', {}, item.toolQty)
                    })
                    : ''
                )
              ]
            )
          }
        },
        {
          title: '当前状态',
          minWidth: 100,
          key: 'handleResult',
          render: function (h, params) {
            let statusStr = ''
            switch (params.row.handleResult) {
              case 1:
                statusStr = '待涂层入库'
                break
              case 0:
                statusStr = '待涂层出库'
                break
            }
            return h('div', statusStr)
          }
        },
        {
          title: '入库时间',
          minWidth: 160,
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
                    marginRight: '5px',
                    display:
                      params.row.handleResult === 0 ? 'inline-block' : 'none'
                  },
                  on: {
                    click: () => {
                      this.setCoat(params.row)
                    }
                  }
                },
                '涂层出库'
              )
            ])
          }
        }
      ],
      detailist: [],
      detailColumns: [
        {
          title: '物料编码',
          key: 'toolNumber',
          minWidth: 260
        },
        {
          title: '物料名称',
          key: 'toolName',
          minWidth: 260
        },
        {
          title: '物料图号',
          key: 'toolMap',
          minWidth: 160
        },
        {
          title: '出库数量',
          key: 'coatQty',
          minWidth: 100,
          render: (h, params) => {
            return h('Input', {
              props: {
                value: this.detailist[params.index].toolQty
              },
              on: {
                input: val => {
                  const self = this
                  self.detailist[params.index].coatQty = val
                }
              }
            })
          }
        }
      ],
      columns: [
        {
          title: '制件编码',
          key: 'partCode'
        },
        {
          title: '制件名称',
          key: 'partName'
        },
        {
          title: '制件数量',
          key: 'partQty',
          render: (h, params) => {
            return h('InputNumber', {
              props: {
                value: this.partList[params.index].partQty
              },
              on: {
                input: val => {
                  const self = this
                  self.partList[params.index].partQty = val
                  let i = 1
                  let qty = 0
                  for (let item of self.partList) {
                    if (item.partQty) {
                      qty = qty + item.partQty
                    }
                    i++
                  }
                  self.processQty = qty
                }
              }
            })
          }
        }
      ],
      formValidate: tool_coat_manage_form_config.formValidate, // user form表单字段
      ruleValidate: tool_coat_manage_form_config.ruleValidate // user form表单验证规则
    }
  },
  created () {
    const self = this
    self.getListData()
    self.getSupplierList()
    self.getEquipmentListData()
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
          url: '/tool/blade-wait-coat-page-list',
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
    supplierSelectChange (item) {
      const self = this
      let supplierList = self.supplierList
      if (item !== undefined) {
        for (const supplier of supplierList) {
          if (supplier.pkId === item) {
            self.supplierName = supplier.supplierName
          }
        }
      }
    },
    searchStaff () {
      const self = this
      if (!self.formValidate.staffCode) {
        return
      }
      let para = {
        staffCode: self.formValidate.staffCode
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
              self.departmentId = data.data.departmentId
              let departmentName = data.data.departmentName
              self.departmentName = departmentName
              self.staffInfo = departmentName + ' - ' + data.data.staffName
            } else {
              self.$Message.error('职工不存在')
              self.formValidate.staffCode = ''
              self.staffInfo = ''
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
    getEquipmentListData () {
      const self = this
      axios
        .request({
          headers: {
            'Content-Type': 'application/json; charset=UTF-8'
          },
          url: '/base/get-equipment-list',
          isAuth: true,
          method: 'post',
          params: []
        })
        .then(function (res) {
          const data = res.data
          if (data.code === 200) {
            self.equipmentList = data.data
          }
        })
        .catch(function (err) {
          console.log(err)
        })
    },

    equipmentListSelectChange (item) {
      let equipmentList = this.equipmentList
      if (item !== undefined) {
        for (const equipment of equipmentList) {
          if (equipment.pkId === item) {
            this.formValidate.equipmentCode = equipment.equipmentCode
            this.formValidate.equipmentName = equipment.equipmentName
          }
        }
      }
    },

    setCoat (row) {
      const self = this
      self.partList = []
      self.pkId = row.pkId
      self.formValidate.remark = ''
      self.staffName = ''
      self.staffInfo = ''
      self.formValidate.staffCode = ''
      if (row.composeNumber) {
        self.composeOutboundModalshow = true
        self.composeNumber = row.composeNumber
        self.headName = row.headName   
        self.detailist = row.detailList
        let para = {
          composeNumber: row.composeNumber
        }
        axios
          .request({
            headers: {
              'Content-Type': 'application/json; charset=UTF-8'
            },
            url: '/tool/blade-compose-get-by-compose-number',
            isAuth: true,
            method: 'post',
            params: para
          })
          .then(function (res) {
            const data = res.data
            if (data.code === 200) {
              self.headNumber = data.data.headNumber
              self.eachProcessQty = data.data.eachProcessQty
              self.partList = []
              for (let item of data.data.partList) {
                item.partQty = 0
                self.partList.push(item)
              }
            }
          })
          .catch(function (err) {
            console.log(err)
          })
      } else {
        self.outboundModalshow = true
        self.toolNumber = row.toolNumber
        self.toolName = row.toolName
        self.toolMap = row.toolMap
        self.toolQty = row.toolQty
      }
    },

    handleSubmit (name) {
      const self = this
      self.$refs[name].validate(valid => {
        if (valid) {
          self.isDisabled = true
          let para = {
            pkId: self.pkId,
            toolNumber: self.toolNumber,
            toolQty: self.toolQty,
            supplierId: self.formValidate.supplierId,
            supplierName: self.$refs.supplierId.values[0].label,
            remark: self.formValidate.remark
          }
          axios
            .request({
              headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
              },
              url: '/tool/blade-set-coat',
              method: 'post',
              isAuth: true,
              data: qs.stringify(para)
            })
            .then(function (res) {
              self.outboundModalshow = false
              if (res.data.code === 200) {
                self.$Message.success('刀条出库信息已记录!')
                self.getListData()
              } else {
                self.$Notice.error({
                  title: '错误提示',
                  desc: res.data.info
                })
              }
              self.isDisabled = false
            })
            .catch(function (err) {
              console.log(err)
            })
        } else {
          self.$Message.error('请输入正确信息!')
        }
      })
    },

    composeSubmit (name) {
      const self = this
      self.$refs[name].validate(valid => {
        if (valid) {
          let i = 1
          let processQty = 0
          for (let item of self.partList) {
            processQty = processQty + item.partQty
            i++
          }

          let eachProcessQty = self.eachProcessQty
          if (processQty < eachProcessQty && !self.formValidate.remark) {
            self.$Message.error('加工数量小于每次换刀加工数量,请输入说明!')
            return
          }
          self.isDisabled = true
          let para = {
            pkId: self.pkId,
            fullNumber: self.composeNumber,
            supplierId: self.formValidate.supplierId,
            supplierName: self.$refs.supplierId.values[0].label,
            equipmentCode: self.formValidate.equipmentCode,
            staffCode: self.formValidate.staffCode,
            departmentId: self.departmentId,
            departmentName: self.departmentName,
            remark: self.formValidate.remark,
            detailList: JSON.stringify(self.detailist),
            partList: JSON.stringify(self.partList)
          }
          axios
            .request({
              headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
              },
              url: '/tool/blade-set-coat',
              method: 'post',
              isAuth: true,
              data: qs.stringify(para)
            })
            .then(function (res) {
              self.composeOutboundModalshow = false
              if (res.data.code === 200) {
                self.$Message.success('刀条组合出库信息已记录!')
                self.getListData()
              } else {
                self.$Notice.error({
                  title: '错误提示',
                  desc: res.data.info
                })
              }
              self.isDisabled = false
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
      this.outboundModalshow = false
      this.$refs[name].resetFields()
    },

    composeReset (name) {
      this.composeOutboundModalshow = false
      this.$refs[name].resetFields()
    }
  }
}
</script>

<style>
.subCol > ul > li {
  margin: 0 -18px;
  /* list-style: none;*/
  text-align: left;
  padding: 9px;
  padding-left: 18px;
  border-bottom: 1px solid #ccc;
  overflow-x: hidden;
}

.subCol > ul > li:last-child {
  border-bottom: none;
}
</style>
