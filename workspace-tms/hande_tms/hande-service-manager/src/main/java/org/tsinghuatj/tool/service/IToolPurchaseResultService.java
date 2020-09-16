package org.tsinghuatj.tool.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.tool.domain.ToolPurchaseResult;

/**
 * @ClassName: IToolPurchaseResultService
 * @Description: ToolPurchaseResult服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IToolPurchaseResultService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param toolPurchaseResult
	 * @return
	 */
	Integer insert(Long userId, ToolPurchaseResult toolPurchaseResult) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param toolPurchaseResult
	 * @param toolPurchaseResultId
	 * @return
	 */
	Integer updateActiveById(Long userId, ToolPurchaseResult toolPurchaseResult, Long toolPurchaseResultId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param toolPurchaseResult
	 * @return
	 */
	List<ToolPurchaseResult> select(Long userId, ToolPurchaseResult toolPurchaseResult) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param toolPurchaseResult
	 * @return
	 */
	Pagination<ToolPurchaseResult> selectPageList(Long userId, ToolPurchaseResult toolPurchaseResult,QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param toolPurchaseResultId
	 * @return
	 */
	ToolPurchaseResult selectById(Long userId, Long toolPurchaseResultId) throws BusinessException;

	
	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param toolPurchaseResultId
	 * @return
	 */
	Integer deleteById(Long userId, Long toolPurchaseResultId) throws BusinessException;
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param toolPurchaseResultId
	 * @return
	 */
	Integer removeById(Long userId, Long toolPurchaseResultId) throws BusinessException;
}
