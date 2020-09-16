let used_tool_check_form_config = {
  formValidate: {},
  ruleValidate: {
    executor: [
      {
        required: true,
        message: "刃磨人不能为空",
        trigger: "blur"
      }
    ]
  }
};
export default used_tool_check_form_config;
