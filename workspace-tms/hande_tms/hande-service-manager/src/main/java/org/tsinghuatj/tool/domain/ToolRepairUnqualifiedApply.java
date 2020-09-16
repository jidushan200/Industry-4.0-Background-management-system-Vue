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
public class ToolRepairUnqualifiedApply implements Serializable {

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
	 * @Fields tool_number : 刀具编码
	 */
	@ApiModelProperty(name = "toolNumber", notes = "刀具编码")
	private String toolNumber;
	/**
	 * @Fields audit_status :
	 *         审核状态(0-待工艺部审核,-1工艺部审核不通过,-2-工艺部审核通过待报废,-3-工艺部审核通过待刃磨,1-质检部判定为合格,2-库房已报废,3-刃磨班已重修)
	 */
	@ApiModelProperty(name = "auditStatus", notes = "审核状态(0-待工艺部审核,-1工艺部审核不通过,-2-工艺部审核通过待报废,-3-工艺部审核通过待刃磨,1-质检部判定为合格,2-库房已报废,3-刃磨班已重修)")
	private Integer auditStatus;
	/**
	 * @Fields applier_id : 申请人Id
	 */
	@ApiModelProperty(name = "applierId", notes = "申请人Id")
	private Long applierId;
	/**
	 * @Fields applier_name : 申请人姓名
	 */
	@ApiModelProperty(name = "applierName", notes = "申请人姓名")
	private String applierName;
	/**
	 * @Fields apply_time : 申请时间
	 */
	@ApiModelProperty(name = "applyTime", notes = "申请时间")
	private Date applyTime;
	/**
	 * @Fields apply_desc : 原因描述
	 */
	@ApiModelProperty(name = "applyDesc", notes = "原因描述")
	private String applyDesc;
	/**
	 * @Fields department_id : 部门ID
	 */
	@ApiModelProperty(name = "departmentId", notes = "部门ID")
	private Long departmentId;
	/**
	 * @Fields department_name : 部门名称
	 */
	@ApiModelProperty(name = "departmentName", notes = "部门名称")
	private String departmentName;
	
	@ApiModelProperty(name = "appendixList", notes = "检验附件")
	private List<ToolAppendix> appendixList;

	@Override
	public String toString() {
		return "ToolRepairUnqualifiedApply " + "[pkId=" + pkId + "]" + ",[createUser=" + createUser + "]"
				+ ",[createTime=" + createTime + "]" + ",[updateUser=" + updateUser + "]" + ",[updateTime=" + updateTime
				+ "]" + ",[delMark=" + delMark + "]" + ",[toolId=" + toolId + "]" + ",[toolName=" + toolName + "]"
				+ ",[toolNumber=" + toolNumber + "]" + ",[auditStatus=" + auditStatus + "]" + ",[applierId=" + applierId
				+ "]" + ",[applierName=" + applierName + "]" + ",[applyTime=" + applyTime + "]" + ",[applyDesc="
				+ applyDesc + "]" + ",[departmentId=" + departmentId + "]" + ",[departmentName=" + departmentName + "]";
	}
}
