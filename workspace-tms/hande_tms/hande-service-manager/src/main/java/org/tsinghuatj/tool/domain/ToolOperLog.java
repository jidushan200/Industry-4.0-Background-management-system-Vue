package org.tsinghuatj.tool.domain;

import java.io.Serializable;
import java.util.Date;

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
public class ToolOperLog implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/**
	 * @Fields pk_id : 主键
	 */
	@ApiModelProperty(name = "pkId", notes = "主键")
	private String pkId;
	/**
	 * @Fields create_user : 创建用户
	 */
	@ApiModelProperty(name = "createUser", notes = "创建用户")
	private Long createUser;
	/**
	 * @Fields create_user_name : 创建用户
	 */
	@ApiModelProperty(name = "createUserName", notes = "创建用户")
	private String createUserName;
	/**
	 * @Fields create_time : 创建时间
	 */
	@ApiModelProperty(name = "createTime", notes = "创建时间")
	private Date createTime;
	/**
	 * @Fields full_number : 刀具条码
	 */
	@ApiModelProperty(name = "fullNumber", notes = "刀具条码")
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
	 * @Fields oper_type : 操作类型(1-新刀入库;2-领用出库;3-生产返库;4-刃磨出库;5-刃磨;6-刃磨检验;7-涂层出库;8-涂层检验;9-刀盘安装;10-刀具报废申请;11-报废审核;12-执行报废)
	 */
	@ApiModelProperty(name = "operType", notes = "操作类型(1-新刀入库;2-领用出库;3-生产返库;4-刃磨出库;5-刃磨;6-刃磨检验;7-涂层出库;8-涂层检验;9-刀盘安装;10-刀具报废申请;11-报废审核;12-执行报废)")
	private Integer operType;
	/**
	 * @Fields operate_info : 操作内容
	 */
	@ApiModelProperty(name = "operateInfo", notes = "操作内容")
	private String operateInfo;
	/**
	 * @Fields operate_remark : 操作备注
	 */
	@ApiModelProperty(name = "operateRemark", notes = "操作备注")
	private String operateRemark;
	
	
}

