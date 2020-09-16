let mould_repair_form_config = {
  formValidate: {},
  ruleValidate: {
    executorId: [{
      required: true
    }],
    repairMeasure: [{
      required: true
    }],
    remark: [{
      required: true,
      message: "备注说明不能为空",
      trigger: "blur"
    }]
  }
};
export default mould_repair_form_config;
