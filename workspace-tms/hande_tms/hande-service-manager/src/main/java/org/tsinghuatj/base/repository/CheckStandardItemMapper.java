package org.tsinghuatj.base.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.base.domain.CheckStandardItem;
import org.tsinghuatj.framework.domain.QueryDto;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface CheckStandardItemMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param checkStandardItem
	 * @return
	 */
	Integer insert(@Param("checkStandardItem") CheckStandardItem checkStandardItem);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param checkStandardItem
	 * @param checkStandardItemId
	 * @return
	 */
	Integer updateActiveById(@Param("checkStandardItem") CheckStandardItem checkStandardItem, @Param("checkStandardItemId") Long checkStandardItemId);

	/**
	 * 查询列表
	 * 
	 * @param checkStandardItem
	 * @return
	 */
	List<CheckStandardItem> select(@Param("checkStandardItem") CheckStandardItem checkStandardItem);
	
	/**
	 * 分页查询列表
	 * 
	 * @param checkStandardItem
	 * @return
	 */
	Page<CheckStandardItem> selectPageList(@Param("checkStandardItem") CheckStandardItem checkStandardItem,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param checkStandardItemId
	 * @return
	 */
	CheckStandardItem selectById(@Param("checkStandardItemId") Long checkStandardItemId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param checkStandardItemId
	 * @return
	 */
	Integer deleteById(@Param("checkStandardItem") CheckStandardItem checkStandardItem);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param checkStandardItemId
	 * @return
	 */
	Integer removeById(@Param("checkStandardItemId") Long checkStandardItemId);
	
	
	/**
	 * 查询列表
	 * 
	 * @param standardId
	 * @return
	 */
	List<CheckStandardItem> selectByStandardId(@Param("standardId") Long standardId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param checkStandardItemId
	 * @return
	 */
	Integer deleteByStandardId(@Param("standardId") Long standardId);
	
	
}
