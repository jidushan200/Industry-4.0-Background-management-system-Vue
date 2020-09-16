let tool_unusual_manage_form_config = {
  formValidate: {},
  ruleValidate: {
    toolName: [
      {
        required: true,
        message: "刀具名称不能为空",
        trigger: "blur"
      }
    ],
    toolNumber: [
      {
        required: true,
        message: "物料编码不能为空",
        trigger: "blur"
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
    equipmentId: [
      {
        required: true,
        message: "设备不能为空",
        type: "number",
        trigger: "change"
      }
    ],
    measures: [
      {
        required: true,
        message: "处理措施不能为空",
        type: "number",
        trigger: "change"
      }
    ]
  }
};
export default tool_unusual_manage_form_config;
