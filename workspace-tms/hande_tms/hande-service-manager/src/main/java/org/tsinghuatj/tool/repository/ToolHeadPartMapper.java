package org.tsinghuatj.tool.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;

import org.tsinghuatj.tool.domain.ToolHeadPart;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface ToolHeadPartMapper {

	/**
	 * 插入数据
	 * 
	 * @param toolHeadPart
	 * @return
	 */
	Integer insert(@Param("toolHeadPart") ToolHeadPart toolHeadPart);

	/**
	 * 根据主键更新有值数据
	 * 
	 * @param toolHeadPart
	 * @param toolHeadPartId
	 * @return
	 */
	Integer updateActiveById(@Param("toolHeadPart") ToolHeadPart toolHeadPart, @Param("toolHeadPartId") Long toolHeadPartId);

	ToolHeadPart selectByHeadPart(@Param("headNumber") String headNumber, @Param("partCode") String partNumber, @Param("pkId") Long pkId);

	/**
	 * 查询列表
	 * 
	 * @param toolHeadPart
	 * @return
	 */
	List<ToolHeadPart> select(@Param("toolHeadPart") ToolHeadPart toolHeadPart);

	/**
	 * 分页查询列表
	 * 
	 * @param toolHeadPart
	 * @return
	 */
	Page<ToolHeadPart> selectPageList(@Param("toolHeadPart") ToolHeadPart toolHeadPart, @Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param toolHeadPartId
	 * @return
	 */
	ToolHeadPart selectById(@Param("toolHeadPartId") Long toolHeadPartId);

	/**
	 * 按主键物理删除
	 * 
	 * @param toolHeadPartId
	 * @return
	 */
	Integer deleteById(@Param("toolHeadPart") ToolHeadPart toolHeadPart);

	/**
	 * 按主键物理删除
	 * 
	 * @param toolHeadPartId
	 * @return
	 */
	Integer removeById(@Param("toolHeadPartId") Long toolHeadPartId);
	
	
}
