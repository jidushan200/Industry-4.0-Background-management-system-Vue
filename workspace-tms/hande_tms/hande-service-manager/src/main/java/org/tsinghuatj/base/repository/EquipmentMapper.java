package org.tsinghuatj.base.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;

import org.tsinghuatj.base.domain.Equipment;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface EquipmentMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param equipment
	 * @return
	 */
	Integer insert(@Param("equipment") Equipment equipment);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param equipment
	 * @param equipmentId
	 * @return
	 */
	Integer updateActiveById(@Param("equipment") Equipment equipment, @Param("equipmentId") Long equipmentId);

	/**
	 * 查询列表
	 * 
	 * @param equipment
	 * @return
	 */
	List<Equipment> select(@Param("equipment") Equipment equipment);
	
	/**
	 * 分页查询列表
	 * 
	 * @param equipment
	 * @return
	 */
	Page<Equipment> selectPageList(@Param("equipment") Equipment equipment,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param equipmentId
	 * @return
	 */
	Equipment selectById(@Param("equipmentId") Long equipmentId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param equipmentId
	 * @return
	 */
	Integer deleteById(@Param("equipment") Equipment equipment);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param equipmentId
	 * @return
	 */
	Integer removeById(@Param("equipmentId") Long equipmentId);
	
	/**
	 * 根据主键查询
	 * 
	 * @param equipmentId
	 * @return
	 */
	Equipment selectByEquipmentCode(@Param("equipmentCode") String equipmentCode,@Param("pkId") Long pkId);
}
