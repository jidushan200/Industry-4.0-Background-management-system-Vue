<template>
  <div>
    <Card>
      <Row>
        <Form ref="formInline" :model="searchCondition" :label-width="65" inline>
          <Col span="24">
            <FormItem prop="fullNumber" label="物料条码">
              <Input type="text" v-model="searchCondition.fullNumber" placeholder="请输入物料条码"></Input>
            </FormItem>
            <FormItem prop="number" label="物料编码">
              <Input type="text" v-model="searchCondition.number" placeholder="请输入物料编码"></Input>
            </FormItem>
            <FormItem prop="dateInterval" label="时间区间">
              <DatePicker
                type="daterange"
                placeholder="请选择时间区间"
                v-model="searchCondition.dateInterval"
                style="width:180px;"
              ></DatePicker>
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

    <Modal v-model="toolProcessDetailModalshow" title="刀具交回" width="900" draggable>
      <Form
        ref="formValidate"
        :model="formValidate"
        :rules="ruleValidate"
        :label-width="100"
        inline
      >
        <Row>
          <Col span="8">
            <FormItem label="物料条码" prop="fullNumber">
              <Input
                style="width:170px"
                v-model="formValidate.fullNumber"
                placeholder="输入物料条码"
                readonly
              />
            </FormItem>
          </Col>
          <Col span="8">
            <FormItem label="物料编码" prop="toolNumber">
              <Input style="width:170px" v-model="formValidate.toolNumber" readonly />
            </FormItem>
          </Col>
          <Col span="8">
            <FormItem label="物料图号" prop="toolMap">
              <Input style="width:170px" v-model="formValidate.toolMap" readonly />
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span="8">
            <FormItem label="物料名称" prop="toolName">
              <Input style="width:170px" v-model="formValidate.toolName" readonly />
            </FormItem>
          </Col>
          <Col span="8">
            <FormItem label="使用部门" prop="departmentName">
              <Input style="width:170px" v-model="formValidate.departmentName" readonly />
            </FormItem>
          </Col>
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
        </Row>
        <Row>
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
            <FormItem label="换刀标准加工数" prop="processEach">
              <Input style="width:170px" v-model="processEach" readonly />
            </FormItem>
          </Col>
          <Col span="8">
            <FormItem label="换刀最小加工数" prop="processEachMin">
              <Input style="width:170px" v-model="processEachMin" readonly />
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span="8">
            <FormItem label="换刀最大加工数" prop="processEachMax">
              <Input style="width:170px" v-model="processEachMax" readonly />
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
            <FormItem label="说明" prop="remark">
              <Input style="width:748px" v-model="formValidate.remark" />
            </FormItem>
          </Col>
        </Row>
      </Form>
      <div slot="footer">
        <Button @click="handleSubmit('formValidate')" type="primary" :disabled="isDisabled">保存</Button>
        <Button @click="handleReset('formValidate')" style="margin-left: 8px">取消</Button>
      </div>
    </Modal>
    <Modal v-model="returnModalShow" width="250" title="刀具还回" @on-ok="returnModalSureBtn">
      <p>确认无加工数量并还回？</p>
    </Modal>
  </div>
</template>

<script>
import axios from "@/libs/api.request";
import tool_process_form_config from "./tool-process-form-config.js";
import returnType from "./return-resion.js";
import { dateFormat, objCopy } from "@/libs/tools.js";

