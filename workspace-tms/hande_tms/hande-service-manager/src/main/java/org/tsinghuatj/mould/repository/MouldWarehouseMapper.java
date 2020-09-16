package org.tsinghuatj.mould.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.mould.domain.MouldWarehouse;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface MouldWarehouseMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param mouldWarehouse
	 * @return
	 */
	Integer insert(@Param("mouldWarehouse") MouldWarehouse mouldWarehouse);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param mouldWarehouse
	 * @param mouldWarehouseId
	 * @return
	 */
	Integer updateActiveById(@Param("mouldWarehouse") MouldWarehouse mouldWarehouse, @Param("mouldWarehouseId") Long mouldWarehouseId);

	/**
	 * 查询列表
	 * 
	 * @param mouldWarehouse
	 * @return
	 */
	List<MouldWarehouse> select(@Param("mouldWarehouse") MouldWarehouse mouldWarehouse);
	
	/**
	 * 分页查询列表
	 * 
	 * @param mouldWarehouse
	 * @return
	 */
	Page<MouldWarehouse> selectPageList(@Param("mouldWarehouse") MouldWarehouse mouldWarehouse,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param mouldWarehouseId
	 * @return
	 */
	MouldWarehouse selectById(@Param("mouldWarehouseId") Long mouldWarehouseId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param mouldWarehouseId
	 * @return
	 */
	Integer deleteById(@Param("mouldWarehouse") MouldWarehouse mouldWarehouse);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param mouldWarehouseId
	 * @return
	 */
	Integer removeById(@Param("mouldWarehouseId") Long mouldWarehouseId);
	
	/**
	 * 查询列表
	 * 
	 * @param mouldIdList
	 * @return
	 */
	List<MouldWarehouse> selectByMouldIdList(@Param("mouldIdList") List<Long> mouldIdList);
}
