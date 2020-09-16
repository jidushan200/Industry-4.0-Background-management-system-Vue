package org.tsinghuatj.measure.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.measure.domain.MeasureOutbound;

/**
 * @ClassName: IMeasureOutboundService
 * @Description: MeasureOutbound服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IMeasureOutboundService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param measureOutbound
	 * @return
	 */
	Integer insert(Long userId, MeasureOutbound measureOutbound) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param measureOutbound
	 * @param measureOutboundId
	 * @return
	 */
	Integer updateActiveById(Long userId, MeasureOutbound measureOutbound, Long measureOutboundId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param measureOutbound
	 * @return
	 */
	List<MeasureOutbound> select(Long userId, MeasureOutbound measureOutbound) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param measureOutbound
	 * @return
	 */
	Pagination<MeasureOutbound> selectPageList(Long userId, MeasureOutbound measureOutbound,QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param measureOutboundId
	 * @return
	 */
	MeasureOutbound selectById(Long userId, Long measureOutboundId) throws BusinessException;

	
	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param measureOutboundId
	 * @return
	 */
	Integer deleteById(Long userId, Long measureOutboundId) throws BusinessException;
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param measureOutboundId
	 * @return
	 */
	Integer removeById(Long userId, Long measureOutboundId) throws BusinessException;
}
