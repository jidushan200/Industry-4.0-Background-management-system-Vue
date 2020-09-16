package org.tsinghuatj.tool.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.tool.domain.ToolApplyAudit;
import org.tsinghuatj.tool.domain.ToolPurchaseApply;
import org.tsinghuatj.tool.domain.ToolScripApply;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface ToolScripApplyMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param toolScripApply
	 * @return
	 */
	Integer insert(@Param("toolScripApply") ToolScripApply toolScripApply);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param toolScripApply
	 * @param toolScripApplyId
	 * @return
	 */
	Integer updateActiveById(@Param("toolScripApply") ToolScripApply toolScripApply, @Param("toolScripApplyId") Long toolScripApplyId);

	/**
	 * 查询列表
	 * 
	 * @param toolScripApply
	 * @return
	 */
	List<ToolScripApply> select(@Param("toolScripApply") ToolScripApply toolScripApply);
	
	/**
	 * 分页查询列表
	 * 
	 * @param toolScripApply
	 * @return
	 */
	Page<ToolScripApply> selectPageList(@Param("toolScripApply") ToolScripApply toolScripApply,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param toolScripApplyId
	 * @return
	 */
	ToolScripApply selectById(@Param("toolScripApplyId") Long toolScripApplyId);
	
	/**
	 * 根据主键查询
	 * 
	 * @param toolScripApplyId
	 * @return
	 */
	ToolScripApply applyGetByfullNumber(@Param("fullNumber") String fullNumber);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param toolScripApplyId
	 * @return
	 */
	Integer deleteById(@Param("toolScripApply") ToolScripApply toolScripApply);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param toolScripApplyId
	 * @return
	 */
	Integer removeById(@Param("toolScripApplyId") Long toolScripApplyId);
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param toolPurchaseApply
	 * @param toolPurchaseApplyId
	 * @return
	 */
	Integer reportAudit(Long userId, String realName, ToolApplyAudit applyAudit, ToolPurchaseApply toolPurchaseApply) throws BusinessException;
}
