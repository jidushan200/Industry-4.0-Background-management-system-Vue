package org.tsinghuatj.tool.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.tool.domain.ToolCoat;

/**
 * @ClassName: IToolCoatService
 * @Description: ToolCoat服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IToolCoatService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param toolCoat
	 * @return
	 */
	Integer insert(Long userId, ToolCoat toolCoat) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param toolCoat
	 * @param toolCoatId
	 * @return
	 */
	Integer updatecoatSettlement(Long userId, String settlementList) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param toolCoat
	 * @param toolCoatId
	 * @return
	 */
	Integer updateActiveById(Long userId, ToolCoat toolCoat, Long toolCoatId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param toolCoat
	 * @return
	 */
	List<ToolCoat> select(Long userId, ToolCoat toolCoat) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param toolCoat
	 * @return
	 */
	Pagination<ToolCoat> selectPageList(Long userId, ToolCoat toolCoat,QueryDto queryDto) throws BusinessException;
	
	/**
	 * 涂层结算分页查询列表
	 * 
	 * @param userId
	 * @param toolCoat
	 * @return
	 */
	Pagination<ToolCoat> selectStatisticsPageList(Long userId, ToolCoat toolCoat,QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param toolCoatId
	 * @return
	 */
	ToolCoat selectById(Long userId, Long toolCoatId) throws BusinessException;

	
	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param toolCoatId
	 * @return
	 */
	Integer deleteById(Long userId, Long toolCoatId) throws BusinessException;
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param toolCoatId
	 * @return
	 */
	Integer removeById(Long userId, Long toolCoatId) throws BusinessException;
}
