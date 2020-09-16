<template>
  <div>
    <Card>
      <Row>
        <Form ref="formInline" :model="searchCondition" :label-width="75" inline>
          <Col span="16">
            <FormItem label="物料编码" prop="toolNumber">
              <Input type="text" v-model="searchCondition.toolNumber" placeholder="请输入物料编码"></Input>
            </FormItem>
            <FormItem label="供应商编码" prop="supplierCode">
              <Input type="text" v-model="searchCondition.supplierCode" placeholder="请输入供应商编码"></Input>
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
              <Button type="success" style="margin-right: 5px;" @click="addCoatPrice">
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
      v-model="coatPriceDetailModalshow"
      :title="isModify?'修改涂层价格':'新增涂层价格'"
      width="600"
      draggable
    >
      <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="90" inline>
        <Row>
          <FormItem label="物料编码" prop="toolNumber">
            <Input
              style="width:170px"
              v-model="formValidate.toolNumber"
              @on-blur="searchToolBase()"
              placeholder="输入物料编码"
            >
              <Icon type="ios-search" slot="suffix" />
            </Input>
          </FormItem>
          <FormItem label="物料名称" prop="toolName">
            <Input v-model="toolName" style="width:170px" readonly />
          </FormItem>
        </Row>
        <Row>
          <FormItem label="供应商编码" prop="supplierCode">
            <Input
              style="width:170px"
              v-model="formValidate.supplierCode"
              @on-blur="searchSupplier()"
              placeholder="供应商编码"
            >
              <Icon type="ios-search" slot="suffix" />
            </Input>
          </FormItem>
          <FormItem label="供应商名称" prop="supplierName">
            <Input style="width:170px" v-model="supplierName" readonly />
          </FormItem>
        </Row>
        <Row>
          <FormItem label="价格" prop="price">
            <Input style="width:170px" v-model="formValidate.price" />
          </FormItem>
        </Row>
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
import coat_price_form_config from "./coat-price-form-config.js";
import { dateFormat, objCopy } from "@/libs/tools.js";
import { getToken, getBaseUrl, util } from "@/libs/util";

