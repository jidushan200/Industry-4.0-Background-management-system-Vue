package org.tsinghuatj.tool.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.tool.domain.ToolBladeCompose;
import org.tsinghuatj.tool.domain.ToolBladeOutbound;

/**
 * @ClassName: IToolBladeOutboundService
 * @Description: ToolBladeOutbound服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IToolBladeOutboundService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param toolBladeOutbound
	 * @return
	 */
	Integer insert(Long userId, ToolBladeOutbound toolBladeOutbound, ToolBladeCompose bladeCompose, Long bladeId) throws BusinessException;

	/**
	 * 组合出库
	 * 
	 * @param userId
	 * @param toolBladeOutbound
	 * @return
	 */
	Integer complexOutbound(Long userId, String headNumber, String composeNumber, String plateNumber, Long departmentId, String departmentName, Long teamId, String teamName, String receiver, String remark, String bladeJson) throws BusinessException;

	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param toolBladeOutbound
	 * @param toolBladeOutboundId
	 * @return
	 */
	Integer updateActiveById(Long userId, ToolBladeOutbound toolBladeOutbound, Long toolBladeOutboundId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param toolBladeOutbound
	 * @return
	 */
	List<ToolBladeOutbound> select(Long userId, ToolBladeOutbound toolBladeOutbound) throws BusinessException;

	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param toolBladeOutbound
	 * @return
	 */
	Pagination<ToolBladeOutbound> selectPageList(Long userId, ToolBladeOutbound toolBladeOutbound, QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param toolBladeOutboundId
	 * @return
	 */
	ToolBladeOutbound selectById(Long userId, Long toolBladeOutboundId) throws BusinessException;

	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param toolBladeOutboundId
	 * @return
	 */
	Integer deleteById(Long userId, Long toolBladeOutboundId) throws BusinessException;

	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param toolBladeOutboundId
	 * @return
	 */
	Integer removeById(Long userId, Long toolBladeOutboundId) throws BusinessException;
}
