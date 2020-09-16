package org.tsinghuatj.tool.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.tool.domain.Tool;
import org.tsinghuatj.tool.domain.ToolBase;
import org.tsinghuatj.tool.domain.ToolWarehouse;

/**
 * @ClassName: IToolService
 * @Description: Tool服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IToolService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param tool
	 * @return
	 */
	Integer insert(Long userId, Tool tool, ToolWarehouse toolWarehouse, Long handleId) throws BusinessException;

	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param tool
	 * @param toolId
	 * @return
	 */
	Integer updateActiveById(Long userId, Tool tool, Long toolId, ToolWarehouse toolWarehouse) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param tool
	 * @return
	 */
	List<Tool> select(Long userId, Tool tool) throws BusinessException;

	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param tool
	 * @return
	 */
	Pagination<Tool> selectPageList(Long userId, Tool tool, QueryDto queryDto) throws BusinessException;

	/**
	 * 分页查询列表
	 * 
	 * @param toolCheck
	 * @return
	 */
	Pagination<Tool> selectWaitCheckPageList(Tool tool, QueryDto queryDto);

	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param tool
	 * @return
	 */
	Pagination<Tool> selectLifePageList(Long userId, Tool tool, QueryDto queryDto) throws BusinessException;

	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param tool
	 * @return
	 */
	Pagination<Tool> selectWaitRepairPageList(Long userId, Tool tool, QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param toolId
	 * @return
	 */
	Tool selectById(Long userId, Long toolId) throws BusinessException;

	/**
	 * 根据编号查询
	 * 
	 * @param userId
	 * @param toolNumber
	 * @return
	 */
	Tool selectByFullNumber(Long userId, String fullNumber) throws BusinessException;

	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param toolId
	 * @return
	 */
	Integer deleteById(Long userId, Long toolId) throws BusinessException;

	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param toolId
	 * @return
	 */
	Integer removeById(Long userId, Long toolId) throws BusinessException;

	/**
	 * 根据删除标识查询
	 */
	Pagination<Tool> selectBydelMark(Long userId, Tool tool, QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param toolNumber
	 * @return
	 */
	ToolBase toolGetByToolNumber(Long userId, String toolNumber) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param toolNumber
	 * @return
	 */
	ToolBase purchaseGetByToolNumber(Long userId, String toolNumber) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param toolNumber
	 * @return
	 */
	ToolBase purchaseGetByToolName(Long userId, String toolMap) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param toolNumber
	 * @return
	 */
	Tool toolGetSeqByToolNumber(Long userId, String toolNumber) throws BusinessException;

	void exportToolLife(HttpServletResponse response, HttpServletRequest request, Long userId, Tool tool, QueryDto queryDto) throws Exception;

	/**
	 * 刀具基础信息表导入
	 */
	Integer toolImport(Long userId, List<Tool> toolList) throws BusinessException;

	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param tool
	 * @return
	 */
	Pagination<Tool> selectStatisticsPageList(Long userId, Tool tool, QueryDto queryDto) throws BusinessException;

	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param tool
	 * @return
	 */
	Pagination<Tool> selectToolStatisticsPageList(Long userId, Tool tool, QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param tool
	 * @param toolId
	 * @return
	 */
	Integer toolScripById(Long userId, String realName, Long toolId) throws BusinessException;
}
