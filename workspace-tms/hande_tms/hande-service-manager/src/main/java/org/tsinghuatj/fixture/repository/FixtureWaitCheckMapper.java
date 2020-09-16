package org.tsinghuatj.fixture.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;

import org.tsinghuatj.fixture.domain.FixtureWaitCheck;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface FixtureWaitCheckMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param fixtureWaitCheck
	 * @return
	 */
	Integer insert(@Param("fixtureWaitCheck") FixtureWaitCheck fixtureWaitCheck);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param fixtureWaitCheck
	 * @param fixtureWaitCheckId
	 * @return
	 */
	Integer updateActiveById(@Param("fixtureWaitCheck") FixtureWaitCheck fixtureWaitCheck, @Param("fixtureWaitCheckId") Long fixtureWaitCheckId);

	/**
	 * 查询列表
	 * 
	 * @param fixtureWaitCheck
	 * @return
	 */
	List<FixtureWaitCheck> select(@Param("fixtureWaitCheck") FixtureWaitCheck fixtureWaitCheck);
	
	/**
	 * 分页查询列表
	 * 
	 * @param fixtureWaitCheck
	 * @return
	 */
	Page<FixtureWaitCheck> selectPageList(@Param("fixtureWaitCheck") FixtureWaitCheck fixtureWaitCheck,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param fixtureWaitCheckId
	 * @return
	 */
	FixtureWaitCheck selectById(@Param("fixtureWaitCheckId") Long fixtureWaitCheckId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param fixtureWaitCheckId
	 * @return
	 */
	Integer deleteById(@Param("fixtureWaitCheck") FixtureWaitCheck fixtureWaitCheck);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param fixtureWaitCheckId
	 * @return
	 */
	Integer removeById(@Param("fixtureWaitCheckId") Long fixtureWaitCheckId);
}
