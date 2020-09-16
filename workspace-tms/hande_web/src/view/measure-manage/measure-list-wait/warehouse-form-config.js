let warehouse_form_config = {
  formValidate: {},
  ruleValidate: {
    warehouse: [
      {
        required: true,
        message: "库位不能为空",
        trigger: "blur"
      }
    ]
  }
};
export default warehouse_form_config;
