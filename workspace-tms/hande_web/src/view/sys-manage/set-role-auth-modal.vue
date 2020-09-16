<style lang="less">
//引入css文件
@import "./set-role-auth-modal.less";
</style>
<template>
  <Modal draggable v-model="setRoleAuthModalShow" title="角色权限设置" width="600" class="set-role-auth-modal">
    <Tree ref="treeelement" :data="treeNode" show-checkbox @on-check-change="onCheckChange"></Tree>
    <Spin size="large" fix v-if="spinShow"></Spin>
    <div slot="footer" class="footer">
      <Button @click="ok" type="primary" :disabled="isDisabled">保存</Button>
      <Button @click="cancel">关闭</Button>
    </div>
  </Modal>
</template>

<script>
import { getTrees } from "@/libs/tools.js";
import axios from "@/libs/api.request";
import { authList, roleAuthList, roleAuthAdd } from "@/api/role_manage.js";
export default {
  name: "set-role-auth-modal",
  data() {
    return {
      isDisabled: false,
      setRoleAuthModalShow: false,
      allAuthTreeData: [],
      currentRole: {},
      selectAuthkeyIds: [],
      fullAuthkeyIds: [],
      spinShow: false,
      defaultProps: {
        children: "children",
        label: "authorName"
      },
      nodeLists: [], // 原始的权限树
      treeNode: [
        {
          keyId: 1,
          title: "parent 1",
          expand: true,
          children: [
            {
              title: "parent 1-1",
              keyId: 2,
              expand: true,
              children: [
                {
                  title: "leaf 1-1-1",
                  keyId: 3,
                  checked: true
                },
                {
                  title: "leaf 1-1-2",
                  keyId: 4,
                  checked: true
                }
              ]
            },
            {
              title: "parent 1-2",
              keyId: 5,
              expand: true,
              children: [
                {
                  title: "leaf 1-2-1",
                  checked: true,
                  keyId: 6
                },
                {
                  title: "leaf 1-2-1",
                  keyId: 7
                }
              ]
            }
          ]
        }
      ]
    };
  },
  created() {},
  methods: {
    init() {},
    //数据加载方法
    loadTreeData() {
      const self = this;
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/auth/sys-auth-list.json",
          isAuth: true,
          method: "post"
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            self.nodeLists = [data.data];
            self.loadCurrentRoleAuthData();
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },
    //当前角色权限加载方法
    loadCurrentRoleAuthData() {
      const self = this;
      let dataParams = {
        roleId: this.currentRole.pkId
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/auth/role-auth-get-by-role-id.json",
          method: "post",
          isAuth: true,
          params: dataParams
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            self.activeNodes(data.data);
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },
    //当前角色权限保存
    saveCurrentRoleAuthData() {
      this.isDisabled = true;
      const self = this;
      let dataParams = {
        authCodes: self.selectAuthkeyIds
          .map(function(item) {
            return item.authorCode;
          })
          .join(","),
        fullAuths: self.fullAuthkeyIds
          .map(function(item) {
            return item.authorCode;
          })
          .join(","),
        roleId: self.currentRole.pkId,
        roleName: self.currentRole.roleName
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/auth/role-auth-save.json",
          method: "post",
          isAuth: true,
          params: dataParams
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            self.setRoleAuthModalShow = false;
            self.$Message.success("保存成功");
            self.isDisabled = false;
          } else {
            self.$Notice.error({
              title: "错误提示",
              desc: res.data.info
            });
            self.isDisabled = false;
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },
    showModal(currentRole) {
      this.setRoleAuthModalShow = true;
      this.currentRole = currentRole;
      this.loadTreeData();
      // this.loadCurrentRoleAuthData();
    },
    // 权限modal cancel
    cancel() {
      this.setRoleAuthModalShow = false;
    },
    // 权限modal ok
    ok() {
      let self = this;
      //调用保存方法
      self.selectAuthkeyIds = [];
      self.fullAuthkeyIds = [];
      let checkedKeys = self.$refs.treeelement.getCheckedNodes();
      let fullkeys = self.$refs.treeelement.getCheckedAndIndeterminateNodes();
      self.selectAuthkeyIds = [...checkedKeys];
      self.fullAuthkeyIds = [...fullkeys];
      self.saveCurrentRoleAuthData();
    },
    onCheckChange(checkedArr, currentValue) {
      //树节点复选框变化
      // console.log("树节点复选框变化");
    },

    activeNodes(checkedIdArr) {
      const self = this;
      let nodes = [].concat(self.nodeLists);
      for (let i = 0; i < nodes.length; i++) var level1 = nodes[i];
      if (
        checkedIdArr.some(function(item) {
          return item == level1["authorCode"];
        })
      ) {
        level1["checked"] = true;
      }

      // 第二级
      if (
        typeof level1["children"] != "undefined" &&
        Array.isArray(level1["children"])
      ) {
        for (var j = 0; j < level1["children"].length; j++) {
          var level2 = level1["children"][j];
          if (
            checkedIdArr.some(function(item) {
              return item == level2["authorCode"];
            })
          ) {
            level2["checked"] = true;
          }
          // 判断是否有第三级
          if (
            typeof level2["children"] != "undefined" &&
            Array.isArray(level2["children"])
          ) {
            for (var m = 0; m < level2["children"].length; m++) {
              var level3 = level2["children"][m];
              if (
                checkedIdArr.some(function(item) {
                  return item == level3["authorCode"];
                })
              ) {
                level3["checked"] = true;
              }
              // 判断是否有第四级
              if (
                typeof level3["children"] != "undefined" &&
                Array.isArray(level3["children"])
              ) {
                for (var n = 0; n < level3["children"].length; n++) {
                  var level4 = level3["children"][n];
                  if (
                    checkedIdArr.some(function(item) {
                      return item == level4["authorCode"];
                    })
                  ) {
                    level4["checked"] = true;
                  }
                  // 判断是否有第五级
                  if (
                    typeof level4["children"] != "undefined" &&
                    Array.isArray(level4["children"])
                  ) {
                    for (var l = 0; l < level4["children"].length; l++) {
                      var level5 = level4["children"][l];
                      if (
                        checkedIdArr.some(function(item) {
                          return item == level5["authorCode"];
                        })
                      ) {
                        level5["checked"] = true;
                      }
                    }
                  }
                  // 判断是否有第五级
                }
              }
              // 判断是否有第四级
            }
          }
          // 判断是否有第三级
        }
      }
      // 判断是否有第二级

      var a = JSON.stringify(nodes);
      self.treeNode = JSON.parse(a);
    }
  }
};
</script>
