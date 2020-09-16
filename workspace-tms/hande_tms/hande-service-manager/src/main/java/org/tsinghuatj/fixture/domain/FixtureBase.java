package org.tsinghuatj.fixture.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
public class FixtureBase implements Serializable {

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
	 * @Fields fixture_number : 夹具编码
	 */
	@ExcelField(title = "夹具编码", order = 1)
	@ApiModelProperty(name = "fixtureNumber", notes = "夹具编码")
	private String fixtureNumber;
	/**
	 * @Fields fixture_name : 夹具名称
	 */
	@ExcelField(title = "夹具名称", order = 2)
	@ApiModelProperty(name = "fixtureName", notes = "夹具名称")
	private String fixtureName;
	/**
	 * @Fields fixture_map : 图号
	 */
	@ExcelField(title = "图号", order = 3)
	@ApiModelProperty(name = "fixtureMap", notes = "图号")
	private String fixtureMap;
	
	
	/**
	 * @Fields fixture_type : 夹具类型(1-组合;2-配件)
	 */
	@ApiModelProperty(name = "fixtureType", notes = "夹具类型")
	private Integer fixtureType;
	
	/**
	 * @Fields price : 价格
	 */
	@ExcelField(title = "价格", order = 4)
	@ApiModelProperty(name = "price", notes = "价格")
	private BigDecimal price;

	private Integer erpQty;
	
	private Integer noCheckQty;
	
	@ApiModelProperty(name = "inventoryQty", notes = "库存数量")
	private Integer inventoryQty;
	
	private String maxSeq;
	
	//夹具配件
	private List<FixtureBaseChild> childList;
}
