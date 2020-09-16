<template>
  <div>
    <Card>
      <Row>
        <Form ref="formInline" :model="searchCondition" :label-width="100" inline>
          <Col span="16">
            <FormItem label="刀条组合编码" prop="toolNumber">
              <Input type="text" v-model="searchCondition.headNumber" placeholder="请输入刀条组合编码"></Input>
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
              <Button type="success" style="margin-right: 5px;" @click="addHeadPartShow">
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
      v-model="headPartDetailModalshow"
      :title="isModify?'修改刀条组合制件':'新增刀条组合制件'"
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
        <Row>
          <FormItem label="刀条组合" prop="headNumber">
            <Select
              style="width:450px"
              v-model="formValidate.headNumber"
              ref="headNumber"
              placeholder="请选择刀条组合"
              clearable
              filterable
            >
              <Icon type="ios-search" slot="suffix" />
              <Option
                v-for="item in headList"
                :value="item.headNumber"
                :key="item.headNumber"
              >{{ item.headNumber }}-{{ item.headName }}</Option>
            </Select>
          </FormItem>
        </Row>
        <Row>
          <FormItem label="制件编号" prop="partCode">
            <Select
              style="width:450px"
              v-model="formValidate.partCode"
              ref="part"
              @on-change="partListSelectChange"
              clearable
              filterable
            >
              <Option
                v-for="item in partList"
                :value="item.partCode"
                :key="item.pkId"
              >{{ item.partCode }}-{{ item.partName }}</Option>
            </Select>
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
import head_part_manage_form_config from "./head-part-manage-form-config.js";
import { dateFormat, objCopy } from "@/libs/tools.js";
import { getToken, getBaseUrl, util } from "@/libs/util";

export default {
  data() {
    return {
      isDisabled: false,
      partName: "",
      headName: "",
      headList: [],
      headPartDetailModalshow: false,
      deleteModalShow: false,
      deleteRowData: {}, //行删除行数据
      isModify: false, //是否是修改明细操作
      partList: [],
      isBatchDetele: false,
      uploadAction:
        getBaseUrl() + "/tool/head-part-import.json?token=" + getToken(),
      searchCondition: {
        toolNumber: "",
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
          title: "刀条组合编码",
          key: "headNumber",
          minWidth: 150
        },
        {
          title: "刀条组合名称",
          key: "headName",
          minWidth: 200
        },
        {
          title: "制件编码",
          key: "partCode",
          minWidth: 140
        },
        {
          title: "制件名称",
          key: "partName",
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
      formValidate: head_part_manage_form_config.formValidate, //user form表单字段
      ruleValidate: head_part_manage_form_config.ruleValidate //user form表单验证规则
    };
  },
  created() {
    const self = this;
    self.getHeadListData();
    self.getListData();
    self.getPartListData();
  },
  methods: {
    getHeadListData() {
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/head-list",
          isAuth: true,
          method: "post"
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200 && data.data !== null) {
            self.headList = data.data;
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },
    getPartListData() {
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/base/part-list",
          isAuth: true,
          method: "post"
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200 && data.data !== null) {
            self.partList = data.data;
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },
    getListData() {
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/head-part-page-list.json",
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

    partListSelectChange(item) {
      let partList = this.partList;
      if (item !== undefined) {
        for (const part of partList) {
          if (part.pkId === item) {
            this.formValidate.partName = part.partName;
            this.formValidate.partCode = part.partCode;
          }
        }
      }
    },
    addHeadPart() {
      this.isDisabled = true;
      const self = this;
      let para = {
        headNumber: self.formValidate.headNumber,
        partCode: self.formValidate.partCode
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/head-part-add",
          method: "post",
          isAuth: true,
          params: para
        })

        .then(function(res) {
          if (res.data.code === 200) {
            self.headPartDetailModalshow = false;
            self.$Message.success("刀条组合制件保存成功!");
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
    editHeadPart() {
      this.isDisabled = true;
      const self = this;
      let para = {
        pkId: self.formValidate.pkId,
        headNumber: self.formValidate.headNumber,
        partCode: self.formValidate.partCode
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/head-part-update",
          method: "post",
          isAuth: true,
          params: para
        })

        .then(function(res) {
          if (res.data.code === 200) {
            self.headPartDetailModalshow = false;
            self.$Message.success("刀条组合制件保存成功!");
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
          url: "/tool/head-part-delete-by-id",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          self.deleteModalShow = false;
          if (res.data.code === 200) {
            self.$Message.success("刀条组合制件删除成功!");
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
      self.headPartDetailModalshow = true;
      self.isModify = true;
      self.formValidate = objCopy(self.tableData.rows[index], {});
      self.pkId = self.tableData.rows[index].pkId;
      self.partName = self.tableData.rows[index].partName;
    },
    addHeadPartShow() {
      const self = this;
      self.headPartDetailModalshow = true;
      self.isModify = false;
      self.formValidate = {};
      self.partName = "";
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
      this.headPartDetailModalshow = false;
    },
    exportData() {
      const self = this;
      window.location.href =
        getBaseUrl() + "/tool/head-part-export?token=" + getToken();
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
            this.editHeadPart();
          } else {
            this.addHeadPart();
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
