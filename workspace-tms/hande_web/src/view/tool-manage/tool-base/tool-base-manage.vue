<template>
  <div>
    <Card>
      <Row>
        <Form ref="formInline" :model="searchCondition" :label-width="65" inline>
          <Col span="16">
            <FormItem label="物料类型" prop="typeId">
              <Select
                style="width:120px"
                v-model="searchCondition.typeId"
                ref="typeId"
                placeholder="请选择物料类型"
              >
                <Option
                  v-for="item in typeList"
                  :value="item.pkId"
                  :key="item.typeNumber"
                >{{ item.typeName }}</Option>
              </Select>
            </FormItem>
            <FormItem label="物料编码" prop="toolNumber">
              <Input style="width:170px" v-model="searchCondition.toolNumber" placeholder="输入物料编码" />
            </FormItem>
            <FormItem label="物料图号" prop="toolMap">
              <Input style="width:170px" v-model="searchCondition.toolMap" placeholder="输入物料图号" />
            </FormItem>
            <!--
            <FormItem label="图号名称" prop="toolMap">
              <Select
                style="width:220px"
                v-model="searchCondition.toolMap"
                placeholder="请选择图号名称"
                clearables
                filterable
              >
                <Icon type="ios-search" slot="suffix" />
                <Option
                  v-for="item in baseList"
                  :value="item.toolMap"
                  :key="item.toolMap"
                >{{ item.toolName }}</Option>
              </Select>
            </FormItem>-->
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
              <Upload
                :show-upload-list="false"
                :action="uploadAction"
                :on-success="handleSuccess"
                :on-error="handleError"
                style="display: inline-block;margin-right: 5px;"
              >
                <Button icon="ios-cloud-upload-outline">导入</Button>
              </Upload>
              <Button type="success" style="margin-right: 5px;" @click="addToolBase">
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
        :data="tableData.rows"
        :columns="tableColumns"
        stripe
        border
        @on-sort-change="changeSort"
      ></Table>

      <div style="margin: 10px;overflow: hidden">
        <div style="float: right;">
          <Page
            :total="tableData.total"
            show-total
            :page-size="searchCondition.rows"
            :current="searchCondition.page"
            @on-change="changePage"
            @on-page-size-change="handlePageSize"
            show-sizer
          ></Page>
        </div>
      </div>
    </Card>

    <Modal v-model="toolBaseDetailModalshow" :title="isModify?'修改刀具':'新增刀具'" width="650" draggable>
      <Form
        ref="formValidate"
        :model="formValidate"
        :rules="ruleValidate"
        :label-width="100"
        inline
      >
        <FormItem label="物料编码" prop="toolNumber">
          <Input
            style="width:170px;text-transform:uppercase"
            v-model="formValidate.toolNumber"
            placeholder="输入物料编码"
          />
        </FormItem>
        <FormItem label="物料图号" prop="toolMap">
          <Input style="width:170px" v-model="formValidate.toolMap" placeholder="输入图号" />
        </FormItem>
        <FormItem label="物料名称" prop="toolName">
          <Input style="width:170px" v-model="formValidate.toolName" placeholder="输入名称" />
        </FormItem>
        <FormItem label="物料类型" prop="typeId">
          <Select style="width:170px" v-model="formValidate.typeId" placeholder="请选择物料类型">
            <Option
              v-for="item in typeList"
              :value="item.pkId"
              :key="item.typeNumber"
            >{{ item.typeName }}</Option>
          </Select>
        </FormItem>
        <FormItem label="每次换刀标准加工数" prop="processEach">
          <Input style="width:170px" v-model="formValidate.processEach" placeholder="每次换刀标准加工数" />
        </FormItem>
        <FormItem label="每次换刀最小加工数" prop="processEachMin">
          <Input style="width:170px" v-model="formValidate.processEachMin" placeholder="每次换刀最小加工数" />
        </FormItem>
        <FormItem label="每次换刀最大加工数" prop="processEachMax">
          <Input style="width:170px" v-model="formValidate.processEachMax" placeholder="每次换刀最大加工数" />
        </FormItem>
        <FormItem label="可加工次数" prop="availableTimes">
          <Input style="width:170px" v-model="formValidate.availableTimes" placeholder="输入可加工次数" />
        </FormItem>
        <FormItem label="最大刃磨量" prop="grindingMax">
          <Input style="width:170px" v-model="formValidate.grindingMax" placeholder="输入最大刃磨量" />
        </FormItem>
        <FormItem label="预警刃磨量" prop="grindingCordon">
          <Input style="width:170px" v-model="formValidate.grindingCordon" placeholder="输入预警刃磨量" />
        </FormItem>
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
import tool_base_manage_form_config from "./tool-base-manage-form-config.js";
import { dateFormat, objCopy } from "@/libs/tools.js";
import { getToken, getBaseUrl } from "@/libs/util";
export default {
  data() {
    return {
      isDisabled: false,
      baseList: [],
      isDisable: false,
      deleteModalShow: false, //控制删除modal提示 显示
      deleteRowData: {}, //行删除行数据
      isBatchDetele: false, //批量删除标志
      multiselectRowData: [], //复选列数据
      toolBaseDetailModalshow: false, //控制明细modal显示
      isModify: false, //是否是修改明细操作
      uploadAction:
        getBaseUrl() + "/tool/tool-base-import.json?token=" + getToken(),
      typeList: [],
      searchCondition: {
        toolNumber: "",
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
          title: "物料图号",
          key: "toolMap",
          minWidth: 160
        },
        {
          title: "物料名称",
          key: "toolName",
          minWidth: 320
        },
        {
          title: "物料类型",
          key: "typeName",
          minWidth: 100
        },
        {
          title: "每次换刀标准加工数",
          key: "processEach",
          minWidth: 160
        },
        {
          title: "每次换刀最小加工数",
          key: "processEachMin",
          minWidth: 160
        },
        {
          title: "每次换刀最大加工数",
          key: "processEachMax",
          minWidth: 160
        },
        {
          title: "可加工次数",
          key: "availableTimes",
          sortable: true,
          minWidth: 120
        },
        {
          title: "理论加工数量",
          key: "processTotal",
          minWidth: 120
        },
        {
          title: "最大刃磨量",
          key: "grindingMax",
          minWidth: 120
        },
        {
          title: "预警刃磨量",
          key: "grindingCordon",
          minWidth: 120
        },
        {
          title: "单价（￥）",
          key: "price",
          sortable: "custom",
          minWidth: 120
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
      formValidate: tool_base_manage_form_config.formValidate, //user form表单字段
      ruleValidate: tool_base_manage_form_config.ruleValidate //user form表单验证规则
    };
  },
  created() {
    const self = this;
    self.getTypeListData();
    //self.getBaseListData();
    self.getListData();
  },
  methods: {
    getBaseListData() {
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/tool-base-list",
          isAuth: true,
          method: "post"
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200 && data.data !== null) {
            self.baseList = data.data;
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
          url: "/tool/tool-base-page-list",
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
          url: "/tool/tool-base-synchro",
          method: "post",
          isAuth: true,
          params: self.searchCondition
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            if (data.data === 1) {
              self.$Message.success("物料同步成功!");
              self.getListData();
            } else {
              self.$Message.error("物料没查到!");
            }
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },

    getTypeListData() {
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/tool-type-list",
          method: "post",
          isAuth: true
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            self.typeList = data.data;
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },
    addNewToolBase() {
      this.isDisabled = true;
      let para = {
        toolNumber: this.formValidate.toolNumber,
        toolMap: this.formValidate.toolMap,
        toolName: this.formValidate.toolName,
        processEach: this.formValidate.processEach,
        processEachMIn: this.formValidate.processEachMin,
        processEachMax: this.formValidate.processEachMax,
        availableTimes: this.formValidate.availableTimes,
        processCordon: this.formValidate.processCordon,
        grindingMax: this.formValidate.grindingMax,
        grindingCordon: this.formValidate.grindingCordon,
        coatMax: this.formValidate.coatMax,
        typeId: this.formValidate.typeId
      };
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/tool-base-add",
          isAuth: true,
          method: "post",
          params: para
        })
        .then(function(res) {
          if (res.data.code === 200) {
            self.toolBaseDetailModalshow = false;
            self.$Message.success("刀具新建成功!");
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
    editToolBase() {
      this.isDisabled = true;
      let para = {
        pkId: this.formValidate.pkId,
        toolNumber: this.formValidate.toolNumber,
        toolMap: this.formValidate.toolMap,
        toolName: this.formValidate.toolName,
        processEach: this.formValidate.processEach,
        processEachMin: this.formValidate.processEachMin,
        processEachMax: this.formValidate.processEachMax,
        availableTimes: this.formValidate.availableTimes,
        processCordon: this.formValidate.processCordon,
        grindingMax: this.formValidate.grindingMax,
        grindingCordon: this.formValidate.grindingCordon,
        coatMax: this.formValidate.coatMax,
        typeId: this.formValidate.typeId
      };
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/tool-base-update",
          method: "post",
          isAuth: true,
          params: para
        })

        .then(function(res) {
          if (res.data.code === 200) {
            self.toolBaseDetailModalshow = false;
            self.$Message.success("刀具信息修改成功!");
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
    deleteRowDate(pkId, toolNumber) {
      let para = {
        pkId: pkId,
        toolNumber: toolNumber
      };
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/tool-base-delete-by-id",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          self.deleteModalShow = false;
          if (res.data.code === 200) {
            self.$Message.success("刀具信息删除成功!");
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
          this.deleteRowData.toolNumber
        );
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
    changeSort() {
      const self = this;
      if (self.sort === "DESC") {
        self.sort = "ASC";
      } else {
        self.sort = "DESC";
      }
      self.searchCondition.sort = self.sort;
      self.changePage(1);
    },
    remove(index) {
      this.deleteRowData = this.tableData.rows[index];
      this.deleteModalShow = true;
      this.isBatchDetele = false;
    },
    addToolBase() {
      this.toolBaseDetailModalshow = true;
      this.isModify = false;
      this.formValidate = {};
      this.$refs["formValidate"].resetFields();
    },

    exportData() {
      const self = this;
      window.location.href =
        getBaseUrl() + "/tool/tool-base-export?token=" + getToken();
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
      const self = this;
      self.$refs[name].validate(valid => {
        if (valid) {
          self.isDisable = true;
          if (self.isModify) {
            self.editToolBase();
          } else {
            self.addNewToolBase();
          }
        } else {
          self.$Message.error("请输入正确信息!");
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
