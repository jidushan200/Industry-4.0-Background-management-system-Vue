package org.tsinghuatj.fixture.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;

import org.tsinghuatj.fixture.domain.FixtureCheck;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface FixtureCheckMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param fixtureCheck
	 * @return
	 */
	Integer insert(@Param("fixtureCheck") FixtureCheck fixtureCheck);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param fixtureCheck
	 * @param fixtureCheckId
	 * @return
	 */
	Integer updateActiveById(@Param("fixtureCheck") FixtureCheck fixtureCheck, @Param("fixtureCheckId") Long fixtureCheckId);

	/**
	 * 查询列表
	 * 
	 * @param fixtureCheck
	 * @return
	 */
	List<FixtureCheck> select(@Param("fixtureCheck") FixtureCheck fixtureCheck);
	
	/**
	 * 分页查询列表
	 * 
	 * @param fixtureCheck
	 * @return
	 */
	Page<FixtureCheck> selectPageList(@Param("fixtureCheck") FixtureCheck fixtureCheck,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param fixtureCheckId
	 * @return
	 */
	FixtureCheck selectById(@Param("fixtureCheckId") Long fixtureCheckId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param fixtureCheckId
	 * @return
	 */
	Integer deleteById(@Param("fixtureCheck") FixtureCheck fixtureCheck);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param fixtureCheckId
	 * @return
	 */
	Integer removeById(@Param("fixtureCheckId") Long fixtureCheckId);
}
