package org.tsinghuatj.fixture.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.fixture.domain.FixtureRepair;

/**
 * @ClassName: IFixtureRepairService
 * @Description: FixtureRepair服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IFixtureRepairService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param fixtureRepair
	 * @return
	 */
	Integer insert(Long userId, FixtureRepair fixtureRepair) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param fixtureRepair
	 * @param fixtureRepairId
	 * @return
	 */
	Integer updateActiveById(Long userId, FixtureRepair fixtureRepair, Long fixtureRepairId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param fixtureRepair
	 * @return
	 */
	List<FixtureRepair> select(Long userId, FixtureRepair fixtureRepair) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param fixtureRepair
	 * @return
	 */
	Pagination<FixtureRepair> selectPageList(Long userId, FixtureRepair fixtureRepair,QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param fixtureRepairId
	 * @return
	 */
	FixtureRepair selectById(Long userId, Long fixtureRepairId) throws BusinessException;

	
	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param fixtureRepairId
	 * @return
	 */
	Integer deleteById(Long userId, Long fixtureRepairId) throws BusinessException;
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param fixtureRepairId
	 * @return
	 */
	Integer removeById(Long userId, Long fixtureRepairId) throws BusinessException;
}
