package org.tsinghuatj.tool.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.service.impl.BaseServiceImpl;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.sys.domain.UserAccount;
import org.tsinghuatj.sys.repository.UserAccountMapper;
import org.tsinghuatj.tool.domain.ToolBase;
import org.tsinghuatj.tool.domain.ToolHeadBlade;
import org.tsinghuatj.tool.repository.ToolBaseMapper;
import org.tsinghuatj.tool.repository.ToolHeadBladeMapper;
import org.tsinghuatj.tool.service.IToolHeadBladeService;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 *
 * ToolHeadBlade 表数据服务层接口实现类
 *
 */
@Service("toolHeadBladeService")
public class ToolHeadBladeServiceImpl extends BaseServiceImpl implements IToolHeadBladeService {

	private @Resource ToolHeadBladeMapper toolHeadBladeMapper;
	private @Resource ToolBaseMapper toolbaseMapper;
	private @Resource UserAccountMapper userAccountMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, ToolHeadBlade toolHeadBlade) throws BusinessException {
		toolHeadBlade.setPkId(getPkId());
		toolHeadBlade.setCreateTime(new Date());
		toolHeadBlade.setCreateUser(userId);
		toolHeadBlade.setUpdateTime(new Date());
		toolHeadBlade.setUpdateUser(userId);
		toolHeadBlade.setDelMark(0);
		ToolHeadBlade headBlade = toolHeadBladeMapper.selectByToolHead(toolHeadBlade.getHeadNumber(), toolHeadBlade.getToolNumber(), null);
		if (null != headBlade) {
			throw new BusinessException("head.blade.exist.error");
		}
		return toolHeadBladeMapper.insert(toolHeadBlade);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, ToolHeadBlade toolHeadBlade, Long toolHeadBladeId) throws BusinessException {
		toolHeadBlade.setUpdateTime(new Date());
		toolHeadBlade.setUpdateUser(userId);
		ToolHeadBlade headBlade = toolHeadBladeMapper.selectByToolHead(toolHeadBlade.getHeadNumber(), toolHeadBlade.getToolNumber(), toolHeadBladeId);
		if (null != headBlade) {
			throw new BusinessException("head.blade.exist.error");
		}
		return toolHeadBladeMapper.updateActiveById(toolHeadBlade, toolHeadBladeId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public ToolHeadBlade selectById(Long userId, Long toolHeadBladeId) throws BusinessException {
		return toolHeadBladeMapper.selectById(toolHeadBladeId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long toolHeadBladeId) throws BusinessException {
		return toolHeadBladeMapper.removeById(toolHeadBladeId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long toolHeadBladeId) throws BusinessException {
		ToolHeadBlade toolHeadBlade = new ToolHeadBlade();
		toolHeadBlade.setPkId(toolHeadBladeId);
		toolHeadBlade.setUpdateTime(new Date());
		toolHeadBlade.setUpdateUser(userId);
		return toolHeadBladeMapper.deleteById(toolHeadBlade);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<ToolHeadBlade> select(Long userId, ToolHeadBlade toolHeadBlade) throws BusinessException {
		return toolHeadBladeMapper.select(toolHeadBlade);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<ToolHeadBlade> selectPageList(Long userId, ToolHeadBlade toolHeadBlade, QueryDto queryDto) throws BusinessException {
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<ToolHeadBlade> page = toolHeadBladeMapper.selectPageList(toolHeadBlade, queryDto);
		List<Long> idList = new ArrayList<Long>();
		for (ToolHeadBlade item : page.getResult()) {
			idList.add(item.getUpdateUser());
			idList.add(item.getCreateUser());
		}
		if (idList.size() > 0) {
			idList = idList.stream().distinct().collect(Collectors.toList());
			List<UserAccount> userList = userAccountMapper.selectByIdList(idList);
			Map<Long, UserAccount> accoutMap = userList.stream().collect(Collectors.toMap(UserAccount::getPkId, t -> t, (k1, k2) -> k1));
			for (ToolHeadBlade item : page.getResult()) {
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
		return new Pagination<ToolHeadBlade>(page.getTotal(), page.getResult());
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer headBladeImport(Long userId, List<ToolHeadBlade> headBladeList) throws BusinessException {
		for (ToolHeadBlade hb : headBladeList) {
			if (null == hb.getToolQty()) {
				continue;
			}
			ToolBase toolbase = toolbaseMapper.selectByToolNumber(hb.getToolNumber(), null);
			if (null == toolbase) {
				continue;
			}
			ToolHeadBlade headBlade = toolHeadBladeMapper.selectByToolHead(hb.getHeadNumber(), hb.getToolNumber(), null);
			if (null != headBlade) {
				if (headBlade.getToolQty().equals(hb.getToolQty())) {
					continue;
				} else {
					headBlade.setToolQty(hb.getToolQty());
					headBlade.setEachProcessQty(hb.getEachProcessQty());
					headBlade.setProcessTimes(hb.getProcessTimes());
					toolHeadBladeMapper.updateActiveById(headBlade, headBlade.getPkId());
				}
			} else {
				hb.setPkId(getPkId());
				hb.setCreateTime(new Date());
				hb.setCreateUser(userId);
				hb.setUpdateTime(new Date());
				hb.setUpdateUser(userId);
				hb.setDelMark(0);
				toolHeadBladeMapper.insert(hb);
			}
		}
		return 1;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<ToolHeadBlade> getComplexheadBladeList(Long userId, ToolHeadBlade toolHeadBlade) throws BusinessException {

		return toolHeadBladeMapper.selectComplexheadBladeList(toolHeadBlade);
	}

}