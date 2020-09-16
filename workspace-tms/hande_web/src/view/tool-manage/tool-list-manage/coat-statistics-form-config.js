let coat_statistics_form_config = {
  formValidate: {},
  ruleValidate: {
    settlementPrice: [{
      required: true,
      message: "结算价格不能为空",
      trigger: "blur",
      type:"number"
    }],
    settlementQty: [{
      required: true,
      message: "结算数量不能为空",
      trigger: "blur",
      type:"number"
    }]
  }
};
export default coat_statistics_form_config;
