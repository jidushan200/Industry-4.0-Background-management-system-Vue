package org.tsinghuatj.measure.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.tsinghuatj.tool.domain.ToolApplyAudit;

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
public class MeasurePurchaseApply implements Serializable {
	
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
	 * @Fields measure_number : 量具编码
	 */
	@ApiModelProperty(name = "measureNumber", notes = "量具编码")
	private String measureNumber;
	/**
	 * @Fields measure_name : 量具名称
	 */
	@ApiModelProperty(name = "measureName", notes = "量具名称")
	private String measureName;
	/**
	 * @Fields model : 规格型号
	 */
	@ApiModelProperty(name = "model", notes = "规格型号")
	private String model;
	/**
	 * @Fields apply_qty : 申购数量
	 */
	@ApiModelProperty(name = "applyQty", notes = "申购数量")
	private Integer applyQty;
	/**
	 * @Fields demand_time : 需求时间
	 */
	@ApiModelProperty(name = "demandTime", notes = "需求时间")
	private Date demandTime;
	/**
	 * @Fields purchase_reasion : 采购原因(1-产量提升；2-量具报废；3-新品开发；4-其他)
	 */
	@ApiModelProperty(name = "purchaseReasion", notes = "采购原因(1-产量提升；2-量具报废；3-新品开发；4-其他)")
	private Integer purchaseReasion;
	/**
	 * @Fields inventory_qty : 库存数量
	 */
	@ApiModelProperty(name = "inventoryQty", notes = "库存数量")
	private Integer inventoryQty;
	/**
	 * @Fields erp_qty : erp库存数量
	 */
	@ApiModelProperty(name = "erpQty", notes = "erp库存数量")
	private Integer erpQty;
	/**
	 * @Fields no_check_qty : erp待检数量
	 */
	@ApiModelProperty(name = "noCheckQty", notes = "erp待检数量")
	private Integer noCheckQty;
	/**
	 * @Fields apply_department_Id : 申请部门ID
	 */
	@ApiModelProperty(name = "applyDepartmentId", notes = "申请部门ID")
	private Long applyDepartmentId;
	/**
	 * @Fields apply_department_Name : 申请部门
	 */
	@ApiModelProperty(name = "applyDepartmentName", notes = "申请部门")
	private String applyDepartmentName;
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
	 * @Fields use_department_Id : 使用部门ID
	 */
	@ApiModelProperty(name = "useDepartmentId", notes = "使用部门ID")
	private Long useDepartmentId;
	/**
	 * @Fields use_department_Name : 使用部门名称
	 */
	@ApiModelProperty(name = "useDepartmentName", notes = "使用部门名称")
	private String useDepartmentName;
	/**
	 * @Fields use_team_id : 使用班组ID
	 */
	@ApiModelProperty(name = "useTeamId", notes = "使用班组ID")
	private Long useTeamId;
	/**
	 * @Fields use_team_name : 使用班组名称
	 */
	@ApiModelProperty(name = "useTeamName", notes = "使用班组名称")
	private String useTeamName;
	/**
	 * @Fields user_id : 使用人ID
	 */
	@ApiModelProperty(name = "userId", notes = "使用人ID")
	private Long userId;
	/**
	 * @Fields user_name : 使用人名称
	 */
	@ApiModelProperty(name = "userName", notes = "使用人名称")
	private String userName;
	/**
	 * @Fields apply_time : 申请时间
	 */
	@ApiModelProperty(name = "applyTime", notes = "申请时间")
	private Date applyTime;
	/**
	 * @Fields apply_status : 申请状态(0-新增待提交；1-待分厂领导审核；-1-分厂领导已驳回；2-待技术部审核；-2-技术部已驳回；3-待最终审核；-3-终审未通过；4-采购部采购；5-采购部采购完成)
	 */
	@ApiModelProperty(name = "applyStatus", notes = "申请状态(0-新增待提交；1-待分厂领导审核；-1-分厂领导已驳回；2-待技术部审核；-2-技术部已驳回；3-待最终审核；-3-终审未通过；4-采购部采购；5-采购部采购完成)")
	private Integer applyStatus;
	/**
	 * @Fields purchase_price : 采购价格
	 */
	@ApiModelProperty(name = "purchasePrice", notes = "采购价格")
	private BigDecimal purchasePrice;
	
	/**
	 * @Fields auditList : 审核日志
	 */
	@ApiModelProperty(name = "auditList", notes = "审核日志")
	private List<ToolApplyAudit> auditList;
	
	/**
	 * @Fields available_number : 可用序号
	 */
	@ApiModelProperty(name = "availableNumber", notes = "可用序号")
	private String availableNumber;
	
	/**
	 * @Fields finish_time : 采购完成时间
	 */
	@ApiModelProperty(name = "finishTime", notes = "采购完成时间")
	private Date finishTime;
	/**
	 * @Fields arrivaled_qty : 到货数量
	 */
	@ApiModelProperty(name = "arrivaledQty", notes = "到货数量")
	private Integer arrivaledQty;
	
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
	 * @Fields arrivaled_qty : 是否新品
	 */
	@ApiModelProperty(name = "isNew", notes = "是否新品")
	private Integer isNew;
}

