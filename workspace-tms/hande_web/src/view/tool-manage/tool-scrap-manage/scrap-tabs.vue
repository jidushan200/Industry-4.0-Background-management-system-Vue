<template>
<div>
        <Tabs type="card" @on-click="tabsClick">
        <TabPane label="刀具报废审核">
          <tool-scrap-audit ref="toolScrapAudit"></tool-scrap-audit>
        </TabPane>
        <TabPane label="已报废刀具列表">
            <tool-scrap-manage ref="toolScrapManage"></tool-scrap-manage>
        </TabPane>
    </Tabs>
  </div>
  
</template>

<script>
import axios from "@/libs/api.request";
import { dateFormat, objCopy } from "@/libs/tools.js";
import toolScrapManage from "./tool-scrap-manage";
import toolScrapAudit from "./tool-scrap-audit";

export default {
  components: {
    toolScrapManage,
    toolScrapAudit
  },
  methods: {
    tabsClick(tab) {
      if (tab === 0) {
        //点击未提交
        this.$refs.toolScrapAudit.getListData();
      } else {
        this.$refs.toolScrapManage.getListData();
      }
    }
  }
};
</script>

<style>
</style>
