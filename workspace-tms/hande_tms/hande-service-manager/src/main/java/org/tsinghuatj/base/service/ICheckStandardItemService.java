package org.tsinghuatj.base.service;

import java.util.List;

import org.tsinghuatj.base.domain.CheckStandardItem;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;

/**
 * @ClassName: ICheckStandardItemService
 * @Description: CheckStandardItem服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface ICheckStandardItemService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param checkStandardItem
	 * @return
	 */
	Integer insert(Long userId, CheckStandardItem checkStandardItem) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param checkStandardItem
	 * @param checkStandardItemId
	 * @return
	 */
	Integer updateActiveById(Long userId, CheckStandardItem checkStandardItem, Long checkStandardItemId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param checkStandardItem
	 * @return
	 */
	List<CheckStandardItem> select(Long userId, CheckStandardItem checkStandardItem) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param checkStandardItem
	 * @return
	 */
	Pagination<CheckStandardItem> selectPageList(Long userId, CheckStandardItem checkStandardItem,QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param checkStandardItemId
	 * @return
	 */
	CheckStandardItem selectById(Long userId, Long checkStandardItemId) throws BusinessException;

	
	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param checkStandardItemId
	 * @return
	 */
	Integer deleteById(Long userId, Long checkStandardItemId) throws BusinessException;
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param checkStandardItemId
	 * @return
	 */
	Integer removeById(Long userId, Long checkStandardItemId) throws BusinessException;
}
