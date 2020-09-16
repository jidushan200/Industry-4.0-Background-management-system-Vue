<template>
  <div>
    <Card>
      <Row>
        <Form ref="formInline" :model="searchCondition" :label-width="65" inline>
          <Col span="16">
          <FormItem label="所属部门" prop="departmentId">
            <Select style="width:120px" v-model="searchCondition.departmentId">
              <Option v-for="item in departmentList" :value="item.pkId" :key="item.pkId">{{ item.departmentName }}</Option>
            </Select>
          </FormItem>
          <FormItem label="员工工号" prop="staffCode">
            <Input type="text" style="width:120px" v-model="searchCondition.staffCode" placeholder="请输入员工工号"></Input>
          </FormItem>
          <FormItem label="员工姓名" prop="staffName">
            <Input type="text" style="width:120px" v-model="searchCondition.staffName" placeholder="请输入员工姓名"></Input>
          </FormItem>
          <FormItem>
            <Button type="primary" style="margin-left: -60px;" @click="doSearch('formInline')">搜索</Button>
          </FormItem>
          <FormItem style="margin-left: -65px;">
            <Button type="warning" @click="synchro()">
              <Icon type="ios-download-outline"></Icon>同步
            </Button>
          </FormItem>
          <FormItem style="margin-left: -65px;">
            <Button type="dashed" @click="doReset('formInline')">重置</Button>
          </FormItem>
          </Col>
          <Col span="8">
          <FormItem style="width: 100%;text-align: right;">
            <Upload :show-upload-list="false" :action="uploadAction" :on-success="handleSuccess" :on-error="handleError" style="display: inline-block;margin-right: 5px;">
              <Button icon="ios-cloud-upload-outline">导入</Button>
            </Upload>
            <Button type="success" style="margin-right: 5px;" @click="addStaff">
              <Icon type="md-add"></Icon>添加
            </Button>
            <Button type="warning" @click="exportData()">
              <Icon type="ios-download-outline"></Icon>导出
            </Button>
          </FormItem>
          </Col>
        </Form>
      </Row>

      <Table ref="tablesMain" :data="tableData.rows" :columns="tableColumns" stripe border></Table>

      <div style="margin: 10px;overflow: hidden">
        <div style="float: right;">
          <Page :total="tableData.total" show-total :page-size="searchCondition.rows" :current="searchCondition.page" @on-change="changePage" @on-page-size-change="handlePageSize" show-sizer></Page>
        </div>
      </div>
    </Card>

    <Modal v-model="staffDetailModalshow" :title="isModify?'修改员工':'新增员工'" width="650" draggable>
      <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="80" inline>
        <FormItem label="员工编码" prop="staffCode">
          <Input style="width:170px" v-model="formValidate.staffCode" placeholder="输入员工编码" />
        </FormItem>
        <FormItem label="员工姓名" prop="staffName">
          <Input style="width:170px" v-model="formValidate.staffName" placeholder="输入员工姓名" />
        </FormItem>
        <FormItem label="员工职务" prop="position">
          <Input style="width:170px" v-model="formValidate.position" placeholder="输入员工职务" />
        </FormItem>
        <FormItem label="性别" prop="gender">
          <RadioGroup style="width: 170px" v-model="formValidate.gender">
            <Radio :label="1">男</Radio>
            <Radio :label="2">女</Radio>
          </RadioGroup>
        </FormItem>
        <FormItem label="所属部门" prop="departmentId">
          <Select style="width:170px" :label-in-value="true" v-model="formValidate.departmentId" ref="department" @on-change="v=>{departmentListSelectChange(v,'type')}">
            <Option v-for="item in departmentList" :value="item.pkId" :key="item.pkId" :label="item.departmentName">{{ item.departmentName }}</Option>
          </Select>
        </FormItem>
        <FormItem label="员工班组" prop="teamId">
          <Select style="width:170px" :label-in-value="true" v-model="formValidate.teamId" ref="team" @on-change="v=>{teamSelectChange(v,'type')}">
            <Option v-for="item in departmentTeamList" :value="item.pkId" :key="item.pkId" :label="item.teamName">{{ item.teamName }}</Option>
          </Select>
        </FormItem>
        <!--
        <FormItem label="是否领导" prop="isLeader">
          <RadioGroup style="width: 170px" v-model="formValidate.isLeader">
            <Radio :label="1">是</Radio>
            <Radio :label="2">不是</Radio>
          </RadioGroup>
        </FormItem>
        -->
        <FormItem label="说明" prop="remark">
          <Input style="width:430px" v-model="formValidate.remark" placeholder="输入员工说明" />
        </FormItem>
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
import staff_manage_form_config from "./staff-manage-form-config.js";
import { dateFormat, objCopy } from "@/libs/tools.js";
import { getToken, getBaseUrl, util } from "@/libs/util";

