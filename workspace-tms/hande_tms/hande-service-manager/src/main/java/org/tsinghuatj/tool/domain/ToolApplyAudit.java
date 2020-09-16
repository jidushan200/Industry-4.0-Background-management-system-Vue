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
public class ToolApplyAudit implements Serializable {

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
	 * @Fields apply_id : 申请单ID
	 */
	@ApiModelProperty(name = "applyId", notes = "申请单ID")
	private Long applyId;
	/**
	 * @Fields apply_type : 申请类型(1-采购申请2-不合格刀具报告)
	 */
	@ApiModelProperty(name = "applyType", notes = "申请类型(1-采购申请2-不合格刀具报告)")
	private Integer applyType;
	/**
	 * @Fields audit_result : 审核结果
	 */
	@ApiModelProperty(name = "auditResult", notes = "审核结果")
	private String auditResult;
	/**
	 * @Fields auditor_id : 审核人Id
	 */
	@ApiModelProperty(name = "auditorId", notes = "审核人Id")
	private Long auditorId;
	/**
	 * @Fields auditor_name : 审核人姓名
	 */
	@ApiModelProperty(name = "auditorName", notes = "审核人姓名")
	private String auditorName;
	/**
	 * @Fields audit_department_id : 审核部门ID
	 */
	@ApiModelProperty(name = "auditDepartmentId", notes = "审核部门ID")
	private Long auditDepartmentId;
	/**
	 * @Fields audit_department_name : 审核部门
	 */
	@ApiModelProperty(name = "auditDepartmentName", notes = "审核部门")
	private String auditDepartmentName;
	/**
	 * @Fields audit_seq : 审核次序
	 */
	@ApiModelProperty(name = "auditSeq", notes = "审核次序")
	private Integer auditSeq;
	/**
	 * @Fields audit_status : 审核状态
	 */
	@ApiModelProperty(name = "auditStatus", notes = "审核状态")
	private Integer auditStatus;
	/**
	 * @Fields remark : 备注
	 */
	@ApiModelProperty(name = "remark", notes = "备注")
	private String remark;

	@Override
	public String toString() {
		return "ToolApplyAudit " + "[pkId=" + pkId + "]" + ",[createUser=" + createUser + "]" + ",[createTime=" + createTime + "]" + ",[updateUser=" + updateUser + "]" + ",[updateTime=" + updateTime + "]" + ",[delMark=" + delMark + "]" + ",[applyId=" + applyId + "]" + ",[applyType=" + applyType + "]" + ",[auditResult=" + auditResult + "]" + ",[auditorId=" + auditorId + "]" + ",[auditorName=" + auditorName + "]" + ",[auditDepartmentId=" + auditDepartmentId + "]" + ",[auditDepartmentName=" + auditDepartmentName + "]" + ",[auditSeq=" + auditSeq + "]" + ",[auditStatus=" + auditStatus + "]" + ",[remark=" + remark + "]";
	}
}
