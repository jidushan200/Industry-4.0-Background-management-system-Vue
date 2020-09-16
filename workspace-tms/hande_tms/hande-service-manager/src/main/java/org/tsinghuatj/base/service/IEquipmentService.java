package org.tsinghuatj.base.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.base.domain.Equipment;

/**
 * @ClassName: IEquipmentService
 * @Description: Equipment服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IEquipmentService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param equipment
	 * @return
	 */
	Integer insert(Long userId, Equipment equipment) throws BusinessException;

	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param equipment
	 * @param equipmentId
	 * @return
	 */
	Integer updateActiveById(Long userId, Equipment equipment, Long equipmentId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param equipment
	 * @return
	 */
	List<Equipment> select(Long userId, Equipment equipment) throws BusinessException;

	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param equipment
	 * @return
	 */
	Pagination<Equipment> selectPageList(Long userId, Equipment equipment, QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param equipmentId
	 * @return
	 */
	Equipment selectById(Long userId, Long equipmentId) throws BusinessException;

	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param equipmentId
	 * @return
	 */
	Integer deleteById(Long userId, Long equipmentId) throws BusinessException;

	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param equipmentId
	 * @return
	 */
	Integer removeById(Long userId, Long equipmentId) throws BusinessException;

	/**
	 * 设备表导入
	 */
	Integer equipmentImport(Long userId, List<Equipment> equipmentList) throws BusinessException;

	/**
	 * 设备信息同步
	 * 
	 * @param userId
	 * @return
	 * @throws BusinessException
	 */
	Integer equipmentSynchro(Long userId, String equipmentCode) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param equipmentId
	 * @return
	 */
	boolean checkEquipmentCode(String equipmentCode, Long pkId) throws BusinessException;
}
