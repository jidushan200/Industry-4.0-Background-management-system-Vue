package org.tsinghuatj.web.controller.fixture;

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
import org.tsinghuatj.fixture.domain.FixtureScripApply;
import org.tsinghuatj.fixture.service.IFixtureScripApplyService;
import org.tsinghuatj.framework.domain.AjaxReturn;
import org.tsinghuatj.framework.domain.CustomUser;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.framework.web.controller.BaseController;
import org.tsinghuatj.support.annotation.OperateLog;
import org.tsinghuatj.tool.domain.ToolApplyAudit;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping({ "/fixture" })
public class FixtureScripApplyController extends BaseController {

	private @Autowired(required = false) IFixtureScripApplyService fixtureScripApplyService;

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
	// @Secure() //
	public String numberValidate(@ApiParam(name = "fullNumber", value = "物料条码", required = true) @RequestParam(required = true) String fullNumber) throws BusinessException {
		log.debug("FixtureScripApplyController.fullNumberValidate<<<");
		if (log.isDebugEnabled()) {
			log.debug("fullNumber:" + fullNumber);
		}

		CustomUser user = getCompleteAuthentication();
		// 参数校验
		Validate.isTrue(StringUtils.isNotBlank(fullNumber));
		// 根据登录名查询
		FixtureScripApply fixtureScripApply = fixtureScripApplyService.applyGetByfullNumber(user.getId(), fullNumber);

		log.debug("FixtureScripApplyController.fullNumberValidate>>>");
		return null == fixtureScripApply ? "true" : "false";
	}

	/**
	 * 夹具报废表信息添加
	 */
	@ApiOperation(value = "夹具报废表信息添加", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "夹具报废表信息添加成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/fixture-scrip-apply-add")
	@OperateLog(info = "夹具报废申请添加[物料条码:%s]", params = { "fullNumber" })
	public AjaxReturn fixtureScripApplyAdd(@ApiParam(name = "checkId", value = "检验单id", required = false) @RequestParam(required = false) Long checkId, @ApiParam(name = "fixtureId", value = "夹具id", required = false) @RequestParam(required = false) Long fixtureId, @ApiParam(name = "fixtureNumber", value = "夹具编码", required = true) @RequestParam(required = true) String fixtureNumber, @ApiParam(name = "fixtureMap", value = "夹具图号", required = false) @RequestParam(required = false) String fixtureMap, @ApiParam(name = "fullNumber", value = "物料条码", required = false) @RequestParam(required = false) String fullNumber,
			@ApiParam(name = "fixtureName", value = "夹具名称", required = false) @RequestParam(required = false) String fixtureName, @ApiParam(name = "departmentId", value = "申请部门Id", required = false) @RequestParam(required = false) Long departmentId, @ApiParam(name = "departmentName", value = "申请部门", required = false) @RequestParam(required = false) String departmentName, @ApiParam(name = "scripRemark", value = "报废原因说明", required = false) @RequestParam(required = false) String scripRemark, @ApiParam(name = "applyStatus", value = "申请单状态", required = false) @RequestParam(required = false) Integer applyStatus,
			@ApiParam(name = "scripResion", value = "报废原因", required = false) @RequestParam(required = false) Integer scripResion) throws BusinessException {
		log.debug("FixtureScripApplyController.fixtureScripApplyAdd<<<");
		if (log.isDebugEnabled()) {
			log.debug("fixtureNumber:" + fixtureNumber);
			log.debug("fixtureMap:" + fixtureMap);
			log.debug("fullNumber:" + fullNumber);
			log.debug("fixtureName:" + fixtureName);
			log.debug("departmentId:" + departmentId);
			log.debug("departmentName:" + departmentName);
			log.debug("scripResion:" + scripResion);

		}
		// 获取当前用户
		CustomUser user = getCompleteAuthentication();

		// 封装参数信息
		FixtureScripApply fixtureScripApply = new FixtureScripApply();
		fixtureScripApply.setFixtureId(fixtureId);
		fixtureScripApply.setFixtureNumber(fixtureNumber);
		fixtureScripApply.setFixtureMap(fixtureMap);
		fixtureScripApply.setFixtureBarcode(fullNumber);
		fixtureScripApply.setFixtureName(fixtureName);
		fixtureScripApply.setApplierId(user.getId());
		fixtureScripApply.setApplierName(user.getRealname());
		fixtureScripApply.setDepartmentId(departmentId);
		fixtureScripApply.setDepartmentName(departmentName);
		fixtureScripApply.setApplyTime(new Date());
		fixtureScripApply.setApplyStatus(applyStatus);
		fixtureScripApply.setScripResion(scripResion);
		fixtureScripApply.setScripRemark(scripRemark);
		fixtureScripApply.setCheckId(checkId);
		log.debug("FixtureScripApplyController.fixtureScripApplyAdd>>>");
		return new AjaxReturn(200, null, fixtureScripApplyService.insert(user.getId(), fixtureScripApply));
	}

