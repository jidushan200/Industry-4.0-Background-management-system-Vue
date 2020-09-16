package org.tsinghuatj.tool.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.tool.domain.ToolRepairPrice;

/**
 * @ClassName: IToolRepairPriceService
 * @Description: ToolRepairPrice服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IToolRepairPriceService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param toolRepairPrice
	 * @return
	 */
	Integer insert(Long userId, ToolRepairPrice toolRepairPrice) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param toolRepairPrice
	 * @param toolRepairPriceId
	 * @return
	 */
	Integer updateActiveById(Long userId, ToolRepairPrice toolRepairPrice, Long toolRepairPriceId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param toolRepairPrice
	 * @return
	 */
	List<ToolRepairPrice> select(Long userId, ToolRepairPrice toolRepairPrice) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param toolRepairPrice
	 * @return
	 */
	Pagination<ToolRepairPrice> selectPageList(Long userId, ToolRepairPrice toolRepairPrice,QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param toolRepairPriceId
	 * @return
	 */
	ToolRepairPrice selectById(Long userId, Long toolRepairPriceId) throws BusinessException;

	
	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param toolRepairPriceId
	 * @return
	 */
	Integer deleteById(Long userId, Long toolRepairPriceId) throws BusinessException;
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param toolRepairPriceId
	 * @return
	 */
	Integer removeById(Long userId, Long toolRepairPriceId) throws BusinessException;
	
	/**
	 * 
	 * @param toolId
	 * @param supplierId
	 * @param toolCoatPriceId
	 * @return
	 * @throws BusinessException
	 */
	ToolRepairPrice repairPriceCheck(Long toolId, Long supplierId) throws BusinessException;
	
	Integer repairPriceImport(Long userId, List<ToolRepairPrice> tpList) throws BusinessException;
}
