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
public class ToolBladeCompose implements Serializable {

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
	 * @Fields head_number : 刀条组合编号
	 */
	@ApiModelProperty(name = "headNumber", notes = "刀盘编号")
	private String headNumber;
	/**
	 * @Fields head_name : 刀条组合名称
	 */
	@ApiModelProperty(name = "headName", notes = "刀条组合名称")
	private String headName;
	/**
	 * @Fields tool_number : 物料编码
	 */
	/*
	 * @ApiModelProperty(name = "toolNumber", notes = "物料编码") private String
	 * toolNumber;
	 *//**
		 * @Fields tool_name : 物料名称
		 */
	/*
	 * @ApiModelProperty(name = "toolName", notes = "物料名称") private String
	 * toolName;
	 *//**
		 * @Fields tool_map : 物料图号
		 *//*
		 * @ApiModelProperty(name = "toolMap", notes = "物料图号") private String
		 * toolMap;
		 */
	/**
	 * @Fields part_code : 制件编码
	 */
	@ApiModelProperty(name = "partCode", notes = "制件编码")
	private String partCode;
	/**
	 * @Fields plate_number : 刀盘编号
	 */
	@ApiModelProperty(name = "plateNumber", notes = "刀盘编号")
	private String plateNumber;

	/**
	 * @Fields last_plate_number : 上次刀盘编号
	 */
	@ApiModelProperty(name = "lastPlateNumber", notes = "上次刀盘编号")
	private String lastPlateNumber;

	/**
	 * @Fields supplement_Qty : 补充数量
	 */
	/*
	 * @ApiModelProperty(name = "supplementQty", notes = "补充数量") private Integer
	 * supplementQty;
	 * 
	 *//**
		 * @Fields supplement_Times : 补充次数
		 */
	/*
	 * @ApiModelProperty(name = "supplementTimes", notes = "补充次数") private
	 * Integer supplementTimes;
	 * 
	 *//**
		 * @Fields tool_qty : 物料数量
		 *//*
		 * @ApiModelProperty(name = "toolQty", notes = "物料数量") private Integer
		 * toolQty;
		 */
	/**
	 * @Fields tool_status : 刀具状态(1-生产中)
	 */
	@ApiModelProperty(name = "toolStatus", notes = "刀具状态(1-生产中)")
	private Integer toolStatus;
	/**
	 * @Fields department_id : 领用部门id
	 */
	@ApiModelProperty(name = "departmentId", notes = "领用部门id")
	private Long departmentId;
	/**
	 * @Fields department_name : 领用部门名称
	 */
	@ApiModelProperty(name = "departmentName", notes = "领用部门名称")
	private String departmentName;
	/**
	 * @Fields team_id : 班组id
	 */
	@ApiModelProperty(name = "teamId", notes = "班组id")
	private Long teamId;
	/**
	 * @Fields team_name : 班组名称
	 */
	@ApiModelProperty(name = "teamName", notes = "班组名称")
	private String teamName;

	/**
	 * @Fields each_process_qty : 每次加工数
	 */
	@ApiModelProperty(name = "eachProcessQty", notes = "每次加工数")
	private Integer eachProcessQty;

	/**
	 * @Fields process_times : 加工次数
	 */
	@ApiModelProperty(name = "processTimes", notes = "加工次数")
	private Integer processTimes;
	/**
	 * @Fields production_qty : 生产产品数量
	 */
	@ApiModelProperty(name = "productionQty", notes = "生产产品数量")
	private Integer productionQty;

	/**
	 * @Fields partList : 制件
	 */
	@ApiModelProperty(name = "partList", notes = "制件")
	private List<ToolBladeComposePart> partList;

	@ApiModelProperty(name = "processList", notes = "processList")
	private List<ToolBladeProcess> processList;

	@ApiModelProperty(name = "repairList", notes = "repairList")
	private List<ToolRepair> repairList;

	@ApiModelProperty(name = "coatList", notes = "coatList")
	private List<ToolCoat> coatList;

	@ApiModelProperty(name = "detailList", notes = "detailList")
	private List<ToolBladeComposeDetail> detailList;

	private List<ToolBladeScrapDetail> scrapList;

}
