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
import org.tsinghuatj.base.domain.Part;
import org.tsinghuatj.base.repository.PartMapper;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.service.impl.BaseServiceImpl;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.mould.domain.MouldBase;
import org.tsinghuatj.mould.domain.MouldPart;
import org.tsinghuatj.mould.repository.MouldBaseMapper;
import org.tsinghuatj.mould.repository.MouldPartMapper;
import org.tsinghuatj.mould.service.IMouldPartService;
import org.tsinghuatj.sys.domain.UserAccount;
import org.tsinghuatj.sys.repository.UserAccountMapper;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;


/**
 *
 * MouldPart 表数据服务层接口实现类
 *
 */
@Service("mouldPartService")
public class MouldPartServiceImpl extends BaseServiceImpl implements IMouldPartService {
	private @Resource UserAccountMapper userAccountMapper;
	private @Resource MouldPartMapper mouldPartMapper;
	private @Resource MouldBaseMapper mouldBaseMapper;
	private @Resource PartMapper partMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, MouldPart mouldPart) throws BusinessException {	
		mouldPart.setPkId(getPkId());
		mouldPart.setCreateTime(new Date());
		mouldPart.setCreateUser(userId);
		mouldPart.setUpdateTime(new Date());
		mouldPart.setUpdateUser(userId);
		mouldPart.setDelMark(0);
		return mouldPartMapper.insert(mouldPart);
	}
				
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, MouldPart mouldPart, Long mouldPartId) throws BusinessException {
		mouldPart.setUpdateTime(new Date());
		mouldPart.setUpdateUser(userId);
		return mouldPartMapper.updateActiveById(mouldPart, mouldPartId);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public MouldPart selectById(Long userId, Long mouldPartId) throws BusinessException {
		return mouldPartMapper.selectById(mouldPartId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long mouldPartId) throws BusinessException {
		return mouldPartMapper.removeById(mouldPartId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long mouldPartId) throws BusinessException {
		MouldPart mouldPart = new MouldPart();
		mouldPart.setPkId(mouldPartId);
		mouldPart.setUpdateTime(new Date());
		mouldPart.setUpdateUser(userId);
		return mouldPartMapper.deleteById(mouldPart);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<MouldPart> select(Long userId, MouldPart mouldPart) throws BusinessException {		
		return mouldPartMapper.select(mouldPart);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<MouldPart> selectPageList(Long userId, MouldPart mouldPart,QueryDto queryDto) throws BusinessException {		
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<MouldPart> page = mouldPartMapper.selectPageList(mouldPart, queryDto);
		List<Long> idList = new ArrayList<Long>();
		for (MouldPart item : page.getResult()) {
			idList.add(item.getUpdateUser());
			idList.add(item.getCreateUser());
		}
		if (idList.size() > 0) {
			idList = idList.stream().distinct().collect(Collectors.toList());
			List<UserAccount> userList = userAccountMapper.selectByIdList(idList);
			Map<Long, UserAccount> accoutMap = userList.stream().collect(Collectors.toMap(UserAccount::getPkId, t -> t, (k1, k2) -> k1));
			for (MouldPart item : page.getResult()) {
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
		return new Pagination<MouldPart>(page.getTotal(), page.getResult());		
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public boolean mouldPartCheck(Long pkId, Long mouldId, Long partId) throws BusinessException {
		MouldPart mouldPart = mouldPartMapper.checkMouldPart(pkId, mouldId, partId);
		if(null!=mouldPart){
			throw new BusinessException("mouldPart.exists.error");
		}
		return true;
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer mouldPartImport(Long userId, List<MouldPart> mouldPartList) throws BusinessException {
		for (MouldPart mp : mouldPartList) {
			MouldBase mouldbase = mouldBaseMapper.selectByMouldNumber(mp.getMouldNumber(), null);
			if (null == mouldbase) {
				continue;
			}
			Part part = partMapper.selectByPartCode(mp.getPartCode(), null);
			if (null == part) {
				continue;
			}
			MouldPart toolPart = mouldPartMapper.checkMouldPart(null, mouldbase.getPkId(), part.getPkId());
			if(null!=toolPart){
				continue;
			}
			mp.setPartId(part.getPkId());
			mp.setMouldId(mouldbase.getPkId());
			mp.setPkId(getPkId());
			mp.setCreateTime(new Date());
			mp.setCreateUser(userId);
			mp.setUpdateTime(new Date());
			mp.setUpdateUser(userId);
			mp.setDelMark(0);
			mouldPartMapper.insert(mp);
		}
		return 1;
	}
}