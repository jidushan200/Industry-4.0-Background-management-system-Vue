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
public class ToolUnqualifiedReport implements Serializable {

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
	 * @Fields check_id : 质检ID
	 */
	@ApiModelProperty(name = "checkId", notes = "质检ID")
	private Long checkId;
	/**
	 * @Fields full_number : 物料条码
	 */
	@ApiModelProperty(name = "fullNumber", notes = "物料条码")
	private String fullNumber;
	/**
	 * @Fields tool_number : 物料编码
	 */
	@ApiModelProperty(name = "toolNumber", notes = "物料编码")
	private String toolNumber;
	/**
	 * @Fields tool_name : 物料名称
	 */
	@ApiModelProperty(name = "toolName", notes = "物料名称")
	private String toolName;
	/**
	 * @Fields tool_map : 物料图号
	 */
	@ApiModelProperty(name = "toolMap", notes = "物料图号")
	private String toolMap;

	/**
	 * @Fields tool_seq : 顺序号
	 */
	@ApiModelProperty(name = "toolSeq", notes = "顺序号")
	private String toolSeq;
	/**
	 * @Fields report_type : 报告类型(1-新刀,2-刃磨,3-涂层)
	 */
	@ApiModelProperty(name = "reportType", notes = "报告类型(1-新刀,2-刃磨,3-涂层)")
	private Integer reportType;
	/**
	 * @Fields new_audit_status :
	 *         新刀审核状态(0-新增待提交,1-已提交待审核,-2-采购部已退回,-3-不让步待采购部退回,2-让步待工艺部审核,3-让步待质检部领导审批,4-已审批待入库,5-已入库,-1-采购已退回
	 */
	@ApiModelProperty(name = "newAuditStatus", notes = "新刀审核状态(0-新增待提交,1-已提交待审核,-2-采购部已退回,-3-不让步待采购部退回,2-让步待工艺部审核,3-让步待质检部领导审批,4-已审批待入库,5-已入库,-1-采购已退回")
	private Integer newAuditStatus;
	/**
	 * @Fields repair_audit_status :
	 *         刃磨审核状态(0-新增待提交,1-已提交待审核,-2-待报废,-3-待刃磨,2-库房已报废,3-刃磨班已重修)
	 */
	@ApiModelProperty(name = "repairAuditStatus", notes = "刃磨审核状态(0-新增待提交,1-已提交待审核,-2-待报废,-3-待刃磨,2-库房已报废,3-刃磨班已重修)")
	private Integer repairAuditStatus;
	/**
	 * @Fields coat_audit_status :
	 *         涂层审核状态(0-新增待提交,1-已提交待审核,-2-生产部已退回,2-让步待工艺部审核,-3-不让步待生产部退回,3-让步待质检部领导审批,4-已审批待入库,5-入库,-1-生产部退回
	 */
	@ApiModelProperty(name = "coatAuditStatus", notes = "涂层审核状态(0-新增待提交,1-已提交待审核,-2-生产部已退回,2-让步待工艺部审核,-3-不让步待生产部退回,3-让步待质检部领导审批,4-已审批待入库,5-入库,-1-生产部退回")
	private Integer coatAuditStatus;
	/**
	 * @Fields reporter_id : 报告人Id
	 */
	@ApiModelProperty(name = "reporterId", notes = "报告人Id")
	private Long reporterId;
	/**
	 * @Fields reporter_name : 报告人姓名
	 */
	@ApiModelProperty(name = "reporterName", notes = "报告人姓名")
	private String reporterName;
	/**
	 * @Fields report_time : 报告时间
	 */
	@ApiModelProperty(name = "reportTime", notes = "报告时间")
	private Date reportTime;
	/**
	 * @Fields report_desc : 报告描述
	 */
	@ApiModelProperty(name = "reportDesc", notes = "报告描述")
	private String reportDesc;
	/**
	 * @Fields supplier_id : 供应商ID
	 */
	@ApiModelProperty(name = "supplierId", notes = "供应商ID")
	private Long supplierId;
	

	@ApiModelProperty(name = "auditor", notes = "审核人")
	private String auditor;

	private ToolCheck toolCheck;
	
	private List<ToolApplyAudit> auditList;
	
	private List<Integer> statusList;
	
}
