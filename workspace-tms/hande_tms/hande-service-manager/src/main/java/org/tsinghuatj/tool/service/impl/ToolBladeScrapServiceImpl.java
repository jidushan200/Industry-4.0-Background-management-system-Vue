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
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.service.impl.BaseServiceImpl;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.framework.utils.JsonUtils;
import org.tsinghuatj.tool.domain.ToolApplyAudit;
import org.tsinghuatj.tool.domain.ToolBlade;
import org.tsinghuatj.tool.domain.ToolBladeCompose;
import org.tsinghuatj.tool.domain.ToolBladeComposeDetail;
import org.tsinghuatj.tool.domain.ToolBladeScrap;
import org.tsinghuatj.tool.domain.ToolBladeScrapDetail;
import org.tsinghuatj.tool.domain.ToolHead;
import org.tsinghuatj.tool.repository.ToolApplyAuditMapper;
import org.tsinghuatj.tool.repository.ToolBladeComposeDetailMapper;
import org.tsinghuatj.tool.repository.ToolBladeComposeMapper;
import org.tsinghuatj.tool.repository.ToolBladeMapper;
import org.tsinghuatj.tool.repository.ToolBladeScrapDetailMapper;
import org.tsinghuatj.tool.repository.ToolBladeScrapMapper;
import org.tsinghuatj.tool.repository.ToolHeadMapper;
import org.tsinghuatj.tool.service.IToolBladeScrapService;
import org.tsinghuatj.tool.service.IToolOperLogService;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 *
 * ToolBladeScrap 表数据服务层接口实现类
 *
 */
@Service("toolBladeScrapService")
public class ToolBladeScrapServiceImpl extends BaseServiceImpl implements IToolBladeScrapService {

