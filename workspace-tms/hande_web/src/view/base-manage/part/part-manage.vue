<template>
  <div>
    <Card>
      <Row>
        <Form ref="formInline" :model="searchCondition" :label-width="65" inline>
          <Col span="16">
            <FormItem label="制件编码" prop="partCode">
              <Input type="text" v-model="searchCondition.partCode" placeholder="请输入制件编码"></Input>
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
                style="display: inline-block;margin-right: 5px;"
              >
                <Button icon="ios-cloud-upload-outline">导入</Button>
              </Upload>
              <Button type="success" style="margin-right: 5px;" @click="addPart">
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

    <Modal v-model="partDetailModalshow" :title="isModify?'修改制件':'新增制件'" width="650" draggable>
      <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="80" inline>
        <FormItem label="制件编码" prop="partCode">
          <Input style="width:170px" v-model="formValidate.partCode" placeholder="输入制件编码" />
        </FormItem>
        <FormItem label="制件名称" prop="partName">
          <Input style="width:170px" v-model="formValidate.partName" placeholder="输入制件名称" />
        </FormItem>

        <FormItem label="制件类型" prop="partTypeId">
          <Select
            style="width:170px"
            v-model="formValidate.partTypeId"
            ref="partType"
            @on-change="partListSelectChange"
          >
            <Option
              v-for="item in partList"
              :value="item.pkId"
              :key="item.pkid"
            >{{ item.partTypeName }}</Option>
          </Select>
        </FormItem>
        <FormItem label="生产部门" prop="departmentId">
          <Select
            style="width:170px"
            v-model="formValidate.departmentId"
            ref="department"
            @on-change="departmentListSelectChange"
          >
            <Option
              v-for="item in departmentList"
              :value="item.pkId"
              :key="item.pkId"
            >{{ item.departmentName }}</Option>
          </Select>
        </FormItem>
        <FormItem label="备注" prop="remark">
          <Input
            type="textarea"
            v-model="formValidate.remark"
            placeholder="输入备注"
            style="width:500px;"
          />
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
import part_manage_form_config from "./part-manage-form-config.js";
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
      partDetailModalshow: false, //控制明细modal显示
      isModify: false, //是否是修改明细操作
      uploadAction: getBaseUrl() + "/base/part-import.json?token=" + getToken(),
      partList: [],
      departmentList: [],
      searchCondition: {
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
          title: "制件编码",
          key: "partCode",
          minWidth: 200
        },
        {
          title: "制件名称",
          key: "partName",
          minWidth: 320
        },
        {
          title: "生产部门",
          key: "departmentName",
          minWidth: 200
        },
        {
          title: "制件类型",
          key: "partTypeName",
          minWidth: 200
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
      formValidate: part_manage_form_config.formValidate, //user form表单字段
      ruleValidate: part_manage_form_config.ruleValidate //user form表单验证规则
    };
  },
  created() {
    const self = this;
    self.getListData();
    self.getDepartmentListData();
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
          url: "/base/tool-part-page-list",
          isAuth: true,
          method: "post",
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
          url: "/base/part-synchro",
          method: "post",
          isAuth: true,
          params: self.searchCondition
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            if (data.data === 1) {
              self.$Message.success("制件同步成功!");
              self.getListData();
            } else {
              self.$Message.error("制件没查到!");
            }
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },
    getDepartmentListData() {
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/base/staff-department-list",
          isAuth: true,
          method: "post"
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            self.departmentList = data.data;
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
          url: "/base/part-name-list",
          isAuth: true,
          method: "post"
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
    addNewPart() {
      let para = {
        partName: this.formValidate.partName,
        partCode: this.formValidate.partCode,
        partType: this.formValidate.partTypeId,
        partTypeName: this.formValidate.partTypeName,
        departmentId: this.formValidate.departmentId,
        departmentName: this.formValidate.departmentName,
        remark: this.formValidate.remark
      };
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/base/tool-part-add",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          if (res.data.code === 200) {
            self.partDetailModalshow = false;
            self.$Message.success("制件新建成功!");
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
    editPart() {
      this.isDisabled = true;
      let para = {
        pkId: this.formValidate.pkId,
        partName: this.formValidate.partName,
        partCode: this.formValidate.partCode,
        partType: this.formValidate.partTypeId,
        partTypeName: this.formValidate.partTypeName,
        departmentId: this.formValidate.departmentId,
        departmentName: this.formValidate.departmentName,
        remark: this.formValidate.remark
      };
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/base/tool-part-update",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          if (res.data.code === 200) {
            self.partDetailModalshow = false;
            self.$Message.success("制件信息修改成功!");
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
          url: "/base/tool-part-delete-by-id",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          self.deleteModalShow = false;
          if (res.data.code === 200) {
            self.$Message.success("制件信息删除成功!");
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
      self.changePage(1);
    },
    doReset(name) {
      const self = this;
      self.$refs[name].resetFields();
      self.changePage(1);
    },
    show(index) {
      this.partDetailModalshow = true;
      this.isModify = true;
      this.formValidate = objCopy(this.tableData.rows[index], {});
    },
    remove(index) {
      this.deleteRowData = this.tableData.rows[index];
      this.deleteModalShow = true;
      this.isBatchDetele = false;
    },
    addPart() {
      this.partDetailModalshow = true;
      this.isModify = false;
      this.formValidate = {};
      this.$refs["formValidate"].resetFields();
    },
    departmentListSelectChange(item) {
      let departmentList = this.departmentList;
      if (item !== undefined) {
        for (const department of departmentList) {
          if (department.pkId === item) {
            this.formValidate.departmentName = department.departmentName;
          }
        }
      }
    },
    partListSelectChange(item) {
      let partList = this.partList;
      if (item !== undefined) {
        for (const part of partList) {
          if (part.pkId === item) {
            this.formValidate.partTypeName = part.partTypeName;
          }
        }
      }
    },
    exportData() {
      const self = this;
      window.location.href =
        getBaseUrl() + "/base/part-export?token=" + getToken();
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
            this.editPart();
          } else {
            this.addNewPart();
          }
        } else {
          this.$Message.error("请输入正确信息!");
        }
      });
    },
    handleReset(name) {
      this.partDetailModalshow = false;
      this.$refs[name].resetFields();
    }
  }
};
</script>

<style>
</style>
