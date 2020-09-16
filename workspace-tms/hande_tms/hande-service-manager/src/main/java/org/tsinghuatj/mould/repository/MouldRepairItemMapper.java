package org.tsinghuatj.mould.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;

import org.tsinghuatj.mould.domain.MouldRepairItem;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface MouldRepairItemMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param mouldRepairItem
	 * @return
	 */
	Integer insert(@Param("mouldRepairItem") MouldRepairItem mouldRepairItem);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param mouldRepairItem
	 * @param mouldRepairItemId
	 * @return
	 */
	Integer updateActiveById(@Param("mouldRepairItem") MouldRepairItem mouldRepairItem, @Param("mouldRepairItemId") Long mouldRepairItemId);

	/**
	 * 查询列表
	 * 
	 * @param mouldRepairItem
	 * @return
	 */
	List<MouldRepairItem> select(@Param("mouldRepairItem") MouldRepairItem mouldRepairItem);
	
	/**
	 * 分页查询列表
	 * 
	 * @param mouldRepairItem
	 * @return
	 */
	Page<MouldRepairItem> selectPageList(@Param("mouldRepairItem") MouldRepairItem mouldRepairItem,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param mouldRepairItemId
	 * @return
	 */
	MouldRepairItem selectById(@Param("mouldRepairItemId") Long mouldRepairItemId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param mouldRepairItemId
	 * @return
	 */
	Integer deleteById(@Param("mouldRepairItem") MouldRepairItem mouldRepairItem);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param mouldRepairItemId
	 * @return
	 */
	Integer removeById(@Param("mouldRepairItemId") Long mouldRepairItemId);
	
	/**
	 * 查询列表
	 * 
	 * @param mouldRepairItem
	 * @return
	 */
	List<MouldRepairItem> selectByRepairId(@Param("repairId") Long repairId);
}
