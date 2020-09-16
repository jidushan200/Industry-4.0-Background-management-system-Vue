<style lang="less">
.fsize {
  font-size: 18px !important;
}
</style>
<template>
  <div>
    <Card>
      <Row>
        <Form ref="formInline" :model="searchCondition" :label-width="65" inline>
          <Col span="20">
          <FormItem prop="fullNumber" label="物料条码">
            <Input type="text" v-model="searchCondition.fullNumber" placeholder="请输入物料条码"></Input>
          </FormItem>
          <!--
          <FormItem prop="warehouseCode" label="入库编码">
            <Input type="text" v-model="searchCondition.warehouseCode" placeholder="请输入入库编码"></Input>
          </FormItem>
          -->
          <FormItem prop="toolNumber" label="物料编码">
            <Input type="text" v-model="searchCondition.toolNumber" placeholder="请输入物料编码"></Input>
          </FormItem>
          <FormItem>
            <Button type="primary" style="margin-left: -60px;" @click="doSearch('formInline')">搜索</Button>
          </FormItem>
          <FormItem style="margin-left: -65px;">
            <Button style type="dashed" @click="doReset('formInline')">重置</Button>
          </FormItem>
          </Col>
          <!-- <Col span="4">
          <FormItem style="width:100%;text-align:right">
            <Button type="warning" style="margin-left: 37px;" @click="exportData()">
              <Icon type="ios-download-outline"></Icon>导出
            </Button>
          </FormItem>
          </Col>-->
        </Form>
      </Row>

      <Table ref="tablesMain" :data="tableData.rows" :columns="tableColumns" stripe border></Table>
      <div style="margin: 10px;overflow: hidden">
        <div style="float: right;">
          <Page show-total :total="tableData.total" :page-size="searchCondition.rows" :current="searchCondition.page" @on-change="changePage" @on-page-size-change="handlePageSize" show-sizer></Page>
        </div>
      </div>
    </Card>

    <Modal v-model="toolRepairDetailModalshow" title="刃磨信息录入" width="400" draggable style="font-size:20px">
      <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="100" inline>
        <Row>
          <FormItem label="本次刃磨量" prop="repairMeasure">
            <InputNumber :max="99" :min="0.01" :step="0.01" style="width:200px" v-model="formValidate.repairMeasure" placeholder="输入本次刃磨量" />
          </FormItem>
        </Row>
        <Row>
          <FormItem label="刃磨供应商" prop="supplierId">
            <Select style="width:200px" v-model="formValidate.supplierId" ref="supplier" @on-change="supplierListSelectChange">
                <Option v-for="item in supplierList" :value="item.pkId" :key="item.pkId">{{ item.supplierName }}</Option>
            </Select>        
          </FormItem>
        </Row>
        <Row>
          <FormItem label="备注说明" prop="remark">
            <Select v-model="formValidate.remark" style="width:200px" @on-change="handleOther">
                <Option v-for="item in remarkType" :value="item.value" :key="item.value">{{ item.name }}</Option>
            </Select>
          </FormItem>
        </Row>
        <Row>
          <FormItem label="其他说明" prop="remarkOther" v-if="isOther">
            <Input style="width:200px" v-model="formValidate.remarkOther" placeholder="请输入其他说明"/>
          </FormItem>
        </Row>
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
import tool_repair_manage_form_config from "./tool-repair-form-config.js";
import { dateFormat, objCopy } from "@/libs/tools.js";
import toolType from "../tool-coat-manage/tool-type.js";
import remarkType from "./remark-type.js";
import { debuglog } from "util";

