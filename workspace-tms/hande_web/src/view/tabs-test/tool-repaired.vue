<template>
  <div>
      <Row >
        <Form ref="formInline" :model="searchCondition" :label-width="65" inline>
          <Col span="20">
            <FormItem prop="toolNumber" label="刀具编码">
              <Input type="text" v-model="searchCondition.toolNumber" placeholder="请输入刀具编码">
              </Input>
            </FormItem>
            <FormItem prop="toolName" label="刀具名称">
              <Input type="text" v-model="searchCondition.toolName" placeholder="请输入刀具名称">
              </Input>
            </FormItem>
            <FormItem prop="toolType" label="刀具类型">
              <Select style="width:158px" v-model="searchCondition.toolType" ref="toolType" placeholder="请选择刀具类型">
                <Option v-for="item in toolType" :value="item.value" :key="item.value">{{ item.name }}</Option>
              </Select>
            </FormItem>
              <FormItem prop="dateInterval" label="时间区间">
                <DatePicker type="daterange" split-panels placeholder="请选择时间区间" placement="bottom-end" v-model="searchCondition.dateInterval"></DatePicker>
              </FormItem>
            <FormItem>
              <Button type="primary" style="margin-left: -60px;" @click="doSearch('formInline')">搜索</Button>
            </FormItem>
            <FormItem style="margin-left: -65px;">
              <Button style="" type="dashed" @click="doReset('formInline')">重置</Button>
            </FormItem>
          </Col>
          <Col span="4">
          <FormItem style="width:100%;text-align:right">
              <Button type="warning" @click="exportData()"><Icon type="ios-download-outline"></Icon>导出</Button>
          </FormItem>
          </Col>
        </Form>
      </Row>

      <Table ref="tablesMain" :data="tableData.rows" :columns="tableColumns" stripe border></Table>

      <div style="margin: 10px;overflow: hidden">
        <div style="float: right;">
          <Page show-total :total="tableData.total" :page-size="searchCondition.rows" :current="searchCondition.page" @on-change="changePage" @on-page-size-change="handlePageSize" show-sizer></Page>
        </div>
      </div>
    

    <Modal v-model="toCheckModalShow" width="300" title="刀具送检确认" @on-ok="toCheckModalSureBtn"
      @on-cancel="toCheckModalCancelBtn">
      <p>确定刀具已完成刃磨，送检刃磨刀具？</p>
    </Modal>
  </div>
</template>

<script>
import axios from "@/libs/api.request";
import tool_repair_manage_form_config from "./tool-repair-form-config.js";
import { dateFormat, objCopy } from "@/libs/tools.js";
import toolType from "./tool-type.js";

export default {
  data() {
    return {
      deleteRowData: {}, //行删除行数据
      isBatchDetele: false, //批量删除标志
      multiselectRowData: [], //复选列数据
      toCheckModalShow: false, //控制送检modal显示
      isModify: false, //是否是修改明细操作
      toolType: toolType,
      searchCondition: {
        toolName: "",
        toolNumber: "",
        toolType: "",
        dateInterval: [],
        toolState: 4,
        page: 1,
        rows: 10
      },
      tableData: {
        total: 0, // 总页数
        rows: [] // 每页条数
      },
      tableColumns: [
        {
          title: "刀具码",
          key: "fullNumber"
        },
        {
          title: "刀具编码",
          key: "toolNumber"
        },
        {
          title: "刀具名称",
          key: "toolName"
        },
        {
          title: "最大刃磨量",
          key: "grindingMax"
        },
        {
          title: "刃磨次数",
          key: "repairTimes"
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
                    disabled: params.row.toolState != 4,
                    size: "small"
                  },
                  style: {
                    marginRight: "5px"
                  },
                  on: {
                    click: () => {
                      this.toCheck(params.index);
                    }
                  }
                },
                "送检"
              )
            ]);
          }
        }
      ],
      formValidate: tool_repair_manage_form_config.formValidate, //user form表单字段
      ruleValidate: tool_repair_manage_form_config.ruleValidate //user form表单验证规则
    };
  },
  created() {
    const self = this;
    self.getListData();
  },
  methods: {
    getListData() {
      const self = this;
      if (!!self.searchCondition.dateInterval) {
        let beginDate = self.searchCondition.dateInterval[0];
        let endDate = self.searchCondition.dateInterval[1];
        if (!!beginDate) {
          self.searchCondition.beginDate = dateFormat(
            new Date(beginDate),
            "yyyy-MM-dd"
          );
        } else {
          self.searchCondition.beginDate = null;
        }
        if (!!endDate) {
          self.searchCondition.endDate = dateFormat(
            new Date(endDate),
            "yyyy-MM-dd"
          );
        } else {
          self.searchCondition.endDate = null;
        }
      } else {
        self.searchCondition.beginDate = null;
        self.searchCondition.endDate = null;
      }
      let para = {
        toolName: self.searchCondition.toolName,
        toolNumber: self.searchCondition.toolNumber,
        toolType: self.searchCondition.toolType,
        page: self.searchCondition.page,
        rows: self.searchCondition.rows,
        beginDate: self.searchCondition.beginDate,
        endDate: self.searchCondition.endDate
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/tool-page-list",
          method: "post",
          isAuth: true,
          params: para
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
    toCheckRowDate(pkId) {
      const self = this;
      let para = {
        pkId: pkId,
        coatFlag: 2,
        toolState: 7
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/tool-state-update",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            self.toCheckModalShow = false;
            self.$Message.success("刀具已成功送检!");
            self.getListData();
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },
    toCheck(index) {
      this.toCheckRowData = this.tableData.rows[index];
      this.toCheckModalShow = true;
    },
    toCheckModalSureBtn() {
      this.toCheckRowDate(this.toCheckRowData.pkId);
      this.toCheckModalShow = false;
    },
    //删除modal 取消方法
    toCheckModalCancelBtn() {
      this.toCheckRowData = {};
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
      self.getListData();
    },
    doReset(name) {
      const self = this;
      self.$refs[name].resetFields();
    },
    exportData() {
      const self = this;
      self.$refs.tablesMain.exportCsv({
        filename: "待涂层刀具列表",
        original: false
      });
    }
  }
};
</script>

<style>
</style>
