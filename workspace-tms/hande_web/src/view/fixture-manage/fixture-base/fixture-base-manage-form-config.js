let fixture_manage_form_config = {
  formValidate: {},
  ruleValidate: {
    fixtureNumber: [
      {
        required: true,
        pattern: /^[A-Z][0-9]{11}$/,
        message: "输入正确夹具编码格式",
        trigger: "blur"
      }
    ],
    fixtureName: [
      {
        required: true,
        message: "夹具名称不能为空",
        trigger: "blur"
      }
    ],
    fixtureMap: [
      {
        required: true,
        message: "图号不能为空",
        trigger: "blur"
      }
    ],
    fixtureType: [
      {
        required: true,
        message: "夹具类型不能为空",
        trigger: "change",
        type: "number"
      }
    ]
  }
};
export default fixture_manage_form_config;
