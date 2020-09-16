package org.tsinghuatj.web.controller.measure;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.tsinghuatj.base.domain.Staff;
import org.tsinghuatj.base.service.IStaffService;
import org.tsinghuatj.framework.constants.MeasureCheckTypeEnum;
import org.tsinghuatj.framework.constants.MeasureStatusEnum;
import org.tsinghuatj.framework.constants.SealTypeEnum;
import org.tsinghuatj.framework.domain.AjaxReturn;
import org.tsinghuatj.framework.domain.CustomUser;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.framework.utils.excel.ExcelUtils;
import org.tsinghuatj.framework.web.controller.BaseController;
import org.tsinghuatj.framework.web.support.annotation.Secure;
import org.tsinghuatj.measure.domain.Measure;
import org.tsinghuatj.measure.domain.MeasureCheck;
import org.tsinghuatj.measure.domain.MeasureOutbound;
import org.tsinghuatj.measure.domain.MeasureWarehouse;
import org.tsinghuatj.measure.service.IMeasureCheckService;
import org.tsinghuatj.measure.service.IMeasureOutboundService;
import org.tsinghuatj.measure.service.IMeasureService;
import org.tsinghuatj.measure.service.IMeasureWarehouseService;
import org.tsinghuatj.support.annotation.OperateLog;
import org.tsinghuatj.tool.domain.Tool;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping({ "/measure" })
public class MeasuerController extends BaseController {

	private @Autowired(required = false) IMeasureService measureService;
	private @Autowired(required = false) IMeasureWarehouseService measureWarehouseService;
	private @Autowired(required = false) IMeasureOutboundService measureOutBoundService;
	private @Autowired(required = false) IMeasureCheckService measureCheckService;
	private @Autowired(required = false) IStaffService staffService;

	/**
	 * 量具入库
	 */
	@ApiOperation(value = "量具入库", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "量具入库成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/measure-warehouse")
	@OperateLog(info = "量具入库[库管员%s->:量具条码%s]", params = { "keeper", "measureBarcode" })
	public AjaxReturn measureWarehouse(
			@ApiParam(name = "pkId", value = "pkId", required = false) @RequestParam(required = false, defaultValue = "") Long pkId,
			@ApiParam(name = "keeperId", value = "库管员Id", required = false) @RequestParam(required = false, defaultValue = "") Long keeperId,
			@ApiParam(name = "keeper", value = "库管员", required = false) @RequestParam(required = false, defaultValue = "") String keeper,
			@ApiParam(name = "warehouse", value = "库位", required = false) @RequestParam(required = false, defaultValue = "") String warehouse,
			@ApiParam(name = "measureBarcode", value = "库位", required = false) @RequestParam(required = false, defaultValue = "") String measureBarcode,
			@ApiParam(name = "measureStatus", value = "量具状态", required = false) @RequestParam(required = false, defaultValue = "") Integer measureStatus)
			throws BusinessException {
		log.debug("MeasuerController.measureWarehouse<<<");
		// 获取当前用户
		CustomUser user = getCompleteAuthentication();

		// 封装参数信息
		Measure measure = new Measure();
		measure.setKeeperId(keeperId);
		measure.setKeeper(keeper);
		measure.setWarehouse(warehouse);
		measure.setMeasureStatus(measureStatus);
		measure.setMeasureBarcode(measureBarcode);
		measure.setStorageTime(new Date());
		measure.setSealMark(0);

		Measure measures = measureService.selectById(user.getId(), pkId);
		MeasureWarehouse measureWarehouse = new MeasureWarehouse();

		measureWarehouse.setMeasureId(pkId);
		measureWarehouse.setMeasureNumber(measures.getMeasureNumber());
		measureWarehouse.setMeasureName(measures.getMeasureName());
		measureWarehouse.setModel(measures.getModel());
		measureWarehouse.setKeeperId(keeperId);
		measureWarehouse.setKeeper(keeper);
		measureWarehouse.setWarehouse(warehouse);
		measureWarehouse.setWarehosingTime(new Date());
		measureWarehouse.setInType(1);
		measureWarehouseService.insert(user.getId(), measureWarehouse);

		return new AjaxReturn(200, null, measureService.updateActiveById(user.getId(), measure, pkId));
	}

