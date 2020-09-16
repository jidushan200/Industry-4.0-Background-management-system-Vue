package org.tsinghuatj.framework.domain;

import java.io.Serializable;

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
public class QueryDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9032647846361976219L;
	@ApiModelProperty("关键字")
	private java.lang.String keyword;
	@ApiModelProperty("页码")
	@Builder.Default
	private java.lang.Integer page = 1;
	@ApiModelProperty("行数")
	@Builder.Default
	private java.lang.Integer rows = 10;
	@ApiModelProperty("开始时间")
	private java.util.Date beginDate;
	@ApiModelProperty("结束时间")
	private java.util.Date endDate;
	@ApiModelProperty("排序")
	private java.lang.String sort;

}
