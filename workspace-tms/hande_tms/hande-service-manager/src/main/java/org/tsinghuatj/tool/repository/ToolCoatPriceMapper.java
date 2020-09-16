package org.tsinghuatj.tool.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;

import org.tsinghuatj.tool.domain.ToolCoatPrice;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface ToolCoatPriceMapper {

	/**
	 * 插入数据
	 * 
	 * @param toolCoatPrice
	 * @return
	 */
	Integer insert(@Param("toolCoatPrice") ToolCoatPrice toolCoatPrice);

	/**
	 * 根据主键更新有值数据
	 * 
	 * @param toolCoatPrice
	 * @param toolCoatPriceId
	 * @return
	 */
	Integer updateActiveById(@Param("toolCoatPrice") ToolCoatPrice toolCoatPrice, @Param("toolCoatPriceId") Long toolCoatPriceId);

	/**
	 * 查询列表
	 * 
	 * @param toolCoatPrice
	 * @return
	 */
	List<ToolCoatPrice> select(@Param("toolCoatPrice") ToolCoatPrice toolCoatPrice);

	/**
	 * 分页查询列表
	 * 
	 * @param toolCoatPrice
	 * @return
	 */
	Page<ToolCoatPrice> selectPageList(@Param("toolCoatPrice") ToolCoatPrice toolCoatPrice, @Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param toolCoatPriceId
	 * @return
	 */
	ToolCoatPrice selectById(@Param("toolCoatPriceId") Long toolCoatPriceId);

	/**
	 * 按主键物理删除
	 * 
	 * @param toolCoatPriceId
	 * @return
	 */
	Integer deleteById(@Param("toolCoatPrice") ToolCoatPrice toolCoatPrice);

	/**
	 * 按主键物理删除
	 * 
	 * @param toolCoatPriceId
	 * @return
	 */
	Integer removeById(@Param("toolCoatPriceId") Long toolCoatPriceId);
	/**
	 * 
	 * @param toolId
	 * @param supplierId
	 * @param pkId
	 * @return
	 */
	ToolCoatPrice coatPriceCheck(@Param("toolId") Long toolId, @Param("supplierId") Long supplierId, @Param("pkId") Long pkId);
	
	/**
	 * 根据主键查询
	 * 
	 * @param toolCoatPriceId
	 * @return
	 */
	ToolCoatPrice selectByToolNumberAndSupplierId(@Param("toolNumber") String toolNumber,@Param("supplierId") Long supplierId);
}
