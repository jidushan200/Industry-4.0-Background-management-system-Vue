package org.tsinghuatj.tool.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.tool.domain.ToolBladeComposeDetail;

/**
 * @ClassName: IToolBladeComposeDetailService
 * @Description: ToolBladeComposeDetail服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IToolBladeComposeDetailService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param toolBladeComposeDetail
	 * @return
	 */
	Integer insert(Long userId, ToolBladeComposeDetail toolBladeComposeDetail) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param toolBladeComposeDetail
	 * @param toolBladeComposeDetailId
	 * @return
	 */
	Integer updateActiveById(Long userId, ToolBladeComposeDetail toolBladeComposeDetail, Long toolBladeComposeDetailId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param toolBladeComposeDetail
	 * @return
	 */
	List<ToolBladeComposeDetail> select(Long userId, ToolBladeComposeDetail toolBladeComposeDetail) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param toolBladeComposeDetail
	 * @return
	 */
	Pagination<ToolBladeComposeDetail> selectPageList(Long userId, ToolBladeComposeDetail toolBladeComposeDetail,QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param toolBladeComposeDetailId
	 * @return
	 */
	ToolBladeComposeDetail selectById(Long userId, Long toolBladeComposeDetailId) throws BusinessException;

	
	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param toolBladeComposeDetailId
	 * @return
	 */
	Integer deleteById(Long userId, Long toolBladeComposeDetailId) throws BusinessException;
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param toolBladeComposeDetailId
	 * @return
	 */
	Integer removeById(Long userId, Long toolBladeComposeDetailId) throws BusinessException;
}
