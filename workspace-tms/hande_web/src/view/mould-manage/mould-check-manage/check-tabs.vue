<template>
<div>
    <Tabs type="card">
      <TabPane label="待检模具">
          <wait-check-list ref="waitCheckList"></wait-check-list>
      </TabPane>
      <TabPane label="已完成">
        <checked-list ref="checkedList"></checked-list>
      </TabPane>
    </Tabs>
  </div>
  
</template>

<script>
import axios from "@/libs/api.request";
import { dateFormat, objCopy } from "@/libs/tools.js";
import checkedList from "./checked-list";
import waitCheckList from "./wait-check-list";

export default {
  components: {
    checkedList,
    waitCheckList
  },
 methods: {
     /*tabsClick(tab) {
      if (tab === 0) {
        //点击待修
        this.$refs.waitCheckList.getListData();
      } else {
        this.$refs.checkedList.getListData();
      }
    }*/
  }
};
</script>
