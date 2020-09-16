<template>
  <div>
    <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="80">
      <FormItem label="City" prop="city">
        <Select v-model="formValidate.city" placeholder="请选择城市">
          <Option v-for="item in citys" :value="item.id" :key="item.id">{{item.name}}</Option>
        </Select>
      </FormItem>
    </Form>
  </div>
</template>

<script>
import axios from "@/libs/api.request";
import { getToken, getBaseUrl } from "@/libs/util";
export default {
  name: "join_page",
  data() {
    return {
      citys: [
        { id: 1, name: "1" },
        { id: 2, name: "2" },
        { id: 3, name: "3" },
        { id: 4, name: "4" }
      ],
      formValidate: {
        city: ""
      },
      ruleValidate: {
        city: [
          {
            required: true,
            message: "Please select the city",
            trigger: "change",
            type: "number"
          }
        ]
      }
    };
  },
  created() {
    this.getListData();
  },
  methods: {
    getListData() {
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          isAuth: true,
          url: "/tool/tool-page-list",
          method: "post",
          params: self.searchCondition
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    }
  }
};
</script>
<style>
.join-page {
  text-align: center;
}
.qq-group-img {
  width: 100%;
}
.join-page-other-icon {
  width: 20px;
  vertical-align: middle;
  margin-right: 6px;
}
.join-page-other {
  text-align: left;
}
.join-page-other .ivu-btn {
  margin-right: 6px;
}
</style>
