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
import org.tsinghuatj.tool.domain.Tool;
import org.tsinghuatj.tool.domain.ToolOutbound;
import org.tsinghuatj.tool.domain.ToolWaitCheck;
import org.tsinghuatj.tool.repository.ToolCoatMapper;
import org.tsinghuatj.tool.repository.ToolMapper;
import org.tsinghuatj.tool.repository.ToolOutboundMapper;
import org.tsinghuatj.tool.repository.ToolWaitCheckMapper;
import org.tsinghuatj.tool.service.IToolOperLogService;
import org.tsinghuatj.tool.service.IToolOutboundService;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;


/**
 *
 * ToolOutbound 表数据服务层接口实现类
 *
 */
@Service("toolOutboundService")
public class ToolOutboundServiceImpl extends BaseServiceImpl implements IToolOutboundService {

	private @Resource ToolOutboundMapper toolOutboundMapper;
	private @Resource ToolMapper toolMapper;
	private @Resource ToolCoatMapper toolCoatMapper;
	private @Resource ToolWaitCheckMapper toolWaitCheckMapper;
	private @Resource IToolOperLogService operLogService;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, ToolOutbound toolOutbound,Integer typeId) throws BusinessException {	
		Date date  = new Date();
		toolOutbound.setPkId(getPkId());
		toolOutbound.setCreateTime(date);
		toolOutbound.setCreateUser(userId);
		toolOutbound.setUpdateTime(date);
		toolOutbound.setUpdateUser(userId);
		toolOutbound.setDelMark(0);
		
		Tool tool = new Tool();
		Integer toolState;
		if(toolOutbound.getOutType() == 1){
			toolState = 2;
			tool.setDepartmentId(toolOutbound.getDepartmentId());
			tool.setDepartmentName(toolOutbound.getDepartmentName());
			operLogService.insert(userId, 2, toolOutbound.getToolNumber(), toolOutbound.getFullNumber(),"领用人:"+toolOutbound.getStaffCode()+"-"+toolOutbound.getStaffName(), toolOutbound.getRemark());
		}else if(toolOutbound.getOutType() == 2){
			toolState = 3;
			tool.setGrinderName(toolOutbound.getStaffName());
			tool.setStaffDepartmentName(toolOutbound.getDepartmentName());
			tool.setGrinderTime(toolOutbound.getReceiveTime());	
			operLogService.insert(userId, 4, toolOutbound.getToolNumber(), toolOutbound.getFullNumber(),"领用人:"+toolOutbound.getStaffCode()+"-"+toolOutbound.getStaffName(), toolOutbound.getRemark());
		}else{
			toolState = 5;
			//涂层出库 增加待检记录
			ToolWaitCheck waitcheck = new ToolWaitCheck();
			waitcheck.setPkId(getPkId());
			waitcheck.setCreateTime(date);
			waitcheck.setCreateUser(userId);
			waitcheck.setUpdateTime(date);
			waitcheck.setUpdateUser(userId);
			waitcheck.setDelMark(0);
			waitcheck.setCheckStatus(0);
			waitcheck.setSetCheckTime(date);
			waitcheck.setCheckType(3);
			waitcheck.setSupplierId(toolOutbound.getSupplierId());
			waitcheck.setSupplierName(toolOutbound.getSupplierName());
			waitcheck.setFullNumber(toolOutbound.getFullNumber());
			waitcheck.setToolNumber(toolOutbound.getToolNumber());
			//waitcheck.setToolName(toolOutbound.getToolName());
			//waitcheck.setToolMap(toolOutbound.getToolMap());
			waitcheck.setTypeId(typeId);
			waitcheck.setToolQty(1);
			toolWaitCheckMapper.insert(waitcheck);		
			operLogService.insert(userId, 7, toolOutbound.getToolNumber(), toolOutbound.getFullNumber(),"涂层厂家:"+toolOutbound.getSupplierName(), toolOutbound.getRemark());
			
		}
		tool.setToolState(toolState);	
		toolMapper.updateActiveById(tool, toolOutbound.getToolId());		
		return toolOutboundMapper.insert(toolOutbound);
	}
				
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, ToolOutbound toolOutbound, Long toolOutboundId) throws BusinessException {
		toolOutbound.setUpdateTime(new Date());
		toolOutbound.setUpdateUser(userId);
		return toolOutboundMapper.updateActiveById(toolOutbound, toolOutboundId);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public ToolOutbound selectById(Long userId, Long toolOutboundId) throws BusinessException {
		return toolOutboundMapper.selectById(toolOutboundId);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public ToolOutbound selectRowByToolId(Long userId, Long toolId) throws BusinessException {
		return toolOutboundMapper.selectRowByToolId(toolId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long toolOutboundId) throws BusinessException {
		return toolOutboundMapper.removeById(toolOutboundId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long toolOutboundId) throws BusinessException {
		ToolOutbound toolOutbound = new ToolOutbound();
		toolOutbound.setPkId(toolOutboundId);
		toolOutbound.setUpdateTime(new Date());
		toolOutbound.setUpdateUser(userId);
		return toolOutboundMapper.deleteById(toolOutbound);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<ToolOutbound> select(Long userId, ToolOutbound toolOutbound) throws BusinessException {		
		return toolOutboundMapper.select(toolOutbound);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<ToolOutbound> selectPageList(Long userId, ToolOutbound toolOutbound,QueryDto queryDto) throws BusinessException {		
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<ToolOutbound> page = toolOutboundMapper.selectPageList(toolOutbound, queryDto);
		return new Pagination<ToolOutbound>(page.getTotal(), page.getResult());		
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public ToolOutbound selectByToolIdAndOutType(Long userId, Long toolId, Integer outType) throws BusinessException {
		// TODO Auto-generated method stub
		return toolOutboundMapper.selectByToolIdAndOutType(toolId, outType);
	}
}