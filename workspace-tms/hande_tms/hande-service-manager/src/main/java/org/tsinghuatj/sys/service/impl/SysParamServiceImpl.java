package org.tsinghuatj.sys.service.impl;

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
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.service.impl.BaseServiceImpl;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.sys.domain.SysParam;
import org.tsinghuatj.sys.domain.UserAccount;
import org.tsinghuatj.sys.repository.SysParamMapper;
import org.tsinghuatj.sys.repository.UserAccountMapper;
import org.tsinghuatj.sys.service.ISysParamService;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 *
 * SysParam 表数据服务层接口实现类
 *
 */
@Service("sysParamService")
public class SysParamServiceImpl extends BaseServiceImpl implements ISysParamService {
	private @Resource SysParamMapper sysParamMapper;
	private @Resource StaffDepartmentMapper staffDepartmentMapper;
	private @Resource UserAccountMapper userAccountMapper;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, SysParam sysParam) throws BusinessException {
		sysParam.setPkId(getPkId());
		sysParam.setCreateTime(new Date());
		sysParam.setCreateUser(userId);
		sysParam.setUpdateTime(new Date());
		sysParam.setUpdateUser(userId);
		sysParam.setDelMark(0);
		return sysParamMapper.insert(sysParam);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, SysParam sysParam, Long sysParamId) throws BusinessException {
		sysParam.setUpdateTime(new Date());
		sysParam.setUpdateUser(userId);
		return sysParamMapper.updateActiveById(sysParam, sysParamId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public SysParam selectById(Long userId, Long sysParamId) throws BusinessException {
		return sysParamMapper.selectById(sysParamId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long sysParamId) throws BusinessException {
		return sysParamMapper.removeById(sysParamId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long sysParamId) throws BusinessException {
		SysParam sysParam = new SysParam();
		sysParam.setPkId(sysParamId);
		sysParam.setUpdateTime(new Date());
		sysParam.setUpdateUser(userId);
		return sysParamMapper.deleteById(sysParam);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<SysParam> select(Long userId, SysParam sysParam) throws BusinessException {
		return sysParamMapper.select(sysParam);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<SysParam> selectPageList(Long userId, SysParam sysParam, QueryDto queryDto) throws BusinessException {
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<SysParam> page = sysParamMapper.selectPageList(sysParam, queryDto);
		List<Long> idList = new ArrayList<Long>();
		for (SysParam item : page.getResult()) {
			idList.add(item.getUpdateUser());
			idList.add(item.getCreateUser());
		}
		if (idList.size() > 0) {
			idList = idList.stream().distinct().collect(Collectors.toList());
			List<UserAccount> userList = userAccountMapper.selectByIdList(idList);
			Map<Long, UserAccount> accoutMap = userList.stream().collect(Collectors.toMap(UserAccount::getPkId, t -> t, (k1, k2) -> k1));
			for (SysParam item : page.getResult()) {
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
		return new Pagination<SysParam>(page.getTotal(), page.getResult());
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<Long> selectDepartMentByParamKey(Long userId, String paramKey) throws BusinessException {
		SysParam param = sysParamMapper.selectByParamKey(paramKey);
		if (null != param) {
			List<Long> list = new ArrayList<Long>();
			String[] nameArray = param.getParamValue().split("、");
			List<StaffDepartment> dlist = staffDepartmentMapper.selectByNameList(java.util.Arrays.asList(nameArray));
			for (StaffDepartment depart : dlist) {
				list.add(depart.getPkId());
			}
			return list;
		}
		return null;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public SysParam selectByParamKey(Long userId, String paramKey) throws BusinessException {
		// TODO Auto-generated method stub
		return sysParamMapper.selectByParamKey(paramKey);
	}
}