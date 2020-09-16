package org.tsinghuatj.fixture.service;

import java.util.List;

import org.tsinghuatj.fixture.domain.FixturePart;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;

/**
 * @ClassName: IFixturePartService
 * @Description: FixturePart服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IFixturePartService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param fixturePart
	 * @return
	 */
	Integer insert(Long userId, FixturePart fixturePart) throws BusinessException;
	
	/**
	 * 导入数据
	 * 
	 * @param userId
	 * @param fixturePartList
	 * @return
	 */
	Integer importFixture(Long userId, List<FixturePart> fixturePartList) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param fixturePart
	 * @param fixturePartId
	 * @return
	 */
	Integer updateActiveById(Long userId, FixturePart fixturePart, Long fixturePartId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param fixturePart
	 * @return
	 */
	List<FixturePart> select(Long userId, FixturePart fixturePart) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param fixturePart
	 * @return
	 */
	Pagination<FixturePart> selectPageList(Long userId, FixturePart fixturePart,QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param fixturePartId
	 * @return
	 */
	FixturePart selectById(Long userId, Long fixturePartId) throws BusinessException;

	
	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param fixturePartId
	 * @return
	 */
	Integer deleteById(Long userId, Long fixturePartId) throws BusinessException;
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param fixturePartId
	 * @return
	 */
	Integer removeById(Long userId, Long fixturePartId) throws BusinessException;
	/**
	 * 检查
	 * @param pkId
	 * @param fixtuId
	 * @param partId
	 * @return
	 * @throws BusinessException
	 */
	boolean checkFixturePart(Long pkId, Long fixtureId, Long partId) throws BusinessException;
}
