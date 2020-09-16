package org.tsinghuatj.fixture.domain;

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
public class FixtureMaintain implements Serializable {
	
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
	@ApiModelProperty(name = "createUserName", notes = "创建用户")
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
	 * @Fields fixture_barcode : 夹具条码
	 */
	@ExcelField(title = "夹具条码", order = 1)
	@ApiModelProperty(name = "fixtureBarcode", notes = "夹具条码")
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
	 * @Fields maintain_item : 保养项目
	 */
	@ExcelField(title = "保养项目", order = 4)
	@ApiModelProperty(name = "maintainItem", notes = "保养项目")
	private String maintainItem;
	/**
	 * @Fields use_status : 使用状态(1-正常使用;2-修磨,3-建议报废)
	 */
	@ApiModelProperty(name = "useStatus", notes = "使用状态(1-正常使用;2-修磨,3-建议报废)")
	private Integer useStatus;
	/**
	 * @Fields useStatusName : 使用状态
	 */
	@ExcelField(title = "使用状态", order = 5)
	@ApiModelProperty(name = "useStatusName", notes = "使用状态")
	private String useStatusName;
	/**
	 * @Fields remark : 说明
	 */
	@ExcelField(title = "备注", order = 6)
	@ApiModelProperty(name = "remark", notes = "说明")
	private String remark;
	
	/**
	 * @Fields maintain_status : 单据状态(0-暂存;1-提交;)
	 */
	@ApiModelProperty(name = "maintainStatus", notes = "单据状态(0-暂存;1-提交;)")
	private Integer maintainStatus;
	
	
}

