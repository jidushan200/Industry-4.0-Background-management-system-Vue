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
public class ToolBladeScrap implements Serializable {

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
	 * @Fields compose_number : 刀头组合码
	 */
	@ApiModelProperty(name = "composeNumber", notes = "刀头组合码")
	private String composeNumber;

	/**
	 * @Fields head_number : 刀条组合码
	 */
	@ApiModelProperty(name = "headNumber", notes = "刀盘编码")
	private String headNumber;

	/**
	 * @Fields head_name : 刀条组合名称
	 */
	@ApiModelProperty(name = "headName", notes = "刀条组合名称")
	private String headName;

	/**
	 * @Fields production_qty : 已生产数量
	 */
	@ApiModelProperty(name = "productionQty", notes = "已生产数量")
	private Integer productionQty;
	/**
	 * @Fields scrap_resion : 报废原因(1-正常报废；2-异常报废)
	 */
	@ApiModelProperty(name = "scrapResion", notes = "报废原因(1-正常报废；2-异常报废)")
	private Integer scrapResion;
	/**
	 * @Fields apply_status : 申请状态(0-新增待提交;1-提交待审核;2-工艺部审核通过;-1-工艺部已驳回)
	 */
	@ApiModelProperty(name = "applyStatus", notes = "申请状态(0-新增待提交;1-提交待审核;2-工艺部审核通过;-1-工艺部已驳回)")
	private Integer applyStatus;
	/**
	 * @Fields scrap_remark : 报废原因说明
	 */
	@ApiModelProperty(name = "scrapRemark", notes = "报废原因说明")
	private String scrapRemark;
	/**
	 * @Fields department_id : 申请部门ID
	 */
	@ApiModelProperty(name = "departmentId", notes = "申请部门ID")
	private Long departmentId;
	/**
	 * @Fields department_name : 申请部门
	 */
	@ApiModelProperty(name = "departmentName", notes = "申请部门")
	private String departmentName;
	/**
	 * @Fields applier_id : 申请人ID
	 */
	@ApiModelProperty(name = "applierId", notes = "申请人ID")
	private Long applierId;
	/**
	 * @Fields applier_name : 申请人
	 */
	@ApiModelProperty(name = "applierName", notes = "申请人")
	private String applierName;

	private List<ToolBladeScrapDetail> detailList;

}
