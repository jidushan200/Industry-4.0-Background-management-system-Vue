package org.tsinghuatj.measure.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;

import org.tsinghuatj.measure.domain.MeasurePurchaseApply;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface MeasurePurchaseApplyMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param measurePurchaseApply
	 * @return
	 */
	Integer insert(@Param("measurePurchaseApply") MeasurePurchaseApply measurePurchaseApply);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param measurePurchaseApply
	 * @param measurePurchaseApplyId
	 * @return
	 */
	Integer updateActiveById(@Param("measurePurchaseApply") MeasurePurchaseApply measurePurchaseApply, @Param("measurePurchaseApplyId") Long measurePurchaseApplyId);

	/**
	 * 查询列表
	 * 
	 * @param measurePurchaseApply
	 * @return
	 */
	List<MeasurePurchaseApply> select(@Param("measurePurchaseApply") MeasurePurchaseApply measurePurchaseApply);
	
	/**
	 * 分页查询列表
	 * 
	 * @param measurePurchaseApply
	 * @return
	 */
	Page<MeasurePurchaseApply> selectPageList(@Param("measurePurchaseApply") MeasurePurchaseApply measurePurchaseApply,@Param("queryDto") QueryDto queryDto);
	
	/**
	 * 分页查询列表
	 * 
	 * @param toolPurchaseApply
	 * @return
	 */
	Page<MeasurePurchaseApply> selectAuditedPageList(@Param("measurePurchaseApply") MeasurePurchaseApply measurePurchaseApply,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param measurePurchaseApplyId
	 * @return
	 */
	MeasurePurchaseApply selectById(@Param("measurePurchaseApplyId") Long measurePurchaseApplyId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param measurePurchaseApplyId
	 * @return
	 */
	Integer deleteById(@Param("measurePurchaseApply") MeasurePurchaseApply measurePurchaseApply);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param measurePurchaseApplyId
	 * @return
	 */
	Integer removeById(@Param("measurePurchaseApplyId") Long measurePurchaseApplyId);
	
	/**
	 * 根据主键查询
	 * 
	 * @param measurePurchaseApplyId
	 * @return
	 */
	MeasurePurchaseApply selectByReceiptId(@Param("receiptId") Long receiptId);
}
