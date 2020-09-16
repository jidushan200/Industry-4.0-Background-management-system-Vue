package org.tsinghuatj.web.controller.fixture;

import java.util.List;

import org.apache.commons.lang3.Validate;
import org.apache.poi.xwpf.converter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tsinghuatj.fixture.domain.FixtureCheck;
import org.tsinghuatj.fixture.domain.FixturePurchaseReceipt;
import org.tsinghuatj.fixture.service.IFixturePurchaseApplyService;
import org.tsinghuatj.fixture.service.IFixturePurchaseReceiptService;
import org.tsinghuatj.framework.domain.AjaxReturn;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.framework.utils.JsonUtils;
import org.tsinghuatj.framework.web.controller.BaseController;
import org.tsinghuatj.support.annotation.OperateLog;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping({ "/fixture" })
public class FixturePurchaseReceiptController extends BaseController {
	private @Autowired(required = false) IFixturePurchaseApplyService applyService;
	private @Autowired(required = false) IFixturePurchaseReceiptService receiptService;

	/**
	 * 夹具收货
	 */
	@ApiOperation(value = "夹具收货", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "夹具收货", response = AjaxReturn.class) })
	@PostMapping(path = { "/purchase-receipt-save" })
	@OperateLog(info = "夹具收货[夹具编号:%s->到货数量:%s]", params = { "fixtureNumber", "qty" })
	public AjaxReturn fixturePurchaseReceiptSave(
			@ApiParam(name = "applyId", value = "夹具ID", required = true) @RequestParam(required = true) Long applyId, 
			@ApiParam(name = "receiptList", value = "到货明细", required = true) @RequestParam(required = true) String receiptList

	) throws BusinessException {
		log.debug("FixturePurchaseReceiptController.fixturePurchaseReceiptSave<<<");
		if (log.isDebugEnabled()) {

		}
		// 获取当前用户
		Long userId = getAuthentication();
		List<FixturePurchaseReceipt> list = null;
		if (StringUtils.isNotEmpty(receiptList)) {
			list = JsonUtils.json2list(receiptList, FixturePurchaseReceipt.class);
		}
		log.debug("FixturePurchaseReceiptController.fixturePurchaseReceiptSave>>>");
		return new AjaxReturn(200, null, applyService.updatePurchaseReceipt(userId, applyId, list));
	}
	
	/**
	 * 夹具退货
	 */
	@ApiOperation(value = "夹具收货", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "夹具收货", response = AjaxReturn.class) })
	@PostMapping(path = { "/fixture-refund" })
	@OperateLog(info = "夹具退货[夹具条码:%s->退货原因:%s]", params = { "fixtureBarcode", "returnReason" })
	public AjaxReturn fixtureRefund(
			@ApiParam(name = "receiptId", value = "收货单ID", required = false) @RequestParam(required = false) Long receiptId, 
			@ApiParam(name = "fixtureBarcode", value = "夹具ID", required = false) @RequestParam(required = false) String fixtureBarcode, 
			@ApiParam(name = "returnReason", value = "退货原因", required = false) @RequestParam(required = false) String returnReason

	) throws BusinessException {
		log.debug("FixturePurchaseReceiptController.fixtureRefund<<<");
		if (log.isDebugEnabled()) {

		}
		// 获取当前用户
		Long userId = getAuthentication();
		FixturePurchaseReceipt receipt = new FixturePurchaseReceipt();
		receipt.setPkId(receiptId);
		receipt.setHandleResult(1);
		log.debug("FixturePurchaseReceiptController.fixtureRefund>>>");
		return new AjaxReturn(200, null, receiptService.updateActiveById(userId, receipt, receiptId));
	}
	
	

	/**
	 * 夹具收货
	 */
	@ApiOperation(value = "夹具收货删除", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "夹具收货删除", response = AjaxReturn.class) })
	@PostMapping(path = { "/purchase-receipt-delete" })
	@OperateLog(info = "夹具收货删除[夹具条码:%s]", params = { "fixtureBarcode" })
	public AjaxReturn fixturePurchaseReceiptDelete(@ApiParam(name = "receiptId", value = "收货单ID", required = true) @RequestParam(required = true) Long receiptId) throws BusinessException {
		log.debug("FixturePurchaseApplyController.fixturePurchaseReceiptDelete<<<");
		if (log.isDebugEnabled()) {

		}
		// 获取当前用户
		Long userId = getAuthentication();

		log.debug("FixturePurchaseApplyController.fixturePurchaseReceiptDelete>>>");
		return new AjaxReturn(200, null, receiptService.deleteById(userId, receiptId));
	}

	@ApiOperation(value = "查询夹具收货单分页列表(待入库和待退货)", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "查询夹具收货单分页列表", response = FixtureCheck.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/purchase-receipt-page-list")
	public AjaxReturn purchaseReceiptPageList(@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, //
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows, @ApiParam(name = "fixtureBarcode", value = "物料条码", required = false) @RequestParam(required = false) String fixtureBarcode, @ApiParam(name = "fixtureMap", value = "夹具图号", required = false) @RequestParam(required = false) String fixtureMap, @ApiParam(name = "checkStatus", value = "检验状态(1-已检验)", required = false) @RequestParam(required = false) Integer checkStatus, @ApiParam(name = "checkResult", value = "检验结果(1-合格待入库2-不合格待退货)", required = false) @RequestParam(required = false) Integer checkResult

	) throws BusinessException {
		log.debug("FixturePurchaseReceiptController.purchaseReceiptPageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("page:" + page);
			log.debug("rows:" + rows);
		}

		// 获取当前用户
		Long userId = getAuthentication();
		// 参数校验
		Validate.isTrue(page >= 0);
		Validate.isTrue(rows <= 100);
		// 封装查询条件
		QueryDto queryDto = new QueryDto();
		queryDto.setPage(page);
		queryDto.setRows(rows);

		FixturePurchaseReceipt where = new FixturePurchaseReceipt();
		where.setCheckStatus(0);
		where.setFixtureMap(fixtureMap);
		where.setFixtureBarcode(fixtureBarcode);
		where.setCheckStatus(checkStatus);
		where.setCheckResult(checkResult);
		Pagination<FixturePurchaseReceipt> pagination = receiptService.selectPageList(userId, where, queryDto);
		log.debug("FixturePurchaseReceiptController.purchaseReceiptPageList>>>");
		return pagination;
	}

	@ApiOperation(value = "夹具收货单id查询", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "夹具收货单id查询", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/purchase-receipt-get-by-id")
	public AjaxReturn purchaseReceiptGetById(@ApiParam(name = "pkId", value = "pkId", required = true) @RequestParam(required = true) Long pkId) throws BusinessException {
		log.debug("FixturePurchaseReceiptController.purchaseReceiptGetById<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
		}

		// 获取当前用户
		Long userId = getAuthentication();
		// 参数校验
		Validate.isTrue(pkId >= 1);

		log.debug("FixturePurchaseReceiptController.purchaseReceiptGetById>>>");
		return new AjaxReturn(200, null, receiptService.selectById(userId, pkId));
	}

}
