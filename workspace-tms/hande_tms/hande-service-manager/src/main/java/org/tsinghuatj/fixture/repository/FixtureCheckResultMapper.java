package org.tsinghuatj.fixture.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;

import org.tsinghuatj.fixture.domain.FixtureCheckResult;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface FixtureCheckResultMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param fixtureCheckResult
	 * @return
	 */
	Integer insert(@Param("fixtureCheckResult") FixtureCheckResult fixtureCheckResult);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param fixtureCheckResult
	 * @param fixtureCheckResultId
	 * @return
	 */
	Integer updateActiveById(@Param("fixtureCheckResult") FixtureCheckResult fixtureCheckResult, @Param("fixtureCheckResultId") Long fixtureCheckResultId);

	/**
	 * 查询列表
	 * 
	 * @param fixtureCheckResult
	 * @return
	 */
	List<FixtureCheckResult> select(@Param("fixtureCheckResult") FixtureCheckResult fixtureCheckResult);
	
	
	List<FixtureCheckResult> selectByCheckId(@Param("checkId") Long checkId);
	
	/**
	 * 分页查询列表
	 * 
	 * @param fixtureCheckResult
	 * @return
	 */
	Page<FixtureCheckResult> selectPageList(@Param("fixtureCheckResult") FixtureCheckResult fixtureCheckResult,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param fixtureCheckResultId
	 * @return
	 */
	FixtureCheckResult selectById(@Param("fixtureCheckResultId") Long fixtureCheckResultId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param fixtureCheckResultId
	 * @return
	 */
	Integer deleteById(@Param("fixtureCheckResult") FixtureCheckResult fixtureCheckResult);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param fixtureCheckResultId
	 * @return
	 */
	Integer removeById(@Param("fixtureCheckResultId") Long fixtureCheckResultId);
}
