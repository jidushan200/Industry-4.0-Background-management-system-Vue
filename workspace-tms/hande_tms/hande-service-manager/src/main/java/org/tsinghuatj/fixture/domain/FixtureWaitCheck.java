package org.tsinghuatj.fixture.domain;

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
public class FixtureWaitCheck implements Serializable {
	
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
	 * @Fields team_id : 领用班组Id
	 */
	@ApiModelProperty(name = "teamId", notes = "领用班组Id")
	private Long teamId;
	/**
	 * @Fields team_name : 领用班组
	 */
	@ApiModelProperty(name = "teamName", notes = "领用班组")
	private String teamName;
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
	 * @Fields set_check_time : 送检时间
	 */
	@ApiModelProperty(name = "setCheckTime", notes = "送检时间")
	private Date setCheckTime;
	/**
	 * @Fields check_type : 检验类型(4-新夹具;5-夹具修磨)
	 */
	@ApiModelProperty(name = "checkType", notes = "检验类型(4-新夹具;5-夹具修磨)")
	private Integer checkType;
	/**
	 * @Fields check_status : 检验状态(0-待检;1-检验中;2-检验完成)
	 */
	@ApiModelProperty(name = "checkStatus", notes = "检验状态(0-待检;1-检验中;2-检验完成)")
	private Integer checkStatus;
	
	
}

