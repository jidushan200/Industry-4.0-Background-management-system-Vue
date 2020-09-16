package org.tsinghuatj.fixture.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.fixture.domain.FixtureWaitCheck;

/**
 * @ClassName: IFixtureWaitCheckService
 * @Description: FixtureWaitCheck服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IFixtureWaitCheckService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param fixtureWaitCheck
	 * @return
	 */
	Integer insert(Long userId, FixtureWaitCheck fixtureWaitCheck) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param fixtureWaitCheck
	 * @param fixtureWaitCheckId
	 * @return
	 */
	Integer updateActiveById(Long userId, FixtureWaitCheck fixtureWaitCheck, Long fixtureWaitCheckId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param fixtureWaitCheck
	 * @return
	 */
	List<FixtureWaitCheck> select(Long userId, FixtureWaitCheck fixtureWaitCheck) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param fixtureWaitCheck
	 * @return
	 */
	Pagination<FixtureWaitCheck> selectPageList(Long userId, FixtureWaitCheck fixtureWaitCheck,QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param fixtureWaitCheckId
	 * @return
	 */
	FixtureWaitCheck selectById(Long userId, Long fixtureWaitCheckId) throws BusinessException;

	
	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param fixtureWaitCheckId
	 * @return
	 */
	Integer deleteById(Long userId, Long fixtureWaitCheckId) throws BusinessException;
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param fixtureWaitCheckId
	 * @return
	 */
	Integer removeById(Long userId, Long fixtureWaitCheckId) throws BusinessException;
}
