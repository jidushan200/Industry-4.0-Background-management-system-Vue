package org.tsinghuatj.base.domain;

import java.io.Serializable;
import java.math.BigDecimal;
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
public class Part implements Serializable {
	
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
	 * @Fields part_name : 零件名称
	 */
	@ExcelField(title = "制件名称", order = 2)
	@ApiModelProperty(name = "partName", notes = "零件名称")
	private String partName;
	/**
	 * @Fields price : 单价
	 */
	@ApiModelProperty(name = "price", notes = "单价")
	private BigDecimal price;
	/**
	 * @Fields part_type : 零件类型
	 */
	@ApiModelProperty(name = "partType", notes = "零件类型")
	private Long partType;
	/**
	 * @Fields part_type_name : 制件类型名称
	 */
	@ExcelField(title = "制件类型", order = 4)
	@ApiModelProperty(name = "partTypeName", notes = "制件类型名称")
	private String partTypeName;
	/**
	 * @Fields department_id : 生产部门id
	 */
	@ApiModelProperty(name = "departmentId", notes = "生产部门id")
	private Long departmentId;
	/**
	 * @Fields department_name : 生产部门
	 */
	@ExcelField(title = "生产部门", order = 3)
	@ApiModelProperty(name = "departmentName", notes = "生产部门")
	private String departmentName;
	/**
	 * @Fields remark : 备注
	 */
	@ApiModelProperty(name = "remark", notes = "备注")
	private String remark;
	/**
	 * @Fields part_code : 制件编码
	 */
	@ExcelField(title = "制件编码", order = 1)
	@ApiModelProperty(name = "partCode", notes = "制件编码")
	private String partCode;
	
	
	private Integer partQty;
	
	
}

