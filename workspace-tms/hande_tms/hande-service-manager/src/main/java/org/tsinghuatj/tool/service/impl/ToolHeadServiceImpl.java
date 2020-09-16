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
import org.tsinghuatj.tool.domain.ToolHead;
import org.tsinghuatj.tool.repository.ToolHeadMapper;
import org.tsinghuatj.tool.service.IToolHeadService;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 *
 * ToolHead 表数据服务层接口实现类
 *
 */
@Service("toolHeadService")
public class ToolHeadServiceImpl extends BaseServiceImpl implements IToolHeadService {
	private @Resource UserAccountMapper userAccountMapper;
	private @Resource ToolHeadMapper toolHeadMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, ToolHead toolHead) throws BusinessException {
		ToolHead old = toolHeadMapper.selectByHeadNumber(toolHead.getHeadNumber(), null);
		if(null!=old){
			throw new BusinessException("headnumber.exists.error");
		}
		toolHead.setPkId(getPkId());
		toolHead.setCreateTime(new Date());
		toolHead.setCreateUser(userId);
		toolHead.setUpdateTime(new Date());
		toolHead.setUpdateUser(userId);
		toolHead.setDelMark(0);
		return toolHeadMapper.insert(toolHead);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, ToolHead toolHead, Long toolHeadId) throws BusinessException {
		ToolHead old = toolHeadMapper.selectByHeadNumber(toolHead.getHeadNumber(), toolHeadId);
		if(null!=old){
			throw new BusinessException("headnumber.exists.error");
		}
		toolHead.setUpdateTime(new Date());
		toolHead.setUpdateUser(userId);
		return toolHeadMapper.updateActiveById(toolHead, toolHeadId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public ToolHead selectById(Long userId, Long toolHeadId) throws BusinessException {
		return toolHeadMapper.selectById(toolHeadId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long toolHeadId) throws BusinessException {
		return toolHeadMapper.removeById(toolHeadId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long toolHeadId) throws BusinessException {
		ToolHead toolHead = new ToolHead();
		toolHead.setPkId(toolHeadId);
		toolHead.setUpdateTime(new Date());
		toolHead.setUpdateUser(userId);
		return toolHeadMapper.deleteById(toolHead);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<ToolHead> select(Long userId, ToolHead toolHead) throws BusinessException {
		return toolHeadMapper.select(toolHead);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<ToolHead> selectPageList(Long userId, ToolHead toolHead, QueryDto queryDto) throws BusinessException {
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<ToolHead> page = toolHeadMapper.selectPageList(toolHead, queryDto);
		List<Long> idList = new ArrayList<Long>();
		for (ToolHead item : page.getResult()) {
			idList.add(item.getUpdateUser());
			idList.add(item.getCreateUser());
		}
		if (idList.size() > 0) {
			idList = idList.stream().distinct().collect(Collectors.toList());
			List<UserAccount> userList = userAccountMapper.selectByIdList(idList);
			Map<Long, UserAccount> accoutMap = userList.stream().collect(Collectors.toMap(UserAccount::getPkId, t -> t, (k1, k2) -> k1));
			for (ToolHead item : page.getResult()) {
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
		return new Pagination<ToolHead>(page.getTotal(), page.getResult());
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public boolean checkHeadNumber(String headNumber, Long pkId) throws BusinessException {
		ToolHead old = toolHeadMapper.selectByHeadNumber(headNumber, pkId);
		if(null!=old){
			throw new BusinessException("headnumber.exists.error");
		}
		return false;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer toolHeadImport(Long userId, List<ToolHead> headList) throws BusinessException {
		Date date = new Date();
		headList.forEach(th -> {
			ToolHead old = toolHeadMapper.selectByHeadNumber(th.getHeadNumber(), null);
			th.setUpdateTime(date);
			th.setUpdateUser(userId);
			if("三面刃".equals(th.getHeadTypeName())){
				th.setHeadType(3);
			}else if ("两面刃".equals(th.getHeadTypeName())){
				th.setHeadType(2);
			}
			if (null != old) {
				if(old.getHeadName().equals(th.getHeadName())){
					toolHeadMapper.updateActiveById(th, old.getPkId());
				}				
			} else {
				try {
					th.setPkId(getPkId());
				} catch (BusinessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				th.setCreateTime(date);
				th.setCreateUser(userId);
				th.setDelMark(0);
				toolHeadMapper.insert(th);
			}
		});
		return 1;
	}
}