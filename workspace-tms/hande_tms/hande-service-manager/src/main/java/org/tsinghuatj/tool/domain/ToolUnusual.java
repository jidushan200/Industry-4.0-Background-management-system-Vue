package org.tsinghuatj.tool.domain;

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
public class ToolUnusual implements Serializable {
	
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
	 * @Fields tool_name : 刀具名称
	 */
	@ExcelField(title = "物料名称", order = 1)
	@ApiModelProperty(name = "toolName", notes = "刀具名称")
	private String toolName;
	/**
	 * @Fields tool_number : 刀具编码
	 */
	@ExcelField(title = "物料编码", order = 2)
	@ApiModelProperty(name = "toolNumber", notes = "刀具编码")
	private String toolNumber;
	/**
	 * @Fields full_number : fullNumber
	 */
	@ExcelField(title = "物料条码", order = 3)
	@ApiModelProperty(name = "fullNumber", notes = "fullNumber")
	private String fullNumber;
	/**
	 * @Fields part_id : 制件ID
	 */
	@ExcelField(title = "制件ID", order = 4)
	@ApiModelProperty(name = "partId", notes = "制件ID")
	private Long partId;
	/**
	 * @Fields part_name : 制件名称
	 */
	@ApiModelProperty(name = "partName", notes = "制件名称")
	private String partName;
	/**
	 * @Fields equipment_id : 设备ID
	 */
	@ApiModelProperty(name = "equipmentId", notes = "设备ID")
	private Long equipmentId;
	/**
	 * @Fields equipment_name : 设备名称
	 */
	@ApiModelProperty(name = "equipmentName", notes = "设备名称")
	private String equipmentName;
	/**
	 * @Fields unusual_resion : 异常原因
	 */
	@ExcelField(title = "异常原因", order = 5)
	@ApiModelProperty(name = "unusualResion", notes = "异常原因")
	private String unusualResion;
	/**
	 * @Fields unusual_time : 异常时间
	 */
	@ExcelField(title = "异常时间", order = 6)
	@ApiModelProperty(name = "unusualTime", notes = "异常时间")
	private Date unusualTime;
	/**
	 * @Fields measures : 处理措施(0-让步使用;1-刃磨)
	 */
	@ExcelField(title = "处理措施", order = 7)
	@ApiModelProperty(name = "measures", notes = "处理措施(0-让步使用;1-刃磨)")
	private Integer measures;
	/**
	 * @Fields remark : 说明
	 */
	@ApiModelProperty(name = "remark", notes = "说明")
	private String remark;
	
	@Override
    public String toString() {
        return  "ToolUnusual " + 
                    "[pkId=" + pkId + "]"
                   + ",[createUser=" + createUser + "]"
                                   + ",[createTime=" + createTime + "]"
                                   + ",[updateUser=" + updateUser + "]"
                                   + ",[updateTime=" + updateTime + "]"
                                   + ",[delMark=" + delMark + "]"
                                   + ",[toolId=" + toolId + "]"
                                   + ",[toolName=" + toolName + "]"
                                   + ",[toolNumber=" + toolNumber + "]"
                                   + ",[fullNumber=" + fullNumber + "]"
                                   + ",[partId=" + partId + "]"
                                   + ",[partName=" + partName + "]"
                                   + ",[equipmentId=" + equipmentId + "]"
                                   + ",[equipmentName=" + equipmentName + "]"
                                   + ",[unusualResion=" + unusualResion + "]"
                                   + ",[unusualTime=" + unusualTime + "]"
                                   + ",[measures=" + measures + "]"
                                   + ",[remark=" + remark + "]"
                ;
	}
}

