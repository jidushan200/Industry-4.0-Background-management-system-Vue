package org.tsinghuatj.web.controller.measure;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.tsinghuatj.framework.web.controller.BaseController;
import org.tsinghuatj.framework.web.support.annotation.Secure;
import org.tsinghuatj.measure.domain.Measure;
import org.tsinghuatj.measure.domain.MeasureBase;
import org.tsinghuatj.measure.domain.MeasureCheck;
import org.tsinghuatj.measure.service.IMeasureCheckService;
import org.tsinghuatj.measure.service.IMeasureService;
import org.tsinghuatj.sys.service.ISysMessageService;
import org.tsinghuatj.tool.domain.Tool;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping({ "/measure" })
public class MeasureCheckController extends BaseController {

	private @Autowired(required = false) IMeasureCheckService measureCheckService;
	private @Resource ISysMessageService sysMessageService;
	private @Autowired(required = false) IMeasureService measureService;

	/**
	 * 量具基础信息分页列表
	 */
	@ApiOperation(value = "量具基础信息分页列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "量具基础信息分页列表查询成功", response = MeasureBase.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/measure-check-page-list")
	public AjaxReturn measureCheckPageList(@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, //
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows, @ApiParam(name = "beginDate", value = "开始日期", required = false) @RequestParam(required = false, defaultValue = "") Date beginDate, @ApiParam(name = "endDate", value = "结束日期", required = false) @RequestParam(required = false, defaultValue = "") Date endDate, @ApiParam(name = "measureNumber", value = "量具编码", required = false) @RequestParam(required = false) String measureNumber, @ApiParam(name = "departmentId", value = "使用部门ID", required = false) @RequestParam(required = false) Long departmentId,
			@ApiParam(name = "checkType", value = "定检类型", required = false) @RequestParam(required = false) Integer checkType, @ApiParam(name = "checkResult", value = "质检结果", required = false) @RequestParam(required = false) Integer checkResult) throws BusinessException {
		log.debug("MeasureCheckController.measureCheckPageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("page:" + page);
			log.debug("rows:" + rows);
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

		MeasureCheck measureCheck = new MeasureCheck();
		measureCheck.setMeasureNumber(measureNumber);
		measureCheck.setCheckResult(checkResult);
		measureCheck.setCheckType(checkType);
		measureCheck.setUseDepartmentId(departmentId);
		Pagination<MeasureCheck> pagination = measureCheckService.selectPageList(curuserId, measureCheck, queryDto);
		log.debug("MeasureCheckController.measureCheckPageList>>>");
		return pagination;
	}

	@ApiOperation(value = "刀具台账导出", notes = "", code = 200, produces = "application/json")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具台账导出", response = Tool.class) })
	@Secure()
	@GetMapping(path = { "/measure-check-export" })
	public String downloadMeasureCheckExcel(HttpServletRequest request, HttpServletResponse response, @ApiParam(name = "measureNumber", value = "量具编码", required = false) @RequestParam(required = false) String measureNumber, @ApiParam(name = "departmentId", value = "使用部门ID", required = false) @RequestParam(required = false) Long departmentId

	) throws Exception {
		Long userId = getAuthentication();
		QueryDto queryDto = new QueryDto();
		queryDto.setPage(1);
		queryDto.setRows(50000);

		MeasureCheck measureCheck = new MeasureCheck();
		measureCheck.setMeasureNumber(measureNumber);
		measureCheck.setUseDepartmentId(departmentId);
		measureCheck.setCheckResult(3);
		Pagination<MeasureCheck> pagination = measureCheckService.selectPageList(userId, measureCheck, queryDto);
		// 创建HSSFWorkbook对象(excel的文档对象)
		HSSFWorkbook wb = new HSSFWorkbook();
		// 建立新的sheet对象（excel的表单）
		HSSFSheet sheet = wb.createSheet("量具质检记录");
		// 在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
		HSSFRow row1 = sheet.createRow(0);
		row1.setHeight((short) (25 * 20));//
		// 创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个

		HSSFCellStyle cellStyle = wb.createCellStyle();
		// 设置单元格内容

		row1.createCell(0).setCellValue("物料编码");
		row1.createCell(1).setCellValue("物料名称");
		row1.createCell(2).setCellValue("物料图号");
		row1.createCell(3).setCellValue("顺序号");
		row1.createCell(4).setCellValue("本厂计量编码");
		row1.createCell(5).setCellValue("领用部门");
		row1.createCell(6).setCellValue("领用班组");
		row1.createCell(7).setCellValue("送检时间");
		row1.createCell(8).setCellValue("供应商名称");
		row1.createCell(9).setCellValue("质检类型");
		row1.createCell(10).setCellValue("质检结果");
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

		for (int i = 0; i <= 10; i++) {
			row1.getCell(i).setCellStyle(cellStyle);
		}

		row1.setHeight((short) 400);// 目的是想把行高设置成25px
		sheet.setColumnWidth(0, 4000);
		sheet.setColumnWidth(1, 5000);
		sheet.setColumnWidth(2, 5000);
		sheet.setColumnWidth(3, 7000);
		sheet.setColumnWidth(4, 4000);
		sheet.setColumnWidth(5, 4000);
		sheet.setColumnWidth(6, 4000);
		sheet.setColumnWidth(7, 4000);
		sheet.setColumnWidth(8, 4000);
		sheet.setColumnWidth(9, 4000);
		sheet.setColumnWidth(10, 4000);
		int i = 1;

		HSSFCellStyle cellStyle1 = wb.createCellStyle();
		cellStyle1.setVerticalAlignment(VerticalAlignment.CENTER);

		for (MeasureCheck item : pagination.getRows()) {
			HSSFRow dataRow = sheet.createRow(i);
			dataRow.setHeight((short) (25 * 20));//
			dataRow.createCell(0).setCellValue(item.getMeasureNumber());
			dataRow.createCell(1).setCellValue(item.getMeasureName());
			dataRow.createCell(2).setCellValue(item.getModel());
			dataRow.createCell(3).setCellValue(item.getMeasureSeq());
			dataRow.createCell(4).setCellValue(item.getFactoryMetrology());
			dataRow.createCell(5).setCellValue(item.getUseDepartmentName());
			dataRow.createCell(6).setCellValue(item.getUseTeamName());
			dataRow.createCell(7).setCellValue(DateFormatUtils.format(item.getDeliveryTime(), "yyyy-MM-dd HH:mm:ss"));
			dataRow.createCell(8).setCellValue(item.getSupplierName());
			dataRow.createCell(9).setCellValue(item.getCheckType() == 1 ? "新量具质检" : "周期定检");
			dataRow.createCell(10).setCellValue(item.getCheckResult() == 1 ? "合格" : "不合格");
			i++;
		}
		// 输出Excel文件
		String fileName = "量具质检记录.xls";
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

	/**
	 * 量具基础信息分页列表
	 */
	@ApiOperation(value = "量具基础信息分页列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "量具基础信息分页列表查询成功", response = MeasureBase.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/check-recode-page-list")
	public AjaxReturn measureCheckRecodePageList(@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, //
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows, @ApiParam(name = "measureNumber", value = "量具编码", required = false) @RequestParam(required = false) String measureNumber, @ApiParam(name = "measureName", value = "量具名称", required = false) @RequestParam(required = false) String measureName, @ApiParam(name = "departmentId", value = "使用部门ID", required = false) @RequestParam(required = false) Long departmentId) throws BusinessException {
		log.debug("MeasureCheckController.measureCheckRecodePageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("page:" + page);
			log.debug("rows:" + rows);
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
		MeasureCheck measureCheck = new MeasureCheck();
		measureCheck.setMeasureNumber(measureNumber);
		measureCheck.setMeasureName(measureName);
		measureCheck.setUseDepartmentId(departmentId);
		measureCheck.setCheckResult(3);
		
		measureCheck.setUseDepartmentId(departmentId);
		Pagination<MeasureCheck> pagination = measureCheckService.selectPageList(curuserId, measureCheck, queryDto);
		log.debug("MeasureCheckController.measureCheckRecodePageList>>>");
		return pagination;
	}

	/**
	 * 量具基础信息分页列表
	 */
	@ApiOperation(value = "量具基础信息分页列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "量具基础信息分页列表查询成功", response = MeasureBase.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/new-measure-check-page-list")
	public AjaxReturn newMeasureCheckPageList(@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, //
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows, @ApiParam(name = "beginDate", value = "开始日期", required = false) @RequestParam(required = false, defaultValue = "") Date beginDate, @ApiParam(name = "endDate", value = "结束日期", required = false) @RequestParam(required = false, defaultValue = "") Date endDate, @ApiParam(name = "measureNumber", value = "量具编码", required = false) @RequestParam(required = false) String measureNumber, @ApiParam(name = "checkResult", value = "质检结果", required = false) @RequestParam(required = false) Integer checkResult) throws BusinessException {
		log.debug("MeasureCheckController.measureCheckPageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("page:" + page);
			log.debug("rows:" + rows);
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

		MeasureCheck measureCheck = new MeasureCheck();
		measureCheck.setMeasureNumber(measureNumber);
		measureCheck.setCheckResult(checkResult);
		Pagination<MeasureCheck> pagination = measureCheckService.selectNewPageList(curuserId, measureCheck, queryDto);
		log.debug("MeasureCheckController.measureCheckPageList>>>");
		return pagination;
	}

	/**
	 * 量具基础信息分页列表
	 */
	@ApiOperation(value = "量具基础信息分页列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "量具基础信息分页列表查询成功", response = MeasureBase.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/measure-check-qualified")
	public AjaxReturn measureCheckQualified(@ApiParam(name = "pkId", value = "pkId", required = false) @RequestParam(required = false) Long pkId, @ApiParam(name = "checkType", value = "定检类型", required = false) @RequestParam(required = false) Integer checkType, @ApiParam(name = "isRepair", value = "定检类型", required = false) @RequestParam(required = false) Integer isRepair, @ApiParam(name = "measureNumber", value = "量具编码", required = false) @RequestParam(required = false) String measureNumber) throws BusinessException {
		log.debug("MeasureCheckController.measureCheckPageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("measureNumber:" + measureNumber);
		}

		// 获取当前用户
		CustomUser user = getCompleteAuthentication();
		// 参数校验
		Validate.isTrue(user.getId() > 0);

		Date date = new Date();
		MeasureCheck measureCheck = measureCheckService.selectById(user.getId(), pkId);
		measureCheck.setChecker(user.getRealname());
		measureCheck.setCheckResult(1);
		measureCheck.setCheckTime(date);

		Measure measure = new Measure();
		if (checkType == 1) {
			String fullNumber = measureCheck.getMeasureNumber() + '-' + measureCheck.getMeasureSeq() + '/' + measureCheck.getModel();
			measure.setMeasureNumber(measureNumber);
			measure.setMeasureName(measureCheck.getMeasureName());
			measure.setModel(measureCheck.getModel());
			measure.setMeasureBarcode(fullNumber);
			measure.setMeasureSeq(Integer.parseInt(measureCheck.getMeasureSeq()));
			measure.setSupplierId(measureCheck.getSupplierId());
			measure.setSupplierName(measureCheck.getSupplierName());
			measure.setCheckResult(1);
			measure.setCheckTime(date);
			measure.setMeasureStatus(2);
			measure.setSealMark(0);
			measure.setRepairTimes(0);
			measure.setStorageTime(new Date());
			measureCheck.setFullNumber(fullNumber);

		} else if (checkType == 2) {
			measure = measureService.selectByFullNumber(user.getId(), measureCheck.getFullNumber());
			if (measure != null) {
				measure.setMeasureStatus(1);
				Integer repairTimes = measure.getRepairTimes();
				if (repairTimes == null) {
					repairTimes = 0;
				}
				if (isRepair == 1 && measure != null) {
					repairTimes = repairTimes + 1;
				}
				measure.setRepairTimes(repairTimes);
			}
			measureService.updateActiveById(user.getId(), measure, measure.getPkId());
		}

		log.debug("MeasureCheckController.measureCheckPageList>>>");
		return new AjaxReturn(200, null, measureCheckService.checkQualified(user.getId(), measureCheck, measure));
	}

	/**
	 * 量具基础信息分页列表
	 */
	@ApiOperation(value = "量具基础信息分页列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "量具基础信息分页列表查询成功", response = MeasureBase.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/measure-check-unqualified")
	public AjaxReturn measureCheckUnqualified(@ApiParam(name = "pkId", value = "pkId", required = false) @RequestParam(required = false, defaultValue = "10") Long pkId, @ApiParam(name = "checkType", value = "定检类型", required = false) @RequestParam(required = false) Integer checkType, @ApiParam(name = "remark", value = "不合格说明", required = false) @RequestParam(required = false) String remark) throws BusinessException {
		log.debug("MeasureCheckController.measureCheckUnqualified<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
			log.debug("checkType:" + checkType);
		}

		// 获取当前用户
		CustomUser user = getCompleteAuthentication();
		// 参数校验
		Validate.isTrue(user.getId() > 0);
		MeasureCheck measureCheck = measureCheckService.selectById(user.getId(), pkId);
		Date date = new Date();
		measureCheck.setCheckTime(date);
		measureCheck.setChecker(user.getRealname());
		measureCheck.setCheckResult(2);
		measureCheck.setRemark(remark);
		if (checkType == 2) {
			Measure measure = measureService.selectByFullNumber(user.getId(), measureCheck.getFullNumber());
			measure.setCheckResult(2);
			measure.setCheckTime(date);
			measure.setIsScrip(1);
			measureService.updateActiveById(user.getId(), measure, measure.getPkId());
			String message = "量具条码为" + measureCheck.getFullNumber() + "的量具已由质量部判定报废,请您及时处理";
			String authCode = "01040403";
			sysMessageService.insert("量具报废", message, user.getId(), user.getRealname(), authCode);
		}

		log.debug("MeasureCheckController.measureCheckUnqualified>>>");
		return new AjaxReturn(200, null, measureCheckService.updateActiveById(user.getId(), measureCheck, pkId));
	}

	@ApiOperation(value = "量具退货确认", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "量具退货确认", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/return-purchase")
	public AjaxReturn returnPurchase(@ApiParam(name = "pkId", value = "pkId", required = true) @RequestParam(required = true) Long pkId) throws BusinessException {
		log.debug("MeasureCheckController.returnPurchase<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
		}

		// 获取当前用户
		CustomUser user = getCompleteAuthentication();
		// 参数校验
		Validate.isTrue(user.getId() > 0);
		MeasureCheck measureCheck = new MeasureCheck();
		measureCheck.setPkId(pkId);
		measureCheck.setHandleResult(1);
		log.debug("MeasureCheckController.returnPurchase>>>");
		return new AjaxReturn(200, null, measureCheckService.updateActiveById(user.getId(), measureCheck, pkId));
	}
}
