package org.tsinghuatj.measure.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.measure.domain.MeasurePurchaseReceipt;

/**
 * @ClassName: IMeasurePurchaseReceiptService
 * @Description: MeasurePurchaseReceipt服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IMeasurePurchaseReceiptService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param measurePurchaseReceipt
	 * @return
	 */
	Integer insert(Long userId, MeasurePurchaseReceipt measurePurchaseReceipt) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param measurePurchaseReceipt
	 * @param measurePurchaseReceiptId
	 * @return
	 */
	Integer updateActiveById(Long userId, MeasurePurchaseReceipt measurePurchaseReceipt, Long measurePurchaseReceiptId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param measurePurchaseReceipt
	 * @return
	 */
	List<MeasurePurchaseReceipt> select(Long userId, MeasurePurchaseReceipt measurePurchaseReceipt) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param measurePurchaseReceipt
	 * @return
	 */
	Pagination<MeasurePurchaseReceipt> selectPageList(Long userId, MeasurePurchaseReceipt measurePurchaseReceipt,QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param measurePurchaseReceiptId
	 * @return
	 */
	MeasurePurchaseReceipt selectById(Long userId, Long measurePurchaseReceiptId) throws BusinessException;

	
	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param measurePurchaseReceiptId
	 * @return
	 */
	Integer deleteById(Long userId, Long measurePurchaseReceiptId) throws BusinessException;
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param measurePurchaseReceiptId
	 * @return
	 */
	Integer removeById(Long userId, Long measurePurchaseReceiptId) throws BusinessException;
}
