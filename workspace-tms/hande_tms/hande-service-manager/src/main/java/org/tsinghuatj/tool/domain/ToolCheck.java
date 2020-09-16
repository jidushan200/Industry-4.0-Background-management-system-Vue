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
public class ToolCheck implements Serializable {

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
	 * @Fields full_number : 刀具编码
	 */
	@ApiModelProperty(name = "fullNumber", notes = "刀具编码")
	private String fullNumber;
	/**
	 * @Fields tool_number : 刀具编码
	 */
	@ApiModelProperty(name = "toolNumber", notes = "刀具编码")
	private String toolNumber;

	/**
	 * @Fields type_id : 类型id
	 */
	@ApiModelProperty(name = "typeId", notes = "类型id")
	private Integer typeId;
	/**
	 * @Fields type_name : 类型名称
	 */
	@ApiModelProperty(name = "typeName", notes = "类型名称")
	private String typeName;
	/**
	 * 
	 */
	@ApiModelProperty(name = "toolName", notes = "刀具名称")
	private String toolName;
	/**
	 * 
	 */
	@ApiModelProperty(name = "toolSeq", notes = "顺序号")
	private String toolSeq;

	/**
	 * 
	 */
	@ApiModelProperty(name = "toolQty", notes = "刀具数量")
	private Integer toolQty;

	/**
	 * 
	 */
	@ApiModelProperty(name = "warehouseCode", notes = "库位号")
	private String warehouseCode;
	/**
	 * 
	 */
	@ApiModelProperty(name = "toolMap", notes = "图号")
	private String toolMap;

	/**
	 * @Fields wait_check_id : waitCheckId
	 */
	@ApiModelProperty(name = "waitCheckId", notes = "待检id")
	private Long waitCheckId;

	/**
	 * @Fields supplier_id : supplierId
	 */
	@ApiModelProperty(name = "supplierId", notes = "supplierId")
	private Long supplierId;
	/**
	 * 
	 */
	@ApiModelProperty(name = "supplierName", notes = "供应商名称")
	private String supplierName;
	/**
	 * @Fields deliveryer : deliveryer
	 */
	@ApiModelProperty(name = "deliveryer", notes = "送货人")
	private String deliveryer;
	/**
	 * @Fields delivery_time : 送货时间
	 */
	@ApiModelProperty(name = "deliveryTime", notes = "送货时间")
	private Date deliveryTime;
	/**
	 * @Fields standard_id : 标准Id
	 */
	@ApiModelProperty(name = "standardId", notes = "标准Id")
	private Long standardId;

	@ApiModelProperty(name = "standardCode", notes = "标准编码")
	private String standardCode;

	/**
	 * @Fields standard_desc : 标准描述
	 */
	@ApiModelProperty(name = "standardDesc", notes = "标准描述")
	private String standardDesc;

	/**
	 * @Fields check_type : 检验类型(1-新刀检验；2-刃磨检验；3-涂层检验)
	 */
	@ApiModelProperty(name = "checkType", notes = "检验类型(1-新刀检验；2-刃磨检验；3-涂层检验)")
	private Integer checkType;
	/**
	 * @Fields check_result : 检验结果(1-合格;2-不合格)
	 */
	@ApiModelProperty(name = "checkResult", notes = "检验结果(1-合格;2-不合格)")
	private Integer checkResult;
	
	/**
	 * @Fields check_time : 检验时间
	 */
	@ApiModelProperty(name = "checkTime", notes = "检验时间")
	private Date checkTime;
	/**
	 * @Fields checker : 检验人
	 */
	@ApiModelProperty(name = "checker", notes = "检验人")
	private String checker;

	/**
	 * @Fields check_status : 检验状态(0-保存;1-提交)
	 */
	@ApiModelProperty(name = "checkStatus", notes = "检验状态(0-保存;1-提交)")
	private Integer checkStatus;
	
	/**
	 * @Fields remark : 说明
	 */
	@ApiModelProperty(name = "remark", notes = "说明")
	private String remark;

	private List<ToolCheckResult> resultList;

	private List<ToolAppendix> appendixList;
	
	private List<ToolBladeComposeDetail> detailList;

}
