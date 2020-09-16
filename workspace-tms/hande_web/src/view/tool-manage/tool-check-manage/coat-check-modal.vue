<style lang="less">
@import url("./tool-check-modal.less");
</style>
<template>
  <!--质检项弹出modal-->
  <Modal
    v-model="checkModalshow"
    class="modal-class"
    :title="isModify?'修改涂层质检':'新增涂层质检'"
    width="1000"
    draggable
  >
    <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="90" inline>
      <Row>
        <Col span="6">
          <FormItem label="物料条码" prop="fullNumber">
            <Input style="width:150px;" v-model="fullNumber"></Input>
          </FormItem>
        </Col>
        <Col span="6">
          <FormItem label="物料名称" prop="toolName">
            <Input v-model="toolName" readonly style="width:150px;" />
          </FormItem>
        </Col>
        <Col span="6">
          <FormItem label="物料图号" prop="toolMap">
            <Input v-model="toolMap" readonly style="width:150px;" />
          </FormItem>
        </Col>
      </Row>
      <Row>
        <Col span="6">
          <FormItem label="涂层厂家" prop="supplierName">
            <Input v-model="supplierName" style="width:150px;" />
          </FormItem>
        </Col>
        <Col span="6">
          <FormItem label="送检人" prop="deliveryer">
            <Input v-model="formValidate.deliveryer" style="width:150px;" />
          </FormItem>
        </Col>
        <Col span="6">
          <FormItem label="送检时间" prop="deliveryTime">
            <DatePicker
              type="datetime"
              v-model="formValidate.deliveryTime"
              placeholder="选择送检时间"
              style="width:150px;"
            ></DatePicker>
          </FormItem>
        </Col>
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
            <DatePicker
              type="datetime"
              v-model="formValidate.checkTime"
              placeholder="选择质检时间"
              style="width:150px;"
            ></DatePicker>
          </FormItem>
        </Col>
        <Col span="12">
          <FormItem label="质检描述" prop="standardDesc">
            <Input v-model="formValidate.remark" style="width:390px;" />
          </FormItem>
        </Col>
      </Row>
      <Table ref="myTable" border :columns="columns" :data="data" highlight-row></Table>
      <Row>
        <Col span="24">
          <FormItem label="附件" prop="appendixIds">
            <span
              class="demo-upload-list"
              style="width:200px;text-align:left"
              v-for="item in uploadList"
            >
              <template v-if="item.status === 'finished'">
                <a type="dashed" :key="item.id" @click="downFile(item.id)">{{item.sourceName}}</a>
                <Icon
                  style="margin-left:15px"
                  type="ios-trash-outline"
                  @click.native="handleRemove(item)"
                  v-if="!isModify"
                ></Icon>
              </template>
              <template v-else>
                <Progress v-if="item.showProgress" :percent="99" hide-info status="active"></Progress>
              </template>
            </span>
            <Upload
              ref="upload"
              :show-upload-list="false"
              :on-success="handleSuccess"
              :format="['zip','doc','docx','pdf','ppt','pptx']"
              :max-size="20480"
              :on-format-error="handleFormatError"
              :on-exceeded-size="handleMaxSize"
              :before-upload="handleBeforeUpload"
              type="drag"
              :action="url"
              style="display: inline-block;width:32px;"
              v-if="!isModify"
            >
              <div style="width: 32px;height:32px;line-height: 32px;">
                <Icon type="md-attach" size="20"></Icon>
              </div>
            </Upload>
          </FormItem>
        </Col>
      </Row>
    </Form>
    <div slot="footer">
      <!-- <Button @click="handleSave()" v-if="btnSave" type="primary">保存</Button> -->
      <Button @click="handleSubmit()" v-if="btnSubmit" type="primary" :disabled="isDisabled">提交</Button>
      <Button @click="closeModal()" style="margin-left: 8px">关闭</Button>
    </div>
  </Modal>
