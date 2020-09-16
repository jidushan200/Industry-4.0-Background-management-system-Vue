let mould_manage_form_config = {
  formValidate: {},
  ruleValidate: {
    mouldNumber: [
      {
        required: true,
        message: "模具编码不能为空",
        trigger: "blur"
      }
    ],
    mouldName: [
      {
        required: true,
        message: "模具名称不能为空",
        trigger: "blur"
      }
    ],
    mouldMap: [
      {
        required: true,
        message: "模具图号不能为空",
        trigger: "blur"
      }
    ],  
    mouldType: [
      {
        required: true,
        message: "模具类型不能为空",
        type: "number",
        trigger: "change"
      }
    ]
  }
};
export default mould_manage_form_config;
