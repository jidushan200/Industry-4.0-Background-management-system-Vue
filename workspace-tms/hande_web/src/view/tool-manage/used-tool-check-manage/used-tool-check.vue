<template>
  <div>

    <Card>
      <Row style="margin-bottom: 10px;">
        <Upload :show-upload-list="false" action="">
          <Button icon="ios-cloud-upload-outline">批量导入</Button>
        </Upload>
        <Button type="primary" style="margin-right: 50px;margin-top:-32px;float:right" @click="exportData()">
          <Icon type="ios-download-outline"></Icon>导出
        </Button>
      </Row>
      <Row>
        <Form ref="formInline" inline>
          <FormItem prop="toolName">
            <Input type="text" v-model="searchCondition.toolName" placeholder="刀具名称">
            </Input>
          </FormItem>
          <FormItem prop="toolNumber">
            <Input type="text" v-model="searchCondition.toolNumber" placeholder="物料编码">
            </Input>
          </FormItem>
          <FormItem prop="toolType">
            <Select style="width:158px" v-model="searchCondition.toolType" ref="toolType" placeholder="请选择刀具类型">
              <Option v-for="item in toolType" :value="item.value" :key="item.value">{{ item.name }}</Option>
            </Select>
          </FormItem>
          <FormItem>
            <Button type="primary" @click="doSearch">搜索</Button>
          </FormItem>
        </Form>
      </Row>

      <Table ref="tablesMain" :data="tableData.rows" :columns="tableColumns" stripe></Table>
      <div style="margin: 10px;overflow: hidden">
        <div style="float: right;">
          <Page show-total :total="tableData.total" :page-size="searchCondition.rows" :current="searchCondition.page" @on-change="changePage"></Page>
        </div>
      </div>
    </Card>

    <Modal v-model="toolCheckDetailModalshow" title="质检信息录入" width="780">
      <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="100" inline>
        <FormItem label="刃磨人" prop="executor">
          <Input style="width:170px" v-model="formValidate.executor" placeholder="输入刃磨人" />
        </FormItem>
        <FormItem label="刃磨量" prop="repairMeasure">
          <InputNumber :max="99" :min="0.01" :step="0.01" style="width:170px" v-model="formValidate.repairMeasure" placeholder="输入刃磨量" />
        </FormItem>

      </Form>
      <div slot="footer">
        <Button @click="handleSubmit('formValidate')">保存</Button>
        <Button @click="handleReset('formValidate')" style="margin-left: 8px">取消</Button>
      </div>
    </Modal>
  </div>
</template>

<script>
import axios from "@/libs/api.request";
import used_tool_check_form_config from "./used-tool-check-form-config.js";
import { dateFormat, objCopy } from "@/libs/tools.js";
import toolType from "../tool-coat-manage/tool-type.js";

export default {
  data() {
    return {
      deleteModalShow: false, //控制删除modal提示 显示
      deleteRowData: {}, //行删除行数据
      isBatchDetele: false, //批量删除标志
      multiselectRowData: [], //复选列数据
      toolCheckDetailModalshow: false, //控制明细modal显示
      isModify: false, //是否是修改明细操作
      toolType: toolType,
      checkColList: [],
      index: 1,
      checkStandard: "",
      formDynamic: {
        items: [
          {
            value: "",
            index: 1,
            status: 1
          }
        ]
      },
      searchCondition: {
        toolName: "",
        toolNumber: "",
        toolType: "",
        toolState: 7,
        page: 1,
        rows: 10
      },
      tableData: {
        total: 0, // 总页数
        rows: [] // 每页条数
      },
      tableColumns: [
        {
          title: "刀具名称",
          key: "toolName"
        },
        {
          title: "物料编码",
          key: "toolNumber"
        },
        {
          title: "刀具码",
          key: "fullNumber"
        },
        {
          title: "刀具图号",
          key: "toolMap"
        },
        {
          title: "操作",
          key: "action",
          width: 200,
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
                    disabled: params.row.toolState != 7,
                    size: "small"
                  },
                  style: {
                    marginRight: "5px"
                  },
                  on: {
                    click: () => {
                      this.addToolCheck(params.index);
                    }
                  }
                },
                "质检"
              )
            ]);
          }
        }
      ],
      formValidate: used_tool_check_form_config.formValidate, //user form表单字段
      ruleValidate: used_tool_check_form_config.ruleValidate //user form表单验证规则
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
          url: "/tool/tool-page-list",
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

    addNewToolCheck() {
      let para = {
        toolId: this.formValidate.pkId,
        executor: this.formValidate.executor,
        repairMeasure: this.formValidate.repairMeasure,
        remark: this.formValidate.remark
      };
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/tool-repair",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          self.toolCheckDetailModalshow = false;
          if (res.data.code === 200) {
            self.$Message.success("刀具刃磨信息已记录!");
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
    changePage(cuurentPage) {
      const self = this;
      self.$set(self.searchCondition, "page", cuurentPage);
      self.getListData();
    },
    doSearch() {
      const self = this;
      self.getListData();
      this.formValidate = {};
    },
    addToolCheck(index) {
      this.toolCheckDetailModalshow = true;
      this.isModify = true;
      this.formValidate = objCopy(this.tableData.rows[index], {});
    },
    exportData() {
      const self = this;
      self.$refs.tablesMain.exportCsv({
        filename: "刃磨信息列表",
        original: false
      });
    },
    checkColListSelectChange(item) {
      const self = this;
      let checkColList = this.checkColList;
      if (item !== undefined) {
        for (const checkCol of checkColList) {
          if (checkCol.pkId === item) {
            self.checkStandard = checkCol.checkStandard;
          }
        }
      }
    },
    handleAdd() {
      this.index++;
      this.formDynamic.items.push({
        value: "",
        index: this.index,
        status: 1
      });
    },
    handleRemove(index) {
      this.formDynamic.items[index].status = 0;
    },
    //form 校验方法
    handleSubmit(name) {
      this.$refs[name].validate(valid => {
        if (valid) {
          this.addNewtoolRepair();
        } else {
          this.$Message.error("请输入正确信息!");
        }
      });
    },
    handleReset(name) {
      this.toolCheckDetailModalshow = false;
      this.$refs[name].resetFields();
    }
  }
};
</script>

<style>
</style>
