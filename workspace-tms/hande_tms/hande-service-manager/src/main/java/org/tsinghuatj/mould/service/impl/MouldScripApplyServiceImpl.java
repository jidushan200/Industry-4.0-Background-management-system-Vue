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
import org.tsinghuatj.mould.domain.MouldScripApply;
import org.tsinghuatj.mould.repository.MouldMapper;
import org.tsinghuatj.mould.repository.MouldScripApplyMapper;
import org.tsinghuatj.mould.service.IMouldScripApplyService;
import org.tsinghuatj.sys.domain.SysMessage;
import org.tsinghuatj.sys.repository.SysMessageMapper;
import org.tsinghuatj.sys.repository.SysUserAuthMapper;
import org.tsinghuatj.tool.domain.ToolApplyAudit;
import org.tsinghuatj.tool.repository.ToolApplyAuditMapper;


/**
 *
 * MouldScripApply 表数据服务层接口实现类
 *
 */
@Service("mouldScripApplyService")
public class MouldScripApplyServiceImpl extends BaseServiceImpl implements IMouldScripApplyService {

	private @Resource MouldScripApplyMapper mouldScripApplyMapper;
	private @Resource MouldMapper mouldMapper;
	private @Resource ToolApplyAuditMapper toolApplyAuditMapper;
	private @Resource SysMessageMapper sysMessageMapper;
	private @Resource SysUserAuthMapper sysUserAuthMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, MouldScripApply mouldScripApply) throws BusinessException {	
		mouldScripApply.setPkId(getPkId());
		mouldScripApply.setCreateTime(new Date());
		mouldScripApply.setCreateUser(userId);
		mouldScripApply.setUpdateTime(new Date());
		mouldScripApply.setUpdateUser(userId);
		mouldScripApply.setDelMark(0);
		return mouldScripApplyMapper.insert(mouldScripApply);
	}
				
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, MouldScripApply mouldScripApply, Long mouldScripApplyId) throws BusinessException {
		mouldScripApply.setUpdateTime(new Date());
		mouldScripApply.setUpdateUser(userId);
		return mouldScripApplyMapper.updateActiveById(mouldScripApply, mouldScripApplyId);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public MouldScripApply selectById(Long userId, Long mouldScripApplyId) throws BusinessException {
		return mouldScripApplyMapper.selectById(mouldScripApplyId);
	}
	

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public MouldScripApply selectByFullNumber(Long userId, String fullNumber) throws BusinessException {
		return mouldScripApplyMapper.selectByFullNumber(fullNumber);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long mouldScripApplyId) throws BusinessException {
		return mouldScripApplyMapper.removeById(mouldScripApplyId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long mouldScripApplyId) throws BusinessException {
		MouldScripApply mouldScripApply = new MouldScripApply();
		mouldScripApply.setPkId(mouldScripApplyId);
		mouldScripApply.setUpdateTime(new Date());
		mouldScripApply.setUpdateUser(userId);
		return mouldScripApplyMapper.deleteById(mouldScripApply);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<MouldScripApply> select(Long userId, MouldScripApply mouldScripApply) throws BusinessException {		
		return mouldScripApplyMapper.select(mouldScripApply);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<MouldScripApply> selectPageList(Long userId, MouldScripApply mouldScripApply,QueryDto queryDto) throws BusinessException {		
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<MouldScripApply> page = mouldScripApplyMapper.selectPageList(mouldScripApply, queryDto);
		return new Pagination<MouldScripApply>(page.getTotal(), page.getResult());		
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer reportAudit(Long userId, String realName, ToolApplyAudit applyAudit,
			MouldScripApply mouldscripReport) throws BusinessException {
		mouldscripReport.setUpdateTime(new Date());
		mouldscripReport.setUpdateUser(userId);
		
		String auditResult = "";
		String message = "";
		String authCode="";

		int auditStatus = mouldscripReport.getApplyStatus();
		String fullNumber = mouldscripReport.getFullNumber();
		if (auditStatus == -1) {
			auditResult = "锻造研究所审核，未通过";
			message = "锻造研究所驳回了模具条码为" + fullNumber + "的报废申请,请您及时处理";
//			authCode = "01040301";
		} else if (auditStatus == 2) {
			auditResult = "锻造研究所审核通过";
			message = "模具条码为" + fullNumber + "的报废申请已通过工艺部审核,请您及时安排报废";
//			authCode = "010401";
			Mould mould = mouldMapper.selectByFullNumber(fullNumber);
			mould.setIsScrip(1);
			mould.setUpdateTime(new Date());
			mould.setUpdateUser(userId);
			mouldMapper.updateActiveById(mould, mould.getPkId());
		} 
		
		// 审核日志
		saveAudit(userId, realName, mouldscripReport.getPkId(), auditResult, applyAudit);
		// 系统消息
		setMessage(message, userId, authCode);
		
		return mouldScripApplyMapper.updateActiveById(mouldscripReport, mouldscripReport.getPkId());
	}
	
	private void saveAudit(Long userId, String realName, Long reportId, String auditResult, ToolApplyAudit applyAudit)
			throws BusinessException {
		ToolApplyAudit applyAuditSeq = toolApplyAuditMapper.selectSeqByApplyId(reportId);
		Integer auditSeq;
		if(applyAuditSeq != null){
			Integer seqMax = applyAuditSeq.getAuditSeq();
			auditSeq = seqMax+1;
		}else{
			auditSeq = 1;
		}
		Date date = new Date();
		applyAudit.setPkId(getPkId());
		applyAudit.setCreateTime(date);
		applyAudit.setCreateUser(userId);
		applyAudit.setUpdateTime(date);
		applyAudit.setUpdateUser(userId);
		applyAudit.setDelMark(0);
		applyAudit.setApplyId(reportId);
		applyAudit.setApplyType(1);
		applyAudit.setAuditResult(auditResult);
		applyAudit.setAuditorId(userId);
		applyAudit.setAuditorName(realName);
		applyAudit.setAuditSeq(auditSeq);
		toolApplyAuditMapper.insert(applyAudit);

	}

	private void setMessage(String message, Long userId, String authCode) throws BusinessException {
		//获取拥有此项权限的用户ID列表
		List<Long> userList = sysUserAuthMapper.selectUserIdByAuthCode(authCode);
		Date date = new Date();
		SysMessage sysMessage = new SysMessage();
		sysMessage.setMessage(message);
		sysMessage.setSenderId(userId);
		sysMessage.setSendTime(date);
		sysMessage.setCreateTime(date);
		sysMessage.setCreateUser(userId);
		sysMessage.setUpdateTime(date);
		sysMessage.setUpdateUser(userId);
		sysMessage.setDelMark(0);
		//遍历拥有权限的人，并设置为该条推送信息的接收人。循环封装并执行添加
		for (Long receiverId : userList) {
			sysMessage.setPkId(getPkId());
			sysMessage.setReceiverId(receiverId);
			sysMessageMapper.insert(sysMessage);
		}
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public boolean fullNumberCheck(String fullNumber, Long pkId) throws BusinessException {
		MouldScripApply mouldscripApply = mouldScripApplyMapper.selectByFullNumber(fullNumber);
		if(mouldscripApply != null){
			throw new BusinessException("mouldFullNumebr.exists.error");
		}
		return true;
	}

}