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
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.service.impl.BaseServiceImpl;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.tool.domain.ToolBladeComposeDetail;
import org.tsinghuatj.tool.domain.ToolHead;
import org.tsinghuatj.tool.domain.ToolWaitCheck;
import org.tsinghuatj.tool.repository.ToolBladeComposeDetailMapper;
import org.tsinghuatj.tool.repository.ToolHeadMapper;
import org.tsinghuatj.tool.repository.ToolWaitCheckMapper;
import org.tsinghuatj.tool.service.IToolWaitCheckService;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 *
 * ToolWaitCheck 表数据服务层接口实现类
 *
 */
@Service("toolWaitCheckService")
public class ToolWaitCheckServiceImpl extends BaseServiceImpl implements IToolWaitCheckService {

	private @Resource ToolWaitCheckMapper toolWaitCheckMapper;
	private @Resource ToolHeadMapper toolHeadMapper;
	private @Resource ToolBladeComposeDetailMapper composeDetailMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, ToolWaitCheck toolWaitCheck) throws BusinessException {
		toolWaitCheck.setPkId(getPkId());
		toolWaitCheck.setCreateTime(new Date());
		toolWaitCheck.setCreateUser(userId);
		toolWaitCheck.setUpdateTime(new Date());
		toolWaitCheck.setUpdateUser(userId);
		toolWaitCheck.setDelMark(0);
		return toolWaitCheckMapper.insert(toolWaitCheck);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, ToolWaitCheck toolWaitCheck, Long toolWaitCheckId) throws BusinessException {
		toolWaitCheck.setUpdateTime(new Date());
		toolWaitCheck.setUpdateUser(userId);
		return toolWaitCheckMapper.updateActiveById(toolWaitCheck, toolWaitCheckId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public ToolWaitCheck selectById(Long userId, Long toolWaitCheckId) throws BusinessException {
		return toolWaitCheckMapper.selectById(toolWaitCheckId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long toolWaitCheckId) throws BusinessException {
		return toolWaitCheckMapper.removeById(toolWaitCheckId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long toolWaitCheckId) throws BusinessException {
		ToolWaitCheck toolWaitCheck = new ToolWaitCheck();
		toolWaitCheck.setPkId(toolWaitCheckId);
		toolWaitCheck.setUpdateTime(new Date());
		toolWaitCheck.setUpdateUser(userId);
		return toolWaitCheckMapper.deleteById(toolWaitCheck);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<ToolWaitCheck> select(Long userId, ToolWaitCheck toolWaitCheck) throws BusinessException {
		return toolWaitCheckMapper.select(toolWaitCheck);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<ToolWaitCheck> selectPageList(Long userId, ToolWaitCheck toolWaitCheck, QueryDto queryDto) throws BusinessException {
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<ToolWaitCheck> page = toolWaitCheckMapper.selectPageList(toolWaitCheck, queryDto);
		return new Pagination<ToolWaitCheck>(page.getTotal(), page.getResult());
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<ToolWaitCheck> selectComposePageList(Long userId, ToolWaitCheck toolWaitCheck, QueryDto queryDto) throws BusinessException {
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<ToolWaitCheck> page = toolWaitCheckMapper.selectComposePageList(toolWaitCheck, queryDto);
		List<String> composeNumberList = new ArrayList<String>();
		List<String> headNumberList = new ArrayList<String>();
		for (ToolWaitCheck item : page) {
			String composeNumber = item.getFullNumber();
			composeNumberList.add(composeNumber);
			String headNumber = composeNumber.substring(0, composeNumber.length() - 6);
			headNumberList.add(headNumber);
			item.setToolNumber(headNumber);
		}
		if (headNumberList.size() > 0) {
			headNumberList = headNumberList.stream().distinct().collect(Collectors.toList());
			List<ToolHead> headList = toolHeadMapper.selectByHeadNumberList(headNumberList);
			Map<String, ToolHead> headMap = headList.stream().collect(Collectors.toMap(ToolHead::getHeadNumber, t -> t, (k1, k2) -> k1));
			List<ToolBladeComposeDetail> detailList = composeDetailMapper.selectByComposeList(composeNumberList);
			Map<String, List<ToolBladeComposeDetail>> waitHandleMap = detailList.stream().collect(Collectors.groupingBy(ToolBladeComposeDetail::getComposeNumber));
			for (ToolWaitCheck item : page) {
				item.setToolName(headMap.get(item.getToolNumber()).getHeadName());
				item.setDetailList(waitHandleMap.get(item.getFullNumber()));
			}
		}

		return new Pagination<ToolWaitCheck>(page.getTotal(), page.getResult());
	}
}