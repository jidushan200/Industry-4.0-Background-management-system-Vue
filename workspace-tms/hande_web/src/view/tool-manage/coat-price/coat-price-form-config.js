let coat_price_form_config = {
    formValidate: {},
    ruleValidate: {
      toolNumber: [
        {
          required: true,
          message: "刀具编码不能为空",
          trigger: "blur"
        }
      ],  
      supplierCode: [
        {
          required: true,
          message: "供应商编码不能为空",
          trigger: "blur"
        }
      ],
      price: [
        {required: true, pattern: /^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/, message: '请输入价格', trigger: 'change'}
      ]  
    }
  };
  export default coat_price_form_config;
  