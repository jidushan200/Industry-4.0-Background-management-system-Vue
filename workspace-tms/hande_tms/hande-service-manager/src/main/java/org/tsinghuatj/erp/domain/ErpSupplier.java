package org.tsinghuatj.erp.domain;

import java.io.Serializable;

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
public class ErpSupplier implements Serializable {
	private static final long serialVersionUID = 1L;


	/**
	 * @Fields supplierCode :
	 */
	@ApiModelProperty(name = "supplierCode", notes = "供应商编码")
	private String supplierCode;

	/**
	 * @Fields supplierName :
	 */
	@ApiModelProperty(name = "supplierName", notes = "供应商名称")
	private String supplierName;


}
