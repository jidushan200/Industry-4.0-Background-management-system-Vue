package org.tsinghuatj.tool.service.impl;

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
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.service.impl.BaseServiceImpl;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.sys.domain.UserAccount;
import org.tsinghuatj.sys.repository.UserAccountMapper;
import org.tsinghuatj.tool.domain.ToolBase;
import org.tsinghuatj.tool.domain.ToolCoatPrice;
import org.tsinghuatj.tool.repository.ToolBaseMapper;
import org.tsinghuatj.tool.repository.ToolCoatPriceMapper;
import org.tsinghuatj.tool.service.IToolCoatPriceService;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 *
 * ToolCoatPrice 表数据服务层接口实现类
 *
 */
@Service("toolCoatPriceService")
public class ToolCoatPriceServiceImpl extends BaseServiceImpl implements IToolCoatPriceService {

	private @Resource ToolCoatPriceMapper toolCoatPriceMapper;	
	private @Resource ToolBaseMapper toolbaseMapper;	
	private @Resource SupplierMapper supplierMapper;
	private @Resource UserAccountMapper userAccountMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, ToolCoatPrice toolCoatPrice) throws BusinessException {
		toolCoatPrice.setPkId(getPkId());
		toolCoatPrice.setCreateTime(new Date());
		toolCoatPrice.setCreateUser(userId);
		toolCoatPrice.setUpdateTime(new Date());
		toolCoatPrice.setUpdateUser(userId);
		toolCoatPrice.setDelMark(0);
		return toolCoatPriceMapper.insert(toolCoatPrice);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, ToolCoatPrice toolCoatPrice, Long toolCoatPriceId) throws BusinessException {
		toolCoatPrice.setUpdateTime(new Date());
		toolCoatPrice.setUpdateUser(userId);
		return toolCoatPriceMapper.updateActiveById(toolCoatPrice, toolCoatPriceId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public ToolCoatPrice selectById(Long userId, Long toolCoatPriceId) throws BusinessException {
		return toolCoatPriceMapper.selectById(toolCoatPriceId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long toolCoatPriceId) throws BusinessException {
		return toolCoatPriceMapper.removeById(toolCoatPriceId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long toolCoatPriceId) throws BusinessException {
		ToolCoatPrice toolCoatPrice = new ToolCoatPrice();
		toolCoatPrice.setPkId(toolCoatPriceId);
		toolCoatPrice.setUpdateTime(new Date());
		toolCoatPrice.setUpdateUser(userId);
		return toolCoatPriceMapper.deleteById(toolCoatPrice);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<ToolCoatPrice> select(Long userId, ToolCoatPrice toolCoatPrice) throws BusinessException {
		return toolCoatPriceMapper.select(toolCoatPrice);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<ToolCoatPrice> selectPageList(Long userId, ToolCoatPrice toolCoatPrice, QueryDto queryDto) throws BusinessException {
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<ToolCoatPrice> page = toolCoatPriceMapper.selectPageList(toolCoatPrice, queryDto);
		List<Long> idList = new ArrayList<Long>();
		for (ToolCoatPrice item : page.getResult()) {
			idList.add(item.getUpdateUser());
			idList.add(item.getCreateUser());
		}
		if (idList.size() > 0) {
			idList = idList.stream().distinct().collect(Collectors.toList());
			List<UserAccount> userList = userAccountMapper.selectByIdList(idList);
			Map<Long, UserAccount> accoutMap = userList.stream().collect(Collectors.toMap(UserAccount::getPkId, t -> t, (k1, k2) -> k1));
			for (ToolCoatPrice item : page.getResult()) {
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
		return new Pagination<ToolCoatPrice>(page.getTotal(), page.getResult());
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public boolean coatPriceCheck(Long toolId, Long supplierId, Long pkId) throws BusinessException {
		ToolCoatPrice toolCoatPrice = toolCoatPriceMapper.coatPriceCheck(toolId, supplierId, pkId);
		if (null != toolCoatPrice) {
			throw new BusinessException("coatprice.exists.error");
		}
		return true;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer coatPriceImport(Long userId, List<ToolCoatPrice> tpList) throws BusinessException {
		for (ToolCoatPrice tp : tpList) {
			ToolBase toolbase = toolbaseMapper.selectByToolNumber(tp.getToolNumber(), null);
			if (null == toolbase) {
				continue;
			}
			Supplier supplier = supplierMapper.selectBySupplierCode(tp.getSupplierCode(), null);
			if (null == supplier) {
				continue;
			}
			tp.setSupplierId(supplier.getPkId());
			tp.setToolId(toolbase.getPkId());
			ToolCoatPrice toolCoatPrice = toolCoatPriceMapper.coatPriceCheck(toolbase.getPkId(), supplier.getPkId(),null);
			if(null!=toolCoatPrice){
				toolCoatPriceMapper.updateActiveById(tp, toolCoatPrice.getPkId());
				continue;
			}			
			tp.setPkId(getPkId());
			tp.setCreateTime(new Date());
			tp.setCreateUser(userId);
			tp.setUpdateTime(new Date());
			tp.setUpdateUser(userId);
			tp.setDelMark(0);
			toolCoatPriceMapper.insert(tp);
		}
		return 1;
	}
}