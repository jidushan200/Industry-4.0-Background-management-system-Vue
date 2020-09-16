package org.tsinghuatj.mould.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.mould.domain.MouldRepair;

/**
 * @ClassName: IMouldRepairService
 * @Description: MouldRepair服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IMouldRepairService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param mouldRepair
	 * @return
	 */
	Integer insert(Long userId, MouldRepair mouldRepair) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param mouldRepair
	 * @param mouldRepairId
	 * @return
	 */
	Integer updateActiveById(Long userId, MouldRepair mouldRepair, Long mouldRepairId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param mouldRepair
	 * @return
	 */
	List<MouldRepair> select(Long userId, MouldRepair mouldRepair) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param mouldRepair
	 * @return
	 */
	Pagination<MouldRepair> selectPageList(Long userId, MouldRepair mouldRepair,QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param mouldRepairId
	 * @return
	 */
	MouldRepair selectById(Long userId, Long mouldRepairId) throws BusinessException;

	
	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param mouldRepairId
	 * @return
	 */
	Integer deleteById(Long userId, Long mouldRepairId) throws BusinessException;
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param mouldRepairId
	 * @return
	 */
	Integer removeById(Long userId, Long mouldRepairId) throws BusinessException;
}
