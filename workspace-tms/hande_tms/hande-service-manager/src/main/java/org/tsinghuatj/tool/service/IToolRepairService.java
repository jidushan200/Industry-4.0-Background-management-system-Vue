package org.tsinghuatj.tool.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.tool.domain.Tool;
import org.tsinghuatj.tool.domain.ToolRepair;

/**
 * @ClassName: IToolRepairService
 * @Description: ToolRepair服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IToolRepairService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param toolRepair
	 * @return
	 */
	Integer insert(Long userId, ToolRepair toolRepair,Tool tool) throws BusinessException;
	
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param toolRepair
	 * @return
	 */
	Integer insertBladeRepair(Long userId, ToolRepair toolRepair,Long waitId) throws BusinessException;
	
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param toolRepair
	 * @return
	 */
	Integer insertComposeBladeRepair(Long userId,String realName, String composeNumber,Long waitId,String detailJson) throws BusinessException;
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param toolRepair
	 * @param toolRepairId
	 * @return
	 */
	Integer updateActiveById(Long userId, ToolRepair toolRepair, Long toolRepairId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param toolRepair
	 * @return
	 */
	List<ToolRepair> select(Long userId, ToolRepair toolRepair) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param toolRepair
	 * @return
	 */
	Pagination<ToolRepair> selectPageList(Long userId, ToolRepair toolRepair,QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param toolRepairId
	 * @return
	 */
	ToolRepair selectById(Long userId, Long toolRepairId) throws BusinessException;

	
	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param toolRepairId
	 * @return
	 */
	Integer deleteById(Long userId, Long toolRepairId) throws BusinessException;
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param toolRepairId
	 * @return
	 */
	Integer removeById(Long userId, Long toolRepairId) throws BusinessException;
}
