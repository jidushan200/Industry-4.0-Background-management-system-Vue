let check_quality_form_config = {
  formValidate: {},
  ruleValidate: {
    supplierId: [{
      required: true,
      type: "number",
      message: '供应商不能为空',
      trigger: 'change'
    }],
    selfCheckReport: [{
      required: true,
      message: '自检报告不能为空!',
      trigger: 'blur',
      type: "number"
    }],
  }
};
export default check_quality_form_config;
