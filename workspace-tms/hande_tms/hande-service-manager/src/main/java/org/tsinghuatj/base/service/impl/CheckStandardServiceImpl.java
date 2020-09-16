package org.tsinghuatj.base.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.tsinghuatj.base.domain.CheckStandard;
import org.tsinghuatj.base.domain.CheckStandardItem;
import org.tsinghuatj.base.domain.Material;
import org.tsinghuatj.base.repository.CheckStandardItemMapper;
import org.tsinghuatj.base.repository.CheckStandardMapper;
import org.tsinghuatj.base.service.ICheckStandardService;
import org.tsinghuatj.fixture.domain.FixtureBase;
import org.tsinghuatj.fixture.repository.FixtureBaseMapper;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.service.impl.BaseServiceImpl;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.sys.domain.UserAccount;
import org.tsinghuatj.sys.repository.UserAccountMapper;
import org.tsinghuatj.tool.domain.ToolAppendix;
import org.tsinghuatj.tool.domain.ToolBase;
import org.tsinghuatj.tool.domain.ToolHead;
import org.tsinghuatj.tool.repository.ToolAppendixMapper;
import org.tsinghuatj.tool.repository.ToolBaseMapper;
import org.tsinghuatj.tool.repository.ToolHeadMapper;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.util.StringUtil;

/**
 *
 * CheckStandard 表数据服务层接口实现类
 *
 */
@Service("checkStandardService")
public class CheckStandardServiceImpl extends BaseServiceImpl implements ICheckStandardService {

