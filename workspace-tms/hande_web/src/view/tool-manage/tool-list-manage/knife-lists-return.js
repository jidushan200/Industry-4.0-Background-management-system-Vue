let knife_lists_return_form_config = {
  formValidate: {},
  ruleValidate: {
    fullNumber: [{
      required: true,
      message: "刀具码不能为空",
      trigger: "blur"
    }],
    toolName: [{
      required: true,
      message: "刀具名称不能为空",
      trigger: "blur"
    }],
    inType: [{
      required: true,
      message: "返库类型不能为空",
      type: "number",
      trigger: "change"
    }],
    keeperId: [{
      required: true,
      message: "库管员不能为空",
      type: "number",
      trigger: "change"
    }],
    warehouse: [{
      required: true,
      message: "库位不能为空",
      trigger: "blur"
    }]
  }
};
export default knife_lists_return_form_config;
