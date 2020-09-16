package org.tsinghuatj.erp.domain;

import java.io.Serializable;
import java.math.BigDecimal;

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
public class ErpMaterial implements Serializable {
	private static final long serialVersionUID = 1L;


	/**
	 * @Fields item_code : 物料编码
	 */
	@ApiModelProperty(name = "item_code", notes = "物料编码")
	private String itemCode;

	/**
	 * @Fields item_name : 物料描述
	 */
	@ApiModelProperty(name = "item_name", notes = "物料描述")
	private String itemName;
	
	/**
	 * @Fields item_name : 物料类型
	 */
	@ApiModelProperty(name = "item_type", notes = "物料类型")
	private String itemType;
	
	/**
	 * @Fields item_price : 物料价格
	 */
	@ApiModelProperty(name = "item_price", notes = "物料价格")
	private BigDecimal itemPrice;
	/**
	 * @Fields sum_onhand_quantity : 库存现有量
	 */
	@ApiModelProperty(name = "sum_onhand_quantity", notes = "库存现有量")
	private Integer sumOnhandQuantity;
	
	/**
	 * @Fields sum_onhand_quantity : 待检数量
	 */
	@ApiModelProperty(name = "sum_nocheck_quantity", notes = "待检数量")
	private Integer sumNocheckQuantity;

}
