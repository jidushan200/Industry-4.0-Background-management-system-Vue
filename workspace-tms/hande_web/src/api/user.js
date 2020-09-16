import axios from '@/libs/api.request'

export const login = ({
  userName,
  password
}) => {
  console.log('user.login')
  const data = {
    userName,
    password
  }
  return axios.request({
    url: '/user/login',
    data,
    method: 'post'
  })
}

export const getUserInfo = (token) => {
  return axios.request({
    url: '/user/get_info',
    params: {
      token
    },
    method: 'get'
  })
}

export const updateLoginPsw = (token, oldloginPwd, loginPwd) => {
  return axios.request({
    url: '/user/user-my-loginpwd-update',
    params: {
      token: token,
      oldloginPwd: oldloginPwd,
      loginPwd: loginPwd
    },
    method: 'post'
  })
}


export const getUnreadCount = (token) => {
  return axios.request({
    url: 'message/count',
    params: {
      token
    },
    method: 'get'
  })
}

export const getMessage = (token) => {
  return axios.request({
    url: 'message/init',
    params: {
      token
    },
    method: 'get'
  })
}

export const getContentByMsgId = msg_id => {
  return axios.request({
    url: 'message/content',
    method: 'get',
    params: {
      msg_id
    }
  })
}

export const hasRead = (token, msg_id) => {
  return axios.request({
    url: 'message/update-read-flag',
    method: 'post',
    params: {
      token: token,
      pkId: msg_id
    }
  })
}

export const removeReaded = (token, msg_id) => {
  return axios.request({
    url: 'message/delete-message',
    method: 'post',
    params: {
      token: token,
      pkId: msg_id
    }
  })
}

export const restoreTrash = msg_id => {
  return axios.request({
    url: 'message/restore',
    method: 'post',
    data: {
      msg_id
    }
  })
}
