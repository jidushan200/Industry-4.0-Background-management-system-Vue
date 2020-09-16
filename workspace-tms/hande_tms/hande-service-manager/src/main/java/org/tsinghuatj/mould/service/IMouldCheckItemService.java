package org.tsinghuatj.mould.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.mould.domain.MouldCheckItem;

/**
 * @ClassName: IMouldCheckItemService
 * @Description: MouldCheckItem服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IMouldCheckItemService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param mouldCheckItem
	 * @return
	 */
	Integer insert(Long userId, MouldCheckItem mouldCheckItem) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param mouldCheckItem
	 * @param mouldCheckItemId
	 * @return
	 */
	Integer updateActiveById(Long userId, MouldCheckItem mouldCheckItem, Long mouldCheckItemId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param mouldCheckItem
	 * @return
	 */
	List<MouldCheckItem> select(Long userId, MouldCheckItem mouldCheckItem) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param mouldCheckItem
	 * @return
	 */
	Pagination<MouldCheckItem> selectPageList(Long userId, MouldCheckItem mouldCheckItem,QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param mouldCheckItemId
	 * @return
	 */
	MouldCheckItem selectById(Long userId, Long mouldCheckItemId) throws BusinessException;

	
	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param mouldCheckItemId
	 * @return
	 */
	Integer deleteById(Long userId, Long mouldCheckItemId) throws BusinessException;
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param mouldCheckItemId
	 * @return
	 */
	Integer removeById(Long userId, Long mouldCheckItemId) throws BusinessException;
}
