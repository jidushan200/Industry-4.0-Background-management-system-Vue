package org.tsinghuatj.account.domain;

import java.io.Serializable;
import java.math.BigDecimal;
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
public class SysMenu implements Serializable {
	
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
	 * @Fields parent_id : 菜单父id
	 */
	@ApiModelProperty(name = "parentId", notes = "菜单父id")
	private Long parentId;
	/**
	 * @Fields name : 菜单名称
	 */
	@ApiModelProperty(name = "name", notes = "菜单名称")
	private String name;
	/**
	 * @Fields path : 路径
	 */
	@ApiModelProperty(name = "path", notes = "路径")
	private String path;
	/**
	 * @Fields title : 标题
	 */
	@ApiModelProperty(name = "title", notes = "标题")
	private String title;
	/**
	 * @Fields menu_icon : 菜单图标
	 */
	@ApiModelProperty(name = "menuIcon", notes = "菜单图标")
	private String menuIcon;
	/**
	 * @Fields sys_icon : 系统图标
	 */
	@ApiModelProperty(name = "sysIcon", notes = "系统图标")
	private String sysIcon;
	/**
	 * @Fields component : 组件
	 */
	@ApiModelProperty(name = "component", notes = "组件")
	private String component;
	/**
	 * @Fields access : 权限编码
	 */
	@ApiModelProperty(name = "access", notes = "权限编码")
	private String access;
	/**
	 * @Fields level : 菜单层级
	 */
	@ApiModelProperty(name = "level", notes = "菜单层级")
	private Integer level;
	
	@Override
    public String toString() {
        return  "SysMenu " + 
                    "[pkId=" + pkId + "]"
                   + ",[createUser=" + createUser + "]"
                                   + ",[createTime=" + createTime + "]"
                                   + ",[updateUser=" + updateUser + "]"
                                   + ",[updateTime=" + updateTime + "]"
                                   + ",[delMark=" + delMark + "]"
                                   + ",[parentId=" + parentId + "]"
                                   + ",[name=" + name + "]"
                                   + ",[path=" + path + "]"
                                   + ",[title=" + title + "]"
                                   + ",[menuIcon=" + menuIcon + "]"
                                   + ",[sysIcon=" + sysIcon + "]"
                                   + ",[component=" + component + "]"
                                   + ",[access=" + access + "]"
                                   + ",[level=" + level + "]"
                ;
	}
}

