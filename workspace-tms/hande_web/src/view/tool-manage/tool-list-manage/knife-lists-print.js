let knife_lists_print_form_config = {
  formValidate: {},
  ruleValidate: {
    printAmount: [{
      required: true,
      message: "打印数量不能为空",
      type: "number",
      trigger: "change"
    }]
  }
};
export default knife_lists_print_form_config;
