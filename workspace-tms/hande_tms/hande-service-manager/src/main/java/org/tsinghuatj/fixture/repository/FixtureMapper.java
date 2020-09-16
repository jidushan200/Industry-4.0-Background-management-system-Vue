package org.tsinghuatj.fixture.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.fixture.domain.Fixture;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface FixtureMapper {

	/**
	 * 插入数据
	 * 
	 * @param fixture
	 * @return
	 */
	Integer insert(@Param("fixture") Fixture fixture);

	/**
	 * 根据主键更新有值数据
	 * 
	 * @param fixture
	 * @param fixtureId
	 * @return
	 */
	Integer updateActiveById(@Param("fixture") Fixture fixture, @Param("fixtureId") Long fixtureId);
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param fixture
	 * @param fixtureId
	 * @return
	 */
	Integer updateActiveByFixtureBarcode(@Param("fixture") Fixture fixture);

	Integer updateReturnWarehouse(@Param("fixture") Fixture fixture, @Param("fixtureId") Long fixtureId);

	/**
	 * 查询列表
	 * 
	 * @param fixture
	 * @return
	 */
	List<Fixture> select(@Param("fixture") Fixture fixture);

	/**
	 * 分页查询列表
	 * 
	 * @param fixture
	 * @return
	 */
	Page<Fixture> selectPageList(@Param("fixture") Fixture fixture, @Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param fixtureId
	 * @return
	 */
	Fixture selectById(@Param("fixtureId") Long fixtureId);

	/**
	 * 根据主键查询
	 * 
	 * @param fixtureId
	 * @return
	 */
	Fixture selectByFixtureBarcode(@Param("fixtureBarcode") String fixtureBarcode);

	/**
	 * 按主键物理删除
	 * 
	 * @param fixtureId
	 * @return
	 */
	Integer deleteById(@Param("fixture") Fixture fixture);

	/**
	 * 按主键物理删除
	 * 
	 * @param fixtureId
	 * @return
	 */
	Integer removeById(@Param("fixtureId") Long fixtureId);

	List<Fixture> selectPartList(@Param("fixtureNumber") String fixtureNumber, @Param("departmentId") Long departmentId);

}
