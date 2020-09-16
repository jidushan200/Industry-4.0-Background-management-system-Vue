<template>
<div>
        <Tabs type="card" @on-click="tabsClick">
        <TabPane label="待办事项">
            <tool-purchase-list ref="toolPurchaseList"></tool-purchase-list>
        </TabPane>
       <TabPane label="已完成事项">
            <tool-purchase-audit ref="toolPurchaseAudit"></tool-purchase-audit>
        </TabPane>
    </Tabs>
  </div>
  
</template>

<script>
import axios from "@/libs/api.request";
import { dateFormat, objCopy } from "@/libs/tools.js";
import toolPurchaseList from "./tool-purchase-list.vue";
import toolPurchaseAudit from "./tool-purchase-audit.vue";

export default {
  components: {
    toolPurchaseAudit,
    toolPurchaseList
  },
  methods: {
    tabsClick(tab) {
      if (tab === 0) {
        this.$refs.toolPurchaseList.getListData();
      } else if (tab === 1) {
        this.$refs.toolPurchaseAudit.getListData();
      }
    }
  }
};
</script>

<style>
</style>
