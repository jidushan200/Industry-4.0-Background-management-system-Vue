import {
  login,
  logout,
  getUserInfo,
  getMessage,
  getContentByMsgId,
  hasRead,
  removeReaded,
  restoreTrash,
  getUnreadCount
} from '@/api/user'
import {
  setToken,
  getToken
} from '@/libs/util'

export default {
  state: {
    userData: {
      data: {
        access: []
      }
    },
    userName: '',
    userId: '',
    departmentId: '',
    roleId:'',
    avatarImgPath: '',
    token: getToken(),
    access: '',
    hasGetInfo: false,
    unreadCount: 0,
    messageUnreadList: [],
    messageReadedList: [],
    messageTrashList: [],
    messageContentStore: {}
  },
  mutations: {
    setAvatar(state, avatarPath) {
      state.avatarImgPath = avatarPath
    },
    setUserData(state, userData) {
      state.userData = userData;
    },
    setUserId(state, id) {
      state.userId = id
    },
    setRoleId(state, roleId) {
      state.roleId = roleId
    },
    setUserName(state, name) {
      state.userName = name
    },
    setDepartmentId(state, departmentId) {
      state.departmentId = departmentId
    },
    setAccess(state, access) {
      state.access = access
    },
    setToken(state, token) {      
      state.token = token
      setToken(token)
    },
    setHasGetInfo(state, status) {
      state.hasGetInfo = status
    },
    setMessageCount(state, count) {
      state.unreadCount = count
    },
    setMessageUnreadList(state, list) {
      state.messageUnreadList = list
    },
    setMessageReadedList(state, list) {
      state.messageReadedList = list
    },
    setMessageTrashList(state, list) {
      state.messageTrashList = list
    },
    updateMessageContentStore(state, {
      msg_id,
      content
    }) {
      state.messageContentStore[msg_id] = content
    },
    moveMsg(state, {
      from,
      to,
      msg_id
    }) {
      const index = state[from].findIndex(_ => _.msg_id === msg_id)
      const msgItem = state[from].splice(index, 1)[0]
      msgItem.loading = false
      state[to].unshift(msgItem)
    }
  },
  getters: {
    messageUnreadCount: state => state.messageUnreadList.length,
    messageReadedCount: state => state.messageReadedList.length,
    messageTrashCount: state => state.messageTrashList.length
  },
  actions: {
    // 登录
    handleLogin({
      commit
    }, {
      userName,
      password
    }) {
      userName = userName.trim()
      return new Promise((resolve, reject) => {
        login({
          userName,
          password
        }).then(res => {
          console.log(res)
          const data = res.data; // 获取用户的信息
          if (data.code == 200) {
            commit('setToken', data.token)
          } else {

          }
          resolve()
        }).catch(err => {
          reject(err)
        })
      })
    },
    // 退出登录
    handleLogOut({
      state,
      commit
    }) {
      return new Promise((resolve, reject) => {
        /*
         * logout(state.token).then(() => { commit('setToken', '')
         * commit('setAccess', []) resolve() }).catch(err => { reject(err) })
         */
        // 如果你的退出登录无需请求接口，则可以直接使用下面三行代码而无需使用logout调用接口
        commit('setToken', '')
        commit('setAccess', [])
        resolve()
      })
    },
    // 获取用户相关信息
    getUserInfo({
      state,
      commit
    }) {
      return new Promise((resolve, reject) => {
        try {
          getUserInfo(state.token).then(res => {
            const data = res.data;           
            commit('setUserData', data)
            commit('setAvatar', data.avatar)
            commit('setUserName', data.realName)
            commit('setUserId', data.userId)
            commit('setDepartmentId', data.departmentId)
            commit('setRoleId', data.roleId)
            commit('setAccess', data.access)
            commit('setHasGetInfo', true)
            resolve(data)
          }).catch(err => {
            reject(err)
          })
        } catch (error) {
          reject(error)
        }
      })
    },
    // 此方法用来获取未读消息条数，接口只返回数值，不返回消息列表
    getUnreadMessageCount({
      state,
      commit
    }) {
      getUnreadCount(state.token).then(res => {
        const {
          data
        } = res
        commit('setMessageCount', data.data)
      })
    },
    // 获取消息列表，其中包含未读、已读、回收站三个列表
    getMessageList({
      state,
      commit
    }) {
      return new Promise((resolve, reject) => {
        getMessage(state.token).then(res => {
          let unread = [];
          let readed = [];
          if (!!res.data) {
            if (!!res.data.data[0]) {
              unread = res.data.data[0]
            }
            if (!!res.data.data[1]) {
              readed = res.data.data[1]
            }
          }
          const trash = [];
          commit('setMessageCount', unread.length)
          commit('setMessageUnreadList', unread.sort((a, b) => new Date(b.createTime) - new Date(a.createTime)))
          commit('setMessageReadedList', readed.map(_ => {
            _.loading = false
            return _
          }).sort((a, b) => new Date(b.createTime) - new Date(a.createTime)))
          commit('setMessageTrashList', trash.map(_ => {
            _.loading = false
            return _
          }).sort((a, b) => new Date(b.createTime) - new Date(a.createTime)))
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },
    // 根据当前点击的消息的id获取内容
    getContentByMsgId({
      state,
      commit
    }, {
      msg_id
    }) {
      return new Promise((resolve, reject) => {
        let contentItem = state.messageContentStore[msg_id]
        if (contentItem) {
          resolve(contentItem)
        } else {
          getContentByMsgId(msg_id).then(res => {
            const content = res.data
            commit('updateMessageContentStore', {
              msg_id,
              content
            })
            resolve(content)
          })
        }
      })
    },
    // 把一个未读消息标记为已读
    hasRead({
      state,
      commit
    }, {
      pkId
    }) {
      return new Promise((resolve, reject) => {
        hasRead(state.token, pkId).then(() => {
          commit('moveMsg', {
            from: 'messageUnreadList',
            to: 'messageReadedList',
            pkId
          })
          commit('setMessageCount', state.unreadCount - 1)
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },
    // 删除一个已读消息到回收站
    removeReaded({
      state,
      commit
    }, {
      pkId
    }) {
      return new Promise((resolve, reject) => {
        removeReaded(state.token, pkId).then(() => {
          commit('moveMsg', {
            from: 'messageReadedList',
            to: 'messageTrashList',
            pkId
          })
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },
    // 还原一个已删除消息到已读消息
    restoreTrash({
      commit
    }, {
      msg_id
    }) {
      return new Promise((resolve, reject) => {
        restoreTrash(msg_id).then(() => {
          commit('moveMsg', {
            from: 'messageTrashList',
            to: 'messageReadedList',
            msg_id
          })
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    }
  }
}
