package org.tsinghuatj.tool.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.tool.domain.ToolBladeComposeDetail;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface ToolBladeComposeDetailMapper {

	/**
	 * 插入数据
	 * 
	 * @param toolBladeComposeDetail
	 * @return
	 */
	Integer insert(@Param("toolBladeComposeDetail") ToolBladeComposeDetail toolBladeComposeDetail);

	/**
	 * 根据主键更新有值数据
	 * 
	 * @param toolBladeComposeDetail
	 * @param toolBladeComposeDetailId
	 * @return
	 */
	Integer updateActiveById(@Param("toolBladeComposeDetail") ToolBladeComposeDetail toolBladeComposeDetail, @Param("toolBladeComposeDetailId") Long toolBladeComposeDetailId);

	/**
	 * 根据主键更新有值数据
	 * 
	 * @param toolBladeComposeDetail
	 * @param toolBladeComposeDetailId
	 * @return
	 */
	Integer updateActiveByNumber(@Param("toolBladeComposeDetail") ToolBladeComposeDetail toolBladeComposeDetail);

	
	
	/**
	 * 查询列表
	 * 
	 * @param toolBladeComposeDetail
	 * @return
	 */
	List<ToolBladeComposeDetail> select(@Param("toolBladeComposeDetail") ToolBladeComposeDetail toolBladeComposeDetail);

	/**
	 * 查询列表
	 * 
	 * @param toolBladeComposeDetail
	 * @return
	 */
	List<ToolBladeComposeDetail> selectByComposeList(@Param("numberList") List<String> numberList);

	

	ToolBladeComposeDetail selectByComposeNumberToolNumber(@Param("composeNumber") String composeNumber, @Param("toolNumber") String toolNumber);

	/**
	 * 分页查询列表
	 * 
	 * @param toolBladeComposeDetail
	 * @return
	 */
	Page<ToolBladeComposeDetail> selectPageList(@Param("toolBladeComposeDetail") ToolBladeComposeDetail toolBladeComposeDetail, @Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param toolBladeComposeDetailId
	 * @return
	 */
	ToolBladeComposeDetail selectById(@Param("toolBladeComposeDetailId") Long toolBladeComposeDetailId);

	/**
	 * 按主键物理删除
	 * 
	 * @param toolBladeComposeDetailId
	 * @return
	 */
	Integer deleteById(@Param("toolBladeComposeDetail") ToolBladeComposeDetail toolBladeComposeDetail);

	/**
	 * 按主键物理删除
	 * 
	 * @param toolBladeComposeDetailId
	 * @return
	 */
	Integer removeById(@Param("toolBladeComposeDetailId") Long toolBladeComposeDetailId);
}
