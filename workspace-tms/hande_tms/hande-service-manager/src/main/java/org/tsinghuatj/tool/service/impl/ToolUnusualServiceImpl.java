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
import org.tsinghuatj.tool.domain.ToolUnusual;
import org.tsinghuatj.tool.repository.ToolUnusualMapper;
import org.tsinghuatj.tool.service.IToolUnusualService;


/**
 *
 * ToolUnusual 表数据服务层接口实现类
 *
 */
@Service("toolUnusualService")
public class ToolUnusualServiceImpl extends BaseServiceImpl implements IToolUnusualService {

	private @Resource ToolUnusualMapper toolUnusualMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, ToolUnusual toolUnusual) throws BusinessException {	
		toolUnusual.setPkId(getPkId());
		toolUnusual.setCreateTime(new Date());
		toolUnusual.setCreateUser(userId);
		toolUnusual.setUpdateTime(new Date());
		toolUnusual.setUpdateUser(userId);
		toolUnusual.setDelMark(0);
		return toolUnusualMapper.insert(toolUnusual);
	}
				
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, ToolUnusual toolUnusual, Long toolUnusualId) throws BusinessException {
		toolUnusual.setUpdateTime(new Date());
		toolUnusual.setUpdateUser(userId);
		return toolUnusualMapper.updateActiveById(toolUnusual, toolUnusualId);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public ToolUnusual selectById(Long userId, Long toolUnusualId) throws BusinessException {
		return toolUnusualMapper.selectById(toolUnusualId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long toolUnusualId) throws BusinessException {
		return toolUnusualMapper.removeById(toolUnusualId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long toolUnusualId) throws BusinessException {
		ToolUnusual toolUnusual = new ToolUnusual();
		toolUnusual.setPkId(toolUnusualId);
		toolUnusual.setUpdateTime(new Date());
		toolUnusual.setUpdateUser(userId);
		return toolUnusualMapper.deleteById(toolUnusual);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<ToolUnusual> select(Long userId, ToolUnusual toolUnusual) throws BusinessException {		
		return toolUnusualMapper.select(toolUnusual);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<ToolUnusual> selectPageList(Long userId, ToolUnusual toolUnusual,QueryDto queryDto) throws BusinessException {		
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<ToolUnusual> page = toolUnusualMapper.selectPageList(toolUnusual, queryDto);
		return new Pagination<ToolUnusual>(page.getTotal(), page.getResult());		
	}

	@Override
	public Integer unusualImport(Long userId, List<ToolUnusual> unusualList) throws BusinessException {
		Date date = new Date();
		unusualList.forEach(toolUnusual -> {	
			try {
				toolUnusual.setPkId(getPkId());
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			toolUnusual.setCreateTime(date);
			toolUnusual.setCreateUser(userId);
			toolUnusual.setUpdateTime(date);
			toolUnusual.setUpdateUser(userId);
			toolUnusual.setDelMark(0);	
			toolUnusualMapper.insert(toolUnusual);
		});
		return 1;
	}
}