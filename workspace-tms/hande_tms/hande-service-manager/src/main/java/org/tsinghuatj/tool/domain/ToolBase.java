package org.tsinghuatj.tool.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
public class ToolBase implements Serializable {

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
	 * @Fields tool_number : 物料编码
	 */
	@ExcelField(title = "物料编码", order = 1)
	@ApiModelProperty(name = "toolNumber", notes = "物料编码")
	private String toolNumber;
	/**
	 * @Fields tool_name : toolName
	 */
	@ExcelField(title = "物料名称", order = 2)
	@ApiModelProperty(name = "toolName", notes = "物料名称")
	private String toolName;
	/**
	 * @Fields tool_map : 刀具图号
	 */
	@ExcelField(title = "物料图号", order = 3)
	@ApiModelProperty(name = "toolMap", notes = "物料图号")
	private String toolMap;
	/**
	 * @Fields process_total : 理论加工数量
	 */

	@ApiModelProperty(name = "processTotal", notes = "理论加工数量")
	private Integer processTotal;
	/**
	 * @Fields grinding_max : 最大刃磨量
	 */
	@ExcelField(title = "最大刃磨量", order = 9)
	@ApiModelProperty(name = "grindingMax", notes = "最大刃磨量")
	private BigDecimal grindingMax;
	/**
	 * @Fields grinding_cordon : 刃磨警戒线
	 */
	@ExcelField(title = "预警刃磨量", order = 11)
	@ApiModelProperty(name = "grindingCordon", notes = "刃磨警戒线")
	private BigDecimal grindingCordon;
	/**
	 * @Fields price : 价格
	 */
	@ExcelField(title = "价格", order = 10)
	@ApiModelProperty(name = "price", notes = "价格")
	private BigDecimal price;
	/**
	 * @Fields tool_type : 刀具类型
	 */
	@ApiModelProperty(name = "typeId", notes = "刀具类型")
	private Integer typeId;
	/**
	 * @Fields tool_type : 刀具类型
	 */
	@ExcelField(title = "物料类型", order = 4)
	@ApiModelProperty(name = "typeName", notes = "刀具类型")
	private String typeName;

	/**
	 * @Fields process_each : 每次还刀加工数
	 */
	@ExcelField(title = "每次还刀加工数", order = 5)
	@ApiModelProperty(name = "processEach", notes = "每次还刀加工数")
	private Integer processEach;

	/**
	 * @Fields process_each_min : 每次还刀最小加工数
	 */
	@ExcelField(title = "每次还刀加工最小数", order = 6)
	@ApiModelProperty(name = "processEachMin", notes = "每次还刀最小加工数")
	private Integer processEachMin;

	/**
	 * @Fields process_each_max : 每次还刀加工最小数
	 */
	@ExcelField(title = "每次还刀最大加工数", order = 7)
	@ApiModelProperty(name = "processEachMax", notes = "每次还刀最大加工数")
	private Integer processEachMax;

	/**
	 * @Fields available_times : 可加工次数
	 */
	@ExcelField(title = "可加工次数", order = 8)
	@ApiModelProperty(name = "availableTimes", notes = "可加工次数")
	private Integer availableTimes;

	/**
	 * @Fields supplier_id : 刀具数量
	 */
	@ApiModelProperty(name = "toolAmount", notes = "刀具数量")
	private Integer toolAmount;

	/**
	 * @Fields supplier_id : ERP数量
	 */
	@ApiModelProperty(name = "erpAmount", notes = "ERP数量")
	private Integer erpAmount;

	/**
	 * @Fields noCheckQty : ERP待检数量
	 */
	@ApiModelProperty(name = "noCheckQty", notes = "ERP待检数量")
	private Integer noCheckQty;

	/**
	 * @Fields noCheckQty : 刀具制件列表
	 */
	@ApiModelProperty(name = "toolPartList", notes = "刀具制件列表")
	private List<ToolPart> toolPartList;

}
