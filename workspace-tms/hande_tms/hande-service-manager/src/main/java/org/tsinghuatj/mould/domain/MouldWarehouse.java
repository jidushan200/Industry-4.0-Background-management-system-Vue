package org.tsinghuatj.mould.domain;

import java.io.Serializable;
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
public class MouldWarehouse implements Serializable {
	
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
	 * @Fields mould_id : 模具ID
	 */
	@ApiModelProperty(name = "mouldId", notes = "模具ID")
	private Long mouldId;
	/**
	 * @Fields mould_name : 模具名称
	 */
	@ApiModelProperty(name = "mouldName", notes = "模具名称")
	private String mouldName;
	/**
	 * @Fields mould_number : 模具编码
	 */
	@ApiModelProperty(name = "mouldNumber", notes = "模具编码")
	private String mouldNumber;
	/**
	 * @Fields warehouse_code : 入库编码
	 */
	@ApiModelProperty(name = "warehouseCode", notes = "入库编码")
	private String warehouseCode;
	/**
	 * @Fields mould_map : 模具图号
	 */
	@ApiModelProperty(name = "mouldMap", notes = "模具图号")
	private String mouldMap;
	/**
	 * @Fields full_number : 模具条码
	 */
	@ApiModelProperty(name = "fullNumber", notes = "模具条码")
	private String fullNumber;
	/**
	 * @Fields in_type : 入库类型(1-新模具入库；2-用后返库；3-检后返库；)
	 */
	@ApiModelProperty(name = "inType", notes = "入库类型(1-新模具入库；2-用后返库；3-检后返库；)")
	private Integer inType;
	/**
	 * @Fields keeper_id : 库管员id
	 */
	@ApiModelProperty(name = "keeperId", notes = "库管员id")
	private Long keeperId;
	/**
	 * @Fields keeper : 库管员
	 */
	@ApiModelProperty(name = "keeper", notes = "库管员")
	private String keeper;
	/**
	 * @Fields remark : 说明
	 */
	@ApiModelProperty(name = "remark", notes = "说明")
	private String remark;
	/**
	 * @Fields warehouse : 库位
	 */
	@ApiModelProperty(name = "warehouse", notes = "库位")
	private String warehouse;
	/**
	 * @Fields warehosing_time : 入库时间
	 */
	@ApiModelProperty(name = "warehosingTime", notes = "入库时间")
	private Date warehosingTime;
	/**
	 * @Fields return_resion : 返回原因(1-正常交回；2-异常交回；3-检后返库；)
	 */
	@ApiModelProperty(name = "returnResion", notes = "返回原因(1-正常交回；2-异常交回；3-检后返库；)")
	private Integer returnResion;
	
	
}

