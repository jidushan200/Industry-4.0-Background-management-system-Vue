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
public class ToolBlade implements Serializable {

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
	 * @Fields department_id : 部门id
	 */
	@ApiModelProperty(name = "departmentId", notes = "部门id")
	private Long departmentId;
	/**
	 * @Fields tool_number : 物料编码
	 */
	@ExcelField(title = "刀条编码", order = 1)
	@ApiModelProperty(name = "toolNumber", notes = "物料编码")
	private String toolNumber;
	/**
	 * @Fields tool_name : 物料名称
	 */
	@ExcelField(title = "刀条名称", order = 2)
	@ApiModelProperty(name = "toolName", notes = "物料名称")
	private String toolName;
	/**
	 * @Fields tool_map : 物料图号
	 */
	@ExcelField(title = "刀条图号", order = 3)
	@ApiModelProperty(name = "toolMap", notes = "物料图号")
	private String toolMap;

	@ExcelField(title = "部门名称", order = 4)
	@ApiModelProperty(name = "departmentName", notes = "部门名称")
	private String departmentName;

	/**
	 * @Fields inventory_qty : 库存数量
	 */
	@ExcelField(title = "库存数量", order = 5)
	@ApiModelProperty(name = "inventoryQty", notes = "库存数量")
	private Integer inventoryQty;

	@ExcelField(title = "在用数量", order = 6)
	@ApiModelProperty(name = "useQty", notes = "在用数量")
	private Integer useQty;
	/**
	 * @Fields scrap_qty : 报废数量
	 */
	@ExcelField(title = "报废数量", order = 7)
	@ApiModelProperty(name = "scrapQty", notes = "报废数量")
	private Integer scrapQty;

}
