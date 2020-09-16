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
public class MeasureOutbound implements Serializable {
	
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
	 * @Fields measure_name : 刀具名称
	 */
	@ApiModelProperty(name = "measureName", notes = "刀具名称")
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
	 * @Fields warehouse : 库位
	 */
	@ApiModelProperty(name = "warehouse", notes = "库位")
	private String warehouse;
	/**
	 * @Fields out_type : 出库类型(1-领用出库；2-定检出库；)
	 */
	@ApiModelProperty(name = "outType", notes = "出库类型(1-领用出库；2-定检出库；)")
	private Integer outType;
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
	 * @Fields receiver : 领用人
	 */
	@ApiModelProperty(name = "receiver", notes = "领用人")
	private String receiver;
	/**
	 * @Fields receive_class : 领用班组
	 */
	@ApiModelProperty(name = "receiveClass", notes = "领用班组")
	private String receiveClass;
	/**
	 * @Fields receive_time : 领用时间
	 */
	@ApiModelProperty(name = "receiveTime", notes = "领用时间")
	private Date receiveTime;
	/**
	 * @Fields keeper_id : 库管员ID
	 */
	@ApiModelProperty(name = "keeperId", notes = "库管员ID")
	private Long keeperId;
	/**
	 * @Fields keeper : 库管员
	 */
	@ApiModelProperty(name = "keeper", notes = "库管员")
	private String keeper;
	/**
	 * @Fields receive_resion : 领用原因
	 */
	@ApiModelProperty(name = "receiveResion", notes = "领用原因")
	private String receiveResion;
	/**
	 * @Fields remark : 说明
	 */
	@ApiModelProperty(name = "remark", notes = "说明")
	private String remark;
	
	@Override
    public String toString() {
        return  "MeasureOutbound " + 
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
                                   + ",[warehouse=" + warehouse + "]"
                                   + ",[outType=" + outType + "]"
                                   + ",[departmentId=" + departmentId + "]"
                                   + ",[departmentName=" + departmentName + "]"
                                   + ",[receiver=" + receiver + "]"
                                   + ",[receiveClass=" + receiveClass + "]"
                                   + ",[receiveTime=" + receiveTime + "]"
                                   + ",[keeperId=" + keeperId + "]"
                                   + ",[keeper=" + keeper + "]"
                                   + ",[receiveResion=" + receiveResion + "]"
                                   + ",[remark=" + remark + "]"
                ;
	}
}

