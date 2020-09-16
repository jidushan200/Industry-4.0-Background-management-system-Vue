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
public class ToolAppendix implements Serializable {

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
	 * @Fields check_id : 检验ID
	 */
	@ApiModelProperty(name = "checkId", notes = "检验ID")
	private Long checkId;
	/**
	 * @Fields tool_id : 刀具ID
	 */
	@ApiModelProperty(name = "toolId", notes = "刀具ID")
	private Long toolId;
	
	/**
	 * @Fields apply_id : 申请单ID
	 */
	@ApiModelProperty(name = "applyId", notes = "申请单ID")
	private Long applyId;
	/**
	 * @Fields source_name : 附件新类型
	 */
	@ApiModelProperty(name = "sourceName", notes = "附件新类型")
	private String sourceName;
	/**
	 * @Fields new_name : 附件新名称
	 */
	@ApiModelProperty(name = "newName", notes = "附件新名称")
	private String newName;
	/**
	 * @Fields appendix_size : 附件大小
	 */
	@ApiModelProperty(name = "appendixSize", notes = "附件大小")
	private Long appendixSize;
	/**
	 * @Fields file_ext : 附件后缀名
	 */
	@ApiModelProperty(name = "fileExt", notes = "附件后缀名")
	private String fileExt;
	/**
	 * @Fields document_type : 文档类型
	 */
	@ApiModelProperty(name = "documentType", notes = "文档类型")
	private String documentType;
	/**
	 * @Fields appendix_time : 附件时间
	 */
	@ApiModelProperty(name = "appendixTime", notes = "附件时间")
	private Date appendixTime;
	/**
	 * @Fields appdenix_type : 附件类型（1-新刀质检报告;2-刃磨质检报告;3-涂层质检报告）
	 */
	@ApiModelProperty(name = "appdenixType", notes = "附件类型（1-新刀质检报告;2-刃磨质检报告;3-涂层质检报告）")
	private Integer appdenixType;
	/**
	 * @Fields tool_type : 刀具类型（1-滚刀；2-拉刀；3-剃齿刀；4-刀头；5-刀条）
	 */
	@ApiModelProperty(name = "toolType", notes = "刀具类型（1-滚刀；2-拉刀；3-剃齿刀；4-刀头；5-刀条）")
	private Integer toolType;

	@Override
	public String toString() {
		return "ToolAppendix " + "[pkId=" + pkId + "]" + ",[createUser=" + createUser + "]" + ",[createTime="
				+ createTime + "]" + ",[updateUser=" + updateUser + "]" + ",[updateTime=" + updateTime + "]"
				+ ",[delMark=" + delMark + "]" + ",[checkId=" + checkId + "]" + ",[toolId=" + toolId + "]"
				+ ",[sourceName=" + sourceName + "]" + ",[newName=" + newName + "]" + ",[appendixSize=" + appendixSize
				+ "]" + ",[fileExt=" + fileExt + "]" + ",[documentType=" + documentType + "]" + ",[appendixTime="
				+ appendixTime + "]" + ",[appdenixType=" + appdenixType + "]" + ",[toolType=" + toolType + "]";
	}
}
