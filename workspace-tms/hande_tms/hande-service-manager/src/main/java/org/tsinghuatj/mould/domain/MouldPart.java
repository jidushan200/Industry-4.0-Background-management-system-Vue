package org.tsinghuatj.mould.domain;

import java.io.Serializable;
import java.util.Date;

import org.tsinghuatj.framework.utils.excel.annotation.ExcelField;

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
public class MouldPart implements Serializable {
	
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
	 * @Fields mould_id : 模具id
	 */
	@ApiModelProperty(name = "mouldId", notes = "模具id")
	private Long mouldId;
	/**
	 * @Fields part_id : 制件ID
	 */
	@ApiModelProperty(name = "partId", notes = "制件ID")
	private Long partId;
	
	/**
	 * @Fields tool_number : 模具编码
	 */
	@ExcelField(title = "模具编码", order = 1)
	@ApiModelProperty(name = "mouldNumber", notes = "模具编码")
	private String mouldNumber;

	/**
	 * @Fields tool_name : 模具名称
	 */
	@ExcelField(title = "模具名称", order = 2)
	@ApiModelProperty(name = "mouldName", notes = "模具名称")
	private String mouldName;
	
	/**
	 * @Fields tool_map : 模具图号
	 */
	@ExcelField(title = "模具图号", order = 3)
	@ApiModelProperty(name = "mouldMap", notes = "模具图号")
	private String mouldMap;

	/**
	 * @Fields part_code : 制件编码
	 */
	@ExcelField(title = "制件编码", order = 4)
	@ApiModelProperty(name = "partCode", notes = "制件编码")
	private String partCode;

	/**
	 * @Fields part_name : 制件名称
	 */
	@ExcelField(title = "制件名称", order = 5)
	@ApiModelProperty(name = "partName", notes = "制件名称")
	private String partName;
}

