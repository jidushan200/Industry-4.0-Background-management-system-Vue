package org.tsinghuatj.tool.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;

import org.tsinghuatj.tool.domain.ToolBladeScrapDetail;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface ToolBladeScrapDetailMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param toolBladeScrapDetail
	 * @return
	 */
	Integer insert(@Param("toolBladeScrapDetail") ToolBladeScrapDetail toolBladeScrapDetail);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param toolBladeScrapDetail
	 * @param toolBladeScrapDetailId
	 * @return
	 */
	Integer updateActiveById(@Param("toolBladeScrapDetail") ToolBladeScrapDetail toolBladeScrapDetail, @Param("toolBladeScrapDetailId") Long toolBladeScrapDetailId);

	/**
	 * 查询列表
	 * 
	 * @param toolBladeScrapDetail
	 * @return
	 */
	List<ToolBladeScrapDetail> select(@Param("toolBladeScrapDetail") ToolBladeScrapDetail toolBladeScrapDetail);
	
	
	/**
	 * 查询列表
	 * 
	 * @param toolBladeScrapDetail
	 * @return
	 */
	List<ToolBladeScrapDetail>  selectByComposeList(@Param("numberList") List<String> numberList);
	
	/**
	 * 查询列表
	 * 
	 * @param toolBladeScrapDetail
	 * @return
	 */
	List<ToolBladeScrapDetail>  selectByComposeNumber(@Param("composeNumber") String composeNumber);
	
	/**
	 * 分页查询列表
	 * 
	 * @param toolBladeScrapDetail
	 * @return
	 */
	Page<ToolBladeScrapDetail> selectPageList(@Param("toolBladeScrapDetail") ToolBladeScrapDetail toolBladeScrapDetail,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param toolBladeScrapDetailId
	 * @return
	 */
	ToolBladeScrapDetail selectById(@Param("toolBladeScrapDetailId") Long toolBladeScrapDetailId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param toolBladeScrapDetailId
	 * @return
	 */
	Integer deleteById(@Param("toolBladeScrapDetail") ToolBladeScrapDetail toolBladeScrapDetail);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param toolBladeScrapDetailId
	 * @return
	 */
	Integer removeById(@Param("toolBladeScrapDetailId") Long toolBladeScrapDetailId);
}
