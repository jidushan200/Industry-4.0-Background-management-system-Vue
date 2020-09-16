package org.tsinghuatj.tool.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.tool.domain.ToolCoatPrice;

/**
 * @ClassName: IToolCoatPriceService
 * @Description: ToolCoatPrice服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IToolCoatPriceService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param toolCoatPrice
	 * @return
	 */
	Integer insert(Long userId, ToolCoatPrice toolCoatPrice) throws BusinessException;

	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param toolCoatPrice
	 * @param toolCoatPriceId
	 * @return
	 */
	Integer updateActiveById(Long userId, ToolCoatPrice toolCoatPrice, Long toolCoatPriceId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param toolCoatPrice
	 * @return
	 */
	List<ToolCoatPrice> select(Long userId, ToolCoatPrice toolCoatPrice) throws BusinessException;

	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param toolCoatPrice
	 * @return
	 */
	Pagination<ToolCoatPrice> selectPageList(Long userId, ToolCoatPrice toolCoatPrice, QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param toolCoatPriceId
	 * @return
	 */
	ToolCoatPrice selectById(Long userId, Long toolCoatPriceId) throws BusinessException;

	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param toolCoatPriceId
	 * @return
	 */
	Integer deleteById(Long userId, Long toolCoatPriceId) throws BusinessException;

	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param toolCoatPriceId
	 * @return
	 */
	Integer removeById(Long userId, Long toolCoatPriceId) throws BusinessException;
	
	/**
	 * 
	 * @param toolId
	 * @param supplierId
	 * @param toolCoatPriceId
	 * @return
	 * @throws BusinessException
	 */
	boolean coatPriceCheck(Long toolId, Long supplierId, Long toolCoatPriceId) throws BusinessException;
	
	
	Integer coatPriceImport(Long userId, List<ToolCoatPrice> tpList) throws BusinessException;
}
