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
public class ErpDepartment implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * @Fields departmentCode :
	 */
	@ApiModelProperty(name = "departmentCode", notes = "")
	private String departmentCode;

	/**
	 * @Fields DESCRIPTION :
	 */
	@ApiModelProperty(name = "departmentName", notes = "")
	private String departmentName;


}
