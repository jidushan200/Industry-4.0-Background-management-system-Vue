let staff_department_manage_form_config = {
  formValidate: {},
  ruleValidate: {
    departmentName: [
      {
        required: true,
        message: "部门名称不能为空",
        trigger: "blur"
      }
    ]
  }
}
export default staff_department_manage_form_config;
