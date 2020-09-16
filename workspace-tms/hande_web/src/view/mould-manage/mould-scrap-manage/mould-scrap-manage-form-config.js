let tool_scrap_manage_form_config = {
  formValidate: {},
  ruleValidate: {
    fullNumber: [{
      required: true,
      message: "模具条码不能为空",
      trigger: "blur"
    }],
    mouldMap: [{
      required: true,
      message: "模具图号不能为空",
      trigger: "blur"
    }],
    mouldName: [{
      required: true,
      message: "模具名称不能为空",
      trigger: "blur"
    }],
    scripResion: [{
      required: true,
      message: "模具条码不能为空",
      trigger: "change",
      type: "number"
    }]
  }
};
export default tool_scrap_manage_form_config;
