package org.tsinghuatj.mould.domain;

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
public class Mould implements Serializable {
	
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
	 * @Fields mould_barcode : 模具条码
	 */
	@ExcelField(title = "模具条码", order = 3)
	@ApiModelProperty(name = "mouldBarcode", notes = "模具条码")
	private String mouldBarcode;
	/**
	 * @Fields warehouse_code : 入库编码
	 */
	@ApiModelProperty(name = "warehouseCode", notes = "入库编码")
	private String warehouseCode;
	/**
	 * @Fields mould_number : 模具编码
	 */
	@ExcelField(title = "模具编码", order = 1)
	@ApiModelProperty(name = "mouldNumber", notes = "模具编码")
	private String mouldNumber;
	/**
	 * @Fields mould_name : 模具名称
	 */
	@ExcelField(title = "模具名称", order = 4)
	@ApiModelProperty(name = "mouldName", notes = "模具名称")
	private String mouldName;
	/**
	 * @Fields mould_map : 模具图号
	 */
	@ExcelField(title = "模具图号", order = 2)
	@ApiModelProperty(name = "mouldMap", notes = "模具图号")
	private String mouldMap;
	/**
	 * @Fields mould_seq : 模具序号
	 */
	@ApiModelProperty(name = "mouldSeq", notes = "模具序号")
	private String mouldSeq;
	/**
	 * @Fields mould_status : 模具状态(1-在库;2-出库;3-修磨;4-报废)
	 */
	
