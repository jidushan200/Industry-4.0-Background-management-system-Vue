package org.tsinghuatj.tool.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;

import org.tsinghuatj.tool.domain.ToolUnusual;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface ToolUnusualMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param toolUnusual
	 * @return
	 */
	Integer insert(@Param("toolUnusual") ToolUnusual toolUnusual);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param toolUnusual
	 * @param toolUnusualId
	 * @return
	 */
	Integer updateActiveById(@Param("toolUnusual") ToolUnusual toolUnusual, @Param("toolUnusualId") Long toolUnusualId);

	/**
	 * 查询列表
	 * 
	 * @param toolUnusual
	 * @return
	 */
	List<ToolUnusual> select(@Param("toolUnusual") ToolUnusual toolUnusual);
	
	/**
	 * 分页查询列表
	 * 
	 * @param toolUnusual
	 * @return
	 */
	Page<ToolUnusual> selectPageList(@Param("toolUnusual") ToolUnusual toolUnusual,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param toolUnusualId
	 * @return
	 */
	ToolUnusual selectById(@Param("toolUnusualId") Long toolUnusualId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param toolUnusualId
	 * @return
	 */
	Integer deleteById(@Param("toolUnusual") ToolUnusual toolUnusual);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param toolUnusualId
	 * @return
	 */
	Integer removeById(@Param("toolUnusualId") Long toolUnusualId);
}
