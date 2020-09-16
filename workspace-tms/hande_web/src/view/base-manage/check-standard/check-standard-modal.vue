<style lang="less">
@import url("./tool-check-modal.less");
</style>
<template>
  <!--质检项弹出modal-->
  <Modal
    v-model="itemModalshow"
    class="modal-class"
    :title="isModify?'修改检验标准':'新增检验标准'"
    width="960"
    draggable
  >
    <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="90" inline>
      <Row>
        <Col span="8">
          <FormItem label="物料编码" prop="materialNumber" v-if="!!!isModify">
            <Input
              style="width:200px"
              v-model="formValidate.materialNumber"
              placeholder="输入物料编码"
              @on-blur="searchMaterialByNumber()"
            >
              <Icon type="ios-search" slot="suffix" />
            </Input>
          </FormItem>
          <FormItem label="物料编码" prop="materialNumber" v-if="isModify">
            <Input style="width:200px" v-model="formValidate.materialNumber" readonly></Input>
          </FormItem>
        </Col>
        <Col span="8">
          <FormItem label="图号名称" prop="baseId" v-if="!!!isModify">
            <Select
              style="width:200px"
              v-model="formValidate.materialId"
              ref="materialName"
              placeholder="请选择图号名称"
              @on-change="searchMaterialById"
              clearable
              filterable
            >
              <Icon type="ios-search" slot="suffix" />
              <Option
                v-for="item in materialList"
                :value="item.pkId"
                :key="item.pkId"
              >{{ item.materialName }}</Option>
            </Select>
          </FormItem>
        </Col>
        <Col span="8">
          <FormItem label="物料名称" prop="materialName" v-if="isModify">
            <Input style="width:200px" v-model="materialName" readonly></Input>
          </FormItem>
        </Col>
        <Col span="8">
          <FormItem label="物料图号" prop="materialMap">
            <Input style="width:200px" v-model="materialMap" placeholder readonly />
          </FormItem>
        </Col>
      </Row>
      <Row>
        <Col span="8">
          <FormItem label="检验类型" prop="checkType">
            <Select
              v-model="formValidate.checkType"
              ref="checkType"
              style="width:200px"
              @on-change="standardValidate()"
            >
              <Option
                v-for="item in checkTypeList"
                :value="item.value"
                :key="item.value"
              >{{ item.name }}</Option>
            </Select>
          </FormItem>
        </Col>
        <Col span="16">
          <FormItem label="附件" prop="appendixIds">
            <div
              style="width:460px;text-align:left"
              class="demo-upload-list"
              v-for="item in uploadList"
            >
              <template v-if="item.status === 'finished'">
                <a type="dashed" :key="item.id" @click="downFile(item.id)">{{item.sourceName}}</a>
                <Icon
                  style="margin-left:15px"
                  type="ios-trash-outline"
                  @click.native="handleRemove(item)"
                ></Icon>
              </template>
            </div>
            <Upload
              ref="upload"
              :show-upload-list="false"
              :default-file-list="defaultList"
              :on-success="handleFormatSuccess"
              :format="['zip','doc','docx','pdf','ppt','pptx']"
              :max-size="20480"
              :on-format-error="handleFormatError"
              :on-exceeded-size="handleMaxSize"
              :before-upload="handleBeforeUpload"
              multiple
              type="drag"
              :action="appendixExportUrl"
              style="display: inline-block;width:32px;"
            >
              <div style="width: 32px;height:32px;line-height: 32px;">
                <Icon type="md-attach" size="20"></Icon>
              </div>
            </Upload>
          </FormItem>
        </Col>
        <Col span="24">
          <FormItem label="标准描述" prop="standardDesc">
            <Input style="width:820px;" v-model="formValidate.standardDesc" placeholder="输入标准描述" />
          </FormItem>
        </Col>
      </Row>
      <Divider>
        <Button @click="addRow" type="primary">添加项</Button>&nbsp;&nbsp;
        <Upload
          :show-upload-list="false"
          :action="uploadAction"
          :on-success="importSuccess"
          :on-error="importError"
          style="display: inline-block;margin-right: 5px;"
        >
          <Button icon="ios-cloud-upload-outline">导入</Button>
        </Upload>
      </Divider>
      <Table ref="myTable" border :columns="columns" :data="data" height="300"></Table>
    </Form>
    <div slot="footer">
      <Button @click="handleSave()" type="primary" v-if="btnSave" :disabled="isDisable">保存</Button>
      <Button @click="closeModal()" style="margin-left: 8px">关闭</Button>
    </div>
  </Modal>
