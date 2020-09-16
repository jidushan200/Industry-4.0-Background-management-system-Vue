let apply_form_config = {
  formValidate: {},
  ruleValidate: {
    fixtureNumber: [
      {
        required: true,
        message: "请输入正确的夹具编码",
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
    partId: [
      {
        required: true,
        message: "制件不能为空",
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
    purchaseQty: [
      {
        required: true,
        message: "申购数量不能为空",
        type: "number",
        trigger: "blur"
      }
    ],
    demandTime: [
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
