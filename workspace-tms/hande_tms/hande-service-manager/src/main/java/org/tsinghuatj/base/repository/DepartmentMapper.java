package org.tsinghuatj.base.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;

import org.tsinghuatj.base.domain.Department;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface DepartmentMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param department
	 * @return
	 */
	Integer insert(@Param("department") Department department);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param department
	 * @param departmentId
	 * @return
	 */
	Integer updateActiveById(@Param("department") Department department, @Param("departmentId") Long departmentId);

	/**
	 * 查询列表
	 * 
	 * @param department
	 * @return
	 */
	List<Department> select(@Param("department") Department department);
	
	/**
	 * 分页查询列表
	 * 
	 * @param department
	 * @return
	 */
	Page<Department> selectPageList(@Param("department") Department department,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param departmentId
	 * @return
	 */
	Department selectById(@Param("departmentId") Long departmentId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param departmentId
	 * @return
	 */
	Integer deleteById(@Param("department") Department department);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param departmentId
	 * @return
	 */
	Integer removeById(@Param("departmentId") Long departmentId);
	
	/**
	 * 根据主键查询
	 * 
	 * @param departmentId
	 * @return
	 */
	Department selectByCode(@Param("departmentCode") String departmentCode);
	
	
	/**
	 * 查询列表
	 * 
	 * @param department
	 * @return
	 */
	List<Department> selectByNameList(@Param("nameList") List<String> nameList);
	
}
