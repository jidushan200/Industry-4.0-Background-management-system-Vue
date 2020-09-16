let purchase_delivery_config = {
  formValidate: {},
  ruleValidate: {
    fixtureNumber: [
      {
        required: true,
        message: "请输入正确的夹具编码",
        pattern: /^[A-Z][0-9]{11}$/,
        trigger: "blur"
      }
    ]
  }
};
export default purchase_delivery_config;
