package org.tsinghuatj.tool.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;

import org.tsinghuatj.tool.domain.ToolAppendix;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface ToolAppendixMapper {

	/**
	 * 插入数据
	 * 
	 * @param toolAppendix
	 * @return
	 */
	Integer insert(@Param("toolAppendix") ToolAppendix toolAppendix);

	/**
	 * 根据主键更新有值数据
	 * 
	 * @param toolAppendix
	 * @param toolAppendixId
	 * @return
	 */
	Integer updateActiveById(@Param("toolAppendix") ToolAppendix toolAppendix,
			@Param("toolAppendixId") Long toolAppendixId);

	/**
	 * 查询列表
	 * 
	 * @param toolAppendix
	 * @return
	 */
	List<ToolAppendix> select(@Param("toolAppendix") ToolAppendix toolAppendix);

	/**
	 * 查询列表
	 * 
	 * @param checkId
	 * @return
	 */
	List<ToolAppendix> selectByCheckId(@Param("checkId") Long checkId);
	
	/**
	 * 查询列表
	 * 
	 * @param checkId
	 * @return
	 */
	List<ToolAppendix> selectByApplyId(@Param("applyId") Long applyId,@Param("appdenixType") Integer appdenixType);

	/**
	 * 分页查询列表
	 * 
	 * @param toolAppendix
	 * @return
	 */
	Page<ToolAppendix> selectPageList(@Param("toolAppendix") ToolAppendix toolAppendix,
			@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param toolAppendixId
	 * @return
	 */
	ToolAppendix selectById(@Param("toolAppendixId") Long toolAppendixId);

	/**
	 * 按主键物理删除
	 * 
	 * @param toolAppendixId
	 * @return
	 */
	Integer deleteById(@Param("toolAppendix") ToolAppendix toolAppendix);

	/**
	 * 按主键物理删除
	 * 
	 * @param toolAppendixId
	 * @return
	 */
	Integer removeById(@Param("toolAppendixId") Long toolAppendixId);
}
