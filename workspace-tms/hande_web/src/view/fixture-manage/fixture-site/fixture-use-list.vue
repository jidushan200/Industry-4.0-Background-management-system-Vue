
<template>
  <div>
    <Card>
      <Row>
        <Form ref="formInline" :model="searchCondition" :label-width="65" inline>
          <Col span="16">
            <FormItem label="夹具编码" prop="fixtureNumber">
              <Input type="text" v-model="searchCondition.fixtureNumber" placeholder="请输入夹具编码"></Input>
            </FormItem>
            <FormItem label="夹具名称" prop="fixtureName">
              <Input type="text" v-model="searchCondition.fixtureName" placeholder="请输入夹具名称"></Input>
            </FormItem>
            <FormItem label="夹具图号" prop="fixtureMap">
              <Input type="text" v-model="searchCondition.fixtureMap" placeholder="请输入夹具图号"></Input>
            </FormItem>
            <FormItem prop="department" label="所属部门">
              <Select style="width:158px" v-model="searchCondition.departmentId" ref="department">
                <Option
                  v-for="item in departmentList"
                  :value="item.pkId"
                  :key="item.pkId"
                >{{ item.departmentName }}</Option>
              </Select>
            </FormItem>
            <FormItem>
              <Button type="primary" style="margin-left: -60px;" @click="doSearch('formInline')">搜索</Button>
            </FormItem>
            <FormItem style="margin-left: -65px;">
              <Button style type="dashed" @click="doReset('formInline')">重置</Button>
            </FormItem>
          </Col>
          <Col span="8">
            <FormItem style="width:100%;text-align:right">
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
    <Modal v-model="returnModalshow" class="modal-class" title="夹具交回" width="700" draggable>
      <Form
        ref="formValidate"
        :model="formValidate"
        :rules="ruleValidate"
        :label-width="100"
        inline
      >
        <Row>
          <Col span="12">
            <FormItem label="物料编码" prop="fixtureNumber">
              <Input v-model="fixtureNumber" readonly style="width:200px;" />
            </FormItem>
          </Col>
          <Col span="12">
            <FormItem label="物料名称" prop="fixtureName">
              <Input v-model="fixtureName" readonly style="width:200px;" />
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span="12">
            <FormItem label="物料图号" prop="fixtureMap">
              <Input v-model="fixtureMap" readonly style="width:200px;" />
            </FormItem>
          </Col>
          <Col span="12">
            <FormItem label="物料条码" prop="fixtureBarcode">
              <Input v-model="fixtureBarcode" readonly style="width:200px;" />
            </FormItem>
          </Col>
        </Row>
        <row>
          <FormItem label="备注" prop="remark">
            <Input style="width:560px" v-model="formValidate.remark" placeholder="备注" />
          </FormItem>
        </row>
      </Form>
      <div slot="footer">
        <Button @click="returnSubmit()" type="primary" :disabled="isDisable">提交</Button>
        <Button @click="closeModal()" style="margin-left: 8px">关闭</Button>
      </div>
    </Modal>
    <Modal
      v-model="deleteModalShow"
      width="250"
      title="夹具报废"
      @on-ok="deleteModalSureBtn"
      @on-cancel="deleteModalCancelBtn"
    >
      <p>是否确定报废该夹具？</p>
    </Modal>
    <Modal
      v-model="setRepairModalShow"
      width="250"
      title="送修磨"
      @on-ok="setRepairModalSureBtn"
      @on-cancel="setRepairModalCancelBtn"
    >
      <p>确定送修所选夹具？</p>
    </Modal>
    <spot-check-modal ref="spotCheckModal" @search="getListData"></spot-check-modal>
    <fixture-replace-modal ref="fixtureReplaceModal" @search="getListData"></fixture-replace-modal>
  </div>
</template>

