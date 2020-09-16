let measure_manage_form_config = {
  formValidate: {},
  ruleValidate: {
    measureNumber: [
      {
        required: true,
        pattern: /^[A-Z][0-9]{11}$/,
        message: "输入正确量具编码格式",
        trigger: "blur"
      }
    ],
    measureName: [
      {
        required: true,
        message: "量具名称不能为空",
        trigger: "blur"
      }
    ],
    model: [
      {
        required: true,
        message: "规格型号不能为空",
        trigger: "blur"
      }
    ]
  }
};
export default measure_manage_form_config;
