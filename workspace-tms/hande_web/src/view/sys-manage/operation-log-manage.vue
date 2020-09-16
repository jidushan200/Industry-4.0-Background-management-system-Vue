<template>
  <div>
    <Card>
      <Form ref="formInline" :model="searchCondition" :label-width="80" inline>
        <Row>
            <FormItem prop="operateCode" label="操作人">
              <Input type="text" v-model="searchCondition.operateName" placeholder="操作人" />
            </FormItem>         
            <FormItem prop="operateInfo" label="操作内容">
              <Input type="text" v-model="searchCondition.operateInfo" placeholder="操作内容" />
            </FormItem>
            <FormItem prop="dateInterval" label="时间区间">
              <DatePicker
                type="daterange"
                split-panels
                placeholder="请选择时间区间"
                placement="bottom-end"
                v-model="searchCondition.dateInterval"
                style="width:180px;"
              ></DatePicker>
            </FormItem>
            <FormItem>
              <Button type="primary" @click="refreshList">搜索</Button>&nbsp;
              <Button style type="dashed" @click="doReset('formInline')">重置</Button>&nbsp;
            </FormItem>         
        </Row>
      </Form>

      <Table
        :data="tableData.rows"
        :columns="tableColumns"
        border
        stripe
        highlight-row
        ref="userList"
      ></Table>
      <div style="margin: 10px;overflow: hidden">
        <div style="float: right;">
          <Page show-total
            :total="tableData.total"
            :page-size="searchCondition.rows"
            :current="searchCondition.page"
            show-sizer
            @on-change="changePage"
            @on-page-size-change="handlePageSize"
          ></Page>
        </div>
      </div>
    </Card>
  </div>
</template>
<script>
import axios from "@/libs/api.request";
import { dateFormat } from "@/libs/tools.js";
export default {
  components: {},
  data() {
    return {
      searchCondition: {
        dateInterval: [],
        operateInfo: "",
        operateName:"",
        page: 1,
        rows: 10
      },
      tableData: {
        total: 0, // 总页数
        rows: [] // 每页条数
      },
      tableColumns: [
        {
          title: "操作人",
          align: "center",
          key: "operateName",
          width: 150
        },
        {
          title: "操作内容",
          align: "center",
          key: "operateInfo"
        },
        {
          title: "操作时间",
          align: "center",
          key: "createTime",
          width: 150,
          render: function(h, params) {
            /**
             * render写法 格式化日期
             */
            return h(
              "div",
              dateFormat(new Date(params.row.createTime), "yyyy-MM-dd HH:mm:ss")
            );
          },
          minWidth: 250
        },
        {
          title: "ip地址",
          align: "center",
          key: "ip",
          width: 150
        }
      ] //table列信息
    };
  },
  created() {
    const self = this;
    self.loadTableData();
  },
  methods: {
    refreshList(searchData) {
      const self = this;
      self.loadTableData();
    },
    //获取数据列表方法
    loadTableData() {
      const self = this;
      if (!!self.searchCondition.dateInterval) {
        let beginDate = self.searchCondition.dateInterval[0];
        let endDate = self.searchCondition.dateInterval[1];
        if (!!beginDate) {
          self.searchCondition.beginDate = dateFormat(
            new Date(beginDate),
            "yyyy-MM-dd"
          );
        } else {
          self.searchCondition.beginDate = null;
        }
        if (!!endDate) {
          self.searchCondition.endDate = dateFormat(
            new Date(endDate),
            "yyyy-MM-dd"
          );
        } else {
          self.searchCondition.endDate = null;
        }
      } else {
        self.searchCondition.beginDate = null;
        self.searchCondition.endDate = null;
      }
      let para = {
        page: self.searchCondition.page,
        rows: self.searchCondition.rows,
        operateInfo: self.searchCondition.operateInfo,
        operateName: self.searchCondition.operateName,
        beginDate: self.searchCondition.beginDate,
        endDate: self.searchCondition.endDate
      };
      axios
        .request({
          headers: {
            "Content-Type": "application/json; charset=UTF-8"
          },
          url: "/auth/operation-log-pagelist.json",
          method: "post",
          isAuth: true,
          params: para
        })
        .then(function(res) {
          const data = res.data;
          if (data.code === 200) {
            self.tableData = data.data;
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },
    
    doReset(name) {
      const self = this;
      self.$refs[name].resetFields();
      self.loadTableData();
    },
    changePage(cuurentPage,staffName) {
      const self = this;
      self.$set(self.searchCondition, "page", cuurentPage);  
      self.loadTableData();
    },
    handlePageSize(value) {
      const self = this;
      self.$set(self.searchCondition, "rows", value);
      self.loadTableData();
    }
  }
};
</script>
