package org.tsinghuatj.fixture.service;

import java.util.List;

import org.tsinghuatj.fixture.domain.FixtureCheck;
import org.tsinghuatj.fixture.domain.FixturePurchaseReceipt;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;

/**
 * @ClassName: IFixtureCheckService
 * @Description: FixtureCheck服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IFixtureCheckService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param fixtureCheck
	 * @return
	 */
	Integer insert(Long userId, FixtureCheck fixtureCheck,Long waitCheckId,String appendixIds) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param fixtureCheck
	 * @param fixtureCheckId
	 * @return
	 */
	Integer updateActiveById(Long userId, FixtureCheck fixtureCheck, Long fixtureCheckId,String appendixIds) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param fixtureCheck
	 * @return
	 */
	List<FixtureCheck> select(Long userId, FixtureCheck fixtureCheck) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param fixtureCheck
	 * @return
	 */
	Pagination<FixtureCheck> selectPageList(Long userId, FixtureCheck fixtureCheck,QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param fixtureCheckId
	 * @return
	 */
	FixtureCheck selectById(Long userId, Long fixtureCheckId) throws BusinessException;

	
	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param fixtureCheckId
	 * @return
	 */
	Integer deleteById(Long userId, Long fixtureCheckId,Integer checkType) throws BusinessException;
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param fixtureCheckId
	 * @return
	 */
	Integer removeById(Long userId, Long fixtureCheckId) throws BusinessException;
	
	/**
	 * 
	 * @param userId
	 * @param fixtureBase
	 * @param queryDto
	 * @return
	 */
	Pagination<FixturePurchaseReceipt> selectWaitCheckPageList(Long userId,FixturePurchaseReceipt receipt, QueryDto queryDto);
	
	
	/**
	 * 夹具领回
	 * 
	 * @param userId
	 * @param fixtureCheckId
	 * @return
	 */
	Integer fixtureTakeBack(Long userId, Long fixtureCheckId,Long fixtureId, String staffName) throws BusinessException;
}