export default {
  data() {
    return {
      isDisabled: false,
      returnType: returnType,
      deleteRowData: {}, //行删除行数据
      toolProcessDetailModalshow: false, //控制明细modal显示
      toolDisabled: "",
      coatDisabled: "",
      partList: [],
      processEach: 0,
      returnModalShow: false,
      processEachMin: 0,
      processEachMax: 0,
      equipmentList: [],
      searchCondition: {
        number: "",
        toolNumber: "",
        fullNumber: "",
        dateInterval: [],
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
          minWidth: 150,
          key: "toolNumber"
        },
        {
          title: "入库编码",
          fixed: "left",
          minWidth: 180,
          key: "warehouseCode"
        },
        {
          title: "物料名称",
          fixed: "left",
          minWidth: 250,
          key: "toolName"
        },
        {
          title: "物料图号",
          minWidth: 180,
          key: "toolMap"
        },
        {
          title: "领用部门",
          minWidth: 200,
          key: "departmentName"
        },
        {
          title: "物料条码",
          minWidth: 260,
          key: "fullNumber"
        },
        {
          title: "已加工次数",
          minWidth: 120,
          key: "processTimes"
        },
        {
          title: "已加工数量",
          minWidth: 120,
          key: "processAmount"
        },
        {
          title: "操作",
          key: "action",
          minWidth: 160,
          align: "center",
          fixed: "right",
          render: (h, params) => {
            return h("div", [
              h(
                "Button",
                {
                  props: {
                    type: "primary",
                    disabled: params.row.toolState !== 2,
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
              ),
              h(
                "Button",
                {
                  props: {
                    type: "warning",
                    disabled: params.row.toolState !== 2,
                    size: "small"
                  },
                  style: {
                    marginRight: "5px"
                  },
                  on: {
                    click: () => {
                      this.returnTool(params.row.fullNumber);
                    }
                  }
                },
                "还回"
              )
            ]);
          }
        }
      ],
      formValidate: tool_process_form_config.formValidate, //user form表单字段
      ruleValidate: tool_process_form_config.ruleValidate //user form表单验证规则
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
        toolNumber: self.searchCondition.toolNumber,
        fullNumber: self.searchCondition.fullNumber,
        number: self.searchCondition.number,
        page: self.searchCondition.page,
        rows: self.searchCondition.rows,
        beginDate: self.searchCondition.beginDate,
        endDate: self.searchCondition.endDate,
        toolState: 2
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
    getToolBaseData(toolNumber) {
      const self = this;
      let para = {
        toolNumber: toolNumber
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/tool-base-get-by-number",
          isAuth: true,
          method: "post",
          params: para
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            self.processEach = data.data.processEach;
            self.processEachMin = data.data.processEachMin;
            self.processEachMax = data.data.processEachMax;
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },
    addNewProcess() {
      const self = this;
      self.isDisabled = true;
      let para = {
        toolId: self.formValidate.pkId,
        fullNumber: self.formValidate.fullNumber,
        toolNumber: self.formValidate.toolNumber,
        toolMap: self.formValidate.toolMap,
        toolName: self.formValidate.toolName,
        departmentId: self.formValidate.departmentId,
        departmentName: self.formValidate.departmentName,
        equipmentCode: self.formValidate.equipmentCode,
        equipmentName: self.formValidate.equipmentName,
        partCode: self.formValidate.partCode,
        partName: self.formValidate.partName,
        processAmount: self.formValidate.processTotal,
        returnResion: self.formValidate.returnResion,
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
          self.toolProcessDetailModalshow = false;
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

    returnTool(fullNumber) {
      const self = this;
      self.returnModalShow = true;
      self.fullNumber = fullNumber;
    },
    returnModalSureBtn() {
      const self = this;
      let para = {
        fullNumber: self.fullNumber
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/no-process-return",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          self.returnModalShow = false;
          if (res.data.code === 200) {
            self.$Message.success("刀具还回成功!");
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
      self.toolProcessDetailModalshow = true;
      self.formValidate = objCopy(self.tableData.rows[index], {});
      self.getPartListData(self.formValidate.toolNumber);
      self.formValidate.processTotal = 0;
      self.getEquipmentListData(this.formValidate.departmentId);
      self.processEach = 0;
      self.processEachMin = 0;
      self.processEachMax = 0;
      self.getToolBaseData(self.formValidate.toolNumber);
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

    //form 校验方法
    handleSubmit(name) {
      const self = this;
      self.$refs[name].validate(valid => {
        if (valid) {
          if (!!!self.formValidate.remark) {
            if (self.formValidate.returnResion == 2) {
              self.$Message.error("异常交回,说明不能为空!");
              return;
            }
            if (self.formValidate.processTotal < self.processEachMin) {
              self.$Message.error("加工数量小于换刀最小加工数量,说明不能为空!");
              return;
            }
          }
          self.isDisabled = true;
          self.addNewProcess();
        } else {
          self.$Message.error("请输入正确信息!");
        }
      });
    },
    handleReset(name) {
      this.toolProcessDetailModalshow = false;
      this.$refs[name].resetFields();
    }
  }
};
</script>

<style>
</style>
