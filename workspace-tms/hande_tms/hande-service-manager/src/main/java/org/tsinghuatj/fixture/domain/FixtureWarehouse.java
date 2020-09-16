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
public class FixtureWarehouse implements Serializable {
	
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
	 * @Fields fixture_barcode : 夹具条码
	 */
	@ApiModelProperty(name = "fixtureBarcode", notes = "夹具条码")
	private String fixtureBarcode;
	/**
	 * @Fields warehouse_type : 类型(1-出库;2-返库)
	 */
	@ApiModelProperty(name = "warehouseType", notes = "类型(1-出库;2-返库)")
	private Integer warehouseType;
	/**
	 * @Fields staff_code : 领用人工号
	 */
	@ApiModelProperty(name = "staffCode", notes = "领用人工号")
	private String staffCode;
	/**
	 * @Fields staff_name : 领用姓名
	 */
	@ApiModelProperty(name = "staffName", notes = "领用姓名")
	private String staffName;
	/**
	 * @Fields remark : 说明
	 */
	@ApiModelProperty(name = "remark", notes = "说明")
	private String remark;
	
	@Override
    public String toString() {
        return  "FixtureWarehouse " + 
                    "[pkId=" + pkId + "]"
                   + ",[createUser=" + createUser + "]"
                                   + ",[createTime=" + createTime + "]"
                                   + ",[updateUser=" + updateUser + "]"
                                   + ",[updateTime=" + updateTime + "]"
                                   + ",[delMark=" + delMark + "]"
                                   + ",[fixtureBarcode=" + fixtureBarcode + "]"
                                   + ",[warehouseType=" + warehouseType + "]"
                                   + ",[staffCode=" + staffCode + "]"
                                   + ",[staffName=" + staffName + "]"
                                   + ",[remark=" + remark + "]"
                ;
	}
}

