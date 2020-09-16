let tool_repair_form_config = {
  formValidate: {},
  ruleValidate: {
    executorId: [
      {
        required: true
      }
    ],
    processTotal: [
      {
        required: true,
        message: "加工数量不能为空",
        type: "number",
        trigger: "change"
      }
    ],
    partId: [
      {
        required: true,
        message: "制件不能为空",
        type: "number",
        trigger: "change"
      }
    ],
    repairMeasure: [
      {
        required: true,
        message: "刃磨量不能为空",
        type: "number",
        trigger: "blur"
      }
    ]
  }
};
export default tool_repair_form_config;
