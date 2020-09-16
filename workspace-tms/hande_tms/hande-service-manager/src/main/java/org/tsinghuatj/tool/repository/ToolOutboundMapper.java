package org.tsinghuatj.tool.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.tool.domain.ToolOutbound;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface ToolOutboundMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param toolOutbound
	 * @return
	 */
	Integer insert(@Param("toolOutbound") ToolOutbound toolOutbound);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param toolOutbound
	 * @param toolOutboundId
	 * @return
	 */
	Integer updateActiveById(@Param("toolOutbound") ToolOutbound toolOutbound, @Param("toolOutboundId") Long toolOutboundId);

	/**
	 * 查询列表
	 * 
	 * @param toolOutbound
	 * @return
	 */
	List<ToolOutbound> select(@Param("toolOutbound") ToolOutbound toolOutbound);
	
	/**
	 * 分页查询列表
	 * 
	 * @param toolOutbound
	 * @return
	 */
	Page<ToolOutbound> selectPageList(@Param("toolOutbound") ToolOutbound toolOutbound,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param toolOutboundId
	 * @return
	 */
	ToolOutbound selectById(@Param("toolOutboundId") Long toolOutboundId);
	/**
	 * 根据主键查询
	 * 
	 * @param toolOutboundId
	 * @return
	 */
	ToolOutbound selectRowByToolId(@Param("toolId") Long toolId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param toolOutboundId
	 * @return
	 */
	Integer deleteById(@Param("toolOutbound") ToolOutbound toolOutbound);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param toolOutboundId
	 * @return
	 */
	Integer removeById(@Param("toolOutboundId") Long toolOutboundId);
	
	/**
	 * 根据主键查询开始时间
	 * 
	 * @param userId
	 * @param toolOutboundId
	 * @return
	 */
	ToolOutbound selectBeginTimeByToolId(@Param("userId")Long userId,@Param("toolOutbound") ToolOutbound toolOutbound);
	/**
	 * 
	 * @param toolId
	 * @param outType
	 * @return
	 */
	ToolOutbound selectByToolIdAndOutType(@Param("toolId")Long toolId,@Param("outType") Integer outType);
}
