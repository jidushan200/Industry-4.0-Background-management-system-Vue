package org.tsinghuatj.tool.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;

import org.tsinghuatj.tool.domain.ToolPurchaseResult;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface ToolPurchaseResultMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param toolPurchaseResult
	 * @return
	 */
	Integer insert(@Param("toolPurchaseResult") ToolPurchaseResult toolPurchaseResult);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param toolPurchaseResult
	 * @param toolPurchaseResultId
	 * @return
	 */
	Integer updateActiveById(@Param("toolPurchaseResult") ToolPurchaseResult toolPurchaseResult, @Param("toolPurchaseResultId") Long toolPurchaseResultId);

	/**
	 * 查询列表
	 * 
	 * @param toolPurchaseResult
	 * @return
	 */
	List<ToolPurchaseResult> select(@Param("toolPurchaseResult") ToolPurchaseResult toolPurchaseResult);
	
	/**
	 * 分页查询列表
	 * 
	 * @param toolPurchaseResult
	 * @return
	 */
	Page<ToolPurchaseResult> selectPageList(@Param("toolPurchaseResult") ToolPurchaseResult toolPurchaseResult,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param toolPurchaseResultId
	 * @return
	 */
	ToolPurchaseResult selectById(@Param("toolPurchaseResultId") Long toolPurchaseResultId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param toolPurchaseResultId
	 * @return
	 */
	Integer deleteById(@Param("toolPurchaseResult") ToolPurchaseResult toolPurchaseResult);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param toolPurchaseResultId
	 * @return
	 */
	Integer removeById(@Param("toolPurchaseResultId") Long toolPurchaseResultId);
}
