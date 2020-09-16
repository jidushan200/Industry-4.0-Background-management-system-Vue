package org.tsinghuatj.tool.domain;

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
public class ToolHeadBlade implements Serializable {

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
	 * @Fields head_number : 刀条组合编号
	 */
	@ExcelField(title = "刀条组合编号", order = 1)
	@ApiModelProperty(name = "headNumber", notes = "刀条组合编号")
	private String headNumber;
	/**
	 * @Fields head_name : 刀条组合名称
	 */
	@ApiModelProperty(name = "headName", notes = "刀条组合名称")
	private String headName;
	/**
	 * @Fields tool_number : 刀条编号
	 */
	@ExcelField(title = "刀条编号", order = 2)
	@ApiModelProperty(name = "toolNumber", notes = "刀条编号")
	private String toolNumber;
	/**
	 * @Fields tool_name : 刀条名称
	 */
	@ExcelField(title = "刀条名称", order = 3)
	@ApiModelProperty(name = "toolName", notes = "刀条名称")
	private String toolName;

	/**
	 * @Fields tool_map : 刀条图号
	 */
	@ExcelField(title = "刀条图号", order = 4)
	@ApiModelProperty(name = "toolMap", notes = "刀条图号")
	private String toolMap;

	/**
	 * @Fields tool_qty : 刀条数量
	 */
	@ExcelField(title = "刀条数量", order = 5)
	@ApiModelProperty(name = "toolQty", notes = "刀条数量")
	private Integer toolQty;

	/**
	 * @Fields each_process_qty : 每次加工数
	 */
	@ExcelField(title = "每次加工数", order = 6)
	@ApiModelProperty(name = "eachProcessQty", notes = "每次加工数")
	private Integer eachProcessQty;

	/**
	 * @Fields process_times : 加工次数
	 */
	@ExcelField(title = "加工次数", order = 7)
	@ApiModelProperty(name = "processTimes", notes = "加工次数")
	private Integer processTimes;

	@ApiModelProperty(name = "grindingCordon", notes = "预警刃磨量")
	private BigDecimal grindingCordon;

	@ApiModelProperty(name = "grindingMax", notes = "最大刃磨量")
	private BigDecimal grindingMax;

	/**
	 * @Fields inventory_qty : 库存数量
	 */
	@ApiModelProperty(name = "inventoryQty", notes = "库存数量")
	private Integer inventoryQty;

}
