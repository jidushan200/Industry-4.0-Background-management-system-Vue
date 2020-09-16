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
import org.tsinghuatj.sys.domain.SysAuthInfo;
import org.tsinghuatj.sys.repository.LoginMapper;
import org.tsinghuatj.sys.repository.SysAuthInfoMapper;
import org.tsinghuatj.sys.service.ISysAuthInfoService;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 *
 * SysAuthInfo 表数据服务层接口实现类
 *
 */
@Service("sysAuthInfoService")
public class SysAuthInfoServiceImpl extends BaseServiceImpl implements ISysAuthInfoService {

	private @Resource SysAuthInfoMapper sysAuthInfoMapper;
	
	private @Resource LoginMapper loginMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, SysAuthInfo sysAuthInfo) throws BusinessException {
		sysAuthInfo.setPkId(getPkId());
		sysAuthInfo.setCreateTime(new Date());
		sysAuthInfo.setCreateUser(userId);
		sysAuthInfo.setUpdateTime(new Date());
		sysAuthInfo.setUpdateUser(userId);
		sysAuthInfo.setDelMark(0);
		return sysAuthInfoMapper.insert(sysAuthInfo);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, SysAuthInfo sysAuthInfo, Long sysAuthInfoId) throws BusinessException {
		sysAuthInfo.setUpdateTime(new Date());
		sysAuthInfo.setUpdateUser(userId);
		return sysAuthInfoMapper.updateActiveById(sysAuthInfo, sysAuthInfoId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public SysAuthInfo selectById(Long userId, Long sysAuthInfoId) throws BusinessException {
		List<SysAuthInfo> list = sysAuthInfoMapper.select(new SysAuthInfo());		
		return recursiveTree(sysAuthInfoId, list);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long sysAuthInfoId) throws BusinessException {
		return sysAuthInfoMapper.removeById(sysAuthInfoId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long sysAuthInfoId) throws BusinessException {
		SysAuthInfo sysAuthInfo = new SysAuthInfo();
		sysAuthInfo.setPkId(sysAuthInfoId);
		sysAuthInfo.setUpdateTime(new Date());
		sysAuthInfo.setUpdateUser(userId);
		return sysAuthInfoMapper.deleteById(sysAuthInfo);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<SysAuthInfo> select(Long userId, SysAuthInfo sysAuthInfo) throws BusinessException {
		List<SysAuthInfo> list = sysAuthInfoMapper.select(sysAuthInfo);
		return list;
	}

	private SysAuthInfo recursiveTree(Long pkId, List<SysAuthInfo> list) {
		SysAuthInfo node = getSysAuthInfoById(pkId, list);
		List<SysAuthInfo> childRen = new ArrayList<SysAuthInfo>();
		List<SysAuthInfo> childTreeNodes = getChildTreeById(pkId, list);
		if (null != childTreeNodes && childTreeNodes.size() > 0) {
			node.setExpand(true);
			for (SysAuthInfo child : childTreeNodes) {
				SysAuthInfo n = recursiveTree(child.getPkId(), list);
				childRen.add(n);
				node.setChildren(childRen);
			}
		} else {
			node.setExpand(false);
		}
		return node;
	}

	private SysAuthInfo getSysAuthInfoById(long pkId, List<SysAuthInfo> list) {
		for (SysAuthInfo item : list) {
			if (item.getPkId().equals(pkId)) {
				return item;
			}
		}
		return null;
	}

	private List<SysAuthInfo> getChildTreeById(long pkId, List<SysAuthInfo> list) {
		List<SysAuthInfo> l = new ArrayList<SysAuthInfo>();
		for (SysAuthInfo item : list) {
			if (item.getParentId().equals(pkId)) {
				l.add(item);
			}
		}
		return l;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<SysAuthInfo> selectPageList(Long userId, SysAuthInfo sysAuthInfo, QueryDto queryDto)
			throws BusinessException {
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<SysAuthInfo> page = sysAuthInfoMapper.selectPageList(sysAuthInfo, queryDto);
		return new Pagination<SysAuthInfo>(page.getTotal(), page.getResult());
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public SysAuthInfo selectAuthTreeByRoleId(Long userId, Long sysRoleId) throws BusinessException {
		List<SysAuthInfo> list = sysAuthInfoMapper.selectByRoleId(sysRoleId);
		
		if (null != list && list.size()>0) {
			return recursiveTree(1l, list);
		} else
			return null;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public List<String> selectAuthByUserId(Long userId, Long sysUserId) throws BusinessException {
		// TODO Auto-generated method stub
		return loginMapper.listUserAuths(sysUserId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public int batchInsert(Long userId, List<SysAuthInfo> authList) throws BusinessException {
		for (SysAuthInfo sysAuth : authList) {
			if (null == sysAuth.getAuthorCode() || "".equals(sysAuth.getAuthorCode())) {
				continue;
			}
			if (sysAuth.getPkId() == null || sysAuth.getPkId() <= 0) {
				sysAuth.setPkId(getPkId());
				sysAuth.setCreateTime(new Date());
				sysAuth.setCreateUser(userId);
				sysAuth.setUpdateTime(new Date());
				sysAuth.setUpdateUser(userId);
				sysAuth.setDelMark(0);
			}

			if ("01".equals(sysAuth.getAuthorCode())) {
				sysAuth.setParentId(0L);
			} else {
				for (SysAuthInfo auth : authList) {
					if (sysAuth.getParentCode().equals(auth.getAuthorCode())) {
						sysAuth.setParentId(auth.getPkId());
						break;
					}
				}
			}
			sysAuthInfoMapper.insert(sysAuth);
		}
		// sysAuthInfoMapper.batchInsert(authList);
		return 1;
	}
}