<template>
  <div>
    <Card>
      <Row>
        <Form ref="formInline" :model="searchCondition" :label-width="75" inline>
          <Col span="16">
          <FormItem label="模具坯编码" prop="embryoCode">
            <Input type="text" v-model="searchCondition.embryoCode" placeholder="请输入模具坯编码"></Input>
          </FormItem>
          <FormItem label="模具坯名称" prop="embryoName">
            <Input type="text" v-model="searchCondition.embryoName" placeholder="请输入模具坯名称"></Input>
          </FormItem>
          <FormItem>
            <Button type="primary" style="margin-left: -75px;" @click="doSearch('formInline')">搜索</Button>
          </FormItem>
          <FormItem style="margin-left: -95px;">
            <Button type="warning" @click="synchro()">
              <Icon type="ios-download-outline"></Icon>同步
            </Button>
          </FormItem>
          <FormItem style="margin-left: -75px;">
            <Button style type="dashed" @click="doReset('formInline')">重置</Button>
          </FormItem>
          </Col>
          <Col span="8">
          <FormItem style="width: 100%;text-align: right;">
            <Upload :show-upload-list="false" :action="uploadAction" :on-success="handleSuccess" :on-error="handleError" style="display: inline-block;margin-right: 5px;">
              <Button icon="ios-cloud-upload-outline">导入</Button>
            </Upload>
            <Button type="success" style="margin-right: 5px;" @click="addEmbryo">
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

    <Modal v-model="mouldEmbryoDetailModalshow" :title="isModify?'修改模具坯':'新增模具坯'" width="600" draggable inline>
      <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="100" inline>
        <FormItem label="模具坯编码" prop="embryoCode">
          <Input style="width:170px" v-model="formValidate.embryoCode" placeholder="输入模具坯编码" />
        </FormItem>
        <FormItem label="模具坯名称" prop="embryoName">
          <Input style="width:170px" v-model="formValidate.embryoName" placeholder="输入模具坯名称" />
        </FormItem>
      </Form>
      <div slot="footer">
        <Button @click="handleSubmit('formValidate')" type="primary" :disabled="isDisabled">保存</Button>
        <Button @click="handleReset('formValidate')" style="margin-left: 8px">取消</Button>
      </div>
    </Modal>

    <Modal v-model="deleteModalShow" width="250" :title="isBatchDetele?'批量删除':'删除'" @on-ok="deleteModalSureBtn" @on-cancel="deleteModalCancelBtn">
      <p>确定删除？</p>
    </Modal>
  </div>
</template>

<script>
import axios from "@/libs/api.request";
import mould_embryo_manage_form_config from "./mould-embryo-manage-form-config.js";
import { dateFormat, objCopy } from "@/libs/tools.js";
import { getToken, getBaseUrl } from "@/libs/util";

export default {
  data() {
    return {
      isDisabled: false,
      deleteModalShow: false, //控制删除modal提示 显示
      deleteRowData: {}, //行删除行数据
      isBatchDetele: false, //批量删除标志
      multiselectRowData: [], //复�?�列数据
      mouldEmbryoDetailModalshow: false, //控制明细modal显示
      isModify: false, //是否是修改明细?
      uploadAction:
        getBaseUrl() + "/mould/embryo-import.json?token=" + getToken(),
      departmentList: [],
      searchCondition: {
        embryoCode: "",
        embryoName: "",
        equipmentName: "",
        page: 1,
        rows: 10
      },
      tableData: {
        total: 0, // 总页�?
        rows: [] // 每页条数
      },
      tableColumns: [
        {
          title: "模具坯编码",
          key: "embryoCode",
          minWidth: 160
        },
        {
          title: "模具坯名称",
          key: "embryoName",
          minWidth: 320
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
      formValidate: mould_embryo_manage_form_config.formValidate, //user form表单字段
      ruleValidate: mould_embryo_manage_form_config.ruleValidate //user form表单验证规则
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
          url: "/mould/embryo-page-list",
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
          url: "/mould/embryo-synchro",
          method: "post",
          isAuth: true,
          params: self.searchCondition
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            if (data.data === 1) {
              self.$Message.success("模具坯同步成功!");
              self.getListData();
            } else {
              self.$Message.error("未找到模具坯!");
            }
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },
    addNewEmbryo() {
      this.isDisabled = true;
      let para = {
        embryoCode: this.formValidate.embryoCode,
        embryoName: this.formValidate.embryoName
      };
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/mould/mould-embryo-add",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          if (res.data.code === 200) {
            self.mouldEmbryoDetailModalshow = false;
            self.$Message.success("模具坯新建成功!");
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
    editEmbryo() {
      let para = {
        pkId: this.formValidate.pkId,
        embryoCode: this.formValidate.embryoCode,
        embryoName: this.formValidate.embryoName
      };
      const self = this;
      isAuth: true,
        axios
          .request({
            headers: {
              "Content-Type": "application/json; charset=UTF-8"
            },
            url: "/mould/embryo-update",
            method: "post",
            isAuth: true,
            params: para
          })
          .then(function(res) {
            if (res.data.code === 200) {
              self.mouldEmbryoDetailModalshow = false;
              self.$Message.success("模具坯信息修改成功!");
              self.getListData();
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
    deleteRowDate(pkId,embryoCode) {
      let para = {
        pkId: pkId,
        embryoCode:embryoCode
      };
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/mould/embryo-delete-by-id",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          self.deleteModalShow = false;
          if (res.data.code === 200) {
            self.$Message.success("模具坯信息删除成功!");
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
        this.deleteRowDate(this.deleteRowData.pkId,this.deleteRowData.embryoCode);
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
      this.mouldEmbryoDetailModalshow = true;
      this.isModify = true;
      this.formValidate = objCopy(this.tableData.rows[index], {});
    },
    remove(index) {
      this.deleteRowData = this.tableData.rows[index];
      this.deleteModalShow = true;
      this.isBatchDetele = false;
    },
    exportData() {
      const self = this;
      window.location.href =
        getBaseUrl() + "/mould/embryo-export?token=" + getToken();
    },
    addEmbryo() {
      this.mouldEmbryoDetailModalshow = true;
      this.isModify = false;
      this.formValidate = {
        amount: 1
      };
      this.$refs["formValidate"].resetFields();
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
            this.editEmbryo();
          } else {
            this.addNewEmbryo();
          }
        } else {
          this.$Message.error("请输入正确信息！");
        }
      });
    },
    handleReset(name) {
      this.mouldEmbryoDetailModalshow = false;
      this.$refs[name].resetFields();
    }
  }
};
</script>

<style>
</style>
