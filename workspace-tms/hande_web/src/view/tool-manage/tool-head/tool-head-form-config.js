let tool_head_form_config = {
  formValidate: {},
  ruleValidate: {
    toolNumber: [
      {
        required: true,
        message: "物料编码不能为空",
        trigger: "blur"
      }
    ],
    staffCode:[{
      required: true,
      message: "领用人不能为空",
      trigger: "blur"
    }],
    warehouseSeq: [
      {
        required: true,
        message: "顺序号不能为空",
        trigger: "blur"
      }
    ]
  }
};
export default tool_head_form_config;
