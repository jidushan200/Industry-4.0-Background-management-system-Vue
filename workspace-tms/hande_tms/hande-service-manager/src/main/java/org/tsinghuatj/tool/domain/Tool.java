package org.tsinghuatj.tool.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.tsinghuatj.framework.utils.excel.annotation.ExcelField;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ApiModel
public class Tool implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * @Fields pk_id : 主键
	 */
	@ApiModelProperty(name = "pkId", notes = "主键")
	private Long pkId;
	/**
	 * @Fields create_user : 创建用户
	 */
	@ApiModelProperty(name = "createUser", notes = "创建用户")
	private Long createUser;
	/**
	 * @Fields create_time : 创建时间
	 */
	@ApiModelProperty(name = "createTime", notes = "创建时间")
	private Date createTime;
	/**
	 * @Fields update_user : 更新用户
	 */
	@ApiModelProperty(name = "updateUser", notes = "更新用户")
	private Long updateUser;

	/**
	 * @Fields update_user : 更新用户
	 */
	@ApiModelProperty(name = "updateUserName", notes = "更新用户")
	private String updateUserName;

	/**
	 * @Fields update_time : 更新时间
	 */
	@ApiModelProperty(name = "updateTime", notes = "更新时间")
	private Date updateTime;
	/**
	 * @Fields del_mark : 删除标识(--0正常--1已删除)
	 */
	@ApiModelProperty(name = "delMark", notes = "删除标识(--0正常--1已删除)")
	private Integer delMark;
	/**
	 * @Fields full_number : 完整编码
	 */
	@ExcelField(title = "物料条码", order = 4)
	@ApiModelProperty(name = "fullNumber", notes = "完整编码")
	private String fullNumber;
	/**
	 * @Fields tool_number : 刀具编码
	 */
	@ExcelField(title = "物料编码", order = 1)
	@ApiModelProperty(name = "toolNumber", notes = "刀具编码")
	private String toolNumber;
	
	/**
	 * @Fields number : 刀具编码
	 */
	@ApiModelProperty(name = "number", notes = "刀具编码")
	private String number;
	/**
	 * @Fields tool_name : 刀具名称
	 */
	@ExcelField(title = "物料名称", order = 2)
	@ApiModelProperty(name = "toolName", notes = "刀具名称")
	private String toolName;
	/**
	 * @Fields tool_map : 刀具图号
	 */
	@ExcelField(title = "物料图号", order = 3)
	@ApiModelProperty(name = "toolMap", notes = "刀具图号")
	private String toolMap;
	
	/**
	 * @Fields tool_type : 刀具类型
	 */
	@ApiModelProperty(name = "typeId", notes = "刀具类型")
	private Integer typeId;
	/**
	 * @Fields tool_type : 刀具类型
	 */
	@ExcelField(title = "刀具类型", order = 7)
	@ApiModelProperty(name = "typeName", notes = "刀具类型")
	private String typeName;
	
	/**
	 * @Fields tool_state : 刀具状态(1-闲置；2-使用中；3-待刃磨；4-刃磨完成；5-待涂层；6-涂层完成；7-待质检；)
	 */
	@ApiModelProperty(name = "toolState", notes = "刀具状态(1-闲置；2-使用中；3-待刃磨；4-刃磨完成；5-待涂层；6-涂层完成；7-待质检；)")
	private Integer toolState;
	/**
	 * @Fields tool_state : 刀具状态(1-闲置；2-使用中；3-待刃磨；4-刃磨完成；5-待涂层；6-涂层完成；7-待质检；)
	 */
	@ExcelField(title = "刀具状态", order = 5)
	@ApiModelProperty(name = "toolStateName", notes = "刀具状态(1-闲置；2-使用中；3-待刃磨；4-刃磨完成；5-待涂层；6-涂层完成；7-待质检；)")
	private String toolStateName;
	/**
	 * @Fields process_part : 加工零件号
	 */
	@ApiModelProperty(name = "processPart", notes = "加工零件号")
	private String processPart;
	/**
	 * @Fields part_name : 制件名称
	 */
	@ApiModelProperty(name = "partCode", notes = "制件编码")
	private String partCode;
	/**
	 * @Fields part_name : 制件名称
	 */
	@ApiModelProperty(name = "partName", notes = "制件名称")
	private String partName;
	/**
	 * @Fields process_total : 理论加工数量
	 */
	@ApiModelProperty(name = "processTotal", notes = "理论加工数量")
	private Integer processTotal;
	/**
	 * @Fields process_each : 每次还刀加工数量
	 */
	@ApiModelProperty(name = "processEach", notes = "每次还刀加工数量")
	private Integer processEach;
	/**
	 * @Fields available_times : 可加工次数
	 */
	@ApiModelProperty(name = "availableTimes", notes = "可加工次数")
	private Integer availableTimes;
	/**
	 * @Fields grinding_max : 最大刃磨量
	 */
	@ApiModelProperty(name = "grindingMax", notes = "最大刃磨量")
	private BigDecimal grindingMax;
	/**
	 * @Fields coat_max : 最大涂层次数
	 */
	@ApiModelProperty(name = "coatMax", notes = "最大涂层次数")
	private Integer coatMax;
	/**
	 * @Fields supplier_id : 供应商ID
	 */
	@ApiModelProperty(name = "supplierId", notes = "供应商ID")
	private Long supplierId;
	/**
	 * @Fields supplier_name : 供应商名称
	 */
	@ExcelField(title = "供应商名称", order =8)
	@ApiModelProperty(name = "supplierName", notes = "供应商名称")
	private String supplierName;
	/**
	 * @Fields price : 价格
	 */
	@ApiModelProperty(name = "price", notes = "价格")
	private BigDecimal price;
	/**
	 * @Fields process_amount : 已完成加工数量
	 */
	@ApiModelProperty(name = "processAmount", notes = "已完成加工数量")
	private Integer processAmount;
	/**
	 * @Fields process_times : 已加工次数
	 */
	@ApiModelProperty(name = "processTimes", notes = "已加工次数")
	private Integer processTimes;
	/**
	 * @Fields repair_times : 已刃磨次数
	 */
	@ApiModelProperty(name = "repairTimes", notes = "已刃磨次数")
	private Integer repairTimes;
	/**
	 * @Fields repair_times : 已刃磨量
	 */
	@ApiModelProperty(name = "repairAmount", notes = "已刃磨量")
	private BigDecimal repairAmount;
	/**
	 * @Fields repair_times : 本次已刃磨量
	 */
	@ApiModelProperty(name = "repairAmountCur", notes = "本次已刃磨量")
	private BigDecimal repairAmountCur;
	/**
	 * @Fields coat_times : 刃磨人
	 */
	@ApiModelProperty(name = "repairor", notes = "刃磨人")
	private String repairor;
	/**
	 * @Fields coat_times : 刃磨时间
	 */
	@ApiModelProperty(name = "repairTime", notes = "刃磨时间")
	private Date repairTime;
	/**
	 * @Fields coat_times : 已涂层次数
	 */
	@ApiModelProperty(name = "coatTimes", notes = "已涂层次数")
	private Integer coatTimes;
	/**
	 * @Fields compromise_flag : 让步使用标识（0-未让步；1-让步使用）
	 */
	@ApiModelProperty(name = "compromiseFlag", notes = "让步使用标识（0-未让步；1-让步使用）")
	private Integer compromiseFlag;
	/**
	 * @Fields tool_seq : 刀具序号
	 */
	@ApiModelProperty(name = "toolSeq", notes = "刀具序号")
	private Integer toolSeq;
	/**
	 * @Fields warehouse_code : 入库编码
	 */
	@ApiModelProperty(name = "warehouseCode", notes = "入库编码")
	private String warehouseCode;
	/**
	 * @Fields warehouse : 库位
	 */
	@ExcelField(title = "库位", order =6)
	@ApiModelProperty(name = "warehouse", notes = "库位")
	private String warehouse;
	/**
	 * @Fields tool_seq : 顺序号
	 */
	@ApiModelProperty(name = "sequenceNumber", notes = "顺序号")
	private String sequenceNumber;
	/**
	 * @Fields department_id : departmentId
	 */
	@ApiModelProperty(name = "departmentId", notes = "departmentId")
	private Long departmentId;
	/**
	 * @Fields department_id : 领用部门
	 */
	@ApiModelProperty(name = "departmentName", notes = "领用部门")
	private String departmentName;
	/**
	 * @Fields tool_amount : toolAmount
	 */
	@ApiModelProperty(name = "toolAmount", notes = "toolAmount")
	private Integer toolAmount;

	@ApiModelProperty(name = "processList", notes = "processList")
	private List<ToolProcess> processList;

	@ApiModelProperty(name = "repairList", notes = "repairList")
	private List<ToolRepair> repairList;

	@ApiModelProperty(name = "coatList", notes = "coatList")
	private List<ToolCoat> coatList;

	@ApiModelProperty(name = "checkList", notes = "checkList")
	private List<ToolCheck> checkList;

	/**
	 * @Fields tool_amount : checkType
	 */
	@ApiModelProperty(name = "checkType", notes = "质检类型")
	private Integer checkType;
	
	/**
	 * @Fields check_Result : checkType
	 */
	@ApiModelProperty(name = "checkResult", notes = "质检结果1-合格2-不合格")
	private Integer checkResult;
	/**
	 * @Fields scrip_applicant : 报废申请人
	 */
	@ApiModelProperty(name = "scripApplicant", notes = "报废申请人")
	private String scripApplicant;
	/**
	 * @Fields scrip_applicant_time : 报废申请时间
	 */
	@ApiModelProperty(name = "scripApplicantTime", notes = "报废申请时间")
	private Date scripApplicantTime;
	/**
	 * @Fields scrip_auditor : 报废审核人
	 */
	@ApiModelProperty(name = "scripAuditor", notes = "报废审核人")
	private String scripAuditor;
	/**
	 * @Fields scrip_audit_time : 报废审核时间
	 */
	@ApiModelProperty(name = "scripAuditTime", notes = "报废审核时间")
	private Date scripAuditTime;
	/**
	 * @Fields scrip_handler : 报废处理人
	 */
	@ApiModelProperty(name = "scripHandler", notes = "报废处理人")
	private String scripHandler;
	/**
	 * @Fields scrip_handler_time : 报废处理时间
	 */
	@ApiModelProperty(name = "scripHandlerTime", notes = "报废处理时间")
	private Date scripHandlerTime;
	/**
	 * @Fields keeper : 库管员
	 */
	@ApiModelProperty(name = "keeper", notes = "库管员")
	private String keeper;
	/**
	 * @Fields grinder_name : 送磨人员
	 */
	@ApiModelProperty(name = "grinderName", notes = "送磨人员")
	private String grinderName;
	/**
	 * @Fields grinder_id : 送磨时间
	 */
	@ApiModelProperty(name = "grinderTime", notes = "送磨时间")
	private Date grinderTime;
	/**
	 * @Fields staff_department_name : 人员部门名称
	 */
	@ApiModelProperty(name = "staffDepartmentName", notes = "人员部门名称")
	private String staffDepartmentName;

	/**
	 * @Fields check_Id : 检验ID
	 */
	@ApiModelProperty(name = "checkId", notes = "检验ID")
	private Long checkId;

	/**
	 * @Fields check_status : 检验状态
	 */
	@ApiModelProperty(name = "checkStatus", notes = "检验状态")
	private Integer checkStatus;
	/**
	 * @Fields is_scrip : 是否允许报废（1-是；2-否）
	 */
	@ApiModelProperty(name = "isScrip", notes = "是否允许报废（1-是；2-否）")
	private Integer isScrip;
	/**
	 * @Fields process_cur : 本次加工数量
	 */
	@ApiModelProperty(name = "processCur", notes = "本次加工数量")
	private Integer processCur;
	
	/**
	 * @Fields repair_cordon : 预警刃磨量
	 */
	@ApiModelProperty(name = "repairCordon", notes = "预警刃磨量")
	private BigDecimal repairCordon;

	@ApiModelProperty(name = "isHead", notes = "是否刀头（1-是；2-否）")
	private Integer isHead;
	/**
	 * @Fields is_scrip : 是否允许报废（1-是；2-否）
	 */
	@ApiModelProperty(name = "isLife", notes = "是否生命周期（1-是；2-否）")
	private Integer isLife;
	/**
	 * @Fields outbound_time : 出库时间
	 */
	@ApiModelProperty(name = "outboundTime", notes = "送磨时间")
	private Date outboundTime;
	
	/**
	 * @Fields coat_price_amount : 涂层总价
	 */
	@ApiModelProperty(name = "coatPriceAmount", notes = "涂层总价")
	private BigDecimal coatPriceAmount;
	/**
	 * @Fields repair_price_amount : 刃磨总价
	 */
	@ApiModelProperty(name = "repairPriceAmount", notes = "刃磨总价")
	private BigDecimal repairPriceAmount;
	
	/**
	 * @Fields repair_price_amount : 涂层性价比
	 */
	@ApiModelProperty(name = "coatStatistics", notes = "涂层性价比")
	private BigDecimal coatStatistics;
	
	/**
	 * @Fields repair_price_amount : 刃磨总价
	 */
	@ApiModelProperty(name = "toolStatistics", notes = "刀具性价比")
	private BigDecimal toolStatistics;
	
	/**
	 * @Fields is_scrip : 是否允许报废（1-是；2-否）
	 */
	@ApiModelProperty(name = "isCoatStatistics", notes = "是否涂层报表")
	private Integer isCoatStatistics;

}
