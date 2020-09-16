package org.tsinghuatj.tool.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.tool.domain.ToolBladeWarehouse;

/**
 * @ClassName: IToolBladeWarehouseService
 * @Description: ToolBladeWarehouse服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IToolBladeWarehouseService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param toolBladeWarehouse
	 * @return
	 */
	Integer insert(Long userId, String realname, ToolBladeWarehouse toolBladeWarehouse, Long checkId) throws BusinessException;

	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param toolBladeWarehouse
	 * @param toolBladeWarehouseId
	 * @return
	 */
	Integer updateActiveById(Long userId, ToolBladeWarehouse toolBladeWarehouse, Long toolBladeWarehouseId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param toolBladeWarehouse
	 * @return
	 */
	List<ToolBladeWarehouse> select(Long userId, ToolBladeWarehouse toolBladeWarehouse) throws BusinessException;

	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param toolBladeWarehouse
	 * @return
	 */
	Pagination<ToolBladeWarehouse> selectPageList(Long userId, ToolBladeWarehouse toolBladeWarehouse, QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param toolBladeWarehouseId
	 * @return
	 */
	ToolBladeWarehouse selectById(Long userId, Long toolBladeWarehouseId) throws BusinessException;

	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param toolBladeWarehouseId
	 * @return
	 */
	Integer deleteById(Long userId, Long toolBladeWarehouseId) throws BusinessException;

	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param toolBladeWarehouseId
	 * @return
	 */
	Integer removeById(Long userId, Long toolBladeWarehouseId) throws BusinessException;
}
