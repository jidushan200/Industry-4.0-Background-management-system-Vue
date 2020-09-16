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
public class ToolBladeWarehouse implements Serializable {
	
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
	 * @Fields compose_number : 刀盘组合码
	 */
	@ApiModelProperty(name = "composeNumber", notes = "刀盘组合码")
	private String composeNumber;
	/**
	 * @Fields tool_number : 物料编码
	 */
	@ApiModelProperty(name = "toolNumber", notes = "物料编码")
	private String toolNumber;
	/**
	 * @Fields tool_name : 物料名称
	 */
	@ApiModelProperty(name = "toolName", notes = "物料名称")
	private String toolName;
	/**
	 * @Fields tool_map : 物料图号
	 */
	@ApiModelProperty(name = "toolMap", notes = "物料名称")
	private String toolMap;
	/**
	 * @Fields tool_qty : 入库数量
	 */
	@ApiModelProperty(name = "toolQty", notes = "入库数量")
	private Integer toolQty;
	/**
	 * @Fields is_new : 是否新刀(1-是;2-不是)
	 */
	@ApiModelProperty(name = "isNew", notes = "是否新刀(1-是;2-不是)")
	private Integer isNew;
	/**
	 * @Fields receipt_id : 收货单id
	 */
	@ApiModelProperty(name = "receiptId", notes = "收货单id")
	private Long receiptId;
	/**
	 * @Fields warehouse : 库位
	 */
	@ApiModelProperty(name = "warehouse", notes = "库位")
	private String warehouse;
	/**
	 * @Fields warehouse_type : 入库类型(1-新刀入库;2-刃磨入库)
	 */
	@ApiModelProperty(name = "warehouseType", notes = "入库类型(1-新刀入库;2-刃磨入库)")
	private Integer warehouseType;
	/**
	 * @Fields department_id : 部门id
	 */
	@ApiModelProperty(name = "departmentId", notes = "部门id")
	private Long departmentId;
	/**
	 * @Fields supplier_id : 供应商id
	 */
	@ApiModelProperty(name = "supplierId", notes = "供应商id")
	private Long supplierId;
	/**
	 * @Fields supplier_name : 供应商名称
	 */
	@ApiModelProperty(name = "supplierName", notes = "供应商名称")
	private String supplierName;
	/**
	 * @Fields deliever : 送货人
	 */
	@ApiModelProperty(name = "deliever", notes = "送货人")
	private String deliever;
	/**
	 * @Fields handle_result : 出来结果(0-待处理;1-已处理)
	 */
	@ApiModelProperty(name = "handleResult", notes = "出来结果(0-待处理;1-已处理)")
	private Integer handleResult;
	/**
	 * @Fields remark : 说明
	 */
	@ApiModelProperty(name = "remark", notes = "说明")
	private String remark;
	
	
}

