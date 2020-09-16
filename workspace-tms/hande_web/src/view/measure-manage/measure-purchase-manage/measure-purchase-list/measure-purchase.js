let measure_purchase_manage_form_config = {
  formValidate: {},
  ruleValidate: {
    measureNumber: [{
      required: true,
      message: "请输入正确的物料编码",
      pattern: /^[A-Z][0-9A-Z]{11}$/
    }],
    model: [{
      required: true,
      message: "型号规格不能为空",
      trigger: "change"
    }],
    applyQty: [{
      required: true,
      message: "申购数量不能为空",
      type: "number",
      trigger: "blur"
    }],
    demandTime: [{
      required: true,
      message: "需求时间不能为空",
      pattern: /.+/,
      trigger: "change"
    }]
  }
};
export default measure_purchase_manage_form_config;
