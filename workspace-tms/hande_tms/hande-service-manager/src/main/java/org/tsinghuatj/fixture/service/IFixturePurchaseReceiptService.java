package org.tsinghuatj.fixture.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.fixture.domain.FixturePurchaseReceipt;

/**
 * @ClassName: IFixturePurchaseReceiptService
 * @Description: FixturePurchaseReceipt服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IFixturePurchaseReceiptService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param fixturePurchaseReceipt
	 * @return
	 */
	Integer insert(Long userId, FixturePurchaseReceipt fixturePurchaseReceipt) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param fixturePurchaseReceipt
	 * @param fixturePurchaseReceiptId
	 * @return
	 */
	Integer updateActiveById(Long userId, FixturePurchaseReceipt fixturePurchaseReceipt, Long fixturePurchaseReceiptId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param fixturePurchaseReceipt
	 * @return
	 */
	List<FixturePurchaseReceipt> select(Long userId, FixturePurchaseReceipt fixturePurchaseReceipt) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param fixturePurchaseReceipt
	 * @return
	 */
	Pagination<FixturePurchaseReceipt> selectPageList(Long userId, FixturePurchaseReceipt fixturePurchaseReceipt,QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param fixturePurchaseReceiptId
	 * @return
	 */
	FixturePurchaseReceipt selectById(Long userId, Long fixturePurchaseReceiptId) throws BusinessException;

	
	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param fixturePurchaseReceiptId
	 * @return
	 */
	Integer deleteById(Long userId, Long fixturePurchaseReceiptId) throws BusinessException;
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param fixturePurchaseReceiptId
	 * @return
	 */
	Integer removeById(Long userId, Long fixturePurchaseReceiptId) throws BusinessException;
}
