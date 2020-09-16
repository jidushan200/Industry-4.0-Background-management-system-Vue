package org.tsinghuatj.tool.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.tool.domain.ToolAppendix;

/**
 * @ClassName: IToolAppendixService
 * @Description: ToolAppendix服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IToolAppendixService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param toolAppendix
	 * @return
	 */
	Integer insert(Long userId, ToolAppendix toolAppendix) throws BusinessException;

	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param toolAppendix
	 * @param toolAppendixId
	 * @return
	 */
	Integer updateActiveById(Long userId, ToolAppendix toolAppendix, Long toolAppendixId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param toolAppendix
	 * @return
	 */
	List<ToolAppendix> select(Long userId, ToolAppendix toolAppendix) throws BusinessException;

	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param toolAppendix
	 * @return
	 */
	Pagination<ToolAppendix> selectPageList(Long userId, ToolAppendix toolAppendix, QueryDto queryDto)
			throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param toolAppendixId
	 * @return
	 */
	ToolAppendix selectById(Long userId, Long toolAppendixId) throws BusinessException;

	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param toolAppendixId
	 * @return
	 */
	Integer deleteById(Long userId, Long toolAppendixId) throws BusinessException;

	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param toolAppendixId
	 * @return
	 */
	Integer removeById(Long userId, Long toolAppendixId) throws BusinessException;

	Long getNextPkId() throws BusinessException;
}
