package org.tsinghuatj.tool.domain;

import java.io.Serializable;
import java.math.BigDecimal;
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
public class ToolRepairPrice implements Serializable {
	
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
	 * @Fields tool_id : 物料id
	 */
	@ApiModelProperty(name = "toolId", notes = "物料id")
	private Long toolId;
	/**
	 * @Fields tool_number : 刀具编码
	 */
	@ExcelField(title = "物料编码", order = 1)
	@ApiModelProperty(name = "toolNumber", notes = "物料编码")
	private String toolNumber;

	/**
	 * @Fields tool_name : 刀具名称
	 */
	@ExcelField(title = "物料名称", order = 2)
	@ApiModelProperty(name = "toolName", notes = "物料名称")
	private String toolName;
	/**
	 * @Fields supplier_id : 供应商ID
	 */
	@ApiModelProperty(name = "supplierId", notes = "供应商ID")
	private Long supplierId;
	/**
	 * @Fields supplier_code : 供应商编码
	 */
	@ExcelField(title = "供应商编码", order = 3)
	@ApiModelProperty(name = "supplierCode", notes = "供应商编码")
	private String supplierCode;
	/**
	 * @Fields supplier_name : 供应商名称
	 */
	@ExcelField(title = "供应商名称", order = 4)
	@ApiModelProperty(name = "supplierName", notes = "供应商名称")
	private String supplierName;
	/**
	 * @Fields price : 价格
	 */
	@ExcelField(title = "价格", order = 5)
	@ApiModelProperty(name = "price", notes = "价格")
	private BigDecimal price;
}

