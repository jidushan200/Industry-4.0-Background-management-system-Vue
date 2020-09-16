package org.tsinghuatj.web.controller.mould;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tsinghuatj.framework.domain.AjaxReturn;
import org.tsinghuatj.framework.domain.CustomUser;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.framework.utils.JsonUtils;
import org.tsinghuatj.framework.utils.excel.ExcelUtils;
import org.tsinghuatj.framework.web.controller.BaseController;
import org.tsinghuatj.framework.web.support.annotation.Secure;
import org.tsinghuatj.mould.domain.CheckItem;
import org.tsinghuatj.mould.domain.Mould;
import org.tsinghuatj.mould.domain.MouldBase;
import org.tsinghuatj.mould.domain.MouldCheck;
import org.tsinghuatj.mould.domain.MouldCheckItem;
import org.tsinghuatj.mould.domain.MouldReceipt;
import org.tsinghuatj.mould.service.IMouldCheckService;
import org.tsinghuatj.mould.service.IMouldReceiptService;
import org.tsinghuatj.mould.service.IMouldService;
import org.tsinghuatj.support.annotation.OperateLog;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping({ "/mould" })
public class MouldCheckController extends BaseController {

	private @Autowired(required = false) IMouldService mouldService;
	private @Autowired(required = false) IMouldCheckService mouldCheckService;
	private @Autowired(required = false) IMouldReceiptService mouldReceiptService;

