<template>
<div>
        <Tabs type="card" @on-click="tabsClick">
        <TabPane label="待接收申请">
            <purchase-reported ref="purchaseReported"></purchase-reported>
        </TabPane>
       <TabPane label="执行采购">
            <tool-purchase ref="toolPurchase"></tool-purchase>
        </TabPane>
         <TabPane label="已完成采购">
          <tool-purchased ref="toolPurchased"></tool-purchased>
        </TabPane>
    </Tabs>
  </div>
  
</template>

<script>
import axios from "@/libs/api.request";
import { dateFormat, objCopy } from "@/libs/tools.js";
import toolPurchase from "./tool-purchase";
import toolPurchased from "./tool-purchased";
import purchaseReported from "./purchase-reported";

export default {
  components: {
    toolPurchased,
    toolPurchase,
    purchaseReported
  },
  methods: {
    tabsClick(tab) {
      if (tab === 0) {
        this.$refs.purchaseReported.getListData();
      } else if (tab === 1) {
        this.$refs.toolPurchase.getListData();
      } else {
        this.$refs.toolPurchased.getListData();
      }
    }
  }
};
</script>

<style>
</style>
