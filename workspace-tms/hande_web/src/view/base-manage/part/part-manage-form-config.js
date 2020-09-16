let part_manage_form_config = {
  formValidate: {},
  ruleValidate: {
    partName: [
      {
        required: true,
        message: "制件名称不能为空",
        trigger: "blur"
      }
    ],
    partCode: [
      {
        required: true,
        message: "制件编码不能为空",
        trigger: "blur"
      }
    ],
    partType: [
      {
        required: true,
        message: "制件类型不能为空",
        type: "number",
        trigger: "change"
      }
    ]
  }
};
export default part_manage_form_config;
