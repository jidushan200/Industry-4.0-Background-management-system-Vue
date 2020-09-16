package org.tsinghuatj.tool.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.tool.domain.ToolPlate;

/**
 * @ClassName: IToolPlateService
 * @Description: ToolPlate服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IToolPlateService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param toolPlate
	 * @return
	 */
	Integer insert(Long userId, ToolPlate toolPlate) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param toolPlate
	 * @param toolPlateId
	 * @return
	 */
	Integer updateActiveById(Long userId, ToolPlate toolPlate, Long toolPlateId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param toolPlate
	 * @return
	 */
	List<ToolPlate> select(Long userId, ToolPlate toolPlate) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param toolPlate
	 * @return
	 */
	Pagination<ToolPlate> selectPageList(Long userId, ToolPlate toolPlate,QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param toolPlateId
	 * @return
	 */
	ToolPlate selectById(Long userId, Long toolPlateId) throws BusinessException;

	
	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param toolPlateId
	 * @return
	 */
	Integer deleteById(Long userId, Long toolPlateId) throws BusinessException;
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param toolPlateId
	 * @return
	 */
	Integer removeById(Long userId, Long toolPlateId) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param toolPlate
	 * @param toolPlateId
	 * @return
	 */
	Integer importToolPlate(Long userId, List<ToolPlate> toolPlateList) throws BusinessException;
}
