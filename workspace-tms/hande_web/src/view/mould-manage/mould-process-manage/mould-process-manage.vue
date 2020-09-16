<template>
  <div>
    <Card>
      <Row>
        <Form ref="formInline" :model="searchCondition" :label-width="65" inline>
          <Col span="24">
            <FormItem prop="mouldBarcode" label="模具条码">
              <Input type="text" v-model="searchCondition.mouldBarcode" placeholder="请输入模具条码"></Input>
            </FormItem>
            <FormItem prop="mouldNumber" label="模具编码">
              <Input type="text" v-model="searchCondition.mouldNumber" placeholder="请输入模具编码"></Input>
            </FormItem>
            <FormItem label="使用班组" prop="useTeamId">
              <Select
                style="width:158px"
                v-model="searchCondition.useTeamId"
                ref="useTeam"
                clearable
                filterable
              >
                <Option
                  v-for="item in teamList"
                  :value="item.pkId"
                  :key="item.pkId"
                >{{ item.teamName }}</Option>
              </Select>
            </FormItem>
            <FormItem>
              <Button type="primary" style="margin-left:-60px" @click="doSearch('formInline')">搜索</Button>
            </FormItem>
            <FormItem style="margin-left:-65px">
              <Button style type="dashed" @click="doReset('formInline')">重置</Button>
            </FormItem>
          </Col>
        </Form>
      </Row>

      <Table ref="tablesMain" :data="tableData.rows" :columns="tableColumns" stripe border></Table>
      <div style="margin: 10px;overflow: hidden">
        <div style="float:right">
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

    <Modal v-model="mouldProcessDetailModalshow" title="模具交回" width="900" draggable>
      <Form
        ref="formValidate"
        :model="formValidate"
        :rules="ruleValidate"
        :label-width="100"
        inline
      >
        <Row>
          <Col span="8">
            <FormItem label="模具条码" prop="mouldBarcode">
              <Input style="width:170px" v-model="formValidate.mouldBarcode" readonly />
            </FormItem>
          </Col>
          <Col span="8">
            <FormItem label="模具编码" prop="mouldNumber">
              <Input style="width:170px" v-model="formValidate.mouldNumber" readonly />
            </FormItem>
          </Col>
          <Col span="8">
            <FormItem label="模具图号" prop="mouldMap">
              <Input style="width:170px" v-model="formValidate.mouldMap" readonly />
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span="8">
            <FormItem label="模具名称" prop="mouldName">
              <Input style="width:170px" v-model="formValidate.mouldName" readonly />
            </FormItem>
          </Col>
          <Col span="8">
            <FormItem label="使用部门" prop="useDepartmentName">
              <Input style="width:170px" v-model="formValidate.useDepartmentName" readonly />
            </FormItem>
          </Col>
          <Col span="8">
            <FormItem label="使用班组" prop="useTeamName">
              <Input style="width:170px" v-model="formValidate.useTeamName" readonly />
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span="8">
            <FormItem label="领用人" prop="userName">
              <Input style="width:170px" v-model="formValidate.userName" readonly />
            </FormItem>
          </Col>
          <Col span="16">
            <FormItem label="出库备注" prop="outRemark">
              <Input style="width:460px" v-model="formValidate.outRemark" readonly />
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span="8">
            <FormItem label="设备名称" prop="equipmentId">
              <Select
                style="width:170px"
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
          <Col span="8">
            <FormItem label="制件编号" prop="partId">
              <Select
                style="width:170px"
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
          <Col span="8">
            <FormItem label="生产数量" prop="processTotal">
              <InputNumber
                style="width:170px"
                :max="99999"
                :min="1"
                :step="1"
                v-model="formValidate.processTotal"
                placeholder="输入生产数量"
              />
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span="8">
            <FormItem label="交回原因" prop="returnResion">
              <Select
                style="width:170px"
                v-model="formValidate.returnResion"
                ref="returnResion"
                placeholder="请选择交回原因"
              >
                <Option
                  v-for="item in returnType"
                  :value="item.value"
                  :key="item.value"
                >{{ item.name }}</Option>
              </Select>
            </FormItem>
          </Col>
          <Col span="16">
            <FormItem label="异常说明" prop="remark">
              <Input style="width:460px" v-model="formValidate.remark" />
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
import mould_process_form_config from "./mould-process-form-config.js";
import returnType from "./return-resion.js";
import { dateFormat, objCopy } from "@/libs/tools.js";

