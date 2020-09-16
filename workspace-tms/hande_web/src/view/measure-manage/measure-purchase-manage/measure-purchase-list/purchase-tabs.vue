<template>
<div>
        <Tabs type="card" @on-click="tabsClick">
        <TabPane label="待办事项">
            <measure-purchase-list ref="measurePurchaseList"></measure-purchase-list>
        </TabPane>
       <TabPane label="已完成事项">
            <measure-purchase-audit ref="measurePurchaseAudit"></measure-purchase-audit>
        </TabPane>
    </Tabs>
  </div>
  
</template>

<script>
import axios from "@/libs/api.request";
import { dateFormat, objCopy } from "@/libs/tools.js";
import measurePurchaseList from "./measure-purchase-list.vue";
import measurePurchaseAudit from "./measure-purchase-audit.vue";

export default {
  components: {
    measurePurchaseAudit,
    measurePurchaseList
  },
  methods: {
    tabsClick(tab) {
      if (tab === 0) {
        this.$refs.measurePurchaseList.getListData();
      } else if (tab === 1) {
        this.$refs.measurePurchaseAudit.getListData();
      }
    }
  }
};
</script>

<style>
</style>
