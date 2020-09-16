package org.tsinghuatj.base.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.base.domain.Staff;

/**
 * @ClassName: IStaffService
 * @Description: Staff服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IStaffService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param staff
	 * @return
	 */
	Integer insert(Long userId, Staff staff) throws BusinessException;

	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param staff
	 * @param staffId
	 * @return
	 */
	Integer updateActiveById(Long userId, Staff staff, Long staffId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param staff
	 * @return
	 */
	List<Staff> select(Long userId, Staff staff) throws BusinessException;

	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param staff
	 * @return
	 */
	Pagination<Staff> selectPageList(Long userId, Staff staff, QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param staffId
	 * @return
	 */
	Staff selectById(Long userId, Long staffId) throws BusinessException;
	
	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param staffId
	 * @return
	 */
	Staff selectByUserId(Long userId, Long userPkId) throws BusinessException;

	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param staffId
	 * @return
	 */
	Integer deleteById(Long userId, Long staffId) throws BusinessException;

	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param staffId
	 * @return
	 */
	Integer removeById(Long userId, Long staffId) throws BusinessException;

	/**
	 * 员工信息表导入
	 */
	Integer staffImport(Long userId, List<Staff> staffList) throws BusinessException;

	/**
	 * 
	 * @param userId
	 * @param staffCode
	 * @return
	 * @throws BusinessException
	 */
	Integer staffSynchro(Long userId, String staffCode) throws BusinessException;
    /**
     * 根据员工编码查询
     * @param curuserId
     * @param staffCode
     * @return
     */
	Staff staffGetByStaffCode(Long curuserId, String staffCode)throws BusinessException;
	/**
	 * 校验员工编号
	 * @param staffCode
	 * @param staffId
	 * @return
	 * @throws BusinessException
	 */
	boolean checkStaffCode(String staffCode,Long staffId)throws BusinessException;
}