</template>
<script>
import { dateFormat, objCopy } from "@/libs/tools.js";
import { getToken, getBaseUrl } from "@/libs/util";
import axios from "@/libs/api.request";
import qs from "qs";
import repair_check_form_config from "./repair-check-form-config.js";
export default {
  data() {
    return {
      isDisabled: false,
      appendixId: null,
      sourceName: "",
      checkModalshow: false,
      checkResult: 2,
      fullNumber: "",
      toolName: "",
      toolMap: "",
      supplierId: null,
      supplierName: "",
      deliveryer: "",
      isModify: false,
      btnSave: true, //保存按钮可见
      btnSubmit: true, //提交按钮可见
      radioList: [{ value: 1, label: "合格" }, { value: 2, label: "不合格" }],
      data: [],
      defaultList: [],
      uploadList: [],
      appendixShowProgress: false,
      uploadAppendixIds: [], //附件ids
      uploadHeaders: {
        Authorization: ""
      },
      url: getBaseUrl() + "/tool/tool-appendix-upload.json?token=" + getToken(),
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
          width: 120,
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
      formValidate: repair_check_form_config.formValidate, // form表单字段
      ruleValidate: repair_check_form_config.ruleValidate // form表单验证规则
    };
  },
  created() {},
  methods: {
    init() {},
    showModal(row, pkId, operType) {
      const self = this;
      self.btnSave = true;
      self.btnSubmit = true;
      if (operType == 2) {
        self.isModify = true;
        //self.$refs.upload.fileList = [];
        //self.uploadList = self.$refs.upload.fileList;
        self.uploadList = [];
        let para = {
          pkId: pkId
        };
        axios
          .request({
            headers: {
              "Content-Type": "application/json; charset=UTF-8"
            },
            url: "/tool/tool-check-get-by-id.json",
            method: "post",
            isAuth: true,
            params: para
          })
          .then(function(res) {
            const data = res.data;
            if (data.code === 200) {
              self.formValidate = objCopy(data.data, {});
              self.formValidate.deliveryTime = new Date(
                self.formValidate.deliveryTime
              );
              self.formValidate.checkTime = new Date(
                self.formValidate.checkTime
              );

              self.fullNumber = data.data.fullNumber;
              self.formValidate.fileList = data.data.fullNumber;
              self.toolNumber = data.data.toolNumber;
              self.toolName = data.data.toolName;
              self.toolMap = data.data.toolMap;
              self.typeId = data.data.typeId;
              self.supplierId = data.data.supplierId;
              self.supplierName = data.data.supplierName;
              self.checkResult = data.data.checkResult;
              self.formValidate.checkType = self.checkResult;
              self.data = data.data.resultList;
              if (data.data.checkStatus != 0) {
                self.btnSave = false;
                self.btnSubmit = false;
              }
              for (let item of data.data.appendixList) {
                self.uploadList.push({
                  id: item.pkId,
                  sourceName: item.sourceName,
                  status: "finished"
                });
                self.uploadAppendixIds.push(item.pkId);
              }
              // self.uploadList = self.$refs.upload.fileList;
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
      } else {
        self.isModify = false;
        self.data = [];
        self.formValidate = {};
        self.formValidate.fullNumber = row.fullNumber;
        self.fullNumber = row.fullNumber;
        self.toolName = row.toolName;
        self.toolNumber = row.toolNumber;
        self.toolMap = row.toolMap;
        self.supplierId = row.supplierId;
        self.supplierName = row.supplierName;
        self.uploadAppendixIds = [];
        self.$refs.upload.fileList = [];
        self.uploadList = self.$refs.upload.fileList;
        self.searchCheckStandard(row.toolNumber);
        self.formValidate.checkTime = new Date();
        self.formValidate.deliveryTime = new Date(row.setCheckTime);
      }
     
      self.checkModalshow = true;
    },
    //查询质检标准
    searchCheckStandard(toolNumber) {
      const self = this;
      let para = {
        materialType: 1,
        materialNumber: toolNumber,
        checkType: 3
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/base/check-standard-get-by-material-number.json",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            if (!!data.data) {
              self.formValidate.toolId = data.data.tool.pkId;
              self.typeId = data.data.tool.typeId;
              self.formValidate.standardId = data.data.checkStandard.pkId;
              self.appendixId = data.data.checkStandard.appendixId;
              self.sourceName = data.data.checkStandard.sourceName;
              self.data = data.data.checkStandard.itemList;
              self.btnSave = true;
              self.btnSubmit = true;
            }
          } else {
            self.formValidate.toolId = null;
            self.toolName = "";
            self.toolName = "";
            self.formValidate.standardId = "";
            self.toolNumber = "";
            self.btnSave = false;
            self.btnSubmit = false;
            self.data = [];
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

    rowValid() {
      const self = this;
      let vl = false;
      for (let i = 0, len = self.data.length; i < len; i++) {
        self.$refs["formDynamic" + i].validate(rowValid => {
          if (!!!rowValid) {
            self.$Message.error("请输入正确信息!");
            return;
          }
        });
      }
      vl = true;
      return vl;
    },

    updateCheckResultSave() {
      this.isDisabled = true;
      const self = this;
      self.formValidate.checkTime = dateFormat(
        new Date(self.formValidate.checkTime),
        "yyyy-MM-dd HH:mm:ss"
      );
      self.formValidate.deliveryTime = dateFormat(
        new Date(self.formValidate.deliveryTime),
        "yyyy-MM-dd HH:mm:ss"
      );
      let para = {
        pkId: self.formValidate.pkId,
        toolId: self.formValidate.toolId,
        fullNumber: self.fullNumber,
        toolNumber: self.toolNumber,
        deliveryer: self.formValidate.deliveryer,
        supplierId: self.supplierId,
        supplierName: self.supplierName,
        deliveryTime: self.formValidate.deliveryTime,
        standardId: self.formValidate.standardId,
        checkType: 3,
        checkTypeName: "涂层质检",
        typeId: self.typeId,
        checkResult: self.formValidate.checkResult,
        checkTime: self.formValidate.checkTime,
        checkStatus: self.formValidate.checkStatus,
        remark: self.formValidate.remark,
        appendixIds: self.uploadAppendixIds.join(","),
        resultList: JSON.stringify(self.data)
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/x-www-form-urlencoded"
          },
          url: "/tool/tool-check-update.json",
          method: "post",
          isAuth: true,
          data: qs.stringify(para)
        })
        .then(function(res) {
          self.checkModalshow = false;
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
          self.isDisabled = false;
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
      self.formValidate.deliveryTime = dateFormat(
        new Date(self.formValidate.deliveryTime),
        "yyyy-MM-dd HH:mm:ss"
      );
      let para = {
        toolId: self.formValidate.toolId,
        fullNumber: self.fullNumber,
        toolNumber: self.toolNumber,
        deliveryer: self.formValidate.deliveryer,
        deliveryTime: self.formValidate.deliveryTime,
        standardId: self.formValidate.standardId,
        supplierId: self.supplierId,
        supplierName: self.supplierName,
        checkType: 3,
        checkTypeName: "涂层质检",
        typeId: self.typeId,
        checkResult: self.formValidate.checkResult,
        checkTime: self.formValidate.checkTime,
        checkStatus: self.formValidate.checkStatus,
        remark: self.formValidate.remark,
        appendixIds: self.uploadAppendixIds.join(","),
        resultList: JSON.stringify(self.data)
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/x-www-form-urlencoded"
          },
          url: "/tool/tool-check-add.json",
          method: "post",
          isAuth: true,
          data: qs.stringify(para)
        })
        .then(function(res) {
          self.checkModalshow = false;
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
          self.isDisabled = false;
        })
        .catch(function(err) {
          console.log(err);
        });
    },

    //保存
    handleSave() {
      const self = this;
      self.formValidate.checkStatus = 0;
      self.$refs["formValidate"].validate(valid => {
        if (valid) {
          self.isDisabled = true;
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
          self.isDisabled = true;
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
      self.checkModalshow = false;
    },

    handleRemove(file) {
      // 从 upload 实例删除数据
      const self = this;
      const fileList = this.$refs.upload.fileList;
      let pkId = file.name;
      let para = {
        pkId: pkId
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/x-www-form-urlencoded"
          },
          url: "/tool/tool-appendix-delete-by-id.json",
          method: "post",
          isAuth: true,
          data: qs.stringify(para)
        })
        .then(function(res) {
          if (res.data.code === 200) {
            self.$refs.upload.fileList.splice(fileList.indexOf(file), 1);
            self.uploadAppendixIds.splice(
              self.uploadAppendixIds.indexOf(pkId),
              1
            );
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
    downFile(pkId) {
      window.location.href =
        getBaseUrl() +
        "/tool/appendix-download?pkId=" +
        pkId +
        "&token=" +
        getToken();
    },
    handleSuccess(res, file) {
      // 因为上传过程为实例，这里模拟添加 url
      const self = this;
      self.appendixShowProgress = false;
      self.uploadList.pop();
      self.uploadList.push({
        id: res.pkId,
        sourceName: res.sourceName,
        status: "finished"
      });
      self.uploadAppendixIds.push(res.pkId);
    },
    handleFormatError(file) {
      this.$Notice.warning({
        title: "文件格式不正确",
        desc: "文件 " + file.name + " 格式不正确。"
      });
    },
    handleMaxSize(file) {
      this.$Notice.warning({
        title: "超出文件大小限制",
        desc: "文件 " + file.name + " 太大，不能超过 20M。"
      });
    },
    /*uploadHeaders: {
        Authorization: getToken()
    },*/
    handleBeforeUpload() {
      this.appendixShowProgress = true;
      const check = this.uploadList.length < 5;
      if (!check) {
        this.$Notice.warning({
          title: "最多只能上传 5 个附件。"
        });
        this.appendixShowProgress = false;
      }
      return check;
    }
  },
  mounted() {}
};
</script>