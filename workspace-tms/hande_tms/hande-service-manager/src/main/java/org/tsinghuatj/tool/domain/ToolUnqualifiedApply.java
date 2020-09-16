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
public class ToolUnqualifiedApply implements Serializable {
	
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
	 * @Fields apply_type : 申请类型(1-新刀不合格索赔;2-刃磨不合格;3-涂层不合格;)
	 */
	@ApiModelProperty(name = "applyType", notes = "申请类型(1-新刀不合格索赔;2-刃磨不合格;3-涂层不合格;)")
	private Integer applyType;
	/**
	 * @Fields audit_status : 申请状态（0-待领导审核;1-待工艺部审核;2-待采购部审核）
	 */
	@ApiModelProperty(name = "auditStatus", notes = "申请状态（0-待领导审核;1-待工艺部审核;2-待采购部审核）")
	private Integer auditStatus;
	/**
	 * @Fields applier : 申请人
	 */
	@ApiModelProperty(name = "applier", notes = "申请人")
	private String applier;
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
	
	@Override
    public String toString() {
        return  "ToolUnqualifiedApply " + 
                    "[pkId=" + pkId + "]"
                   + ",[createUser=" + createUser + "]"
                                   + ",[createTime=" + createTime + "]"
                                   + ",[updateUser=" + updateUser + "]"
                                   + ",[updateTime=" + updateTime + "]"
                                   + ",[delMark=" + delMark + "]"
                                   + ",[toolId=" + toolId + "]"
                                   + ",[toolName=" + toolName + "]"
                                   + ",[toolNumber=" + toolNumber + "]"
                                   + ",[applyType=" + applyType + "]"
                                   + ",[auditStatus=" + auditStatus + "]"
                                   + ",[applier=" + applier + "]"
                                   + ",[applyTime=" + applyTime + "]"
                                   + ",[applyDesc=" + applyDesc + "]"
                                   + ",[departmentId=" + departmentId + "]"
                                   + ",[departmentName=" + departmentName + "]"
                ;
	}
}

