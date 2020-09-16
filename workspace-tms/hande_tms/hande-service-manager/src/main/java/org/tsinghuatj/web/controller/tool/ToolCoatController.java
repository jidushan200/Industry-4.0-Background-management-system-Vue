package org.tsinghuatj.web.controller.tool;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;

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
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.framework.web.controller.BaseController;
import org.tsinghuatj.framework.web.support.annotation.Secure;
import org.tsinghuatj.support.annotation.OperateLog;
import org.tsinghuatj.tool.domain.ToolCoat;
import org.tsinghuatj.tool.service.IToolCoatService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping({ "/tool" })
public class ToolCoatController extends BaseController {

	private @Autowired(required = false) IToolCoatService toolCoatService;

	/**
	 * 刀具涂层信息添加
	 */
	@ApiOperation(value = "刀具涂层信息添加", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "添加刀具涂层信息成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/tool-coat-add")
	@OperateLog(info = "刀具涂层[刀具条码:%s->涂层供应商:%s]", params = { "fullNumber", "coatSupplier" })
	public AjaxReturn toolCoatAdd(
			@ApiParam(name = "toolId", value = "刀具id", required = true) @RequestParam(required = true) Long toolId,
			@ApiParam(name = "fullNumber", value = "刀具码", required = true) @RequestParam(required = true) String fullNumber,
			@ApiParam(name = "supplierId", value = "涂层供应商", required = true) @RequestParam(required = true) Long supplierId,
			@ApiParam(name = "coatSupplier", value = "涂层供应商", required = true) @RequestParam(required = true) String coatSupplier,
			@ApiParam(name = "remark", value = "备注", required = false) @RequestParam(required = false, defaultValue = "") String remark)
			throws BusinessException {
		log.debug("ToolCoatController.toolCoatAdd<<<");
		if (log.isDebugEnabled()) {
			log.debug("toolId:" + toolId);
			log.debug("fullNumber:" + fullNumber);
			log.debug("coatSupplier:" + coatSupplier);
			log.debug("remark:" + remark);
		}
		// 获取当前用户
		Long userId = getAuthentication();

		// 封装参数信息
		ToolCoat toolCoat = new ToolCoat();
		toolCoat.setToolId(toolId);
		toolCoat.setFullNumber(fullNumber);
		toolCoat.setSupplierId(supplierId);
		toolCoat.setCoatSupplier(coatSupplier);
		toolCoat.setCoatTime(new Date());
		toolCoat.setRemark(remark);

		log.debug("ToolCoatController.toolCoatAdd>>>");

		return new AjaxReturn(200, null, toolCoatService.insert(userId, toolCoat));
	}

	/**
	 * 刀具涂层信息id查询
	 */
	@ApiOperation(value = "刀具涂层信息id查询", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具涂层信息Id查询成功", response = ToolCoat.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/tool-coat-get-by-id")
	public AjaxReturn toolCoatGetById(
			@ApiParam(name = "pkId", value = "pkId", required = true) @RequestParam(required = true) Long pkId)
			throws BusinessException {
		log.debug("ToolCoatController.toolCoatGetById<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
		}

		// 获取当前用户
		Long curuserId = getAuthentication();
		// 参数校验

		Validate.isTrue(pkId >= 1);

		log.debug("ToolCoatController.toolCoatGetById>>>");
		return new AjaxReturn(200, null, toolCoatService.selectById(curuserId, pkId));
	}

