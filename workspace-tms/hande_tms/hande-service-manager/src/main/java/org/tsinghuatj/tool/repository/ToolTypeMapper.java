package org.tsinghuatj.tool.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;

import org.tsinghuatj.tool.domain.ToolType;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface ToolTypeMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param toolType
	 * @return
	 */
	Integer insert(@Param("toolType") ToolType toolType);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param toolType
	 * @param toolTypeId
	 * @return
	 */
	Integer updateActiveById(@Param("toolType") ToolType toolType, @Param("toolTypeId") Long toolTypeId);

	/**
	 * 查询列表
	 * 
	 * @param toolType
	 * @return
	 */
	List<ToolType> select(@Param("toolType") ToolType toolType);
	
	/**
	 * 分页查询列表
	 * 
	 * @param toolType
	 * @return
	 */
	Page<ToolType> selectPageList(@Param("toolType") ToolType toolType,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param toolTypeId
	 * @return
	 */
	ToolType selectById(@Param("toolTypeId") Long toolTypeId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param toolTypeId
	 * @return
	 */
	Integer deleteById(@Param("toolType") ToolType toolType);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param toolTypeId
	 * @return
	 */
	Integer removeById(@Param("toolTypeId") Long toolTypeId);
}
