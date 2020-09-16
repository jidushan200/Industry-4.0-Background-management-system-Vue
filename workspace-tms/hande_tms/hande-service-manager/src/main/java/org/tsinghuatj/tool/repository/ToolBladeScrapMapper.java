package org.tsinghuatj.tool.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;

import org.tsinghuatj.tool.domain.ToolBladeScrap;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface ToolBladeScrapMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param toolBladeScrap
	 * @return
	 */
	Integer insert(@Param("toolBladeScrap") ToolBladeScrap toolBladeScrap);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param toolBladeScrap
	 * @param toolBladeScrapId
	 * @return
	 */
	Integer updateActiveById(@Param("toolBladeScrap") ToolBladeScrap toolBladeScrap, @Param("toolBladeScrapId") Long toolBladeScrapId);

	/**
	 * 查询列表
	 * 
	 * @param toolBladeScrap
	 * @return
	 */
	List<ToolBladeScrap> select(@Param("toolBladeScrap") ToolBladeScrap toolBladeScrap);
	
	/**
	 * 分页查询列表
	 * 
	 * @param toolBladeScrap
	 * @return
	 */
	Page<ToolBladeScrap> selectPageList(@Param("toolBladeScrap") ToolBladeScrap toolBladeScrap,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param toolBladeScrapId
	 * @return
	 */
	ToolBladeScrap selectById(@Param("toolBladeScrapId") Long toolBladeScrapId);
	
	/**
	 * 根据主键查询
	 * 
	 * @param toolBladeScrapId
	 * @return
	 */
	ToolBladeScrap selectByComposeNumber(@Param("composeNumber") String composeNumber);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param toolBladeScrapId
	 * @return
	 */
	Integer deleteById(@Param("toolBladeScrap") ToolBladeScrap toolBladeScrap);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param toolBladeScrapId
	 * @return
	 */
	Integer removeById(@Param("toolBladeScrapId") Long toolBladeScrapId);
}