<script>
import axios from "@/libs/api.request";
import { dateFormat, objCopy } from "@/libs/tools.js";
import { getToken, getBaseUrl } from "@/libs/util";
import spotCheckModal from "./spot-check-modal.vue";
import fixtureReplaceModal from "./fixture-replace-modal.vue";
import warehouse_form_config from "./warehouse-form-config.js";
export default {
  components: {
    spotCheckModal,
    fixtureReplaceModal
  },
  data() {
    return {
      isDisable: false,
      fixtureNumber: "",
      fixtureName: "",
      fixtureMap: "",
      fixtureBarcode: "",
      deleteModalShow: false, //控制删除modal提示 显示
      returnModalshow: false,
      setRepairModalShow: false,
      goBackModalShow: false,
      departmentList: [],
      searchCondition: {
        fixtureNumber: "",
        fixtureName: "",
        fixtureMap: "",
        departmentId: null,
        inUse: 1,
        page: 1,
        rows: 10
      },
      tableData: {
        total: 0, // 总页数
        rows: [] // 每页条数
      },
      tableColumns: [
        {
          title: "夹具编码",
          key: "fixtureNumber",
          minWidth: 150
        },
        {
          title: "夹具名称",
          key: "fixtureName",
          minWidth: 300
        },
        {
          title: "图号",
          key: "fixtureMap",
          minWidth: 150
        },
        {
          title: "夹具条码",
          key: "fixtureBarcode",
          minWidth: 160
        },
        {
          title: "配件",
          align: "center",
          children: [
            {
              title: "配件编码",
              key: "childList",
              align: "center",
              width: 130,
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
              width: 250,
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
              width: 160,
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
              title: "配件条码",
              key: "childList",
              align: "center",
              width: 150,
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
                              return h(
                                "li",
                                {},
                                !!item.fixtureBarcode
                                  ? item.fixtureBarcode
                                  : "空缺"
                              );
                            }
                          )
                        : ""
                    )
                  ]
                );
              }
            },
            {
              title: "操作",
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
                              return h("li", {}, [
                                h(
                                  "a",
                                  {
                                    props: {
                                      type: "primary",
                                      size: "small"
                                    },
                                    style: {
                                      fontWeight: 800,
                                      marginRight: "5px"
                                    },
                                    on: {
                                      click: () => {
                                        this.replace(
                                          item.fixtureNumber,
                                          item.fixtureBarcode,
                                          this.tableData.rows[params.index]
                                            .fixtureBarcode
                                        );
                                      }
                                    }
                                  },
                                  "替换"
                                )
                              ]);
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
          title: "部门",
          key: "departmentName",
          minWidth: 120
        },
        {
          title: "领用人",
          key: "staffName",
          minWidth: 100
        },
        {
          title: "机床",
          key: "equipmentName",
          minWidth: 120
        },
        {
          title: "点检次数",
          key: "spotTimes",
          minWidth: 100
        },
        {
          title: "最后点检时间",
          key: "lastSpotTime",
          minWidth: 160,
          render: function(h, params) {
            if (!!params.row.lastSpotTime) {
              return h(
                "div",
                dateFormat(new Date(params.row.lastSpotTime), "yyyy-MM-dd")
              );
            }
          }
        },
        {
          title: "夹具状态",
          key: "fixtureStatus",
          minWidth: 120,
          render: function(h, params) {
            let statusStr = "";
            switch (params.row.fixtureStatus) {
              case 1:
                statusStr = "正常使用";
                break;
              case 2:
                statusStr = "待修磨";
                break;
              case 3:
                statusStr = "待送检";
                break;
              case 4:
                statusStr = "待检验";
                break;
              case 5:
                statusStr = "质检已完成";
                break;
              case 6:
                statusStr = "待报废";
                break;
            }
            return h("div", statusStr);
          }
        },
        {
          title: "操作",
          key: "action",
          width: 200,
          align: "center",
          fixed: "right",
          render: (h, params) => {
            return h("div", [
              h(
                "Button",
                {
                  props: {
                    type: "info",
                    size: "small"
                  },
                  style: {
                    marginRight: "5px",
                    display:
                      params.row.fixtureStatus == 1 ? "inline-block" : "none"
                  },
                  on: {
                    click: () => {
                      this.spot(params.row);
                    }
                  }
                },
                "点检"
              ),
              h(
                "Button",
                {
                  props: {
                    type: "warning",
                    size: "small"
                  },
                  style: {
                    marginRight: "5px",
                    display:
                      params.row.fixtureStatus == 1 ? "inline-block" : "none"
                  },
                  on: {
                    click: () => {
                      this.setRepair(
                        params.row.pkId,
                        params.row.fixtureBarcode
                      );
                    }
                  }
                },
                "送修磨"
              ),
              h(
                "Button",
                {
                  props: {
                    type: "primary",
                    size: "small"
                  },
                  style: {
                    marginRight: "5px",
                    display:
                      params.row.fixtureStatus == 1 ? "inline-block" : "none"
                  },
                  on: {
                    click: () => {
                      this.returnWarehouse(params.row);
                    }
                  }
                },
                "回库"
              ),
              h(
                "Button",
                {
                  props: {
                    type: "error",
                    size: "small"
                  },
                  style: {
                    marginRight: "5px",
                    display:
                      params.row.fixtureStatus == 6 ? "inline-block" : "none"
                  },
                  on: {
                    click: () => {
                      this.remove(params.index);
                    }
                  }
                },
                "报废"
              )
            ]);
          }
        }
      ],
      formValidate: warehouse_form_config.formValidate, // form表单字段
      ruleValidate: warehouse_form_config.ruleValidate // form表单验证规则
    };
  },
  created() {
    const self = this;
    self.getDepartmentListData();
    self.getListData();
  },
  methods: {
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
    getListData() {
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/fixture/fixture-page-list",
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

    deleteRowDate(pkId, fixtureBarcode) {
      let para = {
        pkId: pkId,
        fixtureBarcode: fixtureBarcode
      };
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/fixture/fixture-delete-by-id",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          self.deleteModalShow = false;
          if (res.data.code === 200) {
            self.$Message.success("夹具已成功报废!");
            self.getListData();
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },
    //返仓库
    returnWarehouse(row) {
      const self = this;
      self.returnModalshow = true;
      self.fixtureBarcode = row.fixtureBarcode;
      self.fixtureNumber = row.fixtureNumber;
      self.fixtureName = row.fixtureName;
      self.fixtureMap = row.fixtureMap;
      self.pkId = row.pkId;
    },
    //回库
    returnSubmit() {
      const self = this;
      self.$refs["formValidate"].validate(valid => {
        if (valid) {
          self.isDisable = true;
          let para = {
            pkId: self.pkId,
            fixtureBarcode: self.fixtureBarcode,
            remark: self.formValidate.remark
          };
          axios
            .request({
              headers: {
                "Content-Type": "application/json; charset=UTF-8"
              },
              url: "/fixture/fixture-return-warehouse.json",
              method: "post",
              isAuth: true,
              params: para
            })
            .then(function(res) {
              self.returnModalshow = false;
              if (res.data.code === 200) {
                self.$Message.success("夹具已返库!");
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
        } else {
          self.$Message.error("请输入正确信息!");
        }
      });
    },

    //关闭弹窗
    closeModal() {
      const self = this;
      self.returnModalshow = false;
    },

    setRepair(pkId, fixtureBarcode) {
      const self = this;
      self.pkId = pkId;
      self.fixtureBarcode = fixtureBarcode;
      self.setRepairModalShow = true;
    },

    remove(index) {
      const self = this;
      self.deleteRowData = self.tableData.rows[index];
      let para = {
        fullNumber: self.deleteRowData.fixtureBarcode
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/fixture/apply-get-by-full.json",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          if (res.data.code === 200) {
            if (res.data.data !== null && res.data.data !== undefined) {
              if (res.data.data.applyStatus === 2) {
                self.deleteModalShow = true;
                self.resiontext = res.data.data.scripResion;
              } else {
                self.$Message.error("该夹具尚未通过报废审核!");
              }
            } else {
              self.$Message.error("该夹具尚未通过报废审核!");
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
    deleteModalSureBtn() {
      this.deleteRowDate(
        this.deleteRowData.pkId,
        this.deleteRowData.fixtureBarcode
      );
    },
    //删除modal 取消方法
    deleteModalCancelBtn() {
      this.deleteRowData = {};
    },
    exportData() {
      const self = this;
      let para = "token=" + getToken();
      if (!!self.searchCondition.fixtureNumber) {
        para = para + "&fixtureNumber=" + self.searchCondition.fixtureNumber;
      }
      if (!!self.searchCondition.fixtureName) {
        para = para + "&fixtureName=" + self.searchCondition.fixtureName;
      }
      if (!!self.searchCondition.fixtureMap) {
        para = para + "&fixtureMap=" + self.searchCondition.fixtureMap;
      }
      if (!!self.searchCondition.departmentId) {
        para = para + "&departmentId=" + self.searchCondition.departmentId;
      }
      window.location.href = getBaseUrl() + "/fixture/fixture-export?" + para;
    },
    spot(row) {
      const self = this;
      self.$refs.spotCheckModal.showModal(row, null, 1);
    },
    setRepairModalSureBtn() {
      const self = this;
      let para = {
        pkId: self.pkId,
        fixtureBarcode: self.fixtureBarcode
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/fixture/set-repair",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          self.setRepairModalShow = false;
          if (res.data.code === 200) {
            self.$Message.success("夹具送修磨成功!");
            self.getListData();
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },

    replace(fixtureNumber, fixtureBarcode, parentBarcode) {
      const self = this;
      self.$refs.fixtureReplaceModal.showModal(
        fixtureNumber,
        fixtureBarcode,
        parentBarcode
      );
    },

    setRepairModalCancelBtn() {},
    print(pkId) {
      window.open(
        "http://10.36.10.11:8080/decision/view/report?viewlet=fixture.cpt&pkId=" +
          pkId,
        "newwindow",
        "height=360,width=600,top=0,left=0,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no"
      );
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
