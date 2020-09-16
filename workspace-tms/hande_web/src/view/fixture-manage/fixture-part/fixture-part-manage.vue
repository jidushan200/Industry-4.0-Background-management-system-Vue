<template>
  <div>
    <Card>
      <Row>
        <Form ref="formInline" :model="searchCondition" :label-width="90" inline>
          <Col span="16">
            <FormItem label="图号名称" prop="fixtureMap">
              <Select
                v-model="searchCondition.fixtureMap"
                ref="checkType"
                style="width:158px;"
                placeholder="请选择夹具图号名称"
                clearable
                filterable
              >
                <Option
                  v-for="item in fixtureList"
                  :value="item.fixtureMap"
                  :key="item.fixtureMap"
                >{{ item.fixtureMap }} {{ item.fixtureName }}</Option>
              </Select>
            </FormItem>
            <FormItem label="制件编码名称" prop="partCode">
              <Select
                v-model="searchCondition.partCode"
                ref="checkType"
                style="width:158px;"
                placeholder="请选择制件编号名称"
                clearable
                filterable
              >
                <Option
                  v-for="item in partList"
                  :value="item.partCode"
                  :key="item.partCode"
                >{{ item.partCode }}-{{ item.partName }}</Option>
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
              <Button type="success" style="margin-right: 5px;" @click="addFixturePart">
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
      v-model="fixturePartDetailModalshow"
      :title="isModify?'修改夹具制件':'新增夹具制件'"
      width="600"
      draggable
    >
      <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="80" inline>
        <Row>
          <FormItem label="夹具编码" prop="fixtureNumber">
            <Input
              style="width:170px"
              v-model="formValidate.fixtureNumber"
              @keyup.enter.native="searchFixtureBase()"
              placeholder="输入夹具编码"
            >
              <Icon type="ios-search" slot="suffix" />
            </Input>
          </FormItem>
          <FormItem label="夹具名称" prop="fixtureName">
            <Input v-model="fixtureName" readonly />
          </FormItem>
        </Row>
        <Row>
          <FormItem label="制件编码" prop="partCode">
            <Input
              style="width:170px"
              v-model="formValidate.partCode"
              @keyup.enter.native="searchPart()"
              placeholder="输入制件编码"
            >
              <Icon type="ios-search" slot="suffix" />
            </Input>
          </FormItem>
          <FormItem label="制件名称" prop="partName">
            <Input v-model="partName" readonly />
          </FormItem>
        </Row>
      </Form>
      <div slot="footer">
        <Button @click="handleSubmit('formValidate')" type="primary" :disabled="isDisable">保存</Button>
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
import fixture_part_form_config from "./fixture-part-form-config.js";
import { dateFormat, objCopy } from "@/libs/tools.js";
import { getToken, getBaseUrl, util } from "@/libs/util";

