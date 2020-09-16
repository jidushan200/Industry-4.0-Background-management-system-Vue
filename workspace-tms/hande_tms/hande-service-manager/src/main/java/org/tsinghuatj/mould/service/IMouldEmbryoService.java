package org.tsinghuatj.mould.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.mould.domain.MouldEmbryo;

/**
 * @ClassName: IMouldEmbryoService
 * @Description: MouldEmbryo服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IMouldEmbryoService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param mouldEmbryo
	 * @return
	 */
	Integer insert(Long userId, MouldEmbryo mouldEmbryo) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param mouldEmbryo
	 * @param mouldEmbryoId
	 * @return
	 */
	Integer updateActiveById(Long userId, MouldEmbryo mouldEmbryo, Long mouldEmbryoId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param mouldEmbryo
	 * @return
	 */
	List<MouldEmbryo> select(Long userId, MouldEmbryo mouldEmbryo) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param mouldEmbryo
	 * @return
	 */
	Pagination<MouldEmbryo> selectPageList(Long userId, MouldEmbryo mouldEmbryo,QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param mouldEmbryoId
	 * @return
	 */
	MouldEmbryo selectById(Long userId, Long mouldEmbryoId) throws BusinessException;

	
	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param mouldEmbryoId
	 * @return
	 */
	Integer deleteById(Long userId, Long mouldEmbryoId) throws BusinessException;
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param mouldEmbryoId
	 * @return
	 */
	Integer removeById(Long userId, Long mouldEmbryoId) throws BusinessException;
	
	/**
	 * 设备表导入
	 */
	Integer embryoImport(Long userId, List<MouldEmbryo> mouldEmbryoList) throws BusinessException;
	
	/**
	 * 设备信息同步
	 * 
	 * @param userId
	 * @return
	 * @throws BusinessException
	 */
	Integer embryoSynchro(Long userId, String embryoCode) throws BusinessException;
	
	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param equipmentId
	 * @return
	 */
	boolean checkEmbryoCode(String embryoCode, Long pkId) throws BusinessException;
}
