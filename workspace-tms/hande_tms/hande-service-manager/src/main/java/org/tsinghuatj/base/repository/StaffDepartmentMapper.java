package org.tsinghuatj.base.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.base.domain.StaffDepartment;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface StaffDepartmentMapper {

	/**
	 * 插入数据
	 * 
	 * @param staffDepartment
	 * @return
	 */
	Integer insert(@Param("staffDepartment") StaffDepartment staffDepartment);

	/**
	 * 根据主键更新有值数据
	 * 
	 * @param staffDepartment
	 * @param staffDepartmentId
	 * @return
	 */
	Integer updateActiveById(@Param("staffDepartment") StaffDepartment staffDepartment, @Param("staffDepartmentId") Long staffDepartmentId);

	/**
	 * 查询列表
	 * 
	 * @param staffDepartment
	 * @return
	 */
	List<StaffDepartment> select(@Param("staffDepartment") StaffDepartment staffDepartment);

	/**
	 * 分页查询列表
	 * 
	 * @param staffDepartment
	 * @return
	 */
	Page<StaffDepartment> selectPageList(@Param("staffDepartment") StaffDepartment staffDepartment, @Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param staffDepartmentId
	 * @return
	 */
	StaffDepartment selectById(@Param("staffDepartmentId") Long staffDepartmentId);

	/**
	 * 按编码查询
	 * 
	 * @param departmentCode
	 * @return
	 */
	StaffDepartment selectByCode(@Param("departmentCode") String departmentCode);

	/**
	 * 按主键物理删除
	 * 
	 * @param staffDepartmentId
	 * @return
	 */
	Integer deleteById(@Param("staffDepartment") StaffDepartment staffDepartment);

	/**
	 * 按主键物理删除
	 * 
	 * @param staffDepartmentId
	 * @return
	 */
	Integer removeById(@Param("staffDepartmentId") Long staffDepartmentId);

	/**
	 * 根据主键查询
	 * 
	 * @param staffDepartmentId
	 * @return
	 */
	StaffDepartment selectByName(@Param("departmentName") String departmentName, @Param("pkId") Long pkId);

	/**
	 * 查询列表
	 * 
	 * @param department
	 * @return
	 */
	List<StaffDepartment> selectByNameList(@Param("nameList") List<String> nameList);
}
