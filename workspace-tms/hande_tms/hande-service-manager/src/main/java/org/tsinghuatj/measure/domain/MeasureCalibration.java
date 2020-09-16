package org.tsinghuatj.measure.domain;

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
public class MeasureCalibration implements Serializable {

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
	 * @Fields measure_Id : 量具ID
	 */
	@ApiModelProperty(name = "measureId", notes = "量具ID")
	private Long measureId;
	/**
	 * @Fields use_department_Id : 使用部门ID
	 */
	@ApiModelProperty(name = "useDepartmentId", notes = "使用部门ID")
	private Long useDepartmentId;
	/**
	 * @Fields use_department_Name : 使用部门名称
	 */
	@ApiModelProperty(name = "useDepartmentName", notes = "使用部门名称")
	private String useDepartmentName;
	/**
	 * @Fields use_team_id : 使用班组ID
	 */
	@ApiModelProperty(name = "useTeamId", notes = "使用班组ID")
	private Long useTeamId;
	/**
	 * @Fields use_team_name : 使用班组名称
	 */
	@ApiModelProperty(name = "useTeamName", notes = "使用班组名称")
	private String useTeamName;
	/**
	 * @Fields user_id : 使用人ID
	 */
	@ApiModelProperty(name = "userId", notes = "使用人ID")
	private Long userId;
	/**
	 * @Fields user_name : 使用人名称
	 */
	@ApiModelProperty(name = "userName", notes = "使用人名称")
	private String userName;
	/**
	 * @Fields manufacturer : 制造商
	 */
	@ApiModelProperty(name = "manufacturer", notes = "制造商")
	private String manufacturer;
	/**
	 * @Fields manufacturing_number : 出厂编号
	 */
	@ApiModelProperty(name = "manufacturingNumber", notes = "出厂编号")
	private String manufacturingNumber;
	/**
	 * @Fields Internal_number : 本厂编号
	 */
	@ApiModelProperty(name = "internalNumber", notes = "本厂编号")
	private String internalNumber;
	/**
	 * @Fields calibration_date : 校准日期
	 */
	@ApiModelProperty(name = "calibrationDate", notes = "校准日期")
	private Date calibrationDate;
	/**
	 * @Fields calibration_cycle : 校准周期
	 */
	@ApiModelProperty(name = "calibrationCycle", notes = "校准周期")
	private Integer calibrationCycle;
	/**
	 * @Fields expiry_date : 有效期至
	 */
	@ApiModelProperty(name = "expiryDate", notes = "有效期至")
	private Date expiryDate;
	/**
	 * @Fields calibration_result : 校准结论(1-使用中;2-在库;3-报废)
	 */
	@ApiModelProperty(name = "calibrationResult", notes = "校准结论(1-使用中;2-在库;3-报废)")
	private Integer calibrationResult;
	/**
	 * @Fields measure_status : 量具状态(1-在用;2-封存;3-报废)
	 */
	@ApiModelProperty(name = "measureStatus", notes = "量具状态(1-在用;2-封存;3-报废)")
	private Integer measureStatus;

	@Override
	public String toString() {
		return "MeasureCalibration " + "[pkId=" + pkId + "]" + ",[createUser=" + createUser + "]" + ",[createTime=" + createTime + "]" + ",[updateUser=" + updateUser + "]" + ",[updateTime=" + updateTime + "]" + ",[delMark=" + delMark + "]" + ",[measureId=" + measureId + "]" + ",[useDepartmentId=" + useDepartmentId + "]" + ",[useDepartmentName=" + useDepartmentName + "]" + ",[useTeamId=" + useTeamId + "]" + ",[useTeamName=" + useTeamName + "]" + ",[userId=" + userId + "]" + ",[userName=" + userName + "]" + ",[manufacturer=" + manufacturer + "]" + ",[manufacturingNumber=" + manufacturingNumber + "]" + ",[internalNumber=" + internalNumber + "]" + ",[calibrationDate=" + calibrationDate + "]" + ",[calibrationCycle="
				+ calibrationCycle + "]" + ",[expiryDate=" + expiryDate + "]" + ",[calibrationResult=" + calibrationResult + "]" + ",[measureStatus=" + measureStatus + "]";
	}
}
