package org.tsinghuatj.web.controller.tool;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
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
import org.tsinghuatj.support.annotation.OperateLog;
import org.tsinghuatj.tool.domain.Tool;
import org.tsinghuatj.tool.domain.ToolRepair;
import org.tsinghuatj.tool.service.IToolRepairService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping({ "/tool" })
public class ToolRepairController extends BaseController {

	private @Autowired(required = false) IToolRepairService toolRepairService;

	/**
	 * 刀具刃磨信息添加
	 */
	@ApiOperation(value = "刀具刃磨信息添加", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具刃磨信息添加成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/tool-repair")
	@OperateLog(info = "刀具刃磨[刀具条码:%s->刃磨量:%s->刃磨人:%s]", params = { "fullNumber", "repairMeasure", "executor" })
	public AjaxReturn toolRepairAdd(@ApiParam(name = "toolId", value = "刀具Id", required = true) @RequestParam(required = true) Long toolId, @ApiParam(name = "supplierId", value = "刀具Id", required = false) @RequestParam(required = false) Long supplierId, @ApiParam(name = "supplierName", value = "刃磨人", required = false) @RequestParam(required = false) String supplierName, @ApiParam(name = "executor", value = "刃磨人", required = true) @RequestParam(required = true) String executor, @ApiParam(name = "toolNumber", value = "刀具码", required = true) @RequestParam(required = true) String toolNumber, @ApiParam(name = "fullNumber", value = "刀具码", required = true) @RequestParam(required = true) String fullNumber,
			@ApiParam(name = "repairMeasure", value = "刃磨量", required = true) @RequestParam(required = true) BigDecimal repairMeasure, @ApiParam(name = "remark", value = "备注", required = false) @RequestParam(required = false, defaultValue = "") String remark) throws BusinessException {
		log.debug("ToolRepairController.toolRepairAdd<<<");
		if (log.isDebugEnabled()) {
			log.debug("toolId:" + toolId);
			log.debug("supplierId:" + supplierId);
			log.debug("supplierName:" + supplierName);
			log.debug("fullNumber:" + fullNumber);
			log.debug("repairMeasure:" + repairMeasure);
			log.debug("remark:" + remark);
		}
		// 获取当前用户
		CustomUser user = getCompleteAuthentication();

		// 封装参数信息
		ToolRepair toolRepair = new ToolRepair();
		toolRepair.setToolId(toolId);
		toolRepair.setSupplierId(supplierId);
		toolRepair.setSupplierName(supplierName);
		toolRepair.setToolNumber(toolNumber);
		toolRepair.setFullNumber(fullNumber);
		toolRepair.setRepairMeasure(repairMeasure);
		toolRepair.setExecutor(user.getRealname());
		toolRepair.setExecutTime(new Date());
		toolRepair.setRemark(remark);

		Tool tool = new Tool();
		tool.setRepairAmountCur(repairMeasure);
		tool.setRepairor(user.getRealname());
		tool.setRepairTime(new Date());
		log.debug("ToolRepairController.toolRepairAdd>>>");

		return new AjaxReturn(200, null, toolRepairService.insert(user.getId(), toolRepair, tool));
	}

	@ApiOperation(value = "刀条刃磨信息添加", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀条刃磨信息添加", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/blade-repair")
	@OperateLog(info = "刀具刃磨[刀具编码:%s->刃磨量:%s]", params = { "toolNumber", "repairMeasure"})
	public AjaxReturn bladeRepairAdd(@ApiParam(name = "waitId", value = "待处理表id", required = true) @RequestParam(required = true) Long waitId, 
			@ApiParam(name = "fullNumber", value = "组合编码", required = false) @RequestParam(required = false) String fullNumber, 
			@ApiParam(name = "toolNumber", value = "刀具编码", required = true) @RequestParam(required = true) String toolNumber, 
			@ApiParam(name = "toolQty", value = "刀具数量", required = true) @RequestParam(required = true) Integer toolQty, 
			@ApiParam(name = "repairMeasure", value = "刃磨量", required = true) @RequestParam(required = true) BigDecimal repairMeasure, 
			@ApiParam(name = "remark", value = "备注", required = false) @RequestParam(required = false) String remark)
			throws BusinessException {
		log.debug("ToolRepairController.bladeRepairAdd<<<");
		if (log.isDebugEnabled()) {
			log.debug("toolNumber:" + toolNumber);
			log.debug("toolQty:" + toolQty);
			log.debug("remark:" + remark);

		}
		// 获取当前用户
		CustomUser user = getCompleteAuthentication();

		// 封装参数信息
		ToolRepair toolRepair = new ToolRepair();
		toolRepair.setFullNumber(fullNumber);
		toolRepair.setToolNumber(toolNumber);
		toolRepair.setToolQty(toolQty);
		toolRepair.setRepairMeasure(repairMeasure);
		toolRepair.setExecutor(user.getRealname());
		toolRepair.setExecutTime(new Date());
		toolRepair.setRemark(remark);
		log.debug("ToolRepairController.bladeRepairAdd>>>");
		return new AjaxReturn(200, null, toolRepairService.insertBladeRepair(user.getId(), toolRepair, waitId));
	}

	@ApiOperation(value = "刀条刃磨信息添加", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀条刃磨信息添加", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/compose-blade-repair")
	@OperateLog(info = "刀条组合刃磨[刀条组合刃磨:%s]", params = { "fullNumber"})
	public AjaxReturn composeBladeRepairAdd(@ApiParam(name = "fullNumber", value = "组合编码", required = false) @RequestParam(required = false) String fullNumber, 
			@ApiParam(name = "waitId", value = "待检ID", required = false) @RequestParam(required = false) Long waitId, 
			@ApiParam(name = "detailJson", value = "待磨刀条", required = false) @RequestParam(required = false) String detailJson

	) throws BusinessException {
		log.debug("ToolRepairController.composeBladeRepairAdd<<<");
		if (log.isDebugEnabled()) {
			log.debug("fullNumber:" + fullNumber);
		}
		// 获取当前用户
		CustomUser user = getCompleteAuthentication();

		log.debug("ToolRepairController.composeBladeRepairAdd>>>");
		toolRepairService.insertComposeBladeRepair(user.getId(), user.getRealname(), fullNumber, waitId, detailJson);
		return new AjaxReturn(200, null, 1);
	}

	/**
	 * 根据主键查找
	 */
	@ApiOperation(value = "刀具刃磨id查询列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具刃磨Id查询列表查询成功", response = ToolRepair.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/tool-repair-get-by-id")
	public AjaxReturn toolRepairGetById(@ApiParam(name = "pkId", value = "pkId", required = true) @RequestParam(required = true) Long pkId) throws BusinessException {
		log.debug("ToolRepairController.toolRepairGetById<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
		}

		// 获取当前用户
		Long curuserId = getAuthentication();
		// 参数校验

		Validate.isTrue(pkId >= 1);

		log.debug("ToolRepairController.toolRepairGetById>>>");
		return new AjaxReturn(200, null, toolRepairService.selectById(curuserId, pkId));
	}

	/**
	 * 查询刀具刃磨信息分页列表
	 */
	@ApiOperation(value = "查询刀具刃磨信息分页列表 ", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "查询刀具刃磨信息分页列表查询成功", response = ToolRepair.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/tool-repair-page-list")
	public AjaxReturn toolRepairPageList(@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, @ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows, @ApiParam(name = "fullNumber", value = "刀具码", required = false) @RequestParam(required = false) String fullNumber, @ApiParam(name = "toolNumber", value = "刀具编码", required = false) @RequestParam(required = false) String toolNumber, @ApiParam(name = "executor", value = "刃磨人", required = false) @RequestParam(required = false) String executor,
			@ApiParam(name = "typeId", value = "刀具类型Id", required = false) @RequestParam(required = false) Integer typeId, @ApiParam(name = "beginDate", value = "开始日期", required = false) @RequestParam(required = false) Date beginDate, @ApiParam(name = "endDate", value = "结束日期", required = false) @RequestParam(required = false) Date endDate

	) throws BusinessException {
		log.debug("ToolRepairController.toolRepairPageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("fullNumber:" + fullNumber);
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

		ToolRepair toolRepair = new ToolRepair();
		toolRepair.setFullNumber(fullNumber);
		toolRepair.setToolNumber(toolNumber);
		toolRepair.setExecutor(executor);
		toolRepair.setTypeId(typeId);
		Pagination<ToolRepair> pagination = toolRepairService.selectPageList(userId, toolRepair, queryDto);
		log.debug("ToolRepairController.toolRepairPageList>>>");
		return pagination;
	}

	@ApiOperation(value = "查询刀具刃磨信息分页列表 ", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "查询刀具刃磨信息分页列表查询成功", response = ToolRepair.class) })
	@RequestMapping(method = RequestMethod.GET, value = "/tool-repair-export")
	public String toolRepairExport(HttpServletRequest request, HttpServletResponse response, @ApiParam(name = "fullNumber", value = "刀具码", required = false) @RequestParam(required = false) String fullNumber, @ApiParam(name = "toolNumber", value = "刀具编码", required = false) @RequestParam(required = false) String toolNumber, @ApiParam(name = "typeId", value = "刀具类型Id", required = false) @RequestParam(required = false) Integer typeId, @ApiParam(name = "executor", value = "刃磨人", required = false) @RequestParam(required = false) String executor, @ApiParam(name = "beginDate", value = "开始日期", required = false) @RequestParam(required = false) Date beginDate,
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

		ToolRepair toolRepair = new ToolRepair();
		toolRepair.setFullNumber(fullNumber);
		toolRepair.setToolNumber(toolNumber);
		toolRepair.setExecutor(executor);
		toolRepair.setTypeId(typeId);
		Pagination<ToolRepair> pagination = toolRepairService.selectPageList(userId, toolRepair, queryDto);
		// 创建HSSFWorkbook对象(excel的文档对象)
		HSSFWorkbook wb = new HSSFWorkbook();
		// 建立新的sheet对象（excel的表单）
		HSSFSheet sheet = wb.createSheet("刀具刃磨记录");
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
		row1.createCell(5).setCellValue("刃磨人");
		row1.createCell(6).setCellValue("刃磨量");
		row1.createCell(7).setCellValue("刃磨时间");
		row1.createCell(8).setCellValue("刃磨价格");
		row1.createCell(9).setCellValue("备注");

		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

		for (int i = 0; i <= 9; i++) {
			row1.getCell(i).setCellStyle(cellStyle);
		}

		row1.setHeight((short) 400);// 目的是想把行高设置成25px
		sheet.setColumnWidth(0, 5000);
		sheet.setColumnWidth(1, 3000);
		sheet.setColumnWidth(2, 5000);
		sheet.setColumnWidth(3, 3000);
		sheet.setColumnWidth(4, 2000);
		sheet.setColumnWidth(5, 3000);
		sheet.setColumnWidth(6, 3000);
		sheet.setColumnWidth(7, 2000);
		sheet.setColumnWidth(8, 2000);
		sheet.setColumnWidth(9, 6000);
		int i = 1;

		HSSFCellStyle cellStyle1 = wb.createCellStyle();
		cellStyle1.setVerticalAlignment(VerticalAlignment.CENTER);
		List<ToolRepair> list = (List<ToolRepair>) pagination.getRows();
		for (ToolRepair item : list) {
			HSSFRow dataRow = sheet.createRow(i);
			dataRow.setHeight((short) (25 * 20));//
			dataRow.createCell(0).setCellValue(item.getFullNumber());
			dataRow.createCell(1).setCellValue(item.getToolNumber());
			dataRow.createCell(2).setCellValue(item.getToolName());
			dataRow.createCell(3).setCellValue(item.getToolMap());

			dataRow.createCell(4).setCellValue(null != item.getToolQty() ? item.getToolQty() : 1);
			dataRow.createCell(5).setCellValue(item.getExecutor());
			if (null != item.getRepairMeasure()) {
				dataRow.createCell(6).setCellValue(item.getRepairMeasure().toString());
			}
			dataRow.createCell(7).setCellValue(DateFormatUtils.format(item.getExecutTime(), "yyyy-MM-dd HH:mm:ss"));
			if (null != item.getRepairPrice()) {
				dataRow.createCell(8).setCellValue(item.getRepairPrice().toString());
			}
			dataRow.createCell(9).setCellValue(item.getRemark());
			i++;
		}

		// 输出Excel文件
		String fileName = "刃磨记录.xls";
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

}
