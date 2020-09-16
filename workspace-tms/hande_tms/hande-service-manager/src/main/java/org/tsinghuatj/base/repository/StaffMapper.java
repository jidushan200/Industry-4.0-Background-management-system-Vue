package org.tsinghuatj.base.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;

import org.tsinghuatj.base.domain.Staff;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface StaffMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param staff
	 * @return
	 */
	Integer insert(@Param("staff") Staff staff);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param staff
	 * @param staffId
	 * @return
	 */
	Integer updateActiveById(@Param("staff") Staff staff, @Param("staffId") Long staffId);

	/**
	 * 查询列表
	 * 
	 * @param staff
	 * @return
	 */
	List<Staff> select(@Param("staff") Staff staff);
	
	/**
	 * 分页查询列表
	 * 
	 * @param staff
	 * @return
	 */
	Page<Staff> selectPageList(@Param("staff") Staff staff,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param staffId
	 * @return
	 */
	Staff selectById(@Param("staffId") Long staffId);
	
	/**
	 * 根据主键查询
	 * 
	 * @param staffId
	 * @return
	 */
	Staff selectByUserId(@Param("userId") Long userId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param staffId
	 * @return
	 */
	Integer deleteById(@Param("staff") Staff staff);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param staffId
	 * @return
	 */
	Integer removeById(@Param("staffId") Long staffId);
	

	/**
	 * 根据主键查询
	 * 
	 * @param staffCode
	 * @return
	 */
	Staff selectByStaffCode(@Param("staffCode") String staffCode, @Param("pkId") Long pkId);
	
	/**
	 * 根据主键查询
	 * 
	 * @param staffCode
	 * @return
	 */
	Staff selectByStaffName(@Param("staffName") String staffName, @Param("pkId") Long pkId);
}
