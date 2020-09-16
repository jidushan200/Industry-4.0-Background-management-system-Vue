package org.tsinghuatj.fixture.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.odftoolkit.odfdom.converter.core.utils.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.tsinghuatj.base.domain.StaffDepartment;
import org.tsinghuatj.base.repository.StaffDepartmentMapper;
import org.tsinghuatj.fixture.domain.Fixture;
import org.tsinghuatj.fixture.domain.FixtureBase;
import org.tsinghuatj.fixture.domain.FixtureCheck;
import org.tsinghuatj.fixture.domain.FixtureChild;
import org.tsinghuatj.fixture.domain.FixturePurchaseReceipt;
import org.tsinghuatj.fixture.domain.FixturePurchaseReceiptDetail;
import org.tsinghuatj.fixture.domain.FixtureScripApply;
import org.tsinghuatj.fixture.domain.FixtureWarehouse;
import org.tsinghuatj.fixture.repository.FixtureBaseMapper;
import org.tsinghuatj.fixture.repository.FixtureCheckMapper;
import org.tsinghuatj.fixture.repository.FixtureChildMapper;
import org.tsinghuatj.fixture.repository.FixtureMapper;
import org.tsinghuatj.fixture.repository.FixturePurchaseReceiptDetailMapper;
import org.tsinghuatj.fixture.repository.FixturePurchaseReceiptMapper;
import org.tsinghuatj.fixture.repository.FixtureScripApplyMapper;
import org.tsinghuatj.fixture.repository.FixtureWarehouseMapper;
import org.tsinghuatj.fixture.service.IFixtureService;
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
 * Fixture 表数据服务层接口实现类
 *
 */
