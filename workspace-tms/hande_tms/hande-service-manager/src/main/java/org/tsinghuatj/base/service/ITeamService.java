package org.tsinghuatj.base.service;

import java.util.List;

import org.tsinghuatj.base.domain.Team;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;

/**
 * @ClassName: ITeamService
 * @Description: Team服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface ITeamService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param team
	 * @return
	 */
	Integer insert(Long userId, Team team) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param team
	 * @param teamId
	 * @return
	 */
	Integer updateActiveById(Long userId, Team team, Long teamId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param team
	 * @return
	 */
	List<Team> select(Long userId, Team team) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param team
	 * @return
	 */
	Pagination<Team> selectPageList(Long userId, Team team,QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param teamId
	 * @return
	 */
	Team selectById(Long userId, Long teamId) throws BusinessException;

	
	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param teamId
	 * @return
	 */
	Integer deleteById(Long userId, Long teamId) throws BusinessException;
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param teamId
	 * @return
	 */
	Integer removeById(Long userId, Long teamId) throws BusinessException;
    /**
     * 按照部门id查询
     * @param curuserId
     * @param departmentId
     * @return
     * @throws BusinessException
     */
	List<Team> selectByDepartmentId(Long curuserId, Long departmentId) throws BusinessException;
	

	/**
	 * 员工信息表导入
	 */
	Integer teamImport(Long userId, List<Team> teamList) throws BusinessException;
}