export default {
  data() {
    return {
      isDisabled: false,
      unusualFlag: "",
      returnType: returnType,
      deleteRowData: {}, //行删除行数据
      mouldProcessDetailModalshow: false, //控制明细modal显示
      toolDisabled: "",
      coatDisabled: "",
      partList: [],
      teamList: [],
      equipmentList: [],
      searchCondition: {
        mouldNumber: "",
        mouldBarcode: "",
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
          minWidth: 290,
          key: "mouldName"
        },
        {
          title: "模具图号",
          minWidth: 180,
          key: "mouldMap"
        },
        {
          title: "领用部门",
          minWidth: 100,
          key: "useDepartmentName"
        },
        {
          title: "领用班组",
          minWidth: 100,
          key: "useTeamName"
        },
        {
          title: "领用人",
          minWidth: 100,
          key: "userName"
        },
        {
          title: "领用备注",
          minWidth: 120,
          key: "outRemark"
        },
        {
          title: "模具条码",
          minWidth: 280,
          key: "mouldBarcode"
        },
        {
          title: "已加工次数",
          minWidth: 100,
          key: "processTimes"
        },
        {
          title: "已加工数量",
          minWidth: 100,
          key: "processAmount"
        },
        {
          title: "操作",
          key: "action",
          minWidth: 120,
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
                    disabled: params.row.mouldStatus !== 2,
                    size: "small"
                  },
                  style: {
                    marginRight: "5px"
                  },
                  on: {
                    click: () => {
                      this.addProcess(params.index);
                    }
                  }
                },
                "交回"
              )
            ]);
          }
        }
      ],
      formValidate: mould_process_form_config.formValidate, //user form表单字段
      ruleValidate: mould_process_form_config.ruleValidate //user form表单验证规则
    };
  },
  created() {
    const self = this;
    self.getListData();
    self.searchAuditStaff();
    self.getTeamListData();
  },
  methods: {
    getTeamListData() {
      const self = this;
      let para = {
        departmentId: 6
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/base/team-list",
          isAuth: true,
          method: "post",
          params: para
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            self.teamList = data.data;
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },
    searchAuditStaff() {
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
              self.searchCondition.departmentId = data.data.departmentId;
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
    getListData() {
      const self = this;
      let para = {
        mouldNumber: self.searchCondition.mouldNumber,
        mouldBarcode: self.searchCondition.mouldBarcode,
        useTeamId: self.searchCondition.useTeamId,
        page: self.searchCondition.page,
        rows: self.searchCondition.rows
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/mould/mould-process-page-list",
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
    getPartListData(mouldNumber) {
      const self = this;
      let para = {
        mouldNumber: mouldNumber
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/mould/mould-part-get-by-number.json",
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
    addNewProcess() {
      this.isDisabled = true;
      let para = {
        mouldId: this.formValidate.pkId,
        fullNumber: this.formValidate.mouldBarcode,
        mouldNumber: this.formValidate.mouldNumber,
        mouldMap: this.formValidate.mouldMap,
        mouldName: this.formValidate.mouldName,
        warehouse: this.formValidate.warehouse,
        departmentId: this.formValidate.useDepartmentId,
        departmentName: this.formValidate.useDepartmentName,
        equipmentId: this.formValidate.equipmentId,
        equipmentCode: this.formValidate.equipmentCode,
        equipmentName: this.formValidate.equipmentName,
        partId: this.formValidate.partId,
        partCode: this.formValidate.partCode,
        partName: this.formValidate.partName,
        processAmount: this.formValidate.processTotal,
        returnResion: this.formValidate.returnResion,
        remark: this.formValidate.remark
      };
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/mould/mould-process-add",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          self.mouldProcessDetailModalshow = false;

          if (res.data.code === 200) {
            self.$Message.success("生产加工计数完成!");
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
    addProcess(index) {
      const self = this;
      self.mouldProcessDetailModalshow = true;
      self.formValidate = objCopy(self.tableData.rows[index], {});
      self.getEquipmentListData();
      self.getPartListData(self.formValidate.mouldNumber);
      self.formValidate.processTotal = 0;
      self.formValidate.remark = "";
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
    exportData() {
      const self = this;
    },
    //form 校验方法
    handleSubmit(name) {
      const self = this;
      self.$refs[name].validate(valid => {
        if (valid) {
          if (
            self.formValidate.returnResion == 2 &&
            !!!self.formValidate.remark
          ) {
            self.$Message.error("异常交回,请输入异常说明!");
            return;
          }
          self.addNewProcess();
        } else {
          self.$Message.error("请输入正确信息!");
        }
      });
    },
    handleReset(name) {
      this.mouldProcessDetailModalshow = false;
      this.$refs[name].resetFields();
    }
  }
};
</script>

<style>
</style>
