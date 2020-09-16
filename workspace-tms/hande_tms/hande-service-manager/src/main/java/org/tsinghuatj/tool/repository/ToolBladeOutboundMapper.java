package org.tsinghuatj.tool.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;

import org.tsinghuatj.tool.domain.ToolBladeOutbound;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface ToolBladeOutboundMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param toolBladeOutbound
	 * @return
	 */
	Integer insert(@Param("toolBladeOutbound") ToolBladeOutbound toolBladeOutbound);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param toolBladeOutbound
	 * @param toolBladeOutboundId
	 * @return
	 */
	Integer updateActiveById(@Param("toolBladeOutbound") ToolBladeOutbound toolBladeOutbound, @Param("toolBladeOutboundId") Long toolBladeOutboundId);

	/**
	 * 查询列表
	 * 
	 * @param toolBladeOutbound
	 * @return
	 */
	List<ToolBladeOutbound> select(@Param("toolBladeOutbound") ToolBladeOutbound toolBladeOutbound);
	
	/**
	 * 分页查询列表
	 * 
	 * @param toolBladeOutbound
	 * @return
	 */
	Page<ToolBladeOutbound> selectPageList(@Param("toolBladeOutbound") ToolBladeOutbound toolBladeOutbound,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param toolBladeOutboundId
	 * @return
	 */
	ToolBladeOutbound selectById(@Param("toolBladeOutboundId") Long toolBladeOutboundId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param toolBladeOutboundId
	 * @return
	 */
	Integer deleteById(@Param("toolBladeOutbound") ToolBladeOutbound toolBladeOutbound);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param toolBladeOutboundId
	 * @return
	 */
	Integer removeById(@Param("toolBladeOutboundId") Long toolBladeOutboundId);
}
