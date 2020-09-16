<template>
  <div>
    <Card>
      <Row>
        <Form ref="formInline" :model="searchCondition" :label-width="65" inline>
          <Col span="18">
          <FormItem label="物料编码" prop="toolNumber">
            <Input style="width:200px" v-model="searchCondition.toolNumber" />
          </FormItem>
          <FormItem>
            <Button type="primary" style="margin-left: -60px" @click="doSearch('formInline')">搜索</Button>
          </FormItem>
          <FormItem style="margin-left: -65px">
            <Button type="dashed" @click="doReset('formInline')">重置</Button>
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

    <Modal v-model="warehouseModalshow" title="刀具入库" width="700" draggable>
      <Form ref="formValidateOut" :model="formValidate" :rules="ruleValidate" :label-width="100" inline>
        <FormItem label="物料编号" prop="toolNumber">
          <Input style="width:200px" v-model="formValidate.toolNumber" readonly />
        </FormItem>
        <FormItem label="物料名称" prop="toolName">
          <Input style="width:200px" v-model="toolName" readonly />
        </FormItem>
        <FormItem label="物料图号" prop="toolMap">
          <Input style="width:200px" v-model="toolMap" readonly />
        </FormItem>
        <FormItem label="物料顺序号" prop="toolSeq">
          <Input style="width:200px" v-model="toolSeq" readonly />
        </FormItem>
        <FormItem label="物料条码" prop="fullNumber">
          <Input style="width:200px" v-model="fullNumber" readonly />
        </FormItem>
        <FormItem label="供应商" prop="supplierName">
          <Input style="width:200px" v-model="supplierName" readonly />
        </FormItem>
        <FormItem label="库位" prop="warehouse">
          <Input style="width:200px" v-model="formValidate.warehouse" />
        </FormItem>
        <FormItem label="库管员" prop="staffInfo">
          <Input style="width:200px" v-model="staffInfo" readonly />
        </FormItem>
        <FormItem label="备注" prop="remark">
          <Input style="width:520px" v-model="formValidate.remark" />
        </FormItem>
      </Form>
      <Table :columns="partTableColumns" :data="toolPartList" border stripe highlight-row height="160"></Table>
      <div slot="footer">
        <Button @click="handleSubmit()" type="primary" v-if="saveEnable" :disabled="isDisabled">确定</Button>
        <Button @click="handleReset()" style="margin-left: 8px">取消</Button>
      </div>
    </Modal>
  </div>
</template>

