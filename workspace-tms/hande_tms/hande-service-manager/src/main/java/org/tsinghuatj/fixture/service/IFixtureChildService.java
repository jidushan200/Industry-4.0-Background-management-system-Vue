package org.tsinghuatj.fixture.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.fixture.domain.FixtureChild;

/**
 * @ClassName: IFixtureChildService
 * @Description: FixtureChild服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IFixtureChildService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param fixtureChild
	 * @return
	 */
	Integer insert(Long userId, FixtureChild fixtureChild) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param fixtureChild
	 * @param fixtureChildId
	 * @return
	 */
	Integer updateActiveById(Long userId, FixtureChild fixtureChild, Long fixtureChildId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param fixtureChild
	 * @return
	 */
	List<FixtureChild> select(Long userId, FixtureChild fixtureChild) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param fixtureChild
	 * @return
	 */
	Pagination<FixtureChild> selectPageList(Long userId, FixtureChild fixtureChild,QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param fixtureChildId
	 * @return
	 */
	FixtureChild selectById(Long userId, Long fixtureChildId) throws BusinessException;

	
	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param fixtureChildId
	 * @return
	 */
	Integer deleteById(Long userId, Long fixtureChildId) throws BusinessException;
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param fixtureChildId
	 * @return
	 */
	Integer removeById(Long userId, Long fixtureChildId) throws BusinessException;
}
