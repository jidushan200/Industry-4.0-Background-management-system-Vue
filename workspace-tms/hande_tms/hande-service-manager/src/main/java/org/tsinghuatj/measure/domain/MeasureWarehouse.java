package org.tsinghuatj.measure.domain;

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
public class MeasureWarehouse implements Serializable {
	
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
	 * @Fields measure_id : 刀具ID
	 */
	@ApiModelProperty(name = "measureId", notes = "刀具ID")
	private Long measureId;
	/**
	 * @Fields measure_name : 物料名称
	 */
	@ApiModelProperty(name = "measureName", notes = "物料名称")
	private String measureName;
	/**
	 * @Fields measure_number : 物料编码
	 */
	@ApiModelProperty(name = "measureNumber", notes = "物料编码")
	private String measureNumber;
	/**
	 * @Fields model : 物料图号
	 */
	@ApiModelProperty(name = "model", notes = "物料图号")
	private String model;
	/**
	 * @Fields measure_barcode : 物料条码
	 */
	@ApiModelProperty(name = "measureBarcode", notes = "物料条码")
	private String measureBarcode;
	/**
	 * @Fields in_type : 入库类型(1-新刀入库；2-用后返库；)
	 */
	@ApiModelProperty(name = "inType", notes = "入库类型(1-新刀入库；2-用后返库；)")
	private Integer inType;
	/**
	 * @Fields keeper_id : 库管员id
	 */
	@ApiModelProperty(name = "keeperId", notes = "库管员id")
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
	
	@Override
    public String toString() {
        return  "MeasureWarehouse " + 
                    "[pkId=" + pkId + "]"
                   + ",[createUser=" + createUser + "]"
                                   + ",[createTime=" + createTime + "]"
                                   + ",[updateUser=" + updateUser + "]"
                                   + ",[updateTime=" + updateTime + "]"
                                   + ",[delMark=" + delMark + "]"
                                   + ",[measureId=" + measureId + "]"
                                   + ",[measureName=" + measureName + "]"
                                   + ",[measureNumber=" + measureNumber + "]"
                                   + ",[model=" + model + "]"
                                   + ",[measureBarcode=" + measureBarcode + "]"
                                   + ",[inType=" + inType + "]"
                                   + ",[keeperId=" + keeperId + "]"
                                   + ",[keeper=" + keeper + "]"
                                   + ",[remark=" + remark + "]"
                                   + ",[warehouse=" + warehouse + "]"
                                   + ",[warehosingTime=" + warehosingTime + "]"
                ;
	}
}

