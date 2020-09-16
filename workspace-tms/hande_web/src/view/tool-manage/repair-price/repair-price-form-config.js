let repair_price_form_config = {
  formValidate: {},
  ruleValidate: {
    toolNumber: [
      {
        required: true,
        message: "刀具编码不能为空",
        trigger: "blur"
      }
    ],
    supplierCode: [
      {
        required: true,
        message: "供应商编码不能为空",
        trigger: "blur"
      }
    ]
  }
};
export default repair_price_form_config;
