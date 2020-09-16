package org.tsinghuatj.tool.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.service.impl.BaseServiceImpl;
import org.tsinghuatj.framework.support.BusinessException;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.tool.domain.ToolBladeComposeDetail;
import org.tsinghuatj.tool.domain.ToolBladeProcess;

import org.tsinghuatj.tool.domain.ToolHead;
import org.tsinghuatj.tool.repository.ToolBladeComposeDetailMapper;
import org.tsinghuatj.tool.repository.ToolBladeProcessMapper;
import org.tsinghuatj.tool.repository.ToolHeadMapper;
import org.tsinghuatj.tool.service.IToolBladeProcessService;

/**
 *
 * ToolBladeProcess 表数据服务层接口实现类
 *
 */
@Service("toolBladeProcessService")
public class ToolBladeProcessServiceImpl extends BaseServiceImpl implements IToolBladeProcessService {

	private @Resource ToolBladeProcessMapper toolBladeProcessMapper;
	private @Resource ToolBladeComposeDetailMapper composeDetailMapper;
	private @Resource ToolHeadMapper toolHeadMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, ToolBladeProcess toolBladeProcess) throws BusinessException {
		toolBladeProcess.setPkId(getPkId());
		toolBladeProcess.setCreateTime(new Date());
		toolBladeProcess.setCreateUser(userId);
		toolBladeProcess.setUpdateTime(new Date());
		toolBladeProcess.setUpdateUser(userId);
		toolBladeProcess.setDelMark(0);
		return toolBladeProcessMapper.insert(toolBladeProcess);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, ToolBladeProcess toolBladeProcess, Long toolBladeProcessId) throws BusinessException {
		toolBladeProcess.setUpdateTime(new Date());
		toolBladeProcess.setUpdateUser(userId);
		return toolBladeProcessMapper.updateActiveById(toolBladeProcess, toolBladeProcessId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public ToolBladeProcess selectById(Long userId, Long toolBladeProcessId) throws BusinessException {
		return toolBladeProcessMapper.selectById(toolBladeProcessId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long toolBladeProcessId) throws BusinessException {
		return toolBladeProcessMapper.removeById(toolBladeProcessId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long toolBladeProcessId) throws BusinessException {
		ToolBladeProcess toolBladeProcess = new ToolBladeProcess();
		toolBladeProcess.setPkId(toolBladeProcessId);
		toolBladeProcess.setUpdateTime(new Date());
		toolBladeProcess.setUpdateUser(userId);
		return toolBladeProcessMapper.deleteById(toolBladeProcess);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<ToolBladeProcess> select(Long userId, ToolBladeProcess toolBladeProcess) throws BusinessException {
		return toolBladeProcessMapper.select(toolBladeProcess);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<ToolBladeProcess> selectPageList(Long userId, ToolBladeProcess toolBladeProcess, QueryDto queryDto) throws BusinessException {
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<ToolBladeProcess> page = toolBladeProcessMapper.selectPageList(toolBladeProcess, queryDto);
		List<String> composeNumberList = new ArrayList<String>();
		List<String> headNumberList = new ArrayList<String>();
		for (ToolBladeProcess item : page.getResult()) {
			String composeNumber = item.getComposeNumber();
			composeNumberList.add(composeNumber);
			String headNumber = composeNumber.substring(0, composeNumber.length() - 6);
			headNumberList.add(headNumber);
			item.setHeadNumber(headNumber);
		}
		if (headNumberList.size() > 0) {
			headNumberList = headNumberList.stream().distinct().collect(Collectors.toList());
			List<ToolHead> headList = toolHeadMapper.selectByHeadNumberList(headNumberList);
			Map<String, ToolHead> headMap = headList.stream().collect(Collectors.toMap(ToolHead::getHeadNumber, t -> t, (k1, k2) -> k1));
			composeNumberList = composeNumberList.stream().distinct().collect(Collectors.toList());
			List<ToolBladeComposeDetail> detailList = composeDetailMapper.selectByComposeList(composeNumberList);
			Map<String, List<ToolBladeComposeDetail>> waitHandleMap = detailList.stream().collect(Collectors.groupingBy(ToolBladeComposeDetail::getComposeNumber));
			for (ToolBladeProcess item : page) {
				item.setHeadName(headMap.get(item.getHeadNumber()).getHeadName());
				item.setDetailList(waitHandleMap.get(item.getComposeNumber()));
			}
		}

		return new Pagination<ToolBladeProcess>(page.getTotal(), page.getResult());
	}
}