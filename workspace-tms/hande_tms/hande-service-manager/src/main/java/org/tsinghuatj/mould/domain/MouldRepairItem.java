package org.tsinghuatj.mould.domain;

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
public class MouldRepairItem implements Serializable {
	
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
	 * @Fields repair_id : 修磨编码
	 */
	@ApiModelProperty(name = "repairId", notes = "修磨编码")
	private Long repairId;
	/**
	 * @Fields procedure_id : 修磨工序Id
	 */
	@ApiModelProperty(name = "procedureId", notes = "修磨工序Id")
	private Long procedureId;
	/**
	 * @Fields procedure_name : 修磨工序
	 */
	@ApiModelProperty(name = "procedureName", notes = "修磨工序")
	private String procedureName;
	/**
	 * @Fields executor_id : 刃磨人Id
	 */
	@ApiModelProperty(name = "executorId", notes = "刃磨人Id")
	private Long executorId;
	/**
	 * @Fields executor : 刃磨人
	 */
	@ApiModelProperty(name = "executor", notes = "刃磨人")
	private String executor;
	/**
	 * @Fields execut_time : 刃磨时间
	 */
	@ApiModelProperty(name = "executTime", notes = "刃磨时间")
	private Date executTime;
}

