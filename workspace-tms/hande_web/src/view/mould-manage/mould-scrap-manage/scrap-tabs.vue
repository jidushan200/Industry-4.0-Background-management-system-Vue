<template>
<div>
        <Tabs type="card" @on-click="tabsClick">
        <TabPane label="模具报废审核">
          <mould-scrap-audit ref="mouldScrapAudit"></mould-scrap-audit>
        </TabPane>
        <TabPane label="已报废模具列表">
            <mould-scrap-manage ref="mouldScrapManage"></mould-scrap-manage>
        </TabPane>
        <TabPane label="新模具报废列表">
            <new-mould-scrap ref="newMouldScrap"></new-mould-scrap>
        </TabPane>
    </Tabs>
  </div>
  
</template>

<script>
import axios from "@/libs/api.request";
import { dateFormat, objCopy } from "@/libs/tools.js";
import mouldScrapManage from "./mould-scrap-manage";
import mouldScrapAudit from "./mould-scrap-audit";
import newMouldScrap from "./new-mould-scrap";

export default {
  components: {
    mouldScrapManage,
    mouldScrapAudit,
    newMouldScrap
  },
  methods: {
    tabsClick(tab) {
      if (tab === 0) {
        //点击未提交
        this.$refs.mouldScrapAudit.getListData();
      } else if (tab === 1) {
        this.$refs.mouldScrapManage.getListData();
      } else {
        this.$refs.newMouldScrap.getListData();
      }
    }
  }
};
</script>

<style>
</style>
