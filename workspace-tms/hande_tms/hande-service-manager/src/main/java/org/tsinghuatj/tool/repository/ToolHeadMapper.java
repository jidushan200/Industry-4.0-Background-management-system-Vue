package org.tsinghuatj.tool.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;

import org.tsinghuatj.tool.domain.ToolHead;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface ToolHeadMapper {

	/**
	 * 插入数据
	 * 
	 * @param toolHead
	 * @return
	 */
	Integer insert(@Param("toolHead") ToolHead toolHead);

	/**
	 * 根据主键更新有值数据
	 * 
	 * @param toolHead
	 * @param toolHeadId
	 * @return
	 */
	Integer updateActiveById(@Param("toolHead") ToolHead toolHead, @Param("toolHeadId") Long toolHeadId);

	/**
	 * 查询列表
	 * 
	 * @param toolHead
	 * @return
	 */
	List<ToolHead> select(@Param("toolHead") ToolHead toolHead);

	/**
	 * 分页查询列表
	 * 
	 * @param toolHead
	 * @return
	 */
	Page<ToolHead> selectPageList(@Param("toolHead") ToolHead toolHead, @Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param toolHeadId
	 * @return
	 */
	ToolHead selectById(@Param("toolHeadId") Long toolHeadId);

	/**
	 * 根据主键查询
	 * 
	 * @param toolHeadId
	 * @return
	 */
	ToolHead selectByHeadNumber(@Param("headNumber") String headNumber, @Param("pkId") Long pkId);

	/**
	 * 根据主键查询
	 * 
	 * @param toolHeadId
	 * @return
	 */
	List<ToolHead> selectByHeadNumberList(@Param("headNumberList") List<String> headNumberList);

	/**
	 * 按主键物理删除
	 * 
	 * @param toolHeadId
	 * @return
	 */
	Integer deleteById(@Param("toolHead") ToolHead toolHead);

	/**
	 * 按主键物理删除
	 * 
	 * @param toolHeadId
	 * @return
	 */
	Integer removeById(@Param("toolHeadId") Long toolHeadId);
}
