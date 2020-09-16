<template>
  <div>
    <Tabs type="card" @on-click="tabsClick">
      <TabPane label="涂层待检">
        <coat-compose-wait-check-list ref="coatComposeWaitCheckList"></coat-compose-wait-check-list>
      </TabPane>
      <TabPane label="检验记录">
        <coat-compose-check-list ref="coatComposeCheckList"></coat-compose-check-list>
      </TabPane>
    </Tabs>
  </div>
</template>

<script>
import axios from "@/libs/api.request";
import { dateFormat, objCopy } from "@/libs/tools.js";

import coatComposeWaitCheckList from "./coat-compose-wait-check-list.vue";
import coatComposeCheckList from "./coat-compose-check-list.vue";

export default {
  components: {
    coatComposeWaitCheckList,
    coatComposeCheckList
  },
  methods: {
    tabsClick(tab) {}
  }
};
</script>

<style>
</style>
