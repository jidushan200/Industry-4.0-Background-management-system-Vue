let tool_part_form_config = {
    formValidate: {},
    ruleValidate: {
      toolNumber: [
        {
          required: true,
          message: "刀具编码不能为空",
          trigger: "blur"
        }
      ],
      toolMap: [
        {
          required: true,
          message: "刀具图号不能为空",
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
  export default tool_part_form_config;
  