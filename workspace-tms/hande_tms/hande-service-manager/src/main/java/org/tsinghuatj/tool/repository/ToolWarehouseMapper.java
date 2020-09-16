package org.tsinghuatj.tool.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;

import org.tsinghuatj.tool.domain.ToolWarehouse;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface ToolWarehouseMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param toolWarehouse
	 * @return
	 */
	Integer insert(@Param("toolWarehouse") ToolWarehouse toolWarehouse);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param toolWarehouse
	 * @param toolWarehouseId
	 * @return
	 */
	Integer updateActiveById(@Param("toolWarehouse") ToolWarehouse toolWarehouse, @Param("toolWarehouseId") Long toolWarehouseId);

	/**
	 * 查询列表
	 * 
	 * @param toolWarehouse
	 * @return
	 */
	List<ToolWarehouse> select(@Param("toolWarehouse") ToolWarehouse toolWarehouse);
	
	/**
	 * 分页查询列表
	 * 
	 * @param toolWarehouse
	 * @return
	 */
	Page<ToolWarehouse> selectPageList(@Param("toolWarehouse") ToolWarehouse toolWarehouse,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param toolWarehouseId
	 * @return
	 */
	ToolWarehouse selectById(@Param("toolWarehouseId") Long toolWarehouseId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param toolWarehouseId
	 * @return
	 */
	Integer deleteById(@Param("toolWarehouse") ToolWarehouse toolWarehouse);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param toolWarehouseId
	 * @return
	 */
	Integer removeById(@Param("toolWarehouseId") Long toolWarehouseId);
}
