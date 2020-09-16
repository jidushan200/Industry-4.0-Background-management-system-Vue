package org.tsinghuatj.tool.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.tool.domain.ToolBladeScrapDetail;

/**
 * @ClassName: IToolBladeScrapDetailService
 * @Description: ToolBladeScrapDetail服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IToolBladeScrapDetailService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param toolBladeScrapDetail
	 * @return
	 */
	Integer insert(Long userId, ToolBladeScrapDetail toolBladeScrapDetail) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param toolBladeScrapDetail
	 * @param toolBladeScrapDetailId
	 * @return
	 */
	Integer updateActiveById(Long userId, ToolBladeScrapDetail toolBladeScrapDetail, Long toolBladeScrapDetailId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param toolBladeScrapDetail
	 * @return
	 */
	List<ToolBladeScrapDetail> select(Long userId, ToolBladeScrapDetail toolBladeScrapDetail) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param toolBladeScrapDetail
	 * @return
	 */
	Pagination<ToolBladeScrapDetail> selectPageList(Long userId, ToolBladeScrapDetail toolBladeScrapDetail,QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param toolBladeScrapDetailId
	 * @return
	 */
	ToolBladeScrapDetail selectById(Long userId, Long toolBladeScrapDetailId) throws BusinessException;

	
	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param toolBladeScrapDetailId
	 * @return
	 */
	Integer deleteById(Long userId, Long toolBladeScrapDetailId) throws BusinessException;
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param toolBladeScrapDetailId
	 * @return
	 */
	Integer removeById(Long userId, Long toolBladeScrapDetailId) throws BusinessException;
}
