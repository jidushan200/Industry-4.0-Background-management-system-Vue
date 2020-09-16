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
          <Col>
            <FormItem prop="measureNumber" label="量具编码">
              <Input type="text" v-model="searchCondition.measureNumber" placeholder="请输入量具编码"></Input>
            </FormItem>
            <FormItem label="量具名称" prop="measureName">
              <Select
                style="width:158px"
                v-model="searchCondition.measureName"
                ref="measureName"
                placeholder="请选择量具名称"
                clearable
                filterable
              >
                <Icon type="ios-search" slot="suffix" />
                <Option
                  v-for="item in baseList"
                  :value="item.measureNumber"
                  :key="item.measureNumber"
                >{{ item.measureName }}</Option>
              </Select>
            </FormItem>
            <FormItem label="使用部门" prop="departmentId">
              <Select style="width:158px" v-model="searchCondition.departmentId" ref="department">
                <Option
                  v-for="item in departmentList"
                  :value="item.pkId"
                  :key="item.pkId"
                >{{ item.departmentName }}</Option>
              </Select>
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
    <Modal v-model="deleteModalShow" width="550" :title="isScrip?'量具报废':'量具退回'">
      <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="90">
        <Row>
          <Col span="24">
            <FormItem prop="measureMake" label="处理措施">
              <Input type="text" v-model="measureMake" readonly></Input>
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span="24">
            <FormItem prop="remark" label="不合格原因">
              <Input type="text" v-model="formValidate.remark" placeholder="请输入不合格原因"></Input>
            </FormItem>
          </Col>
        </Row>
      </Form>
      <div slot="footer">
        <Button
          @click="handleSubmit()"
          type="primary"
          style="margin-left: 8px"
          :disabled="isDisabled"
        >提交</Button>
        <Button @click="handleReset()" style="margin-left: 8px">关闭</Button>
      </div>
    </Modal>
    <check-quality-model ref="checkQualityModel" @search="doSearch"></check-quality-model>
  </div>
</template>

<script>
import axios from "@/libs/api.request";
import { dateFormat, objCopy } from "@/libs/tools.js";
import checkQualityModel from "./check-quality-model.vue";
import check_form_config from "./check-form-config.js";

export default {
  components: {
    checkQualityModel
  },
  data() {
    return {
      baseList: [],
      isDisabled: false,
      measureMake: "",
      deleteRowData: {},
      isScrip: false,
      departmentList: [],
      deleteModalShow: false,
      deleteContent: "",
      searchCondition: {
        measureName: "",
        measureNumber: "",
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
          minWidth: 150,
          key: "measureNumber"
        },
        {
          title: "顺序号",
          minWidth: 120,
          key: "measureSeq"
        },
        {
          title: "物料名称",
          minWidth: 240,
          key: "measureName"
        },
        {
          title: "物料图号",
          minWidth: 150,
          key: "model"
        },
        {
          title: "本厂计量编码",
          minWidth: 150,
          key: "factoryMetrology"
        },
        {
          title: "领用部门",
          minWidth: 150,
          key: "useDepartmentName"
        },
        {
          title: "领用班组",
          minWidth: 100,
          key: "useTeamName"
        },
        {
          title: "送检时间",
          minWidth: 120,
          key: "deliveryTime",
          render: function(h, params) {
            if (!!params.row.deliveryTime) {
              return h(
                "div",
                dateFormat(new Date(params.row.deliveryTime), "yyyy-MM-dd")
              );
            }
          }
        },
        {
          title: "供应商名称",
          minWidth: 250,
          key: "supplierName"
        },

        {
          title: "质检类型",
          minWidth: 150,
          key: "checkType",
          render: function(h, params) {
            let statusStr = "";
            switch (params.row.checkType) {
              case 1:
                statusStr = "新量具质检";
                break;
              case 2:
                statusStr = "周期定检";
                break;
            }
            return h("div", statusStr);
          }
        },
        {
          title: "操作",
          key: "action",
          minWidth: 200,
          align: "center",
          fixed: "right",
          render: (h, params) => {
            return h("div", [
              h(
                "Button",
                {
                  props: {
                    type: "success",
                    size: "small"
                  },
                  style: {
                    marginRight: "5px"
                  },
                  on: {
                    click: () => {
                      this.doQualified(params.index);
                    }
                  }
                },
                "合格"
              ),
              h(
                "Button",
                {
                  props: {
                    type: "error",
                    size: "small"
                  },
                  style: {
                    marginRight: "5px"
                  },
                  on: {
                    click: () => {
                      this.doUnqualified(params.row);
                    }
                  }
                },
                "不合格"
              )
            ]);
          }
        }
      ],
      formValidate: check_form_config.formValidate, //form表单字段
      ruleValidate: check_form_config.ruleValidate //form表单验证规则
    };
  },
  created() {
    const self = this;
    self.getListData();
    self.getBaseListData();
    self.getDepartmentListData();
  },
  methods: {
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
    getBaseListData() {
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/measure/measure-base-list",
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
      let measureNumber = null;
      if (self.searchCondition.measureName) {
        measureNumber = self.searchCondition.measureName;
      } else {
        measureNumber = self.searchCondition.measureNumber;
      }
      let para = {
        measureNumber: measureNumber,
        page: self.searchCondition.page,
        rows: self.searchCondition.rows,
        departmentId: self.searchCondition.departmentId,
        beginDate: self.searchCondition.beginDate,
        endDate: self.searchCondition.endDate
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/measure/measure-check-page-list",
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
    doQualified(index) {
      const self = this;
      self.$refs.checkQualityModel.showModal(self.tableData.rows[index]);
    },

    handleSubmit() {
      const self = this;
      self.$refs["formValidate"].validate(valid => {
        if (valid) {
          let para = {
            pkId: self.pkId,
            checkType: self.checkType,
            remark: self.formValidate.remark
          };
          self.isDisabled = true;
          axios
            .request({
              headers: {
                "Content-Type": "application/json; charset=UTF-8"
              },
              url: "/measure/measure-check-unqualified.json",
              method: "post",
              isAuth: true,
              params: para
            })
            .then(function(res) {
              const data = res.data;
              if (data.code === 200) {
                self.deleteModalShow = false;
                self.$Message.success("量具检验不合格，已处理!");
                self.getListData();
              }
              self.isDisabled = false;
            })
            .catch(function(err) {
              console.log(err);
            });
        } else {
          this.$Message.error("请输入正确信息!");
        }
      });
    },
    handleReset() {
      const self = this;
      self.deleteModalShow = false;
    },
    doUnqualified(row) {
      const self = this;
      self.deleteModalShow = true;
      self.formValidate = {};
      self.pkId = row.pkId;
      self.checkType = row.checkType;
      if (row.checkType === 1) {
        self.isScrip = false;
        self.measureMake = "新量具退回";
      } else if (row.checkType === 2) {
        self.isScrip = true;
        self.measureMake = "量具报废";
      }
    }
  }
};
</script>

<style>
</style>
