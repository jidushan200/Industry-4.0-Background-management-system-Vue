package org.tsinghuatj.web.controller.tool;

import java.io.IOException;
import java.io.OutputStream;
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
import org.tsinghuatj.framework.web.controller.BaseController;
import org.tsinghuatj.support.annotation.OperateLog;
import org.tsinghuatj.tool.domain.ToolCheck;
import org.tsinghuatj.tool.domain.ToolCheckResult;
import org.tsinghuatj.tool.domain.ToolWaitCheck;
import org.tsinghuatj.tool.service.IToolCheckService;
import org.tsinghuatj.tool.service.IToolWaitCheckService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping({ "/tool" })
public class ToolCheckController extends BaseController {

	private @Autowired(required = false) IToolCheckService toolCheckService;

	private @Autowired(required = false) IToolWaitCheckService toolWaitCheckService;

	/**
	 * 查询刀具质检报告分页列表
	 */
	@ApiOperation(value = "查询刀具质检报告分页列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具质检报告分页列表查询成功", response = ToolCheck.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/wait-check-page-list")
	public AjaxReturn waitCheckPageList(@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, //
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows, @ApiParam(name = "typeId", value = "物料类型", required = false) @RequestParam(required = false) Integer typeId, @ApiParam(name = "fullNumber", value = "物料条码", required = false) @RequestParam(required = false) String fullNumber, @ApiParam(name = "toolNumber", value = "刀具编码", required = false) @RequestParam(required = false) String toolNumber, @ApiParam(name = "warehouseCode", value = "入库编码", required = false) @RequestParam(required = false) String warehouseCode, @ApiParam(name = "checkType", value = "检验类型", required = false) @RequestParam(required = false) Integer checkType)
			throws BusinessException {
		log.debug("ToolCheckController.waitCheckPageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("page:" + page);
			log.debug("rows:" + rows);
			log.debug("toolNumber:" + toolNumber);
			log.debug("checkType:" + checkType);
		}

		// 获取当前用户
		// Long userId = getAuthentication();
		// 参数校验

		Validate.isTrue(page >= 0);
		Validate.isTrue(rows <= 100);
		// 封装查询条件
		QueryDto queryDto = new QueryDto();
		queryDto.setPage(page);
		queryDto.setRows(rows);

		Long userId = getAuthentication();
		ToolWaitCheck waitCheck = new ToolWaitCheck();
		waitCheck.setTypeId(typeId);
		waitCheck.setFullNumber(fullNumber);
		waitCheck.setToolNumber(toolNumber);
		waitCheck.setCheckType(checkType);
		Pagination<ToolWaitCheck> pagination = toolWaitCheckService.selectPageList(userId, waitCheck, queryDto);
		log.debug("ToolCheckController.waitCheckPageList>>>");
		return pagination;
	}

	/**
	 * 查询刀具质检报告分页列表
	 */
	@ApiOperation(value = "查询刀具质检报告分页列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具质检报告分页列表查询成功", response = ToolCheck.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/compose-wait-check-page-list")
	public AjaxReturn composeWaitCheckPageList(@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, //
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows, @ApiParam(name = "fullNumber", value = "物料条码", required = false) @RequestParam(required = false) String fullNumber, @ApiParam(name = "checkType", value = "检验类型", required = false) @RequestParam(required = false) Integer checkType) throws BusinessException {
		log.debug("ToolCheckController.composeWaitCheckPageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("page:" + page);
			log.debug("rows:" + rows);
			log.debug("fullNumber:" + fullNumber);
			log.debug("checkType:" + checkType);
		}

		// 获取当前用户
		// Long userId = getAuthentication();
		// 参数校验

		Validate.isTrue(page >= 0);
		Validate.isTrue(rows <= 100);
		// 封装查询条件
		QueryDto queryDto = new QueryDto();
		queryDto.setPage(page);
		queryDto.setRows(rows);

		Long userId = getAuthentication();
		ToolWaitCheck waitCheck = new ToolWaitCheck();
		waitCheck.setFullNumber(fullNumber);
		waitCheck.setCheckType(checkType);
		Pagination<ToolWaitCheck> pagination = toolWaitCheckService.selectComposePageList(userId, waitCheck, queryDto);
		log.debug("ToolCheckController.composeWaitCheckPageList>>>");
		return pagination;
	}

	/**
	 * 刀具质检报告添加
	 */
	@ApiOperation(value = "刀具质检报告添加", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "添加刀具质检报告成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/tool-check-add")
	@OperateLog(info = "刀具质检报告添加[刀具条码:%s->刀具编码:%s->质检类型:%s]", params = { "fullNumber", "toolNumber", "checkTypeName" })
	public AjaxReturn toolCheckAdd(@ApiParam(name = "toolId", value = "刀具Id", required = false) @RequestParam(required = false) Long toolId, @ApiParam(name = "fullNumber", value = "物料条码", required = false) @RequestParam(required = false) String fullNumber, @ApiParam(name = "toolNumber", value = "物料编码", required = true) @RequestParam(required = true) String toolNumber, @ApiParam(name = "toolSeq", value = "刀具顺序号", required = false) @RequestParam(required = false) String toolSeq, @ApiParam(name = "toolQty", value = "数量", required = false) @RequestParam(required = false) Integer toolQty, @ApiParam(name = "typeId", value = "类型ID", required = false) @RequestParam(required = false) Integer typeId,
			@ApiParam(name = "checkType", value = "质检类型", required = true) @RequestParam(required = true) Integer checkType, @ApiParam(name = "checkTypeName", value = "质检类型名称", required = false) @RequestParam(required = false) String checkTypeName, @ApiParam(name = "waitCheckId", value = "待检Id", required = false) @RequestParam(required = false) Long waitCheckId, @ApiParam(name = "supplierId", value = "供应商Id", required = false) @RequestParam(required = false) Long supplierId, @ApiParam(name = "supplierName", value = "供应商Id", required = false) @RequestParam(required = false) String supplierName, @ApiParam(name = "deliveryer", value = "送货人", required = false) @RequestParam(required = false) String deliveryer,
			@ApiParam(name = "deliveryTime", value = "送货时间", required = false) @RequestParam(required = false) Date deliveryTime, @ApiParam(name = "standardId", value = "质检标准Id", required = true) @RequestParam(required = true) Long standardId, @ApiParam(name = "checkTime", value = "质检时间", required = true) @RequestParam(required = true) Date checkTime, @ApiParam(name = "checkResult", value = "质检结果", required = true) @RequestParam(required = true) Integer checkResult, @ApiParam(name = "checkStatus", value = "质检状态", required = true) @RequestParam(required = true) Integer checkStatus, @ApiParam(name = "remark", value = "备注", required = false) @RequestParam(required = false, defaultValue = "") String remark,
			@ApiParam(name = "appendixIds", value = "附件ID", required = false) @RequestParam(required = false) String appendixIds, @ApiParam(name = "resultList", value = "检验项", required = true) @RequestParam(required = true) String resultList) throws BusinessException {
		log.debug("ToolCheckController.toolCheckAdd<<<");
		if (log.isDebugEnabled()) {
			log.debug("toolId:" + toolId);
			log.debug("fullNumber:" + fullNumber);
			log.debug("checkType:" + checkType);
			log.debug("checkTime:" + checkTime);
			log.debug("remark:" + remark);
		}
		// 获取当前用户
		CustomUser user = getCompleteAuthentication();

		// 封装参数信息
		ToolCheck toolCheck = new ToolCheck();
		toolCheck.setToolId(toolId);
		toolCheck.setFullNumber(fullNumber);
		toolCheck.setToolNumber(toolNumber);
		toolCheck.setToolSeq(toolSeq);
		toolCheck.setToolQty(toolQty);
		toolCheck.setTypeId(typeId);
		toolCheck.setCheckType(checkType);
		toolCheck.setWaitCheckId(waitCheckId);
		toolCheck.setStandardId(standardId);
		toolCheck.setSupplierId(supplierId);
		toolCheck.setSupplierName(supplierName);
		toolCheck.setDeliveryer(deliveryer);
		toolCheck.setDeliveryTime(deliveryTime);
		toolCheck.setCheckTime(checkTime);
		toolCheck.setChecker(user.getRealname());
		toolCheck.setCheckResult(checkResult);
		toolCheck.setCheckStatus(1);
		toolCheck.setRemark(remark);

		List<ToolCheckResult> results = JsonUtils.json2list(resultList, ToolCheckResult.class);
		toolCheck.setResultList(results);
		log.debug("ToolCheckController.toolCheckAdd>>>");

		return new AjaxReturn(200, null, toolCheckService.insert(user.getId(), toolCheck, appendixIds));
	}
	
	
	/**
	 * 刀条组合质检报告添加
	 */
	@ApiOperation(value = "刀条组合质检报告添加", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀条组合质检报告添加", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/compose-check-add")
	@OperateLog(info = "刀条组合质检报告添加[组合条码:%s->质检类型:%s]", params = { "fullNumber", "checkTypeName" })
	public AjaxReturn composeCheckAdd(
			@ApiParam(name = "fullNumber", value = "物料条码", required = false) @RequestParam(required = false) String fullNumber,			
			@ApiParam(name = "checkType", value = "质检类型", required = true) @RequestParam(required = true) Integer checkType, 
			@ApiParam(name = "checkTypeName", value = "质检类型名称", required = false) @RequestParam(required = false) String checkTypeName, 
			@ApiParam(name = "waitCheckId", value = "待检Id", required = false) @RequestParam(required = false) Long waitCheckId, 
			@ApiParam(name = "supplierId", value = "供应商Id", required = false) @RequestParam(required = false) Long supplierId, 					
			@ApiParam(name = "deliveryTime", value = "送货时间", required = false) @RequestParam(required = false) Date deliveryTime,
			@ApiParam(name = "standardId", value = "质检标准Id", required = true) @RequestParam(required = true) Long standardId, 
			@ApiParam(name = "checkTime", value = "质检时间", required = true) @RequestParam(required = true) Date checkTime, 
			@ApiParam(name = "checkResult", value = "质检结果", required = true) @RequestParam(required = true) Integer checkResult, 
			@ApiParam(name = "remark", value = "备注", required = false) @RequestParam(required = false) String remark,
			@ApiParam(name = "appendixIds", value = "附件ID", required = false) @RequestParam(required = false) String appendixIds, 
			@ApiParam(name = "resultList", value = "检验项", required = true) @RequestParam(required = true) String resultList) throws BusinessException {
		log.debug("ToolCheckController.composeCheckAdd<<<");
		if (log.isDebugEnabled()) {			
			log.debug("fullNumber:" + fullNumber);
			log.debug("checkType:" + checkType);
			log.debug("checkTime:" + checkTime);
			log.debug("remark:" + remark);
		}
		// 获取当前用户
		CustomUser user = getCompleteAuthentication();

		// 封装参数信息
		ToolCheck toolCheck = new ToolCheck();		
		toolCheck.setFullNumber(fullNumber);		
		toolCheck.setCheckType(checkType);
		toolCheck.setWaitCheckId(waitCheckId);
		toolCheck.setStandardId(standardId);
		toolCheck.setSupplierId(supplierId);		
		toolCheck.setDeliveryTime(deliveryTime);
		toolCheck.setCheckTime(checkTime);
		toolCheck.setChecker(user.getRealname());
		toolCheck.setCheckResult(checkResult);
		toolCheck.setCheckStatus(1);
		toolCheck.setRemark(remark);
		List<ToolCheckResult> results = JsonUtils.json2list(resultList, ToolCheckResult.class);
		toolCheck.setResultList(results);
		log.debug("ToolCheckController.composeCheckAdd>>>");

		return new AjaxReturn(200, null, toolCheckService.insert(user.getId(), toolCheck, appendixIds));
	}
	
	
	

	/**
	 * 刀具质检报告信息修改
	 */
	@ApiOperation(value = "刀具质检报告信息修改", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具质检报告信息修改成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/tool-check-update")
	@OperateLog(info = "刀具质检报告添加[刀具条码:%s->刀具编码:%s->质检类型:%s]", params = { "fullNumber", "toolNumber", "checkTypeName" })
	public AjaxReturn toolCheckUpdate(@ApiParam(name = "pkId", value = "报告id", required = true) @RequestParam(required = true) Long pkId, @ApiParam(name = "standardId", value = "质检标准Id", required = true) @RequestParam(required = true) Long standardId, @ApiParam(name = "toolId", value = "刀具Id", required = false) @RequestParam(required = false) Long toolId, @ApiParam(name = "fullNumber", value = "刀具编码", required = false) @RequestParam(required = false) String fullNumber, @ApiParam(name = "toolNumber", value = "物料编码", required = false) @RequestParam(required = false) String toolNumber, @ApiParam(name = "toolSeq", value = "刀具顺序号", required = false) @RequestParam(required = false) String toolSeq,
			@ApiParam(name = "deliveryer", value = "送货人", required = false) @RequestParam(required = false) String deliveryer, @ApiParam(name = "deliveryTime", value = "送货时间", required = false) @RequestParam(required = false) Date deliveryTime, @ApiParam(name = "supplierId", value = "供应商Id", required = false) @RequestParam(required = false) Long supplierId, @ApiParam(name = "waitCheckId", value = "待检Id", required = false) @RequestParam(required = false) Long waitCheckId, @ApiParam(name = "toolQty", value = "数量", required = false) @RequestParam(required = false) Integer toolQty, @ApiParam(name = "typeId", value = "类型ID", required = false) @RequestParam(required = false) Integer typeId,
			@ApiParam(name = "checkType", value = "质检类型", required = true) @RequestParam(required = true) Integer checkType, @ApiParam(name = "checkTime", value = "质检时间", required = true) @RequestParam(required = true) Date checkTime, @ApiParam(name = "checkResult", value = "质检结果", required = true) @RequestParam(required = true) Integer checkResult, @ApiParam(name = "checkStatus", value = "质检状态", required = true) @RequestParam(required = true) Integer checkStatus, @ApiParam(name = "remark", value = "备注", required = false) @RequestParam(required = false, defaultValue = "") String remark, @ApiParam(name = "appendixIds", value = "附件ID", required = false) @RequestParam(required = false) String appendixIds,
			@ApiParam(name = "resultList", value = "检验项", required = true) @RequestParam(required = true) String resultList) throws BusinessException {
		log.debug("ToolCheckController.toolCheckUpdate<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
			log.debug("checkTime:" + checkTime);
			log.debug("remark:" + remark);
		}
		// 获取当前用户

		// 封装参数信息
		ToolCheck toolCheck = new ToolCheck();
		CustomUser user = getCompleteAuthentication();
		toolCheck.setChecker(user.getRealname());
		toolCheck.setCheckTime(checkTime);
		toolCheck.setCheckResult(checkResult);
		toolCheck.setFullNumber(fullNumber);
		toolCheck.setToolNumber(toolNumber);
		toolCheck.setCheckTime(checkTime);
		toolCheck.setWaitCheckId(waitCheckId);
		toolCheck.setChecker(user.getRealname());
		toolCheck.setCheckResult(checkResult);
		toolCheck.setCheckStatus(checkStatus);
		toolCheck.setToolQty(toolQty);
		toolCheck.setSupplierId(supplierId);
		toolCheck.setDeliveryer(deliveryer);
		toolCheck.setDeliveryTime(deliveryTime);
		toolCheck.setRemark(remark);
		toolCheck.setTypeId(typeId);
		toolCheck.setCheckType(checkType);
		List<ToolCheckResult> results = JsonUtils.json2list(resultList, ToolCheckResult.class);
		toolCheck.setResultList(results);
		log.debug("ToolCheckController.toolCheckUpdate>>>");
		return new AjaxReturn(200, null, toolCheckService.updateActiveById(user.getId(), toolCheck, pkId, appendixIds));
	}

	/**
	 * 刀具质检报告删除
	 */
	@ApiOperation(value = "刀具质检报告删除", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具质检报告删除成功", response = AjaxReturn.class) })
	@OperateLog(info = "刀具质检报告删除[刀具条码:%s->刀具编码:%s->质检类型:%s]", params = { "fullNumber", "toolNumber", "checkTypeName" })
	@RequestMapping(method = RequestMethod.POST, value = "/tool-check-delete-by-id")
	public AjaxReturn toolCheckDeleteById(@ApiParam(name = "pkId", value = "主键", required = true) @RequestParam(required = true) Long pkId) throws BusinessException {
		log.debug("ToolCheckController.toolCheckDeleteById<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
		}

		// 获取当前用户
		Long userId = getAuthentication();
		// 参数校验

		Validate.isTrue(pkId > 0);

		log.debug("ToolCheckController.toolCheckDeleteById>>>");
		return new AjaxReturn(200, null, toolCheckService.deleteById(userId, pkId));
	}

	/**
	 * 刀具质检报告id查询
	 */
	@ApiOperation(value = "刀具质检报告id查询", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具质检报告Id查询成功", response = ToolCheck.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/tool-check-get-by-id")
	public AjaxReturn toolCheckGetById(@ApiParam(name = "pkId", value = "pkId", required = true) @RequestParam(required = true) Long pkId) throws BusinessException {
		log.debug("ToolCheckController.toolCheckGetById<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
		}
		// 获取当前用户
		Long curuserId = getAuthentication();
		// 参数校验
		Validate.isTrue(pkId >= 1);

		log.debug("ToolCheckController.toolCheckGetById>>>");
		return new AjaxReturn(200, null, toolCheckService.selectById(curuserId, pkId));
	}

	/**
	 * 刀具质检报告id查询
	 */
	@ApiOperation(value = "刀具质检报告id查询", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具质检报告Id查询成功", response = ToolCheck.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/tool-check-get-by-seq")
	public AjaxReturn toolCheckGetBySeq(@ApiParam(name = "toolNumber", value = "toolNumber", required = true) @RequestParam(required = true) String toolNumber, @ApiParam(name = "toolSeq", value = "toolSeq", required = true) @RequestParam(required = true) String toolSeq) throws BusinessException {
		log.debug("ToolCheckController.toolCheckGetBySeq<<<");
		if (log.isDebugEnabled()) {
			log.debug("toolNumber:" + toolNumber);
			log.debug("toolSeq:" + toolSeq);
		}
		// 获取当前用户
		Long curuserId = getAuthentication();
		// 参数校验

		log.debug("ToolCheckController.toolCheckGetBySeq>>>");
		return new AjaxReturn(200, null, toolCheckService.toolCheckGetBySeq(curuserId, toolNumber, toolSeq));
	}

	/**
	 * 查询刀具质检报告分页列表
	 */
	@ApiOperation(value = "查询刀具质检报告分页列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具质检报告分页列表查询成功", response = ToolCheck.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/tool-check-page-list")
	public AjaxReturn toolCheckPageList(@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, //
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows, @ApiParam(name = "typeId", value = "类型ID", required = false) @RequestParam(required = false) Integer typeId, @ApiParam(name = "toolNumber", value = "刀具编码", required = false) @RequestParam(required = false) String toolNumber, @ApiParam(name = "fullNumber", value = "刀具条码", required = false) @RequestParam(required = false) String fullNumber, @ApiParam(name = "beginDate", value = "开始日期", required = false) @RequestParam(required = false) Date beginDate, @ApiParam(name = "endDate", value = "结束日期", required = false) @RequestParam(required = false) Date endDate,
			@ApiParam(name = "checkType", value = "检验类型", required = false) @RequestParam(required = false) Integer checkType) throws BusinessException {
		log.debug("ToolCheckController.toolCheckPageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("page:" + page);
			log.debug("rows:" + rows);
			log.debug("toolNumber:" + toolNumber);
			log.debug("checkType:" + checkType);
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

		ToolCheck toolCheck = new ToolCheck();
		toolCheck.setToolNumber(toolNumber);
		toolCheck.setFullNumber(fullNumber);
		toolCheck.setCheckType(checkType);
		toolCheck.setTypeId(typeId);
		Pagination<ToolCheck> pagination = toolCheckService.selectPageList(curuserId, toolCheck, queryDto);
		log.debug("ToolCheckController.toolCheckPageList>>>");
		return pagination;
	}

	@ApiOperation(value = "质检结果导出", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "质检结果导出", response = String.class) })
	@GetMapping("/check-export")
	public String checkExport(HttpServletRequest request, HttpServletResponse response, @ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows, @ApiParam(name = "toolNumber", value = "刀具编码", required = false) @RequestParam(required = false) String toolNumber, @ApiParam(name = "fullNumber", value = "刀具条码", required = false) @RequestParam(required = false) String fullNumber, @ApiParam(name = "beginDate", value = "开始日期", required = false) @RequestParam(required = false) Date beginDate, @ApiParam(name = "endDate", value = "结束日期", required = false) @RequestParam(required = false) Date endDate,
			@ApiParam(name = "checkType", value = "检验类型", required = false) @RequestParam(required = true) Integer checkType) throws BusinessException, IOException {
		log.debug("ToolCheckController.checkExport<<<");
		if (log.isDebugEnabled()) {
			log.debug("toolNumber:" + toolNumber);
		}

		// 获取当前用户
		Long curuserId = getAuthentication();
		// 参数校验
		Validate.isTrue(curuserId > 0);

		String checkTypeName = "";
		if (checkType == 1) {
			checkTypeName = "新刀质检";
		} else if (checkType == 2) {
			checkTypeName = "刃磨质检";
		} else if (checkType == 3) {
			checkTypeName = "涂层质检";
		}
		// 封装查询条件
		QueryDto queryDto = new QueryDto();
		queryDto.setPage(1);
		queryDto.setRows(50000);
		queryDto.setBeginDate(beginDate);
		queryDto.setEndDate(endDate);

		ToolCheck toolCheck = new ToolCheck();
		toolCheck.setToolNumber(toolNumber);
		toolCheck.setFullNumber(fullNumber);
		toolCheck.setCheckType(checkType);
		Pagination<ToolCheck> pagination = toolCheckService.selectPageList(curuserId, toolCheck, queryDto);
		HSSFWorkbook wb = new HSSFWorkbook();
		// 建立新的sheet对象（excel的表单）
		HSSFSheet sheet = wb.createSheet("质检结果");
		HSSFRow row1 = sheet.createRow(0);
		row1.setHeight((short) (25 * 20));//

		HSSFCellStyle cellStyle = wb.createCellStyle();
		// 设置单元格内容
		row1.createCell(0).setCellValue("质检类型");
		row1.createCell(1).setCellValue("物料条码");
		row1.createCell(2).setCellValue("物料编码");
		row1.createCell(3).setCellValue("物料名称");
		row1.createCell(4).setCellValue("物料图号");
		row1.createCell(5).setCellValue("物料数量");
		row1.createCell(6).setCellValue("供应商");
		row1.createCell(7).setCellValue("质检人");
		row1.createCell(8).setCellValue("检验结果");
		row1.createCell(9).setCellValue("检验时间");
		row1.createCell(10).setCellValue("备注");

		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		sheet.setColumnWidth(0, 3000);
		sheet.setColumnWidth(1, 7000);
		sheet.setColumnWidth(2, 3000);
		sheet.setColumnWidth(3, 6000);
		sheet.setColumnWidth(4, 6000);
		sheet.setColumnWidth(5, 3000);
		sheet.setColumnWidth(6, 5000);
		sheet.setColumnWidth(7, 3000);
		sheet.setColumnWidth(8, 4000);
		sheet.setColumnWidth(9, 4000);
		sheet.setColumnWidth(10, 5000);
		int i = 1;
		HSSFCellStyle cellStyle1 = wb.createCellStyle();
		cellStyle1.setVerticalAlignment(VerticalAlignment.CENTER);
		for (ToolCheck item : pagination.getRows()) {

			HSSFRow dataRow = sheet.createRow(i);
			dataRow.setHeight((short) (25 * 20));
			dataRow.createCell(0).setCellValue(checkTypeName);

			if (checkType == 1) {
				dataRow.createCell(1).setCellValue(item.getToolNumber() + "-" + item.getToolSeq() + "/" + item.getToolMap());
			} else {
				dataRow.createCell(1).setCellValue(item.getFullNumber());
			}

			dataRow.createCell(2).setCellValue(item.getToolNumber());
			dataRow.createCell(3).setCellValue(item.getToolName());
			dataRow.createCell(4).setCellValue(item.getToolMap());
			dataRow.createCell(5).setCellValue(null != item.getToolQty() ? item.getToolQty() : 1);
			dataRow.createCell(6).setCellValue(item.getSupplierName());
			dataRow.createCell(7).setCellValue(item.getChecker());
			if (item.getCheckResult() == 1) {
				dataRow.createCell(8).setCellValue(item.getCheckResult() == 1 ? "合格" : "不合格");
			}
			dataRow.createCell(9).setCellValue(DateFormatUtils.format(item.getCheckTime(), "yyyy-MM-dd HH:mm:ss"));
			dataRow.createCell(10).setCellValue(item.getRemark());

			i++;
		}
		String fileName = checkTypeName + "结果.xls";
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
	 * 刀具质检报告根据刀具id查询
	 */
	@ApiOperation(value = "刀具质检报告根据刀具id查询", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具质检报告根据刀具id查询", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/check-get-by-tool-id")
	public AjaxReturn checkGetByToolId(@ApiParam(name = "toolId", value = "刀具id", required = true) @RequestParam(required = true) Long toolId, @ApiParam(name = "checkType", value = "检验类型(1-新刀检验;2-刃磨检验;3-涂层检验)", required = true) @RequestParam(required = true) Integer checkType) throws BusinessException {
		log.debug("ToolCheckController.toolCheckGetById<<<");
		if (log.isDebugEnabled()) {
			log.debug("toolId:" + toolId);
		}

		// 获取当前用户
		Long userId = getAuthentication();
		// 参数校验
		Validate.isTrue(toolId >= 1);

		log.debug("ToolCheckController.toolCheckGetById>>>");
		return new AjaxReturn(200, null, toolCheckService.selectByToolId(userId, toolId, checkType));
	}

	/**
	 * 新刀条送修磨、涂层
	 */
	@ApiOperation(value = "新刀条送修磨涂层", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "新刀条送修磨涂层", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/blade-set-handle")
	@OperateLog(info = "新刀条送修磨涂层[刀具编码:%s->供应商名称:%s]", params = { "toolNumber", "supplierName" })
	public AjaxReturn bladeSetHandle(@ApiParam(name = "checkId", value = "质检id", required = true) @RequestParam(required = true) Long checkId, @ApiParam(name = "handleType", value = "处理类型2-刃磨3-涂层", required = true) @RequestParam(required = true) Integer handleType) throws BusinessException {
		log.debug("ToolCheckController.bladeSetHandle<<<");
		if (log.isDebugEnabled()) {
			log.debug("checkId:" + checkId);
			log.debug("handleType:" + handleType);
		}
		// 获取当前用户
		Long userId = getAuthentication();

		log.debug("ToolCheckController.bladeSetHandle>>>");
		return new AjaxReturn(200, null, toolCheckService.toolBladeSetHandle(userId, checkId, handleType));
	}

}
