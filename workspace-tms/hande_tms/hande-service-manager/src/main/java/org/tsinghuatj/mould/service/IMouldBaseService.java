package org.tsinghuatj.mould.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.mould.domain.MouldBase;

/**
 * @ClassName: IMouldBaseService
 * @Description: MouldBase服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IMouldBaseService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param mouldBase
	 * @return
	 */
	Integer insert(Long userId, MouldBase mouldBase) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param mouldBase
	 * @param mouldBaseId
	 * @return
	 */
	Integer updateActiveById(Long userId, MouldBase mouldBase, Long mouldBaseId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param mouldBase
	 * @return
	 */
	List<MouldBase> select(Long userId, MouldBase mouldBase) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param mouldBase
	 * @return
	 */
	Pagination<MouldBase> selectPageList(Long userId, MouldBase mouldBase,QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param mouldBaseId
	 * @return
	 */
	MouldBase selectById(Long userId, Long mouldBaseId) throws BusinessException;

	
	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param mouldBaseId
	 * @return
	 */
	Integer deleteById(Long userId, Long mouldBaseId) throws BusinessException;
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param mouldBaseId
	 * @return
	 */
	Integer removeById(Long userId, Long mouldBaseId) throws BusinessException;
	
	boolean mouldNumberCheck(String mouldNumber,Long pkId)throws BusinessException;
	
	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param toolBaseId
	 * @return
	 */
	MouldBase selectByNumber(Long userId, String mouldNumber) throws BusinessException;
	
	/**
	 * 刀具基础信息表导入
	 */
	Integer mouldBaseImport(Long userId, List<MouldBase> mouldBaseList) throws BusinessException;
	
	
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param mouldBaseId
	 * @return
	 */
	Integer removeByMouldNumber(Long userId, String mouldNumber) throws BusinessException;
}
