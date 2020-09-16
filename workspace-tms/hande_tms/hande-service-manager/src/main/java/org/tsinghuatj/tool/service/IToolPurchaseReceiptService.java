package org.tsinghuatj.tool.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.tool.domain.ToolPurchaseReceipt;

/**
 * @ClassName: IToolPurchaseReceiptService
 * @Description: ToolPurchaseReceipt服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IToolPurchaseReceiptService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param toolPurchaseReceipt
	 * @return
	 */
	Integer insert(Long userId, ToolPurchaseReceipt toolPurchaseReceipt) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param toolPurchaseReceipt
	 * @param toolPurchaseReceiptId
	 * @return
	 */
	Integer updateActiveById(Long userId, ToolPurchaseReceipt toolPurchaseReceipt, Long toolPurchaseReceiptId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param toolPurchaseReceipt
	 * @return
	 */
	List<ToolPurchaseReceipt> select(Long userId, ToolPurchaseReceipt toolPurchaseReceipt) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param toolPurchaseReceipt
	 * @return
	 */
	Pagination<ToolPurchaseReceipt> selectPageList(Long userId, ToolPurchaseReceipt toolPurchaseReceipt,QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param toolPurchaseReceiptId
	 * @return
	 */
	ToolPurchaseReceipt selectById(Long userId, Long toolPurchaseReceiptId) throws BusinessException;

	
	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param toolPurchaseReceiptId
	 * @return
	 */
	Integer deleteById(Long userId, Long toolPurchaseReceiptId) throws BusinessException;
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param toolPurchaseReceiptId
	 * @return
	 */
	Integer removeById(Long userId, Long toolPurchaseReceiptId) throws BusinessException;
}
