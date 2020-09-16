<template>
  <div>
    <Card>
      <Row>
        <!-- 搜索栏 -->
        <Form ref="formInline" :model="searchCondition" :label-width="100" inline>
          <Col span="16">
            <FormItem label="刀条组合编码" prop="headNumber">
              <Input type="text" v-model="searchCondition.headNumber" placeholder="请输入刀条组合编码"></Input>
            </FormItem>
            <FormItem label="组合类型" prop="headType">
              <Select
                v-model="searchCondition.headType"
                ref="checkType"
                style="width:158px;"
                placeholder="组合类型"
              >
                <Option
                  v-for="item in headTypeList"
                  :value="item.value"
                  :key="item.value"
                >{{ item.name }}</Option>
              </Select>
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
              <Button type="success" style="margin-right: 5px;" @click="addHeadShow">
                <Icon type="md-add"></Icon>添加
              </Button>
              <Button type="warning" @click="exportData()">
                <Icon type="ios-download-outline"></Icon>导出
              </Button>
            </FormItem>
          </Col>
        </Form>
      </Row>

      <!-- table栏 -->
      <Table ref="tablesMain" :data="tableData.rows" :columns="tableColumns" stripe border></Table>

      <!-- 分页器 -->
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

    <!-- 对话框——编辑/添加 -->
    <Modal
      v-model="headDetailModalshow"
      :title="isModify ? '修改刀条组合' : '新增刀条组合'"
      width="400"
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
          <FormItem label="刀条组合编码" prop="headNumber">
            <Input style="width:200px" v-model="formValidate.headNumber" :disabled="isModify" />
          </FormItem>
        </Row>
        <Row>
          <FormItem label="刀条组合名称" prop="headName">
            <Input style="width:200px" v-model="formValidate.headName" />
          </FormItem>
        </Row>
        <Row>
          <FormItem label="刀条组合类型" prop="headType">
            <RadioGroup v-model="formValidate.headType">
              <Radio :label="1">三面刃</Radio>
              <Radio :label="2">两面刃</Radio>
            </RadioGroup>
          </FormItem>
        </Row>
      </Form>
      <div slot="footer">
        <Button @click="handleSubmit('formValidate')" type="primary" :disabled="isDisabled">保存</Button>
        <Button @click="handleReset('formValidate')" style="margin-left: 8px">取消</Button>
      </div>
    </Modal>

    <!-- 对话框——删除 -->
    <Modal
      v-model="deleteModalShow"
      width="250"
      :title="isBatchDetele ? '批量删除' : '删除'"
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
      headName: "",
      headDetailModalshow: false,
      deleteModalShow: false,
      deleteRowData: {}, //行删除行数据
      isModify: false, //是否是修改明细操作
      supplierList: [],
      headTypeList: [
        {
          name: "三面刃",
          value: 1
        },
        {
          name: "两面刃",
          value: 2
        }
      ],
      isBatchDetele: false,
      uploadAction: getBaseUrl() + "/tool/head-import.json?token=" + getToken(),
      searchCondition: {
        headNumber: "",
        headName: "",
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
          minWidth: 160
        },
        {
          title: "刀条组合名称",
          key: "headName",
          minWidth: 200
        },
        {
          title: "刀条组合类型",
          key: "headType",
          width: 150,
          render: function(h, params) {
            let temp = "";
            switch (params.row.headType) {
              case 1:
                temp = "三面刃";
                break;
              case 2:
                temp = "两面刃";
                break;
              default:
                break;
            }
            return h("div", temp);
          }
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
          url: "/tool/head-page-list.json",
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

    addHead() {
      this.isDisabled = true;
      const self = this;
      let para = {
        headNumber: self.formValidate.headNumber,
        headName: self.formValidate.headName,
        headType: self.formValidate.headType
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/head-add",
          method: "post",
          isAuth: true,
          params: para
        })

        .then(function(res) {
          if (res.data.code === 200) {
            self.headDetailModalshow = false;
            self.$Message.success("刀条组合保存成功!");
            self.isDisabled = false;
            self.getListData();
          } else {
            self.$Message.error(res.data.info);
            self.isDisabled = false;
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },
    editHead() {
      this.isDisabled = true;
      const self = this;
      let para = {
        pkId: self.formValidate.pkId,
        headNumber: self.formValidate.headNumber,
        headName: self.formValidate.headName,
        headType: self.formValidate.headType
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/head-update",
          method: "post",
          isAuth: true,
          params: para
        })

        .then(function(res) {
          if (res.data.code === 200) {
            self.headDetailModalshow = false;
            self.$Message.success("刀条组合保存成功!");
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
          url: "/tool/head-delete-by-id",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          self.deleteModalShow = false;
          if (res.data.code === 200) {
            self.$Message.success("刀条组合删除成功!");
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
      self.headDetailModalshow = true;
      self.isModify = true;
      self.formValidate = objCopy(self.tableData.rows[index], {});
    },
    addHeadShow() {
      const self = this;
      self.headDetailModalshow = true;
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
      this.headDetailModalshow = false;
    },
    exportData() {
      const self = this;
      window.location.href =
        getBaseUrl() + "/tool/head-export?token=" + getToken();
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
            this.editHead();
          } else {
            this.addHead();
          }
        } else {
          this.$Message.error("请输入正确信息!");
        }
      });
    }
  }
};
</script>

<style></style>
