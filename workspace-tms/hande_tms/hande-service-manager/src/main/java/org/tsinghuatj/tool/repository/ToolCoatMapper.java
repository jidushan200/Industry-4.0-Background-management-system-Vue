package org.tsinghuatj.tool.repository;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.tool.domain.ToolCoat;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface ToolCoatMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param toolCoat
	 * @return
	 */
	Integer insert(@Param("toolCoat") ToolCoat toolCoat);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param toolCoat
	 * @param toolCoatId
	 * @return
	 */
	Integer updateActiveById(@Param("toolCoat") ToolCoat toolCoat, @Param("toolCoatId") Long toolCoatId);

	/**
	 * 查询列表
	 * 
	 * @param toolCoat
	 * @return
	 */
	List<ToolCoat> select(@Param("toolCoat") ToolCoat toolCoat);
	
	/**
	 * 分页查询列表
	 * 
	 * @param toolCoat
	 * @return
	 */
	Page<ToolCoat> selectPageList(@Param("toolCoat") ToolCoat toolCoat,@Param("queryDto") QueryDto queryDto);
	
	
	/**
	 * 分页查询列表
	 * 
	 * @param toolCoat
	 * @return
	 */
	Page<ToolCoat> selectStatisticsPageList(@Param("toolCoat") ToolCoat toolCoat,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param toolCoatId
	 * @return
	 */
	ToolCoat selectById(@Param("toolCoatId") Long toolCoatId);
	
	/**
	 * 根据toolId查询
	 * 
	 * @param toolId
	 * @return
	 */
	List<ToolCoat> selectByToolId(@Param("toolId") Long toolId,@Param("queryDto") QueryDto queryDto);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param toolCoatId
	 * @return
	 */
	Integer deleteById(@Param("toolCoat") ToolCoat toolCoat);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param toolCoatId
	 * @return
	 */
	Integer removeById(@Param("toolCoatId") Long toolCoatId);
	
	/**
	 * 根据toolId查询
	 * 
	 * @param toolId
	 * @return
	 */
	ToolCoat selectSeqByToolId(@Param("toolId") Long toolId);
	
	/**
	 * 根据toolId查询
	 * 
	 * @param toolId
	 * @return
	 */
	ToolCoat selectSeqByFullNumber(@Param("fullNumber") String fullNumber);
	
	/**
	 * 查询列表
	 * 
	 * @param toolCoat
	 * @return
	 */
	List<ToolCoat> selectBytoolIdList(@Param("toolIdList") List<Long> toolIdList);
	
	/**
	 * 查询列表
	 * 
	 * @param toolCoat
	 * @return
	 */
	List<ToolCoat> selectByFullNumberList(@Param("fullNumberList") List<String> fullNumberList);
	
	BigDecimal sumCoatPriceByToolId(@Param("toolId") Long toolId);
	
	int countCoatQtyByToolId(@Param("toolId") Long toolId);
}
