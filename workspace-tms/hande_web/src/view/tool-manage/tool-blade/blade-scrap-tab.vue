<template>
  <div>
    <Tabs type="card" @on-click="tabsClick">
      <TabPane label="刀具报废申请">
        <blade-scrap-apply-list ref="bladeScrapApplyList"></blade-scrap-apply-list>
      </TabPane>
      <TabPane label="已报废刀条列表">
        <blade-scrap-list ref="bladeScrapList"></blade-scrap-list>
      </TabPane>
    </Tabs>
  </div>
</template>

<script>
import axios from "@/libs/api.request";
import { dateFormat, objCopy } from "@/libs/tools.js";
import bladeScrapApplyList from "./blade-scrap-apply-list";
import bladeScrapList from "./blade-scrap-list";

export default {
  components: {
    bladeScrapApplyList,
    bladeScrapList
  },
  methods: {
    tabsClick(tab) {
      if (tab === 0) {
        //点击未提交
        this.$refs.bladeScrapApplyList.getListData();
      } else {
        this.$refs.bladeScrapList.getListData();
      }
    }
  }
};
</script>

<style>
</style>
