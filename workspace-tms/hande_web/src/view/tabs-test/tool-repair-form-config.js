let tool_repair_form_config = {
  formValidate: {},
  ruleValidate: {
    executor: [
      {
        required: true,
        message: "刃磨人不能为空",
        trigger: "blur"
      }
    ],
    repairMeasure: [
      {
        required: true,
        message: "刃磨量不能为空",
        type: "Number",
        trigger: "blur"
      }
    ]
  }
};
export default tool_repair_form_config;
