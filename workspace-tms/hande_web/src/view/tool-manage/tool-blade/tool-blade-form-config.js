let tool_blade_form_config = {
  formValidate: {},
  ruleValidate: {
    toolNumber: [{
      required: true,
      message: "刀条编码不能为空",
      trigger: "blur"
    }],
    composeNumber: [
      {
        required: true,
        message: "刀条组合条码不能为空",
        trigger: "blur"
      }
    ],
    headNumber: [{
      required: true,
      message: "刀条组合编码不能为空",    
      trigger: 'change'
    }],

    composeNumber: [{
      required: true,
      message: "刀条组合条码不能为空",
      trigger: "blur"
    }],
    plateNumber: [{
      required: true,
      message: "刀盘编码不能为空",
      trigger: "blur"
    }],
    staffCode: [{
      required: true,
      message: "员工编码不能为空",
      trigger: "blur"
    }],
    useQty: [{
      required: true,
      message: "出库数量不能为空",
      type: "number",
      trigger: "blur"
    }],
    outType: [{
      required: true,
      message: "出库类型不能为空",
      type: "number",
      trigger: 'change'
    }],
    toolQty: [{
      required: true,
      message: "刀具数量不能为空",
      type: "number",
      trigger: "blur"
    }],
    supplierId: [{
      required: true,
      message: "供应商不能为空",
      type: "number",
      trigger: "change"
    }],
    checkResult: [{
      required: true,
      type: "number",
      message: '质检结果不能为空',
      trigger: 'change'
    }]
  }
}
export default tool_blade_form_config;
