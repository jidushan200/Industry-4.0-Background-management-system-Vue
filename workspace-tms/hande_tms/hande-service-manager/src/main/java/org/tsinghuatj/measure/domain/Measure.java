package org.tsinghuatj.measure.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.tsinghuatj.framework.utils.excel.annotation.ExcelField;

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
public class Measure implements Serializable {
	
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
	 * @Fields measure_number : 量具编码
	 */
	@ExcelField(title = "物料编码", order = 1)
	@ApiModelProperty(name = "measureNumber", notes = "量具编码")
	private String measureNumber;
	/**
	 * @Fields measure_name : 名称规格
	 */
	@ExcelField(title = "物料名称", order = 2)
	@ApiModelProperty(name = "measureName", notes = "名称规格")
	private String measureName;
	/**
	 * @Fields model : 规格型号
	 */
	@ExcelField(title = "规格型号", order = 3)
	@ApiModelProperty(name = "model", notes = "规格型号")
	private String model;
	/**
	 * @Fields measure_barcode : 量具条码
	 */
	@ExcelField(title = "物料条码", order = 4)
	@ApiModelProperty(name = "measureBarcode", notes = "量具条码")
	private String measureBarcode;
	/**
	 * @Fields apply_department_id : 申请部门ID
	 */
	@ApiModelProperty(name = "applyDepartmentId", notes = "申请部门ID")
	private Long applyDepartmentId;
	/**
	 * @Fields apply_department_name : 申请部门
	 */
	@ApiModelProperty(name = "applyDepartmentName", notes = "申请部门")
	private String applyDepartmentName;
	/**
	 * @Fields applier_id : 申请人ID
	 */
	@ApiModelProperty(name = "applierId", notes = "申请人ID")
	private Long applierId;
	/**
	 * @Fields applier_name : 申请人
	 */
	@ApiModelProperty(name = "applierName", notes = "申请人")
	private String applierName;
	/**
	 * @Fields use_department_id : 使用部门ID
	 */
	@ApiModelProperty(name = "useDepartmentId", notes = "使用部门ID")
	private Long useDepartmentId;
	/**
	 * @Fields use_department_name : 使用部门名称
	 */
	@ExcelField(title = "使用部门", order = 10)
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
	@ExcelField(title = "使用班组", order = 11)
	@ApiModelProperty(name = "useTeamName", notes = "使用班组名称")
	private String useTeamName;
	/**
	
	/**
	 * @Fields user_name : 使用人名称
	 */
	@ExcelField(title = "使用人工号", order = 12)
	@ApiModelProperty(name = "userCode", notes = "使用人工号")
	private String userCode;
	/**
	 * @Fields user_name : 使用人名称
	 */
	@ExcelField(title = "使用人", order = 13)
	@ApiModelProperty(name = "userName", notes = "使用人名称")
	private String userName;
	/**
	 * @Fields supplier_id : 供应商ID
	 */
	@ApiModelProperty(name = "supplierId", notes = "供应商ID")
	private Long supplierId;
	/**
	 * @Fields supplier_name : 供应商名称
	 */
	@ExcelField(title = "制造商", order = 15)
	@ApiModelProperty(name = "supplierName", notes = "供应商名称")
	private String supplierName;
	/**
	 * @Fields manufacturer : 制造商
	 */
	@ApiModelProperty(name = "manufacturer", notes = "制造商")
	private String manufacturer;
	/**
	 * @Fields manufacturing_number : 出厂编号
	 */
	@ExcelField(title = "厂家计量编号", order = 16)
	@ApiModelProperty(name = "manufacturingNumber", notes = "出厂编号")
	private String manufacturingNumber;
	/**
	 * @Fields storage_time : 入库时间
	 */
	@ApiModelProperty(name = "storageTime", notes = "入库时间")
	private Date storageTime;
	/**
	 * @Fields measure_status : 量具状态(1-在用;2-封存;3-报废)
	 */
	@ApiModelProperty(name = "measureStatus", notes = "量具状态(1-在用;2-封存;3-报废)")
	private Integer measureStatus;
	/**
	 * @Fields measure_status : 量具状态(1-在用;2-封存;3-报废)
	 */
	@ExcelField(title = "量具状态", order = 5)
	@ApiModelProperty(name = "measureStatusName", notes = "量具状态(1-在用;2-封存;3-报废)")
	private String measureStatusName;
	/**
	 * @Fields measure_seq : 量具序号
	 */
	@ApiModelProperty(name = "measureSeq", notes = "量具序号")
	private Integer measureSeq;
	/**
	 * @Fields factory_metrology : 本厂计量编号
	 */
	@ExcelField(title = "本厂计量编码", order = 6)
	@ApiModelProperty(name = "factoryMetrology", notes = "本厂计量编号")
	private String factoryMetrology;
	/**
	 * @Fields check_time : 检定时间
	 */
	@ApiModelProperty(name = "checkTime", notes = "检定时间")
	private Date checkTime;
	/**
	 * @Fields check_time : 检定时间
	 */
	@ExcelField(title = "检定时间", order = 7)
	@ApiModelProperty(name = "checkTimeString", notes = "检定时间")
	private String checkTimeString;
	/**
	 * @Fields check_cycle : 检定周期
	 */
	@ExcelField(title = "检定周期(日)", order = 9)
	@ApiModelProperty(name = "checkCycle", notes = "检定周期")
	private Integer checkCycle;
	/**
	 * @Fields next_check_time : 下次检定日期
	 */
	@ApiModelProperty(name = "nextCheckTime", notes = "下次检定日期")
	private Date nextCheckTime;
	/**
	 * @Fields next_check_time : 下次检定日期
	 */
	@ExcelField(title = "下次检定日期", order = 8)
	@ApiModelProperty(name = "nextCheckTimeString", notes = "下次检定日期")
	private String nextCheckTimeString;
	/**
	 * @Fields check_result : 质检结果（0-不合格；1-合格）
	 */
	@ApiModelProperty(name = "checkResult", notes = "质检结果（0-不合格；1-合格）")
	private Integer checkResult;
	/**
	 * @Fields repair_times : 修磨次数
	 */
	@ExcelField(title = "修理次数", order = 14)
	@ApiModelProperty(name = "repairTimes", notes = "修磨次数")
	private Integer repairTimes;
	/**
	 * @Fields warehouse : 库位
	 */
	@ExcelField(title = "库位", order =6)
	@ApiModelProperty(name = "warehouse", notes = "库位")
	private String warehouse;
	/**
	 * @Fields keeper_id : 库管员Id
	 */
	@ApiModelProperty(name = "keeperId", notes = "库管员Id")
	private Long keeperId;
	/**
	 * @Fields keeper : 库管员
	 */
	@ApiModelProperty(name = "keeper", notes = "库管员")
	private String keeper;
	/**
	 * @Fields seal_time : 封存时间
	 */
	@ApiModelProperty(name = "sealTime", notes = "封存时间")
	private Date sealTime;
	/**
	 * @Fields enable_time : 启用时间
	 */
	@ApiModelProperty(name = "enableTime", notes = "启用时间")
	private Date enableTime;
	