	private @Resource ToolBladeScrapMapper toolBladeScrapMapper;
	private @Resource ToolBladeScrapDetailMapper toolBladeScrapDetailMapper;
	private @Resource ToolBladeComposeMapper bladeComposeMapper;
	private @Resource ToolBladeComposeDetailMapper bladeComposeDetailMapper;
	private @Resource ToolApplyAuditMapper toolApplyAuditMapper;
	private @Resource ToolBladeMapper toolBladeMapper;
	private @Resource IToolOperLogService operLogService;
	private @Resource ToolHeadMapper toolHeadMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, ToolBladeScrap toolBladeScrap, String detailJson) throws BusinessException {
		Date date = new Date();
		toolBladeScrap.setPkId(getPkId());
		toolBladeScrap.setCreateTime(date);
		toolBladeScrap.setCreateUser(userId);
		toolBladeScrap.setUpdateTime(date);
		toolBladeScrap.setUpdateUser(userId);
		toolBladeScrap.setDelMark(0);
		List<ToolBladeScrapDetail> list = JsonUtils.json2list(detailJson, ToolBladeScrapDetail.class);
		for (ToolBladeScrapDetail item : list) {
			item.setPkId(getPkId());
			item.setComposeNumber(toolBladeScrap.getComposeNumber());
			item.setCreateTime(date);
			item.setCreateUser(userId);
			item.setUpdateTime(date);
			item.setUpdateUser(userId);
			item.setDelMark(0);
			toolBladeScrapDetailMapper.insert(item);
		}
		return toolBladeScrapMapper.insert(toolBladeScrap);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, ToolBladeScrap toolBladeScrap, Long toolBladeScrapId, String detailJson) throws BusinessException {
		Date date = new Date();
		toolBladeScrap.setUpdateTime(date);
		toolBladeScrap.setUpdateUser(userId);
		List<ToolBladeScrapDetail> list = JsonUtils.json2list(detailJson, ToolBladeScrapDetail.class);
		for (ToolBladeScrapDetail item : list) {
			item.setUpdateTime(date);
			item.setUpdateUser(userId);
			item.setDelMark(0);
			toolBladeScrapDetailMapper.updateActiveById(item, item.getPkId());
		}
		return toolBladeScrapMapper.updateActiveById(toolBladeScrap, toolBladeScrapId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public ToolBladeScrap selectById(Long userId, Long toolBladeScrapId) throws BusinessException {
		ToolBladeScrap scarp = toolBladeScrapMapper.selectById(toolBladeScrapId);
		scarp.setDetailList(toolBladeScrapDetailMapper.selectByComposeNumber(scarp.getComposeNumber()));
		return scarp;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long toolBladeScrapId) throws BusinessException {
		return toolBladeScrapMapper.removeById(toolBladeScrapId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long toolBladeScrapId) throws BusinessException {
		ToolBladeScrap toolBladeScrap = new ToolBladeScrap();
		toolBladeScrap.setPkId(toolBladeScrapId);
		toolBladeScrap.setUpdateTime(new Date());
		toolBladeScrap.setUpdateUser(userId);
		return toolBladeScrapMapper.deleteById(toolBladeScrap);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<ToolBladeScrap> select(Long userId, ToolBladeScrap toolBladeScrap) throws BusinessException {
		return toolBladeScrapMapper.select(toolBladeScrap);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<ToolBladeScrap> selectPageList(Long userId, ToolBladeScrap toolBladeScrap, QueryDto queryDto) throws BusinessException {
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<ToolBladeScrap> page = toolBladeScrapMapper.selectPageList(toolBladeScrap, queryDto);
		List<String> composeNumberList = new ArrayList<String>();
		List<String> headNumberList = new ArrayList<String>();
		for (ToolBladeScrap item : page) {
			String composeNumber = item.getComposeNumber();
			composeNumberList.add(composeNumber);
			String headNumber = composeNumber.substring(0, composeNumber.length() - 6);
			headNumberList.add(headNumber);
			item.setHeadNumber(headNumber);
		}
		if (headNumberList.size() > 0) {
			List<ToolBladeScrapDetail> detailList = toolBladeScrapDetailMapper.selectByComposeList(composeNumberList);
			List<ToolHead> headList = toolHeadMapper.selectByHeadNumberList(headNumberList);
			Map<String, ToolHead> headMap = headList.stream().collect(Collectors.toMap(ToolHead::getHeadNumber, t -> t, (k1, k2) -> k1));
			Map<String, List<ToolBladeScrapDetail>> detailMap = detailList.stream().collect(Collectors.groupingBy(ToolBladeScrapDetail::getComposeNumber));
			page.forEach(item -> {
				item.setDetailList(detailMap.get(item.getComposeNumber()));
				item.setHeadName(headMap.get(item.getHeadNumber()).getHeadName());
			});
		}
		return new Pagination<ToolBladeScrap>(page.getTotal(), page.getResult());
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<ToolBladeCompose> selectScrapPageList(Long userId, ToolBladeCompose toolBladeCompose, QueryDto queryDto) throws BusinessException {
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<ToolBladeCompose> page = bladeComposeMapper.selectScrapPageList(toolBladeCompose, queryDto);
		List<String> numberList = new ArrayList<String>();
		for (ToolBladeCompose item : page) {
			numberList.add(item.getComposeNumber());
		}
		if (numberList.size() > 0) {
			List<ToolBladeScrapDetail> detailList = toolBladeScrapDetailMapper.selectByComposeList(numberList);
			Map<String, List<ToolBladeScrapDetail>> detailMap = detailList.stream().collect(Collectors.groupingBy(ToolBladeScrapDetail::getComposeNumber));
			page.forEach(item -> {
				item.setScrapList(detailMap.get(item.getComposeNumber()));
			});
		}
		return new Pagination<ToolBladeCompose>(page.getTotal(), page.getResult());
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public ToolBladeCompose selectByComposeNumber(Long userId, String composeNumber) throws BusinessException {
		ToolBladeScrap bladeScrap = toolBladeScrapMapper.selectByComposeNumber(composeNumber);
		if (null != bladeScrap) {
			throw new BusinessException("blade.scrap.exists.error");
		}
		ToolBladeCompose toolBladeCompose = bladeComposeMapper.selectByComposeNumber(composeNumber);
		if (null != toolBladeCompose && toolBladeCompose.getToolStatus() < 7) {
			ToolBladeComposeDetail where = new ToolBladeComposeDetail();
			where.setComposeNumber(composeNumber);
			List<ToolBladeComposeDetail> detailList = bladeComposeDetailMapper.select(where);
			for (ToolBladeComposeDetail item : detailList) {
				item.setScrapQty(item.getToolQty() + (null != item.getSupplementQty() ? item.getSupplementQty() : 0));
			}
			toolBladeCompose.setDetailList(detailList);
		}
		return toolBladeCompose;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer audit(Long userId, String realName, Long departMentId, String departMentName, String remark, ToolBladeScrap toolBladeScrap) throws BusinessException {
		Date date = new Date();
		toolBladeScrap.setUpdateTime(date);
		toolBladeScrap.setUpdateUser(userId);
		String auditResult = "";
		toolBladeScrapMapper.updateActiveById(toolBladeScrap, toolBladeScrap.getPkId());
		if (toolBladeScrap.getApplyStatus() == 2) {
			ToolBladeCompose toolBladeCompose = new ToolBladeCompose();
			toolBladeCompose.setComposeNumber(toolBladeScrap.getComposeNumber());
			toolBladeCompose.setToolStatus(7);// 待报废
			bladeComposeMapper.updateActiveByComposeNumber(toolBladeCompose, toolBladeScrap.getComposeNumber());
			auditResult = "工艺部审核通过";
		} else if (toolBladeScrap.getApplyStatus() == -1) {
			auditResult = "工艺部审核,未通过";
		}
		operLogService.insert(userId, 11, "", toolBladeScrap.getComposeNumber(), "审核人:" + realName + " 审核结果:" + auditResult, remark);

		ToolApplyAudit applyAudit = new ToolApplyAudit();
		applyAudit.setPkId(getPkId());
		applyAudit.setCreateTime(date);
		applyAudit.setCreateUser(userId);
		applyAudit.setUpdateTime(date);
		applyAudit.setUpdateUser(userId);
		applyAudit.setDelMark(0);
		applyAudit.setApplyId(toolBladeScrap.getPkId());
		applyAudit.setApplyType(1);
		applyAudit.setAuditResult(auditResult);
		applyAudit.setAuditorId(userId);
		applyAudit.setAuditorName(realName);
		applyAudit.setRemark(remark);
		toolApplyAuditMapper.insert(applyAudit);
		return 1;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer bladeScrap(Long userId, String composeNumber) throws BusinessException {
		Date date = new Date();
		ToolBladeCompose toolBladeCompose = bladeComposeMapper.selectByComposeNumber(composeNumber);
		toolBladeCompose.setToolStatus(8);// 报废
		bladeComposeMapper.updateActiveByComposeNumber(toolBladeCompose, composeNumber);
		List<ToolBladeScrapDetail> detailList = toolBladeScrapDetailMapper.selectByComposeNumber(composeNumber);
		for (ToolBladeScrapDetail detail : detailList) {
			ToolBlade toolBlade = toolBladeMapper.selectByNumber(detail.getToolNumber(), null);
			ToolBladeScrap bladeScrap = toolBladeScrapMapper.selectByComposeNumber(composeNumber);
			bladeScrap.setApplyStatus(3);
			bladeScrap.setUpdateTime(date);
			bladeScrap.setUpdateUser(userId);
			toolBladeScrapMapper.updateActiveById(bladeScrap, bladeScrap.getPkId());
			if (null == toolBlade.getScrapQty()) {
				toolBlade.setScrapQty(detail.getToolQty());
			} else {
				toolBlade.setScrapQty(toolBlade.getScrapQty() + detail.getToolQty());
			}
			toolBlade.setUseQty(toolBlade.getUseQty() - detail.getToolQty());
			toolBlade.setUpdateTime(date);
			toolBlade.setUpdateUser(userId);
			toolBladeMapper.updateActiveById(toolBlade, toolBlade.getPkId());
		}
		return 1;
	}

}