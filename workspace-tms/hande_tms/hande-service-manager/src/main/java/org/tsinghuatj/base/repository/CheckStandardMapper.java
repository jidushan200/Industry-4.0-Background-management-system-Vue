package org.tsinghuatj.base.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.base.domain.CheckStandard;
import org.tsinghuatj.framework.domain.QueryDto;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface CheckStandardMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param checkStandard
	 * @return
	 */
	Integer insert(@Param("checkStandard") CheckStandard checkStandard);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param checkStandard
	 * @param checkStandardId
	 * @return
	 */
	Integer updateActiveById(@Param("checkStandard") CheckStandard checkStandard, @Param("checkStandardId") Long checkStandardId);

	/**
	 * 查询列表
	 * 
	 * @param checkStandard
	 * @return
	 */
	List<CheckStandard> selectToolList(@Param("checkStandard") CheckStandard checkStandard);
	/**
	 * 查询列表
	 * 
	 * @param checkStandard
	 * @return
	 */
	List<CheckStandard> selectFixtureList(@Param("checkStandard") CheckStandard checkStandard);
	
	/**
	 * 分页查询列表
	 * 
	 * @param checkStandard
	 * @return
	 */
	Page<CheckStandard> selectToolPageList(@Param("checkStandard") CheckStandard checkStandard,@Param("queryDto") QueryDto queryDto);
	
	/**
	 * 分页查询列表
	 * 
	 * @param checkStandard
	 * @return
	 */
	Page<CheckStandard> selectFixturePageList(@Param("checkStandard") CheckStandard checkStandard,@Param("queryDto") QueryDto queryDto);
	
	
	/**
	 * 分页查询列表
	 * 
	 * @param checkStandard
	 * @return
	 */
	Page<CheckStandard> selectHeadPageList(@Param("checkStandard") CheckStandard checkStandard,@Param("queryDto") QueryDto queryDto);


	/**
	 * 根据主键查询
	 * 
	 * @param checkStandardId
	 * @return
	 */
	CheckStandard selectById(@Param("checkStandardId") Long checkStandardId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param checkStandardId
	 * @return
	 */
	Integer deleteById(@Param("checkStandard") CheckStandard checkStandard);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param checkStandardId
	 * @return
	 */
	Integer removeById(@Param("checkStandardId") Long checkStandardId);
	
	/**
	 *根据刀具编号查询
	 * 
	 * @param toolNumber
	 * @param checkType
	 * @param pkId
	 * @return
	 */
	CheckStandard selectByMaterialNumber(@Param("materialType")Integer materialType,@Param("materialNumber")String materialNumber, @Param("checkType")Integer checkType, @Param("pkId")Long pkId);
	
}
