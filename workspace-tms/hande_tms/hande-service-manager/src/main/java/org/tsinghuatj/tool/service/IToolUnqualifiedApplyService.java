package org.tsinghuatj.tool.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.tool.domain.ToolUnqualifiedApply;

/**
 * @ClassName: IToolUnqualifiedApplyService
 * @Description: ToolUnqualifiedApply服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IToolUnqualifiedApplyService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param toolUnqualifiedApply
	 * @return
	 */
	Integer insert(Long userId, ToolUnqualifiedApply toolUnqualifiedApply) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param toolUnqualifiedApply
	 * @param toolUnqualifiedApplyId
	 * @return
	 */
	Integer updateActiveById(Long userId, ToolUnqualifiedApply toolUnqualifiedApply, Long toolUnqualifiedApplyId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param toolUnqualifiedApply
	 * @return
	 */
	List<ToolUnqualifiedApply> select(Long userId, ToolUnqualifiedApply toolUnqualifiedApply) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param toolUnqualifiedApply
	 * @return
	 */
	Pagination<ToolUnqualifiedApply> selectPageList(Long userId, ToolUnqualifiedApply toolUnqualifiedApply,QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param toolUnqualifiedApplyId
	 * @return
	 */
	ToolUnqualifiedApply selectById(Long userId, Long toolUnqualifiedApplyId) throws BusinessException;

	
	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param toolUnqualifiedApplyId
	 * @return
	 */
	Integer deleteById(Long userId, Long toolUnqualifiedApplyId) throws BusinessException;
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param toolUnqualifiedApplyId
	 * @return
	 */
	Integer removeById(Long userId, Long toolUnqualifiedApplyId) throws BusinessException;
}
