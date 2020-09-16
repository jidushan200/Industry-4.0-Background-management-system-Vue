let tool_coat_form_config = {
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
    staffCode: [
      {
        required: true,
        message: "工人职工号不能为空",      
        trigger: "blur"
      }
    ],
    equipmentId: [
      {
        required: true,
        message: "设备不能为空",
        type: "number",
        trigger: "change"
      }
    ]
  }
}
export default tool_coat_form_config;
