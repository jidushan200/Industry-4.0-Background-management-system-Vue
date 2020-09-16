package org.tsinghuatj.tool.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.tool.domain.ToolBladeComposePart;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface ToolBladeComposePartMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param toolBladeComposePart
	 * @return
	 */
	Integer insert(@Param("toolBladeComposePart") ToolBladeComposePart toolBladeComposePart);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param toolBladeComposePart
	 * @param toolBladeComposePartId
	 * @return
	 */
	Integer updateActiveById(@Param("toolBladeComposePart") ToolBladeComposePart toolBladeComposePart, @Param("toolBladeComposePartId") Long toolBladeComposePartId);

	/**
	 * 查询列表
	 * 
	 * @param toolBladeComposePart
	 * @return
	 */
	List<ToolBladeComposePart> select(@Param("toolBladeComposePart") ToolBladeComposePart toolBladeComposePart);
	
	/**
	 * 查询列表
	 * 
	 * @param toolBladeComposePart
	 * @return
	 */
	List<ToolBladeComposePart> selectByComposeList(@Param("numberList") List<String> numberList);
	/**
	 * 分页查询列表
	 * 
	 * @param toolBladeComposePart
	 * @return
	 */
	Page<ToolBladeComposePart> selectPageList(@Param("toolBladeComposePart") ToolBladeComposePart toolBladeComposePart,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param toolBladeComposePartId
	 * @return
	 */
	ToolBladeComposePart selectById(@Param("toolBladeComposePartId") Long toolBladeComposePartId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param toolBladeComposePartId
	 * @return
	 */
	Integer deleteById(@Param("toolBladeComposePart") ToolBladeComposePart toolBladeComposePart);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param toolBladeComposePartId
	 * @return
	 */
	Integer removeById(@Param("toolBladeComposePartId") Long toolBladeComposePartId);
	
	/**
	 * 根据主键查询
	 * 
	 * @param toolHeadPartId
	 * @return
	 */
	List<ToolBladeComposePart> selectByComposeNumber(@Param("composeNumber") String composeNumber);
	
	/**
	 * 根据主键查询
	 * 
	 * @param toolHeadPartId
	 * @return
	 */
	List<ToolBladeComposePart> selectByHeadNumber(@Param("headNumber") String headNumber);

}
