package org.tsinghuatj.tool.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.tool.domain.ToolType;

/**
 * @ClassName: IToolTypeService
 * @Description: ToolType服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IToolTypeService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param toolType
	 * @return
	 */
	Integer insert(Long userId, ToolType toolType) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param toolType
	 * @param toolTypeId
	 * @return
	 */
	Integer updateActiveById(Long userId, ToolType toolType, Long toolTypeId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param toolType
	 * @return
	 */
	List<ToolType> select(Long userId, ToolType toolType) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param toolType
	 * @return
	 */
	Pagination<ToolType> selectPageList(Long userId, ToolType toolType,QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param toolTypeId
	 * @return
	 */
	ToolType selectById(Long userId, Long toolTypeId) throws BusinessException;

	
	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param toolTypeId
	 * @return
	 */
	Integer deleteById(Long userId, Long toolTypeId) throws BusinessException;
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param toolTypeId
	 * @return
	 */
	Integer removeById(Long userId, Long toolTypeId) throws BusinessException;
}