	/**
	 * @Fields is_scrip : 是否可以报废（1-是；0-不是）
	 */
	@ApiModelProperty(name = "isScrip", notes = "是否可以报废（1-是；0-不是）")
	private Integer isScrip;
	
	/**
	 * @Fields seal_mark : 封存状态（0-启用；1-封存）
	 */
	@ApiModelProperty(name = "sealMark", notes = "封存状态（0-启用；1-封存）")
	private Integer sealMark;
	/**
	 * @Fields seal_mark : 封存状态（0-启用；1-封存）
	 */
	@ExcelField(title = "封存状态", order =7)
	@ApiModelProperty(name = "sealMarkName", notes = "封存状态（0-启用；1-封存）")
	private String sealMarkName;
	
	/**
	 * @Fields enable_time : 台账标记
	 */
	@ApiModelProperty(name = "isList", notes = "台账标记")
	private Integer isList;
	/**
	 * @Fields enable_time : 生命周期标记
	 */
	@ApiModelProperty(name = "isLife", notes = "生命周期标记")
	private Integer isLife;
	
	/**
	 * @Fields enable_time : 入库列表
	 */
	@ApiModelProperty(name = "warehouseList", notes = "入库列表")
	private List<MeasureWarehouse> warehouseList;
	
	/**
	 * @Fields enable_time : 台账标记
	 */
	@ApiModelProperty(name = "outboundList", notes = "出库列表")
	private List<MeasureOutbound> outboundList;
	
	/**
	 * @Fields checkList : 台账标记
	 */
	@ApiModelProperty(name = "checkList", notes = "质检列表")
	private List<MeasureCheck> checkList;
	
	@ApiModelProperty("开始时间")
	private java.util.Date beginDate;
	@ApiModelProperty("结束时间")
	private java.util.Date endDate;

}

