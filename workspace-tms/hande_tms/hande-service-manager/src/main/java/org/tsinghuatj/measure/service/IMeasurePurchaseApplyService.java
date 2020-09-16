package org.tsinghuatj.measure.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.measure.domain.MeasureBase;
import org.tsinghuatj.measure.domain.MeasurePurchaseApply;
import org.tsinghuatj.measure.domain.MeasurePurchaseReceipt;
import org.tsinghuatj.tool.domain.ToolApplyAudit;

/**
 * @ClassName: IMeasurePurchaseApplyService
 * @Description: MeasurePurchaseApply服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IMeasurePurchaseApplyService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param measurePurchaseApply
	 * @return
	 */
	Integer insert(Long userId, MeasurePurchaseApply measurePurchaseApply) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param measurePurchaseApply
	 * @param measurePurchaseApplyId
	 * @return
	 */
	Integer updateActiveById(Long userId, MeasurePurchaseApply measurePurchaseApply, Long measurePurchaseApplyId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param measurePurchaseApply
	 * @return
	 */
	List<MeasurePurchaseApply> select(Long userId, MeasurePurchaseApply measurePurchaseApply) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param measurePurchaseApply
	 * @return
	 */
	Pagination<MeasurePurchaseApply> selectPageList(Long userId, MeasurePurchaseApply measurePurchaseApply,QueryDto queryDto) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param toolPurchaseApply
	 * @return
	 */
	Pagination<MeasurePurchaseApply> selectAuditedPageList(Long userId,MeasurePurchaseApply measurePurchaseApply,QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param measurePurchaseApplyId
	 * @return
	 */
	MeasurePurchaseApply selectById(Long userId, Long measurePurchaseApplyId) throws BusinessException;

	
	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param measurePurchaseApplyId
	 * @return
	 */
	Integer deleteById(Long userId, Long measurePurchaseApplyId) throws BusinessException;
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param measurePurchaseApplyId
	 * @return
	 */
	Integer removeById(Long userId, Long measurePurchaseApplyId) throws BusinessException;
	
	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param toolNumber
	 * @return
	 */
	MeasureBase purchaseGetByMeasureNumber(Long userId,String measureNumber) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param toolPurchaseApply
	 * @param toolPurchaseApplyId
	 * @return
	 */
	Integer reportAudit(Long userId, String realName, ToolApplyAudit applyAudit, MeasurePurchaseApply measurePurchaseApply) throws BusinessException;
	
	/**
	 * 更新采购到货
	 * 
	 * @param userId
	 * @param toolPurchaseApply
	 * @param toolPurchaseApplyId
	 * @return
	 */
	Integer updatePurchaseReceipt(Long userId,Long pkId,Integer arrivaledQty, List<MeasurePurchaseReceipt> receiptList) throws BusinessException;
	
	/**
	 * 查询采购收货单列表
	 * 
	 * @param userId
	 * @param toolPurchaseReceipt
	 * @return
	 */
	List<MeasurePurchaseReceipt> selectByApplyId(Long userId, Long applyId) throws BusinessException;
}
