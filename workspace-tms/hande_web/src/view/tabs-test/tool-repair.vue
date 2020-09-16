<template>
  <div class="toolRepair">

    <Card>
      <Row>
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
            <Button type="warning" style="margin-left: 37px;" @click="exportData()">
              <Icon type="ios-download-outline"></Icon>导出
            </Button>
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
    </Card>

    <Modal v-model="toolRepairDetailModalshow" title="刃磨信息录入" width="600" draggable>
      <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="100" inline>
        <FormItem label="刃磨人" prop="executorId">
          <Select style="width:170px" v-model="formValidate.executorId" ref="executorId" @on-change="staffListSelectChange">
            <Option v-for="item in staffList" :value="item.pkId" :key="item.pkId">{{ item.staffName }}</Option>
          </Select>
        </FormItem>
        <FormItem label="刃磨量" prop="repairMeasure">
          <InputNumber :max="99" :min="0.01" :step="0.01" style="width:170px" v-model="formValidate.repairMeasure" placeholder="输入刃磨量" />
        </FormItem>
        <FormItem label="备注" prop="remark">
          <Input style="width:170px" v-model="formValidate.remark" placeholder="输入备注" />
        </FormItem>
      </Form>
      <div slot="footer">
        <Button @click="handleSubmit('formValidate')" type="primary" :disabled="isDisabled">保存</Button>
        <Button @click="handleReset('formValidate')" style="margin-left: 8px">取消</Button>
      </div>
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
      isDisabled: false,
      deleteModalShow: false, //控制删除modal提示 显示
      deleteRowData: {}, //行删除行数据
      isBatchDetele: false, //批量删除标志
      multiselectRowData: [], //复选列数据
      toolRepairDetailModalshow: false, //控制明细modal显示
      isModify: false, //是否是修改明细操作
      toolType: toolType,
      staffList: [],
      searchCondition: {
        toolName: "",
        toolNumber: "",
        toolType: "",
        dateInterval: [],
        toolState: 3,
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
                    disabled: params.row.toolState != 3,
                    size: "small"
                  },
                  style: {
                    marginRight: "5px"
                  },
                  on: {
                    click: () => {
                      this.addToolRepair(params.index);
                    }
                  }
                },
                "刃磨"
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
    self.getStaffListData();
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
    getStaffListData() {
      const self = this;
      let para = {
        departmentId: 450
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/base/staff-list",
          isAuth: true,
          method: "post",
          params: para
        })
        .then(function(res) {
          const data = res.data;

          if (data.code === 200) {
            self.staffList = data.data;
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },
    addNewtoolRepair() {
      this.isDisabled = true;
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
          self.toolRepairDetailModalshow = false;
          if (res.data.code === 200) {
            self.$Message.success("刀具刃磨信息已记录!");
            self.isDisabled = false;
            self.getListData();
          } else {
            this.$Notice.error({
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
      this.formValidate = {};
    },
    doReset(name) {
      const self = this;
      self.$refs[name].resetFields();
    },
    addToolRepair(index) {
      this.toolRepairDetailModalshow = true;
      this.isModify = true;
      this.formValidate = objCopy(this.tableData.rows[index], {});
      this.formValidate.repairMeasure = 0.01;
    },
    exportData() {
      const self = this;
      self.$refs.tablesMain.exportCsv({
        filename: "刃磨信息列表",
        original: false
      });
    },
    staffListSelectChange(item) {
      let staffList = this.staffList;
      if (item !== undefined) {
        for (const staff of staffList) {
          if (staff.pkId === item) {
            this.formValidate.executor = staff.staffName;
          }
        }
      }
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
      this.toolRepairDetailModalshow = false;
      this.$refs[name].resetFields();
    }
  }
};
</script>

<style>
</style>
