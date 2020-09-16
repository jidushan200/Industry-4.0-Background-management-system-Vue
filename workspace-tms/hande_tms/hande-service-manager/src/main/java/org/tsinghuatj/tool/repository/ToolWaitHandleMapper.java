package org.tsinghuatj.tool.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;

import org.tsinghuatj.tool.domain.ToolWaitHandle;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface ToolWaitHandleMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param toolWaitHandle
	 * @return
	 */
	Integer insert(@Param("toolWaitHandle") ToolWaitHandle toolWaitHandle);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param toolWaitHandle
	 * @param toolWaitHandleId
	 * @return
	 */
	Integer updateActiveById(@Param("toolWaitHandle") ToolWaitHandle toolWaitHandle, @Param("toolWaitHandleId") Long toolWaitHandleId);

	/**
	 * 查询列表
	 * 
	 * @param toolWaitHandle
	 * @return
	 */
	List<ToolWaitHandle> select(@Param("toolWaitHandle") ToolWaitHandle toolWaitHandle);
	
	/**
	 * 分页查询列表
	 * 
	 * @param toolWaitHandle
	 * @return
	 */
	Page<ToolWaitHandle> selectPageList(@Param("toolWaitHandle") ToolWaitHandle toolWaitHandle,@Param("queryDto") QueryDto queryDto);

	
	
	
	/**
	 * 分页查询列表
	 * 
	 * @param toolWaitHandle
	 * @return
	 */
	Page<ToolWaitHandle> selectNewToolPageList(@Param("toolWaitHandle") ToolWaitHandle toolWaitHandle,@Param("queryDto") QueryDto queryDto);

	
	
	/**
	 * 分页查询列表
	 * 
	 * @param toolWaitHandle
	 * @return
	 */
	Page<ToolWaitHandle> selectCoatPageList(@Param("toolWaitHandle") ToolWaitHandle toolWaitHandle,@Param("queryDto") QueryDto queryDto);

	
	
	
	
	/**
	 * 根据主键查询
	 * 
	 * @param toolWaitHandleId
	 * @return
	 */
	ToolWaitHandle selectById(@Param("toolWaitHandleId") Long toolWaitHandleId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param toolWaitHandleId
	 * @return
	 */
	Integer deleteById(@Param("toolWaitHandle") ToolWaitHandle toolWaitHandle);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param toolWaitHandleId
	 * @return
	 */
	Integer removeById(@Param("toolWaitHandleId") Long toolWaitHandleId);
}
