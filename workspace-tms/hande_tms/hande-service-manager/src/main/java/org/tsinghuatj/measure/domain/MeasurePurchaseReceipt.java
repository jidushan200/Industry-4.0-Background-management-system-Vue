package org.tsinghuatj.measure.domain;

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
public class MeasurePurchaseReceipt implements Serializable {
	
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
	 * @Fields measure_number : 物料编码
	 */
	@ApiModelProperty(name = "measureNumber", notes = "物料编码")
	private String measureNumber;
	/**
	 * @Fields model : 图号
	 */
	@ApiModelProperty(name = "model", notes = "图号")
	private String model;
	/**
	 * @Fields measure_name : 物料名称
	 */
	@ApiModelProperty(name = "measureName", notes = "物料名称")
	private String measureName;
	/**
	 * @Fields sequence_number : 顺序号
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
	
	@Override
    public String toString() {
        return  "MeasurePurchaseReceipt " + 
                    "[pkId=" + pkId + "]"
                   + ",[createUser=" + createUser + "]"
                                   + ",[createTime=" + createTime + "]"
                                   + ",[updateUser=" + updateUser + "]"
                                   + ",[updateTime=" + updateTime + "]"
                                   + ",[delMark=" + delMark + "]"
                                   + ",[applyId=" + applyId + "]"
                                   + ",[measureNumber=" + measureNumber + "]"
                                   + ",[model=" + model + "]"
                                   + ",[measureName=" + measureName + "]"
                                   + ",[sequenceNumber=" + sequenceNumber + "]"
                                   + ",[supplierId=" + supplierId + "]"
                                   + ",[supplierName=" + supplierName + "]"
                                   + ",[deliverer=" + deliverer + "]"
                                   + ",[deliveryTime=" + deliveryTime + "]"
                ;
	}
}

