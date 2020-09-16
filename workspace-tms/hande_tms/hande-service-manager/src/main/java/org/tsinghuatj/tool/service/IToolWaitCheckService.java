package org.tsinghuatj.tool.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.tool.domain.ToolWaitCheck;

/**
 * @ClassName: IToolWaitCheckService
 * @Description: ToolWaitCheck服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IToolWaitCheckService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param toolWaitCheck
	 * @return
	 */
	Integer insert(Long userId, ToolWaitCheck toolWaitCheck) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param toolWaitCheck
	 * @param toolWaitCheckId
	 * @return
	 */
	Integer updateActiveById(Long userId, ToolWaitCheck toolWaitCheck, Long toolWaitCheckId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param toolWaitCheck
	 * @return
	 */
	List<ToolWaitCheck> select(Long userId, ToolWaitCheck toolWaitCheck) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param toolWaitCheck
	 * @return
	 */
	Pagination<ToolWaitCheck> selectPageList(Long userId, ToolWaitCheck toolWaitCheck,QueryDto queryDto) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param toolWaitCheck
	 * @return
	 */
	Pagination<ToolWaitCheck> selectComposePageList(Long userId, ToolWaitCheck toolWaitCheck,QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param toolWaitCheckId
	 * @return
	 */
	ToolWaitCheck selectById(Long userId, Long toolWaitCheckId) throws BusinessException;

	
	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param toolWaitCheckId
	 * @return
	 */
	Integer deleteById(Long userId, Long toolWaitCheckId) throws BusinessException;
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param toolWaitCheckId
	 * @return
	 */
	Integer removeById(Long userId, Long toolWaitCheckId) throws BusinessException;
}
