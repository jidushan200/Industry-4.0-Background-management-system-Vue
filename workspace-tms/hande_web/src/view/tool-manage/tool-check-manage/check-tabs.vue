<template>
<div>
        <Tabs type="card" @on-click="tabsClick">
        <TabPane label="待检刀具">
            <will-check-list ref="willCheckList"></will-check-list>
        </TabPane>
        <TabPane label="质量检验">
            <tool-check-list ref="toolCheckList"></tool-check-list>
        </TabPane>
    </Tabs>
  </div>
  
</template>

<script>
import axios from "@/libs/api.request";
import { dateFormat, objCopy } from "@/libs/tools.js";
import willCheckList from "./will-check-list.vue";
import toolCheckList from "./tool-check-list.vue";

export default {
  components: {
    willCheckList,
    toolCheckList
  },
  methods: {
    tabsClick(tab) {
      if (tab === 0) {
        //点击未提交
        this.$refs.willCheckList.getListData();
      } else {
        this.$refs.toolCheckList.getListData();
      }
    }
  }
};
</script>

<style>
</style>
