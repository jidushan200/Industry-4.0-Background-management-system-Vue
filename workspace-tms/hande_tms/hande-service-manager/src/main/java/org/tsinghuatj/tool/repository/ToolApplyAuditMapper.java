package org.tsinghuatj.tool.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;

import org.tsinghuatj.tool.domain.ToolApplyAudit;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface ToolApplyAuditMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param toolApplyAudit
	 * @return
	 */
	Integer insert(@Param("toolApplyAudit") ToolApplyAudit toolApplyAudit);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param toolApplyAudit
	 * @param toolApplyAuditId
	 * @return
	 */
	Integer updateActiveById(@Param("toolApplyAudit") ToolApplyAudit toolApplyAudit, @Param("toolApplyAuditId") Long toolApplyAuditId);

	/**
	 * 查询列表
	 * 
	 * @param applyId
	 * @return
	 */
	List<ToolApplyAudit> select(@Param("applyId") Long applyId);
	
	/**
	 * 分页查询列表
	 * 
	 * @param toolApplyAudit
	 * @return
	 */
	Page<ToolApplyAudit> selectPageList(@Param("toolApplyAudit") ToolApplyAudit toolApplyAudit,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param toolApplyAuditId
	 * @return
	 */
	ToolApplyAudit selectById(@Param("toolApplyAuditId") Long toolApplyAuditId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param toolApplyAuditId
	 * @return
	 */
	Integer deleteById(@Param("toolApplyAudit") ToolApplyAudit toolApplyAudit);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param toolApplyAuditId
	 * @return
	 */
	Integer removeById(@Param("toolApplyAuditId") Long toolApplyAuditId);
	
	/**
	 * 查询最大序号
	 * 
	 * @param applyId
	 * @return
	 */
	ToolApplyAudit selectSeqByApplyId(@Param("applyId") Long applyId);
}
