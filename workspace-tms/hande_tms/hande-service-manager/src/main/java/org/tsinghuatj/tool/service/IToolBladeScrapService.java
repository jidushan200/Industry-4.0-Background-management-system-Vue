package org.tsinghuatj.tool.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.tool.domain.ToolBladeCompose;
import org.tsinghuatj.tool.domain.ToolBladeScrap;

/**
 * @ClassName: IToolBladeScripService
 * @Description: ToolBladeScrip服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IToolBladeScrapService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param toolBladeScrip
	 * @return
	 */
	Integer insert(Long userId, ToolBladeScrap toolBladeScrip,String detailJson) throws BusinessException;

	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param toolBladeScrip
	 * @param toolBladeScripId
	 * @return
	 */
	Integer updateActiveById(Long userId, ToolBladeScrap toolBladeScrip, Long toolBladeScripId,String detailJson) throws BusinessException;

	/**
	 * 审核
	 * 
	 * @param userId
	 * @param toolBladeScrip
	 * @param toolBladeScripId
	 * @return
	 */
	Integer audit(Long userId, String realName, Long departMentId, String departMentName, String remark, ToolBladeScrap toolBladeScrip) throws BusinessException;

	/**
	 * 审核
	 * 
	 * @param userId
	 * @param toolBladeScrip
	 * @param toolBladeScripId
	 * @return
	 */
	Integer bladeScrap(Long userId, String composeNumber) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param toolBladeScrip
	 * @return
	 */
	List<ToolBladeScrap> select(Long userId, ToolBladeScrap toolBladeScrip) throws BusinessException;

	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param toolBladeScrip
	 * @return
	 */
	Pagination<ToolBladeScrap> selectPageList(Long userId, ToolBladeScrap toolBladeScrip, QueryDto queryDto) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param toolBladeScrip
	 * @return
	 */
	Pagination<ToolBladeCompose> selectScrapPageList(Long userId,ToolBladeCompose toolBladeCompose, QueryDto queryDto) throws BusinessException;


	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param toolBladeScripId
	 * @return
	 */
	ToolBladeScrap selectById(Long userId, Long toolBladeScripId) throws BusinessException;

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
	 * @param toolBladeScripId
	 * @return
	 */
	Integer deleteById(Long userId, Long toolBladeScripId) throws BusinessException;

	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param toolBladeScripId
	 * @return
	 */
	Integer removeById(Long userId, Long toolBladeScripId) throws BusinessException;
}
