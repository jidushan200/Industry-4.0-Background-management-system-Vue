package org.tsinghuatj.mould.domain;

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
public class MouldRepair implements Serializable {
	
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
	 * @Fields mould_id : 模具ID
	 */
	@ApiModelProperty(name = "mouldId", notes = "模具ID")
	private Long mouldId;
	/**
	 * @Fields full_number : 模具条码
	 */
	@ApiModelProperty(name = "fullNumber", notes = "模具条码")
	private String fullNumber;
	/**
	 * @Fields repair_user_id : repairUserId
	 */
	@ApiModelProperty(name = "repairUserId", notes = "repairUserId")
	private Long repairUserId;
	/**
	 * @Fields repair_user : repairUser
	 */
	@ApiModelProperty(name = "repairUser", notes = "repairUser")
	private String repairUser;
	/**
	 * @Fields repair_time : repairTime
	 */
	@ApiModelProperty(name = "repairTime", notes = "repairTime")
	private Date repairTime;
	/**
	 * @Fields repair_seq : 刃磨次序
	 */
	@ApiModelProperty(name = "repairSeq", notes = "刃磨次序")
	private Integer repairSeq;
	/**
	 * @Fields remark : 备注
	 */
	@ApiModelProperty(name = "remark", notes = "备注")
	private String remark;
	
	@ApiModelProperty(name = "itemList", notes = "质检项")
	private List<MouldRepairItem> itemList;
}

