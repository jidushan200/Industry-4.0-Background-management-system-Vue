let staff_manage_form_config = {
  formValidate: {},
  ruleValidate: {
    staffName: [
      {
        required: true,
        message: "人员姓名不能为空",
        trigger: "blur"
      }
    ],
    staffCode: [
      {
        required: true,
        message: "员工编码不能为空",
        trigger: "blur"
      }
    ],
    position: [
      {
        required: true,
        message: "员工职务不能为空",
        trigger: "blur"
      }
    ],
    gender: [
      {
        required: true,
        message: "性别不能为空",
        type: "number",
        trigger: "change"
      }
    ],
    staffStatus: [
      {
        required: true,
        message: "员工状态不能为空",
        type: "number",
        trigger: "change"
      }
    ],
    departmentId: [
      {
        required: true,
        message: "所属部门不能为空",
        type: "number",
        trigger: "change"
      }
    ]
  }
};
export default staff_manage_form_config;
