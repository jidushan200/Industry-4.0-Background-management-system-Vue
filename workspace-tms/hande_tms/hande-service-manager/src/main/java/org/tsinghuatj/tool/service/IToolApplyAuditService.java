package org.tsinghuatj.tool.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.tool.domain.ToolApplyAudit;

/**
 * @ClassName: IToolApplyAuditService
 * @Description: ToolApplyAudit服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IToolApplyAuditService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param toolApplyAudit
	 * @return
	 */
	Integer insert(Long userId, ToolApplyAudit toolApplyAudit) throws BusinessException;
	
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param toolApplyAudit
	 * @param toolApplyAuditId
	 * @return
	 */
	Integer updateActiveById(Long userId, ToolApplyAudit toolApplyAudit, Long toolApplyAuditId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param applyId
	 * @return
	 */
	List<ToolApplyAudit> selectByApplyId(Long userId, Long applyId) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param toolApplyAudit
	 * @return
	 */
	Pagination<ToolApplyAudit> selectPageList(Long userId, ToolApplyAudit toolApplyAudit,QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param toolApplyAuditId
	 * @return
	 */
	ToolApplyAudit selectById(Long userId, Long toolApplyAuditId) throws BusinessException;

	
	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param toolApplyAuditId
	 * @return
	 */
	Integer deleteById(Long userId, Long toolApplyAuditId) throws BusinessException;
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param toolApplyAuditId
	 * @return
	 */
	Integer removeById(Long userId, Long toolApplyAuditId) throws BusinessException;
}
