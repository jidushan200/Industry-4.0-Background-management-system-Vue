package org.tsinghuatj.base.domain;

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
public class Equipment implements Serializable {

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
	 * @Fields equipment_code : 设备编码
	 */
	@ExcelField(title = "设备编码", order = 1)
	@ApiModelProperty(name = "equipmentCode", notes = "设备编码")
	private String equipmentCode;
	/**
	 * @Fields equipment_name : 设备名称
	 */
	@ExcelField(title = "设备名称", order = 2)
	@ApiModelProperty(name = "equipmentName", notes = "设备名称")
	private String equipmentName;
	/**
	 * @Fields department_id : 部门ID
	 */
	@ApiModelProperty(name = "departmentId", notes = "部门ID")
	private Long departmentId;
	/**
	 * @Fields department_name : 存放部门
	 */
	@ExcelField(title = "所属部门", order = 6)
	@ApiModelProperty(name = "departmentName", notes = "存放部门")
	private String departmentName;
	/**
	 * @Fields amount : 总数
	 */
	@ApiModelProperty(name = "amount", notes = "总数")
	private Integer amount;
	/**
	 * @Fields tag_number : 标签号
	 */
	@ExcelField(title = "标签号", order = 4)
	@ApiModelProperty(name = "tagNumber", notes = "标签号")
	private String tagNumber;
	/**
	 * @Fields manufacturer_name : 制造商
	 */
	@ExcelField(title = "制造商", order = 5)
	@ApiModelProperty(name = "manufacturerName", notes = "制造商")
	private String manufacturerName;
	/**
	 * @Fields model_number : 型号
	 */
	@ExcelField(title = "设备型号", order = 3)
	@ApiModelProperty(name = "modelNumber", notes = "型号")
	private String modelNumber;

	@Override
	public String toString() {
		return "Equipment " + "[pkId=" + pkId + "]" + ",[createUser=" + createUser + "]" + ",[createTime=" + createTime + "]" + ",[updateUser=" + updateUser + "]" + ",[updateTime=" + updateTime + "]" + ",[delMark=" + delMark + "]" + ",[equipmentCode=" + equipmentCode + "]" + ",[equipmentName=" + equipmentName + "]" + ",[departmentId=" + departmentId + "]" + ",[departmentName=" + departmentName + "]" + ",[amount=" + amount + "]" + ",[tagNumber=" + tagNumber + "]" + ",[manufacturerName=" + manufacturerName + "]" + ",[modelNumber=" + modelNumber + "]";
	}
}
