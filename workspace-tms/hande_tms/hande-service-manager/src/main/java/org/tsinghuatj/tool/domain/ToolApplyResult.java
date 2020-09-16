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
public class ToolApplyResult implements Serializable {
	
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
	 * @Fields result : 审核结果(1-同意；2-驳回)
	 */
	@ApiModelProperty(name = "result", notes = "审核结果(1-同意；2-驳回)")
	private Integer result;
	/**
	 * @Fields approver_id : 审批人Id
	 */
	@ApiModelProperty(name = "approverId", notes = "审批人Id")
	private Long approverId;
	/**
	 * @Fields approver_name : 审批人姓名
	 */
	@ApiModelProperty(name = "approverName", notes = "审批人姓名")
	private String approverName;
	/**
	 * @Fields remark : 备注
	 */
	@ApiModelProperty(name = "remark", notes = "备注")
	private String remark;
	/**
	 * @Fields apply_type : 申请类型（0-不合格刀具申请；1-采购申请）
	 */
	@ApiModelProperty(name = "applyType", notes = "申请类型（0-不合格刀具申请；1-采购申请）")
	private Integer applyType;
	/**
	 * @Fields apply_id : 申请单ID
	 */
	@ApiModelProperty(name = "applyId", notes = "申请单ID")
	private Long applyId;
	
	
}

