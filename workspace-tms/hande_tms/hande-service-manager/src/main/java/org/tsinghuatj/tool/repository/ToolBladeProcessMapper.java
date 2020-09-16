package org.tsinghuatj.tool.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.tool.domain.ToolBladeProcess;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface ToolBladeProcessMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param toolBladeProcess
	 * @return
	 */
	Integer insert(@Param("toolBladeProcess") ToolBladeProcess toolBladeProcess);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param toolBladeProcess
	 * @param toolBladeProcessId
	 * @return
	 */
	Integer updateActiveById(@Param("toolBladeProcess") ToolBladeProcess toolBladeProcess, @Param("toolBladeProcessId") Long toolBladeProcessId);

	/**
	 * 查询列表
	 * 
	 * @param toolBladeProcess
	 * @return
	 */
	List<ToolBladeProcess> select(@Param("toolBladeProcess") ToolBladeProcess toolBladeProcess);
	
	/**
	 * 分页查询列表
	 * 
	 * @param toolBladeProcess
	 * @return
	 */
	Page<ToolBladeProcess> selectPageList(@Param("toolBladeProcess") ToolBladeProcess toolBladeProcess,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param toolBladeProcessId
	 * @return
	 */
	ToolBladeProcess selectById(@Param("toolBladeProcessId") Long toolBladeProcessId);
	
	
	/**
	 * 根据主键查询
	 * 
	 * @param toolBladeProcessId
	 * @return
	 */
	Integer countByComposeNumber(@Param("composeNumber") String composeNumber);
	
	/**
	 * 根据主键查询
	 * 
	 * @param toolBladeProcessId
	 * @return
	 */
	Integer sumProcessQtyByComposeNumber(@Param("composeNumber") String composeNumber);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param toolBladeProcessId
	 * @return
	 */
	Integer deleteById(@Param("toolBladeProcess") ToolBladeProcess toolBladeProcess);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param toolBladeProcessId
	 * @return
	 */
	Integer removeById(@Param("toolBladeProcessId") Long toolBladeProcessId);
	
	
	List<ToolBladeProcess> selectByComposeNumberList(@Param("numberList") List<String> numberList);
}
