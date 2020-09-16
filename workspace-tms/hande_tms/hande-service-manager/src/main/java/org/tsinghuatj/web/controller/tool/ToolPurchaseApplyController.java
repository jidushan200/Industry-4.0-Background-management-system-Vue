package org.tsinghuatj.web.controller.tool;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
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
import org.apache.poi.xwpf.converter.core.utils.StringUtils;
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
import org.tsinghuatj.framework.utils.JsonUtils;
import org.tsinghuatj.framework.web.controller.BaseController;
import org.tsinghuatj.support.annotation.OperateLog;
import org.tsinghuatj.tool.domain.ToolApplyAudit;
import org.tsinghuatj.tool.domain.ToolPurchaseApply;
import org.tsinghuatj.tool.domain.ToolPurchaseReceipt;
import org.tsinghuatj.tool.service.IToolPurchaseApplyService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping({ "/tool" })
public class ToolPurchaseApplyController extends BaseController {

	private @Autowired(required = false) IToolPurchaseApplyService toolPurchaseApplyService;

	/**
	 * 刀具申购表信息添加
	 */
	@ApiOperation(value = "刀具申购表信息添加", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具申购表信息添加成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/tool-purchase-apply-add")
	@OperateLog(info = "刀具申购单信息添加[物料编码:%s]", params = { "toolNumber" })
	public AjaxReturn toolPurchaseApplyAdd(@ApiParam(name = "toolNumber", value = "刀具编码", required = false) @RequestParam(required = false) String toolNumber, @ApiParam(name = "toolMap", value = "刀具图号", required = false) @RequestParam(required = false) String toolMap, @ApiParam(name = "toolName", value = "刀具名称", required = false) @RequestParam(required = false) String toolName, @ApiParam(name = "erpQty", value = "ERP库存", required = false) @RequestParam(required = false) Integer erpQty, @ApiParam(name = "applyTime", value = "需求时间", required = false) @RequestParam(required = false) Date applyTime,
			@ApiParam(name = "departmentId", value = "需求部门Id", required = false) @RequestParam(required = false) Long departmentId, @ApiParam(name = "departmentName", value = "需求部门", required = false) @RequestParam(required = false) String departmentName, @ApiParam(name = "noCheckQty", value = "ERP待检刀具", required = false) @RequestParam(required = false) Integer noCheckQty, @ApiParam(name = "purchaseType", value = "采购类型", required = false) @RequestParam(required = false) Integer purchaseType, @ApiParam(name = "keepQty", value = "库存数量", required = false) @RequestParam(required = false, defaultValue = "") Integer keepQty,
			@ApiParam(name = "needQty", value = "需求数量", required = false) @RequestParam(required = false, defaultValue = "") Integer needQty, @ApiParam(name = "purchaseResion", value = "原因描述", required = false) @RequestParam(required = false, defaultValue = "") Integer purchaseResion, @ApiParam(name = "applyStatus", value = "申请状态", required = true) @RequestParam(required = true) Integer applyStatus, @ApiParam(name = "remark", value = "备注", required = false) @RequestParam(required = false, defaultValue = "") String remark, @ApiParam(name = "partId", value = "申请状态", required = false) @RequestParam(required = false) Long partId,
			@ApiParam(name = "partName", value = "备注", required = false) @RequestParam(required = false, defaultValue = "") String partName) throws BusinessException {
		log.debug("ToolPurchaseApplyController.toolPurchaseApplyAdd<<<");
		if (log.isDebugEnabled()) {
			log.debug("purchaseType:" + purchaseType);
			log.debug("toolNumber:" + toolNumber);
			log.debug("needQty:" + needQty);
			log.debug("purchaseResion:" + purchaseResion);
			log.debug("applyStatus:" + applyStatus);
			log.debug("remark:" + remark);

		}
		// 获取当前用户
		CustomUser user = getCompleteAuthentication();

		// 封装参数信息
		ToolPurchaseApply toolPurchaseApply = new ToolPurchaseApply();
		toolPurchaseApply.setToolMap(toolMap);
		toolPurchaseApply.setToolName(toolName);
		toolPurchaseApply.setErpQty(erpQty);
		toolPurchaseApply.setApplierId(user.getId());
		toolPurchaseApply.setApplierName(user.getRealname());
		toolPurchaseApply.setApplyTime(applyTime);
		toolPurchaseApply.setDepartmentId(departmentId);
		toolPurchaseApply.setDepartmentName(departmentName);
		toolPurchaseApply.setPurchaseType(purchaseType);
		toolPurchaseApply.setToolNumber(toolNumber);
		toolPurchaseApply.setKeepQty(keepQty);
		toolPurchaseApply.setNeedQty(needQty);
		toolPurchaseApply.setNeedTime(new Date());
		toolPurchaseApply.setPurchaseResion(purchaseResion);
		toolPurchaseApply.setApplyStatus(applyStatus);
		toolPurchaseApply.setRemark(remark);
		toolPurchaseApply.setNoCheckQty(noCheckQty);
		toolPurchaseApply.setPartId(partId);
		toolPurchaseApply.setPartName(partName);

		log.debug("ToolPurchaseApplyController.toolPurchaseApplyAdd>>>");

		return new AjaxReturn(200, null, toolPurchaseApplyService.insert(user.getId(), toolPurchaseApply));
	}

	/**
	 * 刀具申购表信息删除
	 */
	@ApiOperation(value = "刀具申购表信息删除", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具申购表信息删除成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/tool-purchase-apply-delete-by-id")
	@OperateLog(info = "刀具申购单信息删除[物料编码:%s]", params = { "toolNumber" })
	public AjaxReturn toolPurchaseApplyDeleteById(@ApiParam(name = "pkId", value = "主键", required = true) @RequestParam(required = true) Long pkId, @ApiParam(name = "toolNumber", value = "刀具编码", required = false) @RequestParam(required = false) String toolNumber) throws BusinessException {
		log.debug("ToolPurchaseApplyController.toolPurchaseApplyDeleteById<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
		}

		// 获取当前用户
		Long userId = getAuthentication();
		// 参数校验
		Validate.isTrue(pkId > 0);
		log.debug("ToolPurchaseApplyController.toolPurchaseApplyDeleteById>>>");
		return new AjaxReturn(200, null, toolPurchaseApplyService.deleteById(userId, pkId));
	}

	/**
	 * 根据主键查找
	 */
	@ApiOperation(value = "刀具申购表id查询列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具申购表Id查询列表查询成功", response = ToolPurchaseApply.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/tool-purchase-apply-get-by-id")
	public AjaxReturn toolPurchaseApplyGetById(@ApiParam(name = "pkId", value = "pkId", required = true) @RequestParam(required = true) Long pkId) throws BusinessException {
		log.debug("ToolPurchaseApplyController.toolPurchaseApplyGetById<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
		}

		// 获取当前用户
		Long curuserId = getAuthentication();
		// 参数校验
		Validate.isTrue(pkId >= 1);

		log.debug("ToolPurchaseApplyController.toolPurchaseApplyGetById>>>");
		return new AjaxReturn(200, null, toolPurchaseApplyService.selectById(curuserId, pkId));
	}

	/**
	 * 查询刀具申购表信息分页列表
	 */
	@ApiOperation(value = "查询刀具申购表信息分页列表  ", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "查询刀具申购表信息分页列表 查询成功", response = ToolPurchaseApply.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/audited-apply-page-list")
	public AjaxReturn auditedApplyPageList(
			@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, 
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows, 
			@ApiParam(name = "purchaseType", value = "采购类型", required = false) @RequestParam(required = false) Integer purchaseType, 
			@ApiParam(name = "departmentId", value = "需求部门Id", required = false) @RequestParam(required = false) Long departmentId, @ApiParam(name = "auditorId", value = "审核人", required = false) @RequestParam(required = false) Long auditorId,
			@ApiParam(name = "beginDate", value = "开始日期", required = false) @RequestParam(required = false) Date beginDate, 
			@ApiParam(name = "endDate", value = "结束日期", required = false) @RequestParam(required = false) Date endDate, 
			@ApiParam(name = "toolNumber", value = "刀具编码", required = false) @RequestParam(required = false) String toolNumber, 
			@ApiParam(name = "applyStatus", value = "申请单状态", required = false) @RequestParam(required = false) Integer applyStatus
			
			) throws BusinessException {
		log.debug("ToolPurchaseApplyController.auditedApplyPageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("page:" + page);
			log.debug("rows:" + rows);
			log.debug("purchaseType:" + purchaseType);
			log.debug("toolNumber:" + toolNumber);
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
		
		ToolPurchaseApply toolPurchaseApply = new ToolPurchaseApply();
		toolPurchaseApply.setDepartmentId(departmentId);
		toolPurchaseApply.setPurchaseType(purchaseType);
		toolPurchaseApply.setToolNumber(toolNumber);
		
		if (auditorId != null) {
			toolPurchaseApply.setAuditorId(auditorId);
		}

		Pagination<ToolPurchaseApply> pagination = toolPurchaseApplyService.selectAuditedPageList(userId, toolPurchaseApply, queryDto);
		log.debug("ToolPurchaseApplyController.auditedApplyPageList>>>");
		return pagination;
	}

	/**
	 * 查询刀具申购表信息分页列表
	 */
	@ApiOperation(value = "查询刀具申购表信息分页列表  ", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "查询刀具申购表信息分页列表 查询成功", response = ToolPurchaseApply.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/tool-purchase-apply-page-list")
	public AjaxReturn toolPurchaseApplyPageList(
			@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, 
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows,
			@ApiParam(name = "purchaseType", value = "采购类型", required = false) @RequestParam(required = false) Integer purchaseType, 
			@ApiParam(name = "departmentId", value = "需求部门Id", required = false) @RequestParam(required = false) Long departmentId,
			@ApiParam(name = "applierId", value = "申请人", required = false) @RequestParam(required = false) Long applierId,
			@ApiParam(name = "applyStatus", value = "采购状态", required = false) @RequestParam(required = false) Integer applyStatus,
			@ApiParam(name = "applyStatusList", value = "采购状态列表", required = false) @RequestParam(required = false) String applyStatusList, 
			@ApiParam(name = "beginDate", value = "开始日期", required = false) @RequestParam(required = false, defaultValue = "") Date beginDate, 
			@ApiParam(name = "endDate", value = "结束日期", required = false) @RequestParam(required = false, defaultValue = "") Date endDate, 
			@ApiParam(name = "toolNumber", value = "刀具编码", required = false) @RequestParam(required = false) String toolNumber) throws BusinessException {
		log.debug("ToolPurchaseApplyController.toolPurchaseApplyPageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("page:" + page);
			log.debug("rows:" + rows);
			log.debug("purchaseType:" + purchaseType);
			log.debug("toolNumber:" + toolNumber);
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
		Integer id;
		List<Integer> statusGroup = new ArrayList<Integer>();

		// 判断状态列表是否为空，如果是空，前端访问的是刀具采购部分，未传递状态列表。如果非空，访问的为申购部分，按权限赋值，拆分并放入list便于查询
		if (applyStatusList != null && applyStatusList != "") {
			String[] arr = applyStatusList.split(",");
			for (String str : arr) {
				id = Integer.parseInt(str);
				statusGroup.add(id);
			}
		}

		ToolPurchaseApply toolPurchaseApply = new ToolPurchaseApply();
		toolPurchaseApply.setDepartmentId(departmentId);
		toolPurchaseApply.setApplyStatus(applyStatus);
		toolPurchaseApply.setPurchaseType(purchaseType);
		toolPurchaseApply.setToolNumber(toolNumber);
		toolPurchaseApply.setStatusList(statusGroup);
		toolPurchaseApply.setApplyStatus(applyStatus);
		if (applierId != null) {
			toolPurchaseApply.setApplierId(applierId);
		}

		Pagination<ToolPurchaseApply> pagination = toolPurchaseApplyService.selectPageList(userId, toolPurchaseApply, queryDto);
		log.debug("ToolPurchaseApplyController.toolPurchaseApplyPageList>>>");
		return pagination;
	}

	@ApiOperation(value = "查询刀具采购收货单  ", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "查询刀具采购收货单", response = ToolPurchaseApply.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/purchase-receipt-page-list")
	public AjaxReturn purchaseReceiptPageList(@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, @ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows, @ApiParam(name = "toolNumber", value = "刀具编码", required = false) @RequestParam(required = false) String toolNumber, @ApiParam(name = "purchaseType", value = "采购类型", required = false) @RequestParam(required = false) Integer purchaseType, @ApiParam(name = "applyStatus", value = "采购状态", required = false) @RequestParam(required = false) Integer applyStatus,
			@ApiParam(name = "beginDate", value = "开始日期", required = false) @RequestParam(required = false) Date beginDate, @ApiParam(name = "endDate", value = "结束日期", required = false) @RequestParam(required = false) Date endDate) throws BusinessException {
		log.debug("ToolPurchaseApplyController.purchaseReceiptPageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("page:" + page);
			log.debug("rows:" + rows);
			log.debug("purchaseType:" + purchaseType);
			log.debug("toolNumber:" + toolNumber);
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

		ToolPurchaseApply toolPurchaseApply = new ToolPurchaseApply();

		toolPurchaseApply.setApplyStatus(6);
		toolPurchaseApply.setPurchaseType(purchaseType);
		toolPurchaseApply.setToolNumber(toolNumber);

		Pagination<ToolPurchaseApply> pagination = toolPurchaseApplyService.selectPurchaseReceiptPageList(userId, toolPurchaseApply, queryDto);
		log.debug("ToolPurchaseApplyController.purchaseReceiptPageList>>>");
		return pagination;
	}

	@ApiOperation(value = "刀具申购单导出 ", code = 200, produces = "application/json", notes = "")
	@RequestMapping(method = RequestMethod.GET, value = "/purchase-export")
	public String purchaseExport(HttpServletRequest request, HttpServletResponse response, @ApiParam(name = "purchaseType", value = "采购类型", required = false) @RequestParam(required = false) Integer purchaseType, @ApiParam(name = "beginDate", value = "开始日期", required = false) @RequestParam(required = false) Date beginDate, @ApiParam(name = "endDate", value = "结束日期", required = false) @RequestParam(required = false) Date endDate, @ApiParam(name = "toolNumber", value = "刀具编码", required = false) @RequestParam(required = false) String toolNumber) throws BusinessException, IOException {
		log.debug("ToolPurchaseApplyController.purchaseExport<<<");
		if (log.isDebugEnabled()) {
			log.debug("purchaseType:" + purchaseType);
			log.debug("toolNumber:" + toolNumber);
		}

		// 获取当前用户
		Long userId = getAuthentication();

		// 封装查询条件
		QueryDto queryDto = new QueryDto();
		queryDto.setPage(1);
		queryDto.setRows(50000);
		queryDto.setBeginDate(beginDate);
		queryDto.setEndDate(endDate);

		ToolPurchaseApply toolPurchaseApply = new ToolPurchaseApply();

		toolPurchaseApply.setPurchaseType(purchaseType);
		toolPurchaseApply.setToolNumber(toolNumber);

		Pagination<ToolPurchaseApply> pagination = toolPurchaseApplyService.selectPageList(userId, toolPurchaseApply, queryDto);
		HSSFWorkbook wb = new HSSFWorkbook();
		// 建立新的sheet对象（excel的表单）
		HSSFSheet sheet = wb.createSheet("刀具申购");
		HSSFRow row1 = sheet.createRow(0);
		row1.setHeight((short) (25 * 20));//

		HSSFCellStyle cellStyle = wb.createCellStyle();
		// 设置单元格内容
		row1.createCell(0).setCellValue("物料编码");
		row1.createCell(1).setCellValue("物料名称");
		row1.createCell(2).setCellValue("物料图号");
		row1.createCell(3).setCellValue("物料类型");
		row1.createCell(4).setCellValue("制件");
		row1.createCell(5).setCellValue("申购部门");
		row1.createCell(6).setCellValue("申购人");
		row1.createCell(7).setCellValue("申购时间");
		row1.createCell(8).setCellValue("需求数量");
		row1.createCell(9).setCellValue("需求时间");
		row1.createCell(10).setCellValue("申购原因");
		row1.createCell(11).setCellValue("申购类型");
		row1.createCell(12).setCellValue("申请状态");
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

		int i = 1;

		HSSFCellStyle cellStyle1 = wb.createCellStyle();
		cellStyle1.setVerticalAlignment(VerticalAlignment.CENTER);
		for (ToolPurchaseApply item : pagination.getRows()) {
			HSSFRow dataRow = sheet.createRow(i);
			dataRow.setHeight((short) (25 * 20));//
			dataRow.createCell(0).setCellValue(item.getToolNumber());
			dataRow.createCell(1).setCellValue(item.getToolName());
			dataRow.createCell(2).setCellValue(item.getToolMap());
			dataRow.createCell(3).setCellValue(item.getTypeName());
			dataRow.createCell(4).setCellValue(item.getPartName());
			dataRow.createCell(5).setCellValue(item.getDepartmentName());
			dataRow.createCell(6).setCellValue(item.getApplierName());
			dataRow.createCell(7).setCellValue(DateFormatUtils.format(item.getNeedTime(), "yyyy-MM-dd HH:mm:ss"));
			dataRow.createCell(8).setCellValue(item.getNeedQty());
			dataRow.createCell(9).setCellValue(DateFormatUtils.format(item.getApplyTime(), "yyyy-MM-dd HH:mm:ss"));
			Integer purchaseResion = item.getPurchaseResion();
			String purchaseResionName = "";
			if (purchaseResion == 1) {
				purchaseResionName = "产量提升";
			} else if (purchaseResion == 2) {
				purchaseResionName = "刀具报废";
			} else if (purchaseResion == 3) {
				purchaseResionName = "产品开发";
			} else if (purchaseResion == 4) {
				purchaseResionName = "其他";
			}
			dataRow.createCell(10).setCellValue(purchaseResionName);
			dataRow.createCell(11).setCellValue(item.getPurchaseType() == 1 ? "新品开发" : "常用刀具");
			Integer applyStatus = item.getApplyStatus();
			String applyStatusName = "";
			if (applyStatus == 1) {
				applyStatusName = "待分厂领导审核";
			} else if (applyStatus == -1) {
				applyStatusName = "分厂领导未通过,已驳回";
			} else if (applyStatus == 2) {
				applyStatusName = "待工艺部审核";
			} else if (applyStatus == -2) {
				applyStatusName = "工艺部未通过,已驳回";
			} else if (applyStatus == 3) {
				applyStatusName = "待主管领导审核";
			} else if (applyStatus == -3) {
				applyStatusName = "主管领导审核未通过,已驳回";
			} else if (applyStatus == 4) {
				applyStatusName = "待采购部接收";
			} else if (applyStatus == -4) {
				applyStatusName = "采购部驳回";
			} else if (applyStatus == 5) {
				applyStatusName = "采购收货中";
			}
			dataRow.createCell(12).setCellValue(applyStatusName);
			i++;
		}
		// 输出Excel文件
		String fileName = "刀具申购.xls";
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
	 * 申购报告审核
	 */
	@ApiOperation(value = "申购报告审核", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "申购报告审核", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/purchase-report-audit")
	@OperateLog(info = "刀具申购单信息审核[物料编码:%s]", params = { "toolNumber" })
	public AjaxReturn purchaseReportAduit(@ApiParam(name = "toolNumber", value = "toolNumber", required = false) @RequestParam(required = false) String toolNumber, @ApiParam(name = "pkId", value = "pkId", required = true) @RequestParam(required = true) Long pkId, @ApiParam(name = "applyStatus", value = "审核状态", required = true) @RequestParam(required = true) Integer applyStatus, @ApiParam(name = "auitDepartmentId", value = "审核部门Id", required = true) @RequestParam(required = true) Long auitDepartmentId, @ApiParam(name = "auitDepartmentName", value = "审核部门", required = true) @RequestParam(required = true) String auitDepartmentName,
			@ApiParam(name = "auditRemark", value = "审核备注", required = false) @RequestParam(required = false) String auditRemark) throws BusinessException {
		log.debug("ToolPurchaseApplyController.purchaseReportAduit<<<");
		if (log.isDebugEnabled()) {
			log.debug("applyId:" + pkId);
			log.debug("applyStatus:" + applyStatus);
			log.debug("auditRemark:" + auditRemark);
		}
		// 获取当前用户
		CustomUser user = getCompleteAuthentication();
		Validate.isTrue(user.getId() > 0);
		// 封装参数信息
		ToolPurchaseApply purchaseReport = toolPurchaseApplyService.selectById(user.getId(), pkId);
		purchaseReport.setApplyStatus(applyStatus);

		ToolApplyAudit applyAudit = new ToolApplyAudit();
		applyAudit.setAuditorId(user.getId());
		applyAudit.setAuditorName(user.getRealname());
		applyAudit.setAuditDepartmentId(auitDepartmentId);
		applyAudit.setAuditDepartmentName(auitDepartmentName);
		applyAudit.setRemark(auditRemark);

		log.debug("ToolPurchaseApplyController.purchaseReportAduit>>>");
		return new AjaxReturn(200, null, toolPurchaseApplyService.reportAudit(user.getId(), user.getRealname(), applyAudit, purchaseReport));
	}

	/**
	 * 刀具申购表信息更新
	 */
	@ApiOperation(value = "刀具申购表信息更新", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具申购表信息更新成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/tool-purchase-apply-update")
	@OperateLog(info = "刀具申购单信息修改[物料编码:%s]", params = { "toolNumber" })
	public AjaxReturn toolPurchaseApplyUpdate(@ApiParam(name = "pkId", value = "刀具编码", required = true) @RequestParam(required = true) Long pkId, @ApiParam(name = "toolMap", value = "刀具图号", required = false) @RequestParam(required = false) String toolMap, @ApiParam(name = "toolName", value = "刀具名称", required = false) @RequestParam(required = false, defaultValue = "") String toolName, @ApiParam(name = "erpQty", value = "ERP库存", required = false) @RequestParam(required = false, defaultValue = "") Integer erpQty, @ApiParam(name = "noCheckQty", value = "ERP待检刀具", required = false) @RequestParam(required = false) Integer noCheckQty,
			@ApiParam(name = "applyTime", value = "需求时间", required = false) @RequestParam(required = false, defaultValue = "") Date applyTime, @ApiParam(name = "departmentId", value = "需求部门Id", required = false) @RequestParam(required = false, defaultValue = "") Long departmentId, @ApiParam(name = "departmentName", value = "需求部门", required = false) @RequestParam(required = false, defaultValue = "") String departmentName, @ApiParam(name = "purchaseType", value = "采购类型", required = false) @RequestParam(required = false, defaultValue = "") Integer purchaseType, @ApiParam(name = "toolNumber", value = "刀具编码", required = false) @RequestParam(required = false, defaultValue = "") String toolNumber,
			@ApiParam(name = "keepQty", value = "TMS库存", required = false) @RequestParam(required = false, defaultValue = "") Integer keepQty, @ApiParam(name = "needQty", value = "需求数量", required = false) @RequestParam(required = false, defaultValue = "") Integer needQty, @ApiParam(name = "purchaseResion", value = "原因描述", required = false) @RequestParam(required = false, defaultValue = "") Integer purchaseResion, @ApiParam(name = "applyStatus", value = "申请状态", required = false) @RequestParam(required = false, defaultValue = "") Integer applyStatus, @ApiParam(name = "arrivaledQty", value = "已到货数量", required = false) @RequestParam(required = false, defaultValue = "") Integer arrivaledQty,
			@ApiParam(name = "remark", value = "备注", required = false) @RequestParam(required = false, defaultValue = "") String remark, @ApiParam(name = "partId", value = "申请状态", required = false) @RequestParam(required = false) Long partId, @ApiParam(name = "partName", value = "备注", required = false) @RequestParam(required = false, defaultValue = "") String partName) throws BusinessException {
		log.debug("ToolPurchaseApplyController.toolPurchaseApplyUpdate<<<");
		if (log.isDebugEnabled()) {
			log.debug("purchaseType:" + purchaseType);
			log.debug("toolNumber:" + toolNumber);
			log.debug("needQty:" + needQty);
			log.debug("purchaseResion:" + purchaseResion);
			log.debug("applyStatus:" + applyStatus);
			log.debug("remark:" + remark);

		}
		// 获取当前用户
		CustomUser user = getCompleteAuthentication();

		// 封装参数信息
		ToolPurchaseApply toolPurchaseApply = new ToolPurchaseApply();
		toolPurchaseApply.setToolMap(toolMap);
		toolPurchaseApply.setToolName(toolName);
		toolPurchaseApply.setErpQty(erpQty);
		toolPurchaseApply.setNoCheckQty(noCheckQty);
		toolPurchaseApply.setApplyTime(applyTime);
		toolPurchaseApply.setDepartmentId(departmentId);
		toolPurchaseApply.setDepartmentName(departmentName);
		// toolPurchaseApply.setAuiterId(auiterId);
		// toolPurchaseApply.setAuiterName(auiterName);
		toolPurchaseApply.setPurchaseType(purchaseType);
		toolPurchaseApply.setToolNumber(toolNumber);
		toolPurchaseApply.setKeepQty(keepQty);
		toolPurchaseApply.setNeedQty(needQty);
		toolPurchaseApply.setPurchaseResion(purchaseResion);
		toolPurchaseApply.setApplyStatus(applyStatus);
		toolPurchaseApply.setRemark(remark);
		toolPurchaseApply.setPartId(partId);
		toolPurchaseApply.setPartName(partName);
		ToolPurchaseApply toolPurchase = toolPurchaseApplyService.selectById(user.getId(), pkId);
		Integer purchaseQty = toolPurchase.getArrivaledQty();
		if (purchaseQty != null && purchaseQty != 0) {
			toolPurchaseApply.setArrivaledQty(arrivaledQty + purchaseQty);
		} else {
			toolPurchaseApply.setArrivaledQty(arrivaledQty);
		}

		/*
		 * if (applyStatus == 6) { toolPurchaseApply.setDelMark(1);
		 * toolPurchaseApply.setOverTime(new Date()); }
		 */

		log.debug("ToolPurchaseApplyController.toolPurchaseApplyUpdate>>>");

		return new AjaxReturn(200, null, toolPurchaseApplyService.updateActiveById(user.getId(), toolPurchaseApply, pkId));
	}

	/**
	 * 采购到货
	 */
	@ApiOperation(value = "采购到货", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "采购到货", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/purchase-apply-receipt")
	@OperateLog(info = "采购到货[物料编码:%s]", params = { "toolNumber" })
	public AjaxReturn purchaseApplyReceipt(@ApiParam(name = "pkId", value = "申请单id", required = true) @RequestParam(required = true) Long pkId, @ApiParam(name = "toolNumber", value = "toolNumber", required = true) @RequestParam(required = true) String toolNumber,
			// @ApiParam(name = "arrivalQty", value = "本次到货数量", required = true)
			// @RequestParam(required = true) Integer arrivalQty,
			@ApiParam(name = "receiptList", value = "到货明细", required = true) @RequestParam(required = true) String receiptList) throws BusinessException {
		log.debug("ToolPurchaseApplyController.purchaseApplyReceipt<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
			// log.debug("arrivalQty:" + arrivalQty);
			log.debug("receiptList:" + receiptList);
		}
		// 获取当前用户
		Long userId = getAuthentication();

		// 封装参数信息
		List<ToolPurchaseReceipt> list = null;
		if (StringUtils.isNotEmpty(receiptList)) {
			list = JsonUtils.json2list(receiptList, ToolPurchaseReceipt.class);
		}
		log.debug("ToolPurchaseApplyController.purchaseApplyReceipt>>>");
		return new AjaxReturn(200, null, toolPurchaseApplyService.updatePurchaseReceipt(userId, pkId, null, list));
	}

	/**
	 * 采购到货
	 */
	@ApiOperation(value = "采购到货", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具申购表信息更新成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/get-purchase-apply-receipt-list")
	public AjaxReturn getPurchaseApplyReceipt(@ApiParam(name = "pkId", value = "申请单ID", required = true) @RequestParam(required = true) Long pkId) throws BusinessException {
		log.debug("ToolPurchaseApplyController.getPurchaseApplyReceipt<<<");

		// 获取当前用户
		Long userId = getAuthentication();
		// 封装参数信息
		log.debug("ToolPurchaseApplyController.getPurchaseApplyReceipt>>>");

		return new AjaxReturn(200, null, toolPurchaseApplyService.selectByApplyId(userId, pkId));
	}

}
