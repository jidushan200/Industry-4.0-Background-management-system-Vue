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
public class CheckStandardItem implements Serializable {

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
	 * @Fields standard_id : 标准编码
	 */
	@ApiModelProperty(name = "standardId", notes = "标准编码")
	private Long standardId;
	/**
	 * @Fields check_item : 检验项
	 */
	@ExcelField(title = "质检项", order = 1)
	@ApiModelProperty(name = "checkItem", notes = "检验项")
	private String checkItem;
	/**
	 * @Fields item_standard : 质检项标准
	 */
	@ExcelField(title = "标准值", order = 2)
	@ApiModelProperty(name = "itemStandard", notes = "质检项标准")
	private String itemStandard;
	/**
	 * @Fields up_deviation : 上偏
	 */
	@ExcelField(title = "上偏差值", order = 3)
	@ApiModelProperty(name = "upDeviation", notes = "上偏")
	private BigDecimal upDeviation;
	/**
	 * @Fields down_deviation : 下偏
	 */
	@ExcelField(title = "下偏差值", order = 4)
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
	@ExcelField(title = "单位", order = 5)
	@ApiModelProperty(name = "unit", notes = "单位")
	private String unit;
	/**
	 * @Fields check_type : 检验类型（1-新刀检验；2-刃磨检验；3-涂层检验）
	 */
	@ApiModelProperty(name = "checkType", notes = "检验类型（1-新刀检验；2-刃磨检验；3-涂层检验）")
	private Integer checkType;
}