	/**
	 * 查询刀具涂层信息分页列表
	 */
	@ApiOperation(value = "查询刀具涂层信息分页列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具涂层信息分页列表查询成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/tool-coat-page-list")
	public AjaxReturn toolCoatPageList(
			@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, //
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows,
			@ApiParam(name = "beginDate", value = "涂回开始日期", required = false) @RequestParam(required = false) Date beginDate,
			@ApiParam(name = "endDate", value = "涂回结束日期", required = false) @RequestParam(required = false) Date endDate,
			@ApiParam(name = "outboundUserName", value = "涂层出库人", required = false) @RequestParam(required = false) String outboundUserName,
			@ApiParam(name = "supplierId", value = "刀具编码", required = false) @RequestParam(required = false) Long supplierId)
			throws BusinessException {
		log.debug("ToolCoatController.toolCoatPageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("page:" + page);
			log.debug("rows:" + rows);
			log.debug("supplierId:" + supplierId);
		}

		// 获取当前用户
		Long userId = getAuthentication();
		// 参数校验
		Validate.isTrue(userId > 0);
		Validate.isTrue(page >= 0);
		Validate.isTrue(rows <= 100);
		// 封装查询条件
		QueryDto queryDto = new QueryDto();
		queryDto.setPage(page);
		queryDto.setRows(rows);
		queryDto.setBeginDate(beginDate);
		queryDto.setEndDate(endDate);

		ToolCoat toolCoat = new ToolCoat();
		toolCoat.setSupplierId(supplierId);
		toolCoat.setOutboundUserName(outboundUserName);
		
		Pagination<ToolCoat> pagination = toolCoatService.selectPageList(userId, toolCoat, queryDto);
		log.debug("ToolCoatController.toolCoatPageList>>>");
		return pagination;
	}

