package org.tsinghuatj.tool.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;

import org.tsinghuatj.tool.domain.ToolApplyResult;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface ToolApplyResultMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param toolApplyResult
	 * @return
	 */
	Integer insert(@Param("toolApplyResult") ToolApplyResult toolApplyResult);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param toolApplyResult
	 * @param toolApplyResultId
	 * @return
	 */
	Integer updateActiveById(@Param("toolApplyResult") ToolApplyResult toolApplyResult, @Param("toolApplyResultId") Long toolApplyResultId);

	/**
	 * 查询列表
	 * 
	 * @param toolApplyResult
	 * @return
	 */
	List<ToolApplyResult> select(@Param("toolApplyResult") ToolApplyResult toolApplyResult);
	
	/**
	 * 分页查询列表
	 * 
	 * @param toolApplyResult
	 * @return
	 */
	Page<ToolApplyResult> selectPageList(@Param("toolApplyResult") ToolApplyResult toolApplyResult,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param toolApplyResultId
	 * @return
	 */
	ToolApplyResult selectById(@Param("toolApplyResultId") Long toolApplyResultId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param toolApplyResultId
	 * @return
	 */
	Integer deleteById(@Param("toolApplyResult") ToolApplyResult toolApplyResult);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param toolApplyResultId
	 * @return
	 */
	Integer removeById(@Param("toolApplyResultId") Long toolApplyResultId);
}
