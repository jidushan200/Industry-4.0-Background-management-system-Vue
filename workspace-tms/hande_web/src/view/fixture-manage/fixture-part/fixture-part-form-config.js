let fixture_part_form_config = {
    formValidate: {},
    ruleValidate: {
      fixtureNumber: [
        {
          required: true,
          message: "夹具编码不能为空",
          trigger: "blur"
        }
      ],
     
      partCode: [
        {
          required: true,
          message: "制件编码不能为空",
          trigger: "blur"
        }
      ],
    }
  };
  export default fixture_part_form_config;
  