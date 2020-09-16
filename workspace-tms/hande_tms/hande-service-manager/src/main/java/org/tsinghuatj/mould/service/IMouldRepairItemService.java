package org.tsinghuatj.mould.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.mould.domain.MouldRepairItem;

/**
 * @ClassName: IMouldRepairItemService
 * @Description: MouldRepairItem服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IMouldRepairItemService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param mouldRepairItem
	 * @return
	 */
	Integer insert(Long userId, MouldRepairItem mouldRepairItem) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param mouldRepairItem
	 * @param mouldRepairItemId
	 * @return
	 */
	Integer updateActiveById(Long userId, MouldRepairItem mouldRepairItem, Long mouldRepairItemId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param mouldRepairItem
	 * @return
	 */
	List<MouldRepairItem> select(Long userId, MouldRepairItem mouldRepairItem) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param mouldRepairItem
	 * @return
	 */
	Pagination<MouldRepairItem> selectPageList(Long userId, MouldRepairItem mouldRepairItem,QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param mouldRepairItemId
	 * @return
	 */
	MouldRepairItem selectById(Long userId, Long mouldRepairItemId) throws BusinessException;

	
	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param mouldRepairItemId
	 * @return
	 */
	Integer deleteById(Long userId, Long mouldRepairItemId) throws BusinessException;
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param mouldRepairItemId
	 * @return
	 */
	Integer removeById(Long userId, Long mouldRepairItemId) throws BusinessException;
	
	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param mouldRepairItem
	 * @return
	 */
	List<MouldRepairItem> selectByRepairId(Long userId, Long repairId) throws BusinessException;
}
