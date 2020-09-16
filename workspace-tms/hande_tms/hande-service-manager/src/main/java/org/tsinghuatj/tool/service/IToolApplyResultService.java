package org.tsinghuatj.tool.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.tool.domain.ToolApplyResult;

/**
 * @ClassName: IToolApplyResultService
 * @Description: ToolApplyResult服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IToolApplyResultService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param toolApplyResult
	 * @return
	 */
	Integer insert(Long userId, ToolApplyResult toolApplyResult) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param toolApplyResult
	 * @param toolApplyResultId
	 * @return
	 */
	Integer updateActiveById(Long userId, ToolApplyResult toolApplyResult, Long toolApplyResultId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param toolApplyResult
	 * @return
	 */
	List<ToolApplyResult> select(Long userId, ToolApplyResult toolApplyResult) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param toolApplyResult
	 * @return
	 */
	Pagination<ToolApplyResult> selectPageList(Long userId, ToolApplyResult toolApplyResult,QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param toolApplyResultId
	 * @return
	 */
	ToolApplyResult selectById(Long userId, Long toolApplyResultId) throws BusinessException;

	
	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param toolApplyResultId
	 * @return
	 */
	Integer deleteById(Long userId, Long toolApplyResultId) throws BusinessException;
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param toolApplyResultId
	 * @return
	 */
	Integer removeById(Long userId, Long toolApplyResultId) throws BusinessException;
}
