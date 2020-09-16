let tool_form_config = {
  formValidate: {},
  ruleValidate: {
    warehouse: [
      {
        required: true,
        message: "库位不能为空",
        trigger: "blur"
      }
    ],
    processPart: [
      {
        required: true,
        message: "生产制件不能为空",
        trigger: "change"
      }
    ],
    inType: [
      {
        required: true,
        message: "返库类型不能为空",
        type: "number",
        trigger: "change"
      }
    ],
    outType: [
      {
        required: true,
        message: "出库类型不能为空",
        type: "number",
        trigger: "change"
      }
    ],
    staffCode: [
      {
        required: true,
        message: "领用人不能为空",
        trigger: "blur"
      }
    ], 
    coatSupplierId: [
      {
        required: true,
        message: "供应商不能为空",
        type: "number",
        trigger: "change"
      }
    ],
  }
};
export default tool_form_config;
