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
public class MouldEmbryo implements Serializable {
	
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
	 * @Fields create_user_name : 创建用户姓名
	 */
	@ApiModelProperty(name = "createUserName", notes = "创建用户姓名")
	private String createUserName;	
	/**
	 * @Fields update_user_name : 更新用户姓名
	 */
	@ApiModelProperty(name = "updateUserName", notes = "创建用户姓名")
	private String updateUserName;
	/**
	 * @Fields embryo_code : 模具坯编码
	 */
	@ExcelField(title = "模具坯编码", order = 1)
	@ApiModelProperty(name = "embryoCode", notes = "模具坯编码")
	private String embryoCode;
	/**
	 * @Fields embryo_name : 模具坯名称
	 */
	@ExcelField(title = "模具坯名称", order = 2)
	@ApiModelProperty(name = "embryoName", notes = "模具坯名称")
	private String embryoName;
	/**
	 * @Fields embryo_desc : 模具坯说明
	 */
	@ApiModelProperty(name = "embryoDesc", notes = "模具坯说明")
	private String embryoDesc;
	
	@Override
    public String toString() {
        return  "MouldEmbryo " + 
                    "[pkId=" + pkId + "]"
                   + ",[createUser=" + createUser + "]"
                                   + ",[createTime=" + createTime + "]"
                                   + ",[updateUser=" + updateUser + "]"
                                   + ",[updateTime=" + updateTime + "]"
                                   + ",[delMark=" + delMark + "]"
                                   + ",[embryoCode=" + embryoCode + "]"
                                   + ",[embryoName=" + embryoName + "]"
                                   + ",[embryoDesc=" + embryoDesc + "]"
                ;
	}
}

