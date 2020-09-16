let tool_coat_manage_form_config = {
  formValidate: {},
  ruleValidate: {
    supplierId: [
      {
        required: true,
        message: "供应商不能为空",
        type: "number",
        trigger: "change"
      }
    ],
    deliever: [
      {
        required: true,
        message: "送货人不能为空",
        trigger: "blur"
      }
    ]
  }
};
export default tool_coat_manage_form_config;
