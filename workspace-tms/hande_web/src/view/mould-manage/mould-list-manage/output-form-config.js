let output_form_config = {
  formValidate: {},
  ruleValidate: {
    keeperId: [{
      required: true,
      message: "库管员不能为空",
      type: "number",
      trigger: "change"
    }],
    outType: [{
      required: true,
      message: "出库类型不能为空",
      type: "number",
      trigger: "change"
    }],
    staffCode: [{
      required: true,
      message: "领用人不能为空",
      trigger: "blur"
    }]
  }
};
export default output_form_config;
