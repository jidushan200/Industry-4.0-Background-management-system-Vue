

### 前端框架说明

技术基础  vue+iview

本框架基于iview-admin

请首先熟悉vue和iview基本知识，再看下iview-admin文档

### 用户登录

目前登录和获取用户信息、权限等接口使用的是本地mock数据，登录界面用户名是super_admin或者admin，密码随便填，后期这两个接口写好后再进行对接

### 增加页面

1、在src/view目录下，按照需要新建页面，参考``src/view/knife-manager`` 页面

2、在src/router/routers 里增加路由，参考代码102行-129行

![1562232947890](C:\Users\dell\AppData\Roaming\Typora\typora-user-images\1562232947890.png)

3、发送请求  ，使用了简单封装的axios,后期会根据需要对此进行再次封装；接口的请求请参考代码105-125行

![1562232888566](C:\Users\dell\AppData\Roaming\Typora\typora-user-images\1562232888566.png)
