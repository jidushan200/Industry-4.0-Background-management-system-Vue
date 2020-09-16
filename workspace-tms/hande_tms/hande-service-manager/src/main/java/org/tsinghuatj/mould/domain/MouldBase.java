package org.tsinghuatj.mould.domain;

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
public class MouldBase implements Serializable {
	
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
	 * @Fields mould_number : 夹具编码
	 */
	@ExcelField(title = "模具编码", order = 1)
	@ApiModelProperty(name = "mouldNumber", notes = "模具编码")
	private String mouldNumber;
	/**
	 * @Fields mould_name : 夹具名称
	 */
	@ExcelField(title = "模具名称", order = 2)
	@ApiModelProperty(name = "mouldName", notes = "模具名称")
	private String mouldName;
	/**
	 * @Fields mould_map : 图号
	 */
	@ExcelField(title = "模具图号", order = 3)
	@ApiModelProperty(name = "mouldMap", notes = "模具图号")
	private String mouldMap;
	/**
	 * @Fields life_max : 价格
	 */
	@ExcelField(title = "最大寿命", order = 4)
	@ApiModelProperty(name = "lifeMax", notes = "价格")
	private Integer lifeMax;
	/**
	 * @Fields inventory_qty : 库存数量
	 */
	@ApiModelProperty(name = "inventoryQty", notes = "库存数量")
	private Integer inventoryQty;
	/**
	 * @Fields max_seq : 最大顺序号
	 */
	@ApiModelProperty(name = "maxSeq", notes = "最大顺序号")
	private String maxSeq;
	/**
	 * @Fields mould_type : 模具类型（1-普段模具；2-精锻模具）
	 */
	@ApiModelProperty(name = "mouldType", notes = "模具类型（1-普段模具；2-精锻模具）")
	private Integer mouldType;
	/**
	 * @Fields mould_type : 模具类型（1-普段模具；2-精锻模具）
	 */
	@ExcelField(title = "模具类型", order = 5)
	@ApiModelProperty(name = "mouldTypeName", notes = "模具类型（1-普段模具；2-精锻模具）")
	private String mouldTypeName;
	
	@Override
    public String toString() {
        return  "MouldBase " + 
                    "[pkId=" + pkId + "]"
                   + ",[createUser=" + createUser + "]"
                                   + ",[createTime=" + createTime + "]"
                                   + ",[updateUser=" + updateUser + "]"
                                   + ",[updateTime=" + updateTime + "]"
                                   + ",[delMark=" + delMark + "]"
                                   + ",[mouldNumber=" + mouldNumber + "]"
                                   + ",[mouldName=" + mouldName + "]"
                                   + ",[mouldMap=" + mouldMap + "]"
                                   + ",[lifeMax=" + lifeMax + "]"
                                   + ",[inventoryQty=" + inventoryQty + "]"
                                   + ",[maxSeq=" + maxSeq + "]"
                                   + ",[mouldType=" + mouldType + "]"
                ;
	}
}

