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
public class CheckResult implements Serializable {
	
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
	 * @Fields col_id : 栏目ID
	 */
	@ExcelField(title = "栏目ID", order = 1)
	@ApiModelProperty(name = "colId", notes = "栏目ID")
	private Long colId;
	/**
	 * @Fields check_col : 检验栏目
	 */
	@ExcelField(title = "检验栏目", order = 2)
	@ApiModelProperty(name = "checkCol", notes = "检验栏目")
	private String checkCol;
	/**
	 * @Fields check_result : 检验结果
	 */
	@ExcelField(title = "检验结果", order = 3)
	@ApiModelProperty(name = "checkResult", notes = "检验结果")
	private String checkResult;
	
	@Override
    public String toString() {
        return  "CheckResult " + 
                    "[pkId=" + pkId + "]"
                   + ",[createUser=" + createUser + "]"
                                   + ",[createTime=" + createTime + "]"
                                   + ",[updateUser=" + updateUser + "]"
                                   + ",[updateTime=" + updateTime + "]"
                                   + ",[delMark=" + delMark + "]"
                                   + ",[colId=" + colId + "]"
                                   + ",[checkCol=" + checkCol + "]"
                                   + ",[checkResult=" + checkResult + "]"
                ;
	}
}

