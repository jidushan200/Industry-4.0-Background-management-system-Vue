package org.tsinghuatj.sys.service.impl;

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
import org.tsinghuatj.sys.domain.OperationLog;
import org.tsinghuatj.sys.repository.OperationLogMapper;
import org.tsinghuatj.sys.service.IOperationLogService;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;


/**
 *
 * OperationLog 表数据服务层接口实现类
 *
 */
@Service("operationLogService")
public class OperationLogServiceImpl extends BaseServiceImpl implements IOperationLogService {

	private @Resource OperationLogMapper operationLogMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, OperationLog operationLog) throws BusinessException {	
		//operationLog.setPkId(getPkId());
		operationLog.setCreateTime(new Date());
		operationLog.setCreateUser(userId);
		operationLog.setUpdateTime(new Date());
		operationLog.setUpdateUser(userId);
		operationLog.setDelMark(0);
		return operationLogMapper.insert(operationLog);
	}
				
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, OperationLog operationLog, String operationLogId) throws BusinessException {
		operationLog.setUpdateTime(new Date());
		operationLog.setUpdateUser(userId);
		return operationLogMapper.updateActiveById(operationLog, operationLogId);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public OperationLog selectById(Long userId, String operationLogId) throws BusinessException {
		return operationLogMapper.selectById(operationLogId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, String operationLogId) throws BusinessException {
		return operationLogMapper.removeById(operationLogId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, String operationLogId) throws BusinessException {
		OperationLog operationLog = new OperationLog();
		operationLog.setPkId(operationLogId);
		operationLog.setUpdateTime(new Date());
		operationLog.setUpdateUser(userId);
		return operationLogMapper.deleteById(operationLog);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<OperationLog> select(Long userId, OperationLog operationLog) throws BusinessException {		
		return operationLogMapper.select(operationLog);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<OperationLog> selectPageList(Long userId, OperationLog operationLog,QueryDto queryDto) throws BusinessException {		
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<OperationLog> page = operationLogMapper.selectPageList(operationLog, queryDto);
		return new Pagination<OperationLog>(page.getTotal(), page.getResult());		
	}
}