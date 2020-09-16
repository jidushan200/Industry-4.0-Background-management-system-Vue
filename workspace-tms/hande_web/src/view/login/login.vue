<style lang="less">
@import "./login.less";
</style>

<template>
  <div class="login">
    <div class="login-con">
      <div class="login-title">
        <img src="../../assets/images/login/login_title.png" height="59" width="319"/>
      </div>

      <div class="form-title-img">
       <img src="../../assets/images/login/login_form_title.png"
            height="50"
            width="352"/>
      </div>
      <div icon="log-in" title="欢迎登录" :bordered="false">
        <div class="form-con">

          <Form ref="formInline" :model="formInline" :rules="ruleInline" >
            <FormItem prop="userName">
              <Input type="text"  v-model="formInline.userName" placeholder="请输入工号">
                  <Icon type="ios-person-outline" slot="prepend"></Icon>
              </Input>
            </FormItem>
            <FormItem prop="password">
              <Input type="password" v-model="formInline.password" placeholder="请输入密码" @keyup.enter.native="handleSubmit('formInline')" >
              <Icon type="ios-lock-outline" slot="prepend"></Icon>
              </Input>
            </FormItem>
            <FormItem>
              <Button type="warning" @click="handleSubmit('formInline')">登录</Button>
            </FormItem>
          </Form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from '@/libs/api.request'
import LoginForm from '_c/login-form'
import qs from 'qs'
import { setToken, getToken } from '@/libs/util'
import { mapActions } from 'vuex'
export default {
  components: {
    LoginForm
  },
  data () {
    return {
      formInline: {
        userName: '',
        password: ''
      },
      ruleInline: {
        userName: [{ required: true, message: '请输入工号', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
      }
    }
  },
  created () {
    console.log('login.get')
    console.log(getToken())
  },
  methods: {
    ...mapActions(['handleLogin', 'getUserInfo']),
    handleSubmit () {
      const self = this
      var dataParams = {
        loginName: self.formInline.userName,
        password: self.formInline.password
      }

      axios
        .request({
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
          },
          url: '/login',
          method: 'post',
          isAuth: false,
          data: qs.stringify(dataParams)
        })
        .then(function (res) {
          const data = res.data
          if (data.code === 200) {
            self.$store.commit('setUserData', data)
            self.$store.commit('setToken', data.data.token)
            self.$store.commit('setAvatar', '')
            self.$store.commit('setUserName', data.data.userName)
            self.$store.commit('setUserId', data.data.userId)
            self.$store.commit('setAccess', data.data.access)
            self.$store.commit('setHasGetInfo', true)
            self.$router.push({
              name: self.$config.homeName
            })
            window.sessionStorage.setItem('data', data)
          } else {
            self.$Message.error(data.info)
          }
        })
        .catch(function (err) {
          self.$Message.error('登录错误')
        })
    }
  }
}
</script>

<style>
</style>
