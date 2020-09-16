<template>
  <div class="user-avatar-dropdown">
    <Row>
      <Col span="16">
      <span style="font-size: 14px;">当前用户:{{realName}}</span>
      </Col>
      <Col span="8">
      <Dropdown @on-click="handleClick">
        <Badge :dot="!!messageUnreadCount">
          <!-- <img class="avator-img" :src="userAvatar"/> -->
          <Avatar :src="avator_img" />
        </Badge>
        <Icon :size="18" type="md-arrow-dropdown"></Icon>
        <DropdownMenu slot="list">
          <DropdownItem name="message">消息中心<Badge style="margin-left: 10px" :count="messageUnreadCount"></Badge></DropdownItem>
          <DropdownItem name="upadatepsw">修改密码</DropdownItem>
          <DropdownItem name="logout">退出登录</DropdownItem>
        </DropdownMenu>
      </Dropdown>
      </Col>
    </Row>
    <!-- 修改密码的模态框 -->
    <Modal title="修改密码" v-model="updatePswModel" width="400" draggable>
      <Form ref="userData" :model="userData" :rules="ruleInline" :label-width="100" inline>
        <FormItem prop="oldloginPwd" label="原密码">
          <Input type="password" style="width:200px" v-model="userData.oldloginPwd" placeholder="原始密码"></Input>
        </FormItem>
        <FormItem prop="loginPwd" label="新密码">
          <Input type="password" style="width:200px" v-model="userData.loginPwd" placeholder="新密码"></Input>
        </FormItem>
        <FormItem prop="repeatLoginPsw" label="重复新密码">
          <Input type="password" style="width:200px" v-model="userData.repeatLoginPsw" placeholder="重复新密码"></Input>
        </FormItem>
      </Form>
      <div slot="footer">
        <Button type="primary" @click="handleUpdatePsw('userData')">确定</Button>
        <Button type="default" @click="cancleModel()" style="margin-right:15px">取消</Button>
      </div>
    </Modal>
  </div>
</template>

<script>
import "./user.less";
import { mapActions } from "vuex";
import { updateLoginPsw } from "@/api/user.js";
import { getToken } from "@/libs/util";
import avator_img from "./avator.png";
export default {
  name: "User",
  data() {
    return {
      avator_img,
      avatarImg: "",
      loginName: "",
      realName: "",
      updatePswModel: false,
      userData: {
        userName: "",
        oldloginPwd: "",
        loginPwd: "",
        repeatLoginPsw: ""
      },
      ruleInline: {
        oldloginPwd: [
          {
            required: true,
            message: "请输入原始密码",
            trigger: "blur"
          }
        ],
        loginPwd: [
          {
            required: true,
            message: "请输入新密码",
            trigger: "blur"
          }
        ],
        repeatLoginPsw: [
          {
            required: true,
            message: "请重新输入新密码",
            trigger: "blur"
          }
        ]
      }
    };
  },
  props: {
    userAvatar: {
      type: String,
      default: ""
    },
    messageUnreadCount: {
      type: Number,
      default: 0
    }
  },
  created() {
    const self = this;
    self.init();
  },
  methods: {
    init() {
      const self = this;
      self.userId = self.$store.state.user.userData.data.userId;
      self.realName = self.$store.state.user.userData.data.realName;
    },
    ...mapActions(["handleLogOut"]),
    logout() {
      this.handleLogOut().then(() => {
        this.$router.push({
          name: "login"
        });
      });
    },
    message() {
      this.$router.push({
        name: "message_page"
      });
    },
    updatePsw() {
      const self = this;
      self.updatePswModel = true;
      self.userData.userName = self.realName;
    },
    handleUpdatePsw(name) {
      this.$refs[name].validate(valid => {
        if (valid) {
          if (this.userData.loginPwd != this.userData.repeatLoginPsw) {
            this.$Message.error("两次密码输入不一致!");
          } else {
            updateLoginPsw(
              getToken(),
              this.userData.oldloginPwd,
              this.userData.loginPwd
            ).then(response => {
              if (response.data.code === 200) {
                let _this = this;
                this.updatePswModel = false;
                _this.$Message.success("密码修改成功,请重新登录");
                setTimeout(() => {                  
                  _this.logout();
                }, 800);
              } else {
                this.$Message.error(response.data.info);
              }
            });
          }
        } else {
          this.$Message.error("请输入内容");
        }
      });
    },
    cancleModel() {
      this.updatePswModel = false;
    },
    handleClick(name) {
      switch (name) {
        case "logout":
          this.logout();
          break;
        case "message":
          this.message();
          break;
        case "upadatepsw":
          this.updatePsw();
          break;
      }
    }
  }
};
</script>
<style scoped>
/* .ivu-input-wrapper {
  width: 60% !important;
} */
</style>
<style>
/* .update-form .ivu-form-item-label {
  width: 95px !important;
  text-align: left !important;
} */
</style>
