let warehouse_form_config = {
  formValidate: {},
  ruleValidate: {
    storeHouse: [
      { required: true, message: '库位不能空!', trigger: 'blur' }],
    returnReason: [{ required: true, message: '退货原因不能空!', trigger: 'blur' }],
    equipmentId: [{ required: true, message: '使用机床不能为空!', trigger: 'blur',type: "number"}],
    staffCode: [{ required: true, message: '领用人不能空!', trigger: 'blur' }],
  }
};
export default warehouse_form_config;
