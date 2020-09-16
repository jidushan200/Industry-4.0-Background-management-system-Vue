let coat_check_form_config = {
  formValidate: {},
  ruleValidate: {
    fullNumber: [
      {
        required: true,
        message: "物料条码不能为空",
        trigger: "blur"
      }
    ],  
    partType: [
      { required: true, type: "number", message: '质检类型不能为空', trigger: 'change' }
    ],
    supplierId: [
      { required: true, type: "number", message: '供应商不能为空', trigger: 'change' }
    ],
    checkTime: [
      { required: true, message: '日期不能为空!', trigger: 'blur', pattern: /.+/ }],
  }
};
export default coat_check_form_config;
