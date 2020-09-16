<template>
  <div>
    <Row :gutter="20">
      <i-col :md="24">
        <Row>
          <i-col :md="24">
            <div class="home-title" style>待办事项</div>
          </i-col>
        </Row>

        <Row style="padding-top: 16px;">
          <i-col :md="12">
            <div class="home-welcome-avatar">
              <img
                :src="leaderImg"
                alt
              />
            </div>
            <div class="home-welcome-note">
              <h2>
                <span v-sayhitouser></span>
                , {{realName}}, 祝你开心每一天
              </h2>
              <p style="color: #666;">汉德车桥(株洲)齿轮有限公司</p>
            </div>
          </i-col>
          <i-col :md="12" style="height: 60px;padding-bottom: 10px;">
            <ul class="prompt-data">
              <li>
                <div class="prompt-data-name">刀具待审批</div>
                <div class="prompt-data-num" @click="toolPurchase()">{{tPurchQty}}</div>
              </li>
              <li>
                <div class="prompt-data-name">量具待审批</div>
                <div class="prompt-data-num" @click="measurePurchase()">{{mPurchQty}}</div>
              </li>
              <li>
                <div class="prompt-data-name">夹具待审批</div>
                <div class="prompt-data-num" @click="fixturePurchase()">{{fPurchQty}}</div>
              </li>
            </ul>
          </i-col>
        </Row>
      </i-col>
    </Row>
  </div>
</template>

<script>
import axios from "@/libs/api.request";
import { getToken, getBaseUrl } from "@/libs/util";
import leader from "./leader.png";
export default {
  name: "home",
  components: {},
  data() {
    return {
      leaderImg:leader,
      tPurchQty:0,
      mPurchQty:0,
      fPurchQty:0,
      realName: ""
    };
  },
  created() {
    const self = this;
    self.realName = self.$store.state.user.userData.data.realName;
    self.getToDoCount();
  },
  methods: {
    getToDoCount() {
      const self = this;     
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/message/to-do-list",
          isAuth: true,
          method: "post"
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200 && data.data !== null) {
            self.tPurchQty = data.data.toolPurchaseQty;
            self.mPurchQty = data.data.measurePurchaseQty;
            self.fPurchQty = data.data.fixturePurchaseQty;
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },

    toolPurchase() {
      this.$router.push({
        name: "tool_purchase_list"
      });
    },
    measurePurchase() {
      this.$router.push({
        name: "measure_purchase_list"
      });
    },
    fixturePurchase() {
      this.$router.push({
        name: "fixture_purchase_list"
      });
    }
  },
  mounted() {},
  // 封装个小指令，判断当前时间是早上，中午，下午，傍晚，晚上
  directives: {
    sayhitouser: function(el, binding, vnode) {
      let now = new Date(),
        hour = now.getHours();
      if (hour < 6) {
        el.innerHTML = "凌晨好";
      } else if (hour < 9) {
        el.innerHTML = "早上好";
      } else if (hour < 12) {
        el.innerHTML = "上午好";
      } else if (hour < 14) {
        el.innerHTML = "中午好";
      } else if (hour < 17) {
        el.innerHTML = "下午好";
      } else if (hour < 19) {
        el.innerHTML = "傍晚好";
      } else if (hour < 22) {
        el.innerHTML = "晚上好";
      } else {
        el.innerHTML = "夜里好";
      }
    }
  }
};
</script>

<style lang="less">
.count-style {
  font-size: 50px;
}
.home-title {
  font-size: 20px;
  color: rgba(0, 0, 0, 0.85);
  font-weight: 600;
  line-height: 32px;
}
.home-welcome-avatar {
  display: block;
  width: 72px;
  height: 72px;
  border-radius: 72px;
  float: left;
}
.home-welcome-avatar img {
  width: 100%;
}
.home-welcome-note {
  float: left;
  margin-left: 24px;
  padding-top: 4px;
}
.home-welcome-note h2 {
  margin-bottom: 12px;
  color: rgba(0, 0, 0, 0.85);
  font-weight: 500;
  font-size: 20px;
  line-height: 28px;
}
.home-welcome-note p {
  font-size: 14px;
  color: rgba(0, 0, 0, 0.45);
  line-height: 22px;
}
.prompt-data {
  padding: 0;
  margin: 0;
  float: right;
  margin-right: 150px;
}
.prompt-data li {
  float: left;
  display: inline-block;
  padding: 0 20px;
  list-style: none;
  border-right: 1px solid #ccc;
}
.prompt-data li:last-child {
  border-right: none;
}
.prompt-data li div.prompt-data-name {
  font-size: 16px;
  color: #2970bb;
}
.prompt-data li div.prompt-data-num {
  text-align: center;
  margin: 5px 16px;
  font-size: 22px;
  background: #e26001;
  color: #fff;
  cursor: pointer;
}
.prompt-data li div.prompt-data-num:hover {
  background: #e28811;
}
</style>
