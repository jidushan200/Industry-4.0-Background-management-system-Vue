
<template>
  <!--质检项弹出modal-->
  <Modal
    v-model="spotModalshow"
    class="modal-class"
    :title="isModify?'夹具点检':'夹具点检'"
    width="1000"
    draggable
  >
    <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="100" inline>
      <Row>
        <Col span="6">
          <FormItem label="物料编码" prop="fixtureNumber">
            <Input v-model="fixtureNumber" readonly style="width:150px;" />
          </FormItem>
        </Col>
        <Col span="6">
          <FormItem label="物料名称" prop="fixtureName">
            <Input v-model="fixtureName" readonly style="width:150px;" />
          </FormItem>
        </Col>
        <Col span="6">
          <FormItem label="物料图号" prop="fixtureMap">
            <Input v-model="fixtureMap" readonly style="width:150px;" />
          </FormItem>
        </Col>
        <Col span="6">
          <FormItem label="物料条码" prop="fixtureBarcode">
            <Input v-model="fixtureBarcode" readonly style="width:150px;" />
          </FormItem>
        </Col>
      </Row>
      <Row>
        <Col span="6">
          <FormItem label="质检结果" prop="checkResult">
            <RadioGroup v-model="checkResult">
              <Radio :label="1">合格</Radio>
              <Radio :label="2">不合格</Radio>
            </RadioGroup>
          </FormItem>
        </Col>
        <Col span="6">
          <FormItem label="质检时间" prop="checkTime">
            <DatePicker type="datetime" v-model="formValidate.checkTime" style="width:150px;"></DatePicker>
          </FormItem>
        </Col>
        <Col span="12">
          <FormItem label="质检描述" prop="standardDesc">
            <Input v-model="formValidate.remark" style="width:390px;" />
          </FormItem>
        </Col>
      </Row>
      <Row>
        <Col span="6" v-if="sourceName">
          <FormItem label="附件" prop="appendixId">
            <Button
              type="dashed"
              style="margin-right: 5px;"
              @click="downFile(appendixId)"
            >{{sourceName}}</Button>
            <Icon type="ios-search" slot="suffix" />
          </FormItem>
        </Col>
      </Row>
      <Table ref="myTable" border :columns="columns" :data="data" height="300"></Table>
    </Form>
    <div slot="footer">
      <!-- <Button @click="handleSave()" v-if="btnSave" type="primary">保存</Button> -->
      <Button @click="handleSubmit()" v-if="btnSubmit" type="primary" :disabled="isDisable">提交</Button>
      <Button @click="closeModal()" style="margin-left: 8px">关闭</Button>
    </div>
  </Modal>
