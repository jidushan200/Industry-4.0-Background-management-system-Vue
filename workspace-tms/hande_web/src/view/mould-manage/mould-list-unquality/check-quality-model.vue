<template>
  <!--申请单信息弹出modal-->
  <Modal v-model="checkQualityModel" title="模具尺寸检测报告" width="1100" draggable>
    <Row>
      <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="80">
        <Col span="8">
          <FormItem prop="mouldName" label="物料名称">
            <Input type="text" v-model="formValidate.mouldName" readonly></Input>
          </FormItem>
        </Col>
        <Col span="8">
          <FormItem prop="mouldNumber" label="物料编码">
            <Input type="text" v-model="formValidate.mouldNumber" readonly></Input>
          </FormItem>
        </Col>
        <Col span="8">
          <FormItem prop="mouldMap" label="物料图号">
            <Input type="text" v-model="formValidate.mouldMap" readonly></Input>
          </FormItem>
        </Col>
        <Col span="8">
          <FormItem prop="supplierName" label="供应商">
            <Input type="text" v-model="formValidate.supplierName" placeholder="请输入供应商"></Input>
          </FormItem>
        </Col>
        <Col span="8">
          <FormItem prop="selfCheckReport" label="自检报告">
            <RadioGroup style="width: 170px" v-model="formValidate.selfCheckReport">
              <Radio :label="1">有</Radio>
              <Radio :label="0">无</Radio>
            </RadioGroup>
          </FormItem>
        </Col>
      </Form>
    </Row>
    <Table ref="myTable" border :columns="columns" :data="data" height="300"></Table>
    <Row style="margin-top:5px;" v-if="uploadList">
      <Col span="24">
        <label style="width:80px;">附件：</label>
        <span
          class="demo-upload-list"
          style="width:200px;text-align:left"
          v-for="item in uploadList"
        >
          <a type="dashed" :key="item.id" @click="downFile(item.id)">{{item.sourceName}}</a>
        </span>
      </Col>
    </Row>
    <div slot="footer">
      <Button
        @click="handleSuccess()"
        type="success"
        style="margin-left: 8px"
        :disabled="isDisabled"
      >让步</Button>
      <Button
        @click="handleRepair()"
        type="warning"
        style="margin-left: 8px"
        :disabled="isDisabled"
      >返修</Button>
      <Button @click="handleScrip()" type="error" style="margin-left: 8px" :disabled="isDisabled">报废</Button>
      <Button @click="handleReset()" style="margin-left: 8px">关闭</Button>
    </div>
  </Modal>
</template>
<script>
import { dateFormat, objCopy } from "@/libs/tools.js";
import axios from "@/libs/api.request";
import qs from "qs";
import { getToken, getBaseUrl } from "@/libs/util";
import check_quality_form_config from "./check-quality-form-config.js";

