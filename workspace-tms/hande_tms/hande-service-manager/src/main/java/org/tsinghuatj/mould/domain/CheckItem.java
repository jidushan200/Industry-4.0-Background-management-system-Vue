package org.tsinghuatj.mould.domain;

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
public class CheckItem implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * @Fields item_seq : 顺序号
	 */
	@ApiModelProperty(name = "leftitemSeq", notes = " 顺序号")
	private Integer litemSeq;

	/**
	 * @Fields check_item : 检验项
	 */
	@ApiModelProperty(name = "leftcheckItem", notes = "检验项")
	private String lcheckItem;
	
	/**
	 * @Fields measured_value : 实测值
	 */
	@ApiModelProperty(name = "measuredValue", notes = "实测值")
	private String lmeasuredValue;
	
	/**
	 * @Fields check_result : 检验结果
	 */
	@ApiModelProperty(name = "leftcheckResult", notes = "检验结果")
	private Integer lcheckResult;
	
	/**
	 * @Fields remark : 备注
	 */
	@ApiModelProperty(name = "leftremark", notes = "备注")
	private String lremark;
	
	
	/**
	 * @Fields item_seq : 顺序号
	 */
	@ApiModelProperty(name = "ritemSeq", notes = " 顺序号")
	private Integer ritemSeq;

	/**
	 * @Fields check_item : 检验项
	 */
	@ApiModelProperty(name = "rcheckItem", notes = "检验项")
	private String rcheckItem;
	
	/**
	 * @Fields measured_value : 实测值
	 */
	@ApiModelProperty(name = "rmeasuredValue", notes = "实测值")
	private String rmeasuredValue;
	
	/**
	 * @Fields check_result : 检验结果
	 */
	@ApiModelProperty(name = "rcheckResult", notes = "检验结果")
	private Integer rcheckResult;
	
	/**
	 * @Fields remark : 备注
	 */
	@ApiModelProperty(name = "rremark", notes = "备注")
	private String rremark;
}
