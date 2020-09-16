package org.tsinghuatj.fixture.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.fixture.domain.FixtureChild;
import org.tsinghuatj.framework.domain.QueryDto;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface FixtureChildMapper {

	/**
	 * 插入数据
	 * 
	 * @param fixtureChild
	 * @return
	 */
	Integer insert(@Param("fixtureChild") FixtureChild fixtureChild);

	/**
	 * 根据主键更新有值数据
	 * 
	 * @param fixtureChild
	 * @param fixtureChildId
	 * @return
	 */
	Integer updateActiveById(@Param("fixtureChild") FixtureChild fixtureChild, @Param("fixtureChildId") Long fixtureChildId);

	/**
	 * 查询列表
	 * 
	 * @param fixtureChild
	 * @return
	 */
	List<FixtureChild> select(@Param("fixtureChild") FixtureChild fixtureChild);

	/**
	 * 分页查询列表
	 * 
	 * @param fixtureChild
	 * @return
	 */
	Page<FixtureChild> selectPageList(@Param("fixtureChild") FixtureChild fixtureChild, @Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param fixtureChildId
	 * @return
	 */
	FixtureChild selectById(@Param("fixtureChildId") Long fixtureChildId);

	/**
	 * 根据主键查询
	 * 
	 * @param fixtureChildId
	 * @return
	 */
	FixtureChild selectByFixtureBarcode(@Param("fixtureBarcode") String fixtureBarcode);

	/**
	 * 按主键物理删除
	 * 
	 * @param fixtureChildId
	 * @return
	 */
	Integer deleteById(@Param("fixtureChild") FixtureChild fixtureChild);

	/**
	 * 按主键物理删除
	 * 
	 * @param fixtureChildId
	 * @return
	 */
	Integer removeById(@Param("fixtureChildId") Long fixtureChildId);

	/**
	 * 查询列表
	 * 
	 * @param fixtureChild
	 * @return
	 */
	List<FixtureChild> selectByParentList(@Param("parentList") List<String> parentList);

	List<FixtureChild> selectPartList(@Param("fixtureNumber") String fixtureNumber, @Param("parentBarcode") String parentBarcode, @Param("departmentId") Long departmentId);

	List<FixtureChild> selectParentPartList(@Param("fixtureNumber") String fixtureNumber, @Param("departmentId") Long departmentId);

	/**
	 * 解绑
	 * 
	 * @param fixtureChildId
	 * @return
	 */
	Integer unbindChild(@Param("fixtureChild") FixtureChild fixtureChild);
	
	/**
	 * 绑定配件原来不为空
	 * 
	 * @param fixtureChildId
	 * @return
	 */
	Integer bindChild(@Param("fixtureChild") FixtureChild fixtureChild);
	
	/**
	 * 绑定配件原来为空
	 * 
	 * @param fixtureChildId
	 * @return
	 */
	Integer bindNullChild(@Param("fixtureChild") FixtureChild fixtureChild);
	
	
}
