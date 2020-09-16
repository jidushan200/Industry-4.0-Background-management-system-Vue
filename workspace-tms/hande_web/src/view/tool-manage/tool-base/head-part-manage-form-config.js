let head_part_manage_form_config = {
  formValidate: {},
  ruleValidate: {
    headNumber: [{
      required: true,
      message: "刀条组合编码不能为空",
      trigger: "blur"
    }],
    headName: [{
      required: true,
      message: "刀条组合名称不能为空",
      trigger: "blur"
    }],
    headType: [{
      required: true,
      message: "刀条组合类型不能为空",
      trigger: "blur",
      type: "number"
    }],
    partCode: [{
      required: true,
      message: "制件编码不能为空",
      trigger: "blur"
    }],
    partName: [{
      required: true,
      message: "制件名称不能为空",
      trigger: "blur"
    }]
  }
}
export default head_part_manage_form_config;
