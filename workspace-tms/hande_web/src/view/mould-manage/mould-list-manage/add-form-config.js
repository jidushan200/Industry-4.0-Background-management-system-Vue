let add_form_config = {
  formValidate: {},
  ruleValidate: {
    mouldNumber: [{
      required: true,
      message: "模具编码不能为空",
      trigger: "blur"
    }],
    mouldName: [{
      required: true,
      message: "模具名称不能为空",
      trigger: "blur"
    }],
    mouldMap: [{
      required: true,
      message: "模具图号不能为空",
      trigger: "blur"
    }],
    embryoId: [
      {
        required: true,
        message: "模具坯不能为空",
        type: "number",
        trigger: "change"
      }
    ]
  }
};
export default add_form_config;
