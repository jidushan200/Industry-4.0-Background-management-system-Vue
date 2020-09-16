package org.tsinghuatj.framework.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.tsinghuatj.framework.service.IBaseService;
import org.tsinghuatj.framework.service.IdGenerator;
import org.tsinghuatj.framework.support.BusinessException;

@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public abstract class BaseServiceImpl implements IBaseService {
	@Autowired
	IdGenerator customIdGenerator;
	
	@Override
	public Long getPkId()throws BusinessException {		
		return customIdGenerator.getUId();		
	}
}
