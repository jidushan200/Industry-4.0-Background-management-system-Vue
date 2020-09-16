let site_form_config = {
  formValidate: {},
  ruleValidate: {
    maintainItem: [{ required: true, type: 'array', min: 1, message: '保养项目不能为空', trigger: 'change' }],
    useStatus: [{ required: true, message: '使用状态不能空!', type: "number", trigger: "change" }],
  }
};
export default site_form_config;
