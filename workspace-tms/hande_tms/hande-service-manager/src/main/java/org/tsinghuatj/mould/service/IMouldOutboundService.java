package org.tsinghuatj.mould.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.mould.domain.MouldOutbound;

/**
 * @ClassName: IMouldOutboundService
 * @Description: MouldOutbound服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IMouldOutboundService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param mouldOutbound
	 * @return
	 */
	Integer insert(Long userId, MouldOutbound mouldOutbound) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param mouldOutbound
	 * @param mouldOutboundId
	 * @return
	 */
	Integer updateActiveById(Long userId, MouldOutbound mouldOutbound, Long mouldOutboundId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param mouldOutbound
	 * @return
	 */
	List<MouldOutbound> select(Long userId, MouldOutbound mouldOutbound) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param mouldOutbound
	 * @return
	 */
	Pagination<MouldOutbound> selectPageList(Long userId, MouldOutbound mouldOutbound,QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param mouldOutboundId
	 * @return
	 */
	MouldOutbound selectById(Long userId, Long mouldOutboundId) throws BusinessException;

	
	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param mouldOutboundId
	 * @return
	 */
	Integer deleteById(Long userId, Long mouldOutboundId) throws BusinessException;
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param mouldOutboundId
	 * @return
	 */
	Integer removeById(Long userId, Long mouldOutboundId) throws BusinessException;
}
