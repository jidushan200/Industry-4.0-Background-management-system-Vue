let tool_check_form_config = {
  formValidate: {},
  ruleValidate: {
    checkTime: [
      { required: true, message: '日期不能为空!', trigger: 'blur', pattern: /.+/ }],

    checkResult: [
      { required: true, message: '质检结果不能空!', trigger: 'blur', type: "number" }],

  }
};
export default tool_check_form_config;
