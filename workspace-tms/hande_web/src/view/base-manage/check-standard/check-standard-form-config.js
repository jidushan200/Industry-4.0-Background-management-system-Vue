let check_standard_form_config = {
    formValidate: {},
    ruleValidate: {
        materialType: [
            {
                required: true,
                message: "物料类型不能为空",
                type:"number",
                trigger: "change"
            }
        ],
        materialNumber: [
            {
                required: true,
                message: "物料编码不能为空",
                trigger: "blur"
            }
        ],
        checkType: [
            {
                required: true,
                message: "检验类型不能为空",
                type:"number",
                trigger: "change"
            }
        ]
    }
};
export default check_standard_form_config;
