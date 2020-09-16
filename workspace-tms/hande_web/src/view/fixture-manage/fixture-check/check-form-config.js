let check_form_config = {
  formValidate: {},
  ruleValidate: {
    checkTime: [
      { required: true, message: '日期不能为空!', trigger: 'blur', pattern: /.+/ }],

    checkResult: [
      { required: true, message: '质检结果不能空!', trigger: 'change', type: "number" }],

  }
};
export default check_form_config;
