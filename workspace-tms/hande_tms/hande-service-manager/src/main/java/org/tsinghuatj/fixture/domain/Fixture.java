package org.tsinghuatj.fixture.domain;

import java.io.Serializable;
import java.math.BigDecimal;
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
public class Fixture implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/**
	 * @Fields pk_id : pkId
	 */
	@ApiModelProperty(name = "pkId", notes = "pkId")
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
	 * @Fields fixture_barcode : 完整编码
	 */
	@ApiModelProperty(name = "fixtureBarcode", notes = "完整编码")
	private String fixtureBarcode;
	/**
	 * @Fields fixture_number : 夹具编码
	 */
	@ApiModelProperty(name = "fixtureNumber", notes = "夹具编码")
	private String fixtureNumber;
	/**
	 * @Fields fixture_name : 夹具名称
	 */
	@ApiModelProperty(name = "fixtureName", notes = "夹具名称")
	private String fixtureName;
	/**
	 * @Fields fixture_map : 夹具图号
	 */
	@ApiModelProperty(name = "fixtureMap", notes = "夹具图号")
	private String fixtureMap;
	
	/**
	 * @Fields fixture_seq : 夹具顺序号
	 */
	@ApiModelProperty(name = "fixtureSeq", notes = "夹具顺序号")
	private String fixtureSeq;
	/**
	 * @Fields repair_times : 夹具类型
	 */
	@ApiModelProperty(name = "fixtureType", notes = "夹具类型")
	private Integer fixtureType;
	
	/**
	 * @Fields supplier_id : 供应商ID
	 */
	@ApiModelProperty(name = "supplierId", notes = "供应商ID")
	private Long supplierId;
	/**
	 * @Fields supplier_name : 供应商名称
	 */
	@ApiModelProperty(name = "supplierName", notes = "供应商名称")
	private String supplierName;
	/**
	 * @Fields price : 价格
	 */
	@ApiModelProperty(name = "price", notes = "价格")
	private BigDecimal price;
	/**
	 * @Fields store_house : 库位
	 */
	@ApiModelProperty(name = "storeHouse", notes = "库位")
	private String storeHouse;
	/**
	 * @Fields warehouse_time : 入库时间
	 */
	@ApiModelProperty(name = "warehouseTime", notes = "入库时间")
	private Date warehouseTime;
	/**
	 * @Fields department_id : 领用部门ID
	 */
	@ApiModelProperty(name = "departmentId", notes = "领用部门ID")
	private Long departmentId;
	/**
	 * @Fields department_name : 领用部门名称
	 */
	@ApiModelProperty(name = "departmentName", notes = "领用部门名称")
	private String departmentName;
	/**
	 * @Fields equipment_id : 设备Id
	 */
	@ApiModelProperty(name = "equipmentId", notes = "设备Id")
	private Long equipmentId;
	/**
	 * @Fields equipment_name : 设备
	 */
	@ApiModelProperty(name = "equipmentName", notes = "设备")
	private String equipmentName;
	/**
	 * @Fields staff_code : 领用人工号
	 */
	@ApiModelProperty(name = "staffCode", notes = "领用人工号")
	private String staffCode;
	/**
	 * @Fields staff_name : 领用人
	 */
	@ApiModelProperty(name = "staffName", notes = "领用人")
	private String staffName;
	/**
	 * @Fields fixture_status : 夹具状态(1-正常使用;2-待修磨;3-待送检;4-待检验;5-质检完成待返库;6-待报废;7-报废）
	 */
	@ApiModelProperty(name = "fixtureStatus", notes = "夹具状态(1-正常使用;2-待修磨;3-待送检;4-待检验;5-质检完成待返库;6-待报废;7-报废）")
	private Integer fixtureStatus;
	/**
	 * @Fields repair_times : 修磨次数
	 */
	@ApiModelProperty(name = "repairTimes", notes = "修磨次数")
	private Integer repairTimes;
	/**
	 * @Fields last_repair_time : 最后修磨时间
	 */
	@ApiModelProperty(name = "lastRepairTime", notes = "最后修磨时间")
	private Date lastRepairTime;
	/**
	 * @Fields maintain_times : 保养次数
	 */
	@ApiModelProperty(name = "maintainTimes", notes = "保养次数")
	private Integer maintainTimes;
	/**
	 * @Fields last_maintain_time : 最后保养时间
	 */
	@ApiModelProperty(name = "lastMaintainTime", notes = "最后保养时间")
	private Date lastMaintainTime;
	/**
	 * @Fields spot_times : 点检次数
	 */
	@ApiModelProperty(name = "spotTimes", notes = "点检次数")
	private Integer spotTimes;
	/**
	 * @Fields last_spot_time : 最后点检时间
	 */
	@ApiModelProperty(name = "lastSpotTime", notes = "最后点检时间")
	private Date lastSpotTime;
	/**
	 * @Fields next_maintain_time : 下次保养时间
	 */
	@ApiModelProperty(name = "nextMaintainTime", notes = "下次保养时间")
	private Date nextMaintainTime;
	/**
	 * @Fields set_repair_time : 送修磨时间
	 */
	@ApiModelProperty(name = "setRepairTime", notes = "送修磨时间")
	private Date setRepairTime;
	/**
	 * @Fields set_check_time : 送检时间
	 */
	@ApiModelProperty(name = "setCheckTime", notes = "送检时间")
	private Date setCheckTime;
	/**
	 * @Fields remark : 说明
	 */
	@ApiModelProperty(name = "remark", notes = "说明")
	private String remark;
	/**
	 * @Fields in_use : 是否在用(1-在用;2-在库;3-报废)
	 */
	@ApiModelProperty(name = "inUse", notes = "是否在用")
	private Integer inUse;
	

	//夹具配件
	private List<FixtureChild> childList;
	
}

