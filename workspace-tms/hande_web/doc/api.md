[用户相关]
-----------
### 登录验证
#### 接口功能
> 验证账号密码
#### 接口地址
> /user/login
#### 返回格式
> JSON
#### 请求方式
> post
#### 请求参数

```json
{
    "userName":"admin",
    "password":123456
}
```
#### 返回字段
| 返回字段 | 字段类型 | 说明                             |
|----------|----------|----------------------------------|
| status   | int      | 返回结果状态。200：正常；0：错误。 |
| msg   | string   | 错误说明                         |
| data     | 对象   | 数据    |

```json
{
    "status": 200,
    "msg":"",
    "data":{
      "token":"dddddd232323233" // 鉴权token
    }
    
}
```

### 查询用户信息
#### 接口功能
> 查询用户相关信息
#### 接口地址
> /user/get_info?token=
#### 返回格式
> JSON
#### 请求方式
> get
#### 请求参数

```
#### 返回字段
| 返回字段 | 字段类型 | 说明                             |
|----------|----------|----------------------------------|
| status   | int      | 返回结果状态。200：正常；0：错误。 |
| msg   | string   | 错误说明                         |
| data     | 对象   | 数据       |
                                 
​```json
{
    "status": 200,
    "msg":"",
    "data":{
      "user_id":"1"，
      "name":"admin"，
      "token":"dddd"，
      "avatar" :"" // 头像,
      "access:['admin','depart_user']  // 角色组,默认admin
    }
    
}
```
