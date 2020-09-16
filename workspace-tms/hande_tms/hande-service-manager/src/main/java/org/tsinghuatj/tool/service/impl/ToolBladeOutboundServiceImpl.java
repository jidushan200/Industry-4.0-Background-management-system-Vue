package org.tsinghuatj.tool.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.service.impl.BaseServiceImpl;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.framework.utils.JsonUtils;
import org.tsinghuatj.tool.domain.ToolBlade;
import org.tsinghuatj.tool.domain.ToolBladeCompose;
import org.tsinghuatj.tool.domain.ToolBladeComposeDetail;
import org.tsinghuatj.tool.domain.ToolBladeOutbound;
import org.tsinghuatj.tool.domain.ToolHeadBlade;
import org.tsinghuatj.tool.domain.ToolPlate;
import org.tsinghuatj.tool.repository.ToolBladeComposeDetailMapper;
import org.tsinghuatj.tool.repository.ToolBladeComposeMapper;
import org.tsinghuatj.tool.repository.ToolBladeComposePartMapper;
import org.tsinghuatj.tool.repository.ToolBladeMapper;
import org.tsinghuatj.tool.repository.ToolBladeOutboundMapper;
import org.tsinghuatj.tool.repository.ToolPlateMapper;
import org.tsinghuatj.tool.service.IToolBladeOutboundService;
import org.tsinghuatj.tool.service.IToolOperLogService;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 *
 * ToolBladeOutbound 表数据服务层接口实现类
 *
 */
@Service("toolBladeOutboundService")
public class ToolBladeOutboundServiceImpl extends BaseServiceImpl implements IToolBladeOutboundService {

