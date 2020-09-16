package org.tsinghuatj.mould.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.mould.domain.MouldReceipt;

/**
 * @ClassName: IMouldReceiptService
 * @Description: MouldReceipt服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IMouldReceiptService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param mouldReceipt
	 * @return
	 */
	Integer insert(Long userId, MouldReceipt mouldReceipt) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param mouldReceipt
	 * @param mouldReceiptId
	 * @return
	 */
	Integer updateActiveById(Long userId, MouldReceipt mouldReceipt, Long mouldReceiptId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param mouldReceipt
	 * @return
	 */
	List<MouldReceipt> select(Long userId, MouldReceipt mouldReceipt) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param mouldReceipt
	 * @return
	 */
	Pagination<MouldReceipt> selectPageList(Long userId, MouldReceipt mouldReceipt,QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param mouldReceiptId
	 * @return
	 */
	MouldReceipt selectById(Long userId, Long mouldReceiptId) throws BusinessException;

	
	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param mouldReceiptId
	 * @return
	 */
	Integer deleteById(Long userId, Long mouldReceiptId) throws BusinessException;
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param mouldReceiptId
	 * @return
	 */
	Integer removeById(Long userId, Long mouldReceiptId) throws BusinessException;
}
