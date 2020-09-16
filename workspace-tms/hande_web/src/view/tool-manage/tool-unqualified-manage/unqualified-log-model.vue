<template>
  <!--审核信息弹出modal-->
  <Modal v-model="logModalshow" title="审核日志" width="1080" draggable>
    <Table :columns="tableColumns" :data="logList" border stripe highlight-row></Table>
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
      logModalshow: false,
      logList: [],
      tableColumns: [
        {
          title: "审核部门",
          key: "auditDepartmentName",
          width: 90
        },
        {
          title: "审核人",
          key: "auditorName",
          width: 90
        },
        {
          title: "审核结果",
          key: "auditResult",
          width: 240
        },
        {
          title: "备注",
          key: "remark",
          minWidth: 150
        },
        {
          title: "审核时间",
          key: "createTime",
          width: 150,
          render: function(h, params) {
            return h(
              "div",
              dateFormat(new Date(params.row.createTime), "yyyy-MM-dd HH:mm:ss")
            );
          }
        }
      ]
    };
  },
  created() {},
  methods: {
    showModal(reportId) {
      const self = this;
      let para = {
        applyId: reportId
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/tool/apply-audit-get-list.json",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            self.logList = data.data;
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
      self.logModalshow = true;
    },
    closeModal() {
      const self = this;
      self.logModalshow = false;
    }
  }
};
</script>