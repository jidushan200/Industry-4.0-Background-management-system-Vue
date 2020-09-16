package org.tsinghuatj.tool.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;

import org.tsinghuatj.tool.domain.ToolBladeCompose;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface ToolBladeComposeMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param toolBladeCompose
	 * @return
	 */
	Integer insert(@Param("toolBladeCompose") ToolBladeCompose toolBladeCompose);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param toolBladeCompose
	 * @param toolBladeComposeId
	 * @return
	 */
	Integer updateActiveById(@Param("toolBladeCompose") ToolBladeCompose toolBladeCompose, @Param("toolBladeComposeId") Long toolBladeComposeId);

	/**
	 * 根据主键更新有值数据
	 * 
	 * @param toolBladeCompose
	 * @param toolBladeComposeId
	 * @return
	 */
	Integer updateActiveByComposeNumber(@Param("toolBladeCompose") ToolBladeCompose toolBladeCompose, @Param("composeNumber") String composeNumber);

	/**
	 * 查询列表
	 * 
	 * @param toolBladeCompose
	 * @return
	 */
	List<ToolBladeCompose> select(@Param("toolBladeCompose") ToolBladeCompose toolBladeCompose);
	
	/**
	 * 分页查询列表
	 * 
	 * @param toolBladeCompose
	 * @return
	 */
	Page<ToolBladeCompose> selectPageList(@Param("toolBladeCompose") ToolBladeCompose toolBladeCompose,@Param("queryDto") QueryDto queryDto);

	
	/**
	 * 分页查询列表
	 * 
	 * @param toolBladeCompose
	 * @return
	 */
	Page<ToolBladeCompose> selectLifePageList(@Param("toolBladeCompose") ToolBladeCompose toolBladeCompose,@Param("queryDto") QueryDto queryDto);

	
	Page<ToolBladeCompose> selectScrapPageList(@Param("toolBladeCompose") ToolBladeCompose toolBladeCompose,@Param("queryDto") QueryDto queryDto);
	/**
	 * 根据主键查询
	 * 
	 * @param toolBladeComposeId
	 * @return
	 */
	ToolBladeCompose selectById(@Param("toolBladeComposeId") Long toolBladeComposeId);
	
	
	/**
	 * 根据主键查询
	 * 
	 * @param toolBladeComposeId
	 * @return
	 */
	ToolBladeCompose selectByComposeNumber(@Param("composeNumber") String composeNumber);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param toolBladeComposeId
	 * @return
	 */
	Integer deleteById(@Param("toolBladeCompose") ToolBladeCompose toolBladeCompose);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param toolBladeComposeId
	 * @return
	 */
	Integer removeById(@Param("toolBladeComposeId") Long toolBladeComposeId);
	
	
	/**
	 * 根据主键查询
	 * 
	 * @param toolBladeComposeId
	 * @return
	 */
	String selectByHeaderNumber(@Param("headNumber") String headNumber);
	
	

	
}
