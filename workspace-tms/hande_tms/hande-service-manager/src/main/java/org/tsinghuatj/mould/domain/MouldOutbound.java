package org.tsinghuatj.mould.domain;

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
public class MouldOutbound implements Serializable {
	
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
	 * @Fields mould_id : 模具ID
	 */
	@ApiModelProperty(name = "mouldId", notes = "模具ID")
	private Long mouldId;
	/**
	 * @Fields mould_name : 模具名称
	 */
	@ApiModelProperty(name = "mouldName", notes = "模具名称")
	private String mouldName;
	/**
	 * @Fields mould_number : 模具编码
	 */
	@ApiModelProperty(name = "mouldNumber", notes = "模具编码")
	private String mouldNumber;
	/**
	 * @Fields mould_map : 模具图号
	 */
	@ApiModelProperty(name = "mouldMap", notes = "模具图号")
	private String mouldMap;
	/**
	 * @Fields mould_barcode : 模具条码
	 */
	@ApiModelProperty(name = "mouldBarcode", notes = "模具条码")
	private String mouldBarcode;
	/**
	 * @Fields warehouse : 库位
	 */
	@ApiModelProperty(name = "warehouse", notes = "库位")
	private String warehouse;
	/**
	 * @Fields out_type : 出库类型(1-领用出库；2-修磨出库；)
	 */
	@ApiModelProperty(name = "outType", notes = "出库类型(1-领用出库；2-修磨出库；)")
	private Integer outType;
	/**
	 * @Fields use_department_id : 领用部门Id
	 */
	@ApiModelProperty(name = "useDepartmentId", notes = "领用部门Id")
	private Long useDepartmentId;
	/**
	 * @Fields use_department_name : 领用部门
	 */
	@ApiModelProperty(name = "useDepartmentName", notes = "领用部门")
	private String useDepartmentName;
	/**
	 * @Fields use_team_id : 领用班组id
	 */
	@ApiModelProperty(name = "useTeamId", notes = "领用班组id")
	private Long useTeamId;
	/**
	 * @Fields use_team_name : 领用班组
	 */
	@ApiModelProperty(name = "useTeamName", notes = "领用班组")
	private String useTeamName;
	/**
	 * @Fields user_id : 领用人Id
	 */
	@ApiModelProperty(name = "userId", notes = "领用人Id")
	private Long userId;
	/**
	 * @Fields user_name : 领用人
	 */
	@ApiModelProperty(name = "userName", notes = "领用人")
	private String userName;
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
	 * @Fields remark : 说明
	 */
	@ApiModelProperty(name = "remark", notes = "说明")
	private String remark;
	
	@Override
    public String toString() {
        return  "MouldOutbound " + 
                    "[pkId=" + pkId + "]"
                   + ",[createUser=" + createUser + "]"
                                   + ",[createTime=" + createTime + "]"
                                   + ",[updateUser=" + updateUser + "]"
                                   + ",[updateTime=" + updateTime + "]"
                                   + ",[delMark=" + delMark + "]"
                                   + ",[mouldId=" + mouldId + "]"
                                   + ",[mouldName=" + mouldName + "]"
                                   + ",[mouldNumber=" + mouldNumber + "]"
                                   + ",[mouldMap=" + mouldMap + "]"
                                   + ",[mouldBarcode=" + mouldBarcode + "]"
                                   + ",[warehouse=" + warehouse + "]"
                                   + ",[outType=" + outType + "]"
                                   + ",[useDepartmentId=" + useDepartmentId + "]"
                                   + ",[useDepartmentName=" + useDepartmentName + "]"
                                   + ",[useTeamId=" + useTeamId + "]"
                                   + ",[useTeamName=" + useTeamName + "]"
                                   + ",[userId=" + userId + "]"
                                   + ",[userName=" + userName + "]"
                                   + ",[receiveTime=" + receiveTime + "]"
                                   + ",[keeperId=" + keeperId + "]"
                                   + ",[keeper=" + keeper + "]"
                                   + ",[remark=" + remark + "]"
                ;
	}
}

