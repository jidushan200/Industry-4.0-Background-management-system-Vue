let supplier_manage_form_config = {
  formValidate: {},
  ruleValidate: {
    supplierName: [
      {
        required: true,
        message: "供应商名称不能为空",
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
export default supplier_manage_form_config;
