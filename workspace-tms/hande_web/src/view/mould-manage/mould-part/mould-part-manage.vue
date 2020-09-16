<template>
  <div>
    <Card>
      <Row>
        <Form ref="formInline" :model="searchCondition" :label-width="65" inline>
          <Col span="16">
            <FormItem label="模具编码" prop="mouldNumber">
              <Input type="text" v-model="searchCondition.mouldNumber" placeholder="请输入模具编码"></Input>
            </FormItem>
            <FormItem label="制件编码" prop="partCode">
              <Input type="text" v-model="searchCondition.partCode" placeholder="请输入制件编码"></Input>
            </FormItem>
            <FormItem>
              <Button type="primary" style="margin-left: -60px;" @click="doSearch('formInline')">搜索</Button>
            </FormItem>
            <FormItem style="margin-left: -65px;">
              <Button style type="dashed" @click="doReset('formInline')">重置</Button>
            </FormItem>
          </Col>
          <Col span="8">
            <FormItem style="width:100%;text-align:right">
              <Upload
                :show-upload-list="false"
                :action="uploadAction"
                :on-success="handleSuccess"
                :on-error="handleError"
                style="display: inline-block;margin-right: 5px;"
              >
                <Button icon="ios-cloud-upload-outline">导入</Button>
              </Upload>
              <Button type="success" style="margin-right: 5px;" @click="addMouldPart">
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
      v-model="mouldPartDetailModalshow"
      :title="isModify?'修改模具制件':'新增模具制件'"
      width="600"
      draggable
    >
      <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="80" inline>
        <Row>
          <FormItem label="模具编码" prop="mouldNumber">
            <Input
              style="width:170px"
              v-model="formValidate.mouldNumber"
              @on-blur="searchMouldBase()"
              placeholder="输入模具编码"
            >
              <Icon type="ios-search" slot="suffix" />
            </Input>
          </FormItem>
          <FormItem label="模具名称" prop="mouldName">
            <Input style="width:170px" v-model="mouldName" readonly />
          </FormItem>
        </Row>
        <Row>
          <FormItem label="制件编码" prop="partCode">
            <Input
              style="width:170px"
              v-model="formValidate.partCode"
              @on-blur="searchPart()"
              placeholder="输入制件编码"
            >
              <Icon type="ios-search" slot="suffix" />
            </Input>
          </FormItem>
          <FormItem label="制件名称" prop="partName">
            <Input style="width:170px" v-model="partName" readonly />
          </FormItem>
        </Row>
      </Form>
      <div slot="footer">
        <Button @click="handleSubmit('formValidate')" :disabled="isDisabled" type="primary">保存</Button>
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
import mould_part_form_config from "./mould-part-form-config.js";
import { dateFormat, objCopy } from "@/libs/tools.js";
import { getToken, getBaseUrl, util } from "@/libs/util";

