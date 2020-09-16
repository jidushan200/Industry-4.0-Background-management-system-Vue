package org.tsinghuatj.tool.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ApiModel
public class ToolPurchaseApply implements Serializable {

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
	 * @Fields purchase_type : 采购类型（1-新品开发；2-常用刀具）
	 */
	@ApiModelProperty(name = "purchaseType", notes = "采购类型（1-新品开发；2-常用刀具）")
	private Integer purchaseType;
	/**
	 * @Fields tool_number : 刀具编码
	 */
	@ApiModelProperty(name = "toolNumber", notes = "刀具编码")
	private String toolNumber;
	/**
	 * @Fields tool_map : toolMap
	 */
	@ApiModelProperty(name = "toolMap", notes = "toolMap")
	private String toolMap;
	/**
	 * @Fields tool_name : toolName
	 */
	@ApiModelProperty(name = "toolName", notes = "toolName")
	private String toolName;
	/**
	 * @Fields typeId : typeId
	 */
	@ApiModelProperty(name = "typeId", notes = "typeId")
	private Integer typeId;
	/**
	 * @Fields type_Name : typeName
	 */
	@ApiModelProperty(name = "typeName", notes = "类型名称")
	private String typeName;
	
	/**
	 * @Fields inventory_qty : 库存数量
	 */
	@ApiModelProperty(name = "inventoryQty", notes = "库存数量")
	private Integer inventoryQty;
	/**
	 * @Fields need_qty : 需求数量
	 */
	@ApiModelProperty(name = "needQty", notes = "需求数量")
	private Integer needQty;
	/**
	 * @Fields need_time : 需求时间
	 */
	@ApiModelProperty(name = "needTime", notes = "需求时间")
	private Date needTime;
	/**
	 * @Fields purchase_resion : 采购原因(1-产量提升；2-刀具报废；3-新品开发；4-其他)
	 */
	@ApiModelProperty(name = "purchaseResion", notes = "采购原因(1-产量提升；2-刀具报废；3-新品开发；4-其他)")
	private Integer purchaseResion;
	/**
	 * @Fields apply_status :
	 *         申请状态(0-新增待提交；1-待分厂领导审核；-1-分厂领导已驳回；2-待技术部审核；-2-技术部已驳回；3-待最终审核；-3-终审未通过；4-采购部采购；5-采购部采购完成)
	 */
	@ApiModelProperty(name = "applyStatus", notes = "申请状态(0-新增待提交；1-待分厂领导审核；-1-分厂领导已驳回；2-待技术部审核；-2-技术部已驳回；3-待最终审核；-3-终审未通过；4-采购部采购；5-采购部采购完成)")
	private Integer applyStatus;
	/**
	 * @Fields remark : 备注
	 */
	@ApiModelProperty(name = "remark", notes = "备注")
	private String remark;
	/**
	 * @Fields result_id : 审核结果ID
	 */
	@ApiModelProperty(name = "resultId", notes = "审核结果ID")
	private Long resultId;
	/**
	 * @Fields report_number : 报告单号
	 */
	@ApiModelProperty(name = "reportNumber", notes = "报告单号")
	private String reportNumber;
	/**
	 * @Fields keep_qty : 库存数量
	 */
	@ApiModelProperty(name = "keepQty", notes = "库存数量")
	private Integer keepQty;
	/**
	 * @Fields applier_id : 申请人ID
	 */
	@ApiModelProperty(name = "applierId", notes = "申请人ID")
	private Long applierId;
	/**
	 * @Fields applier_name : 申请人
	 */
	@ApiModelProperty(name = "applierName", notes = "申请人")
	private String applierName;
	/**
	 * @Fields apply_time : 申请时间
	 */
	@ApiModelProperty(name = "applyTime", notes = "申请时间")
	private Date applyTime;
	/**
	 * @Fields departmentId : 申请部门ID
	 */
	@ApiModelProperty(name = "departmentId", notes = "申请部门ID")
	private Long departmentId;
	/**
	 * @Fields departmentName : 申请部门
	 */
	@ApiModelProperty(name = "departmentName", notes = "申请部门")
	private String departmentName;
	/**
	 * @Fields auiterId : 审核领导ID
	 */
	@ApiModelProperty(name = "auiterId", notes = "审核领导ID")
	private Long auiterId;
	/**
	 * @Fields auiterName : 审核领导
	 */
	@ApiModelProperty(name = "auiterName", notes = "审核领导")
	private String auiterName;
	
	/**
	 * @Fields erp_qty : erpQty
	 */
	@ApiModelProperty(name = "erpQty", notes = "erpQty")
	private Integer erpQty;

	/**
	 * @Fields vailable_number : 可用序号
	 */
	@ApiModelProperty(name = "available_number", notes = "可用序号")
	private String availableNumber;

	/**
	 * @Fields auditList : 审核日志
	 */
	@ApiModelProperty(name = "auditList", notes = "审核日志")
	private List<ToolApplyAudit> auditList;
	
	/**
	 * @Fields finish_Time : finishTime
	 */
	@ApiModelProperty(name = "finishTime", notes = "finishTime")
	private Date finishTime;
	
	/**
	 * @Fields arrivaled_qty : 到货数量
	 */
	@ApiModelProperty(name = "arrivaledQty", notes = "到货数量")
	private Integer arrivaledQty;
	
	/**
	 * @Fields no_check_qty : ERP待检数量
	 */
	@ApiModelProperty(name = "noCheckQty", notes = "ERP待检数量")
	private Integer noCheckQty;
	/**
	 * @Fields no_check_qty : 申请人
	 */
	@ApiModelProperty(name = "applierStr", notes = "申请人")
	private String applierStr;

	/**
	 * @Fields statusList :状态列表
	 */
	@ApiModelProperty(name = "statusList", notes = "状态列表")
	private List<Integer> statusList;
	
	/**
	 * @Fields auiterId : 审核人ID
	 */
	@ApiModelProperty(name = "auditorId", notes = "审核人ID")
	private Long auditorId;
	/**
	 * @Fields auiterId : 审核结果
	 */
	@ApiModelProperty(name = "auditResult", notes = "审核结果")
	private String auditResult;
	/**
	 * @Fields auiterId : 审核人
	 */
	@ApiModelProperty(name = "auditorName", notes = "审核人")
	private String auditorName;
	
	/**
	 * @Fields part_id : 制件ID
	 */
	@ApiModelProperty(name = "partId", notes = "制件ID")
	private Long partId;
	/**
	 * @Fields part_name : 制件名称
	 */
	@ApiModelProperty(name = "partName", notes = "制件名称")
	private String partName;
	
	/**
	 * 收货明细
	 */
	private List<ToolPurchaseReceipt> receipList;
}
