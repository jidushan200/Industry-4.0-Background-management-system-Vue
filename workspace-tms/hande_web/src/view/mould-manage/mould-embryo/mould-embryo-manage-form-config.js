let mould_embryo_manage_form_config = {
  formValidate: {},
  ruleValidate: {
    embryoName: [{
      required: true,
      message: "模具胚名称不能为空",
      trigger: "blur"
    }],
    embryoCode: [{
      required: true,
      message: "模具胚编码不能为空",
      trigger: "blur"
    }]
  }
};
export default mould_embryo_manage_form_config;
