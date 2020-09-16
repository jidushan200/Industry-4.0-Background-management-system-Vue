<template>
  <div>
    <Card>
      <Row>
        <Form ref="formInline" :model="searchCondition" :label-width="100" inline>
          <Col span="16">
            <FormItem label="刀盘名称" prop="plateName">
              <Input type="text" v-model="searchCondition.partCode" placeholder="请输入刀盘名称"></Input>
            </FormItem>
            <FormItem>
              <Button type="primary" style="margin-left: -60px;" @click="doSearch('formInline')">搜索</Button>
            </FormItem>
            <FormItem style="margin-left: -65px;">
              <Button style type="dashed" @click="doReset('formInline')">重置</Button>
            </FormItem>
          </Col>
          <Col span="8">
            <FormItem style="width: 100%;text-align: right;">
              <Upload
                :show-upload-list="false"
                :action="uploadAction"
                :on-success="handleSuccess"
                :on-error="handleError"
                style="display: inline-block;margin-right: 5px;"
              >
                <Button icon="ios-cloud-upload-outline">导入</Button>
              </Upload>
              <Button type="success" style="margin-right: 5px;" @click="addplateShow">
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
    <Modal v-model="plateDetailModalshow" :title="isModify?'修改刀盘':'新增刀盘'" width="400" draggable>
      <Form
        ref="formValidate"
        :model="formValidate"
        :rules="ruleValidate"
        :label-width="100"
        inline
      >
        <Row>
          <row>
            <FormItem label="刀盘编码" prop="plateNumber">
              <Input style="width:200px" v-model="formValidate.plateNumber" />
            </FormItem>
          </row>
          <row>
            <FormItem label="刀盘名称" prop="plateName">
              <Input style="width:200px" v-model="formValidate.plateName" />
            </FormItem>
          </row>
          <row>
            <FormItem label="使用状态" prop="useStatus">
              <RadioGroup v-model="formValidate.useStatus">
                <Radio :label="1">在用</Radio>
                <Radio :label="2">在库</Radio>
              </RadioGroup>
            </FormItem>
          </row>
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
import plate_manage_form_config from "./plate-manage-form-config.js";
import { dateFormat, objCopy } from "@/libs/tools.js";
import { getToken, getBaseUrl, util } from "@/libs/util";

export default {
  data() {
    return {
      isDisabled: false,
      plateName: "",
      plateDetailModalshow: false,
      deleteModalShow: false,
      deleteRowData: {}, //行删除行数据
      isModify: false, //是否是修改明细操作
      isBatchDetele: false,
      uploadAction:
        getBaseUrl() + "/tool/plate-import.json?token=" + getToken(),
      searchCondition: {
        plateNumber: "",
        plateName: "",
        page: 1,
        rows: 10
      },
      tableData: {
        total: 0, // 总页数
        rows: [] // 每页条数
      },
      tableColumns: [
        {
          title: "刀盘编码",
          key: "plateNumber",
          minWidth: 200
        },
        {
          title: "刀盘名称",
          key: "plateName",
          minWidth: 200
        },
        {
          title: "使用状态",
          key: "useStatus",
          minWidth: 120,
          render: function(h, params) {
            let statusStr = "";
            switch (params.row.useStatus) {
              case 1:
                statusStr = "在用";
                break;
              case 2:
                statusStr = "在库";
                break;
            }
            return h("div", statusStr);
          }
        },
        {
          title: "操作",
          key: "action",
          width: 200,
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
      formValidate: plate_manage_form_config.formValidate, //user form表单字段
      ruleValidate: plate_manage_form_config.ruleValidate //user form表单验证规则
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
          plateers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/plate-page-list.json",
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

    addplate() {
      this.isDisabled = true;
      const self = this;
      let para = {
        plateNumber: self.formValidate.plateNumber,
        plateName: self.formValidate.plateName,
        useStatus: self.formValidate.useStatus
      };
      axios
        .request({
          plateers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/plate-add",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          if (res.data.code === 200) {
            self.plateDetailModalshow = false;
            self.$Message.success("刀盘保存成功!");
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
    editplate() {
      this.isDisabled = true;
      const self = this;
      let para = {
        pkId: self.formValidate.pkId,
        plateNumber: self.formValidate.plateNumber,
        plateName: self.formValidate.plateName,
        useStatus: self.formValidate.useStatus
      };
      axios
        .request({
          plateers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/plate-update",
          method: "post",
          isAuth: true,
          params: para
        })

        .then(function(res) {
          if (res.data.code === 200) {
            self.plateDetailModalshow = false;
            self.$Message.success("刀盘保存成功!");
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
          plateers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/plate-delete-by-id",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          self.deleteModalShow = false;
          if (res.data.code === 200) {
            self.$Message.success("刀盘删除成功!");
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
    show(index) {
      const self = this;
      self.plateDetailModalshow = true;
      self.isModify = true;
      self.formValidate = objCopy(self.tableData.rows[index], {});
    },
    addplateShow() {
      const self = this;
      self.plateDetailModalshow = true;
      self.isModify = false;
      self.formValidate = {};
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
    remove(index) {
      this.deleteRowData = this.tableData.rows[index];
      this.deleteModalShow = true;
      this.isBatchDetele = false;
    },
    handleReset(name) {
      this.$refs["formValidate"].resetFields();
      this.plateDetailModalshow = false;
    },
    exportData() {
      const self = this;
      window.location.href =
        getBaseUrl() + "/tool/plate-export?token=" + getToken();
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
            this.editplate();
          } else {
            this.addplate();
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
