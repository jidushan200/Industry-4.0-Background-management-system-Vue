package org.tsinghuatj.tool.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.service.impl.BaseServiceImpl;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.sys.repository.SysMessageMapper;
import org.tsinghuatj.sys.repository.SysUserAuthMapper;
import org.tsinghuatj.sys.service.ISysMessageService;
import org.tsinghuatj.tool.domain.Tool;
import org.tsinghuatj.tool.domain.ToolApplyAudit;
import org.tsinghuatj.tool.domain.ToolScripApply;
import org.tsinghuatj.tool.domain.ToolUnqualifiedReport;
import org.tsinghuatj.tool.repository.ToolApplyAuditMapper;
import org.tsinghuatj.tool.repository.ToolScripApplyMapper;
import org.tsinghuatj.tool.repository.ToolUnqualifiedReportMapper;
import org.tsinghuatj.tool.service.IToolOperLogService;
import org.tsinghuatj.tool.service.IToolScripApplyService;
import org.tsinghuatj.tool.service.IToolService;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 *
 * ToolScripApply 表数据服务层接口实现类
 *
 */
@Service("toolScripApplyService")
public class ToolScripApplyServiceImpl extends BaseServiceImpl implements IToolScripApplyService {

	private @Resource ToolScripApplyMapper toolScripApplyMapper;
	private @Resource ToolApplyAuditMapper toolApplyAuditMapper;
	private @Resource ISysMessageService sysMessageService;
	private @Resource SysMessageMapper sysMessageMapper;
	private @Resource SysUserAuthMapper sysUserAuthMapper;
	private @Resource IToolOperLogService operLogService;
	private @Resource ToolUnqualifiedReportMapper toolUnqualifiedReportMapper;
	private @Resource IToolService toolService;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, ToolScripApply toolScripApply) throws BusinessException {
		toolScripApply.setPkId(getPkId());
		toolScripApply.setCreateTime(new Date());
		toolScripApply.setCreateUser(userId);
		toolScripApply.setUpdateTime(new Date());
		toolScripApply.setUpdateUser(userId);
		toolScripApply.setDelMark(0);

		String message = "";
		String authCode = "";
		String applier = toolScripApply.getApplierName();
		if (toolScripApply.getApplyStatus() == 1) {
			message = "收到物料条码为" + toolScripApply.getFullNumber() + "的报废申请,请您及时处理";
			authCode = "01040302";
			operLogService.insert(userId, 10, toolScripApply.getToolNumber(), toolScripApply.getFullNumber(), "申请人:" + toolScripApply.getApplierName(), toolScripApply.getScripRemark());
		}
		// 系统消息
		sysMessageService.insert("刀具报废申请", message, userId, applier, authCode);
		return toolScripApplyMapper.insert(toolScripApply);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, ToolScripApply toolScripApply, Long toolScripApplyId) throws BusinessException {
		toolScripApply.setUpdateTime(new Date());
		toolScripApply.setUpdateUser(userId);

		String message = "";
		String authCode = "";
		String applier = toolScripApply.getApplierName();
		if (toolScripApply.getApplyStatus() == 1) {
			message = "收到物料条码为" + toolScripApply.getFullNumber() + "的报废申请,请您及时处理";
			authCode = "01040302";
			operLogService.insert(userId, 10, toolScripApply.getToolNumber(), toolScripApply.getFullNumber(), "申请人:" + toolScripApply.getApplierName(), toolScripApply.getScripRemark());

		}
		// 系统消息
		sysMessageService.insert("刀具报废申请", message, userId, applier, authCode);
		return toolScripApplyMapper.updateActiveById(toolScripApply, toolScripApplyId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public ToolScripApply selectById(Long userId, Long toolScripApplyId) throws BusinessException {
		return toolScripApplyMapper.selectById(toolScripApplyId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long toolScripApplyId) throws BusinessException {
		return toolScripApplyMapper.removeById(toolScripApplyId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long toolScripApplyId) throws BusinessException {
		ToolScripApply toolScripApply = new ToolScripApply();
		toolScripApply.setPkId(toolScripApplyId);
		toolScripApply.setUpdateTime(new Date());
		toolScripApply.setUpdateUser(userId);
		return toolScripApplyMapper.deleteById(toolScripApply);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<ToolScripApply> select(Long userId, ToolScripApply toolScripApply) throws BusinessException {
		return toolScripApplyMapper.select(toolScripApply);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<ToolScripApply> selectPageList(Long userId, ToolScripApply toolScripApply, QueryDto queryDto) throws BusinessException {
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<ToolScripApply> page = toolScripApplyMapper.selectPageList(toolScripApply, queryDto);
		return new Pagination<ToolScripApply>(page.getTotal(), page.getResult());
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public ToolScripApply applyGetByfullNumber(Long userId, String fullNumber) throws BusinessException {
		ToolScripApply toolScripApply = toolScripApplyMapper.applyGetByfullNumber(fullNumber);
		if (null == toolScripApply) {
			ToolUnqualifiedReport toolUnqualifiedReport = toolUnqualifiedReportMapper.selectFullNumberReportType(fullNumber, 2);
			if (null != toolUnqualifiedReport) {
				toolScripApply = new ToolScripApply();
				toolScripApply.setFullNumber(fullNumber);
				toolScripApply.setApplyStatus(2);
				toolScripApply.setScripResion(2);
			}
		}
		return toolScripApply;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer reportAudit(Long userId, String realName, ToolApplyAudit applyAudit, ToolScripApply toolscripReport) throws BusinessException {
		toolscripReport.setUpdateTime(new Date());
		toolscripReport.setUpdateUser(userId);

		String auditResult = "";
		String message = "";
		String authCode = "";

		int auditStatus = toolscripReport.getApplyStatus();
		String fullNumber = toolscripReport.getFullNumber();
		if (auditStatus == -1) {
			auditResult = "工艺部审核，未通过";
			message = "工艺部驳回了物料条码为" + fullNumber + "的报废申请,请您及时处理";
			authCode = "01040301";
		} else if (auditStatus == 2) {
			auditResult = "工艺部审核通过";
			message = "物料条码为" + fullNumber + "的报废申请已通过工艺部审核,请您及时安排报废";
			authCode = "010401";
			Tool tool = toolService.selectByFullNumber(userId, toolscripReport.getFullNumber());
			if (tool != null) {
				tool.setScripAuditor(realName);
				tool.setScripAuditTime(new Date());
				tool.setIsScrip(1);
				toolService.updateActiveById(userId, tool, tool.getPkId(), null);
			}
		}
		operLogService.insert(userId, 11, toolscripReport.getToolNumber(), fullNumber, "审核人:" + realName + " 审核结果:"+auditResult, applyAudit.getRemark());
		// 审核日志
		saveAudit(userId, realName, toolscripReport.getPkId(), auditResult, applyAudit);
		// 系统消息
		sysMessageService.insert("刀具报废申请", message, userId, realName, authCode);

		return toolScripApplyMapper.updateActiveById(toolscripReport, toolscripReport.getPkId());
	}

	private void saveAudit(Long userId, String realName, Long reportId, String auditResult, ToolApplyAudit applyAudit) throws BusinessException {
		ToolApplyAudit applyAuditSeq = toolApplyAuditMapper.selectSeqByApplyId(reportId);
		Integer auditSeq;
		if (applyAuditSeq != null) {
			Integer seqMax = applyAuditSeq.getAuditSeq();
			auditSeq = seqMax + 1;
		} else {
			auditSeq = 1;
		}
		Date date = new Date();
		applyAudit.setPkId(getPkId());
		applyAudit.setCreateTime(date);
		applyAudit.setCreateUser(userId);
		applyAudit.setUpdateTime(date);
		applyAudit.setUpdateUser(userId);
		applyAudit.setDelMark(0);
		applyAudit.setApplyId(reportId);
		applyAudit.setApplyType(1);
		applyAudit.setAuditResult(auditResult);
		applyAudit.setAuditorId(userId);
		applyAudit.setAuditorName(realName);
		applyAudit.setAuditSeq(auditSeq);
		toolApplyAuditMapper.insert(applyAudit);

	}
}