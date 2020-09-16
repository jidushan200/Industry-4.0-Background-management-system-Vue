package org.tsinghuatj.fixture.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
public class FixturePurchaseReceipt implements Serializable {
	
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
	 * @Fields apply_id : 采购单ID
	 */
	@ApiModelProperty(name = "applyId", notes = "采购单ID")
	private Long applyId;
	/**
	 * @Fields fixture_barcode : 条码
	 */
	@ApiModelProperty(name = "fixtureBarcode", notes = "条码")
	private String fixtureBarcode;
	/**
	 * @Fields fixture_number : 夹具编码
	 */
	@ApiModelProperty(name = "fixtureNumber", notes = "夹具编码")
	private String fixtureNumber;
	/**
	 * @Fields fixture_map : 夹具图号
	 */
	@ApiModelProperty(name = "fixtureMap", notes = "夹具图号")
	private String fixtureMap;
	/**
	 * @Fields fixture_name : 夹具名称
	 */
	@ApiModelProperty(name = "fixtureName", notes = "夹具名称")
	private String fixtureName;
	/**
	 * @Fields fixture_type : 夹具类型(1-组合;2-配件)
	 */
	@ApiModelProperty(name = "fixtureType", notes = "夹具类型(1-组合;2-配件)")
	private Integer fixtureType;
	/**
	 * @Fields procurement_type : 采购类型(1-自制;2-外购)
	 */
	@ApiModelProperty(name = "procurementType", notes = "采购类型(1-自制;2-外购)")
	private Integer procurementType;
	/**
	 * @Fields supplier_id : 供应商ID
	 */
	@ApiModelProperty(name = "supplierId", notes = "供应商ID")
	private Long supplierId;
	/**
	 * @Fields supplier_name : 供应商名称
	 */
	@ApiModelProperty(name = "supplierName", notes = "供应商名称")
	private String supplierName;
	/**
	 * @Fields deliverer : 送货人
	 */
	@ApiModelProperty(name = "deliverer", notes = "送货人")
	private String deliverer;
	/**
	 * @Fields delivery_time : 送货时间
	 */
	@ApiModelProperty(name = "deliveryTime", notes = "送货时间")
	private Date deliveryTime;
	/**
	 * @Fields wait_check_id : 待检ID
	 */
	@ApiModelProperty(name = "waitCheckId", notes = "待检ID")
	private Long waitCheckId;
	/**
	 * @Fields check_status : 检验状态(0-待检;1-检验中;2-检验完成)
	 */
	@ApiModelProperty(name = "checkStatus", notes = "检验状态(0-待检;1-检验中;2-检验完成)")
	private Integer checkStatus;
	/**
	 * @Fields check_result : 质检结果(1-合格入库;2-不合格退货)
	 */
	@ApiModelProperty(name = "checkResult", notes = "质检结果(1-合格入库;2-不合格退货)")
	private Integer checkResult;
	/**
	 * @Fields handle_result : 处理结果(0-待入库退货;1-已入库退货)
	 */
	@ApiModelProperty(name = "handleResult", notes = "处理结果(0-待入库退货;1-已入库退货)")
	private Integer handleResult;
	
	/**
	 * @Fields detailList : 配件明细
	 */
	private List<FixturePurchaseReceiptDetail> detailList;
	
}

