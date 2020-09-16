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
import org.tsinghuatj.framework.utils.excel.ExcelUtils;
import org.tsinghuatj.framework.web.controller.BaseController;
import org.tsinghuatj.framework.web.support.annotation.Secure;
import org.tsinghuatj.support.annotation.OperateLog;
import org.tsinghuatj.tool.domain.Tool;
import org.tsinghuatj.tool.domain.ToolBladeProcess;
import org.tsinghuatj.tool.domain.ToolProcess;
import org.tsinghuatj.tool.service.IToolBladeProcessService;
import org.tsinghuatj.tool.service.IToolProcessService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping({ "/tool" })
public class ToolProcessController extends BaseController {

	private @Autowired(required = false) IToolProcessService toolProcessService;
	
	private @Autowired(required = false) IToolBladeProcessService bladeProcessService;

	/**
	 * 刀具加工信息添加
	 */
	@ApiOperation(value = "刀具加工信息添加", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具加工信息添加成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/tool-process-add")
	@OperateLog(info = "刀具加工[刀具条码：%s->制件名称:%s->加工数量:%s]", params = { "fullNumber", "partName", "processAmount" })
	public AjaxReturn toolProcessAdd(@ApiParam(name = "toolId", value = "刀具Id", required = true) @RequestParam(required = true) Long toolId, @ApiParam(name = "fullNumber", value = "物料条码", required = true) @RequestParam(required = true) String fullNumber, @ApiParam(name = "toolNumber", value = "物料编码", required = true) @RequestParam(required = true) String toolNumber, @ApiParam(name = "toolMap", value = "物料图号", required = false) @RequestParam(required = false) String toolMap, @ApiParam(name = "toolName", value = "物料名称", required = false) @RequestParam(required = false) String toolName, @ApiParam(name = "departmentId", value = "部门ID", required = false) @RequestParam(required = false) Long departmentId,
			@ApiParam(name = "departmentName", value = " 部门名称", required = false) @RequestParam(required = false) String departmentName, @ApiParam(name = "teamId", value = "班组ID", required = false) @RequestParam(required = false) Long teamId, @ApiParam(name = "teamName", value = " 班组名称", required = false) @RequestParam(required = false) String teamName, @ApiParam(name = "staffCode", value = " 员工编号", required = false) @RequestParam(required = false) String staffCode, @ApiParam(name = "staffName", value = " 员工编号", required = false) @RequestParam(required = false) String staffName, @ApiParam(name = "partCode", value = "绑定设备编码", required = false) @RequestParam(required = false) String partCode,
			@ApiParam(name = "partName", value = " 设备名称", required = false) @RequestParam(required = false) String partName, @ApiParam(name = "equipmentCode", value = "绑定设备编码", required = false) @RequestParam(required = false) String equipmentCode, @ApiParam(name = "equipmentName", value = " 设备名称", required = false) @RequestParam(required = false) String equipmentName, @ApiParam(name = "processAmount", value = "加工数量", required = true) @RequestParam(required = true) Integer processAmount, @ApiParam(name = "returnResion", value = "交回原因", required = true) @RequestParam(required = true) Integer returnResion,
			@ApiParam(name = "repairMeasure", value = "刃磨量", required = false) @RequestParam(required = false) BigDecimal repairMeasure, @ApiParam(name = "remark", value = " 异常说明", required = false) @RequestParam(required = false) String remark) throws BusinessException {
		log.debug("ToolProcessController.toolProcessAdd<<<");
		if (log.isDebugEnabled()) {
			log.debug("fullNumber:" + fullNumber);
			log.debug("toolNumber:" + toolNumber);
			log.debug("toolMap" + toolMap);
			log.debug("toolName:" + toolName);
			log.debug("departmentId:" + departmentId);
			log.debug("departmentName:" + departmentName);

			log.debug("partCode:" + partCode);
			log.debug("partName:" + partName);

			log.debug("equipmentCode:" + equipmentCode);
			log.debug("equipmentName:" + equipmentName);
			log.debug("processAmount:" + processAmount);
			log.debug("returnResion:" + returnResion);
			log.debug("remark:" + remark);
		}
		// 获取当前用户
		CustomUser user = getCompleteAuthentication();

		// 封装参数信息
		ToolProcess toolProcess = new ToolProcess();
		toolProcess.setToolId(toolId);
		toolProcess.setFullNumber(fullNumber);
		toolProcess.setToolNumber(toolNumber);

		toolProcess.setToolMap(toolMap);
		toolProcess.setToolName(toolName);
		toolProcess.setDepartmentId(departmentId);
		toolProcess.setDepartmentName(departmentName);
		toolProcess.setTeamId(teamId);
		toolProcess.setTeamName(teamName);

		toolProcess.setStaffCode(staffCode);
		toolProcess.setStaffName(staffName);
		toolProcess.setPartCode(partCode);
		toolProcess.setPartName(partName);

		toolProcess.setEquipmentCode(equipmentCode);
		toolProcess.setEquipmentName(equipmentName);

		toolProcess.setEndTime(new Date());

		toolProcess.setProcessQty(processAmount);

		toolProcess.setReturnResion(returnResion);
		toolProcess.setRemark(remark);

		log.debug("ToolProcessController.toolProcessAdd>>>");
		return new AjaxReturn(200, null, toolProcessService.insert(user.getId(), user.getRealname(), toolProcess, repairMeasure));
	}

	/**
	 * 刀具加工信息添加
	 */
	@ApiOperation(value = "刀具无加工数量返回", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具无加工数量返回", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/no-process-return")
	@OperateLog(info = "刀具加工[刀具条码：%s]", params = { "fullNumber"})
	public AjaxReturn noProcessReturn(			
			@ApiParam(name = "fullNumber", value = "物料条码", required = true) @RequestParam(required = true) String fullNumber
			) throws BusinessException {
		log.debug("ToolProcessController.noProcessReturn<<<");
		if (log.isDebugEnabled()) {
			log.debug("fullNumber:" + fullNumber);
		}
		// 获取当前用户
		Long userId = getAuthentication();
		
		log.debug("ToolProcessController.noProcessReturn>>>");
		return new AjaxReturn(200, null, toolProcessService.noProcessReturn(userId, fullNumber));
	}

	/**
	 * 根据主键查找
	 */
	@ApiOperation(value = "刀具加工id查询列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具加工Id查询列表查询成功", response = ToolProcess.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/tool-process-get-by-id")
	public AjaxReturn toolProcessGetById(@ApiParam(name = "pkId", value = "pkId", required = true) @RequestParam(required = true) Long pkId) throws BusinessException {
		log.debug("ToolProcessController.toolProcessGetById<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
		}

		// 获取当前用户
		Long curuserId = getAuthentication();
		// 参数校验

		Validate.isTrue(pkId >= 1);

		log.debug("ToolProcessController.toolProcessGetById>>>");
		return new AjaxReturn(200, null, toolProcessService.selectById(curuserId, pkId));
	}

	/**
	 * 查询刀具加工信息分页列表
	 */
	@ApiOperation(value = "查询刀具加工信息分页列表 ", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "查询刀具加工信息分页列表查询成功", response = Tool.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/tool-process-page-list")
	public AjaxReturn toolProcessPageList(@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, @ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows, @ApiParam(name = "toolNumber", value = "物料编码", required = false) @RequestParam(required = false) String toolNumber, @ApiParam(name = "fullNumber", value = "物料条码", required = false) @RequestParam(required = false) String fullNumber, @ApiParam(name = "beginDate", value = "开始日期", required = false) @RequestParam(required = false) Date beginDate,
			@ApiParam(name = "endDate", value = "结束日期", required = false) @RequestParam(required = false) Date endDate) throws BusinessException {
		log.debug("ToolProcessController.toolProcessPageList<<<");
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
		ToolProcess process = new ToolProcess();
		process.setToolNumber(toolNumber);
		process.setFullNumber(fullNumber);
		Pagination<ToolProcess> pagination = toolProcessService.selectPageList(userId, process, queryDto);
		log.debug("ToolProcessController.toolProcessPageList>>>");
		return pagination;
	}
	
	
	@ApiOperation(value = "查询刀条加工信息分页列表 ", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "查询刀条加工信息分页列表查询成功", response = Tool.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/blade-process-page-list")
	public AjaxReturn bladeProcessPageList(
			@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, 
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows,			
			@ApiParam(name = "composeNumber", value = "刀条组合条码", required = false) @RequestParam(required = false) String composeNumber, 
			@ApiParam(name = "beginDate", value = "开始日期", required = false) @RequestParam(required = false) Date beginDate,
			@ApiParam(name = "endDate", value = "结束日期", required = false) @RequestParam(required = false) Date endDate) throws BusinessException {
		log.debug("ToolProcessController.bladeProcessPageList<<<");
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
		ToolBladeProcess process = new ToolBladeProcess();	
		process.setComposeNumber(composeNumber);
		Pagination<ToolBladeProcess> pagination = bladeProcessService.selectPageList(userId, process, queryDto);
		log.debug("ToolProcessController.bladeProcessPageList>>>");
		return pagination;
	}
	
	
	/**
	 * 查询刀具加工信息分页列表
	 * 
	 * @throws IOException
	 */
	@ApiOperation(value = "查询刀条加工信息分页列表 ", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "查询刀条加工信息分页列表查询成功", response = String.class) })
	@GetMapping(value = "/blade-process-export")
	@Secure()
	public String bladeProcessExport(HttpServletRequest request, HttpServletResponse response,
			@ApiParam(name = "toolNumber", value = "物料编码", required = false) @RequestParam(required = false) String toolNumber, 
			@ApiParam(name = "composeNumber", value = "刀条组合条码", required = false) @RequestParam(required = false) String composeNumber, 			
			@ApiParam(name = "beginDate", value = "开始日期", required = false) @RequestParam(required = false) Date beginDate, @ApiParam(name = "endDate", value = "结束日期", required = false) @RequestParam(required = false) Date endDate) throws BusinessException, IOException {
		log.debug("ToolProcessController.bladeProcessExport<<<");
		if (log.isDebugEnabled()) {
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
		ToolBladeProcess process = new ToolBladeProcess();		
		process.setComposeNumber(composeNumber);
		// 创建HSSFWorkbook对象(excel的文档对象)
		HSSFWorkbook wb = new HSSFWorkbook();
		// 建立新的sheet对象（excel的表单）
		HSSFSheet sheet = wb.createSheet("刀条生产统计");
		// 在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
		HSSFRow row1 = sheet.createRow(0);
		row1.setHeight((short) (25 * 20));//
		// 创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个

		HSSFCellStyle cellStyle = wb.createCellStyle();
		// 设置单元格内容
		row1.createCell(0).setCellValue("刀条组合条码");
		row1.createCell(1).setCellValue("刀条组合名称");
		row1.createCell(2).setCellValue("物料编码");
		row1.createCell(3).setCellValue("物料名称");
		row1.createCell(4).setCellValue("物料图号");
		row1.createCell(5).setCellValue("使用部门");	
		row1.createCell(6).setCellValue("领用时间");
		row1.createCell(7).setCellValue("归还时间");
		row1.createCell(8).setCellValue("制件编号");
		row1.createCell(9).setCellValue("制件名称");
		row1.createCell(10).setCellValue("设备标签");
		row1.createCell(11).setCellValue("设备名称");
		row1.createCell(12).setCellValue("理论加工数量");
		row1.createCell(13).setCellValue("加工数量");		

		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

		for (int i = 0; i <= 13; i++) {
			row1.getCell(i).setCellStyle(cellStyle);
		}
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
		sheet.setColumnWidth(11, 5000);
		sheet.setColumnWidth(12, 5000);
		sheet.setColumnWidth(13, 5000);		
		int i = 1;
		HSSFCellStyle cellStyle1 = wb.createCellStyle();
		cellStyle1.setVerticalAlignment(VerticalAlignment.CENTER);
		Pagination<ToolBladeProcess> pagination = bladeProcessService.selectPageList(userId, process, queryDto);
		for (ToolBladeProcess item : pagination.getRows()) {
			HSSFRow dataRow = sheet.createRow(i);
			dataRow.setHeight((short) (25 * 20));//
			dataRow.createCell(0).setCellValue(item.getComposeNumber());
			dataRow.createCell(1).setCellValue(item.getHeadName());
			
			//dataRow.createCell(2).setCellValue(item.getToolNumber());
			//dataRow.createCell(3).setCellValue(item.getToolName());
			//dataRow.createCell(4).setCellValue(item.getToolMap());			
			dataRow.createCell(5).setCellValue(item.getDepartmentName());	
			dataRow.createCell(6).setCellValue(DateFormatUtils.format(item.getBeginTime(), "yyyy-MM-dd hh:MM"));
			dataRow.createCell(7).setCellValue(DateFormatUtils.format(item.getEndTime(), "yyyy-MM-dd hh:MM"));
			dataRow.createCell(8).setCellValue(item.getPartCode());
			dataRow.createCell(9).setCellValue(item.getPartName());
			dataRow.createCell(10).setCellValue(item.getTagNumber());
			dataRow.createCell(11).setCellValue(item.getEquipmentName());
			if (null != item.getTheoreticalQty()) {
				dataRow.createCell(12).setCellValue(item.getTheoreticalQty());
			}
			if (null != item.getProcessQty()) {
				dataRow.createCell(13).setCellValue(item.getProcessQty());
			}			
			i++;
		}
		// 输出Excel文件
		String fileName = "刀条生产统计报表.xls";
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
	 * 查询刀具加工信息分页列表
	 * 
	 * @throws IOException
	 */
	@ApiOperation(value = "查询刀具加工信息分页列表 ", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "查询刀具加工信息分页列表查询成功", response = String.class) })
	@GetMapping(value = "/tool-process-export")
	@Secure()
	public String toolProcessExport(HttpServletRequest request, HttpServletResponse response, @ApiParam(name = "toolNumber", value = "物料编码", required = false) @RequestParam(required = false) String toolNumber, @ApiParam(name = "fullNumber", value = "物料条码", required = false) @RequestParam(required = false) String fullNumber, @ApiParam(name = "beginDate", value = "开始日期", required = false) @RequestParam(required = false) Date beginDate, @ApiParam(name = "endDate", value = "结束日期", required = false) @RequestParam(required = false) Date endDate) throws BusinessException, IOException {
		log.debug("ToolProcessController.toolProcessExport<<<");
		if (log.isDebugEnabled()) {
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
		ToolProcess process = new ToolProcess();
		process.setToolNumber(toolNumber);
		process.setFullNumber(fullNumber);

		// 创建HSSFWorkbook对象(excel的文档对象)
		HSSFWorkbook wb = new HSSFWorkbook();
		// 建立新的sheet对象（excel的表单）
		HSSFSheet sheet = wb.createSheet("刀条生产统计");
		// 在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
		HSSFRow row1 = sheet.createRow(0);
		row1.setHeight((short) (25 * 20));//
		// 创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个

		HSSFCellStyle cellStyle = wb.createCellStyle();
		// 设置单元格内容
		row1.createCell(0).setCellValue("物料编码");
		row1.createCell(1).setCellValue("物料名称");
		row1.createCell(2).setCellValue("物料图号");
		row1.createCell(3).setCellValue("物料条码");
		row1.createCell(4).setCellValue("使用部门");
		row1.createCell(5).setCellValue("使用班组");
		row1.createCell(6).setCellValue("使用人");
		row1.createCell(7).setCellValue("领用时间");
		row1.createCell(8).setCellValue("归还时间");
		row1.createCell(9).setCellValue("制件编号");
		row1.createCell(10).setCellValue("制件名称");
		row1.createCell(11).setCellValue("设备标签");
		row1.createCell(12).setCellValue("设备名称");
		row1.createCell(13).setCellValue("理论加工数量");
		row1.createCell(14).setCellValue("加工数量");
		row1.createCell(15).setCellValue("交回原因");
		row1.createCell(16).setCellValue("备注");

		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

		for (int i = 0; i <= 15; i++) {
			row1.getCell(i).setCellStyle(cellStyle);
		}

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
		sheet.setColumnWidth(11, 5000);
		sheet.setColumnWidth(12, 5000);
		sheet.setColumnWidth(13, 5000);
		sheet.setColumnWidth(14, 5000);
		sheet.setColumnWidth(15, 5000);
		sheet.setColumnWidth(16, 5000);
		int i = 1;
		HSSFCellStyle cellStyle1 = wb.createCellStyle();
		cellStyle1.setVerticalAlignment(VerticalAlignment.CENTER);
		Pagination<ToolProcess> pagination = toolProcessService.selectPageList(userId, process, queryDto);
		for (ToolProcess item : pagination.getRows()) {
			HSSFRow dataRow = sheet.createRow(i);
			dataRow.setHeight((short) (25 * 20));//
			dataRow.createCell(0).setCellValue(item.getToolNumber());
			dataRow.createCell(1).setCellValue(item.getToolName());
			dataRow.createCell(2).setCellValue(item.getToolMap());
			dataRow.createCell(3).setCellValue(item.getFullNumber());
			dataRow.createCell(4).setCellValue(item.getDepartmentName());
			dataRow.createCell(5).setCellValue(item.getTeamName());
			dataRow.createCell(6).setCellValue(item.getStaffName());
			dataRow.createCell(7).setCellValue(DateFormatUtils.format(item.getBeginTime(), "yyyy-MM-dd hh:MM"));
			dataRow.createCell(8).setCellValue(DateFormatUtils.format(item.getEndTime(), "yyyy-MM-dd hh:MM"));
			dataRow.createCell(9).setCellValue(item.getPartCode());
			dataRow.createCell(10).setCellValue(item.getPartName());
			dataRow.createCell(11).setCellValue(item.getTagNumber());
			dataRow.createCell(12).setCellValue(item.getEquipmentName());
			if (null != item.getTheoreticalQty()) {
				dataRow.createCell(13).setCellValue(item.getTheoreticalQty());
			}
			if (null != item.getProcessQty()) {
				dataRow.createCell(14).setCellValue(item.getProcessQty());
			}
			dataRow.createCell(15).setCellValue(1 == item.getReturnResion() ? "正常交回" : "异常交回");
			dataRow.createCell(16).setCellValue(item.getRemark());
			i++;
		}
		// 输出Excel文件
		String fileName = "生产统计报表.xls";
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
	 * 查询刀具涂层信息分页列表
	 */
	@ApiOperation(value = "查询刀具涂层加工分页列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "查询刀具涂层加工分页列表", response = ToolProcess.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/coat-process-page-list")
	public AjaxReturn coatProcessPageList(@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, //
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows, @ApiParam(name = "beginDate", value = "开始日期", required = false) @RequestParam(required = false, defaultValue = "") Date beginDate, @ApiParam(name = "endDate", value = "结束日期", required = false) @RequestParam(required = false, defaultValue = "") Date endDate, @ApiParam(name = "fullNumber", value = "物料条码", required = false) @RequestParam(required = false) String fullNumber, @ApiParam(name = "toolNumber", value = "物料编码", required = false) @RequestParam(required = false) String toolNumber,
			@ApiParam(name = "toolMap", value = "物料图号", required = false) @RequestParam(required = false) String toolMap, @ApiParam(name = "supplierId", value = "刀具编码", required = false) @RequestParam(required = false) Long supplierId) throws BusinessException {
		log.debug("ToolProcessController.coatProcessPageList<<<");
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
		ToolProcess where = new ToolProcess();
		where.setSupplierId(supplierId);
		where.setToolMap(toolMap);
		where.setFullNumber(fullNumber);
		Pagination<ToolProcess> pagination = toolProcessService.selectCoatProcessPageList(userId, where, queryDto);
		log.debug("ToolProcessController.coatProcessPageList>>>");
		return pagination;
	}

	/**
	 * 涂层供应商导出
	 * 
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "涂层供应商导出", notes = "", code = 200, produces = "application/json")
	@ApiResponses({ @ApiResponse(code = 200, message = "涂层供应商导出", response = ToolProcess.class) })
	// @Secure()
	@GetMapping(path = { "/coat-process-export" })
	@OperateLog(info = "涂层供应商导出[%s]", params = {})
	public ResponseEntity<byte[]> downloadCoatProcessExcel(@ApiParam(name = "beginDate", value = "开始日期", required = false) @RequestParam(required = false, defaultValue = "") Date beginDate, @ApiParam(name = "endDate", value = "结束日期", required = false) @RequestParam(required = false, defaultValue = "") Date endDate, @ApiParam(name = "fullNumber", value = "物料条码", required = false) @RequestParam(required = false) String fullNumber, @ApiParam(name = "toolNumber", value = "物料编码", required = false) @RequestParam(required = false) String toolNumber, @ApiParam(name = "toolMap", value = "物料图号", required = false) @RequestParam(required = false) String toolMap,
			@ApiParam(name = "supplierId", value = "刀具编码", required = false) @RequestParam(required = false) Long supplierId) throws Exception {
		Long userId = getAuthentication();
		QueryDto queryDto = new QueryDto();
		queryDto.setPage(1);
		queryDto.setRows(100000);
		queryDto.setBeginDate(beginDate);
		queryDto.setEndDate(endDate);
		ToolProcess where = new ToolProcess();
		where.setSupplierId(supplierId);
		where.setToolMap(toolMap);
		where.setFullNumber(fullNumber);
		Pagination<ToolProcess> pagination = toolProcessService.selectCoatProcessPageList(userId, where, queryDto);
		byte[] data = ExcelUtils.getInstance().exportObjects2ExcelByteArray((List<ToolProcess>) pagination.getRows(), ToolProcess.class, true, null, true);
		return getResponseEntity(data, "涂层供应商.xlsx");
	}

}
