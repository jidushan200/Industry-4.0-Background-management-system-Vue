package org.tsinghuatj.tool.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.tool.domain.ToolBase;

/**
 * @ClassName: IToolBaseService
 * @Description: ToolBase服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IToolBaseService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param toolBase
	 * @return
	 */
	Integer insert(Long userId, ToolBase toolBase) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param toolBase
	 * @param toolBaseId
	 * @return
	 */
	Integer updateActiveById(Long userId, ToolBase toolBase, Long toolBaseId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param toolBase
	 * @return
	 */
	List<ToolBase> select(Long userId, ToolBase toolBase) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param toolBase
	 * @return
	 */
	Pagination<ToolBase> selectPageList(Long userId, ToolBase toolBase,QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param toolBaseId
	 * @return
	 */
	ToolBase selectById(Long userId, Long toolBaseId) throws BusinessException;

	
	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param toolBaseId
	 * @return
	 */
	Integer deleteById(Long userId, Long toolBaseId) throws BusinessException;
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param toolBaseId
	 * @return
	 */
	Integer removeById(Long userId, Long toolBaseId) throws BusinessException;
	/**
	 * 刀具基础信息表导入
	 */
	Integer toolBaseImport(Long userId, List<ToolBase> toolBaseList) throws BusinessException;
	
	
	/**
	 * 刀具信息同步
	 * @param userId
	 * @return
	 * @throws BusinessException
	 */
	Integer toolBaseSynchro(Long userId,String toolNumber)throws BusinessException;
	
	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param toolBaseId
	 * @return
	 */
	ToolBase selectByNumber(Long userId, String toolNumber) throws BusinessException;
	
	
	boolean toolNumberCheck(String toolNumber,Long pkId)throws BusinessException;

    /**
     * 校验toolMap
     * @param toolMap
     * @return
     */
	int toolMapValidate(String toolMap);

	
	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param toolBase
	 * @return
	 */
	List<ToolBase> selectMapList(Long userId, ToolBase toolBase) throws BusinessException;

}
