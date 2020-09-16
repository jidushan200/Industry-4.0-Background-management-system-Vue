package org.tsinghuatj.measure.domain;

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
public class MeasureCheck implements Serializable {

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
	 * @Fields measure_id : 量具ID
	 */
	@ApiModelProperty(name = "measureId", notes = "量具ID")
	private Long measureId;
	/**
	 * @Fields full_number : 物料条码
	 */
	@ApiModelProperty(name = "fullNumber", notes = "物料条码")
	private String fullNumber;
	/**
	 * @Fields measure_number : 物料编码
	 */
	@ApiModelProperty(name = "measureNumber", notes = "物料编码")
	private String measureNumber;
	/**
	 * @Fields measure_name : 物料名称
	 */
	@ApiModelProperty(name = "measureName", notes = "物料名称")
	private String measureName;
	/**
	 * @Fields model : 物料图号
	 */
	@ApiModelProperty(name = "model", notes = "物料图号")
	private String model;
	/**
	 * @Fields measure_seq : 顺序号
	 */
	@ApiModelProperty(name = "measureSeq", notes = "顺序号")
	private String measureSeq;
	/**
	 * @Fields supplier_id : 供应商id
	 */
	@ApiModelProperty(name = "supplierId", notes = "供应商id")
	private Long supplierId;
	/**
	 * @Fields supplier_name : 供应商名称
	 */
	@ApiModelProperty(name = "supplierName", notes = "供应商名称")
	private String supplierName;

	/**
	 * @Fields use_department_id : 使用部门ID
	 */
	@ApiModelProperty(name = "useDepartmentId", notes = "使用部门ID")
	private Long useDepartmentId;
	/**
	 * @Fields use_department_name : 使用部门名称
	 */
	@ExcelField(title = "使用部门", order = 10)
	@ApiModelProperty(name = "useDepartmentName", notes = "使用部门名称")
	private String useDepartmentName;

	@ApiModelProperty(name = "useTeamName", notes = "使用班组名称")
	private String useTeamName;

	@ApiModelProperty(name = "factoryMetrology", notes = "本厂计量编号")
	private String factoryMetrology;
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
	 * @Fields check_type : 检验类型(1-新刀检验;2-定期周检)
	 */
	@ApiModelProperty(name = "checkType", notes = "检验类型(1-新刀检验;2-定期周检)")
	private Integer checkType;
	/**
	 * @Fields check_result : 检验结果(1-合格;2-不合格)
	 */
	@ApiModelProperty(name = "checkResult", notes = "检验结果(1-合格;2-不合格)")
	private Integer checkResult;
	/**
	 * @Fields check_time : 检验时间
	 */
	@ApiModelProperty(name = "checkTime", notes = "检验时间")
	private Date checkTime;
	/**
	 * @Fields checker : 检验人
	 */
	@ApiModelProperty(name = "checker", notes = "检验人")
	private String checker;
	/**
	 * @Fields receipt_id : 收货单id
	 */
	@ApiModelProperty(name = "receiptId", notes = "收货单id")
	private Long receiptId;
	/**
	 * @Fields remark : 说明
	 */
	@ApiModelProperty(name = "remark", notes = "说明")
	private String remark;

	private Integer handleResult;
	private Long applyDepartmentId;
	private String applyDepartmentName;
	private Long applierId;
	private String applierName;
	
	
}
