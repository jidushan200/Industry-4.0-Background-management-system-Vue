<template>
  <div>
    <Card>
      <Row>
        <Form ref="formInline" :model="searchCondition" :label-width="65" inline>
          <Col span="18">
          <FormItem label="图号名称" prop="toolNumber">
            <Select style="width:158px" v-model="searchCondition.toolNumber" ref="toolName" placeholder="请选择图号名称" clearable filterable>
              <Icon type="ios-search" slot="suffix" />
              <Option v-for="item in baseList" :value="item.toolNumber" :key="item.toolNumber">{{ item.toolName }}</Option>
            </Select>
          </FormItem>
          <FormItem>
            <Button type="primary" style="margin-left: -60px" @click="doSearch('formInline')">搜索</Button>
          </FormItem>
          <FormItem style="margin-left: -65px">
            <Button type="dashed" @click="doReset('formInline')">重置</Button>
          </FormItem>
          </Col>
        </Form>
      </Row>

      <Table ref="tablesMain" :data="tableData.rows" :columns="tableColumns" stripe border></Table>
      <div style="margin: 10px;overflow: hidden">
        <div style="float: right;">
          <Page show-total :total="tableData.total" :page-size="searchCondition.rows" :current="searchCondition.page" @on-change="changePage" @on-page-size-change="handlePageSize" show-sizer></Page>
        </div>
      </div>
    </Card>

    <Modal v-model="warehouseModalshow" title="刀条入库" width="700" draggable>
      <Form ref="formValidateOut" :model="formValidate" :rules="ruleValidate" :label-width="100" inline>
        <FormItem label="刀条编号" prop="toolNumber">
          <Input style="width:200px" v-model="toolNumber" readonly />
        </FormItem>
        <FormItem label="刀条名称" prop="toolName">
          <Input style="width:200px" v-model="toolName" readonly />
        </FormItem>
        <FormItem label="刀条图号" prop="toolMap">
          <Input style="width:200px" v-model="toolMap" readonly />
        </FormItem>
        <FormItem label="刀条数量" prop="toolQty">
          <Input style="width:200px" v-model="toolQty" readonly />
        </FormItem>
        <FormItem label="库位" prop="warehouse">
          <Input style="width:200px" v-model="formValidate.warehouse" />
        </FormItem>
        <FormItem label="库管员" prop="staffInfo">
          <Input style="width:200px" v-model="staffInfo" readonly />
        </FormItem>
        <FormItem label="说明" prop="remark">
          <Input style="width:510px" v-model="formValidate.remark" placeholder="输入说明" />
        </FormItem>
      </Form>
      <div slot="footer">
        <Button @click="handleSubmit()" type="primary" :disabled="isDisabled">确定</Button>
        <Button @click="handleReset()" style="margin-left: 8px">取消</Button>
      </div>
    </Modal>
  </div>
</template>

<script>
import axios from "@/libs/api.request";
import { getToken, getBaseUrl } from "@/libs/util";
import { dateFormat, objCopy } from "@/libs/tools.js";
import tool_blade_form_config from "./tool-blade-form-config.js";
export default {
  components: {},
  data() {
    return {
      isDisabled: false,
      baseList: [],
      toolNumber: "",
      toolName: "",
      toolMap: "",
      toolQty: 0,
      staffInfo: "",
      remark: "",
      warehouseModalshow: false,
      searchCondition: {
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
          title: "刀条编码",
          key: "toolNumber",
          minWidth: 150,
          fixed: "left"
        },
        {
          title: "刀条名称",
          key: "toolName",
          minWidth: 280
        },
        {
          title: "刀条图号",
          key: "toolMap",
          minWidth: 180
        },
        {
          title: "刀条数量",
          key: "toolQty",
          minWidth: 100
        },
        {
          title: "申请部门",
          key: "departmentName",
          minWidth: 120
        },
        {
          title: "申请人",
          key: "applierName",
          minWidth: 100
        },
        {
          title: "供应商",
          key: "supplierName",
          minWidth: 280
        },
        {
          title: "送货时间",
          key: "deliveryTime",
          minWidth: 120,
          render: function(h, params) {
            return h(
              "div",
              dateFormat(new Date(params.row.deliveryTime), "yyyy-MM-dd")
            );
          }
        },
        {
          title: "操作",
          key: "action",
          width: 150,
          align: "center",
          fixed: "right",
          render: (h, params) => {
            return h("div", [
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
                      this.warehouseShow(params.row);
                    }
                  }
                },
                "入库"
              )
            ]);
          }
        }
      ],
      formValidate: tool_blade_form_config.formValidate, // form表单字段
      ruleValidate: tool_blade_form_config.ruleValidate // form表单验证规则
    };
  },
  created() {
    const self = this;
    self.getListData();
    self.getBaseListData();
    self.searchCurrentStaff();
  },
  methods: {
    getBaseListData() {
      const self = this;
      let para = {
        typeId: 4
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/tool-base-list",
          isAuth: true,
          params: para,
          method: "post"
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200 && data.data !== null) {
            self.baseList = data.data;
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },
    getListData() {
      const self = this;
      let para = {
        toolNumber: self.searchCondition.toolNumber,
        typeId: 4,
        page: self.searchCondition.page,
        rows: self.searchCondition.rows
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/wait-warehouse-page-list",
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
    searchCurrentStaff() {
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/base/staff-get-current.json",
          method: "post",
          isAuth: true
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            if (data.data !== null && data.data !== undefined) {
              self.departmentId = data.data.departmentId;
              self.departmentName = data.data.departmentName;
              self.staffInfo =
                data.data.departmentName + "-" + data.data.staffName;
            } else {
              self.$Message.error("无法找到与职工号对应的职工");
            }
          } else {
            self.$Notice.error({
              title: "错误提示",
              desc: res.data.info
            });
          }
        })
        .catch(function(err) {
          console.log(err);
        });
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

    warehouseShow(row) {
      const self = this;
      self.warehouseModalshow = true;
      self.handleId = row.pkId;
      self.toolNumber = row.toolNumber;
      self.toolName = row.toolName;
      self.toolMap = row.toolMap;
      self.toolQty = row.toolQty;
      self.supplierId = row.supplierId;
      self.supplierName = row.supplierName;
      self.deliever = row.deliever;
    },
    handleReset() {
      const self = this;
      self.warehouseModalshow = false;
    },
    //form 校验方法
    handleSubmit() {
      this.isDisabled = true;
      const self = this;
      let para = {
        handleId: self.handleId,
        toolNumber: self.toolNumber,
        toolName: self.toolName,
        toolMap: self.toolMap,
        toolQty: self.toolQty,
        supplierId: self.supplierId,
        supplierName: self.supplierName,
        deliever: self.deliever,
        warehouse: self.formValidate.warehouse,
        warehouseType: 1,
        departmentId: self.departmentId,
        departmentName: self.departmentName,
        remark: self.formValidate.remark
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/blade-warehouse",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          self.warehouseModalshow = false;
          if (res.data.code === 200) {
            self.$Message.success("刀条入库成功!");
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
    }
  }
};
</script>

<style>
</style>
