package org.tsinghuatj.tool.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;

import org.tsinghuatj.tool.domain.ToolPart;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface ToolPartMapper {

	/**
	 * 插入数据
	 * 
	 * @param toolPart
	 * @return
	 */
	Integer insert(@Param("toolPart") ToolPart toolPart);

	/**
	 * 根据主键更新有值数据
	 * 
	 * @param toolPart
	 * @param toolPartId
	 * @return
	 */
	Integer updateActiveById(@Param("toolPart") ToolPart toolPart, @Param("toolPartId") Long toolPartId);

	/**
	 * 查询列表
	 * 
	 * @param toolPart
	 * @return
	 */
	List<ToolPart> select(@Param("toolPart") ToolPart toolPart);

	/**
	 * 分页查询列表
	 * 
	 * @param toolPart
	 * @return
	 */
	Page<ToolPart> selectPageList(@Param("toolPart") ToolPart toolPart, @Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param toolPartId
	 * @return
	 */
	ToolPart selectById(@Param("toolPartId") Long toolPartId);

	/**
	 * 按主键物理删除
	 * 
	 * @param toolPartId
	 * @return
	 */
	Integer deleteById(@Param("toolPart") ToolPart toolPart);

	/**
	 * 按主键物理删除
	 * 
	 * @param toolPartId
	 * @return
	 */
	Integer removeById(@Param("toolPartId") Long toolPartId);

	/**
	 * 
	 * @param pkId
	 * @param toolId
	 * @param partId
	 * @return
	 */
	ToolPart checkToolPart(@Param("pkId") Long pkId, @Param("toolId") Long toolId, @Param("partId") Long partId);
	
	
	/**
	 * 根据主键查询
	 * 
	 * @param toolId
	 * @return
	 */
	ToolPart selectByToolIdLimit(@Param("toolNumber") String toolNumber);
}
