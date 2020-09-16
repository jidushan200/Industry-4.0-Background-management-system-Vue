package org.tsinghuatj.mould.domain;

import java.io.Serializable;
import java.util.Date;

import org.tsinghuatj.framework.utils.excel.annotation.ExcelField;

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
public class MouldReceipt implements Serializable {
	
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
	 * @Fields mould_number : 模具编码
	 */
	@ExcelField(title = "物料编码", order = 1)
	@ApiModelProperty(name = "mouldNumber", notes = "模具编码")
	private String mouldNumber;
	/**
	 * @Fields mould_name : 模具名称
	 */
	@ExcelField(title = "物料名称", order = 3)
	@ApiModelProperty(name = "mouldName", notes = "模具名称")
	private String mouldName;
	/**
	 * @Fields mould_map : 模具图号
	 */
	@ExcelField(title = "物料图号", order = 4)
	@ApiModelProperty(name = "mouldMap", notes = "模具图号")
	private String mouldMap;
	/**
	 * @Fields mould_seq : 模具序号
	 */
	@ExcelField(title = "物料序号", order = 2)
	@ApiModelProperty(name = "mouldSeq", notes = "模具序号")
	private String mouldSeq;
	/**
	 * @Fields mould_status : 模具状态(0-新模具待检；1-合格入库;2-不合格处理;)
	 */
	@ApiModelProperty(name = "mouldStatus", notes = "模具状态(0-新模具待检；1-合格入库;2-不合格处理;)")
	private Integer mouldStatus;
	/**
	 * @Fields mould_type : 模具类型（1-普段模具；2-精锻模具）
	 */
	@ApiModelProperty(name = "mouldType", notes = "模具类型（1-普段模具；2-精锻模具）")
	private Integer mouldType;
	/**
	 * @Fields life_max : 最大寿命
	 */
	@ApiModelProperty(name = "lifeMax", notes = "最大寿命")
	private Integer lifeMax;
	/**
	 * @Fields heat_number : 热处理编码
	 */
	@ExcelField(title = "热处理批次号", order = 5)
	@ApiModelProperty(name = "heatNumber", notes = "热处理编码")
	private String heatNumber;
	/**
	 * @Fields surface_number : 表面处理编码
	 */
	@ExcelField(title = "表面处理批次号", order = 6)
	@ApiModelProperty(name = "surfaceNumber", notes = "表面处理编码")
	private String surfaceNumber;
	/**
	 * @Fields check_type : 质检类型（1-新模具质检；2-修磨质检）
	 */
	@ApiModelProperty(name = "checkType", notes = "质检类型（1-新模具质检；2-修磨质检）")
	private Integer checkType;
	
	@ExcelField(title = "质检类型", order = 7)
	@ApiModelProperty(name = "checkTypeName", notes = "质检类型")
	private String checkTypeName;
	/**
	 * @Fields embryo_code : 模具胚编码
	 */
	@ApiModelProperty(name = "embryoCode", notes = "模具胚编码")
	private String embryoCode;
	/**
	 * @Fields embryo_name : 模具胚名称
	 */
	@ApiModelProperty(name = "embryoName", notes = "模具胚名称")
	private String embryoName;
	/**
	 * @Fields scrip_handler : 报废处理人
	 */
	@ApiModelProperty(name = "scripHandler", notes = "报废处理人")
	private String scripHandler;
	/**
	 * @Fields scrip_handler_time : 报废处理时间
	 */
	@ApiModelProperty(name = "scripHandlerTime", notes = "报废处理时间")
	private Date scripHandlerTime;
}

