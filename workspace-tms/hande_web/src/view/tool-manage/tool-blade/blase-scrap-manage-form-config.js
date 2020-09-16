let blade_scrap_manage_form_config = {
    formValidate: {},
    ruleValidate: {
        composeNumber: [
            {
                required: true,
                message: "刀盘组合条码不能为空",
                trigger: "blur"
            }
        ],
        scrapResion: [
            {
                required: true,
                message: "报废原因不能为空",
                type: "number",
                trigger: "blur"
            }
        ]
    }
}
export default blade_scrap_manage_form_config;
