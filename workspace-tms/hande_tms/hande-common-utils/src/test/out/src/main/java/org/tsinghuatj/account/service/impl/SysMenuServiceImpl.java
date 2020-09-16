package org.tsinghuatj.account.service.impl;

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
import org.tsinghuatj.account.domain.SysMenu;
import org.tsinghuatj.account.repository.SysMenuMapper;
import org.tsinghuatj.account.service.ISysMenuService;


/**
 *
 * SysMenu 表数据服务层接口实现类
 *
 */
@Service("sysMenuService")
public class SysMenuServiceImpl extends BaseServiceImpl implements ISysMenuService {

	private @Resource SysMenuMapper sysMenuMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, SysMenu sysMenu) throws BusinessException {		
		sysMenu.setCreateTime(new Date());
		sysMenu.setCreateUser(userId);
		sysMenu.setUpdateTime(new Date());
		sysMenu.setUpdateUser(userId);
		sysMenu.setDelMark(0);
		return sysMenuMapper.insert(sysMenu);
	}
				
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, SysMenu sysMenu, Long sysMenuId) throws BusinessException {
		sysMenu.setUpdateTime(new Date());
		sysMenu.setUpdateUser(userId);
		return sysMenuMapper.updateActiveById(sysMenu, sysMenuId);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public SysMenu selectById(Long userId, Long sysMenuId) throws BusinessException {
		return sysMenuMapper.selectById(sysMenuId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long sysMenuId) throws BusinessException {
		return sysMenuMapper.removeById(sysMenuId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long sysMenuId) throws BusinessException {
		return sysMenuMapper.deleteById(sysMenuId);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<SysMenu> select(Long userId, SysMenu sysMenu) throws BusinessException {		
		return sysMenuMapper.select(sysMenu);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<SysMenu> selectPageList(Long userId, SysMenu sysMenu,QueryDto queryDto) throws BusinessException {		
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<SysMenu> page = sysMenuMapper.selectPageList(sysMenu, queryDto);
		return new Pagination<SysMenu>(page.getTotal(), page.getResult());		
	}
}