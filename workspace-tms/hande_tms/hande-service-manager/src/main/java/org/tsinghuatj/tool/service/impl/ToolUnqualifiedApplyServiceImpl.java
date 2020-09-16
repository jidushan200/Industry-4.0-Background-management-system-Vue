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
import org.tsinghuatj.tool.domain.ToolUnqualifiedApply;
import org.tsinghuatj.tool.repository.ToolUnqualifiedApplyMapper;
import org.tsinghuatj.tool.service.IToolUnqualifiedApplyService;


/**
 *
 * ToolUnqualifiedApply 表数据服务层接口实现类
 *
 */
@Service("toolUnqualifiedApplyService")
public class ToolUnqualifiedApplyServiceImpl extends BaseServiceImpl implements IToolUnqualifiedApplyService {

	private @Resource ToolUnqualifiedApplyMapper toolUnqualifiedApplyMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, ToolUnqualifiedApply toolUnqualifiedApply) throws BusinessException {	
		toolUnqualifiedApply.setPkId(getPkId());
		Date date = new Date();
		toolUnqualifiedApply.setCreateTime(date);
		toolUnqualifiedApply.setCreateUser(userId);
		toolUnqualifiedApply.setUpdateTime(date);
		toolUnqualifiedApply.setUpdateUser(userId);
		toolUnqualifiedApply.setApplyTime(date);
		toolUnqualifiedApply.setDelMark(0);
		return toolUnqualifiedApplyMapper.insert(toolUnqualifiedApply);
	}
				
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, ToolUnqualifiedApply toolUnqualifiedApply, Long toolUnqualifiedApplyId) throws BusinessException {
		toolUnqualifiedApply.setUpdateTime(new Date());
		toolUnqualifiedApply.setUpdateUser(userId);
		return toolUnqualifiedApplyMapper.updateActiveById(toolUnqualifiedApply, toolUnqualifiedApplyId);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public ToolUnqualifiedApply selectById(Long userId, Long toolUnqualifiedApplyId) throws BusinessException {
		return toolUnqualifiedApplyMapper.selectById(toolUnqualifiedApplyId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long toolUnqualifiedApplyId) throws BusinessException {
		return toolUnqualifiedApplyMapper.removeById(toolUnqualifiedApplyId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long toolUnqualifiedApplyId) throws BusinessException {
		ToolUnqualifiedApply toolUnqualifiedApply = new ToolUnqualifiedApply();
		toolUnqualifiedApply.setPkId(toolUnqualifiedApplyId);
		toolUnqualifiedApply.setUpdateTime(new Date());
		toolUnqualifiedApply.setUpdateUser(userId);
		return toolUnqualifiedApplyMapper.deleteById(toolUnqualifiedApply);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<ToolUnqualifiedApply> select(Long userId, ToolUnqualifiedApply toolUnqualifiedApply) throws BusinessException {		
		return toolUnqualifiedApplyMapper.select(toolUnqualifiedApply);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<ToolUnqualifiedApply> selectPageList(Long userId, ToolUnqualifiedApply toolUnqualifiedApply,QueryDto queryDto) throws BusinessException {		
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<ToolUnqualifiedApply> page = toolUnqualifiedApplyMapper.selectPageList(toolUnqualifiedApply, queryDto);
		return new Pagination<ToolUnqualifiedApply>(page.getTotal(), page.getResult());		
	}
}