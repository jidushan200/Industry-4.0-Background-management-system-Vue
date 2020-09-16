package org.tsinghuatj.tool.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.tool.domain.ToolBlade;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface ToolBladeMapper {

	/**
	 * 插入数据
	 * 
	 * @param toolBlade
	 * @return
	 */
	Integer insert(@Param("toolBlade") ToolBlade toolBlade);

	/**
	 * 根据主键更新有值数据
	 * 
	 * @param toolBlade
	 * @param toolBladeId
	 * @return
	 */
	Integer updateActiveById(@Param("toolBlade") ToolBlade toolBlade, @Param("toolBladeId") Long toolBladeId);

	/**
	 * 查询列表
	 * 
	 * @param toolBlade
	 * @return
	 */
	List<ToolBlade> select(@Param("toolBlade") ToolBlade toolBlade);

	/**
	 * 分页查询列表
	 * 
	 * @param toolBlade
	 * @return
	 */
	Page<ToolBlade> selectPageList(@Param("toolBlade") ToolBlade toolBlade, @Param("queryDto") QueryDto queryDto);

	
	/**
	 * 根据主键查询
	 * 
	 * @param toolBladeId
	 * @return
	 */
	ToolBlade selectById(@Param("toolBladeId") Long toolBladeId);
	
	
	

	/**
	 * 根据主键查询
	 * 
	 * @param toolBladeId
	 * @return
	 */
	ToolBlade selectByNumber(@Param("toolNumber") String toolNumber,@Param("departmentId") Long departmentId);

	/**
	 * 按主键物理删除
	 * 
	 * @param toolBladeId
	 * @return
	 */
	Integer deleteById(@Param("toolBlade") ToolBlade toolBlade);

	/**
	 * 按主键物理删除
	 * 
	 * @param toolBladeId
	 * @return
	 */
	Integer removeById(@Param("toolBladeId") Long toolBladeId);
}
