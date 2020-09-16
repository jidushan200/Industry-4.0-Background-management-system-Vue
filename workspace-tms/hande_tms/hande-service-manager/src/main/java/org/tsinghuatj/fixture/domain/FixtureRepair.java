package org.tsinghuatj.fixture.domain;

import java.io.Serializable;
import java.math.BigDecimal;
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
public class FixtureRepair implements Serializable {

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
	 * @Fields create_user : 创建用户
	 */
	@ExcelField(title = "修磨人", order = 5)
	@ApiModelProperty(name = "createUser", notes = "创建用户")
	private String createUserName;
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
	 * @Fields fixture_id : 夹具ID
	 */
	@ApiModelProperty(name = "fixtureId", notes = "夹具ID")
	private Long fixtureId;
	/**
	 * @Fields fixture_barcode : 夹具码
	 */
	@ExcelField(title = "夹具条码", order = 1)
	@ApiModelProperty(name = "fixtureBarcode", notes = "夹具码")
	private String fixtureBarcode;
	/**
	 * @Fields fixture_map : 夹具图号
	 */
	@ExcelField(title = "夹具图号", order = 2)
	@ApiModelProperty(name = "fixtureMap", notes = "夹具图号")
	private String fixtureMap;
	/**
	 * @Fields fixture_name : 夹具名称
	 */
	@ExcelField(title = "夹具名称", order = 3)
	@ApiModelProperty(name = "fixtureName", notes = "夹具名称")
	private String fixtureName;
	/**
	 * @Fields repair_measure : 修磨量
	 */
	@ExcelField(title = "修磨量", order = 4)
	@ApiModelProperty(name = "repairMeasure", notes = "修磨量")
	private BigDecimal repairMeasure;
	/**
	 * @Fields remark : 备注
	 */
	@ApiModelProperty(name = "remark", notes = "备注")
	private String remark;
	
	/**
	 * @Fields repair-Status : 修磨状态
	 */
	@ApiModelProperty(name = "repairStatus", notes = "修磨状态")
	private Integer repairStatus;
	
	/**
	 * @Fields create_user : 创建用户
	 */
	@ExcelField(title = "修磨时间", order = 6)
	@ApiModelProperty(name = "createTimeStr", notes = "修磨时间")
	private String createTimeStr;

}
