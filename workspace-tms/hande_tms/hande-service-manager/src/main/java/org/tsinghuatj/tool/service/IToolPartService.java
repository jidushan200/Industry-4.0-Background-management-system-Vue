package org.tsinghuatj.tool.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.tool.domain.ToolPart;

/**
 * @ClassName: IToolPartService
 * @Description: ToolPart服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IToolPartService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param toolPart
	 * @return
	 */
	Integer insert(Long userId, ToolPart toolPart) throws BusinessException;

	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param toolPart
	 * @param toolPartId
	 * @return
	 */
	Integer updateActiveById(Long userId, ToolPart toolPart, Long toolPartId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param toolPart
	 * @return
	 */
	List<ToolPart> select(Long userId, ToolPart toolPart) throws BusinessException;

	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param toolPart
	 * @return
	 */
	Pagination<ToolPart> selectPageList(Long userId, ToolPart toolPart, QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param toolPartId
	 * @return
	 */
	ToolPart selectById(Long userId, Long toolPartId) throws BusinessException;

	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param toolPartId
	 * @return
	 */
	Integer deleteById(Long userId, Long toolPartId) throws BusinessException;

	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param toolPartId
	 * @return
	 */
	Integer removeById(Long userId, Long toolPartId) throws BusinessException;

	/**
	 * 
	 * @param pkId
	 * @param toolId
	 * @param partId
	 * @return
	 * @throws BusinessException
	 */
	boolean checkToolPart(Long pkId, Long toolId, Long partId) throws BusinessException;
	
	/**
	 * 
	 * @param userId
	 * @param tpList
	 * @return
	 * @throws BusinessException
	 */
	Integer toolPartImport(Long userId, List<ToolPart> tpList) throws BusinessException;
}
