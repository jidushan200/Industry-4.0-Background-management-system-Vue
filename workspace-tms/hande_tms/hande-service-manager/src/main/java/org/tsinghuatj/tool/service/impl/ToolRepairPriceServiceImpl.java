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
import org.tsinghuatj.tool.domain.ToolRepairPrice;
import org.tsinghuatj.tool.repository.ToolBaseMapper;
import org.tsinghuatj.tool.repository.ToolRepairPriceMapper;
import org.tsinghuatj.tool.service.IToolRepairPriceService;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 *
 * ToolRepairPrice 表数据服务层接口实现类
 *
 */
@Service("toolRepairPriceService")
public class ToolRepairPriceServiceImpl extends BaseServiceImpl implements IToolRepairPriceService {
	private @Resource ToolRepairPriceMapper toolRepairPriceMapper;
	private @Resource ToolBaseMapper toolbaseMapper;
	private @Resource SupplierMapper supplierMapper;
	private @Resource UserAccountMapper userAccountMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, ToolRepairPrice toolRepairPrice) throws BusinessException {
		toolRepairPrice.setPkId(getPkId());
		toolRepairPrice.setCreateTime(new Date());
		toolRepairPrice.setCreateUser(userId);
		toolRepairPrice.setUpdateTime(new Date());
		toolRepairPrice.setUpdateUser(userId);
		toolRepairPrice.setDelMark(0);
		return toolRepairPriceMapper.insert(toolRepairPrice);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, ToolRepairPrice toolRepairPrice, Long toolRepairPriceId) throws BusinessException {
		toolRepairPrice.setUpdateTime(new Date());
		toolRepairPrice.setUpdateUser(userId);
		return toolRepairPriceMapper.updateActiveById(toolRepairPrice, toolRepairPriceId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public ToolRepairPrice selectById(Long userId, Long toolRepairPriceId) throws BusinessException {
		return toolRepairPriceMapper.selectById(toolRepairPriceId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long toolRepairPriceId) throws BusinessException {
		return toolRepairPriceMapper.removeById(toolRepairPriceId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long toolRepairPriceId) throws BusinessException {
		ToolRepairPrice toolRepairPrice = new ToolRepairPrice();
		toolRepairPrice.setPkId(toolRepairPriceId);
		toolRepairPrice.setUpdateTime(new Date());
		toolRepairPrice.setUpdateUser(userId);
		return toolRepairPriceMapper.deleteById(toolRepairPrice);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<ToolRepairPrice> select(Long userId, ToolRepairPrice toolRepairPrice) throws BusinessException {
		return toolRepairPriceMapper.select(toolRepairPrice);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<ToolRepairPrice> selectPageList(Long userId, ToolRepairPrice toolRepairPrice, QueryDto queryDto) throws BusinessException {
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<ToolRepairPrice> page = toolRepairPriceMapper.selectPageList(toolRepairPrice, queryDto);
		List<Long> idList = new ArrayList<Long>();
		for (ToolRepairPrice item : page.getResult()) {
			idList.add(item.getUpdateUser());
			idList.add(item.getCreateUser());
		}
		if (idList.size() > 0) {
			idList = idList.stream().distinct().collect(Collectors.toList());
			List<UserAccount> userList = userAccountMapper.selectByIdList(idList);
			Map<Long, UserAccount> accoutMap = userList.stream().collect(Collectors.toMap(UserAccount::getPkId, t -> t, (k1, k2) -> k1));
			for (ToolRepairPrice item : page.getResult()) {
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
		return new Pagination<ToolRepairPrice>(page.getTotal(), page.getResult());
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public ToolRepairPrice repairPriceCheck(Long toolId, Long supplierId) throws BusinessException {
		return toolRepairPriceMapper.repairPriceCheck(toolId, supplierId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer repairPriceImport(Long userId, List<ToolRepairPrice> tpList) throws BusinessException {
		for (ToolRepairPrice tp : tpList) {
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
			ToolRepairPrice repairPrice = toolRepairPriceMapper.repairPriceCheck(toolbase.getPkId(), supplier.getPkId());
			if (null != repairPrice) {
				toolRepairPriceMapper.updateActiveById(tp, repairPrice.getPkId());
				continue;
			}
			tp.setPkId(getPkId());
			tp.setCreateTime(new Date());
			tp.setCreateUser(userId);
			tp.setUpdateTime(new Date());
			tp.setUpdateUser(userId);
			tp.setDelMark(0);
			toolRepairPriceMapper.insert(tp);
		}
		return 1;
	}
}