package org.tsinghuatj.tool.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.tool.domain.ToolPurchaseReceipt;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface ToolPurchaseReceiptMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param toolPurchaseReceipt
	 * @return
	 */
	Integer insert(@Param("toolPurchaseReceipt") ToolPurchaseReceipt toolPurchaseReceipt);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param toolPurchaseReceipt
	 * @param toolPurchaseReceiptId
	 * @return
	 */
	Integer updateActiveById(@Param("toolPurchaseReceipt") ToolPurchaseReceipt toolPurchaseReceipt, @Param("toolPurchaseReceiptId") Long toolPurchaseReceiptId);

	/**
	 * 查询列表
	 * 
	 * @param toolPurchaseReceipt
	 * @return
	 */
	List<ToolPurchaseReceipt> selectByApplyId(@Param("applyId") Long applyId);
	
	/**
	 * 分页查询列表
	 * 
	 * @param toolPurchaseReceipt
	 * @return
	 */
	Page<ToolPurchaseReceipt> selectPageList(@Param("toolPurchaseReceipt") ToolPurchaseReceipt toolPurchaseReceipt,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param toolPurchaseReceiptId
	 * @return
	 */
	ToolPurchaseReceipt selectById(@Param("toolPurchaseReceiptId") Long toolPurchaseReceiptId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param toolPurchaseReceiptId
	 * @return
	 */
	Integer deleteById(@Param("toolPurchaseReceipt") ToolPurchaseReceipt toolPurchaseReceipt);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param toolPurchaseReceiptId
	 * @return
	 */
	Integer removeById(@Param("toolPurchaseReceiptId") Long toolPurchaseReceiptId);
	
	/**
	 * 查询列表
	 * 
	 * @param toolPurchaseReceipt
	 * @return
	 */
	int selectCountByApplyId(@Param("applyId") Long applyId);
	
	
	Page<ToolPurchaseReceipt> selectPurchaseReceiptPageList(@Param("toolPurchaseReceipt") ToolPurchaseReceipt toolPurchaseReceipt,@Param("queryDto") QueryDto queryDto);
	
	/**
	 * 查询收货明细列表
	 * 
	 * @param toolPurchaseReceipt
	 * @return
	 */
	List<ToolPurchaseReceipt> selectByApplyIdList(@Param("applyIdList") List<Long> applyIdList);
	
}
