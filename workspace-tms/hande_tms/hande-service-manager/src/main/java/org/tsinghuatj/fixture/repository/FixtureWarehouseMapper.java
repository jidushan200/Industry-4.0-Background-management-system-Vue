package org.tsinghuatj.fixture.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;

import org.tsinghuatj.fixture.domain.FixtureWarehouse;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface FixtureWarehouseMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param fixtureWarehouse
	 * @return
	 */
	Integer insert(@Param("fixtureWarehouse") FixtureWarehouse fixtureWarehouse);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param fixtureWarehouse
	 * @param fixtureWarehouseId
	 * @return
	 */
	Integer updateActiveById(@Param("fixtureWarehouse") FixtureWarehouse fixtureWarehouse, @Param("fixtureWarehouseId") Long fixtureWarehouseId);

	/**
	 * 查询列表
	 * 
	 * @param fixtureWarehouse
	 * @return
	 */
	List<FixtureWarehouse> select(@Param("fixtureWarehouse") FixtureWarehouse fixtureWarehouse);
	
	/**
	 * 分页查询列表
	 * 
	 * @param fixtureWarehouse
	 * @return
	 */
	Page<FixtureWarehouse> selectPageList(@Param("fixtureWarehouse") FixtureWarehouse fixtureWarehouse,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param fixtureWarehouseId
	 * @return
	 */
	FixtureWarehouse selectById(@Param("fixtureWarehouseId") Long fixtureWarehouseId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param fixtureWarehouseId
	 * @return
	 */
	Integer deleteById(@Param("fixtureWarehouse") FixtureWarehouse fixtureWarehouse);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param fixtureWarehouseId
	 * @return
	 */
	Integer removeById(@Param("fixtureWarehouseId") Long fixtureWarehouseId);
}
