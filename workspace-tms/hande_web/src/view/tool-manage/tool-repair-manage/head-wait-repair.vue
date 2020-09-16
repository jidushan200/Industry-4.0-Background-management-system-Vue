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
            <FormItem prop="fullNumber" label="物料条码">
              <Input
                type="text"
                ref="fullNumberFocus"
                v-model="searchCondition.fullNumber"
                placeholder="请输入物料条码"
              ></Input>
            </FormItem>
            <!--
            <FormItem prop="warehouseCode" label="入库编码">
              <Input type="text" v-model="searchCondition.warehouseCode" placeholder="请输入入库编码"></Input>
            </FormItem>
            -->
            <FormItem prop="toolNumber" label="物料编码">
              <Input type="text" v-model="searchCondition.toolNumber" placeholder="请输入物料编码"></Input>
            </FormItem>
            <FormItem>
              <Button type="primary" style="margin-left: -60px;" @click="doSearch('formInline')">搜索</Button>
            </FormItem>
            <FormItem style="margin-left: -65px;">
              <Button style type="dashed" @click="doReset('formInline')">重置</Button>
            </FormItem>
          </Col>
          <!-- <Col span="4">
          <FormItem style="width:100%;text-align:right">
            <Button type="warning" style="margin-left: 37px;" @click="exportData()">
              <Icon type="ios-download-outline"></Icon>导出
            </Button>
          </FormItem>
          </Col>-->
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

    <Modal v-model="headRepairModalshow" title="刀头刃磨信息录入" width="700" draggable>
      <Form
        ref="formValidate"
        :model="formValidate"
        :rules="ruleValidate"
        :label-width="100"
        inline
      >
        <Row>
          <Col span="12">
            <FormItem label="物料条码" prop="fullNumber">
              <Input style="width:530px" v-model="formValidate.fullNumber" readonly />
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span="12">
            <FormItem label="物料名称" prop="toolName">
              <Input style="width:200px" v-model="formValidate.toolName" readonly />
            </FormItem>
          </Col>
          <Col span="12">
            <FormItem label="物料图号" prop="toolMap">
              <Input style="width:200px" v-model="formValidate.toolMap" readonly />
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span="24">
            <FormItem label="使用人职工号" prop="staffCode">
              <Input
                style="width:200px"
                v-model="formValidate.staffCode"
                placeholder="输入领用人职工号"
                @on-blur="searchStaff()"
              >
                <Icon type="ios-search" slot="suffix" />
              </Input>&nbsp;
            </FormItem>
            <Input style="width:200px" v-model="staffInfo" readonly />
          </Col>
        </Row>
        <Row>
          <Col span="12">
            <FormItem label="设备名称" prop="equipmentId">
              <Select
                style="width:200px"
                v-model="formValidate.equipmentId"
                ref="equipmentId"
                @on-change="equipmentListSelectChange"
                clearable
                filterable
              >
                <Option
                  v-for="item in equipmentList"
                  :value="item.pkId"
                  :key="item.pkId"
                >{{ item.tagNumber }}-{{ item.equipmentName }}</Option>
              </Select>
            </FormItem>
          </Col>
          <Col span="12">
            <FormItem label="制件编号" prop="partId">
              <Select
                style="width:200px"
                v-model="formValidate.partId"
                ref="part"
                @on-change="partListSelectChange"
                clearable
                filterable
              >
                <Option
                  v-for="item in partList"
                  :value="item.pkId"
                  :key="item.pkId"
                >{{ item.partCode }}-{{ item.partName }}</Option>
              </Select>
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span="12">
            <FormItem label="生产数量" prop="processTotal">
              <InputNumber
                style="width:200px"
                :max="99999"
                :min="1"
                :step="1"
                v-model="formValidate.processTotal"
                placeholder="输入生产数量"
              />
            </FormItem>
          </Col>
          <Col span="12">
            <FormItem label="本次刃磨量" prop="repairMeasure">
              <InputNumber
                :max="99"
                :min="0.01"
                :step="0.01"
                style="width:200px"
                v-model="formValidate.repairMeasure"
                placeholder="输入本次刃磨量"
              />
            </FormItem>
          </Col>
          <Col span="24">
            <FormItem label="备注" prop="remark">
              <Input style="width:540px" v-model="formValidate.remark" placeholder="输入备注" />
            </FormItem>
          </Col>
        </Row>
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

