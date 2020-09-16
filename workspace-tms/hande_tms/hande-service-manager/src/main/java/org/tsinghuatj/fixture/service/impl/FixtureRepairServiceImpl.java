package org.tsinghuatj.fixture.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.tsinghuatj.fixture.domain.Fixture;
import org.tsinghuatj.fixture.domain.FixtureRepair;
import org.tsinghuatj.fixture.domain.FixtureWaitCheck;
import org.tsinghuatj.fixture.repository.FixtureMapper;
import org.tsinghuatj.fixture.repository.FixtureRepairMapper;
import org.tsinghuatj.fixture.repository.FixtureWaitCheckMapper;
import org.tsinghuatj.fixture.service.IFixtureRepairService;
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
 * FixtureRepair 表数据服务层接口实现类
 *
 */
@Service("fixtureRepairService")
public class FixtureRepairServiceImpl extends BaseServiceImpl implements IFixtureRepairService {

	private @Resource FixtureRepairMapper fixtureRepairMapper;
	private @Resource FixtureMapper fixtureMapper;
	private @Resource FixtureWaitCheckMapper waitCheckMapper;
	private @Resource UserAccountMapper userAccountMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, FixtureRepair fixtureRepair) throws BusinessException {
		Date date = new Date();
		fixtureRepair.setPkId(getPkId());
		fixtureRepair.setCreateTime(new Date());
		fixtureRepair.setCreateUser(userId);
		fixtureRepair.setUpdateTime(new Date());
		fixtureRepair.setUpdateUser(userId);
		fixtureRepair.setDelMark(0);
		if (fixtureRepair.getRepairStatus() == 1) {
			Fixture fixture = fixtureMapper.selectById(fixtureRepair.getFixtureId());
			if (null != fixture.getRepairTimes()) {
				fixture.setRepairTimes(fixture.getRepairTimes() + 1);
			} else {
				fixture.setRepairTimes(1);
			}
			fixture.setLastRepairTime(new Date());
			fixture.setFixtureStatus(4);
			fixtureMapper.updateActiveById(fixture, fixture.getPkId());
			// 送检
			FixtureWaitCheck waitcheck = new FixtureWaitCheck();
			waitcheck.setPkId(getPkId());
			waitcheck.setCreateTime(date);
			waitcheck.setCreateUser(userId);
			waitcheck.setUpdateTime(date);
			waitcheck.setUpdateUser(userId);
			waitcheck.setDelMark(0);
			waitcheck.setFixtureBarcode(fixture.getFixtureBarcode());
			waitcheck.setFixtureName(fixture.getFixtureName());
			waitcheck.setFixtureNumber(fixture.getFixtureNumber());
			waitcheck.setFixtureMap(fixture.getFixtureMap());
			waitcheck.setDepartmentId(fixture.getDepartmentId());
			waitcheck.setDepartmentName(fixture.getDepartmentName());
			waitcheck.setSetCheckTime(date);
			waitcheck.setCheckType(5);
			waitcheck.setCheckStatus(0);
			waitCheckMapper.insert(waitcheck);
		}
		return fixtureRepairMapper.insert(fixtureRepair);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, FixtureRepair fixtureRepair, Long fixtureRepairId) throws BusinessException {
		fixtureRepair.setUpdateTime(new Date());
		fixtureRepair.setUpdateUser(userId);
		if (fixtureRepair.getRepairStatus() == 1) {
			Fixture fixture = fixtureMapper.selectById(fixtureRepair.getFixtureId());
			fixture.setRepairTimes(fixture.getRepairTimes() + 1);
			fixture.setLastRepairTime(new Date());
			fixtureMapper.updateActiveById(fixture, fixture.getPkId());
		}
		return fixtureRepairMapper.updateActiveById(fixtureRepair, fixtureRepairId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public FixtureRepair selectById(Long userId, Long fixtureRepairId) throws BusinessException {
		return fixtureRepairMapper.selectById(fixtureRepairId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long fixtureRepairId) throws BusinessException {
		return fixtureRepairMapper.removeById(fixtureRepairId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long fixtureRepairId) throws BusinessException {
		FixtureRepair fixtureRepair = new FixtureRepair();
		fixtureRepair.setPkId(fixtureRepairId);
		fixtureRepair.setUpdateTime(new Date());
		fixtureRepair.setUpdateUser(userId);
		return fixtureRepairMapper.deleteById(fixtureRepair);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<FixtureRepair> select(Long userId, FixtureRepair fixtureRepair) throws BusinessException {
		return fixtureRepairMapper.select(fixtureRepair);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<FixtureRepair> selectPageList(Long userId, FixtureRepair fixtureRepair, QueryDto queryDto) throws BusinessException {
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<FixtureRepair> page = fixtureRepairMapper.selectPageList(fixtureRepair, queryDto);
		List<Long> idList = new ArrayList<Long>();
		for (FixtureRepair repair : page.getResult()) {
			idList.add(repair.getCreateUser());
			repair.setCreateTimeStr(DateFormatUtils.format(repair.getCreateTime(), "yyyy-MM-dd HH:mm"));
		}
		if (idList.size() > 0) {
			idList = idList.stream().distinct().collect(Collectors.toList());
			List<UserAccount> userList = userAccountMapper.selectByIdList(idList);
			Map<Long, UserAccount> accoutMap = userList.stream().collect(Collectors.toMap(UserAccount::getPkId, t -> t, (k1, k2) -> k1));
			for (FixtureRepair item : page.getResult()) {
				UserAccount account = accoutMap.get(item.getCreateUser());
				item.setCreateUserName(account.getRealName());
			}
		}

		return new Pagination<FixtureRepair>(page.getTotal(), page.getResult());
	}
}