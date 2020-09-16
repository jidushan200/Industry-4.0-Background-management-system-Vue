<template>
  <div>
    <Tabs type="card" @on-click="tabsClick">
      <TabPane label="新刀待检">
        <new-wait-check-list ref="newWaitCheckList"></new-wait-check-list>
      </TabPane>
      <TabPane label="检验记录">
        <new-check-list ref="newCheckList"></new-check-list>
      </TabPane>
    </Tabs>
  </div>
</template>

<script>
import axios from "@/libs/api.request";
import { dateFormat, objCopy } from "@/libs/tools.js";

import newWaitCheckList from "./new-wait-check-list.vue";
import newCheckList from "./new-check-list.vue";

export default {
  components: {
    newWaitCheckList,
    newCheckList
  },
  methods: {
    tabsClick(tab) {
      if (tab === 0) {
        this.$refs.newWaitCheckList.getListData();
      } else if (tab === 1) {
        this.$refs.newCheckList.getListData();
      } 
    }
  }
};
</script>

<style>
</style>
