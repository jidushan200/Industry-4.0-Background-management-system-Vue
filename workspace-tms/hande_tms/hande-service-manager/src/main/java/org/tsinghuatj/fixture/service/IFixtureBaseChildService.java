package org.tsinghuatj.fixture.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.fixture.domain.FixtureBaseChild;

/**
 * @ClassName: IFixtureBaseChildService
 * @Description: FixtureBaseChild服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IFixtureBaseChildService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param fixtureBaseChild
	 * @return
	 */
	Integer insert(Long userId, FixtureBaseChild fixtureBaseChild) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param fixtureBaseChild
	 * @param fixtureBaseChildId
	 * @return
	 */
	Integer updateActiveById(Long userId, FixtureBaseChild fixtureBaseChild, Long fixtureBaseChildId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param fixtureBaseChild
	 * @return
	 */
	List<FixtureBaseChild> select(Long userId, FixtureBaseChild fixtureBaseChild) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param fixtureBaseChild
	 * @return
	 */
	Pagination<FixtureBaseChild> selectPageList(Long userId, FixtureBaseChild fixtureBaseChild,QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param fixtureBaseChildId
	 * @return
	 */
	FixtureBaseChild selectById(Long userId, Long fixtureBaseChildId) throws BusinessException;

	
	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param fixtureBaseChildId
	 * @return
	 */
	Integer deleteById(Long userId, Long fixtureBaseChildId) throws BusinessException;
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param fixtureBaseChildId
	 * @return
	 */
	Integer removeById(Long userId, Long fixtureBaseChildId) throws BusinessException;
}
