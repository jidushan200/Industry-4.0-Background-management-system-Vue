package org.tsinghuatj.fixture.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.fixture.domain.FixtureScripApply;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface FixtureScripApplyMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param fixtureScripApply
	 * @return
	 */
	Integer insert(@Param("fixtureScripApply") FixtureScripApply fixtureScripApply);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param fixtureScripApply
	 * @param fixtureScripApplyId
	 * @return
	 */
	Integer updateActiveById(@Param("fixtureScripApply") FixtureScripApply fixtureScripApply, @Param("fixtureScripApplyId") Long fixtureScripApplyId);

	/**
	 * 查询列表
	 * 
	 * @param fixtureScripApply
	 * @return
	 */
	List<FixtureScripApply> select(@Param("fixtureScripApply") FixtureScripApply fixtureScripApply);
	
	/**
	 * 分页查询列表
	 * 
	 * @param fixtureScripApply
	 * @return
	 */
	Page<FixtureScripApply> selectPageList(@Param("fixtureScripApply") FixtureScripApply fixtureScripApply,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param fixtureScripApplyId
	 * @return
	 */
	FixtureScripApply selectById(@Param("fixtureScripApplyId") Long fixtureScripApplyId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param fixtureScripApplyId
	 * @return
	 */
	Integer deleteById(@Param("fixtureScripApply") FixtureScripApply fixtureScripApply);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param fixtureScripApplyId
	 * @return
	 */
	Integer removeById(@Param("fixtureScripApplyId") Long fixtureScripApplyId);
	
	
	/**
	 * 根据主键查询
	 * 
	 * @param fixtureBarcode
	 * @return
	 */
	FixtureScripApply applyGetByfullNumber(@Param("fixtureBarcode") String fixtureBarcode);
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param fixtureScripApply
	 * @param fixtureScripApplyId
	 * @return
	 */
	Integer updateActiveByFixtureId(@Param("fixtureScripApply") FixtureScripApply fixtureScripApply);

}
