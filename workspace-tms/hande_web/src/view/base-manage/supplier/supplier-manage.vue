<template>
  <div>
    <Card>
      <Row>
        <Form
          ref="formInline"
          :model="searchCondition"
          :label-width="80"
          class="modal-class"
          inline
        >
          <Col span="16">
            <FormItem label="供应商编码" prop="supplierCode">
              <Input
                style="width:170px"
                type="text"
                v-model="searchCondition.supplierCode"
                placeholder="请输入供应商编码"
              ></Input>
            </FormItem>
            <FormItem label="供应商名称" prop="supplierName">
              <Input
                style="width:170px"
                type="text"
                v-model="searchCondition.supplierName"
                placeholder="请输入供应商名称"
              ></Input>
            </FormItem>
            <FormItem>
              <Button style="margin-left: -75px;" type="primary" @click="doSearch('formInline')">搜索</Button>
            </FormItem>
            <FormItem>
              <Button type="warning" style="margin-left: -95px;" @click="synchro()">
                <Icon type="ios-download-outline"></Icon>同步
              </Button>
            </FormItem>
            <FormItem style="margin-left: -100px;">
              <Button type="dashed" @click="doReset('formInline')">重置</Button>
            </FormItem>
          </Col>
          <Col span="8">
            <FormItem style="width: 100%;text-align: right;">
              <Upload
                :show-upload-list="false"
                :action="uploadAction"
                :on-success="handleSuccess"
                :on-error="handleError"
                accept=".xls, .xlsx"
                style="display: inline-block;margin-right: 5px;"
              >
                <Button icon="ios-cloud-upload-outline">导入</Button>
              </Upload>
              <Button type="success" style="margin-right: 5px;" @click="addSupplier">
                <Icon type="md-add"></Icon>添加
              </Button>
              <Button type="warning" @click="exportData()">
                <Icon type="ios-download-outline"></Icon>导出
              </Button>
            </FormItem>
          </Col>
        </Form>
      </Row>

      <Table
        ref="tablesMain"
        show-total
        :data="tableData.rows"
        :columns="tableColumns"
        stripe
        border
      ></Table>
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
      v-model="supplierDetailModalshow"
      :title="isModify?'修改供应商':'新增供应商'"
      width="600"
      draggable
    >
      <Form
        ref="formValidate"
        :model="formValidate"
        :rules="ruleValidate"
        :label-width="100"
        inline
      >
        <FormItem label="供应商编码" prop="supplierCode">
          <Input style="width:170px" v-model="formValidate.supplierCode" placeholder="输入供应商编码" />
        </FormItem>
        <FormItem label="供应商名称" prop="supplierName">
          <Input style="width:170px" v-model="formValidate.supplierName" placeholder="输入供应商名称" />
        </FormItem>
        <FormItem label="新刀" prop="isNewTool">
          <RadioGroup style="width: 170px" v-model="formValidate.isNewTool">
            <Radio :label="0">否</Radio>
            <Radio :label="1">是</Radio>
          </RadioGroup>
        </FormItem>
        <FormItem label="刃磨" prop="isRepair">
          <RadioGroup style="width: 170px" v-model="formValidate.isRepair">
            <Radio :label="0">否</Radio>
            <Radio :label="1">是</Radio>
          </RadioGroup>
        </FormItem>
        <FormItem label="涂层" prop="isCoat">
          <RadioGroup style="width: 170px" v-model="formValidate.isCoat">
            <Radio :label="0">否</Radio>
            <Radio :label="1">是</Radio>
          </RadioGroup>
        </FormItem>
        <FormItem label="量具" prop="isMeasure">
          <RadioGroup style="width: 170px" v-model="formValidate.isMeasure">
            <Radio :label="0">否</Radio>
            <Radio :label="1">是</Radio>
          </RadioGroup>
        </FormItem>
        <FormItem label="夹具" prop="isFixture">
          <RadioGroup style="width: 170px" v-model="formValidate.isFixture">
            <Radio :label="0">否</Radio>
            <Radio :label="1">是</Radio>
          </RadioGroup>
        </FormItem>
        <FormItem label="模具" prop="isMould">
          <RadioGroup style="width: 170px" v-model="formValidate.isMould">
            <Radio :label="0">否</Radio>
            <Radio :label="1">是</Radio>
          </RadioGroup>
        </FormItem>
        <FormItem label="供应商评价" prop="evaluation">
          <Input style="width:430px" v-model="formValidate.evaluation" placeholder="输入供应商评价" />
        </FormItem>
      </Form>
      <div slot="footer">
        <Button @click="handleSubmit('formValidate')" type="primary" :disabled="isDisabled">保存</Button>
        <Button @click="handleReset('formValidate')" style="margin-left: 8px">取消</Button>
      </div>
    </Modal>

    <Modal
      v-model="deleteModalShow"
      width="250"
      :title="isBatchDetele?'批量删除':'删除'"
      @on-ok="deleteModalSureBtn"
      @on-cancel="deleteModalCancelBtn"
    >
      <p>确定删除所选项？</p>
    </Modal>
  </div>
