<style>
.ivu-table .demo-table-info-row td {
  background-color: #2db7f5;
  color: #fff;
}

.ivu-table .demo-table-error-row td {
  background-color: #ff8c00 !important;
  /* #ff6600
  #ffA500 */
  color: #fff;
}

.ivu-table td.demo-table-info-column {
  background-color: #2db7f5;
  color: #fff;
}

.ivu-table .demo-table-info-cell-name {
  background-color: #2db7f5;
  color: #fff;
}

.ivu-table .demo-table-info-cell-age {
  background-color: #ff6600;
  color: #fff;
}

.ivu-table .demo-table-info-cell-address {
  background-color: #187;
  color: #fff;
}
</style>
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
              <Select
                style="width:180px"
                v-model="searchCondition.fixtureMap"
                clearable
                filterable
                ref="fixtureMap"
              >
                <Option
                  v-for="item in baseList"
                  :value="item.fixtureMap"
                  :key="item.fixtureMap"
                >{{ item.fixtureMap }}-{{ item.fixtureName }}</Option>
              </Select>
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
              <Upload
                :show-upload-list="false"
                :action="uploadAction"
                :on-success="handleSuccess"
                :on-error="handleError"
                style="display: inline-block;margin-right: 5px;"
              >
                <Button icon="ios-cloud-upload-outline">导入</Button>
              </Upload>
              <Button type="warning" @click="exportData()">
                <Icon type="ios-download-outline"></Icon>导出
              </Button>
            </FormItem>
          </Col>
        </Form>
      </Row>

      <Table
        ref="tablesMain"
        :data="tableData.rows"
        :columns="tableColumns"
        :row-class-name="rowClassName"
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
      v-model="deleteModalShow"
      width="250"
      title="夹具报废"
      @on-ok="deleteModalSureBtn"
      @on-cancel="deleteModalCancelBtn"
    >
      <p>是否确定报废该夹具？</p>
    </Modal>
    <fixture-maintain-modal ref="fixtureMaintainModal" @search="getListData"></fixture-maintain-modal>
    <fixture-outbound-modal ref="fixtureOutboundModal" @search="getListData"></fixture-outbound-modal>
    <fixture-replace-modal ref="fixtureReplaceModal" @search="getListData"></fixture-replace-modal>
  </div>
</template>

<script>
import axios from "@/libs/api.request";
import warehouse_form_config from "./warehouse-form-config.js";
import { dateFormat, objCopy } from "@/libs/tools.js";
import { getToken, getBaseUrl } from "@/libs/util";
import fixtureMaintainModal from "./fixture-maintain-modal.vue";
import fixtureOutboundModal from "./fixture-outbound-modal.vue";
import fixtureReplaceModal from "./fixture-replace-modal.vue";
export default {
  components: {
    fixtureOutboundModal,
    fixtureMaintainModal,
    fixtureReplaceModal
  },
  data() {
    return {
      deleteModalShow: false, //控制删除modal提示 显示
      departmentList: [],
      baseList: [],
      uploadAction:
        getBaseUrl() + "/fixture/fixture-import.json?token=" + getToken(),
      searchCondition: {
        fixtureNumber: "",
        fixtureName: "",
        fixtureMap: "",
        departmentId: null,
        inUse: 2,
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
          title: "保养次数",
          key: "maintainTimes",
          minWidth: 100
        },
        {
          title: "最后保养时间",
          key: "lastMaintainTime",
          minWidth: 160,
          render: function(h, params) {
            if (!!params.row.lastMaintainTime) {
              return h(
                "div",
                dateFormat(
                  new Date(params.row.lastMaintainTime),
                  "yyyy-MM-dd HH:mm:ss"
                )
              );
            }
          }
        },
        {
          title: "下次保养时间",
          key: "nextMaintainTime",
          minWidth: 120,
          render: function(h, params) {
            if (!!params.row.nextMaintainTime) {
              return h(
                "div",
                dateFormat(new Date(params.row.nextMaintainTime), "yyyy-MM-dd")
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
                statusStr = "在库";
                break;
              case 2:
                statusStr = "待修磨";
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
                    marginRight: "5px"
                  },
                  on: {
                    click: () => {
                      this.print(params.row.pkId);
                    }
                  }
                },
                "打印"
              ),
              h(
                "Button",
                {
                  props: {
                    type: "success",
                    size: "small"
                  },
                  style: {
                    marginRight: "5px",
                    display:
                      params.row.fixtureStatus == 1 ? "inline-block" : "none"
                  },
                  on: {
                    click: () => {
                      this.outbound(params.row);
                    }
                  }
                },
                "领用"
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
                      this.maintain(params.row);
                    }
                  }
                },
                "保养"
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
      ]
    };
  },
  created() {
    const self = this;
    self.getDepartmentListData();
    self.getBaseListData();
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
    getBaseListData() {
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/fixture/get-base-list",
          isAuth: true,
          method: "post"
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            self.baseList = data.data;
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
    maintain(row) {
      const self = this;
      self.$refs.fixtureMaintainModal.showModal(row);
    },

    rowClassName(row, index) {
      if (row.fixtureStatus > 5) {
        return;
      }
      let dateDiff = Math.floor(
        (row.nextMaintainTime - new Date()) / (24 * 3600 * 1000)
      );
      if (dateDiff < 7) {
        return "demo-table-error-row";
      }
    },

    replace(fixtureNumber, fixtureBarcode, parentBarcode) {
      const self = this;
      self.$refs.fixtureReplaceModal.showModal(
        fixtureNumber,
        fixtureBarcode,
        parentBarcode
      );
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
    outbound(row) {
      const self = this;
      self.$refs.fixtureOutboundModal.showModal(row);
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
