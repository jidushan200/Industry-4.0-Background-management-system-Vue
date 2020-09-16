package org.tsinghuatj.tool.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;

import org.tsinghuatj.tool.domain.ToolCheckResult;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface ToolCheckResultMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param toolCheckResult
	 * @return
	 */
	Integer insert(@Param("toolCheckResult") ToolCheckResult toolCheckResult);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param toolCheckResult
	 * @param toolCheckResultId
	 * @return
	 */
	Integer updateActiveById(@Param("toolCheckResult") ToolCheckResult toolCheckResult, @Param("toolCheckResultId") Long toolCheckResultId);

	/**
	 * 查询列表
	 * 
	 * @param toolCheckResult
	 * @return
	 */
	List<ToolCheckResult> selectByCheckId(@Param("checkId") Long checkId);
	
	/**
	 * 分页查询列表
	 * 
	 * @param toolCheckResult
	 * @return
	 */
	Page<ToolCheckResult> selectPageList(@Param("toolCheckResult") ToolCheckResult toolCheckResult,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param toolCheckResultId
	 * @return
	 */
	ToolCheckResult selectById(@Param("toolCheckResultId") Long toolCheckResultId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param toolCheckResultId
	 * @return
	 */
	Integer deleteById(@Param("toolCheckResult") ToolCheckResult toolCheckResult);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param toolCheckResultId
	 * @return
	 */
	Integer removeById(@Param("toolCheckResultId") Long toolCheckResultId);
}