</template>

<script>
import axios from "@/libs/api.request";
import supplier_manage_form_config from "./supplier-manage-form-config.js";
import { dateFormat, objCopy } from "@/libs/tools.js";
import { getToken, getBaseUrl } from "@/libs/util";

export default {
  data() {
    return {
      isDisabled: false,
      deleteModalShow: false, //控制删除modal提示 显示
      deleteRowData: {}, //行删除行数据
      isBatchDetele: false, //批量删除标志
      multiselectRowData: [], //复选列数据
      supplierDetailModalshow: false, //控制明细modal显示
      isModify: false, //是否是修改明细操作
      uploadAction:
        getBaseUrl() + "/base/supplier-import.json?token=" + getToken(),
      toolDisabled: "",
      coatDisabled: "",
      searchCondition: {
        supplierName: "",
        supplierCode: "",
        page: 1,
        rows: 10
      },
      tableData: {
        total: 0, // 总页数
        rows: [] // 每页条数
      },
      tableColumns: [
        {
          title: "供应商编码",
          key: "supplierCode",
          sortable: true,
          minWidth: 200
        },
        {
          title: "供应商名称",
          key: "supplierName",
          minWidth: 200
        },
        {
          title: "新刀",
          key: "isNewTool",
          align: "center",
          render: function(h, params) {
            return h("div", params.row.isNewTool == 1 ? "√" : "×");
          },
          minWidth: 60
        },
        {
          title: "刃磨",
          key: "isRepair",
          align: "center",
          render: function(h, params) {
            return h("div", params.row.isRepair == 1 ? "√" : "×");
          },
          minWidth: 60
        },
        {
          title: "涂层",
          key: "isCoat",
          align: "center",
          render: function(h, params) {
            return h("div", params.row.isCoat == 1 ? "√" : "×");
          },
          minWidth: 60
        },
        {
          title: "量具",
          key: "isMeasure",
          align: "center",
          render: function(h, params) {
            return h("div", params.row.isMeasure == 1 ? "√" : "×");
          },
          minWidth: 60
        },
        {
          title: "夹具",
          key: "isFixture",
          align: "center",
          render: function(h, params) {
            return h("div", params.row.isFixture == 1 ? "√" : "×");
          },
          minWidth: 60
        },
        {
          title: "模具",
          key: "isMould",
          align: "center",
          render: function(h, params) {
            return h("div", params.row.isMould == 1 ? "√" : "×");
          },
          minWidth: 60
        },
        {
          title: "创建人",
          key: "createUserName",
          minWidth: 100
        },
        {
          title: "创建时间",
          key: "createTime",
          minWidth: 140,
          render: function(h, params) {
            return h(
              "div",
              dateFormat(new Date(params.row.createTime), "yyyy-MM-dd HH:mm")
            );
          }
        },
        {
          title: "最后修改人",
          key: "updateUserName",
          minWidth: 100
        },
        {
          title: "最后修改时间",
          key: "updateTime",
           minWidth: 140,
          render: function(h, params) {
            return h(
              "div",
              dateFormat(new Date(params.row.updateTime), "yyyy-MM-dd HH:mm")
            );
          }
        },
        {
          title: "操作",
          key: "action",
          width: 160,
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
                    size: "small"
                  },
                  style: {
                    marginRight: "5px"
                  },
                  on: {
                    click: () => {
                      this.show(params.index);
                    }
                  }
                },
                "编辑"
              ),
              h(
                "Button",
                {
                  props: {
                    type: "error",
                    size: "small"
                  },
                  on: {
                    click: () => {
                      this.remove(params.index);
                    }
                  }
                },
                "删除"
              )
            ]);
          }
        }
      ],
      formValidate: supplier_manage_form_config.formValidate, //user form表单字段
      ruleValidate: supplier_manage_form_config.ruleValidate //user form表单验证规则
    };
  },
  created() {
    const self = this;
    self.getListData();
  },
  methods: {
    getListData() {
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/base/supplier-page-list",
          method: "post",
          isAuth: true,
          params: self.searchCondition
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
    synchro() {
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/base/supplier-synchro",
          method: "post",
          isAuth: true,
          params: self.searchCondition
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            if (data.data === 1) {
              self.$Message.success("供应商同步成功!");
              self.getListData();
            } else {
              self.$Message.error("供应商没查到!");
            }
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },

    addNewsupplier() {
      const self = this;
      self.isDisabled = true;
      let para = {
        supplierName: self.formValidate.supplierName,
        supplierCode: self.formValidate.supplierCode,
        evaluation: self.formValidate.evaluation,
        isNewTool: self.formValidate.isNewTool,
        isRepair: self.formValidate.isRepair,
        isCoat: self.formValidate.isCoat,
        isMeasure: self.formValidate.isMeasure,
        isFixture: self.formValidate.isFixture,
        isMould: self.formValidate.isMould
      };

      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/base/supplier-add",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          if (res.data.code === 200) {
            self.supplierDetailModalshow = false;
            self.$Message.success("供应商新建成功!");
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
    editsupplier() {
      const self = this;
      self.isDisabled = true;
      let para = {
        pkId: self.formValidate.pkId,
        supplierName: self.formValidate.supplierName,
        supplierCode: self.formValidate.supplierCode,
        evaluation: self.formValidate.evaluation,
        isNewTool: self.formValidate.isNewTool,
        isRepair: self.formValidate.isRepair,
        isCoat: self.formValidate.isCoat,
        isMeasure: self.formValidate.isMeasure,
        isFixture: self.formValidate.isFixture,
        isMould: self.formValidate.isMould
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/base/supplier-update",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          if (res.data.code === 200) {
            self.supplierDetailModalshow = false;
            self.$Message.success("供应商信息修改成功!");
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
    deleteRowDate(pkId) {
      let para = {
        pkId: pkId
      };
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/base/supplier-delete-by-id",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          self.deleteModalShow = false;
          if (res.data.code === 200) {
            self.$Message.success("供应商信息删除成功!");
            self.getListData();
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },
    deleteModalSureBtn() {
      if (this.isBatchDetele) {
        let ids = [];
        for (const item of this.multiselectRowData) {
          ids.push(item.pkId);
        }
      } else {
        this.deleteRowDate(this.deleteRowData.pkId);
      }
    },
    //删除modal 取消方法
    deleteModalCancelBtn() {
      if (this.isBatchDetele) {
        this.multiselectRowData = [];
      } else {
        this.deleteRowData = {};
      }
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
      this.formValidate = {};
      self.changePage(1);
    },
    doReset(name) {
      const self = this;
      self.$refs[name].resetFields();
      self.changePage(1);
    },
    show(index) {
      this.supplierDetailModalshow = true;
      this.isModify = true;
      this.formValidate = objCopy(this.tableData.rows[index], {});
      if (
        this.formValidate.isNewTool === null &&
        this.formValidate.isNewTool === undefined
      ) {
        this.formValidate.isNewTool = 0;
      }
      if (
        this.formValidate.isRepair === null &&
        this.formValidate.isRepair === undefined
      ) {
        this.formValidate.isRepair = 0;
      }
      if (
        this.formValidate.isCoat === null &&
        this.formValidate.isCoat === undefined
      ) {
        this.formValidate.isCoat = 0;
      }
      if (
        this.formValidate.isMould === null &&
        this.formValidate.isMould === undefined
      ) {
        this.formValidate.isMould = 0;
      }
    },
    remove(index) {
      this.deleteRowData = this.tableData.rows[index];
      this.deleteModalShow = true;
      this.isBatchDetele = false;
    },
    addSupplier() {
      this.supplierDetailModalshow = true;
      this.isModify = false;
      this.toolDisabled = false;
      this.formValidate = {};
      this.formValidate.isNewTool = 0;
      this.formValidate.isRepair = 0;
      this.formValidate.isCoat = 0;
      this.formValidate.isMeasure = 0;
      this.formValidate.isFixture = 0;
      this.formValidate.isMould = 0;
      this.$refs["formValidate"].resetFields();
    },
    exportData() {
      const self = this;
      window.location.href =
        getBaseUrl() + "/base/supplier-export?token=" + getToken();
    },
    handleSuccess() {
      const self = this;
      self.getListData();
      self.$Message.success("文件导入成功!");
    },
    handleError() {
      const self = this;
      self.getListData();
      self.$Message.success("文件导入失败!");
    },
    //form 校验方法
    handleSubmit(name) {
      this.$refs[name].validate(valid => {
        if (valid) {
          if (this.isModify) {
            this.editsupplier();
          } else {
            this.addNewsupplier();
          }
        } else {
          this.$Message.error("请输入正确信息!");
        }
      });
    },
    handleReset(name) {
      this.supplierDetailModalshow = false;
      this.$refs[name].resetFields();
    }
  }
};
</script>

<style>
</style>