	/**
	 * 夹具报废表信息删除
	 */
	@ApiOperation(value = "夹具报废表信息删除", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "夹具报废表信息删除成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/fixture-scrip-apply-delete-by-id")
	@OperateLog(info = "夹具报废申请删除[夹具条码:%s]", params = { "fixtureBarcode" })
	public AjaxReturn fixtureScripApplyDeleteById(@ApiParam(name = "pkId", value = "主键", required = true) @RequestParam(required = true) Long pkId) throws BusinessException {
		log.debug("FixtureScripApplyController.fixtureScripApplyDeleteById<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
		}

		// 获取当前用户
		Long userId = getAuthentication();
		// 参数校验

		Validate.isTrue(pkId > 0);

		log.debug("FixtureScripApplyController.fixtureScripApplyDeleteById>>>");
		return new AjaxReturn(200, null, fixtureScripApplyService.deleteById(userId, pkId));
	}

	/**
	 * 根据主键查找
	 */
	@ApiOperation(value = "夹具报废表id查询列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "夹具报废表Id查询列表查询成功", response = FixtureScripApply.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/fixture-scrip-apply-get-by-id")
	public AjaxReturn fixtureScripApplyGetById(@ApiParam(name = "pkId", value = "pkId", required = true) @RequestParam(required = true) Long pkId) throws BusinessException {
		log.debug("FixtureScripApplyController.fixtureScripApplyGetById<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
		}

		// 获取当前用户
		Long curuserId = getAuthentication();
		// 参数校验

		Validate.isTrue(pkId >= 1);

		log.debug("FixtureScripApplyController.fixtureScripApplyGetById>>>");
		return new AjaxReturn(200, null, fixtureScripApplyService.selectById(curuserId, pkId));
	}

	/**
	 * 根据fullNumber查找
	 */
	@ApiOperation(value = "夹具刃磨id查询列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "夹具刃磨Id查询列表查询成功", response = FixtureScripApply.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/apply-get-by-full")
	public AjaxReturn applyGetByfullNumber(@ApiParam(name = "fullNumber", value = "pkId", required = true) @RequestParam(required = true) String fullNumber) throws BusinessException {
		log.debug("FixtureScripApplyController.applyGetByfullNumber<<<");
		if (log.isDebugEnabled()) {
			log.debug("fullNumber:" + fullNumber);
		}

		// 获取当前用户
		Long curuserId = getAuthentication();
		// 参数校验

		log.debug("FixtureScripApplyController.applyGetByfullNumber>>>");
		return new AjaxReturn(200, null, fixtureScripApplyService.applyGetByfullNumber(curuserId, fullNumber));
	}

