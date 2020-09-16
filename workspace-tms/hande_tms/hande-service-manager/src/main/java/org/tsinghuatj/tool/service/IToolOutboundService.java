package org.tsinghuatj.tool.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.tool.domain.ToolOutbound;

/**
 * @ClassName: IToolOutboundService
 * @Description: ToolOutbound服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IToolOutboundService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param toolOutbound
	 * @return
	 */
	Integer insert(Long userId, ToolOutbound toolOutbound,Integer typeId) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param toolOutbound
	 * @param toolOutboundId
	 * @return
	 */
	Integer updateActiveById(Long userId, ToolOutbound toolOutbound, Long toolOutboundId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param toolOutbound
	 * @return
	 */
	List<ToolOutbound> select(Long userId, ToolOutbound toolOutbound) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param toolOutbound
	 * @return
	 */
	Pagination<ToolOutbound> selectPageList(Long userId, ToolOutbound toolOutbound,QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param toolOutboundId
	 * @return
	 */
	ToolOutbound selectById(Long userId, Long toolOutboundId) throws BusinessException;
	
	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param toolOutboundId
	 * @return
	 */
	ToolOutbound selectRowByToolId(Long userId, Long toolId) throws BusinessException;
	
	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param toolOutboundId
	 * @return
	 */
	ToolOutbound selectByToolIdAndOutType(Long userId, Long toolId,Integer outType) throws BusinessException;
	
	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param toolOutboundId
	 * @return
	 */
	Integer deleteById(Long userId, Long toolOutboundId) throws BusinessException;
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param toolOutboundId
	 * @return
	 */
	Integer removeById(Long userId, Long toolOutboundId) throws BusinessException;
}
