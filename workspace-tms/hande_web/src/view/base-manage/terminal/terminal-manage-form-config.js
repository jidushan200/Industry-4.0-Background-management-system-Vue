let terminal_manage_form_config = {
  formValidate: {},
  ruleValidate: {
    terminalCode: [
      {
        required: true,
        message: "终端编码不能为空",
        trigger: "blur"
      }
    ],
    ip: [
      {
        required: true,
        message: "IP地址不能为空",
        trigger: "blur"
      }
    ],
    managerId: [
      {
        required: true,
        message: "负责人不能为空",
        type: "number",
        trigger: "change"
      }
    ]
  }
};
export default terminal_manage_form_config;
