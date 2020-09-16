package org.tsinghuatj.base.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;

import org.tsinghuatj.base.domain.Team;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface TeamMapper {

	/**
	 * 插入数据
	 * 
	 * @param team
	 * @return
	 */
	Integer insert(@Param("team") Team team);

	/**
	 * 根据主键更新有值数据
	 * 
	 * @param team
	 * @param teamId
	 * @return
	 */
	Integer updateActiveById(@Param("team") Team team, @Param("teamId") Long teamId);

	/**
	 * 查询列表
	 * 
	 * @param team
	 * @return
	 */
	List<Team> select(@Param("team") Team team);

	/**
	 * 分页查询列表
	 * 
	 * @param team
	 * @return
	 */
	Page<Team> selectPageList(@Param("team") Team team, @Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param teamId
	 * @return
	 */
	Team selectById(@Param("teamId") Long teamId);

	/**
	 * 按主键物理删除
	 * 
	 * @param teamId
	 * @return
	 */
	Integer deleteById(@Param("team") Team team);

	/**
	 * 按主键物理删除
	 * 
	 * @param teamId
	 * @return
	 */
	Integer removeById(@Param("teamId") Long teamId);

	/**
	 * 按照部门查询
	 * 
	 * @param departmentId
	 * @return
	 */
	List<Team> selectByDepartmentId(Long departmentId);

	/**
	 * 根据名称查
	 * 
	 * @param team
	 * @return
	 */
	Team selectByName(@Param("departmentId") Long departmentId,@Param("teamName") String teamName);
}