	/**
	 * 量具入库
	 */
	@ApiOperation(value = "新量具入库", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "量具入库成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/new-measure-warehouse")
	@OperateLog(info = "量具入库[库管员%s->:量具条码%s]", params = { "keeper", "measureBarcode" })
	public AjaxReturn newMeasureWarehouse(
			@ApiParam(name = "pkId", value = "pkId", required = false) @RequestParam(required = false, defaultValue = "") Long pkId,
			@ApiParam(name = "keeperId", value = "库管员Id", required = false) @RequestParam(required = false, defaultValue = "") Long keeperId,
			@ApiParam(name = "keeper", value = "库管员", required = false) @RequestParam(required = false, defaultValue = "") String keeper,
			@ApiParam(name = "warehouse", value = "库位", required = false) @RequestParam(required = false, defaultValue = "") String warehouse,
			@ApiParam(name = "measureBarcode", value = "库位", required = false) @RequestParam(required = false, defaultValue = "") String measureBarcode
		)
			throws BusinessException {
		log.debug("MeasuerController.newMeasureWarehouse<<<");
		// 获取当前用户
		CustomUser user = getCompleteAuthentication();

		// 封装参数信息
		MeasureCheck measureCheck = measureCheckService.selectById(user.getId(), pkId);
		measureCheck.setChecker(user.getRealname());
		measureCheck.setCheckResult(1);

		Measure measure = new Measure();
		String fullNumber = measureCheck.getMeasureNumber() + '-' + measureCheck.getMeasureSeq() + '/'+ measureCheck.getModel();
		measure.setMeasureNumber(measureCheck.getMeasureNumber());
		measure.setMeasureName(measureCheck.getMeasureName());
		measure.setModel(measureCheck.getModel());
		measure.setMeasureBarcode(fullNumber);
		measure.setMeasureSeq(Integer.parseInt(measureCheck.getMeasureSeq()));
		measure.setSupplierId(measureCheck.getSupplierId());
		measure.setSupplierName(measureCheck.getSupplierName());
		measure.setCheckResult(1);
		measure.setCheckTime(measureCheck.getCheckTime());
		measure.setMeasureStatus(1);
		measure.setSealMark(0);
		measure.setRepairTimes(0);
		measure.setMeasureStatus(2);
		measure.setWarehouse(warehouse);
		measure.setStorageTime(new Date());
		measureCheck.setFullNumber(fullNumber);

		MeasureWarehouse measureWarehouse = new MeasureWarehouse();

		measureWarehouse.setMeasureId(pkId);
		measureWarehouse.setMeasureNumber(measure.getMeasureNumber());
		measureWarehouse.setMeasureName(measure.getMeasureName());
		measureWarehouse.setModel(measure.getModel());
		measureWarehouse.setKeeperId(keeperId);
		measureWarehouse.setKeeper(keeper);
		measureWarehouse.setWarehouse(warehouse);
		measureWarehouse.setWarehosingTime(new Date());
		measureWarehouse.setInType(1);
		measureWarehouse.setWarehosingTime(new Date());
		
		measureWarehouseService.insert(user.getId(), measureWarehouse);

		return new AjaxReturn(200, null, measureCheckService.checkQualified(user.getId(), measureCheck, measure));
	}

	/**
	 * 量具出库
	 */
	@ApiOperation(value = "量具出库", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "量具出库成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/measure-outbound")
	@OperateLog(info = "量具出库[库管员%s->:量具条码%s]", params = { "keeper", "measureBarcode" })
	public AjaxReturn measureOutbound(
			@ApiParam(name = "pkId", value = "pkId", required = false) @RequestParam(required = false) Long pkId,
			@ApiParam(name = "measureBarcode", value = "measureBarcode", required = false) @RequestParam(required = false, defaultValue = "") String measureBarcode,
			@ApiParam(name = "measureNumber", value = "物料编码", required = false) @RequestParam(required = false, defaultValue = "") String measureNumber,
			@ApiParam(name = "model", value = "型号规格", required = false) @RequestParam(required = false, defaultValue = "") String model,
			@ApiParam(name = "measureName", value = "物料名称", required = false) @RequestParam(required = false, defaultValue = "") String measureName,
			@ApiParam(name = "warehouse", value = "库位", required = false) @RequestParam(required = false, defaultValue = "") String warehouse,
			@ApiParam(name = "outType", value = "出库类型", required = false) @RequestParam(required = false, defaultValue = "") Integer outType,
			@ApiParam(name = "receiveResion", value = "领用原因", required = false) @RequestParam(required = false, defaultValue = "") String receiveResion,
			@ApiParam(name = "departmentId", value = "使用部门id", required = false) @RequestParam(required = false, defaultValue = "") Long departmentId,
			@ApiParam(name = "departmentName", value = "领用部门", required = false) @RequestParam(required = false, defaultValue = "") String departmentName,
			@ApiParam(name = "teamId", value = "使用班组id", required = false) @RequestParam(required = false, defaultValue = "") Long teamId,
			@ApiParam(name = "teamName", value = "领用班组", required = false) @RequestParam(required = false, defaultValue = "") String teamName,
			@ApiParam(name = "staffCode", value = "领用人", required = false) @RequestParam(required = false, defaultValue = "") String staffCode,
			@ApiParam(name = "staffName", value = "领用人", required = false) @RequestParam(required = false, defaultValue = "") String staffName,
			@ApiParam(name = "remark", value = "说明", required = false) @RequestParam(required = false, defaultValue = "") String remark)
			throws BusinessException {
		log.debug("MeasuerController.toolAdd<<<");
		// 获取当前用户
		CustomUser user = getCompleteAuthentication();

		// 封装参数信息
		Measure measure = new Measure();
		if (outType == 1) {
			measure.setUseDepartmentId(departmentId);
			measure.setUseDepartmentName(departmentName);
			measure.setUseTeamId(teamId);
			measure.setUseTeamName(teamName);
			measure.setUserCode(staffCode);
			measure.setUserName(staffName);
			measure.setMeasureStatus(3);
		}

		MeasureOutbound measureOutbound = new MeasureOutbound();
		measureOutbound.setMeasureId(pkId);
		measureOutbound.setMeasureNumber(measureNumber);
		measureOutbound.setMeasureName(measureName);
		measureOutbound.setModel(model);
		measureOutbound.setWarehouse(warehouse);
		measureOutbound.setOutType(outType);
		measureOutbound.setReceiveResion(receiveResion);
		measureOutbound.setDepartmentId(departmentId);
		measureOutbound.setDepartmentName(departmentName);
		measureOutbound.setReceiveClass(teamName);
		measureOutbound.setReceiver(staffName);
		measureOutbound.setKeeperId(user.getId());
		measureOutbound.setKeeper(user.getRealname());
		measureOutbound.setRemark(remark);
		measureOutBoundService.insert(user.getId(), measureOutbound);

		return new AjaxReturn(200, null, measureService.updateActiveById(user.getId(), measure, pkId));
	}

