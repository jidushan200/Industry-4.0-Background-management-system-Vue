let head_blade_manage_form_config = {
  formValidate: {},
  ruleValidate: {
    headNumber: [
      {
        required: true,
        message: "刀盘编码不能为空",
        trigger: "blur"
      }
    ],
    toolNumber: [
      {
        required: true,
        message: "刀条编码不能为空",
        trigger: "blur"
      }
    ],
    toolName: [
      {
        required: true,
        message: "刀条名称不能为空",
        trigger: "blur"
      }
    ],
    toolQty: [
      {
        required: true,
        message: "请输入刀条数量",
        type: "number",
        trigger: "change"
      }
    ],
    eachProcessQty: [
      {
        required: true,
        message: "请输入每次换刀加工数",
        type: "number",
        trigger: "change"
      }
    ],
    processTimes: [
      {
        required: true,
        message: "请输入加工次数",
        type: "number",
        trigger: "change"
      }
    ],
    grindingMax: [
      {
        required: true,
        message: "请输入最大刃磨量",
        type: "number",
        trigger: "change"
      }
    ],
    grindingCordon: [
      {
        required: true,
        message: "请输入刃磨警戒线",
        type: "number",
        trigger: "change"
      }
    ]
  }
}
export default head_blade_manage_form_config;
