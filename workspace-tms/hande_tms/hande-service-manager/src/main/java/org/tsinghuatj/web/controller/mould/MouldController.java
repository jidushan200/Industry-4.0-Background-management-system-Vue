package org.tsinghuatj.web.controller.mould;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.odftoolkit.odfdom.converter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.tsinghuatj.framework.constants.MouldStatusEnum;
import org.tsinghuatj.framework.domain.AjaxReturn;
import org.tsinghuatj.framework.domain.CustomUser;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.framework.utils.excel.ExcelUtils;
import org.tsinghuatj.framework.web.controller.BaseController;
import org.tsinghuatj.framework.web.support.annotation.Secure;
import org.tsinghuatj.mould.domain.Mould;
import org.tsinghuatj.mould.domain.MouldCheck;
import org.tsinghuatj.mould.domain.MouldOutbound;
import org.tsinghuatj.mould.domain.MouldReceipt;
import org.tsinghuatj.mould.domain.MouldScripApply;
import org.tsinghuatj.mould.domain.MouldWarehouse;
import org.tsinghuatj.mould.service.IMouldOutboundService;
import org.tsinghuatj.mould.service.IMouldReceiptService;
import org.tsinghuatj.mould.service.IMouldScripApplyService;
import org.tsinghuatj.mould.service.IMouldService;
import org.tsinghuatj.mould.service.IMouldWarehouseService;
import org.tsinghuatj.support.annotation.OperateLog;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping({ "/mould" })
public class MouldController extends BaseController {

	private @Autowired(required = false) IMouldService mouldService;
	private @Autowired(required = false) IMouldScripApplyService mouldScripApplyService;
	private @Autowired(required = false) IMouldWarehouseService mouldWarehouseService;
	private @Autowired(required = false) IMouldOutboundService mouldOutBoundService;
	private @Autowired(required = false) IMouldReceiptService mouldReceiptService;

	/**
	 * 模具入库
	 */
	@ApiOperation(value = "模具入库", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "模具入库成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/mould-receipt")
	@OperateLog(info = "模具入库[模具条码:%s]", params = { "fullNumber" })
	public AjaxReturn mouldReceipt(@ApiParam(name = "mouldNumber", value = "物料编码", required = true) @RequestParam(required = true) String mouldNumber, @ApiParam(name = "mouldSeq", value = "模具序号", required = false) @RequestParam(required = false) String mouldSeq, @ApiParam(name = "mouldMap", value = "物料图号", required = false) @RequestParam(required = false) String mouldMap, @ApiParam(name = "mouldName", value = "物料名称", required = false) @RequestParam(required = false) String mouldName, @ApiParam(name = "mouldType", value = "物料名称", required = false) @RequestParam(required = false) Integer mouldType, @ApiParam(name = "lifeMax", value = "最大寿命", required = false) @RequestParam(required = false) Integer lifeMax,
			@ApiParam(name = "heatNumber", value = "热处理批次号", required = false) @RequestParam(required = false) String heatNumber, @ApiParam(name = "surfaceNumber", value = "表面处理批次号", required = false) @RequestParam(required = false) String surfaceNumber, @ApiParam(name = "embryoCode", value = "表面处理批次号", required = false) @RequestParam(required = false) String embryoCode, @ApiParam(name = "embryoName", value = "表面处理批次号", required = false) @RequestParam(required = false) String embryoName

	) throws BusinessException {
		log.debug("mouldController.mouldReceipt<<<");
		if (log.isDebugEnabled()) {
			log.debug("mouldNumber:" + mouldNumber);
			log.debug("mouldMap:" + mouldMap);
			log.debug("mouldName:" + mouldName);
			log.debug("mouldSeq:" + mouldSeq);
		}
		// 获取当前用户
		CustomUser user = getCompleteAuthentication();
		// 封装参数信息
		MouldReceipt mouldReceipt = new MouldReceipt();
		mouldReceipt.setMouldSeq(mouldSeq);
		mouldReceipt.setMouldNumber(mouldNumber);
		mouldReceipt.setMouldMap(mouldMap);
		mouldReceipt.setMouldName(mouldName);
		mouldReceipt.setMouldType(mouldType);
		if (lifeMax == null) {
			lifeMax = 0;
		}
		mouldReceipt.setLifeMax(lifeMax);
		mouldReceipt.setHeatNumber(heatNumber);
		mouldReceipt.setSurfaceNumber(surfaceNumber);
		// 新模具默认状态为待检
		mouldReceipt.setMouldStatus(0);
		mouldReceipt.setCheckType(1);
		mouldReceipt.setEmbryoCode(embryoCode);
		mouldReceipt.setEmbryoName(embryoName);

		log.debug("mouldController.mouldReceipt>>>");

		return new AjaxReturn(200, null, mouldReceiptService.insert(user.getId(), mouldReceipt));
	}

