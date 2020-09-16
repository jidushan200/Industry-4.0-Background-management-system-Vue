package org.tsinghuatj.base.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.tsinghuatj.framework.utils.excel.annotation.ExcelField;
import org.tsinghuatj.tool.domain.ToolAppendix;

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
public class CheckStandard implements Serializable {

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
	 * @Fields materialTypeName : 物料类型
	 */
	@ExcelField(title = "物料类型", order = 1)
	@ApiModelProperty(name = "materialTypeName", notes = "物料类型")
	private String materialTypeName;
	/**
	 * @Fields material_number : 物料编码
	 */
	@ExcelField(title = "物料编码", order = 2)
	@ApiModelProperty(name = "materialNumber", notes = "物料编码")
	private String materialNumber;

	/**
	 * @Fields material_name : 物料名称
	 */
	@ExcelField(title = "物料名称", order = 3)
	@ApiModelProperty(name = "materialName", notes = "物料名称")
	private String materialName;

	/**
	 * @Fields material_map : 物料图号
	 */
	@ExcelField(title = "物料图号", order = 4)
	@ApiModelProperty(name = "materialMap", notes = "物料图号")
	private String materialMap;

	/**
	 * @Fields material_type : 物料类型(1-物料;2-夹具)
	 */
	@ApiModelProperty(name = "materialType", notes = "物料类型(1-刀具;2-夹具)")
	private Integer materialType;
	
	/**
	 * @Fields type_id : 刀具类型
	 */
	@ApiModelProperty(name = "typeId", notes = "刀具类型")
	private Integer typeId;
	/**
	 * @Fields checkTypeName : 检验类型名称
	 */
	@ExcelField(title = "检验类型", order = 5)
	@ApiModelProperty(name = "checkTypeName", notes = "检验类型名称")
	private String checkTypeName;
	/**
	 * @Fields check_type : 检验类型（1-新刀检验；2-刃磨检验；3-涂层检验）
	 */
	@ApiModelProperty(name = "checkType", notes = "检验类型（1-新刀检验；2-刃磨检验；3-涂层检验）")
	private Integer checkType;
	/**
	 * @Fields standard_desc : 标准描述
	 */
	@ExcelField(title = "标准描述", order = 5)
	@ApiModelProperty(name = "standardDesc", notes = "标准描述")
	private String standardDesc;

	/**
	 * @Fields appendix_id : 附件
	 */
	@ApiModelProperty(name = "appendixId", notes = "附件")
	private Long appendixId;

	/**
	 * @Fields source_Name : 附件名称
	 */
	@ApiModelProperty(name = "sourceName", notes = "附件名称")
	private String sourceName;

	@ApiModelProperty(name = "itemList", notes = "质检项")
	private List<CheckStandardItem> itemList;
	
	@ApiModelProperty(name = "appendixList", notes = "附件")
	private List<ToolAppendix> appendixList;

}
