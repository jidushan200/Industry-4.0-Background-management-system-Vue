package org.tsinghuatj.fixture.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.fixture.domain.FixtureCheckResult;

/**
 * @ClassName: IFixtureCheckResultService
 * @Description: FixtureCheckResult服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IFixtureCheckResultService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param fixtureCheckResult
	 * @return
	 */
	Integer insert(Long userId, FixtureCheckResult fixtureCheckResult) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param fixtureCheckResult
	 * @param fixtureCheckResultId
	 * @return
	 */
	Integer updateActiveById(Long userId, FixtureCheckResult fixtureCheckResult, Long fixtureCheckResultId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param fixtureCheckResult
	 * @return
	 */
	List<FixtureCheckResult> select(Long userId, FixtureCheckResult fixtureCheckResult) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param fixtureCheckResult
	 * @return
	 */
	Pagination<FixtureCheckResult> selectPageList(Long userId, FixtureCheckResult fixtureCheckResult,QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param fixtureCheckResultId
	 * @return
	 */
	FixtureCheckResult selectById(Long userId, Long fixtureCheckResultId) throws BusinessException;

	
	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param fixtureCheckResultId
	 * @return
	 */
	Integer deleteById(Long userId, Long fixtureCheckResultId) throws BusinessException;
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param fixtureCheckResultId
	 * @return
	 */
	Integer removeById(Long userId, Long fixtureCheckResultId) throws BusinessException;
}
