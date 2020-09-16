package org.tsinghuatj.mould.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.mould.domain.MouldScripApply;
import org.tsinghuatj.tool.domain.ToolApplyAudit;

/**
 * @ClassName: IMouldScripApplyService
 * @Description: MouldScripApply服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IMouldScripApplyService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param mouldScripApply
	 * @return
	 */
	Integer insert(Long userId, MouldScripApply mouldScripApply) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param mouldScripApply
	 * @param mouldScripApplyId
	 * @return
	 */
	Integer updateActiveById(Long userId, MouldScripApply mouldScripApply, Long mouldScripApplyId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param mouldScripApply
	 * @return
	 */
	List<MouldScripApply> select(Long userId, MouldScripApply mouldScripApply) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param mouldScripApply
	 * @return
	 */
	Pagination<MouldScripApply> selectPageList(Long userId, MouldScripApply mouldScripApply,QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param mouldScripApplyId
	 * @return
	 */
	MouldScripApply selectById(Long userId, Long mouldScripApplyId) throws BusinessException;

	
	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param mouldScripApplyId
	 * @return
	 */
	Integer deleteById(Long userId, Long mouldScripApplyId) throws BusinessException;
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param mouldScripApplyId
	 * @return
	 */
	Integer removeById(Long userId, Long mouldScripApplyId) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param toolPurchaseApply
	 * @param toolPurchaseApplyId
	 * @return
	 */
	Integer reportAudit(Long userId, String realName, ToolApplyAudit applyAudit, MouldScripApply mouldScripApply) throws BusinessException;
	
	
	boolean fullNumberCheck(String fullNumebr,Long pkId)throws BusinessException;
	
	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param mouldScripApplyId
	 * @return
	 */
	MouldScripApply selectByFullNumber(Long userId,String fullNumber) throws BusinessException;
	
}
