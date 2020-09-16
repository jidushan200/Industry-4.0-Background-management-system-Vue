package org.tsinghuatj.framework.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Collection;

@Data
@EqualsAndHashCode(callSuper = false)
public class Pagination<E> extends AjaxReturn {
	/**
	 * 
	 */

	@ApiModelProperty("总行数")
	private java.lang.Long total;
	@ApiModelProperty("数据行")
	private java.util.Collection<E> rows;
	@ApiModelProperty("当前页")
	private Integer currentPage;
	@ApiModelProperty("总页数")
	private java.lang.Long totalPage;

	public Pagination() {
		this.code = 200;
		this.info = null;
		this.data = null;
	}

	public Pagination(Long total, Collection<E> rows) {
		this.code = 200;
		this.info = null;
		this.data = null;
		this.total = total;
		this.rows = rows;
	}

}