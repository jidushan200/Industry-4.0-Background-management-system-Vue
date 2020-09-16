package org.tsinghuatj.base.domain;

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
public class Material implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(name = "pkId", notes = "物料Id")
	private Long pkId;
	/**
	 * @Fields tool_number : 物料编码
	 */

	@ApiModelProperty(name = "materialNumber", notes = "物料编码")
	private String materialNumber;
	/**
	 * @Fields tool_name : toolName
	 */

	@ApiModelProperty(name = "materialName", notes = "物料名称")
	private String materialName;
	/**
	 * @Fields tool_map : 刀具图号
	 */
	
	@ApiModelProperty(name = "materialMap", notes = "物料图号")
	private String materialMap;
}
