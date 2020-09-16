package org.tsinghuatj.fixture.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;

import org.tsinghuatj.fixture.domain.FixtureRepair;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface FixtureRepairMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param fixtureRepair
	 * @return
	 */
	Integer insert(@Param("fixtureRepair") FixtureRepair fixtureRepair);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param fixtureRepair
	 * @param fixtureRepairId
	 * @return
	 */
	Integer updateActiveById(@Param("fixtureRepair") FixtureRepair fixtureRepair, @Param("fixtureRepairId") Long fixtureRepairId);

	/**
	 * 查询列表
	 * 
	 * @param fixtureRepair
	 * @return
	 */
	List<FixtureRepair> select(@Param("fixtureRepair") FixtureRepair fixtureRepair);
	
	/**
	 * 分页查询列表
	 * 
	 * @param fixtureRepair
	 * @return
	 */
	Page<FixtureRepair> selectPageList(@Param("fixtureRepair") FixtureRepair fixtureRepair,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param fixtureRepairId
	 * @return
	 */
	FixtureRepair selectById(@Param("fixtureRepairId") Long fixtureRepairId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param fixtureRepairId
	 * @return
	 */
	Integer deleteById(@Param("fixtureRepair") FixtureRepair fixtureRepair);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param fixtureRepairId
	 * @return
	 */
	Integer removeById(@Param("fixtureRepairId") Long fixtureRepairId);
}
