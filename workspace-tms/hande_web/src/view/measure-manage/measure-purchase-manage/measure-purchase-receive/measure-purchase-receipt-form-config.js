let receipt_form_config = {
  formValidate: {},
  ruleValidate: {
    arrivalQty: [
      {
        required: true,
        message: "申购数量不能为空",
        type: "number",
        trigger: "blur"
      }
    ],
  }
};
export default receipt_form_config;
