package org.tsinghuatj.fixture.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.fixture.domain.FixturePart;
import org.tsinghuatj.framework.domain.QueryDto;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface FixturePartMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param fixturePart
	 * @return
	 */
	Integer insert(@Param("fixturePart") FixturePart fixturePart);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param fixturePart
	 * @param fixturePartId
	 * @return
	 */
	Integer updateActiveById(@Param("fixturePart") FixturePart fixturePart, @Param("fixturePartId") Long fixturePartId);

	/**
	 * 查询列表
	 * 
	 * @param fixturePart
	 * @return
	 */
	List<FixturePart> select(@Param("fixturePart") FixturePart fixturePart);
	
	/**
	 * 分页查询列表
	 * 
	 * @param fixturePart
	 * @return
	 */
	Page<FixturePart> selectPageList(@Param("fixturePart") FixturePart fixturePart,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param fixturePartId
	 * @return
	 */
	FixturePart selectById(@Param("fixturePartId") Long fixturePartId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param fixturePartId
	 * @return
	 */
	Integer deleteById(@Param("fixturePart") FixturePart fixturePart);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param fixturePartId
	 * @return
	 */
	Integer removeById(@Param("fixturePartId") Long fixturePartId);
	
	/**
	 * 检查
	 * @param pkId
	 * @param toolId
	 * @param partId
	 * @return
	 */
	FixturePart checkFixturePart(@Param("pkId") Long pkId, @Param("fixtureId") Long fixtureId, @Param("partId") Long partId);
}
