package org.tsinghuatj.tool.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.tool.domain.ToolBladeProcess;

/**
 * @ClassName: IToolBladeProcessService
 * @Description: ToolBladeProcess服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IToolBladeProcessService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param toolBladeProcess
	 * @return
	 */
	Integer insert(Long userId, ToolBladeProcess toolBladeProcess) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param toolBladeProcess
	 * @param toolBladeProcessId
	 * @return
	 */
	Integer updateActiveById(Long userId, ToolBladeProcess toolBladeProcess, Long toolBladeProcessId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param toolBladeProcess
	 * @return
	 */
	List<ToolBladeProcess> select(Long userId, ToolBladeProcess toolBladeProcess) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param toolBladeProcess
	 * @return
	 */
	Pagination<ToolBladeProcess> selectPageList(Long userId, ToolBladeProcess toolBladeProcess,QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param toolBladeProcessId
	 * @return
	 */
	ToolBladeProcess selectById(Long userId, Long toolBladeProcessId) throws BusinessException;

	
	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param toolBladeProcessId
	 * @return
	 */
	Integer deleteById(Long userId, Long toolBladeProcessId) throws BusinessException;
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param toolBladeProcessId
	 * @return
	 */
	Integer removeById(Long userId, Long toolBladeProcessId) throws BusinessException;
}
