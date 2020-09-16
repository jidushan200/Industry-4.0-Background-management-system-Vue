<template>
  <div>

    <Card>
      <Row>
        <Form ref="formInline" :model="searchCondition" :label-width="65" inline>
          <Col span="20">
          <FormItem prop="fullNumber" label="刀具码">
            <Input type="text" v-model="searchCondition.fullNumber" placeholder="请输入刀具码">
            </Input>
          </FormItem>
          <FormItem prop="toolName" label="刀具名称">
            <Input type="text" v-model="searchCondition.toolName" placeholder="请输入刀具名称">
            </Input>
          </FormItem>
          <FormItem prop="toolType" label="刀具类型">
            <Select style="width:158px" v-model="searchCondition.toolType" ref="toolType" placeholder="请选择刀具类型">
              <Option v-for="item in toolType" :value="item.value" :key="item.value">{{ item.name }}</Option>
            </Select>
          </FormItem>
          <FormItem>
            <Button type="primary" style="margin-left: -60px;" @click="doSearch('formInline')">搜索</Button>
          </FormItem>
          <FormItem style="margin-left: -65px;">
            <Button style="" type="dashed" @click="doReset('formInline')">重置</Button>
          </FormItem>
          </Col>
          <Col span="4">
          <FormItem style="width:100%;text-align:right">
            <Button type="warning" style="margin-left: 37px;" @click="exportData()">
              <Icon type="ios-download-outline"></Icon>导出
            </Button>
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

    <Modal v-model="toolCoatDetailModalshow" title="记录涂层信息" width="600" draggable>
      <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="100" inline>
        <FormItem label="涂层供应商" prop="supplierId">
          <Select style="width:170px" v-model="formValidate.supplierId" ref="supplier" @on-change="supplierListSelectChange">
            <Option v-for="item in supplierList" :value="item.pkId" :key="item.pkId">{{ item.supplierName }}</Option>
          </Select>
        </FormItem>
        <FormItem label="送货人" prop="deliever">
          <Input style="width:170px" v-model="formValidate.deliever" placeholder="输入送货人" />
        </FormItem>
        <FormItem label="备注" prop="remark">
          <Input style="width:450px" v-model="formValidate.remark" placeholder="输入备注" />
        </FormItem>
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
import tool_coat_manage_form_config from "./tool-coat-manage-form-config.js";
import { dateFormat, objCopy } from "@/libs/tools.js";
import toolType from "./tool-type.js";
import toolSelect from "./tool-select.js";
export default {
  data() {
    return {
      isDisabled: false,
      deleteRowData: {}, //行删除行数据
      isBatchDetele: false, //批量删除标志
      multiselectRowData: [], //复选列数据
      toolCoatDetailModalshow: false, //控制明细modal显示
      isModify: false, //是否是修改明细操作
      toolType: toolType,
      toolSelect: toolSelect,
      supplierList: [],
      searchCondition: {
        fullNumber: "",
        toolName: "",
        toolType: "",
        toolState: 5,
        page: 1,
        rows: 10
      },
      tableData: {
        total: 0, // 总页数
        rows: [] // 每页条数
      },
      tableColumns: [
        {
          title: "刀具码",
          key: "fullNumber"
        },
        {
          title: "物料编码",
          key: "toolNumber"
        },
        {
          title: "刀具名称",
          key: "toolName"
        },
        {
          title: "最大涂层数",
          key: "coatMax"
        },
        {
          title: "涂层次数",
          key: "coatTimes"
        },
        {
          title: "操作",
          key: "action",
          width: 200,
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
                    type: "success",
                    disabled: params.row.toolState != 5,
                    size: "small"
                  },
                  style: {
                    marginRight: "5px"
                  },
                  on: {
                    click: () => {
                      this.addToolCoat(params.index);
                    }
                  }
                },
                "回涂确认"
              )
            ]);
          }
        }
      ],
      formValidate: tool_coat_manage_form_config.formValidate, //user form表单字段
      ruleValidate: tool_coat_manage_form_config.ruleValidate //user form表单验证规则
    };
  },
  created() {
    const self = this;
    self.getListData();
    self.getSupplierListData();
  },
  methods: {
    getListData() {
      const self = this;
      let para = {
        fullNumber: self.searchCondition.fullNumber,
        page: self.searchCondition.page,
        rows: self.searchCondition.rows,
        toolState: 5
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/tool-page-list",
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
    getSupplierListData() {
      const self = this;
      let para = {
        isCoat: 1
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
    addNewToolCoat() {
      this.formValidate.unusualTime = dateFormat(
        new Date(this.formValidate.unusualTime),
        "yyyy-MM-dd HH:mm:ss"
      );
      this.isDisabled = true;
      let para = {
        toolId: this.formValidate.pkId,
        fullNumber: this.formValidate.fullNumber,
        supplierId: this.formValidate.supplierId,
        coatSupplier: this.formValidate.coatSupplier,
        deliever: this.formValidate.deliever,
        remark: this.formValidate.remark
      };
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/tool-coat-add",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          self.toolCoatDetailModalshow = false;
          if (res.data.code === 200) {
            self.$Message.success("刀具回涂已确认!");
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
    addToolCoat(index) {
      this.toolCoatDetailModalshow = true;
      this.formValidate = objCopy(this.tableData.rows[index], {});
      this.formValidate.supplierId = "";
    },
    exportData() {
      const self = this;
      self.$refs.tablesMain.exportCsv({
        filename: "待涂层刀具列表",
        original: false
      });
    },
    handleSuccess() {
      const self = this;
      self.$Message.success("文件导入成功!");
    },
    handleError() {
      const self = this;
      self.$Message.success("文件导入失败!");
    },
    supplierListSelectChange(item) {
      let supplierList = this.supplierList;
      if (item !== undefined) {
        for (const supplier of supplierList) {
          if (supplier.pkId === item) {
            this.formValidate.coatSupplier = supplier.supplierName;
          }
        }
      }
    },
    //form 校验方法
    handleSubmit(name) {
      this.$refs[name].validate(valid => {
        if (valid) {
          this.addNewToolCoat();
        } else {
          this.$Message.error("请输入正确信息!");
        }
      });
    },
    handleReset(name) {
      this.toolCoatDetailModalshow = false;
      this.$refs[name].resetFields();
    }
  }
};
</script>

<style>
</style>
