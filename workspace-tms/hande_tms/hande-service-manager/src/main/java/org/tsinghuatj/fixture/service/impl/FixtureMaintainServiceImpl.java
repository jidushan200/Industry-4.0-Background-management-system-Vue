package org.tsinghuatj.fixture.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.tsinghuatj.fixture.domain.Fixture;
import org.tsinghuatj.fixture.domain.FixtureMaintain;
import org.tsinghuatj.fixture.domain.FixtureScripApply;
import org.tsinghuatj.fixture.repository.FixtureMaintainMapper;
import org.tsinghuatj.fixture.repository.FixtureMapper;
import org.tsinghuatj.fixture.service.IFixtureMaintainService;
import org.tsinghuatj.fixture.service.IFixtureScripApplyService;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.service.impl.BaseServiceImpl;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.sys.domain.SysParam;
import org.tsinghuatj.sys.domain.UserAccount;
import org.tsinghuatj.sys.repository.SysParamMapper;
import org.tsinghuatj.sys.repository.UserAccountMapper;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 *
 * FixtureMaintain 表数据服务层接口实现类
 *
 */
@Service("fixtureMaintainService")
public class FixtureMaintainServiceImpl extends BaseServiceImpl implements IFixtureMaintainService {

	private @Resource FixtureMaintainMapper fixtureMaintainMapper;
	private @Resource UserAccountMapper userAccountMapper;
	private @Resource FixtureMapper fixtureMapper;
	private @Autowired(required = false) IFixtureScripApplyService fixtureScripApplyService;
	private @Resource SysParamMapper sysParamMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, String userName,FixtureMaintain fixtureMaintain) throws BusinessException {
		fixtureMaintain.setPkId(getPkId());
		Date date = new Date();
		fixtureMaintain.setCreateTime(date);
		fixtureMaintain.setCreateUser(userId);
		fixtureMaintain.setUpdateTime(date);
		fixtureMaintain.setUpdateUser(userId);
		fixtureMaintain.setDelMark(0);
		if (fixtureMaintain.getMaintainStatus() == 1) {
			Fixture fixture = fixtureMapper.selectById(fixtureMaintain.getFixtureId());
			if(null!=fixture.getMaintainTimes()){
				fixture.setMaintainTimes(fixture.getMaintainTimes() + 1);
			}else{
				fixture.setMaintainTimes(1);
			}
			
			fixture.setLastMaintainTime(date);
			fixtureMapper.updateActiveById(fixture, fixture.getPkId());
			//建议报废,提交一条报废申请
			if(fixtureMaintain.getUseStatus()==3){
				// 封装参数信息
				FixtureScripApply fixtureScripApply = new FixtureScripApply();
				fixtureScripApply.setFixtureId(fixture.getPkId());
				fixtureScripApply.setFixtureNumber(fixture.getFixtureNumber());
				fixtureScripApply.setFixtureMap(fixture.getFixtureName());
				fixtureScripApply.setFixtureBarcode(fixture.getFixtureBarcode());
				fixtureScripApply.setFixtureName(fixture.getFixtureName());
				fixtureScripApply.setApplierId(userId);
				fixtureScripApply.setApplierName(userName);
				fixtureScripApply.setDepartmentId(fixture.getDepartmentId());
				fixtureScripApply.setDepartmentName(fixture.getDepartmentName());
				fixtureScripApply.setApplyTime(new Date());
				fixtureScripApply.setApplyStatus(1);
				fixtureScripApply.setScripResion(1);
				fixtureScripApply.setScripRemark("夹具保养,建议报废");
				//fixtureScripApply.setCheckId(checkId);
				fixtureScripApplyService.insert(userId, fixtureScripApply);
			}else{
				// 设置下次保养时间
				SysParam sysParam = sysParamMapper.selectByParamKey("fixtureMaintainCycle");
				if (null != sysParam) {
					Calendar calendar = new GregorianCalendar();
					calendar.setTime(date);
					calendar.add(GregorianCalendar.DAY_OF_MONTH, Integer.parseInt(sysParam.getParamValue()));// 把日期往后增加一个周期日.整数往后推,负数往前移动
					fixture.setNextMaintainTime(calendar.getTime());
				}
			}
		}
		return fixtureMaintainMapper.insert(fixtureMaintain);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, FixtureMaintain fixtureMaintain, Long fixtureMaintainId) throws BusinessException {
		fixtureMaintain.setUpdateTime(new Date());
		fixtureMaintain.setUpdateUser(userId);
		if (fixtureMaintain.getMaintainStatus() == 1) {
			Fixture fixture = fixtureMapper.selectById(fixtureMaintain.getFixtureId());
			fixture.setMaintainTimes(fixture.getMaintainTimes() + 1);
			fixture.setLastMaintainTime(new Date());
			fixtureMapper.updateActiveById(fixture, fixture.getPkId());
		}
		return fixtureMaintainMapper.updateActiveById(fixtureMaintain, fixtureMaintainId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public FixtureMaintain selectById(Long userId, Long fixtureMaintainId) throws BusinessException {
		return fixtureMaintainMapper.selectById(fixtureMaintainId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long fixtureMaintainId) throws BusinessException {
		return fixtureMaintainMapper.removeById(fixtureMaintainId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long fixtureMaintainId) throws BusinessException {
		FixtureMaintain fixtureMaintain = new FixtureMaintain();
		fixtureMaintain.setPkId(fixtureMaintainId);
		fixtureMaintain.setUpdateTime(new Date());
		fixtureMaintain.setUpdateUser(userId);
		return fixtureMaintainMapper.deleteById(fixtureMaintain);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<FixtureMaintain> select(Long userId, FixtureMaintain fixtureMaintain) throws BusinessException {
		return fixtureMaintainMapper.select(fixtureMaintain);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<FixtureMaintain> selectPageList(Long userId, FixtureMaintain fixtureMaintain, QueryDto queryDto) throws BusinessException {
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<FixtureMaintain> page = fixtureMaintainMapper.selectPageList(fixtureMaintain, queryDto);

		List<Long> idList = new ArrayList<Long>();
		for (FixtureMaintain item : page.getResult()) {
			idList.add(item.getUpdateUser());
		}

		if (idList.size() > 0) {
			idList = idList.stream().distinct().collect(Collectors.toList());
			List<UserAccount> userList = userAccountMapper.selectByIdList(idList);
			Map<Long, UserAccount> accoutMap = userList.stream().collect(Collectors.toMap(UserAccount::getPkId, t -> t, (k1, k2) -> k1));
			for (FixtureMaintain item : page.getResult()) {
				UserAccount account = accoutMap.get(item.getUpdateUser());
				item.setCreateUserName(account.getRealName());
			}
		}
		return new Pagination<FixtureMaintain>(page.getTotal(), page.getResult());
	}
}