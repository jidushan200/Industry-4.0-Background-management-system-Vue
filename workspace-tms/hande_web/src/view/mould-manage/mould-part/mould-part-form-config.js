let mould_part_form_config = {
  formValidate: {},
  ruleValidate: {
    mouldNumber: [{
      required: true,
      message: "刀具编码不能为空",
      trigger: "blur"
    }],
    partCode: [{
      required: true,
      message: "制件编码不能为空",
      trigger: "blur"
    }],
  }
};
export default mould_part_form_config;