	/**
	 * 模具入库
	 */
	@ApiOperation(value = "模具入库", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "模具入库成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/mould-warehouse")
	@OperateLog(info = "模具入库[模具条码:%s]", params = { "mouldBarcode" })
	public AjaxReturn mouldAdd(@ApiParam(name = "pkId", value = "pkId", required = false) @RequestParam(required = false) Long pkId, @ApiParam(name = "warehouse", value = "库位", required = false) @RequestParam(required = false) String warehouse) throws BusinessException {
		log.debug("mouldController.mouldAdd<<<");
		if (log.isDebugEnabled()) {
			log.debug("warehouse:" + warehouse);
		}
		// 获取当前用户
		CustomUser user = getCompleteAuthentication();
		Date date = new Date();
		// 封装参数信息
		Mould mould = mouldService.selectById(user.getId(), pkId);
		mould.setWarehouse(warehouse);
		mould.setKeeperId(user.getId());
		mould.setKeeper(user.getRealname());
		mould.setWarehouseTime(date);
		// 新模具默认状态为待检
		mould.setMouldStatus(1);

		MouldWarehouse mouldWarehouse = new MouldWarehouse();

		mouldWarehouse.setMouldId(pkId);
		mouldWarehouse.setMouldNumber(mould.getMouldNumber());
		mouldWarehouse.setMouldName(mould.getMouldName());
		mouldWarehouse.setWarehouseCode(mould.getWarehouseCode());
		mouldWarehouse.setMouldMap(mould.getMouldMap());
		mouldWarehouse.setFullNumber(mould.getMouldBarcode());
		mouldWarehouse.setKeeperId(user.getId());
		mouldWarehouse.setKeeper(user.getRealname());
		mouldWarehouse.setWarehouse(warehouse);
		mouldWarehouse.setWarehosingTime(date);

		mouldWarehouseService.insert(user.getId(), mouldWarehouse);

		log.debug("mouldController.mouldAdd>>>");

		return new AjaxReturn(200, null, mouldService.updateActiveById(user.getId(), mould, pkId));
	}

	/**
	 * 量具出库
	 */
	@ApiOperation(value = "量具出库", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "量具出库成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/mould-outbound")
	@OperateLog(info = "模具出库[领用人%s->:量具条码%s]", params = { "receiver", "mouldBarcode" })
	public AjaxReturn mouldOutbound(@ApiParam(name = "pkId", value = "pkId", required = false) @RequestParam(required = false, defaultValue = "") Long pkId, @ApiParam(name = "mouldBarcode", value = "物料条码", required = false) @RequestParam(required = false, defaultValue = "") String mouldBarcode, @ApiParam(name = "mouldNumber", value = "物料编码", required = false) @RequestParam(required = false, defaultValue = "") String mouldNumber, @ApiParam(name = "mouldMap", value = "型号规格", required = false) @RequestParam(required = false, defaultValue = "") String mouldMap, @ApiParam(name = "mouldName", value = "物料名称", required = false) @RequestParam(required = false, defaultValue = "") String mouldName,
			@ApiParam(name = "warehouse", value = "库位", required = false) @RequestParam(required = false, defaultValue = "") String warehouse, @ApiParam(name = "outType", value = "出库类型", required = false) @RequestParam(required = false, defaultValue = "") Integer outType, @ApiParam(name = "departmentId", value = "使用部门id", required = false) @RequestParam(required = false, defaultValue = "") Long departmentId, @ApiParam(name = "departmentName", value = "领用部门", required = false) @RequestParam(required = false, defaultValue = "") String departmentName, @ApiParam(name = "receiveClassId", value = "使用班组id", required = false) @RequestParam(required = false, defaultValue = "") Long receiveClassId,
			@ApiParam(name = "receiveClass", value = "领用班组", required = false) @RequestParam(required = false, defaultValue = "") String receiveClass, @ApiParam(name = "receiverId", value = "使用人id", required = false) @RequestParam(required = false, defaultValue = "") Long receiverId, @ApiParam(name = "receiver", value = "领用人", required = false) @RequestParam(required = false, defaultValue = "") String receiver, @ApiParam(name = "remark", value = "说明", required = false) @RequestParam(required = false, defaultValue = "") String remark) throws BusinessException {
		log.debug("mouldController.mouldOutbound<<<");
		// 获取当前用户
		CustomUser user = getCompleteAuthentication();

		// 封装参数信息
		Mould mould = new Mould();
		mould.setKeeperId(user.getId());
		mould.setKeeper(user.getRealname());
		mould.setUseDepartmentId(departmentId);
		mould.setUseDepartmentName(departmentName);
		mould.setUseTeamId(receiveClassId);
		mould.setUseTeamName(receiveClass);
		mould.setUserId(receiverId);
		mould.setUserName(receiver);
		mould.setReceiveTime(new Date());
		mould.setOutRemark(remark);
		if (outType == 1) {
			mould.setMouldStatus(2);
		} else if (outType == 2) {
			mould.setMouldStatus(3);
		}

		MouldOutbound mouldOutbound = new MouldOutbound();
		mouldOutbound.setMouldId(pkId);
		mouldOutbound.setMouldBarcode(mouldBarcode);
		mouldOutbound.setMouldNumber(mouldNumber);
		mouldOutbound.setMouldName(mouldName);
		mouldOutbound.setMouldMap(mouldMap);
		mouldOutbound.setWarehouse(warehouse);
		mouldOutbound.setOutType(outType);
		mouldOutbound.setUseDepartmentId(departmentId);
		mouldOutbound.setUseDepartmentName(departmentName);
		mouldOutbound.setUseTeamId(receiveClassId);
		mouldOutbound.setUseTeamName(receiveClass);
		mouldOutbound.setUserId(receiverId);
		mouldOutbound.setUserName(receiver);
		mouldOutbound.setKeeperId(user.getId());
		mouldOutbound.setKeeper(user.getRealname());
		mouldOutbound.setRemark(remark);
		mouldOutbound.setReceiveTime(new Date());
		mouldOutBoundService.insert(user.getId(), mouldOutbound);
		log.debug("mouldController.mouldOutbound>>>");

		return new AjaxReturn(200, null, mouldService.updateActiveById(user.getId(), mould, pkId));
	}

