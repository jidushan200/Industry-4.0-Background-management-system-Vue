<template>
  <div>
    <Tabs type="card" @on-click="tabsClick">
      <TabPane label="待磨刀条">
        <blade-wait-repair ref="bladeWaitRepair"></blade-wait-repair>
      </TabPane>
      <!--
      <TabPane label="磨后待检">
        <blade-wait-check ref="bladeWaitCheck"></blade-wait-check>
      </TabPane>-->
      <TabPane label="刃磨记录">
        <blade-repair-list ref="bladeRepairList"></blade-repair-list>
      </TabPane>
    </Tabs>
  </div>
</template>

<script>
import axios from '@/libs/api.request'
import { dateFormat, objCopy } from '@/libs/tools.js'
import bladeWaitRepair from './blade-wait-repair'
import bladeRepairList from './blade-repair-list'

export default {
  components: {
    bladeWaitRepair,
    bladeRepairList
  },
  methods: {
    tabsClick (tab) {
      if (tab === 0) {
        // 点击未提交
        this.$refs.bladeWaitRepair.getListData()
      } else if (tab === 1) {
        this.$refs.bladeRepairList.getListData()
      }
    }
  }
}
</script>

<style>
</style>
