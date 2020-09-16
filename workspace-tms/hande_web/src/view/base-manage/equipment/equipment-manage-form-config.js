let equipment_manage_form_config = {
  formValidate: {},
  ruleValidate: {
    equipmentName: [
      {
        required: true,
        message: "设备名称不能为空",
        trigger: "blur"
      }
    ],
    equipmentCode: [
      {
        required: true,
        message: "设备编码不能为空",
        trigger: "blur"
      }
    ]
  }
};
export default equipment_manage_form_config;
