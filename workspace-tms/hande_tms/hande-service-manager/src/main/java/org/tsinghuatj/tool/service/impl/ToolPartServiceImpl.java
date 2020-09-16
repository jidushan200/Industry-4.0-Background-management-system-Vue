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
import org.tsinghuatj.base.domain.Part;
import org.tsinghuatj.base.repository.PartMapper;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.service.impl.BaseServiceImpl;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.sys.domain.UserAccount;
import org.tsinghuatj.sys.repository.UserAccountMapper;
import org.tsinghuatj.tool.domain.ToolBase;
import org.tsinghuatj.tool.domain.ToolPart;
import org.tsinghuatj.tool.repository.ToolBaseMapper;
import org.tsinghuatj.tool.repository.ToolPartMapper;
import org.tsinghuatj.tool.service.IToolPartService;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 *
 * ToolPart 表数据服务层接口实现类
 *
 */
@Service("toolPartService")
public class ToolPartServiceImpl extends BaseServiceImpl implements IToolPartService {
	private @Resource ToolPartMapper toolPartMapper;
	private @Resource ToolBaseMapper toolbaseMapper;
	private @Resource PartMapper partMapper;
	private @Resource UserAccountMapper userAccountMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, ToolPart toolPart) throws BusinessException {
		toolPart.setPkId(getPkId());
		toolPart.setCreateTime(new Date());
		toolPart.setCreateUser(userId);
		toolPart.setUpdateTime(new Date());
		toolPart.setUpdateUser(userId);
		toolPart.setDelMark(0);
		return toolPartMapper.insert(toolPart);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, ToolPart toolPart, Long toolPartId) throws BusinessException {
		toolPart.setUpdateTime(new Date());
		toolPart.setUpdateUser(userId);
		return toolPartMapper.updateActiveById(toolPart, toolPartId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public ToolPart selectById(Long userId, Long toolPartId) throws BusinessException {
		return toolPartMapper.selectById(toolPartId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long toolPartId) throws BusinessException {
		return toolPartMapper.removeById(toolPartId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long toolPartId) throws BusinessException {
		ToolPart toolPart = new ToolPart();
		toolPart.setPkId(toolPartId);
		toolPart.setUpdateTime(new Date());
		toolPart.setUpdateUser(userId);
		return toolPartMapper.deleteById(toolPart);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<ToolPart> select(Long userId, ToolPart toolPart) throws BusinessException {
		return toolPartMapper.select(toolPart);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<ToolPart> selectPageList(Long userId, ToolPart toolPart, QueryDto queryDto) throws BusinessException {
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<ToolPart> page = toolPartMapper.selectPageList(toolPart, queryDto);
		List<Long> idList = new ArrayList<Long>();
		for (ToolPart item : page.getResult()) {
			idList.add(item.getUpdateUser());
			idList.add(item.getCreateUser());
		}
		if (idList.size() > 0) {
			idList = idList.stream().distinct().collect(Collectors.toList());
			List<UserAccount> userList = userAccountMapper.selectByIdList(idList);
			Map<Long, UserAccount> accoutMap = userList.stream().collect(Collectors.toMap(UserAccount::getPkId, t -> t, (k1, k2) -> k1));
			for (ToolPart item : page.getResult()) {
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
		return new Pagination<ToolPart>(page.getTotal(), page.getResult());
	}

	@Override
	public boolean checkToolPart(Long pkId, Long toolId, Long partId) throws BusinessException {
		ToolPart toolPart = toolPartMapper.checkToolPart(pkId, toolId, partId);
		if (null != toolPart) {
			throw new BusinessException("toolpart.exists.error");
		}
		return true;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer toolPartImport(Long userId, List<ToolPart> tpList) throws BusinessException {
		for (ToolPart tp : tpList) {
			ToolBase toolbase = toolbaseMapper.selectByToolNumber(tp.getToolNumber(), null);
			if (null == toolbase) {
				continue;
			}
			Part part = partMapper.selectByPartCode(tp.getPartCode(), null);
			if (null == part) {
				continue;
			}
			ToolPart toolPart = toolPartMapper.checkToolPart(null, toolbase.getPkId(), part.getPkId());
			if(null!=toolPart){
				continue;
			}
			tp.setPartId(part.getPkId());
			tp.setToolId(toolbase.getPkId());
			tp.setPkId(getPkId());
			tp.setCreateTime(new Date());
			tp.setCreateUser(userId);
			tp.setUpdateTime(new Date());
			tp.setUpdateUser(userId);
			tp.setDelMark(0);
			toolPartMapper.insert(tp);
		}
		return 1;
	}
}