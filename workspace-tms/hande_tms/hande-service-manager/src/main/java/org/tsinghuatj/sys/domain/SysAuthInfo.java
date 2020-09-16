package org.tsinghuatj.sys.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.tsinghuatj.framework.utils.excel.annotation.ExcelField;

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
public class SysAuthInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * @Fields pk_id : 主键
	 */
	@ExcelField(title = "pkId", order = 1)
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
	 * @Fields parent_Id : 父ID
	 */
	@ApiModelProperty(name = "parentId", notes = "父ID")
	private Long parentId;
	/**
	 * @Fields author_code : 授权码
	 */
	@ExcelField(title = "authorCode", order = 2)
	@ApiModelProperty(name = "authorCode", notes = "授权码")
	private String authorCode;
	/**
	 * @Fields author_name : 授权码描述
	 */
	@ExcelField(title = "authorName", order = 3)
	@ApiModelProperty(name = "authorName", notes = "授权码描述")
	private String authorName;

	/**
	 * @Fields parent_code : 父编码
	 */
	@ExcelField(title = "parentAuthCode", order = 4)
	@ApiModelProperty(name = "parentCode", notes = "父编码")
	private String parentCode;
	
	/**
	 * @Fields level : 级
	 */
	@ExcelField(title = "level", order = 5)
	@ApiModelProperty(name = "level", notes = "级")
	private Integer level;
	/**
	 * @Fields author_name : 授权码描述
	 */
	@ApiModelProperty(name = "title", notes = "授权码描述")
	private String title;

	private boolean expand;
	// 字节点
	private List<SysAuthInfo> children;

	@Override
	public String toString() {
		return "SysAuthInfo " + "[pkId=" + pkId + "]" + ",[createUser=" + createUser + "]" + ",[createTime="
				+ createTime + "]" + ",[updateUser=" + updateUser + "]" + ",[updateTime=" + updateTime + "]"
				+ ",[delMark=" + delMark + "]" + ",[authorCode=" + authorCode + "]" + ",[authorName=" + authorName
				+ "]";
	}
}
