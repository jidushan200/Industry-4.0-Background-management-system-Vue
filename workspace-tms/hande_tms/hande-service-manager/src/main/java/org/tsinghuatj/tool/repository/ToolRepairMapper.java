package org.tsinghuatj.tool.repository;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.tool.domain.ToolRepair;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface ToolRepairMapper {

	/**
	 * 插入数据
	 * 
	 * @param toolRepair
	 * @return
	 */
	Integer insert(@Param("toolRepair") ToolRepair toolRepair);

	/**
	 * 根据主键更新有值数据
	 * 
	 * @param toolRepair
	 * @param toolRepairId
	 * @return
	 */
	Integer updateActiveById(@Param("toolRepair") ToolRepair toolRepair, @Param("toolRepairId") Long toolRepairId);

	/**
	 * 查询列表
	 * 
	 * @param toolRepair
	 * @return
	 */
	List<ToolRepair> select(@Param("toolRepair") ToolRepair toolRepair);

	/**
	 * 分页查询列表
	 * 
	 * @param toolRepair
	 * @return
	 */
	Page<ToolRepair> selectPageList(@Param("toolRepair") ToolRepair toolRepair, @Param("queryDto") QueryDto queryDto);

	/**
	 * 分页查询列表
	 * 
	 * @param toolRepair
	 * @return
	 */
	Page<ToolRepair> selectBladePageList(@Param("toolRepair") ToolRepair toolRepair, @Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param toolRepairId
	 * @return
	 */
	ToolRepair selectById(@Param("toolRepairId") Long toolRepairId);

	/**
	 * 根据toolId查询
	 * 
	 * @param toolId
	 * @return
	 */
	List<ToolRepair> selectByToolId(@Param("toolId") Long toolId, @Param("queryDto") QueryDto queryDto);

	/**
	 * 按主键物理删除
	 * 
	 * @param toolRepairId
	 * @return
	 */
	Integer deleteById(@Param("toolRepair") ToolRepair toolRepair);

	/**
	 * 按主键物理删除
	 * 
	 * @param toolRepairId
	 * @return
	 */
	Integer removeById(@Param("toolRepairId") Long toolRepairId);

	/**
	 * 根据toolId查询
	 * 
	 * @param toolId
	 * @return
	 */
	ToolRepair selectSeqByToolId(@Param("toolId") Long toolId);

	List<ToolRepair> selectBytoolIdList(@Param("toolIdList") List<Long> toolIdList);

	List<ToolRepair> selectByFullNumberList(@Param("fullNumberList") List<String> fullNumberList);

	BigDecimal sumRepairPriceByToolId(@Param("toolId") Long toolId);

	int countRepairQtyByToolId(@Param("toolId") Long toolId);

}
