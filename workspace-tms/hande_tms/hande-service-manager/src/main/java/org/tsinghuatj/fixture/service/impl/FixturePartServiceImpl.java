package org.tsinghuatj.fixture.service.impl;

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
import org.tsinghuatj.fixture.domain.FixtureBase;
import org.tsinghuatj.fixture.domain.FixturePart;
import org.tsinghuatj.fixture.repository.FixtureBaseMapper;
import org.tsinghuatj.fixture.repository.FixturePartMapper;
import org.tsinghuatj.fixture.service.IFixturePartService;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.service.impl.BaseServiceImpl;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.sys.domain.UserAccount;
import org.tsinghuatj.sys.repository.UserAccountMapper;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 *
 * FixturePart 表数据服务层接口实现类
 *
 */
@Service("fixturePartService")
public class FixturePartServiceImpl extends BaseServiceImpl implements IFixturePartService {

	private @Resource FixturePartMapper fixturePartMapper;
	private @Resource FixtureBaseMapper fixtureBaseMapper;
	private @Resource PartMapper partMapper;
	private @Resource UserAccountMapper userAccountMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, FixturePart fixturePart) throws BusinessException {
		fixturePart.setPkId(getPkId());
		fixturePart.setCreateTime(new Date());
		fixturePart.setCreateUser(userId);
		fixturePart.setUpdateTime(new Date());
		fixturePart.setUpdateUser(userId);
		fixturePart.setDelMark(0);
		return fixturePartMapper.insert(fixturePart);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, FixturePart fixturePart, Long fixturePartId) throws BusinessException {
		fixturePart.setUpdateTime(new Date());
		fixturePart.setUpdateUser(userId);
		return fixturePartMapper.updateActiveById(fixturePart, fixturePartId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public FixturePart selectById(Long userId, Long fixturePartId) throws BusinessException {
		return fixturePartMapper.selectById(fixturePartId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long fixturePartId) throws BusinessException {
		return fixturePartMapper.removeById(fixturePartId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long fixturePartId) throws BusinessException {
		FixturePart fixturePart = new FixturePart();
		fixturePart.setPkId(fixturePartId);
		fixturePart.setUpdateTime(new Date());
		fixturePart.setUpdateUser(userId);
		return fixturePartMapper.deleteById(fixturePart);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<FixturePart> select(Long userId, FixturePart fixturePart) throws BusinessException {
		return fixturePartMapper.select(fixturePart);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<FixturePart> selectPageList(Long userId, FixturePart fixturePart, QueryDto queryDto) throws BusinessException {
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<FixturePart> page = fixturePartMapper.selectPageList(fixturePart, queryDto);
		List<Long> idList = new ArrayList<Long>();
		for (FixturePart item : page.getResult()) {
			idList.add(item.getUpdateUser());
			idList.add(item.getCreateUser());
		}
		if (idList.size() > 0) {
			idList = idList.stream().distinct().collect(Collectors.toList());
			List<UserAccount> userList = userAccountMapper.selectByIdList(idList);
			Map<Long, UserAccount> accoutMap = userList.stream().collect(Collectors.toMap(UserAccount::getPkId, t -> t, (k1, k2) -> k1));
			for (FixturePart item : page.getResult()) {
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
		return new Pagination<FixturePart>(page.getTotal(), page.getResult());
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public boolean checkFixturePart(Long pkId, Long fixtureId, Long partId) throws BusinessException {
		FixturePart fixturePart = fixturePartMapper.checkFixturePart(pkId, fixtureId, partId);
		if (null != fixturePart) {
			throw new BusinessException("fixturePart.exists.error");
		}
		return true;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer importFixture(Long userId, List<FixturePart> fixturePartList) throws BusinessException {
		for (FixturePart fp : fixturePartList) {
			FixtureBase Fixturebase = fixtureBaseMapper.selectByNumber(fp.getFixtureNumber(), null);
			if (null == Fixturebase) {
				continue;
			}
			Part part = partMapper.selectByPartCode(fp.getPartCode(), null);
			if (null == part) {
				continue;
			}
			FixturePart fixturePart = fixturePartMapper.checkFixturePart(null, Fixturebase.getPkId(), part.getPkId());
			if (null != fixturePart) {
				continue;
			}
			fp.setPartId(part.getPkId());
			fp.setFixtureId(Fixturebase.getPkId());
			fp.setPkId(getPkId());
			fp.setCreateTime(new Date());
			fp.setCreateUser(userId);
			fp.setUpdateTime(new Date());
			fp.setUpdateUser(userId);
			fp.setDelMark(0);
			fixturePartMapper.insert(fp);
		}
		return 1;

	}
}