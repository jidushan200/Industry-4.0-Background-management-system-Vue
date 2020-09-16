package org.tsinghuatj.mould.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;

import org.tsinghuatj.mould.domain.MouldReceipt;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface MouldReceiptMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param mouldReceipt
	 * @return
	 */
	Integer insert(@Param("mouldReceipt") MouldReceipt mouldReceipt);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param mouldReceipt
	 * @param mouldReceiptId
	 * @return
	 */
	Integer updateActiveById(@Param("mouldReceipt") MouldReceipt mouldReceipt, @Param("mouldReceiptId") Long mouldReceiptId);

	/**
	 * 查询列表
	 * 
	 * @param mouldReceipt
	 * @return
	 */
	List<MouldReceipt> select(@Param("mouldReceipt") MouldReceipt mouldReceipt);
	
	/**
	 * 分页查询列表
	 * 
	 * @param mouldReceipt
	 * @return
	 */
	Page<MouldReceipt> selectPageList(@Param("mouldReceipt") MouldReceipt mouldReceipt,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param mouldReceiptId
	 * @return
	 */
	MouldReceipt selectById(@Param("mouldReceiptId") Long mouldReceiptId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param mouldReceiptId
	 * @return
	 */
	Integer deleteById(@Param("mouldReceipt") MouldReceipt mouldReceipt);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param mouldReceiptId
	 * @return
	 */
	Integer removeById(@Param("mouldReceiptId") Long mouldReceiptId);
	
	/**
	 * 根据主键查询
	 * 
	 * @param mouldReceiptId
	 * @return
	 */
	MouldReceipt selectByMouldNumber(@Param("mouldNumber") String mouldNumber);
	
}
