package org.tsinghuatj.tool.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

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
public class ToolCoat implements Serializable {

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
	 * @Fields tool_id : 刀具ID
	 */
	@ApiModelProperty(name = "toolId", notes = "刀具ID")
	private Long toolId;
	/**
	 * @Fields full_number : 刀具码
	 */
	@ApiModelProperty(name = "fullNumber", notes = "刀具码")
	private String fullNumber;
	/**
	 * @Fields tool_number : 刀具编码
	 */
	@ApiModelProperty(name = "toolNumber", notes = "刀具编码")
	private String toolNumber;
	@ApiModelProperty(name = "toolName", notes = "刀具名称")
	private String toolName;
	@ApiModelProperty(name = "toolMap", notes = "刀具图号")
	private String toolMap;
	/**
	 * @Fields tool_Qty : 数量
	 */
	@ApiModelProperty(name = "toolQty", notes = "数量")
	private Integer toolQty;
	/**
	 * @Fields type_Id : 刀具类型
	 */
	@ApiModelProperty(name = "typeId", notes = "刀具类型")
	private Integer typeId;
	
	/**
	 * @Fields outbound_user_id : 涂层出库人
	 */
	@ApiModelProperty(name = "outboundUserId", notes = "刀具ID")
	private Long outboundUserId;
	/**
	 * @Fields outboundUserName : 涂层出库人
	 */
	@ApiModelProperty(name = "outboundUserName", notes = "涂层出库人")
	private String outboundUserName;
	/**
	 * @Fields outbound_time : 涂层出库时间
	 */
	@ApiModelProperty(name = "outboundTime", notes = "涂层出库时间")
	private Date outboundTime;
	
	/**
	 * @Fields coat_seq : 涂层次序
	 */
	@ApiModelProperty(name = "coatSeq", notes = "涂层次序")
	private Integer coatSeq;
	/**
	 * @Fields coat_supplier : 涂层厂商
	 */
	@ApiModelProperty(name = "coatSupplier", notes = "涂层厂商")
	private String coatSupplier;
	/**
	 * @Fields coat_time : 涂层时间
	 */
	@ApiModelProperty(name = "coatTime", notes = "涂层时间")
	private Date coatTime;
	/**
	 * @Fields remark : 备注
	 */
	@ApiModelProperty(name = "remark", notes = "备注")
	private String remark;
	/**
	 * @Fields supplier_id : supplierId
	 */
	@ApiModelProperty(name = "supplierId", notes = "supplierId")
	private Long supplierId;
	/**
	 * @Fields deliever : deliever
	 */
	@ApiModelProperty(name = "deliever", notes = "deliever")
	private String deliever;

	/**
	 * @Fields coat_price : 涂层价格
	 */
	@ApiModelProperty(name = "coatPrice", notes = "涂层价格")
	private BigDecimal coatPrice;

	/**
	 * @Fields check_remark : 检验备注
	 */
	@ApiModelProperty(name = "checkRemark", notes = "检验备注")
	private String checkRemark;

	/**
	 * @Fields repair_remark : 刃磨备注
	 */
	@ApiModelProperty(name = "repairRemark", notes = "刃磨备注")
	private String repairRemark;

	/**
	 * @Fields technology_remark : 工艺备注
	 */
	@ApiModelProperty(name = "technologyRemark", notes = "工艺备注")
	private String technologyRemark;

	/**
	 * @Fields settlementTime : 结算时间
	 */
	@ApiModelProperty(name = "settlementTime", notes = "更新时间")
	private Date settlementTime;

	/**
	 * @Fields settlement_id : settlementId
	 */
	@ApiModelProperty(name = "settlementId", notes = "结算人id")
	private Long settlementId;

	/**
	 * @Fields settlementName : 结算人姓名
	 */
	@ApiModelProperty(name = "settlementName", notes = "结算人姓名")
	private String settlementName;

	/**
	 * @Fields process_Qty : 加工数量
	 */
	@ApiModelProperty(name = "processQty", notes = "加工数量")
	private Integer processQty;

	/**
	 * @Fields process_Qty : 理论加工数量
	 */
	@ApiModelProperty(name = "theoreticalQty", notes = "理论加工数量")
	private Integer theoreticalQty;

	/**
	 * @Fields completion_degree : 完成度
	 */
	@ApiModelProperty(name = "completionDegree", notes = "完成度=加工数量/理论加工数量")
	private BigDecimal completionDegree;
	
	/**
	 * @Fields degree_min : 完成度
	 */
	@ApiModelProperty(name = "degreeMin", notes = "完成度")
	private BigDecimal degreeMin;
	/**
	 * @Fields degree_min : 完成度
	 */
	@ApiModelProperty(name = "degreeMin", notes = "完成度")
	private BigDecimal degreeMax;

	/**
	 * @Fields settlement_price : 结算单价
	 */
	@ApiModelProperty(name = "settlementPrice", notes = "结算单价 手工输入")
	private BigDecimal settlementPrice;

	/**
	 * @Fields settlementQty : 结算数量
	 */
	@ApiModelProperty(name = "settlementQty", notes = "结算数量手工输入")
	private Integer settlementQty;
	
	
	/**
	 * @Fields settlementStatus : 结算状态
	 */
	@ApiModelProperty(name = "settlementStatus", notes = "结算状态(0-未结算;1-已结算)")
	private Integer settlementStatus;

	/**
	 * @Fields settlement_amount : 结算总价
	 */
	@ApiModelProperty(name = "settlementAmount", notes = "结算总价=结算单价*结算数量")
	private BigDecimal settlementAmount;

	/**
	 * @Fields pay_price : 支付单价
	 */
	@ApiModelProperty(name = "payPrice", notes = "支付单价=结算单价*结算系数")
	private BigDecimal payPrice;

	/**
	 * @Fields pay_amount : 支付总价
	 */
	@ApiModelProperty(name = "payAmount", notes = "支付总价=结算单价*结算系数*结算数量")
	private BigDecimal payAmount;
	
	
	@ApiModelProperty(name = "beginTime", notes = "结算开始时间")
	private Date beginTime;
	
	@ApiModelProperty(name = "endTime", notes = "结算结束时间")
	private Date endTime;
}
