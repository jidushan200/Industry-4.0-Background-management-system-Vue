package org.tsinghuatj.tool.service.impl;

import java.math.BigDecimal;
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
import org.tsinghuatj.framework.utils.JsonUtils;
import org.tsinghuatj.tool.domain.Tool;
import org.tsinghuatj.tool.domain.ToolBladeCompose;
import org.tsinghuatj.tool.domain.ToolRepair;
import org.tsinghuatj.tool.domain.ToolRepairPrice;
import org.tsinghuatj.tool.domain.ToolWaitCheck;
import org.tsinghuatj.tool.domain.ToolWaitHandle;
import org.tsinghuatj.tool.repository.ToolBladeComposeMapper;
import org.tsinghuatj.tool.repository.ToolMapper;
import org.tsinghuatj.tool.repository.ToolRepairMapper;
import org.tsinghuatj.tool.repository.ToolRepairPriceMapper;
import org.tsinghuatj.tool.repository.ToolWaitCheckMapper;
import org.tsinghuatj.tool.repository.ToolWaitHandleMapper;
import org.tsinghuatj.tool.service.IToolOperLogService;
import org.tsinghuatj.tool.service.IToolRepairService;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 *
 * ToolRepair 表数据服务层接口实现类
 *
 */
@Service("toolRepairService")
public class ToolRepairServiceImpl extends BaseServiceImpl implements IToolRepairService {