	/**
	 * 量具返库
	 */
	@ApiOperation(value = "量具返库", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "量具返库成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/mould-return")
	@OperateLog(info = "量具返库[模具条码%s]", params = { "mouldBarcode" })
	public AjaxReturn mouldReturn(@ApiParam(name = "pkId", value = "pkId", required = false) @RequestParam(required = false, defaultValue = "") Long pkId, @ApiParam(name = "mouldBarcode", value = "库位", required = false) @RequestParam(required = false, defaultValue = "") String mouldBarcode, @ApiParam(name = "warehouse", value = "库位", required = false) @RequestParam(required = false, defaultValue = "") String warehouse, @ApiParam(name = "remark", value = "说明", required = false) @RequestParam(required = false, defaultValue = "") String remark) throws BusinessException {
		log.debug("mouldController.mouldReturn<<<");
		// 获取当前用户
		CustomUser user = getCompleteAuthentication();
		Date date = new Date();
		// 封装参数信息
		Mould mould = mouldService.selectById(user.getId(), pkId);
		mould.setWarehouse(warehouse);
		mould.setKeeperId(user.getId());
		mould.setKeeper(user.getRealname());
		mould.setMouldStatus(1);
		mould.setReturnRemark(remark);
		MouldWarehouse mouldWarehouse = new MouldWarehouse();
		mouldWarehouse.setMouldId(pkId);
		mouldWarehouse.setMouldNumber(mould.getMouldNumber());
		mouldWarehouse.setMouldName(mould.getMouldName());
		mouldWarehouse.setMouldMap(mould.getMouldMap());
		mouldWarehouse.setFullNumber(mould.getMouldBarcode());
		mouldWarehouse.setKeeperId(user.getId());
		mouldWarehouse.setKeeper(user.getRealname());
		mouldWarehouse.setWarehouse(warehouse);
		mouldWarehouse.setRemark(remark);
		mouldWarehouse.setWarehosingTime(date);
		mouldWarehouse.setInType(3);
		mouldWarehouseService.insert(user.getId(), mouldWarehouse);
		log.debug("mouldController.mouldReturn>>>");

		return new AjaxReturn(200, null, mouldService.updateActiveById(user.getId(), mould, pkId));
	}

	/**
	 * 模具删除
	 */
	@ApiOperation(value = "模具删除", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "模具删除成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/mould-delete-by-id")
	@OperateLog(info = "模具报废[模具条码%s]", params = { "mouldBarcode" })
	public AjaxReturn mouldDeleteById(@ApiParam(name = "pkId", value = "主键", required = true) @RequestParam(required = true) Long pkId, @ApiParam(name = "mouldBarcode", value = "mouldBarcode", required = true) @RequestParam(required = true) String mouldBarcode) throws BusinessException {
		log.debug("mouldController.mouldDeleteById<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
		}

		// 获取当前用户
		CustomUser user = getCompleteAuthentication();
		// 参数校验

		Validate.isTrue(pkId > 0);
		Mould mould = mouldService.selectById(user.getId(), pkId);
		mould.setScripHandler(user.getRealname());
		mould.setScripHandleTime(new Date());
		mould.setMouldStatus(7);

		MouldScripApply scripApply = mouldScripApplyService.selectByFullNumber(user.getId(), mould.getMouldBarcode());
		if (scripApply != null) {
			scripApply.setApplyStatus(3);
			mouldScripApplyService.updateActiveById(user.getId(), scripApply, scripApply.getPkId());
		}

		log.debug("mouldController.mouldDeleteById>>>");
		return new AjaxReturn(200, null, mouldService.updateActiveById(user.getId(), mould, mould.getPkId()));
	}

	/**
	 * 根据主键查找
	 */
	@ApiOperation(value = "模具id查询列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "模具Id查询列表查询成功", response = Mould.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/mould-get-by-id")
	public AjaxReturn mouldGetById(@ApiParam(name = "pkId", value = "pkId", required = true) @RequestParam(required = true) Long pkId) throws BusinessException {
		log.debug("mouldController.mouldGetById<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
		}

		// 获取当前用户
		Long curuserId = getAuthentication();
		// 参数校验

		Validate.isTrue(pkId >= 1);

		log.debug("mouldController.mouldGetById>>>");
		return new AjaxReturn(200, null, mouldService.selectById(curuserId, pkId));
	}

