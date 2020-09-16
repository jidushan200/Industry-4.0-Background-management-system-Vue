package org.tsinghuatj.fixture.service;

import java.util.List;

import org.tsinghuatj.fixture.domain.FixtureBase;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;

/**
 * @ClassName: IFixtureBaseService
 * @Description: FixtureBase服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IFixtureBaseService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param fixtureBase
	 * @return
	 */
	Integer insert(Long userId, FixtureBase fixtureBase) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param fixtureBase
	 * @param fixtureBaseId
	 * @return
	 */
	Integer updateActiveById(Long userId, FixtureBase fixtureBase, Long fixtureBaseId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param fixtureBase
	 * @return
	 */
	List<FixtureBase> select(Long userId, FixtureBase fixtureBase) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param fixtureBase
	 * @return
	 */
	Pagination<FixtureBase> selectPageList(Long userId, FixtureBase fixtureBase,QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param fixtureBaseId
	 * @return
	 */
	FixtureBase selectById(Long userId, Long fixtureBaseId,Integer postType) throws BusinessException;

	
	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param fixtureBaseId
	 * @return
	 */
	Integer deleteById(Long userId, Long fixtureBaseId,String fixtureNumber) throws BusinessException;
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param fixtureBaseId
	 * @return
	 */
	Integer removeById(Long userId, Long fixtureBaseId) throws BusinessException;
	 /**
     * 按编码查找
     * @param userId
     * @param fixtureNumber
     * @return
     * @throws BusinessException
     */
	FixtureBase selectByNumber(Long userId, String fixtureNumber)throws BusinessException;

	/**
	 * 刀具信息同步
	 * @param userId
	 * @return
	 * @throws BusinessException
	 */
	Integer fixtureBaseSynchro(Long userId,String fixtureNumber)throws BusinessException;
	
	/**
	 * 刀具基础信息表导入
	 */
	Integer fixtureBaseImport(Long userId, List<FixtureBase> fixtureBaseList) throws BusinessException;
	
	
	FixtureBase purchaseGetByFixtureNumber(Long userId,String fixtureNumber)throws BusinessException;
}
