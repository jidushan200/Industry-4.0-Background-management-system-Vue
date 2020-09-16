let knife_lists_out_form_config = {
  formValidate: {},
  ruleValidate: {

    outType: [
      {
        required: true,
        message: "出库类型不能为空",
        type: "number",
        trigger: "change"
      }
    ],
   
    receiverId: [
      {
        required: true,
        message: "领用人不能为空",
        type: "number",
        trigger: "change"
      }
    ]
  }
};
export default knife_lists_out_form_config;
