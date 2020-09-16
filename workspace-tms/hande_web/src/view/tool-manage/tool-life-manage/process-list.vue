<template>
  <div>
    <Tabs type="card">
      <TabPane label="刀具生产统计">
        <tool-process-list ref="toolProcessList"></tool-process-list>
      </TabPane>
      <TabPane label="刀条生产统计">
        <blade-process-list ref="bladeProcessList"></blade-process-list>
      </TabPane>
    </Tabs>
  </div>
</template>

<script>
import axios from '@/libs/api.request'
import { dateFormat, objCopy } from '@/libs/tools.js'
import toolProcessList from './tool-process-list'
import bladeProcessList from './blade-process-list'

export default {
  components: {
    toolProcessList,
    bladeProcessList
  },
  methods: {}
}
</script>