export default {
  data() {
    return {
      isDisabled: false,
      deleteModalShow: false, //控制删除modal提示 显示
      deleteRowData: {}, //行删除行数据
      isBatchDetele: false, //批量删除标志
      multiselectRowData: [], //复选列数据
      staffDetailModalshow: false, //控制明细modal显示
      isModify: false, //是否是修改明细操作
      uploadAction:
        getBaseUrl() + "/base/staff-import.json?token=" + getToken(),
      departmentList: [],
      teamList: [],
      departmentTeamList: [],
      searchCondition: {
        staffName: "",
        staffCode: "",
        page: 1,
        rows: 10
      },
      tableData: {
        total: 0, // 总页数
        rows: [] // 每页条数
      },
      tableColumns: [
        {
          title: "员工编码",
          key: "staffCode",
          minWidth: 100
        },
        {
          title: "员工姓名",
          key: "staffName",
          minWidth: 100
        },
        {
          title: "性别",
          key: "gender",
          render: function(h, params) {
            return h("div", params.row.gender == 1 ? "男" : "女");
          },
          minWidth: 80
        },
        {
          title: "部门名称",
          key: "departmentName",
          minWidth: 140
        },
        {
          title: "班组",
          key: "teamName",
          minWidth: 100
        },
        {
          title: "员工职务",
          key: "position",
          minWidth: 100
        },
        {
          title: "创建人",
          key: "createUserName",
          minWidth: 100
        },
        {
          title: "创建时间",
          key: "createTime",
          minWidth: 140,
          render: function(h, params) {
            return h(
              "div",
              dateFormat(new Date(params.row.createTime), "yyyy-MM-dd HH:mm")
            );
          }
        },
        {
          title: "最后修改人",
          key: "updateUserName",
          minWidth: 100
        },
        {
          title: "最后修改时间",
          key: "updateTime",
           minWidth: 140,
          render: function(h, params) {
            return h(
              "div",
              dateFormat(new Date(params.row.updateTime), "yyyy-MM-dd HH:mm")
            );
          }
        },
        {
          title: "操作",
          key: "action",
          width: 160,
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
      formValidate: staff_manage_form_config.formValidate, //user form表单字段
      ruleValidate: staff_manage_form_config.ruleValidate //user form表单验证规则
    };
  },
  created() {
    const self = this;
    self.getListData();
    self.getDepartmentListData();
    self.getTeamList();
  },
  methods: {
    getListData() {
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/base/staff-page-list",
          method: "post",
          isAuth: true,
          params: self.searchCondition
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

    synchro() {
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/base/staff-synchro",
          method: "post",
          isAuth: true,
          params: self.searchCondition
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            if (data.data === 1) {
              self.$Message.success("员工同步成功!");
              self.getListData();
            } else {
              self.$Message.error("员工没查到!");
            }
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },

    getDepartmentListData() {
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/base/staff-department-list",
          isAuth: true,
          method: "post"
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            self.departmentList = data.data;
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },
    getTeamList() {
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/base/team-get-list",
          isAuth: true,
          method: "post"
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            self.teamList = data.data;
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },
    addNewStaff() {
      this.isDisabled = true;
      let para = {
        staffName: this.formValidate.staffName,
        position: this.formValidate.position,
        staffCode: this.formValidate.staffCode,
        departmentId: this.formValidate.departmentId,
        departmentName: this.formValidate.departmentName,
        teamId: this.formValidate.teamId,
        teamName: this.formValidate.teamName,
        leader: this.formValidate.isLeader,
        gender: this.formValidate.gender,
        remark: this.formValidate.remark
      };
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/base/staff-add",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          if (res.data.code === 200) {
            self.staffDetailModalshow = false;
            self.$Message.success("员工新建成功!");
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
    editStaff() {
      this.isDisabled = true;
      let para = {
        pkId: this.formValidate.pkId,
        staffName: this.formValidate.staffName,
        position: this.formValidate.position,
        staffCode: this.formValidate.staffCode,
        departmentId: this.formValidate.departmentId,
        departmentName: this.formValidate.departmentName,
        teamId: this.formValidate.teamId,
        teamName: this.formValidate.teamName,
        leader: this.formValidate.isLeader,
        gender: this.formValidate.gender,
        remark: this.formValidate.remark
      };
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/base/staff-update",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          if (res.data.code === 200) {
            self.staffDetailModalshow = false;
            self.$Message.success("员工信息修改成功!");
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
    deleteRowDate(pkId) {
      let para = {
        pkId: pkId
      };
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/base/staff-delete-by-id",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          self.deleteModalShow = false;
          if (res.data.code === 200) {
            self.$Message.success("人员信息删除成功!");
            self.getListData();
          }
        })
        .catch(function(err) {
          console.log(err);
        });
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
      self.changePage(1);
    },
    show(index) {
      const self = this;
      self.staffDetailModalshow = true;
      self.isModify = true;
      self.formValidate = objCopy(self.tableData.rows[index], {});
      self.departmentTeamList = [];
      for (let team of self.teamList) {
        if (team.departmentId == self.formValidate.departmentId) {
          self.departmentTeamList.push(team);
        }
      }
    },
    addStaff() {
      this.staffDetailModalshow = true;
      this.isModify = false;
      this.formValidate = {
        gender: 1,
        isLeader: 2
      };
      this.$refs["formValidate"].resetFields();
    },
    remove(index) {
      this.deleteRowData = this.tableData.rows[index];
      this.deleteModalShow = true;
      this.isBatchDetele = false;
    },
    deleteModalSureBtn() {
      if (this.isBatchDetele) {
        let ids = [];
        for (const item of this.multiselectRowData) {
          ids.push(item.pkId);
        }
      } else {
        this.deleteRowDate(this.deleteRowData.pkId);
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
    departmentListSelectChange(item, type) {
      const self = this;
      self.formValidate.departmentName = item.label;
      self.departmentTeamList = [];
      for (let team of self.teamList) {
        if (team.departmentId == item.value) {
          self.departmentTeamList.push(team);
        }
      }
    },
    teamSelectChange(item, type) {
      const self = this;
      self.formValidate.teamName = item.label;
    },
    exportData() {
      const self = this;
      window.location.href =
        getBaseUrl() + "/base/staff-export?token=" + getToken();
    },
    handleSuccess() {
      const self = this;
      self.getListData();
      self.$Message.success("文件导入成功!");
    },
    handleError() {
      const self = this;
      self.getListData();
      self.$Message.success("文件导入失败!");
    },
    //form 校验方法
    handleSubmit(name) {
      this.$refs[name].validate(valid => {
        if (valid) {
          if (this.isModify) {
            this.editStaff();
          } else {
            this.addNewStaff();
          }
        } else {
          this.$Message.error("请输入正确信息!");
        }
      });
    },
    handleReset(name) {
      this.staffDetailModalshow = false;
      this.$refs[name].resetFields();
    }
  }
};
</script>

<style>
</style>