	@ApiModelProperty(name = "mouldStatus", notes = "模具状态(1-在库;2-出库;3-修磨;4-报废)")
	private Integer mouldStatus;
	/**
	 * @Fields mouldStatusName : 模具状态(1-在库;2-出库;3-修磨;4-报废)
	 */
	@ExcelField(title = "模具状态", order = 5)
	@ApiModelProperty(name = "mouldStatusName", notes = "模具状态(1-在库;2-出库;3-修磨;4-报废)")
	private String mouldStatusName;
	/**
	 * @Fields mould_type : 模具类型（1-普段模具；2-精锻模具）
	 */
	@ApiModelProperty(name = "mouldType", notes = "模具类型（1-普段模具；2-精锻模具）")
	private Integer mouldType;
	/**
	 * @Fields mould_type : 模具类型（1-普段模具；2-精锻模具）
	 */
	@ExcelField(title = "模具类型", order = 6)
	@ApiModelProperty(name = "mouldTypeName", notes = "模具类型（1-普段模具；2-精锻模具）")
	private String mouldTypeName;
	/**
	 * @Fields life_max : 最大寿命
	 */
	@ExcelField(title = "最大寿命", order = 7)
	@ApiModelProperty(name = "lifeMax", notes = "最大寿命")
	private Integer lifeMax;
	/**
	 * @Fields warehouse : 库位
	 */
	@ExcelField(title = "库位", order =12)
	@ApiModelProperty(name = "warehouse", notes = "库位")
	private String warehouse;
	/**
	 * @Fields warehouse_time : 入库时间
	 */
	@ApiModelProperty(name = "warehouseTime", notes = "入库时间")
	private Date warehouseTime;
	/**
	 * @Fields mould_amount : 模具数量
	 */
	@ApiModelProperty(name = "mouldAmount", notes = "模具数量")
	private Integer mouldAmount;
	/**
	 * @Fields keeper_id : 库管员id
	 */
	@ApiModelProperty(name = "keeperId", notes = "库管员id")
	private Long keeperId;
	/**
	 * @Fields keeper : 库管员
	 */
	@ApiModelProperty(name = "keeper", notes = "库管员")
	private String keeper;
	/**
	 * @Fields use_department_id : 领用部门Id
	 */
	@ApiModelProperty(name = "useDepartmentId", notes = "领用部门Id")
	private Long useDepartmentId;
	/**
	 * @Fields use_department_name : 领用部门
	 */
	@ApiModelProperty(name = "useDepartmentName", notes = "领用部门")
	private String useDepartmentName;
	/**
	 * @Fields use_team_id : 领用班组id
	 */
	@ApiModelProperty(name = "useTeamId", notes = "领用班组id")
	private Long useTeamId;
	/**
	 * @Fields use_team_name : 领用班组
	 */
	@ApiModelProperty(name = "useTeamName", notes = "领用班组")
	private String useTeamName;
	/**
	 * @Fields user_id : 领用人Id
	 */
	@ApiModelProperty(name = "userId", notes = "领用人Id")
	private Long userId;
	/**
	 * @Fields user_name : 领用人
	 */
	@ApiModelProperty(name = "userName", notes = "领用人")
	private String userName;
	/**
	 * @Fields receive_time : 领用时间
	 */
	@ApiModelProperty(name = "receiveTime", notes = "领用时间")
	private Date receiveTime;
	/**
	 * @Fields process_amount : 已完成加工数量
	 */
	@ApiModelProperty(name = "processAmount", notes = "已完成加工数量")
	private Integer processAmount;
	/**
	 * @Fields process_times : 已加工次数
	 */
	@ApiModelProperty(name = "processTimes", notes = "已加工次数")
	private Integer processTimes;
	/**
	 * @Fields repair_times : 已修磨次数
	 */
	@ApiModelProperty(name = "repairTimes", notes = "已修磨次数")
	private Integer repairTimes;
	/**
	 * @Fields repair_time : 修磨时间
	 */
	@ApiModelProperty(name = "repairTime", notes = "修磨时间")
	private Date repairTime;
	/**
	 * @Fields grinder_id : 修磨人Id
	 */
	@ApiModelProperty(name = "grinderId", notes = "修磨人Id")
	private Long grinderId;
	/**
	 * @Fields grinder : 修磨人
	 */
	@ApiModelProperty(name = "grinder", notes = "修磨人")
	private String grinder;
	/**
	 * @Fields heat_number : 热处理编码
	 */
	@ExcelField(title = "热处理编码", order = 9)
	@ApiModelProperty(name = "heatNumber", notes = "热处理编码")
	private String heatNumber;
	/**
	 * @Fields surface_number : 表面处理编码
	 */
	@ExcelField(title = "表面处理编码", order = 10)
	@ApiModelProperty(name = "surfaceNumber", notes = "表面处理编码")
	private String surfaceNumber;
	/**
	 * @Fields scrip_handler : 报废人
	 */
	@ApiModelProperty(name = "scripHandler", notes = "报废人")
	private String scripHandler;
	/**
	 * @Fields scrip_handle_time : 报废操作时间
	 */
	@ApiModelProperty(name = "scripHandleTime", notes = "报废操作时间")
	private Date scripHandleTime;
	/**
	 * @Fields is_scrip : 是否允许报废
	 */
	@ApiModelProperty(name = "isScrip", notes = "是否允许报废")
	private Integer isScrip;
	/**
	 * @Fields is_compromise : 让步使用（1-是；2-否）
	 */
	@ApiModelProperty(name = "isCompromise", notes = "让步使用（1-是；2-否）")
	private Integer isCompromise;
	/**
	 * @Fields is_compromise : 让步使用（1-是；2-否）
	 */
	@ExcelField(title = "让步状态", order = 8)
	@ApiModelProperty(name = "isCompromiseName", notes = "让步使用（1-是；2-否）")
	private String isCompromiseName;
	/**
	 * @Fields isList : 
	 */
	@ApiModelProperty(name = "isList", notes = "是否台账")
	private Integer isList;
	/**
	 * @Fields isLife : 
	 */
	@ApiModelProperty(name = "isLife", notes = "是否生命周期")
	private Integer isLife;
	/**
	 * @Fields isScrip : 是否报废
	 */
	@ApiModelProperty(name = "isScripList", notes = "是否报废")
	private Integer isScripList;
	/**
	 * @Fields enable_time : 入库列表
	 */
	@ApiModelProperty(name = "warehouseList", notes = "入库列表")
	private List<MouldWarehouse> warehouseList;
	/**
	 * @Fields enable_time : 台账标记
	 */
	@ApiModelProperty(name = "outboundList", notes = "出库列表")
	private List<MouldOutbound> outboundList;
	/**
	 * @Fields checkList : 台账标记
	 */
	@ApiModelProperty(name = "checkList", notes = "质检列表")
	private List<MouldCheck> checkList;
	
	/**
	 * @Fields processList : 制件
	 */
	@ApiModelProperty(name = "processList", notes = "制件列表")
	private List<MouldProcess> processList;
	
	/**
	 * @Fields embryo_code : 模具胚编码
	 */
	@ApiModelProperty(name = "embryoCode", notes = "模具胚编码")
	private String embryoCode;
	/**
	 * @Fields embryo_name : 模具胚名称
	 */
	@ApiModelProperty(name = "embryoName", notes = "模具胚名称")
	private String embryoName;
	
	/**
	 * @Fields remark : 备注
	 */
	@ApiModelProperty(name = "outRemark", notes = "备注")
	private String outRemark;
	/**
	 * @Fields remark : 备注
	 */
	@ApiModelProperty(name = "returnRemark", notes = "备注")
	private String returnRemark;
	
	@ApiModelProperty("开始时间")
	private java.util.Date beginDate;
	@ApiModelProperty("结束时间")
	private java.util.Date endDate;
	
	/**
	 * @Fields return_resion : 返回原因(1-正常交回；2-异常交回)
	 */
	@ApiModelProperty(name = "returnResion", notes = "返回原因(1-正常交回；2-异常交回)")
	private Integer returnResion;

	
}

