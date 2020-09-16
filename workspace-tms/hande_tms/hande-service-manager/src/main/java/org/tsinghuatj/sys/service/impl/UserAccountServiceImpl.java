package org.tsinghuatj.sys.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.service.impl.BaseServiceImpl;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.sys.domain.AuthDto;
import org.tsinghuatj.sys.domain.SysUserAuth;
import org.tsinghuatj.sys.domain.UserAccount;
import org.tsinghuatj.sys.repository.SysAuthInfoMapper;
import org.tsinghuatj.sys.repository.SysUserAuthMapper;
import org.tsinghuatj.sys.repository.UserAccountMapper;
import org.tsinghuatj.sys.service.IUserAccountService;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 *
 * UserAccount 表数据服务层接口实现类
 *
 */
@Service("userAccountService")
public class UserAccountServiceImpl extends BaseServiceImpl implements IUserAccountService {
	private @Resource UserAccountMapper userAccountMapper;
	private @Resource SysUserAuthMapper sysUserAuthMapper;
	private @Resource SysAuthInfoMapper sysAuthInfoMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, UserAccount userAccount, List<String> authCodes) throws BusinessException {
		long pkId = getPkId();
		userAccount.setPkId(pkId);
		String loginPwd = "";
		Date date = new Date();
		PasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
		loginPwd = bcryptPasswordEncoder.encode(userAccount.getLoginPwd());
		userAccount.setLoginPwd(loginPwd);
		userAccount.setCreateTime(date);
		userAccount.setCreateUser(userId);
		userAccount.setUpdateTime(date);
		userAccount.setUpdateUser(userId);
		userAccount.setDelMark(0);
		userAccountMapper.insert(userAccount);
		if (CollectionUtils.isNotEmpty(authCodes)) {
			AuthDto authDto = new AuthDto();
			authDto.setAuthCodes(authCodes);
			List<SysUserAuth> list = new ArrayList<SysUserAuth>();
			authDto.getAuthCodes().forEach(authCode -> {
				SysUserAuth sysUserAuth = new SysUserAuth();
				try {
					sysUserAuth.setPkId(getPkId());
				} catch (BusinessException e) {
					e.printStackTrace();
				}
				sysUserAuth.setAuthCode(authCode);
				// 获取要保存的用户pkid
				sysUserAuth.setUserId(pkId);
				sysUserAuth.setCreateUser(userId);
				sysUserAuth.setCreateTime(date);
				sysUserAuth.setUpdateUser(userId);
				sysUserAuth.setUpdateTime(date);
				sysUserAuth.setDelMark(0);
				list.add(sysUserAuth);
			});
			sysUserAuthMapper.batchInsert(list);
		}
		return 1;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, UserAccount userAccount, Long userAccountId, List<String> authCodes) throws BusinessException {

		Date date = new Date();
		userAccount.setUpdateTime(new Date());
		userAccount.setUpdateUser(userId);
		if (StringUtils.isNoneBlank(userAccount.getLoginPwd())) {
			PasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
			String loginPwd = bcryptPasswordEncoder.encode(userAccount.getLoginPwd());
			userAccount.setLoginPwd(loginPwd);
		}
		userAccountMapper.updateActiveById(userAccount, userAccountId);
		if (CollectionUtils.isNotEmpty(authCodes)) {
			AuthDto authDto = new AuthDto();
			authDto.setAuthCodes(authCodes);
			List<SysUserAuth> list = new ArrayList<SysUserAuth>();
			authDto.getAuthCodes().forEach(authCode -> {
				SysUserAuth sysUserAuth = new SysUserAuth();
				try {
					sysUserAuth.setPkId(getPkId());
				} catch (BusinessException e) {
					e.printStackTrace();
				}
				sysUserAuth.setAuthCode(authCode);
				// 获取要保存的用户pkid
				sysUserAuth.setUserId(userAccountId);
				sysUserAuth.setCreateUser(userId);
				sysUserAuth.setCreateTime(date);
				sysUserAuth.setUpdateUser(userId);
				sysUserAuth.setUpdateTime(date);
				sysUserAuth.setDelMark(0);
				list.add(sysUserAuth);
			});
			sysUserAuthMapper.deleteByUserId(userAccountId);
			sysUserAuthMapper.batchInsert(list);
		}
		return 1;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public UserAccount selectById(Long userId, Long userAccountId) throws BusinessException {
		return userAccountMapper.selectById(userAccountId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long userAccountId) throws BusinessException {
		return userAccountMapper.removeById(userAccountId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long userAccountId) throws BusinessException {
		UserAccount userAccount = new UserAccount();
		userAccount.setPkId(userAccountId);
		userAccount.setUpdateTime(new Date());
		userAccount.setUpdateUser(userId);
		sysUserAuthMapper.deleteByUserId(userAccountId);
		return userAccountMapper.deleteById(userAccount);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<UserAccount> select(Long userId, UserAccount userAccount) throws BusinessException {
		return userAccountMapper.select(userAccount);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<UserAccount> selectPageList(Long userId, UserAccount userAccount, QueryDto queryDto) throws BusinessException {
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<UserAccount> page = userAccountMapper.selectPageList(userAccount, queryDto);
		List<Long> idList = new ArrayList<Long>();
		for (UserAccount item : page.getResult()) {
			idList.add(item.getUpdateUser());
			idList.add(item.getCreateUser());
		}
		if (idList.size() > 0) {
			idList = idList.stream().distinct().collect(Collectors.toList());
			List<UserAccount> userList = userAccountMapper.selectByIdList(idList);
			Map<Long, UserAccount> accoutMap = userList.stream().collect(Collectors.toMap(UserAccount::getPkId, t -> t, (k1, k2) -> k1));
			for (UserAccount item : page.getResult()) {
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
		return new Pagination<UserAccount>(page.getTotal(), page.getResult());
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Integer batchDeleteAccountByIds(Long userId, String ids) throws BusinessException {
		// TODO Auto-generated method stub
		String[] arr = ids.split(",");
		Long id;
		List<Long> idGroup = new ArrayList<Long>();
		for (String str : arr) {
			id = Long.parseLong(str);
			idGroup.add(id);
			sysUserAuthMapper.deleteByUserId(id);
		}
		userAccountMapper.batchDeleteAccountByIds(idGroup);
		return 1;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<Long> getUserIdByAuthCode(Long userId, String authCode) throws BusinessException {
		return sysUserAuthMapper.selectUserIdByAuthCode(authCode);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public boolean checkLoginName(String loginName, Long pkId) throws BusinessException {
		UserAccount userAccount = userAccountMapper.checkLoginName(loginName, pkId);
		if (null != userAccount) {
			throw new BusinessException("loginname.exists.error");
		}
		return true;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<UserAccount> selectCompletePageList(Long userId, UserAccount userAccount) throws BusinessException {
		List<UserAccount> list = userAccountMapper.select(userAccount);
		for (UserAccount ua : list) {
			ua.setAuthList(sysAuthInfoMapper.selectByUserId(ua.getPkId()));
		}
		return list;
	}
}