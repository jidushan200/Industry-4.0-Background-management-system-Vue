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
public class ErpEquipment implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * @Fields equipmentCode :
	 */
	@ApiModelProperty(name = "equipmentCode", notes = "")
	private String equipmentCode;

	/**
	 * @Fields equipmentName :
	 */
	@ApiModelProperty(name = "equipmentName", notes = "")
	private String equipmentName;

	/**
	 * @Fields TAG_NUMBER :
	 */
	@ApiModelProperty(name = "TAG_NUMBER", notes = "")
	private String tagNumber;

	/**
	 * @Fields MANUFACTURER_NAME :
	 */
	@ApiModelProperty(name = "MANUFACTURER_NAME", notes = "")
	private String manufacturerName;

	/**
	 * @Fields MODEL_NUMBER :
	 */
	@ApiModelProperty(name = "MODEL_NUMBER", notes = "")
	private String modelNumber;

	/**
	 * @Fields department_Code :
	 */
	@ApiModelProperty(name = "department_Code", notes = "")
	private String departmentCode;
	/**
	 * @Fields DISTRIBUTION_LOCATION :
	 */
	@ApiModelProperty(name = "DISTRIBUTION_LOCATION", notes = "")
	private String distributionLocation;

}
