package org.tsinghuatj.tool.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.tsinghuatj.base.domain.Staff;
import org.tsinghuatj.base.domain.Supplier;
import org.tsinghuatj.base.repository.StaffDepartmentMapper;
import org.tsinghuatj.base.repository.StaffMapper;
import org.tsinghuatj.base.service.ISupplierService;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.service.impl.BaseServiceImpl;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.sys.service.ISysMessageService;
import org.tsinghuatj.tool.domain.Tool;
import org.tsinghuatj.tool.domain.ToolApplyAudit;
import org.tsinghuatj.tool.domain.ToolBase;
import org.tsinghuatj.tool.domain.ToolCheck;
import org.tsinghuatj.tool.domain.ToolPurchaseReceipt;
import org.tsinghuatj.tool.domain.ToolUnqualifiedReport;
import org.tsinghuatj.tool.domain.ToolWaitCheck;
import org.tsinghuatj.tool.domain.ToolWaitHandle;
import org.tsinghuatj.tool.repository.ToolApplyAuditMapper;
import org.tsinghuatj.tool.repository.ToolBaseMapper;
import org.tsinghuatj.tool.repository.ToolMapper;
import org.tsinghuatj.tool.repository.ToolPurchaseReceiptMapper;
import org.tsinghuatj.tool.repository.ToolUnqualifiedReportMapper;
import org.tsinghuatj.tool.repository.ToolWaitCheckMapper;
import org.tsinghuatj.tool.repository.ToolWaitHandleMapper;
import org.tsinghuatj.tool.service.IToolCheckService;
import org.tsinghuatj.tool.service.IToolOperLogService;
import org.tsinghuatj.tool.service.IToolUnqualifiedReportService;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 *
 * ToolUnqualifiedReport 表数据服务层接口实现类
 *
 */
@Service("toolUnqualifiedReportService")
public class ToolUnqualifiedReportServiceImpl extends BaseServiceImpl implements IToolUnqualifiedReportService {

