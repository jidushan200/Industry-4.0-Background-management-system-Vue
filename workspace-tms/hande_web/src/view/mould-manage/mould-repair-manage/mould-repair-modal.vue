<template>
  <!--质检项弹出modal-->
  <Modal v-model="itemModalshow" title="模具修磨" width="950" draggable>
    <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="100" inline>
      <Row>
        <Col span="8">
        <FormItem label="模具图号" prop="mouldMap">
          <Input style="width:170px" v-model="formValidate.mouldMap" readonly></Input>
        </FormItem>
        </Col>
        <Col span="8">
        <FormItem label="模具编码" prop="mouldNumber">
          <Input style="width:170px" v-model="formValidate.mouldNumber" placeholder="输入物料编码" @keyup.enter.native="searchMaterialByNumber()" readonly>
          <Icon type="ios-search" slot="suffix" />
          </Input>
        </FormItem>
        </Col>
        <Col span="8">
        <FormItem label="模具名称" prop="mouldName">
          <Input style="width:170px" v-model="formValidate.mouldName" readonly></Input>
        </FormItem>
        </Col>
      </Row>
      <Row>
        <Col span="8">
        <FormItem label="责任人" prop="userName">
          <Input style="width:170px" v-model="formValidate.userName" placeholder readonly />
        </FormItem>
        </Col>
        <Col span="8">
        <FormItem label="送磨时间" prop="receiveTime">
          <DatePicker type="datetime" style="width: 170px" v-model="formValidate.receiveTime" readonly></DatePicker>
        </FormItem>
        </Col>
      </Row>
      <Row>
        <Col span="24">
        <FormItem label="备注说明" prop="remark">
          <Input type="textarea" :rows="1" style="width:778px" v-model="formValidate.remark" placeholder="输入备注说明" />
        </FormItem>
        </Col>
      </Row>
      <Divider>
        <Button @click="addRow">添加项</Button>
      </Divider>
      <Table ref="myTable" border :columns="columns" :data="data" height="300"></Table>
    </Form>
    <div slot="footer">
      <Button @click="handleSave()" type="primary" v-if="btnSave" :disabled="isDisabled">保存</Button>
      <Button @click="handleReset()" style="margin-left: 8px">关闭</Button>
    </div>
  </Modal>
</template>
<script>
import { dateFormat, objCopy } from "@/libs/tools.js";
import axios from "@/libs/api.request";
import qs from "qs";
import mould_repair_manage_form_config from "./mould-repair-form-config.js";
export default {
  data() {
    return {
      isDisabled: false,
      btnSave: true,
      itemModalshow: false,
      procedureList: [],
      data: [],
      columns: [
        {
          title: "修磨工序",
          key: "procedureId",
          minwidth: 240,
          render: (h, params) => {
            return h(
              "Select",
              {
                props: {
                  clearable: true,
                  filterable: true,
                  value: this.data[params.index].procedureId
                },
                on: {
                  "on-change": event => {
                    this.data[params.index].procedureId = event;
                  }
                }
              },
              this.procedureList.map(item => {
                return h("Option", {
                  props: {
                    value: item.pkId,
                    label: item.procedureName
                  }
                });
              })
            );
          }
        },
        {
          title: "修磨工",
          key: "executor",
          minwidth: 140,
          render: (h, params) => {
            return h("Input", {
              props: {
                value: this.data[params.index].executor
              },
              on: {
                input: val => {
                  this.data[params.index].executor = val;
                }
              }
            });
          }
        },
        {
          title: "修磨时间",
          key: "executTime",
          minwidth: 170,
          render: (h, params) => {
            return h("DatePicker", {
              props: {
                value: dateFormat(
                  new Date(this.data[params.index].executTime),
                  "yyyy-MM-dd HH:mm:ss"
                ),
                type: "datetime"
              },
              on: {
                input: val => {
                  this.data[params.index].executTime = val;
                }
              }
            });
          }
        },
        {
          title: "操作",
          key: "action",
          width: 90,
          render: (h, params) => {
            /**
             * render写法 添加按钮
             */
            return h("div", [
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
                      this.delRow(params.index);
                    }
                  }
                },
                "删除"
              )
            ]);
          }
        }
      ],
      formValidate: mould_repair_manage_form_config.formValidate, // form表单字段
      ruleValidate: mould_repair_manage_form_config.ruleValidate // form表单验证规则
    };
  },
  created() {},
  methods: {
    init() {},
    getProcedureListData() {
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/x-www-form-urlencoded"
          },
          url: "/mould/mould-repair-procedure-list.json",
          method: "post",
          isAuth: true
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            self.procedureList = data.data;
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
    showModal(row) {
      const self = this;
      self.formValidate = objCopy(row, {});
      self.itemModalshow = true;
      self.formValidate.receiveTime = dateFormat(
        new Date(self.formValidate.receiveTime),
        "yyyy-MM-dd HH:mm:ss"
      );
      self.data = [];
      self.addRow();
      self.getProcedureListData();
    },
    /** 删除行 */
    delRow(idx) {
      this.data.splice(idx, 1);
    },
    addRow() {
      this.data.push({
        procedureId: null,
        executor: "",
        executTime: dateFormat(new Date(), "yyyy-MM-dd HH:mm:ss")
      });
    },
    //保存
    handleSave() {
      const self = this;
      self.$refs["formValidate"].validate(valid => {
        if (valid) {
          self.addMouldRepairSave();
        } else {
          self.$Message.error("请输入正确标准信息!");
        }
      });
    },
    rowValid() {
      const self = this;
      let vl = false;
      vl = true;
      return vl;
    },
    addMouldRepairSave() {
      const self = this;
      self.formValidate.receiveTime = dateFormat(
        new Date(self.formValidate.receiveTime),
        "yyyy-MM-dd HH:mm:ss"
      );
      if (!!!self.data || self.data.length < 1) {
        self.$Message.error("修磨信息不能空!");
        return;
      }
      let i = 1;
      for (let item of self.data) {
        if (!!!item.procedureId) {
          self.$Message.error("第" + i + "行修磨工序不能空!");
          return;
        }
        if (!!!item.executor) {
          self.$Message.error("第" + i + "行修磨工不能空!");
          return;
        }
        i++;
      }
      self.isDisabled = true;
      let para = {
        mouldId: self.formValidate.pkId,
        fullNumber: self.formValidate.mouldBarcode,
        repairUserId: self.formValidate.userId,
        repairUser: self.formValidate.userName,
        repairTime: self.formValidate.receiveTime,
        remark: self.formValidate.remark,
        itemList: JSON.stringify(self.data)
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/x-www-form-urlencoded"
          },
          url: "/mould/mould-repair-add.json",
          method: "post",
          isAuth: true,
          data: qs.stringify(para)
        })
        .then(function(res) {
          self.itemModalshow = false;
          if (res.data.code === 200) {
            self.$Message.success("模具修磨成功!");
            self.isDisabled = false;
            self.$emit("search");
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
    //关闭弹窗
    handleReset() {
      const self = this;
      self.itemModalshow = false;
    }
  },
  mounted() {
    this.addRow();
  }
};
</script>