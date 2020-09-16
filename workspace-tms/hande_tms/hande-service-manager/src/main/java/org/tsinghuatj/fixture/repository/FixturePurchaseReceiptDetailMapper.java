package org.tsinghuatj.fixture.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.fixture.domain.FixturePurchaseReceiptDetail;
import org.tsinghuatj.framework.domain.QueryDto;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface FixturePurchaseReceiptDetailMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param fixturePurchaseReceiptDetail
	 * @return
	 */
	Integer insert(@Param("fixturePurchaseReceiptDetail") FixturePurchaseReceiptDetail fixturePurchaseReceiptDetail);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param fixturePurchaseReceiptDetail
	 * @param fixturePurchaseReceiptDetailId
	 * @return
	 */
	Integer updateActiveById(@Param("fixturePurchaseReceiptDetail") FixturePurchaseReceiptDetail fixturePurchaseReceiptDetail, @Param("fixturePurchaseReceiptDetailId") Long fixturePurchaseReceiptDetailId);

	/**
	 * 查询列表
	 * 
	 * @param fixturePurchaseReceiptDetail
	 * @return
	 */
	List<FixturePurchaseReceiptDetail> select(@Param("fixturePurchaseReceiptDetail") FixturePurchaseReceiptDetail fixturePurchaseReceiptDetail);
	
	/**
	 * 分页查询列表
	 * 
	 * @param fixturePurchaseReceiptDetail
	 * @return
	 */
	Page<FixturePurchaseReceiptDetail> selectPageList(@Param("fixturePurchaseReceiptDetail") FixturePurchaseReceiptDetail fixturePurchaseReceiptDetail,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param fixturePurchaseReceiptDetailId
	 * @return
	 */
	FixturePurchaseReceiptDetail selectById(@Param("fixturePurchaseReceiptDetailId") Long fixturePurchaseReceiptDetailId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param fixturePurchaseReceiptDetailId
	 * @return
	 */
	Integer deleteById(@Param("fixturePurchaseReceiptDetail") FixturePurchaseReceiptDetail fixturePurchaseReceiptDetail);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param fixturePurchaseReceiptDetailId
	 * @return
	 */
	Integer removeById(@Param("fixturePurchaseReceiptDetailId") Long fixturePurchaseReceiptDetailId);
	
	/**
	 * 查询列表
	 * 
	 * @param fixturePurchaseReceipt
	 * @return
	 */
	List<FixturePurchaseReceiptDetail> selectByReceiptId(@Param("receiptId") Long receiptId);
}