	/**
	 * 查询刀具刃磨信息分页列表
	 * 
	 * @throws IOException
	 */
	@ApiOperation(value = "查询刀具刃磨信息分页列表 ", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "查询刀具刃磨信息分页列表查询成功", response = String.class) })
	@RequestMapping(method = RequestMethod.GET, value = "/tool-coat-export")
	public String toolCoatExport(HttpServletRequest request, HttpServletResponse response,
			@ApiParam(name = "fullNumber", value = "刀具码", required = false) @RequestParam(required = false) String fullNumber,
			@ApiParam(name = "toolNumber", value = "刀具编码", required = false) @RequestParam(required = false) String toolNumber,
			@ApiParam(name = "outboundUserName", value = "涂层出库人", required = false) @RequestParam(required = false) String outboundUserName,
			@ApiParam(name = "supplierId", value = "刀具编码", required = false) @RequestParam(required = false) Long supplierId,
			@ApiParam(name = "beginDate", value = "开始日期", required = false) @RequestParam(required = false) Date beginDate,
			@ApiParam(name = "endDate", value = "结束日期", required = false) @RequestParam(required = false) Date endDate

	) throws BusinessException, IOException {
		log.debug("ToolRepairController.toolRepairExport<<<");
		if (log.isDebugEnabled()) {
			log.debug("fullNumber:" + fullNumber);
			log.debug("beginDate:" + beginDate);
			log.debug("endDate:" + endDate);
		}

		// 获取当前用户
		Long userId = getAuthentication();

		// 封装查询条件
		QueryDto queryDto = new QueryDto();
		queryDto.setPage(1);
		queryDto.setRows(50000);
		queryDto.setBeginDate(beginDate);
		queryDto.setEndDate(endDate);

		ToolCoat toolCoat = new ToolCoat();
		toolCoat.setSupplierId(supplierId);
		toolCoat.setOutboundUserName(outboundUserName);
		Pagination<ToolCoat> pagination = toolCoatService.selectPageList(userId, toolCoat, queryDto);
		// 创建HSSFWorkbook对象(excel的文档对象)
		HSSFWorkbook wb = new HSSFWorkbook();
		// 建立新的sheet对象（excel的表单）
		HSSFSheet sheet = wb.createSheet("刀具涂层记录");
		// 在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
		HSSFRow row1 = sheet.createRow(0);
		row1.setHeight((short) (25 * 20));//
		// 创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个

		HSSFCellStyle cellStyle = wb.createCellStyle();
		// 设置单元格内容
		row1.createCell(0).setCellValue("物料条码");
		row1.createCell(1).setCellValue("物料编码");
		row1.createCell(2).setCellValue("物料名称");
		row1.createCell(3).setCellValue("物料图号");
		row1.createCell(4).setCellValue("物料数量");
		row1.createCell(5).setCellValue("涂层出库人");
		row1.createCell(6).setCellValue("涂层出库时间");
		row1.createCell(7).setCellValue("供应商");
		row1.createCell(8).setCellValue("涂层时间");
		row1.createCell(9).setCellValue("涂层价格");
		row1.createCell(10).setCellValue("备注");

		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

		for (int i = 0; i <= 8; i++) {
			row1.getCell(i).setCellStyle(cellStyle);
		}

		row1.setHeight((short) 400);// 目的是想把行高设置成25px
		sheet.setColumnWidth(0, 5000);
		sheet.setColumnWidth(1, 3000);
		sheet.setColumnWidth(2, 5000);
		sheet.setColumnWidth(3, 3000);
		sheet.setColumnWidth(4, 2000);
		sheet.setColumnWidth(5, 2000);
		sheet.setColumnWidth(6, 3000);
		sheet.setColumnWidth(7, 3000);
		sheet.setColumnWidth(8, 3000);
		sheet.setColumnWidth(9, 2000);
		sheet.setColumnWidth(10, 6000);
		int i = 1;

		HSSFCellStyle cellStyle1 = wb.createCellStyle();
		cellStyle1.setVerticalAlignment(VerticalAlignment.CENTER);
		List<ToolCoat> list = (List<ToolCoat>) pagination.getRows();
		for (ToolCoat item : list) {
			HSSFRow dataRow = sheet.createRow(i);
			dataRow.setHeight((short) (25 * 20));//
			dataRow.createCell(0).setCellValue(item.getFullNumber());
			dataRow.createCell(1).setCellValue(item.getToolNumber());
			dataRow.createCell(2).setCellValue(item.getToolName());
			dataRow.createCell(3).setCellValue(item.getToolMap());
			dataRow.createCell(4).setCellValue(null != item.getToolQty() ? item.getToolQty() : 1);
			if(null!=item.getOutboundUserName()){
				dataRow.createCell(5).setCellValue(item.getOutboundUserName());
			}
			if(null!=item.getCoatTime()){
				dataRow.createCell(6).setCellValue(DateFormatUtils.format(item.getCoatTime(), "yyyy-MM-dd HH:mm:ss"));
			}
			dataRow.createCell(7).setCellValue(item.getCoatSupplier());
			dataRow.createCell(8).setCellValue(DateFormatUtils.format(item.getCoatTime(), "yyyy-MM-dd HH:mm:ss"));
			if (null != item.getCoatPrice()) {
				dataRow.createCell(9).setCellValue(item.getCoatPrice().toString());
			}
			dataRow.createCell(10).setCellValue(item.getRemark());
			i++;
		}

		// 输出Excel文件
		String fileName = "涂层记录.xls";
		// 获得浏览器信息并转换为大写
		String agent = request.getHeader("User-Agent").toUpperCase();
		if (agent.indexOf("MSIE") > 0 || (agent.indexOf("GECKO") > 0 && agent.indexOf("RV:11") > 0)) {
			// 微软的浏览器(IE和Edge浏览器)
			fileName = URLEncoder.encode(fileName, "UTF-8");
		} else {
			fileName = new String(fileName.getBytes("UTF-8"), "iso-8859-1");
		}
		OutputStream output = response.getOutputStream();
		response.reset();
		response.setHeader("Content-disposition", "attachment; filename=" + fileName);
		response.setContentType("application/msexcel");
		wb.write(output);
		output.close();
		wb.close();
		return null;
	}

	@ApiOperation(value = "查询刀具涂层结算信息分页列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具涂层结算信息分页列表查询成功", response = ToolCoat.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/coat-statistics-page-list")
	public AjaxReturn coatStatisticsPageList(
			@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, //
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows,
			@ApiParam(name = "beginDate", value = "开始日期", required = false) @RequestParam(required = false) Date beginDate,
			@ApiParam(name = "endDate", value = "结束日期", required = false) @RequestParam(required = false) Date endDate,
			@ApiParam(name = "beginTime", value = "结算开始日期", required = false) @RequestParam(required = false) Date beginTime,
			@ApiParam(name = "endTime", value = "结算结束日期", required = false) @RequestParam(required = false) Date endTime,
			@ApiParam(name = "degreeMin", value = "完成度", required = false) @RequestParam(required = false) BigDecimal degreeMin, //
			@ApiParam(name = "degreeMax", value = "完成度", required = false) @RequestParam(required = false) BigDecimal degreeMax, //
			@ApiParam(name = "sort", value = "排序", required = false) @RequestParam(required = false) String sort,
			@ApiParam(name = "settlementStatus", value = "结算状态(0-未结算;1-已结算)", required = false) @RequestParam(required = false) Integer settlementStatus,
			@ApiParam(name = "supplierId", value = "供应商Id", required = false) @RequestParam(required = false) Long supplierId)
			throws BusinessException {
		log.debug("ToolCoatController.coatStatisticsPageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("page:" + page);
			log.debug("rows:" + rows);
			log.debug("supplierId:" + supplierId);
		}

		// 获取当前用户
		Long userId = getAuthentication();
		// 参数校验
		Validate.isTrue(userId > 0);
		Validate.isTrue(page >= 0);
		// Validate.isTrue(rows <= 100);
		// 封装查询条件
		QueryDto queryDto = new QueryDto();
		queryDto.setPage(page);
		queryDto.setRows(rows);
		queryDto.setBeginDate(beginDate);
		queryDto.setEndDate(endDate);
		queryDto.setSort(sort);

		ToolCoat toolCoat = new ToolCoat();
		toolCoat.setSupplierId(supplierId);
		toolCoat.setBeginTime(beginTime);
		toolCoat.setEndTime(endTime);
		if(null!=degreeMin){
			toolCoat.setDegreeMin(degreeMin.divide(new BigDecimal(100)));
		}
		if(null!=degreeMax){
			toolCoat.setDegreeMax(degreeMax.divide(new BigDecimal(100)));
		}		
		
		toolCoat.setSettlementStatus(settlementStatus);
		Pagination<ToolCoat> pagination = toolCoatService.selectStatisticsPageList(userId, toolCoat, queryDto);
		log.debug("ToolCoatController.coatStatisticsPageList>>>");
		return pagination;
	}

	@ApiOperation(value = "涂层结算导出", notes = "", code = 200, produces = "application/json")
	@ApiResponses({ @ApiResponse(code = 200, message = "涂层结算导出", response = String.class) })
	@Secure()
	@GetMapping(path = { "/coat-settlement-export" })
	public String downloadCoatSettlementExcel(HttpServletRequest request, HttpServletResponse response,
			@ApiParam(name = "beginDate", value = "开始日期", required = false) @RequestParam(required = false) Date beginDate,
			@ApiParam(name = "endDate", value = "结束日期", required = false) @RequestParam(required = false) Date endDate,
			@ApiParam(name = "beginTime", value = "结算开始日期", required = false) @RequestParam(required = false) Date beginTime,
			@ApiParam(name = "endTime", value = "结算结束日期", required = false) @RequestParam(required = false) Date endTime,
			@ApiParam(name = "degreeMin", value = "完成度", required = false) @RequestParam(required = false) BigDecimal degreeMin, //
			@ApiParam(name = "degreeMax", value = "完成度", required = false) @RequestParam(required = false) BigDecimal degreeMax, //
			@ApiParam(name = "sort", value = "排序", required = false) @RequestParam(required = false) String sort,
			@ApiParam(name = "settlementStatus", value = "结算状态(0-未结算;1-已结算)", required = false) @RequestParam(required = false) Integer settlementStatus,
			@ApiParam(name = "supplierId", value = "供应商Id", required = false) @RequestParam(required = false) Long supplierId

	) throws Exception {
		Long userId = getAuthentication();
		QueryDto queryDto = new QueryDto();
		queryDto.setPage(1);
		queryDto.setRows(50000);
		queryDto.setBeginDate(beginDate);
		queryDto.setEndDate(endDate);

		ToolCoat toolCoat = new ToolCoat();
		toolCoat.setSupplierId(supplierId);
		toolCoat.setBeginTime(beginTime);
		toolCoat.setEndTime(endTime);
		if(null!=degreeMin){
			toolCoat.setDegreeMin(degreeMin.divide(new BigDecimal(100)));
		}
		if(null!=degreeMax){
			toolCoat.setDegreeMax(degreeMax.divide(new BigDecimal(100)));
		}		
		toolCoat.setSettlementStatus(settlementStatus);
		Pagination<ToolCoat> pagination = toolCoatService.selectStatisticsPageList(userId, toolCoat, queryDto);
		// 创建HSSFWorkbook对象(excel的文档对象)
		HSSFWorkbook wb = new HSSFWorkbook();
		// 建立新的sheet对象（excel的表单）
		HSSFSheet sheet = wb.createSheet("涂层生产计数记录");
		// 在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
		HSSFRow row1 = sheet.createRow(0);
		row1.setHeight((short) (25 * 20));//
		// 创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个

		HSSFCellStyle cellStyle = wb.createCellStyle();
		// 设置单元格内容

		row1.createCell(0).setCellValue("结算价格");
		row1.createCell(1).setCellValue("结算总价");
		row1.createCell(2).setCellValue("理论价格");
		row1.createCell(3).setCellValue("支付价格");
		row1.createCell(4).setCellValue("支付总价");
		row1.createCell(5).setCellValue("结算数量");
		row1.createCell(6).setCellValue("结算人");
		row1.createCell(7).setCellValue("结算时间");
		row1.createCell(8).setCellValue("供应商名称");
		row1.createCell(9).setCellValue("物料编码");
		row1.createCell(10).setCellValue("物料名称");
		row1.createCell(11).setCellValue("物料图号");
		row1.createCell(12).setCellValue("物料条码");
		row1.createCell(13).setCellValue("涂回时间");
		row1.createCell(14).setCellValue("加工数量");
		row1.createCell(15).setCellValue("理论加工数量");
		row1.createCell(16).setCellValue("加工完成度");
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

		for (int i = 0; i <= 16; i++) {
			row1.getCell(i).setCellStyle(cellStyle);
		}

		row1.setHeight((short) 400);// 目的是想把行高设置成25px
		sheet.setColumnWidth(0, 2000);
		sheet.setColumnWidth(1,2000);
		sheet.setColumnWidth(2, 2000);
		sheet.setColumnWidth(3, 2000);
		sheet.setColumnWidth(4, 2000);
		sheet.setColumnWidth(5, 2000);
		sheet.setColumnWidth(6, 2000);
		sheet.setColumnWidth(7, 4000);
		sheet.setColumnWidth(8, 5000);
		sheet.setColumnWidth(9, 3000);
		sheet.setColumnWidth(10, 4000);
		sheet.setColumnWidth(11, 3000);
		sheet.setColumnWidth(12, 3000);
		sheet.setColumnWidth(13, 4000);
		sheet.setColumnWidth(14, 2000);
		sheet.setColumnWidth(15, 2000);
		sheet.setColumnWidth(16, 2000);
		int i = 1;

		HSSFCellStyle cellStyle1 = wb.createCellStyle();
		cellStyle1.setVerticalAlignment(VerticalAlignment.CENTER);

		NumberFormat percent = NumberFormat.getPercentInstance(); // 建立百分比格式化引用
		percent.setMaximumFractionDigits(3); // 百分比小数点最多3位
		for (ToolCoat item : pagination.getRows()) {
			HSSFRow dataRow = sheet.createRow(i);
			dataRow.setHeight((short) (25 * 20));//
			if (null != item.getSettlementPrice()) {
				dataRow.createCell(0).setCellValue(item.getSettlementPrice().toString());
				dataRow.createCell(1).setCellValue(item.getSettlementAmount().toString());
							
				dataRow.createCell(3).setCellValue(item.getPayPrice().toString());
				dataRow.createCell(4).setCellValue(item.getPayAmount().toString());
				
				dataRow.createCell(6).setCellValue(item.getSettlementName());
				dataRow.createCell(7)
						.setCellValue(DateFormatUtils.format(item.getSettlementTime(), "yyyy-MM-dd HH:mm:ss"));
			}
			if(null!=item.getCoatPrice()){
				dataRow.createCell(2).setCellValue(item.getCoatPrice().toString());
			}	
			dataRow.createCell(5).setCellValue(item.getSettlementQty());
			dataRow.createCell(8).setCellValue(item.getCoatSupplier());
			dataRow.createCell(9).setCellValue(item.getToolNumber());
			dataRow.createCell(10).setCellValue(item.getToolName());
			dataRow.createCell(11).setCellValue(item.getToolMap());
			if (null != item.getFullNumber()) {
				dataRow.createCell(12).setCellValue(item.getFullNumber());
			}
			dataRow.createCell(13).setCellValue(DateFormatUtils.format(item.getCoatTime(), "yyyy-MM-dd HH:mm:ss"));
			if (null != item.getProcessQty()) {
				dataRow.createCell(14).setCellValue(item.getProcessQty());				
			}	
			if (null != item.getTheoreticalQty()) {
				dataRow.createCell(15).setCellValue(item.getTheoreticalQty());
			}
			if (null != item.getCompletionDegree()) {
				dataRow.createCell(16).setCellValue(percent.format(item.getCompletionDegree()));
			}
			i++;
		}
		// 输出Excel文件
		String fileName = "涂层结算.xls";
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
	 * 刀具涂层信息id查询
	 */
	@ApiOperation(value = "刀具涂层信息id查询", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具涂层信息Id查询成功", response = ToolCoat.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/tool-coat-settlement-update")
	public AjaxReturn toolCoatUpdateSettlement(
			@ApiParam(name = "pkId", value = "pkId", required = true) @RequestParam(required = true) Long pkId,
			@ApiParam(name = "settlementPrice", value = "结算单价", required = true) @RequestParam(required = true) BigDecimal settlementPrice,
			@ApiParam(name = "settlementQty", value = "结算数量", required = true) @RequestParam(required = true) Integer settlementQty)
			throws BusinessException {
		log.debug("ToolCoatController.toolCoatUpdateSettlement<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
		}

		// 获取当前用户
		Long curuserId = getAuthentication();
		// 参数校验

		Validate.isTrue(pkId >= 1);
		ToolCoat toolCoat = new ToolCoat();

		toolCoat.setPkId(pkId);
		toolCoat.setSettlementTime(new Date());
		toolCoat.setCreateUser(curuserId);
		toolCoat.setSettlementPrice(settlementPrice);
		toolCoat.setSettlementQty(settlementQty);
		log.debug("ToolCoatController.toolCoatUpdateSettlement>>>");
		return new AjaxReturn(200, null, toolCoatService.updateActiveById(curuserId, toolCoat, pkId));
	}

	@ApiOperation(value = "涂层结算导出", notes = "", code = 200, produces = "application/json")
	@ApiResponses({ @ApiResponse(code = 200, message = "涂层结算导出", response = String.class) })
	@Secure()
	@GetMapping(path = { "/coat-statistics-export" })
	public String downloadCoatStatisticsExcel(HttpServletRequest request, HttpServletResponse response,
			@ApiParam(name = "beginDate", value = "开始日期", required = false) @RequestParam(required = false) Date beginDate,
			@ApiParam(name = "endDate", value = "结束日期", required = false) @RequestParam(required = false) Date endDate,
			@ApiParam(name = "supplierId", value = "供应商Id", required = false) @RequestParam(required = false) Long supplierId

	) throws Exception {
		Long userId = getAuthentication();
		QueryDto queryDto = new QueryDto();
		queryDto.setPage(1);
		queryDto.setRows(50000);
		queryDto.setBeginDate(beginDate);
		queryDto.setEndDate(endDate);

		ToolCoat toolCoat = new ToolCoat();
		toolCoat.setSupplierId(supplierId);
		Pagination<ToolCoat> pagination = toolCoatService.selectStatisticsPageList(userId, toolCoat, queryDto);
		// 创建HSSFWorkbook对象(excel的文档对象)
		HSSFWorkbook wb = new HSSFWorkbook();
		// 建立新的sheet对象（excel的表单）
		HSSFSheet sheet = wb.createSheet("涂层生产计数记录");
		// 在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
		HSSFRow row1 = sheet.createRow(0);
		row1.setHeight((short) (25 * 20));//
		// 创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个

		HSSFCellStyle cellStyle = wb.createCellStyle();
		// 设置单元格内容

		row1.createCell(0).setCellValue("供应商名称");
		row1.createCell(1).setCellValue("物料编码");
		row1.createCell(2).setCellValue("物料名称");
		row1.createCell(3).setCellValue("物料图号");
		row1.createCell(4).setCellValue("物料条码");
		row1.createCell(5).setCellValue("涂回时间");
		row1.createCell(6).setCellValue("加工数量");
		row1.createCell(7).setCellValue("理论加工数量");
		row1.createCell(8).setCellValue("加工完成度");

		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

		for (int i = 0; i <= 8; i++) {
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
		int i = 1;

		HSSFCellStyle cellStyle1 = wb.createCellStyle();
		cellStyle1.setVerticalAlignment(VerticalAlignment.CENTER);

		NumberFormat percent = NumberFormat.getPercentInstance(); // 建立百分比格式化引用
		percent.setMaximumFractionDigits(3); // 百分比小数点最多3位
		for (ToolCoat item : pagination.getRows()) {
			HSSFRow dataRow = sheet.createRow(i);
			dataRow.setHeight((short) (25 * 20));//
			dataRow.createCell(0).setCellValue(item.getCoatSupplier());
			dataRow.createCell(1).setCellValue(item.getToolNumber());
			dataRow.createCell(2).setCellValue(item.getToolName());
			dataRow.createCell(3).setCellValue(item.getToolMap());
			dataRow.createCell(4).setCellValue(item.getFullNumber());
			dataRow.createCell(5).setCellValue(DateFormatUtils.format(item.getCoatTime(), "yyyy-MM-dd HH:mm:ss"));
			if (null != item.getProcessQty()) {
				dataRow.createCell(6).setCellValue(item.getProcessQty());
			}
			if (null != item.getTheoreticalQty()) {
				dataRow.createCell(7).setCellValue(item.getTheoreticalQty());
			}
			if (null != item.getCompletionDegree()) {
				dataRow.createCell(8).setCellValue(percent.format(item.getCompletionDegree()));
			}
			i++;
		}
		// 输出Excel文件
		String fileName = "涂层生产计数.xls";
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

	@ApiOperation(value = "刀具涂层信息id查询", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具涂层信息Id查询成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/coat-settlement")
	public AjaxReturn coatSettlement(
			@ApiParam(name = "settlementList", value = "涂层结算记录", required = true) @RequestParam(required = true) String settlementList)
			throws BusinessException {
		log.debug("ToolCoatController.coatSettlement<<<");
		// 获取当前用户
		Long userId = getAuthentication();
		// 参数校验
		toolCoatService.updatecoatSettlement(userId, settlementList);
		log.debug("ToolCoatController.coatSettlement>>>");
		return new AjaxReturn(200, null, 1);
	}

}
