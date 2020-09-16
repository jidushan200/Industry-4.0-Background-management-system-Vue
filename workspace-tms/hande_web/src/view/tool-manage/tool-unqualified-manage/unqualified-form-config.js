let unqualified_form_config = {
  formValidate: {},
  ruleValidate: {

    fullNumber: [
      {
        required: true,
        message: "物料条码不能为空",
        trigger: "blur"
      }
    ],
    toolNumber: [{
      required: true,
      message: "物料编码不能为空",
      trigger: "blur"
    }]

  }
};
export default unqualified_form_config;
