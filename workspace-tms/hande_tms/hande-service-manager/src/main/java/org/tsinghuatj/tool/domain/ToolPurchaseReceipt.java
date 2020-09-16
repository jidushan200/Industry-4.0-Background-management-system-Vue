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
public class ToolPurchaseReceipt implements Serializable {

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
	 * @Fields apply_id : 采购单ID
	 */
	@ApiModelProperty(name = "applyId", notes = "采购单ID")
	private Long applyId;
	/**
	 * @Fields tool_number : 物料编码
	 */
	@ApiModelProperty(name = "toolNumber", notes = "物料编码")
	private String toolNumber;
	/**
	 * @Fields tool_map : 图号
	 */
	@ApiModelProperty(name = "toolMap", notes = "图号")
	private String toolMap;
	/**
	 * @Fields tool_name : 物料名称
	 */
	@ApiModelProperty(name = "toolName", notes = "物料名称")
	private String toolName;
	/**
	 * @Fields tool_qty : 物料数量
	 */
	@ApiModelProperty(name = "toolQty", notes = "物料数量")
	private Integer toolQty;
	/**
	 * @Fields tool_seq : 顺序号
	 */
	@ApiModelProperty(name = "sequenceNumber", notes = "顺序号")
	private String sequenceNumber;
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
	/**
	 * @Fields deliverer : 送货人
	 */
	@ApiModelProperty(name = "deliverer", notes = "送货人")
	private String deliverer;
	/**
	 * @Fields delivery_time : 送货时间
	 */
	@ApiModelProperty(name = "deliveryTime", notes = "送货时间")
	private Date deliveryTime;
	/**
	 * @Fields wait_check_id : 待检ID
	 */
	@ApiModelProperty(name = "waitCheckId", notes = "待检ID")
	private Long waitCheckId;

	/**
	 * @Fields check_status : 检验状态(0-待检;1-检验中;2-检验完成)
	 */
	@ApiModelProperty(name = "checkStatus", notes = "检验状态(0-待检;1-检验中;2-检验完成)")
	private Integer checkStatus;
	
	
	private String createUserName;

	/*@Override
	public boolean equals(Object o) {
		ToolPurchaseReceipt item = (ToolPurchaseReceipt) o;
		return pkId.equals(item.getPkId());
	}*/

}