export default {
  data() {
    return {
      isOther: false,
      remarkType: remarkType,
      isDisabled: false,
      toolRepairDetailModalshow: false, //控制明细modal显示
      supplierList: [],
      executor: "",
      searchCondition: {
        warehouseCode: "",
        fullNumber: "",
        toolNumber: "",
        page: 1,
        rows: 10
      },
      tableData: {
        total: 0, // 总页数
        rows: [] // 每页条数
      },
      tableColumns: [
        {
          title: "物料编码",
          fixed: "left",
          minWidth: 200,
          key: "toolNumber"
        },
        {
          title: "物料名称",
          fixed: "left",
          minWidth: 200,
          key: "toolName"
        },
        {
          title: "物料图号",
          minWidth: 200,
          key: "toolMap"
        },
        {
          title: "物料条码",
          width: 250,
          key: "fullNumber"
        },
        {
          title: "最大刃磨量",
          minWidth: 120,
          key: "grindingMax"
        },
        {
          title: "刃磨次数",
          minWidth: 120,
          key: "repairTimes"
        },
        {
          title: "累计刃磨量",
          minWidth: 120,
          key: "repairAmount"
        },
        {
          title: "送磨时间",
          minWidth: 160,
          key: "grinderTime",
          render: function(h, params) {
            return h(
              "div",
              dateFormat(
                new Date(params.row.grinderTime),
                "yyyy-MM-dd HH:mm:ss"
              )
            );
          }
        },
        {
          title: "送磨人",
          minWidth: 120,
          key: "grinderName"
        },
        {
          title: "送磨人部门",
          minWidth: 120,
          key: "staffDepartmentName"
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
                    disabled: params.row.toolState != 3,
                    size: "small"
                  },
                  style: {
                    marginRight: "5px"
                  },
                  on: {
                    click: () => {
                      this.addToolRepair(params.row);
                    }
                  }
                },
                "刃磨"
              )
            ]);
          }
        }
      ],
      formValidate: tool_repair_manage_form_config.formValidate, //user form表单字段
      ruleValidate: tool_repair_manage_form_config.ruleValidate //user form表单验证规则
    };
  },
  created() {
    const self = this;
    self.getListData();
    self.getStaffListData();
    self.getSupplierListData();
  },
  methods: {
    getListData() {
      const self = this;
      let para = {
        warehouseCode: self.searchCondition.warehouseCode,
        fullNumber: self.searchCondition.fullNumber,
        toolNumber: self.searchCondition.toolNumber,
        page: self.searchCondition.page,
        rows: self.searchCondition.rows,
        toolState: 3
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/tool-wait-repair-page-list",
          method: "post",
          isAuth: true,
          params: para
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
    getStaffListData() {
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/base/staff-get-current",
          isAuth: true,
          method: "post"
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            self.executor = data.data.staffName;
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },
    getSupplierListData() {
      const self = this;
      let para = {
        isRepair: 1
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/base/supplier-list",
          isAuth: true,
          method: "post",
          params: para
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            self.supplierList = data.data;
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },
    addNewtoolRepair() {
      this.isDisabled = true;
      let remark = "";
      if (this.formValidate.remark !== 0) {
        remark = this.formValidate.remark;
      } else {
        remark = this.formValidate.remarkOther;
      }
      let para = {
        toolId: this.formValidate.pkId,
        executor: this.executor,
        supplierId: this.formValidate.supplierId,
        supplierName: this.formValidate.supplierName,
        toolNumber: this.formValidate.toolNumber,
        fullNumber: this.formValidate.fullNumber,
        repairMeasure: this.formValidate.repairMeasure,
        remark: remark
      };
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/tool-repair",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          self.toolRepairDetailModalshow = false;
          if (res.data.code === 200) {
            self.$Message.success("刀具刃磨信息已记录!");
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
      self.getListData();
    },
    addToolRepair(row) {
      const self = this;
      self.isOther = false;
      self.toolRepairDetailModalshow = true;
      self.formValidate = objCopy(row, {});
      self.formValidate.repairMeasure = 0.01;
    },
    exportData() {
      const self = this;
      self.$refs.tablesMain.exportCsv({
        filename: "待磨信息列表",
        original: false
      });
    },
    supplierListSelectChange(item) {
      let supplierList = this.supplierList;
      if (item !== undefined) {
        for (const supplier of supplierList) {
          if (supplier.pkId === item) {
            this.formValidate.supplierName = supplier.supplierName;
          }
        }
      }
    },
    handleOther(val) {
      const self = this;
      if (val === 0) {
        self.isOther = true;
      } else {
        self.isOther = false;
      }
    },
    //form 校验方法
    handleSubmit(name) {
      const self = this;
      self.$refs[name].validate(valid => {
        if (valid) {
          self.addNewtoolRepair();
        } else {
          this.$Message.error("请输入正确信息!");
        }
      });
    },
    handleReset(name) {
      const self = this;
      self.toolRepairDetailModalshow = false;
      self.$refs[name].resetFields();
    }
  }
};
</script>

<style>
</style>
