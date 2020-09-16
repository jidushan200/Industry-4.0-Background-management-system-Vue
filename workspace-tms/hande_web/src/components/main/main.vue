<template>
  <Layout style="height:100%" class="main">
    <Sider
      hide-trigger
      collapsible
      :width="256"
      :collapsed-width="64"
      v-model="collapsed"
      class="left-sider"
      :style="{overflow: 'hidden'}"
    >
      <side-menu
        accordion
        ref="sideMenu"
        :active-name="$route.name"
        :collapsed="collapsed"
        @on-select="turnToPage"
        :menu-list="menuList"
      >
        <!-- 需要放在菜单上面的内容，如Logo，写在side-menu标签内部，如下 -->
        <div class="logo-con">
          <img v-show="!collapsed" :src="maxLogo" key="max-logo" />
          <img v-show="collapsed" :src="minLogo" key="min-logo" />
        </div>
      </side-menu>
    </Sider>
    <Layout>
      <Header class="header-con">
        <header-bar :collapsed="collapsed" @on-coll-change="handleCollapsedChange">
          <fullscreen v-model="isFullscreen" />
          <user
            :message-unread-count="unreadCount"
            :user-avatar="userAvatar"
            style="margin-right: 10px;width:180px"
          />
          <!--<language v-if="$config.useI18n" @on-lang-change="setLocal" style="margin-right: 10px;" :lang="local"/>-->
          <!--      <error-store v-if="$config.plugin['error-store'] && $config.plugin['error-store'].showInHeader" :has-read="hasReadErrorPage" :count="errorCount"></error-store>-->
        </header-bar>
      </Header>
      <Content class="main-content-con">
        <Layout class="main-layout-con">
          <div class="tag-nav-wrapper">
            <tags-nav
              :value="$route"
              @input="handleClick"
              :list="tagNavList"
              @on-close="handleCloseTag"
            />
          </div>
          <Content class="content-wrapper">
            <keep-alive :include="cacheList">
              <router-view />
            </keep-alive>
            <ABackTop :height="100" :bottom="80" :right="50" container=".content-wrapper"></ABackTop>
          </Content>
        </Layout>
      </Content>
    </Layout>
  </Layout>