</template>
<script>
import { dateFormat, objCopy } from "@/libs/tools.js";
import axios from "@/libs/api.request";
import qs from "qs";
import check_standard_form_config from "./check-standard-form-config.js";
import { getToken, getBaseUrl } from "@/libs/util";
export default {
  data() {
    return {
      btnSave: true,
      isDisable: false,
      itemModalshow: false,
      appendixShowProgress: false,
      isModify: false,
      checkTypeList: [],
      materialType: 1,
      materialTypeName: "",
      materialName: "",
      materialMap: "",
      appendixIds: [], //附件ids
      materialList: [],
      defaultList: [],
      uploadList: [],
      data: [],
      partList: [],
      appendixId: null,
      sourceName: "",
      uploadAction:
        getBaseUrl() +
        "/base/check-standard-item-import.json?token=" +
        getToken(),
      appendixExportUrl:
        getBaseUrl() + "/tool/tool-appendix-upload.json?token=" + getToken(),
      columns: [
        {
          title: "质检项",
          key: "checkItem",
          width: 330,
          render: (h, params) => {
            return h("Input", {
              props: {
                value: this.data[params.index].checkItem
              },
              on: {
                input: val => {
                  this.data[params.index].checkItem = val;
                }
              }
            });
          }
        },
        {
          title: "标准值",
          key: "itemStandard",
          width: 160,
          render: (h, params) => {
            return h("Input", {
              props: {
                value: this.data[params.index].itemStandard
              },
              on: {
                input: val => {
                  this.data[params.index].itemStandard = val;
                }
              }
            });
          }
        },
        {
          title: "上偏差值",
          key: "upDeviation",
          width: 120,
          render: (h, params) => {
            return h("Input", {
              props: {
                value: this.data[params.index].upDeviation
              },
              on: {
                input: val => {
                  this.data[params.index].upDeviation = val;
                }
              }
            });
          }
        },
        {
          title: "下偏差值",
          key: "downDeviation",
          width: 120,
          render: (h, params) => {
            return h("Input", {
              props: {
                value: this.data[params.index].downDeviation
              },
              on: {
                input: val => {
                  this.data[params.index].downDeviation = val;
                }
              }
            });
          }
        },
        {
          title: "单位",
          key: "unit",
          width: 100,
          render: (h, params) => {
            return h("Input", {
              props: {
                value: this.data[params.index].unit
              },
              on: {
                input: val => {
                  this.data[params.index].unit = val;
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
      formValidate: check_standard_form_config.formValidate, // form表单字段
      ruleValidate: check_standard_form_config.ruleValidate // form表单验证规则
    };
  },
  created() {
    // this.getBaseListData();
  },
  methods: {
    init() {
      self.materialList = [];
      self.checkTypeList = [];
    },
    standardValidate() {
      const self = this;
      if (!!!self.formValidate.materialNumber) {
        return;
      } else {
        let paras = {
          materialType: self.materialType,
          materialNumber: self.formValidate.materialNumber,
          checkType: self.formValidate.checkType
        };
        axios
          .request({
            headers: {
              "Content-Type": "application/json; charset=UTF-8"
            },
            url: "/base/standard-number-validate.json",
            method: "post",
            isAuth: true,
            params: paras
          })
          .then(function(result) {
            if (result.data) {
              self.btnSave = true;
            } else {
              self.btnSave = false;
              self.$Message.error("质检标准已经存在");
            }
          })
          .catch(function(err) {
            console.log(err);
          });
      }
    },

    getBaseListData(materialType) {
      const self = this;
      let url;
      let paras = {
        materialType: materialType
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/base/material-base-list",
          isAuth: true,
          params: paras,
          method: "post"
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200 && data.data !== null) {
            self.materialList = data.data;
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },
    showModal(standardId, materialType, materialName, materialMap) {
      const self = this;
      self.appendixIds = [];
      self.materialType = materialType;
      self.materialName = materialName;
      self.materialMap = materialMap;
      self.getBaseListData(materialType);
      if (materialType == 1) {
        self.materialTypeName = "刀具";
        self.checkTypeList = [
          {
            name: "新刀质检",
            value: 1
          },
          {
            name: "刃磨质检",
            value: 2
          },
          {
            name: "涂层质检",
            value: 3
          }
        ];
      } else if (materialType == 2) {
        self.materialTypeName = "夹具";
        self.checkTypeList = [
          {
            name: "新夹具质检",
            value: 4
          },
          {
            name: "夹具修磨质检",
            value: 5
          },
          {
            name: "夹具点检",
            value: 6
          }
        ];
      } else if (materialType == 3) {
        self.materialTypeName = "刀条组合";
        self.checkTypeList = [
          {
            name: "刃磨质检",
            value: 7
          },
          {
            name: "涂层质检",
            value: 8
          }
        ];
      }
      this.$refs.upload.fileList = [];
      if (!!standardId) {
        self.isModify = true;
        let para = {
          pkId: standardId
        };
        axios
          .request({
            headers: {
              "Content-Type": "application/json; charset=UTF-8"
            },
            url: "/base/check-standard-get-by-id.json",
            method: "post",
            isAuth: true,
            params: para
          })
          .then(function(res) {
            const data = res.data;
            if (data.code === 200) {
              self.formValidate = objCopy(data.data, {});
              self.appendixId = data.data.appendixId;
              self.sourceName = data.data.sourceName;
              self.data = data.data.itemList;
              for (let item of data.data.appendixList) {
                self.uploadList.push({
                  id: item.pkId,
                  sourceName: item.sourceName,
                  status: "finished"
                });
                self.appendixIds.push(item.pkId);
              }
              self.uploadList = self.$refs.upload.fileList;
            } else {
              self.$Message.error(res.data.info);
            }
          })
          .catch(function(err) {
            console.log(err);
          });
        self.uploadList = self.$refs.upload.fileList;
      } else {
        self.isModify = false;
        self.data = [];
        self.formValidate = {};
        self.formValidate.materialId = null;
        self.materialName = null;
        self.materialMap = null;
        self.$refs["formValidate"].resetFields();
        self.uploadList = self.$refs.upload.fileList;
      }
      self.itemModalshow = true;
    },

    /** 删除行 */
    delRow(idx) {
      this.data.splice(idx, 1);
    },
    addRow() {
      this.data.push({
        checkItem: "",
        itemStandard: "",
        checkOffset: ""
      });
    },

    getcheckTypeName(checkType) {
      let checkTypeName = "";
      if (checkType == 1) {
        checkTypeName = "新刀质检";
      } else if (checkType == 2 || checkType == 7) {
        checkTypeName = "刃磨质检";
      } else if (checkType == 3 || checkType == 8) {
        checkTypeName = "涂层质检";
      } else if (checkType == 4) {
        checkTypeName = "新夹具质检";
      } else if (checkType == 5) {
        checkTypeName = "夹具刃磨质检";
      } else if (checkType == 6) {
        checkTypeName = "夹具点检";
      }
      return checkTypeName;
    },
    //保存
    handleSave() {
      const self = this;
      self.$refs["formValidate"].validate(valid => {
        if (valid) {
          if (self.isModify) {
            self.updateCheckStandardSave();
          } else {
            self.addCheckStandardSave();
          }
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
    updateCheckStandardSave() {
      const self = this;
      if (!!!self.data || self.data.length < 1) {
        self.$Message.error("质检项不能为空!");
        return;
      }
      let i = 1;
      for (let item of self.data) {
        if (!!!item.checkItem) {
          self.$Message.error("第" + i + "行质检项不能空!");
          return;
        }
        if (!!!item.itemStandard) {
          self.$Message.error("第" + i + "行标准值不能空!");
          return;
        }
        i++;
      }
      self.isDisable = true;
      let para = {
        pkId: self.formValidate.pkId,
        materialNumber: self.formValidate.materialNumber,
        partCode: self.formValidate.partCode,
        standardDesc: self.formValidate.standardDesc,
        checkType: self.formValidate.checkType,
        checkTypeName: self.getcheckTypeName(self.formValidate.checkType),
        appendixIds: self.appendixIds.join(","),
        itemList: JSON.stringify(self.data)
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/x-www-form-urlencoded"
          },
          url: "/base/check-standard-update.json",
          method: "post",
          isAuth: true,
          data: qs.stringify(para)
        })
        .then(function(res) {
          self.itemModalshow = false;
          if (res.data.code === 200) {
            self.$Message.success("质检标准保存成功!");
            self.$emit("search");
          } else {
            self.$Notice.error({
              title: "错误提示",
              desc: res.data.info
            });
          }
          self.isDisable = false;
        })
        .catch(function(err) {
          console.log(err);
        });
    },

    searchMaterialById(val) {
      if (val == undefined) {
        return;
      }
      const self = this;
      const item = this.materialList.find(item => item.pkId === val);
      self.materialName = item.materialName;
      self.formValidate.materialId = item.pkId;
      self.materialMap = item.materialMap;
      self.formValidate.materialNumber = item.materialNumber;
    },
    searchMaterialByNumber() {
      const self = this;
      let materialNumber = self.formValidate.materialNumber;
      if(!!!materialNumber){
        return;
      }
      const item = self.materialList.find(
        item => item.materialNumber === materialNumber
      );
      if (!!item) {
        self.materialName = item.materialName;
        self.formValidate.materialId = item.pkId;
        self.materialMap = item.materialMap;
        self.formValidate.materialNumber = item.materialNumber;
      } else {
        self.materialName = null;
        self.formValidate.materialId = null;
        self.materialMap = null;
        self.formValidate.materialNumber = "";       
        self.$Message.error("您输入的物料不存在");
      }
    },
    addCheckStandardSave() {
      const self = this;
      if (!!!self.data || self.data.length < 1) {
        self.$Message.error("质检项不能为空!");
        return;
      }
      let i = 1;
      for (let item of self.data) {
        if (!!!item.checkItem) {
          self.$Message.error("第" + i + "行质检项不能空!");
          return;
        }
        if (!!!item.itemStandard) {
          self.$Message.error("第" + i + "行标准值不能空!");
          return;
        }
        i++;
      }
      self.isDisable = true;
      let para = {
        materialType: self.materialType,
        materialNumber: self.formValidate.materialNumber,
        materialName: self.materialName,
        materialMap: self.materialMap,
        partCode: self.partCode,
        standardDesc: self.formValidate.standardDesc,
        checkType: self.formValidate.checkType,
        checkTypeName: self.getcheckTypeName(self.formValidate.checkType),
        appendixIds: self.appendixIds.join(","),
        itemList: JSON.stringify(self.data)
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/x-www-form-urlencoded"
          },
          url: "/base/check-standard-add.json",
          method: "post",
          isAuth: true,
          data: qs.stringify(para)
        })
        .then(function(res) {
          self.itemModalshow = false;
          if (res.data.code === 200) {
            self.$Message.success("质检标准保存成功!");
            self.$emit("search");
          } else {
            self.$Notice.error({
              title: "错误提示",
              desc: res.data.info
            });
          }
          self.isDisable = false;
        })
        .catch(function(err) {
          console.log(err);
        });
    },

    //关闭弹窗
    closeModal() {
      const self = this;
      self.itemModalshow = false;
    },
    exportUrl() {
      let exportUrl;
      exportUrl =
        getBaseUrl() + "/tool/tool-appendix-upload.json?token=" + getToken();
      return exportUrl;
    },
    beforeUpload(file) {
      this.$refs.upload.clearFiles();
    },
    handleSuccess(response, file, fileList) {
      console.log(response);
      let self = this;
      if (response.code == 200) {
        self.$Message.success("文件上传成功!");
      }
    },
    handleRemove(file) {
      // 从 upload 实例删除数据
      const self = this;
      const fileList = this.$refs.upload.fileList;
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
            self.appendixIds.splice(self.appendixIds.indexOf(pkId), 1);
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
    handleFormatSuccess(res, file) {
      // 因为上传过程为实例，这里模拟添加 url
      this.appendixShowProgress = false;
      this.uploadList = [];
      this.uploadList.push({
        id: res.pkId,
        sourceName: res.sourceName,
        status: "finished"
      });
      this.appendixIds.push(res.pkId);
      // this.uploadList = this.$refs.upload.fileList;
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
      const check = this.uploadList.length < 1;
      if (!check) {
        this.$Notice.warning({
          title: "只能上传 1 个附件。"
        });
        this.appendixShowProgress = false;
      }
      return check;
    },
    importSuccess(res) {
      const self = this;
      // debugger;
      if (res.code === 200) {
        self.data = res.data;
        self.$Message.success("文件导入成功!");
      } else {
        self.$Notice.error({
          title: "错误提示",
          desc: res.data.info
        });
      }
    },
    importError() {
      const self = this;
      self.$Message.success("文件导入失败!");
    }
  },
  mounted() {
    this.addRow();
  }
};
</script>
<style>
/* .file-upload > .ivu-upload-list > .ivu-upload-list-file {
  line-height: 18px !important;
}
.file-upload > .ivu-upload-list > .ivu-upload-list-file > span {
  display: inline-block;
  overflow: hidden !important;
  text-overflow: ellipsis !important;
  white-space: nowrap !important;
  width: 90% !important;
} */
.file-upload
  > .ivu-upload-list
  > .ivu-upload-list-file
  > .ivu-upload-list-remove {
  float: none !important;
}
</style>