package org.tsinghuatj.base.domain;

import java.io.Serializable;
import java.util.Date;

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
public class Staff implements Serializable {

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
	 * @Fields staff_code : 员工编码
	 */
	@ExcelField(title = "员工编码", order = 1)
	@ApiModelProperty(name = "staffCode", notes = "员工编码")
	private String staffCode;
	/**
	 * @Fields staff_name : 员工姓名
	 */
	@ExcelField(title = "员工姓名", order = 2)
	@ApiModelProperty(name = "staffName", notes = "员工姓名")
	private String staffName;

	/**
	 * @Fields gender : 性别(0-女;1-男)
	 */
	// @ExcelField(title = "gender", order = 4)
	@ApiModelProperty(name = "gender", notes = "性别(0-女;1-男)")
	private Integer gender;

	/**
	 * @Fields gender : 性别(0-女;1-男)
	 */
	@ExcelField(title = "性别", order = 3)
	@ApiModelProperty(name = "genderName", notes = "性别(0-女;1-男)")
	private String genderName;
	/**
	 * @Fields staff_status : 员工状态(0-在职;1-离职)
	 */
	@ApiModelProperty(name = "staffStatus", notes = "员工状态(0-在职;1-离职)")
	private Integer staffStatus;
	/**
	 * @Fields position : 职务
	 */
	@ExcelField(title = "员工职务", order = 6)
	@ApiModelProperty(name = "position", notes = "岗位")
	private String position;
	/**
	 * @Fields department_id : 所属部门
	 */
	@ApiModelProperty(name = "departmentId", notes = "所属部门")
	private Long departmentId;
	/**
	 * @Fields department_name : 所属部门
	 */
	@ExcelField(title = "部门名称", order = 4)
	@ApiModelProperty(name = "departmentName", notes = "所属部门")
	private String departmentName;

	/**
	 * @Fields team_id : 班组ID
	 */
	@ApiModelProperty(name = "teamId", notes = "班组ID")
	private Long teamId;
	/**
	 * @Fields team_name : 所属班组
	 */
	@ExcelField(title = "班组", order = 5)
	@ApiModelProperty(name = "teamName", notes = "所属班组")
	private String teamName;

	/**
	 * @Fields staff_status : 是否领导(1-是；2-不是)
	 */
	@ApiModelProperty(name = "leader", notes = "是否领导(1-是；2-不是)")
	private Integer leader;

	/**
	 * @Fields remark : 备注
	 */
	@ApiModelProperty(name = "remark", notes = "备注")
	private String remark;

}
