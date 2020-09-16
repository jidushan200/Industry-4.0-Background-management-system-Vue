<template>
  <!--审核信息弹出modal-->
  <Modal v-model="warehouseDetailModalshow" title="新模具" width="600" draggable>
    <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="100" inline>
      <FormItem label="模具编码" prop="mouldNumber">
        <Input
          style="width:170px"
          v-model="formValidate.mouldNumber"
          @on-blur="searchMould()"
          placeholder="输入物料编码"
        >
          <Icon type="ios-search" slot="suffix" />
        </Input>
      </FormItem>
      <FormItem label="模具序号" prop="mouldSeq">
        <Input style="width:170px" v-model="mouldSeq" readonly></Input>
      </FormItem>
      <FormItem label="模具图号" prop="mouldMap">
        <Input style="width:170px" v-model="mouldMap" readonly />
      </FormItem>
      <FormItem label="模具名称" prop="mouldName">
        <Input style="width:170px" v-model="mouldName" readonly />
      </FormItem>
      <FormItem label="最大生产寿命" prop="lifeMax">
        <Input style="width:170px" v-model="lifeMax" readonly />
      </FormItem>
      <FormItem label="模具坯名称" prop="embryoId">
        <Select
          style="width:170px"
          v-model="formValidate.embryoId"
          ref="embryoId"
          @on-change="embryoListSelectChange"
          clearable
          filterable
        >
          <Option
            v-for="item in embryoList"
            :value="item.pkId"
            :key="item.pkId"
          >{{ item.embryoCode }}-{{ item.embryoName }}</Option>
        </Select>
      </FormItem>
      <FormItem label="热处理批次号" prop="heatNumber">
        <Input style="width:170px" v-model="formValidate.heatNumber" placeholder="输入热处理批次号" />
      </FormItem>
      <FormItem label="表面处理批次号" prop="surfaceNumber">
        <Input style="width:170px" v-model="formValidate.surfaceNumber" placeholder="输入表面处理批次号" />
      </FormItem>
    </Form>
    <div slot="footer">
      <Button @click="handlePrint()" type="info" :disabled="isDisabled" v-if="mouldSeq">条码</Button>
      <Button @click="handleSubmit('formValidate')" type="primary" :disabled="isDisabled">提交</Button>
      <Button @click="handleReset('formValidate')" style="margin-left: 8px">取消</Button>
    </div>
  </Modal>
</template>
<script>
import { dateFormat, objCopy } from "@/libs/tools.js";
import axios from "@/libs/api.request";
import { getToken, getBaseUrl } from "@/libs/util";
import add_form_config from "./add-form-config.js";
export default {
  data() {
    return {
      isDisabled: false,
      embryoList: [],
      mouldType: "",
      mouldSeq: "",
      mouldMap: "",
      mouldName: "",
      lifeMax: "",
      warehouseDetailModalshow: false,
      formValidate: add_form_config.formValidate, //form表单字段
      ruleValidate: add_form_config.ruleValidate //form表单验证规则
    };
  },
  created() {},
  methods: {
    showModal() {
      const self = this;
      self.formValidate = {};
      self.warehouseDetailModalshow = true;
      self.mouldSeq = "";
      self.mouldMap = "";
      self.mouldName = "";
      self.lifeMax = "";
      self.getMouldEmbryoList();
    },
    getMouldEmbryoList() {
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/mould/get-embryo-list",
          isAuth: true,
          method: "post",
          params: []
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            self.embryoList = data.data;
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },
    getMouldSeq(para) {
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/mould/get-mould-seq.json",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            self.mouldSeq = data.data.mouldSeq;
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
    searchMould() {
      const self = this;
      if (!!!self.formValidate.mouldNumber) {
        return;
      } else {
        let para = {
          mouldNumber: self.formValidate.mouldNumber
        };
        self.getMouldSeq(para);
        axios
          .request({
            headers: {
              "Content-Type": "application/json; charset=UTF-8"
            },
            url: "/mould/mould-base-get-by-number.json",
            method: "post",
            isAuth: true,
            params: para
          })
          .then(function(res) {
            const data = res.data;
            if (data.code === 200) {
              if (data.data === null || data.data === undefined) {
                self.$Message.error("无法找到模具基础信息");
              } else {
                if (!!data.data.mouldMap) {
                  self.mouldMap = data.data.mouldMap;
                  self.formValidate.mouldMap = self.mouldMap;
                  self.mouldName = data.data.mouldName;
                  self.formValidate.mouldName = self.mouldName;
                  self.lifeMax = data.data.lifeMax;
                  self.mouldType = data.data.mouldType;
                } else {
                  self.formValidate.mouldNumber = "";
                  self.$Message.error("请维护模具图号后再进行入库操作");
                }
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
    },
    handleSubmit() {
      const self = this;
      this.$refs["formValidate"].validate(valid => {
        if (valid) {
          self.isDisabled = true;
          let para = {
            mouldNumber: self.formValidate.mouldNumber,
            mouldType: self.mouldType,
            mouldSeq: self.mouldSeq,
            mouldName: self.mouldName,
            mouldMap: self.mouldMap,
            lifeMax: self.lifeMax,
            heatNumber: self.formValidate.heatNumber,
            surfaceNumber: self.formValidate.surfaceNumber,
            embryoCode: self.formValidate.embryoCode,
            embryoName: self.formValidate.embryoName,
            fullNumber:self.formValidate.mouldNumber+'-'+self.mouldSeq+'/'+self.mouldMap
          };
          axios
            .request({
              headers: {
                "Content-Type": "application/json; charset=UTF-8"
              },
              url: "/mould/mould-receipt",
              method: "post",
              isAuth: true,
              params: para
            })
            .then(function(res) {
              if (res.data.code === 200) {
                self.warehouseDetailModalshow = false;
                self.$Message.success("模具到货成功!");
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
        } else {
          this.$Message.error("请输入正确信息!");
        }
      });
    },
    embryoListSelectChange(item) {
      const self = this;
      let embryoList = self.embryoList;
      if (item !== undefined) {
        for (const embryo of embryoList) {
          if (embryo.pkId === item) {
            self.formValidate.embryoCode = embryo.embryoCode;
            self.formValidate.embryoName = embryo.embryoName;
          }
        }
      }
    },
    handleReset() {
      const self = this;
      self.warehouseDetailModalshow = false;
    },
    handlePrint() {
      const self = this;
      let mouldNumber = self.formValidate.mouldNumber;
      let mouldSeq = self.mouldSeq;
      if (!!!mouldNumber || !!!mouldSeq) {
        self.$Message.error("编号和图号不能为空");
        return;
      }
      window.open(
        "http://10.36.10.11:8080/decision/view/report?viewlet=mould_base.cpt&mouldNumber=" +
          mouldNumber +
          "&mouldSeq=" +
          mouldSeq,
        "newwindow",
        "height=360,width=600,top=0,left=0,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no"
      );
    }
  }
};
</script>
