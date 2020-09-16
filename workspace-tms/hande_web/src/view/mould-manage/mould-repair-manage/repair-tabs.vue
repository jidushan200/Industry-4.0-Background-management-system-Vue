<template>
<div>
    <Tabs type="card" @on-click="tabsClick">
      <TabPane label="待修模具">
          <mould-repair ref="mouldRepair"></mould-repair>
      </TabPane>
      <TabPane label="修磨记录">
        <mould-repair-list ref="mouldRepairList"></mould-repair-list>
      </TabPane>
    </Tabs>
  </div>
  
</template>

<script>
import axios from "@/libs/api.request";
import { dateFormat, objCopy } from "@/libs/tools.js";
import mouldRepair from "./mould-repair";
import mouldRepairList from "./mould-repair-list";

export default {
  components: {
    mouldRepair,
    mouldRepairList
  },
  methods: {
    tabsClick(tab) {
      if (tab === 0) {
        //点击待修
        this.$refs.mouldRepair.getListData();
      } else {
        this.$refs.mouldRepairList.getListData();
      }
    }
  }
};
</script>
