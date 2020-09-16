<template>
  <div>
    <Tabs type="card" @on-click="tabsClick">
      <TabPane label="涂层待检">
        <coat-wait-check-list ref="coatWaitCheckList"></coat-wait-check-list>
      </TabPane>
      <TabPane label="检验记录">
        <coat-check-list ref="coatCheckList"></coat-check-list>
      </TabPane>
    </Tabs>
  </div>
</template>

<script>
import axios from "@/libs/api.request";
import { dateFormat, objCopy } from "@/libs/tools.js";

import coatWaitCheckList from "./coat-wait-check-list.vue";
import coatCheckList from "./coat-check-list.vue";

export default {
  components: {
    coatWaitCheckList,
    coatCheckList
  },
  methods: {
    tabsClick(tab) {}
  }
};
</script>

<style>
</style>
