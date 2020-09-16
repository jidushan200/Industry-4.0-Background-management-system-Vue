package org.tsinghuatj.sys.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.service.impl.BaseServiceImpl;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.sys.domain.AuthDto;
import org.tsinghuatj.sys.domain.SysRoleAuth;
import org.tsinghuatj.sys.domain.SysRoleInfo;
import org.tsinghuatj.sys.repository.SysRoleAuthMapper;
import org.tsinghuatj.sys.repository.SysRoleInfoMapper;
import org.tsinghuatj.sys.service.ISysRoleAuthService;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 *
 * SysRoleAuth 表数据服务层接口实现类
 *
 */
@Service("sysRoleAuthService")
public class SysRoleAuthServiceImpl extends BaseServiceImpl implements ISysRoleAuthService {

	private @Resource SysRoleAuthMapper sysRoleAuthMapper;

	private @Resource SysRoleInfoMapper sysRoleInfoMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, SysRoleAuth sysRoleAuth) throws BusinessException {
		sysRoleAuth.setPkId(getPkId());
		sysRoleAuth.setCreateTime(new Date());
		sysRoleAuth.setCreateUser(userId);
		sysRoleAuth.setUpdateTime(new Date());
		sysRoleAuth.setUpdateUser(userId);
		sysRoleAuth.setDelMark(0);
		return sysRoleAuthMapper.insert(sysRoleAuth);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, SysRoleAuth sysRoleAuth, Long sysRoleAuthId) throws BusinessException {
		sysRoleAuth.setUpdateTime(new Date());
		sysRoleAuth.setUpdateUser(userId);
		return sysRoleAuthMapper.updateActiveById(sysRoleAuth, sysRoleAuthId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public SysRoleAuth selectById(Long userId, Long sysRoleAuthId) throws BusinessException {
		return sysRoleAuthMapper.selectById(sysRoleAuthId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long sysRoleAuthId) throws BusinessException {
		return sysRoleAuthMapper.removeById(sysRoleAuthId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long sysRoleAuthId) throws BusinessException {
		SysRoleAuth sysRoleAuth = new SysRoleAuth();
		sysRoleAuth.setPkId(sysRoleAuthId);
		sysRoleAuth.setUpdateTime(new Date());
		sysRoleAuth.setUpdateUser(userId);
		return sysRoleAuthMapper.deleteById(sysRoleAuth);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<SysRoleAuth> select(Long userId, SysRoleAuth sysRoleAuth) throws BusinessException {
		return sysRoleAuthMapper.select(sysRoleAuth);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<SysRoleAuth> selectPageList(Long userId, SysRoleAuth sysRoleAuth, QueryDto queryDto)
			throws BusinessException {
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<SysRoleAuth> page = sysRoleAuthMapper.selectPageList(sysRoleAuth, queryDto);
		return new Pagination<SysRoleAuth>(page.getTotal(), page.getResult());
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<String> selectByRoleId(Long userId, Long sysRoleId) throws BusinessException {
		return sysRoleAuthMapper.selectByRoleId(sysRoleId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Integer batchSave(Long userId, AuthDto authDto) throws BusinessException {
		List<SysRoleAuth> roleAuthList = new ArrayList<SysRoleAuth>();
		authDto.getFullAuths().forEach(authCode -> {
			SysRoleAuth sysRoleAuth = new SysRoleAuth();
			try {
				sysRoleAuth.setPkId(getPkId());
			} catch (BusinessException e) {
				e.printStackTrace();
			}
			sysRoleAuth.setCreateUser(userId);
			sysRoleAuth.setCreateTime(new Date());
			sysRoleAuth.setUpdateUser(userId);
			sysRoleAuth.setUpdateTime(new Date());
			sysRoleAuth.setDelMark(0);
			sysRoleAuth.setRoleId(authDto.getRoleId());
			sysRoleAuth.setAuthCode(authCode);
			roleAuthList.add(sysRoleAuth);
		});
		sysRoleAuthMapper.deleteByRoleId(authDto.getRoleId());
		sysRoleAuthMapper.batchInsert(roleAuthList);
		List<String> authList = authDto.getAuthCodes();
		String auths = "";
		if (null != authList && authList.size() > 0) {
			SysRoleInfo sysRoleInfo = new SysRoleInfo();
			sysRoleInfo.setPkId(authDto.getRoleId());

			for (String item : authList) {
				auths = auths + item + ",";
			}
			sysRoleInfo.setRoleAuth(auths);
		}
		sysRoleInfoMapper.updateRoleAuth(auths, authDto.getRoleId());
		return 1;
	}
}