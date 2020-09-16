package org.tsinghuatj.mould.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.tsinghuatj.tool.domain.ToolAppendix;

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
public class MouldCheck implements Serializable {
	
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
	 * @Fields mould_id : mouldId
	 */
	@ApiModelProperty(name = "mouldId", notes = "mouldId")
	private Long mouldId;
	/**
	 * @Fields receipt_id : 新模具ID
	 */
	@ApiModelProperty(name = "receiptId", notes = "新模具ID")
	private Long receiptId;
	/**
	 * @Fields mould_number : 物料编码
	 */
	@ApiModelProperty(name = "mouldNumber", notes = "物料编码")
	private String mouldNumber;
	/**
	 * @Fields mould_name : 物料名称
	 */
	@ApiModelProperty(name = "mouldName", notes = "物料名称")
	private String mouldName;
	/**
	 * @Fields mould_map : 物料图号
	 */
	@ApiModelProperty(name = "mouldMap", notes = "物料图号")
	private String mouldMap;
	/**
	 * @Fields mould_seq : 模具序号
	 */
	@ApiModelProperty(name = "mouldSeq", notes = "模具序号")
	private String mouldSeq;
	/**
	 * @Fields supplier_id : 新模具ID
	 */
	@ApiModelProperty(name = "supplierId", notes = "新模具ID")
	private Long supplierId;
	/**
	 * @Fields supplier_name : 供应商名称
	 */
	@ApiModelProperty(name = "supplierName", notes = "供应商名称")
	private String supplierName;
	/**
	 * @Fields self_check_report : 自检报告（有/没有）
	 */
	@ApiModelProperty(name = "selfCheckReport", notes = "自检报告（有/没有）")
	private Integer selfCheckReport;
	/**
	 * @Fields checker_id : 质检人ID
	 */
	@ApiModelProperty(name = "checkerId", notes = "质检人ID")
	private Long checkerId;
	/**
	 * @Fields checker : 质检人
	 */
	@ApiModelProperty(name = "checker", notes = "质检人")
	private String checker;
	/**
	 * @Fields check_time : 质检时间
	 */
	@ApiModelProperty(name = "checkTime", notes = "质检时间")
	private Date checkTime;
	/**
	 * @Fields check_status : 合格状态（1-合格；2-不合格；3-不合格已处理）
	 */
	@ApiModelProperty(name = "checkStatus", notes = "合格状态（1-合格；2-不合格；3-不合格已处理）")
	private Integer checkStatus;
	/**
	 * @Fields check_type : 质检类型（1-新模具质检；2-修磨质检）
	 */
	@ApiModelProperty(name = "checkType", notes = "质检类型（1-新模具质检；2-修磨质检）")
	private Integer checkType;
	/**
	 * @Fields report_status : 质检状态（1-待质检；2-已质检）
	 */
	@ApiModelProperty(name = "reportStatus", notes = "质检状态（1-待质检；2-已质检）")
	private Integer reportStatus;
	/**
	 * @Fields itemList : 质检项
	 */
	@ApiModelProperty(name = "itemList", notes = "质检项")
	private List<MouldCheckItem> itemList;
	/**
	 * @Fields itemList : 质检项
	 */
	@ApiModelProperty(name = "checkItemList", notes = "质检项")
	private List<CheckItem> checkItemList;
	
	private List<ToolAppendix> appendixList;
}

