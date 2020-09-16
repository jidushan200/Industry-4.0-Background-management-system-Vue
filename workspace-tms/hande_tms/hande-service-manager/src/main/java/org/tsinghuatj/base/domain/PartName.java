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
public class PartName implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * @Fields pk_id : 主键
	 */
	@ApiModelProperty(name = "pkId", notes = "主键")
	private Long pkId;
	/**
	 * @Fields part_type_name : 制件名称
	 */
	@ApiModelProperty(name = "partTypeName", notes = "制件名称")
	private String partTypeName;

	@Override
	public String toString() {
		return "PartName " + "[pkId=" + pkId + "]" + ",[partTypeName=" + partTypeName + "]";
	}
}
