package org.tsinghuatj.mould.domain;

import java.io.Serializable;
import java.util.Date;

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
public class MouldCheckItem implements Serializable {
	
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
	 * @Fields item_seq : 顺序号
	 */
	@ApiModelProperty(name = "itemSeq", notes = "顺序号")
	private Integer itemSeq;
	/**
	 * @Fields check_item : 检验项
	 */
	@ApiModelProperty(name = "checkItem", notes = "检验项")
	private String checkItem;
	/**
	 * @Fields check_result : 检验结果
	 */
	@ApiModelProperty(name = "checkResult", notes = "检验结果")
	private String checkResult;
	/**
	 * @Fields is_qualified : 判定(1-合格;2-不合格)
	 */
	@ApiModelProperty(name = "isQualified", notes = "判定(1-合格;2-不合格)")
	private Integer isQualified;
	/**
	 * @Fields remark : 备注
	 */
	@ApiModelProperty(name = "remark", notes = "备注")
	private String remark;
	
	@Override
    public String toString() {
        return  "MouldCheckItem " + 
                    "[pkId=" + pkId + "]"
                   + ",[createUser=" + createUser + "]"
                                   + ",[createTime=" + createTime + "]"
                                   + ",[updateUser=" + updateUser + "]"
                                   + ",[updateTime=" + updateTime + "]"
                                   + ",[delMark=" + delMark + "]"
                                   + ",[checkId=" + checkId + "]"
                                   + ",[itemSeq=" + itemSeq + "]"
                                   + ",[checkItem=" + checkItem + "]"
                                   + ",[checkResult=" + checkResult + "]"
                                   + ",[isQualified=" + isQualified + "]"
                                   + ",[remark=" + remark + "]"
                ;
	}
}

