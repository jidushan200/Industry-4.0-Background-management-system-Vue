<template>
  <div>
    <Tabs type="card">
      <TabPane label="组合刃磨待检">
        <repair-compose-wait-check-list ref="repairComposeWaitCheckList"></repair-compose-wait-check-list>
      </TabPane>
      <TabPane label="组合检验记录">
        <repair-compose-check-list ref="repairComposeCheckList"></repair-compose-check-list>
      </TabPane>
    </Tabs>
  </div>
</template>

<script>
import axios from '@/libs/api.request'
import { dateFormat, objCopy } from '@/libs/tools.js'

import repairComposeWaitCheckList from './repair-compose-wait-check-list.vue'
import repairComposeCheckList from './repair-compose-check-list.vue'

export default {
  components: {
    repairComposeWaitCheckList,
    repairComposeCheckList
  },
  methods: {
    tabsClick (tab) {}
  }
}
</script>

<style>
</style>
