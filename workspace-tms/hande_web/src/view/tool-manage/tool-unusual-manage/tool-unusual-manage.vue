<template>
  <div>
    <Card>
      <Row>
        <Form ref="formInline" :model="searchCondition" :label-width="65" inline>
          <Col span="18">
          <FormItem prop="toolNumber" label="物料编码">
            <Input type="text" v-model="searchCondition.toolNumber" placeholder="请输入物料编码">
            </Input>
          </FormItem>
          <FormItem prop="fullNumber" label="物料条码">
            <Input type="text" v-model="searchCondition.fullNumber" placeholder="请输入物料条码">
            </Input>
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
          <Col span="6">
          <FormItem style="width:100%;text-align:right">
            <!-- <Upload :show-upload-list="false" action="http://127.0.0.1:8088/tool/tool-unusual-import.json" :on-success="handleSuccess" :on-error="handleError" style="display: inline-block;margin-right: 5px;">
              <Button icon="ios-cloud-upload-outline">批量导入</Button>
            </Upload> -->
            <Button type="success" style="margin-right: 5px" @click="addToolUnusuall">
              <Icon type="md-add"></Icon>添加
            </Button>
            <!-- <Button type="warning" @click="exportData()"><Icon type="ios-download-outline"></Icon>导出</Button> -->
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

    <Modal v-model="toolUnusuallDetailModalshow" :title="isModify?'修改异常报告':'新增异常报告'" width="900" draggable>
      <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="80">
        <Row>
          <Col span="8">
          <FormItem label="物料条码" prop="fullNumber">
            <Input v-model="formValidate.fullNumber" placeholder="输入物料条码" @on-blur="searchTool()" />
          </FormItem>
          </Col>
          <Col span="8">
          <FormItem label="绑定设备" prop="equipmentId">
            <Select v-model="formValidate.equipmentId" ref="equipment" @on-change="equipmentListSelectChange" clearable filterable>
              <Option v-for="item in equipmentList" :value="item.pkId" :key="item.pkId">{{ item.equipmentCode }}-{{ item.equipmentName }}</Option>
            </Select>
          </FormItem>
          </Col>
          <Col span="8">
          <FormItem label="制件名称" prop="partId">
            <Select v-model="formValidate.partId" ref="part" @on-change="partListSelectChange">
              <Option v-for="item in partList" :value="item.pkId" :key="item.pkId">{{ item.partCode }}-{{ item.partName }}</Option>
            </Select>
          </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span="8">
          <FormItem label="异常原因" prop="unusualResion">
            <Input v-model="formValidate.unusualResion" placeholder="输入异常原因" />
          </FormItem>
          </Col>
          <Col span="8">
          <FormItem label="处理措施" prop="measures">
            <Select v-model="formValidate.measures" ref="measures">
              <Option v-for="item in measures" :value="item.value" :key="item.value">{{ item.name }}</Option>
            </Select>
          </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span="24">
          <FormItem label="备注" prop="remark">
            <Input type="textarea" v-model="formValidate.remark" placeholder="输入备注" />
          </FormItem>
          </Col>
        </Row>
      </Form>
      <div slot="footer">
        <Button @click="handleSubmit('formValidate')" type="primary" :disabled="isDisabled">保存</Button>
        <Button @click="handleReset('formValidate')" style="margin-left: 8px">取消</Button>
      </div>
    </Modal>

    <Modal v-model="deleteModalShow" width="250" :title="isBatchDetele?'批量删除':'删除'" @on-ok="deleteModalSureBtn" @on-cancel="deleteModalCancelBtn">
      <p>确定删除所选项？</p>
    </Modal>
  </div>
</template>

<script>
import axios from "@/libs/api.request";
import tool_unusual_manage_form_config from "./tool-unusual-manage-form-config.js";
import { dateFormat, objCopy } from "@/libs/tools.js";
import measures from "./measures.js";

