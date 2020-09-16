package org.tsinghuatj.tool.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
import org.tsinghuatj.tool.domain.Tool;
import org.tsinghuatj.tool.domain.ToolBase;
import org.tsinghuatj.tool.domain.ToolCheck;
import org.tsinghuatj.tool.domain.ToolCoat;
import org.tsinghuatj.tool.domain.ToolOutbound;
import org.tsinghuatj.tool.domain.ToolProcess;
import org.tsinghuatj.tool.domain.ToolRepair;
import org.tsinghuatj.tool.domain.ToolWaitCheck;
import org.tsinghuatj.tool.repository.ToolBaseMapper;
import org.tsinghuatj.tool.repository.ToolCheckMapper;
import org.tsinghuatj.tool.repository.ToolCoatMapper;
import org.tsinghuatj.tool.repository.ToolMapper;
import org.tsinghuatj.tool.repository.ToolOutboundMapper;
import org.tsinghuatj.tool.repository.ToolProcessMapper;
import org.tsinghuatj.tool.repository.ToolWaitCheckMapper;
import org.tsinghuatj.tool.service.IToolOperLogService;
import org.tsinghuatj.tool.service.IToolProcessService;
import org.tsinghuatj.tool.service.IToolRepairService;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 *
 * ToolProcess 表数据服务层接口实现类
 *
 */
@Service("toolProcessService")
public class ToolProcessServiceImpl extends BaseServiceImpl implements IToolProcessService {

