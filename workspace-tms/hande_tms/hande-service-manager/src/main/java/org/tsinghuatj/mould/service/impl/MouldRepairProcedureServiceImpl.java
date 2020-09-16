package org.tsinghuatj.mould.service.impl;

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
import org.tsinghuatj.mould.domain.MouldRepairProcedure;
import org.tsinghuatj.mould.repository.MouldRepairProcedureMapper;
import org.tsinghuatj.mould.service.IMouldRepairProcedureService;
import org.tsinghuatj.sys.domain.UserAccount;
import org.tsinghuatj.sys.repository.UserAccountMapper;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;


/**
 *
 * MouldRepairProcedure 表数据服务层接口实现类
 *
 */
@Service("mouldRepairProcedureService")
public class MouldRepairProcedureServiceImpl extends BaseServiceImpl implements IMouldRepairProcedureService {
	private @Resource UserAccountMapper userAccountMapper;
	private @Resource MouldRepairProcedureMapper mouldRepairProcedureMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, MouldRepairProcedure mouldRepairProcedure) throws BusinessException {	
		mouldRepairProcedure.setPkId(getPkId());
		mouldRepairProcedure.setCreateTime(new Date());
		mouldRepairProcedure.setCreateUser(userId);
		mouldRepairProcedure.setUpdateTime(new Date());
		mouldRepairProcedure.setUpdateUser(userId);
		mouldRepairProcedure.setDelMark(0);
		return mouldRepairProcedureMapper.insert(mouldRepairProcedure);
	}
				
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, MouldRepairProcedure mouldRepairProcedure, Long mouldRepairProcedureId) throws BusinessException {
		mouldRepairProcedure.setUpdateTime(new Date());
		mouldRepairProcedure.setUpdateUser(userId);
		return mouldRepairProcedureMapper.updateActiveById(mouldRepairProcedure, mouldRepairProcedureId);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public MouldRepairProcedure selectById(Long userId, Long mouldRepairProcedureId) throws BusinessException {
		return mouldRepairProcedureMapper.selectById(mouldRepairProcedureId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long mouldRepairProcedureId) throws BusinessException {
		return mouldRepairProcedureMapper.removeById(mouldRepairProcedureId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long mouldRepairProcedureId) throws BusinessException {
		MouldRepairProcedure mouldRepairProcedure = new MouldRepairProcedure();
		mouldRepairProcedure.setPkId(mouldRepairProcedureId);
		mouldRepairProcedure.setUpdateTime(new Date());
		mouldRepairProcedure.setUpdateUser(userId);
		return mouldRepairProcedureMapper.deleteById(mouldRepairProcedure);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<MouldRepairProcedure> select(Long userId, MouldRepairProcedure mouldRepairProcedure) throws BusinessException {		
		return mouldRepairProcedureMapper.select(mouldRepairProcedure);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<MouldRepairProcedure> selectPageList(Long userId, MouldRepairProcedure mouldRepairProcedure,QueryDto queryDto) throws BusinessException {		
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<MouldRepairProcedure> page = mouldRepairProcedureMapper.selectPageList(mouldRepairProcedure, queryDto);
		List<Long> idList = new ArrayList<Long>();
		for (MouldRepairProcedure item : page.getResult()) {
			idList.add(item.getUpdateUser());
			idList.add(item.getCreateUser());
		}
		if (idList.size() > 0) {
			idList = idList.stream().distinct().collect(Collectors.toList());
			List<UserAccount> userList = userAccountMapper.selectByIdList(idList);
			Map<Long, UserAccount> accoutMap = userList.stream().collect(Collectors.toMap(UserAccount::getPkId, t -> t, (k1, k2) -> k1));
			for (MouldRepairProcedure item : page.getResult()) {
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
		return new Pagination<MouldRepairProcedure>(page.getTotal(), page.getResult());		
	}
}