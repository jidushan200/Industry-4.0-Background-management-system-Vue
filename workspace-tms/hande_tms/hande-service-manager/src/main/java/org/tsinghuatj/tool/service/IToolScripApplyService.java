package org.tsinghuatj.tool.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.tool.domain.ToolApplyAudit;
import org.tsinghuatj.tool.domain.ToolScripApply;

/**
 * @ClassName: IToolScripApplyService
 * @Description: ToolScripApply服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IToolScripApplyService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param toolScripApply
	 * @return
	 */
	Integer insert(Long userId, ToolScripApply toolScripApply) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param toolScripApply
	 * @param toolScripApplyId
	 * @return
	 */
	Integer updateActiveById(Long userId, ToolScripApply toolScripApply, Long toolScripApplyId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param toolScripApply
	 * @return
	 */
	List<ToolScripApply> select(Long userId, ToolScripApply toolScripApply) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param toolScripApply
	 * @return
	 */
	Pagination<ToolScripApply> selectPageList(Long userId, ToolScripApply toolScripApply,QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param toolScripApplyId
	 * @return
	 */
	ToolScripApply selectById(Long userId, Long toolScripApplyId) throws BusinessException;
	
	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param toolScripApplyId
	 * @return
	 */
	ToolScripApply applyGetByfullNumber(Long userId, String fullNumber) throws BusinessException;

	
	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param toolScripApplyId
	 * @return
	 */
	Integer deleteById(Long userId, Long toolScripApplyId) throws BusinessException;
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param toolScripApplyId
	 * @return
	 */
	Integer removeById(Long userId, Long toolScripApplyId) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param toolPurchaseApply
	 * @param toolPurchaseApplyId
	 * @return
	 */
	Integer reportAudit(Long userId, String realName, ToolApplyAudit applyAudit, ToolScripApply toolscripReport) throws BusinessException;
}
