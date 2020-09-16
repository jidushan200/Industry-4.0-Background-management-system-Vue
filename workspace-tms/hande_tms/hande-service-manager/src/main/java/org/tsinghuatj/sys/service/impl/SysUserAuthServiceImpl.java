package org.tsinghuatj.sys.service.impl;

import java.util.Date;
import java.util.List;

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
import org.tsinghuatj.sys.domain.SysUserAuth;
import org.tsinghuatj.sys.repository.SysUserAuthMapper;
import org.tsinghuatj.sys.service.ISysUserAuthService;


/**
 *
 * SysUserAuth 表数据服务层接口实现类
 *
 */
@Service("sysUserAuthService")
public class SysUserAuthServiceImpl extends BaseServiceImpl implements ISysUserAuthService {

	private @Resource SysUserAuthMapper sysUserAuthMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, SysUserAuth sysUserAuth) throws BusinessException {	
		sysUserAuth.setPkId(getPkId());
		sysUserAuth.setCreateTime(new Date());
		sysUserAuth.setCreateUser(userId);
		sysUserAuth.setUpdateTime(new Date());
		sysUserAuth.setUpdateUser(userId);
		sysUserAuth.setDelMark(0);
		return sysUserAuthMapper.insert(sysUserAuth);
	}
				
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, SysUserAuth sysUserAuth, Long sysUserAuthId) throws BusinessException {
		sysUserAuth.setUpdateTime(new Date());
		sysUserAuth.setUpdateUser(userId);
		return sysUserAuthMapper.updateActiveById(sysUserAuth, sysUserAuthId);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public SysUserAuth selectById(Long userId, Long sysUserAuthId) throws BusinessException {
		return sysUserAuthMapper.selectById(sysUserAuthId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long sysUserAuthId) throws BusinessException {
		return sysUserAuthMapper.removeById(sysUserAuthId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long sysUserAuthId) throws BusinessException {
		SysUserAuth sysUserAuth = new SysUserAuth();
		sysUserAuth.setPkId(sysUserAuthId);
		sysUserAuth.setUpdateTime(new Date());
		sysUserAuth.setUpdateUser(userId);
		return sysUserAuthMapper.deleteById(sysUserAuth);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<SysUserAuth> select(Long userId, SysUserAuth sysUserAuth) throws BusinessException {		
		return sysUserAuthMapper.select(sysUserAuth);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<SysUserAuth> selectPageList(Long userId, SysUserAuth sysUserAuth,QueryDto queryDto) throws BusinessException {		
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<SysUserAuth> page = sysUserAuthMapper.selectPageList(sysUserAuth, queryDto);
		return new Pagination<SysUserAuth>(page.getTotal(), page.getResult());		
	}
}