package org.tsinghuatj.tool.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
public class ToolWaitCheck implements Serializable {
	
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
	 * @Fields full_number : 完整编码
	 */
	@ApiModelProperty(name = "fullNumber", notes = "完整编码")
	private String fullNumber;
	/**
	 * @Fields tool_number : 刀具编码
	 */
	@ApiModelProperty(name = "toolNumber", notes = "刀具编码")
	private String toolNumber;
	/**
	 * @Fields tool_name : 刀具名称
	 */
	@ApiModelProperty(name = "toolName", notes = "刀具名称")
	private String toolName;
	/**
	 * @Fields tool_map : 刀具图号
	 */
	@ApiModelProperty(name = "toolMap", notes = "刀具图号")
	private String toolMap;
	/**
	 * @Fields tool_seq : 刀具序号
	 */
	@ApiModelProperty(name = "toolSeq", notes = "刀具序号")
	private String toolSeq;
	/**
	 * @Fields type_id : 类型ID
	 */
	@ApiModelProperty(name = "typeId", notes = "类型ID")
	private Integer typeId;
	
	/**
	 * @Fields receipt_id : 收货单ID
	 */
	@ApiModelProperty(name = "receiptId", notes = "收货单ID")
	private Long receiptId;
	/**
	 * @Fields isNew : 是否新刀条
	 */
	@ApiModelProperty(name = "isNew", notes = "isNew")
	private Integer isNew;
	/**
	 * @Fields type_Name : 类型名称
	 */
	@ApiModelProperty(name = "typeName", notes = "刀类型名称")
	private String typeName;
	/**
	 * @Fields tool_qty : 刀具数量
	 */
	@ApiModelProperty(name = "toolQty", notes = "刀具数量")
	private Integer toolQty;
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
	 * @Fields check_type : 检验类型(1-新刀质检;2-刃磨修磨;3-涂层质检)
	 */
	@ApiModelProperty(name = "checkType", notes = "检验类型(1-新刀质检;2-刃磨修磨;3-涂层质检)")
	private Integer checkType;
	/**
	 * @Fields check_status : 检验状态(0-待检;1-检验中;2-检验完成)
	 */
	@ApiModelProperty(name = "checkStatus", notes = "检验状态(0-待检;1-检验中;2-检验完成)")
	private Integer checkStatus;
	
	/**
	 * @Fields check_id : 检验Id
	 */
	@ApiModelProperty(name = "checkId", notes = "检验Id")
	private Long checkId;
	
	private List<ToolBladeComposeDetail> detailList;
	
	
}

