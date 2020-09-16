let repair_form_config = {
  formValidate: {},
  ruleValidate: {
	  repairMeasure: [
      { required: true, message: '修磨量不能空!', trigger: 'blur' ,type:'number'}],
  }
};
export default repair_form_config;
