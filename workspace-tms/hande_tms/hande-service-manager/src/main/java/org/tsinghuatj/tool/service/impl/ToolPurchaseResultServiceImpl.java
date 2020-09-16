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
import org.tsinghuatj.tool.domain.ToolPurchaseResult;
import org.tsinghuatj.tool.repository.ToolPurchaseResultMapper;
import org.tsinghuatj.tool.service.IToolPurchaseResultService;


/**
 *
 * ToolPurchaseResult 表数据服务层接口实现类
 *
 */
@Service("toolPurchaseResultService")
public class ToolPurchaseResultServiceImpl extends BaseServiceImpl implements IToolPurchaseResultService {

	private @Resource ToolPurchaseResultMapper toolPurchaseResultMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, ToolPurchaseResult toolPurchaseResult) throws BusinessException {	
		toolPurchaseResult.setPkId(getPkId());
		toolPurchaseResult.setCreateTime(new Date());
		toolPurchaseResult.setCreateUser(userId);
		toolPurchaseResult.setUpdateTime(new Date());
		toolPurchaseResult.setUpdateUser(userId);
		toolPurchaseResult.setDelMark(0);
		return toolPurchaseResultMapper.insert(toolPurchaseResult);
	}
				
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, ToolPurchaseResult toolPurchaseResult, Long toolPurchaseResultId) throws BusinessException {
		toolPurchaseResult.setUpdateTime(new Date());
		toolPurchaseResult.setUpdateUser(userId);
		return toolPurchaseResultMapper.updateActiveById(toolPurchaseResult, toolPurchaseResultId);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public ToolPurchaseResult selectById(Long userId, Long toolPurchaseResultId) throws BusinessException {
		return toolPurchaseResultMapper.selectById(toolPurchaseResultId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long toolPurchaseResultId) throws BusinessException {
		return toolPurchaseResultMapper.removeById(toolPurchaseResultId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long toolPurchaseResultId) throws BusinessException {
		ToolPurchaseResult toolPurchaseResult = new ToolPurchaseResult();
		toolPurchaseResult.setPkId(toolPurchaseResultId);
		toolPurchaseResult.setUpdateTime(new Date());
		toolPurchaseResult.setUpdateUser(userId);
		return toolPurchaseResultMapper.deleteById(toolPurchaseResult);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<ToolPurchaseResult> select(Long userId, ToolPurchaseResult toolPurchaseResult) throws BusinessException {		
		return toolPurchaseResultMapper.select(toolPurchaseResult);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<ToolPurchaseResult> selectPageList(Long userId, ToolPurchaseResult toolPurchaseResult,QueryDto queryDto) throws BusinessException {		
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<ToolPurchaseResult> page = toolPurchaseResultMapper.selectPageList(toolPurchaseResult, queryDto);
		return new Pagination<ToolPurchaseResult>(page.getTotal(), page.getResult());		
	}
}