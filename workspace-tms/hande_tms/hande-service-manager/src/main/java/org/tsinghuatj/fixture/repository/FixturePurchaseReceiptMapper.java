package org.tsinghuatj.fixture.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;

import org.tsinghuatj.fixture.domain.FixturePurchaseReceipt;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface FixturePurchaseReceiptMapper {

	/**
	 * 插入数据
	 * 
	 * @param fixturePurchaseReceipt
	 * @return
	 */
	Integer insert(@Param("fixturePurchaseReceipt") FixturePurchaseReceipt fixturePurchaseReceipt);

	/**
	 * 根据主键更新有值数据
	 * 
	 * @param fixturePurchaseReceipt
	 * @param fixturePurchaseReceiptId
	 * @return
	 */
	Integer updateActiveById(@Param("fixturePurchaseReceipt") FixturePurchaseReceipt fixturePurchaseReceipt, @Param("fixturePurchaseReceiptId") Long fixturePurchaseReceiptId);

	/**
	 * 查询列表
	 * 
	 * @param fixturePurchaseReceipt
	 * @return
	 */
	List<FixturePurchaseReceipt> select(@Param("fixturePurchaseReceipt") FixturePurchaseReceipt fixturePurchaseReceipt);

	/**
	 * 查询列表
	 * 
	 * @param fixturePurchaseReceipt
	 * @return
	 */
	List<FixturePurchaseReceipt> selectByApplyId(@Param("applyId") Long applyId);

	Integer selectCountByApplyId(@Param("applyId") Long applyId);

	/**
	 * 分页查询列表
	 * 
	 * @param fixturePurchaseReceipt
	 * @return
	 */
	Page<FixturePurchaseReceipt> selectPageList(@Param("fixturePurchaseReceipt") FixturePurchaseReceipt fixturePurchaseReceipt, @Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param fixturePurchaseReceiptId
	 * @return
	 */
	FixturePurchaseReceipt selectById(@Param("fixturePurchaseReceiptId") Long fixturePurchaseReceiptId);

	/**
	 * 按主键物理删除
	 * 
	 * @param fixturePurchaseReceiptId
	 * @return
	 */
	Integer deleteById(@Param("fixturePurchaseReceipt") FixturePurchaseReceipt fixturePurchaseReceipt);

	/**
	 * 按主键物理删除
	 * 
	 * @param fixturePurchaseReceiptId
	 * @return
	 */
	Integer removeById(@Param("fixturePurchaseReceiptId") Long fixturePurchaseReceiptId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param fixturePurchaseReceiptId
	 * @return
	 */
	Integer deleteDetailByReceiptId(@Param("receiptId") Long receiptId);
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param fixturePurchaseReceipt
	 * @param fixturePurchaseReceiptId
	 * @return
	 */
	Integer updateByWaitCheckId(@Param("checkStatus") Integer checkStatus, @Param("checkResult") Integer checkResult,@Param("waitCheckId") Long waitCheckId);

}
