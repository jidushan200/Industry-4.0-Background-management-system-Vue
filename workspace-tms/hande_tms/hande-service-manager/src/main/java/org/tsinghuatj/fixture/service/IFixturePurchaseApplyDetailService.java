package org.tsinghuatj.fixture.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.fixture.domain.FixturePurchaseApplyDetail;

/**
 * @ClassName: IFixturePurchaseApplyDetailService
 * @Description: FixturePurchaseApplyDetail服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IFixturePurchaseApplyDetailService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param fixturePurchaseApplyDetail
	 * @return
	 */
	Integer insert(Long userId, FixturePurchaseApplyDetail fixturePurchaseApplyDetail) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param fixturePurchaseApplyDetail
	 * @param fixturePurchaseApplyDetailId
	 * @return
	 */
	Integer updateActiveById(Long userId, FixturePurchaseApplyDetail fixturePurchaseApplyDetail, Long fixturePurchaseApplyDetailId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param fixturePurchaseApplyDetail
	 * @return
	 */
	List<FixturePurchaseApplyDetail> select(Long userId, FixturePurchaseApplyDetail fixturePurchaseApplyDetail) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param fixturePurchaseApplyDetail
	 * @return
	 */
	Pagination<FixturePurchaseApplyDetail> selectPageList(Long userId, FixturePurchaseApplyDetail fixturePurchaseApplyDetail,QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param fixturePurchaseApplyDetailId
	 * @return
	 */
	FixturePurchaseApplyDetail selectById(Long userId, Long fixturePurchaseApplyDetailId) throws BusinessException;

	
	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param fixturePurchaseApplyDetailId
	 * @return
	 */
	Integer deleteById(Long userId, Long fixturePurchaseApplyDetailId) throws BusinessException;
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param fixturePurchaseApplyDetailId
	 * @return
	 */
	Integer removeById(Long userId, Long fixturePurchaseApplyDetailId) throws BusinessException;
}
