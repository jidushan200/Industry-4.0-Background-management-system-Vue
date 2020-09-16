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
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.service.impl.BaseServiceImpl;
import org.tsinghuatj.framework.support.BusinessException;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.sys.domain.SysRoleInfo;
import org.tsinghuatj.sys.domain.UserAccount;
import org.tsinghuatj.sys.repository.SysRoleInfoMapper;
import org.tsinghuatj.sys.repository.UserAccountMapper;
import org.tsinghuatj.sys.service.ISysRoleInfoService;


/**
 *
 * SysRoleInfo 表数据服务层接口实现类
 *
 */
@Service("sysRoleInfoService")
public class SysRoleInfoServiceImpl extends BaseServiceImpl implements ISysRoleInfoService {
	private @Resource UserAccountMapper userAccountMapper;
	private @Resource SysRoleInfoMapper sysRoleInfoMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, SysRoleInfo sysRoleInfo) throws BusinessException {	
		sysRoleInfo.setPkId(getPkId());
		sysRoleInfo.setCreateTime(new Date());
		sysRoleInfo.setCreateUser(userId);
		sysRoleInfo.setUpdateTime(new Date());
		sysRoleInfo.setUpdateUser(userId);
		sysRoleInfo.setDelMark(0);
		return sysRoleInfoMapper.insert(sysRoleInfo);
	}
				
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, SysRoleInfo sysRoleInfo, Long sysRoleInfoId) throws BusinessException {
		sysRoleInfo.setUpdateTime(new Date());
		sysRoleInfo.setUpdateUser(userId);
		return sysRoleInfoMapper.updateActiveById(sysRoleInfo, sysRoleInfoId);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public SysRoleInfo selectById(Long userId, Long sysRoleInfoId) throws BusinessException {
		return sysRoleInfoMapper.selectById(sysRoleInfoId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long sysRoleInfoId) throws BusinessException {
		return sysRoleInfoMapper.removeById(sysRoleInfoId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long sysRoleInfoId) throws BusinessException {
		int count = sysRoleInfoMapper.selectUserCountByRoleId(sysRoleInfoId);
		if(count>0){
			throw new BusinessException("useraccount.exists.error");
		}
		SysRoleInfo sysRoleInfo = new SysRoleInfo();
		sysRoleInfo.setPkId(sysRoleInfoId);
		sysRoleInfo.setUpdateTime(new Date());
		sysRoleInfo.setUpdateUser(userId);
		return sysRoleInfoMapper.deleteById(sysRoleInfo);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<SysRoleInfo> select(Long userId, SysRoleInfo sysRoleInfo) throws BusinessException {		
		return sysRoleInfoMapper.select(sysRoleInfo);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<SysRoleInfo> selectPageList(Long userId, SysRoleInfo sysRoleInfo,QueryDto queryDto) throws BusinessException {		
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<SysRoleInfo> page = sysRoleInfoMapper.selectPageList(sysRoleInfo, queryDto);
		List<Long> idList = new ArrayList<Long>();
		for (SysRoleInfo item : page.getResult()) {
			idList.add(item.getUpdateUser());
			idList.add(item.getCreateUser());
		}
		if (idList.size() > 0) {
			idList = idList.stream().distinct().collect(Collectors.toList());
			List<UserAccount> userList = userAccountMapper.selectByIdList(idList);
			Map<Long, UserAccount> accoutMap = userList.stream().collect(Collectors.toMap(UserAccount::getPkId, t -> t, (k1, k2) -> k1));
			for (SysRoleInfo item : page.getResult()) {
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
		return new Pagination<SysRoleInfo>(page.getTotal(), page.getResult());		
	}
}