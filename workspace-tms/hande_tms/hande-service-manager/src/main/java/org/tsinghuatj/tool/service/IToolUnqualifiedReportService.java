package org.tsinghuatj.tool.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.tool.domain.ToolUnqualifiedReport;

/**
 * @ClassName: IToolUnqualifiedReportService
 * @Description: ToolUnqualifiedReport服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IToolUnqualifiedReportService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param toolUnqualifiedReport
	 * @return
	 */
	Integer insert(Long userId, ToolUnqualifiedReport toolUnqualifiedReport,Integer status) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param toolUnqualifiedReport
	 * @param toolUnqualifiedReportId
	 * @return
	 */
	Integer updateActiveById(Long userId, Long pkId,Integer status,String reportDesc) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param toolUnqualifiedReport
	 * @return
	 */
	List<ToolUnqualifiedReport> select(Long userId, ToolUnqualifiedReport toolUnqualifiedReport) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param toolUnqualifiedReport
	 * @return
	 */
	Pagination<ToolUnqualifiedReport> selectPageList(Long userId, ToolUnqualifiedReport toolUnqualifiedReport,QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param toolUnqualifiedReportId
	 * @return
	 */
	ToolUnqualifiedReport selectById(Long userId, Long toolUnqualifiedReportId,boolean includeChcek) throws BusinessException;

	
	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param toolUnqualifiedReportId
	 * @return
	 */
	Integer deleteById(Long userId, Long toolUnqualifiedReportId) throws BusinessException;
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param toolUnqualifiedReportId
	 * @return
	 */
	Integer removeById(Long userId, Long toolUnqualifiedReportId) throws BusinessException;
	
	/**
	 * 
	 * @param userId
	 * @param toolUnqualifiedReport
	 * @param remark
	 * @return
	 * @throws BusinessException
	 */
	Integer reportAudit(Long userId, String realName, ToolUnqualifiedReport toolUnqualifiedReport,String remark) throws BusinessException;
}
