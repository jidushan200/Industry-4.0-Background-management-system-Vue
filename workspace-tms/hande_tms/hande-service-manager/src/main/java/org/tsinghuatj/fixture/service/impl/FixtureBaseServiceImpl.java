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
import org.tsinghuatj.erp.domain.ErpMaterial;
import org.tsinghuatj.erp.repository.ErpMaterialMapper;
import org.tsinghuatj.fixture.domain.FixtureBase;
import org.tsinghuatj.fixture.domain.FixtureBaseChild;
import org.tsinghuatj.fixture.repository.FixtureBaseChildMapper;
import org.tsinghuatj.fixture.repository.FixtureBaseMapper;
import org.tsinghuatj.fixture.service.IFixtureBaseService;
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
import com.github.pagehelper.util.StringUtil;

/**
 *
 * FixtureBase 表数据服务层接口实现类
 *
 */
@Service("fixtureBaseService")
public class FixtureBaseServiceImpl extends BaseServiceImpl implements IFixtureBaseService {
	private @Resource FixtureBaseMapper fixtureBaseMapper;
	private @Resource ErpMaterialMapper erpMaterialMapper;
	private @Resource SysParamMapper paramMapper;
	private @Resource FixtureBaseChildMapper fixtureBaseChildMapper;
	private @Resource UserAccountMapper userAccountMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, FixtureBase fixtureBase) throws BusinessException {
		if (null != fixtureBaseMapper.selectByNumber(fixtureBase.getFixtureNumber(), null)) {
			throw new BusinessException("toolnumber.exists.error");
		}
		fixtureBase.setPkId(getPkId());
		fixtureBase.setCreateTime(new Date());
		fixtureBase.setCreateUser(userId);
		fixtureBase.setUpdateTime(new Date());
		fixtureBase.setUpdateUser(userId);
		fixtureBase.setDelMark(0);

		if (fixtureBase.getFixtureType() == 1 && null != fixtureBase.getChildList()) {
			for (FixtureBaseChild item : fixtureBase.getChildList()) {
				item.setPkId(getPkId());
				item.setCreateTime(new Date());
				item.setCreateUser(userId);
				item.setUpdateTime(new Date());
				item.setUpdateUser(userId);
				item.setDelMark(0);
				item.setParentNumber(fixtureBase.getFixtureNumber());
				fixtureBaseChildMapper.insert(item);
			}
		}
		return fixtureBaseMapper.insert(fixtureBase);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, FixtureBase fixtureBase, Long fixtureBaseId) throws BusinessException {
		if (null != fixtureBaseMapper.selectByNumber(fixtureBase.getFixtureNumber(), fixtureBaseId)) {
			throw new BusinessException("toolnumber.exists.error");
		}
		fixtureBase.setUpdateTime(new Date());
		fixtureBase.setUpdateUser(userId);
		FixtureBaseChild where = new FixtureBaseChild();
		where.setParentNumber(fixtureBase.getFixtureNumber());
		where.setUpdateTime(new Date());
		where.setUpdateUser(userId);
		fixtureBaseChildMapper.deleteByParentNumber(where);
		fixtureBaseMapper.updateActiveById(fixtureBase, fixtureBaseId);
		if (fixtureBase.getFixtureType() == 1 && null != fixtureBase.getChildList()) {
			for (FixtureBaseChild item : fixtureBase.getChildList()) {
				item.setPkId(getPkId());
				item.setCreateTime(new Date());
				item.setCreateUser(userId);
				item.setUpdateTime(new Date());
				item.setUpdateUser(userId);
				item.setDelMark(0);
				item.setParentNumber(fixtureBase.getFixtureNumber());
				fixtureBaseChildMapper.insert(item);
			}
		}
		return 1;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public FixtureBase selectById(Long userId, Long fixtureBaseId, Integer postType) throws BusinessException {
		FixtureBase fixtureBase = fixtureBaseMapper.selectById(fixtureBaseId);
		if (null != fixtureBase) {
			if (1 == postType) {
				ErpMaterial material = erpMaterialMapper.selectByMaterialNumber(fixtureBase.getFixtureNumber());
				if (null != material) {
					fixtureBase.setErpQty(material.getSumOnhandQuantity());
					fixtureBase.setNoCheckQty(material.getSumNocheckQuantity());
				}
			}
			if (fixtureBase.getFixtureType() == 1) {
				fixtureBase.setChildList(fixtureBaseChildMapper.selectByParenNumber(fixtureBase.getFixtureNumber()));
			}
		} else {
			throw new BusinessException("material.not.exists.error");
		}
		return fixtureBase;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long fixtureBaseId) throws BusinessException {
		return fixtureBaseMapper.removeById(fixtureBaseId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long fixtureBaseId, String fixtureNumber) throws BusinessException {
		FixtureBase fixtureBase = new FixtureBase();
		Date date = new Date();
		fixtureBase.setPkId(fixtureBaseId);
		fixtureBase.setUpdateTime(date);
		fixtureBase.setUpdateUser(userId);
		FixtureBaseChild where = new FixtureBaseChild();
		if (fixtureBaseChildMapper.countByFixtureNumber(fixtureNumber) > 0) {
			throw new BusinessException("fixture.is.child.exists.error");
		}
		where.setParentNumber(fixtureBase.getFixtureNumber());
		where.setUpdateTime(date);
		where.setUpdateUser(userId);
		fixtureBaseChildMapper.deleteByParentNumber(where);
		return fixtureBaseMapper.deleteById(fixtureBase);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<FixtureBase> select(Long userId, FixtureBase fixtureBase) throws BusinessException {
		return fixtureBaseMapper.select(fixtureBase);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<FixtureBase> selectPageList(Long userId, FixtureBase fixtureBase, QueryDto queryDto) throws BusinessException {
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<FixtureBase> page = fixtureBaseMapper.selectPageList(fixtureBase, queryDto);
		List<String> parentNumberList = new ArrayList<String>();
		List<Long> idList = new ArrayList<Long>();
		for (FixtureBase item : page) {
			if (item.getFixtureType() == 1) {
				parentNumberList.add(item.getFixtureNumber());
			}
			idList.add(item.getUpdateUser());
			idList.add(item.getCreateUser());
		}
		if (idList.size() > 0) {
			idList = idList.stream().distinct().collect(Collectors.toList());
			List<UserAccount> userList = userAccountMapper.selectByIdList(idList);
			Map<Long, UserAccount> accoutMap = userList.stream().collect(Collectors.toMap(UserAccount::getPkId, t -> t, (k1, k2) -> k1));
			for (FixtureBase item : page.getResult()) {
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
		if (parentNumberList.size() > 0) {
			List<FixtureBaseChild> childList = fixtureBaseChildMapper.selectByParentList(parentNumberList);
			Map<String, List<FixtureBaseChild>> childMap = childList.stream().collect(Collectors.groupingBy(FixtureBaseChild::getParentNumber));
			for (String parentNumber : childMap.keySet()) {
				List<FixtureBaseChild> list = childMap.get(parentNumber);
				for (FixtureBase item : page) {
					if (item.getFixtureNumber().equals(parentNumber)) {
						item.setChildList(list);
						break;
					}
				}
			}
		}
		return new Pagination<FixtureBase>(page.getTotal(), page.getResult());
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public FixtureBase selectByNumber(Long userId, String fixtureNumber) throws BusinessException {
		return fixtureBaseMapper.selectByNumber(fixtureNumber, null);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer fixtureBaseSynchro(Long userId, String fixtureNumber) throws BusinessException {
		ErpMaterial material = erpMaterialMapper.selectByMaterialNumber(fixtureNumber);
		if (null == material) {
			return 0;
		}
		SysParam param = paramMapper.selectByParamKey("prefixFixture");
		if (!material.getItemCode().startsWith(param.getParamValue())) {
			return 0;
		}
		int length = fixtureNumber.length();
		String type = fixtureNumber.substring(length - 2, length - 1);// 编号右截取第二位0代表组合,
		Integer fixtureType = 2;
		if (type.equals("0")) {
			fixtureType = 1;
		}
		FixtureBase fixtureBase = fixtureBaseMapper.selectByNumber(fixtureNumber, null);
		Date date = new Date();
		if (null != fixtureBase) {
			fixtureBase.setFixtureName(material.getItemName());
			fixtureBase.setFixtureType(fixtureType);
			fixtureBase.setPrice(material.getItemPrice());
			fixtureBase.setUpdateTime(date);
			fixtureBase.setUpdateUser(userId);
			fixtureBaseMapper.updateActiveById(fixtureBase, fixtureBase.getPkId());
		} else {
			fixtureBase = new FixtureBase();
			fixtureBase.setPkId(getPkId());
			fixtureBase.setFixtureType(fixtureType);
			fixtureBase.setCreateTime(new Date());
			fixtureBase.setCreateUser(userId);
			fixtureBase.setUpdateTime(new Date());
			fixtureBase.setUpdateUser(userId);
			fixtureBase.setDelMark(0);
			fixtureBase.setFixtureNumber(material.getItemCode());
			fixtureBase.setFixtureName(material.getItemName());
			fixtureBase.setPrice(material.getItemPrice());
			fixtureBaseMapper.insert(fixtureBase);
		}

		return 1;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = false, rollbackFor = Exception.class)
	public Integer fixtureBaseImport(Long userId, List<FixtureBase> fixtureBaseList) throws BusinessException {
		Date date = new Date();
		fixtureBaseList.forEach(fixtureBase -> {
			if (StringUtil.isEmpty(fixtureBase.getFixtureNumber()) || StringUtil.isEmpty(fixtureBase.getFixtureName())) {
				return;
			}
			fixtureBase.setUpdateTime(date);
			fixtureBase.setUpdateUser(userId);
			FixtureBase old = fixtureBaseMapper.selectByNumber(fixtureBase.getFixtureNumber(), null);
			if (null != old) {
				FixtureBaseChild where = new FixtureBaseChild();
				where.setParentNumber(fixtureBase.getFixtureNumber());
				where.setUpdateTime(date);
				where.setUpdateUser(userId);
				fixtureBaseChildMapper.deleteByParentNumber(where);
				fixtureBaseMapper.updateActiveById(fixtureBase, old.getPkId());
			} else {
				try {
					fixtureBase.setPkId(getPkId());
				} catch (BusinessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				fixtureBase.setCreateTime(date);
				fixtureBase.setCreateUser(userId);
				fixtureBase.setDelMark(0);
				fixtureBaseMapper.insert(fixtureBase);
			}
			if (fixtureBase.getFixtureType() == 1 && null != fixtureBase.getChildList()) {
				for (FixtureBaseChild item : fixtureBase.getChildList()) {
					try {
						item.setPkId(getPkId());
					} catch (BusinessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					item.setCreateTime(new Date());
					item.setCreateUser(userId);
					item.setUpdateTime(new Date());
					item.setUpdateUser(userId);
					item.setDelMark(0);
					item.setParentNumber(fixtureBase.getFixtureNumber());
					fixtureBaseChildMapper.insert(item);
				}
			}
		});
		return 1;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = false, rollbackFor = Exception.class)
	public FixtureBase purchaseGetByFixtureNumber(Long userId, String fixtureNumber) throws BusinessException {
		FixtureBase fixtureBase = fixtureBaseMapper.selectByNumber(fixtureNumber, null);
		if (null == fixtureBase) {
			ErpMaterial material = erpMaterialMapper.selectByMaterialNumber(fixtureNumber);
			if (null == material) {
				throw new BusinessException("material.not.exists.error");
			}
			SysParam param = paramMapper.selectByParamKey("prefixFixture");
			if (!material.getItemCode().startsWith(param.getParamValue())) {
				throw new BusinessException("material.not.exists.error");
			}
			fixtureBase = new FixtureBase();
			fixtureBase.setPkId(getPkId());
			fixtureBase.setCreateTime(new Date());
			fixtureBase.setCreateUser(userId);
			fixtureBase.setUpdateTime(new Date());
			fixtureBase.setUpdateUser(userId);
			fixtureBase.setDelMark(0);
			fixtureBase.setFixtureNumber(material.getItemCode());
			fixtureBase.setFixtureName(material.getItemName());
			fixtureBase.setPrice(material.getItemPrice());
			fixtureBaseMapper.insert(fixtureBase);
			fixtureBase.setErpQty(material.getSumOnhandQuantity());
			fixtureBase.setNoCheckQty(material.getSumNocheckQuantity());
			fixtureBase.setInventoryQty(0);
		} else {
			ErpMaterial material = erpMaterialMapper.selectByMaterialNumber(fixtureNumber);
			if (null != material) {
				fixtureBase.setErpQty(material.getSumOnhandQuantity());
				fixtureBase.setNoCheckQty(material.getSumNocheckQuantity());
			}
			if (fixtureBase.getFixtureType() == 1) {
				fixtureBase.setChildList(fixtureBaseChildMapper.selectByParenNumber(fixtureNumber));
			}
		}
		return fixtureBase;

	}
}