export default {
  data() {
    return {
      isDisabled: false,
      isNew: false,
      data: [],
      checkQualityModel: false,
      uploadList: [],
      radioList: [
        {
          value: 1,
          label: "合格"
        },
        {
          value: 2,
          label: "不合格"
        }
      ],
      columns: [
        {
          title: "序号",
          align: "center",
          key: "litemSeq",
          width: 70
        },
        {
          title: "质检项",
          key: "lcheckItem",
          minwidth: 160,
          render: (h, params) => {
            return h("Input", {
              props: {
                readonly: true,
                value: this.data[params.index].lcheckItem
              },
              on: {
                input: val => {
                  this.data[params.index].lcheckItem = val;
                }
              }
            });
          }
        },
        {
          title: "实测值",
          key: "lmeasuredValue",
          width: 130,
          render: (h, params) => {
            return h("Input", {
              props: {
                readonly: true,
                value: this.data[params.index].lmeasuredValue
              },
              on: {
                input: val => {
                  this.data[params.index].lmeasuredValue = val;
                }
              }
            });
          }
        },
        {
          title: "质检结果",
          key: "lcheckResult",
          width: 160,
          render: (h, params) => {
            return h(
              "RadioGroup",
              {
                props: {
                  value: this.radioText(this.data[params.index].lcheckResult)
                },
                on: {
                  "on-change": event => {
                    this.data[params.index].lcheckResult =
                      event == "合格" ? 1 : 2;
                  }
                }
              },
              this.radioList.map(item => {
                return h("Radio", {
                  props: {
                    disabled: true,
                    label: item.label,
                    key: item.value
                  }
                });
              })
            );
          }
        },
        {
          title: "序号",
          key: "ritemSeq",
          align: "center",
          width: 70
        },
        {
          title: "质检项",
          key: "rcheckItem",
          minwidth: 160,
          render: (h, params) => {
            return h("Input", {
              props: {
                readonly: true,
                value: this.data[params.index].rcheckItem
              },
              on: {
                input: val => {
                  this.data[params.index].rcheckItem = val;
                }
              }
            });
          }
        },
        {
          title: "实测值",
          key: "rmeasuredValue",
          width: 130,
          render: (h, params) => {
            return h("Input", {
              props: {
                readonly: true,
                value: this.data[params.index].rmeasuredValue
              },
              on: {
                input: val => {
                  this.data[params.index].rmeasuredValue = val;
                }
              }
            });
          }
        },
        {
          title: "质检结果",
          key: "rcheckResult",
          width: 160,
          render: (h, params) => {
            return h(
              "RadioGroup",
              {
                props: {
                  value: this.radioText(this.data[params.index].rcheckResult)
                },
                on: {
                  "on-change": event => {
                    this.data[params.index].rcheckResult =
                      event == "合格" ? 1 : 2;
                  }
                }
              },
              this.radioList.map(item => {
                return h("Radio", {
                  props: {
                    disabled: true,
                    label: item.label,
                    key: item.value
                  }
                });
              })
            );
          }
        }
      ],
      formValidate: check_quality_form_config.formValidate, //form表单字段
      ruleValidate: check_quality_form_config.ruleValidate //form表单验证规则
    };
  },
  created() {},
  methods: {
    init() {
      const self = this;
    },
    radioText(value) {
      if (value == 1) {
        return "合格";
      } else if (value == 2) {
        return "不合格";
      }
    },
    showModal(row) {
      const self = this;
      self.uploadList = [];
      let para = {
        pkId: row.pkId
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/mould/get-check-by-id",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            self.formValidate = data.data;
            self.data = data.data.checkItemList;
            for (let item of data.data.appendixList) {
              self.uploadList.push({
                id: item.pkId,
                sourceName: item.sourceName,
                status: "finished"
              });
            }
          }
        })
        .catch(function(err) {
          console.log(err);
        });
      if (row.checkType === 1) {
        self.isNew = true;
      } else if (row.checkType === 2) {
        self.isNew = false;
      }
      self.checkQualityModel = true;
    },
    handleSuccess() {
      this.isDisabled = true;
      const self = this;
      let para = {
        pkId: self.formValidate.pkId,
        mouldId: self.formValidate.mouldId,
        mouldNumber: self.formValidate.mouldNumber,
        mouldMap: self.formValidate.mouldMap,
        mouldReceiptId: self.formValidate.receiptId,
        checkType: self.formValidate.checkType
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/mould/mould-check-compromise.json",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            self.checkQualityModel = false;
            self.$Message.success("量具检验不合格，让步处理已准许返库!");
            self.isDisabled = false;
            self.$emit("search");
          } else {
            self.isDisabled = false;
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },
    handleRepair() {
      const self = this;
      this.isDisabled = true;
      let para = {
        pkId: self.formValidate.pkId,
        mouldNumber: self.formValidate.mouldNumber,
        mouldReceiptId: self.formValidate.receiptId,
        mouldId: self.formValidate.mouldId
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/mould/mould-check-repair.json",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            self.checkQualityModel = false;
            self.$Message.success("量具检验不合格，已返修!");
            self.isDisabled = false;
            self.$emit("search");
          } else {
            self.isDisabled = false;
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },
    handleScrip() {
      const self = this;
      this.isDisabled = true;
      let para = {
        pkId: self.formValidate.pkId,
        mouldId: self.formValidate.mouldId,
        mouldNumber: self.formValidate.mouldNumber,
        mouldReceiptId: self.formValidate.receiptId,
        checkType: self.formValidate.checkType
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/mould/mould-check-scrip.json",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            self.checkQualityModel = false;
            self.$Message.success("量具检验不合格，已准许报废!");
            self.isDisabled = false;
            self.$emit("search");
          } else {
            self.isDisabled = false;
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },
    handleReset() {
      const self = this;
      self.checkQualityModel = false;
    },
    downFile(pkId) {
      window.location.href =
        getBaseUrl() +
        "/tool/appendix-download?pkId=" +
        pkId +
        "&token=" +
        getToken();
    }
  }
};
</script>
