package org.tsinghuatj.tool.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.tool.domain.ToolWarehouse;

/**
 * @ClassName: IToolWarehouseService
 * @Description: ToolWarehouse服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IToolWarehouseService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param toolWarehouse
	 * @return
	 */
	Integer insert(Long userId, ToolWarehouse toolWarehouse) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param toolWarehouse
	 * @param toolWarehouseId
	 * @return
	 */
	Integer updateActiveById(Long userId, ToolWarehouse toolWarehouse, Long toolWarehouseId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param toolWarehouse
	 * @return
	 */
	List<ToolWarehouse> select(Long userId, ToolWarehouse toolWarehouse) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param toolWarehouse
	 * @return
	 */
	Pagination<ToolWarehouse> selectPageList(Long userId, ToolWarehouse toolWarehouse,QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param toolWarehouseId
	 * @return
	 */
	ToolWarehouse selectById(Long userId, Long toolWarehouseId) throws BusinessException;

	
	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param toolWarehouseId
	 * @return
	 */
	Integer deleteById(Long userId, Long toolWarehouseId) throws BusinessException;
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param toolWarehouseId
	 * @return
	 */
	Integer removeById(Long userId, Long toolWarehouseId) throws BusinessException;
}