	/**
	 * 查询模具待检信息分页列表
	 */
	@ApiOperation(value = "查询模具待检信息分页列表 ", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "查询模具待检信息分页列表查询成功", response = MouldReceipt.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/mould-check-page-list")
	public AjaxReturn mouldCheckPageList(
			@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, 
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows, 
			@ApiParam(name = "beginDate", value = "开始日期", required = false) @RequestParam(required = false, defaultValue = "") Date beginDate, 
			@ApiParam(name = "endDate", value = "结束日期", required = false) @RequestParam(required = false, defaultValue = "") Date endDate, 
			@ApiParam(name = "mouldBarcode", value = "模具条码", required = false) @RequestParam(required = false) String mouldBarcode,
			@ApiParam(name = "mouldName", value = "模具名称", required = false) @RequestParam(required = false) String mouldName, 
			@ApiParam(name = "mouldNumber", value = "模具编码", required = false) @RequestParam(required = false) String mouldNumber) throws BusinessException {
		log.debug("MouldCheckController.mouldCheckPageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("beginDate:" + beginDate);
			log.debug("endDate:" + endDate);
		}

		// 获取当前用户
		Long userId = getAuthentication();
		// 参数校验
		Validate.isTrue(page >= 1);
		Validate.isTrue(rows <= 100);
		// 封装查询条件
		QueryDto queryDto = new QueryDto();
		queryDto.setPage(page);
		queryDto.setRows(rows);
		queryDto.setBeginDate(beginDate);
		queryDto.setEndDate(endDate);

		MouldReceipt mouldReceipt = new MouldReceipt();
		if (StringUtils.isNotEmpty(mouldBarcode)) {
			int index = mouldBarcode.indexOf("-");
			mouldNumber = mouldBarcode.substring(0, index);
			String mouldSeq = mouldBarcode.substring(index + 1, index + 6);
			mouldReceipt.setMouldSeq(mouldSeq);
			String mouldMap = mouldBarcode.substring(index + 7, mouldBarcode.length());
			mouldReceipt.setMouldMap(mouldMap);
		}
		mouldReceipt.setMouldName(mouldName);
		mouldReceipt.setMouldNumber(mouldNumber);
		mouldReceipt.setMouldStatus(0);

		Pagination<MouldReceipt> pagination = mouldReceiptService.selectPageList(userId, mouldReceipt, queryDto);
		log.debug("MouldCheckController.mouldCheckPageList>>>");
		return pagination;
	}
	
	
	
	@ApiOperation(value = "待检模具导出", notes = "", code = 200, produces = "application/json")
	@ApiResponses({ @ApiResponse(code = 200, message = "待检模具导出", response = MouldBase.class) })
	@Secure()
	@GetMapping(path = { "/mould-check-export" })
	// @OperateLog(info = "模具基础信息导出")
	public ResponseEntity<byte[]> downloadmouldCheckExcel(
			@ApiParam(name = "beginDate", value = "开始日期", required = false) @RequestParam(required = false, defaultValue = "") Date beginDate, 
			@ApiParam(name = "endDate", value = "结束日期", required = false) @RequestParam(required = false, defaultValue = "") Date endDate, 
			@ApiParam(name = "mouldBarcode", value = "模具条码", required = false) @RequestParam(required = false) String mouldBarcode,
			@ApiParam(name = "mouldName", value = "模具名称", required = false) @RequestParam(required = false) String mouldName, 
			@ApiParam(name = "mouldNumber", value = "模具编码", required = false) @RequestParam(required = false) String mouldNumber) throws Exception {
		Long userId = getAuthentication();
		
		QueryDto queryDto = new QueryDto();
		queryDto.setPage(1);
		queryDto.setRows(50000);
		queryDto.setBeginDate(beginDate);
		queryDto.setEndDate(endDate);

		MouldReceipt mouldReceipt = new MouldReceipt();
		if (StringUtils.isNotEmpty(mouldBarcode)) {
			int index = mouldBarcode.indexOf("-");
			mouldNumber = mouldBarcode.substring(0, index);
			String mouldSeq = mouldBarcode.substring(index + 1, index + 6);
			mouldReceipt.setMouldSeq(mouldSeq);
			String mouldMap = mouldBarcode.substring(index + 7, mouldBarcode.length());
			mouldReceipt.setMouldMap(mouldMap);
		}
		mouldReceipt.setMouldName(mouldName);
		mouldReceipt.setMouldNumber(mouldNumber);
		mouldReceipt.setMouldStatus(0);
		
		Pagination<MouldReceipt> pagination = mouldReceiptService.selectPageList(userId, mouldReceipt, queryDto);
		pagination.getRows().stream().forEach(item -> {
			if (item.getCheckType() == 1) {
				item.setCheckTypeName("新模具质检");
			} else if (item.getCheckType() == 2) {
				item.setCheckTypeName("修磨质检");
			}
		});
		byte[] data = ExcelUtils.getInstance().exportObjects2ExcelByteArray((List<MouldReceipt>) pagination.getRows(), MouldReceipt.class, true, null, true);
		return getResponseEntity(data, "待检模具表.xlsx");
	}
	
	
	
	

	/**
	 * 查询模具已完成质检信息分页列表
	 */
	@ApiOperation(value = "查询模具已完成质检信息分页列表 ", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "查询模具已完成质检信息分页列表查询成功", response = Mould.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/mould-checked-page-list")
	public AjaxReturn mouldCheckedPageList(@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, @ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows, @ApiParam(name = "beginDate", value = "开始日期", required = false) @RequestParam(required = false, defaultValue = "") Date beginDate, @ApiParam(name = "endDate", value = "结束日期", required = false) @RequestParam(required = false, defaultValue = "") Date endDate, @ApiParam(name = "mouldBarcode", value = "模具条码", required = false) @RequestParam(required = false) String mouldBarcode,
			@ApiParam(name = "mouldNumber", value = "领用部门ID", required = false) @RequestParam(required = false) String mouldNumber) throws BusinessException {
		log.debug("MouldCheckController.mouldCheckPageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("beginDate:" + beginDate);
			log.debug("endDate:" + endDate);
		}

		// 获取当前用户
		Long userId = getAuthentication();
		// 参数校验
		Validate.isTrue(page >= 1);
		Validate.isTrue(rows <= 100);
		// 封装查询条件
		QueryDto queryDto = new QueryDto();
		queryDto.setPage(page);
		queryDto.setRows(rows);
		queryDto.setBeginDate(beginDate);
		queryDto.setEndDate(endDate);

		MouldCheck mouldCheck = new MouldCheck();
		if (StringUtils.isNotEmpty(mouldBarcode)) {
			int index = mouldBarcode.indexOf("-");
			mouldNumber = mouldBarcode.substring(0, index);
			String mouldSeq = mouldBarcode.substring(index + 1, index + 6);
			mouldCheck.setMouldSeq(mouldSeq);
			String mouldMap = mouldBarcode.substring(index + 7, mouldBarcode.length());
			mouldCheck.setMouldMap(mouldMap);
		}
		mouldCheck.setMouldNumber(mouldNumber);
		mouldCheck.setReportStatus(2);
		// mouldCheck.setCheckerId(userId);

		Pagination<MouldCheck> pagination = mouldCheckService.selectPageList(userId, mouldCheck, queryDto);
		log.debug("MouldCheckController.mouldCheckPageList>>>");
		return pagination;
	}

	/**
	 * 模具质检新增
	 */
	@ApiOperation(value = "模具质检新增", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "模具质检成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/mould-check-add", produces = "application/json;charset=UTF-8")
	@OperateLog(info = "模具质检信息新增[模具编码:%s->模具顺序号:%s]", params = { "mouldNumber", "mouldSeq" })
	public AjaxReturn mouldRepairAdd(@ApiParam(name = "mouldReceiptId", value = "待检信息ID", required = true) @RequestParam(required = true) Long mouldReceiptId, @ApiParam(name = "mouldSeq", value = "模具序号", required = true) @RequestParam(required = true) String mouldSeq, @ApiParam(name = "mouldNumber", value = "刀具编码", required = true) @RequestParam(required = true) String mouldNumber, @ApiParam(name = "mouldName", value = "刀具编码", required = true) @RequestParam(required = true) String mouldName, @ApiParam(name = "mouldMap", value = "刀具编码", required = true) @RequestParam(required = true) String mouldMap,
			@ApiParam(name = "supplierId", value = "供应商id", required = false) @RequestParam(required = false) Long supplierId, @ApiParam(name = "supplierName", value = "刀具编码", required = false) @RequestParam(required = false) String supplierName, @ApiParam(name = "selfCheckReport", value = "备注说明", required = false) @RequestParam(required = false) Integer selfCheckReport, @ApiParam(name = "checkStatus", value = "质检状态", required = false) @RequestParam(required = false) Integer checkStatus, @ApiParam(name = "checkType", value = "质检类型", required = false) @RequestParam(required = false) Integer checkType,
			@ApiParam(name = "appendixIds", value = "附件", required = false) @RequestParam(required = false) String appendixIds, @ApiParam(name = "itemList", value = "检验项", required = false) @RequestParam(required = false) String itemList) throws BusinessException {
		log.debug("MouldRepairController.mouldRepairAdd<<<");
		if (log.isDebugEnabled()) {
			log.debug("itemList:" + itemList);
		}
		// 获取当前用户
		CustomUser user = getCompleteAuthentication();
		MouldReceipt mouldReceipt = new MouldReceipt();
		Long mouldId = null;
		if (checkType == 2) {
			String warehouseCode = mouldNumber + '-' + mouldSeq;
			String fullNumber = warehouseCode + '/' + mouldMap;
			Mould mould = mouldService.selectByFullNumber(user.getId(), fullNumber);
			if (mould != null) {
				mouldId = mould.getPkId();
				if (checkStatus == 1) {
					mould.setMouldStatus(5);
					mouldReceipt.setMouldStatus(1);
				} else if (checkStatus == 2) {
					mould.setMouldStatus(6);
					mouldReceipt.setMouldStatus(2);
				}
				mouldService.updateActiveById(user.getId(), mould, mouldId);
			} else {
				Object arry[] = { 0, fullNumber };
				throw new BusinessException("mould.not.exists.error", arry);
			}
		} else if (checkType == 1) {
			MouldReceipt receipt = mouldReceiptService.selectById(user.getId(), mouldReceiptId);
			if (checkStatus == 1) {
				String warehouseCode = mouldNumber + '-' + mouldSeq;
				String mouldBarcode = warehouseCode + '/' + mouldMap;
				// 封装参数信息
				Mould mould = new Mould();
				mould.setMouldBarcode(mouldBarcode);
				mould.setMouldSeq(mouldSeq);
				mould.setWarehouseCode(warehouseCode);
				mould.setMouldNumber(mouldNumber);
				mould.setMouldMap(mouldMap);
				mould.setMouldName(mouldName);
				mould.setMouldType(receipt.getMouldType());
				mould.setLifeMax(receipt.getLifeMax());
				mould.setHeatNumber(receipt.getHeatNumber());
				mould.setSurfaceNumber(receipt.getSurfaceNumber());
				mould.setEmbryoCode(receipt.getEmbryoCode());
				mould.setEmbryoName(receipt.getEmbryoName());
				mould.setProcessTimes(0);
				mould.setProcessAmount(0);
				mould.setRepairTimes(0);
				mould.setMouldAmount(1);
				mould.setMouldStatus(0);

				mouldService.insert(user.getId(), mould);
				mouldId = mould.getPkId();
				// 封装待质检列表中的数据状态
				mouldReceipt.setMouldStatus(1);
			} else if (checkStatus == 2) {
				mouldId = null;
				// 封装待质检列表中的数据状态
				mouldReceipt.setMouldStatus(2);
			}
		}
		// 更改待质检列表中的数据状态
		mouldReceiptService.updateActiveById(user.getId(), mouldReceipt, mouldReceiptId);

		List<CheckItem> items = JsonUtils.json2list(itemList, CheckItem.class);
		List<MouldCheckItem> checkItems = new ArrayList<>();
		MouldCheck mouldCheck = new MouldCheck();
		mouldCheck.setMouldId(mouldId);
		mouldCheck.setReceiptId(mouldReceiptId);
		mouldCheck.setMouldNumber(mouldNumber);
		mouldCheck.setMouldName(mouldName);
		mouldCheck.setMouldMap(mouldMap);
		mouldCheck.setMouldSeq(mouldSeq);
		mouldCheck.setSupplierId(supplierId);
		mouldCheck.setSupplierName(supplierName);
		mouldCheck.setSelfCheckReport(selfCheckReport);
		mouldCheck.setCheckerId(user.getId());
		mouldCheck.setChecker(user.getRealname());
		mouldCheck.setCheckTime(new Date());
		mouldCheck.setCheckStatus(checkStatus);
		mouldCheck.setCheckType(checkType);
		mouldCheck.setReportStatus(2);

		items.forEach(item -> {
			if (item.getLcheckItem() != null && item.getLcheckItem() != "") {
				MouldCheckItem checkItem = new MouldCheckItem();
				checkItem.setCheckItem(item.getLcheckItem());
				checkItem.setCheckResult(item.getLmeasuredValue());
				if (item.getLcheckResult() == null) {
					item.setLcheckResult(2);
				}
				checkItem.setIsQualified(item.getLcheckResult());
				checkItem.setItemSeq(item.getLitemSeq());
				checkItems.add(checkItem);
			}
			if (item.getRcheckItem() != null && item.getRcheckItem() != "") {
				MouldCheckItem checkItem = new MouldCheckItem();
				checkItem.setCheckItem(item.getRcheckItem());
				checkItem.setCheckResult(item.getRmeasuredValue());
				if (item.getRcheckResult() == null) {
					item.setRcheckResult(2);
				}
				checkItem.setIsQualified(item.getRcheckResult());
				checkItem.setItemSeq(item.getRitemSeq());
				checkItems.add(checkItem);
			}
		});
		mouldCheck.setItemList(checkItems);

		log.debug("MouldRepairController.mouldRepairAdd>>>");
		return new AjaxReturn(200, null, mouldCheckService.insert(user.getId(), mouldCheck, appendixIds));
	}

	/**
	 * 根据Id查询模具质检信息
	 */
	@ApiOperation(value = "根据Id查询模具质检信息 ", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "根据Id查询模具质检信息查询成功", response = MouldCheck.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/get-check-by-id")
	public AjaxReturn getCheckById(@ApiParam(name = "pkId", value = "pkId", required = false) @RequestParam(required = false) Long pkId) throws BusinessException {
		log.debug("MouldCheckController.getCheckById<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
		}

		// 获取当前用户
		Long userId = getAuthentication();
		// 封装查询条件
		MouldCheck mouldCheck = mouldCheckService.selectById(userId, pkId);
		log.debug("MouldCheckController.getCheckById>>>");
		return new AjaxReturn(200, null, mouldCheck);
	}

	@ApiOperation(value = "根据Id查询模具质检信息 ", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "根据Id查询模具质检信息查询成功", response = MouldCheck.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/get-check-item")
	public AjaxReturn getCheckItem() throws BusinessException {
		log.debug("MouldCheckController.getCheckItem<<<");

		// 获取当前用户
		// Long userId = getAuthentication();
		// 封装查询条件
		List<CheckItem> checkItemList = new ArrayList<CheckItem>();

		CheckItem item = new CheckItem();
		for (int i = 1; i < 21; i++) {
			item = new CheckItem();
			item.setLitemSeq(i);
			item.setRitemSeq(20 + i);
			if (i == 1) {
				item.setLcheckItem("外观缺陷检查");
			} else if (i == 2) {
				item.setLcheckItem("模具标识确认");
			}
			checkItemList.add(item);
		}

		log.debug("MouldCheckController.getCheckItem>>>");
		return new AjaxReturn(200, null, checkItemList);
	}

	/**
	 * 模具不合格列表
	 */
	@ApiOperation(value = "模具入库", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "查询模具不合格列表成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/check-unquality-page-list")
	public AjaxReturn checkUnqualityPageList(@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, @ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows, @ApiParam(name = "beginDate", value = "开始日期", required = false) @RequestParam(required = false, defaultValue = "") Date beginDate, @ApiParam(name = "endDate", value = "结束日期", required = false) @RequestParam(required = false, defaultValue = "") Date endDate, @ApiParam(name = "mouldName", value = "模具名称", required = false) @RequestParam(required = false) String mouldName,
			@ApiParam(name = "mouldNumber", value = "模具编号", required = false) @RequestParam(required = false) String mouldNumber) throws BusinessException {
		log.debug("MouldCheckController.checkUnqualityPageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("beginDate:" + beginDate);
			log.debug("endDate:" + endDate);
			log.debug("mouldNumber:" + mouldNumber);
		}

		// 获取当前用户
		Long userId = getAuthentication();
		// 参数校验
		Validate.isTrue(page >= 1);
		Validate.isTrue(rows <= 100);
		// 封装查询条件
		QueryDto queryDto = new QueryDto();
		queryDto.setPage(page);
		queryDto.setRows(rows);
		queryDto.setBeginDate(beginDate);
		queryDto.setEndDate(endDate);

		MouldCheck mouldCheck = new MouldCheck();
		mouldCheck.setMouldName(mouldName);
		mouldCheck.setMouldNumber(mouldNumber);
		mouldCheck.setCheckStatus(2);
		mouldCheck.setReportStatus(2);

		Pagination<MouldCheck> pagination = mouldCheckService.selectPageList(userId, mouldCheck, queryDto);

		log.debug("MouldCheckController.checkUnqualityPageList>>>");

		return pagination;
	}

	@ApiOperation(value = "根据Id查询模具质检信息 ", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "根据Id查询模具质检信息查询成功", response = MouldCheck.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/mould-check-compromise")
	@OperateLog(info = "模具不合格判定让步[模具编码:%s]", params = { "mouldNumber" })
	public AjaxReturn mouldCheckCompromise(@ApiParam(name = "pkId", value = "pkId", required = true) @RequestParam(required = true) Long pkId, @ApiParam(name = "mouldId", value = "模具ID", required = false) @RequestParam(required = false, defaultValue = "10") Long mouldId, @ApiParam(name = "mouldNumber", value = "模具编码", required = false) @RequestParam(required = false) String mouldNumber, @ApiParam(name = "mouldMap", value = "模具图号", required = false) @RequestParam(required = false, defaultValue = "10") String mouldMap, @ApiParam(name = "mouldReceiptId", value = "模具编码", required = true) @RequestParam(required = true) Long mouldReceiptId,
			@ApiParam(name = "checkType", value = "模具ID", required = false) @RequestParam(required = false, defaultValue = "10") Integer checkType) throws BusinessException {
		log.debug("MouldCheckController.mouldCheckCompromise<<<");

		// 获取当前用户
		Long userId = getAuthentication();
		// 封装查询条件

		if (checkType == 1) {
			MouldReceipt mouldReceipt = mouldReceiptService.selectById(userId, mouldReceiptId);
			String mouldSeq = mouldReceipt.getMouldSeq();
			String warehouseCode = mouldNumber + '-' + mouldSeq;
			String mouldBarcode = warehouseCode + '/' + mouldMap;
			// 封装参数信息
			Mould mould = new Mould();
			mould.setMouldBarcode(mouldBarcode);
			mould.setMouldSeq(mouldSeq);
			mould.setWarehouseCode(warehouseCode);
			mould.setMouldNumber(mouldNumber);
			mould.setMouldMap(mouldReceipt.getMouldMap());
			mould.setMouldName(mouldReceipt.getMouldName());
			mould.setMouldType(mouldReceipt.getMouldType());
			mould.setLifeMax(mouldReceipt.getLifeMax());
			mould.setHeatNumber(mouldReceipt.getHeatNumber());
			mould.setSurfaceNumber(mouldReceipt.getSurfaceNumber());
			mould.setProcessTimes(0);
			mould.setProcessAmount(0);
			mould.setRepairTimes(0);
			mould.setMouldAmount(1);
			mould.setMouldStatus(0);
			mould.setIsCompromise(1);

			mouldService.insert(userId, mould);
			mouldReceipt.setMouldStatus(1);
			mouldReceiptService.updateActiveById(userId, mouldReceipt, mouldReceiptId);
		} else if (checkType == 2) {
			Mould mould = new Mould();
			mould.setMouldStatus(5);
			mould.setIsCompromise(1);
			mouldService.updateActiveById(userId, mould, mouldId);
		}

		MouldCheck mouldCheck = new MouldCheck();
		mouldCheck.setCheckStatus(3);
		log.debug("MouldCheckController.mouldCheckCompromise>>>");
		return new AjaxReturn(200, null, mouldCheckService.updateActiveById(userId, mouldCheck, pkId));
	}

	@ApiOperation(value = "根据Id查询模具质检信息 ", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "根据Id查询模具质检信息查询成功", response = MouldCheck.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/mould-check-repair")
	@OperateLog(info = "模具不合格判定返修[模具编码:%s]", params = { "mouldNumber" })
	public AjaxReturn mouldCheckRepair(@ApiParam(name = "pkId", value = "pkId", required = true) @RequestParam(required = true) Long pkId, 
			@ApiParam(name = "mouldNumber", value = "模具编号", required = false) @RequestParam(required = false) String mouldNumber, 
			@ApiParam(name = "mouldReceiptId", value = "收货单号", required = false) @RequestParam(required = false) Long mouldReceiptId, 
			@ApiParam(name = "mouldId", value = "模具ID", required = false) @RequestParam(required = false) Long mouldId) throws BusinessException {
		log.debug("MouldCheckController.mouldCheckRepair<<<");

		// 获取当前用户
		CustomUser user = getCompleteAuthentication();

		// 新模具
		if (null == mouldId) {
			MouldReceipt mouldReceipt = mouldReceiptService.selectById(user.getId(), mouldReceiptId);
			String mouldSeq = mouldReceipt.getMouldSeq();
			String warehouseCode = mouldNumber + '-' + mouldSeq;
			String mouldBarcode = warehouseCode + '/' + mouldReceipt.getMouldMap();
			// 封装参数信息
			Mould mould = new Mould();
			mould.setMouldBarcode(mouldBarcode);
			mould.setMouldSeq(mouldSeq);
			mould.setWarehouseCode(warehouseCode);
			mould.setMouldNumber(mouldNumber);
			mould.setMouldMap(mouldReceipt.getMouldMap());
			mould.setMouldName(mouldReceipt.getMouldName());
			mould.setMouldType(mouldReceipt.getMouldType());
			mould.setLifeMax(mouldReceipt.getLifeMax());
			mould.setHeatNumber(mouldReceipt.getHeatNumber());
			mould.setSurfaceNumber(mouldReceipt.getSurfaceNumber());
			mould.setProcessTimes(0);
			mould.setProcessAmount(0);
			mould.setRepairTimes(0);
			mould.setMouldAmount(1);
			mould.setMouldStatus(3);
			mould.setIsCompromise(1);
			
			mould.setReceiveTime(new Date());
			mould.setKeeper(user.getRealname());
			mould.setKeeperId(user.getId());
			
			mouldService.insert(user.getId(), mould);
		} else {
			Mould mould = new Mould();
			mould.setMouldStatus(3);
			mouldService.updateActiveById(user.getId(), mould, mouldId);
		}
		MouldCheck mouldCheck = new MouldCheck();
		mouldCheck.setCheckStatus(3);
		log.debug("MouldCheckController.mouldCheckRepair>>>");
		return new AjaxReturn(200, null, mouldCheckService.updateActiveById(user.getId(), mouldCheck, pkId));
	}

	@ApiOperation(value = "根据Id查询模具质检信息 ", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "根据Id查询模具质检信息查询成功", response = MouldCheck.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/mould-check-scrip")
	@OperateLog(info = "模具不合格判定报废[模具编码:%s]", params = { "mouldNumber" })
	public AjaxReturn mouldCheckScrip(@ApiParam(name = "pkId", value = "pkId", required = true) @RequestParam(required = true) Long pkId, @ApiParam(name = "mouldId", value = "模具ID", required = false) @RequestParam(required = false, defaultValue = "10") Long mouldId, @ApiParam(name = "mouldNumber", value = "mouldNumber", required = false) @RequestParam(required = false) String mouldNumber, @ApiParam(name = "mouldReceiptId", value = "模具编码", required = true) @RequestParam(required = true) Long mouldReceiptId, @ApiParam(name = "checkType", value = "质检类型", required = false) @RequestParam(required = false, defaultValue = "10") Integer checkType) throws BusinessException {
		log.debug("MouldCheckController.mouldCheckScrip<<<");

		// 获取当前用户
		CustomUser user = getCompleteAuthentication();
		// 封装条件
		MouldCheck mouldCheck = new MouldCheck();
		mouldCheck.setCheckStatus(3);

		// 根据质检类型判断处理方式
		if (checkType == 2) {
			// 如果是修磨质检，直接更改台账允许报废
			Mould mould = mouldService.selectById(user.getId(), mouldId);
			mould.setIsScrip(1);
			mouldService.updateActiveById(user.getId(), mould, mouldId);
		} else if (checkType == 1) {
			// 如果新量具不合格执行报废，则更改其状态为3-不合格重制
			MouldReceipt mouldReceipt = mouldReceiptService.selectById(user.getId(), mouldReceiptId);
			mouldReceipt.setMouldStatus(3);
			mouldReceipt.setScripHandler(user.getRealname());
			mouldReceipt.setScripHandlerTime(new Date());
			mouldReceiptService.updateActiveById(user.getId(), mouldReceipt, mouldReceiptId);
		}

		log.debug("MouldCheckController.mouldCheckScrip>>>");
		return new AjaxReturn(200, null, mouldCheckService.updateActiveById(user.getId(), mouldCheck, pkId));
	}

	/**
	 * 查询模具分页列表
	 */
	@ApiOperation(value = "查询模具分页列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "模具分页列表查询成功", response = Mould.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/new-mould-scrip-page-list")
	public AjaxReturn mouldPageList(@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, //
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows, @ApiParam(name = "mouldNumber", value = "模具编码", required = false) @RequestParam(required = false) String mouldNumber, @ApiParam(name = "mouldBarcode", value = "模具条码", required = false) @RequestParam(required = false) String mouldBarcode, @ApiParam(name = "beginDate", value = "开始日期", required = false) @RequestParam(required = false) Date beginDate, @ApiParam(name = "endDate", value = "结束日期", required = false) @RequestParam(required = false) Date endDate) throws BusinessException {
		log.debug("mouldController.mouldPageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("page:" + page);
			log.debug("rows:" + rows);
			log.debug("mouldNumber:" + mouldNumber);
			log.debug("beginDate:" + beginDate);
			log.debug("endDate:" + endDate);
		}

		// 获取当前用户
		Long curuserId = 1l;
		// 参数校验
		Validate.isTrue(curuserId > 0);
		Validate.isTrue(page >= 0);
		Validate.isTrue(rows <= 100);
		// 封装查询条件
		QueryDto queryDto = new QueryDto();
		queryDto.setPage(page);
		queryDto.setRows(rows);
		queryDto.setBeginDate(beginDate);
		queryDto.setEndDate(endDate);

		MouldReceipt mouldReceipt = new MouldReceipt();
		mouldReceipt.setMouldStatus(3);
		if (StringUtils.isNotEmpty(mouldBarcode)) {
			int index = mouldBarcode.indexOf("-");
			mouldNumber = mouldBarcode.substring(0, index);
			String mouldSeq = mouldBarcode.substring(index + 1, index + 6);
			mouldReceipt.setMouldSeq(mouldSeq);
			String mouldMap = mouldBarcode.substring(index + 7, mouldBarcode.length());
			mouldReceipt.setMouldMap(mouldMap);
		}
		mouldReceipt.setMouldNumber(mouldNumber);

		Pagination<MouldReceipt> pagination = mouldReceiptService.selectPageList(curuserId, mouldReceipt, queryDto);
		log.debug("mouldController.mouldPageList>>>");
		return pagination;
	}

}
