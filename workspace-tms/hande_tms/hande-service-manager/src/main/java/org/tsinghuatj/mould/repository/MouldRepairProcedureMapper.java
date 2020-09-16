package org.tsinghuatj.mould.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;

import org.tsinghuatj.mould.domain.MouldRepairProcedure;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface MouldRepairProcedureMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param mouldRepairProcedure
	 * @return
	 */
	Integer insert(@Param("mouldRepairProcedure") MouldRepairProcedure mouldRepairProcedure);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param mouldRepairProcedure
	 * @param mouldRepairProcedureId
	 * @return
	 */
	Integer updateActiveById(@Param("mouldRepairProcedure") MouldRepairProcedure mouldRepairProcedure, @Param("mouldRepairProcedureId") Long mouldRepairProcedureId);

	/**
	 * 查询列表
	 * 
	 * @param mouldRepairProcedure
	 * @return
	 */
	List<MouldRepairProcedure> select(@Param("mouldRepairProcedure") MouldRepairProcedure mouldRepairProcedure);
	
	/**
	 * 分页查询列表
	 * 
	 * @param mouldRepairProcedure
	 * @return
	 */
	Page<MouldRepairProcedure> selectPageList(@Param("mouldRepairProcedure") MouldRepairProcedure mouldRepairProcedure,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param mouldRepairProcedureId
	 * @return
	 */
	MouldRepairProcedure selectById(@Param("mouldRepairProcedureId") Long mouldRepairProcedureId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param mouldRepairProcedureId
	 * @return
	 */
	Integer deleteById(@Param("mouldRepairProcedure") MouldRepairProcedure mouldRepairProcedure);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param mouldRepairProcedureId
	 * @return
	 */
	Integer removeById(@Param("mouldRepairProcedureId") Long mouldRepairProcedureId);
}
