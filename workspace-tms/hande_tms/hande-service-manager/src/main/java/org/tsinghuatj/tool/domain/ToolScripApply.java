package org.tsinghuatj.tool.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
public class ToolScripApply implements Serializable {
	
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
	 * @Fields tool_number : 刀具编码
	 */
	@ApiModelProperty(name = "toolNumber", notes = "刀具编码")
	private String toolNumber;
	/**
	 * @Fields scrip_resion : 报废原因(1-正常报废；2-异常报废)
	 */
	@ApiModelProperty(name = "scripResion", notes = "报废原因(1-正常报废；2-异常报废)")
	private Integer scripResion;
	/**
	 * @Fields apply_status : 申请状态(0-新增待提交；1-工艺部审核通过；-1-工艺部已驳回)
	 */
	@ApiModelProperty(name = "applyStatus", notes = "申请状态(0-新增待提交；1-工艺部审核通过；-1-工艺部已驳回)")
	private Integer applyStatus;
	/**
	 * @Fields scrip_remark : 报废原因说明
	 */
	@ApiModelProperty(name = "scripRemark", notes = "报废原因说明")
	private String scripRemark;

	/**
	 * @Fields applier_id : 申请人ID
	 */
	@ApiModelProperty(name = "applierId", notes = "申请人ID")
	private Long applierId;
	/**
	 * @Fields applier_name : 申请人编号
	 */
	@ApiModelProperty(name = "applierCode", notes = "申请人编号")
	private String applierCode;
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
	 * @Fields tool_map : 刀具图号
	 */
	@ApiModelProperty(name = "toolMap", notes = "刀具图号")
	private String toolMap;
	/**
	 * @Fields tool_name : 刀具名称
	 */
	@ApiModelProperty(name = "toolName", notes = "刀具名称")
	private String toolName;
	/**
	 * @Fields over_time : 审核时间
	 */
	@ApiModelProperty(name = "overTime", notes = "审核时间")
	private Date overTime;
	/**
	 * @Fields full_number : 物料条码
	 */
	@ApiModelProperty(name = "fullNumber", notes = "物料条码")
	private String fullNumber;
	/**
	 * @Fields warehouse_code : 入库编码
	 */
	@ApiModelProperty(name = "warehouseCode", notes = "入库编码")
	private String warehouseCode;
	/**
	 * @Fields disagree_remark : 不同意原因说明
	 */
	@ApiModelProperty(name = "disagreeRemark", notes = "不同意原因说明")
	private String disagreeRemark;
	
	/**
	 * @Fields statusList :状态列表
	 */
	@ApiModelProperty(name = "statusList", notes = "状态列表")
	private List<Integer> statusList;
	
	
	private Integer processAmount;
	
	
}

