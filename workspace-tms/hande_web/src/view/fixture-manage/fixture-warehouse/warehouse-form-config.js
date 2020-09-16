let warehouse_form_config = {
  formValidate: {},
  ruleValidate: {
    storeHouse: [
      { required: true, message: '库位不能空!', trigger: 'blur' }],
    departmentId: [
      { required: true, message: '部门不能空!', trigger: 'change', type: 'number' }]
  }
}
export default warehouse_form_config;