export default {
  data() {
    return {
      isDisable: false,
      fixtureName: "",
      partName: " ",
      fixturePartDetailModalshow: false,
      deleteModalShow: false,
      deleteRowData: {}, //行删除行数据
      isModify: false, //是否是修改明细操作
      fixtureList: [],
      partList: [],
      isBatchDetele: false,
      uploadAction:
        getBaseUrl() + "/fixture/fixture-part-import.json?token=" + getToken(),
      searchCondition: {
        fixtureNumber: "",
        fixtureName: "",
        partCode: "",
        partName: "",
        page: 1,
        rows: 10
      },
      tableData: {
        total: 0, // 总页数
        rows: [] // 每页条数
      },
      tableColumns: [
        {
          title: "夹具编码",
          key: "fixtureNumber",
          minWidth: 150
        },
        {
          title: "夹具名称",
          key: "fixtureName",
          minWidth: 300
        },
        {
          title: "制件编码",
          key: "partCode",
          minWidth: 200
        },
        {
          title: "制件名称",
          key: "partName",
          minWidth: 160
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
      formValidate: fixture_part_form_config.formValidate, //user form表单字段
      ruleValidate: fixture_part_form_config.ruleValidate //user form表单验证规则
    };
  },
  created() {
    const self = this;
    self.getListData();
    self.getFixtureListData();
    self.getPartListData();
  },
  methods: {
    getListData() {
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/fixture/fixturePart-page-list.json",
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

    getFixtureListData() {
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/fixture/get-base-list",
          method: "post",
          isAuth: true
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            self.fixtureList = data.data;
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
          method: "post",
          isAuth: true
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            self.partList = data.data;
          }
        })
        .catch(function(err) {
          console.log(err);
        });
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
    addNewToolPart() {
      this.isDisabled = true;
      const self = this;
      self.isModify = false;
      self.fixturePartDetailModalshow = true;
      let para = {
        fixtureId: self.fixtureId,
        partId: self.partId
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/fixture/fixturePart-add.json",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            self.fixturePartDetailModalshow = false;
            self.$Message.success("夹具制件新增成功!");
            self.isDisabled = false;
            self.getListData();
          } else {
            self.$Notice.error({
              title: "错误提示",
              desc: res.data.info
            });
            self.isDisabled = false;
          }
          self.isDisable = false;
        })
        .catch(function(err) {
          console.log(err);
        });
    },
    editToolPart() {
      this.isModify = false;
      this.isDisabled = true;
      const self = this;
      let para = {
        pkId: self.pkId,
        fixtureId: self.fixtureId,
        partId: self.partId
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/fixture/fixturePart-update.json",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            self.$Message.success("夹具制件更新成功!");
            self.isDisabled = false;
            self.fixturePartDetailModalshow = false;
            self.getListData();
          } else {
            self.$Notice.error({
              title: "错误提示",
              desc: res.data.info
            });
            self.isDisabled = false;
          }
          self.isDisable = false;
        })
        .catch(function(err) {
          console.log(err);
        });
    },
    searchFixtureBase() {
      const self = this;
      if (!!!this.formValidate.fixtureNumber) {
        return;
      }
      let para = {
        fixtureNumber: this.formValidate.fixtureNumber
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/fixture/fixture-base-get-by-number.json",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          //self.reportDetailModalshow = false;
          const data = res.data;
          if (data.code === 200) {
            if (!!data.data) {
              self.fixtureName = data.data.fixtureName;
              self.fixtureId = data.data.pkId;
            } else {
              self.fixtureName = null;
              self.fixtureId = null;
              self.formValidate.fixtureNumber = "";
              self.$Notice.error({
                title: "错误提示",
                desc: "您输入的夹具不存在"
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
          //self.reportDetailModalshow = false;
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
          url: "/fixture/fixturepart-delete",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          self.deleteModalShow = false;
          if (res.data.code === 200) {
            self.$Message.success("夹具制件信息删除成功!");
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
      self.fixturePartDetailModalshow = true;
      self.isModify = true;
      self.formValidate = objCopy(self.tableData.rows[index], {});
      self.fixtureId = self.tableData.rows[index].fixtureId;
      self.fixtureName = self.tableData.rows[index].fixtureName;
      self.partId = self.tableData.rows[index].partId;
      self.partName = self.tableData.rows[index].partName;
      self.pkId = self.tableData.rows[index].pkId;
    },
    addFixturePart() {
      const self = this;
      self.fixturePartDetailModalshow = true;
      self.isModify = false;
      self.formValidate = {};
      self.$refs["formValidate"].resetFields();
      self.partName = "";
      self.fixtureName = "";
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
      this.fixturePartDetailModalshow = false;
    },
    exportData() {
      window.location.href =
        getBaseUrl() + "/fixture/fixture-part-export?token=" + getToken();
    },
    //form 校验方法
    handleSubmit(name) {
      const self = this;
      self.$refs[name].validate(valid => {
        if (valid) {
          self.isDisable = true;
          if (self.isModify) {
            self.editToolPart();
          } else {
            self.addNewToolPart();
          }
        } else {
          self.$Message.error("请输入正确信息!");
        }
      });
    }
  }
};
</script>

<style>
</style>
