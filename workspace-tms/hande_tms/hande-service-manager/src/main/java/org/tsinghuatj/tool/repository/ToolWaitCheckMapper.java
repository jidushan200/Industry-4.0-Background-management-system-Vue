package org.tsinghuatj.tool.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;

import org.tsinghuatj.tool.domain.ToolWaitCheck;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface ToolWaitCheckMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param toolWaitCheck
	 * @return
	 */
	Integer insert(@Param("toolWaitCheck") ToolWaitCheck toolWaitCheck);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param toolWaitCheck
	 * @param toolWaitCheckId
	 * @return
	 */
	Integer updateActiveById(@Param("toolWaitCheck") ToolWaitCheck toolWaitCheck, @Param("toolWaitCheckId") Long toolWaitCheckId);
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param toolWaitCheck
	 * @param toolWaitCheckId
	 * @return
	 */
	Integer updateActiveByFullNumber(@Param("toolWaitCheck") ToolWaitCheck toolWaitCheck);

	/**
	 * 查询列表
	 * 
	 * @param toolWaitCheck
	 * @return
	 */
	List<ToolWaitCheck> select(@Param("toolWaitCheck") ToolWaitCheck toolWaitCheck);
	
	/**
	 * 分页查询列表
	 * 
	 * @param toolWaitCheck
	 * @return
	 */
	Page<ToolWaitCheck> selectPageList(@Param("toolWaitCheck") ToolWaitCheck toolWaitCheck,@Param("queryDto") QueryDto queryDto);
	
	/**
	 * 分页查询列表
	 * 
	 * @param toolWaitCheck
	 * @return
	 */
	Page<ToolWaitCheck> selectComposePageList(@Param("toolWaitCheck") ToolWaitCheck toolWaitCheck,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param toolWaitCheckId
	 * @return
	 */
	ToolWaitCheck selectById(@Param("toolWaitCheckId") Long toolWaitCheckId);
	
	
	ToolWaitCheck selectByUnusualReportId(@Param("reportId") Long reportId);
	
	/**
	 * 根据主键查询
	 * 
	 * @param toolWaitCheckId
	 * @return
	 */
	ToolWaitCheck selectByFullNumber(@Param("fullNumber") String fullNumber,@Param("checkType") Integer checkType);
	
	
	
	/**
	 * 按主键物理删除
	 * 
	 * @param toolWaitCheckId
	 * @return
	 */
	Integer deleteById(@Param("toolWaitCheck") ToolWaitCheck toolWaitCheck);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param toolWaitCheckId
	 * @return
	 */
	Integer removeById(@Param("toolWaitCheckId") Long toolWaitCheckId);
}