	/**
	 * 量具补充定检周期
	 */
	@ApiOperation(value = "量具补充定检周期", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "量具补充定检周期成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/measure-check-cycle")
	@OperateLog(info = "量具补充定检周期[量具条码%s]", params = { "measureBarcode" })
	public AjaxReturn measureCheckCycle(
			@ApiParam(name = "pkId", value = "pkId", required = false) @RequestParam(required = false, defaultValue = "") Long pkId,
			@ApiParam(name = "measureBarcode", value = "measureBarcode", required = false) @RequestParam(required = false, defaultValue = "") String measureBarcode,
			@ApiParam(name = "checkCycle", value = "定检周期", required = false) @RequestParam(required = false, defaultValue = "1") Integer checkCycle,
			@ApiParam(name = "factoryMetrology", value = "本厂计量编号", required = false) @RequestParam(required = false, defaultValue = "") String factoryMetrology,
			@ApiParam(name = "manufacturingNumber", value = "厂家编号", required = false) @RequestParam(required = false, defaultValue = "") String manufacturingNumber)
			throws BusinessException {
		log.debug("MeasuerController.measureCheckCycle<<<");
		// 获取当前用户
		CustomUser user = getCompleteAuthentication();
		Date date = new Date();
		// 封装参数信息
		Measure measure = new Measure();
		measure.setCheckCycle(checkCycle);
		measure.setCheckTime(date);
		measure.setFactoryMetrology(factoryMetrology);
		measure.setManufacturingNumber(manufacturingNumber);

		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(GregorianCalendar.DAY_OF_MONTH, checkCycle);// 把日期往后增加一个周期日.整数往后推,负数往前移动
		date = calendar.getTime();
		measure.setNextCheckTime(date);
		measure.setMeasureStatus(4);

		return new AjaxReturn(200, null, measureService.updateActiveById(user.getId(), measure, pkId));
	}

	/**
	 * 量具返库
	 */
	@ApiOperation(value = "量具返库", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "量具返库成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/measure-return")
	@OperateLog(info = "量具返库[库管员%s->:量具条码%s]", params = { "keeper", "measureBarcode" })
	public AjaxReturn measureReturn(
			@ApiParam(name = "pkId", value = "pkId", required = false) @RequestParam(required = false, defaultValue = "") Long pkId,
			@ApiParam(name = "measureBarcode", value = "measureBarcode", required = false) @RequestParam(required = false, defaultValue = "") String measureBarcode,
			@ApiParam(name = "warehouse", value = "库位", required = false) @RequestParam(required = false, defaultValue = "") String warehouse,
			@ApiParam(name = "keeperId", value = "库管员Id", required = false) @RequestParam(required = false, defaultValue = "") Long keeperId,
			@ApiParam(name = "keeper", value = "库管员", required = false) @RequestParam(required = false, defaultValue = "") String keeper,
			@ApiParam(name = "remark", value = "说明", required = false) @RequestParam(required = false, defaultValue = "") String remark)
			throws BusinessException {
		log.debug("MeasuerController.toolAdd<<<");
		// 获取当前用户
		CustomUser user = getCompleteAuthentication();
		Date date = new Date();
		// 封装参数信息
		Measure measure = new Measure();
		measure.setWarehouse(warehouse);
		measure.setKeeperId(keeperId);
		measure.setKeeper(keeper);
		measure.setMeasureStatus(5);

		Measure measures = measureService.selectById(user.getId(), pkId);
		MeasureWarehouse measureWarehouse = new MeasureWarehouse();

		measureWarehouse.setMeasureId(pkId);
		measureWarehouse.setMeasureBarcode(measures.getMeasureBarcode());
		measureWarehouse.setMeasureNumber(measures.getMeasureNumber());
		measureWarehouse.setMeasureName(measures.getMeasureName());
		measureWarehouse.setModel(measures.getModel());
		measureWarehouse.setKeeperId(keeperId);
		measureWarehouse.setKeeper(keeper);
		measureWarehouse.setWarehouse(warehouse);
		measureWarehouse.setWarehosingTime(date);
		measureWarehouse.setInType(2);
		measureWarehouseService.insert(user.getId(), measureWarehouse);

		MeasureOutbound measureOutbound = new MeasureOutbound();
		measureOutbound.setMeasureId(pkId);
		measureOutbound.setMeasureNumber(measures.getMeasureNumber());
		measureOutbound.setMeasureName(measures.getMeasureName());
		measureOutbound.setModel(measures.getModel());
		measureOutbound.setWarehouse(warehouse);
		measureOutbound.setOutType(2);

		Staff staff = staffService.selectByUserId(user.getId(), user.getId());
		measureOutbound.setDepartmentId(user.getDepartmentId());
		measureOutbound.setDepartmentName(staff.getDepartmentName());
		measureOutbound.setReceiveClass(staff.getTeamName());
		measureOutbound.setReceiver(staff.getStaffName());
		measureOutbound.setKeeperId(keeperId);
		measureOutbound.setKeeper(keeper);
		measureOutbound.setRemark(remark);
		measureOutBoundService.insert(user.getId(), measureOutbound);

		MeasureCheck measureCheck = new MeasureCheck();

		String seq = String.format("%0" + 5 + "d", measures.getMeasureSeq());
		measureCheck.setFullNumber(measures.getMeasureBarcode());
		measureCheck.setMeasureSeq(seq);
		measureCheck.setMeasureNumber(measures.getMeasureNumber());
		measureCheck.setMeasureName(measures.getMeasureName());
		measureCheck.setModel(measures.getModel());
		measureCheck.setSupplierId(measures.getSupplierId());
		measureCheck.setSupplierName(measures.getSupplierName());
		measureCheck.setDeliveryer(keeper);
		measureCheck.setDeliveryTime(date);
		measureCheck.setCheckType(2);
		measureCheckService.insert(user.getId(), measureCheck);

		return new AjaxReturn(200, null, measureService.updateActiveById(user.getId(), measure, pkId));
	}

