<template>
  <div>
    <Card>
      <Row>
        <Form ref="formInline" :model="searchCondition" :label-width="80" inline>
          <FormItem prop="paramName" label="参数名称">
            <Input type="text" v-model="searchCondition.paramName" placeholder="参数名称" />
          </FormItem>
          <FormItem>
            <Button type="primary" @click="doSearch('formInline')">搜索</Button>&nbsp;
            <Button type="dashed" @click="doReset('formInline')">重置</Button>&nbsp;
          </FormItem>
        </Form>
      </Row>

      <Table :data="tableData.rows" :columns="tableColumns" border stripe highlight-row></Table>
      <div style="margin: 10px;overflow: hidden">
        <div style="float: right;">
          <Page show-total
            :total="tableData.total"
            :page-size="searchCondition.rows"
            :current="searchCondition.page"
            @on-change="changePage"
          ></Page>
        </div>
      </div>
    </Card>

    <Modal v-model="paramDetailModalshow" :title="isModify?'修改参数':'新增参数'" width="320" draggable>
      <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="80">
        <FormItem label="参数键" prop="paramKey">
          <Input v-model="formValidate.paramKey" placeholder="输入参数键" />
        </FormItem>
        <FormItem label="参数名称" prop="paramName">
          <Input v-model="formValidate.paramName" placeholder="输入参数名称" />
        </FormItem>
        <FormItem label="参数值" prop="paramValue">
          <Input v-model="formValidate.paramValue" placeholder="输入参数值" />
        </FormItem>
      </Form>
      <div slot="footer">
        <Button type="primary" @click="handleSubmit('formValidate')" :disabled="isDisabled">保存</Button>
        <Button @click="handleReset('formValidate')" style="margin-left: 8px">取消</Button>
      </div>
    </Modal>
  </div>
</template>

<script>
import axios from "@/libs/api.request";
import sys_param_manage_form_config from "./sys-param-manage-form-config.js";
import { dateFormat, objCopy, hasOneOf } from "@/libs/tools.js";
let self = this;
export default {
  data() {
    return {
      isDisabled: false,
      paramDetailModalshow: false, //控制明细modal显示
      isModify: false, //是否是修改明细操作
      searchCondition: {
        paramName: "",
        page: 1,
        rows: 10
      },
      tableData: {
        total: 0, // 总页数
        rows: [] // 每页条数
      },
      tableColumns: [
        {
          title: "参数键",
          key: "paramKey",
          minWidth: 120
        },
        {
          title: "参数名称",
          key: "paramName",
          minWidth: 120
        },
        {
          title: "参数值",
          key: "paramValue",
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
          align: "center",
          width: 120,
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
                "修改"
              )
            ]);
          }
        }
      ],
      formValidate: sys_param_manage_form_config.formValidate, //user form表单字段
      ruleValidate: sys_param_manage_form_config.ruleValidate //user form表单验证规则
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
          url: "/sys/sys-param-page-list.json",
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
    doSearch(name) {
      const self = this;
      self.changePage(1);
    },
    doReset(name) {
      const self = this;
      self.$refs[name].resetFields();
      self.getListData();
    },
    show(index) {
      this.paramDetailModalshow = true;
      this.isModify = true;
      this.formValidate = objCopy(this.tableData.rows[index], {});
    },
    editParamSubmit() {
      this.isDisabled = true;
      const self = this;
      let para = {
        pkId: this.formValidate.pkId,
        paramKey: this.formValidate.paramKey,
        paramName: this.formValidate.paramName,
        paramValue: this.formValidate.paramValue
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/sys/sys-param-update.json",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          const data = res.data;
          self.paramDetailModalshow = false;
          if (res.data.code === 200) {
            self.$Message.success("参数修改成功!");
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
    //form 校验方法
    handleSubmit(name) {
      this.$refs[name].validate(valid => {
        if (valid) {
          if (this.isModify) {
            this.editParamSubmit();
          } else {
            this.addRoleSubmit();
          }
        } else {
          this.$Message.error("请输入正确信息!");
        }
      });
    },
    handleReset(name) {
      this.paramDetailModalshow = false;
      this.$refs[name].resetFields();
    }
  }
};
</script>

<style>
</style>
