package org.tsinghuatj.fixture.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.fixture.domain.FixtureBase;
import org.tsinghuatj.framework.domain.QueryDto;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface FixtureBaseMapper {

	/**
	 * 插入数据
	 * 
	 * @param fixtureBase
	 * @return
	 */
	Integer insert(@Param("fixtureBase") FixtureBase fixtureBase);

	/**
	 * 根据主键更新有值数据
	 * 
	 * @param fixtureBase
	 * @param fixtureBaseId
	 * @return
	 */
	Integer updateActiveById(@Param("fixtureBase") FixtureBase fixtureBase, @Param("fixtureBaseId") Long fixtureBaseId);

	/**
	 * 查询列表
	 * 
	 * @param fixtureBase
	 * @return
	 */
	List<FixtureBase> select(@Param("fixtureBase") FixtureBase fixtureBase);
	
	/**
	 * 查询列表
	 * 
	 * @param fixtureBase
	 * @return
	 */
	List<FixtureBase> selectByNumberList(@Param("numberList") List<String> numberList);

	/**
	 * 分页查询列表
	 * 
	 * @param fixtureBase
	 * @return
	 */
	Page<FixtureBase> selectPageList(@Param("fixtureBase") FixtureBase fixtureBase, @Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param fixtureBaseId
	 * @return
	 */
	FixtureBase selectById(@Param("fixtureBaseId") Long fixtureBaseId);

	/**
	 * 按主键物理删除
	 * 
	 * @param fixtureBaseId
	 * @return
	 */
	Integer deleteById(@Param("fixtureBase") FixtureBase fixtureBase);

	/**
	 * 按主键物理删除
	 * 
	 * @param fixtureBaseId
	 * @return
	 */
	Integer removeById(@Param("fixtureBaseId") Long fixtureBaseId);

	/**
	 * 按编码查询
	 * 
	 * @param fixtureNumber
	 * @return
	 */
	FixtureBase selectByNumber(@Param("fixtureNumber") String fixtureNumber, @Param("pkId") Long pkId);

	Integer countByFixtureNumber(@Param("fixtureNumber") String fixtureNumber);
	
	
	FixtureBase selectByFixtureMap(@Param("fixtureMap") String fixtureMap);

}
