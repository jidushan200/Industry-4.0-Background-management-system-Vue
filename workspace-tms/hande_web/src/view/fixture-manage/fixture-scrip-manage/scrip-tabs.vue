<template>
<div>
        <Tabs type="card" @on-click="tabsClick">
        <TabPane label="夹具报废审核">
          <fixture-scrip-audit ref="fixtureScripAudit"></fixture-scrip-audit>
        </TabPane>
        <TabPane label="已报废夹具列表">
            <fixture-scrip-manage ref="fixtureScripManage"></fixture-scrip-manage>
        </TabPane>
    </Tabs>
  </div>
  
</template>

<script>
import axios from "@/libs/api.request";
import { dateFormat, objCopy } from "@/libs/tools.js";
import fixtureScripManage from "./fixture-scrip-manage";
import fixtureScripAudit from "./fixture-scrip-audit";

export default {
  components: {
    fixtureScripManage,
    fixtureScripAudit
  },
  methods: {
    tabsClick(tab) {
      if (tab === 0) {
        //点击未提交
        this.$refs.fixtureScripAudit.getListData();
      } else {
        this.$refs.fixtureScripManage.getListData();
      }
    }
  }
};
</script>

<style>
</style>
