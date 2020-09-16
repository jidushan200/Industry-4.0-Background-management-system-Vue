package org.tsinghuatj.base.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.tsinghuatj.base.domain.Supplier;
import org.tsinghuatj.base.repository.SupplierMapper;
import org.tsinghuatj.base.service.ISupplierService;
import org.tsinghuatj.erp.domain.ErpSupplier;
import org.tsinghuatj.erp.repository.ErpSupplierMapper;
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
 * Supplier 表数据服务层接口实现类
 *
 */
@Service("supplierService")
public class SupplierServiceImpl extends BaseServiceImpl implements ISupplierService {

	private @Resource SupplierMapper supplierMapper;
	private @Resource UserAccountMapper userAccountMapper;
	private @Resource ErpSupplierMapper erpsupplierMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, Supplier supplier) throws BusinessException {
		supplier.setPkId(getPkId());
		supplier.setCreateTime(new Date());
		supplier.setCreateUser(userId);
		supplier.setUpdateTime(new Date());
		supplier.setUpdateUser(userId);
		supplier.setDelMark(0);
		return supplierMapper.insert(supplier);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, Supplier supplier, Long supplierId) throws BusinessException {
		supplier.setUpdateTime(new Date());
		supplier.setUpdateUser(userId);
		return supplierMapper.updateActiveById(supplier, supplierId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Supplier selectById(Long userId, Long supplierId) throws BusinessException {
		return supplierMapper.selectById(supplierId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long supplierId) throws BusinessException {
		return supplierMapper.removeById(supplierId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long supplierId) throws BusinessException {
		Supplier supplier = new Supplier();
		supplier.setPkId(supplierId);
		supplier.setUpdateTime(new Date());
		supplier.setUpdateUser(userId);
		return supplierMapper.deleteById(supplier);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<Supplier> select(Long userId, Supplier supplier) throws BusinessException {
		return supplierMapper.select(supplier);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<Supplier> selectPageList(Long userId, Supplier supplier, QueryDto queryDto) throws BusinessException {
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<Supplier> page = supplierMapper.selectPageList(supplier, queryDto);
		List<Long> idList = new ArrayList<Long>();
		for (Supplier item : page.getResult()) {
			idList.add(item.getUpdateUser());
			idList.add(item.getCreateUser());
		}
		if (idList.size() > 0) {
			idList = idList.stream().distinct().collect(Collectors.toList());
			List<UserAccount> userList = userAccountMapper.selectByIdList(idList);
			Map<Long, UserAccount> accoutMap = userList.stream().collect(Collectors.toMap(UserAccount::getPkId, t -> t, (k1, k2) -> k1));
			for (Supplier item : page.getResult()) {
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
		return new Pagination<Supplier>(page.getTotal(), page.getResult());
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer supplierImport(Long userId, List<Supplier> supplierList) throws BusinessException {
		Date date = new Date();
		List<String> supplierCodelist = new ArrayList<String>();
		for (Supplier item : supplierList) {
			supplierCodelist.add(item.getSupplierCode());
		}
		supplierCodelist = supplierCodelist.stream().distinct().collect(Collectors.toList());
		List<Supplier> dList = supplierMapper.selectCodeList(supplierCodelist);
		Map<String, Supplier> supplierMap = dList.stream().collect(Collectors.toMap(Supplier::getSupplierCode, t -> t, (k1, k2) -> k1));
		supplierList.forEach(supplier -> {
			supplier.setIsNewTool("是".equals(supplier.getIsNewSupplier())?1:0);
			supplier.setIsRepair("是".equals(supplier.getIsRepairSupplier())?1:0);
			supplier.setIsCoat("是".equals(supplier.getIsCoatSupplier())?1:0);
			supplier.setIsMeasure("是".equals(supplier.getIsMeasureSupplier())?1:0);
			supplier.setIsFixture("是".equals(supplier.getIsFixtureSupplier())?1:0);
			supplier.setIsMould("是".equals(supplier.getIsMouldSupplier())?1:0);
			Supplier dsSupplier = supplierMap.get(supplier.getSupplierCode());
			
			if (null != dsSupplier) {
				supplier.setSupplierCode(dsSupplier.getSupplierCode());
			}
			supplier.setUpdateTime(date);
			supplier.setUpdateUser(userId);
			Supplier old = supplierMapper.selectBySupplierCode(supplier.getSupplierCode(), null);
			if (null != old) {
				supplierMapper.updateActiveById(supplier, old.getPkId());
			} else {
				supplier.setDelMark(0);
				supplier.setCreateTime(date);
				supplier.setCreateUser(userId);
				try {
					supplier.setPkId(getPkId());
				} catch (BusinessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				supplierMapper.insert(supplier);
			}
		});
		return 1;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer supplierSynchro(Long userId, String supplierCode) throws BusinessException {
		ErpSupplier erpSupplier = erpsupplierMapper.selectBySupplierCode(supplierCode);
		if (null == erpSupplier) {
			return 0;
		}
		Supplier supplier = supplierMapper.selectBySupplierCode(supplierCode, null);
		if (null != supplier) {
			supplier.setUpdateTime(new Date());
			supplier.setUpdateUser(userId);
			supplier.setSupplierName(erpSupplier.getSupplierName());
			supplierMapper.updateActiveById(supplier, supplier.getPkId());
		} else {
			supplier = new Supplier();
			supplier.setPkId(getPkId());
			supplier.setCreateTime(new Date());
			supplier.setCreateUser(userId);
			supplier.setUpdateTime(new Date());
			supplier.setUpdateUser(userId);
			supplier.setDelMark(0);
			supplier.setSupplierCode(supplierCode);
			supplier.setSupplierName(erpSupplier.getSupplierName());
			supplierMapper.insert(supplier);
		}

		return 1;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Supplier selectByCode(Long userId, String supplierCode) throws BusinessException {
		// TODO Auto-generated method stub
		return supplierMapper.selectBySupplierCode(supplierCode, null);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public boolean checkSupplierCode(String supplierCode, Long pkId) throws BusinessException {
		Supplier supplier = supplierMapper.selectBySupplierCode(supplierCode, pkId);
		if (null != supplier) {
			throw new BusinessException("suppliercode.exists.error");
		}
		return true;
	}
}