	private @Resource ToolProcessMapper toolProcessMapper;
	private @Resource ToolOutboundMapper toolOutboundMapper;
	private @Resource ToolMapper toolMapper;
	private @Resource ToolBaseMapper toolBaseMapper;
	private @Resource IToolRepairService toolRepairService;
	private @Resource ToolWaitCheckMapper toolWaitCheckMapper;
	private @Resource ToolCoatMapper toolCoatMapper;
	private @Resource ToolCheckMapper toolCheckMapper;
	private @Resource IToolOperLogService operLogService;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, String userName, ToolProcess toolProcess, BigDecimal repairMeasure) throws BusinessException {
		Tool tool = toolMapper.selectById(toolProcess.getToolId());
		ToolBase toolbase = toolBaseMapper.selectByToolNumber(tool.getToolNumber(), null);
		Long processId = getPkId();
		toolProcess.setTheoreticalQty(toolbase.getProcessEachMin());
		if (null != toolbase.getProcessEachMin() && toolProcess.getProcessQty() < toolbase.getProcessEachMin()) {
			toolProcess.setUpToStandard(2);
		} else {
			toolProcess.setUpToStandard(1);
		}
		toolProcess.setTypeId(tool.getTypeId());
		Date date = new Date();

		// 刀头刃磨填写加工数
		if (null != repairMeasure) {
			ToolRepair toolRepair = new ToolRepair();
			toolRepair.setToolId(toolProcess.getToolId());
			toolRepair.setFullNumber(toolProcess.getFullNumber());
			toolRepair.setToolNumber(toolProcess.getToolNumber());
			toolRepair.setRepairMeasure(repairMeasure);
			toolRepair.setExecutor(userName);
			toolRepair.setExecutTime(date);
			toolRepair.setRemark(toolProcess.getRemark());
			tool.setRepairAmountCur(repairMeasure);
			tool.setRepairor(userName);
			tool.setRepairTime(new Date());
			toolRepairService.insert(userId, toolRepair, tool);
			// 直接待检
			ToolWaitCheck toolWaitCheck = new ToolWaitCheck();
			Long waitCheckId = getPkId();
			toolWaitCheck.setPkId(waitCheckId);
			toolWaitCheck.setFullNumber(toolProcess.getFullNumber());
			toolWaitCheck.setToolNumber(toolProcess.getToolNumber());
			// toolWaitCheck.setToolName(toolProcess.getToolName());
			// toolWaitCheck.setToolMap(toolProcess.getToolMap());
			toolWaitCheck.setTypeId(3);
			toolWaitCheck.setCheckType(2);
			toolWaitCheck.setCheckStatus(0);
			toolWaitCheck.setSetCheckTime(date);
			toolWaitCheck.setCreateUser(userId);
			toolWaitCheck.setCreateTime(date);
			toolWaitCheck.setCheckStatus(0);
			toolWaitCheck.setUpdateTime(date);
			toolWaitCheck.setDelMark(0);
			toolWaitCheck.setUpdateUser(userId);
			toolWaitCheck.setToolQty(1);
			toolWaitCheckMapper.insert(toolWaitCheck);
			tool.setToolState(7);
			ToolCheck toolCheck = toolCheckMapper.selectByToolId(tool.getPkId(), 2);
			if (null != toolCheck) {
				toolProcess.setBeginTime(toolCheck.getCheckTime());
			} else {
				toolProcess.setBeginTime(tool.getCreateTime());
			}
			operLogService.insert(userId, 5, tool.getToolNumber(), tool.getFullNumber(), "刃磨人:" + userName + " 制件:" + toolProcess.getPartCode() + " 加工数量:" + toolProcess.getProcessQty(), toolProcess.getRemark());

		} else {
			tool.setToolState(8);
			ToolOutbound toolOutbound = toolOutboundMapper.selectByToolIdAndOutType(toolProcess.getToolId(), 1);
			if (null != toolOutbound) {
				toolProcess.setBeginTime(toolOutbound.getReceiveTime());
				toolProcess.setTeamId(toolOutbound.getTeamId());
				toolProcess.setTeamName(toolOutbound.getTeamName());
				toolProcess.setStaffCode(toolOutbound.getStaffCode());
				toolProcess.setStaffName(toolOutbound.getStaffName());
			}
			// 关联生产计数,第一次生产计数没有涂层供应商
			ToolCoat toolCoat = toolCoatMapper.selectSeqByToolId(toolProcess.getToolId());
			if (null != toolCoat) {
				toolCoat.setTypeId(tool.getTypeId());
				toolCoat.setProcessQty(toolProcess.getProcessQty());
				if (null != toolbase.getProcessEach()) {
					toolCoat.setTheoreticalQty(toolbase.getProcessEach());
					toolCoat.setCompletionDegree(new BigDecimal(toolProcess.getProcessQty()).divide(new BigDecimal(toolbase.getProcessEach()), 2, RoundingMode.HALF_UP));
				}
				toolCoatMapper.updateActiveById(toolCoat, toolCoat.getPkId());
			}
			operLogService.insert(userId, 3, tool.getToolNumber(), tool.getFullNumber(), "返库人:" + toolOutbound.getStaffCode() + "-" + toolOutbound.getStaffName() + " 制件:" + toolProcess.getPartCode() + " 加工数量:" + toolProcess.getProcessQty(), toolProcess.getRemark());

		}
		toolProcess.setPkId(processId);
		toolProcess.setCreateTime(date);
		toolProcess.setCreateUser(userId);
		toolProcess.setUpdateTime(date);
		toolProcess.setUpdateUser(userId);
		toolProcess.setDelMark(0);

		// 查询加工次序 加工数量
		ToolProcess processSeq = toolProcessMapper.selectSeqByToolId(toolProcess.getToolId());
		Integer seq;
		if (processSeq != null) {
			// 加工次序
			Integer processSeqMax = processSeq.getProcessSeq();
			seq = processSeqMax + 1;
		} else {
			seq = 1;
		}
		// 加工数量
		toolProcess.setProcessSeq(seq);
		toolProcessMapper.insert(toolProcess);
		// 查询tool表的总数量

		Integer amount = toolProcess.getProcessQty();
		Integer amountTool = tool.getProcessAmount();
		if (amountTool != null) {
			amountTool = amountTool + amount;
		} else {
			amountTool = amount;
		}

		// 更改tool表
		tool.setProcessTimes(seq);
		tool.setProcessAmount(amountTool);
		tool.setProcessCur(toolProcess.getProcessQty());

		toolMapper.updateActiveById(tool, toolProcess.getToolId());

		return 1;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, ToolProcess toolProcess, Long toolProcessId) throws BusinessException {
		toolProcess.setUpdateTime(new Date());
		toolProcess.setUpdateUser(userId);
		return toolProcessMapper.updateActiveById(toolProcess, toolProcessId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public ToolProcess selectById(Long userId, Long toolProcessId) throws BusinessException {
		return toolProcessMapper.selectById(toolProcessId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long toolProcessId) throws BusinessException {
		return toolProcessMapper.removeById(toolProcessId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long toolProcessId) throws BusinessException {
		ToolProcess toolProcess = new ToolProcess();
		toolProcess.setPkId(toolProcessId);
		toolProcess.setUpdateTime(new Date());
		toolProcess.setUpdateUser(userId);
		return toolProcessMapper.deleteById(toolProcess);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<ToolProcess> select(Long userId, ToolProcess toolProcess) throws BusinessException {
		return toolProcessMapper.select(toolProcess);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<ToolProcess> selectPageList(Long userId, ToolProcess toolProcess, QueryDto queryDto) throws BusinessException {
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<ToolProcess> page = toolProcessMapper.selectPageList(toolProcess, queryDto);
		return new Pagination<ToolProcess>(page.getTotal(), page.getResult());
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<ToolProcess> selectCoatProcessPageList(Long userId, ToolProcess toolProcess, QueryDto queryDto) throws BusinessException {
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<ToolProcess> page = toolProcessMapper.selectCoatProcessPageList(toolProcess, queryDto);
		return new Pagination<ToolProcess>(page.getTotal(), page.getResult());
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public ToolProcess selectLatestProcess(Long userId, ToolProcess toolProcess) throws BusinessException {
		return toolProcessMapper.selectLatestProcess(toolProcess);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer noProcessReturn(Long userId, String fullNumber) throws BusinessException {
		Tool tool = new Tool();
		tool.setFullNumber(fullNumber);
		tool.setToolState(8);
		tool.setUpdateTime(new Date());
		tool.setUpdateUser(userId);
		return toolMapper.updateActiveByNumber(tool);
	}
}