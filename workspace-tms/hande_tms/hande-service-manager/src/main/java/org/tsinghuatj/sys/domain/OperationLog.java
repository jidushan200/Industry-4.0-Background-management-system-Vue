package org.tsinghuatj.sys.domain;

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
public class OperationLog implements Serializable {

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
	 * @Fields del_mark : 删除标识(1正常, 2已删除)
	 */
	@ApiModelProperty(name = "delMark", notes = "删除标识(1正常, 2已删除)")
	private Integer delMark;
	/**
	 * @Fields operate_id : 操作人ID
	 */
	@ApiModelProperty(name = "operateId", notes = "操作人ID")
	private Long operateId;
	/**
	 * @Fields operate_name : 操作人
	 */
	@ApiModelProperty(name = "operateName", notes = "操作人")
	private String operateName;
	/**
	 * @Fields ip : ip地址
	 */
	@ApiModelProperty(name = "ip", notes = "ip地址")
	private String ip;
	/**
	 * @Fields operate_info : 操作信息
	 */
	@ApiModelProperty(name = "operateInfo", notes = "操作信息")
	private String operateInfo;

	
}
