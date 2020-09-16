let check_cycle_form_config = {
  formValidate: {},
  ruleValidate: {
    checkCycle: [{
      required: true,
      message: "定检周期不能为空",
      type: "number",
      trigger: "blur"
    }],
    factoryMetrology: [{
      required: true,
      message: "本厂计量编号不能为空",
      trigger: "blur"
    }],
  }
};
export default check_cycle_form_config;
