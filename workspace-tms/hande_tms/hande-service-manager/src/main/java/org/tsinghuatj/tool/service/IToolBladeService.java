package org.tsinghuatj.tool.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.tool.domain.ToolBlade;

/**
 * @ClassName: IToolBladeService
 * @Description: ToolBlade服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IToolBladeService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param toolBlade
	 * @return
	 */
	Integer insert(Long userId, ToolBlade toolBlade) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param toolBlade
	 * @param toolBladeId
	 * @return
	 */
	Integer updateActiveById(Long userId, ToolBlade toolBlade, Long toolBladeId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param toolBlade
	 * @return
	 */
	List<ToolBlade> select(Long userId, ToolBlade toolBlade) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param toolBlade
	 * @return
	 */
	Pagination<ToolBlade> selectPageList(Long userId, ToolBlade toolBlade,QueryDto queryDto) throws BusinessException;
	
	

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param toolBladeId
	 * @return
	 */
	ToolBlade selectById(Long userId, Long toolBladeId) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param toolBladeId
	 * @return
	 */
	ToolBlade selectByNumber(Long userId, String number) throws BusinessException;

	
	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param toolBladeId
	 * @return
	 */
	Integer deleteById(Long userId, Long toolBladeId) throws BusinessException;
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param toolBladeId
	 * @return
	 */
	Integer removeById(Long userId, Long toolBladeId) throws BusinessException;
	
	/**
	 * 
	 * @param userId
	 * @param bladeList
	 * @return
	 * @throws BusinessException
	 */
	Integer toolBladeImport(Long userId, List<ToolBlade> bladeList) throws BusinessException;
}