	private @Resource ToolBladeOutboundMapper toolBladeOutboundMapper;
	private @Resource ToolBladeMapper toolBladeMapper;
	private @Resource ToolBladeComposeMapper bladeComposeMapper;
	private @Resource ToolBladeComposeDetailMapper composeDetailMapper;
	private @Resource ToolPlateMapper plateMapper;
	private @Resource ToolBladeComposePartMapper composePartMapper;
	private @Resource IToolOperLogService operLogService;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, ToolBladeOutbound toolBladeOutbound, ToolBladeCompose bladeCompose, Long bladeId) throws BusinessException {
		Date date = new Date();
		toolBladeOutbound.setPkId(getPkId());
		toolBladeOutbound.setCreateTime(date);
		toolBladeOutbound.setCreateUser(userId);
		toolBladeOutbound.setUpdateTime(date);
		toolBladeOutbound.setUpdateUser(userId);
		toolBladeOutbound.setDelMark(0);
		toolBladeOutboundMapper.insert(toolBladeOutbound);
		// 领用出库
		if (toolBladeOutbound.getOutType() == 1) {
			ToolBlade toolBlade = toolBladeMapper.selectById(bladeId);
			toolBlade.setInventoryQty(toolBlade.getInventoryQty() - toolBladeOutbound.getToolQty());
			if (null != toolBlade.getUseQty()) {
				toolBlade.setUseQty(toolBlade.getUseQty() + toolBladeOutbound.getToolQty());
			} else {
				toolBlade.setUseQty(toolBladeOutbound.getToolQty());
			}

			toolBlade.setUpdateTime(date);
			toolBlade.setUpdateUser(userId);
			toolBladeMapper.updateActiveById(toolBlade, toolBlade.getPkId());

			bladeCompose.setPkId(getPkId());
			bladeCompose.setCreateTime(date);
			bladeCompose.setCreateUser(userId);
			bladeCompose.setUpdateTime(date);
			bladeCompose.setUpdateUser(userId);
			bladeCompose.setDelMark(0);
			bladeComposeMapper.insert(bladeCompose);

			ToolBladeComposeDetail bladeComposeDetail = new ToolBladeComposeDetail();

			bladeComposeDetail.setComposeNumber(bladeCompose.getComposeNumber());
			bladeComposeDetail.setToolNumber(toolBladeOutbound.getToolNumber());
			bladeComposeDetail.setToolQty(toolBladeOutbound.getToolQty());
			bladeComposeDetail.setPkId(getPkId());
			bladeComposeDetail.setCreateTime(date);
			bladeComposeDetail.setCreateUser(userId);
			bladeComposeDetail.setUpdateTime(date);
			bladeComposeDetail.setUpdateUser(userId);
			bladeComposeDetail.setDelMark(0);

			composeDetailMapper.insert(bladeComposeDetail);

			// 修改刀盘使用状态
			ToolPlate plate = new ToolPlate();
			plate.setPlateNumber(bladeCompose.getPlateNumber());
			plate.setUseStatus(1);
			plate.setUpdateTime(date);
			plate.setUpdateUser(userId);
			plateMapper.updateActiveByNumber(plate, plate.getPlateNumber());
			operLogService.insert(userId, 2, toolBladeOutbound.getToolNumber(), bladeCompose.getComposeNumber(), "领用人:" + toolBladeOutbound.getReceiver() + " 刀条数量:" + toolBladeOutbound.getToolQty(), "");

		} else if (toolBladeOutbound.getOutType() == 4) {
			// 刀条补充出库
			ToolBlade toolBlade = toolBladeMapper.selectByNumber(toolBladeOutbound.getToolNumber(), toolBladeOutbound.getDepartmentId());
			toolBlade.setInventoryQty(toolBlade.getInventoryQty() - toolBladeOutbound.getToolQty());
			toolBlade.setUpdateTime(date);
			toolBlade.setUpdateUser(userId);
			toolBladeMapper.updateActiveById(toolBlade, toolBlade.getPkId());
			ToolBladeComposeDetail bladeComposeDetail = composeDetailMapper.selectByComposeNumberToolNumber(bladeCompose.getComposeNumber(), toolBlade.getToolNumber());
			if (null == bladeComposeDetail.getSupplementQty()) {
				bladeComposeDetail.setSupplementQty(toolBladeOutbound.getToolQty());
				bladeComposeDetail.setSupplementTimes(1);
			} else {
				bladeComposeDetail.setSupplementQty(bladeComposeDetail.getSupplementQty() + toolBladeOutbound.getToolQty());
				bladeComposeDetail.setSupplementTimes(bladeComposeDetail.getSupplementTimes() + 1);
			}
			composeDetailMapper.updateActiveById(bladeComposeDetail, bladeComposeDetail.getPkId());
			operLogService.insert(userId, 2, bladeComposeDetail.getToolNumber(), bladeCompose.getComposeNumber(), "领用人:+" + toolBladeOutbound.getReceiver() + " 刀条补充出库数量:" + toolBladeOutbound.getToolQty(), "");

		}
		return 1;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, ToolBladeOutbound toolBladeOutbound, Long toolBladeOutboundId) throws BusinessException {
		toolBladeOutbound.setUpdateTime(new Date());
		toolBladeOutbound.setUpdateUser(userId);
		return toolBladeOutboundMapper.updateActiveById(toolBladeOutbound, toolBladeOutboundId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public ToolBladeOutbound selectById(Long userId, Long toolBladeOutboundId) throws BusinessException {
		return toolBladeOutboundMapper.selectById(toolBladeOutboundId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long toolBladeOutboundId) throws BusinessException {
		return toolBladeOutboundMapper.removeById(toolBladeOutboundId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long toolBladeOutboundId) throws BusinessException {
		ToolBladeOutbound toolBladeOutbound = new ToolBladeOutbound();
		toolBladeOutbound.setPkId(toolBladeOutboundId);
		toolBladeOutbound.setUpdateTime(new Date());
		toolBladeOutbound.setUpdateUser(userId);
		return toolBladeOutboundMapper.deleteById(toolBladeOutbound);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<ToolBladeOutbound> select(Long userId, ToolBladeOutbound toolBladeOutbound) throws BusinessException {
		return toolBladeOutboundMapper.select(toolBladeOutbound);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<ToolBladeOutbound> selectPageList(Long userId, ToolBladeOutbound toolBladeOutbound, QueryDto queryDto) throws BusinessException {
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<ToolBladeOutbound> page = toolBladeOutboundMapper.selectPageList(toolBladeOutbound, queryDto);
		return new Pagination<ToolBladeOutbound>(page.getTotal(), page.getResult());
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer complexOutbound(Long userId, String headNumber, String composeNumber, String plateNumber, Long departmentId, String departmentName, Long teamId, String teamName, String receiver, String remark, String bladeJson) throws BusinessException {

		List<ToolHeadBlade> list = JsonUtils.json2list(bladeJson, ToolHeadBlade.class);
		Date date = new Date();

		// 构建刀盘刀条组合
		ToolBladeCompose bladeCompose = new ToolBladeCompose();
		bladeCompose.setComposeNumber(composeNumber);

		bladeCompose.setToolStatus(1);
		bladeCompose.setHeadNumber(headNumber);

		bladeCompose.setPlateNumber(plateNumber);
		bladeCompose.setDepartmentId(departmentId);
		bladeCompose.setDepartmentName(departmentName);
		bladeCompose.setTeamId(teamId);
		bladeCompose.setTeamName(teamName);
		bladeCompose.setPkId(getPkId());
		bladeCompose.setCreateTime(date);
		bladeCompose.setCreateUser(userId);
		bladeCompose.setUpdateTime(date);
		bladeCompose.setUpdateUser(userId);
		bladeCompose.setDelMark(0);
		bladeComposeMapper.insert(bladeCompose);

		// 修改刀盘使用状态
		ToolPlate plate = new ToolPlate();
		plate.setPlateNumber(plateNumber);
		plate.setUseStatus(1);
		plate.setUpdateTime(date);
		plate.setUpdateUser(userId);
		plateMapper.updateActiveByNumber(plate, plate.getPlateNumber());

		for (ToolHeadBlade item : list) {
			ToolBladeOutbound toolOutbound = new ToolBladeOutbound();
			toolOutbound.setToolNumber(item.getToolNumber());
			toolOutbound.setToolQty(item.getToolQty());
			toolOutbound.setComposeNumber(composeNumber);
			toolOutbound.setOutType(1);
			toolOutbound.setDepartmentId(departmentId);
			toolOutbound.setDepartmentName(departmentName);
			toolOutbound.setTeamId(teamId);
			toolOutbound.setTeamName(teamName);
			toolOutbound.setReceiver(receiver);
			toolOutbound.setRemark(remark);
			toolOutbound.setPkId(getPkId());
			toolOutbound.setCreateTime(date);
			toolOutbound.setCreateUser(userId);
			toolOutbound.setUpdateTime(date);
			toolOutbound.setUpdateUser(userId);
			toolOutbound.setDelMark(0);
			toolBladeOutboundMapper.insert(toolOutbound);

			ToolBlade toolBlade = toolBladeMapper.selectByNumber(item.getToolNumber(), departmentId);
			toolBlade.setInventoryQty(toolBlade.getInventoryQty() - item.getToolQty());
			if (null != toolBlade.getUseQty()) {
				toolBlade.setUseQty(toolBlade.getUseQty() + item.getToolQty());
			} else {
				toolBlade.setUseQty(item.getToolQty());
			}
			toolBlade.setUpdateTime(date);
			toolBlade.setUpdateUser(userId);
			toolBladeMapper.updateActiveById(toolBlade, toolBlade.getPkId());

			ToolBladeComposeDetail bladeComposeDetail = new ToolBladeComposeDetail();

			bladeComposeDetail.setComposeNumber(bladeCompose.getComposeNumber());
			bladeComposeDetail.setToolNumber(toolOutbound.getToolNumber());
			bladeComposeDetail.setToolQty(toolOutbound.getToolQty());
			bladeComposeDetail.setPkId(getPkId());
			bladeComposeDetail.setCreateTime(date);
			bladeComposeDetail.setCreateUser(userId);
			bladeComposeDetail.setUpdateTime(date);
			bladeComposeDetail.setUpdateUser(userId);
			bladeComposeDetail.setDelMark(0);

			composeDetailMapper.insert(bladeComposeDetail);

			operLogService.insert(userId, 2, toolOutbound.getToolNumber(), bladeCompose.getComposeNumber(), "领用人:" + receiver + " 刀条数量:" + toolOutbound.getToolQty(), "");

		}

		return 1;
	}
}