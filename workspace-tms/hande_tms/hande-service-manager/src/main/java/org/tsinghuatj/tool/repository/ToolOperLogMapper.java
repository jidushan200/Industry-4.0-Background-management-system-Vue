package org.tsinghuatj.tool.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;

import org.tsinghuatj.tool.domain.ToolOperLog;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface ToolOperLogMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param toolOperLog
	 * @return
	 */
	Integer insert(@Param("toolOperLog") ToolOperLog toolOperLog);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param toolOperLog
	 * @param toolOperLogId
	 * @return
	 */
	Integer updateActiveById(@Param("toolOperLog") ToolOperLog toolOperLog, @Param("toolOperLogId") String toolOperLogId);

	/**
	 * 查询列表
	 * 
	 * @param toolOperLog
	 * @return
	 */
	List<ToolOperLog> select(@Param("toolOperLog") ToolOperLog toolOperLog);
	
	/**
	 * 分页查询列表
	 * 
	 * @param toolOperLog
	 * @return
	 */
	Page<ToolOperLog> selectPageList(@Param("toolOperLog") ToolOperLog toolOperLog,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param toolOperLogId
	 * @return
	 */
	ToolOperLog selectById(@Param("toolOperLogId") String toolOperLogId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param toolOperLogId
	 * @return
	 */
	Integer deleteById(@Param("toolOperLog") ToolOperLog toolOperLog);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param toolOperLogId
	 * @return
	 */
	Integer removeById(@Param("toolOperLogId") String toolOperLogId);
}