export default {
  data() {
    return {
      isDisabled: false,
      deleteModalShow: false, //控制删除modal提示 显示
      deleteRowData: {}, //行删除行数据
      isBatchDetele: false, //批量删除标志
      multiselectRowData: [], //复选列数据
      toolUnusuallDetailModalshow: false, //控制明细modal显示
      isModify: false, //是否是修改明细操作
      measures: measures,
      equipmentList: [],
      partList: [],
      searchCondition: {
        dateInterval: [],
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
          title: "物料条码",
          width: 250,
          key: "fullNumber"
        },
        {
          title: "物料编码",
          width: 200,
          key: "toolNumber"
        },
        {
          title: "物料名称",
          width: 200,
          key: "toolName"
        },
        {
          title: "绑定设备",
          width: 200,
          key: "equipmentName"
        },

        {
          title: "制件名称",
          width: 200,
          key: "partName"
        },
        {
          title: "异常原因",
          width: 200,
          key: "unusualResion"
        },
        {
          title: "异常时间",
          width: 200,
          key: "unusualTime",
          render: function(h, params) {
            return h(
              "div",
              dateFormat(
                new Date(params.row.unusualTime),
                "yyyy-MM-dd HH:mm:ss"
              )
            );
          }
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
                "编辑"
              ),
              h(
                "Button",
                {
                  props: {
                    type: "error",
                    size: "small"
                  },
                  on: {
                    click: () => {
                      this.remove(params.index);
                    }
                  }
                },
                "删除"
              )
            ]);
          }
        }
      ],
      formValidate: tool_unusual_manage_form_config.formValidate, //user form表单字段
      ruleValidate: tool_unusual_manage_form_config.ruleValidate //user form表单验证规则
    };
  },
  created() {
    const self = this;
    self.getListData();
    self.getEquipmentListData();
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
        fullNumber: self.searchCondition.fullNumber,
        toolNumber: self.searchCondition.toolNumber,
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
          url: "/tool/tool-unusual-page-list",
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
    addNewToolUnusuall() {
      this.formValidate.unusualTime = dateFormat(
        new Date(this.formValidate.unusualTime),
        "yyyy-MM-dd HH:mm:ss"
      );
      this.isDisabled = true;
      let para = {
        toolId: this.formValidate.toolId,
        toolName: this.formValidate.toolName,
        toolNumber: this.formValidate.toolNumber,
        fullNumber: this.formValidate.fullNumber,
        equipmentId: this.formValidate.equipmentId,
        equipmentName: this.formValidate.equipmentName,
        partId: this.formValidate.partId,
        partName: this.formValidate.partName,
        unusualResion: this.formValidate.unusualResion,
        measures: this.formValidate.measures,
        remark: this.formValidate.remark
      };
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/tool-unusual-add",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          self.toolUnusuallDetailModalshow = false;
          if (res.data.code === 200) {
            self.$Message.success("异常报告新建成功!");
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
    editToolUnusuall() {
      this.formValidate.unusualTime = dateFormat(
        new Date(this.formValidate.unusualTime),
        "yyyy-MM-dd HH:mm:ss"
      );
      this.isDisabled = true;
      let para = {
        pkId: this.formValidate.pkId,
        toolId: this.formValidate.toolId,
        toolName: this.formValidate.toolName,
        toolNumber: this.formValidate.toolNumber,
        fullNumber: this.formValidate.fullNumber,
        equipmentId: this.formValidate.equipmentId,
        equipmentName: this.formValidate.equipmentName,
        partId: this.formValidate.partId,
        partName: this.formValidate.partName,
        unusualResion: this.formValidate.unusualResion,
        measures: this.formValidate.measures,
        remark: this.formValidate.remark
      };
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/tool-unusual-update",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          self.toolUnusuallDetailModalshow = false;
          if (res.data.code === 200) {
            self.$Message.success("异常报告信息修改成功!");
            self.isDisabled = false;
            self.getListData();
          } else {
            self.isDisabled = false;
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },
    searchTool() {
      const self = this;
      if (!!!self.formValidate.fullNumber) {
        return;
      } else {
        let para = {
          fullNumber: self.formValidate.fullNumber
        };
        axios
          .request({
            headers: {
              "Content-Type": "application/json; charset=UTF-8"
            },
            url: "/tool/tool-get-by-full-number.json",
            method: "post",
            isAuth: true,
            params: para
          })
          .then(function(res) {
            const data = res.data;
            if (data.code === 200) {
              if (data.data !== undefined && data.data !== null) {
                if (!!!data.data.pkId) {
                  self.formValidate.toolId = data.data.pkId;
                }
                self.formValidate.toolNumber = data.data.toolNumber;
                self.formValidate.toolName = data.data.toolName;
                self.getPartListData(self.formValidate.toolNumber);
              }
            } else {
              self.$Notice.error({
                title: "错误提示",
                desc: res.data.info
              });
            }
          });
      }
    },
    deleteRowDate(pkId, fullNumber) {
      let para = {
        pkId: pkId,
        fullNumber: fullNumber
      };
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/tool-unusual-delete-by-id",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          self.deleteModalShow = false;
          if (res.data.code === 200) {
            self.$Message.success("异常报告信息删除成功!");
            self.getListData();
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },
    deleteModalSureBtn() {
      if (this.isBatchDetele) {
        let ids = [];
        for (const item of this.multiselectRowData) {
          ids.push(item.pkId);
        }
      } else {
        this.deleteRowDate(
          this.deleteRowData.pkId,
          this.deleteRowData.fullNumber
        );
      }
    },
    //删除modal 取消方法
    deleteModalCancelBtn() {
      if (this.isBatchDetele) {
        this.multiselectRowData = [];
      } else {
        this.deleteRowData = {};
      }
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
    },
    show(index) {
      this.toolUnusuallDetailModalshow = true;
      this.isModify = true;
      this.formValidate = objCopy(this.tableData.rows[index], {});
      this.formValidate.unusualTime = new Date(this.formValidate.unusualTime);
    },
    remove(index) {
      this.deleteRowData = this.tableData.rows[index];
      this.deleteModalShow = true;
      this.isBatchDetele = false;
    },
    addToolUnusuall() {
      this.toolUnusuallDetailModalshow = true;
      this.isModify = false;
      this.formValidate = {
        checkType: 0
      };
      this.$refs["formValidate"].resetFields();
    },
    partListSelectChange(item) {
      let partList = this.partList;
      if (item !== undefined) {
        for (const part of partList) {
          if (part.pkId === item) {
            this.formValidate.partName = part.partName;
          }
        }
      }
    },
    equipmentListSelectChange(item) {
      let equipmentList = this.equipmentList;
      if (item !== undefined) {
        for (const equipment of equipmentList) {
          if (equipment.pkId === item) {
            this.formValidate.equipmentName = equipment.equipmentName;
          }
        }
      }
    },
    exportData() {
      const self = this;
      self.$refs.tablesMain.exportCsv({
        filename: "异常报告列表",
        original: false
      });
    },
    handleSuccess() {
      const self = this;
      self.$Message.success("文件导入成功!");
    },
    handleError() {
      const self = this;
      self.$Message.success("文件导入失败!");
    },
    //form 校验方法
    handleSubmit(name) {
      this.$refs[name].validate(valid => {
        if (valid) {
          if (this.isModify) {
            this.editToolUnusuall();
          } else {
            this.addNewToolUnusuall();
          }
        } else {
          this.$Message.error("请输入正确信息!");
        }
      });
    },
    handleReset(name) {
      this.toolUnusuallDetailModalshow = false;
      this.$refs[name].resetFields();
    }
  }
};
</script>

<style>
</style>
