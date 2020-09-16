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
public class SysMessage implements Serializable {

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
	 * @Fields sender_id : 发送人Id
	 */
	@ApiModelProperty(name = "senderId", notes = "发送人Id")
	private Long senderId;
	/**
	 * @Fields sender_name : 发送人姓名
	 */
	@ApiModelProperty(name = "senderName", notes = "发送人姓名")
	private String senderName;
	/**
	 * @Fields send_time : 发送时间
	 */
	@ApiModelProperty(name = "sendTime", notes = "发送时间")
	private Date sendTime;
	/**
	 * @Fields message : 消息内容
	 */
	@ApiModelProperty(name = "message", notes = "消息内容")
	private String message;
	
	/**
	 * @Fields title : 消息内容
	 */
	@ApiModelProperty(name = "title", notes = "消息内容")
	private String title;
	/**
	 * @Fields receiver_id : 接收人Id
	 */
	@ApiModelProperty(name = "receiverId", notes = "接收人Id")
	private Long receiverId;
	/**
	 * @Fields read_flag : 阅读标识(--0正常--1已删除)
	 */
	@ApiModelProperty(name = "readFlag", notes = "阅读标识(--0正常--1已删除)")
	private Integer readFlag;
	/**
	 * @Fields read_time : 阅读时间
	 */
	@ApiModelProperty(name = "readTime", notes = "阅读时间")
	private Date readTime;
	
	private String strTime;

	@Override
	public String toString() {
		return "SysMessage " + "[pkId=" + pkId + "]" + ",[createUser=" + createUser + "]" + ",[createTime=" + createTime
				+ "]" + ",[updateUser=" + updateUser + "]" + ",[updateTime=" + updateTime + "]" + ",[delMark=" + delMark
				+ "]" + ",[senderId=" + senderId + "]" + ",[senderName=" + senderName + "]" + ",[sendTime=" + sendTime
				+ "]" + ",[message=" + message + "]" + ",[receiverId=" + receiverId + "]" + ",[readFlag=" + readFlag
				+ "]" + ",[readTime=" + readTime + "]";
	}
}
