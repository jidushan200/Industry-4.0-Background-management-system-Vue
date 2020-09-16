package org.tsinghuatj.tool.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.tool.domain.ToolUnusual;

/**
 * @ClassName: IToolUnusualService
 * @Description: ToolUnusual服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IToolUnusualService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param toolUnusual
	 * @return
	 */
	Integer insert(Long userId, ToolUnusual toolUnusual) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param toolUnusual
	 * @param toolUnusualId
	 * @return
	 */
	Integer updateActiveById(Long userId, ToolUnusual toolUnusual, Long toolUnusualId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param toolUnusual
	 * @return
	 */
	List<ToolUnusual> select(Long userId, ToolUnusual toolUnusual) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param toolUnusual
	 * @return
	 */
	Pagination<ToolUnusual> selectPageList(Long userId, ToolUnusual toolUnusual,QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param toolUnusualId
	 * @return
	 */
	ToolUnusual selectById(Long userId, Long toolUnusualId) throws BusinessException;

	
	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param toolUnusualId
	 * @return
	 */
	Integer deleteById(Long userId, Long toolUnusualId) throws BusinessException;
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param toolUnusualId
	 * @return
	 */
	Integer removeById(Long userId, Long toolUnusualId) throws BusinessException;
	/*
	 * 刀具异常报告导入
	 */
	
	Integer unusualImport(Long userId, List<ToolUnusual> unusualList) throws BusinessException;
}
