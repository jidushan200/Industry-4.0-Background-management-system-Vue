package org.tsinghuatj.tool.service.impl;

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
import org.tsinghuatj.framework.utils.StringUtils;
import org.tsinghuatj.tool.domain.ToolOperLog;
import org.tsinghuatj.tool.repository.ToolOperLogMapper;
import org.tsinghuatj.tool.service.IToolOperLogService;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;


/**
 *
 * ToolOperLog 表数据服务层接口实现类
 *
 */
@Service("toolOperLogService")
public class ToolOperLogServiceImpl extends BaseServiceImpl implements IToolOperLogService {

	private @Resource ToolOperLogMapper toolOperLogMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId,Integer operType,String toolNumber,String fullNumber,String operateInfo,String operateRemark ) throws BusinessException {	
		ToolOperLog toolOperLog = new ToolOperLog();		
		toolOperLog.setPkId(StringUtils.uuid());		
		toolOperLog.setCreateTime(new Date());
		toolOperLog.setCreateUser(userId);
		toolOperLog.setOperType(operType);
		toolOperLog.setFullNumber(fullNumber);
		toolOperLog.setToolNumber(toolNumber);
		toolOperLog.setOperateInfo(operateInfo);
		toolOperLog.setOperateRemark(operateRemark);
		return toolOperLogMapper.insert(toolOperLog);
	}
				
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, ToolOperLog toolOperLog, String toolOperLogId) throws BusinessException {
		return toolOperLogMapper.updateActiveById(toolOperLog, toolOperLogId);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public ToolOperLog selectById(Long userId, String toolOperLogId) throws BusinessException {
		return toolOperLogMapper.selectById(toolOperLogId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, String toolOperLogId) throws BusinessException {
		return toolOperLogMapper.removeById(toolOperLogId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, String toolOperLogId) throws BusinessException {
		ToolOperLog toolOperLog = new ToolOperLog();
		toolOperLog.setPkId(toolOperLogId);
		return toolOperLogMapper.deleteById(toolOperLog);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<ToolOperLog> select(Long userId, ToolOperLog toolOperLog) throws BusinessException {		
		return toolOperLogMapper.select(toolOperLog);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<ToolOperLog> selectPageList(Long userId, ToolOperLog toolOperLog,QueryDto queryDto) throws BusinessException {		
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<ToolOperLog> page = toolOperLogMapper.selectPageList(toolOperLog, queryDto);
		return new Pagination<ToolOperLog>(page.getTotal(), page.getResult());		
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer init(ToolOperLog toolOperLog) throws BusinessException {
		toolOperLog.setPkId(StringUtils.uuid());
		return toolOperLogMapper.insert(toolOperLog);
	}
}