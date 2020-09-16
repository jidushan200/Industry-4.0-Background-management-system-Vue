<template>
  <Modal v-model="complexOutBoundModalshow" class="modal-class" title="组合出库" width="930" draggable>
    <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="100" inline>
      <Row>
        <Col span="8">
          <FormItem label="刀条组合" prop="headNumber">
            <Select
              style="width:200px"
              v-model="formValidate.headNumber"
              ref="headNumber"
              placeholder="请选择刀条组合"
              @on-change="headSelectChange"
            >
              <Option
                v-for="item in headList"
                :value="item.headNumber"
                :key="item.pkId"
              >{{ item.headName }}</Option>
            </Select>
          </FormItem>
        </Col>
        <Col span="8">
          <FormItem label="刀条组合条码" prop="composeNumber">
            <Input style="width:200px" v-model="composeNumber" readonly />
          </FormItem>
        </Col>
      </Row>
      <Row>
        <Table
          ref="myTable"
          border
          :columns="columns"
          :data="headBladList"
          height="240"
          style="margin-bottom:5px;"
        ></Table>
      </Row>
      <Row>
        <Col span="8">
          <FormItem label="刀盘" prop="plateNumber">
            <Select
              style="width:200px"
              v-model="formValidate.plateNumber"
              ref="plateNumber"
              placeholder="请选择刀盘"
              clearable
              filterable
            >
              <Option
                v-for="item in plateList"
                :value="item.plateNumber"
                :key="item.pkId"
              >{{ item.plateNumber }}</Option>
            </Select>
          </FormItem>
        </Col>
        <Col span="16">
          <FormItem label="领用人职工号" prop="staffCode">
            <Input
              style="width:200px"
              v-model="formValidate.staffCode"
              placeholder="输入职工号"
              @on-blur="searchStaff()"
            >
              <Icon type="ios-search" slot="suffix" />
            </Input>&nbsp;
            <Input style="width:230px" v-model="staffInfo" readonly></Input>
          </FormItem>
        </Col>
      </Row>
      <Row> 
        <Col span="24">
          <FormItem label="说明" prop="remark">
            <Input style="width:540px" v-model="formValidate.remark" placeholder="输入说明" />
          </FormItem>
        </Col>
      </Row>
    </Form>
    <div slot="footer">
      <Button @click="handleSubmit('formValidate')" type="primary" :disabled="isDisabled">出库</Button>
      <Button @click="closeModal('formValidate')" style="margin-left: 8px">取消</Button>
    </div>
  </Modal>
</template>
<script>
import { dateFormat, objCopy } from "@/libs/tools.js";
import { getToken, getBaseUrl } from "@/libs/util";
import axios from "@/libs/api.request";
import qs from "qs";
import tool_blade_form_config from "./tool-blade-form-config.js";
export default {
  data() {
    return {
      isDisabled: false,
      complexOutBoundModalshow: false,
      composeNumber: "",
      plateList: [],
      toolName: "",
      toolMap: "",
      staffInfo: "",
      headList: [],
      headBladList: [],
      columns: [
        {
          title: "刀条编码",
          key: "toolNumber",
          width: "150px"
        },
        {
          title: "刀条名称",
          key: "toolName",
          width: "200px"
        },
        {
          title: "刀条图号",
          key: "toolMap",
          width: "120px"
        },
        {
          title: "库存数量",
          key: "inventoryQty",
          width: "100px"
        },
        {
          title: "标准数量",
          key: "toolQty",
          width: "100px"
        },
        {
          title: "领用数量",
          key: "useQty",
          width: "100px"
        },
        {
          title: "操作",
          key: "action",
          width: 100,
          align: "center",
          fixed: "right",
          render: (h, params) => {
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
                      this.delete(params.row, 1);
                    }
                  }
                },
                "删除"
              )
            ]);
          }
        }
      ],
      formValidate: tool_blade_form_config.formValidate, // form表单字段
      ruleValidate: tool_blade_form_config.ruleValidate // form表单验证规则
    };
  },
  created() {
    const self = this;
    self.getHeadList();
    self.getPlateList();
  },
  init() {
    const self = this;
  },
  methods: {
    showModal() {
      const self = this;
      self.complexOutBoundModalshow = true;
      self.formValidate = {};
      self.$refs["formValidate"].resetFields();
    },

    getHeadList() {
      const self = this;
      let para = {
        headType: 2
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/head-list",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            self.headList = data.data;
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },

    getPlateList() {
      const self = this;
      let para = {
        useStatus: 2
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/plate-list",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            self.plateList = data.data;
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },

    searchStaff() {
      const self = this;
      if (!self.formValidate.staffCode) {
        return;
      }
      let para = {
        staffCode: self.formValidate.staffCode
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/base/staff-get-by-code.json",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            if (data.data !== null && data.data !== undefined) {
              self.departmentId = data.data.departmentId;
              self.departmentName = data.data.departmentName;
              self.teamId = data.data.teamId;
              self.teamName = data.data.teamName;
              self.receiver = data.data.staffName;
              self.staffInfo = self.departmentName + "-" + self.receiver;
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

    headSelectChange(headNumber) {
      const self = this;
      if (!headNumber) {
        return;
      }
      let para = {
        headNumber: headNumber
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/get-complex-head-blade-list.json",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          const { data } = res;
          if (data.code === 200) {
            self.headBladList = data.data;
            if (data.data.length > 0) {
              self.createComposeNumber(headNumber);
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

    createComposeNumber(headNumber) {
      const self = this;
      // self.composeNumber = "";
      // self.formValidate.composeNumber = "";
      let para = {
        headNumber: headNumber
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/create-compose-number",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            self.composeNumber = data.data;
            self.formValidate.composeNumber = data.data;
          } else {
            self.$Message.error(data.info);
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },

    // 保存
    handleSubmit() {
      const self = this;
      const someFlag = self.computeBladList();
      self.$refs["formValidate"].validate(valid => {
        if (valid && someFlag === false) {
          this.isDisabled = true;
          let para = {
            headId: self.formValidate.headId,
            composeNumber: self.formValidate.composeNumber,
            headNumber: self.formValidate.headNumber,
            plateNumber: self.formValidate.plateNumber,
            departmentId: self.departmentId,
            departmentName: self.departmentName,
            teamId: self.teamId,
            teamName: self.teamName,
            receiver: self.receiver,
            remark: self.formValidate.remark,
            bladeJson: JSON.stringify(self.headBladList)
          };
          axios
            .request({
              headers: {
                "Content-Type": "application/x-www-form-urlencoded"
              },
              url: "/tool/blade-complex-outbound.json",
              method: "post",
              isAuth: true,
              data: qs.stringify(para)
            })
            .then(function(res) {
              self.complexOutBoundModalshow = false;
              if (res.data.code === 200) {
                self.$Message.success("组合出库成功!");
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
          self.$Message.error("请输入正确信息!");
        }
      });
    },
    computeBladList() {
      // 判断库存数量是否小于标准数量
      return this.headBladList.some(function(item) {
        return item.inventoryQty < item.toolQty;
      });
    },

    // 关闭弹窗
    closeModal() {
      const self = this;
      self.complexOutBoundModalshow = false;
    }
  }
};
</script>
