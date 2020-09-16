let tool_check_form_config = {
  formValidate: {},
  ruleValidate: {
    fullNumber: [
      {
        required: true,
        message: "物料条码不能为空",
        trigger: "blur"
      }
    ],
    checkType: [
      {
        required: true,
        type: "number",
        message: '质检类型不能为空',
        trigger: 'change'
      }
    ],
    checkResult: [
      { required: true, type: "number", message: '质检结果不能为空', trigger: 'change' }
    ],
    checkTime: [
      { required: true, message: '日期不能为空!', trigger: 'blur', pattern: /.+/ }],
  }
};
export default tool_check_form_config;
