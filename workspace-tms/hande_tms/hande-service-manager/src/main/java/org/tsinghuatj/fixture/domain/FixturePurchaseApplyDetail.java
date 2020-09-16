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
public class FixturePurchaseApplyDetail implements Serializable {
	
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
	 * @Fields apply_id : 申请单id
	 */
	@ApiModelProperty(name = "applyId", notes = "申请单id")
	private Long applyId;
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
	 * @Fields fixture_map : 图号
	 */
	@ApiModelProperty(name = "fixtureMap", notes = "图号")
	private String fixtureMap;
	/**
	 * @Fields base_qty : 基准数量
	 */
	@ApiModelProperty(name = "baseQty", notes = "基准数量")
	private Integer baseQty;
	/**
	 * @Fields fixture_qty : 申购数量
	 */
	@ApiModelProperty(name = "fixtureQty", notes = "申购数量")
	private Integer fixtureQty;
	/**
	 * @Fields available_number : 可用序号
	 */
	@ApiModelProperty(name = "availableNumber", notes = "可用序号")
	private String availableNumber;
	
	
}

