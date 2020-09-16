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
import org.tsinghuatj.tool.domain.ToolAppendix;
import org.tsinghuatj.tool.repository.ToolAppendixMapper;
import org.tsinghuatj.tool.service.IToolAppendixService;


/**
 *
 * ToolAppendix 表数据服务层接口实现类
 *
 */
@Service("toolAppendixService")
public class ToolAppendixServiceImpl extends BaseServiceImpl implements IToolAppendixService {

	private @Resource ToolAppendixMapper toolAppendixMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, ToolAppendix toolAppendix) throws BusinessException {	
		//toolAppendix.setPkId(getPkId());
		toolAppendix.setCreateTime(new Date());
		toolAppendix.setCreateUser(userId);
		toolAppendix.setUpdateTime(new Date());
		toolAppendix.setUpdateUser(userId);
		toolAppendix.setDelMark(0);
		return toolAppendixMapper.insert(toolAppendix);
	}
				
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, ToolAppendix toolAppendix, Long toolAppendixId) throws BusinessException {
		toolAppendix.setUpdateTime(new Date());
		toolAppendix.setUpdateUser(userId);
		return toolAppendixMapper.updateActiveById(toolAppendix, toolAppendixId);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public ToolAppendix selectById(Long userId, Long toolAppendixId) throws BusinessException {
		return toolAppendixMapper.selectById(toolAppendixId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long toolAppendixId) throws BusinessException {
		return toolAppendixMapper.removeById(toolAppendixId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long toolAppendixId) throws BusinessException {
		ToolAppendix toolAppendix = new ToolAppendix();
		toolAppendix.setPkId(toolAppendixId);
		toolAppendix.setUpdateTime(new Date());
		toolAppendix.setUpdateUser(userId);
		return toolAppendixMapper.deleteById(toolAppendix);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<ToolAppendix> select(Long userId, ToolAppendix toolAppendix) throws BusinessException {		
		return toolAppendixMapper.select(toolAppendix);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<ToolAppendix> selectPageList(Long userId, ToolAppendix toolAppendix,QueryDto queryDto) throws BusinessException {		
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<ToolAppendix> page = toolAppendixMapper.selectPageList(toolAppendix, queryDto);
		return new Pagination<ToolAppendix>(page.getTotal(), page.getResult());		
	}

	@Override
	public Long getNextPkId() throws BusinessException {
		// TODO Auto-generated method stub
		return getPkId();
	}
}