
<template>
  <div>
    <Card>
      <Row>
        <Form ref="formInline" :model="searchCondition" :label-width="65" inline>
          <Col span="16">
            <FormItem label="夹具名称" prop="fixtureName">
              <Input type="text" v-model="searchCondition.fixtureName" placeholder="请输入夹具名称"></Input>
            </FormItem>
            <FormItem label="夹具图号" prop="fixtureMap">
              <Input type="text" v-model="searchCondition.fixtureMap" placeholder="请输入夹具图号"></Input>
            </FormItem>
            <FormItem label="领用部门" prop="departmentId">
              <Select style="width:120px" v-model="searchCondition.departmentId">
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

    <spot-check-modal ref="spotCheckModal" @search="getListData"></spot-check-modal>
    <Modal v-model="setRepairModalShow" width="250" title="送修磨" @on-ok="setRepairModalSureBtn">
      <p>确定送修所选夹具？</p>
    </Modal>
    <Modal v-model="takeBackModalShow" width="250" title="领回夹具" @on-ok="takeBackSureBtn">
      <p>确定领回夹具？</p>
    </Modal>
  </div>
</template>

<script>
import axios from "@/libs/api.request";
import { dateFormat, objCopy } from "@/libs/tools.js";

import spotCheckModal from "./spot-check-modal.vue";
export default {
  components: { spotCheckModal },
  data() {
    return {
      setRepairModalShow: false,
      takeBackModalShow: false,
      departmentList: [],
      supplierList: [],
      searchCondition: {
        fixtureNumber: "",
        fixtureName: "",
        fixtureMap: "",
        departmentId: "",
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
          title: "夹具条码",
          key: "fixtureBarcode",
          minWidth: 200
        },
        {
          title: "夹具名称",
          key: "fixtureName",
          minWidth: 220
        },
        {
          title: "部门",
          key: "departmentName",
          minWidth: 100
        },
        {
          title: "班组",
          key: "teamName",
          minWidth: 100
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
          title: "校准精度",
          key: "calibrationAccuracy",
          minWidth: 120
        },

        {
          title: "修磨次数",
          key: "repairTimes",
          minWidth: 100
        },
        {
          title: "最后修磨时间",
          key: "lastRepairTime",
          minWidth: 160,
          render: function(h, params) {
            if (!!params.row.lastRepairTime) {
              return h(
                "div",
                dateFormat(
                  new Date(params.row.lastRepairTime),
                  "yyyy-MM-dd HH:mm:ss"
                )
              );
            }
          }
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
                dateFormat(
                  new Date(params.row.lastSpotTime),
                  "yyyy-MM-dd HH:mm:ss"
                )
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
              case 7:
                statusStr = "已报废";
                break;
            }
            return h("div", statusStr);
          }
        },
        {
          title: "领用时间",
          key: "createTime",
          sortable: true,
          minWidth: 160,
          render: function(h, params) {
            return h(
              "div",
              dateFormat(new Date(params.row.createTime), "yyyy-MM-dd")
            );
          }
        },
        {
          title: "备注",
          key: "receiveReason",
          minWidth: 160
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
                    type: "success",
                    size: "small"
                  },
                  style: {
                    marginRight: "5px",
                    display:
                      params.row.fixtureStatus == 5 ? "inline-block" : "none"
                  },
                  on: {
                    click: () => {
                      this.takeBack(params.row.pkId, params.row.fixtureBarcode);
                    }
                  }
                },
                "返库"
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

    takeBackSureBtn() {
      const self = this;
      let para = {
        fixtureId: self.fixtureId,
        fixtureBarcode: self.fixtureBarcode
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/fixture/fixture-take-back.json",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          if (res.data.code === 200) {
            self.$Message.success("领回成功!");
            self.getListData();
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

    spot(row) {
      const self = this;
      self.$refs.spotCheckModal.showModal(row, null, 1);
    },
    setRepair(pkId, fixtureBarcode) {
      const self = this;
      self.setRepairId = pkId;
      self.fixtureBarcode = fixtureBarcode;
      self.setRepairModalShow = true;
    },

    takeBack(pkId, fixtureBarcode) {
      const self = this;
      self.fixtureId = pkId;
      self.fixtureBarcode = fixtureBarcode;
      self.takeBackModalShow = true;
    },

    setRepairModalSureBtn() {
      const self = this;
      let para = {
        pkId: self.setRepairId,
        fixtureBarcode: self.fixtureBarcode,
        fixtureStatus: 2,
        oper: "setRepair",
        operType: "送修磨"
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/fixture/fixture-oper",
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
    }
  }
};
</script>

<style>
</style>