	/**
	 * 量具报废
	 */
	@ApiOperation(value = "量具报废", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "量具删除成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/measure-delete-by-id")
	@OperateLog(info = "量具报废[量具条码%s]", params = { "measureBarcode" })
	public AjaxReturn measureDeleteById(
			@ApiParam(name = "pkId", value = "主键", required = true) @RequestParam(required = true) Long pkId,
			@ApiParam(name = "measureBarcode", value = "measureBarcode", required = false) @RequestParam(required = false) String measureBarcode)
			throws BusinessException {

		// 获取当前用户
		Long userId = getAuthentication();
		// 参数校验

		Validate.isTrue(pkId > 0);
		Measure measure = new Measure();
		measure.setMeasureStatus(8);

		return new AjaxReturn(200, null, measureService.updateActiveById(userId, measure, pkId));
	}

	/**
	 * 量具封存
	 */
	@ApiOperation(value = "量具封存", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "量具删除成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/measure-seal-by-id")
	@OperateLog(info = "量具封存[量具条码%s]", params = { "measureBarcode" })
	public AjaxReturn measureSealById(
			@ApiParam(name = "pkId", value = "主键", required = true) @RequestParam(required = true) Long pkId,
			@ApiParam(name = "measureStatus", value = "量具状态", required = true) @RequestParam(required = true) Integer measureStatus,
			@ApiParam(name = "measureBarcode", value = "量具条码", required = false) @RequestParam(required = false) String measureBarcode)
			throws BusinessException {

		// 获取当前用户
		Long userId = getAuthentication();
		// 参数校验

		Validate.isTrue(pkId > 0);
		Measure measure = new Measure();
		Date date = new Date();
		measure.setMeasureStatus(measureStatus);
		if (measureStatus == 6) {
			measure.setSealMark(1);
			measure.setSealTime(date);
		} else if (measureStatus == 2) {
			measure.setSealMark(0);
			measure.setEnableTime(date);
		}
		return new AjaxReturn(200, null, measureService.updateActiveById(userId, measure, pkId));
	}

	/**
	 * 量具丢失找回
	 */
	@ApiOperation(value = "量具丢失找回", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "量具丢失找回成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/measure-lost-find")
	@OperateLog(info = "量具丢失找回[量具条码%s]", params = { "measureBarcode" })
	public AjaxReturn measureLostFind(
			@ApiParam(name = "pkId", value = "主键", required = true) @RequestParam(required = true) Long pkId,
			@ApiParam(name = "measureStatus", value = "量具状态", required = true) @RequestParam(required = true) Integer measureStatus,
			@ApiParam(name = "measureBarcode", value = "量具条码", required = false) @RequestParam(required = false) String measureBarcode)
			throws BusinessException {

		// 获取当前用户
		Long userId = getAuthentication();
		// 参数校验

		Validate.isTrue(pkId > 0);
		Measure measure = new Measure();
		measure.setMeasureStatus(measureStatus);

		return new AjaxReturn(200, null, measureService.updateActiveById(userId, measure, pkId));
	}

	/**
	 * 根据主键查找
	 */
	@ApiOperation(value = "量具id查询列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "量具Id查询列表查询成功", response = Measure.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/measure-get-by-id")
	public AjaxReturn measureGetById(
			@ApiParam(name = "pkId", value = "pkId", required = true) @RequestParam(required = true) Long pkId)
			throws BusinessException {

		// 获取当前用户
		CustomUser user = getCompleteAuthentication();
		// 参数校验

		Validate.isTrue(pkId >= 1);

		return new AjaxReturn(200, null, measureService.selectById(user.getId(), pkId));
	}

	/**
	 * 根据物料条码查找
	 */
	@ApiOperation(value = "物料条码查询列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "物料条码查询列表查询成功", response = Measure.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/measure-get-by-fullNumber")
	public AjaxReturn measureGetByfullNumber(
			@ApiParam(name = "fullNumber", value = "物料编码", required = true) @RequestParam(required = true) String fullNumber)
			throws BusinessException {

		// 获取当前用户
		CustomUser user = getCompleteAuthentication();
		// 参数校验

		return new AjaxReturn(200, null, measureService.selectByFullNumber(user.getId(), fullNumber));
	}

