package org.tsinghuatj.tool.domain;

import java.io.Serializable;
import java.util.Date;

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
public class ToolWarehouse implements Serializable {
	
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
	 * @Fields tool_id : 刀具ID
	 */
	@ApiModelProperty(name = "toolId", notes = "刀具ID")
	private Long toolId;
	/**
	 * @Fields tool_name : 刀具名称
	 */
	@ApiModelProperty(name = "toolName", notes = "刀具名称")
	private String toolName;
	/**
	 * @Fields tool_number : 刀具编码
	 */
	@ApiModelProperty(name = "toolNumber", notes = "刀具编码")
	private String toolNumber;
	/**
	 * @Fields warehouse_code : 入库编码
	 */
	@ApiModelProperty(name = "warehouseCode", notes = "入库编码")
	private String warehouseCode;
	/**
	 * @Fields tool_map : 物料图号
	 */
	@ApiModelProperty(name = "toolMap", notes = "物料图号")
	private String toolMap;
	/**
	 * @Fields full_number : 物料条码
	 */
	@ApiModelProperty(name = "fullNumber", notes = "物料条码")
	private String fullNumber;
	/**
	 * @Fields in_type : 入库类型(1-新刀入库；2-用后返库；3-刃磨返库；4-涂层返库)
	 */
	@ApiModelProperty(name = "inType", notes = "入库类型(1-新刀入库；2-用后返库；3-刃磨返库；4-涂层返库)")
	private Integer inType;
	/**
	 * @Fields keeper_id : keeperId
	 */
	@ApiModelProperty(name = "keeperId", notes = "keeperId")
	private Long keeperId;
	/**
	 * @Fields keeper : 库管员
	 */
	@ApiModelProperty(name = "keeper", notes = "库管员")
	private String keeper;
	/**
	 * @Fields remark : 说明
	 */
	@ApiModelProperty(name = "remark", notes = "说明")
	private String remark;
	/**
	 * @Fields warehouse : 库位
	 */
	@ApiModelProperty(name = "warehouse", notes = "库位")
	private String warehouse;
	/**
	 * @Fields warehosing_time : 入库时间
	 */
	@ApiModelProperty(name = "warehosingTime", notes = "入库时间")
	private Date warehosingTime;
	/**
	 * @Fields deliever : 送货人
	 */
	@ApiModelProperty(name = "deliever", notes = "送货人")
	private String deliever;
	/**
	 * @Fields return_resion : 返回原因(1-正常交回；2-异常交回；3-检后返库；)
	 */
	@ApiModelProperty(name = "returnResion", notes = "返回原因(1-正常交回；2-异常交回；3-检后返库；)")
	private Integer returnResion;
	
	@Override
    public String toString() {
        return  "ToolWarehouse " + 
                    "[pkId=" + pkId + "]"
                   + ",[createUser=" + createUser + "]"
                                   + ",[createTime=" + createTime + "]"
                                   + ",[updateUser=" + updateUser + "]"
                                   + ",[updateTime=" + updateTime + "]"
                                   + ",[delMark=" + delMark + "]"
                                   + ",[toolId=" + toolId + "]"
                                   + ",[toolName=" + toolName + "]"
                                   + ",[toolNumber=" + toolNumber + "]"
                                   + ",[warehouseCode=" + warehouseCode + "]"
                                   + ",[toolMap=" + toolMap + "]"
                                   + ",[fullNumber=" + fullNumber + "]"
                                   + ",[inType=" + inType + "]"
                                   + ",[keeperId=" + keeperId + "]"
                                   + ",[keeper=" + keeper + "]"
                                   + ",[remark=" + remark + "]"
                                   + ",[warehouse=" + warehouse + "]"
                                   + ",[warehosingTime=" + warehosingTime + "]"
                                   + ",[deliever=" + deliever + "]"
                                   + ",[returnResion=" + returnResion + "]"
                ;
	}
}

