package org.tsinghuatj.sys.service;

import java.util.List;

import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.sys.domain.OperationLog;
import org.tsinghuatj.sys.domain.UserAccount;

public interface ILoginService {
	UserAccount getUserInfoByName(String username) throws BusinessException;

	UserAccount getUserInfoById(Long pkId) throws BusinessException;

	List<String> listAutoByUser(Long userId) throws BusinessException;
	
	Integer saveLoginlog(OperationLog log)throws BusinessException;

}