	/**
	 * 查询量具信息分页列表
	 */
	@ApiOperation(value = "查询量具信息分页列表  ", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "查询量具信息分页列表 查询成功", response = Measure.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/measure-page-list")
	public AjaxReturn measurePageList(
			@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page,
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows,
			@ApiParam(name = "beginDate", value = "开始日期", required = false) @RequestParam(required = false, defaultValue = "") Date beginDate,
			@ApiParam(name = "endDate", value = "结束日期", required = false) @RequestParam(required = false, defaultValue = "") Date endDate,
			
			@ApiParam(name = "measureStatus", value = "量具状态", required = false) @RequestParam(required = false, defaultValue = "") Integer measureStatus,
			@ApiParam(name = "measureNumber", value = "量具编码", required = false) @RequestParam(required = false) String measureNumber,			
			@ApiParam(name = "measureBarcode", value = "量具条码", required = false) @RequestParam(required = false) String measureBarcode,
			@ApiParam(name = "factoryMetrology", value = "本厂计量编码", required = false) @RequestParam(required = false) String factoryMetrology,
			@ApiParam(name = "measureName", value = "量具名称", required = false) @RequestParam(required = false) String measureName,
			@ApiParam(name = "staffCode", value = "员工编号", required = false) @RequestParam(required = false) String staffCode,
			@ApiParam(name = "departmentId", value = "使用部门id", required = false) @RequestParam(required = false) Long departmentId,
			@ApiParam(name = "sort", value = "排序", required = false) @RequestParam(required = false) String sort,
			@ApiParam(name = "isList", value = "是否为台账", required = false) @RequestParam(required = false) Integer isList)
			throws BusinessException {

		// 获取当前用户
		Long curuserId = getAuthentication();
		// 参数校验
		Validate.isTrue(page >= 1);
		Validate.isTrue(rows <= 100);
		// 封装查询条件
		QueryDto queryDto = new QueryDto();
		queryDto.setPage(page);
		queryDto.setRows(rows);
		queryDto.setBeginDate(beginDate);
		queryDto.setEndDate(endDate);
		queryDto.setSort(sort);
		// 封装参数
		Measure measure = new Measure();
		measure.setMeasureStatus(measureStatus);
		measure.setMeasureBarcode(measureBarcode);
		measure.setMeasureName(measureName);
		measure.setUseDepartmentId(departmentId);
		measure.setFactoryMetrology(factoryMetrology);
		measure.setIsList(isList);
		measure.setUserCode(staffCode);
		Pagination<Measure> pagination = measureService.selectPageList(curuserId, measure, queryDto);
		return pagination;
	}
	
	/**
	 * 量具信息表导出
	 * 
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "量具信息表导出", notes = "", code = 200, produces = "application/json")
	@ApiResponses({ @ApiResponse(code = 200, message = "量具信息表导出", response = Measure.class) })
	@Secure()
	@GetMapping(path = { "/measure-export" })	
	public ResponseEntity<byte[]> downloadMeasureExcel(HttpServletResponse response,
			@ApiParam(name = "beginDate", value = "开始日期", required = false) @RequestParam(required = false) Date beginDate,
			@ApiParam(name = "endDate", value = "结束日期", required = false) @RequestParam(required = false) Date endDate,
			
			@ApiParam(name = "measureStatus", value = "量具状态", required = false) @RequestParam(required = false, defaultValue = "") Integer measureStatus,
			@ApiParam(name = "measureNumber", value = "量具编码", required = false) @RequestParam(required = false) String measureNumber,			
			@ApiParam(name = "measureBarcode", value = "量具条码", required = false) @RequestParam(required = false) String measureBarcode,
			@ApiParam(name = "factoryMetrology", value = "本厂计量编码", required = false) @RequestParam(required = false) String factoryMetrology,
			@ApiParam(name = "measureName", value = "量具名称", required = false) @RequestParam(required = false) String measureName,
			@ApiParam(name = "staffCode", value = "员工编号", required = false) @RequestParam(required = false) String staffCode,
			@ApiParam(name = "departmentId", value = "使用部门id", required = false) @RequestParam(required = false) Long departmentId)
			throws Exception {
		Long userId = getAuthentication();

		QueryDto queryDto = new QueryDto();
		queryDto.setPage(1);
		queryDto.setRows(50000);
		queryDto.setBeginDate(beginDate);
		queryDto.setEndDate(endDate);
	
		// 封装参数
		Measure measure = new Measure();
		measure.setMeasureStatus(measureStatus);
		measure.setMeasureBarcode(measureBarcode);
		measure.setMeasureName(measureName);
		measure.setUseDepartmentId(departmentId);
		measure.setFactoryMetrology(factoryMetrology);
		measure.setIsList(1);
		measure.setUserCode(staffCode);
		Pagination<Measure> pagination = measureService.selectPageList(userId, measure, queryDto);

		pagination.getRows().stream().forEach(item -> {
			if (item.getMeasureStatus() == 1) {
				item.setMeasureStatusName("待入库");
			} else if (item.getMeasureStatus() == 2) {
				item.setMeasureStatusName("在库");
			} else if (item.getMeasureStatus() == 3) {
				item.setMeasureStatusName("完善定检周期");
			} else if (item.getMeasureStatus() == 4) {
				item.setMeasureStatusName("使用");
			} else if (item.getMeasureStatus() == 5) {
				item.setMeasureStatusName("返库待检");
			} else if (item.getMeasureStatus() == 6) {
				item.setMeasureStatusName("封存");
			} else if (item.getMeasureStatus() == 7) {
				item.setMeasureStatusName("丢失");
			} else if (item.getMeasureStatus() == 8) {
				item.setMeasureStatusName("报废");
			}
			if (item.getSealMark() == 0 || item.getSealMark() == null) {
				item.setSealMarkName("启用");
			} else if (item.getSealMark() == 1) {
				item.setSealMarkName("封存");
			}
			if (null != item.getCheckTime()) {
				item.setCheckTimeString(DateFormatUtils.format(item.getCheckTime(), "yyyy-MM-dd HH:mm:ss"));
			} else {
				item.setCheckTimeString("");
			}
			if (null != item.getNextCheckTime()) {
				item.setNextCheckTimeString(DateFormatUtils.format(item.getNextCheckTime(), "yyyy-MM-dd HH:mm:ss"));
			}
		});
		byte[] data = ExcelUtils.getInstance().exportObjects2ExcelByteArray((List<Measure>) pagination.getRows(), Measure.class, true, null,true);
		return getResponseEntity(data, "量具台账.xlsx");
	}
	

	/**
	 * 量具更新
	 */
	@ApiOperation(value = "量具更新", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "量具入库成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/measure-update")
	@OperateLog(info = "量具信息修改[量具条码:%s]", params = { "measureBarcode" })
	public AjaxReturn measureUpdate(
			@ApiParam(name = "pkId", value = "主键", required = true) @RequestParam(required = true) Long pkId,
			@ApiParam(name = "measureBarcode", value = "量具条码", required = true) @RequestParam(required = true) String measureBarcode,
			@ApiParam(name = "measureNumber", value = "量具编码", required = false) @RequestParam(required = false, defaultValue = "") String measureNumber,
			@ApiParam(name = "measureName", value = "名称规格", required = false) @RequestParam(required = false) String measureName,
			@ApiParam(name = "model", value = "规格型号", required = false) @RequestParam(required = false) String model,
			@ApiParam(name = "applyDepartmentId", value = "申请部门ID", required = false) @RequestParam(required = false) Long applyDepartmentId,
			@ApiParam(name = "applyDepartmentName", value = "申请部门", required = false) @RequestParam(required = false, defaultValue = "") String applyDepartmentName,
			@ApiParam(name = "applierId", value = "申请人ID", required = false) @RequestParam(required = false, defaultValue = "") Long applierId,
			@ApiParam(name = "applierName", value = "申请人", required = false) @RequestParam(required = false, defaultValue = "") String applierName,
			@ApiParam(name = "useDepartmentId", value = "使用部门ID", required = false) @RequestParam(required = false, defaultValue = "") Long useDepartmentId,
			@ApiParam(name = "useDepartmentName", value = "使用部门名称", required = false) @RequestParam(required = false, defaultValue = "") String useDepartmentName,
			@ApiParam(name = "useTeamId", value = "使用班组ID", required = false) @RequestParam(required = false) Long useTeamId,
			@ApiParam(name = "useTeamName", value = "使用班组名称", required = false) @RequestParam(required = false, defaultValue = "") String useTeamName,
			@ApiParam(name = "userCode", value = "使用人ID", required = false) @RequestParam(required = false, defaultValue = "") String userCode,
			@ApiParam(name = "userName", value = "使用人名称", required = false) @RequestParam(required = false, defaultValue = "") String userName,
			@ApiParam(name = "supplierId", value = "供应商ID", required = false) @RequestParam(required = false, defaultValue = "") Long supplierId,
			@ApiParam(name = "supplierName", value = "供应商名称", required = false) @RequestParam(required = false, defaultValue = "") String supplierName,
			@ApiParam(name = "manufacturer", value = "制造商", required = false) @RequestParam(required = false, defaultValue = "") String manufacturer,
			@ApiParam(name = "manufacturingNumber", value = "出厂编号", required = false) @RequestParam(required = false, defaultValue = "") String manufacturingNumber,
			@ApiParam(name = "storageTime", value = "入库时间", required = false) @RequestParam(required = false, defaultValue = "") Date storageTime,
			@ApiParam(name = "measureStatus", value = "量具状态", required = false) @RequestParam(required = false, defaultValue = "") Integer measureStatus)
			throws BusinessException {
		// 获取当前用户
		CustomUser user = getCompleteAuthentication();

		// 封装参数信息
		Measure measure = new Measure();
		measure.setMeasureBarcode(measureBarcode);
		measure.setMeasureNumber(measureNumber);
		measure.setMeasureName(measureName);
		measure.setModel(model);
		measure.setApplyDepartmentId(applyDepartmentId);
		measure.setApplyDepartmentName(applyDepartmentName);
		measure.setApplierId(applierId);
		measure.setApplierName(applierName);
		measure.setUseDepartmentId(useDepartmentId);
		measure.setUseDepartmentName(useDepartmentName);
		measure.setUseTeamId(useTeamId);
		measure.setUseTeamName(useTeamName);
		measure.setUserCode(userCode);
		measure.setUserName(userName);
		measure.setSupplierId(supplierId);
		measure.setSupplierName(supplierName);
		measure.setManufacturer(manufacturer);
		measure.setManufacturingNumber(manufacturingNumber);
		measure.setStorageTime(storageTime);
		measure.setMeasureStatus(measureStatus);

		return new AjaxReturn(200, null, measureService.updateActiveById(user.getId(), measure, pkId));
	}

	/**
	 * 查询刀具全生命周期分页列表
	 */
	@ApiOperation(value = "查询刀具全生命周期分页列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具全生命周期分页列表查询成功", response = Tool.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/measure-life-page-list")
	public AjaxReturn measureLifePageList(
			@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, //
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows,
			@ApiParam(name = "fullNumber", value = "刀具码", required = false) @RequestParam(required = false) String fullNumber,
			@ApiParam(name = "beginDate", value = "开始日期", required = false) @RequestParam(required = false) Date beginDate,
			@ApiParam(name = "endDate", value = "结束日期", required = false) @RequestParam(required = false) Date endDate,
			@ApiParam(name = "staffCode", value = "员工姓名", required = false) @RequestParam(required = false) String staffCode,
			@ApiParam(name = "measureNumber", value = "物料编码", required = false) @RequestParam(required = false) String measureNumber)
			throws BusinessException {
		log.debug("MeasuerController.measureLifePageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("page:" + page);
			log.debug("rows:" + rows);
			log.debug("fullNumber:" + fullNumber);
			log.debug("measureNumber:" + measureNumber);
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

		Measure measure = new Measure();
		measure.setMeasureBarcode(fullNumber);
		measure.setMeasureNumber(measureNumber);
		measure.setUserCode(staffCode);
		Pagination<Measure> pagination = measureService.selectLifePageList(curuserId, measure, queryDto);
		log.debug("MeasuerController.measureLifePageList>>>");
		return pagination;
	}

	

	

	/**
	 * 量具台账导入
	 * 
	 * @param request
	 * @return
	 * @throws BusinessException
	 */
	@ApiOperation(value = "量具台账信息导入", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "量具台账信息导入", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/measure-import")
	@OperateLog(info = "量具信息导入")
	// @Secure()
	public AjaxReturn measureBaseImport(@RequestParam(value = "file", required = true) MultipartFile file)
			throws Exception {
		Long userId = getAuthentication();
		byte[] data = file.getBytes();
		InputStream inputStream = new ByteArrayInputStream(data);
		List<Measure> measureBaseList = ExcelUtils.getInstance().readExcel2Objects(inputStream, Measure.class, 1, 10000,
				0);
		inputStream.close();
		measureService.measureImport(userId, measureBaseList);
		return new AjaxReturn(200, null, 1);
	}

	/**
	 * 查询量具信息分页列表
	 */
	@ApiOperation(value = "查询量具信息分页列表  ", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "查询量具信息分页列表 查询成功", response = Measure.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/measure-scrip-page-list")
	public AjaxReturn measurePageList(
			@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page,
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows,
			@ApiParam(name = "beginDate", value = "开始日期", required = false) @RequestParam(required = false, defaultValue = "") Date beginDate,
			@ApiParam(name = "endDate", value = "结束日期", required = false) @RequestParam(required = false, defaultValue = "") Date endDate,
			@ApiParam(name = "measureNumber", value = "量具编码", required = false) @RequestParam(required = false, defaultValue = "") String measureNumber)
			throws BusinessException {

		// 获取当前用户
		Long curuserId = getAuthentication();
		// 参数校验
		Validate.isTrue(page >= 1);
		Validate.isTrue(rows <= 100);
		// 封装查询条件
		QueryDto queryDto = new QueryDto();
		queryDto.setPage(page);
		queryDto.setRows(rows);
		queryDto.setBeginDate(beginDate);
		queryDto.setEndDate(endDate);
		// 封装参数
		Measure measure = new Measure();
		measure.setMeasureNumber(measureNumber);

		Pagination<Measure> pagination = measureService.selectScripPageList(curuserId, measure, queryDto);
		return pagination;
	}

	@ApiOperation(value = "夹具生命周期导出", notes = "", code = 200, produces = "application/json")
	@ApiResponses({ @ApiResponse(code = 200, message = "夹具生命周期导出", response = Measure.class) })
	// @Secure()
	@GetMapping(path = { "/measure-life-export" })
	@OperateLog(info = "量具生命周期信息导出")
	public String measureLifeExport(HttpServletResponse response,
			@ApiParam(name = "fullNumber", value = "量具条码", required = false) @RequestParam(required = false, defaultValue = "") String fullNumber,
			@ApiParam(name = "measureNumber", value = "量具编码", required = false) @RequestParam(required = false, defaultValue = "") String measureNumber,
			@ApiParam(name = "beginDate", value = "开始日期", required = false) @RequestParam(required = false, defaultValue = "") Date beginDate,
			@ApiParam(name = "endDate", value = "结束日期", required = false) @RequestParam(required = false, defaultValue = "") Date endDate)
			throws Exception {

		QueryDto queryDto = new QueryDto();
		queryDto.setPage(1);
		queryDto.setRows(100000);
		queryDto.setBeginDate(beginDate);
		queryDto.setEndDate(endDate);

		Measure measure = new Measure();
		measure.setMeasureBarcode(fullNumber);
		measure.setMeasureNumber(measureNumber);

		Pagination<Measure> pagination = measureService.selectLifePageList(22l, measure, queryDto);
		// 创建HSSFWorkbook对象(excel的文档对象)
		HSSFWorkbook wb = new HSSFWorkbook();
		// 建立新的sheet对象（excel的表单）
		HSSFSheet sheet = wb.createSheet("夹具生命周期");
		// 在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
		HSSFRow row1 = sheet.createRow(0);
		row1.setHeight((short) (25 * 20));//
		// 创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个

		HSSFCellStyle cellStyle = wb.createCellStyle();
		// 设置单元格内容
		row1.createCell(0).setCellValue("物料条码");
		row1.createCell(1).setCellValue("物料名称");
		row1.createCell(2).setCellValue("型号规格");
		row1.createCell(3).setCellValue("物料编码");
		row1.createCell(4).setCellValue("量具状态");
		row1.createCell(5).setCellValue("封存状态");
		row1.createCell(6).setCellValue("责任部门");
		row1.createCell(7).setCellValue("责任班组");
		row1.createCell(8).setCellValue("责任人");
		row1.createCell(9).setCellValue("厂家计量编号");
		row1.createCell(10).setCellValue("本厂计量编号");
		row1.createCell(11).setCellValue("检定记录");
		row1.createCell(14).setCellValue("入库记录");
		row1.createCell(16).setCellValue("出库记录");

		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

		for (int i = 0; i <= 10; i++) {
			row1.getCell(i).setCellStyle(cellStyle);
		}
		row1.getCell(14).setCellStyle(cellStyle);
		row1.getCell(16).setCellStyle(cellStyle);

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
		sheet.setColumnWidth(16, 6000);
		sheet.setColumnWidth(17, 6000);
		sheet.setColumnWidth(18, 6000);
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

		sheet.addMergedRegion(new CellRangeAddress(0, 0, 11, 13));
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 14, 15));
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 16, 18));
		// 在sheet里创建第二行
		HSSFRow row2 = sheet.createRow(1);
		// 创建单元格并设置单元格内容
		row2.createCell(11).setCellValue("检定时间");
		row2.createCell(12).setCellValue("检定类型");
		row2.createCell(13).setCellValue("定检人");

		row2.createCell(14).setCellValue("入库时间");
		row2.createCell(15).setCellValue("库管员");

		row2.createCell(16).setCellValue("出库时间");
		row2.createCell(17).setCellValue("领用人");
		row2.createCell(18).setCellValue("库管员");
		row2.setHeight((short) (25 * 20));//
		for (int i = 11; i < 18; i++) {
			row2.getCell(i).setCellStyle(cellStyle);
		}
		int i = 2;
		int startRow;

		HSSFCellStyle cellStyle1 = wb.createCellStyle();
		cellStyle1.setVerticalAlignment(VerticalAlignment.CENTER);

		for (Measure item : pagination.getRows()) {
			int cCount = 0, wCount = 0, oCount = 0, maxCount = 0;
			HSSFRow dataRow = sheet.createRow(i);
			dataRow.setHeight((short) (25 * 20));//
			startRow = i;

			List<MeasureCheck> checkList = item.getCheckList();
			if (!CollectionUtils.isEmpty(checkList)) {
				cCount = checkList.size();
			}
			List<MeasureWarehouse> warehouseList = item.getWarehouseList();
			if (!CollectionUtils.isEmpty(warehouseList)) {
				wCount = warehouseList.size();
			}
			List<MeasureOutbound> outboundList = item.getOutboundList();
			if (!CollectionUtils.isEmpty(outboundList)) {
				oCount = outboundList.size();
			}
			maxCount = cCount > wCount ? cCount : wCount;
			maxCount = maxCount > oCount ? maxCount : oCount;

			dataRow.createCell(0).setCellValue(item.getMeasureBarcode());

			dataRow.createCell(1).setCellValue(item.getMeasureName());
			dataRow.createCell(2).setCellValue(item.getModel());

			dataRow.createCell(3).setCellValue(item.getMeasureNumber());
			dataRow.createCell(4).setCellValue(MeasureStatusEnum.getName(item.getMeasureStatus()));
			dataRow.createCell(5).setCellValue(SealTypeEnum.getName(item.getSealMark()));

			dataRow.createCell(6).setCellValue(item.getUseDepartmentName());
			dataRow.createCell(7).setCellValue(item.getUseTeamName());
			dataRow.createCell(8).setCellValue(item.getUserName());
			dataRow.createCell(9).setCellValue(item.getManufacturingNumber());
			dataRow.createCell(10).setCellValue(item.getFactoryMetrology());

			if (maxCount == 0) {
				i++;
				continue;
			}

			for (int l = 0; l < 11; l++) {
				dataRow.getCell(l).setCellStyle(cellStyle1);
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
					MeasureCheck check = checkList.get(j);
					dataRow.createCell(11).setCellValue(null == check.getCheckTime() ? ""
							: DateFormatUtils.format(check.getCheckTime(), "yyyy-MM-dd HH:mm:ss"));
					dataRow.createCell(12).setCellValue(MeasureCheckTypeEnum.getName(check.getCheckType()));
					dataRow.createCell(13).setCellValue(check.getChecker());
				}
				if (wCount <= j) {
					dataRow.createCell(14).setCellValue("");
					dataRow.createCell(15).setCellValue("");
				} else {
					MeasureWarehouse warehouse = warehouseList.get(j);
					dataRow.createCell(14).setCellValue(null == warehouse.getWarehosingTime() ? ""
							: DateFormatUtils.format(warehouse.getWarehosingTime(), "yyyy-MM-dd HH:mm:ss"));
					dataRow.createCell(15).setCellValue(warehouse.getKeeper());
				}
				if (oCount <= j) {
					dataRow.createCell(16).setCellValue("");
					dataRow.createCell(17).setCellValue("");
					dataRow.createCell(18).setCellValue("");
				} else {
					MeasureOutbound outbound = outboundList.get(j);
					dataRow.createCell(16).setCellValue(null == outbound.getCreateTime() ? ""
							: DateFormatUtils.format(outbound.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
					dataRow.createCell(17).setCellValue(outbound.getDepartmentName() + '-' + outbound.getReceiveClass()
							+ '-' + outbound.getReceiver());
					dataRow.createCell(18).setCellValue(outbound.getKeeper());
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
		OutputStream output = response.getOutputStream();
		response.reset();
		response.setHeader("Content-disposition", "attachment; filename=measure.xls");
		response.setContentType("application/msexcel");
		wb.write(output);
		output.close();
		wb.close();
		return null;
	}

}
