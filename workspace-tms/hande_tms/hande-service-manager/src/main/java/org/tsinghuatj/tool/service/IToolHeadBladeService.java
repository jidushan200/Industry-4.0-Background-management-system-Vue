package org.tsinghuatj.tool.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.tool.domain.ToolHeadBlade;

/**
 * @ClassName: IToolHeadBladeService
 * @Description: ToolHeadBlade服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IToolHeadBladeService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param toolHeadBlade
	 * @return
	 */
	Integer insert(Long userId, ToolHeadBlade toolHeadBlade) throws BusinessException;

	/**
	 * 导入
	 * 
	 * @param userId
	 * @param toolHeadBlade
	 * @return
	 */
	Integer headBladeImport(Long userId, List<ToolHeadBlade> headBladeList) throws BusinessException;

	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param toolHeadBlade
	 * @param toolHeadBladeId
	 * @return
	 */
	Integer updateActiveById(Long userId, ToolHeadBlade toolHeadBlade, Long toolHeadBladeId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param toolHeadBlade
	 * @return
	 */
	List<ToolHeadBlade> select(Long userId, ToolHeadBlade toolHeadBlade) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param toolHeadBlade
	 * @return
	 */
	Pagination<ToolHeadBlade> selectPageList(Long userId, ToolHeadBlade toolHeadBlade, QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param toolHeadBladeId
	 * @return
	 */
	ToolHeadBlade selectById(Long userId, Long toolHeadBladeId) throws BusinessException;

	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param toolHeadBladeId
	 * @return
	 */
	Integer deleteById(Long userId, Long toolHeadBladeId) throws BusinessException;

	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param toolHeadBladeId
	 * @return
	 */
	Integer removeById(Long userId, Long toolHeadBladeId) throws BusinessException;
	

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param toolHeadBlade
	 * @return
	 */
	List<ToolHeadBlade> getComplexheadBladeList(Long userId, ToolHeadBlade toolHeadBlade) throws BusinessException;
}
