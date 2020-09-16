package org.tsinghuatj.fixture.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.fixture.domain.FixtureBaseChild;
import org.tsinghuatj.framework.domain.QueryDto;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface FixtureBaseChildMapper {

	/**
	 * 插入数据
	 * 
	 * @param fixtureBaseChild
	 * @return
	 */
	Integer insert(@Param("fixtureBaseChild") FixtureBaseChild fixtureBaseChild);

	/**
	 * 根据主键更新有值数据
	 * 
	 * @param fixtureBaseChild
	 * @param fixtureBaseChildId
	 * @return
	 */
	Integer updateActiveById(@Param("fixtureBaseChild") FixtureBaseChild fixtureBaseChild, @Param("fixtureBaseChildId") Long fixtureBaseChildId);

	/**
	 * 查询列表
	 * 
	 * @param fixtureBaseChild
	 * @return
	 */
	List<FixtureBaseChild> select(@Param("fixtureBaseChild") FixtureBaseChild fixtureBaseChild);

	/**
	 * 分页查询列表
	 * 
	 * @param fixtureBaseChild
	 * @return
	 */
	Page<FixtureBaseChild> selectPageList(@Param("fixtureBaseChild") FixtureBaseChild fixtureBaseChild, @Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param fixtureBaseChildId
	 * @return
	 */
	FixtureBaseChild selectById(@Param("fixtureBaseChildId") Long fixtureBaseChildId);

	/**
	 * 按主键物理删除
	 * 
	 * @param fixtureBaseChildId
	 * @return
	 */
	Integer deleteById(@Param("fixtureBaseChild") FixtureBaseChild fixtureBaseChild);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param fixtureBaseChildId
	 * @return
	 */
	Integer deleteByParentNumber(@Param("fixtureBaseChild") FixtureBaseChild fixtureBaseChild);

	/**
	 * 按主键物理删除
	 * 
	 * @param fixtureBaseChildId
	 * @return
	 */
	Integer removeById(@Param("fixtureBaseChildId") Long fixtureBaseChildId);

	/**
	 * 查询列表
	 * 
	 * @param fixtureBaseChild
	 * @return
	 */
	List<FixtureBaseChild> selectByParentList(@Param("parentList") List<String> parentList);

	/**
	 * 查询列表
	 * 
	 * @param fixtureBaseChild
	 * @return
	 */
	List<FixtureBaseChild> selectByParenNumber(@Param("parentNumber") String parentNumber);
	
	/**
	 * 查询列表
	 * 
	 * @param fixtureBaseChild
	 * @return
	 */
	int countByFixtureNumber(@Param("fixtureNumber") String fixtureNumber);
}