<script>
import axios from "@/libs/api.request";
import { getToken, getBaseUrl } from "@/libs/util";
import { dateFormat, objCopy } from "@/libs/tools.js";
import knife_lists_out_form_config from "./knife-lists-form-config.js";
export default {
  components: {},
  data() {
    return {
      isDisabled: false,
      fullNumber: "",
      toolNumber: "",
      toolName: "",
      toolMap: "",
      toolSeq: "",
      supplierName: "",
      staffInfo: "",
      toolPartList: [],
      saveEnable: false,
      warehouseModalshow: false,
      searchCondition: {
        toolNumber: "",
        page: 1,
        rows: 10
      },
      tableData: {
        total: 0, // 总页数
        rows: [] // 每页条数
      },
      partTableColumns: [
        {
          title: "制件编码",
          key: "partCode"
        },
        {
          title: "制件名称",
          key: "partName"
        }
      ],
      tableColumns: [
        {
          title: "物料编码",
          key: "toolNumber",
          minWidth: 150,
          fixed: "left"
        },
        {
          title: "物料顺序号",
          key: "toolSeq",
          minWidth: 100
        },
        {
          title: "物料名称",
          key: "toolName",
          minWidth: 200
        },
        {
          title: "物料图号",
          key: "toolMap",
          minWidth: 180
        },

        {
          title: "申请部门",
          key: "departmentName",
          minWidth: 120
        },
        {
          title: "申请人",
          key: "applierName",
          minWidth: 100
        },
        {
          title: "供应商",
          key: "supplierName",
          minWidth: 280
        },
        {
          title: "送货时间",
          key: "deliveryTime",
          minWidth: 120,
          render: function(h, params) {
            return h(
              "div",
              dateFormat(new Date(params.row.deliveryTime), "yyyy-MM-dd")
            );
          }
        },
        {
          title: "操作",
          key: "action",
          width: 150,
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
                      this.warehouseShow(params.row);
                    }
                  }
                },
                "入库"
              )
            ]);
          }
        }
      ],
      formValidate: knife_lists_out_form_config.formValidate, // form表单字段
      ruleValidate: knife_lists_out_form_config.ruleValidate // form表单验证规则
    };
  },
  created() {
    const self = this;
    self.getListData();
    self.searchCurrentStaff();
  },
  methods: {
    getListData() {
      const self = this;
      let para = {
        toolNumber: self.searchCondition.toolNumber,
        typeId: 1,
        page: self.searchCondition.page,
        rows: self.searchCondition.rows
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/wait-warehouse-page-list",
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
    searchCurrentStaff() {
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/base/staff-get-current.json",
          method: "post",
          isAuth: true
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            if (data.data !== null && data.data !== undefined) {
              self.departmentId = data.data.departmentId;
              self.departmentName = data.data.departmentName;
              self.staffInfo =
                data.data.departmentName + "-" + data.data.staffName;
            } else {
              self.$Message.error("无法找到与职工号对应的职工");
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

    doSearch(name) {
      const self = this;
      self.changePage(1);
    },
    doReset(name) {
      const self = this;
      self.$refs[name].resetFields();
      self.getListData();
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

    searchTool(toolNumber) {
      const self = this;
      let para = {
        toolNumber: toolNumber
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/tool-get-by-tool-number.json",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            if (!!data.data.toolMap) {
              self.typeId = data.data.typeId;
              self.toolPartList = data.data.toolPartList;
              if (undefined == data.data.grindingCordon) {
                self.$Message.error("请先维护刃磨预警信息");
              } else {
                self.formValidate.repairCordon = data.data.grindingCordon;
                self.saveEnable = true;
              }
            } else {
              self.$Message.error("请先维护物料图号");
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

    warehouseShow(row) {
      const self = this;
      self.saveEnable = false;
      self.warehouseModalshow = true;
      self.handleId = row.pkId;
      self.toolName = row.toolName;
      self.toolMap = row.toolMap;
      self.toolSeq = row.toolSeq;
      self.supplierId = row.supplierId;
      self.supplierName = row.supplierName;
      self.deliever = row.deliever;
      self.formValidate.toolNumber = row.toolNumber;
      self.toolNumber = row.toolNumber;
      self.formValidate.warehouseCode = row.toolNumber + "-" + row.toolSeq;
      self.fullNumber = self.formValidate.warehouseCode + "/" + row.toolMap;
      self.searchTool(row.toolNumber);
      self.formValidate.warehouse = "";
    },
    handleReset() {
      const self = this;
      self.warehouseModalshow = false;
    },
    //form 校验方法
    handleSubmit() {
      const self = this;
      if (!self.formValidate.repairCordon) {
        self.$Message.error("请先维护刃磨预警信息");
      } else {
        this.isDisabled = true;
        let para = {
          handleId: self.handleId,
          repairCordon: self.formValidate.repairCordon,
          toolNumber: self.formValidate.toolNumber,
          warehouseCode: self.formValidate.warehouseCode,
          toolMap: self.toolMap,
          toolName: self.toolName,
          fullNumber: self.fullNumber,
          typeId: self.typeId,
          supplierId: self.supplierId,
          supplierName: self.supplierName,
          warehouse: self.formValidate.warehouse,
          deliever: self.formValidate.deliever,
          remark: self.formValidate.remark
        };
        axios
          .request({
            headers: {
              "Content-Type": "application/json; charset=UTF-8"
            },
            url: "/tool/tool-warehouse",
            method: "post",
            isAuth: true,
            params: para
          })
          .then(function(res) {
            self.warehouseModalshow = false;
            if (res.data.code === 200) {
              self.$Message.success("刀具入库成功!");
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
      }
    }
  }
};
</script>

<style>
</style>