	private @Resource ToolUnqualifiedReportMapper toolUnqualifiedReportMapper;
	private @Resource ToolApplyAuditMapper toolApplyAuditMapper;
	private @Resource ToolBaseMapper toolBaseMapper;
	private @Resource ToolMapper toolMapper;
	private @Resource IToolCheckService toolCheckService;
	private @Resource StaffDepartmentMapper staffdepartmentMapper;
	private @Resource StaffMapper staffMapper;
	private @Resource ISysMessageService sysMessageService;
	private @Resource ToolWaitCheckMapper toolWaitCheckMapper;
	private @Resource IToolOperLogService operLogService;
	private @Resource ISupplierService supplierService;
	private @Resource ToolPurchaseReceiptMapper purchaseReceiptMapper;
	private @Resource ToolWaitHandleMapper toolWaitHandleMapper;
	private @Resource ToolWaitCheckMapper waitCheckMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, ToolUnqualifiedReport item, Integer status) throws BusinessException {
		Date dt = new Date();
		item.setPkId(getPkId());
		item.setCreateTime(dt);
		item.setCreateUser(userId);
		item.setUpdateTime(dt);
		item.setUpdateUser(userId);
		item.setDelMark(0);
		item.setReportTime(dt);
		String type = "", authCode = "";
		String tool = "";
		if (item.getReportType() == 1) {
			item.setNewAuditStatus(status);
			type = "新刀不合格报告";
			authCode = "01060202";
			tool = "物料编号为" + item.getToolNumber();
		} else if (item.getReportType() == 2) {
			item.setRepairAuditStatus(status);
			type = "刃磨不合格报告";
			authCode = "01060203";
			tool = "刀具条码为" + item.getFullNumber();
		} else if (item.getReportType() == 3) {
			item.setCoatAuditStatus(status);
			type = "涂层不合格报告";
			authCode = "01060204";
			tool = "刀具条码为" + item.getFullNumber();
		}
		if (status == 1) {
			Staff staff = staffMapper.selectByUserId(userId);
			String message = staff.getDepartmentName() + "-" + item.getReporterName() + "于" + DateFormatUtils.format(dt, "yyyy-MM-dd HH:mm") + "提交了" + type + "质检不合格报告," + tool + ",请您及时审核";
			sysMessageService.insert(type, message, userId, staff.getStaffName(), authCode);
		}

		return toolUnqualifiedReportMapper.insert(item);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, Long pkId, Integer status, String reportDesc) throws BusinessException {
		ToolUnqualifiedReport item = toolUnqualifiedReportMapper.selectById(pkId);
		item.setUpdateTime(new Date());
		item.setUpdateUser(userId);
		item.setReportDesc(reportDesc);
		String type = "", authCode = "";
		if (item.getReportType() == 1) {
			item.setNewAuditStatus(status);
			type = "新刀质检不合格报告";
			authCode = "01060202";
		} else if (item.getReportType() == 2) {
			item.setRepairAuditStatus(status);
			type = "刃磨质检不合格报告";
			authCode = "01060203";
		} else if (item.getReportType() == 3) {
			item.setCoatAuditStatus(status);
			type = "涂层质检不合格报告";
			authCode = "01060204";
		}
		if (status == 1) {
			Staff staff = staffMapper.selectByUserId(userId);
			String message = staff.getDepartmentName() + "-" + item.getReporterName() + "于" + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm") + "提交了" + type + ",物料编码为" + item.getToolNumber() + ",请您及时审核";
			sysMessageService.insert("刀具质检不合格报告", message, userId, item.getReporterName(), authCode);
		}
		return toolUnqualifiedReportMapper.updateActiveById(item, pkId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public ToolUnqualifiedReport selectById(Long userId, Long pkId, boolean includeChcek) throws BusinessException {
		ToolUnqualifiedReport report = toolUnqualifiedReportMapper.selectById(pkId);
		if (includeChcek) {
			ToolCheck toolCheck = toolCheckService.selectById(userId, report.getCheckId());
			report.setToolCheck(toolCheck);
		}
		boolean auditor = false;
		if (report.getReportType() == 1) {
			if (!(report.getNewAuditStatus() == 0 || report.getNewAuditStatus() == -2 || report.getNewAuditStatus() == -1 || report.getNewAuditStatus() == 5)) {
				auditor = true;
			}
			/*
			 * if (0 != report.getRepairAuditStatus()) {
			 * report.setAuditList(toolApplyAuditMapper.select(pkId)); }
			 */

		} else if (report.getReportType() == 2) {
			if (!(report.getRepairAuditStatus() == 0 || report.getRepairAuditStatus() == 2 || report.getRepairAuditStatus() == 3)) {
				auditor = true;
			}
			/*
			 * if (0 != report.getRepairAuditStatus()) {
			 * report.setAuditList(toolApplyAuditMapper.select(pkId)); }
			 */
		} else if (report.getReportType() == 3) {
			if (!(report.getCoatAuditStatus() == 0 || report.getCoatAuditStatus() == -2 || report.getCoatAuditStatus() == -1 || report.getCoatAuditStatus() == 5)) {
				auditor = true;
			}
			/*
			 * if (0 != report.getCoatAuditStatus()) {
			 * report.setAuditList(toolApplyAuditMapper.select(pkId)); }
			 */
		}
		if (auditor) {
			Staff staff = staffMapper.selectByUserId(userId);
			if (null != staff) {
				report.setAuditor(staff.getDepartmentName() + "-" + staff.getStaffName());
			}
		}
		return report;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long toolUnqualifiedReportId) throws BusinessException {
		return toolUnqualifiedReportMapper.removeById(toolUnqualifiedReportId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long toolUnqualifiedReportId) throws BusinessException {
		ToolUnqualifiedReport toolUnqualifiedReport = new ToolUnqualifiedReport();
		toolUnqualifiedReport.setPkId(toolUnqualifiedReportId);
		toolUnqualifiedReport.setUpdateTime(new Date());
		toolUnqualifiedReport.setUpdateUser(userId);
		return toolUnqualifiedReportMapper.deleteById(toolUnqualifiedReport);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<ToolUnqualifiedReport> select(Long userId, ToolUnqualifiedReport toolUnqualifiedReport) throws BusinessException {
		return toolUnqualifiedReportMapper.select(toolUnqualifiedReport);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<ToolUnqualifiedReport> selectPageList(Long userId, ToolUnqualifiedReport toolUnqualifiedReport, QueryDto queryDto) throws BusinessException {
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<ToolUnqualifiedReport> page = toolUnqualifiedReportMapper.selectPageList(toolUnqualifiedReport, queryDto);
		List<String> numberList = new ArrayList<String>();
		for (ToolUnqualifiedReport item : page.getResult()) {
			numberList.add(item.getToolNumber());
		}
		if (numberList.size() > 0) {
			numberList = numberList.stream().distinct().collect(Collectors.toList());
			List<ToolBase> tbList = toolBaseMapper.selectByNumberList(numberList);
			for (ToolUnqualifiedReport item : page.getResult()) {
				for (ToolBase tb : tbList) {
					if (item.getToolNumber().equals(tb.getToolNumber())) {
						item.setToolName(tb.getToolName());
						item.setToolMap(tb.getToolMap());
						break;
					}
				}
			}
		}
		return new Pagination<ToolUnqualifiedReport>(page.getTotal(), page.getResult());
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer reportAudit(Long userId, String realName, ToolUnqualifiedReport report, String remark) throws BusinessException {
		Date date = new Date();
		report.setUpdateTime(date);
		report.setUpdateUser(userId);
		Long reportId = report.getPkId();
		toolUnqualifiedReportMapper.updateActiveById(report, reportId);
		Staff staff = staffMapper.selectByUserId(userId);
		String departName = staff.getDepartmentName();
		String auditResult = "";
		String message = "";
		String dt = DateFormatUtils.format(date, "yyyy-MM-dd HH:mm");
		String authCode = "";
		if (report.getReportType() == 2) {
			int auditStatus = report.getRepairAuditStatus();
			if (auditStatus == -2) {
				auditResult = "工艺部审核,送库房报废";
				message = departName + "-" + realName + "于" + dt + "审核了刃磨质检不合格报告,刀具条码为" + report.getFullNumber() + ",请您及时报废";
				authCode = "01060206";
				// 把刀具状态改成待报废
				Tool tool = toolMapper.selectByFullNumber(report.getFullNumber());
				tool.setScripAuditor(realName);
				tool.setScripAuditTime(date);
				tool.setIsScrip(1);
				tool.setUpdateTime(date);
				tool.setUpdateUser(userId);
				toolMapper.updateActiveById(tool, tool.getPkId());

			} else if (auditStatus == -3) {
				auditResult = "工艺部审核,刃磨部重修";
				message = departName + "-" + realName + "于" + dt + "审核了刃磨质检不合格报告,刀具条码为" + report.getFullNumber() + ",请您及时重新修磨";
				authCode = "01060206";
				// 把刀具状态改为待修磨
				Tool tool = toolMapper.selectByFullNumber(report.getFullNumber());
				tool.setToolState(3);
				tool.setUpdateUser(userId);
				tool.setUpdateTime(date);
				toolMapper.updateActiveById(tool, tool.getPkId());
			} else if (auditStatus == 2) {
				auditResult = "库房已经报废";
			} else if (auditStatus == 3) {
				auditResult = "刃磨部重修";
			}
		} else if (report.getReportType() == 3) {
			int auditStatus = report.getCoatAuditStatus();
			if (auditStatus == -2) {
				auditResult = "生产部审核,退回";
				// 把刀具状态改成待涂层质检，增加一条刃磨待检记录
				Tool tool = toolMapper.selectByFullNumber(report.getFullNumber());
				tool.setToolState(5);
				tool.setUpdateUser(userId);
				tool.setUpdateTime(date);
				toolMapper.updateActiveById(tool, tool.getPkId());

				// 涂层出库 增加待检记录
				ToolWaitCheck waitcheck = new ToolWaitCheck();
				waitcheck.setPkId(getPkId());
				waitcheck.setCreateTime(date);
				waitcheck.setCreateUser(userId);
				waitcheck.setUpdateTime(date);
				waitcheck.setUpdateUser(userId);
				waitcheck.setDelMark(0);
				waitcheck.setCheckStatus(0);
				waitcheck.setSetCheckTime(date);
				waitcheck.setCheckType(3);
				Supplier supplier = supplierService.selectById(userId, report.getSupplierId());
				waitcheck.setSupplierId(report.getSupplierId());
				waitcheck.setSupplierName(supplier.getSupplierName());
				waitcheck.setFullNumber(tool.getFullNumber());
				waitcheck.setToolNumber(tool.getToolNumber());
				// waitcheck.setToolName(toolOutbound.getToolName());
				// waitcheck.setToolMap(toolOutbound.getToolMap());
				waitcheck.setTypeId(tool.getTypeId());
				waitcheck.setToolQty(1);
				toolWaitCheckMapper.insert(waitcheck);
				operLogService.insert(userId, 7, tool.getToolNumber(), tool.getFullNumber(), "涂层厂家(" + supplier.getSupplierName() + ")", "涂层质检不合格,生产部退回重涂");

			} else if (auditStatus == 2) {
				auditResult = "生产部审核,让步使用";
				message = departName + "-" + realName + "于" + dt + "审核了涂层质检不合格报告,刀具条码为" + report.getFullNumber() + ",审核意见为让步使用,请您及时审核";
				authCode = "01060204";
			} else if (auditStatus == -3) {
				auditResult = "待生产部退回";
				message = departName + "-" + realName + "于" + dt + "审核了刃磨质检不合格报告,刀具条码为" + report.getFullNumber() + ",请您及时退回";
				authCode = "01060203";
				// 把刀具状态改成待涂层质检，增加一条刃磨待检记录
				Tool tool = toolMapper.selectByFullNumber(report.getFullNumber());
				tool.setToolState(5);
				tool.setUpdateUser(userId);
				tool.setUpdateTime(date);
				toolMapper.updateActiveById(tool, tool.getPkId());

				// 涂层出库 增加待检记录
				ToolWaitCheck waitcheck = new ToolWaitCheck();
				waitcheck.setPkId(getPkId());
				waitcheck.setCreateTime(date);
				waitcheck.setCreateUser(userId);
				waitcheck.setUpdateTime(date);
				waitcheck.setUpdateUser(userId);
				waitcheck.setDelMark(0);
				waitcheck.setCheckStatus(0);
				waitcheck.setSetCheckTime(date);
				waitcheck.setCheckType(3);
				Supplier supplier = supplierService.selectById(userId, report.getSupplierId());
				waitcheck.setSupplierId(report.getSupplierId());
				waitcheck.setSupplierName(supplier.getSupplierName());
				waitcheck.setFullNumber(tool.getFullNumber());
				waitcheck.setToolNumber(tool.getToolNumber());
				// waitcheck.setToolName(toolOutbound.getToolName());
				// waitcheck.setToolMap(toolOutbound.getToolMap());
				waitcheck.setTypeId(tool.getTypeId());
				waitcheck.setToolQty(1);
				toolWaitCheckMapper.insert(waitcheck);
				operLogService.insert(userId, 7, tool.getToolNumber(), tool.getFullNumber(), "涂层厂家(" + supplier.getSupplierName() + ")", "涂层质检不合格,生产部判断重涂");

			} else if (auditStatus == 3) {
				auditResult = "工艺部审核,让步使用";
				message = departName + "-" + realName + "于" + dt + "审核了涂层质检不合格报告,刀具条码为" + report.getFullNumber() + ",审核意见为让步使用,请您及时审核";
				authCode = "01060205";
			} else if (auditStatus == 4) {
				auditResult = "质检部领导审批开具合格单,待入库";
				// message = "质检部提交了刃磨不合格报告,编号为" + reportNumber + ",请您及时审核";
			} else if (auditStatus == 5) {
				auditResult = "已入库";
				// message = "质检部提交了刃磨不合格报告,编号为" + reportNumber + ",请您及时审核";
			}
		} else if (report.getReportType() == 1) {
			int auditStatus = report.getNewAuditStatus();
			if (auditStatus == -2) {
				auditResult = "采购部审核,退回";
			} else if (auditStatus == 2) {
				auditResult = "采购部审核,让步使用";
				message = departName + "-" + realName + "于" + dt + "审核了新刀质检不合格报告,物料编码为" + report.getToolNumber() + ",审核意见为让步使用,请您及时审核";
				authCode = "01060204";
			} else if (auditStatus == -3) {
				auditResult = "待采购部退回";
				message = departName + "-" + realName + "于" + dt + "审核了新刀质检不合格报告,物料编码为" + report.getToolNumber() + ",请您及时退回";
				authCode = "01060202";
			} else if (auditStatus == 3) {
				auditResult = "工艺部审核,让步使用";
				message = departName + "-" + realName + "于" + dt + "审核了新刀质检不合格报告,物料编码为" + report.getToolNumber() + ",审核意见为让步使用,请您及时审核";
				authCode = "01060205";
			} else if (auditStatus == 4) {
				auditResult = "质检部领导审批开具合格单,待入库";
				// message = "质检部提交了刃磨不合格报告,编号为" + reportNumber + ",请您及时审核";
				// 增加待入库新刀
				ToolWaitCheck waitCheck = waitCheckMapper.selectByUnusualReportId(reportId);
				insertWaitWhouse(userId, waitCheck);
			} else if (auditStatus == 5) {
				auditResult = "已入库";
				// message = "质检部提交了刃磨不合格报告,编号为" + reportNumber + ",请您及时审核";
			}
		}
		// 审核日志
		saveAudit(userId, staff.getDepartmentId(), staff.getDepartmentName(), realName, reportId, auditResult, remark);
		// 系统消息
		if (!"".equals(authCode)) {
			sysMessageService.insert("刀具质检不合格报告", message, userId, realName, authCode);
		}

		return 1;
	}

	// 新刀条待入库
	private void insertWaitWhouse(Long userId, ToolWaitCheck checkwait) throws BusinessException {
		Date date = new Date();
		ToolWaitHandle waitHandle = new ToolWaitHandle();
		waitHandle.setReceiptId(checkwait.getReceiptId());
		waitHandle.setPkId(getPkId());
		waitHandle.setCreateTime(date);
		waitHandle.setCreateUser(userId);
		waitHandle.setUpdateTime(date);
		waitHandle.setUpdateUser(userId);
		waitHandle.setDelMark(0);
		waitHandle.setTypeId(checkwait.getTypeId());
		waitHandle.setToolNumber(checkwait.getToolNumber());
		waitHandle.setToolQty(checkwait.getToolQty());
		waitHandle.setToolSeq(checkwait.getToolSeq());
		waitHandle.setHandleType(1);
		waitHandle.setHandleResult(0);
		waitHandle.setIsNew(1);
		waitHandle.setReceiptId(checkwait.getReceiptId());
		// 修改收货单质检状态
		ToolPurchaseReceipt purchaseReceipt = new ToolPurchaseReceipt();
		purchaseReceipt.setCheckStatus(2);
		purchaseReceipt.setPkId(checkwait.getReceiptId());
		purchaseReceipt.setUpdateTime(date);
		purchaseReceipt.setUpdateUser(userId);
		purchaseReceiptMapper.updateActiveById(purchaseReceipt, checkwait.getReceiptId());
		toolWaitHandleMapper.insert(waitHandle);
	}

	private void saveAudit(Long userId, Long departmentId, String departmentName, String realName, Long reportId, String auditResult, String remark) throws BusinessException {
		ToolApplyAudit applyAudit = new ToolApplyAudit();
		Date date = new Date();
		applyAudit.setPkId(getPkId());
		applyAudit.setCreateTime(date);
		applyAudit.setCreateUser(userId);
		applyAudit.setUpdateTime(date);
		applyAudit.setUpdateUser(userId);
		applyAudit.setDelMark(0);
		applyAudit.setApplyId(reportId);
		applyAudit.setApplyType(2);
		applyAudit.setAuditResult(auditResult);
		applyAudit.setAuditorId(userId);
		applyAudit.setAuditorName(realName);
		applyAudit.setAuditDepartmentId(departmentId);
		applyAudit.setAuditDepartmentName(departmentName);
		applyAudit.setRemark(remark);
		toolApplyAuditMapper.insert(applyAudit);
	}

}