package org.tsinghuatj.measure.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;

import org.tsinghuatj.measure.domain.MeasurePurchaseReceipt;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface MeasurePurchaseReceiptMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param measurePurchaseReceipt
	 * @return
	 */
	Integer insert(@Param("measurePurchaseReceipt") MeasurePurchaseReceipt measurePurchaseReceipt);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param measurePurchaseReceipt
	 * @param measurePurchaseReceiptId
	 * @return
	 */
	Integer updateActiveById(@Param("measurePurchaseReceipt") MeasurePurchaseReceipt measurePurchaseReceipt, @Param("measurePurchaseReceiptId") Long measurePurchaseReceiptId);

	/**
	 * 查询列表
	 * 
	 * @param measurePurchaseReceipt
	 * @return
	 */
	List<MeasurePurchaseReceipt> select(@Param("measurePurchaseReceipt") MeasurePurchaseReceipt measurePurchaseReceipt);
	
	/**
	 * 分页查询列表
	 * 
	 * @param measurePurchaseReceipt
	 * @return
	 */
	Page<MeasurePurchaseReceipt> selectPageList(@Param("measurePurchaseReceipt") MeasurePurchaseReceipt measurePurchaseReceipt,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param measurePurchaseReceiptId
	 * @return
	 */
	MeasurePurchaseReceipt selectById(@Param("measurePurchaseReceiptId") Long measurePurchaseReceiptId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param measurePurchaseReceiptId
	 * @return
	 */
	Integer deleteById(@Param("measurePurchaseReceipt") MeasurePurchaseReceipt measurePurchaseReceipt);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param measurePurchaseReceiptId
	 * @return
	 */
	Integer removeById(@Param("measurePurchaseReceiptId") Long measurePurchaseReceiptId);
	
	/**
	 * 查询列表
	 * 
	 * @param toolPurchaseReceipt
	 * @return
	 */
	List<MeasurePurchaseReceipt> selectByApplyId(@Param("applyId") Long applyId);
	
	/**
	 * 查询列表
	 * 
	 * @param toolPurchaseReceipt
	 * @return
	 */
	Integer selectCountByApplyId(@Param("applyId") Long applyId);
}
