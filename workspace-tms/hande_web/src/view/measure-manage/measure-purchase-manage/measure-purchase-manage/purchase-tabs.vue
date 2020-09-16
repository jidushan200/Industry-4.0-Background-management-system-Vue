<template>
<div>
        <Tabs type="card" @on-click="tabsClick">
        <TabPane label="已完申购">
            <purchase-reported ref="purchaseReported"></purchase-reported>
        </TabPane>
       <TabPane label="正在采购">
            <measure-purchase ref="measurePurchase"></measure-purchase>
        </TabPane>
         <TabPane label="已完采购">
          <measure-purchased ref="measurePurchased"></measure-purchased>
        </TabPane>
    </Tabs>
  </div>
  
</template>

<script>
import axios from "@/libs/api.request";
import { dateFormat, objCopy } from "@/libs/tools.js";
import measurePurchase from "./measure-purchase";
import measurePurchased from "./measure-purchased";
import purchaseReported from "./purchase-reported";

export default {
  components: {
    measurePurchased,
    measurePurchase,
    purchaseReported
  },
  methods: {
    tabsClick(tab) {
      if (tab === 0) {
        this.$refs.purchaseReported.getListData();
      } else if (tab === 1) {
        this.$refs.measurePurchase.getListData();
      } else {
        this.$refs.measurePurchased.getListData();
      }
    }
  }
};
</script>

<style>
</style>
