package org.tsinghuatj.mould.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.mould.domain.MouldRepairProcedure;

/**
 * @ClassName: IMouldRepairProcedureService
 * @Description: MouldRepairProcedure服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IMouldRepairProcedureService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param mouldRepairProcedure
	 * @return
	 */
	Integer insert(Long userId, MouldRepairProcedure mouldRepairProcedure) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param mouldRepairProcedure
	 * @param mouldRepairProcedureId
	 * @return
	 */
	Integer updateActiveById(Long userId, MouldRepairProcedure mouldRepairProcedure, Long mouldRepairProcedureId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param mouldRepairProcedure
	 * @return
	 */
	List<MouldRepairProcedure> select(Long userId, MouldRepairProcedure mouldRepairProcedure) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param mouldRepairProcedure
	 * @return
	 */
	Pagination<MouldRepairProcedure> selectPageList(Long userId, MouldRepairProcedure mouldRepairProcedure,QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param mouldRepairProcedureId
	 * @return
	 */
	MouldRepairProcedure selectById(Long userId, Long mouldRepairProcedureId) throws BusinessException;

	
	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param mouldRepairProcedureId
	 * @return
	 */
	Integer deleteById(Long userId, Long mouldRepairProcedureId) throws BusinessException;
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param mouldRepairProcedureId
	 * @return
	 */
	Integer removeById(Long userId, Long mouldRepairProcedureId) throws BusinessException;
}
