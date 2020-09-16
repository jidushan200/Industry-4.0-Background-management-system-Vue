package org.tsinghuatj.tool.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.tool.domain.ToolOperLog;

/**
 * @ClassName: IToolOperLogService
 * @Description: ToolOperLog服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IToolOperLogService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param toolOperLog
	 * @return
	 */
	Integer insert(Long userId, Integer operType, String toolNumber, String fullNumber, String operateInfo,
			String operateRemark) throws BusinessException;
	
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param toolOperLog
	 * @return
	 */
	Integer init(ToolOperLog toolOperLog) throws BusinessException;

	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param toolOperLog
	 * @param toolOperLogId
	 * @return
	 */
	Integer updateActiveById(Long userId, ToolOperLog toolOperLog, String toolOperLogId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param toolOperLog
	 * @return
	 */
	List<ToolOperLog> select(Long userId, ToolOperLog toolOperLog) throws BusinessException;

	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param toolOperLog
	 * @return
	 */
	Pagination<ToolOperLog> selectPageList(Long userId, ToolOperLog toolOperLog, QueryDto queryDto)
			throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param toolOperLogId
	 * @return
	 */
	ToolOperLog selectById(Long userId, String toolOperLogId) throws BusinessException;

	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param toolOperLogId
	 * @return
	 */
	Integer deleteById(Long userId, String toolOperLogId) throws BusinessException;

	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param toolOperLogId
	 * @return
	 */
	Integer removeById(Long userId, String toolOperLogId) throws BusinessException;
}
