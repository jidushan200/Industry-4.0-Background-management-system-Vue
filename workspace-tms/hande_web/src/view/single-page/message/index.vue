<template>
  <Card shadow>
    <div>
      <div class="message-page-con message-category-con">
        <Menu width="auto" active-name="unread" @on-select="handleSelect">
          <MenuItem name="unread">
            <span class="category-title">未读消息</span>
            <Badge style="margin-left: 10px" :count="messageUnreadCount"></Badge>
          </MenuItem>
          <MenuItem name="readed">
            <span class="category-title">已读消息</span>
            <Badge style="margin-left: 10px" class-name="gray-dadge" :count="messageReadedCount"></Badge>
          </MenuItem>
          <!--
          <MenuItem name="trash">
            <span class="category-title">回收站</span>
            <Badge style="margin-left: 10px" class-name="gray-dadge" :count="messageTrashCount"></Badge>
          </MenuItem>
          -->
        </Menu>
      </div>
      <div class="message-page-con message-list-con">
        <Spin fix v-if="listLoading" size="large"></Spin>
        <Menu width="auto" active-name :class="titleClass" @on-select="handleView">
          <MenuItem v-for="item in messageList" :name="item.pkId" :key="`msg_${item.pkId}`">
            <div>
              <p class="msg-title">{{ item.title }}</p>
              <Badge status="default" :text="item.strTime" />
              <Button
                style="float: right;margin-right: 20px;"
                :style="{ display: item.loading ? 'inline-block !important' : '' }"
                :loading="item.loading"
                size="small"
                :icon="currentMessageType === 'readed' ? 'md-trash' : 'md-redo'"
                :title="currentMessageType === 'readed' ? '删除' : '还原'"
                type="text"
                v-show="currentMessageType !== 'unread'"
                @click.native.stop="removeMsg(item)"
              ></Button>
            </div>
          </MenuItem>
        </Menu>
      </div>
      <div class="message-page-con message-view-con">
        <Spin fix v-if="contentLoading" size="large"></Spin>
        <div class="message-view-header">
          <h2 class="message-view-title">{{ showingMsgItem.title }}</h2>         
        </div>
        <div class="message-view-sendinfo">
        <span v-if="isNull">发送人：<span class="message-view-sender">{{ showingMsgItem.senderName }}</span></span>
         <span v-if="isNull">发送时间：<time class="message-view-time">{{ showingMsgItem.strTime }}</time></span>
         </div>
        <div style="text-indent:35px;font-size:16px;line-height:36px" v-html="messageContent"></div>
      </div>
    </div>
  </Card>
</template>

<script>
import { mapState, mapGetters, mapMutations, mapActions } from "vuex";
const listDic = {
  unread: "messageUnreadList",
  readed: "messageReadedList",
  trash: "messageTrashList"
};
export default {
  name: "message_page",
  data() {
    return {
      isNull: false,
      listLoading: true,
      contentLoading: false,
      currentMessageType: "unread",
      messageContent: "",
      showingMsgItem: {}
    };
  },
  computed: {
    ...mapState({
      messageUnreadList: state => state.user.messageUnreadList,
      messageReadedList: state => state.user.messageReadedList,
      messageTrashList: state => state.user.messageTrashList,
      messageList() {
        return this[listDic[this.currentMessageType]];
      },
      titleClass() {
        return {
          "not-unread-list": this.currentMessageType !== "unread"
        };
      }
    }),
    ...mapGetters([
      "messageUnreadCount",
      "messageReadedCount",
      "messageTrashCount"
    ])
  },
  methods: {
    ...mapMutations([
      //
    ]),
    ...mapActions([
      "getContentByMsgId",
      "getMessageList",
      "hasRead",
      "removeReaded",
      "restoreTrash"
    ]),
    stopLoading(name) {
      this[name] = false;
    },
    handleSelect(name) {
      this.currentMessageType = name;
    },
    handleView(pkId) {
      this.contentLoading = true;

      const row = this.messageList.find(item => item.pkId === pkId);
      this.messageContent = row.message;

      if (row) {
        this.showingMsgItem = row;
        this.isNull = true;
      }
      if (this.currentMessageType === "unread") this.hasRead({ pkId });
      this.stopLoading("contentLoading");

      /*this.getContentByMsgId({ msg_id })
        .then(content => {
          this.messageContent = content;
          const item = this.messageList.find(item => item.msg_id === msg_id);
          if (item) this.showingMsgItem = item;
          if (this.currentMessageType === "unread") this.hasRead({ msg_id });
          this.stopLoading("contentLoading");
        })
        .catch(() => {
          this.stopLoading("contentLoading");
        });*/
    },
    removeMsg(item) {
      item.loading = true;
      const pkId = item.pkId;
      if (this.currentMessageType === "readed") this.removeReaded({ pkId });
      else this.restoreTrash({ pkId });
    }
  },
  mounted() {
    this.listLoading = true;
    // 请求获取消息列表
    this.getMessageList()
      .then(() => this.stopLoading("listLoading"))
      .catch(() => this.stopLoading("listLoading"));
  }
};
</script>

<style lang="less">
.message-page {
  &-con {
    height: ~"calc(100vh - 176px)";
    display: inline-block;
    vertical-align: top;
    position: relative;
    &.message-category-con {
      border-right: 1px solid #e6e6e6;
      width: 200px;
    }
    &.message-list-con {
      border-right: 1px solid #e6e6e6;
      width: 230px;
    }
    &.message-view-con {
      position: absolute;
      left: 446px;
      top: 16px;
      right: 16px;
      bottom: 16px;
      overflow: auto;
      padding: 12px 20px 0;
      .message-view-header {
        margin-bottom: 20px;
        text-align: center;
        .message-view-title {
          font-size: 18px;
          display: inline-block;
        }
      }
      .message-view-sendinfo {
        margin-bottom: 20px;
        text-align: center;
        .message-view-sender {
          margin-right: 20px;
        }
      }
    }
    .category-title {
      display: inline-block;
      width: 65px;
    }
    .gray-dadge {
      background: gainsboro;
    }
    .not-unread-list {
      .msg-title {
        color: rgb(170, 169, 169);
      }
      .ivu-menu-item {
        .ivu-btn.ivu-btn-text.ivu-btn-small.ivu-btn-icon-only {
          display: none;
        }
        &:hover {
          .ivu-btn.ivu-btn-text.ivu-btn-small.ivu-btn-icon-only {
            display: inline-block;
          }
        }
      }
    }
  }
}
</style>
