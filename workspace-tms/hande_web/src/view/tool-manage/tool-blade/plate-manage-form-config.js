let plate_manage_form_config = {
  formValidate: {},
  ruleValidate: {
	 plateNumber: [
      {
        required: true,
        message: "刀盘编码不能为空",
        trigger: "blur"
      }
    ],
    plateName: [
      {
    	  required: true,
          message: "刀盘名称不能为空",
          trigger: "blur"            
      }
    ],
    useStatus: [
      {
        required: true,
        message: "使用状态不能为空",
        type: "number",
        trigger: "change"
      }
    ]
  }
};
export default plate_manage_form_config;
