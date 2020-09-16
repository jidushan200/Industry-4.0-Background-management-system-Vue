let repair_check_form_config = {
  formValidate: {},
  ruleValidate: {
    fullNumber: [
      {
        required: true,
        message: "物料条码不能为空",
        trigger: "blur"
      }
    ],  
    checkResult: [
      { required: true, type: "number", message: '质检结果不能为空', trigger: 'change' }
    ],
    checkTime: [
      { required: true, message: '日期不能为空!', trigger: 'blur', pattern: /.+/ }],
  }
};
export default repair_check_form_config;
