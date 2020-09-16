package org.tsinghuatj.tool.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;

import org.tsinghuatj.tool.domain.ToolBladeWarehouse;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface ToolBladeWarehouseMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param toolBladeWarehouse
	 * @return
	 */
	Integer insert(@Param("toolBladeWarehouse") ToolBladeWarehouse toolBladeWarehouse);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param toolBladeWarehouse
	 * @param toolBladeWarehouseId
	 * @return
	 */
	Integer updateActiveById(@Param("toolBladeWarehouse") ToolBladeWarehouse toolBladeWarehouse, @Param("toolBladeWarehouseId") Long toolBladeWarehouseId);

	/**
	 * 查询列表
	 * 
	 * @param toolBladeWarehouse
	 * @return
	 */
	List<ToolBladeWarehouse> select(@Param("toolBladeWarehouse") ToolBladeWarehouse toolBladeWarehouse);
	
	/**
	 * 分页查询列表
	 * 
	 * @param toolBladeWarehouse
	 * @return
	 */
	Page<ToolBladeWarehouse> selectPageList(@Param("toolBladeWarehouse") ToolBladeWarehouse toolBladeWarehouse,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param toolBladeWarehouseId
	 * @return
	 */
	ToolBladeWarehouse selectById(@Param("toolBladeWarehouseId") Long toolBladeWarehouseId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param toolBladeWarehouseId
	 * @return
	 */
	Integer deleteById(@Param("toolBladeWarehouse") ToolBladeWarehouse toolBladeWarehouse);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param toolBladeWarehouseId
	 * @return
	 */
	Integer removeById(@Param("toolBladeWarehouseId") Long toolBladeWarehouseId);
}