export default {
  data() {
    return {
      isDisabled: false,
      headRepairModalshow: false,
      staffInfo: "",
      equipmentList: [],
      partList: [],
      searchCondition: {
        warehouseCode: "",
        fullNumber: "",
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
          fixed: "left",
          minWidth: 200,
          key: "toolNumber"
        },
        {
          title: "物料名称",
          fixed: "left",
          minWidth: 200,
          key: "toolName"
        },
        {
          title: "入库编码",
          fixed: "left",
          minWidth: 200,
          key: "warehouseCode"
        },
        {
          title: "物料图号",
          minWidth: 200,
          key: "toolMap"
        },
        {
          title: "物料条码",
          width: 250,
          key: "fullNumber"
        },
        {
          title: "最大刃磨量",
          minWidth: 120,
          key: "grindingMax"
        },
        {
          title: "刃磨次数",
          minWidth: 120,
          key: "repairTimes"
        },
        {
          title: "累计刃磨量",
          minWidth: 120,
          key: "repairAmount"
        },
        {
          title: "出库时间",
          key: "outboundTime",
          width: 150,
          render: function(h, params) {
            return h(
              "div",
              dateFormat(new Date(params.row.updateTime), "yyyy-MM-dd HH:mm:ss")
            );
          }
        },
        {
          title: "责任人",
          minWidth: 120,
          key: "grinderName"
        },
        {
          title: "责任部门",
          minWidth: 120,
          key: "staffDepartmentName"
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
                    disabled: params.row.toolState != 3,
                    size: "small"
                  },
                  style: {
                    marginRight: "5px"
                  },
                  on: {
                    click: () => {
                      this.addToolRepair(params.row);
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
    setTimeout(function() {
      self.$refs["fullNumberFocus"].focus();
    }, 200);
  },
  methods: {
    getListData() {
      const self = this;
      let para = {
        warehouseCode: self.searchCondition.warehouseCode,
        fullNumber: self.searchCondition.fullNumber,
        toolNumber: self.searchCondition.toolNumber,
        page: self.searchCondition.page,
        rows: self.searchCondition.rows,
        toolState: 3,
        typeId: 3
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/tool-wait-repair-page-list",
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

    searchStaff() {
      const self = this;
      if (!!!self.formValidate.staffCode) {
        return;
      }
      let para = {
        staffCode: self.formValidate.staffCode
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/base/staff-get-by-code.json",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            if (data.data !== null && data.data !== undefined) {
              self.formValidate.departmentId = data.data.departmentId;
              self.formValidate.departmentName = data.data.departmentName;
              self.formValidate.teamId = data.data.teamId;
              self.formValidate.teamName = data.data.teamName;
              self.formValidate.staffName = data.data.staffName;
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

    getPartListData(toolNumber) {
      const self = this;
      let para = {
        toolNumber: toolNumber
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/base/toolpart-get-by-number.json",
          isAuth: true,
          method: "post",
          params: para
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
    getEquipmentListData() {
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/base/get-equipment-list",
          isAuth: true,
          method: "post",
          params: []
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            self.equipmentList = data.data;
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },
    partListSelectChange(item) {
      let partList = this.partList;
      if (item !== undefined) {
        for (const part of partList) {
          if (part.pkId === item) {
            this.formValidate.partName = part.partName;
            this.formValidate.partCode = part.partCode;
          }
        }
      }
    },
    equipmentListSelectChange(item) {
      let equipmentList = this.equipmentList;
      if (item !== undefined) {
        for (const equipment of equipmentList) {
          if (equipment.pkId === item) {
            this.formValidate.equipmentCode = equipment.equipmentCode;
            this.formValidate.equipmentName = equipment.equipmentName;
          }
        }
      }
    },

    addNewProcess() {
      this.isDisabled = true;
      const self = this;
      let para = {
        toolId: self.formValidate.pkId,
        fullNumber: self.formValidate.fullNumber,
        toolNumber: self.formValidate.toolNumber,
        toolMap: self.formValidate.toolMap,
        toolName: self.formValidate.toolName,
        departmentId: self.formValidate.departmentId,
        departmentName: self.formValidate.departmentName,
        staffCode: self.formValidate.staffCode,
        staffName: self.formValidate.staffName,
        teamId: self.formValidate.teamId,
        teamName: self.formValidate.teamName,
        equipmentId: self.formValidate.equipmentId,
        equipmentCode: self.formValidate.equipmentCode,
        equipmentName: self.formValidate.equipmentName,
        partId: self.formValidate.partId,
        partCode: self.formValidate.partCode,
        partName: self.formValidate.partName,
        processAmount: self.formValidate.processTotal,
        returnResion: 1,
        repairMeasure: self.formValidate.repairMeasure,
        remark: self.formValidate.remark
      };

      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/tool-process-add",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          self.headRepairModalshow = false;
          if (res.data.code === 200) {
            self.$Message.success("生产加工计数修磨完成!");
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
    addToolRepair(row) {
      const self = this;
      self.typeId = row.typeId;
      self.headRepairModalshow = true;
      self.getPartListData(row.toolNumber);
      self.getEquipmentListData();
      self.formValidate = objCopy(row, {});
      self.formValidate.repairMeasure = 0.01;
      self.formValidate.departmentName = row.staffDepartmentName;
    },
    exportData() {
      const self = this;
      self.$refs.tablesMain.exportCsv({
        filename: "待磨信息列表",
        original: false
      });
    },

    //form 校验方法
    handleSubmit(name) {
      const self = this;
      self.$refs[name].validate(valid => {
        if (valid) {
          self.addNewProcess();
        } else {
          this.$Message.error("请输入正确信息!");
        }
      });
    },
    handleReset(name) {
      const self = this;
      self.headRepairModalshow = false;
      self.$refs[name].resetFields();
    }
  }
};
</script>

<style>
</style>
