package org.tsinghuatj.fixture.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.fixture.domain.FixtureMaintain;

/**
 * @ClassName: IFixtureMaintainService
 * @Description: FixtureMaintain服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IFixtureMaintainService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param fixtureMaintain
	 * @return
	 */
	Integer insert(Long userId, String useName,FixtureMaintain fixtureMaintain) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param fixtureMaintain
	 * @param fixtureMaintainId
	 * @return
	 */
	Integer updateActiveById(Long userId, FixtureMaintain fixtureMaintain, Long fixtureMaintainId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param fixtureMaintain
	 * @return
	 */
	List<FixtureMaintain> select(Long userId, FixtureMaintain fixtureMaintain) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param fixtureMaintain
	 * @return
	 */
	Pagination<FixtureMaintain> selectPageList(Long userId, FixtureMaintain fixtureMaintain,QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param fixtureMaintainId
	 * @return
	 */
	FixtureMaintain selectById(Long userId, Long fixtureMaintainId) throws BusinessException;

	
	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param fixtureMaintainId
	 * @return
	 */
	Integer deleteById(Long userId, Long fixtureMaintainId) throws BusinessException;
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param fixtureMaintainId
	 * @return
	 */
	Integer removeById(Long userId, Long fixtureMaintainId) throws BusinessException;
}
