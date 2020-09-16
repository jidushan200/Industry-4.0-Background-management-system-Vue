package org.tsinghuatj.tool.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.tool.domain.ToolBladeCompose;

/**
 * @ClassName: IToolBladeComposeService
 * @Description: ToolBladeCompose服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IToolBladeComposeService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param toolBladeCompose
	 * @return
	 */
	Integer insert(Long userId, String realName, ToolBladeCompose toolBladeCompose) throws BusinessException;

	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param toolBladeCompose
	 * @param toolBladeComposeId
	 * @return
	 */
	Integer updateActiveById(Long userId, ToolBladeCompose toolBladeCompose, Long toolBladeComposeId) throws BusinessException;

	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param toolBladeCompose
	 * @param toolBladeComposeId
	 * @return
	 */
	Integer bladeComposeInstall(Long userId, String userName, ToolBladeCompose toolBladeCompose) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param toolBladeCompose
	 * @return
	 */
	List<ToolBladeCompose> select(Long userId, ToolBladeCompose toolBladeCompose) throws BusinessException;

	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param toolBladeCompose
	 * @return
	 */
	Pagination<ToolBladeCompose> selectPageList(Long userId, ToolBladeCompose toolBladeCompose, QueryDto queryDto) throws BusinessException;

	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param toolBladeCompose
	 * @return
	 */
	Pagination<ToolBladeCompose> selectLifePageList(Long userId, ToolBladeCompose toolBladeCompose, QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param toolBladeComposeId
	 * @return
	 */
	ToolBladeCompose selectById(Long userId, Long toolBladeComposeId) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param composeNumber
	 * @return
	 */
	ToolBladeCompose selectByComposeNumber(Long userId, String composeNumber) throws BusinessException;

	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param toolBladeComposeId
	 * @return
	 */
	Integer deleteById(Long userId, Long toolBladeComposeId) throws BusinessException;

	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param toolBladeComposeId
	 * @return
	 */
	Integer removeById(Long userId, Long toolBladeComposeId) throws BusinessException;

	/**
	 * 刀条送刃磨
	 * 
	 * @param userId
	 * @param toolBladeCompose
	 * @param toolBladeComposeId
	 * @return
	 */
	Integer setRepair(Long userId, String userName, ToolBladeCompose toolBladeCompose, String remark) throws BusinessException;

	String createComposeNumber(Long userId, String headNumber) throws BusinessException;

}
