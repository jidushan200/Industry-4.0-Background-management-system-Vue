let department_manage_form_config = {
  formValidate: {},
  ruleValidate: {
    departmentName: [
      {
        required: true,
        message: "部门名称不能为空",
        trigger: "blur"
      }
    ],
    departmentCode: [
      {
        required: true,
        message: "部门编码不能为空",
        trigger: "blur"
      }
    ]
  }
};
export default department_manage_form_config;
