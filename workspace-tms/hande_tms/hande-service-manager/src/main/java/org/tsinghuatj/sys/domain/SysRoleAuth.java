package org.tsinghuatj.sys.domain;

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
public class SysRoleAuth implements Serializable {

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
	 * @Fields del_mark : 删除标识(--1正常--2已删除)
	 */
	@ApiModelProperty(name = "delMark", notes = "删除标识(--1正常--2已删除)")
	private Integer delMark;
	/**
	 * @Fields role_id : 角色ID
	 */
	@ApiModelProperty(name = "roleId", notes = "角色ID")
	private Long roleId;
	/**
	 * @Fields auth_code : 角色授权码
	 */
	@ApiModelProperty(name = "authCode", notes = "角色授权码")
	private String authCode;

	@Override
	public String toString() {
		return "SysRoleAuth " + "[pkId=" + pkId + "]" + ",[createUser=" + createUser + "]" + ",[createTime="
				+ createTime + "]" + ",[updateUser=" + updateUser + "]" + ",[updateTime=" + updateTime + "]"
				+ ",[delMark=" + delMark + "]" + ",[roleId=" + roleId + "]" + ",[authCode=" + authCode + "]";
	}
}
