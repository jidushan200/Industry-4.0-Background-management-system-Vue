package org.tsinghuatj.base.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.tsinghuatj.base.domain.Staff;
import org.tsinghuatj.base.domain.StaffDepartment;
import org.tsinghuatj.base.domain.Team;
import org.tsinghuatj.base.repository.DepartmentMapper;
import org.tsinghuatj.base.repository.StaffDepartmentMapper;
import org.tsinghuatj.base.repository.StaffMapper;
import org.tsinghuatj.base.repository.TeamMapper;
import org.tsinghuatj.base.service.IStaffService;
import org.tsinghuatj.erp.domain.ErpStaff;
import org.tsinghuatj.erp.repository.ErpStaffMapper;
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
 * Staff 表数据服务层接口实现类
 *
 */
@Service("staffService")
public class StaffServiceImpl extends BaseServiceImpl implements IStaffService {
	private @Resource StaffMapper staffMapper;
	private @Resource ErpStaffMapper erpstaffMapper;
	private @Resource DepartmentMapper departmentMapper;
	private @Resource TeamMapper teamMapper;
	private @Resource UserAccountMapper userAccountMapper;
	private @Resource StaffDepartmentMapper staffdepartmentMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, Staff staff) throws BusinessException {
		staff.setPkId(getPkId());
		staff.setCreateTime(new Date());
		staff.setCreateUser(userId);
		staff.setUpdateTime(new Date());
		staff.setUpdateUser(userId);
		staff.setDelMark(0);
		return staffMapper.insert(staff);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, Staff staff, Long staffId) throws BusinessException {
		staff.setUpdateTime(new Date());
		staff.setUpdateUser(userId);
		return staffMapper.updateActiveById(staff, staffId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Staff selectById(Long userId, Long staffId) throws BusinessException {
		return staffMapper.selectById(staffId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Staff selectByUserId(Long userId, Long userPkId) throws BusinessException {
		return staffMapper.selectByUserId(userPkId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long staffId) throws BusinessException {
		return staffMapper.removeById(staffId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long staffId) throws BusinessException {
		Staff staff = new Staff();
		staff.setPkId(staffId);
		staff.setUpdateTime(new Date());
		staff.setUpdateUser(userId);
		return staffMapper.deleteById(staff);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<Staff> select(Long userId, Staff staff) throws BusinessException {
		return staffMapper.select(staff);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<Staff> selectPageList(Long userId, Staff staff, QueryDto queryDto) throws BusinessException {
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<Staff> page = staffMapper.selectPageList(staff, queryDto);
		List<Long> idList = new ArrayList<Long>();
		for (Staff item : page.getResult()) {
			idList.add(item.getUpdateUser());
			idList.add(item.getCreateUser());
		}
		if (idList.size() > 0) {
			idList = idList.stream().distinct().collect(Collectors.toList());
			List<UserAccount> userList = userAccountMapper.selectByIdList(idList);
			Map<Long, UserAccount> accoutMap = userList.stream().collect(Collectors.toMap(UserAccount::getPkId, t -> t, (k1, k2) -> k1));
			for (Staff item : page.getResult()) {
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
		return new Pagination<Staff>(page.getTotal(), page.getResult());
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer staffImport(Long userId, List<Staff> staffList) throws BusinessException {
		Date date = new Date();
		List<String> deartNamelist = new ArrayList<String>();
		for (Staff item : staffList) {
			deartNamelist.add(item.getDepartmentName());
		}
		deartNamelist = deartNamelist.stream().distinct().collect(Collectors.toList());
		List<StaffDepartment> dList = staffdepartmentMapper.selectByNameList(deartNamelist);
		Map<String, StaffDepartment> accoutMap = dList.stream().collect(Collectors.toMap(StaffDepartment::getDepartmentName, t -> t, (k1, k2) -> k1));
		staffList.forEach(staff -> {
			if (StringUtil.isEmpty(staff.getStaffCode()) || StringUtil.isEmpty(staff.getStaffName())) {
				return;
			}
			StaffDepartment depart = accoutMap.get(staff.getDepartmentName());
			if (null != depart) {
				staff.setDepartmentId(depart.getPkId());
			}
			Team team = null;
			if (StringUtils.isNotEmpty(staff.getTeamName())) {
				team = teamMapper.selectByName(staff.getDepartmentId(), staff.getTeamName());
				if (null != team) {
					staff.setTeamId(team.getPkId());
					staff.setTeamName(team.getTeamName());
				}
			}

			staff.setUpdateTime(date);
			staff.setUpdateUser(userId);
			Staff oldstaff = staffMapper.selectByStaffCode(staff.getStaffCode(), null);
			if (null == oldstaff) {
				try {
					staff.setPkId(getPkId());
				} catch (BusinessException e) {
					e.printStackTrace();
				}
				if ("男".equals(staff.getGenderName())) {
					staff.setGender(1);
				} else {
					staff.setGender(2);
				}
				staff.setCreateTime(date);
				staff.setCreateUser(userId);
				staff.setDelMark(0);
				staff.setStaffStatus(1);
				staffMapper.insert(staff);
			} else {
				staffMapper.updateActiveById(staff, oldstaff.getPkId());
			}
		});
		return 1;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer staffSynchro(Long userId, String staffCode) throws BusinessException {
		ErpStaff erpstaff = erpstaffMapper.selectByStaffNumber(staffCode);
		if (null == erpstaff) {
			return 0;
		}
		Staff staff = staffMapper.selectByStaffCode(staffCode, null);
		Date date = new Date();
		StaffDepartment staffdepartment = staffdepartmentMapper.selectByName(erpstaff.getDepartmentName(), null);
		Team team = null;

		if (null == staffdepartment) {
			staffdepartment = new StaffDepartment();
			staffdepartment.setPkId(getPkId());
			staffdepartment.setDepartmentName(erpstaff.getDepartmentName());
			staffdepartment.setCreateUser(userId);
			staffdepartment.setCreateTime(date);
			staffdepartment.setUpdateTime(date);
			staffdepartment.setUpdateUser(userId);
			staffdepartment.setDelMark(0);
			staffdepartmentMapper.insert(staffdepartment);
		}
		Long staffDepartId = staffdepartment.getPkId();
		if (null != staff) {
			staff.setGender(erpstaff.getGender().equals("M") ? 1 : 2);
			staff.setStaffName(erpstaff.getStaffName());
			staff.setDepartmentId(staffDepartId);
			staff.setDepartmentName(erpstaff.getDepartmentName());
			staff.setPosition(erpstaff.getPostion());
			staff.setTeamName(erpstaff.getTeam());
			staff.setUpdateTime(date);
			staff.setUpdateUser(userId);
			if (StringUtils.isNotEmpty(erpstaff.getTeam())) {
				team = teamMapper.selectByName(staffDepartId, erpstaff.getTeam());
			}
			if (null != team) {
				staff.setTeamId(team.getPkId());
				staff.setTeamName(team.getTeamName());
			}
			if ("M".equals(erpstaff.getGender())) {
				staff.setGender(1);
			} else {
				staff.setGender(2);
			}
			staffMapper.updateActiveById(staff, staff.getPkId());
		} else {
			staff = new Staff();
			staff.setPkId(getPkId());
			staff.setGender(erpstaff.getGender().equals("M") ? 1 : 2);
			staff.setStaffCode(staffCode);
			staff.setStaffName(erpstaff.getStaffName());
			staff.setDepartmentId(staffDepartId);
			staff.setDepartmentName(erpstaff.getDepartmentName());
			staff.setPosition(erpstaff.getPostion());
			if (StringUtils.isNotEmpty(erpstaff.getTeam())) {
				team = teamMapper.selectByName(staffDepartId, erpstaff.getTeam());
			}
			if (null != team) {
				staff.setTeamId(team.getPkId());
				staff.setTeamName(team.getTeamName());
			}
			if ("M".equals(erpstaff.getGender())) {
				staff.setGender(1);
			} else {
				staff.setGender(2);
			}
			staff.setCreateTime(date);
			staff.setCreateUser(userId);
			staff.setUpdateTime(date);
			staff.setUpdateUser(userId);
			staff.setDelMark(0);
			staff.setStaffStatus(0);
			staffMapper.insert(staff);
		}
		return 1;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Staff staffGetByStaffCode(Long curuserId, String staffCode) throws BusinessException {
		return staffMapper.selectByStaffCode(staffCode, null);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public boolean checkStaffCode(String staffCode, Long staffId) throws BusinessException {
		Staff staff = staffMapper.selectByStaffCode(staffCode, staffId);
		if (null != staff) {
			throw new BusinessException("staffcode.exists.error");
		}
		return true;
	}
}