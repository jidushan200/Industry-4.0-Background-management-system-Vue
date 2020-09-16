package org.tsinghuatj.tool.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;

import org.tsinghuatj.tool.domain.ToolPlate;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface ToolPlateMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param toolPlate
	 * @return
	 */
	Integer insert(@Param("toolPlate") ToolPlate toolPlate);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param toolPlate
	 * @param toolPlateId
	 * @return
	 */
	Integer updateActiveById(@Param("toolPlate") ToolPlate toolPlate, @Param("toolPlateId") Long toolPlateId);
	
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param toolPlate
	 * @param toolPlateId
	 * @return
	 */
	Integer updateActiveByNumber(@Param("toolPlate") ToolPlate toolPlate, @Param("plateNumber") String plateNumber);
	
	
	/**
	 * 查询列表
	 * 
	 * @param toolPlate
	 * @return
	 */
	List<ToolPlate> select(@Param("toolPlate") ToolPlate toolPlate);
	
	/**
	 * 分页查询列表
	 * 
	 * @param toolPlate
	 * @return
	 */
	Page<ToolPlate> selectPageList(@Param("toolPlate") ToolPlate toolPlate,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param toolPlateId
	 * @return
	 */
	ToolPlate selectById(@Param("toolPlateId") Long toolPlateId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param toolPlateId
	 * @return
	 */
	Integer deleteById(@Param("toolPlate") ToolPlate toolPlate);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param toolPlateId
	 * @return
	 */
	Integer removeById(@Param("toolPlateId") Long toolPlateId);
	
	/**
	 * 根据主键查询
	 * 
	 * @param toolPlateId
	 * @return
	 */
	ToolPlate selectByPlateNumber(@Param("plateNumber") String plateNumber);
	
}
