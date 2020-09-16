package org.tsinghuatj.tool.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;

import org.tsinghuatj.tool.domain.ToolProcess;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface ToolProcessMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param toolProcess
	 * @return
	 */
	Integer insert(@Param("toolProcess") ToolProcess toolProcess);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param toolProcess
	 * @param toolProcessId
	 * @return
	 */
	Integer updateActiveById(@Param("toolProcess") ToolProcess toolProcess, @Param("toolProcessId") Long toolProcessId);

	/**
	 * 查询列表
	 * 
	 * @param toolProcess
	 * @return
	 */
	List<ToolProcess> select(@Param("toolProcess") ToolProcess toolProcess);
	
	
	ToolProcess selectLatestProcess(@Param("toolProcess") ToolProcess toolProcess);
	
	/**
	 * 分页查询列表
	 * 
	 * @param toolProcess
	 * @return
	 */
	Page<ToolProcess> selectPageList(@Param("toolProcess") ToolProcess toolProcess,@Param("queryDto") QueryDto queryDto);
	/**
	 * 涂层生产
	 * @param toolProcess
	 * @param queryDto
	 * @return
	 */
	Page<ToolProcess> selectCoatProcessPageList(@Param("toolProcess") ToolProcess toolProcess,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param toolProcessId
	 * @return
	 */
	ToolProcess selectById(@Param("toolProcessId") Long toolProcessId);
	
	/**
	 * 根据toolId查询
	 * 
	 * @param toolId
	 * @return
	 */
	List<ToolProcess> selectByToolId(@Param("toolId") Long toolId,@Param("queryDto") QueryDto queryDto);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param toolProcessId
	 * @return
	 */
	Integer deleteById(@Param("toolProcess") ToolProcess toolProcess);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param toolProcessId
	 * @return
	 */
	Integer removeById(@Param("toolProcessId") Long toolProcessId);
	
	/**
	 * 根据toolId查询
	 * 
	 * @param toolId
	 * @return
	 */
	ToolProcess selectSeqByToolId(@Param("toolId") Long toolId);
	
	List<ToolProcess> selectBytoolIdList(@Param("toolIdList") List<Long> toolIdList);
	
	
	/**
	 * 根据toolId查询
	 * 
	 * @param toolId
	 * @return
	 */
	Integer sumProcessQtyByToolId(@Param("toolId") Long toolId);
	
}
