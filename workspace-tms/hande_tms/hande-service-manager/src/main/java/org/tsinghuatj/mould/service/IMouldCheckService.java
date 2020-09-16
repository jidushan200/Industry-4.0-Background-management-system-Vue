package org.tsinghuatj.mould.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.mould.domain.MouldCheck;

/**
 * @ClassName: IMouldCheckService
 * @Description: MouldCheck服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IMouldCheckService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param mouldCheck
	 * @return
	 */
	Integer insert(Long userId, MouldCheck mouldCheck, String appendixIds) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param mouldCheck
	 * @param mouldCheckId
	 * @return
	 */
	Integer updateActiveById(Long userId, MouldCheck mouldCheck, Long mouldCheckId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param mouldCheck
	 * @return
	 */
	List<MouldCheck> select(Long userId, MouldCheck mouldCheck) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param mouldCheck
	 * @return
	 */
	Pagination<MouldCheck> selectPageList(Long userId, MouldCheck mouldCheck,QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param mouldCheckId
	 * @return
	 */
	MouldCheck selectById(Long userId, Long mouldCheckId) throws BusinessException;

	
	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param mouldCheckId
	 * @return
	 */
	Integer deleteById(Long userId, Long mouldCheckId) throws BusinessException;
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param mouldCheckId
	 * @return
	 */
	Integer removeById(Long userId, Long mouldCheckId) throws BusinessException;
}
