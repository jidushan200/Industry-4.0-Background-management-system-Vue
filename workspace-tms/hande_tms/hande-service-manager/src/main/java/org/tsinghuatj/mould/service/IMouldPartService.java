package org.tsinghuatj.mould.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.mould.domain.MouldPart;

/**
 * @ClassName: IMouldPartService
 * @Description: MouldPart服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IMouldPartService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param mouldPart
	 * @return
	 */
	Integer insert(Long userId, MouldPart mouldPart) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param mouldPart
	 * @param mouldPartId
	 * @return
	 */
	Integer updateActiveById(Long userId, MouldPart mouldPart, Long mouldPartId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param mouldPart
	 * @return
	 */
	List<MouldPart> select(Long userId, MouldPart mouldPart) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param mouldPart
	 * @return
	 */
	Pagination<MouldPart> selectPageList(Long userId, MouldPart mouldPart,QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param mouldPartId
	 * @return
	 */
	MouldPart selectById(Long userId, Long mouldPartId) throws BusinessException;

	
	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param mouldPartId
	 * @return
	 */
	Integer deleteById(Long userId, Long mouldPartId) throws BusinessException;
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param mouldPartId
	 * @return
	 */
	Integer removeById(Long userId, Long mouldPartId) throws BusinessException;
	
	/**
	 * 
	 * @param pkId
	 * @param toolId
	 * @param partId
	 * @return
	 * @throws BusinessException
	 */
	boolean mouldPartCheck(Long pkId, Long mouldId, Long partId) throws BusinessException;
	
	/**
	 * 
	 * @param userId
	 * @param tpList
	 * @return
	 * @throws BusinessException
	 */
	Integer mouldPartImport(Long userId, List<MouldPart> mouldPartList) throws BusinessException;
}
