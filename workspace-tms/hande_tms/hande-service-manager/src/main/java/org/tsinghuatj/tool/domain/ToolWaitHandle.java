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
public class ToolWaitHandle implements Serializable {

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
	 * @Fields compose_number : 组合编码
	 */
	@ApiModelProperty(name = "composeNumber", notes = "组合编码")
	private String composeNumber;
	/**
	 * @Fields headName : 组合名称
	 */
	@ApiModelProperty(name = "headNumber", notes = "组合名称")
	private String headNumber;
	/**
	 * @Fields headName : 组合名称
	 */
	@ApiModelProperty(name = "headName", notes = "组合名称")
	private String headName;
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
	 * @Fields tool_seq : 刀具顺序号
	 */
	@ApiModelProperty(name = "toolSeq", notes = "刀具顺序号")
	private String toolSeq;

	/**
	 * @Fields receipt_id : 收货单id
	 */
	@ApiModelProperty(name = "receiptId", notes = "收货单")
	private Long receiptId;
	/**
	 * @Fields type_id : 类型ID
	 */
	@ApiModelProperty(name = "typeId", notes = "类型ID")
	private Integer typeId;
	/**
	 * @Fields supplier_id : 供应商id
	 */
	@ApiModelProperty(name = "supplierId", notes = "供应商id")
	private Long supplierId;
	/**
	 * @Fields is_new : 是否新刀(1-是;0-不是)
	 */
	@ApiModelProperty(name = "isNew", notes = "是否新刀(1-是;0-不是)")
	private Integer isNew;
	/**
	 * @Fields tool_qty : 刀具数量
	 */
	@ApiModelProperty(name = "toolQty", notes = "刀具数量")
	private Integer toolQty;
	/**
	 * @Fields handle_type : 处理类型(2-刃磨;3-涂层)
	 */
	@ApiModelProperty(name = "handleType", notes = "处理类型(2-刃磨;3-涂层)")
	private Integer handleType;
	/**
	 * @Fields handle_result : 处理结果(0-待处理;1-已处理)
	 */
	@ApiModelProperty(name = "handleResult", notes = "处理结果(0-待处理;1-已处理)")
	private Integer handleResult;

	/**
	 * @Fields deliever : 送货人
	 */
	@ApiModelProperty(name = "deliever", notes = "送货人")
	private String deliever;

	@ApiModelProperty(name = "departmentId", notes = "申请部门Id")
	private Long departmentId;

	@ApiModelProperty(name = "departmentName", notes = "申请部门")
	private String departmentName;

	@ApiModelProperty(name = "applierId", notes = "申请人id")
	private Long applierId;

	@ApiModelProperty(name = "applierName", notes = "申请人")
	private String applierName;

	@ApiModelProperty(name = "supplierName", notes = "供应商")
	private String supplierName;

	@ApiModelProperty(name = "deliveryTime", notes = "送货时间")
	private Date deliveryTime;
	
	private List<ToolBladeComposeDetail> detailList;

}