	/**
	 * 查询夹具报废表信息分页列表
	 */
	@ApiOperation(value = "查询夹具报废表信息分页列表  ", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "查询夹具报废表信息分页列表 查询成功", response = FixtureScripApply.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/fixture-scrip-apply-page-list")
	public AjaxReturn fixtureScripApplyPageList(
			@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, 
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows, 
			@ApiParam(name = "fixtureBarcode", value = "物料条码", required = false) @RequestParam(required = false) String fixtureBarcode, 
			@ApiParam(name = "scripResion", value = "报废原因", required = false) @RequestParam(required = false) Integer scripResion, 
			@ApiParam(name = "applierId", value = "申请人", required = false) @RequestParam(required = false) Long applierId,
			@ApiParam(name = "applyStatusList", value = "报废状态列表", required = false) @RequestParam(required = false) String applyStatusList, 
			@ApiParam(name = "beginDate", value = "开始日期", required = false) @RequestParam(required = false, defaultValue = "") Date beginDate, 
			@ApiParam(name = "endDate", value = "结束日期", required = false) @RequestParam(required = false, defaultValue = "") Date endDate, 
			@ApiParam(name = "fixtureNumber", value = "夹具编码", required = false) @RequestParam(required = false) String fixtureNumber) throws BusinessException {
		log.debug("FixtureScripApplyController.fixtureScripApplyPageList<<<");
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

		FixtureScripApply fixtureScripApply = new FixtureScripApply();
		fixtureScripApply.setFixtureNumber(fixtureNumber);
		fixtureScripApply.setFixtureBarcode(fixtureBarcode);
		fixtureScripApply.setScripResion(scripResion);
		if (applierId != null) {
			fixtureScripApply.setApplierId(applierId);
		}

		Integer id;
		List<Integer> statusGroup = new ArrayList<Integer>();
		// 判断状态列表是否为空，如果是空，前端访问的是夹具采购部分，未传递状态列表。如果非空，访问的为申购部分，按权限赋值，拆分并放入list便于查询
		if (applyStatusList != null && applyStatusList != "") {
			String[] arr = applyStatusList.split(",");
			for (String str : arr) {
				id = Integer.parseInt(str);
				statusGroup.add(id);
			}
		}
		fixtureScripApply.setStatusList(statusGroup);

		Pagination<FixtureScripApply> pagination = fixtureScripApplyService.selectPageList(user.getId(), fixtureScripApply, queryDto);
		log.debug("FixtureScripApplyController.fixtureScripApplyPageList>>>");
		return pagination;
	}

	/**
	 * 报废报告审核
	 */
	@ApiOperation(value = "报废报告审核", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "报废报告审核", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/scrip-report-audit")
	@OperateLog(info = "夹具报废申请审核[夹具条码:%s]", params = { "fixtureBarcode" })
	public AjaxReturn scripReportAduit(
			@ApiParam(name = "pkId", value = "pkId", required = true) @RequestParam(required = true) Long pkId, 
			@ApiParam(name = "applyStatus", value = "审核状态", required = true) @RequestParam(required = true) Integer applyStatus,		
			@ApiParam(name = "departmentId", value = "审核部门Id", required = true) @RequestParam(required = true) Long departmentId,
			@ApiParam(name = "departmentName", value = "审核部门", required = true) @RequestParam(required = true) String departmentName,
			@ApiParam(name = "disagreeRemark", value = "审核备注", required = false) @RequestParam(required = false) String disagreeRemark) throws BusinessException {
		log.debug("ToolScripApplyController.purchaseReportAduit<<<");
		if (log.isDebugEnabled()) {
			log.debug("applyId:" + pkId);
			log.debug("applyStatus:" + applyStatus);
		}
		// 获取当前用户
		CustomUser user = getCompleteAuthentication();
		Validate.isTrue(user.getId() > 0);
		// 封装参数信息
		FixtureScripApply fixtureScripReport = fixtureScripApplyService.selectById(user.getId(), pkId);
		fixtureScripReport.setApplyStatus(applyStatus);

		ToolApplyAudit applyAudit = new ToolApplyAudit();
		applyAudit.setAuditorId(user.getId());
		applyAudit.setAuditorName(user.getRealname());
		applyAudit.setAuditDepartmentId(departmentId);
		applyAudit.setAuditDepartmentName(departmentName);
		applyAudit.setRemark(disagreeRemark);

		// Tool tool = toolService.selectByFullNumber(user.getId(),
		// fixtureScripReport.getFixtureBarcode());
		// if(tool != null){
		// tool.setScripAuditor(auiterName);
		// tool.setScripAuditTime(new Date());
		// toolService.updateActiveById(user.getId(), tool, tool.getPkId(),
		// null);
		// }
		log.debug("ToolScripApplyController.purchaseReportAduit>>>");
		return new AjaxReturn(200, null, fixtureScripApplyService.reportAudit(user.getId(), user.getRealname(), applyAudit, fixtureScripReport));
	}

	/**
	 * 夹具报废表信息更新
	 */
	@ApiOperation(value = "夹具报废表信息更新", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "夹具报废表信息更新成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/fixture-scrip-apply-update")
	@OperateLog(info = "夹具报废申请更新[fixtureBarcode:%s]", params = { "fixtureBarcode" })
	public AjaxReturn fixtureScripApplyUpdate(@ApiParam(name = "pkId", value = "pkId", required = true) @RequestParam(required = true) Long pkId, 
			@ApiParam(name = "fixtureNumber", value = "夹具编码", required = true) @RequestParam(required = true) String fixtureNumber, 
			@ApiParam(name = "fixtureMap", value = "夹具图号", required = false) @RequestParam(required = false) String fixtureMap, 
			@ApiParam(name = "fixtureBarcode", value = "物料条码", required = false) @RequestParam(required = false) String fixtureBarcode, 
			@ApiParam(name = "fixtureName", value = "夹具名称", required = false) @RequestParam(required = false) String fixtureName,
			@ApiParam(name = "departmentId", value = "申请部门Id", required = false) @RequestParam(required = false) Long departmentId, 
			@ApiParam(name = "departmentName", value = "申请部门", required = false) @RequestParam(required = false) String departmentName, 
			@ApiParam(name = "scripRemark", value = "报废原因说明", required = false) @RequestParam(required = false) String scripRemark, 
			@ApiParam(name = "scripResion", value = "报废原因", required = false) @RequestParam(required = false) Integer scripResion,
			@ApiParam(name = "applyStatus", value = "审核状态", required = true) @RequestParam(required = true) Integer applyStatus) throws BusinessException {
		log.debug("ToolScripApplyController.toolScripApplyUpdate<<<");
		if (log.isDebugEnabled()) {
			log.debug("fixtureNumber:" + fixtureNumber);
			log.debug("fixtureMap:" + fixtureMap);
			log.debug("fixtureBarcode:" + fixtureBarcode);
			log.debug("fixtureName:" + fixtureName);
			log.debug("departmentId:" + departmentId);
			log.debug("departmentName:" + departmentName);
			log.debug("scripResion:" + scripResion);

		}
		// 获取当前用户
		CustomUser user = getCompleteAuthentication();

		// 封装参数信息
		FixtureScripApply fixtureScripApply = new FixtureScripApply();
		fixtureScripApply.setFixtureNumber(fixtureNumber);
		fixtureScripApply.setFixtureMap(fixtureMap);
		fixtureScripApply.setFixtureBarcode(fixtureBarcode);
		fixtureScripApply.setFixtureName(fixtureName);
		fixtureScripApply.setApplierId(user.getId());	
		fixtureScripApply.setApplierName(user.getRealname());
		fixtureScripApply.setDepartmentId(departmentId);
		fixtureScripApply.setDepartmentName(departmentName);
		fixtureScripApply.setApplyStatus(applyStatus);
		fixtureScripApply.setScripResion(scripResion);
		fixtureScripApply.setScripRemark(scripRemark);
		log.debug("ToolScripApplyController.toolScripApplyUpdate>>>");

		return new AjaxReturn(200, null, fixtureScripApplyService.updateActiveById(user.getId(), fixtureScripApply, pkId));
	}
}
