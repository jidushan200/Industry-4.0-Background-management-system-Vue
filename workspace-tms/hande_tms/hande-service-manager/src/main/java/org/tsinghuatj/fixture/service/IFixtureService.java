package org.tsinghuatj.fixture.service;

import java.util.List;

import org.tsinghuatj.fixture.domain.Fixture;
import org.tsinghuatj.fixture.domain.FixtureChild;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;

/**
 * @ClassName: IFixtureService
 * @Description: Fixture服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IFixtureService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param fixture
	 * @return
	 */
	Integer insert(Long userId, Long checkId, Fixture fixture) throws BusinessException;

	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param fixture
	 * @param fixtureId
	 * @return
	 */
	Integer updateActiveById(Long userId, Fixture fixture, Long fixtureId) throws BusinessException;

	/**
	 * 领用
	 * 
	 * @param userId
	 * @param fixture
	 * @param fixtureId
	 * @return
	 */
	Integer use(Long userId, Fixture fixture, String remark) throws BusinessException;
	
	/**
	 * 返库
	 * 
	 * @param userId
	 * @param fixture
	 * @param fixtureId
	 * @return
	 */
	Integer returnWarehouse(Long userId, Fixture fixture, String remark) throws BusinessException;
	/**
	 * 修磨质检合格返库
	 * 
	 * @param userId
	 * @param fixture
	 * @param fixtureId
	 * @return
	 */
	Integer goBack(Long userId, Fixture fixture,Long checkId) throws BusinessException;
	/**
	 * 送修磨
	 * 
	 * @param userId
	 * @param fixture
	 * @param fixtureId
	 * @return
	 */
	Integer setRepair(Long userId, Fixture fixture,Long checkId) throws BusinessException;
	
	/**
	 * 替换
	 * 
	 * @param userId
	 * @param fixture
	 * @param fixtureId
	 * @return
	 */
	Integer replace(Long userId, String parentBarcode,String fixtrueBarcode,String oldParentBarcode,String oldBarcode,String oldNumber) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param fixture
	 * @return
	 */
	List<Fixture> select(Long userId, Fixture fixture) throws BusinessException;

	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param fixture
	 * @return
	 */
	Pagination<Fixture> selectPageList(Long userId, Fixture fixture, QueryDto queryDto) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param fixture
	 * @return
	 */
	Pagination<Fixture> selectComposePageList(Long userId, Fixture fixture, QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param fixtureId
	 * @return
	 */
	Fixture selectById(Long userId, Long fixtureId) throws BusinessException;

	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param fixtureId
	 * @return
	 */
	Integer deleteById(Long userId, Long fixtureId) throws BusinessException;

	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param fixtureId
	 * @return
	 */
	Integer removeById(Long userId, Long fixtureId) throws BusinessException;

	/**
	 * 根据编号查询
	 * 
	 * @param userId
	 * @param toolNumber
	 * @return
	 */
	Fixture selectByFullNumber(Long userId, String fullNumber) throws BusinessException;

	/**
	 * 夹具台账导入
	 * 
	 * @param userId
	 * @param fixture
	 * @return
	 */
	Integer importFixture(Long userId, List<Fixture> fixtureList) throws BusinessException;
	
	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param fixture
	 * @return
	 */
	List<FixtureChild> selectPartList(Long userId, Fixture fixture) throws BusinessException;
}
