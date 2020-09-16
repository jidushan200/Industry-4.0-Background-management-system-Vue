package org.tsinghuatj.mould.domain;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ApiModel
public class MouldProcess implements Serializable {
	
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
	 * @Fields mould_id : 刀具ID
	 */
	@ApiModelProperty(name = "mouldId", notes = "刀具ID")
	private Long mouldId;
	/**
	 * @Fields mould_number : 物料编码
	 */
	@ApiModelProperty(name = "mouldNumber", notes = "物料编码")
	private String mouldNumber;
	/**
	 * @Fields mould_name : 物料名称
	 */
	@ApiModelProperty(name = "mouldName", notes = "物料名称")
	private String mouldName;
	/**
	 * @Fields full_number : 物料条码
	 */
	@ApiModelProperty(name = "fullNumber", notes = "物料条码")
	private String fullNumber;
	/**
	 * @Fields mould_map : 物料图号
	 */
	@ApiModelProperty(name = "mouldMap", notes = "物料图号")
	private String mouldMap;
	/**
	 * @Fields equipment_id : 绑定设备ID
	 */
	@ApiModelProperty(name = "equipmentId", notes = "绑定设备ID")
	private Long equipmentId;
	/**
	 * @Fields equipment_code : 设备编码
	 */
	@ApiModelProperty(name = "equipmentCode", notes = "设备编码")
	private String equipmentCode;
	/**
	 * @Fields equipment_name : 设备名称
	 */
	@ApiModelProperty(name = "equipmentName", notes = "设备名称")
	private String equipmentName;
	/**
	 * @Fields part_id : 制件ID
	 */
	@ApiModelProperty(name = "partId", notes = "制件ID")
	private Long partId;
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
	 * @Fields process_amount : 加工数量
	 */
	@ApiModelProperty(name = "processAmount", notes = "加工数量")
	private Integer processAmount;
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
	 * @Fields department_id : 使用部门ID
	 */
	@ApiModelProperty(name = "departmentId", notes = "使用部门ID")
	private Long departmentId;
	/**
	 * @Fields department_name : 使用部门
	 */
	@ApiModelProperty(name = "departmentName", notes = "使用部门")
	private String departmentName;
	
	
}

