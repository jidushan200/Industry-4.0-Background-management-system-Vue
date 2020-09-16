<template>
  <div>
    <Tabs type="card" @on-click="tabsClick">
      <TabPane label="待磨刀具">
        <tool-repair ref="toolRepair"></tool-repair>
      </TabPane>
      <TabPane label="磨后待检">
        <tool-repaired ref="toolRepaired"></tool-repaired>
      </TabPane>
      <TabPane label="刃磨记录">
        <tool-repair-list ref="toolRepairList"></tool-repair-list>
      </TabPane>
    </Tabs>
  </div>
</template>

<script>
import axios from "@/libs/api.request";
import { dateFormat, objCopy } from "@/libs/tools.js";
import toolRepair from "./tool-repair";
import toolRepaired from "./tool-repaired";
import toolRepairList from "./tool-repair-list";

export default {
  components: {
    toolRepaired,
    toolRepair,
    toolRepairList
  },
  methods: {
    tabsClick(tab) {
      if (tab === 0) {
        //点击未提交
        this.$refs.toolRepair.getListData();
      } else if (tab === 1) {
        //点击已提交
        this.$refs.toolRepaired.getListData();
      } else {
        this.$refs.toolRepairList.getListData();
      }
    }
  }
};
</script>

<style>
</style>
