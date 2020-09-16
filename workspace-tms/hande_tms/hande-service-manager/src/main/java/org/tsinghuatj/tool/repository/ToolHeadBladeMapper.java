package org.tsinghuatj.tool.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;

import org.tsinghuatj.tool.domain.ToolHeadBlade;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface ToolHeadBladeMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param toolHeadBlade
	 * @return
	 */
	Integer insert(@Param("toolHeadBlade") ToolHeadBlade toolHeadBlade);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param toolHeadBlade
	 * @param toolHeadBladeId
	 * @return
	 */
	Integer updateActiveById(@Param("toolHeadBlade") ToolHeadBlade toolHeadBlade, @Param("toolHeadBladeId") Long toolHeadBladeId);

	/**
	 * 查询列表
	 * 
	 * @param toolHeadBlade
	 * @return
	 */
	List<ToolHeadBlade> select(@Param("toolHeadBlade") ToolHeadBlade toolHeadBlade);
	
	/**
	 * 查询列表
	 * 
	 * @param toolHeadBlade
	 * @return
	 */
	List<ToolHeadBlade> selectComplexheadBladeList(@Param("toolHeadBlade") ToolHeadBlade toolHeadBlade);
	
	/**
	 * 分页查询列表
	 * 
	 * @param toolHeadBlade
	 * @return
	 */
	Page<ToolHeadBlade> selectPageList(@Param("toolHeadBlade") ToolHeadBlade toolHeadBlade,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param toolHeadBladeId
	 * @return
	 */
	ToolHeadBlade selectById(@Param("toolHeadBladeId") Long toolHeadBladeId);
	
	
	/**
	 * 根据主键查询
	 * 
	 * @param toolHeadBladeId
	 * @return
	 */
	ToolHeadBlade selectByToolHead(@Param("headNumber") String headNumber,@Param("toolNumber") String toolNumber,@Param("pkId") Long pkId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param toolHeadBladeId
	 * @return
	 */
	Integer deleteById(@Param("toolHeadBlade") ToolHeadBlade toolHeadBlade);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param toolHeadBladeId
	 * @return
	 */
	Integer removeById(@Param("toolHeadBladeId") Long toolHeadBladeId);
}
