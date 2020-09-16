package org.tsinghuatj.tool.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;

import org.tsinghuatj.tool.domain.ToolUnqualifiedApply;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface ToolUnqualifiedApplyMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param toolUnqualifiedApply
	 * @return
	 */
	Integer insert(@Param("toolUnqualifiedApply") ToolUnqualifiedApply toolUnqualifiedApply);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param toolUnqualifiedApply
	 * @param toolUnqualifiedApplyId
	 * @return
	 */
	Integer updateActiveById(@Param("toolUnqualifiedApply") ToolUnqualifiedApply toolUnqualifiedApply, @Param("toolUnqualifiedApplyId") Long toolUnqualifiedApplyId);

	/**
	 * 查询列表
	 * 
	 * @param toolUnqualifiedApply
	 * @return
	 */
	List<ToolUnqualifiedApply> select(@Param("toolUnqualifiedApply") ToolUnqualifiedApply toolUnqualifiedApply);
	
	/**
	 * 分页查询列表
	 * 
	 * @param toolUnqualifiedApply
	 * @return
	 */
	Page<ToolUnqualifiedApply> selectPageList(@Param("toolUnqualifiedApply") ToolUnqualifiedApply toolUnqualifiedApply,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param toolUnqualifiedApplyId
	 * @return
	 */
	ToolUnqualifiedApply selectById(@Param("toolUnqualifiedApplyId") Long toolUnqualifiedApplyId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param toolUnqualifiedApplyId
	 * @return
	 */
	Integer deleteById(@Param("toolUnqualifiedApply") ToolUnqualifiedApply toolUnqualifiedApply);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param toolUnqualifiedApplyId
	 * @return
	 */
	Integer removeById(@Param("toolUnqualifiedApplyId") Long toolUnqualifiedApplyId);
}
