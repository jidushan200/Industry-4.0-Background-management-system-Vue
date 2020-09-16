let user_manage_form_config = {
  formValidate: {},
  ruleValidate: {
    loginName: [
      {
        required: true,
        message: "登陆名不能为空",
        trigger: "blur"
      }
    ],
    loginPwd: [
      {
        required: true,
        message: "登陆密码为6-12位数字及字母的组合",
        min: 6,
        max: 12,
        //pattern: /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,12}$/,
        trigger: "blur"
      }
    ],
    userName: [
      {
        required: true,
        message: "用户名不能为空",
        trigger: "blur"
      }
    ],
    realName: [
      {
        required: true,
        message: "真实姓名不能为空",
        trigger: "blur"
      }
    ],
   /* mobile: [
      {
        required: true,
        message: "联系电话不能为空",
        trigger: "blur"
      },
    ],*/
    roleId: [
      {
        required: true,
        message: "角色不能为空",
        type:"number",
        trigger: "change"
      }
    ]
    
  }
};
export default user_manage_form_config;
