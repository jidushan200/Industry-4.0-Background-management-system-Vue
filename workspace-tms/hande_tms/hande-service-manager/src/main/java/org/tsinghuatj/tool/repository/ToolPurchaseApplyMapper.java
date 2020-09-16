package org.tsinghuatj.tool.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;

import org.tsinghuatj.tool.domain.ToolPurchaseApply;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface ToolPurchaseApplyMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param toolPurchaseApply
	 * @return
	 */
	Integer insert(@Param("toolPurchaseApply") ToolPurchaseApply toolPurchaseApply);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param toolPurchaseApply
	 * @param toolPurchaseApplyId
	 * @return
	 */
	Integer updateActiveById(@Param("toolPurchaseApply") ToolPurchaseApply toolPurchaseApply, @Param("toolPurchaseApplyId") Long toolPurchaseApplyId);

	/**
	 * 查询列表
	 * 
	 * @param toolPurchaseApply
	 * @return
	 */
	List<ToolPurchaseApply> select(@Param("toolPurchaseApply") ToolPurchaseApply toolPurchaseApply);
	
	/**
	 * 分页查询列表
	 * 
	 * @param toolPurchaseApply
	 * @return
	 */
	Page<ToolPurchaseApply> selectPageList(@Param("toolPurchaseApply") ToolPurchaseApply toolPurchaseApply,@Param("queryDto") QueryDto queryDto);
	
	
	Page<ToolPurchaseApply>	selectPurchaseReceiptPageList(@Param("toolPurchaseApply") ToolPurchaseApply toolPurchaseApply,@Param("queryDto") QueryDto queryDto);
	
	/**
	 * 分页查询列表
	 * 
	 * @param toolPurchaseApply
	 * @return
	 */
	Page<ToolPurchaseApply> selectAuditedPageList(@Param("toolPurchaseApply") ToolPurchaseApply toolPurchaseApply,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param toolPurchaseApplyId
	 * @return
	 */
	ToolPurchaseApply selectById(@Param("toolPurchaseApplyId") Long toolPurchaseApplyId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param toolPurchaseApplyId
	 * @return
	 */
	Integer deleteById(@Param("toolPurchaseApply") ToolPurchaseApply toolPurchaseApply);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param toolPurchaseApplyId
	 * @return
	 */
	Integer removeById(@Param("toolPurchaseApplyId") Long toolPurchaseApplyId);
}