export default {
  data() {
    return {
      isDisabled: false,
      uploadAction:
        getBaseUrl() + "/mould/mould-part-import.json?token=" + getToken(),
      mouldName: "",
      partName: " ",
      mouldPartDetailModalshow: false,
      deleteModalShow: false,
      deleteRowData: {}, //行删除行数据
      isModify: false, //是否是修改明细操作
      supplierList: [],
      isBatchDetele: false,
      searchCondition: {
        mouldNumber: "",
        partCode: "",
        page: 1,
        rows: 10
      },
      tableData: {
        total: 0, // 总页数
        rows: [] // 每页条数
      },
      tableColumns: [
        {
          title: "模具编码",
          key: "mouldNumber",
          minWidth: 160
        },
        {
          title: "模具名称",
          key: "mouldName",
          minWidth: 200
        },
        {
          title: "制件编码",
          key: "partCode",
          minWidth: 160
        },
        {
          title: "制件名称",
          key: "partName",
          minWidth: 360
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
      formValidate: mould_part_form_config.formValidate, //user form表单字段
      ruleValidate: mould_part_form_config.ruleValidate //user form表单验证规则
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
          url: "/mould/mould-part-page-list.json",
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
    addNewMouldPart() {
      this.isDisabled = true;
      const self = this;
      self.isModify = false;
      self.mouldPartDetailModalshow = true;
      let para = {
        mouldNumber: self.mouldNumber,
        partCode: self.partCode,
        mouldId: self.mouldId,
        partId: self.partId
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/mould/mould-part-add.json",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            self.mouldPartDetailModalshow = false;
            self.$Message.success("模具制件新增成功!");
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
    editMouldPart() {
      this.isModify = false;
      this.isDisabled = true;
      const self = this;
      let para = {
        pkId: self.pkId,
        mouldNumber: self.mouldNumber,
        partCode: self.partCode,
        mouldId: self.mouldId,
        partId: self.partId
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/mould/mould-part-update.json",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            self.$Message.success("模具制件更新成功!");
            self.isDisabled = false;
            self.mouldPartDetailModalshow = false;
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
    searchMouldBase() {
      const self = this;
      if (!!!this.formValidate.mouldNumber) {
        return;
      }
      let para = {
        mouldNumber: this.formValidate.mouldNumber
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/mould/mould-base-get-by-number.json",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            if (!!data.data) {
              self.mouldName = data.data.mouldName;
              self.mouldId = data.data.pkId;
            } else {
              self.mouldName = null;
              self.mouldId = null;
              self.formValidate.mouldNumber = "";
              self.$Notice.error({
                title: "错误提示",
                desc: "您输入的模具不存在"
              });
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
    searchPart() {
      const self = this;
      if (!!!this.formValidate.partCode) {
        return;
      }
      let para = {
        partCode: this.formValidate.partCode
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/base/part-get-code.json",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            if (!!data.data) {
              self.partName = data.data.partName;
              self.partId = data.data.pkId;
            } else {
              self.partName = null;
              self.partId = null;
              self.formValidate.partCode = "";
              self.$Notice.error({
                title: "错误提示",
                desc: "您输入的制件不存在"
              });
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
    deleteRowDate(pkId, mouldNumber, partCode) {
      let para = {
        pkId: pkId,
        mouldNumber: mouldNumber,
        partCode: partCode
      };
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/mould/mould-part-delete",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          self.deleteModalShow = false;
          if (res.data.code === 200) {
            self.$Message.success("模具制件信息删除成功!");
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
        this.deleteRowDate(
          this.deleteRowData.pkId,
          this.deleteRowData.mouldNumber,
          this.deleteRowData.partCode
        );
      }
    },
    show(index) {
      const self = this;
      self.mouldPartDetailModalshow = true;
      self.isModify = true;
      self.formValidate = objCopy(self.tableData.rows[index], {});
      self.mouldId = self.tableData.rows[index].mouldId;
      self.mouldName = self.tableData.rows[index].mouldName;
      self.partId = self.tableData.rows[index].partId;
      self.partName = self.tableData.rows[index].partName;
      self.pkId = self.tableData.rows[index].pkId;
    },
    addMouldPart() {
      const self = this;
      self.mouldPartDetailModalshow = true;
      self.isModify = false;
      self.formValidate = {};
      self.$refs["formValidate"].resetFields();
      self.partName = "";
      self.mouldName = "";
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
    handleSuccess() {
      const self = this;
      self.getListData();
      self.$Message.success("文件导入成功!");
    },
    handleError() {
      const self = this;
      self.$Message.success("文件导入失败!");
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
    remove(index) {
      this.deleteRowData = this.tableData.rows[index];
      this.deleteModalShow = true;
      this.isBatchDetele = false;
    },
    handleReset(name) {
      this.$refs["formValidate"].resetFields();
      this.mouldPartDetailModalshow = false;
    },
    exportData() {
      const self = this;
      window.location.href =
        getBaseUrl() + "/mould/mould-part-export?token=" + getToken();
    },
    //form 校验方法
    handleSubmit(name) {
      this.$refs[name].validate(valid => {
        if (valid) {
          if (this.isModify) {
            this.editMouldPart();
          } else {
            this.addNewMouldPart();
          }
        } else {
          this.$Message.error("请输入正确信息!");
        }
      });
    }
  }
};
</script>

<style>
</style>
