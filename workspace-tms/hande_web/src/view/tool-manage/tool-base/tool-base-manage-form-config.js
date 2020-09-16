let department_manage_form_config = {
  formValidate: {},
  ruleValidate: {
    toolNumber: [
      {
        required: true,
        message: "物料编码不能为空",
        trigger: "blur"
      }
    ],
    toolName: [
      {
        required: true,
        message: "物料名称不能为空",
        trigger: "blur"
      }
    ],
    toolMap: [
      {
        required: true,
        message: "物料图号不能为空",
        trigger: "blur"
      }
    ],
    price: [
      {
        required: true,
        pattern: /^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/,
        message: "请输入价格",
        trigger: "change"
      }
    ],
    typeId: [
      {
        required: true,
        message: "物料类型不能为空",
        type: "number",
        trigger: "change"
      }
    ]
  }
};
export default department_manage_form_config;
