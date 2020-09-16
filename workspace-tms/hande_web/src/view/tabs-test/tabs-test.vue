<template>
<div>
        <Tabs type="card" @on-click="tabsClick">
        <TabPane label="标签一">
            <tool-repaired ref="toolRepaired"></tool-repaired>
        </TabPane>
        <TabPane label="标签二">
            <tool-repair ref="toolRepair"></tool-repair>
        </TabPane>
        <TabPane label="标签三">标签三的内容</TabPane>
    </Tabs>
  </div>
  
</template>

<script>
import axios from "@/libs/api.request";
import { dateFormat, objCopy } from "@/libs/tools.js";
import toolRepair from "./tool-repair";
import toolRepaired from "./tool-repaired";

export default {
  components: {
    toolRepair,
    toolRepaired
  },
  methods: {
    tabsClick(tab) {
      if (tab === 0) {
        //点击未提交
        this.$refs.toolRepair.getListData();
      } else {
        //点击已提交
        this.$refs.toolRepaired.getListData();
      }
    }
  }
};
</script>

<style>
</style>