@Service("fixtureService")
public class FixtureServiceImpl extends BaseServiceImpl implements IFixtureService {
	private @Resource FixtureMapper fixtureMapper;
	private @Resource FixtureChildMapper fixtureChildMapper;
	private @Resource FixtureBaseMapper fixtureBaseMapper;
	private @Resource FixturePurchaseReceiptMapper receiptMapper;
	private @Resource FixturePurchaseReceiptDetailMapper detailMapper;
	private @Resource FixtureWarehouseMapper warehouseMapper;
	private @Resource FixtureScripApplyMapper fixtureScripApplyMapper;
	private @Resource SysParamMapper sysParamMapper;
	private @Resource FixtureCheckMapper checkMapper;
	private @Resource StaffDepartmentMapper staffdepartmentMapper;
	private @Resource UserAccountMapper userAccountMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, Long receiptId, Fixture fixture) throws BusinessException {
		FixturePurchaseReceipt purchaseReceipt = receiptMapper.selectById(receiptId);
		fixture.setFixtureNumber(purchaseReceipt.getFixtureNumber());
		fixture.setFixtureName(purchaseReceipt.getFixtureName());
		fixture.setFixtureMap(purchaseReceipt.getFixtureMap());
		fixture.setFixtureBarcode(purchaseReceipt.getFixtureBarcode());
		fixture.setFixtureType(purchaseReceipt.getFixtureType());
		if (null != purchaseReceipt.getSupplierId()) {
			fixture.setSupplierId(purchaseReceipt.getSupplierId());
			fixture.setSupplierName(purchaseReceipt.getSupplierName());
		}
		Date date = new Date();
		// 设置下次保养时间
		SysParam sysParam = sysParamMapper.selectByParamKey("fixtureMaintainCycle");
		if (null != sysParam) {
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(date);
			calendar.add(GregorianCalendar.DAY_OF_MONTH, Integer.parseInt(sysParam.getParamValue()));
			fixture.setNextMaintainTime(calendar.getTime());
		}

		fixture.setPkId(getPkId());
		fixture.setCreateTime(date);
		fixture.setCreateUser(userId);
		fixture.setUpdateTime(date);
		fixture.setUpdateUser(userId);
		fixture.setDelMark(0);
		fixture.setWarehouseTime(date);
		fixture.setFixtureStatus(1);
		fixture.setInUse(2);
		// 夹具配件
		FixtureChild fixtureChild;
		if (purchaseReceipt.getFixtureType() == 1) {
			List<FixturePurchaseReceiptDetail> detailList = detailMapper.selectByReceiptId(receiptId);
			for (FixturePurchaseReceiptDetail detail : detailList) {
				fixtureChild = new FixtureChild();
				fixtureChild.setParentBarcode(fixture.getFixtureBarcode());
				fixtureChild.setFixtureNumber(detail.getFixtureNumber());
				fixtureChild.setFixtureName(detail.getFixtureName());
				fixtureChild.setFixtureMap(detail.getFixtureMap());
				fixtureChild.setFixtureBarcode(detail.getFixtureBarcode());
				insertFixtureChild(userId, fixtureChild, date);
			}
		}
		fixtureMapper.insert(fixture);

		purchaseReceipt.setUpdateUser(userId);
		purchaseReceipt.setUpdateTime(date);
		purchaseReceipt.setHandleResult(1);
		receiptMapper.updateActiveById(purchaseReceipt, receiptId);
		return 1;
	}

	private Integer insertFixtureChild(Long userId, FixtureChild fixtureChild, Date date) throws BusinessException {
		fixtureChild.setPkId(getPkId());
		fixtureChild.setCreateTime(date);
		fixtureChild.setCreateUser(userId);
		fixtureChild.setUpdateTime(date);
		fixtureChild.setUpdateUser(userId);
		fixtureChild.setDelMark(0);
		fixtureChildMapper.insert(fixtureChild);
		return 1;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, Fixture fixture, Long fixtureId) throws BusinessException {
		fixture.setUpdateTime(new Date());
		fixture.setUpdateUser(userId);
		return fixtureMapper.updateActiveById(fixture, fixtureId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Fixture selectById(Long userId, Long fixtureId) throws BusinessException {
		return fixtureMapper.selectById(fixtureId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long fixtureId) throws BusinessException {
		return fixtureMapper.removeById(fixtureId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long fixtureId) throws BusinessException {
		Fixture fixture = new Fixture();
		fixture.setPkId(fixtureId);
		fixture.setUpdateTime(new Date());
		fixture.setUpdateUser(userId);
		fixture.setFixtureStatus(7);
		fixture.setInUse(3);
		fixtureMapper.updateActiveById(fixture, fixtureId);
		FixtureScripApply fixtureScripApply = new FixtureScripApply();
		fixtureScripApply.setFixtureId(fixtureId);
		fixtureScripApply.setApplyStatus(3);
		fixtureScripApply.setUpdateTime(new Date());
		fixtureScripApply.setUpdateUser(userId);
		fixtureScripApplyMapper.updateActiveByFixtureId(fixtureScripApply);
		return 1;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<Fixture> select(Long userId, Fixture fixture) throws BusinessException {
		return fixtureMapper.select(fixture);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<Fixture> selectPageList(Long userId, Fixture fixture, QueryDto queryDto) throws BusinessException {
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<Fixture> page = fixtureMapper.selectPageList(fixture, queryDto);
		List<String> parentBarcodeList = new ArrayList<String>();
		List<Long> idList = new ArrayList<Long>();
		for (Fixture item : page) {
			idList.add(item.getUpdateUser());
			idList.add(item.getCreateUser());
			if (item.getFixtureType() == 1) {
				parentBarcodeList.add(item.getFixtureBarcode());
			}
		}
		if (parentBarcodeList.size() > 0) {
			List<FixtureChild> childList = fixtureChildMapper.selectByParentList(parentBarcodeList);
			Map<String, List<FixtureChild>> childMap = childList.stream().collect(Collectors.groupingBy(FixtureChild::getParentBarcode));
			for (String parentBarcode : childMap.keySet()) {
				List<FixtureChild> list = childMap.get(parentBarcode);
				for (Fixture item : page) {
					if (item.getFixtureBarcode().equals(parentBarcode)) {
						item.setChildList(list);
						break;
					}
				}
			}
		}
		if(idList.size()>0){
			idList = idList.stream().distinct().collect(Collectors.toList());
			List<UserAccount> userList = userAccountMapper.selectByIdList(idList);
			Map<Long, UserAccount> accoutMap = userList.stream().collect(Collectors.toMap(UserAccount::getPkId, t -> t, (k1, k2) -> k1));
			for (Fixture item : page.getResult()) {
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
		return new Pagination<Fixture>(page.getTotal(), page.getResult());
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Fixture selectByFullNumber(Long userId, String fullNumber) throws BusinessException {
		return fixtureMapper.selectByFixtureBarcode(fullNumber);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer importFixture(Long userId, List<Fixture> fixtureList) throws BusinessException {
		Date date = new Date();

		// 设置下次保养时间
		SysParam sysParam = sysParamMapper.selectByParamKey("fixtureMaintainCycle");
		List<StaffDepartment> dList = staffdepartmentMapper.select(new StaffDepartment());
		for (Fixture fixture : fixtureList) {
			if (StringUtil.isEmpty(fixture.getFixtureNumber()) || StringUtil.isEmpty(fixture.getFixtureName())) {
				continue;
			}
			FixtureBase fixtureBase = fixtureBaseMapper.selectByNumber(fixture.getFixtureNumber(), null);
			if (null == fixtureBase) {
				continue;
			}
			
			Fixture oldfixture = fixtureMapper.selectByFixtureBarcode(fixture.getFixtureBarcode());
			if (null != oldfixture) {
				continue;
			}
			Long departmentId = getDepartmentId(fixture.getDepartmentName(), dList);
			fixture.setDepartmentId(departmentId);
			fixture.setPkId(getPkId());
			fixture.setCreateTime(date);
			fixture.setCreateUser(userId);
			fixture.setUpdateTime(date);
			fixture.setUpdateUser(userId);
			fixture.setDelMark(0);
			fixture.setWarehouseTime(date);
			fixture.setFixtureStatus(1);
			fixture.setInUse(2);
			if (null != sysParam) {
				Calendar calendar = new GregorianCalendar();
				calendar.setTime(date);
				calendar.add(GregorianCalendar.DAY_OF_MONTH, Integer.parseInt(sysParam.getParamValue()));
				fixture.setNextMaintainTime(calendar.getTime());
			}
			fixture.setFixtureBarcode(fixture.getFixtureBarcode());
			fixtureBase.setInventoryQty(fixtureBase.getInventoryQty() + 1);
			fixtureBase.setMaxSeq(Integer.parseInt(fixtureBase.getMaxSeq()) + 1 + "");
			fixtureBaseMapper.updateActiveById(fixtureBase, fixtureBase.getPkId());
			fixtureMapper.insert(fixture);
			// 夹具配件
			if (fixture.getFixtureType() == 1 && null != fixture.getChildList()) {

				for (FixtureChild child : fixture.getChildList()) {
					child.setParentBarcode(fixture.getFixtureBarcode());
					child.setFixtureNumber(child.getFixtureNumber());
					child.setFixtureName(child.getFixtureName());
					child.setFixtureMap(child.getFixtureMap());
					child.setFixtureBarcode(child.getFixtureBarcode());
					insertFixtureChild(userId, child, date);
				}
			}
		}
		return 1;
	}

	private Long getDepartmentId(String departmentName, List<StaffDepartment> dList) {
		for (StaffDepartment item : dList) {
			if (departmentName.equals(item.getDepartmentName())) {
				return item.getPkId();
			}
		}
		return null;
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer use(Long userId, Fixture fixture, String remark) throws BusinessException {
		Date date = new Date();
		fixture.setUpdateTime(date);
		fixture.setUpdateUser(userId);
		fixture.setInUse(1);
		fixture.setFixtureStatus(1);
		fixture.setRemark(remark);
		fixtureMapper.updateActiveById(fixture, fixture.getPkId());
		FixtureWarehouse warehouse = new FixtureWarehouse();
		warehouse.setPkId(getPkId());
		warehouse.setCreateTime(date);
		warehouse.setCreateUser(userId);
		warehouse.setUpdateTime(date);
		warehouse.setUpdateUser(userId);
		warehouse.setDelMark(0);
		warehouse.setWarehouseType(1);
		warehouse.setStaffCode(fixture.getStaffCode());
		warehouse.setStaffName(fixture.getStaffName());
		warehouse.setFixtureBarcode(fixture.getFixtureBarcode());
		warehouse.setRemark(remark);
		warehouseMapper.insert(warehouse);
		return 1;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer returnWarehouse(Long userId, Fixture fixture, String remark) throws BusinessException {
		Date date = new Date();
		fixture.setUpdateTime(date);
		fixture.setUpdateUser(userId);
		fixture.setInUse(2);
		fixture.setRemark(remark);
		fixtureMapper.updateReturnWarehouse(fixture, fixture.getPkId());
		FixtureWarehouse warehouse = new FixtureWarehouse();
		warehouse.setPkId(getPkId());
		warehouse.setCreateTime(date);
		warehouse.setCreateUser(userId);
		warehouse.setUpdateTime(date);
		warehouse.setUpdateUser(userId);
		warehouse.setDelMark(0);
		warehouse.setWarehouseType(2);
		warehouse.setRemark(remark);
		warehouse.setFixtureBarcode(fixture.getFixtureBarcode());
		warehouseMapper.insert(warehouse);
		return 1;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer setRepair(Long userId, Fixture fixture, Long checkId) throws BusinessException {
		Date date = new Date();
		fixture.setUpdateTime(date);
		fixture.setUpdateUser(userId);
		fixture.setFixtureStatus(2);
		fixture.setInUse(2);
		fixture.setSetRepairTime(date);
		if (null != checkId) {
			fixtureMapper.updateActiveByFixtureBarcode(fixture);
			FixtureCheck fixtureCheck = new FixtureCheck();
			fixtureCheck.setPkId(checkId);
			fixtureCheck.setHandleResult(1);
			fixtureCheck.setUpdateTime(date);
			fixtureCheck.setUpdateUser(userId);
			checkMapper.updateActiveById(fixtureCheck, checkId);
		} else {
			fixtureMapper.updateActiveById(fixture, fixture.getPkId());
		}
		return 1;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<Fixture> selectComposePageList(Long userId, Fixture fixture, QueryDto queryDto) throws BusinessException {
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<Fixture> page = fixtureMapper.selectPageList(fixture, queryDto);
		return new Pagination<Fixture>(page.getTotal(), page.getResult());
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<FixtureChild> selectPartList(Long userId, Fixture fixture) throws BusinessException {
		List<FixtureChild> list = fixtureChildMapper.selectParentPartList(fixture.getFixtureNumber(), fixture.getDepartmentId());
		if (null == list || list.size() < 1) {
			list = fixtureChildMapper.selectPartList(fixture.getFixtureNumber(), fixture.getFixtureBarcode(), fixture.getDepartmentId());
		}
		return list;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Integer replace(Long userId, String parentBarcode, String fixtureBarcode, String oldParentBarcode, String oldBarcode, String oldNumber) throws BusinessException {
		Date date = new Date();

		// 如果fixtureBarcode为空原本就空缺,如果不为空把原来的配件替换掉,
		if (StringUtils.isNotEmpty(oldBarcode)) {
			// 把原来的配件当做新夹具(父亲)
			FixtureChild fixtureChild = fixtureChildMapper.selectByFixtureBarcode(oldBarcode);
			Fixture fixture = fixtureMapper.selectByFixtureBarcode(fixtureChild.getParentBarcode());
			Fixture newfixture = new Fixture();
			newfixture.setPkId(getPkId());
			newfixture.setCreateTime(date);
			newfixture.setCreateUser(userId);
			newfixture.setUpdateTime(date);
			newfixture.setUpdateUser(userId);
			newfixture.setDelMark(0);
			newfixture.setFixtureBarcode(oldBarcode);
			newfixture.setInUse(2);
			newfixture.setFixtureNumber(fixtureChild.getFixtureNumber());
			newfixture.setFixtureName(fixtureChild.getFixtureName());
			newfixture.setFixtureMap(fixtureChild.getFixtureMap());
			newfixture.setFixtureType(2);
			newfixture.setDepartmentId(fixture.getDepartmentId());
			newfixture.setDepartmentName(fixture.getDepartmentName());
			newfixture.setFixtureStatus(fixture.getFixtureStatus());
			fixtureMapper.insert(newfixture);
		}

		// 新的配件需要从原来的组合上解绑
		if (StringUtils.isNotEmpty(parentBarcode)) {
			FixtureChild fixtureChild = new FixtureChild();
			fixtureChild.setParentBarcode(parentBarcode);
			fixtureChild.setFixtureBarcode(fixtureBarcode);
			fixtureChild.setUpdateTime(date);
			fixtureChild.setUpdateUser(userId);
			fixtureChildMapper.unbindChild(fixtureChild);
		} else {
			// 新配件以前是个体，从t_fixture表删除
			Fixture newfixture = new Fixture();
			newfixture.setFixtureBarcode(fixtureBarcode);
			newfixture.setUpdateTime(date);
			newfixture.setUpdateUser(userId);
			fixtureMapper.deleteById(newfixture);
		}
		// 把新的夹具配件绑定到原来的配件上绑定到原来的t_fixture_child 中
		FixtureChild fixtureChild = new FixtureChild();
		if (StringUtils.isNotEmpty(oldBarcode)) {
			fixtureChild.setFixtureBarcode(oldBarcode);// oldBarCode可能为空
			fixtureChild.setNewFixtureBarcode(fixtureBarcode);
			fixtureChild.setParentBarcode(oldParentBarcode);
			fixtureChild.setUpdateTime(date);
			fixtureChild.setUpdateUser(userId);
			fixtureChildMapper.bindChild(fixtureChild);
		} else {
			fixtureChild.setNewFixtureBarcode(fixtureBarcode);
			fixtureChild.setParentBarcode(oldParentBarcode);
			fixtureChild.setFixtureNumber(oldNumber);
			fixtureChild.setUpdateTime(date);
			fixtureChild.setUpdateUser(userId);
			fixtureChildMapper.bindNullChild(fixtureChild);
		}
		return 1;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer goBack(Long userId, Fixture fixture, Long checkId) throws BusinessException {
		Date date = new Date();
		fixture.setUpdateTime(date);
		fixture.setUpdateUser(userId);
		fixtureMapper.updateActiveByFixtureBarcode(fixture);
		FixtureCheck fixtureCheck = new FixtureCheck();
		fixtureCheck.setPkId(checkId);
		fixtureCheck.setHandleResult(1);
		fixtureCheck.setUpdateTime(date);
		fixtureCheck.setUpdateUser(userId);
		checkMapper.updateActiveById(fixtureCheck, checkId);
		return 1;
	}
}