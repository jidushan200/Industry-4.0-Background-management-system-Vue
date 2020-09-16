package org.tsinghuatj.mould.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.mould.domain.MouldWarehouse;

/**
 * @ClassName: IMouldWarehouseService
 * @Description: MouldWarehouse服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IMouldWarehouseService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param mouldWarehouse
	 * @return
	 */
	Integer insert(Long userId, MouldWarehouse mouldWarehouse) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param mouldWarehouse
	 * @param mouldWarehouseId
	 * @return
	 */
	Integer updateActiveById(Long userId, MouldWarehouse mouldWarehouse, Long mouldWarehouseId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param mouldWarehouse
	 * @return
	 */
	List<MouldWarehouse> select(Long userId, MouldWarehouse mouldWarehouse) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param mouldWarehouse
	 * @return
	 */
	Pagination<MouldWarehouse> selectPageList(Long userId, MouldWarehouse mouldWarehouse,QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param mouldWarehouseId
	 * @return
	 */
	MouldWarehouse selectById(Long userId, Long mouldWarehouseId) throws BusinessException;

	
	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param mouldWarehouseId
	 * @return
	 */
	Integer deleteById(Long userId, Long mouldWarehouseId) throws BusinessException;
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param mouldWarehouseId
	 * @return
	 */
	Integer removeById(Long userId, Long mouldWarehouseId) throws BusinessException;
}
