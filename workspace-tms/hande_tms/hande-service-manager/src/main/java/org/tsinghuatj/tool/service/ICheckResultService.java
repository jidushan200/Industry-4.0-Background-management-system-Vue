package org.tsinghuatj.tool.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.tool.domain.CheckResult;

/**
 * @ClassName: ICheckResultService
 * @Description: CheckResult服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface ICheckResultService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param checkResult
	 * @return
	 */
	Integer insert(Long userId, CheckResult checkResult) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param checkResult
	 * @param checkResultId
	 * @return
	 */
	Integer updateActiveById(Long userId, CheckResult checkResult, Long checkResultId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param checkResult
	 * @return
	 */
	List<CheckResult> select(Long userId, CheckResult checkResult) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param checkResult
	 * @return
	 */
	Pagination<CheckResult> selectPageList(Long userId, CheckResult checkResult,QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param checkResultId
	 * @return
	 */
	CheckResult selectById(Long userId, Long checkResultId) throws BusinessException;

	
	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param checkResultId
	 * @return
	 */
	Integer deleteById(Long userId, Long checkResultId) throws BusinessException;
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param checkResultId
	 * @return
	 */
	Integer removeById(Long userId, Long checkResultId) throws BusinessException;
	
	/**
	 * 质检结果表导入
	 */
	Integer checkResultImport(Long userId, List<CheckResult> CheckResultList) throws BusinessException;
}
