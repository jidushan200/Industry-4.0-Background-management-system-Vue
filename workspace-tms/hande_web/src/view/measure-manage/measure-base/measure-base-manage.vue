<template>
  <div>
    <Card>
      <Row>
        <Form ref="formInline" :model="searchCondition" :label-width="65" inline>
          <Col span="16">
          <FormItem label="量具编码" prop="measureNumber">
            <Input type="text" v-model="searchCondition.measureNumber" placeholder="请输入量具编码"></Input>
          </FormItem>
          <FormItem>
            <Button type="primary" style="margin-left: -60px;" @click="doSearch('formInline')">搜索</Button>
          </FormItem>
          <FormItem style="margin-left: -65px;">
            <Button type="warning" @click="synchro()">
              <Icon type="ios-download-outline"></Icon>同步
            </Button>
          </FormItem>
          <FormItem style="margin-left: -65px;">
            <Button style type="dashed" @click="doReset('formInline')">重置</Button>
          </FormItem>
          </Col>
          <Col span="8">
          <FormItem style="width:100%;text-align:right">
            <Upload :show-upload-list="false" :action="uploadAction" :on-success="handleSuccess" :on-error="handleError" style="display: inline-block;margin-right: 5px;">
              <Button icon="ios-cloud-upload-outline">导入</Button>
            </Upload>
            <Button type="success" style="margin-right: 5px;" @click="addMeasureBase">
              <Icon type="md-add"></Icon>添加
            </Button>
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
          <Page show-total :total="tableData.total" :page-size="searchCondition.rows" :current="searchCondition.page" @on-change="changePage" @on-page-size-change="handlePageSize" show-sizer></Page>
        </div>
      </div>
    </Card>

    <Modal v-model="toolBaseDetailModalshow" :title="isModify?'修改量具':'新增量具'" width="650" draggable>
      <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="100" inline>
        <FormItem label="量具编码" prop="measureNumber">
          <Input style="width:170px" v-model="formValidate.measureNumber" placeholder="输入量具编码" />
        </FormItem>
        <FormItem label="量具名称" prop="measureName">
          <Input style="width:170px" v-model="formValidate.measureName" placeholder="输入量具名称" />
        </FormItem>
        <FormItem label="规格型号" prop="model">
          <Input style="width:170px" v-model="formValidate.model" placeholder="输入规格型号" />
        </FormItem>

      </Form>
      <div slot="footer">
        <Button @click="handleSubmit('formValidate')" type="primary" :disabled="isDisabled">保存</Button>
        <Button @click="handleReset('formValidate')" style="margin-left: 8px">取消</Button>
      </div>
    </Modal>

    <Modal v-model="deleteModalShow" width="250" :title="isBatchDetele?'批量删除':'删除'" @on-ok="deleteModalSureBtn" @on-cancel="deleteModalCancelBtn">
      <p>确定删除所选项？</p>
    </Modal>
  </div>
</template>

<script>
import axios from "@/libs/api.request";
import measure_base_manage_form_config from "./measure-base-manage-form-config.js";
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
      toolBaseDetailModalshow: false, //控制明细modal显示
      isModify: false, //是否是修改明细操作
      uploadAction:
        getBaseUrl() + "/measure/measure-base-import.json?token=" + getToken(),
      supplierList: [],
      searchCondition: {
        measureNumber: "",
        page: 1,
        rows: 10
      },
      tableData: {
        total: 0, // 总页数
        rows: [] // 每页条数
      },
      tableColumns: [
        {
          title: "量具编码",
          key: "measureNumber",
          minWidth: 200
        },
        {
          title: "量具名称",
          key: "measureName",
          minWidth: 200
        },
        {
          title: "规格型号",
          key: "model",
          minWidth: 250
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
      formValidate: measure_base_manage_form_config.formValidate, //user form表单字段
      ruleValidate: measure_base_manage_form_config.ruleValidate //user form表单验证规则
    };
  },
  created() {
    const self = this;
    self.getListData();
    // self.getSupplierListData();
  },
  methods: {
    getListData() {
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/measure/measure-base-page-list",
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
          url: "/measure/measure-base-synchro",
          method: "post",
          isAuth: true,
          params: self.searchCondition
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            if (data.data === 1) {
              self.$Message.success("量具同步成功!");
              self.getListData();
            } else {
              self.$Message.error("量具没查到!");
            }
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },

    addNewToolBase() {
      this.isDisabled = true;
      let para = {
        measureNumber: this.formValidate.measureNumber,
        measureName: this.formValidate.measureName,
        model: this.formValidate.model
      };
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/measure/measure-base-add",
          isAuth: true,
          method: "post",
          params: para
        })
        .then(function(res) {
          if (res.data.code === 200) {
            self.toolBaseDetailModalshow = false;
            self.$Message.success("量具新建成功!");
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
    editToolBase() {
      this.isDisabled = true;
      let para = {
        pkId: this.formValidate.pkId,
        measureNumber: this.formValidate.measureNumber,
        measureName: this.formValidate.measureName,
        model: this.formValidate.model
      };
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/measure/measure-base-update",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          if (res.data.code === 200) {
            self.toolBaseDetailModalshow = false;
            self.$Message.success("量具信息修改成功!");
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
    deleteRowDate(pkId,measureNumber) {
      let para = {
        pkId: pkId,
        measureNumber:measureNumber
      };
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/measure/measure-base-delete-by-id",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          self.deleteModalShow = false;
          if (res.data.code === 200) {
            self.$Message.success("量具信息删除成功!");
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
        this.deleteRowDate(this.deleteRowData.pkId,this.deleteRowData.measureNumber);
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
      self.changePage(1);
    },
    doReset(name) {
      const self = this;
      self.$refs[name].resetFields();
      self.changePage(1);
    },
    show(index) {
      this.toolBaseDetailModalshow = true;
      this.isModify = true;
      this.formValidate = objCopy(this.tableData.rows[index], {});
      //this.formValidate.price = parseInt(this.formValidate.price);
    },
    remove(index) {
      this.deleteRowData = this.tableData.rows[index];
      this.deleteModalShow = true;
      this.isBatchDetele = false;
    },
    addMeasureBase() {
      this.toolBaseDetailModalshow = true;
      this.isModify = false;
      this.formValidate = {};
      this.$refs["formValidate"].resetFields();
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
    exportData() {
      const self = this;
      window.location.href =
        getBaseUrl() + "/measure/measure-base-export?token=" + getToken();
    },
    handleSuccess() {
      const self = this;
      self.getListData();
      self.$Message.success("文件导入成功!");
    },
    handleError() {
      const self = this;
      self.$Message.success("文件导入失败!");
    },
    //form 校验方法
    handleSubmit(name) {
      this.$refs[name].validate(valid => {
        if (valid) {
          if (this.isModify) {
            this.editToolBase();
          } else {
            this.addNewToolBase();
          }
        } else {
          this.$Message.error("请输入正确信息!");
        }
      });
    },
    handleReset(name) {
      this.toolBaseDetailModalshow = false;
      this.$refs[name].resetFields();
    }
  }
};
</script>

<style>
</style>
