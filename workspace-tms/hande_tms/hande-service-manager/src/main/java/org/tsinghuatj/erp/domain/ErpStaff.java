package org.tsinghuatj.erp.domain;

import java.io.Serializable;
import java.util.Date;

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
public class ErpStaff implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * @Fields staffName :
	 */
	@ApiModelProperty(name = "staffName", notes = "")
	private String staffName;

	/**
	 * @Fields staffNumber :
	 */
	@ApiModelProperty(name = "staffNumber", notes = "")
	private String staffNumber;

	/**
	 * @Fields SEX :
	 */
	@ApiModelProperty(name = "gender", notes = "")
	private String gender;

	/**
	 * @Fields departmentName :部门
	 */
	@ApiModelProperty(name = "departmentName", notes = "")
	private String departmentName;

	/**
	 * @Fields team :班组
	 */
	@ApiModelProperty(name = "team", notes = "")
	private String team;
	/**
	 * @Fields postion :职务
	 */
	@ApiModelProperty(name = "postion", notes = "")
	private String postion;

	/**
	 * @Fields MODEL_NUMBER : 最后更新时间
	 */
	@ApiModelProperty(name = "LAST_UPDATE_DATE", notes = "最后更新时间")
	private Date lastUpdateDate;

}
