package org.tsinghuatj.tool.domain;

import java.io.Serializable;
import java.util.Date;

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
public class ToolBladeComposeDetail implements Serializable {
	
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
	 * @Fields compose_number : 组合编码
	 */
	@ApiModelProperty(name = "composeNumber", notes = "组合编码")
	private String composeNumber;
	/**
	 * @Fields tool_number : 物料编码
	 */
	@ApiModelProperty(name = "toolNumber", notes = "物料编码")
	private String toolNumber;
	
	/**
	 * @Fields tool_number : 物料名称
	 */
	@ApiModelProperty(name = "toolName", notes = "物料名称")
	private String toolName;
	
	/**
	 * @Fields tool_map : 物料图号
	 */
	@ApiModelProperty(name = "toolMap", notes = "物料图号")
	private String toolMap;
	/**
	 * @Fields tool_qty : 物料数量
	 */
	@ApiModelProperty(name = "toolQty", notes = "物料数量")
	private Integer toolQty;
	/**
	 * @Fields supplement_qty : 补充数量
	 */
	@ApiModelProperty(name = "supplementQty", notes = "补充数量")
	private Integer supplementQty;
	/**
	 * @Fields supplement_times : 补充次数
	 */
	@ApiModelProperty(name = "supplementTimes", notes = "补充次数")
	private Integer supplementTimes;
	
	/**
	 * @Fields repair_Measure : 刃磨量
	 */
	@ApiModelProperty(name = "repairMeasure", notes = "刃磨量")
	private Integer repairMeasure;
	
	/**
	 * @Fields remark : 备注
	 */
	@ApiModelProperty(name = "remark", notes = "备注")
	private String remark;
	
	/**
	 * @Fields tool_qty : 报废数量
	 */
	@ApiModelProperty(name = "scrapQty", notes = "报废数量")
	private Integer scrapQty;
	
	/**
	 * @Fields coat_qty : 送涂数量
	 */
	@ApiModelProperty(name = "coatQty", notes = "送涂数量")
	private Integer coatQty;
}

