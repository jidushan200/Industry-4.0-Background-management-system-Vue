
<template>
  <div>
    <Card>
      <Row>
        <Form ref="formInline" :model="searchCondition" :label-width="65" inline>
          <Col span="16">
            <FormItem label="夹具编号" prop="fixtureNumber">
              <Input type="text" v-model="searchCondition.fixtureNumber" placeholder="请输入夹具编号"></Input>
            </FormItem>
            <FormItem label="夹具图号" prop="fixtureMap">
              <Select
                v-model="searchCondition.fixtureMap"
                ref="checkType"
                style="width:158px;"
                placeholder="请选择夹具图号名称"
                clearable
                filterable
              >
                <Option
                  v-for="item in fixtureList"
                  :value="item.fixtureMap"
                  :key="item.fixtureMap"
                >{{ item.fixtureMap }} {{ item.fixtureName }}</Option>
              </Select>
            </FormItem>
            <FormItem label="夹具名称" prop="fixtureName">
              <Input type="text" v-model="searchCondition.fixtureName" placeholder="请输入夹具名称"></Input>
            </FormItem>
            <FormItem label="夹具类型" prop="fixtureType">
              <Select
                v-model="searchCondition.fixtureType"
                ref="checkType"
                style="width:158px;"
                placeholder="请选择夹具类型"
              >
                <Option
                  v-for="item in fixtureTypeList"
                  :value="item.value"
                  :key="item.value"
                >{{ item.name }}</Option>
              </Select>
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
              <Button style type="dashed" @click="doReset('formInline')">重置</Button>
            </FormItem>
          </Col>
          <Col span="8">
            <FormItem style="width:100%;text-align:right">
              <Upload
                :show-upload-list="false"
                :action="uploadAction"
                :on-success="handleSuccess"
                :on-error="handleError"
                style="display: inline-block;margin-right: 5px;"
              >
                <Button icon="ios-cloud-upload-outline">导入</Button>
              </Upload>
              <Button type="success" style="margin-right: 5px;" @click="addMeasureBase">
                <Icon type="md-add"></Icon>添加
              </Button>
              <Button type="warning" @click="exportData()">
                <Icon type="ios-download-outline"></Icon>导出
              </Button>
            </FormItem>
          </Col>
        </Form>
      </Row>

      <Table
        ref="tablesMain"
        show-total
        :data="tableData.rows"
        :columns="tableColumns"
        stripe
        border
      ></Table>

      <div style="margin: 10px;overflow: hidden">
        <div style="float: right;">
          <Page
            show-total
            :total="tableData.total"
            :page-size="searchCondition.rows"
            :current="searchCondition.page"
            @on-change="changePage"
            @on-page-size-change="handlePageSize"
            show-sizer
          ></Page>
        </div>
      </div>
    </Card>

    <Modal
      v-model="fixtureBaseDetailModalshow"
      :title="isModify?'修改夹具':'新增夹具'"
      width="950"
      draggable
    >
      <Form
        ref="formValidate"
        :model="formValidate"
        :rules="ruleValidate"
        :label-width="100"
        inline
      >
        <row>
          <Col span="8">
            <FormItem label="夹具编码" prop="fixtureNumber">
              <Input style="width:170px" v-model="formValidate.fixtureNumber" placeholder="输入夹具编码" />
            </FormItem>
          </Col>
          <Col span="8">
            <FormItem label="夹具名称" prop="fixtureName">
              <Input style="width:170px" v-model="formValidate.fixtureName" placeholder="输入夹具名称" />
            </FormItem>
          </Col>
          <Col span="8">
            <FormItem label="图号" prop="fixtureMap">
              <Input style="width:170px" v-model="formValidate.fixtureMap" placeholder="输入图号" />
            </FormItem>
          </Col>
        </row>
        <row>
          <Col span="8">
            <FormItem label="夹具类型" prop="fixtureType">
              <RadioGroup v-model="formValidate.fixtureType">
                <Radio :label="1">组合</Radio>
                <Radio :label="2">配件</Radio>
              </RadioGroup>
            </FormItem>
          </Col>
          <Col span="8">
            <FormItem label="价格" prop="price">
              <Input style="width:170px" v-model="formValidate.price" placeholder="输入价格" />
            </FormItem>
          </Col>
        </row>
        <Divider>
          <Button @click="addRow" type="primary">添加配件</Button>
        </Divider>
        <Table
          :columns="childColumns"
          :data="childfixtureList"
          border
          stripe
          highlight-row
          height="300"
        ></Table>
      </Form>
      <div slot="footer">
        <Button @click="handleSubmit('formValidate')" type="primary" :disabled="isDisable">保存</Button>
        <Button @click="handleReset('formValidate')" style="margin-left: 8px">取消</Button>
      </div>
    </Modal>

    <Modal
      v-model="deleteModalShow"
      width="250"
      :title="isBatchDetele?'批量删除':'删除'"
      @on-ok="deleteModalSureBtn"
      @on-cancel="deleteModalCancelBtn"
    >
      <p>确定删除所选项？</p>
    </Modal>
  </div>
