let apply_form_config = {
  formValidate: {},
  ruleValidate: {
    toolNumber: [
      {
        required: true,
        message: "请输入正确的物料编码",
        pattern: /^[A-Z][0-9]{11}$/,
        trigger: "blur"
      }
    ],
    purchaseType: [
      {
        required: true,
        message: "申购类型不能为空",
        type: "number",
        trigger: "change"
      }
    ],
    purchaseResion: [
      {
        required: true,
        message: "申购原因不能为空",
        type: "number",
        trigger: "change"
      }
    ],
    needQty: [
      {
        required: true,
        message: "申购数量不能为空",
        type: "number",
        trigger: "blur"
      }
    ],
    departmentId: [
      {
        required: true,
        message: "申请部门不能为空",
        type: "number",
        trigger: "change"
      }
    ],
    applierId: [
      {
        required: true,
        message: "申请人不能为空",
        type: "number",
        trigger: "blur"
      }
    ],
    applyTime: [
      {
        required: true,
        message: "需求时间不能为空",
        pattern: /.+/,
        trigger: "change"
      }
    ]
  }
};
export default apply_form_config;
