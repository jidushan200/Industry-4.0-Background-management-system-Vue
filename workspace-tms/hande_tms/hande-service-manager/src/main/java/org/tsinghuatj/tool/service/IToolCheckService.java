package org.tsinghuatj.tool.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.tool.domain.ToolCheck;

/**
 * @ClassName: IToolCheckService
 * @Description: ToolCheck服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IToolCheckService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param toolCheck
	 * @return
	 */
	Integer insert(Long userId, ToolCheck toolCheck, String appendixIds) throws BusinessException;

	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param toolCheck
	 * @param toolCheckId
	 * @return
	 */
	Integer updateActiveById(Long userId, ToolCheck toolCheck, Long toolCheckId, String appendixIds)
			throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param toolCheck
	 * @return
	 */
	List<ToolCheck> select(Long userId, ToolCheck toolCheck) throws BusinessException;

	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param toolCheck
	 * @return
	 */
	Pagination<ToolCheck> selectPageList(Long userId, ToolCheck toolCheck, QueryDto queryDto) throws BusinessException;
	
	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param toolCheckId
	 * @return
	 */
	ToolCheck selectById(Long userId, Long toolCheckId) throws BusinessException;

	/**
	 * 根据刀具id查询
	 * 
	 * @param userId
	 * @param toolId
	 * @param checkType
	 * @return
	 */
	ToolCheck selectByToolId(Long userId, Long toolId, Integer checkType) throws BusinessException;
	
	/**
	 * 根据刀具id查询
	 * 
	 * @param userId
	 * @param toolId
	 * @param checkType
	 * @return
	 */
	ToolCheck toolCheckGetBySeq(Long userId, String toolNumber, String toolSeq) throws BusinessException;

	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param toolCheckId
	 * @return
	 */
	Integer deleteById(Long userId, Long toolCheckId) throws BusinessException;

	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param toolCheckId
	 * @return
	 */
	Integer removeById(Long userId, Long toolCheckId) throws BusinessException;
	
	
	Integer toolBladeSetHandle(Long userId, Long toolCheckId,Integer handleType) throws BusinessException;
	
	/**
	 * 送涂层
	 * @param userId
	 * @param toolCheckId
	 * @return
	 * @throws BusinessException
	 */
	Integer toolBladeSetCoat(Long userId, Long toolCheckId) throws BusinessException;
}
