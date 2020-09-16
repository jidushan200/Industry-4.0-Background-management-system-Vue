package org.tsinghuatj.fixture.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.tsinghuatj.tool.domain.ToolApplyAudit;

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
public class FixturePurchaseApply implements Serializable {

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
	 * @Fields fixture_number : 夹具编码
	 */
	@ApiModelProperty(name = "fixtureNumber", notes = "夹具编码")
	private String fixtureNumber;
	/**
	 * @Fields fixture_name : 夹具名称
	 */
	@ApiModelProperty(name = "fixtureName", notes = "夹具名称")
	private String fixtureName;
	/**
	 * @Fields fixture_map : 图号
	 */
	@ApiModelProperty(name = "fixtureMap", notes = "图号")
	private String fixtureMap;
	/**
	 * @Fields fixture_type : 夹具类型
	 */
	@ApiModelProperty(name = "fixtureType", notes = "夹具类型")
	private Integer fixtureType;
	/**
	 * @Fields part_Id : 制件
	 */
	@ApiModelProperty(name = "partId", notes = "制件")
	private Long partId;
	/**
	 * @Fields part_code : 制件编码
	 */
	@ApiModelProperty(name = "partCode", notes = "制件编码")
	private String partCode;
	/**
	 * @Fields part_name : 制件名称
	 */
	@ApiModelProperty(name = "partName", notes = "制件名称")
	private String partName;

	/**
	 * @Fields purchase_qty : 申购数量
	 */
	@ApiModelProperty(name = "purchaseQty", notes = "申购数量")
	private Integer purchaseQty;
	/**
	 * @Fields demand_time : 需求时间
	 */
	@ApiModelProperty(name = "demandTime", notes = "需求时间")
	private Date demandTime;
	/**
	 * @Fields purchase_type : 申购类型(1-新品开发;2-常用夹具)
	 */
	@ApiModelProperty(name = "purchaseType", notes = "申购类型(1-新品开发;2-常用夹具)")
	private Integer purchaseType;
	/**
	 * @Fields purchase_resion : 采购原因(1-产量提升；2-夹具报废；3-新品开发；4-其他)
	 */
	@ApiModelProperty(name = "purchaseResion", notes = "采购原因(1-产量提升；2-夹具报废；3-新品开发；4-其他)")
	private Integer purchaseResion;
	/**
	 * @Fields inventory_qty : 库存数量
	 */
	@ApiModelProperty(name = "inventoryQty", notes = "库存数量")
	private Integer inventoryQty;
	/**
	 * @Fields erp_qty : erp库存数量
	 */
	@ApiModelProperty(name = "erpQty", notes = "erp库存数量")
	private Integer erpQty;
	/**
	 * @Fields no_check_qty : erp待检数量
	 */
	@ApiModelProperty(name = "noCheckQty", notes = "erp待检数量")
	private Integer noCheckQty;
	/**
	 * @Fields apply_department_id : 申请部门ID
	 */
	@ApiModelProperty(name = "applyDepartmentId", notes = "申请部门ID")
	private Long applyDepartmentId;
	/**
	 * @Fields apply_department_name : 申请部门
	 */
	@ApiModelProperty(name = "applyDepartmentName", notes = "申请部门")
	private String applyDepartmentName;
	/**
	 * @Fields applier_id : 申请人ID
	 */
	@ApiModelProperty(name = "applierId", notes = "申请人ID")
	private Long applierId;
	/**
	 * @Fields applier_name : 申请人
	 */
	@ApiModelProperty(name = "applierName", notes = "申请人")
	private String applierName;
	/**
	 * @Fields apply_time : 申请时间
	 */
	@ApiModelProperty(name = "applyTime", notes = "申请时间")
	private Date applyTime;
	/**
	 * @Fields apply_status : 申请状态(0-新增待提交；1-待部门领导审核；-1-部门领导已驳回；2-待(工艺部/研究所)审核；)
	 */
	@ApiModelProperty(name = "applyStatus", notes = "申请状态(0-新增待提交；1-待部门领导审核；-1-部门领导已驳回;2-待(工艺部/研究所)审核;3-装备分厂审核;4-待装备分厂接收;5-待采购询价;6-工艺部图纸会签;7-待主管领导审核;8-待采购接收;9-采购已接收;10-装备分厂已接收;11-完成)")
	private Integer applyStatus;

	private List<Integer> statusList;

	/**
	 * @Fields procurement_Type : 采购类型(1-自制;2-外购)
	 */
	@ApiModelProperty(name = "procurementType", notes = "采购类型(1-自制;2-外购)")
	private Integer procurementType;

	/**
	 * @Fields amount : 金额
	 */
	@ApiModelProperty(name = "amount", notes = "金额")
	private BigDecimal amount;
	/**
	 * @Fields remark : 备注
	 */
	@ApiModelProperty(name = "remark", notes = "备注")
	private String remark;

	/**
	 * @Fields available_number : 可用顺序号
	 */
	@ApiModelProperty(name = "availableNumber", notes = "可用顺序号")
	private String availableNumber;

	/**
	 * @Fields finish_time : 采购/自制完成时间
	 */
	@ApiModelProperty(name = "finishTime", notes = "采购完成时间")
	private Date finishTime;

	/**
	 * @Fields arrived_qty : 到货数量
	 */
	@ApiModelProperty(name = "arrivedQty", notes = "到货数量")
	private Integer arrivedQty;
	/**
	 * 申请部门
	 */
	List<Long> departmentIdList;

	/**
	 * 审核日志
	 */
	List<ToolApplyAudit> auditList;
	/**
	 * 收货
	 */
	List<FixturePurchaseReceipt> receiptList;

	// 夹具配件
	private List<FixturePurchaseApplyDetail> detailList;

}
