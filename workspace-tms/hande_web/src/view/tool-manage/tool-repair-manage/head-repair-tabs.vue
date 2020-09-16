<template>
  <div>
    <Tabs type="card" @on-click="tabsClick">
      <TabPane label="待磨刀头">
        <head-wait-repair ref="headWaitRepair"></head-wait-repair>
      </TabPane>
      <TabPane label="刃磨记录">
        <head-repair-list ref="headRepairList"></head-repair-list>
      </TabPane>
    </Tabs>
  </div>
</template>

<script>
import axios from "@/libs/api.request";
import { dateFormat, objCopy } from "@/libs/tools.js";
import headWaitRepair from "./head-wait-repair";
import headRepairList from "./head-repair-list";

export default {
  components: {
    headWaitRepair,
    headRepairList
  },
  methods: {
    tabsClick(tab) {
      if (tab === 0) {
        this.$refs.headWaitRepair.getListData();
      } else if (tab === 1) {
        this.$refs.headRepairList.getListData();
      }
    }
  }
};
</script>

<style>
</style>
