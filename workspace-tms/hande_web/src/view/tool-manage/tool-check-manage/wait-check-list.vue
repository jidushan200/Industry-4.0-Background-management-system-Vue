<style lang="less">
.fsize {
  font-size: 18px !important;
}
</style>
<template>
  <div>
    <Card>
      <Row>
        <Form ref="formInline" :model="searchCondition" :label-width="65" inline>
          <Col span="20">
            <FormItem prop="fullNumber" label="物料条码">
              <Input type="text" v-model="searchCondition.fullNumber" placeholder="请输入物料条码"></Input>
            </FormItem>
            <FormItem prop="toolNumber" label="物料编码">
              <Input type="text" v-model="searchCondition.toolNumber" placeholder="请输入物料编码"></Input>
            </FormItem>
            <FormItem prop="checkType" label="检验类型">
              <Select
                style="width:158px"
                v-model="searchCondition.checkType"
                ref="checkType"
                placeholder="请选择检验类型"
              >
                <Option
                  v-for="item in checkType"
                  :value="item.value"
                  :key="item.value"
                >{{ item.name }}</Option>
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
          <Page show-total
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
    <new-check-modal ref="newCheckModal" @search="getListData"></new-check-modal>
    <repair-check-modal ref="repairCheckModal" @search="getListData"></repair-check-modal>
    <repair-blade-modal ref="repairBladeModal" @search="getListData"></repair-blade-modal>
    <coat-blade-modal ref="coatBladeModal" @search="getListData"></coat-blade-modal>
  </div>
</template>

<script>
import axios from "@/libs/api.request";
import { dateFormat, objCopy } from "@/libs/tools.js";
import checkType from "./check-type.js";
import newCheckModal from "./new-check-modal.vue";
import repairCheckModal from "./repair-check-modal.vue";
import repairBladeModal from "./repair-blade-modal.vue";
import coatBladeModal from "./coat-blade-modal.vue";
export default {
  components: {
    newCheckModal,
    repairCheckModal,
    repairBladeModal,
    coatBladeModal
  },
  data() {
    return {
      checkType: checkType,
      searchCondition: {
        fullNumber: "",
        toolNumber: "",
        checkType: null,
        page: 1,
        rows: 10
      },
      tableData: {
        total: 0, // 总页数
        rows: [] // 每页条数
      },
      tableColumns: [
        {
          title: "物料编码",
          minWidth: 150,
          key: "toolNumber"
        },
        {
          title: "物料名称",
          minWidth: 250,
          key: "toolName"
        },
        {
          title: "物料图号",
          width: 200,
          key: "toolMap"
        },
        {
          title: "物料条码",
          minWidth: 250,
          key: "fullNumber"
        },
        {
          title: "物料类型",
          minWidth: 100,
          key: "typeName"
        },
        {
          title: "入库编码",
          minWidth: 200,
          key: "warehouseCode"
        },
        {
          title: "顺序号",
          minWidth: 120,
          key: "toolSeq"
        },
        {
          title: "待检数量",
          minWidth: 120,
          key: "toolQty"
        },
        {
          title: "质检类型",
          minWidth: 150,
          key: "checkType",
          render: function(h, params) {
            let statusStr = "";
            switch (params.row.checkType) {
              case 1:
                statusStr = "新刀质检";
                break;
              case 2:
                statusStr = "刃磨质检";
                break;
              case 3:
                statusStr = "涂层质检";
                break;
            }
            return h("div", statusStr);
          }
        },
        {
          title: "送检时间",
          minWidth: 160,
          render: function(h, params) {
            return h(
              "div",
              dateFormat(
                new Date(params.row.setCheckTime),
                "yyyy-MM-dd HH:mm:ss"
              )
            );
          }
        },
        {
          title: "操作",
          key: "action",
          minWidth: 160,
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
                    //disabled: params.row.checkStatus > 1
                  },
                  style: {
                    marginRight: "5px",
                    display:
                      params.row.checkType == 2 && params.row.typeId != 4
                        ? "none"
                        : "inline-block"
                  },
                  on: {
                    click: () => {
                      this.toolCheck(params.row);
                    }
                  }
                },
                "检验"
              )
            ]);
          }
        }
      ]
    };
  },
  created() {
    const self = this;
    self.getListData();
  },
  methods: {
    getListData() {
      const self = this;
      let para = {
        fullNumber: self.searchCondition.fullNumber,
        toolNumber: self.searchCondition.toolNumber,
        checkType: self.searchCondition.checkType,
        page: self.searchCondition.page,
        rows: self.searchCondition.rows
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/wait-check-page-list",
          method: "post",
          isAuth: true,
          params: para
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
      this.formValidate = {};
    },
    doReset(name) {
      const self = this;
      self.$refs[name].resetFields();
      self.getListData();
    },
    toolCheck(row) {
      const self = this;
      if (row.checkType == 1) {
        if (row.checkStatus == 0) {
          self.$refs.newCheckModal.showModal(row, null, 1);
        } else {
          self.$refs.newCheckModal.showModal(null, row.checkId, 2);
        }
      } else if (row.checkType == 2) {
          //刀条刃磨
        if (row.checkStatus == 0) {
          self.$refs.repairBladeModal.showModal(row, null, 1);
        } else {
          self.$refs.repairBladeModal.showModal(null,row.checkId, 2);
        }
      }else if (row.checkType ==3) {
        //刀条涂层
        if (row.checkStatus == 0) {
          self.$refs.coatBladeModal.showModal(row, null, 1);
        } else {
          self.$refs.coatBladeModal.showModal(null,row.checkId, 2);
        }
      }
    }
  }
};
</script>

<style>
</style>
