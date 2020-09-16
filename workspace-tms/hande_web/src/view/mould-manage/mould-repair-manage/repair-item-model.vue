<template>
  <!--审核信息弹出modal-->
  <Modal v-model="repairItemModalshow" title="修磨工序" width="1080" draggable>
    <Table :columns="tableColumns" :data="repairList" border stripe highlight-row></Table>
    <div slot="footer">
      <Button @click="closeModal()" style="margin-left: 8px">关闭</Button>
    </div>
  </Modal>
</template>
<script>
import { dateFormat, objCopy } from "@/libs/tools.js";
import axios from "@/libs/api.request";
export default {
  data() {
    return {
      repairItemModalshow: false,
      repairList: [],
      tableColumns: [
        {
          title: "修磨工序",
          key: "procedureName",
          minwidth: 100
        },
        {
          title: "修磨人",
          key: "executor",
          minwidth: 160
        },
        {
          title: "修磨时间",
          key: "executTime",
          minwidth: 150,
          render: function(h, params) {
            return h(
              "div",
              dateFormat(new Date(params.row.executTime), "yyyy-MM-dd HH:mm:ss")
            );
          }
        }
      ]
    };
  },
  created() {},
  methods: {
    showModal(repairId) {
      const self = this;
      let para = {
        repairId: repairId
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/mould/repair-item-get-list.json",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            self.repairList = data.data;
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
      self.repairItemModalshow = true;
    },
    closeModal() {
      const self = this;
      self.repairItemModalshow = false;
    }
  }
};
</script>