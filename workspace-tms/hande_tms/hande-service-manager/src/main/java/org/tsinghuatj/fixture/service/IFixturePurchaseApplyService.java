package org.tsinghuatj.fixture.service;

import java.math.BigDecimal;
import java.util.List;

import org.tsinghuatj.fixture.domain.FixturePurchaseApply;
import org.tsinghuatj.fixture.domain.FixturePurchaseReceipt;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;

/**
 * @ClassName: IFixturePurchaseApplyService
 * @Description: FixturePurchaseApply服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IFixturePurchaseApplyService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param fixturePurchaseApply
	 * @return
	 */
	Integer insert(Long userId, FixturePurchaseApply fixturePurchaseApply) throws BusinessException;

	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param fixturePurchaseApply
	 * @param fixturePurchaseApplyId
	 * @return
	 */
	Integer updateActiveById(Long userId, FixturePurchaseApply fixturePurchaseApply, Long fixturePurchaseApplyId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param fixturePurchaseApply
	 * @return
	 */
	List<FixturePurchaseApply> select(Long userId, FixturePurchaseApply fixturePurchaseApply) throws BusinessException;

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
	
	/**
	 * 根据主键查询(收货)
	 * 
	 * @param userId
	 * @param fixturePurchaseApplyId
	 * @return
	 */
	FixturePurchaseApply selectByPkId(Long userId, Long pkId) throws BusinessException;

	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param fixturePurchaseApplyId
	 * @return
	 */
	Integer deleteById(Long userId, Long fixturePurchaseApplyId) throws BusinessException;

	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param fixturePurchaseApplyId
	 * @return
	 */
	Integer removeById(Long userId, Long fixturePurchaseApplyId) throws BusinessException;

	/**
	 * 审核
	 * 
	 * @param userId
	 * @param pkId
	 * @return
	 */
	Integer auditPurchaseApply(Long userId, String realName, Long pkId, Integer auditStatus, BigDecimal amount,String remark) throws BusinessException;
	
	/**
	 * 更新采购到货
	 * 
	 * @param userId
	 * @param toolPurchaseApply
	 * @param toolPurchaseApplyId
	 * @return
	 */
	Integer updatePurchaseReceipt(Long userId,Long pkId,List<FixturePurchaseReceipt> receiptList) throws BusinessException;
}
