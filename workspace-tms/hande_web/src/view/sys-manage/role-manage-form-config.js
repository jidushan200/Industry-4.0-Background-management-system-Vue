let role_manage_form_config = {
  formValidate: {},
  ruleValidate: {
    roleName: [
      {
        required: true,
        message: "角色名称不能为空",
        trigger: "blur"
      }
    ],
    remark: [
      {
        required: true,
        message: "角色描述不能为空",
        trigger: "blur"
      }
    ]
  }
};
export default role_manage_form_config;