	private @Resource ToolRepairMapper toolRepairMapper;
	private @Resource ToolRepairPriceMapper toolRepairPriceMapper;
	private @Resource ToolMapper toolMapper;
	private @Resource ToolWaitHandleMapper toolWaitHandleMapper;
	private @Resource ToolWaitCheckMapper toolWaitCheckMapper;
	private @Resource IToolOperLogService operLogService;
	private @Resource ToolBladeComposeMapper toolBladeComposeMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, ToolRepair toolRepair, Tool tool) throws BusinessException {
		toolRepair.setPkId(getPkId());
		toolRepair.setCreateTime(new Date());
		toolRepair.setCreateUser(userId);
		toolRepair.setUpdateTime(new Date());
		toolRepair.setUpdateUser(userId);
		toolRepair.setDelMark(0);

		ToolRepairPrice toolRepairPrice = toolRepairPriceMapper.selectBySupplierId(toolRepair.getSupplierId(), toolRepair.getToolNumber());
		if (toolRepairPrice != null) {
			toolRepair.setRepairPrice(toolRepairPrice.getPrice());
		} else {
			toolRepair.setRepairPrice(BigDecimal.ZERO);
		}
		ToolRepair toolRepairSeq = toolRepairMapper.selectSeqByToolId(toolRepair.getToolId());
		// 获取本次刃磨的刃磨次序
		Integer seq;
		if (toolRepairSeq != null && null != toolRepairSeq.getRepairSeq()) {
			Integer repairSeqMax = toolRepairSeq.getRepairSeq();
			seq = repairSeqMax + 1;
		} else {
			seq = 1;
		}
		toolRepair.setRepairSeq(seq);
		// 根据toolId查询被刃磨刀具
		Tool repairTool = toolMapper.selectById(toolRepair.getToolId());
		BigDecimal repairAmount;
		if (repairTool.getRepairAmount() != null) {
			repairAmount = repairTool.getRepairAmount().add(toolRepair.getRepairMeasure());
		} else {
			repairAmount = toolRepair.getRepairMeasure();
		}

		// 更新刃磨次数
		tool.setRepairTimes(seq);
		tool.setRepairAmount(repairAmount);
		// 更改刀具状态
		/*
		 * if(repairTool.getToolType() == 2){ tool.setToolState(2); }else{
		 * tool.setToolState(4); }
		 */
		tool.setToolState(4);
		// 执行tool表的刃磨次数及刀具状态的更改
		toolMapper.updateActiveById(tool, toolRepair.getToolId());
		// 执行刃磨记录的新增
		operLogService.insert(userId, 5, toolRepair.getToolNumber(), toolRepair.getFullNumber(), "刃磨人:" + toolRepair.getExecutor(), toolRepair.getRemark());

		return toolRepairMapper.insert(toolRepair);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, ToolRepair toolRepair, Long toolRepairId) throws BusinessException {
		toolRepair.setUpdateTime(new Date());
		toolRepair.setUpdateUser(userId);
		return toolRepairMapper.updateActiveById(toolRepair, toolRepairId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public ToolRepair selectById(Long userId, Long toolRepairId) throws BusinessException {
		return toolRepairMapper.selectById(toolRepairId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long toolRepairId) throws BusinessException {
		return toolRepairMapper.removeById(toolRepairId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long toolRepairId) throws BusinessException {
		ToolRepair toolRepair = new ToolRepair();
		toolRepair.setPkId(toolRepairId);
		toolRepair.setUpdateTime(new Date());
		toolRepair.setUpdateUser(userId);
		return toolRepairMapper.deleteById(toolRepair);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<ToolRepair> select(Long userId, ToolRepair toolRepair) throws BusinessException {
		return toolRepairMapper.select(toolRepair);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<ToolRepair> selectPageList(Long userId, ToolRepair toolRepair, QueryDto queryDto) throws BusinessException {
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<ToolRepair> page;
		if (null != toolRepair.getTypeId() && toolRepair.getTypeId() == 4) {
			page = toolRepairMapper.selectBladePageList(toolRepair, queryDto);
		} else {
			page = toolRepairMapper.selectPageList(toolRepair, queryDto);
		}
		return new Pagination<ToolRepair>(page.getTotal(), page.getResult());
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insertBladeRepair(Long userId, ToolRepair toolRepair, Long waitId) throws BusinessException {
		// 刃磨记录
		Date date = new Date();
		toolRepair.setPkId(getPkId());
		toolRepair.setCreateTime(date);
		toolRepair.setCreateUser(userId);
		toolRepair.setUpdateTime(date);
		toolRepair.setUpdateUser(userId);
		toolRepair.setDelMark(0);
		toolRepairMapper.insert(toolRepair);

		// 修改待处理状态
		ToolWaitHandle toolWaitHandle = toolWaitHandleMapper.selectById(waitId);
		toolWaitHandle.setUpdateTime(date);
		toolWaitHandle.setUpdateUser(userId);
		toolWaitHandle.setHandleResult(2);
		toolWaitHandleMapper.updateActiveById(toolWaitHandle, waitId);
		// 增加待检记录
		ToolWaitCheck waitcheck = new ToolWaitCheck();
		waitcheck.setPkId(getPkId());
		waitcheck.setCreateTime(date);
		waitcheck.setCreateUser(userId);
		waitcheck.setUpdateTime(date);
		waitcheck.setUpdateUser(userId);
		waitcheck.setDelMark(0);
		waitcheck.setCheckStatus(0);
		waitcheck.setSetCheckTime(date);
		waitcheck.setCheckType(2);
		waitcheck.setFullNumber(toolWaitHandle.getComposeNumber());
		waitcheck.setToolNumber(toolWaitHandle.getToolNumber());
		// waitcheck.setToolName(toolWaitHandle.getToolName());
		// waitcheck.setToolMap(toolWaitHandle.getToolMap());
		waitcheck.setTypeId(toolWaitHandle.getTypeId());
		waitcheck.setToolQty(toolWaitHandle.getToolQty());
		waitcheck.setIsNew(toolWaitHandle.getIsNew());
		waitcheck.setReceiptId(toolWaitHandle.getReceiptId());
		toolWaitCheckMapper.insert(waitcheck);
		if (null != toolWaitHandle.getComposeNumber()) {
			ToolBladeCompose bladeCompose = new ToolBladeCompose();
			bladeCompose.setComposeNumber(toolWaitHandle.getComposeNumber());
			bladeCompose.setToolStatus(3);// 待刃磨质检
			waitcheck.setUpdateTime(date);
			waitcheck.setUpdateUser(userId);
			toolBladeComposeMapper.updateActiveByComposeNumber(bladeCompose, bladeCompose.getComposeNumber());
		}
		operLogService.insert(userId, 5, toolWaitHandle.getToolNumber(), toolWaitHandle.getComposeNumber(), "刃磨人:" + toolRepair.getExecutor(), toolRepair.getRemark());

		return 1;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insertComposeBladeRepair(Long userId, String realName, String composeNumber, Long waitId, String detailJson) throws BusinessException {
		List<ToolRepair> repairList = JsonUtils.json2list(detailJson, ToolRepair.class);
		Date date = new Date();
		for (ToolRepair item : repairList) {
			item.setPkId(getPkId());
			item.setFullNumber(composeNumber);
			item.setCreateTime(date);
			item.setCreateUser(userId);
			item.setUpdateTime(date);
			item.setUpdateUser(userId);
			item.setDelMark(0);
			item.setExecutor(realName);
			item.setExecutTime(date);
			toolRepairMapper.insert(item);
			operLogService.insert(userId, 5, item.getToolNumber(), composeNumber, "刃磨人:" + realName, item.getRemark());
		}

		// 修改待处理状态
		ToolWaitHandle toolWaitHandle = new ToolWaitHandle();
		toolWaitHandle.setUpdateTime(date);
		toolWaitHandle.setUpdateUser(userId);
		toolWaitHandle.setHandleResult(2);
		toolWaitHandle.setPkId(waitId);
		toolWaitHandleMapper.updateActiveById(toolWaitHandle, waitId);

		// 增加组合刃磨待检记录
		ToolWaitCheck waitcheck = new ToolWaitCheck();
		waitcheck.setPkId(getPkId());
		waitcheck.setCreateTime(date);
		waitcheck.setCreateUser(userId);
		waitcheck.setUpdateTime(date);
		waitcheck.setUpdateUser(userId);
		waitcheck.setDelMark(0);
		waitcheck.setCheckStatus(0);
		waitcheck.setSetCheckTime(date);
		waitcheck.setCheckType(7);
		waitcheck.setFullNumber(composeNumber);
		toolWaitCheckMapper.insert(waitcheck);
		if (null != toolWaitHandle.getComposeNumber()) {
			ToolBladeCompose bladeCompose = new ToolBladeCompose();
			bladeCompose.setComposeNumber(toolWaitHandle.getComposeNumber());
			bladeCompose.setToolStatus(3);// 待刃磨质检
			waitcheck.setUpdateTime(date);
			waitcheck.setUpdateUser(userId);
			toolBladeComposeMapper.updateActiveByComposeNumber(bladeCompose, bladeCompose.getComposeNumber());
		}

		return 1;
	}
}