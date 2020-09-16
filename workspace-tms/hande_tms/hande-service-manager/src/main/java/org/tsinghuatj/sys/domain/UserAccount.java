package org.tsinghuatj.sys.domain;

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
public class UserAccount implements Serializable {

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
	 * @Fields create_user_name : 创建用户姓名
	 */
	@ApiModelProperty(name = "createUserName", notes = "创建用户姓名")
	private String createUserName;	
	/**
	 * @Fields update_user_name : 更新用户姓名
	 */
	@ApiModelProperty(name = "updateUserName", notes = "创建用户姓名")
	private String updateUserName;
	/**
	 * @Fields del_mark : 删除标识(--1正常--2已删除)
	 */
	@ApiModelProperty(name = "delMark", notes = "删除标识(--1正常--2已删除)")
	private Integer delMark;
	/**
	 * @Fields login_name : 登录名
	 */
	@ApiModelProperty(name = "loginName", notes = "登录名")
	private String loginName;
	/**
	 * @Fields login_pwd : 密码
	 */
	@ApiModelProperty(name = "loginPwd", notes = "密码")
	private String loginPwd;
	/**
	 * @Fields real_name : 真实姓名
	 */
	@ApiModelProperty(name = "realName", notes = "真实姓名")
	private String realName;
	/**
	 * @Fields mobile : 联系电话
	 */
	@ApiModelProperty(name = "mobile", notes = "mobile")
	private String mobile;
	/**
	 * @Fields email : email
	 */
	@ApiModelProperty(name = "email", notes = "email")
	private String email;

	/**
	 * @Fields role_id : 角色Id
	 */
	@ApiModelProperty(name = "roleId", notes = "角色Id")
	private Long roleId;

	/**
	 * @Fields role_name : 角色名称
	 */
	@ApiModelProperty(name = "roleName", notes = "角色名称")
	private String roleName;

	/**
	 * @Fields auth_code : 权限码
	 */
	@ApiModelProperty(name = "authCode", notes = "权限码")
	private String authCode;

	//子节点
	private List<SysAuthInfo> authList;
	
	/**
	 * @Fields department_Id : 部门Id
	 */
	@ApiModelProperty(name = "departmentId", notes = "部门Id")
	private Long departmentId;
	
	
	/**
	 * @Fields department_name : 部门名称
	 */
	@ApiModelProperty(name = "departmentName", notes = "部门名称")
	private String departmentName;

}
