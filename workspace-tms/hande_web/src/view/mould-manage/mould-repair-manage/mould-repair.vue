<style lang="less">
.fsize {
  font-size: 18px !important;
}
</style>
<template>
  <div>
    <Card>
      <Row>
        <Form ref="formInline" :model="searchCondition" :label-width="65" inline>
          <Col span="20">
            <FormItem prop="mouldBarcode" label="模具条码">
              <Input type="text" v-model="searchCondition.mouldBarcode" placeholder="请输入模具条码"></Input>
            </FormItem>
            <FormItem prop="mouldNumber" label="模具编码">
              <Input type="text" v-model="searchCondition.mouldNumber" placeholder="请输入模具编码"></Input>
            </FormItem>
            <FormItem>
              <Button type="primary" style="margin-left: -60px;" @click="doSearch('formInline')">搜索</Button>
            </FormItem>
            <FormItem style="margin-left: -65px;">
              <Button style type="dashed" @click="doReset('formInline')">重置</Button>
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

    <mould-repair-modal ref="mouldRepairModal" @search="doSearch"></mould-repair-modal>
  </div>
</template>

<script>
import axios from "@/libs/api.request";
import mouldRepairModal from "./mould-repair-modal.vue";
import { dateFormat, objCopy } from "@/libs/tools.js";

export default {
  components: {
    mouldRepairModal
  },
  data() {
    return {
      deleteModalShow: false, //控制删除modal提示 显示
      deleteRowData: {}, //行删除行数据
      isBatchDetele: false, //批量删除标志
      multiselectRowData: [], //复选列数据
      toolRepairDetailModalshow: false, //控制明细modal显示
      isModify: false, //是否是修改明细操作
      staffList: [],
      searchCondition: {
        mouldBarcode: "",
        mouldNumber: "",
        page: 1,
        rows: 10
      },
      tableData: {
        total: 0, // 总页数
        rows: [] // 每页条数
      },
      tableColumns: [
        {
          title: "模具编码",
          fixed: "left",
          minWidth: 150,
          key: "mouldNumber"
        },
        {
          title: "模具名称",
          fixed: "left",
          minWidth: 270,
          key: "mouldName"
        },
        {
          title: "模具图号",
          minWidth: 160,
          key: "mouldMap"
        },
        {
          title: "送修人",
          minWidth: 100,
          key: "userName"
        },
        {
          title: "送修时间",
          minWidth: 160,
          key: "receiveTime",
          render: function(h, params) {
            return h(
              "div",
              dateFormat(
                new Date(params.row.receiveTime),
                "yyyy-MM-dd HH:mm:ss"
              )
            );
          }
        },
        {
          title: "模具条码",
          minWidth: 260,
          key: "mouldBarcode"
        },
        {
          title: "已修磨次数",
          minWidth: 120,
          key: "repairTimes"
        },
        {
          title: "操作",
          key: "action",
          width: 120,
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
                    disabled: params.row.mouldStatus != 3,
                    size: "small"
                  },
                  style: {
                    marginRight: "5px"
                  },
                  on: {
                    click: () => {
                      this.addMouldRepair(params.row);
                    }
                  }
                },
                "修磨"
              )
            ]);
          }
        }
      ]
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
        mouldBarcode: self.searchCondition.mouldBarcode,
        mouldNumber: self.searchCondition.mouldNumber,
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
          url: "/mould/mould-repair-page-list",
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
      self.getListData();
    },
    addMouldRepair(row) {
      const self = this;
      self.$refs.mouldRepairModal.showModal(row);
    },
    exportData() {
      const self = this;
      self.$refs.tablesMain.exportCsv({
        filename: "待磨信息列表",
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
    }
  }
};
</script>

<style>
</style>
