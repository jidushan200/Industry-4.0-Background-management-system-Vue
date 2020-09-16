<template>
  <div>
    <Card>
      <Row>
        <Form ref="formInline" :model="searchCondition" :label-width="65" inline>
          <Col span="18">
            <FormItem label="组合编码" prop="materialNumber">
              <Input type="text" v-model="searchCondition.materialNumber" placeholder="请输入物料编码" />
            </FormItem>
            <FormItem label="检验类型" prop="checkType">
              <Select
                v-model="searchCondition.checkType"
                ref="checkType"
                style="width:158px;"
                placeholder="请选择质检类型"
              >
                <Option
                  v-for="item in checkTypeList"
                  :value="item.value"
                  :key="item.value"
                >{{ item.name }}</Option>
              </Select>
            </FormItem>
            <FormItem>
              <Button type="primary" style="margin-left: -60px;" @click="doSearch('formInline')">搜索</Button>
            </FormItem>
            <FormItem style="margin-left: -65px;">
              <Button type="dashed" @click="doReset('formInline')">重置</Button>
            </FormItem>
          </Col>
          <Col span="6">
            <FormItem style="width:100%;text-align:right">
              <Upload
                :show-upload-list="true"
                :action="uploadAction"
                :on-success="handleSuccess"
                :on-error="handleError"
                style="display: inline-block;margin-right: 5px;"
              >
                <Button icon="ios-cloud-upload-outline">导入</Button>
              </Upload>

              <Button type="success" style="margin-right: 5px;" @click="addCheckStandard">
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
      v-model="deleteModalShow"
      width="250"
      :title="isBatchDetele?'批量删除':'删除'"
      @on-ok="deleteModalSureBtn"
      @on-cancel="deleteModalCancelBtn"
    >
      <p>确定删除所选项？</p>
    </Modal>
    <check-standard-modal ref="checkStandardModal" @search="getListData"></check-standard-modal>
  </div>
</template>

<script>
import axios from "@/libs/api.request";
import { getToken, getBaseUrl } from "@/libs/util";
import check_standard_form_config from "./check-standard-form-config.js";
import { dateFormat, objCopy } from "@/libs/tools.js";
import checkStandardModal from "./check-standard-modal.vue";
export default {
  components: {
    checkStandardModal
  },
  data() {
    return {
      deleteModalShow: false, //控制删除modal提示 显示
      deleteRowData: {}, //行删除行数据
      isBatchDetele: false, //批量删除标志
      multiselectRowData: [], //复选列数据
      typeList: [],
      checkTypeList: [
        {
          name: "刃磨质检",
          value: 7
        },
        {
          name: "涂层质检",
          value: 8
        }
      ],
      uploadAction:
        getBaseUrl() + "/base/check-standard-import.json?token=" + getToken(),
      searchCondition: {
        materialType: 3,
        materialNumber: "",
        checkType: "",
        page: 1,
        rows: 10
      },
      tableData: {
        total: 0, // 总页数
        rows: [] // 每页条数
      },
      tableColumns: [
        {
          title: "组合编码",
          key: "materialNumber",
          minWidth: 160
        },
        {
          title: "组合名称",
          key: "materialName",
          minWidth: 300
        },      
        {
          title: "检验类型",
          key: "checkType",
          minWidth: 120,
          render: function(h, params) {
            let typeStr = "";
            switch (params.row.checkType) {
              case 7:
                typeStr = "刃磨质检";
                break;
              case 8:
                typeStr = "涂层质检";
                break;
            }
            return h("div", typeStr);
          }
        },
        {
          title: "标准描述",
          key: "standardDesc",
          minWidth: 280
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
                      this.show(
                        params.row.pkId,
                        params.row.materialName,
                        params.row.materialMap
                      );
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
                      this.delete(params.index);
                    }
                  }
                },
                "删除"
              )
            ]);
          }
        }
      ],
      formValidate: check_standard_form_config.formValidate, // form表单字段
      ruleValidate: check_standard_form_config.ruleValidate // form表单验证规则
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
          url: "/base/check-standard-page-list",
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
    delete(index) {
      this.deleteRowData = this.tableData.rows[index];
      this.deleteModalShow = true;
      this.isBatchDetele = false;
    },

    getcheckTypeName(checkType) {
      let checkTypeName = "";
      if (checkType == 7) {
        checkTypeName = "刃磨质检";
      } else if (checkType == 8) {
        checkTypeName = "涂层质检";
      }
      return checkTypeName;
    },

    deleteModalSureBtn() {
      const self = this;
      let para = {
        pkId: self.deleteRowData.pkId,
        materialNumber: self.deleteRowData.materialNumber,
        checkTypeName: self.getcheckTypeName(self.deleteRowData.checkType)
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/base/check-standard-delete-by-id",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          self.deleteModalShow = false;
          if (res.data.code === 200) {
            self.$Message.success("质检项信息删除成功!");
            self.getListData();
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },

    //删除modal 取消方法
    deleteModalCancelBtn() {
      if (this.isBatchDetele) {
        this.multiselectRowData = [];
      } else {
        this.deleteRowData = {};
      }
    },
    addCheckStandard() {
      const self = this;
      self.$refs.checkStandardModal.showModal(null, 3);
    },
    show(pkId, materialName, materialMap) {
      const self = this;
      self.$refs.checkStandardModal.showModal(
        pkId,
        3,
        materialName,
        materialMap
      );
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
      let checkType = self.searchCondition.checkType;
      let para = "&materialType=3";
      if (!!checkType) {
        para += "&checkType=" + checkType;
      }
      let materialNumber = self.searchCondition.materialNumber;

      if (!!materialNumber) {
        para = para + "&materialNumber=" + materialNumber;
      }
      window.location.href =
        getBaseUrl() + "/base/check-standard-export?token=" + getToken() + para;
    }
  }
};
</script>


