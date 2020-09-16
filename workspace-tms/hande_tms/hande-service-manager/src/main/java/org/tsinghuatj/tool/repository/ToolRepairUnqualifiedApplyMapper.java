package org.tsinghuatj.tool.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;

import org.tsinghuatj.tool.domain.ToolRepairUnqualifiedApply;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface ToolRepairUnqualifiedApplyMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param toolRepairUnqualifiedApply
	 * @return
	 */
	Integer insert(@Param("toolRepairUnqualifiedApply") ToolRepairUnqualifiedApply toolRepairUnqualifiedApply);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param toolRepairUnqualifiedApply
	 * @param toolRepairUnqualifiedApplyId
	 * @return
	 */
	Integer updateActiveById(@Param("toolRepairUnqualifiedApply") ToolRepairUnqualifiedApply toolRepairUnqualifiedApply, @Param("toolRepairUnqualifiedApplyId") Long toolRepairUnqualifiedApplyId);

	/**
	 * 查询列表
	 * 
	 * @param toolRepairUnqualifiedApply
	 * @return
	 */
	List<ToolRepairUnqualifiedApply> select(@Param("toolRepairUnqualifiedApply") ToolRepairUnqualifiedApply toolRepairUnqualifiedApply);
	
	/**
	 * 分页查询列表
	 * 
	 * @param toolRepairUnqualifiedApply
	 * @return
	 */
	Page<ToolRepairUnqualifiedApply> selectPageList(@Param("toolRepairUnqualifiedApply") ToolRepairUnqualifiedApply toolRepairUnqualifiedApply,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param toolRepairUnqualifiedApplyId
	 * @return
	 */
	ToolRepairUnqualifiedApply selectById(@Param("toolRepairUnqualifiedApplyId") Long toolRepairUnqualifiedApplyId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param toolRepairUnqualifiedApplyId
	 * @return
	 */
	Integer deleteById(@Param("toolRepairUnqualifiedApply") ToolRepairUnqualifiedApply toolRepairUnqualifiedApply);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param toolRepairUnqualifiedApplyId
	 * @return
	 */
	Integer removeById(@Param("toolRepairUnqualifiedApplyId") Long toolRepairUnqualifiedApplyId);
}