export default {
  data() {
    return {
      isDisabled: false,
      toolName: "",
      supplierName: "",
      coatPriceDetailModalshow: false,
      deleteModalShow: false, //控制删除modal提示 显示
      deleteRowData: {}, //行删除行数据
      isModify: false, //是否是修改明细操作
      uploadAction:
        getBaseUrl() +
        "/base/coat-supplier-coat-import.json?token=" +
        getToken(),
      supplierList: [],
      isBatchDetele: false,
      searchCondition: {
        toolNumber: "",
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
          title: "物料编码",
          key: "toolNumber",
          minWidth: 160
        },
        {
          title: "物料名称",
          key: "toolName",
          minWidth: 280
        },
        {
          title: "供应商编码",
          key: "supplierCode",
          minWidth: 120
        },
        {
          title: "供应商名称",
          key: "supplierName",
          width: 280
        },
        {
          title: "涂层价格",
          key: "price",
          minWidth: 100
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
      formValidate: coat_price_form_config.formValidate, //user form表单字段
      ruleValidate: coat_price_form_config.ruleValidate //user form表单验证规则
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
          url: "/base/coat-price-page-list",
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
    addNewCoatPrice() {
      this.isModify = false;
      this.isDisabled = true;
      const self = this;
      self.coatPriceDetailModalshow = true;
      let para = {
        toolId: self.toolId,
        toolNumber: self.formValidate.toolNumber,
        supplierId: self.supplierId,
        supplierName: self.supplierName,
        price: self.formValidate.price
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/base/coat-price-add.json",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            self.coatPriceDetailModalshow = false;
            self.$Message.success("涂层价格新增成功!");
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
    editCoatPrice() {
      this.isDisabled = true;
      this.isModify = true;
      const self = this;
      self.coatPriceDetailModalshow = true;
      let para = {
        toolId: self.toolId,
        toolNumber: self.formValidate.toolNumber,
        supplierName: self.supplierName,
        supplierId: self.supplierId,
        pkId: self.pkId,
        price: self.formValidate.price
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/base/coat-price-update.json",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            self.coatPriceDetailModalshow = false;
            self.$Message.success("涂层价格更新成功!");
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
    searchToolBase() {
      const self = this;
      if (!!!self.formValidate.toolNumber) {
        return;
      }
      let para = {
        toolNumber: self.formValidate.toolNumber
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/tool-base-get-by-number.json",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          //self.reportDetailModalshow = false;
          const data = res.data;
          if (data.code === 200) {
            if (!!data.data) {
              self.toolName = data.data.toolName;
              self.toolId = data.data.pkId;
            } else {
              self.toolName = null;
              self.toolId = null;
              self.formValidate.toolNumber = "";
              self.$Notice.error({
                title: "错误提示",
                desc: "您输入的物料不存在"
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
    searchSupplier() {
      const self = this;
      if (!!!self.formValidate.supplierCode) {
        return;
      }
      let para = {
        supplierCode: this.formValidate.supplierCode
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/base/supplier-get-by-code.json",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          //self.reportDetailModalshow = false;
          const data = res.data;
          if (data.code === 200) {
            if (!!data.data) {
              self.supplierName = data.data.supplierName;
              self.supplierId = data.data.pkId;
            } else {
              self.supplierName = null;
              self.supplierId = null;
              self.formValidate.supplierCode = "";
              self.$Notice.error({
                title: "错误提示",
                desc: "您输入的供应商不存在"
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
    deleteRowDate(pkId, supplierName, toolNumber) {
      let para = {
        pkId: pkId,
        supplierName: supplierName,
        toolNumber: toolNumber
      };
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/base/coat-price-delete-by-id",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          self.deleteModalShow = false;
          if (res.data.code === 200) {
            self.$Message.success("涂层价格删除成功!");
            self.getListData();
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },
    deleteModalSureBtn() {
      const self = this;
      if (self.isBatchDetele) {
        let ids = [];
        for (const item of self.multiselectRowData) {
          ids.push(item.pkId);
        }
      } else {
        self.deleteRowDate(
          self.deleteRowData.pkId,
          self.deleteRowData.supplierName,
          self.deleteRowData.toolNumber
        );
      }
    },
    show(index) {
      const self = this;
      self.coatPriceDetailModalshow = true;
      self.isModify = true;
      self.formValidate = objCopy(self.tableData.rows[index], {});
      self.pkId = self.tableData.rows[index].pkId;
      self.toolId = self.tableData.rows[index].toolId;
      self.toolName = self.tableData.rows[index].toolName;
      self.supplierId = self.tableData.rows[index].supplierId;
      self.supplierName = self.tableData.rows[index].supplierName;
    },
    addCoatPrice() {
      const self = this;
      self.coatPriceDetailModalshow = true;
      self.isModify = false;
      self.formValidate = {};
      (self.supplierName = ""),
        (self.toolName = ""),
        self.$refs["formValidate"].resetFields();
    },
    //删除modal 取消方法
    deleteModalCancelBtn() {
      const self = this;
      if (self.isBatchDetele) {
        self.multiselectRowData = [];
      } else {
        self.deleteRowData = {};
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
    remove(index) {
      const self = this;
      self.deleteRowData = self.tableData.rows[index];
      self.deleteModalShow = true;
      self.isBatchDetele = false;
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
    exportData() {
      const self = this;
      window.location.href =
        getBaseUrl() + "/base/coat-price-export?token=" + getToken();
    },
    //form 校验方法
    handleSubmit(name) {
      this.$refs[name].validate(valid => {
        if (valid) {
          if (this.isModify) {
            this.editCoatPrice();
          } else {
            this.addNewCoatPrice();
          }
        } else {
          this.$Message.error("请输入正确信息!");
        }
      });
    },
    handleReset(name) {
      this.coatPriceDetailModalshow = false;
      this.$refs[name].resetFields();
    }
  }
};
</script>

<style>
</style>
