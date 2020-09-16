package org.tsinghuatj.web.controller.tool;

import java.io.IOException;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tsinghuatj.base.domain.Supplier;
import org.tsinghuatj.framework.domain.AjaxReturn;
import org.tsinghuatj.framework.domain.CustomUser;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.framework.web.controller.BaseController;
import org.tsinghuatj.framework.web.support.annotation.Secure;
import org.tsinghuatj.support.annotation.OperateLog;
import org.tsinghuatj.tool.domain.ToolBlade;
import org.tsinghuatj.tool.domain.ToolBladeCompose;
import org.tsinghuatj.tool.domain.ToolBladeComposeDetail;
import org.tsinghuatj.tool.domain.ToolBladeProcess;
import org.tsinghuatj.tool.domain.ToolCoat;
import org.tsinghuatj.tool.domain.ToolRepair;
import org.tsinghuatj.tool.service.IToolBladeComposeService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping({ "/tool" })
public class ToolBladeComposeController extends BaseController {
	private @Autowired(required = false) IToolBladeComposeService bladeComposeService;

	@ApiOperation(value = "查询刀头刀条组合分页列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "查询刀头刀条组合分页列表", response = ToolBladeCompose.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/blade-compose-page-list")
	public AjaxReturn bladeComposePageList(@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, //
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows, @ApiParam(name = "beginDate", value = "开始日期", required = false) @RequestParam(required = false) Date beginDate, @ApiParam(name = "endDate", value = "结束日期", required = false) @RequestParam(required = false) Date endDate, @ApiParam(name = "composeNumber", value = "刀条组合条码", required = false) @RequestParam(required = false) String composeNumber, @ApiParam(name = "headNumber", value = "刀条组合码", required = false) @RequestParam(required = false) String headNumber) throws BusinessException {
		log.debug("ToolBladeComposeController.bladeComposePageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("page:" + page);
			log.debug("rows:" + rows);
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

		ToolBladeCompose where = new ToolBladeCompose();
		where.setHeadNumber(headNumber);
		where.setComposeNumber(composeNumber);
		Pagination<ToolBladeCompose> pagination = bladeComposeService.selectPageList(curuserId, where, queryDto);
		log.debug("ToolBladeComposeController.bladeComposePageList>>>");
		return pagination;
	}

	@ApiOperation(value = "查询刀头刀条组合分页列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "查询刀头刀条组合分页列表", response = String.class) })
	@GetMapping("/blade-compose-export")
	public String bladeComposeExport(HttpServletRequest request, HttpServletResponse response, @ApiParam(name = "toolNumber", value = "刀具编码", required = false) @RequestParam(required = false) String toolNumber) throws BusinessException, IOException {
		log.debug("ToolBladeComposeController.bladeComposeExport<<<");
		if (log.isDebugEnabled()) {
			log.debug("toolNumber:" + toolNumber);
		}

		// 获取当前用户
		Long curuserId = getAuthentication();
		// 参数校验
		Validate.isTrue(curuserId > 0);

		// 封装查询条件
		QueryDto queryDto = new QueryDto();
		queryDto.setPage(1);
		queryDto.setRows(50000);
		ToolBladeCompose where = new ToolBladeCompose();

		Pagination<ToolBladeCompose> pagination = bladeComposeService.selectPageList(curuserId, where, queryDto);

		HSSFWorkbook wb = new HSSFWorkbook();
		// 建立新的sheet对象（excel的表单）
		HSSFSheet sheet = wb.createSheet("刀条生命周期");
		HSSFRow row1 = sheet.createRow(0);
		row1.setHeight((short) (25 * 20));//

		HSSFCellStyle cellStyle = wb.createCellStyle();
		// 设置单元格内容
		row1.createCell(0).setCellValue("刀条组合条码");
		row1.createCell(1).setCellValue("刀条组合码");
		row1.createCell(2).setCellValue("刀条组合名称");
		row1.createCell(3).setCellValue("刀盘编码");

		row1.createCell(4).setCellValue("刀条编码");
		row1.createCell(5).setCellValue("刀条名称");
		row1.createCell(6).setCellValue("刀条数量");
		row1.createCell(7).setCellValue("补充数量");

		row1.createCell(8).setCellValue("设备标签");
		row1.createCell(9).setCellValue("设备名称");
		row1.createCell(10).setCellValue("制件编码");
		row1.createCell(11).setCellValue("制件名称");
		row1.createCell(12).setCellValue("制件数量");

		row1.createCell(13).setCellValue("组合时间");
		row1.createCell(14).setCellValue("刀条状态");
		row1.createCell(15).setCellValue("已生产数量");

		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		sheet.setColumnWidth(0, 3000);
		sheet.setColumnWidth(1, 7000);
		sheet.setColumnWidth(2, 3000);
		sheet.setColumnWidth(3, 2000);
		sheet.setColumnWidth(4, 6000);
		sheet.setColumnWidth(5, 4000);
		sheet.setColumnWidth(6, 4000);
		sheet.setColumnWidth(7, 3000);
		sheet.setColumnWidth(8, 4000);
		sheet.setColumnWidth(9, 7000);
		sheet.setColumnWidth(11, 5000);
		sheet.setColumnWidth(13, 3000);

		int i = 1;
		int startRow;
		HSSFCellStyle cellStyle1 = wb.createCellStyle();
		cellStyle1.setVerticalAlignment(VerticalAlignment.CENTER);
		for (ToolBladeCompose item : pagination.getRows()) {

			HSSFRow dataRow = sheet.createRow(i);
			dataRow.setHeight((short) (25 * 20));//

			dataRow.createCell(0).setCellValue(item.getComposeNumber());
			dataRow.createCell(1).setCellValue(item.getHeadNumber());
			dataRow.createCell(2).setCellValue(item.getHeadName());
			dataRow.createCell(3).setCellValue(item.getPlateNumber());
			dataRow.createCell(13).setCellValue(DateFormatUtils.format(item.getCreateTime(), "yyyy-MM-dd HH:mm"));

			String statusName = "";
			int toolStatus = item.getToolStatus();
			if (toolStatus == 1) {
				statusName = "生产中";
			} else if (toolStatus == 2) {
				statusName = "待刃磨";
			} else if (toolStatus == 3) {
				statusName = "待刃磨质检";
			} else if (toolStatus == 4) {
				statusName = "待涂层";
			} else if (toolStatus == 5) {
				statusName = "待涂层质检";
			} else if (toolStatus == 6) {
				statusName = "待安装";
			} else if (toolStatus == 7) {
				statusName = "待报废";
			} else if (toolStatus == 8) {
				statusName = "已报废";
			}

			dataRow.createCell(14).setCellValue(statusName);
			if (null != item.getProductionQty()) {
				dataRow.createCell(15).setCellValue(item.getProductionQty());
			}

			int itemCount = 0, detailCount = 0, processCount = 0;

			List<ToolBladeComposeDetail> detailList = item.getDetailList();

			if (!CollectionUtils.isEmpty(detailList)) {
				detailCount = detailList.size();
			}

			List<ToolBladeProcess> processList = item.getProcessList();
			if (!CollectionUtils.isEmpty(processList)) {
				processCount = processList.size();
			}
			if (detailCount >= processCount) {
				itemCount = detailCount;
			} else {
				itemCount = processCount;
			}

			if (itemCount == 0) {
				i++;
				continue;
			}

			startRow = i;
			for (int j = 0; j < itemCount; j++) {
				if (j > 0) {
					dataRow = sheet.createRow(i);
					dataRow.setHeight((short) (25 * 20));// 目的是想把行高设置成25px
				}

				if (j < detailCount) {
					ToolBladeComposeDetail detail = detailList.get(j);
					dataRow.createCell(4).setCellValue(detail.getToolNumber());
					dataRow.createCell(5).setCellValue(detail.getToolName());
					dataRow.createCell(6).setCellValue(detail.getToolQty());
					if (null != detail.getSupplementQty()) {
						dataRow.createCell(7).setCellValue(detail.getSupplementQty());
					}
				}
				if (j < processCount) {
					ToolBladeProcess process = processList.get(j);
					dataRow.createCell(8).setCellValue(process.getTagNumber());
					dataRow.createCell(9).setCellValue(process.getEquipmentName());
					dataRow.createCell(10).setCellValue(process.getPartCode());
					dataRow.createCell(11).setCellValue(process.getPartName());
					dataRow.createCell(12).setCellValue(process.getProcessQty());
				}
				i++;
			}

			if (itemCount == 1) {
				continue;
			}

			int endRow = startRow + itemCount - 1;
			sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 0, 0));
			sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 1, 1));
			sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 2, 2));
			sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 3, 3));

			sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 13, 13));
			sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 14, 14));
			sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 15, 15));
		}
		String fileName = "刀条生产.xls";
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

	@ApiOperation(value = "查询刀条组合生命周期", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "查询刀条组合生命周期", response = ToolBlade.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/blade-compose-life-page-list")
	public AjaxReturn bladeComposeLifePageList(@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, //
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows, @ApiParam(name = "composeNumber", value = "刀盘组合条码", required = false) @RequestParam(required = false) String composeNumber, @ApiParam(name = "toolNumber", value = "刀具编码", required = false) @RequestParam(required = false) String toolNumber, @ApiParam(name = "beginDate", value = "开始日期", required = false) @RequestParam(required = false) Date beginDate, @ApiParam(name = "endDate", value = "结束日期", required = false) @RequestParam(required = false) Date endDate) throws BusinessException {
		log.debug("ToolBladeComposeController.bladeComposePageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("page:" + page);
			log.debug("rows:" + rows);
			log.debug("toolNumber:" + toolNumber);
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
		ToolBladeCompose where = new ToolBladeCompose();
		where.setComposeNumber(composeNumber);
		Pagination<ToolBladeCompose> pagination = bladeComposeService.selectLifePageList(userId, where, queryDto);
		log.debug("ToolBladeComposeController.bladeComposePageList>>>");
		return pagination;
	}

	@ApiOperation(value = "刀具基础信息查询", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具基础信息成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/blade-compose-get-by-compose-number")
	public AjaxReturn bladeComposeGetByComposeNumber(@ApiParam(name = "composeNumber", value = "组合编码", required = true) @RequestParam(required = true) String composeNumber) throws BusinessException {
		log.debug("ToolBladeComposeController.bladeComposeGetByComposeNumber<<<");
		if (log.isDebugEnabled()) {
			log.debug("composeNumber:" + composeNumber);
		}

		// 获取当前用户
		Long userId = getAuthentication();
		// 参数校验
		log.debug("ToolBladeComposeController.bladeComposeGetByComposeNumber>>>");
		return new AjaxReturn(200, null, bladeComposeService.selectByComposeNumber(userId, composeNumber));
	}

	/**
	 * 刀条组合涂层返库安装
	 */
	@ApiOperation(value = "刀条组合涂层返库安装", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀条组合涂层返库安装", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/blade-compose-install")
	@OperateLog(info = "刀条组合涂层返库安装[刀条组合条码:%s]", params = { "composeNumber" })
	public AjaxReturn bladeComposeInstall(@ApiParam(name = "pkId", value = "组合Id", required = true) @RequestParam(required = true) Long pkId, @ApiParam(name = "plateNumber", value = "刀盘编号", required = true) @RequestParam(required = true) String plateNumber) throws BusinessException {
		log.debug("ToolBladeComposeController.bladeComposeInstall<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
		}

		// 获取当前用户
		CustomUser user = getCompleteAuthentication();
		// 参数校验
		ToolBladeCompose bladeCompose = new ToolBladeCompose();
		bladeCompose.setPkId(pkId);
		bladeCompose.setToolStatus(1);
		bladeCompose.setPlateNumber(plateNumber);
		log.debug("ToolBladeComposeController.bladeComposeInstall>>>");
		return new AjaxReturn(200, null, bladeComposeService.bladeComposeInstall(user.getId(), user.getRealname(), bladeCompose));
	}

	@ApiOperation(value = "刀具生命周期导出", notes = "", code = 200, produces = "application/json")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具生命周期导出", response = Supplier.class) })
	@Secure()
	@GetMapping(path = { "/blade-export" })
	public String bladeExport(HttpServletRequest request, HttpServletResponse response, @ApiParam(name = "composeNumber", value = "刀盘组合条码", required = false) @RequestParam(required = false) String composeNumber, @ApiParam(name = "toolNumber", value = "刀具编码", required = false) @RequestParam(required = false) String toolNumber, @ApiParam(name = "beginDate", value = "开始日期", required = false) @RequestParam(required = false) Date beginDate, @ApiParam(name = "endDate", value = "结束日期", required = false) @RequestParam(required = false) Date endDate) throws Exception {
		QueryDto queryDto = new QueryDto();
		queryDto.setPage(1);
		queryDto.setRows(100000);

		ToolBladeCompose where = new ToolBladeCompose();
		where.setComposeNumber(composeNumber);

		Long userId = getAuthentication();
		Pagination<ToolBladeCompose> pagination = bladeComposeService.selectLifePageList(userId, where, queryDto);
		// 创建HSSFWorkbook对象(excel的文档对象)
		HSSFWorkbook wb = new HSSFWorkbook();
		// 建立新的sheet对象（excel的表单）
		HSSFSheet sheet = wb.createSheet("刀条生命周期");
		// 在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
		HSSFRow row1 = sheet.createRow(0);
		row1.setHeight((short) (25 * 20));//
		// 创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个

		HSSFCellStyle cellStyle = wb.createCellStyle();
		// 设置单元格内容
		row1.createCell(0).setCellValue("刀条组合条码");
		row1.createCell(1).setCellValue("刀条组合编码");
		row1.createCell(2).setCellValue("刀条组合名称");
		row1.createCell(3).setCellValue("刀条");

		row1.createCell(8).setCellValue("组合状态");
		row1.createCell(9).setCellValue("已加工数量");

		row1.createCell(10).setCellValue("加工记录");

		row1.createCell(17).setCellValue("刃磨记录");

		row1.createCell(20).setCellValue("涂层记录");

		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

		for (int i = 0; i <= 3; i++) {
			row1.getCell(i).setCellStyle(cellStyle);
		}
		row1.getCell(8).setCellStyle(cellStyle);
		row1.getCell(9).setCellStyle(cellStyle);
		row1.getCell(10).setCellStyle(cellStyle);
		row1.getCell(17).setCellStyle(cellStyle);
		row1.getCell(20).setCellStyle(cellStyle);

		row1.setHeight((short) 400);// 目的是想把行高设置成25px
		sheet.setColumnWidth(0, 5000);
		sheet.setColumnWidth(1, 4000);
		sheet.setColumnWidth(2, 5000);
		sheet.setColumnWidth(3, 6500);
		sheet.setColumnWidth(4, 4500);
		sheet.setColumnWidth(5, 5000);
		sheet.setColumnWidth(6, 3000);
		
		sheet.setColumnWidth(9, 3000);
		sheet.setColumnWidth(10, 4000);
		sheet.setColumnWidth(11, 4000);
		
		sheet.setColumnWidth(12, 4000);
		sheet.setColumnWidth(13, 5000);
		sheet.setColumnWidth(14, 4000);
		sheet.setColumnWidth(15, 6000);
		sheet.setColumnWidth(17, 4000);
		sheet.setColumnWidth(18, 4000);
		sheet.setColumnWidth(20, 4000);
		sheet.setColumnWidth(21, 4000);
		sheet.setColumnWidth(22, 7000);
		// 合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 1, 1));
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 2, 2));

		sheet.addMergedRegion(new CellRangeAddress(0, 0, 3, 7));
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 8, 8));
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 9, 9));

		sheet.addMergedRegion(new CellRangeAddress(0, 0, 10, 16));
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 17, 19));
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 20, 22));
		// 在sheet里创建第二行
		HSSFRow row2 = sheet.createRow(1);
		// 创建单元格并设置单元格内容

		row2.createCell(3).setCellValue("物料编码");
		row2.createCell(4).setCellValue("物料名称");
		row2.createCell(5).setCellValue("物料图号");
		row2.createCell(6).setCellValue("物料数量");
		row2.createCell(7).setCellValue("补充数量");

		row2.createCell(10).setCellValue("加工开始时间");
		row2.createCell(11).setCellValue("加工结束时间");
		row2.createCell(12).setCellValue("设备标签");
		row2.createCell(13).setCellValue("设备名称");
		row2.createCell(14).setCellValue("制件编码");
		row2.createCell(15).setCellValue("制件名称");
		row2.createCell(16).setCellValue("制件数量");

		row2.createCell(17).setCellValue("物料编码");
		row2.createCell(18).setCellValue("刃磨时间");
		row2.createCell(19).setCellValue("刃磨量");

		row2.createCell(20).setCellValue("物料编码");
		row2.createCell(21).setCellValue("涂层时间");
		row2.createCell(22).setCellValue("涂层厂家");

		row2.setHeight((short) (25 * 20));//

		int i = 2;
		int startRow;

		HSSFCellStyle cellStyle1 = wb.createCellStyle();
		cellStyle1.setVerticalAlignment(VerticalAlignment.CENTER);

		for (ToolBladeCompose item : pagination.getRows()) {
			int dCount = 0, pCount = 0, rCount = 0, cCount = 0, maxCount = 0;
			HSSFRow dataRow = sheet.createRow(i);
			dataRow.setHeight((short) (25 * 20));//
			startRow = i;

			List<ToolBladeComposeDetail> detailList = item.getDetailList();
			if (!CollectionUtils.isEmpty(detailList)) {
				dCount = detailList.size();
			}
			List<ToolBladeProcess> processList = item.getProcessList();
			if (!CollectionUtils.isEmpty(processList)) {
				pCount = processList.size();
			}
			List<ToolRepair> repairList = item.getRepairList();
			if (!CollectionUtils.isEmpty(repairList)) {
				rCount = repairList.size();
			}
			List<ToolCoat> coatList = item.getCoatList();
			if (!CollectionUtils.isEmpty(coatList)) {
				cCount = coatList.size();
			}
			maxCount = dCount > pCount ? dCount : pCount;
			maxCount = maxCount > rCount ? maxCount : rCount;
			maxCount = maxCount > cCount ? maxCount : cCount;

			dataRow.createCell(0).setCellValue(item.getComposeNumber());
			dataRow.createCell(1).setCellValue(item.getHeadNumber());
			dataRow.createCell(2).setCellValue(item.getHeadName());

			int toolStatus = item.getToolStatus();

			String statusName = "";
			if (toolStatus == 1) {
				statusName = "生产中";
			} else if (toolStatus == 2) {
				statusName = "待刃磨";
			} else if (toolStatus == 3) {
				statusName = "待刃磨质检";
			} else if (toolStatus == 4) {
				statusName = "待涂层";
			} else if (toolStatus == 5) {
				statusName = "待涂层质检";
			} else if (toolStatus == 6) {
				statusName = "待安装";
			} else if (toolStatus == 7) {
				statusName = "待报废";
			} else if (toolStatus == 8) {
				statusName = "已报废";
			}
			dataRow.createCell(8).setCellValue(statusName);
			if (null != item.getProductionQty()) {
				dataRow.createCell(9).setCellValue(item.getProductionQty());
			}
			if (maxCount == 0) {
				i++;
				continue;
			}

			for (int j = 0; j < maxCount; j++) {
				if (j > 0) {
					dataRow = sheet.createRow(i);
					dataRow.setHeight((short) (25 * 20));// 目的是想把行高设置成25px
				}
				if (j < dCount) {
					ToolBladeComposeDetail detail = detailList.get(j);
					dataRow.createCell(3).setCellValue(detail.getToolNumber());
					dataRow.createCell(4).setCellValue(detail.getToolName());
					dataRow.createCell(5).setCellValue(detail.getToolMap());
					dataRow.createCell(6).setCellValue(detail.getToolQty());
					if (null != detail.getSupplementQty()) {
						dataRow.createCell(7).setCellValue(detail.getSupplementQty());
					}
				}
				if (j < pCount) {
					ToolBladeProcess process = processList.get(j);
					dataRow.createCell(10).setCellValue(null == process.getBeginTime() ? "" : DateFormatUtils.format(process.getBeginTime(), "yyyy-MM-dd HH:mm"));
					dataRow.createCell(11).setCellValue(null == process.getEndTime() ? "" : DateFormatUtils.format(process.getEndTime(), "yyyy-MM-dd HH:mm"));
					dataRow.createCell(12).setCellValue(process.getTagNumber());
					dataRow.createCell(13).setCellValue(process.getEquipmentName());
					dataRow.createCell(14).setCellValue(process.getPartCode());
					dataRow.createCell(15).setCellValue(process.getPartName());
					dataRow.createCell(16).setCellValue(process.getProcessQty());
				}
				if (j < rCount) {
					ToolRepair repair = repairList.get(j);
					dataRow.createCell(17).setCellValue(repair.getToolNumber());
					dataRow.createCell(18).setCellValue(null == repair.getExecutTime() ? "" : DateFormatUtils.format(repair.getExecutTime(), "yyyy-MM-dd HH:mm"));
					dataRow.createCell(19).setCellValue(null == repair.getRepairMeasure() ? "" : repair.getRepairMeasure().toString());
				}
				if (j < cCount) {
					ToolCoat coat = coatList.get(j);
					dataRow.createCell(20).setCellValue(coat.getToolNumber());
					dataRow.createCell(21).setCellValue(null == coat.getCoatTime() ? "" : DateFormatUtils.format(coat.getCoatTime(), "yyyy-MM-dd HH:mm"));
					dataRow.createCell(22).setCellValue(coat.getCoatSupplier());
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
			sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 8, 8));
			sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 9, 9));
		}

		String fileName = "刀条生命周期.xls";
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

	@ApiOperation(value = "刀条送刃磨", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀条送刃磨", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/blade-set-repair")
	@OperateLog(info = "刀条组合刀条组合送刃磨[刀条组合条码:%s]", params = { "composeNumber" })
	public AjaxReturn bladeSetRepair(@ApiParam(name = "pkId", value = "刀条记录id", required = false) @RequestParam(required = false) Long pkId, @ApiParam(name = "composeNumber", value = "组合码", required = false) @RequestParam(required = false) String composeNumber, @ApiParam(name = "remark", value = "说明", required = false) @RequestParam(required = false) String remark) throws BusinessException {
		log.debug("ToolBladeController.bladeSetRepair<<<");
		if (log.isDebugEnabled()) {

			log.debug("composeNumber:" + composeNumber);
			log.debug("remark:" + remark);
		}
		// 获取当前用户
		CustomUser user = getCompleteAuthentication();

		// 刃磨出库
		ToolBladeCompose bladeCompose = new ToolBladeCompose();
		bladeCompose.setComposeNumber(composeNumber);
		bladeCompose.setToolStatus(2);// 待刃磨

		log.debug("ToolBladeController.bladeSetRepair>>>");
		return new AjaxReturn(200, null, bladeComposeService.setRepair(user.getId(), user.getRealname(), bladeCompose, remark));
	}

	@ApiOperation(value = "获取刀条组合号", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "获取刀条组合刀条组合号", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/create-compose-number")
	public AjaxReturn createComposeNumber(@ApiParam(name = "headNumber", value = "刀条组合编码", required = true) @RequestParam(required = true) String headNumber) throws BusinessException {
		log.debug("ToolBladeController.createComposeNumber<<<");
		if (log.isDebugEnabled()) {
			log.debug("headNumber:" + headNumber);
		}
		// 获取当前用户
		Long userId = getAuthentication();

		log.debug("ToolBladeController.createComposeNumber>>>");
		return new AjaxReturn(200, null, bladeComposeService.createComposeNumber(userId, headNumber));
	}

}
