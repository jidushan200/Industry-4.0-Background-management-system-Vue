package org.tsinghuatj.base.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.base.domain.StaffDepartment;

/**
 * @ClassName: IStaffDepartmentService
 * @Description: StaffDepartment服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IStaffDepartmentService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param staffDepartment
	 * @return
	 */
	Integer insert(Long userId, StaffDepartment staffDepartment) throws BusinessException;

	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param staffDepartment
	 * @param staffDepartmentId
	 * @return
	 */
	Integer updateActiveById(Long userId, StaffDepartment staffDepartment, Long staffDepartmentId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param staffDepartment
	 * @return
	 */
	List<StaffDepartment> select(Long userId, StaffDepartment staffDepartment) throws BusinessException;

	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param staffDepartment
	 * @return
	 */
	Pagination<StaffDepartment> selectPageList(Long userId, StaffDepartment staffDepartment, QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param staffDepartmentId
	 * @return
	 */
	StaffDepartment selectById(Long userId, Long staffDepartmentId) throws BusinessException;

	/**
	 * 根据编码查询
	 * 
	 * @param userId
	 * @param staffCode
	 * @return
	 * @throws BusinessException
	 */
	StaffDepartment selectByCode(Long userId, String departmentCode) throws BusinessException;

	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param staffDepartmentId
	 * @return
	 */
	Integer deleteById(Long userId, Long staffDepartmentId) throws BusinessException;

	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param staffDepartmentId
	 * @return
	 */
	Integer removeById(Long userId, Long staffDepartmentId) throws BusinessException;

	Integer staffDepartmentImport(Long userId, List<StaffDepartment> staffDepartmentList) throws BusinessException;
}
