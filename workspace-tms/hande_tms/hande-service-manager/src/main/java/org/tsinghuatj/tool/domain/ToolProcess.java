package org.tsinghuatj.tool.domain;

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
public class ToolProcess implements Serializable {
	
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
	@ExcelField(title = "生产时间", order = 9)
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
	 * @Fields tool_id : 刀具ID
	 */
	@ApiModelProperty(name = "toolId", notes = "刀具ID")
	private Long toolId;
	/**
	 * @Fields tool_number : 物料编码
	 */
	@ApiModelProperty(name = "toolNumber", notes = "物料编码")
	private String toolNumber;
	
	/**
	 * @Fields full_number : 物料条码
	 */
	@ExcelField(title = "物料条码", order = 1)
	@ApiModelProperty(name = "fullNumber", notes = "物料条码")
	private String fullNumber;
	/**
	 * @Fields tool_name : 物料名称
	 */
	@ExcelField(title = "物料名称", order = 2)
	@ApiModelProperty(name = "toolName", notes = "物料名称")
	private String toolName;
	/**
	 * @Fields tool_map : 物料图号
	 */
	@ExcelField(title = "物料图号", order = 3)
	@ApiModelProperty(name = "toolMap", notes = "物料图号")
	private String toolMap;
	
	/**
	 * @Fields type_Id : 物料类型
	 */
	@ApiModelProperty(name = "typeId", notes = "物料类型")
	private Integer typeId;
	/**
	 * @Fields equipment_code : 设备编码
	 */
	@ApiModelProperty(name = "equipmentCode", notes = "设备编码")
	private String equipmentCode;
	/**
	 * @Fields tagNumber : 设备标签
	 */
	@ApiModelProperty(name = "tagNumber", notes = "设备标签")
	private String tagNumber;
	/**
	 * @Fields equipment_name : 设备名称
	 */
	@ApiModelProperty(name = "equipmentName", notes = "设备名称")
	private String equipmentName;
	/**
	 * @Fields part_code : 制件编码
	 */
	
	@ApiModelProperty(name = "partCode", notes = "制件编码")
	private String partCode;
	/**
	 * @Fields part_name : 制件名称
	 */
	@ExcelField(title = "制件名称", order = 4)
	@ApiModelProperty(name = "partName", notes = "制件名称")
	private String partName;
	/**
	 * @Fields begin_time : 开始时间
	 */
	@ApiModelProperty(name = "beginTime", notes = "开始时间")
	private Date beginTime;
	/**
	 * @Fields end_time : 结束时间
	 */
	@ApiModelProperty(name = "endTime", notes = "结束时间")
	private Date endTime;
	/**
	 * @Fields coat_supplier : 涂层厂商
	 */
	@ExcelField(title = "涂层厂商", order = 5)
	@ApiModelProperty(name = "supplierName", notes = "涂层厂商")
	private String supplierName;
	/**
	 * @Fields theoretical_qty : 理论加工数
	 */
	@ExcelField(title = "换刀最小加工数", order = 6)
	@ApiModelProperty(name = "theoreticalQty", notes = "理论加工数")
	private Integer theoreticalQty;
	/**
	 * @Fields process_seq : 加工次序
	 */
	@ApiModelProperty(name = "processSeq", notes = "加工次序")
	private Integer processSeq;
	/**
	 * @Fields process_qty : 加工数量
	 */
	@ExcelField(title = "加工数量", order = 7)
	@ApiModelProperty(name = "processQty", notes = "加工数量")
	private Integer processQty;
	/**
	 * @Fields department_id : 使用部门ID
	 */
	@ApiModelProperty(name = "departmentId", notes = "使用部门ID")
	private Long departmentId;
	/**
	 * @Fields department_name : 使用部门
	 */
	@ApiModelProperty(name = "departmentName", notes = "使用部门")
	private String departmentName;
	/**
	 * @Fields team_id : 班组ID
	 */
	@ApiModelProperty(name = "teamId", notes = "班组ID")
	private Long teamId;
	/**
	 * @Fields team_name : 班组名称
	 */
	@ApiModelProperty(name = "teamName", notes = "班组名称")
	private String teamName;
	/**
	 * @Fields staff_code : 员工编号
	 */
	@ApiModelProperty(name = "staffCode", notes = "员工编号")
	private String staffCode;
	/**
	 * @Fields staff_name : 员工姓名
	 */
	@ApiModelProperty(name = "staffName", notes = "员工姓名")
	private String staffName;
	
	/**
	 * @Fields up_to_standard : 是否达标
	 */
	@ApiModelProperty(name = "upToStandard", notes = "是否达标")
	private Integer upToStandard;
	/**
	 * @Fields return_resion : 交回原因（1-正常交回；2-异常交回）
	 */
	@ApiModelProperty(name = "returnResion", notes = "交回原因（1-正常交回；2-异常交回）")
	private Integer returnResion;
	/**
	 * @Fields remark : 异常说明
	 */
	@ApiModelProperty(name = "remark", notes = "异常说明")
	private String remark;
	
	/**
	 * @Fields supplier_id : supplierId
	 */
	@ApiModelProperty(name = "supplierId", notes = "supplierId")
	private Long supplierId;
	
	/**
	 * @Fields coatTime : 涂层时间
	 */
	@ExcelField(title = "涂层时间", order = 8)
	@ApiModelProperty(name = "coatTime", notes = "涂层时间")
	private Date coatTime;
	
	
}