	/**
	 * 根据完整编码查找
	 */
	@ApiOperation(value = "根据编码查找", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "根据编码查找", response = Mould.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/get-mould-seq")
	public AjaxReturn getMouldSeq(@ApiParam(name = "mouldNumber", value = "mouldNumber", required = true) @RequestParam(required = true) String mouldNumber) throws BusinessException {
		log.debug("mouldController.mouldGetByFullNumber<<<");
		if (log.isDebugEnabled()) {
			log.debug("mouldNumber:" + mouldNumber);
		}

		// 获取当前用户
		Long curuserId = getAuthentication();
		// 参数校验

		log.debug("mouldController.mouldGetByFullNumber>>>");
		return new AjaxReturn(200, null, mouldService.selectSeqByMouldNumber(curuserId, mouldNumber));
	}

	/**
	 * 根据完整编码查找
	 */
	@ApiOperation(value = "根据编码查找", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "根据编码查找", response = Mould.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/mould-get-by-full-number")
	public AjaxReturn mouldGetByFullNumber(@ApiParam(name = "fullNumber", value = "fullNumber", required = true) @RequestParam(required = true) String fullNumber) throws BusinessException {
		log.debug("mouldController.mouldGetByFullNumber<<<");
		if (log.isDebugEnabled()) {
			log.debug("fullNumber:" + fullNumber);
		}

		// 获取当前用户
		Long curuserId = getAuthentication();
		// 参数校验
		Validate.isTrue(StringUtils.isNotEmpty(fullNumber));

		log.debug("mouldController.mouldGetByFullNumber>>>");
		return new AjaxReturn(200, null, mouldService.selectByFullNumber(curuserId, fullNumber));
	}

	/**
	 * 查询模具分页列表
	 */
	@ApiOperation(value = "查询模具分页列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "模具分页列表查询成功", response = Mould.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/mould-wait-page-list")
	public AjaxReturn mouldWaitPageList(@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, //
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows,
			@ApiParam(name = "mouldBarcode", value = "模具条码", required = false) @RequestParam(required = false) String mouldBarcode,
			@ApiParam(name = "beginDate", value = "开始日期", required = false) @RequestParam(required = false) Date beginDate, 
			@ApiParam(name = "endDate", value = "结束日期", required = false) @RequestParam(required = false) Date endDate) throws BusinessException {
		log.debug("mouldController.mouldPageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("page:" + page);
			log.debug("rows:" + rows);
			log.debug("mouldBarcode:" + mouldBarcode);
			log.debug("beginDate:" + beginDate);
			log.debug("endDate:" + endDate);
		}

		// 获取当前用户
		Long curuserId = getAuthentication();
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

		Mould mould = new Mould();
		mould.setMouldBarcode(mouldBarcode);

		Pagination<Mould> pagination = mouldService.selectPageList(curuserId, mould, queryDto);
		log.debug("mouldController.mouldPageList>>>");
		return pagination;
	}

	/**
	 * 查询模具分页列表
	 */
	@ApiOperation(value = "查询模具分页列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "模具分页列表查询成功", response = Mould.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/mould-page-list")
	public AjaxReturn mouldPageList(@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, //
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows, @ApiParam(name = "sort", value = "排序", required = false) @RequestParam(required = false) String sort, @ApiParam(name = "mouldBarcode", value = "模具码", required = false) @RequestParam(required = false, defaultValue = "") String mouldBarcode, @ApiParam(name = "mouldNumber", value = "模具编码", required = false) @RequestParam(required = false, defaultValue = "") String mouldNumber, @ApiParam(name = "mouldStatus", value = "模具状态", required = false) @RequestParam(required = false, defaultValue = "") Integer mouldStatus,
			@ApiParam(name = "beginDate", value = "开始日期", required = false) @RequestParam(required = false, defaultValue = "") Date beginDate, @ApiParam(name = "endDate", value = "结束日期", required = false) @RequestParam(required = false, defaultValue = "") Date endDate, @ApiParam(name = "mouldMap", value = "模具图号", required = false) @RequestParam(required = false, defaultValue = "") String mouldMap, @ApiParam(name = "isScripList", value = "模具名称", required = false) @RequestParam(required = false, defaultValue = "") Integer isScripList) throws BusinessException {
		log.debug("mouldController.mouldPageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("page:" + page);
			log.debug("rows:" + rows);
			log.debug("mouldBarcode:" + mouldBarcode);
			log.debug("mouldNumber:" + mouldNumber);
			log.debug("mouldMap:" + mouldMap);
			log.debug("mouldStatus:" + mouldStatus);
			log.debug("beginDate:" + beginDate);
			log.debug("endDate:" + endDate);
		}

		// 获取当前用户
		Long curuserId = getAuthentication();
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
		queryDto.setSort(sort);
		Mould mould = new Mould();
		mould.setMouldBarcode(mouldBarcode);
		mould.setMouldNumber(mouldNumber);
		mould.setMouldMap(mouldMap);

		
		if (null != mouldStatus) {
			if (mouldStatus == 7) {
				mould.setIsScrip(1);
			} else {
				mould.setMouldStatus(mouldStatus);
			}
		}
		mould.setIsScripList(isScripList);
		mould.setIsList(1);

		Pagination<Mould> pagination = mouldService.selectPageList(curuserId, mould, queryDto);
		log.debug("mouldController.mouldPageList>>>");
		return pagination;
	}

	/**
	 * 查询模具全生命周期分页列表
	 */
	@ApiOperation(value = "查询模具全生命周期分页列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "模具全生命周期分页列表查询成功", response = Mould.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/mould-life-page-list")
	public AjaxReturn mouldLifePageList(@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, //
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false) Integer rows, 
			@ApiParam(name = "mouldBarcode", value = "模具码", required = false) @RequestParam(required = false) String mouldBarcode, 
			@ApiParam(name = "beginDate", value = "开始日期", required = false) @RequestParam(required = false) Date beginDate, 
			@ApiParam(name = "endDate", value = "结束日期", required = false) @RequestParam(required = false) Date endDate, 
			@ApiParam(name = "mouldNumber", value = "模具编码", required = false) @RequestParam(required = false) String mouldNumber,
			@ApiParam(name = "mouldName", value = "模具名称", required = false) @RequestParam(required = false) String mouldName
			) throws BusinessException {
		log.debug("mouldController.mouldLifePageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("page:" + page);
			log.debug("rows:" + rows);
			log.debug("mouldBarcode:" + mouldBarcode);
			log.debug("mouldNumber:" + mouldNumber);
			log.debug("beginDate:" + beginDate);
			log.debug("endDate:" + endDate);
		}

		// 获取当前用户
		Long curuserId = getAuthentication();
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

		Mould mould = new Mould();
		mould.setMouldBarcode(mouldBarcode);
		mould.setMouldNumber(mouldNumber);
		mould.setMouldName(mouldName);
		Pagination<Mould> pagination = mouldService.selectLifePageList(curuserId, mould, queryDto);
		log.debug("mouldController.mouldLifePageList>>>");
		return pagination;
	}

	/**
	 * 模具台账信息导入
	 * 
	 * @param request
	 * @return
	 * @throws BusinessException
	 */
	@ApiOperation(value = "模具台账信息导入", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "模具台账信息导入", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/mould-import")
	@OperateLog(info = "模具台账导入")
	// @Secure()
	public AjaxReturn mouldImport(@RequestParam(value = "file", required = true) MultipartFile file) throws Exception {
		CustomUser user = getCompleteAuthentication();
		byte[] data = file.getBytes();
		InputStream inputStream = new ByteArrayInputStream(data);
		List<Mould> mouldList = ExcelUtils.getInstance().readExcel2Objects(inputStream, Mould.class, 1, 1000, 0);
		inputStream.close();
		mouldService.mouldImport(user.getId(), mouldList);
		return new AjaxReturn(200, null, 1);
	}

	@ApiOperation(value = "模具台账导出", notes = "", code = 200, produces = "application/json")
	@ApiResponses({ @ApiResponse(code = 200, message = "模具台账导出", response = String.class) })
	@Secure()
	@GetMapping(path = { "/mould-list-export" })
	@OperateLog(info = "模具台账导出")
	public String mouldListExport(HttpServletRequest request, HttpServletResponse response, @ApiParam(name = "mouldBarcode", value = "模具码", required = false) @RequestParam(required = false) String mouldBarcode, @ApiParam(name = "mouldNumber", value = "刀具码", required = false) @RequestParam(required = false) String mouldNumber, @ApiParam(name = "beginDate", value = "开始日期", required = false) @RequestParam(required = false) Date beginDate, @ApiParam(name = "endDate", value = "结束日期", required = false) @RequestParam(required = false) Date endDate) throws Exception {

		Long curuserId = getAuthentication();
		QueryDto queryDto = new QueryDto();
		queryDto.setPage(1);
		queryDto.setRows(100000);
		queryDto.setBeginDate(beginDate);
		queryDto.setEndDate(endDate);

		Mould mould = new Mould();
		mould.setMouldBarcode(mouldBarcode);
		mould.setMouldNumber(mouldNumber);
		mould.setIsList(1);

		Pagination<Mould> pagination = mouldService.selectPageList(curuserId, mould, queryDto);
		// 创建HSSFWorkbook对象(excel的文档对象)
		HSSFWorkbook wb = new HSSFWorkbook();
		// 建立新的sheet对象（excel的表单）
		HSSFSheet sheet = wb.createSheet("模具台账");
		// 在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
		HSSFRow row1 = sheet.createRow(0);
		row1.setHeight((short) (25 * 20));//
		// 创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个

		HSSFCellStyle cellStyle = wb.createCellStyle();
		// 设置单元格内容
		row1.createCell(0).setCellValue("模具编码");
		row1.createCell(1).setCellValue("模具名称");
		row1.createCell(2).setCellValue("模具图号");
		row1.createCell(3).setCellValue("模具条码");
		row1.createCell(4).setCellValue("库位");
		row1.createCell(5).setCellValue("模具坯编码");
		row1.createCell(6).setCellValue("模具坯名称");
		row1.createCell(7).setCellValue("库管员");
		row1.createCell(8).setCellValue("模具数量");
		row1.createCell(9).setCellValue("让步状态");
		row1.createCell(10).setCellValue("模具状态");
		row1.createCell(11).setCellValue("领用部门");
		row1.createCell(12).setCellValue("领用班组");
		row1.createCell(13).setCellValue("领用人");
		row1.createCell(14).setCellValue("入库时间");
		row1.createCell(15).setCellValue("领用时间");
		row1.createCell(16).setCellValue("最后更新时间");
		row1.createCell(17).setCellValue("已加工次数");
		row1.createCell(18).setCellValue("已加工数量");
		row1.createCell(19).setCellValue("最大生产寿命");
		row1.createCell(20).setCellValue("热处理批次号");
		row1.createCell(21).setCellValue("表面处理批次号");
		row1.createCell(22).setCellValue("已修磨次数");
		row1.createCell(23).setCellValue("修磨人");
		row1.createCell(24).setCellValue("修磨时间");
		row1.createCell(25).setCellValue("出库备注");
		row1.createCell(26).setCellValue("返库备注");
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

		/*
		 * for (int i = 0; i <= 10; i++) {
		 * row1.getCell(i).setCellStyle(cellStyle); }
		 * row1.getCell(13).setCellStyle(cellStyle);
		 * row1.getCell(15).setCellStyle(cellStyle);
		 */

		row1.setHeight((short) 400);// 目的是想把行高设置成25px
		sheet.setColumnWidth(0, 7000);
		sheet.setColumnWidth(1, 7000);
		sheet.setColumnWidth(2, 5000);
		sheet.setColumnWidth(3, 4000);
		sheet.setColumnWidth(4, 4000);
		sheet.setColumnWidth(5, 4000);
		sheet.setColumnWidth(6, 5000);
		sheet.setColumnWidth(7, 4000);
		sheet.setColumnWidth(8, 4000);
		sheet.setColumnWidth(9, 5000);
		sheet.setColumnWidth(10, 5000);
		sheet.setColumnWidth(11, 3000);
		sheet.setColumnWidth(12, 5000);
		sheet.setColumnWidth(13, 3000);
		sheet.setColumnWidth(14, 5000);
		sheet.setColumnWidth(15, 6000);
		sheet.setColumnWidth(16, 5000);
		sheet.setColumnWidth(17, 6000);

		int i = 1;

		HSSFCellStyle cellStyle1 = wb.createCellStyle();
		cellStyle1.setVerticalAlignment(VerticalAlignment.CENTER);

		for (Mould item : pagination.getRows()) {

			HSSFRow dataRow = sheet.createRow(i);
			dataRow.setHeight((short) (25 * 20));//

			dataRow.createCell(0).setCellValue(item.getMouldNumber());
			dataRow.createCell(1).setCellValue(item.getMouldName());
			dataRow.createCell(2).setCellValue(item.getMouldMap());
			dataRow.createCell(3).setCellValue(item.getMouldBarcode());
			dataRow.createCell(4).setCellValue(item.getWarehouse());
			dataRow.createCell(5).setCellValue(item.getEmbryoCode());
			dataRow.createCell(6).setCellValue(item.getEmbryoName());
			dataRow.createCell(7).setCellValue(item.getKeeper());
			dataRow.createCell(8).setCellValue(item.getMouldAmount());
			if (null != item.getIsCompromise() && 1 == item.getIsCompromise()) {
				dataRow.createCell(9).setCellValue("让步使用");
			} else {
				dataRow.createCell(9).setCellValue("正常使用");
			}
			dataRow.createCell(10).setCellValue(MouldStatusEnum.getName(item.getMouldStatus()));
			dataRow.createCell(11).setCellValue(item.getUseDepartmentName());
			dataRow.createCell(12).setCellValue(item.getUseTeamName());
			dataRow.createCell(13).setCellValue(item.getUserName());
			if (null != item.getWarehouseTime()) {
				dataRow.createCell(14).setCellValue(DateFormatUtils.format(item.getWarehouseTime(), "yyyy-MM-dd HH:mm:ss"));
			}
			if (null != item.getReceiveTime()) {
				dataRow.createCell(15).setCellValue(DateFormatUtils.format(item.getReceiveTime(), "yyyy-MM-dd HH:mm:ss"));
			}
			dataRow.createCell(16).setCellValue(DateFormatUtils.format(item.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
			dataRow.createCell(17).setCellValue(item.getProcessTimes());
			dataRow.createCell(18).setCellValue(item.getProcessAmount());
			if (null != item.getLifeMax()) {
				dataRow.createCell(19).setCellValue(item.getLifeMax());
			}

			dataRow.createCell(20).setCellValue(item.getHeatNumber());
			dataRow.createCell(21).setCellValue(item.getSurfaceNumber());
			dataRow.createCell(22).setCellValue(item.getRepairTimes());
			dataRow.createCell(23).setCellValue(item.getGrinder());
			if (null != item.getRepairTime()) {
				dataRow.createCell(24).setCellValue(DateFormatUtils.format(item.getRepairTime(), "yyyy-MM-dd HH:mm:ss"));
			}
			dataRow.createCell(25).setCellValue(item.getOutRemark());
			dataRow.createCell(26).setCellValue(item.getReturnRemark());
			i++;
		}

		// 输出Excel文件
		String fileName = "模具台账.xls";
		// 获得浏览器信息并转换为大写
		String agent = request.getHeader("User-Agent").toUpperCase();
		if (agent.indexOf("MSIE") > 0 || (agent.indexOf("GECKO") > 0 && agent.indexOf("RV:11") > 0)) {
			// 微软的浏览器(IE和Edge浏览器)
			fileName = URLEncoder.encode(fileName, "UTF-8");
		} else {
			fileName = new String(fileName.getBytes("UTF-8"), "iso-8859-1");
		}
		// 输出Excel文件
		OutputStream output = response.getOutputStream();
		response.reset();
		response.setHeader("Content-disposition", "attachment; filename=" + fileName);
		response.setContentType("application/msexcel");
		wb.write(output);
		output.close();
		wb.close();
		return null;
	}

	@ApiOperation(value = "模具生命周期导出", notes = "", code = 200, produces = "application/json")
	@ApiResponses({ @ApiResponse(code = 200, message = "模具生命周期导出", response = Mould.class) })
	@Secure()
	@GetMapping(path = { "/mould-life-export" })
	@OperateLog(info = "模具生命周期导出")
	public String mouldLifeExport(HttpServletRequest request, HttpServletResponse response, @ApiParam(name = "mouldBarcode", value = "刀具码", required = false) @RequestParam(required = false, defaultValue = "") String mouldBarcode, @ApiParam(name = "mouldNumber", value = "刀具码", required = false) @RequestParam(required = false, defaultValue = "") String mouldNumber, @ApiParam(name = "beginDate", value = "开始日期", required = false) @RequestParam(required = false, defaultValue = "") Date beginDate, @ApiParam(name = "endDate", value = "结束日期", required = false) @RequestParam(required = false, defaultValue = "") Date endDate) throws Exception {

		QueryDto queryDto = new QueryDto();
		queryDto.setPage(1);
		queryDto.setRows(100000);
		queryDto.setBeginDate(beginDate);
		queryDto.setEndDate(endDate);

		Mould mould = new Mould();
		mould.setMouldBarcode(mouldBarcode);
		mould.setMouldNumber(mouldNumber);

		Pagination<Mould> pagination = mouldService.selectLifePageList(22l, mould, queryDto);
		// 创建HSSFWorkbook对象(excel的文档对象)
		HSSFWorkbook wb = new HSSFWorkbook();
		// 建立新的sheet对象（excel的表单）
		HSSFSheet sheet = wb.createSheet("模具生命周期");
		// 在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
		HSSFRow row1 = sheet.createRow(0);
		row1.setHeight((short) (25 * 20));//
		// 创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个

		HSSFCellStyle cellStyle = wb.createCellStyle();
		// 设置单元格内容
		row1.createCell(0).setCellValue("模具编码");
		row1.createCell(1).setCellValue("模具名称");
		row1.createCell(2).setCellValue("模具图号");
		row1.createCell(3).setCellValue("模具条码");
		row1.createCell(4).setCellValue("模具状态");
		row1.createCell(5).setCellValue("最大寿命");
		row1.createCell(6).setCellValue("已加工次数");
		row1.createCell(7).setCellValue("已加工数量");
		row1.createCell(8).setCellValue("已修磨次数");
		row1.createCell(9).setCellValue("热处理批次号");
		row1.createCell(10).setCellValue("表面处理批次号");
		row1.createCell(11).setCellValue("质检记录");
		row1.createCell(13).setCellValue("入库记录");
		row1.createCell(15).setCellValue("出库记录");

		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

		for (int i = 0; i <= 10; i++) {
			row1.getCell(i).setCellStyle(cellStyle);
		}
		row1.getCell(13).setCellStyle(cellStyle);
		row1.getCell(15).setCellStyle(cellStyle);

		row1.setHeight((short) 400);// 目的是想把行高设置成25px
		sheet.setColumnWidth(0, 7000);
		sheet.setColumnWidth(1, 7000);
		sheet.setColumnWidth(2, 5000);
		sheet.setColumnWidth(3, 4000);
		sheet.setColumnWidth(4, 4000);
		sheet.setColumnWidth(5, 4000);
		sheet.setColumnWidth(6, 5000);
		sheet.setColumnWidth(7, 4000);
		sheet.setColumnWidth(8, 4000);
		sheet.setColumnWidth(9, 5000);
		sheet.setColumnWidth(10, 5000);
		sheet.setColumnWidth(11, 3000);
		sheet.setColumnWidth(12, 5000);
		sheet.setColumnWidth(13, 3000);
		sheet.setColumnWidth(14, 5000);
		sheet.setColumnWidth(15, 6000);
		sheet.setColumnWidth(16, 5000);
		sheet.setColumnWidth(17, 6000);
		// 合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 1, 1));
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 2, 2));
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 3, 3));
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 4, 4));
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 5, 5));
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 6, 6));
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 7, 7));
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 8, 8));
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 9, 9));
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 10, 10));

		sheet.addMergedRegion(new CellRangeAddress(0, 0, 11, 12));
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 13, 14));
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 15, 17));
		// 在sheet里创建第二行
		HSSFRow row2 = sheet.createRow(1);
		// 创建单元格并设置单元格内容
		row2.createCell(11).setCellValue("质检时间");
		row2.createCell(12).setCellValue("质检人");

		row2.createCell(13).setCellValue("入库时间");
		row2.createCell(14).setCellValue("库管员");

		row2.createCell(15).setCellValue("出库时间");
		row2.createCell(16).setCellValue("领用人");
		row2.createCell(17).setCellValue("库管员");
		row2.setHeight((short) (25 * 20));//
		for (int i = 11; i < 17; i++) {
			row2.getCell(i).setCellStyle(cellStyle);
		}
		int i = 2;
		int startRow;

		HSSFCellStyle cellStyle1 = wb.createCellStyle();
		cellStyle1.setVerticalAlignment(VerticalAlignment.CENTER);

		for (Mould item : pagination.getRows()) {
			int cCount = 0, wCount = 0, oCount = 0, maxCount = 0;
			HSSFRow dataRow = sheet.createRow(i);
			dataRow.setHeight((short) (25 * 20));//
			startRow = i;

			List<MouldCheck> checkList = item.getCheckList();
			if (!CollectionUtils.isEmpty(checkList)) {
				cCount = checkList.size();
			}
			List<MouldWarehouse> warehouseList = item.getWarehouseList();
			if (!CollectionUtils.isEmpty(warehouseList)) {
				wCount = warehouseList.size();
			}
			List<MouldOutbound> outboundList = item.getOutboundList();
			if (!CollectionUtils.isEmpty(outboundList)) {
				oCount = outboundList.size();
			}
			maxCount = cCount > wCount ? cCount : wCount;
			maxCount = maxCount > oCount ? maxCount : oCount;

			dataRow.createCell(0).setCellValue(item.getMouldNumber());

			dataRow.createCell(1).setCellValue(item.getMouldName());
			dataRow.createCell(2).setCellValue(item.getMouldMap());

			dataRow.createCell(3).setCellValue(item.getMouldBarcode());
			dataRow.createCell(4).setCellValue(MouldStatusEnum.getName(item.getMouldStatus()));
			if (null != item.getLifeMax()) {
				dataRow.createCell(5).setCellValue(item.getLifeMax());
			}

			dataRow.createCell(6).setCellValue(item.getProcessTimes());
			dataRow.createCell(7).setCellValue(item.getProcessAmount());
			dataRow.createCell(8).setCellValue(item.getRepairTimes());
			dataRow.createCell(9).setCellValue(item.getHeatNumber());
			dataRow.createCell(10).setCellValue(item.getSurfaceNumber());

			if (maxCount == 0) {
				i++;
				continue;
			}

			for (int j = 0; j < maxCount; j++) {
				if (j > 0) {
					dataRow = sheet.createRow(i);
					dataRow.setHeight((short) (25 * 20));// 目的是想把行高设置成25px
				}
				if (cCount <= j) {
					dataRow.createCell(11).setCellValue("");
					dataRow.createCell(12).setCellValue("");
					dataRow.createCell(13).setCellValue("");
				} else {
					MouldCheck check = checkList.get(j);
					dataRow.createCell(11).setCellValue(null == check.getCheckTime() ? "" : DateFormatUtils.format(check.getCheckTime(), "yyyy-MM-dd HH:mm:ss"));
					dataRow.createCell(12).setCellValue(check.getChecker());
				}
				if (wCount <= j) {
					dataRow.createCell(13).setCellValue("");
					dataRow.createCell(14).setCellValue("");
				} else {
					MouldWarehouse warehouse = warehouseList.get(j);
					dataRow.createCell(13).setCellValue(null == warehouse.getWarehosingTime() ? "" : DateFormatUtils.format(warehouse.getWarehosingTime(), "yyyy-MM-dd HH:mm:ss"));
					dataRow.createCell(14).setCellValue(warehouse.getKeeper());
				}
				if (oCount <= j) {
					dataRow.createCell(15).setCellValue("");
					dataRow.createCell(16).setCellValue("");
					dataRow.createCell(17).setCellValue("");
				} else {
					MouldOutbound outbound = outboundList.get(j);
					dataRow.createCell(15).setCellValue(null == outbound.getReceiveTime() ? "" : DateFormatUtils.format(outbound.getReceiveTime(), "yyyy-MM-dd HH:mm:ss"));
					dataRow.createCell(16).setCellValue(outbound.getUseDepartmentName() + '-' + outbound.getUseTeamName() + '-' + outbound.getUserName());
					dataRow.createCell(17).setCellValue(outbound.getKeeper());
				}
				i++;
			}

			if (maxCount == 1) {
				continue;
			}
			int endRow = startRow + maxCount - 1;
			sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 0, 0));
			sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 1, 1));
			sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 2, 2));
			sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 3, 3));
			sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 4, 4));
			sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 5, 5));
			sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 6, 6));
			sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 7, 7));
			sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 8, 8));
			sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 9, 9));
			sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 10, 10));
		}

		// 输出Excel文件
		String fileName = "模具生命周期.xls";
		// 获得浏览器信息并转换为大写
		String agent = request.getHeader("User-Agent").toUpperCase();
		if (agent.indexOf("MSIE") > 0 || (agent.indexOf("GECKO") > 0 && agent.indexOf("RV:11") > 0)) {
			// 微软的浏览器(IE和Edge浏览器)
			fileName = URLEncoder.encode(fileName, "UTF-8");
		} else {
			fileName = new String(fileName.getBytes("UTF-8"), "iso-8859-1");
		}
		// 输出Excel文件
		OutputStream output = response.getOutputStream();
		response.reset();
		response.setHeader("Content-disposition", "attachment; filename=" + fileName);
		response.setContentType("application/msexcel");
		wb.write(output);
		output.close();
		wb.close();
		return null;
	}
}
