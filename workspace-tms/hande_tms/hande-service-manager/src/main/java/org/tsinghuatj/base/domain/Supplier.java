package org.tsinghuatj.base.domain;

import java.io.Serializable;
import java.util.Date;

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
public class Supplier implements Serializable {
	
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
	 * @Fields supplier_code : 供应商编码
	 */
	@ExcelField(title = "供应商编码", order = 1)
	@ApiModelProperty(name = "supplierCode", notes = "供应商编码")
	private String supplierCode;
	/**
	 * @Fields supplier_name : 供应商名称
	 */
	@ExcelField(title = "供应商名称", order = 2)
	@ApiModelProperty(name = "supplierName", notes = "供应商名称")
	private String supplierName;
	/**
	 * @Fields evaluation : 供应商评价
	 */
	@ExcelField(title = "供应商评价", order = 3)
	@ApiModelProperty(name = "evaluation", notes = "供应商评价")
	private String evaluation;
	/**
	 * @Fields is_new_tool : 新刀（0-不提供；1-提供）
	 */
	@ApiModelProperty(name = "isNewTool", notes = "新刀（0-不提供；1-提供）")
	private Integer isNewTool;
	/**
	 * @Fields is_new_tool : 新刀（0-不提供；1-提供）
	 */
	@ExcelField(title = "新刀", order = 4)
	@ApiModelProperty(name = "isNewSupplier", notes = "新刀（0-不提供；1-提供）")
	private String isNewSupplier;
	/**
	 * @Fields is_repair : 刃磨（0-不提供；1-提供）
	 */
	@ApiModelProperty(name = "isRepair", notes = "刃磨（0-不提供；1-提供）")
	private Integer isRepair;
	/**
	 * @Fields is_repair : 刃磨（0-不提供；1-提供）
	 */
	@ExcelField(title = "刃磨", order = 5)
	@ApiModelProperty(name = "isRepairSupplier", notes = "刃磨（0-不提供；1-提供）")
	private String isRepairSupplier;
	/**
	 * @Fields is_coat : 涂层（0-不提供；1-提供）
	 */
	@ApiModelProperty(name = "isCoat", notes = "涂层（0-不提供；1-提供）")
	private Integer isCoat;
	/**
	 * @Fields is_repair : 涂层（0-不提供；1-提供）
	 */
	@ExcelField(title = "涂层", order = 6)
	@ApiModelProperty(name = "isCoatSupplier", notes = "涂层（0-不提供；1-提供）")
	private String isCoatSupplier;
	/**
	 * @Fields is_measure : 量具（0-不提供；1-提供）
	 */
	@ApiModelProperty(name = "isMeasure", notes = "量具（0-不提供；1-提供）")
	private Integer isMeasure;
	/**
	 * @Fields is_repair : 量具（0-不提供；1-提供
	 */
	@ExcelField(title = "量具", order = 7)
	@ApiModelProperty(name = "isMeasureSupplier", notes = "量具（0-不提供；1-提供）")
	private String isMeasureSupplier;
	/**
	 * @Fields is_fixture : 夹具（0-不提供；1-提供）
	 */
	@ApiModelProperty(name = "isFixture", notes = "夹具（0-不提供；1-提供）")
	private Integer isFixture;
	/**
	 * @Fields is_repair : 夹具（0-不提供；1-提供）
	 */
	@ExcelField(title = "夹具", order = 8)
	@ApiModelProperty(name = "isFixtureSupplier", notes = "夹具（0-不提供；1-提供）")
	private String isFixtureSupplier;
	
	/**
	 * @Fields is_mould : 模具供应商（0-不是；1-是）
	 */
	@ApiModelProperty(name = "isMould", notes = "模具供应商（0-不是；1-是）")
	private Integer isMould;
	
	/**
	 * @Fields is_repair : 夹具（0-不提供；1-提供）
	 */
	@ExcelField(title = "模具", order = 9)
	@ApiModelProperty(name = "isMouldSupplier", notes = "模具（0-不提供；1-提供）")
	private String isMouldSupplier;
	
}

