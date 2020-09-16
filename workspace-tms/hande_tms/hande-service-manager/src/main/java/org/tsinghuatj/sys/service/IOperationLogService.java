package org.tsinghuatj.sys.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.sys.domain.OperationLog;

/**
 * @ClassName: IOperationLogService
 * @Description: OperationLog服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IOperationLogService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param operationLog
	 * @return
	 */
	Integer insert(Long userId, OperationLog operationLog) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param operationLog
	 * @param operationLogId
	 * @return
	 */
	Integer updateActiveById(Long userId, OperationLog operationLog, String operationLogId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param operationLog
	 * @return
	 */
	List<OperationLog> select(Long userId, OperationLog operationLog) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param operationLog
	 * @return
	 */
	Pagination<OperationLog> selectPageList(Long userId, OperationLog operationLog,QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param operationLogId
	 * @return
	 */
	OperationLog selectById(Long userId, String operationLogId) throws BusinessException;

	
	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param operationLogId
	 * @return
	 */
	Integer deleteById(Long userId, String operationLogId) throws BusinessException;
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param operationLogId
	 * @return
	 */
	Integer removeById(Long userId, String operationLogId) throws BusinessException;
}
