package org.tsinghuatj.measure.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.measure.domain.MeasureWarehouse;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface MeasureWarehouseMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param measureWarehouse
	 * @return
	 */
	Integer insert(@Param("measureWarehouse") MeasureWarehouse measureWarehouse);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param measureWarehouse
	 * @param measureWarehouseId
	 * @return
	 */
	Integer updateActiveById(@Param("measureWarehouse") MeasureWarehouse measureWarehouse, @Param("measureWarehouseId") Long measureWarehouseId);

	/**
	 * 查询列表
	 * 
	 * @param measureWarehouse
	 * @return
	 */
	List<MeasureWarehouse> select(@Param("measureWarehouse") MeasureWarehouse measureWarehouse);
	
	/**
	 * 分页查询列表
	 * 
	 * @param measureWarehouse
	 * @return
	 */
	Page<MeasureWarehouse> selectPageList(@Param("measureWarehouse") MeasureWarehouse measureWarehouse,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param measureWarehouseId
	 * @return
	 */
	MeasureWarehouse selectById(@Param("measureWarehouseId") Long measureWarehouseId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param measureWarehouseId
	 * @return
	 */
	Integer deleteById(@Param("measureWarehouse") MeasureWarehouse measureWarehouse);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param measureWarehouseId
	 * @return
	 */
	Integer removeById(@Param("measureWarehouseId") Long measureWarehouseId);
	
	/**
	 * 查询列表
	 * 
	 * @param measureIdList
	 * @return
	 */
	List<MeasureWarehouse> selectByMeasureIdList(@Param("measureIdList") List<Long> measureIdList);
}
