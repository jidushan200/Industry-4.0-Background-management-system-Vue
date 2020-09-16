package org.tsinghuatj.web.controller.tool;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
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
import org.tsinghuatj.framework.web.support.annotation.Secure;
import org.tsinghuatj.support.annotation.OperateLog;
import org.tsinghuatj.tool.domain.Tool;
import org.tsinghuatj.tool.domain.ToolApplyAudit;
import org.tsinghuatj.tool.domain.ToolRepair;
import org.tsinghuatj.tool.domain.ToolScripApply;
import org.tsinghuatj.tool.service.IToolScripApplyService;
import org.tsinghuatj.tool.service.IToolService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping({ "/tool" })
public class ToolScripApplyController extends BaseController {

	private @Autowired(required = false) IToolScripApplyService toolScripApplyService;
	private @Autowired(required = false) IToolService toolService;

	/**
	 * 报废校验
	 * 
	 * @param request
	 * @return
	 * @throws BusinessException
	 */
	@ApiOperation(value = "报废校验", code = 200, produces = "application/json", notes = "登录名校验")
	@ApiResponses({ @ApiResponse(code = 200, message = "报废校验 (false-存在 true-不存在)", response = String.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/scrip-fullNumber-validate")
	@Secure() //
	public String scripfullNumberValidate(
			@ApiParam(name = "fullNumber", value = "物料条码", required = true) @RequestParam(required = true) String fullNumber)
			throws BusinessException {
		log.debug("ToolScripApplyController.fullNumberValidate<<<");
		if (log.isDebugEnabled()) {
			log.debug("fullNumber:" + fullNumber);
		}

		CustomUser user = getCompleteAuthentication();
		// 参数校验
		Validate.isTrue(StringUtils.isNotBlank(fullNumber));
		// 根据登录名查询
		ToolScripApply toolScripApply = toolScripApplyService.applyGetByfullNumber(user.getId(), fullNumber);

		log.debug("ToolScripApplyController.fullNumberValidate>>>");
		return null == toolScripApply ? "true" : "false";
	}

	/**
	 * 刀具报废表信息添加
	 */
	@ApiOperation(value = "刀具报废表信息添加", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具报废表信息添加成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/tool-scrip-apply-add")
	@OperateLog(info = "刀具报废申请添加[物料条码:%s]", params = { "fullNumber" })
	public AjaxReturn toolScripApplyAdd(
			@ApiParam(name = "toolNumber", value = "刀具编码", required = true) @RequestParam(required = true) String toolNumber,
			@ApiParam(name = "toolMap", value = "刀具图号", required = false) @RequestParam(required = false) String toolMap,
			@ApiParam(name = "fullNumber", value = "物料条码", required = false) @RequestParam(required = false) String fullNumber,
			@ApiParam(name = "warehouseCode", value = "入库编码", required = false) @RequestParam(required = false) String warehouseCode,
			@ApiParam(name = "toolName", value = "刀具名称", required = false) @RequestParam(required = false) String toolName,
			@ApiParam(name = "applierCode", value = "申请人编码", required = false) @RequestParam(required = false) String applierCode,
			@ApiParam(name = "departmentId", value = "申请部门Id", required = false) @RequestParam(required = false) Long departmentId,
			@ApiParam(name = "departmentName", value = "申请部门", required = false) @RequestParam(required = false) String departmentName,
			@ApiParam(name = "scripRemark", value = "报废原因说明", required = false) @RequestParam(required = false) String scripRemark,
			@ApiParam(name = "applyStatus", value = "报废原因说明", required = false) @RequestParam(required = false) Integer applyStatus,
			@ApiParam(name = "scripResion", value = "报废原因", required = false) @RequestParam(required = false) Integer scripResion)
			throws BusinessException {
		log.debug("ToolScripApplyController.toolPurchaseApplyAdd<<<");
		if (log.isDebugEnabled()) {
			log.debug("toolNumber:" + toolNumber);
			log.debug("toolMap:" + toolMap);
			log.debug("fullNumber:" + fullNumber);
			log.debug("warehouseCode:" + warehouseCode);
			log.debug("toolName:" + toolName);
			log.debug("departmentId:" + departmentId);
			log.debug("departmentName:" + departmentName);
			log.debug("scripResion:" + scripResion);

		}
		// 获取当前用户
		CustomUser user = getCompleteAuthentication();

		// 封装参数信息
		ToolScripApply toolScripApply = new ToolScripApply();
		toolScripApply.setToolNumber(toolNumber);
		toolScripApply.setToolMap(toolMap);
		toolScripApply.setFullNumber(fullNumber);
		toolScripApply.setWarehouseCode(warehouseCode);
		toolScripApply.setToolName(toolName);
		toolScripApply.setApplierId(user.getId());
		toolScripApply.setApplierCode(applierCode);
		toolScripApply.setApplierName(user.getRealname());
		toolScripApply.setDepartmentId(departmentId);
		toolScripApply.setDepartmentName(departmentName);
		toolScripApply.setApplyTime(new Date());
		toolScripApply.setApplyStatus(applyStatus);
		toolScripApply.setScripResion(scripResion);
		toolScripApply.setScripRemark(scripRemark);

		Tool tool = toolService.selectByFullNumber(user.getId(), fullNumber);
		if (tool != null) {
			tool.setScripApplicant(user.getRealname());
			tool.setScripApplicantTime(new Date());
			toolService.updateActiveById(user.getId(), tool, tool.getPkId(), null);
		}

		log.debug("ToolScripApplyController.toolPurchaseApplyAdd>>>");

		return new AjaxReturn(200, null, toolScripApplyService.insert(user.getId(), toolScripApply));
	}

	/**
	 * 刀具报废表信息删除
	 */
	@ApiOperation(value = "刀具报废表信息删除", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具报废表信息删除成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/tool-scrip-apply-delete-by-id")
	@OperateLog(info = "刀具报废申请删除[刀具条码:%s]", params = { "fullNumber" })
	public AjaxReturn toolScripApplyDeleteById(
			@ApiParam(name = "pkId", value = "主键", required = true) @RequestParam(required = true) Long pkId,
			@ApiParam(name = "fullNumber", value = "fullNumber", required = true) @RequestParam(required = true) String fullNumber)
			throws BusinessException {
		log.debug("ToolScripApplyController.toolPurchaseApplyDeleteById<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
		}

		// 获取当前用户
		Long userId = getAuthentication();
		// 参数校验

		Validate.isTrue(pkId > 0);

		log.debug("ToolScripApplyController.toolPurchaseApplyDeleteById>>>");
		return new AjaxReturn(200, null, toolScripApplyService.deleteById(userId, pkId));
	}

	/**
	 * 根据主键查找
	 */
	@ApiOperation(value = "刀具报废表id查询列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具报废表Id查询列表查询成功", response = ToolScripApply.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/tool-scrip-apply-get-by-id")
	public AjaxReturn toolScripApplyGetById(
			@ApiParam(name = "pkId", value = "pkId", required = true) @RequestParam(required = true) Long pkId)
			throws BusinessException {
		log.debug("ToolScripApplyController.toolPurchaseApplyGetById<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
		}

		// 获取当前用户
		Long curuserId = getAuthentication();
		// 参数校验

		Validate.isTrue(pkId >= 1);

		log.debug("ToolScripApplyController.toolPurchaseApplyGetById>>>");
		return new AjaxReturn(200, null, toolScripApplyService.selectById(curuserId, pkId));
	}

	/**
	 * 根据fullNumber查找
	 */
	@ApiOperation(value = "刀具刃磨id查询列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具刃磨Id查询列表查询成功", response = ToolRepair.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/apply-get-by-full")
	public AjaxReturn applyGetByfullNumber(
			@ApiParam(name = "fullNumber", value = "pkId", required = true) @RequestParam(required = true) String fullNumber)
			throws BusinessException {
		log.debug("ToolRepairController.applyGetByfullNumber<<<");
		if (log.isDebugEnabled()) {
			log.debug("fullNumber:" + fullNumber);
		}

		// 获取当前用户
		Long curuserId = getAuthentication();
		// 参数校验

		log.debug("ToolRepairController.applyGetByfullNumber>>>");
		return new AjaxReturn(200, null, toolScripApplyService.applyGetByfullNumber(curuserId, fullNumber));
	}

	/**
	 * 查询刀具报废表信息分页列表
	 */
	@ApiOperation(value = "查询刀具报废表信息分页列表  ", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "查询刀具报废表信息分页列表 查询成功", response = ToolScripApply.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/tool-scrip-apply-page-list")
	public AjaxReturn toolScripApplyPageList(
			@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page,
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows,
			@ApiParam(name = "fullNumber", value = "物料条码", required = false) @RequestParam(required = false) String fullNumber,
			@ApiParam(name = "warehouseCode", value = "入库编码", required = false) @RequestParam(required = false) String warehouseCode,
			@ApiParam(name = "scripResion", value = "报废原因", required = false) @RequestParam(required = false) Integer scripResion,
			@ApiParam(name = "applierId", value = "申请人", required = false) @RequestParam(required = false) Long applierId,
			@ApiParam(name = "applyStatusList", value = "报废状态列表", required = false) @RequestParam(required = false) String applyStatusList,
			@ApiParam(name = "beginDate", value = "开始日期", required = false) @RequestParam(required = false, defaultValue = "") Date beginDate,
			@ApiParam(name = "endDate", value = "结束日期", required = false) @RequestParam(required = false, defaultValue = "") Date endDate,
			@ApiParam(name = "toolNumber", value = "刀具编码", required = false) @RequestParam(required = false) String toolNumber)
			throws BusinessException {
		log.debug("ToolScripApplyController.toolPurchaseApplyPageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("page:" + page);
			log.debug("rows:" + rows);

		}

		// 获取当前用户
		CustomUser user = getCompleteAuthentication();
		// 参数校验
		Validate.isTrue(page >= 1);
		Validate.isTrue(rows <= 100);
		// 封装查询条件
		QueryDto queryDto = new QueryDto();
		queryDto.setPage(page);
		queryDto.setRows(rows);
		queryDto.setBeginDate(beginDate);
		queryDto.setEndDate(endDate);

		ToolScripApply toolScripApply = new ToolScripApply();
		toolScripApply.setToolNumber(toolNumber);
		toolScripApply.setFullNumber(fullNumber);
		toolScripApply.setWarehouseCode(warehouseCode);
		toolScripApply.setScripResion(scripResion);
		if (applierId != null) {
			toolScripApply.setApplierId(applierId);
		}

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
		toolScripApply.setStatusList(statusGroup);

		Pagination<ToolScripApply> pagination = toolScripApplyService.selectPageList(user.getId(), toolScripApply,
				queryDto);
		log.debug("ToolScripApplyController.toolPurchaseApplyPageList>>>");
		return pagination;
	}

	/**
	 * 报废报告审核
	 */
	@ApiOperation(value = "报废报告审核", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "报废报告审核", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/scrip-report-audit")
	@OperateLog(info = "刀具报废申请审核[刀具条码:%s]", params = { "fullNumber" })
	public AjaxReturn scripReportAduit(
			@ApiParam(name = "pkId", value = "pkId", required = true) @RequestParam(required = true) Long pkId,
			@ApiParam(name = "fullNumber", value = "fullNumber", required = false) @RequestParam(required = false) String fullNumber,
			@ApiParam(name = "applyStatus", value = "审核状态", required = true) @RequestParam(required = true) Integer applyStatus,
			@ApiParam(name = "auiterId", value = "审核领导Id", required = true) @RequestParam(required = true) Long auiterId,
			@ApiParam(name = "auiterName", value = "审核领导", required = true) @RequestParam(required = true) String auiterName,
			@ApiParam(name = "auitDepartmentId", value = "审核部门Id", required = true) @RequestParam(required = true) Long auitDepartmentId,
			@ApiParam(name = "auitDepartmentName", value = "审核部门", required = true) @RequestParam(required = true) String auitDepartmentName,
			@ApiParam(name = "disagreeRemark", value = "审核备注", required = false) @RequestParam(required = false) String disagreeRemark)
			throws BusinessException {
		log.debug("ToolScripApplyController.purchaseReportAduit<<<");
		if (log.isDebugEnabled()) {
			log.debug("applyId:" + pkId);
			log.debug("applyStatus:" + applyStatus);
		}
		// 获取当前用户
		CustomUser user = getCompleteAuthentication();
		Validate.isTrue(user.getId() > 0);
		// 封装参数信息
		ToolScripApply toolscripReport = toolScripApplyService.selectById(user.getId(), pkId);
		toolscripReport.setApplyStatus(applyStatus);

		ToolApplyAudit applyAudit = new ToolApplyAudit();
		applyAudit.setAuditorId(auiterId);
		applyAudit.setAuditorName(auiterName);
		applyAudit.setAuditDepartmentId(auitDepartmentId);
		applyAudit.setAuditDepartmentName(auitDepartmentName);
		applyAudit.setRemark(disagreeRemark);

		
		log.debug("ToolScripApplyController.purchaseReportAduit>>>");
		return new AjaxReturn(200, null,
				toolScripApplyService.reportAudit(user.getId(), user.getRealname(), applyAudit, toolscripReport));
	}

	/**
	 * 刀具报废表信息更新
	 */
	@ApiOperation(value = "刀具报废表信息更新", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具报废表信息更新成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/tool-scrip-apply-update")
	@OperateLog(info = "刀具报废申请更新[刀具条码:%s]", params = { "fullNumber" })
	public AjaxReturn toolScripApplyUpdate(
			@ApiParam(name = "pkId", value = "pkId", required = true) @RequestParam(required = true) Long pkId,
			@ApiParam(name = "toolNumber", value = "刀具编码", required = true) @RequestParam(required = true) String toolNumber,
			@ApiParam(name = "toolMap", value = "刀具图号", required = false) @RequestParam(required = false) String toolMap,
			@ApiParam(name = "fullNumber", value = "物料条码", required = false) @RequestParam(required = false) String fullNumber,
			@ApiParam(name = "warehouseCode", value = "入库编码", required = false) @RequestParam(required = false) String warehouseCode,
			@ApiParam(name = "toolName", value = "刀具名称", required = false) @RequestParam(required = false) String toolName,
			@ApiParam(name = "applierCode", value = "申请人编码", required = false) @RequestParam(required = false) String applierCode,
			@ApiParam(name = "departmentId", value = "申请部门Id", required = false) @RequestParam(required = false) Long departmentId,
			@ApiParam(name = "departmentName", value = "申请部门", required = false) @RequestParam(required = false) String departmentName,
			@ApiParam(name = "scripRemark", value = "报废原因说明", required = false) @RequestParam(required = false) String scripRemark,
			@ApiParam(name = "scripResion", value = "报废原因", required = false) @RequestParam(required = false) Integer scripResion,
			@ApiParam(name = "applyStatus", value = "审核状态", required = true) @RequestParam(required = true) Integer applyStatus

	) throws BusinessException {
		log.debug("ToolScripApplyController.toolScripApplyUpdate<<<");
		if (log.isDebugEnabled()) {
			log.debug("toolNumber:" + toolNumber);
			log.debug("toolMap:" + toolMap);
			log.debug("fullNumber:" + fullNumber);
			log.debug("warehouseCode:" + warehouseCode);
			log.debug("toolName:" + toolName);
			log.debug("departmentId:" + departmentId);
			log.debug("departmentName:" + departmentName);
			log.debug("scripResion:" + scripResion);

		}
		// 获取当前用户
		CustomUser user = getCompleteAuthentication();

		// 封装参数信息
		ToolScripApply toolScripApply = new ToolScripApply();
		toolScripApply.setToolNumber(toolNumber);
		toolScripApply.setToolMap(toolMap);
		toolScripApply.setFullNumber(fullNumber);
		toolScripApply.setWarehouseCode(warehouseCode);
		toolScripApply.setToolName(toolName);
		toolScripApply.setApplierId(user.getId());
		toolScripApply.setApplierCode(applierCode);
		toolScripApply.setApplierName(user.getRealname());
		toolScripApply.setDepartmentId(departmentId);
		toolScripApply.setDepartmentName(departmentName);
		toolScripApply.setApplyStatus(applyStatus);
		toolScripApply.setScripResion(scripResion);
		toolScripApply.setScripRemark(scripRemark);
		log.debug("ToolScripApplyController.toolScripApplyUpdate>>>");

		return new AjaxReturn(200, null, toolScripApplyService.updateActiveById(user.getId(), toolScripApply, pkId));
	}

}
