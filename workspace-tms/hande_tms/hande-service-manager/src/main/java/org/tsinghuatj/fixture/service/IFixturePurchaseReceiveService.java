package org.tsinghuatj.fixture.service;

import org.tsinghuatj.fixture.domain.FixturePurchaseApply;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;

/**
 * @ClassName: IFixturePurchaseApplyService
 * @Description: FixturePurchaseApply服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IFixturePurchaseReceiveService {
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param fixturePurchaseApply
	 * @return
	 */
	Pagination<FixturePurchaseApply> selectPageList(Long userId, FixturePurchaseApply fixturePurchaseApply, QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param fixturePurchaseApplyId
	 * @return
	 */
	FixturePurchaseApply selectById(Long userId, Long fixturePurchaseApplyId) throws BusinessException;
	
}
