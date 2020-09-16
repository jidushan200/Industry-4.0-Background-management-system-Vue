package org.tsinghuatj.tool.service.impl;

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
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.service.impl.BaseServiceImpl;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.framework.utils.JsonUtils;
import org.tsinghuatj.sys.domain.UserAccount;
import org.tsinghuatj.sys.repository.UserAccountMapper;
import org.tsinghuatj.tool.domain.Tool;
import org.tsinghuatj.tool.domain.ToolCoat;
import org.tsinghuatj.tool.repository.ToolCoatMapper;
import org.tsinghuatj.tool.repository.ToolMapper;
import org.tsinghuatj.tool.service.IToolCoatService;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 *
 * ToolCoat 表数据服务层接口实现类
 *
 */
@Service("toolCoatService")
public class ToolCoatServiceImpl extends BaseServiceImpl implements IToolCoatService {

	private @Resource ToolCoatMapper toolCoatMapper;
	private @Resource ToolMapper toolMapper;
	private @Resource UserAccountMapper userAccountMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, ToolCoat toolCoat) throws BusinessException {
		toolCoat.setPkId(getPkId());
		toolCoat.setCreateTime(new Date());
		toolCoat.setCreateUser(userId);
		toolCoat.setUpdateTime(new Date());
		toolCoat.setUpdateUser(userId);
		toolCoat.setDelMark(0);
		ToolCoat toolCoatSeq = toolCoatMapper.selectSeqByToolId(toolCoat.getToolId());
		Integer seq;
		if (toolCoatSeq != null) {
			Integer coatSeqMax = toolCoatSeq.getCoatSeq();
			seq = coatSeqMax + 1;
		} else {
			seq = 1;
		}
		toolCoat.setCoatSeq(seq);
		toolCoat.setCoatTime(new Date());
		Integer coatTimes;
		Tool coatTool = toolMapper.selectById(toolCoat.getToolId());
		if (coatTool.getCoatTimes() != null) {
			coatTimes = coatTool.getCoatTimes() + 1;
		} else {
			coatTimes = 1;
		}
		toolCoat.setTypeId(coatTool.getTypeId());
		toolCoatMapper.insert(toolCoat);
		Tool tool = new Tool();
		tool.setCoatTimes(coatTimes);
		tool.setToolState(6);
		toolMapper.updateActiveById(tool, toolCoat.getToolId());

		return 1;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, ToolCoat toolCoat, Long toolCoatId) throws BusinessException {
		toolCoat.setUpdateTime(new Date());
		toolCoat.setUpdateUser(userId);
		return toolCoatMapper.updateActiveById(toolCoat, toolCoatId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public ToolCoat selectById(Long userId, Long toolCoatId) throws BusinessException {
		return toolCoatMapper.selectById(toolCoatId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long toolCoatId) throws BusinessException {
		return toolCoatMapper.removeById(toolCoatId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long toolCoatId) throws BusinessException {
		ToolCoat toolCoat = new ToolCoat();
		toolCoat.setPkId(toolCoatId);
		toolCoat.setUpdateTime(new Date());
		toolCoat.setUpdateUser(userId);
		return toolCoatMapper.deleteById(toolCoat);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<ToolCoat> select(Long userId, ToolCoat toolCoat) throws BusinessException {
		return toolCoatMapper.select(toolCoat);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<ToolCoat> selectPageList(Long userId, ToolCoat toolCoat, QueryDto queryDto) throws BusinessException {
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<ToolCoat> page = toolCoatMapper.selectPageList(toolCoat, queryDto);
		return new Pagination<ToolCoat>(page.getTotal(), page.getResult());
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<ToolCoat> selectStatisticsPageList(Long userId, ToolCoat toolCoat, QueryDto queryDto) throws BusinessException {
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<ToolCoat> page = toolCoatMapper.selectStatisticsPageList(toolCoat, queryDto);
		List<Long> accountIdList = new ArrayList<Long>();// 结算人id
		for (ToolCoat tc : page) {
			if (null != tc.getSettlementId()) {
				accountIdList.add(tc.getSettlementId());
			}
			BigDecimal settlementPrice = tc.getSettlementPrice();
			if (null != settlementPrice) {
				tc.setSettlementAmount(settlementPrice.multiply(new BigDecimal(tc.getSettlementQty())));
				tc.setPayPrice(settlementPrice.multiply(tc.getCompletionDegree()));
				tc.setPayAmount(tc.getPayPrice().multiply(new BigDecimal(tc.getSettlementQty())));
			}
		}
		if (accountIdList.size() > 0) {
			accountIdList = accountIdList.stream().distinct().collect(Collectors.toList());
			List<UserAccount> userList = userAccountMapper.selectByIdList(accountIdList);
			Map<Long, UserAccount> accoutMap = userList.stream().collect(Collectors.toMap(UserAccount::getPkId, t -> t, (k1, k2) -> k1));
			for (ToolCoat tc : page) {
				if (null != tc.getSettlementId()) {
					UserAccount account = accoutMap.get(tc.getSettlementId());
					tc.setSettlementName(account.getRealName());
				}
			}
		}

		return new Pagination<ToolCoat>(page.getTotal(), page.getResult());
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updatecoatSettlement(Long userId, String settlementList) throws BusinessException {
		List<ToolCoat> coatList = JsonUtils.json2list(settlementList, ToolCoat.class);
		Date date = new Date();
		for (ToolCoat toolCoat : coatList) {
			toolCoat.setUpdateTime(date);
			toolCoat.setUpdateUser(userId);
			toolCoat.setSettlementId(userId);
			toolCoat.setSettlementTime(date);
			toolCoat.setSettlementStatus(1);
			toolCoatMapper.updateActiveById(toolCoat, toolCoat.getPkId());
		}
		return 1;
	}
}