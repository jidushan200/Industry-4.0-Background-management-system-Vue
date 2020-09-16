package org.tsinghuatj.tool.domain;

import java.io.Serializable;
import java.util.Date;

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
public class ToolBladeOutbound implements Serializable {
	
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
	 * @Fields composeNumber : 刀盘刀条组合码
	 */
	@ApiModelProperty(name = "composeNumber", notes = "刀盘刀条组合码")
	private String composeNumber;

	/**
	 * @Fields tool_number : 物料编码
	 */
	@ApiModelProperty(name = "toolNumber", notes = "物料编码")
	private String toolNumber;
	/**
	 * @Fields tool_qty : 需求数量
	 */
	@ApiModelProperty(name = "toolQty", notes = "需求数量")
	private Integer toolQty;
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
	 * @Fields team_id : 领用班组ID
	 */
	@ApiModelProperty(name = "teamId", notes = "领用班组ID")
	private Long teamId;
	/**
	 * @Fields team_name : 领用班组
	 */
	@ApiModelProperty(name = "teamName", notes = "领用班组")
	private String teamName;
	
	/**
	 * @Fields supplier_id : 供应商
	 */
	@ApiModelProperty(name = "supplierId", notes = "供应商ID")
	private Long supplierId;
	/**
	 * @Fields supplier_name : 供应商
	 */
	@ApiModelProperty(name = "supplierName", notes = "供应商")
	private String supplierName;
	/**
	 * @Fields receiver : 领用人
	 */
	@ApiModelProperty(name = "receiver", notes = "领用人")
	private String receiver;
	/**
	 * @Fields remark : 说明
	 */
	@ApiModelProperty(name = "remark", notes = "说明")
	private String remark;
	
	
}

