package org.tsinghuatj.web.controller.tool;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tsinghuatj.framework.constants.SerialNumberPrefixConstants;
import org.tsinghuatj.framework.domain.AjaxReturn;
import org.tsinghuatj.framework.domain.CustomUser;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.framework.utils.StringUtils;
import org.tsinghuatj.framework.web.controller.BaseController;
import org.tsinghuatj.support.annotation.OperateLog;
import org.tsinghuatj.sys.service.ISerialNumberService;
import org.tsinghuatj.tool.domain.ToolUnqualifiedReport;
import org.tsinghuatj.tool.service.IToolUnqualifiedReportService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping({ "/tool" })
public class ToolUnqualifiedReportController extends BaseController {
	private @Autowired(required = false) IToolUnqualifiedReportService unqualifiedReportService;

	private @Autowired(required = false) ISerialNumberService serialNumberService;

	/**
	 * 刀具不合格报告
	 */
	@ApiOperation(value = "刀具不合格报告", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具不合格报告", response = AjaxReturn.class) })
	@PostMapping(path = { "/unqualified-report-get-number" })
	public AjaxReturn unqualifiedReportGetNumber() throws BusinessException {
		log.debug("ToolUnqualifiedReportController.unqualifiedReportGetNumber<<<");
		// 获取当前用户
		Long userId = getAuthentication();
		Validate.isTrue(userId > 0);
		// 封装参数信息.

		// validateUserRole(user, RoleEnum.quality.getCode());
		log.debug("ToolUnqualifiedReportController.unqualifiedReportGetNumber>>>");
		return new AjaxReturn(200, null, serialNumberService.nextSerialNo(SerialNumberPrefixConstants.UNQUALIFIED_REPORT_PREFIX));
	}

	/**
	 * 刀具不合格报告添加
	 */
	@ApiOperation(value = "刀具不合格报告", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具不合格报告", response = AjaxReturn.class) })
	@OperateLog(info = "刀具不合格报告新增[刀具条码:%s]", params = { "fullNumber" })
	@PostMapping(path = { "/unqualified-report-add" })
	public AjaxReturn unqualifiedReportAdd(@ApiParam(name = "toolId", value = "刀具Id", required = true) @RequestParam(required = true) Long toolId, @ApiParam(name = "toolNumber", value = "刀具编码", required = false) @RequestParam(required = false) String toolNumber, @ApiParam(name = "fullNumber", value = "刀具名称", required = false) @RequestParam(required = false) String fullNumber, @ApiParam(name = "reportType", value = "报告类型(1-新刀不合格;2-涂层不合格;3-刃磨不合格)", required = true) @RequestParam(required = true) Integer reportType, @ApiParam(name = "reportStatus", value = "状态(0-新增;1-提交)", required = true) @RequestParam(required = true) Integer reportStatus,
			@ApiParam(name = "reportDesc", value = "报告描述", required = true) @RequestParam(required = false) String reportDesc) throws BusinessException {
		log.debug("ToolUnqualifiedReportController.unqualifiedReportAdd<<<");
		if (log.isDebugEnabled()) {
			log.debug("toolNumber:" + toolNumber);

			log.debug("reportType:" + reportType);
		}
		// 获取当前用户
		// Long userId = getAuthentication();

		CustomUser user = getCompleteAuthentication();
		Validate.isTrue(user.getId() > 0);
		// 封装参数信息
		ToolUnqualifiedReport report = new ToolUnqualifiedReport();
		report.setToolNumber(toolNumber);
		report.setFullNumber(fullNumber);
		report.setReportType(reportType);
		report.setReportDesc(reportDesc);
		report.setReporterId(user.getId());
		report.setReporterName(user.getRealname());
		log.debug("ToolUnqualifiedReportController.unqualifiedReportAdd>>>");
		return new AjaxReturn(200, null, unqualifiedReportService.insert(user.getId(), report, reportStatus));
	}

	/**
	 * 刀具不合格报告修改
	 */
	@ApiOperation(value = "刀具不合格报告修改", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具不合格报告修改", response = AjaxReturn.class) })
	@PostMapping(path = { "/unqualified-report-update" })
	@OperateLog(info = "刀具不合格报告修改[刀具条码:%s]", params = { "fullNumber" })
	public AjaxReturn unqualifiedReportUpdate(@ApiParam(name = "pkId", value = "报告id", required = true) @RequestParam(required = true) Long pkId, @ApiParam(name = "fullNumber", value = "fullNumber", required = false) @RequestParam(required = false) String fullNumber, @ApiParam(name = "reportType", value = "申请类型(1-新刀不合格;2-刃磨不合格;3-涂层不合格)", required = true) @RequestParam(required = true) Integer reportType, @ApiParam(name = "reportStatus", value = "状态(0-新增;1-提交)", required = true) @RequestParam(required = true) Integer reportStatus, @ApiParam(name = "reportDesc", value = "申请原因描述", required = true) @RequestParam(required = false) String reportDesc) throws BusinessException {
		log.debug("ToolUnqualifiedReportController.unqualifiedReportUpdate<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
			log.debug("reportStatus:" + reportStatus);
			log.debug("reportDesc:" + reportDesc);
		}
		// 获取当前用户
		Long userId = getAuthentication();
		Validate.isTrue(userId > 0);
		// 封装参数信息
		log.debug("ToolUnqualifiedReportController.unqualifiedReportUpdate>>>");
		return new AjaxReturn(200, null, unqualifiedReportService.updateActiveById(userId, pkId, reportStatus, reportDesc));
	}

	/**
	 * 刀具不合格报告修改
	 */
	@ApiOperation(value = "刀具不合格报告修改", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具不合格报告修改", response = AjaxReturn.class) })
	@PostMapping(path = { "/unqualified-report-get-by-id" })

	public AjaxReturn unqualifiedReportGetById(@ApiParam(name = "pkId", value = "报告id", required = true) @RequestParam(required = true) Long pkId

	) throws BusinessException {

		// 获取当前用户
		Long userId = getAuthentication();
		Validate.isTrue(userId > 0);

		return new AjaxReturn(200, null, unqualifiedReportService.selectById(userId, pkId, true));
	}

	/**
	 * 刀具不合格报告列表
	 */
	@ApiOperation(value = "刀具不合格报告列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具不合格报告列表", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/unqualified-report-pagelist")
	public AjaxReturn unqualifiedReportPageList(@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, // apply_type
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows, @ApiParam(name = "reportType", value = "报告类型", required = true) @RequestParam(required = false) String fullNumber, @ApiParam(name = "toolNumber", value = "刀具编码", required = true) @RequestParam(required = false) String toolNumber, @ApiParam(name = "reportType", value = "报告类型", required = true) @RequestParam(required = true) Integer reportType, @ApiParam(name = "status", value = "状态", required = false) @RequestParam(required = false) Integer status, @ApiParam(name = "statusArray", value = "状态", required = false) @RequestParam(required = false) String statusArray,
			@ApiParam(name = "beginDate", value = "开始日期", required = false) @RequestParam(required = false, defaultValue = "") Date beginDate, @ApiParam(name = "endDate", value = "结束日期", required = false) @RequestParam(required = false, defaultValue = "") Date endDate

	// @ApiParam(name = "auditStatus", value = "审核状态", required = true)
	// @RequestParam(required = true) Integer auditStatusstatusArray
	) throws BusinessException {
		log.debug("ToolUnqualifiedReportController.toolUnqualifiedReportPageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("reportType:" + reportType);
			log.debug("beginDate:" + beginDate);
			log.debug("endDate:" + endDate);
		}
		// 获取当前用户
		Long userId = getAuthentication();
		Validate.isTrue(userId > 0);
		// 封装参数信息
		ToolUnqualifiedReport report = new ToolUnqualifiedReport();
		report.setReportType(reportType);
		report.setFullNumber(fullNumber);
		report.setToolNumber(toolNumber);
		// apply.setApplierId(userId);
		QueryDto queryDto = new QueryDto();
		queryDto.setPage(page);
		queryDto.setRows(rows);
		queryDto.setBeginDate(beginDate);
		queryDto.setEndDate(endDate);
		if (reportType == 1) {
			if (StringUtils.isNotEmpty(statusArray)) {
				String[] arr = statusArray.split(",");
				if (arr.length == 1) {
					report.setNewAuditStatus(Integer.parseInt(arr[0]));
				} else {
					List<Integer> statusList = new ArrayList<Integer>();
					for (String item : arr) {
						statusList.add(Integer.parseInt(item));
					}
					report.setStatusList(statusList);
				}
			}
		} else if (reportType == 2) {
			report.setRepairAuditStatus(status);
		} else if (reportType == 3) {
			if (StringUtils.isNotEmpty(statusArray)) {
				String[] arr = statusArray.split(",");
				if (arr.length == 1) {
					report.setCoatAuditStatus(Integer.parseInt(arr[0]));
				} else {
					List<Integer> statusList = new ArrayList<Integer>();
					for (String item : arr) {
						statusList.add(Integer.parseInt(item));
					}
					report.setStatusList(statusList);
				}
			}
		}

		Pagination<ToolUnqualifiedReport> pagination = unqualifiedReportService.selectPageList(userId, report, queryDto);
		log.debug("ToolUnqualifiedReportController.toolUnqualifiedReportPageList>>>");
		return pagination;
	}

	/**
	 * 刀具不合格报告
	 */
	@ApiOperation(value = "刀具不合格报告", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具不合格报告", response = AjaxReturn.class) })
	@OperateLog(info = "刀具不合格删除[id:%s]", params = { "pkId" })
	@PostMapping(path = { "/unqualified-report-delete-by-id" })
	public AjaxReturn unqualifiedReportDeleteById(@ApiParam(name = "pkId", value = "pkId", required = true) @RequestParam(required = true) Long pkId) throws BusinessException {
		log.debug("ToolUnqualifiedReportController.unqualifiedReportDeleteById<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
		}
		// 获取当前用户
		Long userId = getAuthentication();
		Validate.isTrue(userId > 0);
		ToolUnqualifiedReport report = unqualifiedReportService.selectById(userId, pkId, false);
		Integer status = 0;
		if (report.getReportType() == 1 && !status.equals(report.getNewAuditStatus())) {
			throw new BusinessException("report.status.do.not.match.error");
		}
		if (report.getReportType() == 2 && !status.equals(report.getRepairAuditStatus())) {
			throw new BusinessException("report.status.do.not.match.error");
		}
		if (report.getReportType() == 3 && !status.equals(report.getCoatAuditStatus())) {
			throw new BusinessException("report.status.do.not.match.error");
		}

		log.debug("ToolUnqualifiedReportController.unqualifiedReportDeleteById>>>");
		return new AjaxReturn(200, null, unqualifiedReportService.deleteById(userId, pkId));
	}

	/**
	 * 刀具不合格审核
	 */
	@ApiOperation(value = "刀具不合格审核", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具不合格审核", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/unqualified-report-audit")
	@OperateLog(info = "刀具不合格审核[刀具条码:%s->状态:%s]", params = { "fullNumber", "auditStatus" })
	public AjaxReturn unqualifiedReportAduit(
			@ApiParam(name = "pkId", value = "pkId", required = true) @RequestParam(required = true) Long pkId, 
			@ApiParam(name = "fullNumber", value = "fullNumber", required = false) @RequestParam(required = false) String fullNumber, 
			@ApiParam(name = "auditStatus", value = "审核状态", required = true) @RequestParam(required = true) Integer auditStatus, 
			@ApiParam(name = "remark", value = "审核备注", required = false) @RequestParam(required = false) String remark) throws BusinessException {
		log.debug("ToolUnqualifiedReportController.unqualifiedReportAduit<<<");
		if (log.isDebugEnabled()) {
			log.debug("applyId:" + pkId);
			log.debug("auditStatus:" + auditStatus);
			log.debug("remark:" + remark);
		}
		// 获取当前用户
		CustomUser user = getCompleteAuthentication();
		Validate.isTrue(user.getId() > 0);
		// 封装参数信息
		// ToolUnqualifiedReport apply =
		// unqualifiedApplyService.selectById(userId, applyId);
		// 新刀不合格
		ToolUnqualifiedReport report = unqualifiedReportService.selectById(user.getId(), pkId, false);
		int reportType = report.getReportType();
		if (reportType == 2) {
			report.setRepairAuditStatus(auditStatus);
		} else if (reportType == 3) {
			report.setCoatAuditStatus(auditStatus);
		} else if (reportType == 1) {
			report.setNewAuditStatus(auditStatus);
		}
		log.debug("ToolUnqualifiedReportController.unqualifiedReportAduit>>>");
		return new AjaxReturn(200, null, unqualifiedReportService.reportAudit(user.getId(), user.getRealname(), report, remark));
	}

}
