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
public class ToolRepair implements Serializable {

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
	 * @Fields repair_seq : 刃磨次序
	 */
	@ApiModelProperty(name = "repairSeq", notes = "刃磨次序")
	private Integer repairSeq;
	/**
	 * @Fields repair_measure : 刃磨量
	 */
	@ApiModelProperty(name = "repairMeasure", notes = "刃磨量")
	private BigDecimal repairMeasure;
	/**
	 * @Fields executor : 刃磨人
	 */
	@ApiModelProperty(name = "executor", notes = "刃磨人")
	private String executor;
	/**
	 * @Fields execut_time : 刃磨时间
	 */
	@ApiModelProperty(name = "executTime", notes = "刃磨时间")
	private Date executTime;
	/**
	 * @Fields remark : 备注
	 */
	@ApiModelProperty(name = "remark", notes = "备注")
	private String remark;

	/**
	 * @Fields typeId : 刀具类型
	 */
	@ApiModelProperty(name = "typeId", notes = "刀具类型")
	private Integer typeId;
	/**
	 * @Fields repair_price : 修磨价格
	 */
	@ApiModelProperty(name = "repairPrice", notes = "修磨价格")
	private BigDecimal repairPrice;
	/**
	 * @Fields supplier_id : 供应商ID
	 */
	@ApiModelProperty(name = "supplierId", notes = "供应商ID")
	private Long supplierId;
	/**
	 * @Fields supplier_name : 供应商名称
	 */
	@ApiModelProperty(name = "supplierName", notes = "供应商名称")
	private String supplierName;
}
