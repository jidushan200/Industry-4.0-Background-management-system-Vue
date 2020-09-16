<style lang="less">
@import url("./check-modal.less");
</style>
<template>
  <Modal v-model="checkQualityModel" title="模具尺寸检测报告" width="1000" draggable>
    <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="80">
      <Row>
        <Col span="8">
          <FormItem prop="mouldName" label="物料名称">
            <Input type="text" v-model="formValidate.mouldName" :readonly="!isNew"></Input>
          </FormItem>
        </Col>
        <Col span="8">
          <FormItem prop="mouldNumber" label="物料编码">
            <Input type="text" v-model="formValidate.mouldNumber" :readonly="!isNew"></Input>
          </FormItem>
        </Col>
        <Col span="8">
          <FormItem prop="mouldMap" label="物料图号">
            <Input type="text" v-model="formValidate.mouldMap" :readonly="!isNew"></Input>
          </FormItem>
        </Col>
        <Col span="8">
          <FormItem prop="mouldSeq" label="顺序号">
            <Input type="text" v-model="formValidate.mouldSeq" :readonly="!isNew"></Input>
          </FormItem>
        </Col>
        <Col span="8">
          <FormItem label="供应商" prop="supplierId">
            <Select
              v-model="formValidate.supplierId"
              ref="supplierId"
              @on-change="supplierSelectChange"
              clearable
              filterable
            >
              <Option
                v-for="item in supplierList"
                :value="item.pkId"
                :key="item.pkId"
              >{{item.supplierName }}</Option>
            </Select>
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
      </Row>
      <Table ref="myTable" border :columns="columns" :data="data" height="300"></Table>
      <Row style="margin-top:5px;">
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
                  v-if="isCheck"
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
              :format="['zip','png','jpeg','jpg','doc','pdf']"
              :max-size="20480"
              :on-format-error="handleFormatError"
              :on-exceeded-size="handleMaxSize"
              :before-upload="handleBeforeUpload"
              type="drag"
              :action="url"
              style="display: inline-block;width:32px;"
              v-if="isCheck"
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
      <Button
        @click="handleSubmit()"
        type="primary"
        style="margin-left: 8px"
        v-if="isCheck"
        :disabled="isDisabled"
      >提交</Button>
      <Button @click="handleReset()" style="margin-left: 8px">关闭</Button>
    </div>
  </Modal>
</template>
<script>
import { dateFormat, objCopy } from "@/libs/tools.js";
import { getToken, getBaseUrl } from "@/libs/util";
import axios from "@/libs/api.request";
import qs from "qs";
import check_quality_form_config from "./check-quality-form-config.js";

export default {
  data() {
    return {
      isDisabled: false,
      isCheck: true,
      data: [],
      checkType: "",
      isNew: true,
      checkQualityModel: false,
      defaultList: [],
      uploadList: [],
      supplierName: "",
      supplierList: [],
      appendixShowProgress: false,
      uploadAppendixIds: [], //附件ids
      uploadHeaders: {
        Authorization: ""
      },
      url: getBaseUrl() + "/tool/tool-appendix-upload.json?token=" + getToken(),
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
                    disabled: !this.isCheck,
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
                    disabled: !this.isCheck,
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
      self.formValidate = {};
      self.formValidate = objCopy(row, {});
      self.data = [];
      self.uploadList = [];
      self.getSupplierList();
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/mould/get-check-item",
          method: "post",
          isAuth: true
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            self.data = data.data;
          }
        })
        .catch(function(err) {
          console.log(err);
        });
      self.checkQualityModel = true;
      self.uploadAppendixIds = [];
      self.$refs.upload.fileList = [];
      self.uploadList = self.$refs.upload.fileList;
    },
    showReportModal(row) {
      const self = this;
      let para = {
        pkId: row.pkId
      };
      self.uploadList = [];
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
              self.uploadAppendixIds.push(item.pkId);
            }
          }
        })
        .catch(function(err) {
          console.log(err);
        });
      self.isCheck = false;
      self.checkQualityModel = true;
    },
    doCheck(checkStatus) {
      this.isDisabled = true;
      const self = this;
      let para = {
        mouldReceiptId: self.formValidate.pkId,
        mouldSeq: self.formValidate.mouldSeq,
        mouldNumber: self.formValidate.mouldNumber,
        mouldName: self.formValidate.mouldName,
        mouldMap: self.formValidate.mouldMap,
        supplierId: self.formValidate.supplierId,
        supplierName: self.supplierName,
        selfCheckReport: self.formValidate.selfCheckReport,
        selfCheckReport: 1,
        checkStatus: checkStatus,
        checkType: self.formValidate.checkType,
        appendixIds: self.uploadAppendixIds.join(","),
        itemList: JSON.stringify(self.data)
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/x-www-form-urlencoded"
          },
          url: "/mould/mould-check-add.json",
          method: "post",
          isAuth: true,
          data: qs.stringify(para)
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            self.checkQualityModel = false;
            self.$Message.success("模具质检合格已准许入库!");
            self.isDisabled = false;
            self.$emit("search");
          } else {
            self.isDisabled = false;
            self.$Message.error("系统错误!");
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },

    getSupplierList() {
      const self = this;
      let para = {
        isMould: 1
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/base/supplier-list",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            self.supplierList = data.data;
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },
    supplierSelectChange(item) {
      const self = this;
      let supplierList = self.supplierList;
      if (item !== undefined) {
        for (const supplier of supplierList) {
          if (supplier.pkId === item) {
            self.supplierName = supplier.supplierName;
          }
        }
      }
    },

    handleSubmit() {
      const self = this;
      self.$refs["formValidate"].validate(valid => {
        if (valid) {
          let checkStatus = 1;
          let datatemp = self.data;
          for (let row of datatemp) {
            if (row.lcheckResult == 2) {
              checkStatus = 2;
            }
            if (row.rcheckResult == 2) {
              checkStatus = 2;
            }
          }
          self.doCheck(checkStatus);
        } else {
          self.$Message.error("请输入正确信息!");
        }
      });
    },
    handleReset() {
      const self = this;
      self.checkQualityModel = false;
    },
    handleRemove(file) {
      // 从 upload 实例删除数据
      const self = this;
      debugger;
      const fileList = self.$refs.upload.fileList;
      let pkId = file.id;
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
