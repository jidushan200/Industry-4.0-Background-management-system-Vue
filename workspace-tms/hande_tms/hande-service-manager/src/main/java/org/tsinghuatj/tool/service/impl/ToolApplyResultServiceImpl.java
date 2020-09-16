package org.tsinghuatj.tool.service.impl;

import java.util.Date;
import java.util.List;

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
import org.tsinghuatj.tool.domain.ToolApplyResult;
import org.tsinghuatj.tool.repository.ToolApplyResultMapper;
import org.tsinghuatj.tool.service.IToolApplyResultService;


/**
 *
 * ToolApplyResult 表数据服务层接口实现类
 *
 */
@Service("toolApplyResultService")
public class ToolApplyResultServiceImpl extends BaseServiceImpl implements IToolApplyResultService {

	private @Resource ToolApplyResultMapper toolApplyResultMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, ToolApplyResult toolApplyResult) throws BusinessException {	
		toolApplyResult.setPkId(getPkId());
		toolApplyResult.setCreateTime(new Date());
		toolApplyResult.setCreateUser(userId);
		toolApplyResult.setUpdateTime(new Date());
		toolApplyResult.setUpdateUser(userId);
		toolApplyResult.setDelMark(0);
		return toolApplyResultMapper.insert(toolApplyResult);
	}
				
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, ToolApplyResult toolApplyResult, Long toolApplyResultId) throws BusinessException {
		toolApplyResult.setUpdateTime(new Date());
		toolApplyResult.setUpdateUser(userId);
		return toolApplyResultMapper.updateActiveById(toolApplyResult, toolApplyResultId);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public ToolApplyResult selectById(Long userId, Long toolApplyResultId) throws BusinessException {
		return toolApplyResultMapper.selectById(toolApplyResultId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long toolApplyResultId) throws BusinessException {
		return toolApplyResultMapper.removeById(toolApplyResultId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long toolApplyResultId) throws BusinessException {
		ToolApplyResult toolApplyResult = new ToolApplyResult();
		toolApplyResult.setPkId(toolApplyResultId);
		toolApplyResult.setUpdateTime(new Date());
		toolApplyResult.setUpdateUser(userId);
		return toolApplyResultMapper.deleteById(toolApplyResult);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<ToolApplyResult> select(Long userId, ToolApplyResult toolApplyResult) throws BusinessException {		
		return toolApplyResultMapper.select(toolApplyResult);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<ToolApplyResult> selectPageList(Long userId, ToolApplyResult toolApplyResult,QueryDto queryDto) throws BusinessException {		
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<ToolApplyResult> page = toolApplyResultMapper.selectPageList(toolApplyResult, queryDto);
		return new Pagination<ToolApplyResult>(page.getTotal(), page.getResult());		
	}
}