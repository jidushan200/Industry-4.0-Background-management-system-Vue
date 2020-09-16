package org.tsinghuatj.mould.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.service.impl.BaseServiceImpl;
import org.tsinghuatj.framework.support.BusinessException;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.util.StringUtil;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.mould.domain.CheckItem;
import org.tsinghuatj.mould.domain.MouldCheck;
import org.tsinghuatj.mould.domain.MouldCheckItem;
import org.tsinghuatj.mould.repository.MouldCheckItemMapper;
import org.tsinghuatj.mould.repository.MouldCheckMapper;
import org.tsinghuatj.mould.service.IMouldCheckService;
import org.tsinghuatj.tool.domain.ToolAppendix;
import org.tsinghuatj.tool.repository.ToolAppendixMapper;

/**
 *
 * MouldCheck 表数据服务层接口实现类
 *
 */
@Service("mouldCheckService")
public class MouldCheckServiceImpl extends BaseServiceImpl implements IMouldCheckService {

	private @Resource MouldCheckMapper mouldCheckMapper;
	private @Resource ToolAppendixMapper appendixMapper;
	private @Resource MouldCheckItemMapper mouldCheckItemMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, MouldCheck mouldCheck, String appendixIds) throws BusinessException {
		Date date = new Date();
		Long checkId = getPkId();
		mouldCheck.setPkId(checkId);
		mouldCheck.setCreateTime(date);
		mouldCheck.setCreateUser(userId);
		mouldCheck.setUpdateTime(date);
		mouldCheck.setUpdateUser(userId);
		mouldCheck.setDelMark(0);

		mouldCheck.getItemList().forEach(item -> {
			try {
				item.setPkId(getPkId());
				item.setCheckId(checkId);
				item.setCreateTime(date);
				item.setCreateUser(userId);
				item.setUpdateTime(date);
				item.setUpdateUser(userId);
				item.setDelMark(0);
				mouldCheckItemMapper.insert(item);
			} catch (BusinessException e) {
				e.printStackTrace();
			}
		});
		mouldCheckMapper.insert(mouldCheck);
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
			toolAppendix.setCheckId(checkId);
			toolAppendix.setToolId(mouldCheck.getMouldId());
			toolAppendix.setAppdenixType(7);
			appendixMapper.updateActiveById(toolAppendix, appendixId);
		}
		return 1;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, MouldCheck mouldCheck, Long mouldCheckId) throws BusinessException {
		mouldCheck.setUpdateTime(new Date());
		mouldCheck.setUpdateUser(userId);
		return mouldCheckMapper.updateActiveById(mouldCheck, mouldCheckId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public MouldCheck selectById(Long userId, Long mouldCheckId) throws BusinessException {
		List<MouldCheckItem> checkItemList = mouldCheckItemMapper.selectByCheckId(mouldCheckId);
		MouldCheck mouldCheck = mouldCheckMapper.selectById(mouldCheckId);

		List<CheckItem> itemList = new ArrayList<CheckItem>();
		CheckItem item = new CheckItem();

		for (int i = 0; i < 20; i++) {
			item = new CheckItem();
			item.setLitemSeq(i + 1);
			item.setRitemSeq(21 + i);
			if (checkItemList.size() < 21 && checkItemList.size() > i) {
				item.setLcheckItem(checkItemList.get(i).getCheckItem());
				item.setLmeasuredValue(checkItemList.get(i).getCheckResult());
				item.setLcheckResult(checkItemList.get(i).getIsQualified());
			} else if (checkItemList.size() > 20 && checkItemList.size() > i + 20) {
				item.setLcheckItem(checkItemList.get(i).getCheckItem());
				item.setLmeasuredValue(checkItemList.get(i).getCheckResult());
				item.setLcheckResult(checkItemList.get(i).getIsQualified());
				item.setRcheckItem(checkItemList.get(i + 20).getCheckItem());
				item.setRmeasuredValue(checkItemList.get(i + 20).getCheckResult());
				item.setRcheckResult(checkItemList.get(i + 20).getIsQualified());
			}
			itemList.add(item);
		}
		mouldCheck.setCheckItemList(itemList);
		mouldCheck.setAppendixList(appendixMapper.selectByCheckId(mouldCheckId));
		return mouldCheck;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long mouldCheckId) throws BusinessException {
		return mouldCheckMapper.removeById(mouldCheckId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long mouldCheckId) throws BusinessException {
		MouldCheck mouldCheck = new MouldCheck();
		mouldCheck.setPkId(mouldCheckId);
		mouldCheck.setUpdateTime(new Date());
		mouldCheck.setUpdateUser(userId);
		return mouldCheckMapper.deleteById(mouldCheck);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<MouldCheck> select(Long userId, MouldCheck mouldCheck) throws BusinessException {
		return mouldCheckMapper.select(mouldCheck);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<MouldCheck> selectPageList(Long userId, MouldCheck mouldCheck, QueryDto queryDto)
			throws BusinessException {
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<MouldCheck> page = mouldCheckMapper.selectPageList(mouldCheck, queryDto);
		return new Pagination<MouldCheck>(page.getTotal(), page.getResult());
	}
}