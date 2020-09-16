package org.tsinghuatj.measure.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.measure.domain.MeasureWarehouse;

/**
 * @ClassName: IMeasureWarehouseService
 * @Description: MeasureWarehouse服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IMeasureWarehouseService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param measureWarehouse
	 * @return
	 */
	Integer insert(Long userId, MeasureWarehouse measureWarehouse) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param measureWarehouse
	 * @param measureWarehouseId
	 * @return
	 */
	Integer updateActiveById(Long userId, MeasureWarehouse measureWarehouse, Long measureWarehouseId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param measureWarehouse
	 * @return
	 */
	List<MeasureWarehouse> select(Long userId, MeasureWarehouse measureWarehouse) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param measureWarehouse
	 * @return
	 */
	Pagination<MeasureWarehouse> selectPageList(Long userId, MeasureWarehouse measureWarehouse,QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param measureWarehouseId
	 * @return
	 */
	MeasureWarehouse selectById(Long userId, Long measureWarehouseId) throws BusinessException;

	
	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param measureWarehouseId
	 * @return
	 */
	Integer deleteById(Long userId, Long measureWarehouseId) throws BusinessException;
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param measureWarehouseId
	 * @return
	 */
	Integer removeById(Long userId, Long measureWarehouseId) throws BusinessException;
}