</template>
<script>
import { dateFormat, objCopy } from "@/libs/tools.js";
import { getToken, getBaseUrl } from "@/libs/util";
import axios from "@/libs/api.request";
import qs from "qs";
import check_form_config from "./check-form-config.js";
export default {
  data() {
    return {
      isDisable: false,
      sourceName: "",
      appendixId: null,
      spotModalshow: false,
      fixtureNumber: "",
      fixtureBarcode: "",
      fixtureName: "",
      fixtureMap: "",
      fixtureSeq: null,
      isModify: false,
      checkResult: 2,
      btnSave: true, //保存按钮可见
      btnSubmit: true, //提交按钮可见
      radioList: [{ value: 1, label: "合格" }, { value: 2, label: "不合格" }],
      data: [],
      columns: [
        {
          title: "质检项",
          key: "checkItem",
          width: 220
        },
        {
          title: "质检标准",
          key: "itemStandard",
          width: 120
        },
        {
          title: "上偏值",
          key: "upDeviation",
          width: 120
        },
        {
          title: "下偏值",
          key: "downDeviation",
          width: 120
        },
        {
          title: "单位",
          key: "unit",
          width: 80
        },
        {
          title: "实测值",
          key: "measuredValue",
          render: (h, params) => {
            return h("Input", {
              props: {
                value: this.data[params.index].measuredValue
              },
              on: {
                input: val => {
                  this.data[params.index].measuredValue = val;
                },
                "on-blur": event => {
                  this.resultDetermine(params.index);
                }
              }
            });
          }
        },
        {
          title: "检验结果",
          key: "checkResult",
          width: 200,
          render: (h, params) => {
            return h(
              "RadioGroup",
              {
                props: {
                  value:
                    this.data[params.index].checkResult == 1 ? "合格" : "不合格"
                },
                on: {
                  "on-change": event => {
                    if (event == "合格") {
                      this.data[params.index].checkResult = 1;
                    } else {
                      this.data[params.index].checkResult = 2;
                    }
                    this.itemRadioChange(event);
                  }
                }
              },
              this.radioList.map(item => {
                return h("Radio", {
                  props: {
                    label: item.label,
                    key: item.value
                  }
                });
              })
            );
          }
        }
      ],
      formValidate: check_form_config.formValidate, // form表单字段
      ruleValidate: check_form_config.ruleValidate // form表单验证规则
    };
  },
  created() {
    const self = this;
  },
  methods: {
    init() {},
    //operType 1-新增 2-修改
    showModal(row, pkId, operType) {
      const self = this;
      self.btnSave = true;
      self.btnSubmit = true;
      self.data = [];
      self.formValidate = {};
      if (operType == 1) {
        //新增
        self.isModify = false;
        self.fixtureBarcode = row.fixtureBarcode;
        self.fixtureMap = row.fixtureMap;
        self.fixtureName = row.fixtureName;
        self.fixtureId = row.fixtureId;
        self.fixtureNumber = row.fixtureNumber;
        self.searchCheckStandard(row.fixtureNumber);
        self.formValidate.checkTime = new Date();
      } else {
        self.isModify = true;
        let para = {
          pkId: pkId
        };
        axios
          .request({
            headers: {
              "Content-Type": "application/json; charset=UTF-8"
            },
            url: "/fixture/fixture-check-get-by-id.json",
            method: "post",
            isAuth: true,
            params: para
          })
          .then(function(res) {
            const data = res.data;
            if (data.code === 200) {
              self.formValidate = objCopy(data.data, {});
              self.formValidate.checkTime = new Date(
                self.formValidate.checkTime
              );
              self.checkResult = data.data.checkResult;
              self.fixtureNumber = data.data.fixtureNumber;
              self.fixtureName = data.data.fixtureName;
              self.fixtureMap = data.data.fixtureMap;
              self.fixtureBarcode = data.data.fixtureBarcode;
              self.data = data.data.resultList;
              if (data.data.checkStatus != 0) {
                self.btnSave = false;
                self.btnSubmit = false;
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
      }
      self.spotModalshow = true;
    },
    //查询质检标准
    searchCheckStandard(fixtureNumber) {
      const self = this;
      let para = {
        materialType: 2,
        materialNumber: fixtureNumber,
        checkType: 6
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/base/check-standard-get-by-material-number",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            if (!!data.data) {
              self.formValidate.standardId = data.data.checkStandard.pkId;
              self.appendixId = data.data.checkStandard.appendixId;
              self.sourceName = data.data.checkStandard.sourceName;
              self.data = data.data.checkStandard.itemList;
            }
          } else {
            self.fixtureBarcode = "";
            self.fixtureNumber = "";
            self.fixtureName = "";
            self.fixtureMap = "";
            self.formValidate.standardId = "";
            self.data = [];
            self.btnSave = false;
            self.btnSubmit = false;
            self.$Message.error(res.data.info);
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },

    rowValid() {
      const self = this;
      let vl = false;

      return vl;
    },

    updateCheckResultSave() {
      this.isDisabled = true;
      const self = this;
      self.formValidate.checkTime = dateFormat(
        new Date(self.formValidate.checkTime),
        "yyyy-MM-dd HH:mm:ss"
      );
      self.deliveryTime = dateFormat(
        new Date(self.deliveryTime),
        "yyyy-MM-dd HH:mm:ss"
      );
      let para = {
        pkId: self.formValidate.pkId,
        fixtureId: self.formValidate.fixtureId,
        fixtureBarcode: self.fixtureBarcode,
        standardId: self.formValidate.standardId,
        checkType: 6,
        checkResult: self.formValidate.checkResult,
        checkTime: self.formValidate.checkTime,
        checkStatus: self.formValidate.checkStatus,
        remark: self.formValidate.remark,
        resultList: JSON.stringify(self.data)
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/x-www-form-urlencoded"
          },
          url: "/fixture/fixture-check-update.json",
          method: "post",
          isAuth: true,
          data: qs.stringify(para)
        })
        .then(function(res) {
          self.spotModalshow = false;
          if (res.data.code === 200) {
            self.$Message.success("夹具点检结果保存成功!");
            self.isDisabled = false;
            self.$emit("search");
          } else {
            self.$Notice.error({
              title: "错误提示",
              desc: res.data.info
            });
            self.isDisabled = false;
          }
          self.isDisable = false;
        })
        .catch(function(err) {
          console.log(err);
        });
    },

    resultDetermine(index) {
      const self = this;
      let item = self.data[index];
      const measuredValue = item.measuredValue;
      if (!!!measuredValue) {
        return;
      }
      const maximum = item.maximum;
      const minimum = item.minimum;
      if (null != maximum && null != minimum) {
        if (measuredValue >= minimum && measuredValue <= maximum) {
          item.checkResult = 1;
        } else {
          item.checkResult = 2;
        }
      } else {
        if (measuredValue == item.itemStandard) {
          item.checkResult = 1;
        } else {
          item.checkResult = 2;
        }
      }
      self.data.splice(index, 1, item);
      if (item.checkResult == 2) {
        self.checkResult = 2;
        self.formValidate.checkResult = 2;
      } else {
        self.validAllItem();
      }
    },

    itemRadioChange(value) {
      const self = this;
      if (value == "不合格") {
        self.checkResult = 2;
        self.formValidate.checkResult = 2;
      } else {
        self.validAllItem();
      }
    },
    validAllItem() {
      const self = this;
      let result = 1;
      for (let row of self.data) {
        if (row.checkResult != 1) {
          self.checkResult = 2;
          return;
        }
      }
      self.checkResult = result;
      self.formValidate.checkResult = result;
    },

    addCheckResultSave() {
      this.isDisabled = true;
      const self = this;
      self.formValidate.checkTime = dateFormat(
        new Date(self.formValidate.checkTime),
        "yyyy-MM-dd HH:mm:ss"
      );

      let para = {
        fixtureId: self.fixtureId,
        fixtureBarcode: self.fixtureBarcode,
        fixtureNumber: self.fixtureNumber,
        standardId: self.formValidate.standardId,
        checkType: 6,
        checkTypeName: "夹具点检",
        checkResult: self.formValidate.checkResult,
        checkTime: self.formValidate.checkTime,
        checkStatus: self.formValidate.checkStatus,
        remark: self.formValidate.remark,
        resultList: JSON.stringify(self.data)
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/x-www-form-urlencoded"
          },
          url: "/fixture/fixture-check-add.json",
          method: "post",
          isAuth: true,
          data: qs.stringify(para)
        })
        .then(function(res) {
          self.spotModalshow = false;
          if (res.data.code === 200) {
            self.$Message.success("质检结果保存成功!");
            self.isDisabled = false;
            self.$emit("search");
          } else {
            self.$Notice.error({
              title: "错误提示",
              desc: res.data.info
            });
            self.isDisabled = false;
          }
          self.isDisable = false;
        })
        .catch(function(err) {
          console.log(err);
        });
    },
    downFile(pkId) {
      window.location.href =
        getBaseUrl() +
        "/tool/appendix-download?pkId=" +
        pkId +
        "&token=" +
        getToken();
    },
    //保存
    handleSave() {
      const self = this;
      self.formValidate.checkStatus = 0;
      self.$refs["formValidate"].validate(valid => {
        if (valid) {
          self.isDisable = true;
          if (self.isModify) {
            self.updateCheckResultSave();
          } else {
            self.addCheckResultSave();
          }
        } else {
          self.$Message.error("请输入正确信息!");
        }
      });
    },
    //提交
    handleSubmit() {
      const self = this;
      self.formValidate.checkStatus = 1;
      self.$refs["formValidate"].validate(valid => {
        if (valid) {
          self.isDisable = true;
          if (self.isModify) {
            self.updateCheckResultSave();
          } else {
            self.addCheckResultSave();
          }
        } else {
          self.$Message.error("请输入正确信息!");
        }
      });
    },

    //关闭弹窗
    closeModal() {
      const self = this;
      self.spotModalshow = false;
    }
  },
  mounted() {}
};
</script>