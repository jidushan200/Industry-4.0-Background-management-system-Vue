let check_form_config = {
  formValidate: {},
  ruleValidate: {
    remark: [{
      required: true,
      message: "请输入不合格原因",
      trigger: "blur"
    }],
    quality: [{
      required: true,
      message: "请选择质检结果",
      trigger: "change",
      type:"number"
    }]
  }
};
export default check_form_config;
