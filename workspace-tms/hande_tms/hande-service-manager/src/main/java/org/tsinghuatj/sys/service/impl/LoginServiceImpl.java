package org.tsinghuatj.sys.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.tsinghuatj.framework.service.impl.BaseServiceImpl;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.sys.domain.OperationLog;
import org.tsinghuatj.sys.domain.UserAccount;
import org.tsinghuatj.sys.repository.LoginMapper;
import org.tsinghuatj.sys.repository.OperationLogMapper;
import org.tsinghuatj.sys.service.ILoginService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("loginService")
public class LoginServiceImpl extends BaseServiceImpl implements ILoginService {
	private @Resource LoginMapper loginMapper;
	private @Resource OperationLogMapper operationLogMapper;

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public UserAccount getUserInfoByName(String username) throws BusinessException {
		log.info("user.login:{}", username);
		UserAccount UserAccount = loginMapper.getUserInfoByName(username);
		return UserAccount;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)	
	public List<String> listAutoByUser(Long userId) throws BusinessException {		
		List<String> resources = loginMapper.listUserAuths(userId);		
		return resources;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public UserAccount getUserInfoById(Long pkId) throws BusinessException {
		return loginMapper.getUserInfoById(pkId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)	
	public Integer saveLoginlog(OperationLog log) throws BusinessException {	
		return operationLogMapper.insert(log);
	}
}
