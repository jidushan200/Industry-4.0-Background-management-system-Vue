package org.tsinghuatj.tool.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.tool.domain.ToolRepairPrice;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface ToolRepairPriceMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param toolRepairPrice
	 * @return
	 */
	Integer insert(@Param("toolRepairPrice") ToolRepairPrice toolRepairPrice);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param toolRepairPrice
	 * @param toolRepairPriceId
	 * @return
	 */
	Integer updateActiveById(@Param("toolRepairPrice") ToolRepairPrice toolRepairPrice, @Param("toolRepairPriceId") Long toolRepairPriceId);

	/**
	 * 查询列表
	 * 
	 * @param toolRepairPrice
	 * @return
	 */
	List<ToolRepairPrice> select(@Param("toolRepairPrice") ToolRepairPrice toolRepairPrice);
	
	/**
	 * 分页查询列表
	 * 
	 * @param toolRepairPrice
	 * @return
	 */
	Page<ToolRepairPrice> selectPageList(@Param("toolRepairPrice") ToolRepairPrice toolRepairPrice,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param toolRepairPriceId
	 * @return
	 */
	ToolRepairPrice selectById(@Param("toolRepairPriceId") Long toolRepairPriceId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param toolRepairPriceId
	 * @return
	 */
	Integer deleteById(@Param("toolRepairPrice") ToolRepairPrice toolRepairPrice);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param toolRepairPriceId
	 * @return
	 */
	Integer removeById(@Param("toolRepairPriceId") Long toolRepairPriceId);
	
	/**
	 * 
	 * @param toolId
	 * @param supplierId
	 * @param pkId
	 * @return
	 */
	ToolRepairPrice repairPriceCheck(@Param("toolId") Long toolId, @Param("supplierId") Long supplierId);
	
	/**
	 * 
	 * @param toolId
	 * @param supplierId
	 * @param pkId
	 * @return
	 */
	ToolRepairPrice selectBySupplierId(@Param("supplierId") Long supplierId,@Param("toolNumber") String toolNumber);	
}
