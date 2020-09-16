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
import org.tsinghuatj.tool.domain.ToolHeadPart;
import org.tsinghuatj.tool.repository.ToolHeadPartMapper;
import org.tsinghuatj.tool.service.IToolHeadPartService;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 *
 * ToolHeadPart 表数据服务层接口实现类
 *
 */
@Service("toolHeadPartService")
public class ToolHeadPartServiceImpl extends BaseServiceImpl implements IToolHeadPartService {

	private @Resource ToolHeadPartMapper toolHeadPartMapper;
	private @Resource PartMapper partMapper;
	private @Resource UserAccountMapper userAccountMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, ToolHeadPart toolHeadPart) throws BusinessException {
		toolHeadPart.setPkId(getPkId());
		toolHeadPart.setCreateTime(new Date());
		toolHeadPart.setCreateUser(userId);
		toolHeadPart.setUpdateTime(new Date());
		toolHeadPart.setUpdateUser(userId);
		toolHeadPart.setDelMark(0);
		ToolHeadPart headPart = toolHeadPartMapper.selectByHeadPart(toolHeadPart.getHeadNumber(), toolHeadPart.getPartCode(), null);
		if (null != headPart) {
			throw new BusinessException("head.part.exist.error");
		}
		return toolHeadPartMapper.insert(toolHeadPart);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, ToolHeadPart toolHeadPart, Long toolHeadPartId) throws BusinessException {
		toolHeadPart.setUpdateTime(new Date());
		toolHeadPart.setUpdateUser(userId);
		ToolHeadPart headPart = toolHeadPartMapper.selectByHeadPart(toolHeadPart.getHeadNumber(), toolHeadPart.getPartCode(), toolHeadPartId);
		if (null != headPart) {
			throw new BusinessException("head.part.exist.error");
		}
		return toolHeadPartMapper.updateActiveById(toolHeadPart, toolHeadPartId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public ToolHeadPart selectById(Long userId, Long toolHeadPartId) throws BusinessException {
		return toolHeadPartMapper.selectById(toolHeadPartId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long toolHeadPartId) throws BusinessException {
		return toolHeadPartMapper.removeById(toolHeadPartId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long toolHeadPartId) throws BusinessException {
		ToolHeadPart toolHeadPart = new ToolHeadPart();
		toolHeadPart.setPkId(toolHeadPartId);
		toolHeadPart.setUpdateTime(new Date());
		toolHeadPart.setUpdateUser(userId);
		return toolHeadPartMapper.deleteById(toolHeadPart);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<ToolHeadPart> select(Long userId, ToolHeadPart toolHeadPart) throws BusinessException {
		return toolHeadPartMapper.select(toolHeadPart);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<ToolHeadPart> selectPageList(Long userId, ToolHeadPart toolHeadPart, QueryDto queryDto) throws BusinessException {
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<ToolHeadPart> page = toolHeadPartMapper.selectPageList(toolHeadPart, queryDto);
		List<Long> idList = new ArrayList<Long>();
		for (ToolHeadPart item : page.getResult()) {
			idList.add(item.getUpdateUser());
			idList.add(item.getCreateUser());
		}
		if (idList.size() > 0) {
			idList = idList.stream().distinct().collect(Collectors.toList());
			List<UserAccount> userList = userAccountMapper.selectByIdList(idList);
			Map<Long, UserAccount> accoutMap = userList.stream().collect(Collectors.toMap(UserAccount::getPkId, t -> t, (k1, k2) -> k1));
			for (ToolHeadPart item : page.getResult()) {
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
		return new Pagination<ToolHeadPart>(page.getTotal(), page.getResult());
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer headPartImport(Long userId, List<ToolHeadPart> headPartList) throws BusinessException {
		for (ToolHeadPart hp : headPartList) {
			Part part = partMapper.selectByPartCode(hp.getPartCode(), null);
			if (null == part) {
				continue;
			}
			ToolHeadPart headPart = toolHeadPartMapper.selectByHeadPart(hp.getHeadNumber(), hp.getPartCode(), null);
			if (null != headPart) {
				toolHeadPartMapper.updateActiveById(headPart, headPart.getPkId());
			} else {
				hp.setPkId(getPkId());
				hp.setCreateTime(new Date());
				hp.setCreateUser(userId);
				hp.setUpdateTime(new Date());
				hp.setUpdateUser(userId);
				hp.setDelMark(0);
				toolHeadPartMapper.insert(hp);
			}
		}
		return 1;
	}

}