	private @Resource CheckStandardMapper checkStandardMapper;
	private @Resource ToolAppendixMapper appendixMapper;
	private @Resource ToolBaseMapper toolBaseMapper;
	private @Resource ToolHeadMapper toolHeadMapper;
	private @Resource FixtureBaseMapper fixtureBaseMapper;
	private @Resource CheckStandardItemMapper itemMapper;
	private @Resource UserAccountMapper userAccountMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, CheckStandard checkStandard, String appendixIds) throws BusinessException {
		Date date = new Date();
		Long standardId = getPkId();
		checkStandard.setPkId(standardId);
		checkStandard.setCreateTime(date);
		checkStandard.setCreateUser(userId);
		checkStandard.setUpdateTime(date);
		checkStandard.setUpdateUser(userId);
		checkStandard.setDelMark(0);
		checkStandardMapper.insert(checkStandard);
		checkStandard.getItemList().forEach(item -> {
			try {
				if (StringUtil.isEmpty(item.getItemStandard())) {
					return;
				}
				item.setPkId(getPkId());
				item.setStandardId(standardId);
				try {
					BigDecimal itemStandard = new BigDecimal(item.getItemStandard());
					if (null != item.getUpDeviation()) {
						item.setMaximum(itemStandard.add(item.getUpDeviation()));
					} else {
						item.setMaximum(itemStandard);
					}
					if (null != item.getDownDeviation()) {
						item.setMinimum(itemStandard.subtract(item.getDownDeviation()));
					} else {
						item.setMinimum(itemStandard);
					}
				} catch (NumberFormatException x) {

				}
				item.setCreateTime(date);
				item.setCreateUser(userId);
				item.setUpdateTime(date);
				item.setUpdateUser(userId);
				item.setDelMark(0);
				itemMapper.insert(item);
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

		// 更新附件
		if (StringUtil.isEmpty(appendixIds)) {
			return 1;
		}
		ToolAppendix toolAppendix;
		String[] idList = appendixIds.split(",");
		Long appendixId;
		for (String id : idList) {
			toolAppendix = new ToolAppendix();
			appendixId = Long.parseLong(id);
			toolAppendix.setPkId(appendixId);
			toolAppendix.setCheckId(standardId);
			toolAppendix.setAppdenixType(5);
			appendixMapper.updateActiveById(toolAppendix, appendixId);
		}
		return 1;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, CheckStandard checkStandard, Long checkStandardId, String appendixIds) throws BusinessException {
		Date date = new Date();
		checkStandard.setUpdateTime(date);
		checkStandard.setUpdateUser(userId);
		checkStandardMapper.updateActiveById(checkStandard, checkStandardId);
		itemMapper.deleteByStandardId(checkStandardId);
		checkStandard.getItemList().forEach(item -> {
			try {
				if (StringUtil.isEmpty(item.getItemStandard())) {
					return;
				}
				item.setPkId(getPkId());
				item.setStandardId(checkStandardId);
				item.setCreateTime(date);
				item.setCreateUser(userId);
				item.setUpdateTime(date);
				item.setUpdateUser(userId);
				try {
					BigDecimal itemStandard = new BigDecimal(item.getItemStandard());
					if (null != item.getUpDeviation()) {
						item.setMaximum(itemStandard.add(item.getUpDeviation()));
					} else {
						item.setMaximum(itemStandard);
					}
					if (null != item.getDownDeviation()) {
						item.setMinimum(itemStandard.subtract(item.getDownDeviation()));
					} else {
						item.setMinimum(itemStandard);
					}
				} catch (NumberFormatException x) {

				}
				item.setDelMark(0);
				itemMapper.insert(item);
			} catch (BusinessException e) {
				e.printStackTrace();
			}
		});
		// 更新附件
		if (StringUtil.isEmpty(appendixIds)) {
			return 1;
		}
		ToolAppendix toolAppendix;
		String[] idList = appendixIds.split(",");
		Long appendixId;
		for (String id : idList) {
			toolAppendix = new ToolAppendix();
			appendixId = Long.parseLong(id);
			toolAppendix.setPkId(appendixId);
			toolAppendix.setCheckId(checkStandardId);
			toolAppendix.setAppdenixType(5);
			appendixMapper.updateActiveById(toolAppendix, appendixId);
		}
		return 1;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public CheckStandard selectById(Long userId, Long checkStandardId) throws BusinessException {
		CheckStandard checkStandard = checkStandardMapper.selectById(checkStandardId);
		checkStandard.setItemList(itemMapper.selectByStandardId(checkStandardId));
		checkStandard.setAppendixList(appendixMapper.selectByCheckId(checkStandardId));
		return checkStandard;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long checkStandardId) throws BusinessException {
		return checkStandardMapper.removeById(checkStandardId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long checkStandardId) throws BusinessException {
		CheckStandard checkStandard = new CheckStandard();
		checkStandard.setPkId(checkStandardId);
		checkStandard.setUpdateTime(new Date());
		checkStandard.setUpdateUser(userId);
		itemMapper.deleteByStandardId(checkStandardId);
		return checkStandardMapper.deleteById(checkStandard);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<CheckStandard> select(Long userId, CheckStandard checkStandard) throws BusinessException {
		List<CheckStandard> csList;
		if (checkStandard.getMaterialType() == 1) {
			csList = checkStandardMapper.selectToolList(checkStandard);
		} else {
			csList = checkStandardMapper.selectFixtureList(checkStandard);
		}
		for (CheckStandard cs : csList) {
			cs.setItemList(itemMapper.selectByStandardId(cs.getPkId()));
		}
		return csList;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<CheckStandard> selectPageList(Long userId, CheckStandard checkStandard, QueryDto queryDto) throws BusinessException {
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<CheckStandard> page;
		if (checkStandard.getMaterialType() == 1) {
			page = checkStandardMapper.selectToolPageList(checkStandard, queryDto);
		} else if (checkStandard.getMaterialType() == 2) {
			page = checkStandardMapper.selectFixturePageList(checkStandard, queryDto);
		} else {
			page = checkStandardMapper.selectHeadPageList(checkStandard, queryDto);
		}
		List<Long> idList = new ArrayList<Long>();
		for (CheckStandard item : page.getResult()) {
			idList.add(item.getUpdateUser());
			idList.add(item.getCreateUser());
		}
		if (idList.size() > 0) {
			idList = idList.stream().distinct().collect(Collectors.toList());
			List<UserAccount> userList = userAccountMapper.selectByIdList(idList);
			Map<Long, UserAccount> accoutMap = userList.stream().collect(Collectors.toMap(UserAccount::getPkId, t -> t, (k1, k2) -> k1));
			for (CheckStandard item : page.getResult()) {
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

		return new Pagination<CheckStandard>(page.getTotal(), page.getResult());
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public CheckStandard selectByMaterialNumber(Long userId, Integer materialType, String materialNumber, Integer checkType) throws BusinessException {
		CheckStandard standard = checkStandardMapper.selectByMaterialNumber(materialType, materialNumber, checkType, null);
		if (null != standard) {
			standard.setItemList(itemMapper.selectByStandardId(standard.getPkId()));
		}
		return standard;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public boolean checkStandardVerification(Integer materialType, String materialNumber, Integer checkType, Long pkId) throws BusinessException {
		CheckStandard standard = checkStandardMapper.selectByMaterialNumber(materialType, materialNumber, checkType, pkId);
		return null == standard ? true : false;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<Material> selectMaterialList(Long userId, Integer materialType) throws BusinessException {
		List<Material> list = new ArrayList<Material>();
		Material material;
		if (materialType == 1) {
			List<ToolBase> tlist = toolBaseMapper.select(new ToolBase());
			for (ToolBase item : tlist) {
				material = new Material();
				material.setPkId(item.getPkId());
				material.setMaterialNumber(item.getToolNumber());
				material.setMaterialName(item.getToolName());
				material.setMaterialMap(item.getToolMap());
				list.add(material);
			}
		} else if (materialType == 2) {
			List<FixtureBase> flist = fixtureBaseMapper.select(new FixtureBase());
			for (FixtureBase item : flist) {
				material = new Material();
				material.setPkId(item.getPkId());
				material.setMaterialNumber(item.getFixtureNumber());
				material.setMaterialName(item.getFixtureName());
				material.setMaterialMap(item.getFixtureMap());
				list.add(material);
			}
		} else {
			List<ToolHead> hlist = toolHeadMapper.select(new ToolHead());
			for (ToolHead item : hlist) {
				material = new Material();
				material.setPkId(item.getPkId());
				material.setMaterialNumber(item.getHeadNumber());
				material.setMaterialName(item.getHeadName());
				list.add(material);
			}
		}
		return list;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer importCheckStandard(Long userId, List<CheckStandard> list) throws BusinessException {
		List<CheckStandard> lst = new ArrayList<CheckStandard>();
		String materialTypeName, checkTypeName;
		String[] materialTypeArray = { "刀具", "夹具", "刀条组合" };
		String[] checkTypeArray = { "新刀质检", "刃磨质检", "涂层质检", "新夹具质检", "夹具修磨质检", "夹具点检" };
		for (CheckStandard item : list) {
			materialTypeName = item.getMaterialTypeName();
			checkTypeName = item.getCheckTypeName();
			if (StringUtil.isEmpty(materialTypeName) || StringUtil.isEmpty(item.getMaterialNumber()) || StringUtil.isEmpty(checkTypeName)) {
				continue;
			}
			Integer materialType = getArrayIndex(materialTypeArray, materialTypeName);
			if (null == materialType) {
				continue;
			}
			Integer checkType = getArrayIndex(checkTypeArray, checkTypeName);
			if (null == checkType) {
				continue;
			}
			if (materialType == 1 && checkType >= 4) {
				continue;
			}
			if (materialType == 2 && checkType < 4) {
				continue;
			}
			if (materialType == 3) {
				if (checkType == 2) {
					checkType = 7;
				} else {
					checkType = 8;
				}
			}
			item.setMaterialType(materialType);
			item.setCheckType(checkType);
			List<CheckStandardItem> itemList = item.getItemList();
			if (null == itemList || itemList.size() < 1) {
				continue;
			}
			CheckStandard checkStandard = checkStandardMapper.selectByMaterialNumber(materialType, item.getMaterialNumber(), checkType, null);
			if (null != checkStandard) {
				checkStandard.setItemList(itemList);
				updateActiveById(userId, checkStandard, checkStandard.getPkId(), "");
			} else {
				item.setItemList(itemList);
				insert(userId, item, "");
			}
		}
		if (lst.size() < 1) {
			return 1;
		}
		return 1;
	}

	private Integer getArrayIndex(String[] array, String value) {
		int i = 1;
		for (String item : array) {
			if (item.equals(value)) {
				return i;
			}
			i++;
		}
		return null;
	}
}