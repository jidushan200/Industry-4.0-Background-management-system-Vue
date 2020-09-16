package org.tsinghuatj.tool.domain;

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
public class ToolPlate implements Serializable {
	
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
	 * @Fields plate_number : 刀盘编码
	 */
	@ExcelField(title = "刀盘编码", order = 1)
	@ApiModelProperty(name = "plateNumber", notes = "刀盘编码")
	private String plateNumber;
	/**
	 * @Fields plate_name : 刀盘名称
	 */
	@ExcelField(title = "刀盘名称", order = 2)
	@ApiModelProperty(name = "plateName", notes = "刀盘名称")
	private String plateName;
	/**
	 * @Fields use_status : 使用状态(1-在用;2-在库)
	 */
	@ApiModelProperty(name = "useStatus", notes = "使用状态(1-在用;2-在库)")
	private Integer useStatus;
	/**
	 * @Fields useStatusName : 使用状态
	 */
	@ExcelField(title = "使用状态", order = 3)
	@ApiModelProperty(name = "useStatusName", notes = "使用状态")
	private String useStatusName;

}

