package org.tsinghuatj.mould.service.impl;

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

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.mould.domain.Mould;
import org.tsinghuatj.mould.domain.MouldReceipt;
import org.tsinghuatj.mould.domain.MouldRepair;
import org.tsinghuatj.mould.domain.MouldRepairProcedure;
import org.tsinghuatj.mould.repository.MouldMapper;
import org.tsinghuatj.mould.repository.MouldReceiptMapper;
import org.tsinghuatj.mould.repository.MouldRepairItemMapper;
import org.tsinghuatj.mould.repository.MouldRepairMapper;
import org.tsinghuatj.mould.repository.MouldRepairProcedureMapper;
import org.tsinghuatj.mould.service.IMouldRepairService;


/**
 *
 * MouldRepair 表数据服务层接口实现类
 *
 */
@Service("mouldRepairService")
public class MouldRepairServiceImpl extends BaseServiceImpl implements IMouldRepairService {

	private @Resource MouldMapper mouldMapper;
	private @Resource MouldRepairMapper mouldRepairMapper;
	private @Resource MouldRepairItemMapper mouldRepairItemMapper;
	private @Resource MouldReceiptMapper mouldReceiptMapper;
	private @Resource MouldRepairProcedureMapper repairProcedureMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, MouldRepair mouldRepair) throws BusinessException {	
		Date date = new Date();
		Long repairId = getPkId();
		mouldRepair.setPkId(repairId);
		mouldRepair.setCreateTime(date);
		mouldRepair.setCreateUser(userId);
		mouldRepair.setUpdateTime(date);
		mouldRepair.setUpdateUser(userId);
		mouldRepair.setDelMark(0);
		
		MouldRepair mouldRepairSeq = mouldRepairMapper.selectSeqByMouldId(mouldRepair.getMouldId());
		//获取本次刃磨的刃磨次序
		Integer seq;
		if(mouldRepairSeq !=null && mouldRepairSeq.getRepairSeq() != null){
			Integer repairSeqMax = mouldRepairSeq.getRepairSeq();
			seq = repairSeqMax + 1;
		}else{
			seq = 1;
		}
		mouldRepair.setRepairSeq(seq);
		
		mouldRepair.getItemList().forEach(item -> {
			try {
				item.setPkId(getPkId());
				item.setRepairId(repairId);
				item.setCreateTime(date);
				item.setCreateUser(userId);
				item.setUpdateTime(date);
				item.setUpdateUser(userId);
				item.setDelMark(0);
				MouldRepairProcedure procedure = repairProcedureMapper.selectById(item.getProcedureId());
				item.setProcedureName(procedure.getProcedureName());
				mouldRepairItemMapper.insert(item);
			} catch (BusinessException e) {
				e.printStackTrace();
			}
		});
		
		//根据toolId查询被刃磨刀具
		Mould repairMould = mouldMapper.selectById(mouldRepair.getMouldId());
		
		//更新刃磨次数
		repairMould.setRepairTimes(seq);
		repairMould.setRepairTime(date);
		repairMould.setGrinderId(mouldRepair.getRepairUserId());
		repairMould.setGrinder(mouldRepair.getRepairUser());
		repairMould.setMouldStatus(4);
		//执行mould表的修磨次数及模具状态的更改
		mouldMapper.updateActiveById(repairMould, repairMould.getPkId());
		
		MouldReceipt mouldReceipt = new MouldReceipt();
		mouldReceipt.setPkId(getPkId());
		mouldReceipt.setCreateTime(new Date());
		mouldReceipt.setCreateUser(userId);
		mouldReceipt.setUpdateTime(new Date());
		mouldReceipt.setUpdateUser(userId);
		mouldReceipt.setDelMark(0);
		mouldReceipt.setMouldNumber(repairMould.getMouldNumber());
		mouldReceipt.setMouldName(repairMould.getMouldName());
		mouldReceipt.setMouldMap(repairMould.getMouldMap());
		mouldReceipt.setMouldSeq(repairMould.getMouldSeq());
		mouldReceipt.setMouldType(repairMould.getMouldType());
		mouldReceipt.setSurfaceNumber(repairMould.getSurfaceNumber());
		mouldReceipt.setHeatNumber(repairMould.getHeatNumber());
		mouldReceipt.setLifeMax(repairMould.getLifeMax());
		mouldReceipt.setMouldStatus(0);
		mouldReceipt.setCheckType(2);
		
		mouldReceiptMapper.insert(mouldReceipt);
		
		return mouldRepairMapper.insert(mouldRepair);
	}
				
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, MouldRepair mouldRepair, Long mouldRepairId) throws BusinessException {
		mouldRepair.setUpdateTime(new Date());
		mouldRepair.setUpdateUser(userId);
		return mouldRepairMapper.updateActiveById(mouldRepair, mouldRepairId);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public MouldRepair selectById(Long userId, Long mouldRepairId) throws BusinessException {
		return mouldRepairMapper.selectById(mouldRepairId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long mouldRepairId) throws BusinessException {
		return mouldRepairMapper.removeById(mouldRepairId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long mouldRepairId) throws BusinessException {
		MouldRepair mouldRepair = new MouldRepair();
		mouldRepair.setPkId(mouldRepairId);
		mouldRepair.setUpdateTime(new Date());
		mouldRepair.setUpdateUser(userId);
		return mouldRepairMapper.deleteById(mouldRepair);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<MouldRepair> select(Long userId, MouldRepair mouldRepair) throws BusinessException {		
		return mouldRepairMapper.select(mouldRepair);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<MouldRepair> selectPageList(Long userId, MouldRepair mouldRepair,QueryDto queryDto) throws BusinessException {		
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<MouldRepair> page = mouldRepairMapper.selectPageList(mouldRepair, queryDto);
		return new Pagination<MouldRepair>(page.getTotal(), page.getResult());		
	}
}