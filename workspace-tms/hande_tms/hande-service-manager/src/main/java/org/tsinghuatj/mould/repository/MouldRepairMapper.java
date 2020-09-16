package org.tsinghuatj.mould.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;

import org.tsinghuatj.mould.domain.MouldRepair;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface MouldRepairMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param mouldRepair
	 * @return
	 */
	Integer insert(@Param("mouldRepair") MouldRepair mouldRepair);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param mouldRepair
	 * @param mouldRepairId
	 * @return
	 */
	Integer updateActiveById(@Param("mouldRepair") MouldRepair mouldRepair, @Param("mouldRepairId") Long mouldRepairId);

	/**
	 * 查询列表
	 * 
	 * @param mouldRepair
	 * @return
	 */
	List<MouldRepair> select(@Param("mouldRepair") MouldRepair mouldRepair);
	
	/**
	 * 分页查询列表
	 * 
	 * @param mouldRepair
	 * @return
	 */
	Page<MouldRepair> selectPageList(@Param("mouldRepair") MouldRepair mouldRepair,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param mouldRepairId
	 * @return
	 */
	MouldRepair selectById(@Param("mouldRepairId") Long mouldRepairId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param mouldRepairId
	 * @return
	 */
	Integer deleteById(@Param("mouldRepair") MouldRepair mouldRepair);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param mouldRepairId
	 * @return
	 */
	Integer removeById(@Param("mouldRepairId") Long mouldRepairId);
	
	/**
	 * 根据主键查询
	 * 
	 * @param mouldRepairId
	 * @return
	 */
	MouldRepair selectSeqByMouldId(@Param("mouldId") Long mouldId);
}
