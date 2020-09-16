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
import org.tsinghuatj.mould.domain.MouldOutbound;
import org.tsinghuatj.mould.domain.MouldProcess;
import org.tsinghuatj.mould.repository.MouldMapper;
import org.tsinghuatj.mould.repository.MouldOutboundMapper;
import org.tsinghuatj.mould.repository.MouldProcessMapper;
import org.tsinghuatj.mould.repository.MouldWarehouseMapper;
import org.tsinghuatj.mould.service.IMouldProcessService;


/**
 *
 * MouldProcess 表数据服务层接口实现类
 *
 */
@Service("mouldProcessService")
public class MouldProcessServiceImpl extends BaseServiceImpl implements IMouldProcessService {

	private @Resource MouldMapper mouldMapper;
	private @Resource MouldProcessMapper mouldProcessMapper;
	private @Resource MouldWarehouseMapper mouldWarehouseMapper;
	private @Resource MouldOutboundMapper mouldOutboundMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, MouldProcess mouldProcess) throws BusinessException {	
		Date date = new Date();
		mouldProcess.setPkId(getPkId());
		mouldProcess.setCreateTime(date);
		mouldProcess.setCreateUser(userId);
		mouldProcess.setUpdateTime(date);
		mouldProcess.setUpdateUser(userId);
		mouldProcess.setDelMark(0);
		
		MouldOutbound mouldOutbound = mouldOutboundMapper.selectRowByByMouldId(mouldProcess.getMouldId());
		if(null!=mouldOutbound && null!=mouldOutbound.getReceiveTime()){
			mouldProcess.setBeginTime(mouldOutbound.getReceiveTime());
		}
		
		//加工次序
		MouldProcess process = mouldProcessMapper.selectSeqByMouldId(mouldProcess.getMouldId());
		Integer seq;
		if(process != null){
			Integer processSeqMax = process.getProcessSeq();
			seq = processSeqMax + 1;	
		}else{
			seq = 1;			
		}
		mouldProcess.setProcessSeq(seq);
		//更改台账的生产总数
		Integer amount;
		amount = mouldProcess.getProcessAmount();
		Integer amountMould=(mouldMapper.selectById(mouldProcess.getMouldId()).getProcessAmount());
		if(amountMould !=null){
			amountMould = amountMould + amount;
		}else{
			amountMould = amount;
		}
		mouldProcessMapper.insert(mouldProcess);
		//修改mould表
		Mould mould = new Mould();
		mould.setProcessTimes(seq);
		mould.setProcessAmount(amountMould);
		mould.setMouldStatus(1);
		mould.setReturnRemark(mouldProcess.getRemark());
		mould.setUpdateTime(date);
		mould.setUpdateUser(userId);
		mould.setReturnResion(mouldProcess.getReturnResion());
		mouldMapper.updateActiveById(mould, mouldProcess.getMouldId());
		
		return 1;
	}
				
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, MouldProcess mouldProcess, Long mouldProcessId) throws BusinessException {
		mouldProcess.setUpdateTime(new Date());
		mouldProcess.setUpdateUser(userId);
		return mouldProcessMapper.updateActiveById(mouldProcess, mouldProcessId);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public MouldProcess selectById(Long userId, Long mouldProcessId) throws BusinessException {
		return mouldProcessMapper.selectById(mouldProcessId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long mouldProcessId) throws BusinessException {
		return mouldProcessMapper.removeById(mouldProcessId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long mouldProcessId) throws BusinessException {
		MouldProcess mouldProcess = new MouldProcess();
		mouldProcess.setPkId(mouldProcessId);
		mouldProcess.setUpdateTime(new Date());
		mouldProcess.setUpdateUser(userId);
		return mouldProcessMapper.deleteById(mouldProcess);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<MouldProcess> select(Long userId, MouldProcess mouldProcess) throws BusinessException {		
		return mouldProcessMapper.select(mouldProcess);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<MouldProcess> selectPageList(Long userId, MouldProcess mouldProcess,QueryDto queryDto) throws BusinessException {		
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<MouldProcess> page = mouldProcessMapper.selectPageList(mouldProcess, queryDto);
		return new Pagination<MouldProcess>(page.getTotal(), page.getResult());		
	}
}