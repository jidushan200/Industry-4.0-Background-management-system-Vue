package org.tsinghuatj.tool.domain;

import java.io.Serializable;
import java.math.BigDecimal;
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
public class ToolCheckResult implements Serializable {

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
	 * @Fields check_id : 检验ID
	 */
	@ApiModelProperty(name = "checkId", notes = "检验ID")
	private Long checkId;
	/**
	 * @Fields item_Id : 质检项ID
	 */
	@ApiModelProperty(name = "itemId", notes = "质检项ID")
	private Long itemId;
	/**
	 * @Fields check_item : 检验栏目
	 */
	@ApiModelProperty(name = "checkItem", notes = "检验栏目")
	private String checkItem;
	/**
	 * @Fields item_standard : 检验标准
	 */
	@ApiModelProperty(name = "itemStandard", notes = "检验标准")
	private String itemStandard;

	/**
	 * @Fields up_deviation : 上偏
	 */
	@ApiModelProperty(name = "upDeviation", notes = "上偏")
	private BigDecimal upDeviation;
	/**
	 * @Fields down_deviation : 下偏
	 */
	@ApiModelProperty(name = "downDeviation", notes = "下偏")
	private BigDecimal downDeviation;
	/**
	 * @Fields minimum : 最小值
	 */
	@ApiModelProperty(name = "minimum", notes = "最小值")
	private BigDecimal minimum;
	/**
	 * @Fields maximum : 最大值
	 */
	@ApiModelProperty(name = "maximum", notes = "最大值")
	private BigDecimal maximum;
	/**
	 * @Fields unit : 单位
	 */
	@ApiModelProperty(name = "unit", notes = "单位")
	private String unit;
	/**
	 * @Fields measured_value : 实测值
	 */
	@ApiModelProperty(name = "measuredValue", notes = "实测值")
	private String measuredValue;
	/**
	 * @Fields check_result : 检验结果(1-合格,2-合格)
	 */
	@ApiModelProperty(name = "checkResult", notes = "检验结果(1-合格,2-合格)")
	private Integer checkResult;

}
