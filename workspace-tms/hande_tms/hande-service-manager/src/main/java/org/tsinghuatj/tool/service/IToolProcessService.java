package org.tsinghuatj.tool.service;

import java.math.BigDecimal;
import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.tool.domain.ToolProcess;

/**
 * @ClassName: IToolProcessService
 * @Description: ToolProcess服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IToolProcessService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param toolProcess
	 * @return
	 */
	Integer insert(Long userId, String userName, ToolProcess toolProcess,BigDecimal repairMeasure) throws BusinessException;
	
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param toolProcess
	 * @return
	 */
	Integer noProcessReturn(Long userId,String fullNumber) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param toolProcess
	 * @param toolProcessId
	 * @return
	 */
	Integer updateActiveById(Long userId, ToolProcess toolProcess, Long toolProcessId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param toolProcess
	 * @return
	 */
	List<ToolProcess> select(Long userId, ToolProcess toolProcess) throws BusinessException;
	
	
	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param toolProcess
	 * @return
	 */
	ToolProcess selectLatestProcess(Long userId, ToolProcess toolProcess) throws BusinessException;
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param toolProcess
	 * @return
	 */
	Pagination<ToolProcess> selectPageList(Long userId, ToolProcess toolProcess,QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param toolProcessId
	 * @return
	 */
	ToolProcess selectById(Long userId, Long toolProcessId) throws BusinessException;

	
	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param toolProcessId
	 * @return
	 */
	Integer deleteById(Long userId, Long toolProcessId) throws BusinessException;
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param toolProcessId
	 * @return
	 */
	Integer removeById(Long userId, Long toolProcessId) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param toolProcess
	 * @return
	 */
	Pagination<ToolProcess> selectCoatProcessPageList(Long userId, ToolProcess toolProcess,QueryDto queryDto) throws BusinessException;
}
