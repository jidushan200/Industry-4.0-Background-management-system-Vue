<template>
<div>
        <Tabs type="card" @on-click="tabsClick">
        <TabPane label="待涂刀具">
            <tool-coat ref="toolCoat"></tool-coat>
        </TabPane>
        <TabPane label="回涂刀具">
            <tool-coated ref="toolCoated"></tool-coated>
        </TabPane>
        <TabPane label="涂层记录">
          <tool-coat-list ref="toolCoatList"></tool-coat-list>
        </TabPane>
    </Tabs>
  </div>
  
</template>

<script>
import axios from "@/libs/api.request";
import { dateFormat, objCopy } from "@/libs/tools.js";
import toolCoat from "./tool-coat";
import toolCoated from "./tool-coated";
import toolCoatList from "./tool-coat-list";

export default {
  components: {
    toolCoated,
    toolCoat,
    toolCoatList
  },
  methods: {
    tabsClick(tab) {
      if (tab === 0) {
        //点击未提交
        this.$refs.toolCoat.getListData();
      } else if (tab === 1) {
        //点击已提交
        this.$refs.toolCoated.getListData();
      } else {
        this.$refs.toolCoatList.getListData();
      }
    }
  }
};
</script>

<style>
</style>
