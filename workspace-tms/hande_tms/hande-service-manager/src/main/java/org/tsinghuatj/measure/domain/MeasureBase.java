package org.tsinghuatj.measure.domain;

import java.io.Serializable;
import java.util.Date;

import org.tsinghuatj.framework.utils.excel.annotation.ExcelField;

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
public class MeasureBase implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * @Fields pk_id : 主键
	 */
	@ApiModelProperty(name = "pkId", notes = "主键")
	private Long pkId;
	/**
	 * @Fields create_user : 创建用户
	 */
	@ApiModelProperty(name = "createUser", notes = "创建用户")
	private Long createUser;
	/**
	 * @Fields create_time : 创建时间
	 */
	@ApiModelProperty(name = "createTime", notes = "创建时间")
	private Date createTime;
	/**
	 * @Fields update_user : 更新用户
	 */
	@ApiModelProperty(name = "updateUser", notes = "更新用户")
	private Long updateUser;
	/**
	 * @Fields update_time : 更新时间
	 */
	@ApiModelProperty(name = "updateTime", notes = "更新时间")
	private Date updateTime;
	/**
	 * @Fields del_mark : 删除标识(--0正常--1已删除)
	 */
	@ApiModelProperty(name = "delMark", notes = "删除标识(--0正常--1已删除)")
	private Integer delMark;
	/**
	 * @Fields create_user_name : 创建用户姓名
	 */
	@ApiModelProperty(name = "createUserName", notes = "创建用户姓名")
	private String createUserName;	
	/**
	 * @Fields update_user_name : 更新用户姓名
	 */
	@ApiModelProperty(name = "updateUserName", notes = "创建用户姓名")
	private String updateUserName;
	/**
	 * @Fields measure_number : 量具编码
	 */
	@ExcelField(title = "量具编码", order = 1)
	@ApiModelProperty(name = "measureNumber", notes = "量具编码")
	private String measureNumber;
	/**
	 * @Fields measure_name : 名称规格
	 */
	@ExcelField(title = "量具名称", order = 2)
	@ApiModelProperty(name = "measureName", notes = "量具名称")
	private String measureName;
	/**
	 * @Fields model : 规格型号
	 */
	@ExcelField(title = "规格型号", order = 3)
	@ApiModelProperty(name = "model", notes = "规格型号")
	private String model;
	
	/**
	 * @Fields measureAmount : TMS库存
	 */
	@ApiModelProperty(name = "measureAmount", notes = "TMS库存")
	private Integer measureAmount;
	
	/**
	 * @Fields measureAmount : ERP库存
	 */
	@ApiModelProperty(name = "erpAmount", notes = "ERP库存")
	private Integer erpAmount;
	
	/**
	 * @Fields measureAmount : ERP待检
	 */
	@ApiModelProperty(name = "noCheckQty", notes = "ERP待检")
	private Integer noCheckQty;

	@Override
	public String toString() {
		return "MeasureBase " + "[pkId=" + pkId + "]" + ",[createUser=" + createUser + "]" + ",[createTime=" + createTime + "]" + ",[updateUser=" + updateUser + "]" + ",[updateTime=" + updateTime + "]" + ",[delMark=" + delMark + "]" + ",[measureNumber=" + measureNumber + "]" + ",[measureName=" + measureName + "]" + ",[model=" + model + "]";
	}
}
