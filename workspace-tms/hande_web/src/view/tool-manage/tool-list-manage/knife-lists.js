let knife_lists_form_config = {
  formValidate: {},
  ruleValidate: {
    toolName: [
      {
        required: true,
        message: "刀具名称不能为空",
        trigger: "blur"
      }
    ],
    fullNumber: [
      {
        required: true,
        message: "刀具码不能为空",
        trigger: "blur"
      }
    ],
    keeperId: [
      {
        required: true,
        message: "库管员不能为空",
        type: "number",
        trigger: "change"
      }
    ],
    deliever: [
      {
        required: true,
        message: "送货人不能为空",
        trigger: "blur"
      }
    ],
    warehouse: [
      {
        required: true,
        message: "库位不能为空",
        trigger: "blur"
      }
    ],
   
    toolType: [
      {
        required: true,
        message: "刀具类型不能为空",
        type: "number",
        trigger: "change"
      }
    ],
  
   
  }
};
export default knife_lists_form_config;
