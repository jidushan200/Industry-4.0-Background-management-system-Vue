package org.tsinghuatj.tool.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;

import org.tsinghuatj.tool.domain.ToolUnqualifiedReport;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface ToolUnqualifiedReportMapper {

	/**
	 * 插入数据
	 * 
	 * @param toolUnqualifiedReport
	 * @return
	 */
	Integer insert(@Param("toolUnqualifiedReport") ToolUnqualifiedReport toolUnqualifiedReport);

	/**
	 * 根据主键更新有值数据
	 * 
	 * @param toolUnqualifiedReport
	 * @param toolUnqualifiedReportId
	 * @return
	 */
	Integer updateActiveById(@Param("toolUnqualifiedReport") ToolUnqualifiedReport toolUnqualifiedReport, @Param("toolUnqualifiedReportId") Long toolUnqualifiedReportId);

	/**
	 * 查询列表
	 * 
	 * @param toolUnqualifiedReport
	 * @return
	 */
	List<ToolUnqualifiedReport> select(@Param("toolUnqualifiedReport") ToolUnqualifiedReport toolUnqualifiedReport);

	/**
	 * 查询列表
	 * 
	 * @param toolUnqualifiedReport
	 * @return
	 */
	ToolUnqualifiedReport selectFullNumberReportType(@Param("fullNumber") String fullNumber, @Param("reportType") Integer reportType);

	/**
	 * 分页查询列表
	 * 
	 * @param toolUnqualifiedReport
	 * @return
	 */
	Page<ToolUnqualifiedReport> selectPageList(@Param("toolUnqualifiedReport") ToolUnqualifiedReport toolUnqualifiedReport, @Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param toolUnqualifiedReportId
	 * @return
	 */
	ToolUnqualifiedReport selectById(@Param("toolUnqualifiedReportId") Long toolUnqualifiedReportId);

	/**
	 * 按主键物理删除
	 * 
	 * @param toolUnqualifiedReportId
	 * @return
	 */
	Integer deleteById(@Param("toolUnqualifiedReport") ToolUnqualifiedReport toolUnqualifiedReport);

	/**
	 * 按主键物理删除
	 * 
	 * @param toolUnqualifiedReportId
	 * @return
	 */
	Integer removeById(@Param("toolUnqualifiedReportId") Long toolUnqualifiedReportId);
}
