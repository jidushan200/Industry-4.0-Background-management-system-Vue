package org.tsinghuatj.tool.domain;

import java.io.Serializable;
import java.util.Date;

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
public class ToolOutbound implements Serializable {
	
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
	 * @Fields tool_id : 刀具ID
	 */
	@ApiModelProperty(name = "toolId", notes = "刀具ID")
	private Long toolId;
	/**
	 * @Fields tool_name : 刀具名称
	 */
	@ApiModelProperty(name = "toolName", notes = "刀具名称")
	private String toolName;
	/**
	 * @Fields tool_number : 物料编码
	 */
	@ApiModelProperty(name = "toolNumber", notes = "物料编码")
	private String toolNumber;
	
	/**
	 * @Fields tool_number : 物料条码
	 */
	@ApiModelProperty(name = "fullNumber", notes = "物料编码")
	private String fullNumber;
	
	
	/**
	 * @Fields warehouse_code : 入库编码
	 */
	@ApiModelProperty(name = "warehouseCode", notes = "入库编码")
	private String warehouseCode;
	/**
	 * @Fields tool_map : 物料图号
	 */
	@ApiModelProperty(name = "toolMap", notes = "物料图号")
	private String toolMap;
	/**
	 * @Fields warehouse : 库位
	 */
	@ApiModelProperty(name = "warehouse", notes = "库位")
	private String warehouse;
	/**
	 * @Fields out_type : 出库类型(1-领用出库；2-刃磨出库；3-涂层出库)
	 */
	@ApiModelProperty(name = "outType", notes = "出库类型(1-领用出库；2-刃磨出库；3-涂层出库)")
	private Integer outType;
	/**
	 * @Fields department_id : 领用部门ID
	 */
	@ApiModelProperty(name = "departmentId", notes = "领用部门ID")
	private Long departmentId;
	/**
	 * @Fields department_name : 领用部门名称
	 */
	@ApiModelProperty(name = "departmentName", notes = "领用部门名称")
	private String departmentName;
	/**
	 * @Fields staff_code : 员工编号
	 */
	@ApiModelProperty(name = "staffCode", notes = "员工编号")
	private String staffCode;
	/**
	 * @Fields staff_name : 员工姓名
	 */
	@ApiModelProperty(name = "staffName", notes = "员工姓名")
	private String staffName;
	/**
	 * @Fields supplier_id : 供应商ID
	 */
	@ApiModelProperty(name = "supplierId", notes = "供应商ID")
	private Long supplierId;
	/**
	 * @Fields supplier_name : 供应商
	 */
	@ApiModelProperty(name = "supplierName", notes = "供应商")
	private String supplierName;
	/**
	 * @Fields tool_amount : 需求数量
	 */
	@ApiModelProperty(name = "toolAmount", notes = "需求数量")
	private Integer toolAmount;

	/**
	 * @Fields team_id : 班组ID
	 */
	@ApiModelProperty(name = "teamId", notes = "班组ID")
	private Long teamId;
	/**
	 * @Fields 班组名称 : 班组名称
	 */
	@ApiModelProperty(name = "teamName", notes = "班组名称")
	private String teamName;
	/**
	 * @Fields receive_time : 领用时间
	 */
	@ApiModelProperty(name = "receiveTime", notes = "领用时间")
	private Date receiveTime;
	/**
	 * @Fields keeper_id : 库管员ID
	 */
	@ApiModelProperty(name = "keeperId", notes = "库管员ID")
	private Long keeperId;
	/**
	 * @Fields keeper : 库管员
	 */
	@ApiModelProperty(name = "keeper", notes = "库管员")
	private String keeper;
	/**
	 * @Fields receive_resion : 领用原因
	 */
	@ApiModelProperty(name = "receiveResion", notes = "领用原因")
	private String receiveResion;
	/**
	 * @Fields remark : 说明
	 */
	@ApiModelProperty(name = "remark", notes = "说明")
	private String remark;
	
	
}