</template>
<script>
import axios from '@/libs/api.request'
import SideMenu from './components/side-menu'
import HeaderBar from './components/header-bar'
import TagsNav from './components/tags-nav'
import User from './components/user'
import ABackTop from './components/a-back-top'
import Fullscreen from './components/fullscreen'
import Language from './components/language'
import ErrorStore from './components/error-store'
import { mapMutations, mapActions, mapGetters } from 'vuex'
import { getNewTagList, routeEqual } from '@/libs/util'
import routers from '@/router/routers'
import minLogo from '@/assets/images/logo-min.jpg'
import maxLogo from '@/assets/images/login/login_title.png'
import './main.less'
export default {
  name: 'Main',
  components: {
    SideMenu,
    HeaderBar,
    Language,
    TagsNav,
    Fullscreen,
    ErrorStore,
    User,
    ABackTop
  },
  data () {
    return {
      timer: null,
      collapsed: false,
      minLogo,
      maxLogo,
      isFullscreen: false
    }
  },
  computed: {
    ...mapGetters(['errorCount']),
    tagNavList () {
      return this.$store.state.app.tagNavList
    },
    tagRouter () {
      return this.$store.state.app.tagRouter
    },
    userAvatar () {
      return this.$store.state.user.avatarImgPath
    },
    cacheList () {
      const list = [
        'ParentView',
        ...(this.tagNavList.length
          ? this.tagNavList
            .filter(item => !(item.meta && item.meta.notCache))
            .map(item => item.name)
          : [])
      ]
      return list
    },
    menuList () {
      return this.$store.getters.menuList
    },
    local () {
      return this.$store.state.app.local
    },
    hasReadErrorPage () {
      return this.$store.state.app.hasReadErrorPage
    },
    unreadCount () {
      return this.$store.state.user.unreadCount
    }
  },
  methods: {
    ...mapMutations([
      'setBreadCrumb',
      'setTagNavList',
      'addTag',
      'setLocal',
      'setHomeRoute',
      'closeTag'
    ]),
    ...mapActions(['handleLogin', 'getUnreadMessageCount']),
    turnToPage (route) {
      let { name, params, query } = {}
      if (typeof route === 'string') name = route
      else {
        name = route.name
        params = route.params
        query = route.query
      }
      if (name.indexOf('isTurnByHref_') > -1) {
        window.open(name.split('_')[1])
        return
      }
      this.$router.push({
        name,
        params,
        query
      })
    },
    handleCollapsedChange (state) {
      this.collapsed = state
    },
    handleCloseTag (res, type, route) {
      if (type !== 'others') {
        if (type === 'all') {
          this.turnToPage(this.$config.homeName)
        } else {
          if (routeEqual(this.$route, route)) {
            this.closeTag(route)
          }
        }
      }
      this.setTagNavList(res)
    },
    handleClick (item) {
      this.turnToPage(item)
    },
    // 跳转到未读消息页面
    clickGotoUnreadMsg () {
      console.log('跳转到未读消息页面')
    },
    // 查询未读取消息
    getUnreadMsg () {
      const self = this
      axios
        .request({
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
          },
          url: '/message/count',
          method: 'get',
          isAuth: true
        })
        .then(function (res) {
          const data = res.data
          if (data.code === 200) {
            if (data.data > 0) {
              self.$Notice.info({
                title: '您有未读消息',
                desc: '未读消息',
                duration: 3000,
                render: h => {
                  return h('span', [
                    '您有' + data.data + '条数据，',
                    h(
                      'a',
                      {
                        on: {
                          click: () => {
                            self.$Notice.destroy()
                            self.$router.push({
                              name: 'message_page'
                            })
                          }
                        }
                      },
                      '点击处理'
                    )
                  ])
                }
              })
            }
          } else {
            self.$Message.error(data.info)
          }
        })
        .catch(function (err) {})
    },
    getRealTimeMsg () {
      const self = this
      axios
        .request({
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
          },
          url: '/message/get-immediate-list',
          method: 'post',
          isAuth: true
        })
        .then(function (res) {
          const data = res.data
          if (data.code === 200) {
            // @todo 此处仿照上方的进行处理，可以对数组进行遍历
            for (var item of data.data) {
              self.$Notice.info({
                title: '消息提醒',
                desc: item.message
              })
            }
          } else {
            self.$Message.error(data.info)
          }
        })
        .catch(function (err) {})
    }
  },
  watch: {
    $route (newRoute) {
      const { name, query, params, meta } = newRoute
      this.addTag({
        route: { name, query, params, meta },
        type: 'push'
      })
      this.setBreadCrumb(newRoute)
      this.setTagNavList(getNewTagList(this.tagNavList, newRoute))
      this.$refs.sideMenu.updateOpenName(newRoute.name)
    }
  },
  created () {
    this.getUnreadMsg()
  },
  mounted () {
    /**
     * @description 初始化设置面包屑导航和标签导航
     */
    const self = this
    this.setTagNavList()
    this.setHomeRoute(routers)
    const { name, params, query, meta } = this.$route
    this.addTag({
      route: { name, params, query, meta }
    })
    this.setBreadCrumb(this.$route)
    // 设置初始语言
    this.setLocal(this.$i18n.locale)
    // 如果当前打开页面不在标签栏中，跳到homeName页
    if (!this.tagNavList.find(item => item.name === this.$route.name)) {
      this.$router.push({
        name: this.$config.homeName
      })
    }

    this.timer = setInterval(function () {
      self.getRealTimeMsg()
    }, 600000)
  },
  beforeDestroy () {
    // 清除定时器
    clearInterval(this.timer)
  }
}
</script>
