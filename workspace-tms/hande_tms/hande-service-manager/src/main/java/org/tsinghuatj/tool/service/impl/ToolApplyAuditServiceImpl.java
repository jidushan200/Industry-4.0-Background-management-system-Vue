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

import org.tsinghuatj.base.repository.StaffMapper;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.tool.domain.ToolApplyAudit;
import org.tsinghuatj.tool.repository.ToolApplyAuditMapper;
import org.tsinghuatj.tool.service.IToolApplyAuditService;


/**
 *
 * ToolApplyAudit 表数据服务层接口实现类
 *
 */
@Service("applyAuditService")
public class ToolApplyAuditServiceImpl extends BaseServiceImpl implements IToolApplyAuditService {

	private @Resource ToolApplyAuditMapper toolApplyAuditMapper;
	
	private @Resource StaffMapper staffMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, ToolApplyAudit toolApplyAudit) throws BusinessException {	
		toolApplyAudit.setPkId(getPkId());
		toolApplyAudit.setCreateTime(new Date());
		toolApplyAudit.setCreateUser(userId);
		toolApplyAudit.setUpdateTime(new Date());
		toolApplyAudit.setUpdateUser(userId);
		toolApplyAudit.setDelMark(0);
		return toolApplyAuditMapper.insert(toolApplyAudit);
	}
				
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, ToolApplyAudit toolApplyAudit, Long toolApplyAuditId) throws BusinessException {
		toolApplyAudit.setUpdateTime(new Date());
		toolApplyAudit.setUpdateUser(userId);
		return toolApplyAuditMapper.updateActiveById(toolApplyAudit, toolApplyAuditId);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public ToolApplyAudit selectById(Long userId, Long toolApplyAuditId) throws BusinessException {
		return toolApplyAuditMapper.selectById(toolApplyAuditId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long toolApplyAuditId) throws BusinessException {
		return toolApplyAuditMapper.removeById(toolApplyAuditId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long toolApplyAuditId) throws BusinessException {
		ToolApplyAudit toolApplyAudit = new ToolApplyAudit();
		toolApplyAudit.setPkId(toolApplyAuditId);
		toolApplyAudit.setUpdateTime(new Date());
		toolApplyAudit.setUpdateUser(userId);
		return toolApplyAuditMapper.deleteById(toolApplyAudit);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<ToolApplyAudit> selectByApplyId(Long userId, Long applyId) throws BusinessException {		
		return toolApplyAuditMapper.select(applyId);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<ToolApplyAudit> selectPageList(Long userId, ToolApplyAudit toolApplyAudit,QueryDto queryDto) throws BusinessException {		
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<ToolApplyAudit> page = toolApplyAuditMapper.selectPageList(toolApplyAudit, queryDto);
		return new Pagination<ToolApplyAudit>(page.getTotal(), page.getResult());		
	}


}