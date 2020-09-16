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
import org.tsinghuatj.framework.constants.MouldTypeEnum;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.service.impl.BaseServiceImpl;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.mould.domain.MouldBase;
import org.tsinghuatj.mould.repository.MouldBaseMapper;
import org.tsinghuatj.mould.service.IMouldBaseService;
import org.tsinghuatj.sys.domain.UserAccount;
import org.tsinghuatj.sys.repository.UserAccountMapper;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 *
 * MouldBase 表数据服务层接口实现类
 *
 */
@Service("mouldBaseService")
public class MouldBaseServiceImpl extends BaseServiceImpl implements IMouldBaseService {

	private @Resource MouldBaseMapper mouldBaseMapper;
	private @Resource UserAccountMapper userAccountMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, MouldBase mouldBase) throws BusinessException {
		mouldBase.setPkId(getPkId());
		mouldBase.setCreateTime(new Date());
		mouldBase.setCreateUser(userId);
		mouldBase.setUpdateTime(new Date());
		mouldBase.setUpdateUser(userId);
		mouldBase.setDelMark(0);
		return mouldBaseMapper.insert(mouldBase);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, MouldBase mouldBase, Long mouldBaseId) throws BusinessException {
		mouldBase.setUpdateTime(new Date());
		mouldBase.setUpdateUser(userId);
		return mouldBaseMapper.updateActiveById(mouldBase, mouldBaseId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public MouldBase selectById(Long userId, Long mouldBaseId) throws BusinessException {
		return mouldBaseMapper.selectById(mouldBaseId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long mouldBaseId) throws BusinessException {
		return mouldBaseMapper.removeById(mouldBaseId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long mouldBaseId) throws BusinessException {
		MouldBase mouldBase = new MouldBase();
		mouldBase.setPkId(mouldBaseId);
		mouldBase.setUpdateTime(new Date());
		mouldBase.setUpdateUser(userId);
		return mouldBaseMapper.deleteById(mouldBase);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<MouldBase> select(Long userId, MouldBase mouldBase) throws BusinessException {
		return mouldBaseMapper.select(mouldBase);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<MouldBase> selectPageList(Long userId, MouldBase mouldBase, QueryDto queryDto) throws BusinessException {
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<MouldBase> page = mouldBaseMapper.selectPageList(mouldBase, queryDto);
		List<Long> idList = new ArrayList<Long>();
		for (MouldBase item : page.getResult()) {
			idList.add(item.getUpdateUser());
			idList.add(item.getCreateUser());
		}
		if (idList.size() > 0) {
			idList = idList.stream().distinct().collect(Collectors.toList());
			List<UserAccount> userList = userAccountMapper.selectByIdList(idList);
			Map<Long, UserAccount> accoutMap = userList.stream().collect(Collectors.toMap(UserAccount::getPkId, t -> t, (k1, k2) -> k1));
			for (MouldBase item : page.getResult()) {
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
		return new Pagination<MouldBase>(page.getTotal(), page.getResult());
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public boolean mouldNumberCheck(String mouldNumber, Long pkId) throws BusinessException {
		MouldBase mouldBase = mouldBaseMapper.selectByMouldNumber(mouldNumber, pkId);
		if (mouldBase != null) {
			throw new BusinessException("mouldNumber.exists.error");
		}
		return true;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public MouldBase selectByNumber(Long userId, String mouldNumber) throws BusinessException {
		return mouldBaseMapper.selectByMouldNumber(mouldNumber, null);
	}

	@Override
	public Integer mouldBaseImport(Long userId, List<MouldBase> mouldBaseList) throws BusinessException {
		Date date = new Date();
		mouldBaseList.forEach(mouldBase -> {
			mouldBase.setUpdateTime(date);
			mouldBase.setUpdateUser(userId);
			mouldBase.setMouldType(MouldTypeEnum.getCode(mouldBase.getMouldTypeName()));
			MouldBase old = mouldBaseMapper.selectByMouldNumber(mouldBase.getMouldNumber(), null);
			if (null != old) {
				mouldBaseMapper.updateActiveById(mouldBase, old.getPkId());
			} else {
				try {
					mouldBase.setPkId(getPkId());
				} catch (BusinessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mouldBase.setCreateTime(date);
				mouldBase.setCreateUser(userId);
				mouldBase.setDelMark(0);
				mouldBaseMapper.insert(mouldBase);
			}
		});
		return 1;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeByMouldNumber(Long userId, String mouldNumber) throws BusinessException {
		// TODO Auto-generated method stub
		return mouldBaseMapper.removeByNumber(mouldNumber);
	}
}