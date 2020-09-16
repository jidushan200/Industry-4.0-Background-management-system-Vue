package org.tsinghuatj.base.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.tsinghuatj.base.domain.StaffDepartment;
import org.tsinghuatj.base.domain.Team;
import org.tsinghuatj.base.repository.StaffDepartmentMapper;
import org.tsinghuatj.base.repository.TeamMapper;
import org.tsinghuatj.base.service.ITeamService;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.service.impl.BaseServiceImpl;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.sys.domain.UserAccount;
import org.tsinghuatj.sys.repository.UserAccountMapper;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.util.StringUtil;

/**
 *
 * Team 表数据服务层接口实现类
 *
 */
@Service("teamService")
public class TeamServiceImpl extends BaseServiceImpl implements ITeamService {
	private @Resource TeamMapper teamMapper;
	private @Resource UserAccountMapper userAccountMapper;
	private @Resource StaffDepartmentMapper staffdepartmentMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, Team team) throws BusinessException {
		team.setPkId(getPkId());
		team.setCreateTime(new Date());
		team.setCreateUser(userId);
		team.setUpdateTime(new Date());
		team.setUpdateUser(userId);
		team.setDelMark(0);
		return teamMapper.insert(team);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, Team team, Long teamId) throws BusinessException {
		team.setUpdateTime(new Date());
		team.setUpdateUser(userId);
		return teamMapper.updateActiveById(team, teamId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Team selectById(Long userId, Long teamId) throws BusinessException {
		return teamMapper.selectById(teamId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long teamId) throws BusinessException {
		return teamMapper.removeById(teamId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long teamId) throws BusinessException {
		Team team = new Team();
		team.setPkId(teamId);
		team.setUpdateTime(new Date());
		team.setUpdateUser(userId);
		return teamMapper.deleteById(team);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<Team> select(Long userId, Team team) throws BusinessException {
		return teamMapper.select(team);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<Team> selectPageList(Long userId, Team team, QueryDto queryDto) throws BusinessException {
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<Team> page = teamMapper.selectPageList(team, queryDto);
		List<Long> idList = new ArrayList<Long>();
		for (Team item : page.getResult()) {
			idList.add(item.getUpdateUser());
			idList.add(item.getCreateUser());
		}
		if (idList.size() > 0) {
			idList = idList.stream().distinct().collect(Collectors.toList());
			List<UserAccount> userList = userAccountMapper.selectByIdList(idList);
			Map<Long, UserAccount> accoutMap = userList.stream().collect(Collectors.toMap(UserAccount::getPkId, t -> t, (k1, k2) -> k1));
			for (Team item : page.getResult()) {
				UserAccount create = accoutMap.get(item.getCreateUser());
				if (null != create) {
					item.setCreateUserName(create.getRealName());
				}
				UserAccount account = accoutMap.get(item.getUpdateUser());
				if (null != account) {
					item.setUpdateUserName(account.getRealName());
				}
			}
		}
		return new Pagination<Team>(page.getTotal(), page.getResult());
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<Team> selectByDepartmentId(Long curuserId, Long departmentId) throws BusinessException {
		return teamMapper.selectByDepartmentId(departmentId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer teamImport(Long userId, List<Team> teamList) throws BusinessException {
		List<StaffDepartment> dList = staffdepartmentMapper.select(new StaffDepartment());
		Map<String, StaffDepartment> accoutMap = dList.stream().collect(Collectors.toMap(StaffDepartment::getDepartmentName, t -> t, (k1, k2) -> k1));
		teamList.forEach(team -> {
			if (StringUtil.isEmpty(team.getDepartmentName()) || StringUtil.isEmpty(team.getTeamName())) {
				return;
			}
			StaffDepartment depart = accoutMap.get(team.getDepartmentName());
			if (null != depart) {
				team.setDepartmentId(depart.getPkId());
			}
			team.setUpdateTime(new Date());
			team.setUpdateUser(userId);
			Team oldteam = teamMapper.selectByName(team.getDepartmentId(),team.getTeamName());
			if (null == oldteam) {
				try {
					team.setPkId(getPkId());
				} catch (BusinessException e) {
					e.printStackTrace();
				}
				team.setCreateTime(new Date());
				team.setCreateUser(userId);
				team.setDelMark(0);
				teamMapper.insert(team);
			} else {
				teamMapper.updateActiveById(team, oldteam.getPkId());
			}
		});
		return 1;
	}
}