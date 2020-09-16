package org.tsinghuatj.fixture.domain;

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
public class FixtureCheck implements Serializable {

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
	 * @Fields fixture_id : 夹具ID
	 */
	@ApiModelProperty(name = "fixtureId", notes = "夹具ID")
	private Long fixtureId;
	/**
	 * @Fields fixture_barcode : 夹具条码
	 */
	@ExcelField(title = "夹具条码", order = 1)
	@ApiModelProperty(name = "fixtureBarcode", notes = "夹具条码")
	private String fixtureBarcode;
	/**
	 * @Fields fixture_number : 物料编码
	 */
	@ExcelField(title = "夹具编码", order = 2)
	@ApiModelProperty(name = "fixtureNumber", notes = "物料编码")
	private String fixtureNumber;

	/**
	 * @Fields fixture_name : 夹具名称
	 */
	@ExcelField(title = "夹具名称", order = 3)
	@ApiModelProperty(name = "fixtureName", notes = "夹具名称")
	private String fixtureName;
	/**
	 * @Fields fixture_map : 图号
	 */
	@ExcelField(title = "夹具图号", order = 4)
	@ApiModelProperty(name = "fixtureMap", notes = "图号")
	private String fixtureMap;
	/**
	 * @Fields supplier_id : 供应商id
	 */
	@ApiModelProperty(name = "supplierId", notes = "供应商id")
	private Long supplierId;
	/**
	 * @Fields supplier_Name : 送货人
	 */
	@ApiModelProperty(name = "supplierName", notes = "供应商")
	private String supplierName;
	/**
	 * @Fields deliveryer : 送货人
	 */
	@ApiModelProperty(name = "deliveryer", notes = "送货人")
	private String deliveryer;
	/**
	 * @Fields delivery_time : 送货时间
	 */
	@ApiModelProperty(name = "deliveryTime", notes = "送货时间")
	private Date deliveryTime;
	/**
	 * @Fields standard_id : 标准Id
	 */
	@ApiModelProperty(name = "standardId", notes = "标准Id")
	private Long standardId;
	/**
	 * @Fields check_type : 检验类型(4-新夹具检验;4-修磨检验)
	 */
	@ApiModelProperty(name = "checkType", notes = "检验类型")
	private Integer checkType;

	@ExcelField(title = "检验类型", order = 5)
	@ApiModelProperty(name = "checkTypeName", notes = "检验类型")
	private String checkTypeName;

	/**
	 * @Fields check_result : 检验结果(1-合格;2-不合格)
	 */
	@ApiModelProperty(name = "checkResult", notes = "检验结果(1-合格;2-不合格)")
	private Integer checkResult;
	
	/**
	 * @Fields check_result : 检验结果(1-合格;2-不合格)
	 */
	@ExcelField(title = "检验结果", order = 6)
	@ApiModelProperty(name = "checkResultName", notes = "检验结果(1-合格;2-不合格)")
	private String checkResultName;
	/**
	 * @Fields check_time : 检验时间
	 */
	@ApiModelProperty(name = "checkTime", notes = "检验时间")
	private Date checkTime;
	
	
	@ExcelField(title = "检验时间", order = 7)
	@ApiModelProperty(name = "checkTimeStr", notes = "检验时间")
	private String checkTimeStr;
	
	/**
	 * @Fields checker : 检验人
	 */
	@ExcelField(title = "检验人", order = 8)
	@ApiModelProperty(name = "checker", notes = "检验人")
	private String checker;
	/**
	 * @Fields check_status : 质检状态(0-保存;1-提交)
	 */
	@ApiModelProperty(name = "checkStatus", notes = "质检状态(0-保存;1-提交)")
	private Integer checkStatus;

	/**
	 * @Fields handle_Result : 处理结果(0-待处理;1-已经处理)
	 */
	@ApiModelProperty(name = "handleResult", notes = "处理结果(0-待处理;1-已处理)")
	private Integer handleResult;

	/**
	 * @Fields waitCheckId : 待检id
	 */
	@ApiModelProperty(name = "waitCheckId", notes = "待检id")
	private Long waitCheckId;
	/**
	 * @Fields remark : 说明
	 */
	@ExcelField(title = "备注", order = 9)
	@ApiModelProperty(name = "remark", notes = "说明")
	private String remark;

	private List<FixtureCheckResult> resultList;

	private List<ToolAppendix> appendixList;

}
