let team_manage_form_config = {
  formValidate: {},
  ruleValidate: {
    departmentId: [
      {
        required: true,
        message: "部门名称不能为空",
        type: "number",
        trigger: "blur"
      }
    ],
    teamName: [
      {
        required: true,
        message: "班组名称不能为空",
        trigger: "blur"
      }
    ],
  }
};
export default team_manage_form_config;
