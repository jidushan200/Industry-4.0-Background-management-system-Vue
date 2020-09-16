<template>
  <div>
    <Tabs type="card">
      <TabPane label="刃磨待检">
        <repair-wait-check-list ref="repairWaitCheckList"></repair-wait-check-list>
      </TabPane>
      <TabPane label="检验记录">
        <repair-check-list ref="repairCheckList"></repair-check-list>
      </TabPane>
    </Tabs>
  </div>
</template>

<script>
import axios from "@/libs/api.request";
import { dateFormat, objCopy } from "@/libs/tools.js";

import repairWaitCheckList from "./repair-wait-check-list.vue";
import repairCheckList from "./repair-check-list.vue";

export default {
  components: {
    repairWaitCheckList,
    repairCheckList
  },
  methods: {
    tabsClick(tab) {}
  }
};
</script>

<style>
</style>
