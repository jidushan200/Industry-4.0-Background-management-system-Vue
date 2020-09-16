package org.tsinghuatj.tool.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.tool.domain.ToolApplyAudit;
import org.tsinghuatj.tool.domain.ToolPurchaseApply;
import org.tsinghuatj.tool.domain.ToolPurchaseReceipt;

/**
 * @ClassName: IToolPurchaseApplyService
 * @Description: ToolPurchaseApply服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IToolPurchaseApplyService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param toolPurchaseApply
	 * @return
	 */
	Integer insert(Long userId, ToolPurchaseApply toolPurchaseApply) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param toolPurchaseApply
	 * @param toolPurchaseApplyId
	 * @return
	 */
	Integer updateActiveById(Long userId, ToolPurchaseApply toolPurchaseApply, Long toolPurchaseApplyId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param toolPurchaseApply
	 * @return
	 */
	List<ToolPurchaseApply> select(Long userId, ToolPurchaseApply toolPurchaseApply) throws BusinessException;
	
	
	Pagination <ToolPurchaseApply> selectPageList(Long userId, ToolPurchaseApply toolPurchaseApply, QueryDto queryDto) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param toolPurchaseApply
	 * @return
	 */
	Pagination<ToolPurchaseApply> selectPurchaseReceiptPageList(Long userId, ToolPurchaseApply toolPurchaseApply,QueryDto queryDto) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param toolPurchaseApply
	 * @return
	 */
	Pagination<ToolPurchaseApply> selectAuditedPageList(Long userId, ToolPurchaseApply toolPurchaseApply,QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param toolPurchaseApplyId
	 * @return
	 */
	ToolPurchaseApply selectById(Long userId, Long toolPurchaseApplyId) throws BusinessException;

	
	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param toolPurchaseApplyId
	 * @return
	 */
	Integer deleteById(Long userId, Long toolPurchaseApplyId) throws BusinessException;
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param toolPurchaseApplyId
	 * @return
	 */
	Integer removeById(Long userId, Long toolPurchaseApplyId) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param toolPurchaseApply
	 * @param toolPurchaseApplyId
	 * @return
	 */
	Integer reportAudit(Long userId, String realName, ToolApplyAudit applyAudit, ToolPurchaseApply toolPurchaseApply) throws BusinessException;
	
	/**
	 * 查询采购收货单列表
	 * 
	 * @param userId
	 * @param toolPurchaseReceipt
	 * @return
	 */
	List<ToolPurchaseReceipt> selectByApplyId(Long userId, Long applyId) throws BusinessException;
	
	/**
	 * 更新采购到货
	 * 
	 * @param userId
	 * @param toolPurchaseApply
	 * @param toolPurchaseApplyId
	 * @return
	 */
	Integer updatePurchaseReceipt(Long userId,Long pkId,Integer arrivaledQty, List<ToolPurchaseReceipt> receiptList) throws BusinessException;
	
}
