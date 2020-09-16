let tool_repair_form_config = {
  formValidate: {},
  ruleValidate: {
    processTotal: [
      {
        required: true,
        message: "加工数量不能为空",
        type: "number",
        trigger: "change"
      }
    ],
    beginTime: [
      {
        required: true,
        message: "开始时间不能为空"
      }
    ],
    endTime: [
      {
        required: true,
        message: "结束时间不能为空"
      }
    ],
    partId: [
      {
        required: true,
        message: "生产制件不能为空",
        type: "number",
        trigger: "change"
      }
    ],
    returnResion: [
      {
        required: true,
        message: "交回原因不能为空",
        type: "number",
        trigger: "change"
      }
    ]
  }
};
export default tool_repair_form_config;