</template>

<script>
import axios from "@/libs/api.request";
import fixture_base_manage_form_config from "./fixture-base-manage-form-config.js";
import { dateFormat, objCopy } from "@/libs/tools.js";
import qs from "qs";
import { getToken, getBaseUrl } from "@/libs/util";
export default {
  data() {
    return {
      isDisable: false,
      deleteModalShow: false, //控制删除modal提示 显示
      deleteRowData: {}, //行删除行数据
      isBatchDetele: false, //批量删除标志
      multiselectRowData: [], //复选列数据
      fixtureBaseDetailModalshow: false, //控制明细modal显示
      isModify: false, //是否是修改明细操作
      uploadAction:
        getBaseUrl() + "/fixture/fixture-base-import.json?token=" + getToken(),
      childfixtureList: [],
      fList: [],
      fixtureList: [],
      fixtureTypeList: [
        {
          name: "组合",
          value: 1
        },
        {
          name: "配件",
          value: 2
        }
      ],
      searchCondition: {
        fixtureNumber: "",
        fixtureMap: "",
        fixtureType: null,
        page: 1,
        rows: 10
      },
      tableData: {
        total: 0, // 总页数
        rows: [] // 每页条数
      },

      childColumns: [
        {
          title: "配件图号",
          key: "fixtureMap",
          minWidth: 280,
          render: (h, params) => {
            return h(
              "Select",
              {
                props: {
                  clearable: true,
                  filterable: true,
                  value: this.childfixtureList[params.index].fixtureMap
                },
                on: {
                  "on-change": event => {
                    this.childfixtureList[params.index].fixtureMap = event;
                    let i = 0;
                    for (let it of this.childfixtureList) {
                      if (
                        !!it.fixtureMap &&
                        event == it.fixtureMap &&
                        i != params.index
                      ) {
                        this.$Message.error("与第" + (i + 1) + "行配件重复!");
                        this.childfixtureList[params.index].fixtureMap = "";
                        this.childfixtureList[params.index].fixtureName = "";
                        this.childfixtureList[params.index].fixtureNumber = "";
                        return;
                      }
                      i++;
                    }
                    for (let item of this.fList) {
                      if (item.fixtureMap == event) {
                        this.childfixtureList[params.index].fixtureName =
                          item.fixtureName;
                        this.childfixtureList[params.index].fixtureNumber =
                          item.fixtureNumber;
                        return;
                      }
                    }
                  }
                }
              },
              this.fList.map(item => {
                return h("Option", {
                  props: {
                    key: item.pkId,
                    value: item.fixtureMap,
                    label: item.fixtureMap
                  }
                });
              })
            );
          }
        },
        {
          title: "配件编号",
          key: "fixtureNumber",
          minWidth: 160,
          render: (h, params) => {
            return h("Input", {
              props: {
                value: this.childfixtureList[params.index].fixtureNumber,
                readonly: true
              }
            });
          }
        },
        {
          title: "配件名称",
          key: "fixtureName",
          minWidth: 280,
          render: (h, params) => {
            return h("Input", {
              props: {
                value: this.childfixtureList[params.index].fixtureName,
                readonly: true
              }
            });
          }
        },

        {
          title: "标准数量",
          key: "fixtureQty",
          width: 120,
          render: (h, params) => {
            return h("Input", {
              props: {
                value: this.childfixtureList[params.index].fixtureQty
              },
              on: {
                input: val => {
                  this.childfixtureList[params.index].fixtureQty = val;
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
      tableColumns: [
        {
          title: "夹具编码",
          key: "fixtureNumber",
          minWidth: 130
        },
        {
          title: "夹具名称",
          key: "fixtureName",
          minWidth: 320
        },
        {
          title: "图号",
          key: "fixtureMap",
          minWidth: 240
        },
        {
          title: "类型",
          key: "fixtureType",
          minWidth: 100,
          render: function(h, params) {
            let temp = "";
            switch (params.row.fixtureType) {
              case 1:
                temp = "组合";
                break;
              case 2:
                temp = "配件";
                break;
              default:
                break;
            }
            return h("div", temp);
          }
        },
        {
          title: "价格",
          key: "price",
          minWidth: 100
        },
        {
          title: "配件",
          align: "center",
          children: [
            {
              title: "配件编号",
              key: "childList",
              align: "center",
              width: 120,
              render: (h, params) => {
                return h(
                  "div",
                  {
                    attrs: {
                      class: "subCol"
                    }
                  },
                  [
                    h(
                      "ul",
                      undefined != this.tableData.rows[params.index].childList
                        ? this.tableData.rows[params.index].childList.map(
                            item => {
                              return h("li", {}, item.fixtureNumber);
                            }
                          )
                        : ""
                    )
                  ]
                );
              }
            },
            {
              title: "配件名称",
              key: "childList",
              align: "center",
              width: 280,
              render: (h, params) => {
                return h(
                  "div",
                  {
                    attrs: {
                      class: "subCol"
                    }
                  },
                  [
                    h(
                      "ul",
                      undefined != this.tableData.rows[params.index].childList
                        ? this.tableData.rows[params.index].childList.map(
                            item => {
                              return h("li", {}, item.fixtureName);
                            }
                          )
                        : ""
                    )
                  ]
                );
              }
            },
            {
              title: "配件图号",
              key: "childList",
              align: "center",
              width: 200,
              render: (h, params) => {
                return h(
                  "div",
                  {
                    attrs: {
                      class: "subCol"
                    }
                  },
                  [
                    h(
                      "ul",
                      undefined != this.tableData.rows[params.index].childList
                        ? this.tableData.rows[params.index].childList.map(
                            item => {
                              return h("li", {}, item.fixtureMap);
                            }
                          )
                        : ""
                    )
                  ]
                );
              }
            },
            {
              title: "配件数量",
              key: "childList",
              align: "center",
              width: 100,
              render: (h, params) => {
                return h(
                  "div",
                  {
                    attrs: {
                      class: "subCol"
                    }
                  },
                  [
                    h(
                      "ul",
                      undefined != this.tableData.rows[params.index].childList
                        ? this.tableData.rows[params.index].childList.map(
                            item => {
                              return h("li", {}, item.fixtureQty);
                            }
                          )
                        : ""
                    )
                  ]
                );
              }
            }
          ]
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
      formValidate: fixture_base_manage_form_config.formValidate, //user form表单字段
      ruleValidate: fixture_base_manage_form_config.ruleValidate //user form表单验证规则
    };
  },
  created() {
    const self = this;
    self.getListData();
    self.getAllFixtureListData();
    self.getFixtureListData();
  },
  methods: {
    getListData() {
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/fixture/fixture-base-page-list",
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

    getFixtureListData() {
      const self = this;
      let para = {
        fixtureType: 2
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/fixture/get-base-list",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            self.fList = data.data;
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },

    getAllFixtureListData() {
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/fixture/get-base-list",
          method: "post",
          isAuth: true
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            self.fixtureList = data.data;
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
          url: "/fixture/fixture-base-synchro",
          method: "post",
          isAuth: true,
          params: self.searchCondition
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            if (data.data === 1) {
              self.$Message.success("夹具同步成功!");
              self.getListData();
            } else {
              self.$Message.error("夹具没查到!");
            }
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },

    addNewFixtureBase() {
      const self = this;
      let childList = null;
      if (self.formValidate.fixtureType == 1) {
        childList = JSON.stringify(self.childfixtureList);
      }
      let para = {
        fixtureNumber: self.formValidate.fixtureNumber,
        fixtureName: self.formValidate.fixtureName,
        fixtureMap: self.formValidate.fixtureMap,
        fixtureType: self.formValidate.fixtureType,
        price: self.formValidate.price,
        childList: childList
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/x-www-form-urlencoded"
          },
          url: "/fixture/fixture-base-add",
          isAuth: true,
          method: "post",
          data: qs.stringify(para)
        })
        .then(function(res) {
          if (res.data.code === 200) {
            self.fixtureBaseDetailModalshow = false;
            self.$Message.success("夹具新建成功!");
            self.getListData();
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
    editFixtureBase() {
      const self = this;
      let childList = null;
      if (self.formValidate.fixtureType == 1) {
        childList = JSON.stringify(self.childfixtureList);
      }
      let para = {
        pkId: self.formValidate.pkId,
        fixtureNumber: self.formValidate.fixtureNumber,
        fixtureName: self.formValidate.fixtureName,
        fixtureMap: self.formValidate.fixtureMap,
        fixtureType: self.formValidate.fixtureType,
        price: self.formValidate.price,
        childList: childList
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/x-www-form-urlencoded"
          },
          url: "/fixture/fixture-base-update",
          method: "post",
          isAuth: true,
          data: qs.stringify(para)
        })
        .then(function(res) {
          if (res.data.code === 200) {
            self.fixtureBaseDetailModalshow = false;
            self.$Message.success("夹具信息修改成功!");
            self.getListData();
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
    deleteRowDate(pkId, fixtureNumber) {
      let para = {
        pkId: pkId,
        fixtureNumber: fixtureNumber
      };
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/fixture/fixture-base-delete-by-id",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          self.deleteModalShow = false;
          if (res.data.code === 200) {
            self.$Message.success("夹具信息删除成功!");
            self.getListData();
          } else {
            self.$Message.error(res.data.info);
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
          this.deleteRowData.fixtureNumber
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
      self.changePage(1);
    },
    show(index) {
      const self = this;
      self.fixtureBaseDetailModalshow = true;
      self.isModify = true;
      self.formValidate = objCopy(self.tableData.rows[index], {});
      self.childfixtureList = [];
      if (self.formValidate.fixtureType == 1) {
        self.getFixtureById(self.formValidate.pkId);
      }
    },
    remove(index) {
      this.deleteRowData = this.tableData.rows[index];
      this.deleteModalShow = true;
      this.isBatchDetele = false;
    },
    addMeasureBase() {
      this.fixtureBaseDetailModalshow = true;
      this.isModify = false;
      this.formValidate = {};
      this.$refs["formValidate"].resetFields();
    },
    getFixtureById(pkId) {
      let para = {
        pkId: pkId,
        postType: 2
      };
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/fixture/fixture-base-get-by-id",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          if (res.data.code === 200) {
            self.childfixtureList = res.data.data.childList;
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },
    /** 删除行 */
    delRow(idx) {
      const self = this;
      debugger;
      self.childfixtureList.splice(idx, 1);
    },
    addRow() {
      const self = this;
      self.childfixtureList.push({
        fixtureNumber: "",
        fixtureName: "",
        fixtureMap: "",
        fixtureQty: ""
      });
    },
    exportData() {
      const self = this;
      let fixtureType = self.searchCondition.fixtureType;
      if (!!fixtureType) {
        window.location.href =
          getBaseUrl() +
          "/fixture/fixture-base-export?fixtureType=" +
          fixtureType +
          "&token=" +
          getToken();
      } else {
        window.location.href =
          getBaseUrl() + "/fixture/fixture-base-export?token=" + getToken();
      }
    },
    handleSuccess(res) {
      const self = this;
      if (res.code == 200) {
        self.$Message.success("文件导入成功!");
        self.getListData();
      } else {
        self.$Message.error(res.info);
      }
    },
    handleError() {
      const self = this;
      self.$Message.success("文件导入失败!");
    },
    //form 校验方法
    handleSubmit(name) {
      const self = this;
      self.$refs[name].validate(valid => {
        if (valid) {
          if (self.formValidate.fixtureType == 1) {
            if (!!!self.childfixtureList || self.childfixtureList.length < 1) {
              self.$Message.error("配件数量不能为空!");
              return;
            }
            let i = 1;
            for (let item of self.childfixtureList) {
              if (!!!item.fixtureMap) {
                self.$Message.error("第" + i + "行配件图号不能空!");
                return;
              }
              if (!!!item.fixtureQty) {
                self.$Message.error("第" + i + "行配件数量不能空!");
                return;
              }
              i++;
            }
          }
          if (
            self.formValidate.fixtureType == 2 &&
            self.childfixtureList.length > 0
          ) {
            self.$Message.error("请删除配件后提交!");
            return;
          }
          self.isDisable = true;
          debugger;
          if (self.isModify) {
            self.editFixtureBase();
          } else {
            self.addNewFixtureBase();
          }
        } else {
          self.$Message.error("请输入正确信息!");
        }
      });
    },
    handleReset(name) {
      this.fixtureBaseDetailModalshow = false;
      this.$refs[name].resetFields();
    }
  }
};
</script>


<style>
.subCol > ul > li {
  margin: 0 -18px;
  list-style: none;
  text-align: center;
  padding: 9px;
  border-bottom: 1px solid #ccc;
  overflow-x: hidden;
}
.subCol > ul > li:last-child {
  border-bottom: none;
}
</style>