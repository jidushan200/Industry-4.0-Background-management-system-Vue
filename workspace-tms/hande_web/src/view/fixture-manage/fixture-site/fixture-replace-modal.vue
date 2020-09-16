
<style>
.ivu-table .demo-table-error-row td {
  background-color: #ff8c00 !important;
  /* #ff6600
  #ffA500 */
  color: #fff;
}
</style>
<template>
  <!--出库modal-->
  <Modal v-model="replaceModalshow" class="modal-class" title="夹具配件替换" width="900" draggable>
    <Table
      :columns="detailColumns"
      height="360"
      :data="detailList"
      border
      stripe
      highlight-row
    ></Table>
    <div slot="footer">
      <Button @click="handleSubmit()" type="primary" :disabled="isDisable" v-if="canReplace">提交</Button>
      <Button @click="closeModal()" style="margin-left: 8px">关闭</Button>
    </div>
  </Modal>
</template>
<script>
import { dateFormat, objCopy } from "@/libs/tools.js";
import { getToken, getBaseUrl } from "@/libs/util";
import axios from "@/libs/api.request";
import qs from "qs";
export default {
  data() {
    return {
      rowId: "",
      isDisable: false,
      departmentId: null,
      replaceModalshow: false,
      fixtureNumber: "",
      parentBarcode: "",
      detailList: [],
      canReplace: false,
      currentIndex: null,
      detailColumns: [
        {
          title: "选择",
          width: 70,
          align: "center",
          render: (h, params) => {
            let index = params.index;
            let flag = false;
            if (this.currentIndex == index) {
              flag = true;
            } else {
              flag = false;
            }
            let self = this;
            return h("div", [
              h("Radio", {
                props: {
                  value: flag
                },
                on: {
                  "on-change": () => {
                    self.currentIndex = index;
                  }
                }
              })
            ]);
          }
        },
        {
          title: "配件条码",
          key: "fixtureBarcode",
          minWidth: 140
        },
        {
          title: "组合条码",
          key: "parentBarcode",
          minWidth: 150
        },
        {
          title: "配件图号",
          key: "fixtureMap",
          minWidth: 150
        },
        {
          title: "配件名称",
          key: "fixtureName",
          minWidth: 270
        }
      ]
    };
  },
  created() {
    const self = this;
    self.departmentId = self.$store.state.user.userData.data.departmentId;
  },
  methods: {
    showModal(fixtureNumber, fixtureBarcode, parentBarcode) {
      debugger;
      const self = this;
      self.rowId = "";
      self.currentIndex = null;
      self.replaceModalshow = true;
      self.parentBarcode = parentBarcode;
      self.fixtureNumber = fixtureNumber;
      self.fixtureBarcode = fixtureBarcode;
      self.canReplace = false;
      self.getFixtureData(fixtureNumber, parentBarcode);
    },

    getFixtureData(fixtureNumber, parentBarcode) {
      const self = this;
      let para = {
        fixtureNumber: fixtureNumber,
        parentBarcode: parentBarcode,
        departmentId: self.departmentId
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/fixture/fixture-get-part-list",
          isAuth: true,
          method: "post",
          params: para
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            self.detailList = data.data;
            if (data.data.length>0) {
              self.canReplace = true;
            }
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },

    //保存
    handleSubmit() {
      const self = this;
      if (typeof self.currentIndex != "number") {
        self.$Message.error("请选择配件!");
        return;
      }
      const row = self.detailList[self.currentIndex];
      let para = {
        fixtureBarcode: row.fixtureBarcode,
        parentBarcode: row.parentBarcode,
        oldParentBarcode: self.parentBarcode,
        oldBarcode: self.fixtureBarcode,
        oldNumber: self.fixtureNumber
      };
      self.isDisable = true;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/fixture/replace-fixtrue.json",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          self.replaceModalshow = false;
          if (res.data.code === 200) {
            self.$Message.success("夹具已替换!");
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
    rowClick(row) {
      this.rowId = row.pkId;
    },
    getSelect(row) {
      alert(row.fixtureBarcode);
    },
    //关闭弹窗
    closeModal() {
      const self = this;
      self.replaceModalshow = false;
    }
  },
  mounted() {}
};
</script>