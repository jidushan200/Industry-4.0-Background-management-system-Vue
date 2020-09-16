package org.tsinghuatj.tool.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
public class ToolBladeProcess implements Serializable {

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
	 * @Fields compose_number : 组合编码
	 */
	@ApiModelProperty(name = "composeNumber", notes = "组合编码")
	private String composeNumber;
	/**
	 * @Fields head_number : 刀盘编号
	 */
	@ApiModelProperty(name = "headNumber", notes = "刀盘组合编号")
	private String headNumber;
	@ApiModelProperty(name = "headName", notes = "刀盘组合名称")
	private String headName;	

	/**
	 * @Fields equipment_code : 设备编码
	 */
	@ApiModelProperty(name = "equipmentCode", notes = "设备编码")
	private String equipmentCode;
	/**
	 * @Fields tag_number : 标签号
	 */
	@ApiModelProperty(name = "tagNumber", notes = "标签号")
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
	@ApiModelProperty(name = "partName", notes = "制件名称")
	private String partName;

	/**
	 * @Fields staff_Code : 员工编码
	 */
	@ApiModelProperty(name = "staffCode", notes = "员工编码")
	private String staffCode;
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
	 * @Fields process_seq : 加工次序
	 */
	@ApiModelProperty(name = "processSeq", notes = "加工次序")
	private Integer processSeq;
	/**
	 * @Fields process_qty : 加工数量
	 */
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
	 * @Fields theoretical_qty : 理论加工数
	 */
	@ApiModelProperty(name = "theoreticalQty", notes = "理论加工数")
	private Integer theoreticalQty;

	/**
	 * @Fields up_to_standard : 是否达标
	 */
	@ApiModelProperty(name = "upToStandard", notes = "是否达标")
	private Integer upToStandard;
	
	
	List<ToolBladeComposeDetail> detailList;
	

}
