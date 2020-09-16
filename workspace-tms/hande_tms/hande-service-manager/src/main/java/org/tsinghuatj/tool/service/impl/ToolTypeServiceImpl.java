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
import org.tsinghuatj.tool.domain.ToolType;
import org.tsinghuatj.tool.repository.ToolTypeMapper;
import org.tsinghuatj.tool.service.IToolTypeService;


/**
 *
 * ToolType 表数据服务层接口实现类
 *
 */
@Service("toolTypeService")
public class ToolTypeServiceImpl extends BaseServiceImpl implements IToolTypeService {

	private @Resource ToolTypeMapper toolTypeMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, ToolType toolType) throws BusinessException {	
		toolType.setPkId(getPkId());
		toolType.setCreateTime(new Date());
		toolType.setCreateUser(userId);
		toolType.setUpdateTime(new Date());
		toolType.setUpdateUser(userId);
		toolType.setDelMark(0);
		return toolTypeMapper.insert(toolType);
	}
				
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, ToolType toolType, Long toolTypeId) throws BusinessException {
		toolType.setUpdateTime(new Date());
		toolType.setUpdateUser(userId);
		return toolTypeMapper.updateActiveById(toolType, toolTypeId);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public ToolType selectById(Long userId, Long toolTypeId) throws BusinessException {
		return toolTypeMapper.selectById(toolTypeId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long toolTypeId) throws BusinessException {
		return toolTypeMapper.removeById(toolTypeId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long toolTypeId) throws BusinessException {
		ToolType toolType = new ToolType();
		toolType.setPkId(toolTypeId);
		toolType.setUpdateTime(new Date());
		toolType.setUpdateUser(userId);
		return toolTypeMapper.deleteById(toolType);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<ToolType> select(Long userId, ToolType toolType) throws BusinessException {		
		return toolTypeMapper.select(toolType);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<ToolType> selectPageList(Long userId, ToolType toolType,QueryDto queryDto) throws BusinessException {		
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<ToolType> page = toolTypeMapper.selectPageList(toolType, queryDto);
		return new Pagination<ToolType>(page.getTotal(), page.getResult());		
	}
}