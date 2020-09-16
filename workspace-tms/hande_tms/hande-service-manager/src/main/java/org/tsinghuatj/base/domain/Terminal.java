package org.tsinghuatj.base.domain;

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
public class Terminal implements Serializable {
	
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
	 * @Fields terminal_code : 终端编码
	 */
	@ExcelField(title = "终端编码", order = 1)
	@ApiModelProperty(name = "terminalCode", notes = "终端编码")
	private String terminalCode;
	/**
	 * @Fields department_id : 部门ID
	 */
	@ApiModelProperty(name = "departmentId", notes = "部门ID")
	private Long departmentId;
	/**
	 * @Fields department_name : 部门名称
	 */
	
	@ExcelField(title = "部门名称", order = 2)
	@ApiModelProperty(name = "departmentName", notes = "部门名称")
	private String departmentName;
	/**
	 * @Fields manager_id : 管理人ID
	 */
	@ApiModelProperty(name = "managerId", notes = "管理人ID")
	private Long managerId;
	/**
	 * @Fields manager_name : 管理人姓名
	 */
	@ExcelField(title = "管理人", order = 3)
	@ApiModelProperty(name = "managerName", notes = "管理人")
	private String managerName;
	/**
	 * @Fields Ip : IP
	 */
	@ExcelField(title = "IP地址", order = 4)
	@ApiModelProperty(name = "ip", notes = "IP")
	private String ip;
	
	@Override
    public String toString() {
        return  "Terminal " + 
                    "[pkId=" + pkId + "]"
                   + ",[createUser=" + createUser + "]"
                                   + ",[createTime=" + createTime + "]"
                                   + ",[updateUser=" + updateUser + "]"
                                   + ",[updateTime=" + updateTime + "]"
                                   + ",[delMark=" + delMark + "]"
                                   + ",[terminalCode=" + terminalCode + "]"
                                   + ",[departmentId=" + departmentId + "]"
                                   + ",[departmentName=" + departmentName + "]"
                                   + ",[managerId=" + managerId + "]"
                                   + ",[managerName=" + managerName + "]"
                                   + ",[ip=" + ip + "]"
                ;
	}
}

