package org.tsinghuatj.base.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.base.domain.Department;

/**
 * @ClassName: IDepartmentService
 * @Description: Department服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IDepartmentService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param department
	 * @return
	 */
	Integer insert(Long userId, Department department) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param department
	 * @param departmentId
	 * @return
	 */
	Integer updateActiveById(Long userId, Department department, Long departmentId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param department
	 * @return
	 */
	List<Department> select(Long userId, Department department) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param department
	 * @return
	 */
	Pagination<Department> selectPageList(Long userId, Department department,QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param departmentId
	 * @return
	 */
	Department selectById(Long userId, Long departmentId) throws BusinessException;

	
	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param departmentId
	 * @return
	 */
	Integer deleteById(Long userId, Long departmentId) throws BusinessException;
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param departmentId
	 * @return
	 */
	Integer removeById(Long userId, Long departmentId) throws BusinessException;
	/**
	 * 部门信息表导入
	 */
	Integer departmentImport(Long userId, List<Department> departmentList) throws BusinessException;
	
	/**
	 * 部门信息同步
	 * @param userId
	 * @return
	 * @throws BusinessException
	 */
	Integer departmentSynchro(Long userId,String departmentCode)throws BusinessException;
}
