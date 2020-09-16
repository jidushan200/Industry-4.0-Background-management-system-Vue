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
import org.tsinghuatj.base.repository.StaffDepartmentMapper;
import org.tsinghuatj.base.service.IStaffDepartmentService;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.service.impl.BaseServiceImpl;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.sys.domain.UserAccount;
import org.tsinghuatj.sys.repository.UserAccountMapper;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 *
 * StaffDepartment 表数据服务层接口实现类
 *
 */
@Service("staffDepartmentService")
public class StaffDepartmentServiceImpl extends BaseServiceImpl implements IStaffDepartmentService {

	private @Resource StaffDepartmentMapper staffDepartmentMapper;
	private @Resource UserAccountMapper userAccountMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, StaffDepartment staffDepartment) throws BusinessException {
		staffDepartment.setPkId(getPkId());
		staffDepartment.setCreateTime(new Date());
		staffDepartment.setCreateUser(userId);
		staffDepartment.setUpdateTime(new Date());
		staffDepartment.setUpdateUser(userId);
		staffDepartment.setDelMark(0);
		staffDepartment = staffDepartmentMapper.selectByName(staffDepartment.getDepartmentName(), null);
		if(null!=staffDepartment){
			throw new BusinessException("department.exists.error");
		}		
		return staffDepartmentMapper.insert(staffDepartment);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, StaffDepartment staffDepartment, Long staffDepartmentId) throws BusinessException {
		staffDepartment.setUpdateTime(new Date());
		staffDepartment.setUpdateUser(userId);
		staffDepartment = staffDepartmentMapper.selectByName(staffDepartment.getDepartmentName(), staffDepartment.getPkId());
		if(null!=staffDepartment){
			throw new BusinessException("department.exists.error");
		}		
		return staffDepartmentMapper.updateActiveById(staffDepartment, staffDepartmentId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public StaffDepartment selectById(Long userId, Long staffDepartmentId) throws BusinessException {
		return staffDepartmentMapper.selectById(staffDepartmentId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long staffDepartmentId) throws BusinessException {
		return staffDepartmentMapper.removeById(staffDepartmentId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long staffDepartmentId) throws BusinessException {
		StaffDepartment staffDepartment = new StaffDepartment();
		staffDepartment.setPkId(staffDepartmentId);
		staffDepartment.setUpdateTime(new Date());
		staffDepartment.setUpdateUser(userId);
		return staffDepartmentMapper.deleteById(staffDepartment);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<StaffDepartment> select(Long userId, StaffDepartment staffDepartment) throws BusinessException {
		return staffDepartmentMapper.select(staffDepartment);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<StaffDepartment> selectPageList(Long userId, StaffDepartment staffDepartment, QueryDto queryDto) throws BusinessException {
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<StaffDepartment> page = staffDepartmentMapper.selectPageList(staffDepartment, queryDto);
		List<Long> idList = new ArrayList<Long>();
		for (StaffDepartment item : page.getResult()) {
			idList.add(item.getUpdateUser());
			idList.add(item.getCreateUser());
		}
		if (idList.size() > 0) {
			idList = idList.stream().distinct().collect(Collectors.toList());
			List<UserAccount> userList = userAccountMapper.selectByIdList(idList);
			Map<Long, UserAccount> accoutMap = userList.stream().collect(Collectors.toMap(UserAccount::getPkId, t -> t, (k1, k2) -> k1));
			for (StaffDepartment item : page.getResult()) {
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
		return new Pagination<StaffDepartment>(page.getTotal(), page.getResult());
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public StaffDepartment selectByCode(Long userId, String departmentCode) throws BusinessException {
		return staffDepartmentMapper.selectByCode(departmentCode);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer staffDepartmentImport(Long userId, List<StaffDepartment> staffDepartmentList) throws BusinessException {
		Date date = new Date();
		for (StaffDepartment item : staffDepartmentList) {
			StaffDepartment department = staffDepartmentMapper.selectByName(item.getDepartmentName(), null);
			if (null != department) {
				department.setDepartmentCode(item.getDepartmentCode());
				staffDepartmentMapper.updateActiveById(department, department.getPkId());
			} else {
				item.setPkId(getPkId());
				item.setCreateTime(date);
				item.setCreateUser(userId);
				item.setUpdateTime(date);
				item.setUpdateUser(userId);
				item.setDelMark(0);
				return staffDepartmentMapper.insert(item);
			}
		}
		return 1;
	}
}